package com.google.common.base;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;

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
 Object[] objects = new Object[]{null, new Object(), "test", 1, 1.0, true, false};
 int hashCode = Objects.hashCode(objects);
 Objects expectedHashCode[] = {null, new Object(), "test", 1, 1, true, false};
 assertArrayEquals(expectedHashCode, Arrays.stream(Objects.toArray(Objects.hashCode(objects))).mapToObj(Object::new).toArray());
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullArray() {
 Objects.hashCode(null);
 }

 @Test
 public void testToStringHelper() {
 Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");

 helper.add("field1", "value1");
 helper.add("field2", 1);
 helper.add("field3", true);
 helper.add("field4", null);
 helper.omitNullValues();

 assertEquals("TestClass{field1=value1, field2=1, field3=true}", helper.toString());
 }

 @Test
 public void testToStringHelperWithNullObject() {
 Objects.ToStringHelper helper = Objects.toStringHelper(null);

 helper.add("field1", "value1");
 helper.add("field2", 1);
 helper.add("field3", true);
 helper.add("field4", null);
 helper.omitNullValues();

 assertEquals("ToStringHelper{omitNullValues=true, className=null, holderHead=ValueHolder{name=null, value=null, next=null}, holderTail=ValueHolder{name=null, value=null, next=null}}", helper.toString());
 }

 @Test
 public void testToStringHelperAddValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");

 helper.addValue(1);
 helper.addValue("value1");
 helper.addValue(true);
 helper.addValue(null);
 helper.omitNullValues();

 assertEquals("TestClass{1, value1=true}", helper.toString());
 }
}