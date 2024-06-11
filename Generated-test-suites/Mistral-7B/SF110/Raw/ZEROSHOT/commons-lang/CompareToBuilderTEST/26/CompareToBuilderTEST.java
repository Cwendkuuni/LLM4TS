package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CompareToBuilderTest {

 @Test
 public void testReflectionCompare() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object() {
 int a = 1;
 int b = 2;
 };
 Object rhs = new Object() {
 int a = 1;
 int b = 3;
 };
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, result);

 lhs = new Object() {
 int a = 1;
 int b = 3;
 };
 result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(1, result);
 }

 @Test
 public void testReflectionCompareWithExcludeFields() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object() {
 int a = 1;
 int b = 2;
 int c = 3;
 };
 Object rhs = new Object() {
 int a = 1;
 int b = 2;
 int c = 4;
 };
 int result = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("c"));
 assertEquals(0, result);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompareWithNull() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = null;
 Object rhs = new Object();
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompareWithDifferentTypes() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object();
 Object rhs = new Integer(1);
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testReflectionAppend() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object() {
 int a = 1;
 int b = 2;
 int c = 3;
 };
 Object rhs = new Object() {
 int a = 1;
 int b = 2;
 int c = 4;
 };
 Field fieldA = null;
 try {
 fieldA = lhs.getClass().getDeclaredField("a");
 } catch (NoSuchFieldException e) {
 fail("Expected field 'a' to exist.");
 }

 compareToBuilder.reflectAppend(lhs, rhs, lhs.getClass(), compareToBuilder, false, new String[0]);
 compareToBuilder.append(fieldA.get(lhs), fieldA.get(rhs));
 int result = compareToBuilder.toComparison();
 assertEquals(-1, result);

 compareToBuilder = new CompareToBuilder();
 compareToBuilder.reflectAppend(lhs, rhs, lhs.getClass(), compareToBuilder, false, new String[0]);
 compareToBuilder.reflectAppend(lhs, rhs, lhs.getClass().getSuperclass(), compareToBuilder, false, new String[0]);
 result = compareToBuilder.toComparison();
 assertEquals(-1, result);
 }

 @Test
 public void testAppendSuper() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.appendSuper(-1);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppend() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();

 // test append for various primitive types
 compareToBuilder = new CompareToBuilder();
 int lhs = 1;
 int rhs = 2;
 compareToBuilder.append(lhs, rhs);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhs = 2;
 rhs = 1;
 compareToBuilder.append(lhs, rhs);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhs = 1;
 rhs = 1;
 compareToBuilder.append(lhs, rhs);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for Object types
 compareToBuilder = new CompareToBuilder();
 Object lhsObj = new Object() {
 int a = 1;
 };
 Object rhsObj = new Object() {
 int a = 2;
 };
 compareToBuilder.append(lhsObj, rhsObj);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsObj = new Object() {
 int a = 2;
 };
 compareToBuilder.append(lhsObj, rhsObj);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsObj = rhsObj;
 compareToBuilder.append(lhsObj, rhsObj);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for arrays
 compareToBuilder = new CompareToBuilder();
 Object[] lhsArr = {1, 2};
 Object[] rhsArr = {1, 3};
 compareToBuilder.append(lhsArr, rhsArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsArr = new Object[] {3, 2};
 compareToBuilder.append(lhsArr, rhsArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsArr = rhsArr;
 compareToBuilder.append(lhsArr, rhsArr);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for long arrays
 compareToBuilder = new CompareToBuilder();
 long[] lhsLongArr = {1L, 2L};
 long[] rhsLongArr = {1L, 3L};
 compareToBuilder.append(lhsLongArr, rhsLongArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsLongArr = new long[] {3L, 2L};
 compareToBuilder.append(lhsLongArr, rhsLongArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsLongArr = rhsLongArr;
 compareToBuilder.append(lhsLongArr, rhsLongArr);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for int arrays
 compareToBuilder = new CompareToBuilder();
 int[] lhsIntArr = {1, 2};
 int[] rhsIntArr = {1, 3};
 compareToBuilder.append(lhsIntArr, rhsIntArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsIntArr = new int[] {3, 2};
 compareToBuilder.append(lhsIntArr, rhsIntArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsIntArr = rhsIntArr;
 compareToBuilder.append(lhsIntArr, rhsIntArr);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for short arrays
 compareToBuilder = new CompareToBuilder();
 short[] lhsShortArr = {1, 2};
 short[] rhsShortArr = {1, 3};
 compareToBuilder.append(lhsShortArr, rhsShortArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsShortArr = new short[] {3, 2};
 compareToBuilder.append(lhsShortArr, rhsShortArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsShortArr = rhsShortArr;
 compareToBuilder.append(lhsShortArr, rhsShortArr);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for char arrays
 compareToBuilder = new CompareToBuilder();
 char[] lhsCharArr = {'a', 'b'};
 char[] rhsCharArr = {'a', 'c'};
 compareToBuilder.append(lhsCharArr, rhsCharArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsCharArr = new char[] {'c', 'a'};
 compareToBuilder.append(lhsCharArr, rhsCharArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsCharArr = rhsCharArr;
 compareToBuilder.append(lhsCharArr, rhsCharArr);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for byte arrays
 compareToBuilder = new CompareToBuilder();
 byte[] lhsByteArr = {1, 2};
 byte[] rhsByteArr = {1, 3};
 compareToBuilder.append(lhsByteArr, rhsByteArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsByteArr = new byte[] {3, 2};
 compareToBuilder.append(lhsByteArr, rhsByteArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsByteArr = rhsByteArr;
 compareToBuilder.append(lhsByteArr, rhsByteArr);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for double arrays
 compareToBuilder = new CompareToBuilder();
 double[] lhsDoubleArr = {1.0, 2.0};
 double[] rhsDoubleArr = {1.0, 3.0};
 compareToBuilder.append(lhsDoubleArr, rhsDoubleArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsDoubleArr = new double[] {3.0, 2.0};
 compareToBuilder.append(lhsDoubleArr, rhsDoubleArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsDoubleArr = rhsDoubleArr;
 compareToBuilder.append(lhsDoubleArr, rhsDoubleArr);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for float arrays
 compareToBuilder = new CompareToBuilder();
 float[] lhsFloatArr = {1.0f, 2.0f};
 float[] rhsFloatArr = {1.0f, 3.0f};
 compareToBuilder.append(lhsFloatArr, rhsFloatArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsFloatArr = new float[] {3.0f, 2.0f};
 compareToBuilder.append(lhsFloatArr, rhsFloatArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsFloatArr = rhsFloatArr;
 compareToBuilder.append(lhsFloatArr, rhsFloatArr);
 assertEquals(0, compareToBuilder.toComparison());

 // test append for boolean arrays
 compareToBuilder = new CompareToBuilder();
 boolean[] lhsBooleanArr = {true, false};
 boolean[] rhsBooleanArr = {false, true};
 compareToBuilder.append(lhsBooleanArr, rhsBooleanArr);
 assertEquals(-1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsBooleanArr = new boolean[] {false, true};
 compareToBuilder.append(lhsBooleanArr, rhsBooleanArr);
 assertEquals(1, compareToBuilder.toComparison());

 compareToBuilder = new CompareToBuilder();
 lhsBooleanArr = rhsBooleanArr;
 compareToBuilder.append(lhsBooleanArr, rhsBooleanArr);
 assertEquals(0, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendWithComparator() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String[] lhsStringArr = {"a", "b"};
 String[] rhsStringArr = {"b", "a"};
 Comparator<String[]> comparator = new Comparator<String[]>() {
 @Override
 public int compare(String[] lhs, String[] rhs) {
 int comparison = 0;
 for (int i = 0; i < lhs.length && comparison == 0; ++i) {
 comparison = lhs[i].compareTo(rhs[i]);
 }
 return comparison;
 }
 };
 compareToBuilder.append(lhsStringArr, rhsStringArr, comparator);
 assertEquals(-1, compareToBuilder.toComparison());
 }
}