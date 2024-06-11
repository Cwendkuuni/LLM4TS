package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;

import org.apache.commons.lang3.*;
import org.junit.Test;

public class CompareToBuilderTest {

 private static final int EXPECTED_ZERO = 0;
 private static final int EXPECTED_NEGATIVE = -1;
 private static final int EXPECTED_POSITIVE = 1;
 private static final int[] INT_ARRAY_1 = {1};
 private static final int[] INT_ARRAY_2 = {2};
 private static final Integer INT_OBJ_1 = 1;
 private static final Integer INT_OBJ_2 = 2;
 private static final Comparator<Object> COMPARATOR = new Comparator<Object>() {
      @Override
      public int compare(Object o1, Object o2) {
          return ((Comparable<Object>) o1).compareTo(o2);
      }
  };

 @Test
 public void testComparison_sameObjects() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = lhs;
     CompareToBuilder result = compareToBuilder.append(lhs, rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_ZERO, comparison);
  }

 @Test
 public void testComparison_differentObjects_nullLhs() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = null;
     Object rhs = new Object();
     CompareToBuilder result = compareToBuilder.append(lhs, rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_NEGATIVE, comparison);
  }

 @Test
 public void testComparison_differentObjects_nullRhs() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = null;
     CompareToBuilder result = compareToBuilder.append(lhs, rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_POSITIVE, comparison);
  }

 @Test
 public void testComparison_differentObjects_differentClasses() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = new String("test");
     CompareToBuilder result = compareToBuilder.append(lhs, rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_NEGATIVE, comparison);
  }

 @Test
 public void testComparison_array_sameDimensions_sameElements() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object[] lhs = new Object[0];
     Object[] rhs = new Object[0];
     CompareToBuilder result = compareToBuilder.append((Object[]) lhs, (Object[]) rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_ZERO, comparison);
  }

 @Test
 public void testComparison_array_sameDimensions_differentElements() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object[] lhs = new Object[] {1, 2, 3};
     Object[] rhs = new Object[] {2, 3, 1};
     CompareToBuilder result = compareToBuilder.append((Object[]) lhs, (Object[]) rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_NEGATIVE, comparison);
  }

 @Test
 public void testComparison_array_sameDimensions_sameElementsOrder() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object[] lhs = new Object[] {1, 2, 3};
     Object[] rhs = new Object[] {3, 2, 1};
     CompareToBuilder result = compareToBuilder.append((Object[]) lhs, (Object[]) rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_POSITIVE, comparison);
  }

 @Test
 public void testComparison_array_differentDimensions() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object[] lhs = new Object[] {1, 2};
     Object[] rhs = new Object[] {1, 2, 3};
     CompareToBuilder result = compareToBuilder.append((Object[]) lhs, (Object[]) rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_NEGATIVE, comparison);
  }

 @Test
 public void testComparison_arrays_differentTypes() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     long[] lhs = {1};
     int[] rhs = {2};
     CompareToBuilder result = compareToBuilder.append(lhs, rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_NEGATIVE, comparison);
  }

 @Test
 public void testComparison_arrays_sameTypes() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     int[] lhs = {1, 2, 3};
     int[] rhs = {1, 2, 3};
     CompareToBuilder result = compareToBuilder.append(lhs, rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_ZERO, comparison);
  }

 @Test
 public void testComparison_arrays_sameTypes_invertedOrder() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     int[] lhs = {1, 2, 3};
     int[] rhs = {3, 2, 1};
     CompareToBuilder result = compareToBuilder.append(lhs, rhs);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_POSITIVE, comparison);
  }

 @Test
 public void testComparison_arrays_differentTypes_integerAndBoolean() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     int[] intArray = {1};
     boolean[] booleanArray = {false};
     CompareToBuilder result = compareToBuilder.append(intArray, booleanArray);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_NEGATIVE, comparison);
  }

 @Test
 public void testComparison_arrays_differentTypes_booleanAndInteger() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     boolean[] booleanArray = {false};
     int[] intArray = {1};
     CompareToBuilder result = compareToBuilder.append(booleanArray, intArray);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_POSITIVE, comparison);
  }

 @Test
 public void testComparison_comparator() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Comparable<Object> lhs = INT_OBJ_1;
     Comparable<Object> rhs = INT_OBJ_2;
     CompareToBuilder result = compareToBuilder.append(lhs, rhs, COMPARATOR);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_NEGATIVE, comparison);
  }

 @Test
 public void testComparison_comparator_sameObjects() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Comparable<Object> lhs = INT_OBJ_1;
     Comparable<Object> rhs = lhs;
     CompareToBuilder result = compareToBuilder.append(lhs, rhs, COMPARATOR);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_ZERO, comparison);
  }

 @Test
 public void testComparison_comparator_nullLhs() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Comparable<Object> lhs = null;
     Comparable<Object> rhs = INT_OBJ_2;
     CompareToBuilder result = compareToBuilder.append(lhs, rhs, COMPARATOR);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_NEGATIVE, comparison);
  }

 @Test
 public void testComparison_comparator_nullRhs() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Comparable<Object> lhs = INT_OBJ_1;
     Comparable<Object> rhs = null;
     CompareToBuilder result = compareToBuilder.append(lhs, rhs, COMPARATOR);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_POSITIVE, comparison);
  }

 @Test
 public void testComparison_comparator_nullBoth() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Comparable<Object> lhs = null;
     Comparable<Object> rhs = null;
     CompareToBuilder result = compareToBuilder.append(lhs, rhs, COMPARATOR);
     int comparison = result.toComparison();
     assertEquals(EXPECTED_ZERO, comparison);
  }

 @Test(expected = NullPointerException.class)
 public void testComparison_nullLhs_reflectionCompare() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = null;
     Object rhs = new Object();
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     fail("Should throw NullPointerException");
  }

 @Test(expected = NullPointerException.class)
 public void testComparison_nullRhs_reflectionCompare() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = null;
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     fail("Should throw NullPointerException");
  }

 @Test(expected = ClassCastException.class)
 public void testComparison_differentClasses() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = new String("test");
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     fail("Should throw ClassCastException");
  }

 @Test(expected = ClassCastException.class)
 public void testComparison_differentSuperclasses() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = new Object() {};
     int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
     fail("Should throw ClassCastException");
  }

 @Test(expected = ArrayStoreException.class)
 public void testComparison_arrayElementsDifferentTypes() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object[] lhs = new Object[] {1, "test"};
     Object[] rhs = new Object[] {2, "test", 3};
     CompareToBuilder result = compareToBuilder.append((Object[]) lhs, (Object[]) rhs);
     int comparison = result.toComparison();
     fail("Should throw ArrayStoreException");
  }

 @Test(expected = InternalError.class)
 public void testComparison_illegalAccessException() {
     CompareToBuilder compareToBuilder = new CompareToBuilder();
     Object lhs = new Object();
     Object rhs = new Object();
     CompareToBuilder result = compareToBuilder.append(lhs, rhs);
     result.appendField("test", "test");
     int comparison = result.toComparison();
     fail("Should throw InternalError");
  }

}