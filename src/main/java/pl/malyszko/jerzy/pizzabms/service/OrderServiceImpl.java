package pl.malyszko.jerzy.pizzabms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.malyszko.jerzy.pizzabms.dao.AnOrderRepository;
import pl.malyszko.jerzy.pizzabms.dao.PizzaRepository;
import pl.malyszko.jerzy.pizzabms.dto.OrderDTO;
import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
import pl.malyszko.jerzy.pizzabms.entity.Pizza;
import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.entity.WishItem;

/**
 * @author Jerzy Mayszko
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	private static final Long PIZZA_SIZE = 8L;
	private static final Long HALF_PIZZA_SIZE = PIZZA_SIZE / 2L;

	@Autowired
	private AnOrderRepository orderRepo;

	@Autowired
	private PizzaRepository pizzaRepo;

	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void distributeWish(Wish wish) {
		AnOrder currentOrder = getCurrentOrder();
		Map<String, List<WishItem>> wishDetails = wish.getWishItems().stream()
				.collect(Collectors
						.groupingBy(wi -> wi.getWishType().getName()));

		Map<Long, Map<String, Long>> theUnfinishedPizzas = currentOrder
				.getOrderCompletions().stream()
				.filter(piz -> !piz.isCompleted())
				.flatMap(pizza -> pizza.getItems().stream())
				.filter(Objects::nonNull)
				.collect(Collectors.groupingBy(wi -> wi.getPizza().getId(),
						Collectors.groupingBy(wi -> wi.getWishType().getName(),
								Collectors.counting())));

		List<String> wishDetailsTypes = new ArrayList<>(wishDetails.keySet());

		exactMatchStrategy(wishDetails, theUnfinishedPizzas, wishDetailsTypes);
		approxMatchStrategy(wishDetails, theUnfinishedPizzas, wishDetailsTypes);
		giveUpStrategy(wishDetails, currentOrder);

	}

	private void giveUpStrategy(Map<String, List<WishItem>> wishDetails,
			AnOrder currentOrder) {
		// TODO Auto-generated method stub

	}

	private void approxMatchStrategy(Map<String, List<WishItem>> wishDetails,
			Map<Long, Map<String, Long>> theUnfinishedPizzas,
			List<String> wishDetailsTypes) {
		ListIterator<String> wishDetailsIterator = wishDetailsTypes
				.listIterator();
		for (Map.Entry<Long, Map<String, Long>> ent : theUnfinishedPizzas
				.entrySet()) {
			ListIterator<String> pizzaItemsIterator = new ArrayList<>(
					ent.getValue().keySet()).listIterator();
			while (pizzaItemsIterator.hasNext()) {
				String pizzaItemType = pizzaItemsIterator.next();
				while (wishDetailsIterator.hasNext()) {
					String typeName = wishDetailsIterator.next();
					long wishTypeSize = Integer
							.valueOf(wishDetails.get(typeName).size())
							.longValue();
					Long piecesSum = ent.getValue().get(pizzaItemType)
							+ wishTypeSize;
					if (piecesSum < PIZZA_SIZE) {

					}
				}
			}
		}

	}

	private void exactMatchStrategy(Map<String, List<WishItem>> wishDetails,
			Map<Long, Map<String, Long>> theUnfinishedPizzas,
			List<String> wishDetailsTypes) {
		ListIterator<String> wishDetailsIterator = wishDetailsTypes
				.listIterator();
		for (Map.Entry<Long, Map<String, Long>> ent : theUnfinishedPizzas
				.entrySet()) {
			ListIterator<String> pizzaItemsIterator = new ArrayList<>(
					ent.getValue().keySet()).listIterator();
			while (pizzaItemsIterator.hasNext()) {
				String pizzaItemType = pizzaItemsIterator.next();
				while (wishDetailsIterator.hasNext()) {
					String typeName = wishDetailsIterator.next();
					long wishTypeSize = Integer
							.valueOf(wishDetails.get(typeName).size())
							.longValue();
					Long completionPizzaSize = ent.getValue()
							.get(pizzaItemType);
					Long piecesSum = completionPizzaSize + wishTypeSize;
					boolean isPizzaSizeReached = Objects.equals(PIZZA_SIZE,
							piecesSum);
					boolean isSameTypePizza = Objects.equals(pizzaItemType,
							typeName);
					if (isPizzaSizeReached) {
						boolean isComplimentary = Objects.equals(4L,
								wishTypeSize);
						if (isSameTypePizza || isComplimentary) {
							assignWishItemsToAPizza(ent.getKey(),
									wishDetails.get(typeName));
							wishDetailsIterator.remove();
							pizzaItemsIterator.remove();
						}
					} else if (Objects.equals(HALF_PIZZA_SIZE, piecesSum)
							&& isSameTypePizza) {
						assignWishItemsToAPizza(ent.getKey(),
								wishDetails.get(typeName));
						wishDetailsIterator.remove();
						ent.getValue().put(typeName, piecesSum);
					} else if (wishTypeSize > HALF_PIZZA_SIZE && Objects
							.equals(HALF_PIZZA_SIZE, completionPizzaSize)) {
						List<WishItem> collect = wishDetails.get(typeName)
								.stream().limit(HALF_PIZZA_SIZE)
								.collect(Collectors.toList());
						assignWishItemsToAPizza(ent.getKey(), collect);
						wishDetails.get(typeName).removeAll(collect);
						pizzaItemsIterator.remove();
					}
				}
			}
		}
	}

	private void assignWishItemsToAPizza(Long id, List<WishItem> list) {
		Optional<Pizza> foundPizza = pizzaRepo.findById(id);
		Pizza pizza = foundPizza.orElseThrow(IllegalStateException::new);
		list.stream().forEach(pizza::addNextItem);
		pizzaRepo.save(pizza);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AnOrder getCurrentOrder() {
		Optional<AnOrder> current = orderRepo.findByCompletedFalse();
		return current.orElseGet(() -> orderRepo.save(new AnOrder()));
	}

	@Override
	public List<OrderDTO> listOrders() {
		List<OrderDTO> retVal = new ArrayList<>();
		Iterable<AnOrder> findAll = orderRepo.findAll();
		findAll.forEach(ord -> retVal.add(OrderDTO.wrap(ord)));
		return retVal;
	}

}
