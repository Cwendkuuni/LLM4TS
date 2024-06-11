package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import java.lang.reflect.*;
import static org.junit.Assert.*;;

public class CompareToBuilderTest {

 @Test
 public void testReflectionCompare_ObjectsWithSameValues() {
 Object lhs = new Object();
 Object rhs = new Object();
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_ObjectsWithDifferentClasses() {
 Object lhs = new Object();
 Object rhs = new String("test");
 try {
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 fail("Expected ClassCastException");
 } catch (ClassCastException e) {
 // Expected
 }
 }

 @Test
 public void testReflectionCompare_ObjectsWithNullValues() {
 Object lhs = null;
 Object rhs = new Object();
 try {
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 Object rhsNull = null;
 int comparison = CompareToBuilder.reflectionCompare(new Object(), rhsNull);
 assertEquals(-1, comparison);
 }

 @Test
 public void testReflectionCompare_ObjectsWithTransients() {
 class TestClass {
 private int value;
 private transient int transientValue;
 }

 TestClass lhs = new TestClass();
 TestClass rhs = new TestClass();
 lhs.value = 1;
 rhs.value = 1;
 lhs.transientValue = 2;
 rhs.transientValue = 3;

 boolean compareTransients = false;
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, compareTransients);
 assertEquals(0, comparison);

 compareTransients = true;
 comparison = CompareToBuilder.reflectionCompare(lhs, rhs, compareTransients);
 assertEquals(-1, comparison);
 }

 @Test
 public void testReflectionCompare_ObjectsWithExcludedFields() {
 class TestClass {
 private int value;
 private String excludedField;
 }

 TestClass lhs = new TestClass();
 TestClass rhs = new TestClass();
 lhs.value = 1;
 lhs.excludedField = "test";
 rhs.value = 1;
 rhs.excludedField = "differentTest";

 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, TestClass.class, new String[]{"excludedField"});
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_ObjectsWithNestedClasses() {
 class OuterClass {
 class InnerClass {
 private int value;
 }
 }

 OuterClass.InnerClass lhs = new OuterClass().new InnerClass();
 OuterClass.InnerClass rhs = new OuterClass().new InnerClass();
 lhs.value = 1;
 rhs.value = 1;

 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, OuterClass.class, null);
 assertEquals(0, comparison);
 }

 @Test(expected = InternalError.class)
 public void testReflectionCompare_UnexpectedException() {
 Object lhs = new Object();
 Object rhs = new Object();
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]);
 fail("Unexpected InternalError");
 }

}