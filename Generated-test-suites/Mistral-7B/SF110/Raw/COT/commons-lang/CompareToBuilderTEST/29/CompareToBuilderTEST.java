package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;
 private int comparison;

 @Test
 public void testComparisonOfObjects() {
     lhs = new Object();
     rhs = new Object();
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.append(lhs, rhs);
     assertEquals(comparison, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfSameObjects() {
     lhs = new Object();
     rhs = lhs;
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(comparison, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfNullLhs() {
     lhs = null;
     rhs = new Object();
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(-1);
     assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfNullRhs() {
     lhs = new Object();
     rhs = null;
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(1);
     assertEquals(1, compareToBuilder.toComparison());
 }

 @Test(expected = NullPointerException.class)
 public void testComparisonOfNullLhsAndRhs() {
     lhs = null;
     rhs = null;
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     compareToBuilder.appendSuper(0);
     CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testComparisonOfDifferentClasses() {
     lhs = new Integer(1);
     rhs = new String("1");
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(comparison, compareToBuilder.toComparison());
 }

 @Test
 public void testReflectionCompareWithExcludeFields() {
     lhs = new Person("John", 25);
     rhs = new Person("John", 25);
     Comparator<Object> comparator = Comparators.comparing("age");
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs, comparator, Person.class, "name");
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     compareToBuilder.appendSuper(comparison);
     assertEquals(0, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfArrays() {
     int[] lhs = {1, 2, 3};
     int[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.append((Object)lhs, (Object)rhs);
     assertEquals(0, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfArraysWithDifferentLengths() {
     int[] lhs = {1, 2};
     int[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfArraysWithNull() {
     int[] lhs = null;
     int[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(1);
     assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfArraysWithDifferentTypes() {
     int[] lhs = {1, 2};
     long[] rhs = {1, 2};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfLongArrays() {
     long[] lhs = {1, 2, 3};
     long[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.append((Object)lhs, (Object)rhs);
     assertEquals(0, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfLongArraysWithDifferentLengths() {
     long[] lhs = {1, 2};
     long[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfLongArraysWithNull() {
     long[] lhs = null;
     long[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(1);
     assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfDoubleArrays() {
     double[] lhs = {1.1, 2.2, 3.3};
     double[] rhs = {1.1, 2.2, 3.3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.append((Object)lhs, (Object)rhs);
     assertEquals(0, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfDoubleArraysWithDifferentLengths() {
     double[] lhs = {1.1, 2.2};
     double[] rhs = {1.1, 2.2, 3.3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfDoubleArraysWithNull() {
     double[] lhs = null;
     double[] rhs = {1.1, 2.2, 3.3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(1);
     assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfByteArrays() {
     byte[] lhs = {1, 2, 3};
     byte[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.append((Object)lhs, (Object)rhs);
     assertEquals(0, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfByteArraysWithDifferentLengths() {
     byte[] lhs = {1, 2};
     byte[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfByteArraysWithNull() {
     byte[] lhs = null;
     byte[] rhs = {1, 2, 3};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(1);
     assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfFloatArrays() {
     float[] lhs = {1.1f, 2.2f, 3.3f};
     float[] rhs = {1.1f, 2.2f, 3.3f};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.append((Object)lhs, (Object)rhs);
     assertEquals(0, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfFloatArraysWithDifferentLengths() {
     float[] lhs = {1.1f, 2.2f};
     float[] rhs = {1.1f, 2.2f, 3.3f};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfFloatArraysWithNull() {
     float[] lhs = null;
     float[] rhs = {1.1f, 2.2f, 3.3f};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(1);
     assertEquals(1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfBooleanArrays() {
     boolean[] lhs = {true, false};
     boolean[] rhs = {true, false};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.append((Object)lhs, (Object)rhs);
     assertEquals(0, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfBooleanArraysWithDifferentLengths() {
     boolean[] lhs = {true, false};
     boolean[] rhs = {true, false, true};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(comparison);
     assertEquals(-1, compareToBuilder.toComparison());
 }

 @Test
 public void testComparisonOfBooleanArraysWithNull() {
     boolean[] lhs = null;
     boolean[] rhs = {true, false};
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     compareToBuilder.appendSuper(1);
     assertEquals(1, compareToBuilder.toComparison());
 }

}