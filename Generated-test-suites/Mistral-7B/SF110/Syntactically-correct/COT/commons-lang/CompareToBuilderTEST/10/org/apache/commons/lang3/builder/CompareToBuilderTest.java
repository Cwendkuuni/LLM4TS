package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;
 private CompareToBuilder compareToBuilder;

 @Test
 public void testReflectionCompare_Objects() {
     lhs = new Object();
     rhs = new Object();
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     assertEquals(0, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompare_Null() {
     lhs = null;
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompare_IncompatibleTypes() {
     lhs = new String("abc");
     rhs = new Integer(123);
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompare_NullLhs() {
     lhs = null;
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testReflectionCompare_ExcludeFields() {
     lhs = new Foo();
     rhs = new Foo();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"field1"});
     assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_CompareTransients() {
     lhs = new FooWithTransientField();
     rhs = new FooWithTransientField();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
     assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_SuperClass() {
     lhs = new ChildClass();
     rhs = new ParentClass();
     comparison = CompareToBuilder.reflectionCompare(lhs, rhs, ParentClass.class);
     assertEquals(0, comparison);
 }

 // Test append methods for primitive types

 @Test
 public void testAppend_IntAndInt() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(1, 1);
     assertEquals(0, comparison);
 }

 @Test
 public void testAppend_IntAndNegativeInt() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(1, -1);
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_IntAndPositiveInt() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(1, 2);
     assertEquals(1, comparison);
 }

 // Test append methods for object types

 @Test
 public void testAppend_Objects_NullLhs() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhs, rhs);
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_Objects_NullRhs() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhs, rhs);
     assertEquals(1, comparison);
 }

 @Test
 public void testAppend_Objects_DifferentLength() {
     String[] lhsArray = {"a", "b", "c"};
     String[] rhsArray = {"a", "b"};
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhsArray, rhsArray);
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_Objects_SameLengthButDifferentValues() {
     String[] lhsArray = {"a", "b", "c"};
     String[] rhsArray = {"d", "e", "f"};
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhsArray, rhsArray);
     assertEquals(-1, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testAppend_Objects_NullArray() {
     String[] lhsArray = null;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhsArray, rhsArray);
 }

 @Test(expected = NullPointerException.class)
 public void testAppend_Objects_NullRhsArray() {
     String[] lhsArray = {"a", "b", "c"};
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhsArray, null);
 }

 @Test
 public void testAppend_Objects_Comparator() {
     String[] lhsArray = {"a", "b", "c"};
     String[] rhsArray = {"c", "b", "a"};
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append(lhsArray, rhsArray, new StringComparator());
     assertEquals(1, comparison);
 }

 // Test append methods for primitive arrays

 @Test
 public void testAppend_LongsAndLongs() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append((long)1, (long)1);
     assertEquals(0, comparison);
 }

 @Test
 public void testAppend_LongsAndNegativeLong() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append((long)1, -1L);
     assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_LongsAndPositiveLong() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     compareToBuilder.append((long)1, 2L);
     assertEquals(1, comparison);
 }

 // ... Test other append methods for int, short, char, byte, double, float, and boolean types ...

 @Test
 public void testToComparison() {
     comparison = 0;
     compareToBuilder = new CompareToBuilder();
     // Set comparison value through various append methods
     compareToBuilder.append(1, 2);
     compareToBuilder.append("abc", "def");
     compareToBuilder.append(new Foo(), new Foo());
     assertEquals(1, comparison);
 }
}

 class Foo {
 // Some fields...
 }

 class FooWithTransientField {
 // Some fields including a transient field...
 }

 class StringComparator implements Comparator<String[]> {
 @Override
 public int compare(String[] lhs, String[] rhs) {
 // Implement comparison logic...
 }
}