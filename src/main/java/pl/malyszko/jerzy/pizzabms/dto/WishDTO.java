package pl.malyszko.jerzy.pizzabms.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.malyszko.jerzy.pizzabms.json.WishDTOJsonDeserializer;
import pl.malyszko.jerzy.pizzabms.json.WishDTOJsonSerializer;

@JsonSerialize(using = WishDTOJsonSerializer.class)
@JsonDeserialize(using = WishDTOJsonDeserializer.class)
public class WishDTO {
	
	private String nick;
	
	private Map<String,Integer> pizzaPieces = new HashMap<>();

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Map<String, Integer> getPizzaPieces() {
		return pizzaPieces;
	}
	
}
