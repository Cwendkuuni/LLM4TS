package org.apache.commons.lang3.builder;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 // Test case for default constructor
 @Test
 public void testDefaultConstructor() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object();
 Object rhs = new Object();
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 // Test case for reflectionCompare method with two objects of the same class
 @Test
 public void testReflectionCompareSameClass() {
 Object lhs = new MyClass();
 Object rhs = new MyClass();
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 // Test case for reflectionCompare method with two objects of different classes but same fields values
 @Test
 public void testReflectionCompareSameValues() {
 Object lhs = new MyClass("field1", "field2");
 Object rhs = new MyClass("field1", "field2");
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 // Test case for reflectionCompare method with two objects of different classes and different fields values
 @Test
 public void testReflectionCompareDifferentValues() {
 Object lhs = new MyClass("field1", "field2");
 Object rhs = new MyClass("field3", "field4");
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, comparison);
 }

 // Test case for reflectionCompare method with null object
 @Test(expected = NullPointerException.class)
 public void testReflectionCompareNullObject() {
 Object lhs = null;
 Object rhs = new MyClass();
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 // Test case for reflectionCompare method with different object classes and one of them is not instance of the other
 @Test(expected = ClassCastException.class)
 public void testReflectionCompareDifferentClasses() {
 Object lhs = new MyClass();
 Object rhs = new MyOtherClass();
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 // Test case for append method with int primitives
 @Test
 public void testAppendIntPrimitives() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int lhs = 5;
 int rhs = 10;
 compareToBuilder.append(lhs, rhs);
 assertEquals(-5, compareToBuilder.toComparison());
 }

 // Test case for append method with long primitives
 @Test
 public void testAppendLongPrimitives() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 long lhs = 5L;
 long rhs = 10L;
 compareToBuilder.append(lhs, rhs);
 assertEquals(-5L, compareToBuilder.toComparison());
 }

 // Test case for append method with String
 @Test
 public void testAppendString() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String lhs = "hello";
 String rhs = "world";
 compareToBuilder.append(lhs, rhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 // Test case for append method with arrays of int primitives
 @Test
 public void testAppendIntArray() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int[] lhs = {1, 2, 3};
 int[] rhs = {1, 2, 4};
 compareToBuilder.append(lhs, rhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 // Test case for append method with arrays of long primitives
 @Test
 public void testAppendLongArray() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 long[] lhs = {1L, 2L, 3L};
 long[] rhs = {1L, 2L, 5L};
 compareToBuilder.append(lhs, rhs);
 assertEquals(-1, compareToBuilder.toComparison());
 }

 // Test case for append method with arrays of String
 @Test
 public void testAppendStringArray() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String[] lhs = {"hello", "world"};
 String[] rhs = {"world", "hello"};
 compareToBuilder.append(lhs, rhs);
 assertEquals(1, compareToBuilder.toComparison());
 }

 // Test case for append method with custom Comparator
 @Test
 public void testAppendCustomComparator() {
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 String[] lhs = {"apple", "banana"};
 String[] rhs = {"banana", "apple"};
 compareToBuilder.append(lhs, rhs, new CustomComparator());
 assertEquals(-1, compareToBuilder.toComparison());
 }

 // Test case for appendSuper method
 @Test
 public void testAppendSuper() {
 CompareToBuilder compareToBuilder1 = new CompareToBuilder();
 CompareToBuilder compareToBuilder2 = new CompareToBuilder();
 compareToBuilder1.appendSuper(1);
 compareToBuilder2.appendSuper(2);
 compareToBuilder1.append(compareToBuilder2);
 assertEquals(1, compareToBuilder1.toComparison());
 }

}

class MyClass {
 String field1;
 String field2;

 public MyClass(String field1, String field2) {
 this.field1 = field1;
 this.field2 = field2;
 }
}

class CustomComparator implements Comparator<Object> {
 @Override
 public int compare(Object o1, Object o2) {
 return -1;
 }
}