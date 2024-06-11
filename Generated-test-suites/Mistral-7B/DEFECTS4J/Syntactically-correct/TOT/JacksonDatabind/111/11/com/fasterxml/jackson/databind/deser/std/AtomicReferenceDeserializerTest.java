import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetNullValue() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 assertEquals(new AtomicReference<Object>(), deserializer.getNullValue(null));
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 assertNull(deserializer.getEmptyValue(null));
 }

 @Test
 public void testReferenceValue() throws JsonMappingException {
 Object value = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> reference = deserializer.referenceValue(value);
 assertEquals(value, deserializer.getReferenced(reference));
 }

 @Test
 public void testUpdateReference() throws JsonMappingException {
 Object value = new Object();
 Object newValue = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 AtomicReference<Object> reference = deserializer.referenceValue(value);
 deserializer.updateReference(reference, newValue);
 assertEquals(newValue, deserializer.getReferenced(reference));
 }

 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceWithNonAtomicReference() throws JsonMappingException {
 Object value = new Object();
 Object newValue = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(String.class), null, null, null);
 AtomicReference<Object> reference = new AtomicReference<Object>(value);
 deserializer.updateReference(reference, newValue); // should throw an exception
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUpdateReferenceWithImmutableAtomicReference() throws JsonMappingException {
 Object value = new Object();
 AtomicReference<Object> reference = new AtomicReference<Object>(value);
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 deserializer.updateReference(reference, new Object()); // should throw an exception
 }

 @Test
 public void testSupportsUpdate() throws JsonMappingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class), null, null, null);
 assertTrue(deserializer.supportsUpdate(null));
 }

 @Test
 public void testDeserialize() throws JsonMappingException, JsonProcessingException {
 String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"value\":1}";
 AtomicReference<Number> expected = new AtomicReference<Number>(1);
 Object result = mapper.readValue(json, AtomicReference.class);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeWithNullValue() throws JsonMappingException, JsonProcessingException {
 String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\",\"value\":null}";
 AtomicReference<Object> expected = new AtomicReference<Object>();
 Object result = mapper.readValue(json, AtomicReference.class);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeWithEmptyValue() throws JsonMappingException, JsonProcessingException {
 String json = "{\"@class\":\"com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer\"}";
 AtomicReference<Object> expected = new AtomicReference<Object>();
 Object result = mapper.readValue(json, AtomicReference.class);
 assertEquals(expected, result);
 }
}