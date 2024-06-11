package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;
 private Integer comparisonResult;

 @Test
 public void testAppendSuper() {
 ComparisonResult expectedComparisonResult = new ComparisonResult(10);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 ComparisonResult actualComparisonResult = compareToBuilder.appendSuper(expectedComparisonResult.getComparisonValue());
 assertEquals(expectedComparisonResult.getComparisonValue(), actualComparisonResult.getComparisonValue());
 }

 @Test
 public void testReflectionCompare() {
 Object lhs = new Object() {
 @Override
 public int compareTo(Object o) {
 return 5;
 }
 };
 Object rhs = new Object() {
 @Override
 public int compareTo(Object o) {
 return 10;
 }
 };
 int comparisonResult = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-5, comparisonResult);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompareWithNullLhs() {
 Object lhs = null;
 Object rhs = new Object();
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompareWithNullRhs() {
 Object lhs = new Object();
 Object rhs = null;
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompareWithDifferentTypes() {
 Object lhs = new Object() {};
 Object rhs = new Double(5.0);
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testReflectionCompareWithSameObjects() {
 Object lhs = new Object();
 Object rhs = lhs;
 int comparisonResult = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparisonResult);
 }

 @Test
 public void testAppendPrimitiveTypes() {
 int lhs = 5;
 int rhs = 10;
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparisonResult = compareToBuilder.append(lhs, rhs);
 assertEquals(-5, comparisonResult);
 }

 @Test
 public void testAppendObjectTypes() {
 Object lhs = new Object() {
 @Override
 public int compareTo(Object o) {
 return 5;
 }
 };
 Object rhs = new Object() {
 @Override
 public int compareTo(Object o) {
 return 10;
 }
 };
 ComparisonResult expectedComparisonResult = new ComparisonResult(5);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparisonResult = compareToBuilder.append(lhs, rhs);
 assertEquals(expectedComparisonResult.getComparisonValue(), comparisonResult);
 }

 @Test
 public void testAppendNullLhs() {
 Object lhs = null;
 Object rhs = new Object();
 ComparisonResult expectedComparisonResult = new ComparisonResult(-1);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparisonResult = compareToBuilder.append(lhs, rhs);
 assertEquals(expectedComparisonResult.getComparisonValue(), comparisonResult);
 }

 @Test
 public void testAppendNullRhs() {
 Object lhs = new Object();
 Object rhs = null;
 ComparisonResult expectedComparisonResult = new ComparisonResult(1);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparisonResult = compareToBuilder.append(lhs, rhs);
 assertEquals(expectedComparisonResult.getComparisonValue(), comparisonResult);
 }

 @Test(expected = ClassCastException.class)
 public void testAppendDifferentTypes() {
 Object lhs = new int[0];
 Object rhs = new String[0];
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.append(lhs, rhs);
 }

 @Test
 public void testAppendArraysWithSameLengthAndEqualElements() {
 int[] lhs = { 5, 5 };
 int[] rhs = { 5, 5 };
 ComparisonResult expectedComparisonResult = new ComparisonResult(0);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparisonResult = compareToBuilder.append(lhs, rhs);
 assertEquals(expectedComparisonResult.getComparisonValue(), comparisonResult);
 }

 @Test
 public void testAppendArraysWithSameLengthAndDifferentElements() {
 int[] lhs = { 5, 5 };
 int[] rhs = { 5, 6 };
 ComparisonResult expectedComparisonResult = new ComparisonResult(-1);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparisonResult = compareToBuilder.append(lhs, rhs);
 assertEquals(expectedComparisonResult.getComparisonValue(), comparisonResult);
 }

 @Test
 public void testAppendArraysWithDifferentLengthsAndEqualElements() {
 int[] lhs = { 5, 5, 5 };
 int[] rhs = { 5, 5 };
 ComparisonResult expectedComparisonResult = new ComparisonResult(-1);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparisonResult = compareToBuilder.append(lhs, rhs);
 assertEquals(expectedComparisonResult.getComparisonValue(), comparisonResult);
 }

 @Test
 public void testAppendArraysWithDifferentLengthsAndDifferentElements() {
 int[] lhs = { 5, 5, 5 };
 int[] rhs = { 5, 5, 6 };
 ComparisonResult expectedComparisonResult = new ComparisonResult(-1);
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparisonResult = compareToBuilder.append(lhs, rhs);
 assertEquals(expectedComparisonResult.getComparisonValue(), comparisonResult);
 }

 private static class ComparisonResult {
 private final int comparisonValue;

 public ComparisonResult(int comparisonValue) {
 this.comparisonValue = comparisonValue;
 }

 public int getComparisonValue() {
 return comparisonValue;
 }
 }
}