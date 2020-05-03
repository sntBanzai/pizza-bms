package pl.malyszko.jerzy.pizzabms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Jerzy Mayszko
 *
 */
@Entity
public class WishItem extends AbstractEntity {

	public WishItem(Wish wish, WishType wishType) {
		super();
		this.wishType = wishType;
		this.wish = wish;
		this.wish.getWishItems().add(this);
	}

	public WishItem() {

	}

	@ManyToOne
	@Column(nullable = false)
	private WishType wishType;

	@ManyToOne
	@Column(nullable = false)
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

}
