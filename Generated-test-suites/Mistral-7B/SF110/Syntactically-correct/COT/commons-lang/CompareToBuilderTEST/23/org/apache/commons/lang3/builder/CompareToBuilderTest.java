package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object createTestObject() {
 return new TestObject();
 }

 private static class TestObject {
 int field1 = 1;
 long field2 = 1L;
 short field3 = 1;
 char field4 = '1';
 byte field5 = 1;
 double field6 = 1.0;
 float field7 = 1.0f;
 boolean field8 = true;
 boolean[] booleanArray = new boolean[]{true, false};
 int[] intArray = new int[]{1, 2, 3};
 long[] longArray = new long[]{1, 2, 3};
 short[] shortArray = new short[]{1, 2, 3};
 char[] charArray = new char[]{'1', '2', '3'};
 byte[] byteArray = new byte[]{1, 2, 3};
 double[] doubleArray = new double[]{1.0, 2.0, 3.0};
 float[] floatArray = new float[]{1.0f, 2.0f, 3.0f};
 boolean booleanValue = true;
 boolean transientBoolean = false;

 transient int transientInt = 1;
 }

 @Test
 public void testReflectionCompare_Objects() {
 Object lhs = createTestObject();
 Object rhs = createTestObject();
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompare_NullLhs() {
 Object lhs = null;
 Object rhs = createTestObject();
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompare_NullRhs() {
 Object lhs = createTestObject();
 Object rhs = null;
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompare_DifferentTypes() {
 Object lhs = createTestObject();
 Object rhs = new Object();
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testAppend_IntegerBuilder_Integer() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = 1;
 Object rhs = 2;
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Long() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = 1L;
 Object rhs = 2L;
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Short() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = (short)1;
 Object rhs = (short)2;
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Char() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = '1';
 Object rhs = '2';
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Byte() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = (byte)1;
 Object rhs = (byte)2;
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Double() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = 1.0;
 Object rhs = 2.0;
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Float() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = 1.0f;
 Object rhs = 2.0f;
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Boolean() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = true;
 Object rhs = false;
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Array_NoComparator() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = new Object[]{1, 2, 3};
 Object rhs = new Object[]{1, 2, 3};
 builder.append((Object)lhs, rhs);
 assertEquals(0, builder.toComparison());
 }

 @Test
 public void testAppend_IntegerBuilder_Array_Comparator() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = new Object[]{1, 2, 3};
 Object rhs = new Object[]{2, 1, 3};
 builder.append((Object)lhs, rhs, new Comparator<Object>() {
 @Override public int compare(Object o1, Object o2) {
 return ((Comparable<Object>)o1).compareTo(o2);
 }
 });
 assertEquals(-1, builder.toComparison());
 }

 @Test(expected = NullPointerException.class)
 public void testAppend_IntegerBuilder_NullArray() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = null;
 Object rhs = new Object[]{1, 2, 3};
 builder.append(lhs, rhs);
 }

 @Test(expected = NullPointerException.class)
 public void testAppend_IntegerBuilder_NullArrayElement() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = new Object[]{1, null, 3};
 Object rhs = new Object[]{1, 2, 3};
 builder.append(lhs, rhs);
 }

 @Test(expected = ArrayStoreException.class)
 public void testAppend_IntegerBuilder_IncompatibleTypes() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = new Object[]{1, "2", 3};
 Object rhs = new Object[]{1, 2, 3};
 builder.append(lhs, rhs);
 }

 @Test
 public void testAppend_IntegerBuilder_Array_DifferentLengths() {
 CompareToBuilder builder = new CompareToBuilder();
 Object lhs = new Object[]{1, 2, 3};
 Object rhs = new Object[]{1, 2};
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendSuper() {
 CompareToBuilder builder = new CompareToBuilder();
 builder.appendSuper(1);
 assertEquals(1, builder.toComparison());
 }

 @Test
 public void testToComparison() {
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(1, 2);
 builder.appendSuper(1);
 assertEquals(-1, builder.toComparison());
 }

}