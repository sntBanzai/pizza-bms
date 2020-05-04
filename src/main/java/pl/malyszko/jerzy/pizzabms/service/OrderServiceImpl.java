package pl.malyszko.jerzy.pizzabms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.malyszko.jerzy.pizzabms.dao.AnOrderRepository;
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
	public AnOrder getCurrentOrder() {
		Optional<AnOrder> current = orderRepo.findByCompletedFalse();
		return current.orElse(orderRepo.save(new AnOrder()));
	}

}
