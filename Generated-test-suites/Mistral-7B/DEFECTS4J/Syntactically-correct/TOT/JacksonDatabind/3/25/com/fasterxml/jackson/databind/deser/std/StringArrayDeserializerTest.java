package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("[]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 assertArrayEqual(new String[0], result);
 }

 @Test
 public void testDeserializeSingleString() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("[\"" + "hello" + "\"]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 assertArrayEqual(new String[]{"hello"}, result);
 }

 @Test
 public void testDeserializeStringArray() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("[\"" + "hello" + "\", \"" + "world" + "\"]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 String[] result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 assertArrayEqual(new String[]{"hello", "world"}, result);
 }

 @Test
 public void testDeserializeWithCustomDeserializer() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("[null, 123]");
 StringArrayDeserializer deserializer = new StringArrayDeserializer(mapper.getFactory().createNullValueDeserializer());
 String[] result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 assertArrayEqual(new String[]{"null", "123"}, result);
 }

 @Test
 public void testDeserializeWithConvertingContentDeserializer() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("[1, 2, 3]");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 ObjectBuffer buffer = mapper.getDeserializationContext().leaseObjectBuffer();
 Object[] chunk = buffer.resetAndStart();
 JsonDeserializer<Integer> intDeserializer = mapper.findValueDeserializer(JsonType.INT_TYPE, null);
 deserializer = new StringArrayDeserializer(intDeserializer);
 String[] result = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 assertArrayEqual(new String[]{"1", "2", "3"}, result);
 mapper.getDeserializationContext().returnObjectBuffer(buffer);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{foo: 1}");
 StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
 deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 fail("Expected IOException but no exception was thrown");
 }

 private void assertArrayEqual(Object expected, Object actual) {
 if (!expected.getClass().isArray() || !actual.getClass().isArray() || expected.getClass().getComponentType() != actual.getClass().getComponentType()) {
 fail("Expected and actual arrays have different types");
 }
 if (!Arrays.deepEquals((Object[])expected, (Object[])actual)) {
 fail("Expected and actual arrays are not equal: " + Arrays.toString((Object[])expected) + " != " + Arrays.toString((Object[])actual));
 }
 }

}