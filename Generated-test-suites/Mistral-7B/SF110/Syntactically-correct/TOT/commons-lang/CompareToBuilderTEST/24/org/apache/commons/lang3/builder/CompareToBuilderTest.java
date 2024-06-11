package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

 @Test
 public void testReflectionCompare_ObjectsWithSameValues() {
 Object lhs = new Object();
 Object rhs = new Object();
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, result);
 }

 @Test
 public void testReflectionCompare_ObjectsWithDifferentClasses() {
 Object lhs = new String("hello");
 Object rhs = new Integer(5);
 int result = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(-1, result);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompare_NullObject() {
 Object lhs = null;
 Object rhs = new Object();
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompare_IncompatibleTypes() {
 Object lhs = new String("hello");
 Object rhs = new Integer(5);
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testReflectionCompare_ExcludeFields() {
 Object lhs = new TestClass();
 Object rhs = new TestClass();
 int result = CompareToBuilder.reflectionCompare(lhs, rhs, new String[]{"field1", "field2"});
 assertEquals(0, result);
 }

 @Test
 public void testReflectionCompare_Transients() {
 Object lhs = new TestClass();
 Object rhs = new TestClass();
 ((TestClass)lhs).transientField = "value";
 int result = CompareToBuilder.reflectionCompare(lhs, rhs, true);
 assertEquals(0, result);
 }

 @Test
 public void testAppend_PrimitiveTypes() {
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(1, 2);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_ObjectArrays() {
 Object[] lhs = {1, 2, 3};
 Object[] rhs = {1, 2, 3};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());
 }

 @Test(expected = NullPointerException.class)
 public void testAppend_NullArrays() {
 Object[] lhs = null;
 Object[] rhs = {1, 2, 3};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 }

 @Test
 public void testAppend_ObjectsWithSameValues() {
 Object lhs = new TestClass();
 Object rhs = new TestClass();
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());
 }

 @Test
 public void testAppend_ObjectsWithDifferentValues() {
 Object lhs = new TestClass();
 Object rhs = new TestClass();
 lhs.field1 = "value1";
 rhs.field1 = "value2";
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppend_IncompatibleTypes() {
 Object lhs = new String("hello");
 Object rhs = new Integer(5);
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 }

 @Test
 public void testAppendSuper() {
 CompareToBuilder builder = new CompareToBuilder();
 builder.appendSuper(2);
 assertEquals(2, builder.toComparison());
 }
}

class TestClass {
 private String field1;
 private String transientField;
}