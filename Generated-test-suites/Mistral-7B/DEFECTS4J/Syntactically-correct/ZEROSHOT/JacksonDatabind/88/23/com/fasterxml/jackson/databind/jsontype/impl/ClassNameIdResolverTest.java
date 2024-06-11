package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.*;

/**
 * Test class for {@link ClassNameIdResolver}.
 */
public class ClassNameIdResolverTest {

 private static final Object DEFAULT_VALUE = new Object();
 private static final Class<?> DEFAULT_CLASS = Object.class;
 private static final TypeFactory typeFactory = new TypeFactoryBuilder()
         .build();
 private static final DatabindContext context = new JacksonAnnotationIntrospector()
         .findInsertableValueType(DEFAULT_CLASS, null, null);
 private ClassNameIdResolver resolver;

 @Before
 public void setUp() throws Exception {
     resolver = new ClassNameIdResolver(DefaultTypeBase.construct(), typeFactory);
 }

 @Test
 public void testGetMechanism() {
     assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
 }

 @Test
 public void testIdFromValue() throws Exception {
     String id = resolver.idFromValue(DEFAULT_VALUE);
     assertTrue(id.startsWith("class "));
     assertEquals(DEFAULT_CLASS.getName(), id.substring(8));
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
     Object value = new Object();
     Class<?> type = String.class;
     String id = resolver.idFromValueAndType(value, type);
     assertTrue(id.startsWith("class "));
     assertEquals(type.getName(), id.substring(8));
 }

 @Test
 public void testTypeFromId() throws IOException {
     String id = "java.lang.String";
     JavaType javaType = resolver.typeFromId(context, id);
     assertEquals(String.class, javaType.getRawClass());
 }

 @Test
 public void testTypeFromIdInvalidClass() throws IOException {
     String id = "com.nonexistent.package.ClassName";
     JavaType javaType = resolver.typeFromId(context, id);
     assertNull(javaType);
 }

 @Test
 public void testTypeFromIdWithGenericType() throws IOException {
     String id = "java.util.List<java.lang.String>";
     JavaType javaType = resolver.typeFromId(context, id);
     assertEquals(List.class, javaType.getRawClass());
     assertEquals(String.class, javaType.getParameterType(0));
 }

 @Test
 public void testTypeFromIdWithEnumType() throws IOException {
     String id = "java.lang.Enum";
     JavaType javaType = resolver.typeFromId(context, id);
     assertNotNull(javaType);
     assertTrue(Enum.class.isAssignableFrom(javaType.getRawClass()));
 }

 @Test
 public void testTypeFromIdWithEnumSubclass() throws IOException {
     String id = "com.test.package.MyEnum";
     JavaType javaType = resolver.typeFromId(context, id);
     assertNotNull(javaType);
     assertTrue(Enum.class.isAssignableFrom(javaType.getRawClass()));
 }

 @Test
 public void testIdFromValueEnumSet() throws Exception {
     EnumSet<Enum> enumSet = EnumSet.allOf(Enum.class);
     String id = resolver.idFromValue(enumSet);
     assertTrue(id.startsWith("java.util.EnumSet"));
     assertTrue(id.endsWith(Enum.class.getName()));
 }

 @Test
 public void testIdFromValueEnumMap() throws Exception {
     EnumMap<Enum, String> enumMap = new EnumMap<>(Enum.class);
     String id = resolver.idFromValue(enumMap);
     assertTrue(id.startsWith("java.util.EnumMap"));
     assertTrue(id.endsWith(Object.class.getName()));
     assertTrue(id.contains("Enum"));
     assertTrue(id.contains("String"));
 }

 @Test
 public void testIdFromValueArraysAsList() throws Exception {
     Object[] array = new Object[1];
     array[0] = new Integer(1);
     String id = resolver.idFromValue(Arrays.asList(array));
     assertTrue(id.startsWith("java.util.ArrayList"));
     assertTrue(id.contains("Integer"));
 }

 @Test
 public void testIdFromValueCollectionsArrayList() throws Exception {
     List<Object> list = Collections.singletonList(new Object());
     String id = resolver.idFromValue(list);
     assertTrue(id.startsWith("java.util.ArrayList"));
 }

 @Test
 public void testIdFromValueArrayType() throws Exception {
     Object[] array = new Object[1];
     array[0] = new Integer(1);
     String id = resolver.idFromValue(array);
     assertTrue(id.startsWith("java.lang.Object[]"));
 }

 @Test
 public void testIdFromValueArrayTypeTwoDimensions() throws Exception {
     Object[][] array = new Object[1][1];
     array[0][0] = new Integer(1);
     String id = resolver.idFromValue(array);
     assertTrue(id.startsWith("java.lang.Object[][]"));
 }
}