package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

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
 Object[] objects = new Object[5];
 Arrays.fill(objects, new Object());
 int hashCode = Objects.hashCode(objects);

 int expectedHashCode = Arrays.hashCode(objects);
 assertEquals(expectedHashCode, hashCode);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullArray() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");

 helper.add("field1", "value1");
 helper.add("field2", "value2");
 helper.add("field3", "value3");

 String actualToString = helper.toString();
 String expectedToString = "ClassName{field1=value1, field2=value2, field3=value3}";

 assertThat(actualToString, equalTo(expectedToString));
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName").omitNullValues();

 helper.add("field1", "value1");
 helper.add("field2", null);
 helper.add("field3", "value3");

 String actualToString = helper.toString();
 String expectedToString = "ClassName{field1=value1, field3=value3}";

 assertThat(actualToString, equalTo(expectedToString));
 }

 @Test
 public void testToStringHelperAddPrimitiveTypes() {
 Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");

 helper.add("field1", 1);
 helper.add("field2", 2.5);
 helper.add("field3", true);

 String actualToString = helper.toString();
 String expectedToString = "ClassName{field1=1, field2=2.5, field3=true}";

 assertThat(actualToString, equalTo(expectedToString));
 }
}