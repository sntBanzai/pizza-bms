package pl.malyszko.jerzy.pizzabms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.malyszko.jerzy.pizzabms.dao.AnOrderRepository;
import pl.malyszko.jerzy.pizzabms.dto.OrderDTO;
import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
import pl.malyszko.jerzy.pizzabms.entity.Wish;

/**
 * @author Jerzy Mayszko
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private AnOrderRepository orderRepo;

	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void distributeWish(Wish wish) {
//		Map<String, Map<String, Long>> collect = pizza.getItems()
//		.stream().filter(Objects::nonNull)
//		.collect(Collectors.groupingBy(
//				wi -> wi.getWishType().getName(),
//				Collectors.groupingBy(
//						wi -> wi.getWish().getNick(),
//
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AnOrder getCurrentOrder() {
		Optional<AnOrder> current = orderRepo.findByCompletedFalse();
		return current.orElse(orderRepo.save(new AnOrder()));
	}

	@Override
	public List<OrderDTO> listOrders() {
		List<OrderDTO> retVal = new ArrayList<>();
		Iterable<AnOrder> findAll = orderRepo.findAll();
		findAll.forEach(ord -> retVal.add(OrderDTO.wrap(ord)));
		return retVal;
	}

}
