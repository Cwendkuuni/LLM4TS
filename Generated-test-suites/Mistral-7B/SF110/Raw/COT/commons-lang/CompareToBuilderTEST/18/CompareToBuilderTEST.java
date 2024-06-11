package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.*;

public class CompareToBuilderTest {

 private CompareToBuilder builder;
 private Object lhs;
 private Object rhs;

 @Before
 public void setUp() {
 lhs = new Object() {};
 rhs = new Object() {};
 builder = new CompareToBuilder();
 }

 @Test
 public void testComparisonOfObjects() {
 builder.append(lhs, rhs);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testComparisonOfNullAndNonNullObjects() {
 builder.append(lhs, rhs);
 builder.append(null, rhs);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonOfNonNullAndNullObjects() {
 builder.append(lhs, rhs);
 builder.append(lhs, null);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testComparisonOfArraysOfObjects() {
 Object[] lhsArray = {new Object() {}, new Object() {}};
 Object[] rhsArray = {new Object() {}, new Object() {}};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testComparisonOfNullAndNonNullArrays() {
 Object[] lhsArray = {new Object() {}};
 Object[] rhsArray = {new Object() {}, new Object() {}};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonOfNonNullAndNullArrays() {
 Object[] lhsArray = {new Object() {}, new Object() {}};
 Object[] rhsArray = {new Object() {}};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testNullLhs() {
 builder.append(null, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testLhsNotInstanceOfRhs() {
 builder.append(new String[]{}, lhs);
 }

 @Test
 public void testComparisonOfObjectsWithExcludedFields() {
 builder.append(lhs, rhs, "field1", "field2");
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testComparisonOfObjectsWithTransientFields() {
 builder.append(lhs, rhs, true);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testComparisonOfArraysWithLengthMismatch() {
 Object[] lhsArray = {new Object() {}, new Object() {}};
 Object[] rhsArray = {new Object() {}};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonOfArraysWithDifferentTypes() {
 Object[] lhsArray = {new Object() {}, new String() {}};
 Object[] rhsArray = {new Object() {}, new Integer() {}};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testReflectionCompare() {
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompareWithExcludedFields() {
 String[] excludeFields = {"field1", "field2"};
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, excludeFields);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompareWithTransientFields() {
 boolean compareTransients = true;
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, compareTransients);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompareWithSuperClass() {
 Class<?> superClass = Object[].class;
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, superClass);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompareWithSuperClassAndExcludedFields() {
 Class<?> superClass = Object[].class;
 String[] excludeFields = {"field1", "field2"};
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, superClass, excludeFields);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompareWithSuperClassAndTransientFields() {
 Class<?> superClass = Object[].class;
 boolean compareTransients = true;
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, superClass, compareTransients);
 assertEquals(0, comparison);
 }
}