package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for StringArrayDeserializer.
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] result = new StringArrayDeserializer().deserialize(MAPPER.getFactory().createParser("[]"), MAPPER.getDeserializationContext());
 assertArrayEqual(new String[0], result);
 }

 @Test
 public void testDeserializeSingleElementArray() throws IOException {
 String[] result = new StringArrayDeserializer().deserialize(MAPPER.getFactory().createParser("['test']"), MAPPER.getDeserializationContext());
 assertArrayEqual(new String[]{"test"}, result);
 }

 @Test
 public void testDeserializeCustomElementDeserializer() throws IOException {
 JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
 StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
 String[] result = deserializer.deserialize(MAPPER.getFactory().createParser("['test1', 'test2']"), MAPPER.getDeserializationContext());
 assertArrayEqual(new String[]{"test1", "test2"}, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidJson() throws IOException {
 String[] result = new StringArrayDeserializer().deserialize(MAPPER.getFactory().createParser("{foo: 'bar'}"), MAPPER.getDeserializationContext());
 fail();
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullInput() throws IOException {
 String[] result = new StringArrayDeserializer().deserialize(MAPPER.getFactory().createParser(null), MAPPER.getDeserializationContext());
 fail();
 }

 @Test(expected = IOException.class)
 public void testDeserializeUnexpectedToken() throws IOException {
 String[] result = new StringArrayDeserializer().deserialize(MAPPER.getFactory().createParser("true"), MAPPER.getDeserializationContext());
 fail();
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(String.class);
 String[] result = new StringArrayDeserializer().deserializeWithType(MAPPER.getFactory().createParser("['test1', 'test2']"), MAPPER.getDeserializationContext(), typeDeserializer);
 assertArrayEqual(new String[]{"test1", "test2"}, result);
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 String[] result = new StringArrayDeserializer().deserialize(MAPPER.getFactory().createParser("null"), MAPPER.getDeserializationContext());
 assertArrayEqual(new String[]{null}, result);
 }

 @Test
 public void testDeserializeWithContextualization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 StringArrayDeserializer deserializer = mapper.findValueDeserializer(String[].class, mapper.getRootValueFactory(StdClassId.INSTANCE, "root"));
 assertNotNull(deserializer);
 String[] result = deserializer.deserialize(mapper.getFactory().createParser("['test1', 'test2']"), mapper.getDeserializationContext());
 assertArrayEqual(new String[]{"test1", "test2"}, result);
 }

 private static void assertArrayEqual(Object expected, Object actual) {
 if (!expected.getClass().isArray() || !actual.getClass().isArray() || expected.getClass().getComponentType() != actual.getClass().getComponentType()) {
 fail("Unexpected array types: expected=" + expected.getClass() + ", actual=" + actual.getClass());
 }
 Object[] expectedArray = (Object[]) expected;
 Object[] actualArray = (Object[]) actual;
 assertArrayEquals(expectedArray, actualArray);
 }

 private static void assertArrayEquals(Object[] expected, Object[] actual) {
 if (expected.length != actual.length) {
 fail("Arrays have different lengths: expected=" + expected.length + ", actual=" + actual.length);
 }
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], actual[i]);
 }
 }

 private static class CustomStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
 return jp.getValueAsString();
 }
}

 private static class ArrayTypeDeserializer extends TypeDeserializer {
 private final Class<?> _elementType;

 public ArrayTypeDeserializer(Class<?> elementType) {
 _elementType = elementType;
 }

 @Override
 public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
 ObjectBuffer buffer = ctxt.leaseObjectBuffer();
 Object[] chunk = buffer.resetAndStart();

 int ix = 0;
 JsonToken t;

 while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
 String value;
 if (t == JsonToken.VALUE_STRING) {
 value = jp.getText();
 } else if (t == JsonToken.VALUE_NULL) {
 value = null;
 } else {
 value = jp.readValueAs(elementType);
 }
 if (ix >= chunk.length) {
 chunk = buffer.appendCompletedChunk(chunk);
 ix = 0;
 }
 chunk[ix++] = value;
 }
 String[] result = buffer.completeAndClearBuffer(chunk, ix, String[].class);
 ctxt.returnObjectBuffer(buffer);
 return result;
 }
}
}