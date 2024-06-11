package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;

 @Test
 public void testComparisonZero() {
     CompareToBuilder builder = new CompareToBuilder();
     lhs = new Object();
     rhs = new Object();
     assertEquals(0, builder.compare(lhs, rhs));
 }

 @Test
 public void testComparisonLhsLessThanRhs() {
     CompareToBuilder builder = new CompareToBuilder();
     lhs = new Object();
     rhs = new Object();
     assertTrue(builder.append(lhs, rhs).comparison < 0);
 }

 @Test
 public void testComparisonLhsGreaterThanRhs() {
     CompareToBuilder builder = new CompareToBuilder();
     lhs = new Object();
     rhs = new Object();
     assertTrue(builder.append(rhs, lhs).comparison > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testComparisonNullLhs() {
     CompareToBuilder builder = new CompareToBuilder();
     lhs = null;
     rhs = new Object();
     builder.append(lhs, rhs);
 }

 @Test(expected = NullPointerException.class)
 public void testComparisonNullRhs() {
     CompareToBuilder builder = new CompareToBuilder();
     lhs = new Object();
     rhs = null;
     builder.append(lhs, rhs);
 }

 @Test
 public void testComparisonDifferentTypes() {
     CompareToBuilder builder = new CompareToBuilder();
     lhs = new Object();
     rhs = new Integer(1);
     assertNull(builder.append(lhs, rhs).comparison);
 }

 @Test
 public void testReflectionCompareSameObjects() {
     CompareToBuilder builder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = lhs;
     assertEquals(0, builder.reflectionCompare(lhs, rhs));
 }

 @Test
 public void testReflectionCompareDifferentObjectsButSameClass() {
     CompareToBuilder builder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = new Object();
     lhs.getClass().setName("MyClass");
     rhs.getClass().setName("MyClass");
     assertNotEquals(0, builder.reflectionCompare(lhs, rhs));
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompareDifferentClasses() {
     CompareToBuilder builder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = new String();
     builder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testReflectionCompareWithExcludedFields() {
     CompareToBuilder builder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = new Object();
     String excludeFields[] = {"field1", "field2"};
     assertEquals(0, builder.reflectionCompare(lhs, rhs, false, null, excludeFields));
 }

 @Test
 public void testReflectionCompareWithTransients() {
     CompareToBuilder builder = new CompareToBuilder();
     Object lhs = new Object() {
         private transient int a;
         private int b;
     };
     Object rhs = new Object() {
         private int a;
         private int b;
     };
     assertEquals(0, builder.reflectionCompare(lhs, rhs, true));
 }

 @Test
 public void testReflectionCompareWithSuperClass() {
     CompareToBuilder builder = new CompareToBuilder();
     Object lhs = new ChildClass();
     Object rhs = new ChildClass();
     assertEquals(0, builder.reflectionCompare(lhs, rhs, ChildClass.class));
 }

 @Test
 public void testReflectionCompareWithSuperClassDifferentOrder() {
     CompareToBuilder builder = new CompareToBuilder();
     Object lhs = new ChildClassA();
     Object rhs = new ChildClassB();
     assertEquals(-1, builder.reflectionCompare(lhs, rhs, ChildClassA.class));
 }

 @Test
 public void testReflectionCompareWithSuperClassDifferentClasses() {
     CompareToBuilder builder = new CompareToBuilder();
     Object lhs = new ChildClassA();
     Object rhs = new ChildClassB();
     assertEquals(-1, builder.reflectionCompare(lhs, rhs, ChildClassA.class));
 }

 private static class ChildClassA extends SuperClass {
 }

 private static class ChildClassB extends SuperClass {
 }

 private static class SuperClass {
 }

}