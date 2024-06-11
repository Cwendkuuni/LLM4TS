package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Objects.equal(obj1, obj1));
 assertTrue(Objects.equal(obj1, obj2));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(null, obj1));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();

 int hashcode1 = Objects.hashCode(obj1);
 int hashcode2 = Objects.hashCode(obj2);
 int hashcode3 = Objects.hashCode(obj3);
 int hashcode4 = Objects.hashCode(obj1, obj2, obj3);

 assertNotSame(hashcode1, hashcode2);
 assertNotSame(hashcode1, hashcode3);
 assertNotSame(hashcode1, hashcode4);
 assertNotSame(hashcode2, hashcode3);
 assertNotSame(hashcode2, hashcode4);
 assertNotSame(hashcode3, hashcode4);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelperNullSelf() {
 Objects.toStringHelper(null);
 }

 @Test
 public void testToStringHelperString() {
 Object obj = new Object();
 String result = Objects.toStringHelper(obj.getClass()).toString();
 assertTrue(result.contains(obj.getClass().getSimpleName()));
 }

 @Test
 public void testToStringHelperClass() {
 Object obj = new Object();
 String result = Objects.toStringHelper(obj.getClass()).toString();
 assertTrue(result.contains(obj.getClass().getSimpleName()));
 }

 @Test
 public void testToStringHelperStringClassName() {
 Object obj = new Object();
 String result = Objects.toStringHelper("className").toString();
 assertTrue(result.contains("className"));
 }

 @Test
 public void testFirstNonNullNullFirst() {
 Object result = Objects.firstNonNull(null, "foo");
 assertEquals("foo", result);
 }

 @Test
 public void testFirstNonNullNullSecond() {
 Object result = Objects.firstNonNull("foo", null);
 assertEquals("foo", result);
 }

 @Test
 public void testFirstNonNullSameObject() {
 Object obj = new Object();
 Object result = Objects.firstNonNull(obj, obj);
 assertEquals(obj, result);
 }

 @Test
 public void testToStringHelperAddString() {
 Object obj = new Object();
 String name = "name";
 String result = Objects.toStringHelper(obj).add(name, null).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains("=null"));
 }

 @Test
 public void testToStringHelperAddBoolean() {
 Object obj = new Object();
 String name = "name";
 boolean booleanValue = true;
 String result = Objects.toStringHelper(obj).add(name, booleanValue).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains(String.valueOf(booleanValue)));
 }

 @Test
 public void testToStringHelperAddChar() {
 Object obj = new Object();
 String name = "name";
 char charValue = 'a';
 String result = Objects.toStringHelper(obj).add(name, charValue).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains(String.valueOf(charValue)));
 }

 @Test
 public void testToStringHelperAddDouble() {
 Object obj = new Object();
 String name = "name";
 double doubleValue = 3.14;
 String result = Objects.toStringHelper(obj).add(name, doubleValue).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains(String.valueOf(doubleValue)));
 }

 @Test
 public void testToStringHelperAddFloat() {
 Object obj = new Object();
 String name = "name";
 float floatValue = 3.14f;
 String result = Objects.toStringHelper(obj).add(name, floatValue).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains(String.valueOf(floatValue)));
 }

 @Test
 public void testToStringHelperAddInt() {
 Object obj = new Object();
 String name = "name";
 int intValue = 42;
 String result = Objects.toStringHelper(obj).add(name, intValue).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains(String.valueOf(intValue)));
 }

 @Test
 public void testToStringHelperAddLong() {
 Object obj = new Object();
 String name = "name";
 long longValue = 42L;
 String result = Objects.toStringHelper(obj).add(name, longValue).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains(String.valueOf(longValue)));
 }

 @Test
 public void testToStringHelperAddObject() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 String name = "name";
 String result = Objects.toStringHelper(obj1).add(name, obj2).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains("obj2"));
 }

 @Test
 public void testToStringHelperAddBooleanOmitNullValues() {
 Object obj = new Object();
 String name = "name";
 boolean booleanValue = true;
 String result = Objects.toStringHelper(obj).omitNullValues().add(name, booleanValue).addValue(null).toString();
 assertTrue(result.contains(name));
 assertTrue(result.contains(String.valueOf(booleanValue)));
 assertFalse(result.contains("=null"));
 }
}