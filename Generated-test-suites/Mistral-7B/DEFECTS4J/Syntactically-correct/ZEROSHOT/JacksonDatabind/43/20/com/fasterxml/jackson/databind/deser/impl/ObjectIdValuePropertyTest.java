package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerProvider.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ObjectIdValuePropertyTest {

 private static final ObjectIdGenerator generator = new ObjectIdGenerator() {
 @Override public ObjectId idFromString(String id) {
 return id;
 }
 };

 private static final ObjectIdResolver resolver = new ObjectIdResolver() {
 @Override public ObjectId idFromObject(Object id) {
 return new ObjectIdImpl(id.toString());
 }
 };

 @JsonId
 @ObjectIdGenerator(generationStrategy = ObjectIdGenerator.IdIntGenerationStrategy.ALWAYS)
 private static class TestClass {
 @JsonProperty
 private ObjectId id;
 private String name;

 // must be settable, otherwise ObjectIdValueProperty test will fail
 public void setId(ObjectId id) {
 this.id = id;
 }

 @ObjectIdResolver
 public ObjectId getId() {
 return id;
 }

 @BeanPropertyDefinition(name = "id", getter = "getId", setter = "setId", visibility = PropertyAccessor.ALL)
 public static BeanPropertyDefinition getIdPropertyDefinition() {
 return null;
 }
 }

 @ObjectIdProperty(propertyName = "id")
 private static class ObjectIdValuePropertyUnderTest extends SettableBeanProperty {
 public ObjectIdValuePropertyUnderTest(ObjectIdReader objectIdReader) {
 super(objectIdReader, new PropertyMetadata());
 }
 }

 @org.junit.Test
 public void testEmptyDeserialization() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectIdReader objectIdReader = new ObjectIdReader(new PropertyName("id"), ObjectIdValuePropertyUnderTest.class);
 ObjectIdValuePropertyUnderTest property = new ObjectIdValuePropertyUnderTest(objectIdReader);
 ObjectMapper.DefaultTyping hibConverter = new ObjectMapper.DefaultTyping();
 BeanDeserializerModifier modifier = new BeanDeserializerModifier();
 JsonDeserializer<?> idDeserializer = mapper.findValueDeserializer(ObjectId.class, null);
 MapperConfig.Builder configBuilder = mapper.getConfig().builder();
 configBuilder.withSerializerFinder(modifier.getSerializerFinder());
 configBuilder.withDeserializerFinder(modifier.getDeserializerFinder());
 ObjectMapper customMapper = configBuilder.build();
 TestClass testInstance = new TestClass();
 JsonParser jsonParser = mapper.getFactory().createParser("{}");
 JsonDeserializer<?> beanDeserializer = customMapper.deserializeValue(jsonParser, TestClass.class, hibConverter);
 assertNull(testInstance.getId());
 beanDeserializer.deserialize(jsonParser, testInstance, hibConverter);
 assertNull(testInstance.getId());
 }

 @org.junit.Test
 public void testDeserializationWithId() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectIdReader objectIdReader = new ObjectIdReader(new PropertyName("id"), ObjectIdValuePropertyUnderTest.class);
 ObjectIdValuePropertyUnderTest property = new ObjectIdValuePropertyUnderTest(objectIdReader);
 ObjectMapper.DefaultTyping hibConverter = new ObjectMapper.DefaultTyping();
 BeanDeserializerModifier modifier = new BeanDeserializerModifier();
 JsonDeserializer<?> idDeserializer = mapper.findValueDeserializer(ObjectId.class, null);
 MapperConfig.Builder configBuilder = mapper.getConfig().builder();
 configBuilder.withSerializerFinder(modifier.getSerializerFinder());
 configBuilder.withDeserializerFinder(modifier.getDeserializerFinder());
 ObjectMapper customMapper = configBuilder.build();
 TestClass testInstance = new TestClass();
 ObjectId id = new ObjectIdImpl("123");
 JsonParser jsonParser = mapper.getFactory().createParser("{\"id\":\"" + id + "\"}");
 JsonDeserializer<?> beanDeserializer = customMapper.deserializeValue(jsonParser, TestClass.class, hibConverter);
 ObjectId deserializedId = (ObjectId) beanDeserializer.deserialize(jsonParser, ObjectId.class, idDeserializer);
 assertEquals(id, deserializedId);
 TestClass deserializedInstance = (TestClass) beanDeserializer.deserialize(jsonParser, TestClass.class, hibConverter);
 TestClass expectedInstance = new TestClass();
 expectedInstance.setId(id);
 assertEquals(expectedInstance, deserializedInstance);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetNull() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectIdReader objectIdReader = new ObjectIdReader(new PropertyName("id"), ObjectIdValuePropertyUnderTest.class);
 ObjectIdValuePropertyUnderTest property = new ObjectIdValuePropertyUnderTest(objectIdReader);
 TestClass testInstance = new TestClass();
 testInstance.setId(null);
 property.setAndReturn(testInstance, null);
 fail("Should have thrown UnsupportedOperationException");
 }
}