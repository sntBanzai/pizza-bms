package pl.malyszko.jerzy.pizzabms.json;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import pl.malyszko.jerzy.pizzabms.dto.WishDTO;
import pl.malyszko.jerzy.pizzabms.entity.Wish;

public class WishDTOJsonSerializer extends JsonSerializer<WishDTO> {

	@Override
	public void serialize(WishDTO value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("eater", value.getNick());

		gen.writeArrayFieldStart("wishes");

		for (Map.Entry<String, Integer> wi : value.getPizzaPieces()
				.entrySet()) {
			gen.writeStartObject();
			gen.writeStringField("pizza", wi.getKey());
			gen.writeNumberField("pieces", wi.getValue());
			gen.writeEndObject();
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}

}
