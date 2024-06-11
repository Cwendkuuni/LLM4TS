package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 assertTrue(Objects.equal(a, b));
 assertFalse(Objects.equal(a, new Object()));
 assertFalse(Objects.equal(null, new Object()));
 assertFalse(Objects.equal(null, null));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[10];
 for (int i = 0; i < objects.length; i++) {
 objects[i] = new Object();
 }
 int hashCode = Objects.hashCode(objects);
 assertNotSame(0, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNull() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 String toString = helper.toString();
 assertTrue(toString.contains("ClassName{"));
 }

 @Test
 public void testToStringHelperAdd() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name1", "value1");
 helper.add("name2", "value2");
 String toString = helper.toString();
 assertTrue(toString.contains("name1=value1, name2=value2"));
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.omitNullValues();
 helper.add("name1", null);
 helper.add("name2", "value2");
 String toString = helper.toString();
 assertFalse(toString.contains("name1="));
 }

 @Test
 public void testToStringHelperAddValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.addValue("value1");
 helper.addValue("value2");
 String toString = helper.toString();
 assertTrue(toString.contains("value1, value2"));
 }

 @Test
 public void testToStringHelperAddBoolean() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name", true);
 String toString = helper.toString();
 assertTrue(toString.contains("name=true"));
 }

 @Test
 public void testToStringHelperAddChar() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name", 'a');
 String toString = helper.toString();
 assertTrue(toString.contains("name='a'"));
 }

 @Test
 public void testToStringHelperAddDouble() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name", 1.2);
 String toString = helper.toString();
 assertTrue(toString.contains("name=1.2"));
 }

 @Test
 public void testToStringHelperAddFloat() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name", 1.5f);
 String toString = helper.toString();
 assertTrue(toString.contains("name=1.5"));
 }

 @Test
 public void testToStringHelperAddInt() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name", 1);
 String toString = helper.toString();
 assertTrue(toString.contains("name=1"));
 }

 @Test
 public void testToStringHelperAddLong() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
 helper.add("name", 1L);
 String toString = helper.toString();
 assertTrue(toString.contains("name=1"));
 }
}