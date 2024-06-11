package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.*;

public class CompareToBuilderTest {

 private Object lhs;
 private Object rhs;
 private CompareToBuilder builder;

 @Before
 public void setup() {
 lhs = new Object() {};
 rhs = new Object() {};
 builder = new CompareToBuilder();
 }

 @Test
 public void testReflectionCompare_Objects() {
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_NullLhs() {
 try {
 CompareToBuilder.reflectionCompare(null, rhs);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testReflectionCompare_NullRhs() {
 try {
 CompareToBuilder.reflectionCompare(lhs, null);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testReflectionCompare_DifferentClasses() {
 lhs = new Object() {};
 rhs = new Object() {};
 Class<?> rhsClass = rhs.getClass();
 Class<?> lhsClass = lhs.getClass();
 if (!lhsClass.isInstance(rhs)) {
 throw new ClassCastException();
 }
 try {
 CompareToBuilder.reflectionCompare(lhs, rhs, rhsClass);
 fail("ClassCastException expected");
 } catch (ClassCastException e) {
 // expected
 }
 }

 @Test
 public void testReflectionCompare_ExcludeFields() {
 String[] excludeFields = new String[]{"field1", "field2"};
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, null, CompareToBuilder.class, excludeFields);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_Transients() {
 Class<?> transientClass = TransientClass.class;
 lhs = new TransientClass();
 rhs = new TransientClass();
 int comparison = CompareToBuilder.reflectionCompare(lhs, rhs, true);
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_Objects() {
 builder.append(lhs, rhs);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_NullLhs() {
 builder.append(lhs, rhs);
 builder.append(null, rhs);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_NullRhs() {
 builder.append(lhs, rhs);
 builder.append(lhs, null);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testAppend_ArrayOfObjects() {
 Object[] lhsArray = new Object[]{"test1", "test2"};
 Object[] rhsArray = new Object[]{"test1", "test3"};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_ArrayOfObjects_Comparator() {
 Object[] lhsArray = new Object[]{"test1", "test2"};
 Object[] rhsArray = new Object[]{"test2", "test1"};
 Comparator<?> comparator = (o1, o2) -> ((String)o1).compareTo((String)o2);
 builder.append(lhsArray, rhsArray, comparator);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testAppend_LongArray() {
 long[] lhsArray = {1, 2, 3, 4};
 long[] rhsArray = {1, 2, 3, 5};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_IntArray() {
 int[] lhsArray = {1, 2, 3, 4};
 int[] rhsArray = {1, 2, 3, 5};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_ShortArray() {
 short[] lhsArray = {1, 2, 3, 4};
 short[] rhsArray = {1, 2, 3, 5};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_CharArray() {
 char[] lhsArray = {'a', 'b', 'c', 'd'};
 char[] rhsArray = {'a', 'b', 'c', 'e'};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_ByteArray() {
 byte[] lhsArray = {1, 2, 3, 4};
 byte[] rhsArray = {1, 2, 3, 5};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_DoubleArray() {
 double[] lhsArray = {1.0, 2.0, 3.0, 4.0};
 double[] rhsArray = {1.0, 2.0, 3.0, 5.0};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_FloatArray() {
 float[] lhsArray = {1.0f, 2.0f, 3.0f, 4.0f};
 float[] rhsArray = {1.0f, 2.0f, 3.0f, 5.0f};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_BooleanArray() {
 boolean[] lhsArray = {true, false, true, false};
 boolean[] rhsArray = {true, false, false, true};
 builder.append(lhsArray, rhsArray);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppendSuper() {
 builder.appendSuper(1);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testToString() {
 assertEquals("CompareToBuilder[comparison=0]", builder.toString());
 }

}

class TransientClass {
 @Transient
 private String transientField;
}