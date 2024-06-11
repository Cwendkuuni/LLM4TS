package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object() {};
 Object obj2 = obj1;
 Object obj3 = new Object() {};

 assertTrue(Objects.equal(obj1, obj2));
 assertTrue(Objects.equal(obj1, obj1));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(null, obj1));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object() {};
 Object obj2 = new Object() {};
 Object obj3 = new Object() {};

 int hashCode1 = Objects.hashCode(obj1);
 int hashCode2 = Objects.hashCode(obj1, obj2);
 int hashCode3 = Objects.hashCode(obj1, obj2, obj3);

 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNull() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object() {};
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1", "value1");
 helper.add("field2", null);
 helper.omitNullValues();
 String result = helper.toString();

 assertEquals("Object{" +
               "field1=value1" +
               "}", result);
 }

 @Test
 public void testToStringHelperWithNullName() {
 Object obj = new Object() {};
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add(null, "value1");
 helper.omitNullValues();
 String result = helper.toString();

 assertEquals("Object{ value1=}", result);
 }

 @Test
 public void testToStringHelperWithMultipleValues() {
 Object obj = new Object() {};
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1", "value1");
 helper.add("field2", "value2");
 helper.add("field3", null);
 helper.omitNullValues();
 String result = helper.toString();

 assertEquals("Object{" +
               "field1=value1" +
               "}", result);
 }

 @Test
 public void testToStringHelperWithCustomClass() {
 MyClass obj = new MyClass();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj.getClass());
 helper.add("field1", "value1");
 helper.add("field2", null);
 helper.omitNullValues();
 String result = helper.toString();

 assertEquals("MyClass{" +
               "}", result);
 }

 @Test
 public void testToStringHelperWithNestedObjects() {
 NestedObject obj = new NestedObject();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1.innerField1", "value1");
 helper.add("field1.innerField2", new NestedInnerObject());
 String result = helper.toString();

 assertEquals("NestedObject{" +
               "field1=NestedInnerObject{" +
               "innerField1=value1" +
               "}", result);
 }

 @Test
 public void testToStringHelperWithNestedObjectsNull() {
 NestedObject obj = new NestedObject();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1.innerField1", "value1");
 helper.add("field1.innerField2", null);
 helper.omitNullValues();
 String result = helper.toString();

 assertEquals("NestedObject{" +
               "field1=NestedInnerObject{}" +
               "}", result);
 }

 @Test
 public void testToStringHelperWithCustomToString() {
 NestedObject obj = new NestedObject();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1", obj);
 String result = helper.toString();

 assertEquals("NestedObject{" +
               "field1=" + obj.toString() +
               "}", result);
 }

 @Test
 public void testToStringHelperWithCustomToStringNull() {
 NestedObject obj = new NestedObject();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1", obj);
 helper.omitNullValues();
 String result = helper.toString();

 assertEquals("NestedObject{" +
               "}", result);
 }
}

class MyClass {
 // Add fields and getters here if necessary
}

class NestedObject {
 NestedInnerObject innerObject;
 // Add fields and getters here if necessary
}

class NestedInnerObject {
 String innerField1;
 // Add fields and getters here if necessary

 @Override
 public String toString() {
 return "NestedInnerObject{" +
        "innerField1=" + innerField1 +
        "}";
 }
}