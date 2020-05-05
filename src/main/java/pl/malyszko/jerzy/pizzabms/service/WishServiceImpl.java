package pl.malyszko.jerzy.pizzabms.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.malyszko.jerzy.pizzabms.dao.PizzaRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishItemRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishRepository;
import pl.malyszko.jerzy.pizzabms.dao.WishTypeRepository;
import pl.malyszko.jerzy.pizzabms.dto.WishDTO;
import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
import pl.malyszko.jerzy.pizzabms.entity.Pizza;
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

	@Autowired
	private PizzaRepository completionRepo;

	@Autowired
	private OrderService orderService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Wish makeAWish(WishDTO dto) {
		Wish aWish = new Wish();
		aWish.setNick(dto.getNick());
		AnOrder currentOrder = orderService.getCurrentOrder();
		aWish.setOrder(currentOrder);
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public WishDTO deleteExistingWish(String nick) {
		AnOrder order = orderService.getCurrentOrder();
		Wish found = wishRepo.findByNickAndOrder(nick, order);
		if (found == null)
			return null;
		WishDTO wishDto = WishDTO.wrap(found);
		if (Objects.isNull(found))
			return null;
		List<Pizza> pizzasToUpdate = found.getWishItems().stream()
				.map(WishItem::removeFromCompletion).filter(Objects::nonNull)
				.collect(Collectors.toList());
		for (Pizza pizza : pizzasToUpdate) {
			if (pizza.hasAnyItem()) {
				completionRepo.save(pizza);
			} else {
				completionRepo.delete(pizza);
			}
		}
		found.getWishItems().forEach(wishItemRepo::delete);
		wishRepo.delete(found);
		return wishDto;
	}

	public WishDTO getCurrentWish(String eater) {
		AnOrder order = orderService.getCurrentOrder();
		Wish found = wishRepo.findByNickAndOrder(eater, order);
		if (found == null)
			return null;
		WishDTO wishDto = WishDTO.wrap(found);
		return wishDto;
	}

}
