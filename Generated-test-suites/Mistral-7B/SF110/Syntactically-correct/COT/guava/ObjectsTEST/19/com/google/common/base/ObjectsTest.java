package com.example.junit.tests;

import static org.junit.Assert.*;

import com.google.common.base.Objects;

import java.util.Arrays;

public class ObjectsTest {

 private Object[] getNullObjectArray() {
 return new Object[0];
 }

 @Test
 public void testEqual() {
 // Basic functionality test
 assertTrue(Objects.equal(null, null));
 assertTrue(Objects.equal(new Object(), new Object()));
 assertFalse(Objects.equal(new Object(), new String("test")));
 // Edge cases and exception handling test
 try {
 Objects.equal(null, new Object());
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 assertTrue(Objects.equal(new Object(), new Object()));
 assertFalse(Objects.equal(new Object(), null));
 assertFalse(Objects.equal(new Object(), new String("test")));
 assertFalse(Objects.equal(new String("test1"), new String("test2")));
 assertFalse(Objects.equal(new boolean[0], new int[0]));
 assertFalse(Objects.equal(new int[0], new long[0]));
 // Test with arrays
 Object[] array1 = {new Object(), new Object()};
 Object[] array2 = {new Object(), new Object()};
 Object[] array3 = {new Object(), null};
 Object[] array4 = {null, new Object()};
 Object[] array5 = {new Object(), new String("test")};
 Object[] array6 = {new String("test1"), new String("test2")};
 Object[] array7 = {new int[0], new long[0]};
 Object[] array8 = {new int[0], new int[0]};
 // Test with primitive types
 byte b1 = 1, b2 = 2;
 short s1 = 1, s2 = 2;
 int i1 = 1, i2 = 2;
 long l1 = 1L, l2 = 2L;
 float f1 = 1.0f, f2 = 2.0f;
 double d1 = 1.0, d2 = 2.0;
 char c1 = 'a', c2 = 'b';
 boolean bool1 = true, bool2 = false;
 // Test with primitive types arrays
 byte[] bytes1 = {1, 2};
 byte[] bytes2 = {1, 2};
 short[] shorts1 = {1, 2};
 short[] shorts2 = {1, 2};
 int[] ints1 = {1, 2};
 int[] ints2 = {1, 2};
 long[] longs1 = {1L, 2L};
 long[] longs2 = {1L, 2L};
 float[] floats1 = {1.0f, 2.0f};
 float[] floats2 = {1.0f, 2.0f};
 double[] doubles1 = {1.0, 2.0};
 double[] doubles2 = {1.0, 2.0};
 char[] chars1 = {'a', 'b'};
 char[] chars2 = {'a', 'b'};
 boolean[] booleans1 = {true, false};
 boolean[] booleans2 = {true, false};
 // Test cases
 assertEquals(Objects.equal(null, null), true);
 assertEquals(Objects.equal(new Object(), new Object()), true);
 assertEquals(Objects.equal(new Object(), null), false);
 assertEquals(Objects.equal(new Object(), new String("test")), false);
 assertEquals(Objects.equal(new String("test1"), new String("test2")), false);
 assertEquals(Objects.equal(new boolean[0], new int[0]), false);
 assertEquals(Objects.equal(new int[0], new long[0]), false);
 assertEquals(Objects.equal(array1, array2), true);
 assertEquals(Objects.equal(array1, array3), false);
 assertEquals(Objects.equal(array3, array4), false);
 assertEquals(Objects.equal(array1, array5), false);
 assertEquals(Objects.equal(array6, array6), true);
 assertEquals(Objects.equal(array7, array8), false);
 assertEquals(Objects.equal(b1, b1), true);
 assertEquals(Objects.equal(b1, b2), false);
 assertEquals(Objects.equal(s1, s1), true);
 assertEquals(Objects.equal(s1, s2), false);
 assertEquals(Objects.equal(i1, i1), true);
 assertEquals(Objects.equal(i1, i2), false);
 assertEquals(Objects.equal(l1, l1), true);
 assertEquals(Objects.equal(l1, l2), false);
 assertEquals(Objects.equal(f1, f1), true);
 assertEquals(Objects.equal(f1, f2), false);
 assertEquals(Objects.equal(d1, d1), true);
 assertEquals(Objects.equal(d1, d2), false);
 assertEquals(Objects.equal(c1, c1), true);
 assertEquals(Objects.equal(c1, c2), false);
 assertEquals(Objects.equal(bool1, bool1), true);
 assertEquals(Objects.equal(bool1, bool2), false);
 assertEquals(Objects.equal(bytes1, bytes2), true);
 assertEquals(Objects.equal(shorts1, shorts2), true);
 assertEquals(Objects.equal(ints1, ints2), true);
 assertEquals(Objects.equal(longs1, longs2), true);
 assertEquals(Objects.equal(floats1, floats2), true);
 assertEquals(Objects.equal(doubles1, doubles2), true);
 assertEquals(Objects.equal(chars1, chars2), true);
 assertEquals(Objects.equal(booleans1, booleans2), true);
 }

 @Test
 public void testHashCode() {
 // Basic functionality test
 assertEquals(Objects.hashCode(new Object[0]), 0);
 assertEquals(Objects.hashCode(new Object()), 0);
 Object[] array = {new Object(), new Object()};
 assertEquals(Objects.hashCode(array), Arrays.hashCode(array));
 // Edge cases and exception handling test
 assertEquals(Objects.hashCode(null), 0);
 try {
 Objects.hashCode(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = DeprecatedException.class)
 public void testToStringHelperDeprecated() {
 // Test toStringHelper deprecated methods
 Object obj = new Object();
 Objects.toStringHelper(obj).toString();
 Objects.toStringHelper(obj.getClass()).toString();
 Objects.toStringHelper("className").toString();
 }

 @Test
 public void testToStringHelper() {
 // Basic functionality test
 Object obj = new Object();
 StringBuilder expected = new StringBuilder("Object{");
 Objects.ToStringHelper helper = Objects.toStringHelper(obj).omitNullValues();
 helper.add("field1", "value1");
 helper.add("field2", "value2");
 assertEquals(helper.toString(), expected.toString());
 // Edge cases and exception handling test
 try {
 Objects.ToStringHelper helper2 = Objects.toStringHelper(null).add("field", null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 try {
 Objects.ToStringHelper helper3 = Objects.toStringHelper(new Object()).add("field", null).omitNullValues();
 assertEquals(helper3.toString(), "Object{field=null}");
 } catch (NullPointerException e) {
 // Unexpected exception
 fail("Unexpected NullPointerException");
 }
 }

 @Test
 public void testFirstNonNull() {
 // Basic functionality test
 Object first = new Object();
 Object second = new Object();
 Object result = Objects.firstNonNull(first, second);
 assertEquals(result, first);
 // Edge cases and exception handling test
 Object firstNull = null;
 Object secondNull = null;
 Object resultNull = Objects.firstNonNull(firstNull, secondNull);
 assertEquals(resultNull, secondNull);
 try {
 Object resultException = Objects.firstNonNull(null, new Object());
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }
}