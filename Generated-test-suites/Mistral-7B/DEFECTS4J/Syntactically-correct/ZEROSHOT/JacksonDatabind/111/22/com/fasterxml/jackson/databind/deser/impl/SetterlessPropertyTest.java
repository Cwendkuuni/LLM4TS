package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class SetterlessPropertyTest {

 private static class TestClass {
 @JsonProperty("property")
 private List<Integer> list;

 @JsonGetter("property")
 public List<Integer> getProperty() {
 return list;
 }
 }

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 ObjectMapper mapper = new ObjectMapper();
 BeanDescription description = mapper.findProperty("property", TestClass.class);
 SettableBeanProperty property = (SettableBeanProperty) description.findProperty("property");
 SetterlessProperty setterlessProperty = (SetterlessProperty) property;
 TestClass testClass = new TestClass();
 setterlessProperty.setValueDeserializer(mock(JsonDeserializer.class));
 testClass.list = new ArrayList<>();
}

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn("property");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(jsonParser.getValueAsString()).thenReturn("[1, 2, 3]");
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 DeserializationContext context = mock(DeserializationContext.class);
 when(context.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));
 when(context.findBeanDescriptionForSerialization(TestClass.class)).thenReturn(mock(BeanDescription.class));
 when(context.findValueDeserializer(Integer[].class)).thenReturn(mock(JsonDeserializer.class));
 when(context.findTypeDeserializer(List.class, null)).thenReturn(mock(TypeDeserializer.class));
 when(context.findAnnotationFunctions(TestClass.class)).thenReturn(mock(Annotations.class));
 when(context.findBeanDescriptionForDeserialization(TestClass.class)).thenReturn(description);

 SetterlessProperty setterlessProperty = (SetterlessProperty) description.findProperty("property");
 setterlessProperty.deserializeAndSet(jsonParser, context, testClass);

 List<Integer> expectedList = new ArrayList<>();
 expectedList.add(1);
 expectedList.add(2);
 expectedList.add(3);
 assertEquals(expectedList, testClass.getProperty());
 }

 @Test
 public void testDeserializeAndSetWithNullValue() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.getCurrentName()).thenReturn("property");
 when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 DeserializationContext context = mock(DeserializationContext.class);

 SetterlessProperty setterlessProperty = (SetterlessProperty) description.findProperty("property");
 setterlessProperty.deserializeAndSet(jsonParser, context, testClass);

 assertNull(testClass.getProperty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 TestClass testClass = new TestClass();
 SetterlessProperty setterlessProperty = (SetterlessProperty) description.findProperty("property");
 setterlessProperty.set(testClass, new ArrayList<>());
 }
}