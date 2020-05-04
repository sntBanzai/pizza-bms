package pl.malyszko.jerzy.pizzabms.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.malyszko.jerzy.pizzabms.entity.Wish;

/**
 * @author Jerzy Mayszko
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void makeAnOrder(Wish wish) {
//		Map<String, Map<String, Long>> collect = pizza.getItems()
//		.stream().filter(Objects::nonNull)
//		.collect(Collectors.groupingBy(
//				wi -> wi.getWishType().getName(),
//				Collectors.groupingBy(
//						wi -> wi.getWish().getNick(),
//
	}

}
