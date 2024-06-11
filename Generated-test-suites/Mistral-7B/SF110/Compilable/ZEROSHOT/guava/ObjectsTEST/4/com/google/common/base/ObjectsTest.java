package com.google.common.base.tests;

import com.google.common.base.Objects;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 assertTrue(Objects.equal(obj1, obj1));
 assertTrue(Objects.equal(obj1, obj2));
 assertFalse(Objects.equal(obj1, obj3));
 assertFalse(Objects.equal(obj1, null));
 assertFalse(Objects.equal(null, obj1));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object[] objects = {obj1, obj2};
 int expectedHashCode = Arrays.hashCode(objects);
 int actualHashCode = Objects.hashCode(objects);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNull() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 String className = "ObjectsTest";
 Objects.ToStringHelper helper = Objects.toStringHelper(className);

 helper.add("field1", "value1");
 helper.add("field2", 1);
 helper.add("field3", 'c');
 helper.add("field4", 3.14);
 helper.addValue(true);
 helper.addValue(42);
 helper.addValue(new Object());

 String expectedToString = String.format("%s{field1=value1, field2=1, field3=c, field4=3.14, field5=true, field6=42, field7=%s}", className, new Object());
 String actualToString = helper.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 String className = "ObjectsTest";
 Objects.ToStringHelper helper = Objects.toStringHelper(className).omitNullValues();

 helper.add("field1", "value1");
 helper.add("field2", null);
 helper.add("field3", 1);
 helper.add("field4", null);
 helper.addValue(true);
 helper.addValue(null);
 helper.addValue(42);
 helper.addValue(new Object());

 String expectedToString = String.format("%s{field1=value1, field3=1}", className);
 String actualToString = helper.toString();
 assertEquals(expectedToString, actualToString);
 }
}