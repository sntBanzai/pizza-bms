package pl.malyszko.jerzy.pizzabms.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.malyszko.jerzy.pizzabms.json.OrdeDTOrJsonSerializer;

@JsonSerialize(using = OrdeDTOrJsonSerializer.class)
public class OrderDTO {

	private List<Map<String, Map<String, Integer>>> completed = new ArrayList<>();
	private List<Map<String, Map<String, Integer>>> notCompleted = new ArrayList<>();
	
	public List<Map<String, Map<String, Integer>>> getCompleted() {
		return completed;
	}
	public List<Map<String, Map<String, Integer>>> getNotCompleted() {
		return notCompleted;
	}

	


}
