package pl.malyszko.jerzy.pizzabms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.malyszko.jerzy.pizzabms.dao.AnOrderRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishRepository;
import pl.malyszko.jerzy.pizzabms.dto.OrderDTO;
import pl.malyszko.jerzy.pizzabms.dto.WishDTO;
import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.service.OrderService;
import pl.malyszko.jerzy.pizzabms.service.WishService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class SpringBootTests {

	@Autowired
	private OrderService orderService;

	@Autowired
	private WishService wishService;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	private WishRepository wishRepo;

	@Autowired
	private AnOrderRepository orderRepo;

	static BiFunction<List<String>, ObjectMapper, WishDTO> transformFunction = (
			fileLines, obMapper) -> {
		try {
			return obMapper.readValue(
					fileLines.stream().reduce("", String::concat),
					WishDTO.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	};

	@Test
	public void shouldContextLoad() {
		assertNotNull(wishService);
		assertNotNull(orderService);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void shouldCreateAndDistributeOrder()
			throws IOException, InterruptedException {
		// given
		assertFalse(TestTransaction.isActive());
		ObjectMapper objectMapper = new ObjectMapper();

		List<String> jsonLines = Files.readAllLines(
				Paths.get("src", "test", "resources", "wish.json"));
		WishDTO firstDto = transformFunction.apply(jsonLines, objectMapper);

		jsonLines = Files.readAllLines(
				Paths.get("src", "test", "resources", "wish2.json"));
		WishDTO secondDto = transformFunction.apply(jsonLines, objectMapper);
		Long orderId = orderService.getCurrentOrder().getId();

		// when
		new TransactionTemplate(platformTransactionManager)
				.execute(txStat -> wishService.makeAWish(firstDto));
		new TransactionTemplate(platformTransactionManager)
				.executeWithoutResult(txStat -> {
					Wish wish = wishRepo.findByNickAndOrder(firstDto.getNick(),
							orderService.getCurrentOrder());
					orderService.distributeWish(wish);
				});
		new TransactionTemplate(platformTransactionManager)
				.execute(txStat -> wishService.makeAWish(secondDto));
		new TransactionTemplate(platformTransactionManager)
				.executeWithoutResult(txStat -> {
					Wish wish = wishRepo.findByNickAndOrder(secondDto.getNick(),
							orderService.getCurrentOrder());
					orderService.distributeWish(wish);
				});

		// then
		OrderDTO orderDTO = new TransactionTemplate(platformTransactionManager)
				.execute(txc -> OrderDTO.wrap(orderRepo.findById(orderId)
						.orElseThrow(AssertionError::new)));
		assertNotNull(orderDTO);
		assertNotEquals(orderId, orderService.getCurrentOrder().getId());
		assertEquals(2, orderDTO.getCompleted().size());
		assertTrue(orderDTO.getNotCompleted().isEmpty());
		assertTrue(orderDTO.getCompleted().stream()
				.allMatch(arg -> Objects.equals(OrderService.PIZZA_SIZE,
						arg.values().stream()
								.flatMap(arg2 -> arg2.values().stream())
								.reduce(0L, Long::sum))));
		assertTrue(Set.of(firstDto.getNick(), secondDto.getNick())
				.containsAll(orderDTO.getCompleted().stream()
						.flatMap(arg -> arg.values().stream())
						.collect(Collectors.flatMapping(
								arg2 -> arg2.keySet().stream(),
								Collectors.toSet()))));
	}

}
