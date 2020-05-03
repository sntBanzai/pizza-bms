package pl.malyszko.jerzy.pizzabms;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
import pl.malyszko.jerzy.pizzabms.entity.Pizza;
import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.entity.WishItem;
import pl.malyszko.jerzy.pizzabms.entity.WishType;

/**
 * @author Jerzy Mayszko
 *
 */
public class JsonTests {

	static String wishJson = "{\"eater\": \"Olo-ziom\",\"wishes\": [{\"pizza\": \"carbonara\",  \"pieces\": 4},"
			+ "{\"pizza\": \"vegetarian\",\"pieces\": 2}]}";

	String orderJson = "{\"order-lines\":[{\"pizzas\":[{"
			+ "\"pizza\":\"carbonara\",\"details\":[{"
			+ "\"eater\":\"Olo-ziom\",\"pieces\":4}]},"
			+ "{\"pizza\":\"carbonara\",\"details\":[{"
			+ "\"eater\":\"coder\",\"pieces\":2},{"
			+ "\"eater\":\"kaktus\",\"pieces\":1}]}],\"completed\":false"
			+ "},{\"pizzas\":[{" + "\"pizza\":\"vegetarian\",\"details\":[{"
			+ "\"eater\":\"Olo-ziom\",\"pieces\":5},{"
			+ "\"eater\":\"coder\",\"pieces\":3}]}],\"completed\":true}]}";

	@Test
	public void shouldDeserializelWish()
			throws JsonMappingException, JsonProcessingException {
		// when
		Wish valueRead = new ObjectMapper().readValue(wishJson, Wish.class);

		// then
		assertNotNull(valueRead);
		assertEquals("Olo-ziom", valueRead.getNick());
		assertEquals(6, valueRead.getWishItems().size());
		assertEquals(2,
				valueRead.getWishItems().stream()
						.map(wi -> wi.getWishType().getName())
						.collect(Collectors.toSet()).size());
		assertEquals(4L, valueRead.getWishItems().stream().filter(
				wi -> Objects.equals("carbonara", wi.getWishType().getName()))
				.count());
		assertEquals(2L, valueRead.getWishItems().stream().filter(
				wi -> Objects.equals("vegetarian", wi.getWishType().getName()))
				.count());
		assertFalse(valueRead.getWishItems().stream()
				.map(wi -> wi.getWishType().getName())
				.anyMatch(wt -> Objects.equals(wt, "hawaiian")));
	}

	@Test
	public void shouldSerializelWish() throws JsonParseException, IOException {
		// given
		WishType wt1 = new WishType("mexicana");
		WishType wt2 = new WishType("salami");
		WishType wt3 = new WishType("pepperoni");
		Wish w = new Wish();
		w.setNick("miszczu");
		int range1 = 7, range2 = 1, range3 = 5;
		IntStream.range(0, range1).forEach(i -> new WishItem(w, wt1));
		IntStream.range(0, range2).forEach(i -> new WishItem(w, wt2));
		IntStream.range(0, range3).forEach(i -> new WishItem(w, wt3));

		// when
		ObjectMapper om = new ObjectMapper();
		String jsonSerialized = om.writeValueAsString(w);

		// then
		assertNotNull(jsonSerialized);
		Wish valueRead = om.readValue(jsonSerialized, Wish.class);
		assertNotNull(valueRead);
		assertEquals("miszczu", valueRead.getNick());
		assertEquals(IntStream.of(range1, range2, range3).sum(),
				valueRead.getWishItems().size());
		assertEquals(3,
				valueRead.getWishItems().stream()
						.map(wi -> wi.getWishType().getName())
						.collect(Collectors.toSet()).size());
		assertEquals(range1, valueRead.getWishItems().stream().filter(
				wi -> Objects.equals("mexicana", wi.getWishType().getName()))
				.count());
		assertEquals(range2, valueRead.getWishItems().stream().filter(
				wi -> Objects.equals("salami", wi.getWishType().getName()))
				.count());
		assertEquals(range3, valueRead.getWishItems().stream().filter(
				wi -> Objects.equals("pepperoni", wi.getWishType().getName()))
				.count());

	}

	@Test
	public void shouldSerializeAnOrder() throws IOException {
		// given
		AnOrder order = new AnOrder();
		Pizza firstPizza = new Pizza(order);
		Pizza secondPizza = new Pizza(order);
		Pizza thirdPizza = new Pizza(order);
		Wish oloWish = new Wish();
		oloWish.setNick("Olo-ziom");
		Wish kaktusWish = new Wish();
		kaktusWish.setNick("kaktus");
		Wish coderWish = new Wish();
		coderWish.setNick("coder");
		WishType carbonaraType = new WishType("carbonara");
		WishType vegetarianType = new WishType("vegetarian");
		IntStream.range(0, 4)
				.mapToObj(i -> new WishItem(oloWish, carbonaraType))
				.forEach(firstPizza::addNextItem);
		IntStream.range(0, 2)
				.mapToObj(i -> new WishItem(coderWish, carbonaraType))
				.forEach(secondPizza::addNextItem);
		secondPizza.addNextItem(new WishItem(kaktusWish, carbonaraType));

		IntStream.range(0, 5)
				.mapToObj(i -> new WishItem(oloWish, vegetarianType))
				.forEach(thirdPizza::addNextItem);
		IntStream.range(0, 3)
				.mapToObj(i -> new WishItem(coderWish, vegetarianType))
				.forEach(thirdPizza::addNextItem);

		// when
		ObjectMapper om = new ObjectMapper();
		String jsonSerialized = om.writerWithDefaultPrettyPrinter()
				.writeValueAsString(order);

		// then
		// System.out.println(jsonSerialized);
		JsonParser parser = new JsonFactory().createParser(jsonSerialized);
		JsonNode node = new ObjectMapper().readTree(parser);
		JsonNode jsonNode = node.get("order-lines");
		assertNotNull(jsonNode);
		assertEquals(JsonNodeType.ARRAY, jsonNode.getNodeType());

		for(int i = 0; i<2; i++) {
			JsonNode jsonNode2 = jsonNode.get(0);
			assertNotNull(jsonNode2);
			assertEquals(JsonNodeType.OBJECT, jsonNode2.getNodeType());
			JsonNode jsonNode3 = jsonNode2.get("pizzas");
			assertNotNull(jsonNode3);
			assertEquals(JsonNodeType.ARRAY, jsonNode3.getNodeType());
		}
		

	}
	
	public static void main(String...strings) {
		System.out.println(wishJson);
	}

}
