import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializerNormalCase() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
        Object testObject = new TestClass("Test");

        String expected = "Test";
        String actual = mapper.writeValueAsString(testObject);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJsonValueSerializerNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
        Object testObject = new TestClass(null);

        String expected = "null";
        String actual = mapper.writeValueAsString(testObject);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJsonValueSerializerExceptionCase() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(String[].class, null);
        Object testObject = new TestClass(new RuntimeException("Test Exception"));

        try {
            mapper.writeValueAsString(testObject);
            Assert.fail("Exception expected");
        } catch (JsonProcessingException e) {
            // Expected
        }
    }

    @Test
    public void testJsonValueSerializerEdgeCase_Enum() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(MyEnum.class, null);
        Object testObject = MyEnum.RED;

        String expected = "RED";
        String actual = mapper.writeValueAsString(testObject);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJsonValueSerializerEdgeCase_ValueSerializable() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(ValueSerializable.class, null);
        Object testObject = new ValueSerializable(42);

        int expected = 42;
        int actual = mapper.readValue(mapper.writeValueAsString(testObject), int.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJsonValueSerializerEdgeCase_NaturalType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper.findValueSerializer(int.class, null);
        Object testObject = 42;

        int expected = 42;
        int actual = mapper.readValue(mapper.writeValueAsString(testObject), int.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJsonValueSerializerContextualization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer1 = mapper.findValueSerializer(Integer[].class, null);
        JsonValueSerializer serializer2 = mapper.findValueSerializer(Integer.class, null);

        TestClass testObject = new TestClass(new Integer[]{1, 2});
        JsonSerializer<?> serializedObject = serializer1.createContextual(mapper.getSerializerProvider(), null);
        Assert.assertSame(serializer1, serializedObject);

        TestClass testObject2 = new TestClass(1);
        JsonSerializer<?> serializedObject2 = serializer2.createContextual(mapper.getSerializerProvider(), null);
        Assert.assertSame(serializer2, serializedObject2);
    }

    static class TestClass {
        private final Object value;

        public TestClass(Object value) {
            this.value = value;
        }

        @JsonValue
        public Object getValue() {
            return value;
        }
    }

    enum MyEnum {
        RED,
        GREEN,
        BLUE
    }

    static class ValueSerializable implements ValueSerializer {
        private final int value;

        public ValueSerializable(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }
}