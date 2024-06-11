package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object a = new Object();
 Object b = a;
 Object c = new Object();

 assertTrue(Objects.equal(a, b));
 assertFalse(Objects.equal(a, c));
 assertFalse(Objects.equal(null, a));
 }

 @Test
 public void testHashCode() {
 Object a = new Object();
 Object b = new Object();
 Object c = new Object();

 int hashCodeA = Objects.hashCode(a);
 int hashCodeB = Objects.hashCode(b);
 int hashCodeC = Objects.hashCode(c);
 int hashCodeObjects = Objects.hashCode(a, b, c);

 assertEquals(hashCodeA, Objects.hashCode(a));
 assertEquals(hashCodeB, Objects.hashCode(b));
 assertEquals(hashCodeC, Objects.hashCode(c));
 assertEquals(hashCodeObjects, Objects.hashCode(a, b, c));
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object();
 String className = Objects.class.getName();
 Objects.ToStringHelper helper = Objects.toStringHelper(className).omitNullValues();

 helper.add("field1", "value1");
 helper.add("field2", "value2");
 helper.addValue(obj);

 assertEquals(className + "{ field1=value1, field2=value2, field3=" + obj + "}", helper.toString());
 }

 @Test
 public void testToStringHelperAddNull() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", "value1");
 helper.add("field2", null);
 helper.addValue(obj);

 assertEquals(Objects.class.getName() + "{ field1=value1, field2=null, field3=" + obj + "}", helper.toString());
 }

 @Test
 public void testToStringHelperNoOmitNullValues() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class);

 helper.add("field1", "value1");
 helper.add("field2", null);
 helper.addValue(obj);

 assertEquals(Objects.class.getName() + "{ field1=value1, field2=null, field3=" + obj + "}", helper.toString());
 }

 @Test
 public void testToStringHelperEmpty() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class);

 assertEquals(Objects.class.getName() + "{}", helper.toString());
 }

 @Test
 public void testToStringHelperSingleValue() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.addValue(obj);

 assertEquals(Objects.class.getName() + "{ field3=" + obj + "}", helper.toString());
 }

 @Test
 public void testToStringHelperSingleString() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", "value");

 assertEquals(Objects.class.getName() + "{ field1=value}", helper.toString());
 }

 @Test
 public void testToStringHelperSingleBoolean() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", true);

 assertEquals(Objects.class.getName() + "{ field1=true}", helper.toString());
 }

 @Test
 public void testToStringHelperSingleChar() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 'c');

 assertEquals(Objects.class.getName() + "{ field1=c}", helper.toString());
 }

 @Test
 public void testToStringHelperSingleDouble() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 1.2);

 assertEquals(Objects.class.getName() + "{ field1=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelperSingleFloat() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 1.2f);

 assertEquals(Objects.class.getName() + "{ field1=1.2}", helper.toString());
 }

 @Test
 public void testToStringHelperSingleInt() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 1);
}

 @Test
 public void testToStringHelperSingleLong() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 1L);

 assertEquals(Objects.class.getName() + "{ field1=1}", helper.toString());
 }

 @Test
 public void testToStringHelperMultipleValues() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", "value1");
 helper.add("field2", obj1);
 helper.add("field3", "value3");
 helper.add("field4", obj2);

 assertEquals(Objects.class.getName() + "{ field1=value1, field2=" + obj1 + ", field3=value3, field4=" + obj2 + "}", helper.toString());
 }

 @Test
 public void testToStringHelperMultipleStrings() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", "value1");
 helper.add("field2", "value2");

 assertEquals(Objects.class.getName() + "{ field1=value1, field2=value2}", helper.toString());
 }

 @Test
 public void testToStringHelperMultipleBooleans() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", true);
 helper.add("field2", false);

 assertEquals(Objects.class.getName() + "{ field1=true, field2=false}", helper.toString());
 }

 @Test
 public void testToStringHelperMultipleChars() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 'a');
 helper.add("field2", 'b');

 assertEquals(Objects.class.getName() + "{ field1=a, field2=b}", helper.toString());
 }

 @Test
 public void testToStringHelperMultipleDoubles() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 1.2);
 helper.add("field2", 2.3);

 assertEquals(Objects.class.getName() + "{ field1=1.2, field2=2.3}", helper.toString());
 }

 @Test
 public void testToStringHelperMultipleFloats() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 1.2f);
 helper.add("field2", 2.3f);

 assertEquals(Objects.class.getName() + "{ field1=1.2, field2=2.3}", helper.toString());
 }

 @Test
 public void testToStringHelperMultipleInts() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 1);
 helper.add("field2", 2);

 assertEquals(Objects.class.getName() + "{ field1=1, field2=2}", helper.toString());
 }

 @Test
 public void testToStringHelperMultipleLongs() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Objects.class).omitNullValues();

 helper.add("field1", 1L);
 helper.add("field2", 2L);

 assertEquals(Objects.class.getName() + "{ field1=1, field2=2}", helper.toString());
 }

}