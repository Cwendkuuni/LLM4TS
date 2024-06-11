package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.PropertyName;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdReferencePropertyTest {

 private ObjectMapper objectMapper;
 private JsonDeserializer<String> stringDeserializer;
 private ObjectNode inputNode;
 private ObjectIdReferenceProperty property;
 private JsonParser jsonParser;

 @Before
 public void setup() throws Exception {
 ObjectIdInfo objectIdInfo = Mockito.mock(ObjectIdInfo.class);
 stringDeserializer = Mockito.mock(JsonDeserializer.class);
 objectMapper = new ObjectMapper();
 objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
 objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(UNWRAP_ROOT_VALUE, false);
 inputNode = ObjectMapperTestUtils.createInput("{\"name\": null, \"id\": null}", ObjectNode.class);
 jsonParser = objectMapper.getFactory().createParser(inputNode);
 property = new ObjectIdReferenceProperty(Mockito.mock(SettableBeanProperty.class), objectIdInfo);
 }

 @Test
 public void testWithName() throws IOException, JsonMappingException {
 ObjectNode updatedInput = ObjectMapperTestUtils.createInput("{\"name\": \"newName\", \"id\": null}", ObjectNode.class);
 JsonDeserializer<String> newDeserializer = Mockito.mock(JsonDeserializer.class);
 ObjectNode expectedOutput = ObjectMapperTestUtils.createOutput("{\"name\": \"newName\", \"id\": null}", ObjectNode.class);
 property = property.withName(new PropertyName("newName"));
 property = property.withValueDeserializer(newDeserializer);
 ObjectNode result = objectMapper.readValue(updatedInput, JsonNode.class).deepGetObject();
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testWithValueDeserializer() throws IOException, JsonMappingException {
 JsonNode expectedInput = ObjectMapperTestUtils.createInputNode("{\"name\": null, \"id\": \"expectedValue\"}");
 String expectedValue = "expectedValue";
 property = property.withValueDeserializer(stringDeserializer);
 property = property.withName("id");
 ObjectNode result = objectMapper.readValue(expectedInput, JsonNode.class).deepGetObject();
 assertEquals(expectedValue, result.get("id"));
 }

 @Test(expected = JsonMappingException.class)
 public void testWithNullValueDeserializer() throws IOException, JsonMappingException {
 JsonNode inputNode = ObjectMapperTestUtils.createInputNode("{\"name\": null, \"id\": null}");
 property = property.withNullProvider(JsonNull.getInstance());
 property = property.withValueDeserializer(null);
 objectMapper.readValue(inputNode, Object.class);
 }

 @Test
 public void testFixAccess() throws IOException {
 Object node = new Object();
 property = property.withName("name");
 property.fixAccess(objectMapper.getDeserializationConfig());
 property.set(node, "value");
 assertEquals("value", node.getClass().getDeclaredField("name").get(node));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithNullValue() throws IOException, JsonMappingException {
 property = property.withName("name");
 property.deserializeAndSet(jsonParser, objectMapper.getDeserializationConfig(), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException, JsonMappingException {
 property = property.withName("name");
 ObjectNode inputNode = ObjectMapperTestUtils.createInput("{\"name\": null, \"id\": null}", ObjectNode.class);
 UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
 property.deserializeAndSet(jsonParser, objectMapper.getDeserializationConfig(), null);
 Mockito.verify(reference).getRoid().appendReferring(Mockito.isInstanceOf(PropertyReferring.class));
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException, JsonMappingException {
 Object testInstance = new Object();
 ObjectNode expectedOutput = ObjectMapperTestUtils.createOutput("{\"name\": null, \"id\": null}", ObjectNode.class);
 property.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationConfig(), testInstance);
 assertEquals(expectedOutput, objectMapper.readValue(jsonParser, JsonNode.class).deepGetObject());
 assertNull(testInstance.getClass().getDeclaredField("name").get(testInstance));
 }

 @Test
 public void testSet() throws IOException, JsonMappingException {
 Object testInstance = new Object();
 ObjectNode inputNode = ObjectMapperTestUtils.createInput("{\"name\": null, \"id\": null}", ObjectNode.class);
 property.set(testInstance, null);
 ObjectNode expectedOutput = ObjectMapperTestUtils.createOutput("{\"name\": null, \"id\": null}", ObjectNode.class);
 assertEquals(expectedOutput, objectMapper.readValue(inputNode, JsonNode.class).deepGetObject());
 assertEquals(null, testInstance.getClass().getDeclaredField("name").get(testInstance));
 }

 @Test
 public void testSetAndReturn() throws IOException, JsonMappingException {
 Object testInstance = new Object();
 ObjectNode inputNode = ObjectMapperTestUtils.createInput("{\"name\": null, \"id\": null}", ObjectNode.class);
 property.setAndReturn(testInstance, null);
 ObjectNode expectedOutput = ObjectMapperTestUtils.createOutput("{\"name\": null, \"id\": null}", ObjectNode.class);
 assertEquals(expectedOutput, objectMapper.readValue(inputNode, JsonNode.class).deepGetObject());
 assertNull(testInstance.getClass().getDeclaredField("name").get(testInstance));
 }

 @Test
 public void testPropertyReferringHandleResolvedForwardReference() throws IOException, JsonMappingException {
 Object testInstance = new Object();
 ObjectNode inputNode = ObjectMapperTestUtils.createInput("{\"name\": null, \"id\": null}", ObjectNode.class);
 UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
 PropertyReferring referring = new PropertyReferring(property, reference, testInstance.getClass(), testInstance);
 property.deserializeSetAndReturn(jsonParser, objectMapper.getDeserializationConfig(), testInstance);
 ObjectNode expectedOutput = ObjectMapperTestUtils.createOutput("{\"name\": null, \"id\": 1}", ObjectNode.class);
 ObjectNode result = objectMapper.readValue(jsonParser, JsonNode.class).deepGetObject();
 assertEquals(expectedOutput, result);
 assertSame(testInstance, result.get("name"));
 }

 @Test
 public void testGetAnnotation() throws IOException, JsonMappingException {
 PropertyName propertyName = new PropertyName("name");
 JsonIdentityInfo identityInfo = new JsonIdentityInfo(ObjectIdGenerators.IntGenerationStrategy.alwaysAsInt, propertyName);
 ObjectNode inputNode = ObjectMapperTestUtils.createInput("{\"@JsonIdentity\": {\"generator\": \"int\", \"property\": \"name\"}}", ObjectNode.class);
 property = new ObjectIdReferenceProperty(Mockito.mock(SettableBeanProperty.class), identityInfo);
 property = property.withName("name");
 assertEquals(identityInfo, property.getAnnotation(JsonIdentityInfo.class));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetAnnotationWithNullProperty() throws IOException, JsonMappingException {
 property = property.withName(null);
 property.getAnnotation(JsonIdentityInfo.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetAnnotationWithNullAnnotation() throws IOException, JsonMappingException {
 property = property.withName("name");
 property.getAnnotation(Null.class);
 }

 @Test
 public void testGetMember() throws IOException, JsonMappingException {
 property = property.withName("name");
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 Mockito.when(member.getAnnotations()).thenReturn(new JsonIdentityInfo[0]);
 Mockito.when(member.getName()).thenReturn(new PropertyName("name"));
 Mockito.when(member.getDeclaringClass()).thenReturn(Object.class);
 assertEquals(member, property.getMember());
 }

 @Test
 public void testGetCreatorIndex() throws IOException, JsonMappingException {
 property = property.withName("name");
 assertEquals(-1, property.getCreatorIndex());
 }

 @Test
 public void testDeserializeJsonSchema() throws IOException, JsonProcessingException {
 JsonSchema schema = Mockito.mock(JsonSchema.class);
 property = property.withName("name");
 property.deserializeSchema(schema, objectMapper.getDeserializationConfig(), null);
 assertEquals(schema, property.getSchema());
 }

 @Test
 public void testGetSchema() throws IOException, JsonMappingException {
 property = property.withName("name");
 JsonSchema schema = Mockito.mock(JsonSchema.class);
 property.setSchema(schema);
 assertEquals(schema, property.getSchema());
 }
}