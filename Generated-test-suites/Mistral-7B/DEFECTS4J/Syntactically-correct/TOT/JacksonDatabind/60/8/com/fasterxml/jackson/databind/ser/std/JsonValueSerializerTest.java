package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

public class JsonValueSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonValueSerializer serializer;
    private Object testBean;
    private Method testMethod;

    @BeforeMethod
    public void setUp() throws Exception {
        Class<?> testClass = TestClass.class;
        testMethod = testClass.getMethod("getValue");
        testBean = testClass.newInstance();
        serializer = new JsonValueSerializer(testMethod, null);
    }

    @Test
    public void testNullValue() throws JsonMappingException, JsonProcessingException {
        ObjectNode result = MAPPER.writeValueAsTree(serializer, null);
        assertEquals(result.toString(), "null");
    }

    @Test
    public void testEmptyStringValue() throws JsonMappingException, JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestStringField("");

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass);
        assertEquals(result.toString(), "\"\"");
    }

    @Test
    public void testSimpleStringValue() throws JsonMappingException, JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestStringField("Test value");

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass);
        assertEquals(result.toString(), "\"Test value\"");
    }

    @Test
    public void testIntegerValue() throws JsonMappingException, JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestIntField(42);

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass);
        assertEquals(result.toString(), "42");
    }

    @Test
    public void testBooleanValue() throws JsonMappingException, JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestBooleanField(true);

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass);
        assertEquals(result.toString(), "true");
    }

    @Test
    public void testDoubleValue() throws JsonMappingException, JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestDoubleField(3.14);

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass);
        assertEquals(result.toString(), "3.14");
    }

    @Test
    public void testEnumValue() throws JsonMappingException, JsonProcessingException, IOException {
        TestClass testClass = new TestClass();
        testClass.setTestEnumField(TestEnum.VALUE1);

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass);
        assertEquals(result.toString(), "\"VALUE1\"");
    }

    @Test
    public void testCustomValueSerializer() throws JsonMappingException, JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestCustomField(new CustomClass());

        JsonSerializer<CustomClass> customSerializer = new CustomSerializer();
        SerializerProvider provider = new DefaultSerializerProvider();
        provider.setSerializerInstance(CustomClass.class, customSerializer);

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass, provider);
        assertEquals(result.toString(), "{\"customField\":\"customSerializer\"}");
    }

    @Test(expectedExceptions = JsonMappingException.class, expectedExceptionsMessageRegExp = ".*Caused by: (.*No serializer found for class.*|.*No serializers found for value.*|.*Cannot deserialize value of type.*|.*Class cast exception)")
    public void testUnknownValueType() throws JsonMappingException, JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestUnknownField(new UnknownClass());

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass);
    }

    @Test
    public void testContextualSerializer() throws JsonMappingException, JsonProcessingException {
        TestContextual testClass = new TestContextual();
        testClass.setTestStringField("Test value");

        ObjectNode result = MAPPER.writeValueAsTree(serializer, testClass);
        assertEquals(result.toString(), "\"Test value\"");

        TestContextual testClass2 = new TestContextual();
        testClass2.setTestIntField(42);

        JsonSerializer<Integer> intSerializer = new JsonSerializer<Integer>() {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeNumber(value.intValue());
            }
        };

        SerializerProvider provider = new DefaultSerializerProvider();
        provider.setSerializerInstance(Integer.class, intSerializer);

        ObjectNode result2 = MAPPER.writeValueAsTree(serializer, testClass2, provider);
        assertEquals(result2.toString(), "42");

        TestContextual testClass3 = new TestContextual();
        testClass3.setTestCustomField(new CustomClass());

        JsonSerializer<CustomClass> customSerializer = new CustomSerializer();
        provider.setSerializerInstance(CustomClass.class, customSerializer);

        ObjectNode result3 = MAPPER.writeValueAsTree(serializer, testClass3, provider);
        assertEquals(result3.toString(), "{\"customField\":\"customSerializer\"}");
    }

    @Test
    public void testJsonFormatVisitor() throws JsonMappingException, JsonProcessingException {
        TestClass testClass = new TestClass();
        testClass.setTestStringField("Test value");

        ObjectMapper mapper = new ObjectMapper();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("testStringField");
        mapper.setSerializerFactory(new JsonSerializerFactoryWrapper(filter));

        JsonNode schema = mapper.findSchema(serializer.getClass());
        assertEquals(schema.getClass(), ObjectNode.class);
        assertEquals(schema.toString(), "{\"properties\":{\"testStringField\":{\"type\":\"string\"}}");
    }

    static class TestClass {
        private String testStringField;
        private int testIntField;
        private boolean testBooleanField;
        private double testDoubleField;
        private TestEnum testEnumField;
        private CustomClass testCustomField;
        private UnknownClass testUnknownField;

        public void setTestStringField(String value) {
            this.testStringField = value;
        }

        public void setTestIntField(int value) {
            this.testIntField = value;
        }

        public void setTestBooleanField(boolean value) {
            this.testBooleanField = value;
        }

        public void setTestDoubleField(double value) {
            this.testDoubleField = value;
        }

        public void setTestEnumField(TestEnum value) {
            this.testEnumField = value;
        }

        public void setTestCustomField(CustomClass value) {
            this.testCustomField = value;
        }

        public void setTestUnknownField(UnknownClass value) {
            this.testUnknownField = value;
        }

        public String getValue() throws JsonMappingException, JsonProcessingException {
            return (String) testMethod.invoke(this);
        }
    }

    static enum TestEnum {
        VALUE1, VALUE2
    }

    static class CustomClass {}

    static class UnknownClass {}

    static class CustomSerializer extends JsonSerializer<CustomClass> {
        @Override
        public void serialize(CustomClass value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString("customSerializer");
        }
    }

    static class JsonSerializerFactoryWrapper extends JsonSerializerFactory {
        private final SimpleBeanPropertyFilter filter;

        public JsonSerializerFactoryWrapper(SimpleBeanPropertyFilter filter) {
            this.filter = filter;
        }

        @Override
        public JsonSerializer<?> findSerializer(Class<?> type, JsonSerializer<?> serializer) {
            return filter.filterOutAndFindSerializer(type, serializer);
        }
    }
}