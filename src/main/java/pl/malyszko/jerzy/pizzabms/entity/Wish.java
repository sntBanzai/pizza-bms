package pl.malyszko.jerzy.pizzabms.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pl.malyszko.jerzy.pizzabms.json.WishDTOJsonDeserializer;
import pl.malyszko.jerzy.pizzabms.json.WishDTOJsonSerializer;

/**
 * @author Jerzy Mayszko
 *
 */
@Entity
public class Wish extends AbstractEntity {

	@Column(nullable = false)
	private String nick;

	@OneToMany
	private List<WishItem> wishes = new ArrayList<>();

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), this.nick);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public List<WishItem> getWishItems() {
		return wishes;
	}
}
