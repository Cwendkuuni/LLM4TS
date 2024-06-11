package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

 private ObjectMapper objectMapper;
 private SimpleModule module;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 module = new SimpleModule();
 objectMapper.registerModule(module);
 }

 @Test
 public void testObjectIdReferenceProperty() throws JsonProcessingException {
 // Testing constructor with SettableBeanProperty and ObjectIdInfo
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(MyPojo.class, "id");
 SettableBeanProperty forward = new SimpleBeanProperty("forward", String.class);
 ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(forward, objectIdInfo);

 // Testing getters and setters
 MyPojo pojo = new MyPojo();
 pojo.setId("initialId");
 property.set(pojo, "newId");
 assertEquals("newId", pojo.getId());

 // Testing withName method
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdReferenceProperty newProperty = property.withName(newName);
 assertEquals(newName, newProperty.getName());

 // Testing withValueDeserializer method
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
 return jsonParser.getValueAsString();
 }
 };
 ObjectIdReferenceProperty propertyWithDeserializer = new ObjectIdReferenceProperty(property, deserializer, null);
 assertSame(deserializer, propertyWithDeserializer.getValueDeserializer());

 // Testing withNullProvider method
 NullValueProvider nullValueProvider = new JsonNull();
 ObjectIdReferenceProperty propertyWithNullProvider = new ObjectIdReferenceProperty(property, null, nullValueProvider);
 assertSame(nullValueProvider, propertyWithNullProvider.getNullProvider());

 // Testing fixAccess method
 DeserializationConfig config = new DeserializationConfig(objectMapper.getFactory());
 propertyWithNullProvider.fixAccess(config);
 AnnotatedMember member = AnnotationIntrospector.findAnnotation(propertyWithNullProvider, JsonDeserializer.class);
 assertNotNull(member);
 JsonDeserializer<?> jsonDeserializer = config.findValueDeserializer(MyPojo.class, "newName");
 assertSame(propertyWithNullProvider, jsonDeserializer);

 // Testing getAnnotation method
 Annotation annotation = propertyWithNullProvider.getAnnotation(JsonDeserialize.class);
 assertNotNull(annotation);

 // Testing getMember method
 AnnotatedMember annotatedMember = propertyWithNullProvider.getMember();
 assertNotNull(annotatedMember);

 // Testing getCreatorIndex method
 int index = propertyWithNullProvider.getCreatorIndex();
 assertEquals(1, index);

 // Testing deserializeAndSet method
 MyPojo pojoWithId = new MyPojo();
 pojoWithId.setId("existingId");
 ObjectReader reader = module.readerForClass(MyPojo.class);
 Object id = "newId";
 reader.setValue(pojoWithId, "id", id);
 propertyWithNullProvider.deserializeAndSet(reader, config, pojoWithId);
 assertEquals(id, pojoWithId.getId());

 // Testing deserializeSetAndReturn method
 MyPojo pojo = new MyPojo();
 Object idFromJson = "newIdFromJson";
 Object incomingPojo = new Object() {{
 setId(idFromJson);
 }};
 Object result = propertyWithNullProvider.deserializeSetAndReturn(objectMapper.readerForValue(incomingPojo), config, pojo);
 assertNull(result);
 assertEquals(idFromJson, pojo.getId());

 // Testing set method
 propertyWithNullProvider.set(pojo, "newIdFromSet");
 assertEquals("newIdFromSet", pojo.getId());

 // Testing setAndReturn method
 Object newPojo = new MyPojo();
 Object newId = "newIdFromSetAndReturn";
 Object resultFromSetAndReturn = propertyWithNullProvider.setAndReturn(newPojo, newId);
 assertSame(newPojo, resultFromSetAndReturn);
 assertEquals(newId, newPojo.getId());

 // Testing error scenarios
 try {
 propertyWithNullProvider.deserializeAndSet(null, config, pojo);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 propertyWithNullProvider.deserializeSetAndReturn(null, config, pojo);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 try {
 propertyWithNullProvider.deserializeSetAndReturn(objectMapper.readerForValue(new Object()), config, pojo);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 try {
 propertyWithNullProvider.deserializeSetAndReturn(objectMapper.readerForValue(new Object()), config, null);
 fail();
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 class MyPojo {
 private String id;

 public String getId() {
 return id;
 }

 public void setId(String id) {
 this.id = id;
 }
 }
}