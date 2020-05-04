package pl.malyszko.jerzy.pizzabms.service;

import pl.malyszko.jerzy.pizzabms.dto.WishDTO;
import pl.malyszko.jerzy.pizzabms.entity.Wish;

/**
 * @author Jerzy Mayszko
 *
 */
public interface WishService {

	Wish makeAWish(WishDTO aWish);
	
}
