import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

 private ObjectNode testObject;
 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setup() throws JsonProcessingException {
 testObject = ObjectMapperBuilder.json().build()
 .readTree("{\"idProperty\":{}}");
 objectIdReader = new ObjectIdReader() {
 @Override
 public ObjectIdReader.IdType getIdType() {
 return ObjectIdReader.IdType.INT;
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
 public PropertyName propertyName() {
 return new PropertyName("id");
 }

 @Override
 public String idPropertyName() {
 return null;
 }
 };
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata());
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectParser parser = ObjectMapperBuilder.json().build()
 .treeToValue(ObjectNode.class).getParser();
 ObjectNode jsonNode = parser.readValueAsTree("{\"id\":1}");
 Object id = objectIdReader.reader.readValueAs(Integer.class, jsonNode);
 Object targetInstance = testObject.asTree().get(0)
 .set("idProperty", objectIdValueProperty);
 objectIdValueProperty.deserializeAndSet(parser, new DeserializationContext() {}, targetInstance);
 assertEquals(1, testObject.get("idProperty").get("id").asInt());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectParser parser = ObjectMapperBuilder.json().build()
 .treeToValue(ObjectNode.class).getParser();
 ObjectNode jsonNode = parser.readValueAsTree("{\"id\":1}");
 Object id = objectIdReader.reader.readValueAs(Integer.class, jsonNode);
 Object targetInstance = testObject.asTree().get(0)
 .set("idProperty", objectIdValueProperty);
 Object result = objectIdValueProperty.deserializeSetAndReturn(parser, new DeserializationContext() {}, targetInstance);
 assertEquals(1, testObject.get("idProperty").get("id").asInt());
 assertEquals(targetInstance, result);
 }

 @Test
 public void testSetAndReturn() throws IOException {
 Object targetInstance = testObject.asTree().get(0)
 .set("idProperty", objectIdValueProperty);
 Object id = new JsonPrimitive(1);
 ObjectIdGenerator idGenerator = new AlwaysStaticObjectIdGenerator();
 ObjectIdResolver idResolver = new AlwaysStaticObjectIdResolver(id);
 ObjectIdValueProperty newObjectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata()) {
 @Override
 public ObjectIdGenerator idGenerator() {
 return idGenerator;
 }

 @Override
 public ObjectIdResolver idResolver() {
 return idResolver;
 }
 };
 Object result = newObjectIdValueProperty.setAndReturn(targetInstance, id);
 assertEquals(id, testObject.get("idProperty").get("id"));
 assertEquals(targetInstance, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullId() throws IOException {
 Object targetInstance = testObject.asTree().get(0)
 .set("idProperty", objectIdValueProperty);
 objectIdValueProperty.set(targetInstance, null);
 fail("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
 }

 @Test
 public void testAnnotatedMemberAndAnnotation() {
 JsonDeserializer<Object> testDeserializer = new StdDeserializer<Object>() {};
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata()) {
 @Override
 public JsonDeserializer<?> getValueDeserializer() {
 return testDeserializer;
 }
 };
 ObjectIdValueProperty annotatedProperty = new ObjectIdValueProperty(objectIdReader, new StandardPropertyMetadata()) {
 @JsonDeserialize(using = testDeserializer.getClass())
 public static class InnerClass {
 }
 @Override
 public AnnotatedMember getMember() {
 return new AnnotatedMember(new Annotation()[0], property);
 }
 };
 assertNull(property.getAnnotation(JsonDeserialize.class));
 assertNotNull(annotatedProperty.getAnnotation(JsonDeserialize.class));
 }
}