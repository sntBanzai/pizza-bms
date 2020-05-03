package pl.malyszko.jerzy.pizzabms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.malyszko.jerzy.pizzabms.dao.WishItemRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishTypeRepository;
import pl.malyszko.jerzy.pizzabms.entity.Wish;

/**
 * @author Jerzy Mayszko
 *
 */
@Service
public class WishServiceImpl implements WishService{

	@Autowired
	private WishRepository wishRepo;

	@Autowired
	private WishItemRepository wishItemRepo;

	@Autowired
	private WishTypeRepository wishTypeRepo;

	public void makeAWish(Wish aWish) {

	}

}
