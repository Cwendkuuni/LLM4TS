package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSimplePojoSerialization() throws Exception {
        POJONode node = new POJONode(new Person("John Doe"));
        JsonNode jsonNode = mapper.writeValueAsTree(node);
        assertEquals("{\"name\":\"John Doe\"}", jsonNode.asText());
    }

    @Test
    public void testNullValueSerialization() throws Exception {
        POJONode node = new POJONode(null);
        JsonNode jsonNode = mapper.writeValueAsTree(node);
        assertEquals("null", jsonNode.asText());
    }

    @Test
    public void testByteArraySerialization() throws Exception {
        byte[] data = {1, 2, 3};
        POJONode node = new POJONode(data);
        JsonNode jsonNode = mapper.writeValueAsTree(node);
        assertEquals("(binary value of 3 bytes)", jsonNode.asText());
    }

    @Test
    public void testSerializationOfJsonSerializableClass() throws Exception {
        MySerializableClass obj = new MySerializableClass("Hello World");
        POJONode node = new POJONode(obj);
        JsonNode jsonNode = mapper.writeValueAsTree(node);
        assertEquals("{\"message\":\"Hello World\"}", jsonNode.asText());
    }

    @Test(expected = IOException.class)
    public void testSerializationFailure() throws Exception {
        POJONode node = new POJONode(new IOException("Test error"));
        mapper.writeValueAsTree(node); // Should throw exception
    }

    @Test
    public void testDeserialization() throws Exception {
        String json = "{\"name\":\"John Doe\"}";
        JsonNode jsonNode = mapper.readTree(json);
        POJONode node = mapper.treeToValue(jsonNode, POJONode.class);
        assertEquals("John Doe", node.getPojo().toString());
    }

    @Test
    public void testDeserializationOfJsonSerializableClass() throws Exception {
        String json = "{\"message\":\"Hello World\"}";
        JsonNode jsonNode = mapper.readTree(json);
        POJONode node = mapper.treeToValue(jsonNode, POJONode.class);
        MySerializableClass obj = (MySerializableClass) node.getPojo();
        assertEquals("Hello World", obj.getMessage());
    }

    @Test
    public void testEquals() {
        Person person1 = new Person("John Doe");
        Person person2 = new Person("John Doe");
        POJONode node1 = new POJONode(person1);
        POJONode node2 = new POJONode(person2);
        POJONode node3 = new POJONode(new Object());

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        Person person = new Person("John Doe");
        POJONode node1 = new POJONode(person);
        POJONode node2 = new POJONode(person);
        POJONode node3 = new POJONode(new Object());

        assertEquals(node1.hashCode(), node2.hashCode());
        assertNotEquals(node1.hashCode(), node3.hashCode());
    }
}

class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}

class MySerializableClass implements JsonSerializable {
    private final String message;

    public MySerializableClass(String message) {
        this.message = message;
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, JsonSerializerContext jsonSerializerContext)
            throws IOException {
        jsonGenerator.writeObjectFieldStart("message");
        jsonGenerator.writeStringField("message", message);
        jsonGenerator.writeEndObject();
    }

    public String getMessage() {
        return message;
    }
}