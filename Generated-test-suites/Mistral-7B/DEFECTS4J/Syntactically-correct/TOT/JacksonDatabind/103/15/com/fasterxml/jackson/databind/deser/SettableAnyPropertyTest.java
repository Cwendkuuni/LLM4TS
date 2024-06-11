import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectMapper objectMapper;
 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 final BeanProperty property = new BeanProperty(null, "anyProperty", String.class, null, null);
 final AnnotatedMember setter = new AnnotatedMember(null, "setAnyProperty", null, null, null, null, null);
 JavaType type = ObjectTypeFactory.defaultInstance.constructType(Map.class, String.class, String.class);
 KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
 JsonDeserializer<Object> valueDeserializer = new StdDeserializer<>();
 TypeDeserializer valueTypeDeserializer = new StdTypeDeserializer();
 settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 }

 @Test
 public void testSettableAnyPropertyDeserializeAndSet() throws JsonProcessingException {
 Object pojo = new Object();
 String propName = "testProperty";
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": \"hello\"}");
 settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), pojo, propName);
 assertEquals("hello", settableAnyProperty.getProperty().get(pojo));
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyDeserializeAndSetInvalidType() throws JsonProcessingException {
 Object pojo = new Object();
 String propName = "testProperty";
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": 123}");
 settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), pojo, propName);
 fail("Expected JsonMappingException but no exception was thrown.");
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyDeserializeAndSetMissingProperty() throws JsonProcessingException {
 Object pojo = new Object();
 String propName = "testProperty";
 JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
 settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), pojo, propName);
 fail("Expected JsonMappingException but no exception was thrown.");
 }

 @Test
 public void testSettableAnyPropertySet() throws JsonProcessingException {
 Object pojo = new Object();
 String propName = "testProperty";
 String value = "world";
 settableAnyProperty.set(pojo, propName, value);
 assertEquals(value, settableAnyProperty.getProperty().get(pojo));
 }

 @Test(expected = IOException.class)
 public void testSettableAnyPropertySetIOException() throws JsonProcessingException {
 Object pojo = new Object();
 String propName = "testProperty";
 String value = "world";
 JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\": \"invalid\"}");
 settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), pojo, propName);
 try {
 settableAnyProperty.set(pojo, propName, value);
 fail("Expected IOException but no exception was thrown.");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertySetInvalidPropertyName() throws JsonProcessingException {
 Object pojo = new Object();
 String propName = "testProperty";
 String value = "world";
 settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser("{}"), objectMapper.getDeserializationContext(), pojo, "invalidPropertyName");
 try {
 settableAnyProperty.set(pojo, "invalidPropertyName", value);
 fail("Expected JsonMappingException but no exception was thrown.");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSettableAnyPropertySetNullInstance() throws JsonProcessingException {
 Object pojo = null;
 String propName = "testProperty";
 String value = "world";
 settableAnyProperty.deserializeAndSet(objectMapper.getFactory().createParser("{}"), objectMapper.getDeserializationContext(), pojo, propName);
 try {
 settableAnyProperty.set(pojo, propName, value);
 fail("Expected NullPointerException but no exception was thrown.");
 } catch (NullPointerException e) {
 // expected
 }
 }
}