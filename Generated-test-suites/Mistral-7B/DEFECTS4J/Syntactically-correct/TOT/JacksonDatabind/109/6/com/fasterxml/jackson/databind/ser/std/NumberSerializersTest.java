import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException, IOException {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Integer integer = 123;
        String json = mapper.writerWithTypeSerializer(new TypeReference<Integer>() {
        }).withFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(integer);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(123, parser.getValueAsInt());

        Object deserialized = parser.readValueAs(Integer.class);
        Assert.assertEquals(integer, deserialized);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException, IOException {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Long longValue = 1234567890123L;
        String json = mapper.writerWithTypeSerializer(new TypeReference<Long>() {
        }).withFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(longValue);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_LONG, token);
        Assert.assertEquals(longValue, parser.getValueAsLong());

        Object deserialized = parser.readValueAs(Long.class);
        Assert.assertEquals(longValue, deserialized);
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException, IOException {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Short shortValue = Short.MIN_VALUE;
        String json = mapper.writerWithTypeSerializer(new TypeReference<Short>() {
        }).withFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(shortValue);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(shortValue.shortValue(), parser.getValueAsInt());

        Object deserialized = parser.readValueAs(Short.class);
        Assert.assertEquals(shortValue, deserialized);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException, IOException {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Float floatValue = 3.14f;
        String json = mapper.writerWithTypeSerializer(new TypeReference<Float>() {
        }).withFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(floatValue);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        Assert.assertEquals(floatValue, parser.getValueAsFloat(), 0.01f);

        Object deserialized = parser.readValueAs(Float.class);
        Assert.assertEquals(floatValue, deserialized);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException, IOException {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        Double doubleValue = 1.79769313486231E308D;
        String json = mapper.writerWithTypeSerializer(new TypeReference<Double>() {
        }).withFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .writeValueAsString(doubleValue);

        JsonParser parser = mapper.getFactory().createParser(json);
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        Assert.assertEquals(doubleValue, parser.getValueAsDouble(), 0.000001);

        Object deserialized = parser.readValueAs(Double.class);
        Assert.assertEquals(doubleValue, deserialized);
    }
}