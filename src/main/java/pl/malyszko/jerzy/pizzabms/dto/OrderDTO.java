package pl.malyszko.jerzy.pizzabms.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
import pl.malyszko.jerzy.pizzabms.entity.Pizza;
import pl.malyszko.jerzy.pizzabms.json.OrdeDTOrJsonSerializer;

/**
 * @author Jerzy Mayszko
 *
 */
@JsonSerialize(using = OrdeDTOrJsonSerializer.class)
public class OrderDTO {

	private List<Map<String, Map<String, Long>>> completed = new ArrayList<>();
	private List<Map<String, Map<String, Long>>> notCompleted = new ArrayList<>();

	public List<Map<String, Map<String, Long>>> getCompleted() {
		return completed;
	}

	public List<Map<String, Map<String, Long>>> getNotCompleted() {
		return notCompleted;
	}

	public static OrderDTO wrap(AnOrder order) {
		OrderDTO retVal = new OrderDTO();
		List<Pizza> orderCompletions = order
				.getOrderCompletions();
		Map<Boolean, Map<Long, Map<String, Map<String, Long>>>> collect = orderCompletions.stream()
				.flatMap(pizza -> pizza.getItems().stream())
				.filter(Objects::nonNull)
				.collect(Collectors.partitioningBy(
						wi -> wi.getPizza().isCompleted(),
						Collectors.groupingBy(wi -> wi.getPizza().getId(),
								Collectors.groupingBy(
										wi -> wi.getWishType().getName(),
										Collectors.groupingBy(
												wi -> wi.getWish().getNick(),
												Collectors.counting())))));
		retVal.completed.addAll(collect.get(Boolean.TRUE).values());
		retVal.notCompleted.addAll(collect.get(Boolean.FALSE).values());
		return retVal;
	}

}
