package org.apache.commons.lang3;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Array;
import java.util.Arrays;

public class CompareToBuilderTest {

 @Test
 public void testComparisonOfObjects() {
 CompareToBuilder builder = new CompareToBuilder();
 Object obj1 = new Object();
 Object obj2 = new Object();
 builder.append(obj1, obj2);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testComparisonOfNullObjects() {
 CompareToBuilder builder = new CompareToBuilder();
 Object obj1 = null;
 Object obj2 = new Object();
 builder.append(obj1, obj2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonOfSameObjects() {
 CompareToBuilder builder = new CompareToBuilder();
 Object obj1 = new Object();
 Object obj2 = obj1;
 builder.append(obj1, obj2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testComparisonOfArrays() {
 CompareToBuilder builder = new CompareToBuilder();
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2, 3};
 builder.append(arr1, arr2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testComparisonOfDifferentArrayLengths() {
 CompareToBuilder builder = new CompareToBuilder();
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {1, 2, 3, 4};
 builder.append(arr1, arr2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonOfArraysWithSameElementsButDifferentOrder() {
 CompareToBuilder builder = new CompareToBuilder();
 int[] arr1 = {1, 2, 3};
 int[] arr2 = {2, 3, 1};
 builder.append(arr1, arr2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonOfArraysWithNullElements() {
 CompareToBuilder builder = new CompareToBuilder();
 int[] arr1 = {1, null, 2};
 int[] arr2 = {1, 2, null};
 builder.append(arr1, arr2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonOfArraysWithNullElementsInDifferentPositions() {
 CompareToBuilder builder = new CompareToBuilder();
 int[] arr1 = {1, null, 2};
 int[] arr2 = {1, 2, null};
 builder.append(arr1, arr2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testComparisonOfArraysWithCustomComparator() {
 CompareToBuilder builder = new CompareToBuilder();
 int[] arr1 = {3, 2, 1};
 int[] arr2 = {1, 2, 3};
 Comparator<int[]> comparator = (o1, o2) -> Integer.compare(Arrays.stream(o1).sum(), Arrays.stream(o2).sum());
 builder.append(arr1, arr2, comparator);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testComparisonOfObjectsWithCustomComparator() {
 CompareToBuilder builder = new CompareToBuilder();
 Object obj1 = new Object1();
 Object obj2 = new Object2();
 Comparator<Object> comparator = (o1, o2) -> o1.hashCode() - o2.hashCode();
 builder.append(obj1, obj2, comparator);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testComparisonOfNullObjectsWithNullComparator() {
 CompareToBuilder builder = new CompareToBuilder();
 Object obj1 = null;
 Object obj2 = new Object();
 Comparator comparator = null;
 builder.append(obj1, obj2, comparator);
 }

 @Test(expected = ClassCastException.class)
 public void testComparisonOfObjectsWithDifferentTypes() {
 CompareToBuilder builder = new CompareToBuilder();
 Object obj1 = new Object1();
 Object obj2 = new Object2();
 builder.append(obj1, obj2);
 }
}

class Object1 {
}

class Object2 {
}