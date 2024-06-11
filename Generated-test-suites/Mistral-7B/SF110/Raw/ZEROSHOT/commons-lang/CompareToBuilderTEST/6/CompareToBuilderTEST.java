package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 @Test
 public void testReflectionCompare() {
 int result = CompareToBuilder.reflectionCompare(new Integer(1), new Integer(1));
 assertEquals(0, result);

 result = CompareToBuilder.reflectionCompare(new Integer(1), new Integer(2));
 assertEquals(-1, result);

 result = CompareToBuilder.reflectionCompare(new Integer(2), new Integer(1));
 assertEquals(1, result);
 }

 @Test
 public void testReflectionCompareBoolean() {
 boolean lhs = true;
 boolean rhs = false;
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, result);

 lhs = false;
 rhs = true;
 result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(1, result);
 }

 @Test
 public void testReflectionCompareWithExcludeFields() {
 Integer lhs = new Integer(1);
 Integer rhs = new Integer(2);
 int result = CompareToBuilder.reflectionCompare(lhs, rhs, "comparison");
 assertEquals(-1, result);
 }

 @Test
 public void testReflectionCompareWithArray() {
 Integer[] lhs = {1, 2, 3};
 Integer[] rhs = {1, 2, 3};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new Integer[]{1, 2, 3};
 rhs = new Integer[]{1, 2, 4};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);

 lhs = new Integer[]{1, 2, 3};
 rhs = new Integer[]{1, 2, 3};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, new Comparator<Object[]>() {
 @Override
 public int compare(Object[] o1, Object[] o2) {
 for (int i = 0; i < o1.length; i++) {
 int c = CompareToBuilder.reflectionCompare(o1[i], o2[i]);
 if (c != 0) {
 return c;
 }
 }
 return 0;
 }
 });
 assertEquals(0, result);
 }

 @Test
 public void testReflectionCompareWithLongArray() {
 long[] lhs = {1, 2, 3};
 long[] rhs = {1, 2, 3};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new long[]{1, 2, 3};
 rhs = new long[]{1, 2, 4};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);
 }

 @Test
 public void testReflectionCompareWithIntArray() {
 int[] lhs = {1, 2, 3};
 int[] rhs = {1, 2, 3};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new int[]{1, 2, 3};
 rhs = new int[]{1, 2, 4};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);
 }

 @Test
 public void testReflectionCompareWithShortArray() {
 short[] lhs = {1, 2, 3};
 short[] rhs = {1, 2, 3};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new short[]{1, 2, 3};
 rhs = new short[]{1, 2, 4};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);
 }

 @Test
 public void testReflectionCompareWithCharArray() {
 char[] lhs = {'a', 'b', 'c'};
 char[] rhs = {'a', 'b', 'c'};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new char[]{'a', 'b', 'c'};
 rhs = new char[]{'a', 'b', 'd'};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);
 }

 @Test
 public void testReflectionCompareWithByteArray() {
 byte[] lhs = {1, 2, 3};
 byte[] rhs = {1, 2, 3};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new byte[]{1, 2, 3};
 rhs = new byte[]{1, 2, 4};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);
 }

 @Test
 public void testReflectionCompareWithDoubleArray() {
 double[] lhs = {1.0, 2.0, 3.0};
 double[] rhs = {1.0, 2.0, 3.0};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new double[]{1.0, 2.0, 3.0};
 rhs = new double[]{1.0, 2.0, 4.0};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);
 }

 @Test
 public void testReflectionCompareWithFloatArray() {
 float[] lhs = {1.0f, 2.0f, 3.0f};
 float[] rhs = {1.0f, 2.0f, 3.0f};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new float[]{1.0f, 2.0f, 3.0f};
 rhs = new float[]{1.0f, 2.0f, 4.0f};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);
 }

 @Test
 public void testReflectionCompareWithBooleanArray() {
 boolean[] lhs = {true, false, true};
 boolean[] rhs = {true, false, true};
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);

 lhs = new boolean[]{true, false, true};
 rhs = new boolean[]{true, false, false};
 result = CompareToBuilder.reflectionCompare(lhs, rhs, null);
 assertEquals(-1, result);
 }

 @Test
 public void testAppend() {
 Object lhs = new Integer(1);
 Object rhs = new Integer(2);
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());

 lhs = new Integer(1);
 rhs = new Integer(1);
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());
 }

 @Test
 public void testAppendWithComparator() {
 Object lhs = new Integer(1);
 Object rhs = new Integer(2);
 Comparator<Object> comparator = new Comparator<Object>() {
 @Override
 public int compare(Object o1, Object o2) {
 return ((Integer)o1).compareTo((Integer)o2);
 }
 };
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs, comparator);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithLong() {
 long lhs = 1;
 long rhs = 2;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithInt() {
 int lhs = 1;
 int rhs = 2;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithShort() {
 short lhs = 1;
 short rhs = 2;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithChar() {
 char lhs = 'a';
 char rhs = 'b';
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithByte() {
 byte lhs = 1;
 byte rhs = 2;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithDouble() {
 double lhs = 1.0;
 double rhs = 2.0;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1.0, builder.toComparison());
 }

 @Test
 public void testAppendWithFloat() {
 float lhs = 1.0f;
 float rhs = 2.0f;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1.0f, builder.toComparison());
 }

 @Test
 public void testAppendWithBoolean() {
 boolean lhs = false;
 boolean rhs = true;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithArray() {
 Object[] lhs = {new Integer(1), new Integer(2), new Integer(3)};
 Object[] rhs = {new Integer(1), new Integer(2), new Integer(3)};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(0, builder.toComparison());

 lhs = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
 rhs = new Object[]{new Integer(1), new Integer(2), new Integer(4)};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithLongArray() {
 long[] lhs = {1, 2, 3};
 long[] rhs = {1, 2, 3};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());

 lhs = new long[]{1, 2, 3};
 rhs = new long[]{1, 2, 4};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithIntArray() {
 int[] lhs = {1, 2, 3};
 int[] rhs = {1, 2, 3};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());

 lhs = new int[]{1, 2, 3};
 rhs = new int[]{1, 2, 4};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithShortArray() {
 short[] lhs = {1, 2, 3};
 short[] rhs = {1, 2, 3};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());

 lhs = new short[]{1, 2, 3};
 rhs = new short[]{1, 2, 4};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithCharArray() {
 char[] lhs = {'a', 'b', 'c'};
 char[] rhs = {'a', 'b', 'c'};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());

 lhs = new char[]{'a', 'b', 'c'};
 rhs = new char[]{'a', 'b', 'd'};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithByteArray() {
 byte[] lhs = {1, 2, 3};
 byte[] rhs = {1, 2, 3};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());

 lhs = new byte[]{1, 2, 3};
 rhs = new byte[]{1, 2, 4};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppendWithDoubleArray() {
 double[] lhs = {1.0, 2.0, 3.0};
 double[] rhs = {1.0, 2.0, 3.0};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());

 lhs = new double[]{1.0, 2.0, 3.0};
 rhs = new double[]{1.0, 2.0, 4.0};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1.0, builder.toComparison());
 }

 @Test
 public void testAppendWithFloatArray() {
 float[] lhs = {1.0f, 2.0f, 3.0f};
 float[] rhs = {1.0f, 2.0f, 3.0f};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());

 lhs = new float[]{1.0f, 2.0f, 3.0f};
 rhs = new float[]{1.0f, 2.0f, 4.0f};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1.0f, builder.toComparison());
 }

 @Test
 public void testAppendWithBooleanArray() {
 boolean[] lhs = {true, false, true};
 boolean[] rhs = {true, false, true};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());

 lhs = new boolean[]{true, false, true};
 rhs = new boolean[]{true, false, false};
 builder = new CompareToBuilder();
 builder.append(lhs, rhs, null);
 assertEquals(-1, builder.toComparison());
 }
}