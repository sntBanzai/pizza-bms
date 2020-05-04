package pl.malyszko.jerzy.pizzabms;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import pl.malyszko.jerzy.pizzabms.dto.OrderDTO;
import pl.malyszko.jerzy.pizzabms.dto.WishDTO;

/**
 * @author Jerzy Mayszko
 *
 */
public class JsonTests {

	private static final String VEGETARIAN = "vegetarian";

	private static final String CARBONARA = "carbonara";

	private static final String CODER = "coder";

	private static final String KAKTUS = "kaktus";

	private static final String OLO_ZIOM = "Olo-ziom";

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
		WishDTO valueRead = new ObjectMapper().readValue(wishJson,
				WishDTO.class);

		// then
		assertNotNull(valueRead);
		assertEquals(OLO_ZIOM, valueRead.getNick());
		assertEquals(6, valueRead.getPizzaPieces().values().stream().reduce(0,
				Integer::sum));
		assertEquals(2, valueRead.getPizzaPieces().keySet().size());
		assertEquals(4, valueRead.getPizzaPieces().get(CARBONARA));
		assertEquals(2, valueRead.getPizzaPieces().get(VEGETARIAN));
		assertFalse(valueRead.getPizzaPieces().keySet().contains("hawaiian"));
	}

	@Test
	public void shouldSerializelWish() throws JsonParseException, IOException {
		// given
		WishDTO dto = new WishDTO();
		String eater = "Czesław Nieeemeeeen";
		dto.setNick(eater);
		int range1 = 7, range2 = 1, range3 = 5;
		dto.getPizzaPieces().put("mexicana", range1);
		dto.getPizzaPieces().put("salami", range2);
		dto.getPizzaPieces().put("pepperoni", range3);

		// when
		ObjectMapper om = new ObjectMapper();
		String jsonSerialized = om.writeValueAsString(dto);

		// then
		assertNotNull(jsonSerialized);
		WishDTO valueRead = om.readValue(jsonSerialized, WishDTO.class);
		assertNotNull(valueRead);
		assertEquals(eater, valueRead.getNick());
		assertEquals(IntStream.of(range1, range2, range3).sum(), valueRead
				.getPizzaPieces().values().stream().reduce(0, Integer::sum));
		assertEquals(3, valueRead.getPizzaPieces().keySet().size());
		assertEquals(range1, valueRead.getPizzaPieces().get("mexicana"));
		assertEquals(range2, valueRead.getPizzaPieces().get("salami"));
		assertEquals(range3, valueRead.getPizzaPieces().get("pepperoni"));

	}

	@Test
	public void shouldSerializeAnOrder() throws IOException {
		// given
		OrderDTO order = new OrderDTO();
		List<Map<String, Map<String, Integer>>> completed = order
				.getCompleted();
		List<Map<String, Map<String, Integer>>> notCompleted = order
				.getNotCompleted();

		Map<String, Map<String, Integer>> firstPizza = new HashMap<>();
		notCompleted.add(firstPizza);
		firstPizza.put(CARBONARA, new HashMap<String, Integer>());
		firstPizza.get(CARBONARA).put(OLO_ZIOM, 4);

		Map<String, Map<String, Integer>> secondPizza = new HashMap<>();
		notCompleted.add(secondPizza);
		secondPizza.put(CARBONARA, new HashMap<String, Integer>());
		secondPizza.get(CARBONARA).put(KAKTUS, 2);

		Map<String, Map<String, Integer>> thirdPizza = new HashMap<>();
		completed.add(thirdPizza);
		thirdPizza.put(VEGETARIAN, new HashMap<String, Integer>());
		thirdPizza.get(VEGETARIAN).put(OLO_ZIOM, 5);
		thirdPizza.get(VEGETARIAN).put(CODER, 3);

		// when
		ObjectMapper om = new ObjectMapper();
		String jsonSerialized = om.writerWithDefaultPrettyPrinter()
				.writeValueAsString(order);

		// then
		System.out.println(jsonSerialized);
		JsonParser parser = new JsonFactory().createParser(jsonSerialized);
		JsonNode node = new ObjectMapper().readTree(parser);
		JsonNode jsonNode = node.get("order-lines");
		assertNotNull(jsonNode);
		assertEquals(JsonNodeType.ARRAY, jsonNode.getNodeType());

		for (int i = 0; i < 2; i++) {
			JsonNode jsonNode2 = jsonNode.get(0);
			assertNotNull(jsonNode2);
			assertEquals(JsonNodeType.OBJECT, jsonNode2.getNodeType());
			JsonNode jsonNode3 = jsonNode2.get("pizzas");
			assertNotNull(jsonNode3);
			assertEquals(JsonNodeType.ARRAY, jsonNode3.getNodeType());
		}

	}

	public static void main(String... strings) {
		System.out.println(wishJson);
	}

}
