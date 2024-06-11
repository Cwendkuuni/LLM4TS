package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = mapper.readValue(JsonFactory.instance.createParser("[]"), String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeArrayWithSingleString() throws IOException {
 String[] result = mapper.readValue(JsonFactory.instance.createParser("[\"" + "hello" + "\"]"), String[].class);
 assertArrayEquals(new String[] {"hello"}, result);
 }

 @Test
 public void testDeserializeArrayWithCustomDeserializer() throws IOException {
 String[] result = mapper.readValue(JsonFactory.instance.createParser("[\"" + "hello" + "\", null, \"world\"]"), String[].class, new TestCustomDeserializer());
 assertArrayEquals(new String[] {"hello", null, "world"}, result);
 }

 @Test
 public void testDeserializeArrayWithNullValue() throws IOException {
 String[] result = mapper.readValue(JsonFactory.instance.createParser("[\"" + "hello" + "\", null]"), String[].class);
 assertArrayEquals(new String[] {"hello", null}, result);
 }

 @Test
 public void testDeserializeArrayWithEmptyString() throws IOException {
 String[] result = mapper.readValue(JsonFactory.instance.createParser("[\"" + "" + "\"]"), String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testDeserializeArrayWithInvalidJson() throws IOException {
 String json = "{\"abc\": [1, 2]}"; // invalid JSON for String array deserialization
 assertThrows(JsonMappingException.class, () -> mapper.readValue(JsonFactory.instance.createParser(json), String[].class));
 }

 @Test
 public void testDeserializeArrayWithCustomContentConverter() throws IOException {
 String[] result = mapper.readValue(JsonFactory.instance.createParser("[\", 1, 2, 3]"), String[].class, new TestCustomContentConverter());
 assertArrayEquals(new String[] {"", "", "", ""}, result);
 }

 @Test
 public void testDeserializeCustomContextual() throws IOException {
 String[] result = mapper.readValue(JsonFactory.instance.createParser("{\"foo\": [\"bar\", \"baz\"]}"), String[].class, new TestCustomContextualDeserializer());
 assertArrayEquals(new String[] {"bar", "baz"}, result);
 }

 private static class TestCustomDeserializer extends JsonDeserializer<String[]> {
 @Override
 public String[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 String[] result = super.deserialize(p, ctxt);
 return new String[] {result[0], null, result[result.length - 1]};
 }
 }

 private static class TestCustomContentConverter extends StdSerializer<String> implements ContentConverter {
 @Override
 public void serialize(SerializerProvider sp, Object o, JsonGenerator jg, SerializerProvider.NullProvider np) throws IOException {
 jg.writeString("");
 }

 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 }

 private static class TestCustomContextualDeserializer extends JsonDeserializer<String[]> {
 @Override
 public boolean isCached() {
 return false;
 }

 @Override
 public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
 return new StringArrayDeserializer(new TestCustomDeserializer());
 }
 }

}