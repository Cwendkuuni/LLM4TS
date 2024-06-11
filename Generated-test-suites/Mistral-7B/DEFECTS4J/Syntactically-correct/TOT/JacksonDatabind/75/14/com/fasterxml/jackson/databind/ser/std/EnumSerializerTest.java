package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private EnumDeserializer deserializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 MyEnum enumInstance = MyEnum.RED;
 mapper = new ObjectMapper();
 serializer = mapper.findValueSerializer(MyEnum.class);
 deserializer = mapper.findValueDeserializer(MyEnum.class);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(enumInstance, generator, null);
 String json = generator.getOutputContent();
 assertEquals("\"RED\"", json);
 }

 @Test
 public void testSerializeAsIndex() throws JsonProcessingException {
 ObjectMapper mapperWithIndex = new ObjectMapper();
 mapperWithIndex.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
 JsonGenerator generatorWithIndex = mapperWithIndex.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = mapperWithIndex.findValueSerializer(MyEnum.class);
 serializer.serialize(enumInstance, generatorWithIndex, null);
 String jsonWithIndex = generatorWithIndex.getOutputContent();
 assertEquals("4", jsonWithIndex); // assuming enum constants are ordered
 }

 @Test
 public void testSerializeToString() throws JsonProcessingException {
 ObjectMapper mapperToString = new ObjectMapper();
 mapperToString.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
 JsonGenerator generatorToString = mapperToString.getFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer = mapperToString.findValueSerializer(MyEnum.class);
 serializer.serialize(enumInstance, generatorToString, null);
 String jsonToString = generatorToString.getOutputContent();
 assertEquals("RED", jsonToString);
 }

 @Test
 public void testDeserialize() throws JsonProcessingException {
 ObjectNode node = mapper.readValue(new ByteArrayInputStream(MySerializerTest.class.getResourceAsStream("/MyEnumTestData.json")), ObjectNode.class);
 ObjectNode enumNode = node.get("enum");
 JsonParser parser = mapper.getFactory().createParser(enumNode);
 Object readValue = deserializer.deserialize(parser, EnumSet.noneOf(MyEnum.class));
 assertEquals(MyEnum.RED, readValue);
 }

 @Test
 public void testDeserializeAsIndex() throws JsonProcessingException {
 ObjectMapper mapperWithIndex = new ObjectMapper();
 mapperWithIndex.configure(DeserializationFeature.READ_ENUMS_USING_INDEX, true);
 ObjectNode nodeWithIndex = mapperWithIndex.readValue(new ByteArrayInputStream(MySerializerTest.class.getResourceAsStream("/MyEnumTestDataIndex.json")), ObjectNode.class);
 ObjectNode enumNodeWithIndex = nodeWithIndex.get("enum");
 JsonParser parserWithIndex = mapperWithIndex.getFactory().createParser(enumNodeWithIndex);
 Object readValueAsIndex = deserializer.deserialize(parserWithIndex, EnumSet.noneOf(MyEnum.class));
 assertEquals(MyEnum.GREEN, readValueAsIndex); // assuming enum constants are ordered
 }

 @Test(expected = IOException.class)
 public void testDeserializeFail() throws JsonProcessingException {
 ObjectMapper mapperFail = new ObjectMapper();
 ObjectNode nodeFail = mapperFail.readValue(new ByteArrayInputStream(MySerializerTest.class.getResourceAsStream("/MyEnumTestDataFail.json")), ObjectNode.class);
 ObjectNode enumNodeFail = nodeFail.get("enum");
 JsonParser parserFail = mapperFail.getFactory().createParser(enumNodeFail);
 deserializer.deserializepartial(parserFail, null);
 }
}