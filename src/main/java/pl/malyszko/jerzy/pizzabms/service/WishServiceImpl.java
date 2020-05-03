package pl.malyszko.jerzy.pizzabms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.malyszko.jerzy.pizzabms.dao.WishItemRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishTypeRepository;
import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.entity.WishItem;
import pl.malyszko.jerzy.pizzabms.entity.WishType;

/**
 * @author Jerzy Mayszko
 *
 */
@Service
public class WishServiceImpl implements WishService {

	@Autowired
	private WishRepository wishRepo;

	@Autowired
	private WishItemRepository wishItemRepo;

	@Autowired
	private WishTypeRepository wishTypeRepo;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void makeAWish(Wish aWish) {
		wishRepo.save(aWish);	
		for (WishItem wi : aWish.getWishItems()) {
			String typeName = wi.getWishType().getName();
			WishType wishType = wishTypeRepo.findByName(typeName);
			if (wishType != null) {
				wi.setWishType(wishType);
			} else {
				wishTypeRepo.save(wi.getWishType());
			}
			wishItemRepo.save(wi);
		}
	}

}
