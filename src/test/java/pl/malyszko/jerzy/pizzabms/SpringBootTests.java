package pl.malyszko.jerzy.pizzabms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiFunction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.malyszko.jerzy.pizzabms.dto.OrderDTO;
import pl.malyszko.jerzy.pizzabms.dto.WishDTO;
import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
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
	public void shouldCreateAndDistributeOrder() throws IOException, InterruptedException {
		// given
		ObjectMapper objectMapper = new ObjectMapper();

		List<String> jsonLines = Files.readAllLines(
				Paths.get("src", "test", "resources", "wish.json"));
		WishDTO firstDto = transformFunction.apply(jsonLines, objectMapper);

		jsonLines = Files.readAllLines(
				Paths.get("src", "test", "resources", "wish2.json"));

		WishDTO secondDto = transformFunction.apply(jsonLines, objectMapper);

		// when
		Wish firstWish = wishService.makeAWish(firstDto);
		orderService.distributeWish(firstWish);
		Wish secondWish = wishService.makeAWish(secondDto);
		orderService.distributeWish(secondWish);

		// then
		AnOrder currentOrder = orderService.getCurrentOrder();
		assertNotNull(currentOrder);
		OrderDTO orderDTO = OrderDTO.wrap(currentOrder);
		assertNotNull(orderDTO);
		assertEquals(2, orderDTO.getCompleted().size());

	}

}
