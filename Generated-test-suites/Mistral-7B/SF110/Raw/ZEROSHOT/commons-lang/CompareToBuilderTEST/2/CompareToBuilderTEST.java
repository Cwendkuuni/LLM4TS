package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import java.lang.reflect.*;
import static org.junit.Assert.*;;

public class CompareToBuilderTest {

 @Test
 public void testComparisonWithObjects() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object();
 Object rhs = new Object();
 int comparison = compareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 @Test
 public void testComparisonWithNulls() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = null;
 Object rhs = new Object();
 int comparison = compareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, comparison);
 comparison = compareToBuilder.reflectionCompare(rhs, lhs);
 assertEquals(1, comparison);
 }

 @Test
 public void testComparisonWithDifferentTypes() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object();
 int rhs = 1;
 int comparison = compareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonWithArrays() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int[] lhs = {1, 2, 3};
 int[] rhs = {1, 2, 3};
 int comparison = compareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 lhs = new int[]{1, 2, 3};
 rhs = new int[]{1, 2, 4};
 comparison = compareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, comparison);
 comparison = compareToBuilder.reflectionCompare(rhs, lhs);
 assertEquals(1, comparison);
 }

 @Test
 public void testComparisonWithArraysAndCustomComparator() {
 Comparator<int[]> comparator = (lhs, rhs) -> Arrays.stream(lhs).mapToInt(i -> i).sum() < Arrays.stream(rhs).mapToInt(i -> i).sum() ? -1 : (Arrays.stream(lhs).mapToInt(i -> i).sum() > Arrays.stream(rhs).mapToInt(i -> i).sum() ? 1 : 0);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int[] lhs = {1, 2, 3};
 int[] rhs = {1, 2, 3};
 int comparison = compareToBuilder.reflectionCompare(lhs, rhs, comparator);
 assertEquals(0, comparison);
 lhs = new int[]{1, 2, 3};
 rhs = new int[]{1, 2, 4};
 comparison = compareToBuilder.reflectionCompare(lhs, rhs, comparator);
 assertEquals(-1, comparison);
 comparison = compareToBuilder.reflectionCompare(rhs, lhs, comparator);
 assertEquals(1, comparison);
 }

 @Test
 public void testComparisonWithArraysAndExcludedFields() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String[] excludeFields = {"field1", "field2"};
 int[] lhs = {1, 2, 3, 4, 5};
 int[] rhs = {1, 2, 3, 4, 5};
 int comparison = compareToBuilder.reflectionCompare(lhs, rhs, CompareToBuilder.class, null, excludeFields);
 assertEquals(0, comparison);
 int[] lhsWithExcludedField = {1, 2, 3, null, 5};
 int[] rhsWithExcludedField = {1, 2, 3, null, 5};
 comparison = compareToBuilder.reflectionCompare(lhsWithExcludedField, rhsWithExcludedField, CompareToBuilder.class, null, excludeFields);
 assertEquals(0, comparison);
 int[] lhsWithExcludedFieldsDifferent = {1, 2, 3, null, 5};
 int[] rhsWithExcludedFieldsDifferent = {1, 2, 3, 6, 5};
 comparison = compareToBuilder.reflectionCompare(lhsWithExcludedFieldsDifferent, rhsWithExcludedFieldsDifferent, CompareToBuilder.class, null, excludeFields);
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonWithSuperClass() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 CompareToBuilder superClassBuilder = new CompareToBuilder();
 Object lhs = new SubClass();
 Object rhs = new SubClass();
 int comparison = compareToBuilder.reflectionCompare(lhs, rhs, Object.class);
 assertEquals(0, comparison);
 comparison = compareToBuilder.reflectionCompare(lhs, rhs, Object.class, superClassBuilder.getClass(), new String[0]);
 assertEquals(-1, comparison);
 }

 public static class SubClass extends SuperClass {
 // Add fields and getters/setters here
 }

 @Test
 public void testAppend() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object();
 Object rhs = new Object();
 compareToBuilder.append(lhs, rhs);
 assertEquals(0, compareToBuilder.toComparison());
 lhs = new Object();
 rhs = new Object();
 compareToBuilder.append(lhs, rhs, (Comparator<?>) Comparator.comparing(o -> o.toString().length()));
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendWithArrays() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int[] lhs = {1, 2, 3};
 int[] rhs = {1, 2, 3};
 compareToBuilder.append(lhs, rhs);
 assertEquals(0, compareToBuilder.toComparison());
 int[] lhsDifferent = {1, 2, 3};
 int[] rhsDifferent = {1, 2, 4};
 compareToBuilder.append(lhsDifferent, rhsDifferent);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendWithArraysAndCustomComparator() {
 Comparator<int[]> comparator = (lhs, rhs) -> Arrays.stream(lhs).mapToInt(i -> i).sum() < Arrays.stream(rhs).mapToInt(i -> i).sum() ? -1 : (Arrays.stream(lhs).mapToInt(i -> i).sum() > Arrays.stream(rhs).mapToInt(i -> i).sum() ? 1 : 0);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int[] lhs = {1, 2, 3};
 int[] rhs = {1, 2, 3};
 compareToBuilder.append(lhs, rhs, comparator);
 assertEquals(0, compareToBuilder.toComparison());
 int[] lhsDifferent = {1, 2, 3};
 int[] rhsDifferent = {1, 2, 4};
 compareToBuilder.append(lhsDifferent, rhsDifferent, comparator);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendWithNulls() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object();
 Object rhs = null;
 compareToBuilder.append(lhs, rhs);
 assertEquals(-1, compareToBuilder.toComparison());
 Object lhsNull = null;
 Object rhsNull = new Object();
 compareToBuilder.append(lhsNull, rhsNull);
 assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendWithDifferentTypes() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = "hello";
 Object rhs = 1;
 compareToBuilder.append(lhs, rhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendWithBooleans() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 boolean lhs = true;
 boolean rhs = false;
 compareToBuilder.append(lhs, rhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendWithArraysAndExcludedFields() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String[] excludeFields = {"field1", "field2"};
 int[] lhs = {1, 2, 3, 4, 5};
 int[] rhs = {1, 2, 3, 4, 5};
 compareToBuilder.append(lhs, rhs);
 assertEquals(0, compareToBuilder.toComparison());
 int[] lhsWithExcludedField = {1, 2, 3, null, 5};
 int[] rhsWithExcludedField = {1, 2, 3, null, 5};
 compareToBuilder.append(lhsWithExcludedField, rhsWithExcludedField);
 assertEquals(0, compareToBuilder.toComparison());
 int[] lhsWithExcludedFieldsDifferent = {1, 2, 3, null, 5};
 int[] rhsWithExcludedFieldsDifferent = {1, 2, 3, 6, 5};
 compareToBuilder.append(lhsWithExcludedFieldsDifferent, rhsWithExcludedFieldsDifferent);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testAppendWithArraysAndExcludedFieldsAndSuperClass() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String[] excludeFields = {"field1", "field2"};
 SubClass lhs = new SubClass();
 SubClass rhs = new SubClass();
 compareToBuilder.append(lhs, rhs);
 assertEquals(0, compareToBuilder.toComparison());
 SubClass lhsWithExcludedField = new SubClass();
 lhsWithExcludedField.setField1(null);
 SubClass rhsWithExcludedField = new SubClass();
 rhsWithExcludedField.setField1(null);
 compareToBuilder.append(lhsWithExcludedField, rhsWithExcludedField);
 assertEquals(0, compareToBuilder.toComparison());
 SubClass lhsWithExcludedFieldsDifferent = new SubClass();
 lhsWithExcludedFieldsDifferent.setField1(null);
 SubClass rhsWithExcludedFieldsDifferent = new SubClass();
 rhsWithExcludedFieldsDifferent.setField2("different");
 compareToBuilder.append(lhsWithExcludedFieldsDifferent, rhsWithExcludedFieldsDifferent);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 public static class SuperClass {
 // Add fields and getters/setters here
 }

 public static class SubClass extends SuperClass {
 private int field1;
 private String field2;

 public int getField1() {
 return field1;
 }

 public void setField1(int field1) {
 this.field1 = field1;
 }

 public String getField2() {
 return field2;
 }

 public void setField2(String field2) {
 this.field2 = field2;
 }
}
}