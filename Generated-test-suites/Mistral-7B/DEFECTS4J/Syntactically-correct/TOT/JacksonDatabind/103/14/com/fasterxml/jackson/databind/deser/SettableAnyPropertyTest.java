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
 JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty(new AnnotatedClass(Map.class), "_properties"),
 new AnnotatedField(Map.class, "properties"),
 javaType,
 null,
 null,
 null);
 }

 @Test
 public void testSettableAnyPropertyDeserializeAndSet() throws IOException {
 Object pojo = new Object();
 String propertyName = "testProperty";
 Object key = "testKey";
 Object value = "testValue";

 JsonParser jsonParser = objectMapper.getFactory().createParser( "{ \"testKey\": \"testValue\" }" );
 settableAnyProperty.deserializeAndSet(jsonParser, null, pojo, propertyName);

 Map<Object, Object> propertiesMap = (Map<Object, Object>) settableAnyProperty.getProperty().getBean();
 assertEquals(value, propertiesMap.get(key));
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyDeserializeNullValue() throws IOException {
 Object pojo = new Object();
 String propertyName = "testProperty";

 JsonParser jsonParser = objectMapper.getFactory().createParser( "{ \"testProperty\": null }" );
 settableAnyProperty.deserializeAndSet(jsonParser, null, pojo, propertyName);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testSettableAnyPropertyDeserializeInvalidType() throws IOException {
 Object pojo = new Object();
 String propertyName = "testProperty";

 JsonParser jsonParser = objectMapper.getFactory().createParser( "{ \"testProperty\": 123 }" );
 settableAnyProperty.deserializeAndSet(jsonParser, null, pojo, propertyName);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testSettableAnyPropertySet() throws IOException {
 Object pojo = new Object();
 String propertyName = "testProperty";
 Object key = "testKey";
 Object value = "testValue";

 settableAnyProperty.set(pojo, key, value);
 Map<Object, Object> propertiesMap = (Map<Object, Object>) settableAnyProperty.getProperty().getBean();
 assertEquals(value, propertiesMap.get(key));
 }

 @Test(expected = IOException.class)
 public void testSettableAnyPropertySetInvalidType() throws IOException {
 Object pojo = new Object();
 String propertyName = "testProperty";
 Object key = "testKey";
 Integer value = 123;

 settableAnyProperty.set(pojo, key, value);
 fail("Expected IOException");
 }
}