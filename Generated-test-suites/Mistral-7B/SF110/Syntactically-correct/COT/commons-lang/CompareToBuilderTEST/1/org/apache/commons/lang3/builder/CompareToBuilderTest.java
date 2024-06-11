package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;

 @Test
 public void testAppendSuper() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.appendSuper(5);
 assertEquals(5, compareToBuilder.toComparison());
 }

 @Test
 public void testReflectionCompare() {
 Object lhs = new Object() {};
 Object rhs = new Object() {};
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);

 lhs = new Object() { int i = 1; };
 rhs = new Object() { int i = 1; };
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);

 lhs = new Object() { int i = 1; };
 rhs = new Object() { int i = 2; };
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, comparison);

 lhs = new Object() { int i = 2; };
 rhs = new Object() { int i = 1; };
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(1, comparison);
 }

 @Test
 public void testReflectionCompareWithExcludeFields() {
 Object lhs = new ExcludedFieldClass() { int i = 1; };
 Object rhs = new ExcludedFieldClass() { int i = 1; };
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field1");
 assertEquals(0, comparison);

 lhs = new ExcludedFieldClass() { int i = 1; };
 rhs = new ExcludedFieldClass() { int i = 2; };
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field1");
 assertEquals(-1, comparison);

 lhs = new ExcludedFieldClass() { int i = 2; };
 rhs = new ExcludedFieldClass() { int i = 1; };
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs, "field1");
 assertEquals(1, comparison);
 }

 @Test
 public void testReflectionCompareWithTransients() {
 Object lhs = new TransientFieldClass() { boolean transientField = true; int i = 1; };
 Object rhs = new TransientFieldClass() { boolean transientField = true; int i = 1; };
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
 assertEquals(0, comparison);

 lhs = new TransientFieldClass() { boolean transientField = true; int i = 1; };
 rhs = new TransientFieldClass() { boolean transientField = false; int i = 1; };
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
 assertEquals(1, comparison);

 lhs = new TransientFieldClass() { boolean transientField = false; int i = 1; };
 rhs = new TransientFieldClass() { boolean transientField = true; int i = 1; };
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();

 int lhsInt = 1;
 int rhsInt = 1;
 compareToBuilder.append(lhsInt, rhsInt);
 assertEquals(0, compareToBuilder.toComparison());

 lhsInt = 1;
 rhsInt = 2;
 compareToBuilder.append(lhsInt, rhsInt);
 assertEquals(-1, compareToBuilder.toComparison());

 lhsInt = 2;
 rhsInt = 1;
 compareToBuilder.append(lhsInt, rhsInt);
 assertEquals(1, compareToBuilder.toComparison());

 long lhsLong = 1L;
 long rhsLong = 1L;
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhsLong, rhsLong);
 assertEquals(0, compareToBuilder.toComparison());

 lhsLong = 1L;
 rhsLong = 2L;
 compareToBuilder.append(lhsLong, rhsLong);
 assertEquals(-1, compareToBuilder.toComparison());

 lhsLong = 2L;
 rhsLong = 1L;
 compareToBuilder.append(lhsLong, rhsLong);
 assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendArray() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();

 Object[] lhsArray = new Object[] { 1, 2, 3 };
 Object[] rhsArray = new Object[] { 1, 2, 3 };
 compareToBuilder.append(lhsArray, rhsArray);
 assertEquals(0, compareToBuilder.toComparison());

 lhsArray = new Object[] { 1, 2, 3 };
 rhsArray = new Object[] { 1, 2, 4 };
 compareToBuilder.append(lhsArray, rhsArray);
 assertEquals(-1, compareToBuilder.toComparison());

 lhsArray = new Object[] { 1, 2, 4 };
 rhsArray = new Object[] { 1, 2, 3 };
 compareToBuilder.append(lhsArray, rhsArray);
 assertEquals(1, compareToBuilder.toComparison());

 int[] intArray = { 1, 2, 3 };
 int[] intArrayRhs = { 1, 2, 3 };
 compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(intArray, intArrayRhs);
 assertEquals(0, compareToBuilder.toComparison());

 int[] intArrayLhs = { 1, 2, 3 };
 int[] intArrayRhs = { 1, 2, 4 };
 compareToBuilder.append(intArrayLhs, intArrayRhs);
 assertEquals(-1, compareToBuilder.toComparison());

 int[] intArrayLhs2 = { 1, 2, 4 };
 int[] intArrayRhs2 = { 1, 2, 3 };
 compareToBuilder.append(intArrayLhs2, intArrayRhs2);
 assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendDouble() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();

 double lhsDouble = 1.0;
 double rhsDouble = 1.0;
 compareToBuilder.append(lhsDouble, rhsDouble);
 assertEquals(0, compareToBuilder.toComparison());

 lhsDouble = 1.0;
 rhsDouble = 2.0;
 compareToBuilder.append(lhsDouble, rhsDouble);
 assertEquals(-1, compareToBuilder.toComparison());

 lhsDouble = 2.0;
 rhsDouble = 1.0;
 compareToBuilder.append(lhsDouble, rhsDouble);
 assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendFloat() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();

 float lhsFloat = 1.0f;
 float rhsFloat = 1.0f;
 compareToBuilder.append(lhsFloat, rhsFloat);
 assertEquals(0, compareToBuilder.toComparison());

 lhsFloat = 1.0f;
 rhsFloat = 2.0f;
 compareToBuilder.append(lhsFloat, rhsFloat);
 assertEquals(-1, compareToBuilder.toComparison());

 lhsFloat = 2.0f;
 rhsFloat = 1.0f;
 compareToBuilder.append(lhsFloat, rhsFloat);
 assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendBoolean() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();

 boolean lhsBoolean = true;
 boolean rhsBoolean = true;
 compareToBuilder.append(lhsBoolean, rhsBoolean);
 assertEquals(0, compareToBuilder.toComparison());

 lhsBoolean = true;
 rhsBoolean = false;
 compareToBuilder.append(lhsBoolean, rhsBoolean);
 assertEquals(1, compareToBuilder.toComparison());

 lhsBoolean = false;
 rhsBoolean = true;
 compareToBuilder.append(lhsBoolean, rhsBoolean);
 assertEquals(-1, compareToBuilder.toComparison());

 lhsBoolean = false;
 rhsBoolean = false;
 compareToBuilder.append(lhsBoolean, rhsBoolean);
 assertEquals(0, compareToBuilder.toComparison());
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompareWithNullLhs() {
 CompareToBuilder.reflectionCompare(null, new Object() {}, false, null, new String[0]);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompareWithNullRhs() {
 CompareToBuilder.reflectionCompare(new Object() {}, null, false, null, new String[0]);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompareWithDifferentClasses() {
 CompareToBuilder.reflectionCompare(new Object() {}, new String() {}, false, null, new String[0]);
 }
}

 class ExcludedFieldClass {
 private int field1;
 private int field2;
 }

 class TransientFieldClass {
 private int i;
 private boolean transientField;
 }