package com.google.common.base;

import static com.google.common.base.MoreObjects.toStringHelper;
import static org.junit.Assert.*;

import org.junit.Test;

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
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object obj3 = new Object();

 int hashCode1 = Objects.hashCode(obj1);
 int hashCode2 = Objects.hashCode(obj2);
 int hashCode3 = Objects.hashCode(obj3);
 int hashCode4 = Objects.hashCode(obj1, obj2);

 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 assertNotSame(hashCode1, hashCode4);

 Object[] array = new Object[3];
 array[0] = obj1;
 array[1] = obj2;
 array[2] = obj3;

 int hashCode5 = Objects.hashCode(array);

 assertNotSame(hashCode1, hashCode5);
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object();

 String name1 = "name1";
 String name2 = "name2";
 boolean booleanValue = true;
 char charValue = 'c';
 double doubleValue = 1.5;
 float floatValue = 2.5f;
 int intValue = 3;
 long longValue = 4L;

 String expected1 = "Object{name1=null, name2=null, booleanValue=true, charValue='c', doubleValue=1.5, floatValue=2.5, intValue=3, longValue=4}";
 String expected2 = "Object{booleanValue=true, name1=null, name2=null, charValue='c', doubleValue=1.5, floatValue=2.5, intValue=3, longValue=4}";
 String expected3 = "Object{name1=null, name2=name2, booleanValue=null, charValue=null, doubleValue=null, floatValue=null, intValue=null, longValue=null}";

 ToStringHelper stringBuilder1 = toStringHelper(obj).add("name1", obj).add("name2", obj2).add("booleanValue", booleanValue).add("charValue", charValue).add("doubleValue", doubleValue).add("floatValue", floatValue).add("intValue", intValue).add("longValue", longValue);
 ToStringHelper stringBuilder2 = toStringHelper(obj).omitNullValues().add("booleanValue", booleanValue).add("name1", obj).add("name2", obj2).add("charValue", charValue).add("doubleValue", doubleValue).add("floatValue", floatValue).add("intValue", intValue).add("longValue", longValue);
 ToStringHelper stringBuilder3 = toStringHelper(obj).omitNullValues().add("name1", null).add("name2", obj2).add("booleanValue", null).add("charValue", null).add("doubleValue", null).add("floatValue", null).add("intValue", intValue).add("longValue", longValue);

 assertEquals(expected1, stringBuilder1.toString());
 assertEquals(expected2, stringBuilder2.toString());
 assertNotEquals(expected1, stringBuilder3.toString());
 }

 @Test
 public void testToStringHelper_addNullValues() {
 Object obj = new Object();

 String expected = "Object{name1=null, name2=null, booleanValue=null, charValue=null, doubleValue=null, floatValue=null, intValue=null, longValue=null}";

 ToStringHelper stringBuilder = toStringHelper(obj).add("name1", null).add("name2", null).add("booleanValue", null).add("charValue", null).add("doubleValue", null).add("floatValue", null).add("intValue", null).add("longValue", null);

 assertEquals(expected, stringBuilder.toString());
 }
}