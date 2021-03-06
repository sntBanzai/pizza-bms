package pl.malyszko.jerzy.pizzabms.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * @author Jerzy Mayszko
 *
 */
@Entity
public class AnOrder extends AbstractEntity {

	@Column(nullable = false)
	private Boolean completed = Boolean.FALSE;
	
	@OneToMany(mappedBy = "order")
	private List<Wish> wishes = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
	private List<Pizza> orderCompletions = new ArrayList<>();

	public List<Pizza> getOrderCompletions() {
		return orderCompletions;
	}

	public void setOrderCompletions(List<Pizza> orderCompletions) {
		this.orderCompletions = orderCompletions;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public List<Wish> getWishes() {
		return wishes;
	}

	public void setWishes(List<Wish> wishes) {
		this.wishes = wishes;
	}
	
	

}
