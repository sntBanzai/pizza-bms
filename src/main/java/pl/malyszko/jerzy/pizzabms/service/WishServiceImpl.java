package pl.malyszko.jerzy.pizzabms.service;

import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.malyszko.jerzy.pizzabms.dao.WishItemRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishTypeRepository;
import pl.malyszko.jerzy.pizzabms.dto.WishDTO;
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

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Wish makeAWish(WishDTO dto) {
		Wish aWish = new Wish();
		aWish.setNick(dto.getNick());
		wishRepo.save(aWish);
		for (Map.Entry<String, Integer> ent : dto.getPizzaPieces().entrySet()) {
			String typeName = ent.getKey();
			WishType wishType = wishTypeRepo.findByName(typeName);
			if (wishType == null) {
				wishType = new WishType(typeName);
				wishTypeRepo.save(wishType);
			}
			final WishType finalWishType = wishType;
			IntStream.range(0, ent.getValue())
					.mapToObj(i -> new WishItem(aWish, finalWishType))
					.forEach(wishItemRepo::save);
			;
		}
		return aWish;
	}

}
