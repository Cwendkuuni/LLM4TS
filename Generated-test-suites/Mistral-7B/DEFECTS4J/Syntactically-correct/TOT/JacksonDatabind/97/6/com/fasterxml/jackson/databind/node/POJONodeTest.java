package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void testPOJONodeSerialization() throws JsonProcessingException {
    String inputJson = "{\"name\":\"John\",\"age\":30}";
    POJONode node = new POJONode(mapper.readValue(inputJson, Person.class));

    String outputJson = "{\"name\":\"John\",\"age\":30}";

    JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
    node.serialize(gen, new SerializerProvider() {});

    String actualJson = gen.getOutputBuffer().toString();
    assertEquals(outputJson, actualJson);
  }

  @Test
  public void testPOJONodeSerializationWithDefaultValue() throws JsonProcessingException {
    POJONode node = new POJONode(null);

    String expectedJson = "null";

    JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
    node.serialize(gen, new SerializerProvider() {});

    String actualJson = gen.getOutputBuffer().toString();
    assertEquals(expectedJson, actualJson);
  }

  @Test
  public void testPOJONodeSerializationWithJsonSerializableObject() throws JsonProcessingException {
    String inputJson = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
    Person person = new Person("John", 30);
    person.setHobbies(new String[]{"reading", "swimming"});

    POJONode node = new POJONode(person);

    String outputJson = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";

    JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
    node.serialize(gen, new SerializerProvider() {});

    String actualJson = gen.getOutputBuffer().toString();
    assertEquals(outputJson, actualJson);
  }

  @Test
  public void testPOJONodeDeserialization() throws JsonProcessingException {
    String inputJson = "{\"name\":\"John\",\"age\":30}";

    POJONode node = mapper.readValue(inputJson, POJONode.class);
    Person expectedPerson = new Person("John", 30);

    Person actualPerson = (Person) node.getPojo();

    assertEquals(expectedPerson.getName(), actualPerson.getName());
    assertEquals(expectedPerson.getAge(), actualPerson.getAge());
  }

  @Test
  public void testPOJONodeDeserializationWithNullValue() throws JsonProcessingException {
    String inputJson = "null";

    POJONode node = mapper.readValue(inputJson, POJONode.class);
    assertNull(node.getPojo());
  }

  @Test
  public void testPOJONodeDeserializationWithJsonSerializableObject() throws JsonProcessingException {
    String inputJson = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";

    POJONode node = mapper.readValue(inputJson, POJONode.class);
    Person expectedPerson = new Person("John", 30);
    expectedPerson.setHobbies(new String[]{"reading", "swimming"});

    Person actualPerson = (Person) node.getPojo();

    assertEquals(expectedPerson.getName(), actualPerson.getName());
    assertEquals(expectedPerson.getAge(), actualPerson.getAge());
    assertArrayEquals(expectedPerson.getHobbies(), actualPerson.getHobbies());
  }

  static class Person implements JsonSerializable {
    private String name;
    private int age;
    private String[] hobbies;

    public Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }

    public String[] getHobbies() {
      return hobbies;
    }

    public void setHobbies(String[] hobbies) {
      this.hobbies = hobbies;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
      gen.writeStringField("name", name);
      gen.writeNumberField("age", age);
      if (hobbies != null) {
        gen.writeFieldName("hobbies");
        gen.writeStartArray();
        for (String hobby : hobbies) {
          gen.writeString(hobby);
        }
        gen.writeEndArray();
      }
    }
  }
}