import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private ObjectMapper objectMapper;
 private SettableAnyProperty settableAnyProperty;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 SettableAnyProperty.StandardSetter setter = new SettableAnyProperty.StandardSetter(
 new BeanProperty.Std(Object.class, "anyProperty"), null, null, null, null, null);
 settableAnyProperty = new SettableAnyProperty(setter, null, JavaType.INT_TYPE, null, null, null);
 }

 @Test
 public void testDeserializeAndSetWithValidInput() throws Exception {
 Object pojo = new Object();
 String propName = "testProperty";
 int expectedValue = 42;

 JsonParser parser = objectMapper.getFactory().createParser("{\"anyProperty\":{\"testProperty\":42}}");
 settableAnyProperty.deserializeAndSet(parser, null, pojo, propName);
 assertEquals(expectedValue, ((Map<?,?>) ((AnnotatedField) settableAnyProperty._setter).getValue(pojo)).get(propName));
 }

 @Test
 public void testDeserializeAndSetWithNullInput() throws Exception {
 Object pojo = new Object();
 String propName = "testProperty";

 JsonParser parser = objectMapper.getFactory().createParser("{\"anyProperty\":null}");
 settableAnyProperty.deserializeAndSet(parser, null, pojo, propName);
 assertNull( ((Map<?,?>) ((AnnotatedField) settableAnyProperty._setter).getValue(pojo)).get(propName));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithInvalidInput() throws Exception {
 Object pojo = new Object();
 String propName = "testProperty";

 JsonParser parser = objectMapper.getFactory().createParser("{\"anyProperty\":[1,2,3]}");
 settableAnyProperty.deserializeAndSet(parser, null, pojo, propName);
 fail("Expected JsonMappingException but no exception was thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeAndSetWithInvalidType() throws Exception {
 Object pojo = new Object();
 String propName = "testProperty";
 Double expectedValue = 42.0;

 SettableAnyProperty<String> stringSettableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(Object.class, "anyProperty"), null, JavaType.TypeFactory.valueType(String.class), null, null, null);
 jsonParser = objectMapper.getFactory().createParser("{\"anyProperty\":42}");
 stringSettableAnyProperty.deserializeAndSet(parser, null, pojo, propName);
 assertEquals(expectedValue.getClass(), ((Map<?,?>) ((AnnotatedField) stringSettableAnyProperty._setter).getValue(pojo)).get(propName).getClass());
 fail("Expected IllegalArgumentException but no exception was thrown.");
 }
}