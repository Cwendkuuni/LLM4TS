package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class SetterlessPropertyTest {

 private SetterlessProperty setterlessProperty;
 private Object testInstance;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
 testInstance = Mockito.spy(Mockito.mock(Object.class));
 SettableBeanProperty.Std propertyDef = new SettableBeanProperty.Std(
 new BeanPropertyDefinition("testProperty", String.class),
 Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class),
 Mockito.mock(Annotations.class),
 Mockito.mock(AnnotatedMethod.class));
 Method getter = Mockito.mock(Method.class);
 setterlessProperty = new SetterlessProperty(propertyDef, Mockito.mock(JavaType.class),
 Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), getter);
 }

 @Test
 public void testGetAnnotation() throws IOException {
 Annotation annotation = Mockito.mock(Annotation.class);
 Mockito.when(getter.getAnnotated().getAnnotation(Annotation.class)).thenReturn(annotation);
 assertEquals(annotation, setterlessProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String testValue = "Test Value";
 JsonParser mockParser = Mockito.mock(JsonParser.class);
 Mockito.when(mockParser.getValueAsString()).thenReturn(testValue);
 Mockito.when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
 Object testCollection = Mockito.mock(Object.class);
 Mockito.when(testInstance.getClass()).thenReturn(Object.class);
 Mockito.doReturn(testCollection).when(testInstance).getClass();
 Mockito.doReturn(testValue).when(testInstance).toString();
 Mockito.when(getter.invoke(testInstance, (Object[]) null)).thenReturn(testCollection);

 setterlessProperty.deserializeAndSet(mockParser, Mockito.mock(DeserializationContext.class), testInstance);
 assertEquals(testValue, testCollection);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 setterlessProperty.set(testInstance, "Test Value");
 }
}