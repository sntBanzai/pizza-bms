package pl.malyszko.jerzy.pizzabms.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Pizza extends AbstractEntity {

	public Pizza() {

	}

	public Pizza(AnOrder order) {
		this.order = order;
	}

	@Column(nullable = false)
	@ManyToOne
	private AnOrder order;

	@Column(nullable = false)
	@OneToOne
	private WishItem itemOne;

	@OneToOne
	private WishItem itemTwo;

	@OneToOne
	private WishItem itemThree;

	@OneToOne
	private WishItem itemFour;

	@OneToOne
	private WishItem itemFive;

	@OneToOne
	private WishItem itemSix;

	@OneToOne
	private WishItem itemSeven;

	@OneToOne
	private WishItem itemEight;

	public WishItem getItemOne() {
		return itemOne;
	}

	public void setItemOne(WishItem itemOne) {
		this.itemOne = itemOne;
	}

	public WishItem getItemTwo() {
		return itemTwo;
	}

	public void setItemTwo(WishItem itemTwo) {
		this.itemTwo = itemTwo;
	}

	public WishItem getItemThree() {
		return itemThree;
	}

	public void setItemThree(WishItem itemThree) {
		this.itemThree = itemThree;
	}

	public WishItem getItemFour() {
		return itemFour;
	}

	public void setItemFour(WishItem itemFour) {
		this.itemFour = itemFour;
	}

	public WishItem getItemFive() {
		return itemFive;
	}

	public void setItemFive(WishItem itemFive) {
		this.itemFive = itemFive;
	}

	public WishItem getItemSix() {
		return itemSix;
	}

	public void setItemSix(WishItem itemSix) {
		this.itemSix = itemSix;
	}

	public WishItem getItemSeven() {
		return itemSeven;
	}

	public void setItemSeven(WishItem itemSeven) {
		this.itemSeven = itemSeven;
	}

	public WishItem getItemEight() {
		return itemEight;
	}

	public void setItemEight(WishItem itemEight) {
		this.itemEight = itemEight;
	}

	public AnOrder getOrder() {
		return order;
	}

	public void setOrder(AnOrder order) {
		this.order = order;
	}

	public List<WishItem> getItems() {
		return Arrays.asList(getItemOne(), getItemTwo(), getItemThree(),
				getItemFour(), getItemFive(), getItemSix(), getItemSeven(),
				getItemEight());
	}

	public Boolean isCompleted() {
		return getItems().stream().noneMatch(Objects::isNull);
	}
}
