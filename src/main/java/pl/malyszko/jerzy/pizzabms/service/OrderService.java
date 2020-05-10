package pl.malyszko.jerzy.pizzabms.service;

import java.util.List;

import pl.malyszko.jerzy.pizzabms.dto.OrderDTO;
import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
import pl.malyszko.jerzy.pizzabms.entity.Wish;

/**
 * @author Jerzy Mayszko
 *
 */
public interface OrderService {

	public static final Long PIZZA_SIZE = 8L;
	public static final Long HALF_PIZZA_SIZE = PIZZA_SIZE / 2L;
	
	AnOrder getCurrentOrder();
	
	void distributeWish(Wish wish);
	
	List<OrderDTO> listOrders();
	
	

}
