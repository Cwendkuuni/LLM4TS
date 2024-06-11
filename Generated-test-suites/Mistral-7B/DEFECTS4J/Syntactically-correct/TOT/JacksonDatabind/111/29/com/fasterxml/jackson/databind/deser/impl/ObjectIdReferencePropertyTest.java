package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.annotations.*;

import static com.fasterxml.jackson.databind.SerializationFeature.*;
import static org.junit.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ObjectIdReferencePropertyTest {

 private ObjectMapper objectMapper;

 @BeforeClass
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(UNWRAP_ROOT_VALUE, false);
 }

 @Test
 public void testBasicDeserialization() throws JsonProcessingException {
 // Given
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("id", 1L);
 rootNode.put("ref", new ObjectNode().put("name", "John Doe"));

 // When
 MyPojo pojo = objectMapper.readValue(rootNode.toString(), MyPojo.class);

 // Then
 assertEquals(1L, pojo.getId());
 assertEquals("John Doe", pojo.getName());
 assertNull(pojo.getRef());
 }

 @Test
 public void testDeserializationWithNullValueProvider() throws JsonProcessingException {
 // Given
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 ObjectIdReferenceProperty nullValueProperty = new ObjectIdReferenceProperty(property, null);
 MyPojo pojo = new MyPojo();
 pojo.setName("John Doe");

 // When
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("ref", null);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setNullValueProvider(new StandardNullValueProvider());
 ObjectNode result = mapper.readTree(rootNode);
 result = mapper.readValue(result.toString(), ObjectNode.class);

 // Then
 assertEquals(pojo, property.deserializeSetAndReturn(objectMapper.getFactory().createParser(result.toString()), null, pojo));
 }

 @Test
 public void testDeserializationWithCustomNullValueProvider() throws JsonProcessingException {
 // Given
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 MyPojo pojo = new MyPojo();
 pojo.setName("John Doe");
 MyCustomNullValueProvider nullValueProvider = new MyCustomNullValueProvider();
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("ref", null);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setNullValueProvider(nullValueProvider);
 ObjectNode result = mapper.readTree(rootNode.toString());
 result = mapper.readValue(result.toString(), ObjectNode.class);

 // When
 MyPojo deserializedPojo = property.deserializeSetAndReturn(objectMapper.getFactory().createParser(result.toString()), null, pojo);

 // Then
 assertNull(deserializedPojo.getRef());
 }

 @Test
 public void testDeserializationWithUnresolvedForwardReference() throws JsonProcessingException {
 // Given
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 ObjectIdReferenceProperty forwardProperty = new ObjectIdReferenceProperty(property, null);
 MyPojo pojo = new MyPojo();
 pojo.setName("John Doe");
 ObjectIdReferenceProperty referencingProperty = new ObjectIdReferenceProperty(forwardProperty, null);
 MyOtherPojo otherPojo = new MyOtherPojo();
 otherPojo.setName("Jane Doe");
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("id", 1L);
 rootNode.put("ref", new ObjectNode().put("name", new ObjectId("referencingProperty")));

 // When
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode result = mapper.readTree(rootNode.toString());
 mapper.readValue(result.toString(), MyPojo.class);

 // Then
 assertTrue(referencingProperty.getReferring().size() > 0);
 MyOtherPojo resolvedOtherPojo = (MyOtherPojo) referencingProperty.getReferring().get(0).getObject();
 assertEquals("Jane Doe", resolvedOtherPojo.getName());
 }

 @Test
 public void testDeserializationWithCircularReference() throws JsonProcessingException {
 // Given
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
 MyPojo pojo1 = new MyPojo();
 pojo1.setName("John Doe");
 MyPojo pojo2 = new MyPojo();
 pojo2.setName("Jane Doe");
 pojo2.setRef(pojo1);
 ObjectNode rootNode = objectMapper.createObjectNode();
 rootNode.put("id1", 1L);
 rootNode.put("id2", 2L);
 rootNode.put("ref1", new ObjectNode().put("id", "ref2"));
 rootNode.put("ref2", new ObjectNode().put("id", "ref1"));

 // When
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode result = mapper.readTree(rootNode.toString());
 MyPojo deserializedPojo1 = mapper.readValue(result.toString(), MyPojo.class);
 MyPojo deserializedPojo2 = mapper.readValue(result.toString(), MyPojo.class);

 // Then
 assertEquals(pojo1, deserializedPojo1);
 assertEquals(pojo2, deserializedPojo2);
 }

 @Test
 public void testSerialization() throws JsonProcessingException {
 // Given
 MyPojo pojo = new MyPojo();
 pojo.setId(1L);
 pojo.setName("John Doe");
 pojo.setRef(new MyOtherPojo());
 MyOtherPojo refPojo = new MyOtherPojo();
 refPojo.setName("Jane Doe");
 pojo.setRef(refPojo);

 // When
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.writeValueAsTree(pojo);
 String json = rootNode.toString();
 MyPojo deserializedPojo = mapper.readValue(json, MyPojo.class);

 // Then
 assertEquals(pojo, deserializedPojo);
 }

 @Test
 public void testSerializationWithEmptyBean() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 MyPojo pojo = new MyPojo();
 ObjectNode rootNode = mapper.writeValueAsTree(pojo);
 String json = rootNode.toString();
 MyPojo deserializedPojo = mapper.readValue(json, MyPojo.class);

 // Then
 assertNotNull(deserializedPojo);
 assertEquals(pojo, deserializedPojo);
 }

 @Test
 public void testSerializiationWithNullValues() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();
 MyPojo pojo = new MyPojo();
 pojo.setName(null);
 pojo.setRef(null);
 ObjectNode rootNode = mapper.writeValueAsTree(pojo);
 String json = rootNode.toString();
 MyPojo deserializedPojo = mapper.readValue(json, MyPojo.class);

 // Then
 assertNotNull(deserializedPojo);
 assertNull(deserializedPojo.getName());
 assertNull(deserializedPojo.getRef());
 }

 @Test
 public void testDeserializationWithUnregisteredProperty() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();

 // When
 MyPojo pojo = mapper.readValue(new ObjectMapper().createParser("{\"name\": \"John Doe\", \"unregisteredProperty\": 1}"), MyPojo.class);

 // Then
 assertNull(pojo.getUnregisteredProperty());
 }

 @Test(expectedExceptions = {JsonMappingException.class})
 public void testDeserializationWithUnregisteredProperty_withException() throws JsonProcessingException {
 // Given
 ObjectMapper mapper = new ObjectMapper();

 // When
 MyPojo pojo = mapper.readValue(new ObjectMapper().createParser("{\"name\": \"John Doe\", \"unregisteredProperty\": 1}"), MyPojo.class);
 }

 @JsonDeserialize(using = ObjectIdReferenceProperty.class)
 static class MyPojo {
 private Long id;
 private String name;
 private MyOtherPojo ref;

 public Long getId() {
 return id;
 }

 public void setId(Long id) {
 this.id = id;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public MyOtherPojo getRef() {
 return ref;
 }

 public void setRef(MyOtherPojo ref) {
 this.ref = ref;
 }
 }

 static class MyOtherPojo {
 private String name;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
 }

 static class MyCustomNullValueProvider extends NullValueProvider.Base {
 @Override
 public Object getValueProviderValue(DeserializationContext ctxt, JsonParser p, Object value) throws IOException {
 return null;
 }
 }
}