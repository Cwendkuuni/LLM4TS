import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.junit.SerializerTestBase;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class NumberSerializerTest extends SerializerTestBase<NumberSerializer> {

    @Override
    protected NumberSerializer createInstance() {
        return new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        BigDecimal number = new BigDecimal("123.45");
        String json = mapper.writeValueAsString(number);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testSerializeBigInteger() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        BigInteger number = new BigInteger("12345678901234567890");
        String json = mapper.writeValueAsString(number);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testSerializeInteger() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testSerializeLong() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Long number = 123456789012345L;
        String json = mapper.writeValueAsString(number);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testSerializeDouble() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Double number = 123.45;
        String json = mapper.writeValueAsString(number);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(number, deserialized, 0.001);
    }

    @Test
    public void testSerializeFloat() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Float number = 123.45f;
        String json = mapper.writeValueAsString(number);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(number, deserialized, 0.001f);
    }

    @Test
    public void testSerializeByte() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Byte number = (byte) 127;
        String json = mapper.writeValueAsString(number);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testSerializeShort() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Short number = (short) 32767;
        String json = mapper.writeValueAsString(number);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testSerializeCurrency() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Currency currency = Currency.getInstance("USD");
        Number amount = 123.45;
        ObjectNode node = mapper.createObjectNode();
        node.put("type", "Currency");
        node.put("value", amount.doubleValue());
        node.put("exponent", 2);
        String json = mapper.writeValueAsString(currency);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(deserialized, amount);
    }

    @Test
    public void testSerializeNull() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(null);
        Number deserialized = mapper.readValue(json, Number.class);
        assertEquals(null, deserialized);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSerializeInvalid() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String invalidJson = "{\"type\":\"Invalid\"}";
        mapper.readValue(invalidJson, Number.class);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = getProvider();
        JsonNode schema = underTest.getSchema(provider, Number.class);
        assertEquals("number", schema.asText());
    }
}