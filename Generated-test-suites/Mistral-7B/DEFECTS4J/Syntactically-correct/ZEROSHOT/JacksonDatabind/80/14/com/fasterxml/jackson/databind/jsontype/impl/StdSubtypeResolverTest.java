package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.ser.impl.TypeSerializerBase;

public class StdSubtypeResolverTest {

 private SubtypeResolver subtypeResolver;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 subtypeResolver = new StdSubtypeResolver();
 objectMapper = new ObjectMapper();
 objectMapper.configure(MapperFeature.AUTO_DETECT_TYPES, false);
 }

 @Test
 public void testRegisterSubtypes() {
 // Test registering subtypes using the overloaded method that accepts classes
 Class<?> baseType = String.class;
 Class<?> subType1 = "com.test.SubType1".getClass();
 Class<?> subType2 = "com.test.SubType2".getClass();
 subtypeResolver.registerSubtypes(subType1, subType2);

 Class<?>[] subTypes = { subType1, subType2 };
 subtypeResolver.registerSubtypes(subTypes);

 HashSet<Class<?>> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
 assertEquals(2, registeredSubtypes.size());
 assertTrue(registeredSubtypes.contains(subType1));
 assertTrue(registeredSubtypes.contains(subType2));
 }

 @Test
 public void testRegisterSubtypesByName() {
 // Test registering subtypes using the NamedType constructor
 Class<?> baseType = String.class;
 String subType1Name = "SubType1";
 String subType2Name = "SubType2";
 Class<?> subType1 = "com.test.SubType1".getClass();
 Class<?> subType2 = "com.test.SubType2".getClass();
 NamedType subType1Named = new NamedType(subType1, subType1Name);
 NamedType subType2Named = new NamedType(subType2, subType2Name);

 subtypeResolver.registerSubtypes(subType1Named, subType2Named);

 HashSet<Class<?>> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
 assertEquals(2, registeredSubtypes.size());
 assertTrue(registeredSubtypes.contains(subType1));
 assertTrue(registeredSubtypes.contains(subType2));

 // Test registering subtypes using the overloaded method that accepts NamedType objects
 NamedType[] namedSubtypes = { subType1Named, subType2Named };
 subtypeResolver.registerSubtypes(namedSubtypes);

 assertEquals(2, subtypeResolver.getRegisteredSubtypes().size());
 }

 @Test
 public void testGetRegisteredSubtypes() {
 // Test that getRegisteredSubtypes returns an empty set if no subtypes have been registered
 assertEquals(0, subtypeResolver.getRegisteredSubtypes().size());
 }

 @Test
 public void testResolveSubtypesByClass() {
 Class<?> baseType = String.class;
 Class<?> subType1 = "com.test.SubType1".getClass();
 Class<?> subType2 = "com.test.SubType2".getClass();

 // Test resolving subtypes when no subtypes have been registered
 assertEquals(0, subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseType).size());

 // Test resolving subtypes when subtypes have been registered by name
 subtypeResolver.registerSubtypesByName(subType1, subType2);
 assertEquals(2, subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseType).size());

 // Test resolving subtypes when subtypes have been registered by class
 subtypeResolver.registerSubtypes(subType1, subType2);
 assertEquals(2, subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseType).size());

 // Test resolving subtypes when base type is annotated
 @JsonTypeName("BaseType")
 @JsonSubTypes({
 @JsonSubTypes.Type(value = SubType1.class, name = "SubType1"),
 @JsonSubTypes.Type(value = SubType2.class, name = "SubType2")
 })
 static class BaseType {
 }

 Class<?> baseTypeAnnotated = BaseType.class;
 assertEquals(2, subtypeResolver.collectAndResolveSubtypesByClass(objectMapper.getConfig(), null, baseTypeAnnotated).size());
 }

 @Test
 public void testResolveSubtypesByTypeId() {
 Class<?> baseType = String.class;
 Class<?> subType1 = "com.test.SubType1".getClass();
 Class<?> subType2 = "com.test.SubType2".getClass();

 // Test resolving subtypes when no subtypes have been registered
 assertEquals(0, subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), null, baseType).size());

 // Test resolving subtypes when subtypes have been registered by name
 subtypeResolver.registerSubtypesByName(subType1, subType2);
 assertEquals(2, subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), null, baseType).size());

 // Test resolving subtypes when subtypes have been registered by class
 subtypeResolver.registerSubtypes(subType1, subType2);
 assertEquals(2, subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), null, baseType).size());

 // Test resolving subtypes when base type is annotated
 @JsonTypeName("BaseType")
 @JsonSubTypes({
 @JsonSubTypes.Type(value = SubType1.class, name = "SubType1"),
 @JsonSubTypes.Type(value = SubType2.class, name = "SubType2")
 })
 static class BaseType {
 }

 Class<?> baseTypeAnnotated = BaseType.class;
 assertEquals(2, subtypeResolver.collectAndResolveSubtypesByTypeId(objectMapper.getConfig(), null, baseTypeAnnotated).size());
 }

 @Test
 public void testCollectAndResolve() {
 Class<?> baseType = String.class;
 Class<?> subType1 = "com.test.SubType1".getClass();
 Class<?> subType2 = "com.test.SubType2".getClass();

 AnnotatedClass subType1Annotated = AnnotatedClass.construct(subType1, null, null);
 AnnotatedClass subType2Annotated = AnnotatedClass.construct(subType2, null, null);
 AnnotatedMember property = new AnnotatedField("test", subType1Annotated, null, null);
 AnnotatedClass baseTypeAnnotated = AnnotatedClass.construct(baseType, null, null);

 // Test collecting and resolving subtypes when no subtypes have been registered
 HashSet<NamedType> resolvedSubtypes = new HashSet<>();
 subtypeResolver.collectAndResolveSubtypes(baseTypeAnnotated, property, baseType, null, resolvedSubtypes, null);
 assertEquals(0, resolvedSubtypes.size());

 // Test collecting and resolving subtypes when subtypes have been registered by name
 subtypeResolver.registerSubtypesByName(subType1, subType2);
 resolvedSubtypes.clear();
 subtypeResolver.collectAndResolveSubtypes(baseTypeAnnotated, property, baseType, null, resolvedSubtypes, null);
 assertEquals(2, resolvedSubtypes.size());
 assertTrue(resolvedSubtypes.contains(new NamedType(subType1, "SubType1")));
 assertTrue(resolvedSubtypes.contains(new NamedType(subType2, "SubType2")));

 // Test collecting and resolving subtypes when subtypes have been registered by class
 subtypeResolver.registerSubtypes(subType1, subType2);
 resolvedSubtypes.clear();
 subtypeResolver.collectAndResolveSubtypes(baseTypeAnnotated, property, baseType, null, resolvedSubtypes, null);
 assertEquals(2, resolvedSubtypes.size());
 assertTrue(resolvedSubtypes.contains(new NamedType(subType1)));
 assertTrue(resolvedSubtypes.contains(new NamedType(subType2)));

 // Test collecting and resolving subtypes when base type is annotated
 @JsonTypeName("BaseType")
 @JsonSubTypes({
 @JsonSubTypes.Type(value = SubType1.class, name = "SubType1"),
 @JsonSubTypes.Type(value = SubType2.class, name = "SubType2")
 })
 static class BaseType {
 }

 resolvedSubtypes.clear();
 AnnotatedClass baseTypeAnnotatedWithSubtypes = AnnotatedClass.construct(BaseType.class, null, null);
 subtypeResolver.collectAndResolveSubtypes(baseTypeAnnotatedWithSubtypes, property, baseType, null, resolvedSubtypes, null);
 assertEquals(2, resolvedSubtypes.size());
 assertTrue(resolvedSubtypes.contains(new NamedType(SubType1.class, "SubType1")));
 assertTrue(resolvedSubtypes.contains(new NamedType(SubType2.class, "SubType2")));
 }

 @Test
 public void testCollectAndResolveByTypeId() {
 Class<?> baseType = String.class;
 Class<?> subType1 = "com.test.SubType1".getClass();
 Class<?> subType2 = "com.test.SubType2".getClass();

 AnnotatedClass subType1Annotated = AnnotatedClass.construct(subType1, null, null);
 AnnotatedClass subType2Annotated = AnnotatedClass.construct(subType2, null, null);
 AnnotatedClass baseTypeAnnotated = AnnotatedClass.construct(baseType, null, null);

 // Test collecting and resolving subtypes by type id when no subtypes have been registered
 Map<String, NamedType> resolvedSubtypes = new HashMap<>();
 subtypeResolver.collectAndResolveSubtypesByTypeId(baseTypeAnnotated, null, baseType, null, resolvedSubtypes);
 assertEquals(0, resolvedSubtypes.size());

 // Test collecting and resolving subtypes by type id when subtypes have been registered by name
 subtypeResolver.registerSubtypesByName(subType1, subType2);
 resolvedSubtypes.clear();
 subtypeResolver.collectAndResolveSubtypesByTypeId(baseTypeAnnotated, null, baseType, null, resolvedSubtypes);
 assertEquals(2, resolvedSubtypes.size());
 assertTrue(resolvedSubtypes.containsKey("SubType1"));
 assertTrue(resolvedSubtypes.containsKey("SubType2"));

 // Test collecting and resolving subtypes by type id when subtypes have been registered by class
 subtypeResolver.registerSubtypes(subType1, subType2);
 resolvedSubtypes.clear();
 subtypeResolver.collectAndResolveSubtypesByTypeId(baseTypeAnnotated, null, baseType, null, resolvedSubtypes);
 assertEquals(2, resolvedSubtypes.size());
 List<NamedType> subtypes = Arrays.asList(new NamedType(subType1), new NamedType(subType2));
 assertTrue(resolvedSubtypes.containsAll(subtypes));

 // Test collecting and resolving subtypes by type id when base type is annotated
 @JsonTypeName("BaseType")
 @JsonSubTypes({
 @JsonSubTypes.Type(value = SubType1.class, name = "SubType1"),
 @JsonSubTypes.Type(value = SubType2.class, name = "SubType2")
 })
 static class BaseType {
 }

 resolvedSubtypes.clear();
 AnnotatedClass baseTypeAnnotatedWithSubtypes = AnnotatedClass.construct(BaseType.class, null, null);
 subtypeResolver.collectAndResolveSubtypesByTypeId(baseTypeAnnotatedWithSubtypes, null, baseType, null, resolvedSubtypes);
 assertEquals(2, resolvedSubtypes.size());
 assertTrue(resolvedSubtypes.containsKey("SubType1"));
 assertTrue(resolvedSubtypes.containsKey("SubType2"));
 }
}