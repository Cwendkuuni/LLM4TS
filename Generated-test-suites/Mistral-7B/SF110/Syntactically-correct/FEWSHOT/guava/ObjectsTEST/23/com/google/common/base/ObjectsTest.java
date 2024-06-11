package com.google.common.base;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.hashCode;
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = new Object();
 Object c = a;
 Object d = new Object();

 assertTrue(equal(null, null));
 assertTrue(equal(a, a));
 assertFalse(equal(a, b));
 assertFalse(equal(a, c));
 assertFalse(equal(a, d));
 }

 @Test
 public void testHashCode() {
 Object a = new Object();
 Object b = new Object();
 Object c = a;
 Object d = new Object();

 assertEquals(System.identityHashCode(a), hashCode(a));
 assertEquals(System.identityHashCode(b), hashCode(b));
 assertEquals(System.identityHashCode(a), hashCode(a, b));
 assertEquals(System.identityHashCode(c), hashCode(a, c));
 assertEquals(System.identityHashCode(d), hashCode(a, d));
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNull() {
 hashCode(null);
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("name1", "value1")
 .add("name2", null)
 .omitNullValues();

 assertEquals("MyClass{name1=value1}", helper.toString());
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
 .add("name1", "value1")
 .add("name2", "value2")
 .addValue(42);

 assertEquals("MyClass{name1=value1, name2=value2, 42}", helper.toString());
 }

 @Test
 public void testToStringHelperEmpty() {
 Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass");

 assertEquals("MyClass{}", helper.toString());
 }
}