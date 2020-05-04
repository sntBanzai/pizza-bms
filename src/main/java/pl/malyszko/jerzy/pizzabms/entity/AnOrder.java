package pl.malyszko.jerzy.pizzabms.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.malyszko.jerzy.pizzabms.json.OrdeDTOrJsonSerializer;

/**
 * @author Jerzy Mayszko
 *
 */
@Entity
public class AnOrder extends AbstractEntity {

	@Column(nullable = false)
	private Boolean completed = Boolean.FALSE;

	@OneToMany
	private List<Pizza> orderCompletions = new ArrayList<>();

	public List<Pizza> getOrderCompletions() {
		return orderCompletions;
	}

	public void setOrderCompletions(List<Pizza> orderCompletions) {
		this.orderCompletions = orderCompletions;
	}

}
