package pl.malyszko.jerzy.pizzabms.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import pl.malyszko.jerzy.pizzabms.dto.WishDTO;
import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.entity.WishItem;
import pl.malyszko.jerzy.pizzabms.entity.WishType;

public class WishDTOJsonDeserializer extends JsonDeserializer<WishDTO> {

	@Override
	public WishDTO deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectCodec oc = p.getCodec();
		JsonNode node = oc.readTree(p);
		p.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		String eater = node.get("eater").asText();
		WishDTO retVal = new WishDTO();
		retVal.setNick(eater);
		Map<String, Integer> pizzaPieces = retVal.getPizzaPieces();
		Consumer<JsonNode> nodeConsumer = jn -> {
			pizzaPieces.put(jn.get("pizza").asText(), jn.get("pieces").asInt());
		};
		JsonNode wishesNode = node.get("wishes");
		wishesNode.forEach(nodeConsumer);
		return retVal;
	}

}
