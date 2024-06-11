package org.junit.tests;

import static org.apache.commons.lang3.builder.CompareToBuilder.*;
import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.*;

public class CompareToBuilderTest {

 private Object lhs, rhs;
 private Object[] arrLhs, arrRhs;
 private long[] longArrLhs, longArrRhs;
 private int[] intArrLhs, intArrRhs;
 private short[] shortArrLhs, shortArrRhs;
 private char[] charArrLhs, charArrRhs;
 private byte[] byteArrLhs, byteArrRhs;
 private double[] doubleArrLhs, doubleArrRhs;
 private float[] floatArrLhs, floatArrRhs;
 private boolean[] booleanArrLhs, booleanArrRhs;

 @Before
 public void setUp() {
 lhs = new Object();
 rhs = new Object();
 arrLhs = new Object[5];
 arrRhs = new Object[5];
 longArrLhs = new long[]{1, 2, 3, 4, 5};
 longArrRhs = new long[]{2, 1, 3, 4, 5};
 intArrLhs = new int[]{1, 2, 3, 4, 5};
 intArrRhs = new int[]{2, 1, 3, 4, 5};
 shortArrLhs = new short[]{1, 2, 3, 4, 5};
 shortArrRhs = new short[]{2, 1, 3, 4, 5};
 charArrLhs = new char[]{'a', 'b', 'c', 'd', 'e'};
 charArrRhs = new char[]{'b', 'a', 'c', 'd', 'e'};
 byteArrLhs = new byte[]{1, 2, 3, 4, 5};
 byteArrRhs = new byte[]{2, 1, 3, 4, 5};
 doubleArrLhs = new double[]{1.0d, 2.0d, 3.0d, 4.0d, 5.0d};
 doubleArrRhs = new double[]{2.0d, 1.0d, 3.0d, 4.0d, 5.0d};
 floatArrLhs = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f};
 floatArrRhs = new float[]{2.0f, 1.0f, 3.0f, 4.0f, 5.0f};
 booleanArrLhs = new boolean[]{true, false, true, false, true};
 booleanArrRhs = new boolean[]{false, true, true, false, true};
 }

 @Test
 public void testReflectionCompare_Objects() {
 assertEquals(0, reflectionCompare(lhs, rhs));
 }

 @Test
 public void testReflectionCompare_Objects_ExcludeFields() {
 final String[] excludeFields = {"excludeField"};
 assertEquals(-1, reflectionCompare(lhs, rhs, ReflectionToStringBuilder.class, excludeFields));
 }

 @Test
 public void testReflectionCompare_Objects_Null() {
 try {
 reflectionCompare(null, rhs);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testReflectionCompare_Objects_ClassCastException() {
 try {
 reflectionCompare(lhs, "String");
 fail();
 } catch (ClassCastException e) {
 // Expected
 }
 }

 @Test
 public void testAppendSuper() {
 final int superCompareTo = 5;
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.appendSuper(superCompareTo);
 assertEquals(superCompareTo, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Objects() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 assertTrue(compareToBuilder.comparison != 0);
 }

 @Test
 public void testAppend_Objects_Comparator() {
 final Comparator<Object> comparator = new Comparator<Object>() {
 @Override public int compare(Object o1, Object o2) {
 return 1;
 }
 };
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs, comparator);
 assertTrue(compareToBuilder.comparison == 1);
 }

 @Test
 public void testAppend_Longs() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(longArrLhs[0], longArrRhs[0]);
 compareToBuilder.append(longArrLhs[1], longArrRhs[1]);
 compareToBuilder.append(longArrLhs[2], longArrRhs[2]);
 compareToBuilder.append(longArrLhs[3], longArrRhs[3]);
 compareToBuilder.append(longArrLhs[4], longArrRhs[4]);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Ints() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(intArrLhs[0], intArrRhs[0]);
 compareToBuilder.append(intArrLhs[1], intArrRhs[1]);
 compareToBuilder.append(intArrLhs[2], intArrRhs[2]);
 compareToBuilder.append(intArrLhs[3], intArrRhs[3]);
 compareToBuilder.append(intArrLhs[4], intArrRhs[4]);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Shorts() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(shortArrLhs[0], shortArrRhs[0]);
 compareToBuilder.append(shortArrLhs[1], shortArrRhs[1]);
 compareToBuilder.append(shortArrLhs[2], shortArrRhs[2]);
 compareToBuilder.append(shortArrLhs[3], shortArrRhs[3]);
 compareToBuilder.append(shortArrLhs[4], shortArrRhs[4]);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Chars() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(charArrLhs[0], charArrRhs[0]);
 compareToBuilder.append(charArrLhs[1], charArrRhs[1]);
 compareToBuilder.append(charArrLhs[2], charArrRhs[2]);
 compareToBuilder.append(charArrLhs[3], charArrRhs[3]);
 compareToBuilder.append(charArrLhs[4], charArrRhs[4]);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Bytes() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(byteArrLhs[0], byteArrRhs[0]);
 compareToBuilder.append(byteArrLhs[1], byteArrRhs[1]);
 compareToBuilder.append(byteArrLhs[2], byteArrRhs[2]);
 compareToBuilder.append(byteArrLhs[3], byteArrRhs[3]);
 compareToBuilder.append(byteArrLhs[4], byteArrRhs[4]);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Doubles() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(doubleArrLhs[0], doubleArrRhs[0]);
 compareToBuilder.append(doubleArrLhs[1], doubleArrRhs[1]);
 compareToBuilder.append(doubleArrLhs[2], doubleArrRhs[2]);
 compareToBuilder.append(doubleArrLhs[3], doubleArrRhs[3]);
 compareToBuilder.append(doubleArrLhs[4], doubleArrRhs[4]);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Floats() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(floatArrLhs[0], floatArrRhs[0]);
 compareToBuilder.append(floatArrLhs[1], floatArrRhs[1]);
 compareToBuilder.append(floatArrLhs[2], floatArrRhs[2]);
 compareToBuilder.append(floatArrLhs[3], floatArrRhs[3]);
 compareToBuilder.append(floatArrLhs[4], floatArrRhs[4]);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Booleans() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(booleanArrLhs[0], booleanArrRhs[0]);
 compareToBuilder.append(booleanArrLhs[1], booleanArrRhs[1]);
 compareToBuilder.append(booleanArrLhs[2], booleanArrRhs[2]);
 compareToBuilder.append(booleanArrLhs[3], booleanArrRhs[3]);
 compareToBuilder.append(booleanArrLhs[4], booleanArrRhs[4]);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Objects() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(arrLhs, arrRhs);
 assertTrue(compareToBuilder.comparison != 0);
 }

 @Test
 public void testAppend_Arrays_Objects_Comparator() {
 final Comparator<Object> comparator = new Comparator<Object>() {
 @Override public int compare(Object o1, Object o2) {
 return 1;
 }
 };
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(arrLhs, arrRhs, comparator);
 assertTrue(compareToBuilder.comparison == 1);
 }

 @Test
 public void testAppend_Arrays_Longs() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(longArrLhs, longArrRhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Ints() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(intArrLhs, intArrRhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Shorts() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(shortArrLhs, shortArrRhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Chars() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(charArrLhs, charArrRhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Bytes() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(byteArrLhs, byteArrRhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Doubles() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(doubleArrLhs, doubleArrRhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Floats() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(floatArrLhs, floatArrRhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Booleans() {
 final CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(booleanArrLhs, booleanArrRhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

}