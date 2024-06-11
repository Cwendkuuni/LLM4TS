package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NamedType;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Unit tests for {@link StdTypeResolverBuilder}.
 */
public class StdTypeResolverBuilderTest {

 @Mock
 private MapperConfig<?> mapperConfig;
 @Mock
 private JavaType baseType;
 @Mock
 private Collection<NamedType> subtypes;
 @InjectMocks
 private StdTypeResolverBuilder instance;

 @Test
 public void testInitialization() {
 // Test empty initialization
 assertNull(instance.getDefaultImpl());
 assertNull(instance.getTypeProperty());
 assertFalse(instance.isTypeIdVisible());
 // Test noTypeInfoBuilder() initialization
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertSame(JsonTypeInfo.Id.NONE, noTypeInfoBuilder._idType);
 assertNull(noTypeInfoBuilder._customIdResolver);
 }

 @Test
 public void testInit() {
 // Test null arguments
 JsonTypeInfo.Id idType = null;
 TypeIdResolver idRes = null;
 when(mapperConfig.findValueTypeSerializer(baseType)).thenReturn(null);
 when(mapperConfig.findValueDeserializer(baseType)).thenReturn(null);
 try {
 instance.init(idType, idRes);
 } catch (IllegalArgumentException e) {
 // expected
 }
 // Test valid arguments
 idType = JsonTypeInfo.Id.CLASS;
 idRes = mock(TypeIdResolver.class);
 when(mapperConfig.findValueTypeSerializer(baseType)).thenReturn(null);
 when(mapperConfig.findValueDeserializer(baseType)).thenReturn(null);
 instance.init(idType, idRes);
 assertSame(idType, instance._idType);
 assertSame(idRes, instance._customIdResolver);
 assertNull(instance.getDefaultImpl());
 assertNull(instance.getTypeProperty());
 assertFalse(instance.isTypeIdVisible());
 }

 @Test
 public void testBuildTypeSerializer() {
 // Test null arguments
 assertNull(instance.buildTypeSerializer(null, null, null));
 // Test no _idType
 instance.init(JsonTypeInfo.Id.NONE, null);
 assertNull(instance.buildTypeSerializer(mapperConfig, baseType, subtypes));
 // Test valid arguments
 instance.init(JsonTypeInfo.Id.CLASS, null);
 when(mapperConfig.getTypeFactory().constructType(String.class)).thenReturn(String.class);
 TypeSerializer result = instance.buildTypeSerializer(mapperConfig, baseType, subtypes);
 assertNotNull(result);
 assertSame(String.class, result.getType());
 }

 @Test
 public void testBuildTypeDeserializer() {
 // Test null arguments
 assertNull(instance.buildTypeDeserializer(null, null, null));
 // Test no _idType
 instance.init(JsonTypeInfo.Id.NONE, null);
 assertNull(instance.buildTypeDeserializer(mapperConfig, baseType, subtypes));
 // Test valid arguments
 instance.init(JsonTypeInfo.Id.CLASS, null);
 TypeDeserializer result = instance.buildTypeDeserializer(mapperConfig, baseType, subtypes);
 assertNotNull(result);
 }

 @Test
 public void testInclusion() {
 // Test null argument
 JsonTypeInfo.As includeAs = null;
 try {
 instance.inclusion(includeAs);
 } catch (IllegalArgumentException e) {
 // expected
 }
 // Test valid arguments
 instance.init(JsonTypeInfo.Id.CLASS, null);
 instance.inclusion(JsonTypeInfo.As.PROPERTY);
 assertSame(JsonTypeInfo.As.PROPERTY, instance._includeAs);
 }

 @Test
 public void testTypeProperty() {
 // Test null argument
 String typeIdPropName = null;
 try {
 instance.typeProperty(typeIdPropName);
 } catch (IllegalArgumentException e) {
 // expected
 }
 // Test empty argument
 typeIdPropName = "";
 instance.typeProperty(typeIdPropName);
 assertSame("", instance.getTypeProperty());
 // Test valid argument
 instance.init(JsonTypeInfo.Id.CLASS, null);
 instance.typeProperty("test");
 assertSame("test", instance.getTypeProperty());
 }

 @Test
 public void testDefaultImpl() {
 // Test null argument
 Class<?> defaultImpl = null;
 try {
 instance.defaultImpl(defaultImpl);
 } catch (IllegalArgumentException e) {
 // expected
 }
 // Test valid argument
 instance.init(JsonTypeInfo.Id.CLASS, null);
 instance.defaultImpl(String.class);
 assertSame(String.class, instance.getDefaultImpl());
 }

 @Test
 public void testTypeIdVisibility() {
 // Test null argument
 boolean isVisible = null;
 try {
 instance.typeIdVisibility(isVisible);
 } catch (IllegalArgumentException e) {
 // expected
 }
 // Test valid argument
 instance.init(JsonTypeInfo.Id.CLASS, null);
 instance.typeIdVisibility(true);
 assertTrue(instance.isTypeIdVisible());
 }

 @Test
 public void testCustomIdResolver() {
 // Test null argument
 TypeIdResolver idResolver = null;
 try {
 instance.init(JsonTypeInfo.Id.CLASS, idResolver);
 } catch (IllegalArgumentException e) {
 // expected
 }
 // Test valid argument
 TypeIdResolver mockIdResolver = mock(TypeIdResolver.class);
 instance.init(JsonTypeInfo.Id.CLASS, mockIdResolver);
 assertSame(mockIdResolver, instance._customIdResolver);
 }

 @Test
 public void testIdResolver() {
 // Test null arguments
 assertNull(instance.idResolver(null, null, null, true, false));
 // Test no _idType
 instance.init(JsonTypeInfo.Id.NONE, null);
 assertNull(instance.idResolver(mapperConfig, baseType, subtypes, true, false));
 // Test valid arguments
 instance.init(JsonTypeInfo.Id.CLASS, null);
 when(mapperConfig.getTypeFactory().constructType(String.class)).thenReturn(String.class);
 TypeIdResolver result = instance.idResolver(mapperConfig, baseType, subtypes, true, false);
 assertNotNull(result);
 }
}