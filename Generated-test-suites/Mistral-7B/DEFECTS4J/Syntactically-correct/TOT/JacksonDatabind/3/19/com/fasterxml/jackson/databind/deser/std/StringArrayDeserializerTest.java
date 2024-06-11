package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertArrayEquals;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class StringArrayDeserializerTest extends BaseMapTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDefaultDeserializer() throws JsonProcessingException {
 String[] expected = new String[]{"one", "two", "three"};
 String[] actual = mapper.readValue("[\"one\", \"two\", \"three\"]", StringArrayDeserializer.class);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testCustomDeserializer() throws JsonProcessingException {
 String[] expected = new String[]{"one", "null", "three"};
 JsonDeserializer<String> stringDeserializer = new StdScalarDeserializer(String.class);
 StringArrayDeserializer serializer = new StringArrayDeserializer(stringDeserializer);
 String[] actual = mapper.readValue("[\"one\", null, \"three\"]", String[].class, serializer);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testNullDeserializer() throws JsonProcessingException {
 String[] expected = new String[0];
 StringArrayDeserializer serializer = new StringArrayDeserializer();
 String[] actual = mapper.readValue("[null]", String[].class, serializer);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testEmptyDeserializer() throws JsonProcessingException {
 String[] expected = new String[0];
 String[] actual = mapper.readValue("[]", String[].class, StringArrayDeserializer.instance);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testInvalidJson() throws JsonProcessingException {
 JsonParser jsonParser = mapper.getFactory().createParser("[invalidJson");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(jsonParser, new TestContext());
 assertNull(result);
 }

 @Test
 public void testNullElementDeserializer() throws JsonProcessingException {
 String[] expected = new String[]{"one", null, "three"};
 StringArrayDeserializer serializer = new StringArrayDeserializer((JsonDeserializer<String>)null);
 String[] actual = mapper.readValue("[\"one\", null, \"three\"]", String[].class, serializer);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testEmptyStringDeserializer() throws JsonProcessingException {
 mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 String[] expected = new String[0];
 StringArrayDeserializer serializer = new StringArrayDeserializer();
 String[] actual = mapper.readValue("[\"\"]", String[].class, serializer);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testEmptyStringNullDeserializer() throws JsonProcessingException {
 mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 String[] expected = new String[0];
 StringArrayDeserializer serializer = new StringArrayDeserializer((JsonDeserializer<String>)null);
 String[] actual = mapper.readValue("[\"\"]", String[].class, serializer);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testInvalidArrayIndex() throws JsonProcessingException {
 String[] expected = new String[2];
 expected[1] = "two";
 String[] actual = mapper.readValue("[\"one\", 1]", String[].class, StringArrayDeserializer.instance);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testEmptyJsonArray() throws JsonProcessingException {
 String[] expected = new String[0];
 String[] actual = mapper.readValue("[]", String[].class, StringArrayDeserializer.instance);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testArrayWithNonStringElement() throws JsonProcessingException {
 String[] expected = new String[1];
 expected[0] = "one";
 int[] numbers = {1, 2, 3};
 String[] actual = mapper.readValue("[1, \"one\", 3]", String[].class, StringArrayDeserializer.instance);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testArrayWithNullElement() throws JsonProcessingException {
 String[] expected = new String[2];
 expected[0] = "one";
 expected[1] = null;
 String[] actual = mapper.readValue("[\"one\", null]", String[].class, StringArrayDeserializer.instance);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testArrayWithEmptyStringElement() throws JsonProcessingException {
 String[] expected = new String[2];
 expected[0] = "one";
 expected[1] = "";
 String[] actual = mapper.readValue("[\"one\", \"\"]", String[].class, StringArrayDeserializer.instance);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testArrayWithObjectElements() throws JsonProcessingException {
 String[] expected = new String[2];
 expected[0] = "one";
 expected[1] = "{\"name\": \"John\", \"age\": 30}";
 String[] actual = mapper.readValue("[\"one\", {\"name\": \"John\", \"age\": 30}]", String[].class, StringArrayDeserializer.instance);
 String result = mapper.readValue("{\"name\": \"John\", \"age\": 30}", String.class);
 assertArrayEquals(expected, actual);
 assertEquals(result, "{\"name\": \"John\", \"age\": 30}");
 }

 @Test
 public void testArrayWithObjectElementArray() throws JsonProcessingException {
 String[] expected = new String[2];
 expected[0] = "one";
 Object[] array = {1, 2, 3};
 expected[1] = "[1, 2, 3]";
 String[] actual = mapper.readValue("[\"one\", [\".\", 1, 2, 3\"]]", String[].class, StringArrayDeserializer.instance);
 String result = mapper.readValue("[1, 2, 3]", Object[].class, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT_TYPE).toString();
 assertArrayEquals(expected, actual);
 assertEquals(result, "[1, 2, 3]");
 }

 @Test
 public void testArrayWithNestedObjectArrays() throws JsonProcessingException {
 String[] expected = new String[2];
 expected[0] = "one";
 Object[] array1 = {1, 2, 3};
 Object[] array2 = {1, 2, 3, 4, 5};
 expected[1] = "[[1, 2, 3], [1, 2, 3, 4, 5]]";
 String[] actual = mapper.readValue("[\"one\", [[\".\", 1, 2, 3], [\"\", 1, 2, 3, 4, 5]]]", String[].class, StringArrayDeserializer.instance);
 String result1 = mapper.readValue("[1, 2, 3]", Object[].class, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT_TYPE).toString();
 String result2 = mapper.readValue("[1, 2, 3, 4, 5]", Object[].class, ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT_TYPE).toString();
 assertArrayEquals(expected, actual);
 assertEquals(result1, "[1, 2, 3]");
 assertEquals(result2, "[1, 2, 3, 4, 5]");
 }

 @Test
 public void testArrayWithNestedObjects() throws JsonProcessingException {
 String[] expected = new String[2];
 expected[0] = "one";
 Object obj = new Object() {{ setName("John"); setAge(30); }};
 expected[1] = "{\"name\": \"John\", \"age\": 30}";
 String[] actual = mapper.readValue("[\"one\", {" + mapper.writeValueAsString(obj) + "}]", String[].class, StringArrayDeserializer.instance);
 String result = mapper.readValue("{\"name\": \"John\", \"age\": 30}", String.class);
 assertArrayEquals(expected, actual);
 assertEquals(result, "{\"name\": \"John\", \"age\": 30}");
 }

 public static class TestContext extends DeserializationContext {
 @Override
 public ObjectBuffer leakObjects() {
 return null;
 }

 @Override
 public void returnObjectBuffer(ObjectBuffer buffer) {
 // No-op
 }
 }
}