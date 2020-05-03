package pl.malyszko.jerzy.pizzabms.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.malyszko.jerzy.pizzabms.json.AnOrderJsonSerializer;

/**
 * @author Jerzy Mayszko
 *
 */
@Entity
@JsonSerialize(using = AnOrderJsonSerializer.class)
public class AnOrder extends AbstractEntity {

	@OneToMany
	private List<Pizza> orderCompletions = new ArrayList<>();

	public List<Pizza> getOrderCompletions() {
		return orderCompletions;
	}

	public void setOrderCompletions(List<Pizza> orderCompletions) {
		this.orderCompletions = orderCompletions;
	}

}
