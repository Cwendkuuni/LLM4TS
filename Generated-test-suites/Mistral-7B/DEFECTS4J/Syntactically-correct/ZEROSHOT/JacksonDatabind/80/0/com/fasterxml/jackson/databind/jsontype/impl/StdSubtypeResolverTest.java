package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

public class StdSubtypeResolverTest {

 private StdSubtypeResolver resolver;
 private MapperConfig<?> config;
 private AnnotationIntrospector introspector;

 @Before
 public void setUp() {
 resolver = new StdSubtypeResolver();
 config = new MapperConfig(new AnnotationIntrospector());
 introspector = config.getAnnotationIntrospector();
 }

 @Test
 public void testRegisterSubtypes() {
 StdSubtypeResolver sr = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = Long.class;

 sr.registerSubtypes(subclass1, subclass2);
 sr.registerSubtypes(new NamedType(subclass1, "IntSubtype"));
 sr.registerSubtypes(new NamedType(subclass2, "LongSubtype"));
 sr.registerSubtypes(new NamedType(String.class, "StringSubtype"));

 LinkedHashSet<NamedType> types = sr.getRegisteredSubtypes();
 assertEquals(3, types.size());

 ClassType superclassType = JavaType.constructType(superclass);
 List<NamedType> subtypes = sr.collectAndResolveSubtypesByClass(config, new AnnotatedMember(null, superclassType), superclassType);
 assertEquals(3, subtypes.size());

 for (NamedType subtype : subtypes) {
 if (subtype.getType().equals(subclass1)) {
 assertEquals("IntSubtype", subtype.getName());
 } else if (subtype.getType().equals(subclass2)) {
 assertEquals("LongSubtype", subtype.getName());
 } else if (subtype.getType().equals(String.class)) {
 assertEquals("StringSubtype", subtype.getName());
 }
 }
 }

 @Test
 public void testRegisterSubtypesClasses() {
 StdSubtypeResolver sr = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = Long.class;

 sr.registerSubtypes(subclass1, subclass2);
 sr.registerSubtypes(Integer.class);
 sr.registerSubtypes(Long.class);
 sr.registerSubtypes(String.class);

 LinkedHashSet<NamedType> types = sr.getRegisteredSubtypes();
 assertEquals(3, types.size());

 ClassType superclassType = JavaType.constructType(superclass);
 List<NamedType> subtypes = sr.collectAndResolveSubtypesByClass(config, new AnnotatedMember(null, superclassType), superclassType);
 assertEquals(3, subtypes.size());

 for (NamedType subtype : subtypes) {
 if (subtype.getType().equals(subclass1)) {
 assertEquals("class integer", subtype.getName());
 } else if (subtype.getType().equals(subclass2)) {
 assertEquals("class long", subtype.getName());
 } else if (subtype.getType().equals(String.class)) {
 assertEquals("class java.lang.String", subtype.getName());
 }
 }
 }

 @Test
 public void testCollectAndResolveSubtypesByClass() {
 StdSubtypeResolver sr = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = Long.class;
 Class<?> subclass3 = Float.class;

 sr.registerSubtypes(subclass1, subclass2, subclass3);
 sr.registerSubtypes(Integer.class);
 sr.registerSubtypes(Long.class);
 sr.registerSubtypes(Float.class);
 sr.registerSubtypes(String.class);

 LinkedHashSet<NamedType> types = sr.getRegisteredSubtypes();
 assertEquals(4, types.size());

 ClassType superclassType = JavaType.constructType(superclass);
 List<NamedType> subtypes = sr.collectAndResolveSubtypesByClass(config, new AnnotatedMember(null, superclassType), superclassType);
 assertEquals(4, subtypes.size());

 for (NamedType subtype : subtypes) {
 if (subtype.getType().equals(subclass1)) {
 assertEquals("class integer", subtype.getName());
 } else if (subtype.getType().equals(subclass2)) {
 assertEquals("class long", subtype.getName());
 } else if (subtype.getType().equals(subclass3)) {
 assertEquals("class float", subtype.getName());
 } else if (subtype.getType().equals(String.class)) {
 assertEquals("class java.lang.String", subtype.getName());
 }
 }
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeId() {
 StdSubtypeResolver sr = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = Long.class;
 Class<?> subclass3 = Float.class;

 sr.registerSubtypes(subclass1, subclass2, subclass3);
 sr.registerSubtypes(Integer.class);
 sr.registerSubtypes(Long.class);
 sr.registerSubtypes(Float.class);
 sr.registerSubtypes(String.class);

 LinkedHashSet<NamedType> types = sr.getRegisteredSubtypes();
 assertEquals(4, types.size());

 JavaType baseType = JavaType.constructType(subclass1.getName());
 List<NamedType> subtypes = sr.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(null, baseType), baseType);
 assertEquals(3, subtypes.size());

 for (NamedType subtype : subtypes) {
 if (subtype.getType().equals(subclass1)) {
 assertEquals(null, subtype.getName());
 } else if (subtype.getType().equals(subclass2)) {
 assertEquals("Long", subtype.getName());
 } else if (subtype.getType().equals(subclass3)) {
 assertEquals("Float", subtype.getName());
 }
 }
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithName() {
 StdSubtypeResolver sr = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = Long.class;
 Class<?> subclass3 = Float.class;

 sr.registerSubtypes(subclass1, subclass2, subclass3);
 sr.registerSubtypes(new NamedType(Integer.class, "IntSubtype"));
 sr.registerSubtypes(new NamedType(Long.class, "LongSubtype"));
 sr.registerSubtypes(new NamedType(Float.class, "FloatSubtype"));
 sr.registerSubtypes(new NamedType(String.class, "StringSubtype"));

 LinkedHashSet<NamedType> types = sr.getRegisteredSubtypes();
 assertEquals(4, types.size());

 JavaType baseType = JavaType.constructType(subclass1.getName());
 List<NamedType> subtypes = sr.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(null, baseType), baseType);
 assertEquals(4, subtypes.size());

 for (NamedType subtype : subtypes) {
 if (subtype.getType().equals(subclass1)) {
 assertEquals("IntSubtype", subtype.getName());
 } else if (subtype.getType().equals(subclass2)) {
 assertEquals("LongSubtype", subtype.getName());
 } else if (subtype.getType().equals(subclass3)) {
 assertEquals("FloatSubtype", subtype.getName());
 } else if (subtype.getType().equals(String.class)) {
 assertEquals("StringSubtype", subtype.getName());
 }
 }
 }

 @Test
 public void testInheritance() {
 StdSubtypeResolver sr = new StdSubtypeResolver();
 Class<?> superclass = Object.class;
 Class<?> subclass1 = Integer.class;
 Class<?> subclass2 = Long.class;
 Class<?> subclass3 = Float.class;

 sr.registerSubtypes(subclass1, subclass2, subclass3);
 sr.registerSubtypes(Integer.class);
 sr.registerSubtypes(Long.class);
 sr.registerSubtypes(Float.class);
 sr.registerSubtypes(Number.class);

 LinkedHashSet<NamedType> types = sr.getRegisteredSubtypes();
 assertEquals(5, types.size());

 JavaType baseType = JavaType.constructType(Number.class.getName());
 List<NamedType> subtypes = sr.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(null, baseType), baseType);
 assertEquals(4, subtypes.size());

 for (NamedType subtype : subtypes) {
 if (subtype.getType().equals(subclass1)) {
 assertEquals("class integer", subtype.getName());
 } else if (subtype.getType().equals(subclass2)) {
 assertEquals("class long", subtype.getName());
 } else if (subtype.getType().equals(subclass3)) {
 assertEquals("class float", subtype.getName());
 } else if (subtype.getType().equals(Number.class)) {
 assertEquals("class java.lang.Number", subtype.getName());
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSuperclass() {
 StdSubtypeResolver sr = new StdSubtypeResolver();
 sr.registerSubtypes(Integer.class);
 sr.registerSubtypes(Long.class);
 sr.registerSubtypes(Float.class);
 sr.registerSubtypes(String.class);

 Class<?> superclass = null;
 JavaType baseType = JavaType.constructType(superclass.getName());
 sr.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(null, baseType), baseType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullClass() {
 StdSubtypeResolver sr = new StdSubtypeResolver();
 sr.registerSubtypes(Integer.class);
 sr.registerSubtypes(Long.class);
 sr.registerSubtypes(Float.class);
 sr.registerSubtypes(String.class);

 JavaType baseType = null;
 List<NamedType> subtypes = sr.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(null, baseType), baseType);
 }
}