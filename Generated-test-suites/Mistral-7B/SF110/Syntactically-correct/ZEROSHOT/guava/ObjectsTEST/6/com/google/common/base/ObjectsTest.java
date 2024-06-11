package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object();
 Object obj2 = obj1;

 // Test equal with null objects
 Assert.assertFalse(Objects.equal(null, null));
 Assert.assertFalse(Objects.equal(null, obj1));
 Assert.assertFalse(Objects.equal(obj1, null));

 // Test equal with same object
 Assert.assertTrue(Objects.equal(obj1, obj2));

 // Test equal with different objects
 Assert.assertFalse(Objects.equal(obj1, new Object()));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object[] objects = {obj1, obj2};

 // Test hashCode with empty array
 int emptyHashCode = Objects.hashCode();
 Assert.assertEquals(emptyHashCode, Objects.hashCode());

 // Test hashCode with single object
 int singleObjectHashCode = Objects.hashCode(obj1);
 Assert.assertEquals(singleObjectHashCode, Objects.hashCode(obj1));

 // Test hashCode with two objects
 int twoObjectsHashCode = Objects.hashCode(obj1, obj2);
 Assert.assertNotEquals(singleObjectHashCode, twoObjectsHashCode);

 // Test hashCode with array containing the objects
 int arrayHashCode = Objects.hashCode(objects);
 Assert.assertEquals(twoObjectsHashCode, arrayHashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringHelperWithNullSelf() {
 Objects.toStringHelper(null);
 }

 @Test
 public void testToStringHelperWithStringClassName() {
 Objects.toStringHelper("ClassName").omitNullValues().add("name1", "value1")
 .add("name2", "value2").toString();
 }

 @Test
 public void testToStringHelperWithClassClassName() {
 Objects.toStringHelper(String.class).omitNullValues().add("name1", "value1")
 .add("name2", "value2").toString();
 }

 @Test
 public void testToStringHelperWithObjectSelf() {
 Object obj = new Object();
 Objects.toStringHelper(obj).omitNullValues().add("name1", "value1")
 .add("name2", "value2").toString();
 }

 @Test
 public void testToStringHelperWithBooleanValue() {
 Objects.toStringHelper(true).omitNullValues().toString();
 }

 @Test
 public void testToStringHelperWithCharValue() {
 Objects.toStringHelper('c').omitNullValues().toString();
 }

 @Test
 public void testToStringHelperWithDoubleValue() {
 Objects.toStringHelper(1.2).omitNullValues().toString();
 }

 @Test
 public void testToStringHelperWithFloatValue() {
 Objects.toStringHelper((float) 1.2).omitNullValues().toString();
 }

 @Test
 public void testToStringHelperWithIntValue() {
 Objects.toStringHelper(1).omitNullValues().toString();
 }

 @Test
 public void testToStringHelperWithLongValue() {
 Objects.toStringHelper(1L).omitNullValues().toString();
 }

 @Test
 public void testToStringHelperWithNullValue() {
 Objects.toStringHelper(null).omitNullValues().toString();
 }

 @Test
 public void testToStringHelperWithNullValueOmitted() {
 Objects.toStringHelper("ClassName").omitNullValues().add("name1", "value1")
 .add("name2", null).toString();
 }

 @Test
 public void testToStringHelperWithMultipleValues() {
 Objects.toStringHelper("ClassName").omitNullValues()
 .add("name1", "value1").add("name2", "value2").add("name3", "value3")
 .toString();
 }
}