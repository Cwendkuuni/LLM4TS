import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private SettableAnyProperty settableAnyProperty;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws JsonProcessingException {
 objectMapper = new ObjectMapper();
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.UNKNOWN, null, null, null);
 }

 @Test
 public void testDefaultConstructor() {
 SettableAnyProperty settableAnyPropertyDefault = new SettableAnyProperty(null, null, null, null, null, null);
 assertNotNull(settableAnyPropertyDefault._property);
 assertNull(settableAnyPropertyDefault._setter);
 }

 @Test
 public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
 settableAnyProperty.withValueDeserializer(new FromStringDeserializer());
 JsonParser parser = objectMapper.getFactory().createParser("{\"key\": \"value\"}");
 settableAnyProperty.deserializeAndSet(parser, null, null, "key");
 Object instance = new Object(){};
 settableAnyProperty.deserializeAndSet(parser, null, instance, "key");
 assertEquals("value", instance.getClass().getDeclaredField("key").get(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSettableAnyPropertyWithoutValueDeserializer() throws IOException {
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.UNKNOWN, null, null, null);
 JsonParser parser = objectMapper.getFactory().createParser("{\"key\": \"value\"}");
 settableAnyProperty.deserializeAndSet(parser, null, null, "key");
 Object instance = new Object(){};
 settableAnyProperty.deserializeAndSet(parser, null, instance, "key");
 assertNotNull(instance.getClass().getDeclaredField("key"));
 instance.getClass().getDeclaredField("key").setAccessible(true);
 try {
 instance.getClass().getDeclaredField("key").set(instance, null);
 fail();
 } catch (IllegalAccessException e) {
 // expected
 }
 settableAnyProperty.deserializeAndSet(parser, null, instance, "key");
 assertNotNull(instance.getClass().getDeclaredField("key"));
}

 @Test(expected = JsonMappingException.class)
 public void testInvalidType() throws IOException {
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.constructUnknownType(new Class[]{String.class, Integer.class}), null, null, null);
 JsonParser parser = objectMapper.getFactory().createParser("{\"key\": 1}");
 try {
 settableAnyProperty.deserializeAndSet(parser, null, null, "key");
 fail();
 } catch (JsonMappingException e) {
 // expected
 assertEquals("Property 'key' of type [class java.lang.String, int] is not assignable to value of type [class java.lang.Integer]", e.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testMissingPropertyName() throws IOException {
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.UNKNOWN, null, null, null);
 try {
 settableAnyProperty.deserializeAndSet(null, null, null, null);
 fail();
 } catch (JsonMappingException e) {
 // expected
 assertEquals("No property descriptor for name [null]", e.getMessage());
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testNullInstance() throws IOException {
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.UNKNOWN, null, null, null);
 settableAnyProperty.deserializeAndSet(null, null, null, "key");
}

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeInInstance() throws IOException {
 Object instance = new Object(){};
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.constructUnknownType(new Class[]{String.class, String.class}), null, null, null);
 settableAnyProperty.deserializeAndSet(null, null, instance, "key");
 settableAnyProperty.deserializeAndSet(null, null, instance, "key");
 try {
 instance.getClass().getDeclaredField("key").set(instance, new Integer(1));
 fail();
 } catch (IllegalAccessException e) {
 // expected
 }
 try {
 settableAnyProperty.deserializeAndSet(null, null, instance, "key");
 } catch (IllegalArgumentException e) {
 // expected
 assertEquals("Problem deserializing 'key' of class [SettableAnyProperty] (expected type: class java.lang.String,java.lang.String; actual type: class java.lang.Integer)", e.getMessage());
 }
 }

 @Test
 public void testKeyDeserializer() throws IOException {
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.UNKNOWN, new FromStringDeserializer(), null, null);
 JsonParser parser = objectMapper.getFactory().createParser("{\"key\": \"value1\"}");
 settableAnyProperty.deserializeAndSet(parser, null, null, "key");
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.UNKNOWN, new FromLongDeserializer(), null, null);
 settableAnyProperty.deserializeAndSet(parser, null, null, "key");
 assertEquals(1L, ((Number) settableAnyProperty.deserializeAndSet(parser, null, null, "key")).longValue());
}

 @Test
 public void testJdkSerialization() throws IOException {
 Object instance = new Object(){};
 Object pojo = new Object(){};
 settableAnyProperty = new SettableAnyProperty(
 new BeanProperty.Std(SettableAnyProperty.class.getDeclaredField("_property")),
 new AnnotatedField(SettableAnyProperty.class, "_setter"),
 JavaType.constructUnknownType(SettableAnyProperty.class.getTypeName()), null, null, null);
 settableAnyProperty.deserializeAndSet(null, null, pojo, "key");
 pojo.getClass().getDeclaredField("key").setAccessible(true);
 pojo.getClass().getDeclaredField("key").set(pojo, instance);
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(objectMapper.writeValueAsBytes(pojo)));
 SettableAnyProperty deserializedSettableAnyProperty = (SettableAnyProperty) ois.readObject();
 assertEquals(instance, deserializedSettableAnyProperty.getProperty().get(deserializedSettableAnyProperty.getInstance()));
 ois.close();
}

 @Test
 public void testToString() {
 assertEquals("[any property on class com.fasterxml.jackson.databind.deser.SettableAnyProperty]", settableAnyProperty.toString());
}

}