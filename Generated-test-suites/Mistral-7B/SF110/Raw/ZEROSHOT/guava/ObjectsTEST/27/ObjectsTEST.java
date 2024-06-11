package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class ObjectsTest {

 @Test
 public void testEqual_nullNull() {
 assertTrue(Objects.equal(null, null));
 }

 @Test
 public void testEqual_nullObject() {
 assertFalse(Objects.equal(null, new Object()));
 }

 @Test
 public void testEqual_nullObject_sameInstance() {
 assertTrue(Objects.equal(null, new Object())); // this is a special case for null
 }

 @Test
 public void testEqual_nullObject_notSameInstance() {
 assertFalse(Objects.equal(new Object(), new Object()));
 }

 @Test
 public void testEqual_equalObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 assertTrue(Objects.equal(obj1, obj2));
 }

 @Test
 public void testEqual_equalObjects_differentClass() {
 Object obj1 = new Object();
 Integer obj2 = new Integer(1);
 assertFalse(Objects.equal(obj1, obj2));
 }

 @Test
 public void testEqual_equalObjects_differentClass_butSameValue() {
 Object obj1 = new Object();
 Integer obj2 = new Integer(obj1.hashCode());
 assertTrue(Objects.equal(obj1, obj2));
 }

 @Test(expected = NullPointerException.class)
 public void testEqual_nullObject_nonNullObject() {
 Objects.equal(null, new Object());
 }

 @Test
 public void testHashCode_emptyArray() {
 assertEquals(0, Objects.hashCode());
 }

 @Test
 public void testHashCode_singleObject() {
 Object obj = new Object();
 int hashCode = Objects.hashCode(obj);
 assertEquals(hashCode, Objects.hashCode(obj));
 }

 @Test
 public void testHashCode_multipleObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 int hashCode = Objects.hashCode(obj1, obj2);
 int hashCode2 = Objects.hashCode(obj2, obj1);
 assertEquals(hashCode, hashCode2);
 }

 @Test
 public void testHashCode_multipleObjects_differentOrder() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 int hashCode = Objects.hashCode(obj1, obj2);
 int hashCode2 = Objects.hashCode(obj2, obj1);
 assertEquals(hashCode, hashCode2);
 }

 @Test
 public void testToStringHelper_default() {
 Object obj = new Object();
 String toString = Objects.toStringHelper(obj).toString();
 String expected = "Object{" +
         "className=class [Ljava.lang.Object;@5a63d392";
 assertEquals(expected, toString);
 }

 @Test
 public void testToStringHelper_omitNullValues_nullValue() {
 Object obj = new Object();
 String toString = Objects.toStringHelper(obj).omitNullValues().toString();
 String expected = "Object{" +
         "className=class [Ljava.lang.Object;@5a63d392}";
 assertEquals(expected, toString);
 }

 @Test
 public void testToStringHelper_addMultipleValues() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj)
         .add("name1", "value1")
         .add("name2", "value2")
         .add("name3", null)
         .add("name4", "value4");
 String toString = toStringHelper.toString();
 String expected = "Object{" +
         "className=class [Ljava.lang.Object;@5a63d392," +
         " name1=value1," +
         " name2=value2," +
         " name4=value4}";
 assertEquals(expected, toString);
 }

 @Test
 public void testToStringHelper_addMultipleValues_omitNullValues() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj)
         .omitNullValues()
         .add("name1", "value1")
         .add("name2", null)
         .add("name3", "value3")
         .add("name4", "value4");
 String toString = toStringHelper.toString();
 String expected = "Object{" +
         "className=class [Ljava.lang.Object;@5a63d392," +
         " name1=value1," +
         " name3=value3," +
         " name4=value4}";
 assertEquals(expected, toString);
 }

 @Test
 public void testToStringHelper_addMultipleValues_sameName() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj)
         .add("name", "value1")
         .add("name", "value2");
 String toString = toStringHelper.toString();
 String expected = "Object{" +
         "className=class [Ljava.lang.Object;@5a63d392," +
         " name=value2}";
 assertEquals(expected, toString);
 }

 @Test
 public void testToStringHelper_addMultipleValues_sameName_omitNullValues() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj)
         .omitNullValues()
         .add("name", "value1")
         .add("name", null)
         .add("name", "value2");
 String toString = toStringHelper.toString();
 String expected = "Object{" +
         "className=class [Ljava.lang.Object;@5a63d392," +
         " name=value2}";
 assertEquals(expected, toString);
 }
}