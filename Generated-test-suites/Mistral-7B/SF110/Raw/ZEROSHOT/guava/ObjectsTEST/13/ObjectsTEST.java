package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 assertTrue(Objects.equal(a, b));

 Object c = new Object();
 assertFalse(Objects.equal(a, c));

 Object d = null;
 assertFalse(Objects.equal(a, d));
 assertFalse(Objects.equal(d, a));
 }

 @Test
 public void testHashCodeWithNullObject() {
 int hashCode = Objects.hashCode(new Object[0]);
 assertEquals(0, hashCode);
 }

 @Test
 public void testHashCodeWithObjects() {
 Object a = new Object();
 Object b = new Object();
 int hashCode = Objects.hashCode(a, b);
 int expectedHashCode = Arrays.hashCode(new Object[]{a, b});
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelperWithNullSelf() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(null);
 }

 @Test
 public void testToStringHelperWithStringClassName() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("className");
 assertEquals("className{}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithObjectSelf() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 assertEquals("Object{" +
         "name=null" +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithStringNameAndNullValue() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 toStringHelper.add("value", null);
 assertEquals("Object{" +
         "name=\"name\"" +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithStringNameAndBooleanValue() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 toStringHelper.add("value", true);
 assertEquals("Object{" +
         "name=\"name\"" +
         "value=true" +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithStringNameAndCharValue() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 toStringHelper.add("value", 'c');
 assertEquals("Object{" +
         "name=\"name\"" +
         "value='c' " +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithStringNameAndDoubleValue() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 toStringHelper.add("value", 3.14);
 assertEquals("Object{" +
         "name=\"name\"" +
         "value=3.14" +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithStringNameAndFloatValue() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 toStringHelper.add("value", 3.14f);
 assertEquals("Object{" +
         "name=\"name\"" +
         "value=3.14" +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithStringNameAndIntValue() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 toStringHelper.add("value", 3);
 assertEquals("Object{" +
         "name=\"name\"" +
         "value=3" +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithStringNameAndLongValue() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 toStringHelper.add("value", 3L);
 assertEquals("Object{" +
         "name=\"name\"" +
         "value=3" +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithStringNameAndObjectValue() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 Object value = new Object();
 toStringHelper.add("value", value);
 assertEquals("Object{" +
         "name=\"name\"" +
         "value=Object{" +
         "name=null" +
         "}" +
         "}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperWithBooleanOmitNullValues() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper("name");
 toStringHelper.omitNullValues();
 Object value = null;
toStringHelper.add("value", value);
 assertEquals("Object{" +
         "name=\"name\"" +
         "}", toStringHelper.toString());
 }
}