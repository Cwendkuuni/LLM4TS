package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
 @Test
 public void testObjectIdInfo() {
 testObjectIdInfoConstruction();
 testObjectIdInfoConstructionWithStringName();
 testObjectIdInfoWithAlwaysAsId();
 }

 private void testObjectIdInfoConstruction() {
 PropertyName propertyName = new PropertyName("testProperty");
 Class<String> scope = String.class;
 Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;

 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 assertNotNull(objectIdInfo);
 }

 private void testObjectIdInfoConstructionWithStringName() {
 String name = "testProperty";
 Class<String> scope = String.class;
 Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;

 ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, objectIdGenerator);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
 assertNotNull(objectIdInfo);
 }

 private void testObjectIdInfoWithAlwaysAsId() {
 PropertyName propertyName = new PropertyName("testProperty");
 Class<String> scope = String.class;
 Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;

 ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator);
 ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
 assertNotSame(objectIdInfo, objectIdInfoWithAlwaysAsId);
 assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
 }
}

class MyCustomObjectIdGenerator implements ObjectIdGenerator<Object> {
 // Empty implementation for testing purposes
}