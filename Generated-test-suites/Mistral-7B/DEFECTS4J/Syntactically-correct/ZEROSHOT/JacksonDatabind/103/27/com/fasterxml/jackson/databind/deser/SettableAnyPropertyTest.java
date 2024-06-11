package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

 private SettableAnyProperty property;
 private Object target;

 @Before
 public void setUp() throws Exception {
 target = new HashMap<>();
 ObjectIdReader idReader = new SimpleObjectIdReader();
 JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>();
 TypeDeserializer valueTypeDeserializer = new StandardTypeDeserializer();
 KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
 property = new SettableAnyProperty(new BeanProperty(new AnnotatedField(null, "test", null, null), null, null),
 new AnnotatedField(null, "test", null, null), Object.class, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String json = "{\"test\":1}";
 JsonParser parser = new JsonParser(new StringReader(json));
 property.deserializeAndSet(parser, new DeserializationContext(), target, "test");
 assertEquals(1, target.getClass().cast(target).get("test"));
 }

 @Test(expected = IOException.class)
 public void testUnresolvedForwardReference() throws IOException {
 String json = "{\"test\":[]}";
 JsonParser parser = new JsonParser(new StringReader(json));
 property = new SettableAnyProperty(new BeanProperty(new AnnotatedField(null, "test", null, null), null, null),
 new AnnotatedField(null, "test", null, null), Object.class, null, null, null);
 property.deserializeAndSet(parser, new DeserializationContext(), target, "test");
 fail("Should throw IOException for unresolved forward reference.");
 }

 @Test
 public void testSet() throws IOException {
 property.set(target, "test", "value");
 assertEquals("value", target.getClass().cast(target).get("test"));
 }

 @Test
 public void testDeserialize() throws IOException {
 String json = "1";
 JsonParser parser = new JsonParser(new StringReader(json));
 Object result = property.deserialize(parser, new DeserializationContext());
 assertEquals(1, result);
 }

 @Test
 public void testWithValueDeserializer() throws IOException {
 JsonDeserializer<Object> customValueDeserializer = new CustomValueDeserializer();
 property = property.withValueDeserializer(customValueDeserializer);
 JsonParser parser = new JsonParser(new StringReader("{\"test\":\"customValue\"}"));
 Object result = property.deserialize(parser, new DeserializationContext());
 assertEquals("customValue", result);
 }

 @Test
 public void testFixAccess() throws IOException {
 property.fixAccess(new DeserializationConfig());
 AnnotatedField setter = (AnnotatedField) property._setter;
 verify(setter).setAccess(eq(true));
 }

 @Test
 public void testJdkSerialization() throws IOException {
 Object serialized = new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(property);
 Object deserialized = new ObjectInputStream(new ByteArrayInputStream((byte[]) serialized)).readObject();
 assertEquals(property.getProperty(), deserialized.getProperty());
 }
}

 class CustomValueDeserializer extends StdDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "customValue";
 }
}