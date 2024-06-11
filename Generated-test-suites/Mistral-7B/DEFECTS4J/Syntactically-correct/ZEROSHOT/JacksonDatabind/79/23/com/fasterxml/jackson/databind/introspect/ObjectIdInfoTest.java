package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ObjectIdInfo
 */
public class ObjectIdInfoTest {

 @Test
 public void testObjectIdInfoConstruction() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, false, null);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=null, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, StringIdGenerator.class, false, null);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.util.StringIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=null, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsId() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, true, null);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=null, alwaysAsId=true", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAllArgs() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, StringIdGenerator.class, false, SimpleObjectIdResolver.class);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=class com.fasterxml.jackson.databind.util.StringIdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfo.toString());
 }

 @Test
 public void testObjectIdInfoWithAlwaysAsIdSetter() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, false, null);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.Object, generatorType=null, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullPropertyName() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, Object.class, null, false, null);
 fail("Should throw IllegalArgumentException with null PropertyName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testObjectIdInfoWithNullScope() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), null, null, false, null);
 fail("Should throw IllegalArgumentException with null Scope");
 }

 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoWithNullGenerator() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, false, null);
 ObjectIdInfo objectIdInfoWithGenerator = objectIdInfo.withGenerator(null);
 fail("Should throw NullPointerException with null Generator");
 }

 @Test(expected = NullPointerException.class)
 public void testObjectIdInfoWithNullResolver() {
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, null, false, null);
 ObjectIdInfo objectIdInfoWithResolver = objectIdInfo.withResolver(null);
 fail("Should throw NullPointerException with null Resolver");
 }
}