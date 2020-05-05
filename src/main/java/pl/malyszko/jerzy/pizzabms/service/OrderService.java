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

	AnOrder getCurrentOrder();
	
	void distributeWish(Wish wish);
	
	List<OrderDTO> listOrders();
	
	

}
