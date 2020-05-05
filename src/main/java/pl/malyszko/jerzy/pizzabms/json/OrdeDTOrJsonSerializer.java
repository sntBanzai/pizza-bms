package pl.malyszko.jerzy.pizzabms.json;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import pl.malyszko.jerzy.pizzabms.dto.OrderDTO;
import pl.malyszko.jerzy.pizzabms.entity.Pizza;

/**
 * @author Jerzy Mayszko
 *
 */
public class OrdeDTOrJsonSerializer extends JsonSerializer<OrderDTO> {

	@Override
	public void serialize(OrderDTO value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeArrayFieldStart("order-lines");

//		Map<Boolean, List<Pizza>> collect = value.getOrderCompletions().stream()
//				.collect(Collectors.partitioningBy(Pizza::isCompleted));

		writeItDown(value.getNotCompleted(), Boolean.FALSE, gen);
		writeItDown(value.getCompleted(), Boolean.TRUE, gen);

		gen.writeEndArray();
		gen.writeEndObject();
	}

	private void writeItDown(
			List<Map<String, Map<String, Long>>> partitioned,
			Boolean completedMark, JsonGenerator gen) throws IOException {
		if (!partitioned.isEmpty()) {
			gen.writeStartObject();
			gen.writeArrayFieldStart("pizzas");
			for (Map<String, Map<String, Long>> pizza : partitioned) {
				for (Map.Entry<String, Map<String, Long>> ent : pizza
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
			}
			gen.writeEndArray();
			gen.writeBooleanField("completed", completedMark);
			gen.writeEndObject();
		}
	}

}
