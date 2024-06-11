package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class ObjectsTest {

 @Test
 public void testEqualNullNull() {
 assertTrue(Objects.equal(null, null));
 }

 @Test
 public void testEqualNullObject() {
 assertFalse(Objects.equal(null, new Object()));
 }

 @Test
 public void testEqualNullString() {
 assertFalse(Objects.equal(null, ""));
 }

 @Test
 public void testEqualNullList() {
 List<String> list1 = Collections.emptyList();
 List<String> list2 = Collections.emptyList();
 assertTrue(Objects.equal(list1, list2));
 }

 @Test
 public void testEqualObjects() {
 String str1 = new String("Hello");
 String str2 = new String("Hello");
 assertTrue(Objects.equal(str1, str2));
 }

 @Test
 public void testEqualSameObject() {
 String str1 = new String("Hello");
 String str2 = str1;
 assertTrue(Objects.equal(str1, str2));
 }

 @Test
 public void testHashCodeNull() {
 Object[] objects = new Object[0];
 assertSame(0, Objects.hashCode(objects));
 }

 @Test
 public void testHashCodeOneObject() {
 Object obj = new Object();
 Object[] objects = {obj};
 assertNotSame(0, Objects.hashCode(objects));
 }

 @Test
 public void testHashCodeMultipleObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object[] objects = {obj1, obj2};
 int hashCode = Objects.hashCode(objects);
 Objects.hashCode(obj1, obj2);
 assertNotSame(hashCode, Objects.hashCode(obj2, obj1));
 }

 @Test
 public void testToStringHelperNull() {
 assertNull(Objects.toStringHelper(null));
 }

 @Test
 public void testToStringHelperString() {
 String className = "String";
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(className);
 assertEquals(className + "{}", toStringHelper.toString());
 }

 @Test
 public void testToStringHelperObject() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 assertEquals(obj.getClass().getName() + "{", toStringHelper.toString().substring(0, toStringHelper.toString().length() - 1));
 }

 @Test
 public void testToStringHelperAdd() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("name", "value");
 assertEquals(toStringHelper.toString(), obj.getClass().getName() + "{ name=value}");
 }

 @Test
 public void testToStringHelperAddNullValue() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("name", null);
 assertEquals(toStringHelper.toString(), obj.getClass().getName() + "{ name=null}");
 }

 @Test
 public void testToStringHelperAddMultiple() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add("name1", "value1");
 toStringHelper.add("name2", "value2");
 assertEquals(toStringHelper.toString(), obj.getClass().getName() + "{ name1=value1, name2=value2}");
 }

 @Test
 public void testToStringHelperOmitNullValues() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
toStringHelper.omitNullValues();
 toStringHelper.add("name1", null);
 toStringHelper.add("name2", "value2");
 assertEquals(toStringHelper.toString(), obj.getClass().getName() + "{ name2=value2}");
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelperNullName() {
 Object obj = new Object();
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(obj);
 toStringHelper.add(null, "value");
 }

 @Test
 public void testFirstNonNullNullFirst() {
 Object first = null;
 Object second = new Object();
 assertSame(second, Objects.firstNonNull(first, second));
 }

 @Test
 public void testFirstNonNullNullSecond() {
 Object first = new Object();
 Object second = null;
 assertSame(first, Objects.firstNonNull(first, second));
 }

 @Test
 public void testFirstNonNullDifferentTypes() {
 Object first = new Object();
 String second = "Hello";
 assertSame(first, Objects.firstNonNull(first, second));
 }

 @Test
 public void testToStringHelperAddPrimitiveBoolean() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(true);
 assertEquals(toStringHelper.toString(), "java.lang.Boolean{value=true}");
 }

 @Test
 public void testToStringHelperAddPrimitiveChar() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper('a');
 assertEquals(toStringHelper.toString(), "java.lang.Character{value='a'}");
 }

 @Test
 public void testToStringHelperAddPrimitiveDouble() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(1.2);
 assertEquals(toStringHelper.toString(), "java.lang.Double{value=1.2}");
 }

 @Test
 public void testToStringHelperAddPrimitiveFloat() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper((float) 1.2);
 assertEquals(toStringHelper.toString(), "java.lang.Float{value=1.2}");
 }

 @Test
 public void testToStringHelperAddPrimitiveInt() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(1);
 assertEquals(toStringHelper.toString(), "java.lang.Integer{value=1}");
 }

 @Test
 public void testToStringHelperAddPrimitiveLong() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(1L);
 assertEquals(toStringHelper.toString(), "java.lang.Long{value=1}");
 }

 @Test
 public void testToStringHelperAddValueBoolean() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(true);
 toStringHelper.addValue(true);
 assertEquals(toStringHelper.toString(), "java.lang.Boolean{value=true, value=true}");
 }

 @Test
 public void testToStringHelperAddValueChar() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper('a');
 toStringHelper.addValue('a');
 assertEquals(toStringHelper.toString(), "java.lang.Character{value='a', value='a'}");
 }

 @Test
 public void testToStringHelperAddValueDouble() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(1.2);
 toStringHelper.addValue(1.2);
 assertEquals(toStringHelper.toString(), "java.lang.Double{value=1.2, value=1.2}");
 }

 @Test
 public void testToStringHelperAddValueFloat() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper((float) 1.2);
 toStringHelper.addValue((float) 1.2);
 assertEquals(toStringHelper.toString(), "java.lang.Float{value=1.2, value=1.2}");
 }

 @Test
 public void testToStringHelperAddValueInt() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(1);
 toStringHelper.addValue(1);
 assertEquals(toStringHelper.toString(), "java.lang.Integer{value=1, value=1}");
 }

 @Test
 public void testToStringHelperAddValueLong() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(1L);
 toStringHelper.addValue(1L);
 assertEquals(toStringHelper.toString(), "java.lang.Long{value=1, value=1}");
 }

 @Test
 public void testToStringHelperAddValueNull() {
 Objects.ToStringHelper toStringHelper = Objects.toStringHelper(null);
 toStringHelper.addValue(null);
 assertEquals(toStringHelper.toString(), "java.lang.Object{value=null, value=null}");
 }
}