package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.type.ClassType;
import com.fasterxml.jackson.introspect.AnnotatedClass;
import com.fasterxml.jackson.introspect.AnnotatedMember;
import com.fasterxml.jackson.introspect.AnnotationIntrospectorImpl;
import com.fasterxml.jackson.introspect.JacksonAnnotationIntrospector;

/**
 * Unit tests for {@link StdSubtypeResolver} class.
 */
public class StdSubtypeResolverTest {

 private ObjectMapper mapper;
 private SubtypeResolver resolver;

 @Before
 public void setUp() throws Exception {
 mapper = new MapperFactoryBuilder().build();
 resolver = mapper.getTypeFactory().constructValueType(SubtypeResolver.class);
 }

 @After
 public void tearDown() throws Exception {
 // N/A
 }

 @Test
 public void testRegistrationByClass() throws Exception {
 // Test registration by class
 resolver.registerSubtypes(new Class[] { SubclassOne.class, SubclassTwo.class });

 // Test that registered subtypes are present
 JavaType baseType = ClassType.construct(BaseClass.class);
 List<NamedType> types = resolver.collectAndResolveSubtypesByClass(null, null, baseType);
 assertEquals(2, types.size());
 assertTrue(types.contains(new NamedType(SubclassOne.class)));
 assertTrue(types.contains(new NamedType(SubclassTwo.class)));
 }

 @Test
 public void testRegistrationByName() throws Exception {
 // Test registration by name
 resolver.registerSubtypes(new NamedType[] {
 new NamedType(SubclassOneByName.class),
 new NamedType(SubclassTwoByName.class)
 });

 // Test that registered subtypes by name are present
 JavaType baseType = ClassType.construct(BaseClass.class);
 List<NamedType> types = resolver.collectAndResolveSubtypesByClass(null, null, baseType);
 assertEquals(2, types.size());
 assertTrue(types.contains(new NamedType(SubclassOneByName.class)));
 assertTrue(types.contains(new NamedType(SubclassTwoByName.class)));
 }

 @Test
 public void testSubtypesByClass() throws Exception {
 // Test subtypes for class with no explicit annotations
 BaseClass obj = new BaseClass("Class with no annotations");
 JavaType baseType = ClassType.construct(BaseClass.class);
 List<NamedType> types = resolver.collectAndResolveSubtypesByClass(null, null, baseType);
 assertEquals(2, types.size());
 assertTrue(types.contains(new NamedType(SubclassOne.class)));
 assertTrue(types.contains(new NamedType(SubclassTwo.class)));

 // Test subtypes for class with JsonTypeInfo annotation
 BaseClassWithAnnotations objAnnotated = new BaseClassWithAnnotations("Class with JsonTypeInfo annotation");
 JavaType baseTypeAnnotated = ClassType.construct(BaseClassWithAnnotations.class);
 List<NamedType> typesAnnotated = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeAnnotated);
 assertEquals(2, typesAnnotated.size());
 assertTrue(typesAnnotated.contains(new NamedType(SubclassOneAnnotated.class)));
 assertTrue(typesAnnotated.contains(new NamedType(SubclassTwoAnnotated.class)));

 // Test subtypes for class with JsonTypeName annotation
 BaseClassWithTypeNameAnnotations objTypeNameAnnotated = new BaseClassWithTypeNameAnnotations("Class with JsonTypeName annotation");
 JavaType baseTypeTypeNameAnnotated = ClassType.construct(BaseClassWithTypeNameAnnotations.class);
 List<NamedType> typesTypeNameAnnotated = resolver.collectAndResolveSubtypesByClass(null, null, baseTypeTypeNameAnnotated);
 assertEquals(2, typesTypeNameAnnotated.size());
 assertTrue(typesTypeNameAnnotated.contains(new NamedType(SubclassOneTypeNameAnnotated.class)));
 assertTrue(typesTypeNameAnnotated.contains(new NamedType(SubclassTwoTypeNameAnnotated.class)));
 }

 @Test
 public void testSubtypesByTypeId() throws Exception {
 // Test subtypes for class with no explicit annotations
 BaseClass obj = new BaseClass("Class with no annotations");
 JavaType baseType = ClassType.construct(BaseClass.class);
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(null, null, baseType);
 assertEquals(2, types.size());
 assertTrue(types.contains(new NamedType(SubclassOne.class)));
 assertTrue(types.contains(new NamedType(SubclassTwo.class)));

 // Test subtypes for class with JsonTypeInfo annotation
 BaseClassWithAnnotations objAnnotated = new BaseClassWithAnnotations("Class with JsonTypeInfo annotation");
 JavaType baseTypeAnnotated = ClassType.construct(BaseClassWithAnnotations.class);
 List<NamedType> typesAnnotated = resolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeAnnotated);
 assertEquals(2, typesAnnotated.size());
 assertTrue(typesAnnotated.contains(new NamedType(SubclassOneAnnotated.class)));
 assertTrue(typesAnnotated.contains(new NamedType(SubclassTwoAnnotated.class)));

 // Test subtypes for class with JsonTypeName annotation
 BaseClassWithTypeNameAnnotations objTypeNameAnnotated = new BaseClassWithTypeNameAnnotations("Class with JsonTypeName annotation");
 JavaType baseTypeTypeNameAnnotated = ClassType.construct(BaseClassWithTypeNameAnnotations.class);
 List<NamedType> typesTypeNameAnnotated = resolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeTypeNameAnnotated);
 assertEquals(2, typesTypeNameAnnotated.size());
 assertTrue(typesTypeNameAnnotated.contains(new NamedType(SubclassOneTypeNameAnnotated.class)));
 assertTrue(typesTypeNameAnnotated.contains(new NamedType(SubclassTwoTypeNameAnnotated.class)));
 }

 @Test
 public void testSubtypesByTypeIdWithRegisteredSubtypes() throws Exception {
 // Test subtypes for class with no explicit annotations and registered subtypes
 BaseClass obj = new BaseClass("Class with no annotations and registered subtypes");
 JavaType baseType = ClassType.construct(BaseClass.class);
 resolver.registerSubtypes(new Class[] { SubclassOne.class, SubclassTwo.class });
 List<NamedType> types = resolver.collectAndResolveSubtypesByTypeId(null, null, baseType);
 assertEquals(2, types.size());
 assertTrue(types.contains(new NamedType(SubclassOne.class)));
 assertTrue(types.contains(new NamedType(SubclassTwo.class)));
 }

 @Test
 public void testSubtypesByTypeIdWithExplicitSubtypes() throws Exception {
 // Test subtypes for class with JsonTypeInfo annotation and explicit subtypes
 BaseClassWithAnnotations objAnnotated = new BaseClassWithAnnotations("Class with JsonTypeInfo annotation and explicit subtypes");
 JavaType baseTypeAnnotated = ClassType.construct(BaseClassWithAnnotations.class);
 resolver.registerSubtypes(new Class[] { SubclassOneAnnotated.class, SubclassTwoAnnotated.class });
 List<NamedType> typesAnnotated = resolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeAnnotated);
 assertEquals(2, typesAnnotated.size());
 assertTrue(typesAnnotated.contains(new NamedType(SubclassOneAnnotated.class)));
 assertTrue(typesAnnotated.contains(new NamedType(SubclassTwoAnnotated.class)));
 }

 @Test
 public void testSubtypesByTypeIdWithNameRegisteredSubtypes() throws Exception {
 // Test subtypes for class with JsonTypeName annotation and registered subtypes by name
 BaseClassWithTypeNameAnnotations objTypeNameAnnotated = new BaseClassWithTypeNameAnnotations("Class with JsonTypeName annotation and registered subtypes by name");
 JavaType baseTypeTypeNameAnnotated = ClassType.construct(BaseClassWithTypeNameAnnotations.class);
 resolver.registerSubtypes(new NamedType[] { new NamedType(SubclassOneTypeNameAnnotated.class),
 new NamedType(SubclassTwoTypeNameAnnotated.class) });
 List<NamedType> typesTypeNameAnnotated = resolver.collectAndResolveSubtypesByTypeId(null, null, baseTypeTypeNameAnnotated);
 assertEquals(2, typesTypeNameAnnotated.size());
 assertTrue(typesTypeNameAnnotated.contains(new NamedType(SubclassOneTypeNameAnnotated.class)));
 assertTrue(typesTypeNameAnnotated.contains(new NamedType(SubclassTwoTypeNameAnnotated.class)));
 }

 private static class BaseClass {
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
 private static class BaseClassWithAnnotations {
 private String name;
 private String type;

 public BaseClassWithAnnotations(String name) {
 this.name = name;
 }
 }

 @JsonTypeName("subclassOne")
 private static class SubclassOne extends BaseClassWithAnnotations {
 public SubclassOne() {
 super("SubclassOne");
 }
 }

 @JsonTypeName("subclassTwo")
 private static class SubclassTwo extends BaseClassWithAnnotations {
 public SubclassTwo() {
 super("SubclassTwo");
 }
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
 @JsonTypeName("subclassOneByName")
 private static class SubclassOneByName extends BaseClass {
 }

 @JsonTypeName("subclassTwoByName")
 private static class SubclassTwoByName extends BaseClass {
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
 @JsonTypeName("subclassOneAnnotated")
 private static class SubclassOneAnnotated extends BaseClassWithAnnotations {
 public SubclassOneAnnotated() {
 super("SubclassOneAnnotated");
 }
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
 @JsonTypeName("subclassTwoAnnotated")
 private static class SubclassTwoAnnotated extends BaseClassWithAnnotations {
 public SubclassTwoAnnotated() {
 super("SubclassTwoAnnotated");
 }
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
 @JsonTypeName("subclassOneTypeNameAnnotated")
 private static class SubclassOneTypeNameAnnotated extends BaseClassWithTypeNameAnnotations {
 public SubclassOneTypeNameAnnotated() {
 super("SubclassOneTypeNameAnnotated");
 }
 }

 @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
 @JsonTypeName("subclassTwoTypeNameAnnotated")
 private static class SubclassTwoTypeNameAnnotated extends BaseClassWithTypeNameAnnotations {
 public SubclassTwoTypeNameAnnotated() {
 super("SubclassTwoTypeNameAnnotated");
 }
 }

 private static class BaseClassWithTypeNameAnnotations {
 private String name;
 private String type;

 public BaseClassWithTypeNameAnnotations(String name) {
 this.name = name;
 }
 }
}