import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.junit.SerializerTestBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Type;

public class NumberSerializersTest extends SerializerTestBase {

    public static void main(String[] args) throws Exception {
        // Initialize JUnit test
        generateTestFilesFromAnnotations(NumberSerializers.class);
        runTest(NumberSerializersTest.class);
    }

    // Test for IntegerSerializer
    @org.junit.Test
    public void testIntegerSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Integer integer = 123;

        String json = mapper.writeValueAsString(integer);
        assertJson("123", json);

        Integer deserialized = mapper.readValue(json, Integer.class);
        assertEquals(integer, deserialized);
    }

    // Test for LongSerializer
    @org.junit.Test
    public void testLongSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Long longNumber = 9876543210L;

        String json = mapper.writeValueAsString(longNumber);
        assertJson("9876543210", json);

        Long deserialized = mapper.readValue(json, Long.class);
        assertEquals(longNumber, deserialized);
    }

    // Test for ShortSerializer
    @org.junit.Test
    public void testShortSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Short shortNumber = 12;

        String json = mapper.writeValueAsString(shortNumber);
        assertJson("12", json);

        Short deserialized = mapper.readValue(json, Short.class);
        assertEquals(shortNumber, deserialized);
    }

    // Test for FloatSerializer
    @org.junit.Test
    public void testFloatSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Float floatNumber = 3.14F;

        String json = mapper.writeValueAsString(floatNumber);
        assertJson("3.14", json);

        Float deserialized = mapper.readValue(json, Float.class);
        assertEquals(floatNumber, deserialized);
    }

    // Test for DoubleSerializer
    @org.junit.Test
    public void testDoubleSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Double doubleNumber = 4.56;

        String json = mapper.writeValueAsString(doubleNumber);
        assertJson("4.56", json);

        Double deserialized = mapper.readValue(json, Double.class);
        assertEquals(doubleNumber, deserialized);
    }

    private void assertJson(String expectedJson, String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String actualJson = mapper.writeValueAsString(mapper.readValue(json, Object.class));
        assertEquals(expectedJson, actualJson);
    }

    private void assertJson(String expectedJson, Object value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(value);
        assertJson(expectedJson, json);
    }

    private static final Type INT_TYPE = TypeFactory.defaultInstance().constructType(Integer.class);
    private static final Type LONG_TYPE = TypeFactory.defaultInstance().constructType(Long.class);
    private static final Type SHORT_TYPE = TypeFactory.defaultInstance().constructType(Short.class);
    private static final Type FLOAT_TYPE = TypeFactory.defaultInstance().constructType(Float.class);
    private static final Type DOUBLE_TYPE = TypeFactory.defaultInstance().constructType(Double.class);

    @Override
    protected SerializerProvider createSerializerProvider() {
        return new StandardSerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> valueClass, Type serializationType) {
                if (valueClass == Integer.class || valueClass == Integer.TYPE) {
                    return NumberSerializers.IntegerSerializer.instance;
                } else if (valueClass == Long.class || valueClass == Long.TYPE) {
                    return NumberSerializers.LongSerializer.instance;
                } else if (valueClass == Short.class || valueClass == Short.TYPE) {
                    return NumberSerializers.ShortSerializer.instance;
                } else if (valueClass == Float.class || valueClass == Float.TYPE) {
                    return NumberSerializers.FloatSerializer.instance;
                } else if (valueClass == Double.class || valueClass == Double.TYPE) {
                    return NumberSerializers.DoubleSerializer.instance;
                }
                return super.findValueSerializer(valueClass, serializationType);
            }

            @Override
            public JsonSerializer<?> findValueSerializer(Type valueType) {
                if (valueType == INT_TYPE) {
                    return NumberSerializers.IntegerSerializer.instance;
                } else if (valueType == LONG_TYPE) {
                    return NumberSerializers.LongSerializer.instance;
                } else if (valueType == SHORT_TYPE) {
                    return NumberSerializers.ShortSerializer.instance;
                } else if (valueType == FLOAT_TYPE) {
                    return NumberSerializers.FloatSerializer.instance;
                } else if (valueType == DOUBLE_TYPE) {
                    return NumberSerializers.DoubleSerializer.instance;
                }
                return super.findValueSerializer(valueType);
            }
        };
    }
}