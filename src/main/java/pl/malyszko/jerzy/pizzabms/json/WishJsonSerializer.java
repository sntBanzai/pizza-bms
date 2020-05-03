package pl.malyszko.jerzy.pizzabms.json;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.entity.WishItem;

public class WishJsonSerializer extends JsonSerializer<Wish> {

	@Override
	public void serialize(Wish value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("eater", value.getNick());

		gen.writeArrayFieldStart("wishes");
		Map<String, Long> collect = value.getWishItems().stream()
				.collect(Collectors.groupingBy(wi -> wi.getWishType().getName(),
						Collectors.counting()));
		for (Map.Entry<String, Long> wi : collect.entrySet()) {
			gen.writeStartObject();
			gen.writeStringField("pizza", wi.getKey());
			gen.writeNumberField("pieces", wi.getValue());
			gen.writeEndObject();
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}

}
