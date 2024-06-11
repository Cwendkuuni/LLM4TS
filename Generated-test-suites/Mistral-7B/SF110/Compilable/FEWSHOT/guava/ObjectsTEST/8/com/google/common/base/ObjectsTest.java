package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = new Object();
 Object c = a;

 assertTrue(Objects.equal(null, null));
 assertTrue(Objects.equal(a, a));
 assertFalse(Objects.equal(a, b));
 assertFalse(Objects.equal(a, c));
 assertFalse(Objects.equal(b, c));
 }

 @Test
 public void testHashCode() {
 Object a = new Object();
 Object b = new Object();
 Object c = a;

 int hashCodeOfNull = Objects.hashCode(null);
 int hashCodeOfA = Objects.hashCode(a);
 int hashCodeOfB = Objects.hashCode(b);
 int hashCodeOfC = Objects.hashCode(c);

 assertEquals(-1, hashCodeOfNull);
 assertEquals(hashCodeOfA, hashCodeOfC); // a and c should have the same hashcode since they refer to the same object
 assertNotEquals(hashCodeOfA, hashCodeOfB);
 }

 @Test
 public void testToStringHelper_String() {
 String className = "MyClass";
 Objects.ToStringHelper helper = Objects.toStringHelper(className);
 helper.add("field1", 1);
 helper.add("field2", "two");
 helper.add("field3", null);
 helper.omitNullValues();

 assertEquals(className + "{field1=1, field2=two}", helper.toString());
 }

 @Test
 public void testToStringHelper_Object() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj);
 helper.add("field1", 1);
 helper.add("field2", "two");
 helper.addValue(null);
 helper.omitNullValues();

 assertEquals(obj.getClass().getName() + "{field1=1, field2=two}", helper.toString());
 }

 @Test
 public void testToStringHelper_Class() {
 Class<?> clazz = String.class;
 Objects.ToStringHelper helper = Objects.toStringHelper(clazz);
 helper.add("field1", "one");
 helper.add("field2", null);
 helper.omitNullValues();

 assertEquals(clazz.getName() + "{field1=one}", helper.toString());
 }

 @Test
 public void testToStringHelper_addSimpleTypes() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 helper.add("field1", 1);
 helper.add("field2", "two");
 helper.add("field3", true);
 helper.add("field4", 3.14);
 helper.omitNullValues();

 assertEquals("{field1=1, field2=two, field3=true, field4=3.14}", helper.toString());
 }

 @Test
 public void testToStringHelper_addComplexTypes() {
 Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
 helper.add("field1", new Object());
 helper.add("field2", new Object().getClass());
 helper.add("field3", new Object() {});
 helper.omitNullValues();

 assertEquals("{field1=<Object>, field2=class java.lang.Object, field3=<Object>}", helper.toString());
 }

 @Test
 public void testFirstNonNull() {
 Object first = null;
 Object second = new Object();
 Object result = Objects.firstNonNull(first, second);

 assertSame(second, result);
 }

 @Test
 public void testFirstNonNull_bothNull() {
 Object first = null;
 Object second = null;
 Object result = Objects.firstNonNull(first, second);

 assertNull(result);
 }

 @Test
 public void testFirstNonNull_firstNotNull() {
 Object first = new Object();
 Object second = null;
 Object result = Objects.firstNonNull(first, second);

 assertSame(first, result);
 }

 @Before
 public void setup() {
 // empty
 }

}