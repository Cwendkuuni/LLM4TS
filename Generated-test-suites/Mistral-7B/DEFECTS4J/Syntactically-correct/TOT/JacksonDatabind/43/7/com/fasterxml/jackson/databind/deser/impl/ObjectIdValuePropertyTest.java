import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectIdValueProperty objectIdValueProperty;
 private ObjectIdReader objectIdReader;
 private JsonDeserializer<String> stringDeserializer;
 private JsonFactory jsonFactory;

 @Before
 public void setup() throws JsonProcessingException {
 objectIdReader = new ObjectIdReader() {
 @Override
 public PropertyName propertyName() {
 return PropertyName.construct("id");
 }

 @Override
 public Class<Object> idType() {
 return Object.class;
 }

 @Override
 public JsonDeserializer<?> getDeserializer() {
 return stringDeserializer;
 }

 @Override
 public ObjectIdGenerator idGenerator() {
 return null;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return null;
 }

 @Override
 public SettableBeanProperty idProperty() {
 return null;
 }
 };

 stringDeserializer = new FromStringDeserializer();
 jsonFactory = new JsonFactory();
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectNode node = jsonFactory.createObjectNode();
 node.put("id", "123");
 JsonParser parser = jsonFactory.createParser(node);
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(parser, null, instance);
 assertEquals("123", objectIdValueProperty.get(instance));
 }

 @Test
 public void testDeserializeSetAndReturnNullId() throws IOException {
 Object instance = new Object();
 Object id = null;
 Object result = objectIdValueProperty.deserializeSetAndReturn(null, null, instance);
 assertNull(result);
 assertEquals(null, objectIdValueProperty.get(instance));
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 Object id = "456";
 objectIdValueProperty.set(instance, id);
 assertEquals(id, objectIdValueProperty.get(instance));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetOnPropertyWithoutIdProperty() throws IOException {
 Object instance = new Object();
 ObjectIdValueProperty objectIdValuePropertyWithoutIdProperty =
 new ObjectIdValueProperty(objectIdReader, PropertyMetadata.STD_OBJ) {
 @Override
 public SettableBeanProperty idProperty() {
 throw new UnsupportedOperationException();
 }
 };
 Object id = "789";
 objectIdValuePropertyWithoutIdProperty.set(instance, id);
 fail("Should throw UnsupportedOperationException");
 }
}