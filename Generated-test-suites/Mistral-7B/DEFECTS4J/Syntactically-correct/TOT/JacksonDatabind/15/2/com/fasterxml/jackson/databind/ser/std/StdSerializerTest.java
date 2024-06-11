import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdSerializerTest {

    private ObjectMapper mapper;
    private StdSerializer<MyClass> serializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        serializer = mapper.findValueSerializer(MyClass.class);
    }

    @Test
    public void testSerializingEmptyObject() throws IOException {
        MyClass obj = new MyClass();
        String json = serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        assertEquals("{}", json);
    }

    @Test
    public void testSerializingObjectWithValues() throws IOException {
        MyClass obj = new MyClass("John Doe", 30);
        String json = serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        ObjectMapper mapper = new ObjectMapper();
        MyClass deserializedObj = mapper.readValue(json, MyClass.class);
        assertEquals(obj.getName(), deserializedObj.getName());
        assertEquals(obj.getAge(), deserializedObj.getAge());
    }

    @Test
    public void testSerializingJsonNode() throws IOException {
        MyClass obj = new MyClass(null, new ObjectNode(JsonNodeFactory.instance));
        String json = serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        ObjectMapper mapper = new ObjectMapper();
        MyClass deserializedObj = mapper.readValue(json, MyClass.class);
        assertTrue(deserializedObj.getNode() != null);
    }

    @Test
    public void testSerializingList() throws IOException {
        MyClass obj = new MyClass(null, Arrays.asList("Alice", "Bob", "Charlie"));
        String json = serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        ObjectMapper mapper = new ObjectMapper();
        MyClass deserializedObj = mapper.readValue(json, MyClass.class);
        List<String> list = deserializedObj.getList();
        assertEquals(3, list.size());
        assertEquals("Alice", list.get(0));
        assertEquals("Bob", list.get(1));
        assertEquals("Charlie", list.get(2));
    }

    @Test
    public void testSerializingMap() throws IOException {
        MyClass obj = new MyClass(null, new ObjectNode(JsonNodeFactory.instance).put("key1", "value1").put("key2", "value2"));
        String json = serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        ObjectMapper mapper = new ObjectMapper();
        MyClass deserializedObj = mapper.readValue(json, MyClass.class);
        Assert.assertNotNull(deserializedObj.getMap());
        assertEquals("value1", deserializedObj.getMap().get("key1").asText());
        assertEquals("value2", deserializedObj.getMap().get("key2").asText());
    }

    @Test
    public void testSerializingCustomJsonSerializer() throws IOException {
        @JsonSerialize(using = CustomSerializer.class)
        static class CustomMyClass extends MyClass {}

        CustomMyClass obj = new CustomMyClass("Custom John Doe", 30);
        String json = serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        ObjectMapper mapper = new ObjectMapper();
        CustomMyClass deserializedObj = mapper.readValue(json, CustomMyClass.class);
        assertEquals(obj.getName(), deserializedObj.getName());
        assertEquals(obj.getAge(), deserializedObj.getAge());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingNullObject() throws IOException {
        MyClass obj = null;
        serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingObjectWithNullName() throws IOException {
        MyClass obj = new MyClass(null, new ObjectNode(JsonNodeFactory.instance));
        serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingObjectWithNegativeAge() throws IOException {
        MyClass obj = new MyClass("John Doe", -1);
        serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializingObjectWithInvalidJsonNode() throws IOException {
        MyClass obj = new MyClass(null, new ObjectNode(JsonNodeFactory.instance).put("invalid", 123));
        serializer.serializeValue(obj, new JsonGenerator(new ByteArrayOutputStream()), new SerializerProvider());
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializingEmptyObject() throws IOException {
        String json = "{}";
        MyClass obj = mapper.readValue(json, MyClass.class);
        assertNull(obj);
    }

    @Test
    public void testDeserializingObjectWithValues() throws IOException {
        String json = "{\"name\":\"John Doe\",\"age\":30}";
        MyClass obj = mapper.readValue(json, MyClass.class);
        assertEquals("John Doe", obj.getName());
        assertEquals(30, obj.getAge());
    }

    @Test
    public void testDeserializingObjectWithJsonNode() throws IOException {
        String json = "{" +
                "\"name\":null," +
                "\"node\":{" +
                "\"key1\":\"value1\"," +
                "\"key2\":\"value2\"" +
                "}" +
                "}";
        MyClass obj = mapper.readValue(json, MyClass.class);
        assertNull(obj.getName());
        ObjectNode node = (ObjectNode) obj.getNode();
        assertEquals("value1", node.get("key1").asText());
        assertEquals("value2", node.get("key2").asText());
    }

    @Test
    public void testDeserializingList() throws IOException {
        String json = "[\"Alice\",\"Bob\",\"Charlie\"]";
        MyClass obj = mapper.readValue(json, MyClass.class);
        List<String> list = obj.getList();
        assertEquals(3, list.size());
        assertEquals("Alice", list.get(0));
        assertEquals("Bob", list.get(1));
        assertEquals("Charlie", list.get(2));
    }

    @Test
    public void testDeserializingMap() throws IOException {
        String json = "{" +
                "\"name\":null," +
                "\"map\":{" +
                "\"key1\":\"value1\"," +
                "\"key2\":\"value2\"" +
                "}" +
                "}";
        MyClass obj = mapper.readValue(json, MyClass.class);
        ObjectNode map = (ObjectNode) obj.getMap();
        assertEquals("value1", map.get("key1").asText());
        assertEquals("value2", map.get("key2").asText());
    }

    @Test
    public void testDeserializingCustomJsonSerializer() throws IOException {
        String json = "{\"customName\":\"Custom John Doe\",\"age\":30}";
        @JsonSerialize(using = CustomSerializer.class)
        static class CustomMyClass extends MyClass {}

        CustomMyClass obj = mapper.readValue(json, CustomMyClass.class);
        assertEquals("Custom John Doe", obj.getName());
        assertEquals(30, obj.getAge());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializingNullJson() throws IOException {
        String json = null;
        MyClass obj = mapper.readValue(json, MyClass.class);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializingObjectWithInvalidName() throws IOException {
        String json = "{\"age\":30}";
        MyClass obj = mapper.readValue(json, MyClass.class);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializingObjectWithNegativeAge() throws IOException {
        String json = "{\"name\":\"John Doe\",\"age\":-1}";
        MyClass obj = mapper.readValue(json, MyClass.class);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testDeserializingObjectWithInvalidJsonNode() throws IOException {
        String json = "{" +
                "\"name\":null," +
                "\"node\":{" +
                "\"key1\":123," +
                "\"key2\":\"value2\"" +
                "}" +
                "}";
        MyClass obj = mapper.readValue(json, MyClass.class);
        fail("Expected JsonMappingException");
    }
}

class MyClass {
    private String name;
    private ObjectNode node;
    private List<String> list;
    private Map<String, Object> map;

    public MyClass(String name, ObjectNode node) {
        this.name = name;
        this.node = node;
    }

    public MyClass(String name, List<String> list) {
        this.name = name;
        this.list = list;
    }

    public MyClass(String name, Map<String, Object> map) {
        this.name = name;
        this.map = map;
    }

    public MyClass(String name, Object node) {
        this.name = name;
        this.node = (ObjectNode) node;
    }

    public String getName() {
        return name;
    }

    public ObjectNode getNode() {
        return node;
    }

    public List<String> getList() {
        return (list != null) ? list : Collections.emptyList();
    }

    public Map<String, Object> getMap() {
        return (map != null) ? map : Collections.emptyMap();
    }
}

class CustomSerializer extends StdSerializer<Object> {
    public CustomSerializer() { this(null); }

    public CustomSerializer(Class<?> t, boolean dummy) { super(t, dummy); }

    @Override
    public void serializeValue(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value instanceof ObjectNode) {
            jgen.writeObject(((ObjectNode) value).toString());
        } else {
            super.serializeValue(value, jgen, provider);
        }
    }
}