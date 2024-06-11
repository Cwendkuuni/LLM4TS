package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class CreatorPropertyTest {

 private Object testInstance;
 private Object testValue;
 private CreatorProperty creatorProperty;
 private JsonDeserializer<Object> jsonDeserializer;
 private NullValueProvider nullValueProvider;
 private DeserializationContext deserializationContext;
 private AnnotatedParameter annotatedParameter;

 @Before
 public void setUp() throws Exception {
 testInstance = new Object() {};
 testValue = "testValue";
 jsonDeserializer = mock(JsonDeserializer.class);
 nullValueProvider = mock(NullValueProvider.class);
 deserializationContext = mock(DeserializationContext.class);
 annotatedParameter = mock(AnnotatedParameter.class);
 creatorProperty = new CreatorProperty(new PropertyName("testProperty"),
 JsonTypeFactory.instance.constructType(Object.class),
 null, null, new Annotations(), annotatedParameter, 0, null,
 PropertyMetadata.STANDARD_IMPLICIT_ONLY);
 }

 @Test
 public void testConstructor() throws Exception {
 // Test default constructor
 CreatorProperty creatorPropertyDefault = new CreatorProperty(null, null, null, null, null, null,
 0, null, PropertyMetadata.STANDARD_IMPLICIT_ONLY);
 assertEquals("testProperty", creatorPropertyDefault.getName());
 assertNull(creatorPropertyDefault.getType());
 assertNull(creatorPropertyDefault.getWrapperName());
 assertNull(creatorPropertyDefault.getTypeDeserializer());
 assertNull(creatorPropertyDefault.getContextAnnotations());
 assertNull(creatorPropertyDefault._annotated);
 assertNull(creatorPropertyDefault._injectableValueId);
 assertNull(creatorPropertyDefault._fallbackSetter);
 assertNull(creatorPropertyDefault._creatorIndex);
 assertFalse(creatorPropertyDefault.isIgnorable());

 // Test constructor with parameters
 CreatorProperty creatorPropertyWithParams = new CreatorProperty(new PropertyName("testProperty"),
 JsonTypeFactory.instance.constructType(Object.class),
 null, null, new Annotations(), annotatedParameter, 0, null,
 PropertyMetadata.STANDARD_IMPLICIT_ONLY, jsonDeserializer, nullValueProvider);
 assertEquals("testProperty", creatorPropertyWithParams.getName());
 assertEquals(JsonTypeFactory.instance.constructType(Object.class), creatorPropertyWithParams.getType());
 assertNull(creatorPropertyWithParams.getWrapperName());
 assertNull(creatorPropertyWithParams.getTypeDeserializer());
 assertSame(new Annotations(), creatorPropertyWithParams.getContextAnnotations());
 assertSame(annotatedParameter, creatorPropertyWithParams._annotated);
 assertNull(creatorPropertyWithParams._injectableValueId);
 assertNull(creatorPropertyWithParams._fallbackSetter);
 assertNull(creatorPropertyWithParams._creatorIndex);
 assertFalse(creatorPropertyWithParams.isIgnorable());
 }

 @Test
 public void testCopyConstructor() throws Exception {
 CreatorProperty creatorPropertyToCopy = new CreatorProperty(new PropertyName("testProperty"),
 JsonTypeFactory.instance.constructType(Object.class),
 null, null, new Annotations(), annotatedParameter, 0, null,
 PropertyMetadata.STANDARD_IMPLICIT_ONLY);
 CreatorProperty copy = new CreatorProperty(creatorPropertyToCopy, new PropertyName("newName"));
 assertEquals("newName", copy.getName());
 assertEquals(creatorPropertyToCopy.getName(), copy.getName());
 assertEquals(creatorPropertyToCopy.getType(), copy.getType());
 assertSame(creatorPropertyToCopy.getTypeDeserializer(), copy.getTypeDeserializer());
 assertSame(creatorPropertyToCopy.getContextAnnotations(), copy.getContextAnnotations());
 assertSame(creatorPropertyToCopy._annotated, copy._annotated);
 assertNull(copy._injectableValueId);
 assertNull(copy._fallbackSetter);
 assertNull(copy._creatorIndex);
 assertFalse(copy.isIgnorable());
 }

 @Test
 public void testWithName() throws Exception {
 SettableBeanProperty settableBeanProperty = mock(SettableBeanProperty.class);
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("testProperty"),
 JsonTypeFactory.instance.constructType(Object.class),
 null, null, new Annotations(), annotatedParameter, 0, null,
 PropertyMetadata.STANDARD_IMPLICIT_ONLY);
 CreatorProperty newCreatorProperty = creatorProperty.withName(new PropertyName("newName"));
 assertEquals("newName", newCreatorProperty.getName());
 assertNotSame(creatorProperty, newCreatorProperty);
 }

 @Test
 public void testWithValueDeserializer() throws Exception {
 JsonDeserializer<Object> newJsonDeserializer = mock(JsonDeserializer.class);
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("testProperty"),
 JsonTypeFactory.instance.constructType(Object.class),
 null, null, new Annotations(), annotatedParameter, 0, null,
 PropertyMetadata.STANDARD_IMPLICIT_ONLY);
 CreatorProperty newCreatorProperty = creatorProperty.withValueDeserializer(newJsonDeserializer);
 assertEquals(newJsonDeserializer, newCreatorProperty.getValueDeserializer());
 assertSame(creatorProperty, newCreatorProperty.getBackReference());
 }

 @Test public void testWithNullProvider() throws Exception {
 NullValueProvider newNullValueProvider = mock(NullValueProvider.class);
 CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("testProperty"),
 JsonTypeFactory.instance.constructType(Object.class),
 null, null, new Annotations(), annotatedParameter, 0, null,
 PropertyMetadata.STANDARD_IMPLICIT_ONLY);
 CreatorProperty newCreatorProperty = creatorProperty.withNullProvider(newNullValueProvider);
 assertEquals(newNullValueProvider, newCreatorProperty.getNullProvider());
 assertSame(creatorProperty, newCreatorProperty.getBackReference());
 }

 @Test
 public void testFixAccess() throws Exception {
 creatorProperty.fixAccess(deserializationContext);
 verify(deserializationContext).fixAccess(creatorProperty._fallbackSetter);
 }

 @Test
 public void testSetFallbackSetter() throws Exception {
 SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
 creatorProperty.setFallbackSetter(fallbackSetter);
 assertSame(fallbackSetter, creatorProperty._fallbackSetter);
 }

 @Test
 public void testMarkAsIgnorable() throws Exception {
 creatorProperty.markAsIgnorable();
 assertTrue(creatorProperty.isIgnorable());
 }

 @Test
 public void testIsIgnorable() throws Exception {
 assertFalse(creatorProperty.isIgnorable());
 }

 @Test
 public void testFindInjectableValue() throws Exception {
 Object injectedValue = new Object();
 when(deserializationContext.findInjectableValue(eq(annotatedParameter.getId()),
 eq(creatorProperty), eq(testInstance))).thenReturn(injectedValue);
 assertEquals(injectedValue, creatorProperty.findInjectableValue(deserializationContext, testInstance));
 }

 @Test
 public void testInject() throws Exception {
 Object beanInstance = testInstance;
 Object injectedValue = new Object();
 when(deserializationContext.findInjectableValue(eq(annotatedParameter.getId()),
 eq(creatorProperty), eq(beanInstance))).thenReturn(injectedValue);
 creatorProperty.inject(deserializationContext, beanInstance);
 assertEquals(injectedValue, testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String json = "{\"testProperty\":\"testValue\"}";
 ObjectReader objectReader = new ObjectMapper().readerFor(testInstance.getClass());
 Object readValue = objectReader.readValue(new StringReader(json), testInstance.getClass());
 creatorProperty.deserializeAndSet(objectReader.getParser(), deserializationContext, testInstance);
 assertEquals(testValue, testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 String json = "{\"testProperty\":\"testValue\"}";
 ObjectReader objectReader = new ObjectMapper().readerFor(testInstance.getClass());
 Object readValue = objectReader.readValue(new StringReader(json), testInstance.getClass());
 assertEquals(testValue, creatorProperty.deserializeSetAndReturn(objectReader.getParser(),
 deserializationContext, testInstance));
 assertEquals(testValue, testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test
 public void testSet() throws Exception {
 creatorProperty.set(testInstance, testValue);
 assertEquals(testValue, testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test
 public void testSetAndReturn() throws Exception {
 assertEquals(testValue, creatorProperty.setAndReturn(testInstance, testValue));
 assertEquals(testValue, testInstance.getClass().getField("testProperty").get(testInstance));
 }

 @Test(expected = IOException.class)
 public void testReportMissingSetter() throws Exception {
 creatorProperty.deserializeAndSet(mock(JsonParser.class), deserializationContext, testInstance);
 }
}