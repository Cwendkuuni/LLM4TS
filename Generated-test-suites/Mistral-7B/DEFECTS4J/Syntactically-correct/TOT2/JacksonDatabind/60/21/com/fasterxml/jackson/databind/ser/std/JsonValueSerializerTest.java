import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setUp() throws InstantiationException, IllegalAccessException {
        objectMapper = new ObjectMapper();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                objectMapper.findMethodValueDeserializer(TestClass.class, "getValue"), null);
        this.jsonValueSerializer = jsonValueSerializer;
    }

    @Test
    public void testSerializationOfSimpleTypes() throws IOException {
        TestClass testClass = new TestClass();
        String expectedJson = "\"expectedValue\"";
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        jsonValueSerializer.serialize(testClass, jsonGenerator, objectMapper.getSerializationConfig());
        String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializationOfCustomType() throws IOException {
        TestClassWithCustomType testClass = new TestClassWithCustomType();
        String expectedJson = "{\"myCustomType\":1}";
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        jsonValueSerializer.serialize(testClass, jsonGenerator, objectMapper.getSerializationConfig());
        String actualJson = jsonGenerator.getOutputFactory().writeValueAsString(jsonGenerator);
        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNullValue() throws IOException {
        TestClass testClass = new TestClass();
        jsonValueSerializer.serialize(testClass, null, objectMapper.getSerializationConfig());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithExceptionInValueMethod() throws IOException, InvocationTargetException, IllegalAccessException {
        TestClassWithExceptionInValueMethod testClass = new TestClassWithExceptionInValueMethod();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        jsonValueSerializer.serialize(testClass, jsonGenerator, objectMapper.getSerializationConfig());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithExceptionInAccessorMethod() throws IOException, InvocationTargetException, IllegalAccessException {
        TestClassWithExceptionInAccessorMethod testClass = new TestClassWithExceptionInAccessorMethod();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        jsonValueSerializer.serialize(testClass, jsonGenerator, objectMapper.getSerializationConfig());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInfiniteRecursion() throws IOException, InvocationTargetException, IllegalAccessException {
        TestClassWithInfiniteRecursion testClass = new TestClassWithInfiniteRecursion();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        jsonValueSerializer.serialize(testClass, jsonGenerator, objectMapper.getSerializationConfig());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNullAccessorMethod() throws IOException, InstantiationException, IllegalAccessException {
        TestClassWithoutAccessorMethod testClass = new TestClassWithoutAccessorMethod();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
        jsonValueSerializer.serialize(testClass, jsonGenerator, objectMapper.getSerializationConfig());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNonSerializableValue() throws IOException {
        TestClassWithNonSerializableValue testClass = new TestClassWithNonSerializableValue();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator();
        jsonValueSerializer.serialize(testClass, jsonGenerator, objectMapper.getSerializationConfig());
    }

    @Test(expected = IllegalStateException.class)
    public void testSerializationWithNullSerializer() throws IOException {
        TestClass testClass = new TestClass();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
        jsonValueSerializer.serialize(testClass, null, objectMapper.getSerializationConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializationWithInvalidType() throws IOException {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, null, null, false);
        jsonValueSerializer.serialize(new Object(), null, objectMapper.getSerializationConfig());
    }

    @Test
    public void testDeserializationOfSimpleTypes() throws IOException {
        String json = "{\"getValue\":\"expectedValue\"}";
        TestClass testClass = objectMapper.readValue(json, TestClass.class);
        String actualValue = testClass.getValue();
        String expectedValue = "expectedValue";
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testDeserializationOfCustomType() throws IOException {
        String json = "{\"myCustomType\":1}";
        TestClassWithCustomType testClass = objectMapper.readValue(json, TestClassWithCustomType.class);
        int actualValue = testClass.getValue();
        int expectedValue = 1;
        Assert.assertEquals(expectedValue, actualValue);
    }

    private static class TestClass {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static class TestClassWithCustomType {
        private int myCustomType;

        public int getValue() {
            return myCustomType;
        }

        public void setValue(int myCustomType) {
            this.myCustomType = myCustomType;
        }
    }

    private static class TestClassWithExceptionInValueMethod {
        private int value;

        @JsonValue
        public String getValueWithException() throws IOException {
            throw new IOException();
        }
    }

    private static class TestClassWithExceptionInAccessorMethod {
        private int value;

        public int getValueWithException() throws IOException {
            throw new IOException();
        }

        public int getValue() {
            return getValueWithException();
        }
    }

    private static class TestClassWithInfiniteRecursion {
        private TestClassWithInfiniteRecursion testClassWithInfiniteRecursion;

        public TestClassWithInfiniteRecursion getValue() {
            return testClassWithInfiniteRecursion;
        }

        public void setValue(TestClassWithInfiniteRecursion testClassWithInfiniteRecursion) {
            this.testClassWithInfiniteRecursion = testClassWithInfiniteRecursion;
        }
    }

    private static class TestClassWithoutAccessorMethod {
        private Object value;

//        public String getValue() {
//            return (String) value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
    }

    private static class TestClassWithNonSerializableValue {
        private NonSerializableValue nonSerializableValue;

        public NonSerializableValue getValue() {
            return nonSerializableValue;
        }

        public void setValue(NonSerializableValue nonSerializableValue) {
            this.nonSerializableValue = nonSerializableValue;
        }
    }

    private static class NonSerializableValue {
        // Non-serializable class
    }
}