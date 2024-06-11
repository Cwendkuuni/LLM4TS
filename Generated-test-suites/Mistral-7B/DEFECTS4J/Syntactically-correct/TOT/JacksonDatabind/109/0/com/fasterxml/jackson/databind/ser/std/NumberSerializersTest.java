import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.ReferenceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    private ObjectMapper objectMapper = JsonMapper.builder().build();

    @Test
    public void testIntegerSerialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonSerializer<Integer> integerSerializer = serializers.get(Integer.class.getName());
        Integer number = 123;
        String json = objectMapper.writerWithRootValueSerializer(integerSerializer)
                .writeValueAsString(number);
        Assert.assertEquals("123", json);
    }

    @Test
    public void testIntegerDeserialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonDeserializer<Integer> integerDeserializer = serializers.get(Integer.class.getName());
        String json = "123";
        Integer deserializedNumber = objectMapper.readerForUpdating(null).readValue(json, Integer.class);
        Assert.assertEquals(123, deserializedNumber.intValue());
    }

    @Test
    public void testIntegerBigIntegerDeserialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonDeserializer<BigInteger> bigIntegerDeserializer = serializers.get(BigInteger.class.getName());
        String json = "12345678901234567890123456789012345678901234567890";
        BigInteger deserializedNumber = objectMapper.readerForUpdating(null).readValue(json, BigInteger.class);
        Assert.assertEquals(new BigInteger("12345678901234567890123456789012345678901234567890"), deserializedNumber);
    }

    @Test
    public void testIntegerDeserializationWithJsonFormat() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Integer number = 123;
        String json = objectMapper.writerWithRootValueSerializer(new IntegerSerializer(Integer.class))
                .writeValueAsString(number);
        Assert.assertEquals("123", json);

        JsonDeserializer<Integer> integerDeserializer = serializers.get(Integer.class.getName());
        json = "123";
        Integer deserializedNumber = objectMapper.readerForUpdating(null).readValue(json, Integer.class);
        Assert.assertEquals(123, deserializedNumber.intValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerDeserializationWithWrongType() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        String json = "123.45";
        JsonDeserializer<Integer> integerDeserializer = serializers.get(Integer.class.getName());
        Integer number = objectMapper.readerForUpdating(null).readValue(json, Integer.class);
    }

    @Test
    public void testFloatSerialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonSerializer<Float> floatSerializer = serializers.get(Float.class.getName());
        Float number = 123.45f;
        String json = objectMapper.writerWithRootValueSerializer(floatSerializer)
                .writeValueAsString(number);
        Assert.assertEquals("123.45", json);
    }

    @Test
    public void testFloatDeserialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonDeserializer<Float> floatDeserializer = serializers.get(Float.class.getName());
        String json = "123.45";
        Float deserializedNumber = objectMapper.readerForUpdating(null).readValue(json, Float.class);
        Assert.assertEquals(123.45f, deserializedNumber, 0.001f);
    }

    @Test(expected = JsonMappingException.class)
    public void testFloatDeserializationWithWrongType() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        String json = "123.45";
        JsonDeserializer<Integer> integerDeserializer = serializers.get(Integer.class.getName());
        Integer number = objectMapper.readerForUpdating(null).readValue(json, Integer.class);
    }

    @Test
    public void testDoubleSerialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonSerializer<Double> doubleSerializer = serializers.get(Double.class.getName());
        Double number = 123.45;
        String json = objectMapper.writerWithRootValueSerializer(doubleSerializer)
                .writeValueAsString(number);
        Assert.assertEquals("123.45", json);
    }

    @Test
    public void testDoubleDeserialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonDeserializer<Double> doubleDeserializer = serializers.get(Double.class.getName());
        String json = "123.45";
        Double deserializedNumber = objectMapper.readerForUpdating(null).readValue(json, Double.class);
        Assert.assertEquals(123.45, deserializedNumber, 0.001);
    }

    @Test(expected = JsonMappingException.class)
    public void testDoubleDeserializationWithWrongType() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        String json = "123.45";
        JsonDeserializer<Integer> integerDeserializer = serializers.get(Integer.class.getName());
        Integer number = objectMapper.readerForUpdating(null).readValue(json, Integer.class);
    }

    @Test
    public void testLongSerialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonSerializer<Long> longSerializer = serializers.get(Long.class.getName());
        Long number = 1234567890123456789L;
        String json = objectMapper.writerWithRootValueSerializer(longSerializer)
                .writeValueAsString(number);
        Assert.assertEquals("1234567890123456789", json);
    }

    @Test
    public void testLongDeserialization() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        JsonDeserializer<Long> longDeserializer = serializers.get(Long.class.getName());
        String json = "1234567890123456789";
        Long deserializedNumber = objectMapper.readerForUpdating(null).readValue(json, Long.class);
        Assert.assertEquals(1234567890123456789L, deserializedNumber);
    }

    @Test(expected = JsonMappingException.class)
    public void testLongDeserializationWithWrongType() throws JsonMappingException, JsonGenerationException, IOException {
        Map<String, JsonSerializer<?>> serializers = new HashMap<>();
        NumberSerializers.addAll(serializers);
        String json = "1234567890123456789";
        JsonDeserializer<Integer> integerDeserializer = serializers.get(Integer.class.getName());
        Integer number = objectMapper.readerForUpdating(null).readValue(json, Integer.class);
    }
}