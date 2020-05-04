package pl.malyszko.jerzy.pizzabms.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.json.WishDTOJsonDeserializer;
import pl.malyszko.jerzy.pizzabms.json.WishDTOJsonSerializer;

@JsonSerialize(using = WishDTOJsonSerializer.class)
@JsonDeserialize(using = WishDTOJsonDeserializer.class)
public class WishDTO {

	private String nick;

	private Map<String, Integer> pizzaPieces = new HashMap<>();

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Map<String, Integer> getPizzaPieces() {
		return pizzaPieces;
	}

	public static WishDTO wrap(Wish wish) {
		WishDTO retVal = new WishDTO();
		retVal.setNick(wish.getNick());
		Map<String, Long> collect = wish.getWishItems().stream()
				.collect(Collectors.groupingBy(wi -> wi.getWishType().getName(),
						Collectors.counting()));
		collect.forEach((k, v) -> retVal.getPizzaPieces().put(k, v.intValue()));
		return retVal;
	}
}
