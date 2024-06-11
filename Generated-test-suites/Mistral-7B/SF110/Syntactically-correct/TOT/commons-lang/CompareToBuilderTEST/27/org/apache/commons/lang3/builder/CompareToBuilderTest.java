package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;;

public class CompareToBuilderTest {

 @Test
 public void testReflectionCompare_Objects_Equal() {
 Object lhs = new Object();
 Object rhs = lhs;
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_Objects_NotEqual_DifferentClasses() {
 Object lhs = new Object();
 Object rhs = new String("Hello World");
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertNotEquals(0, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompare_Objects_Null() {
 Object lhs = null;
 Object rhs = new Object();
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompare_Objects_IncompatibleClasses() {
 Object lhs = new Integer(1);
 Object rhs = new String("Hello World");
 CompareToBuilder.reflectionCompare(lhs, rhs);
 }

 @Test
 public void testReflectionCompare_Objects_WithExcludedFields() {
 Object lhs = new MyClass("field1", "field2");
 Object rhs = new MyClass("field1", "field3");
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, Arrays.asList("field2"));
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_Objects_WithTransients() {
 Object lhs = new MyClass("field1", null);
 Object rhs = new MyClass("field1", "field2");
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
 assertEquals(0, comparison);
 }

 @Test
 public void testAppendSuper() {
 CompareToBuilder builder = new CompareToBuilder();
 builder.appendSuper(5);
 assertEquals(5, builder.toComparison());
 }

 @Test
 public void testAppend_PrimitiveTypes() {
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(1, 2);
 assertEquals(-1, builder.toComparison());
 builder.append(2, 1);
 assertEquals(1, builder.toComparison());
 builder.append(1, 1);
 assertEquals(0, builder.toComparison());
 }

 @Test
 public void testAppend_Objects() {
 Object lhs = new Object();
 Object rhs = new Object();
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());
 }

 @Test
 public void testAppend_Arrays_Equal() {
 Object[] lhs = {1, 2, 3};
 Object[] rhs = {1, 2, 3};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());
 }

 @Test
 public void testAppend_Arrays_NotEqual() {
 Object[] lhs = {1, 2, 3};
 Object[] rhs = {1, 2, 4};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test(expected = NullPointerException.class)
 public void testAppend_Arrays_Null() {
 Object[] lhs = null;
 Object[] rhs = new Object[0];
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 }

 @Test
 public void testAppend_Arrays_WithComparator() {
 Object[] lhs = {"a", "b", "c"};
 Object[] rhs = {"b", "c", "a"};
 Comparator<Object> comparator = (o1, o2) -> ((String)o1).compareTo((String)o2);
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs, comparator);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_Arrays_WithCustomComparator() {
 Object[] lhs = {1, 2, 3};
 Object[] rhs = {3, 2, 1};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs, new MyComparator());
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_Arrays_WithDifferentLengths() {
 Object[] lhs = {1, 2, 3};
 Object[] rhs = {1, 2};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(-1, builder.toComparison());
 }

 @Test
 public void testAppend_CustomTypes() {
 MyClass lhs = new MyClass("field1", "field2");
 MyClass rhs = new MyClass("field1", "field3");
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(lhs, rhs);
 assertEquals(0, builder.toComparison());
 }

 @Test
 public void testAppendSuper() {
 CompareToBuilder builder = new CompareToBuilder();
 builder.appendSuper(5);
 assertEquals(5, builder.toComparison());
 }
}

class MyClass {
 String field1;
 String field2;

 MyClass(String field1, String field2) {
 this.field1 = field1;
 this.field2 = field2;
 }
}

class MyComparator implements Comparator<Object> {
 @Override
 public int compare(Object o1, Object o2) {
 return ((String)o1).compareTo((String)o2);
 }
}