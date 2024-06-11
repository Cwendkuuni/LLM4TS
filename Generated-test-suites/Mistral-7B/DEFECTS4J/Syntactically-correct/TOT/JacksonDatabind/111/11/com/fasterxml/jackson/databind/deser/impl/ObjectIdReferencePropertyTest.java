import com.fasterxml.jackson.core.base. PropertyName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

 private ObjectMapper mapper;
 private ObjectIdReferenceProperty property;
 private ObjectIdReferenceProperty propertyWithName;
 private ObjectIdReferenceProperty propertyWithValueDeserializer;
 private ObjectIdReferenceProperty propertyWithNullProvider;
 private Object id;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.createObjectNode();
 property = new ObjectIdReferenceProperty(null, "_default", mapper.findDeserializer(String.class), null);
 propertyWithName = new ObjectIdReferenceProperty(property, new PropertyName("_name"));
 propertyWithValueDeserializer = new ObjectIdReferenceProperty(property, mapper.findDeserializer(Integer.class), null);
 propertyWithNullProvider = new ObjectIdReferenceProperty(property, null);
 id = mapper.readValue(mapper.writeValueAsString(new ObjectIdInfo(null, "_id", null)), ObjectIdInfo.class).getId();
 }

 @Test
 public void test_withName() throws Exception {
 Object propertyInstance = new Object();
 propertyWithName.set(propertyInstance, "name");
 assertEquals("name", propertyWithName.getMember().getName());
 }

 @Test(expected = IllegalStateException.class)
 public void test_withNullName() throws Exception {
 propertyWithName = new ObjectIdReferenceProperty(property, null);
 propertyWithName.set(null, "name");
 fail("Expected NullPointerException or IllegalStateException but got none");
 }

 @Test
 public void test_withValueDeserializer() throws Exception {
 Object propertyInstance = new Object();
 JsonNode node = mapper.readTree("{\"id\":1}");
 propertyWithValueDeserializer.set(propertyInstance, mapper.treeToValue(node, Integer.class));
 assertEquals(1, propertyWithValueDeserializer.get(propertyInstance));
 }

 @Test
 public void test_withNullValueDeserializer() throws Exception {
 propertyWithNullProvider.set(null, null);
 assertNull(propertyWithNullProvider.get(null));
 }

 @Test
 public void test_deserializeAndSet() throws Exception {
 Object propertyInstance = new Object();
 ObjectNode rootNodeWithId = mapper.createObjectNode();
 rootNodeWithId.set("_id", id);
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationConfig(), propertyInstance);
 ObjectIdInfo idInfo = mapper.readValue(mapper.writeValueAsString(new ObjectIdInfo(id, "_id", null)), ObjectIdInfo.class);
 assertEquals(idInfo.getId(), property.get(propertyInstance));
 }

 @Test
 public void test_deserializeSetAndReturn() throws Exception {
 Object propertyInstance = new Object();
 ObjectNode rootNodeWithId = mapper.createObjectNode();
 rootNodeWithId.set("_id", id);
 assertEquals(null, property.deserializeSetAndReturn(mapper.parserForTesting(), mapper.deserializationConfig(), propertyInstance));
 ObjectIdInfo idInfo = mapper.readValue(mapper.writeValueAsString(new ObjectIdInfo(id, "_id", null)), ObjectIdInfo.class);
 assertEquals(idInfo.getId(), property.get(propertyInstance));
 }

 @Test
 public void test_set() throws Exception {
 Object propertyInstance = new Object();
 property.set(propertyInstance, id);
 assertEquals(id, property.get(propertyInstance));
 }

 @Test
 public void test_setAndReturn() throws Exception {
 Object propertyInstance = new Object();
 assertEquals(null, property.setAndReturn(propertyInstance, id));
 assertEquals(id, property.get(propertyInstance));
 }

 @Test
 public void test_fixAccess() throws Exception {
 property.fixAccess(mapper.deserializationConfig());
 propertyWithName.fixAccess(mapper.deserializationConfig());
 propertyWithValueDeserializer.fixAccess(mapper.deserializationConfig());
 propertyWithNullProvider.fixAccess(mapper.deserializationConfig());
 }

 @Test
 public void test_getAnnotation() throws Exception {
 assertTrue(property.getAnnotation(null) == null);
 assertTrue(propertyWithName.getAnnotation(null) == null);
 assertTrue(propertyWithValueDeserializer.getAnnotation(null) == null);
 assertTrue(propertyWithNullProvider.getAnnotation(null) == null);
 }

 @Test
 public void test_getMember() throws Exception {
 assertNotNull(property.getMember());
 assertNotNull(propertyWithName.getMember());
 assertNotNull(propertyWithValueDeserializer.getMember());
 assertNotNull(propertyWithNullProvider.getMember());
 }

 @Test
 public void test_getCreatorIndex() throws Exception {
 assertEquals(-1, property.getCreatorIndex());
 assertEquals(-1, propertyWithName.getCreatorIndex());
 assertEquals(-1, propertyWithValueDeserializer.getCreatorIndex());
 assertEquals(-1, propertyWithNullProvider.getCreatorIndex());
 }

 @Test
 public void test_deserializeAndSet_UnresolvedForwardReference() throws Exception {
 Object propertyInstance = new Object();
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.set("id", "_UNRESOLVED_");
 property.deserializeAndSet(mapper.parserForTesting(), mapper.deserializationConfig(), propertyInstance);
 Referring ref = property.get(propertyInstance);
 assertNotNull(ref);
 assertEquals("ObjectIdReferenceProperty", ref.getClass().getSimpleName());
 assertEquals(property, ref.getParent());
 assertEquals(null, property.get(propertyInstance));
 }
}