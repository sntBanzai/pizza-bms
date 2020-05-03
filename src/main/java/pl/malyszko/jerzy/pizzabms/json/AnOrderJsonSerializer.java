package pl.malyszko.jerzy.pizzabms.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import pl.malyszko.jerzy.pizzabms.entity.AnOrder;
import pl.malyszko.jerzy.pizzabms.entity.Pizza;

/**
 * @author Jerzy Mayszko
 *
 */
public class AnOrderJsonSerializer extends JsonSerializer<AnOrder> {

	@Override
	public void serialize(AnOrder value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeArrayFieldStart("order-lines");

		Map<Boolean, List<Pizza>> collect = value.getOrderCompletions().stream()
				.collect(Collectors.partitioningBy(Pizza::isCompleted));

		writeItDown(collect, Boolean.FALSE, gen);
		writeItDown(collect, Boolean.TRUE, gen);

		gen.writeEndArray();
		gen.writeStartObject();
	}

	private void writeItDown(Map<Boolean, List<Pizza>> partitioned,
			Boolean completedMark, JsonGenerator gen) throws IOException {
		List<Pizza> list = partitioned.get(completedMark);
		if (!list.isEmpty()) {
			for (Pizza pizza : list) {
				gen.writeStartObject();
				gen.writeArrayFieldStart("pizzas");
				Map<String, Map<String, Long>> collect = pizza.getItems()
						.stream()
						.collect(Collectors.groupingBy(
								wi -> wi.getWishType().getName(),
								Collectors.groupingBy(
										wi -> wi.getWish().getNick(),
										Collectors.counting())));
				for (Map.Entry<String, Map<String, Long>> ent : collect
						.entrySet()) {
					gen.writeStartObject();
					gen.writeStringField("pizza", ent.getKey());
					gen.writeArrayFieldStart("details");
					for (Map.Entry<String, Long> ent2 : ent.getValue()
							.entrySet()) {
						gen.writeStartObject();
						gen.writeStringField("eater", ent2.getKey());
						gen.writeNumberField("pieces", ent2.getValue());
						gen.writeEndObject();
					}
					gen.writeEndArray();
					gen.writeEndObject();
				}
				gen.writeEndArray();
				gen.writeBooleanField("completed", completedMark);
				gen.writeEndObject();
			}
		}
	}

}
