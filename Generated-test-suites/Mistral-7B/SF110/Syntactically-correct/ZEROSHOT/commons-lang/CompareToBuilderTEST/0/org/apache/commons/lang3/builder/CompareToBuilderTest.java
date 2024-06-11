package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

 @Test
 public void testReflectionCompare() {
 Integer lhs = 1;
 Integer rhs = 2;
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, comparison);

 lhs = 2;
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(1, comparison);

 lhs = null;
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertNull(lhs);
 throw new AssertionError("Expected comparison to be 1, but was 0");

 rhs = null;
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertNull(rhs);
 throw new AssertionError("Expected comparison to be -1, but was 0");
 }

 @Test
 public void testReflectionCompareWithExcludeFields() {
 Integer lhs = new Integer(1);
 Integer rhs = new Integer(2);
 String excludeField = "field1";
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{excludeField});
 assertEquals(-1, comparison);

 lhs = new Integer(2);
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{excludeField});
 assertEquals(1, comparison);
 }

 @Test
 public void testReflectionCompareWithTransients() {
 TransientInteger lhs = new TransientInteger(1);
 TransientInteger rhs = new TransientInteger(2);
 boolean compareTransients = true;
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, compareTransients);
 assertEquals(-1, comparison);

 lhs = new TransientInteger(2);
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs, compareTransients);
 assertEquals(1, comparison);
 }

 @Test
 public void testReflectionCompareWithSuperClass() {
 ParentClass parentLhs = new ParentClass(1);
 ParentClass parentRhs = new ParentClass(2);
 int superCompareTo = 0;
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 comparison = compareToBuilder.appendSuper(superCompareTo)
 .appendSuperClass(parentLhs.getClass())
 .append(parentLhs, parentRhs)
 .toComparison();
 assertEquals(superCompareTo, comparison);
 }

 @Test
 public void testAppend() {
 Integer lhs = 1;
 Integer rhs = 2;

 // int comparison
 comparison = compareToBuilder.append(lhs, rhs).toComparison();
 assertEquals(-1, comparison);

 // long comparison
 long lhsLong = 1L;
 long rhsLong = 2L;
 comparison = compareToBuilder.append((long)lhs, (long)rhs).toComparison();
 assertEquals(-1, comparison);

 // boolean comparison
 boolean lhsBoolean = false;
 boolean rhsBoolean = true;
 comparison = compareToBuilder.append(lhsBoolean, rhsBoolean).toComparison();
 assertEquals(-1, comparison);

 // array comparison
 Object[] lhsArray = {1, 2};
 Object[] rhsArray = {2, 1};
 comparison = compareToBuilder.append(lhsArray, rhsArray).toComparison();
 assertEquals(-1, comparison);

 // custom comparator comparison
 Comparator<Object> comparator = new Comparator<Object>() {
 @Override
 public int compare(Object o1, Object o2) {
 return ((Integer)o1).intValue() - ((Integer)o2).intValue();
 }
 };
 Object[] lhsArrayWithComparator = {new Integer(1), new Integer(2)};
 Object[] rhsArrayWithComparator = {new Integer(2), new Integer(1)};
 comparison = compareToBuilder.append(lhsArrayWithComparator, rhsArrayWithComparator, comparator).toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppendSuper() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 compareToBuilder.appendSuper(5);
 int comparison = compareToBuilder.toComparison();
 assertEquals(5, comparison);
 }
}

class TransientInteger {
 private int value;

 @SuppressWarnings("unused")
 private transient int transientValue;

 public TransientInteger(int value) {
 this.value = value;
 }

 public int getValue() {
 return value;
 }
}

class ParentClass {
 private int value;

 public ParentClass(int value) {
 this.value = value;
 }

 public int getValue() {
 return value;
 }

 public Class<?> getSuperclass() {
 return SuperClass.class;
 }
}

class SuperClass {
 // empty class
}