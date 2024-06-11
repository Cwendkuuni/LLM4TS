package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;
 private Comparator<?> comparator;

 @Test
 public void testReflectionCompare() {
     lhs = new Object() {};
     rhs = new Object() {};
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     assertEquals(0, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompareLhsNull() {
     lhs = null;
     rhs = new Object() {};
     CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompareRhsNull() {
     lhs = new Object() {};
     rhs = null;
     CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompareDifferentClasses() {
     lhs = new Object() {};
     rhs = new String("test");
     CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testReflectionCompareWithFields() {
     lhs = new TestClass1();
     rhs = new TestClass1();
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
     assertEquals(0, comparison);

     lhs = new TestClass1();
     rhs = new TestClass2();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
     assertEquals(-1, comparison);
 }

 @Test
 public void testReflectionCompareWithExcludedFields() {
     lhs = new TestClass1();
     rhs = new TestClass1();
     String[] excludeFields = {"field1", "field2"};
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true, null, excludeFields);
     assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompareWithSuperclasses() {
     lhs = new TestClass1();
     rhs = new TestClass2();
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, TestClass1.class, null, new String[0]);
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppendSuper() {
     CompareToBuilder builder = new CompareToBuilder();
     int comparison = builder.appendSuper(-10).toComparison();
     assertEquals(-10, comparison);
 }

 @Test
 public void testAppendNullLhs() {
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(null, rhs);
     int comparison = builder.toComparison();
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppendNullRhs() {
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, null);
     int comparison = builder.toComparison();
     assertEquals(1, comparison);
 }

 @Test
 public void testAppendArray() {
     Object[] lhs = {1, 2, 3};
     Object[] rhs = {1, 2, 3};
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, rhs);
     int comparison = builder.toComparison();
     assertEquals(0, comparison);
 }

 @Test
 public void testAppendArrayDifferentLengths() {
     Object[] lhs = {1, 2, 3};
     Object[] rhs = {1, 2, 3, 4};
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, rhs);
     int comparison = builder.toComparison();
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppendArrayCustomComparator() {
     Object[] lhs = {1, 2, 3};
     Object[] rhs = {2, 1, 3};
     Comparator<Object[]> comparator = (l, r) -> ((Comparable<Object>)l[0]).compareTo(r[0]);
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, rhs, comparator);
     int comparison = builder.toComparison();
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppendPrimitiveTypes() {
     int lhs = 1;
     int rhs = 1;
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, rhs);
     int comparison = builder.toComparison();
     assertEquals(0, comparison);
 }

 @Test
 public void testAppendPrimitiveTypesDifferent() {
     int lhs = 1;
     int rhs = 2;
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, rhs);
     int comparison = builder.toComparison();
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppendCharacter() {
     char lhs = 'a';
     char rhs = 'b';
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, rhs);
     int comparison = builder.toComparison();
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppendBoolean() {
     boolean lhs = true;
     boolean rhs = false;
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, rhs);
     int comparison = builder.toComparison();
     assertEquals(1, comparison);
 }

 @Test
 public void testAppendBooleanSame() {
     boolean lhs = true;
     boolean rhs = true;
     CompareToBuilder builder = new CompareToBuilder();
     builder.append(lhs, rhs);
     int comparison = builder.toComparison();
     assertEquals(0, comparison);
 }

}

class TestClass1 {
 private int field1 = 1;
 private int field2 = 2;
}

class TestClass2 extends TestClass1 {
 private int field3 = 3;
}