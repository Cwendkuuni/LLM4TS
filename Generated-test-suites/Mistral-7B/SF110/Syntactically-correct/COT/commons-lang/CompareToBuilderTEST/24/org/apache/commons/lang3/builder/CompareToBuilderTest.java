package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;
 private CompareToBuilder compareToBuilder;

 @Test
 public void testReflectionCompare() {
     lhs = new Object();
     rhs = new Object();
     int result = CompareToBuilder.reflectionCompare(lhs, rhs);
     assertEquals(0, result);

     lhs = null;
     rhs = new Object();
     try {
         result = CompareToBuilder.reflectionCompare(lhs, rhs);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // Expected
     }

     lhs = new Object();
     rhs = null;
     try {
         result = CompareToBuilder.reflectionCompare(lhs, rhs);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // Expected
     }

     lhs = new String("test");
     rhs = new String("test");
     result = CompareToBuilder.reflectionCompare(lhs, rhs);
     assertEquals(0, result);

     lhs = new String("test1");
     rhs = new String("test2");
     result = CompareToBuilder.reflectionCompare(lhs, rhs);
     assertEquals(-1, result);

     lhs = new String("test2");
     rhs = new String("test1");
     result = CompareToBuilder.reflectionCompare(lhs, rhs);
     assertEquals(1, result);
 }

 @Test
 public void testReflectionCompareWithExcludeFields() {
     lhs = new Person("John", 25, "Male");
     rhs = new Person("John", 25, "Female");
     int result = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"gender"});
     assertEquals(0, result);
 }

 @Test
 public void testReflectionCompareWithArray() {
     Object[] lhs = {1, 2, 3};
     Object[] rhs = {1, 2, 3};
     int result = CompareToBuilder.reflectionCompare(lhs, rhs);
     assertEquals(0, result);

     lhs = new int[]{1, 2, 3};
     rhs = new int[]{1, 2, 4};
     result = CompareToBuilder.reflectionCompare(lhs, rhs);
     assertEquals(-1, result);

     lhs = new int[]{1, 2, 3};
     rhs = new int[]{1, 2, 3};
     result = CompareToBuilder.reflectionCompare(lhs, rhs, new IntegerComparator());
     assertEquals(0, result);

     lhs = new int[]{1, 2, 3};
     rhs = new int[]{1, 2, 3};
     result = CompareToBuilder.reflectionCompare(lhs, rhs, Comparator.comparingInt(a -> (int)a[0]));
     assertEquals(0, result);
 }

 @Test
 public void testAppendSuper() {
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.appendSuper(5);
     int result = compareToBuilder.toComparison();
     assertEquals(5, result);
 }

 @Test
 public void testAppend() {
     compareToBuilder = new CompareToBuilder();

     compareToBuilder.append(1, 2);
     int result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(2, 1);
     result = compareToBuilder.toComparison();
     assertEquals(1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(1.0, 1.1);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append("test1", "test2");
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(true, false);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);
 }

 @Test
 public void testAppendArray() {
     compareToBuilder = new CompareToBuilder();

     Object[] lhs = {1, 2, 3};
     Object[] rhs = {1, 2, 3};
     compareToBuilder.append(lhs, rhs);
     int result = compareToBuilder.toComparison();
     assertEquals(0, result);

     lhs = new int[]{1, 2, 3};
     rhs = new int[]{1, 2, 4};
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhs, rhs);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     lhs = new int[]{1, 2, 3};
     rhs = new int[]{1, 2, 3};
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhs, rhs, Comparator.comparingInt(a -> (int)a[0]));
     result = compareToBuilder.toComparison();
     assertEquals(0, result);
 }

 @Test
 public void testAppendPrimitiveTypes() {
     compareToBuilder = new CompareToBuilder();

     compareToBuilder.append(1l, 2l);
     int result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(1, 2);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append((short)1, (short)2);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append((char)1, (char)2);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append((byte)1, (byte)2);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(1.0d, 2.0d);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(1.0f, 2.0f);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);

     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(true, false);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);
 }

 @Test
 public void testAppendBooleanArray() {
     compareToBuilder = new CompareToBuilder();

     boolean[] lhs = {true, false, true};
     boolean[] rhs = {true, false, true};
     compareToBuilder.append(lhs, rhs);
     int result = compareToBuilder.toComparison();
     assertEquals(0, result);

     lhs = new boolean[]{true, false, true};
     rhs = new boolean[]{false, true, false};
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhs, rhs);
     result = compareToBuilder.toComparison();
     assertEquals(-1, result);
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException() {
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(null, null);
 }

 @Test(expected = ClassCastException.class)
 public void testClassCastException() {
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append("test", 1);
 }
}

 class Person {
 private String name;
 private int age;
 private String gender;

 public Person(String name, int age, String gender) {
 this.name = name;
 this.age = age;
 this.gender = gender;
 }

 @Override
 public String toString() {
 return name + "," + age + "," + gender;
 }
 }

 class IntegerComparator implements Comparator<Object> {
 @Override
 public int compare(Object o1, Object o2) {
 if (o1 instanceof int[]) {
 int[] arr1 = (int[])o1;
 int[] arr2 = (int[])o2;
 return Arrays.compare(arr1, arr2);
 }
 if (o2 instanceof int[]) {
 int[] arr1 = (int[])o1;
 int[] arr2 = (int[])o2;
 return Arrays.compare(arr1, arr2);
 }
 throw new UnsupportedOperationException();
 }
}