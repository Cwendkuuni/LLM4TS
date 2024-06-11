package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.fasterxml.jackson.databind.SerializationFeature.*;

public class ObjectIdReferencePropertyTest {

 private ObjectIdInfo objectIdInfo;
 private SettableBeanProperty forwardProperty;
 private ObjectIdReferenceProperty objectIdReferenceProperty;
 private JsonDeserializer<?> jsonDeserializer;
 private JsonParser jsonParser;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectIdInfo = new ObjectIdInfo(new Annotation[]{}, "id");
 forwardProperty = new TestSettableBeanProperty();
 jsonDeserializer = new TestJsonDeserializer();
 jsonParser = new TestJsonParser();
 objectMapper = new ObjectMapper();
 objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
 objectMapper.configure(FAIL_ON_EMPTY_BEANS_FIND_EMPTY_ARRAYS, false);
 }

 @Test
 public void testObjectIdReferencePropertyCreation() {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 assertEquals("Forward property should be assigned", forwardProperty, objectIdReferenceProperty._forward);
 assertEquals("ObjectIdInfo should be assigned", objectIdInfo, objectIdReferenceProperty._objectIdInfo);
 }

 @Test
 public void testObjectIdReferencePropertySerialization() throws IOException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("id", "existingId");
 JsonNode result = objectMapper.readTree(objectMapper.writeValueAsString(objectIdReferenceProperty));
 assertEquals("Serialized ObjectIdReferenceProperty should match input", input, result);
 }

 @Test
 public void testObjectIdReferencePropertyDeserialization() throws IOException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("_class", ObjectIdReferenceProperty.class.getName());
 input.set("forward", forwardProperty.serializeToNode(objectMapper));
 input.set("objectIdInfo", objectIdInfo.serializeToNode(objectMapper));
 ObjectIdReferenceProperty deserializedObjectIdReferenceProperty = objectMapper.readValue(input.toString(), ObjectIdReferenceProperty.class);
 assertEquals("Deserialized ObjectIdReferenceProperty should match input", objectIdReferenceProperty, deserializedObjectIdReferenceProperty);
 }

 @Test
 public void testObjectIdReferencePropertyDeserializationWithValueDeserializer() throws IOException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("_class", ObjectIdReferenceProperty.class.getName());
 input.set("forward", forwardProperty.serializeToNode(objectMapper));
 input.set("objectIdInfo", objectIdInfo.serializeToNode(objectMapper));
 input.set("valueDeserializer", jsonDeserializer.getClass().getName());
 ObjectIdReferenceProperty deserializedObjectIdReferenceProperty = objectMapper.readValue(input.toString(), ObjectIdReferenceProperty.class);
 assertEquals("Deserialized ObjectIdReferenceProperty should match input", objectIdReferenceProperty, deserializedObjectIdReferenceProperty);
 }

 @Test
 public void testObjectIdReferencePropertyDeserializationWithNullProvider() throws IOException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("_class", ObjectIdReferenceProperty.class.getName());
 input.set("forward", forwardProperty.serializeToNode(objectMapper));
 input.set("objectIdInfo", objectIdInfo.serializeToNode(objectMapper));
 input.set("nullProvider", new TestNullValueProvider().getClass().getName());
 ObjectIdReferenceProperty deserializedObjectIdReferenceProperty = objectMapper.readValue(input.toString(), ObjectIdReferenceProperty.class);
 assertEquals("Deserialized ObjectIdReferenceProperty should match input", objectIdReferenceProperty, deserializedObjectIdReferenceProperty);
 }

 @Test
 public void testObjectIdReferencePropertyDeserializationWithUnresolvedForwardReference() throws IOException {
 ObjectNode input = objectMapper.createObjectNode();
 input.put("_class", ObjectIdReferenceProperty.class.getName());
 input.set("forward", forwardProperty.serializeToNode(objectMapper).withoutField("_class"));
 input.set("objectIdInfo", objectIdInfo.serializeToNode(objectMapper));
 ObjectIdReferenceProperty deserializedObjectIdReferenceProperty = objectMapper.readValue(input.toString(), ObjectIdReferenceProperty.class);
 assertNotNull("Deserialized ObjectIdReferenceProperty should have a unresolved forward reference", deserializedObjectIdReferenceProperty._forward);
 assertTrue("Unresolved forward reference should be an instance of UnresolvedForwardReference", deserializedObjectIdReferenceProperty._forward instanceof UnresolvedForwardReference);
 }

 @Test
 public void testObjectIdReferencePropertyFixAccess() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 objectIdReferenceProperty.fixAccess(objectMapper.getConfig());
 assertNotNull("Forward property should have been fixed accessed", objectIdReferenceProperty._forward.getMember());
 }

 @Test
 public void testObjectIdReferencePropertyGetAnnotation() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 assertEquals("Annotation should be retrieved correctly", objectIdInfo, objectIdReferenceProperty.getAnnotation(ObjectIdInfo.class));
 }

 @Test
 public void testObjectIdReferencePropertyGetMember() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 AnnotatedMember member = objectIdReferenceProperty.getMember();
 assertEquals("Member should be retrieved correctly", forwardProperty.getName(), member.getName());
 }

 @Test
 public void testObjectIdReferencePropertyGetCreatorIndex() throws IOException {
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 assertEquals("Creator index should be retrieved correctly", forwardProperty.getCreatorIndex(), objectIdReferenceProperty.getCreatorIndex());
 }

 @Test
 public void testObjectIdReferencePropertyDeserializeAndSet() throws IOException {
 Object pojo = new TestPojo();
 jsonParser.setValue("id", "existingId");
 objectIdReferenceProperty.deserializeAndSet(jsonParser, objectMapper.getConfig(), pojo);
 assertEquals("Property value should be set correctly", "existingId", pojo.getId());
 }

 @Test
 public void testObjectIdReferencePropertyDeserializeSetAndReturnNull() throws IOException {
 Object pojo = new TestPojo();
 jsonParser.setValue("id", null);
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, objectMapper.getConfig(), pojo);
 assertNull("Result should be null", result);
 }

 @Test
 public void testObjectIdReferencePropertyDeserializeSetAndReturnUnresolvedForwardReference() throws IOException {
 Object pojo = new TestPojo();
 UnresolvedForwardReference reference = new UnresolvedForwardReference();
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 reference.setRoid(new ReferringObjectId(pojo.getClass(), pojo));
 objectIdReferenceProperty._forward = reference;
 jsonParser.setValue("id", "existingId");
 Object result = objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, objectMapper.getConfig(), pojo);
 assertNull("Result should be null", result);
 assertEquals("Referring object id should be updated", pojo.getClass(), reference.getRoid().getType());
 }

 @Test
 public void testObjectIdReferencePropertySet() throws IOException {
 Object pojo = new TestPojo();
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 objectIdReferenceProperty.set(pojo, "existingId");
 assertEquals("Property value should be set correctly", "existingId", pojo.getId());
 }

 @Test
 public void testObjectIdReferencePropertySetAndReturn() throws IOException {
 Object pojo = new TestPojo();
 ObjectIdReferenceProperty objectIdReferenceProperty = new ObjectIdReferenceProperty(forwardProperty, objectIdInfo);
 Object result = objectIdReferenceProperty.setAndReturn(pojo, "existingId");
 assertEquals("Result should be the updated pojo", pojo, result);
 }
}

class TestPojo {
 private String id;

 public String getId() {
 return id;
 }

 public void setId(String id) {
 this.id = id;
 }
}

class TestSettableBeanProperty extends SettableBeanProperty {
 @Override
 public String getName() {
 return "name";
 }
}

class TestJsonDeserializer extends JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "value";
 }
}

class TestJsonParser extends JsonParser {
 @Override
 public void nextValue() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public void nextTextValue() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public boolean nextBoolean() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public int nextIntValue() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public long nextLongValue() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public float nextFloatValue() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public double nextDoubleValue() throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public void nextValue(Class<?> aClass) throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public <T> T nextValue(Class<T> aClass) throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public Object nextValue() throws IOException {
 throw new UnsupportedOperationException();
 }
}

class TestNullValueProvider implements NullValueProvider {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return null;
 }
}

class ReferringObjectId extends Referring {
 private Class<?> _type;
 private Object _referringObj;

 public ReferringObjectId(Class<?> type, Object referringObj) {
 _type = type;
 _referringObj = referringObj;
 }

 public Class<?> getType() {
 return _type;
 }
}