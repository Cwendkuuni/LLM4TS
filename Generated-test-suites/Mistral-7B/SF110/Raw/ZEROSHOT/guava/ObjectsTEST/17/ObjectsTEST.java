package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Objects.equal(obj1, obj2));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(null, obj1));
 assertFalse(Objects.equal(obj1, null));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[5];
 Arrays.fill(objects, new Object());
 int hashCode = Objects.hashCode(objects);

 int expectedHashCode = 31 * (31 + Arrays.hashCode(objects));
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeNullObjects() {
 Objects.hashCode(new Object[] { null });
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");

 assertEquals("StringClass{}", helper.toString());

 helper = helper.omitNullValues();
 String[] strings = new String[5];
 Arrays.fill(strings, null);
 helper.add("field1", strings[0]);
 helper.add("field2", strings[1]);
 helper.add("field3", strings[2]);
 helper.add("field4", strings[3]);
 helper.add("field5", strings[4]);

 assertEquals("StringClass{field3=null, field4=null, field5=null}", helper.toString());
 }

 @Test
 public void testToStringHelperAddObject() {
 Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
 helper.add("field1", new Object());
 assertEquals("StringClass{field1=<Object>}", helper.toString());
 }

 @Test
 public void testToStringHelperAddBoolean() {
 Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
 helper.add("field1", true);
 assertEquals("StringClass{field1=true}", helper.toString());
 }

 @Test
 public void testToStringHelperAddChar() {
 Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
 helper.add("field1", 'c');
 assertEquals("StringClass{field1=c}", helper.toString());
 }

 @Test
 public void testToStringHelperAddDouble() {
 Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
 helper.add("field1", 3.14);
 assertEquals("StringClass{field1=3.14}", helper.toString());
 }

 @Test
 public void testToStringHelperAddFloat() {
 Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
 helper.add("field1", 3.14f);
 assertEquals("StringClass{field1=3.14}", helper.toString());
 }

 @Test
 public void testToStringHelperAddInt() {
 Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
 helper.add("field1", 1);
 assertEquals("StringClass{field1=1}", helper.toString());
 }

 @Test
 public void testToStringHelperAddLong() {
 Objects.ToStringHelper helper = Objects.toStringHelper("StringClass");
 helper.add("field1", 1L);
 assertEquals("StringClass{field1=1}", helper.toString());
 }
}