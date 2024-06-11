package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testFindForCollection() throws JsonMappingException {
 JavaType typeSingletonSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.class);
 JavaType typeSingletonList = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);
 JavaType typeSingletonMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 JavaType typeUnmodifiableSet = TypeFactory.defaultInstance().constructType(Set.class, Boolean.class);
 JavaType typeUnmodifiableList = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);
 JavaType typeUnmodifiableMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 JavaType typeAsList = TypeFactory.defaultInstance().constructType(List.class, Boolean.class);

 DeserializationContext ctx = mock(DeserializationContext.class);
 when(ctx.findValueType(typeSingletonSet)).thenReturn(typeSingletonSet);
 when(ctx.findValueType(typeSingletonList)).thenReturn(typeSingletonList);
 when(ctx.findValueType(typeSingletonMap)).thenReturn(typeSingletonMap);
 when(ctx.findValueType(typeUnmodifiableSet)).thenReturn(typeUnmodifiableSet);
 when(ctx.findValueType(typeUnmodifiableList)).thenReturn(typeUnmodifiableList);
 when(ctx.findValueType(typeUnmodifiableMap)).thenReturn(typeUnmodifiableMap);
 when(ctx.findValueType(typeAsList)).thenReturn(typeAsList);

 Converter converter = JavaUtilCollectionsDeserializers.findForCollection(ctx, typeSingletonSet);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForCollection(ctx, typeSingletonList);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForCollection(ctx, typeSingletonMap);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForCollection(ctx, typeUnmodifiableSet);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForCollection(ctx, typeUnmodifiableList);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForCollection(ctx, typeUnmodifiableMap);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForCollection(ctx, typeAsList);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForCollection(ctx, new TypeReference<List<String>>() {}.getType());
 assertNull(converter);
 }

 @Test
 public void testFindForMap() throws JsonMappingException {
 JavaType typeSingletonMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
 JavaType typeUnmodifiableMap = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);

 DeserializationContext ctx = mock(DeserializationContext.class);
 when(ctx.findValueType(typeSingletonMap)).thenReturn(typeSingletonMap);
 when(ctx.findValueType(typeUnmodifiableMap)).thenReturn(typeUnmodifiableMap);

 Converter converter = JavaUtilCollectionsDeserializers.findForMap(ctx, typeSingletonMap);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForMap(ctx, typeUnmodifiableMap);
 assertNotNull(converter);
 assertEquals(StdDelegatingDeserializer.class, converter.getClass());

 converter = JavaUtilCollectionsDeserializers.findForMap(ctx, new TypeReference<Map<String, Integer>>() {}.getType());
 assertNull(converter);
 }

}