package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

 @Test
 public void testReflectionCompare_ObjectsWithSameValues() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_ObjectsWithDifferentClasses() {
 Object obj1 = new Object();
 String obj2 = new String("hello");
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2);
 assertNotEquals(0, comparison);
 }

 @Test(expected = NullPointerException.class)
 public void testReflectionCompare_NullArguments() {
 Object obj1 = null;
 Object obj2 = new Object();
 CompareToBuilder.reflectionCompare(obj1, obj2);
 }

 @Test(expected = ClassCastException.class)
 public void testReflectionCompare_ArgumentsWithIncompatibleTypes() {
 Object obj1 = new String("hello");
 Object obj2 = new Integer(1);
 CompareToBuilder.reflectionCompare(obj1, obj2);
 }

 @Test
 public void testReflectionCompare_ObjectsWithTransients() {
 class TestClass {
 private int privateInt;
 private transient String transientString;

 public TestClass(int privateInt, String transientString) {
 this.privateInt = privateInt;
 this.transientString = transientString;
 }
 }

 Object obj1 = new TestClass(1, "test");
 Object obj2 = new TestClass(1, null);
 boolean compareTransients = false;
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, compareTransients);
 assertEquals(0, comparison);

 compareTransients = true;
 comparison = CompareToBuilder.reflectionCompare(obj1, obj2, compareTransients);
 assertEquals(-1, comparison);
 }

 @Test
 public void testReflectionCompare_ObjectsWithExcludedFields() {
 class TestClass {
 private int privateInt;
 private String privateString;

 public TestClass(int privateInt, String privateString) {
 this.privateInt = privateInt;
 this.privateString = privateString;
 }
 }

 Object obj1 = new TestClass(1, "test");
 Object obj2 = new TestClass(1, "anotherTest");
 Collection<String> excludeFields = Arrays.asList("privateString");
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, excludeFields.toArray(new String[0]));
 assertEquals(0, comparison);
 }

 @Test
 public void testReflectionCompare_ObjectsWithCustomComparator() {
 class TestClass {
 private int privateInt;

 public TestClass(int privateInt) {
 this.privateInt = privateInt;
 }

 @Override
 public int compareTo(Object o) {
 TestClass other = (TestClass)o;
 return Integer.compare(this.privateInt, other.privateInt);
 }
 }

 Object obj1 = new TestClass(1);
 Object obj2 = new TestClass(2);
 Comparator<?> comparator = null;
 int comparison = CompareToBuilder.reflectionCompare(obj1, obj2, comparator);
 assertEquals(-1, comparison);

 comparator = Objects.comparingInt(TestClass::getPrivateInt);
 comparison = CompareToBuilder.reflectionCompare(obj1, obj2, comparator);
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppendSuper() {
 int superCompareTo = 1;
 CompareToBuilder builder = new CompareToBuilder();
 builder.appendSuper(superCompareTo);
 assertEquals(superCompareTo, builder.toComparison());
 }

 @Test
 public void testAppend_Objects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(obj1, obj2);
 int comparison = builder.toComparison();
 assertNotEquals(0, comparison);
 }

 @Test
 public void testAppend_ObjectsWithComparators() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Comparator<?> comparator = null;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(obj1, obj2, comparator);
 int comparison = builder.toComparison();
 assertNotEquals(0, comparison);
 }

 @Test
 public void testAppend_ObjectsArray() {
 Object[] obj1 = {1, 2, 3};
 Object[] obj2 = {1, 2, 3};
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(obj1, obj2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_ObjectsArrayWithComparators() {
 Object[] obj1 = {1, 2, 3};
 Object[] obj2 = {1, 2, 3};
 Comparator<Object[]> comparator = null;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(obj1, obj2, comparator);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes() {
 int int1 = 1;
 int int2 = 2;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(int1, int2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Equal() {
 int int1 = 1;
 int int2 = 1;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(int1, int2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_DifferentSigns() {
 int int1 = -1;
 int int2 = 1;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(int1, int2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_SameSign() {
 int int1 = 1;
 int int2 = 1;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(int1, int2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Long() {
 long long1 = 1L;
 long long2 = 2L;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(long1, long2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Long_Equal() {
 long long1 = 1L;
 long long2 = 1L;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(long1, long2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Long_DifferentSigns() {
 long long1 = -1L;
 long long2 = 1L;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(long1, long2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Long_SameSign() {
 long long1 = 1L;
 long long2 = 1L;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(long1, long2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Float() {
 float float1 = 1.0f;
 float float2 = 2.0f;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(float1, float2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Float_Equal() {
 float float1 = 1.0f;
 float float2 = 1.0f;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(float1, float2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Float_Different() {
 float float1 = 1.0f;
 float float2 = 2.0f;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(float1, float2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Double() {
 double double1 = 1.0;
 double double2 = 2.0;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(double1, double2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Double_Equal() {
 double double1 = 1.0;
 double double2 = 1.0;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(double1, double2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Double_Different() {
 double double1 = 1.0;
 double double2 = 2.0;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(double1, double2);
 int comparison = builder.toComparison();
 assertEquals(-1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Boolean() {
 boolean boolean1 = true;
 boolean boolean2 = false;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(boolean1, boolean2);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Boolean_Equal() {
 boolean boolean1 = true;
 boolean boolean2 = true;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(boolean1, boolean2);
 int comparison = builder.toComparison();
 assertEquals(0, comparison);
 }

 @Test
 public void testAppend_PrimitiveTypes_Boolean_Different() {
 boolean boolean1 = true;
 boolean boolean2 = false;
 CompareToBuilder builder = new CompareToBuilder();
 builder.append(boolean1, boolean2);
 int comparison = builder.toComparison();
 assertEquals(1, comparison);
 }
}