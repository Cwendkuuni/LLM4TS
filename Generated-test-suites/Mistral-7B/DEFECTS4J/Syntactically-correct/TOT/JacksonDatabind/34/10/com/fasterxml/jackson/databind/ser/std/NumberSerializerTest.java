import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class NumberSerializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        BigDecimal bigDecimal = new BigDecimal("123.456");
        String json = mapper.writeValueAsString(bigDecimal);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(bigDecimal, deserialized);
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        String json = mapper.writeValueAsString(bigInteger);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(bigInteger, deserialized);
    }

    @Test
    public void testSerializeInt() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        int intValue = 123;
        String json = mapper.writeValueAsString(intValue);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(intValue, deserialized.intValue());
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        long longValue = 1234567890123456789L;
        String json = mapper.writeValueAsString(longValue);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(longValue, deserialized.longValue());
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        double doubleValue = 123.456;
        String json = mapper.writeValueAsString(doubleValue);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(doubleValue, deserialized.doubleValue(), 0.001);
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        float floatValue = 123.456f;
        String json = mapper.writeValueAsString(floatValue);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(floatValue, deserialized.floatValue(), 0.001f);
    }

    @Test
    public void testSerializeByte() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        byte byteValue = 127;
        String json = mapper.writeValueAsString(byteValue);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(byteValue, deserialized.byteValue());
    }

    @Test
    public void testSerializeShort() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        short shortValue = 123;
        String json = mapper.writeValueAsString(shortValue);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(shortValue, deserialized.shortValue());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        String stringValue = "123";
        String json = mapper.writeValueAsString(stringValue);

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Number deserialized = numberSerializer.deserialize(jsonParser, Number.class, null);
        Assert.assertEquals(new BigInteger(stringValue), deserialized);
    }

    @Test(expected = JsonMappingException.class)
    public void testFailDeserializeNull() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        String json = null;

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        numberSerializer.deserialize(jsonParser, Number.class, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testFailDeserializeInvalidJson() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        String json = "{\"invalid\": \"json\"}";

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        numberSerializer.deserialize(jsonParser, Number.class, null);
    }

    @Test
    public void testFailDeserializeInvalidType() throws JsonProcessingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        String json = "{\"invalid\": []}";

        JsonParser jsonParser = mapper.getFactory().createParser(json);
        try {
            numberSerializer.deserialize(jsonParser, String.class, null);
        } catch (JsonMappingException e) {
            Assert.assertEquals("Can not construct instance of java.lang.String from JSON String", e.getMessage());
        }
    }
}