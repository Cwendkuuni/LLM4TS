package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private SubTypeValidator validator;

 @Before
 public void setup() {
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidTypes() throws JsonMappingException, JsonProcessingException {
 JavaType validType = new ObjectTypeFactory().constructType(String.class);
 validator.validateSubType(new TestDeserializationContext(), validType);
 }

 @Test
 public void testNastyClassNames() {
 Set<String> defaultNoDeserClassNames = SubTypeValidator.DEFAULT_NO_DESER_CLASS_NAMES;

 for (String className : defaultNoDeserClassNames) {
 assertThrows("Illegal type to deserialize", JsonMappingException.class, () -> {
 validator.validateSubType(new TestDeserializationContext(), new ClassType(className));
 });
 }
 }

 @Test
 public void testSubclassesOfNastyClasses() throws JsonMappingException, JsonProcessingException {
 // Test subclasses of "AbstractApplicationContext"
 JavaType subclass1Type = new ObjectTypeFactory().constructType(Subclass1.class);
 validator.validateSubType(new TestDeserializationContext(), subclass1Type);

 // Test subclasses of "AbstractPointcutAdvisor"
 JavaType subclass2Type = new ObjectTypeFactory().constructType(Subclass2.class);
 validator.validateSubType(new TestDeserializationContext(), subclass2Type);
 }

 private static class ClassType extends SimpleType {
 private final Class<?> clazz;

 public ClassType(Class<?> clazz) {
 super(clazz);
 this.clazz = clazz;
 }
 }

 private static class ObjectTypeFactory {
 public JavaType constructType(Class<?> clazz) {
 return new SimpleTypeModifier().modifySimpleType(new ClassType(clazz));
 }
 }

 private static class SimpleType {
 public JavaType getType() {
 return null;
 }
 }

 private static class SimpleTypeModifier {
 public JavaType modifySimpleType(JavaType baseType) {
 return baseType;
 }
 }

 private static class TestDeserializationContext extends DeserializationContext {
 protected TestDeserializationContext() {
 super(new ObjectMapper(), Collections.emptySet(), Collections.emptyMap());
 }
 }

}