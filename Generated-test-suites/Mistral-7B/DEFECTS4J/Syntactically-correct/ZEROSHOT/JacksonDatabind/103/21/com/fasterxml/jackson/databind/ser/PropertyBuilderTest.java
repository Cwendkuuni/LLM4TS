package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private SerializationConfig config;
 private BeanDescription beanDescription;

 @Before
 public void setUp() {
 config = new SerializationConfig(new AnnotationIntrospector());
 beanDescription = new BeanDescription(Object.class);
 propertyBuilder = new PropertyBuilder(config, beanDescription);
 }

 @Test
 public void testGetClassAnnotations() {
 // Test getting class annotations from PropertyBuilder
 assertNotNull(propertyBuilder.getClassAnnotations());
 }

 @Test
 public void testBuildWriter() throws JsonMappingException {
 // Test building BeanPropertyWriter using PropertyBuilder
 Class<?> propertyType = String.class;
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", propertyType);
 JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(propertyType);
 JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
 TypeSerializer typeSer = null;
 TypeSerializer contentTypeSer = null;
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 boolean defaultUseStaticTyping = true;

 BeanPropertyWriter actual = propertyBuilder.buildWriter(new SerializerProvider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

 assertEquals(propDef, actual.getPropertyDefinition());
 assertEquals(am, actual.getMember());
 assertNull(actual.getNames());
 assertEquals(propertyType, actual.getDeclaredType());
 assertEquals(ser, actual.getSerializer());
 assertNull(actual.getTypeSerializer());
 assertNull(actual.getContentTypeSerializer());
 assertEquals(false, actual.suppressNulls());
 assertNull(actual.getValueToSuppress());
 }

 @Test
 public void testFindSerializationType() throws JsonMappingException {
 // Test finding serialization type using PropertyBuilder
 Annotated annotation = Mockito.mock(Annotated.class);
 JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(String[].class);

 JavaType actual = propertyBuilder.findSerializationType(annotation, true, declaredType);

 assertEquals(declaredType, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeWithInvalidType() throws JsonMappingException {
 // Test finding serialization type with invalid type using PropertyBuilder
 Annotated annotation = Mockito.mock(Annotated.class);
 JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(int.class);

 propertyBuilder.findSerializationType(annotation, true, declaredType);
 }

 @Test
 public void testGetDefaultBean() {
 // Test getting default bean value from PropertyBuilder
 Object actual = propertyBuilder.getDefaultBean();

 assertNull(actual);
 }

 @Test
 public void testGetPropertyDefaultValue() {
 // Test getting property default value from PropertyBuilder
 String propName = "testProperty";
 AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
 JavaType type = ObjectTypeFactory.defaultInstance().constructType(String.class);

 Object actual = propertyBuilder.getPropertyDefaultValue(propName, member, type);

 assertNull(actual);
 }

 @Test
 public void testGetDefaultValue() {
 // Test getting default value from PropertyBuilder
 JavaType type = ObjectTypeFactory.defaultInstance().constructType(String.class);

 Object actual = propertyBuilder.getDefaultValue(type);

 assertEquals("", actual);
 }
}