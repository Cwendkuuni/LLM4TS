package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private ObjectMapper mapper;
 private JsonParser parser;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 try {
 parser = mapper.getFactory().createParser("{\"type\":\"com.test.Person\", \"name\":\"John\", \"age\":30}");
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(Person.class),
 mapper.getTypeIdResolver(), "_type", true, Person.class);
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertEquals(new Person("John", 30), result);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 parser.nextToken(); // consume {"type": "com.test.Person"}
 parser.nextToken(); // consume ": "
 parser.nextToken(); // consume "com.test.Person"
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertEquals(new Person("John", 30), result);
 }

 @Test(expected = IOException.class)
 public void testMissingTypeId() throws Exception {
 parser.nextToken(); // consume "{"
 JsonDeserializer<Person> deserializer = mapper.findValueDeserializer(Person.class);
 Object result = deserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected exception: WrongTokenException");
 }

 @Test
 public void testMissingEndObject() throws Exception {
 parser.nextToken(); // consume "{"
 parser.nextToken(); // consume "name"
 parser.nextToken(); // consume "John"
 parser.nextToken(); // consume ": "
 parser.nextToken(); // consume "30"
 parser.nextToken(); // consume ","
 fail("Expected exception: WrongTokenException");
 }

 @Test
 public void testTypeIdVisible() throws Exception {
 parser.nextToken(); // consume "{"
 parser.nextToken(); // consume "type"
 parser.nextToken(); // consume ": "
 parser.nextToken(); // consume "com.test.Person"
 parser.nextToken(); // consume ","
 parser.nextToken(); // consume "_type"
 parser.nextToken(); // consume ": "
 parser.nextToken(); // consume "PersonType"
 parser.nextToken(); // consume ","
 Object result = deserializer.deserializeTypedFromObject(parser, mapper.getDeserializationContext());
 assertEquals(new Person("John", 30), result);
 }

 @Test(expected = IOException.class)
 public void testMissingTypeIdVisible() throws Exception {
 parser.nextToken(); // consume "{"
 parser.nextToken(); // consume "type"
 parser.nextToken(); // consume ": "
 parser.nextToken(); // consume "com.test.Person"
 parser.nextToken(); // consume ","
 fail("Expected exception: WrongTokenException");
 }
}

class Person {
 private final String name;
 private final int age;

 public Person(String name, int age) {
 this.name = name;
 this.age = age;
 }

 @Override
 public String toString() {
 return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
 }
}