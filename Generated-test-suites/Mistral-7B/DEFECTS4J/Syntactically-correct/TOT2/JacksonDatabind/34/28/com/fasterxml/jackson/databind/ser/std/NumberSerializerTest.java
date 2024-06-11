import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(bigDecimal, jsonGenerator, mapper.getSerializerProvider());
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(jsonGenerator.getOutputStr()));
        JsonNode jsonNode = jsonParser.readValueAsTree();
        ObjectNode jsonObject = (ObjectNode) jsonNode;
        assertNotNull(jsonObject);
        assertNotNull(jsonObject.get("number"));
        assertEquals("123.45", jsonObject.get("number").asText());
    }

    @Test
    public void testSerialize_BigInteger() throws Exception {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(bigInteger, jsonGenerator, mapper.getSerializerProvider());
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(jsonGenerator.getOutputStr()));
        JsonNode jsonNode = jsonParser.readValueAsTree();
        ObjectNode jsonObject = (ObjectNode) jsonNode;
        assertNotNull(jsonObject);
        assertNotNull(jsonObject.get("number"));
        assertEquals("12345678901234567890", jsonObject.get("number").asText());
    }

    // ... test cases for other method signatures
}