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

import pl.malyszko.jerzy.pizzabms.entity.Wish;
import pl.malyszko.jerzy.pizzabms.entity.WishItem;
import pl.malyszko.jerzy.pizzabms.entity.WishType;

public class WishJsonDeserializer extends JsonDeserializer<Wish> {

	@Override
	public Wish deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectCodec oc = p.getCodec();
		JsonNode node = oc.readTree(p);
		p.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		String eater = node.get("eater").asText();
		Wish retVal = new Wish();
		retVal.setNick(eater);
		Map<String, Integer> pizzaPieces = new HashMap<>();
		Consumer<JsonNode> nodeConsumer = jn -> {
			pizzaPieces.put(jn.get("pizza").asText(), jn.get("pieces").asInt());
		};
		JsonNode wishesNode = node.get("wishes");
		wishesNode.forEach(nodeConsumer);
		for (Map.Entry<String, Integer> ent : pizzaPieces.entrySet()) {
			WishType wt = new WishType();
			wt.setName(ent.getKey());
			IntStream.range(0, ent.getValue())
					.forEach(i -> new WishItem(retVal, wt));
		}
		return retVal;
	}

}
