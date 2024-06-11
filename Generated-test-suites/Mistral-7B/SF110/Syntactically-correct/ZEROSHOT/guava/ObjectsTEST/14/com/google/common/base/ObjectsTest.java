package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Objects.equal(obj1, obj2));
 assertTrue(Objects.equal(obj1, obj1));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(null, obj1));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();

 int hashCode1 = Objects.hashCode(obj1);
 int hashCode2 = Objects.hashCode(obj2);
 int hashCode3 = Objects.hashCode(obj3);
 int hashCodeArray = Objects.hashCode(obj1, obj2, obj3);

 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCodeArray);
 assertNotSame(hashCode2, hashCodeArray);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNull() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);
 Object obj = new Object();

 toStringHelper.add("field1", obj);
 String toString = toStringHelper.toString();

 assertTrue(toString.contains(className + "{field1="));
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className).omitNullValues();
 Object obj1 = new Object();
 Object obj2 = null;

 toStringHelper.add("field1", obj1);
 toStringHelper.add("field2", obj2);
 String toString = toStringHelper.toString();

 assertTrue(toString.contains(className + "{field1="));
 assertFalse(toString.contains("field2="));
 }

 @Test
 public void testToStringHelperAddValues() {
 String className = "com.google.common.base.Objects";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);
 Object obj = new Object();
 boolean booleanValue = true;
 char charValue = 'a';
 double doubleValue = 1.2;
 float floatValue = 1.2f;
 int intValue = 1;
 long longValue = 1L;

 toStringHelper.addValue(obj);
 toStringHelper.addValue(booleanValue);
 toStringHelper.addValue(charValue);
 toStringHelper.addValue(doubleValue);
 toStringHelper.addValue(floatValue);
 toStringHelper.addValue(intValue);
 toStringHelper.addValue(longValue);
 String toString = toStringHelper.toString();

 assertTrue(toString.contains(className + "{field1=" + obj.getClass().getName() + ", field2=" + booleanValue + ", field3=" + charValue + ", field4=" + doubleValue + ", field5=" + floatValue + ", field6=" + intValue + ", field7=" + longValue + "}"));
 }
}