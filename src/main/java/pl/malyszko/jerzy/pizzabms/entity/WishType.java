package pl.malyszko.jerzy.pizzabms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class WishType extends AbstractEntity {

	public WishType(String name) {
		this.name = name;
	}

	public WishType() {

	}

	@Column(nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
