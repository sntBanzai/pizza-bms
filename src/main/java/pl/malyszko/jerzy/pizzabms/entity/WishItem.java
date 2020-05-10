package pl.malyszko.jerzy.pizzabms.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author Jerzy Mayszko
 *
 */
@Entity
public class WishItem extends AbstractEntity {

	@OneToOne
	private Pizza pizza;

	public WishItem(Wish wish, WishType wishType) {
		super();
		this.wishType = wishType;
		this.wish = wish;
		this.wish.getWishItems().add(this);
	}

	public WishItem() {

	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "wishTypeId")
	private WishType wishType;

	@ManyToOne(optional = false)
	@JoinColumn(name = "wishId")
	private Wish wish;

	public WishType getWishType() {
		return wishType;
	}

	public void setWishType(WishType wishType) {
		this.wishType = wishType;
	}

	public Wish getWish() {
		return wish;
	}

	public void setWish(Wish wish) {
		this.wish = wish;
	}

	public Pizza getPizza() {
		return pizza;
	}

	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}

	public Pizza removeFromCompletion() {
		if (pizza != null) {
			pizza.removeItem(this);
			Pizza toRemove = pizza;
			pizza = null;
			return toRemove;
		}
		return null;
	}

}
