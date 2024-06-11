package org.junit.test;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

 @Test
 public void testAppend() {
 // Test for basic functionality
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 Object lhs = new Object();
 Object rhs = new Object();
 compareToBuilder.append(lhs, rhs);
 int result = compareToBuilder.toComparison();
 assertEquals(0, result);

 // Test for edge cases and exception handling
 compareToBuilder = new CompareToBuilder();
 Object sameInstanceLhs = new Object();
 compareToBuilder.append(sameInstanceLhs, sameInstanceLhs);
 result = compareToBuilder.toComparison();
 assertEquals(0, result);

 compareToBuilder = new CompareToBuilder();
 Object lhsNull = null;
 Object rhs = new Object();
 compareToBuilder.append(lhsNull, rhs);
 try {
 result = compareToBuilder.toComparison();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 compareToBuilder = new CompareToBuilder();
 Object lhs = new Object();
 Object rhsNull = null;
 compareToBuilder.append(lhs, rhsNull);
 try {
 result = compareToBuilder.toComparison();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }

 compareToBuilder = new CompareToBuilder();
 Object lhsDifferentClass = new Object1();
 Object rhs = new Object2();
 compareToBuilder.append(lhsDifferentClass, rhs);
 result = compareToBuilder.toComparison();
 assertEquals(-1, result); // or 1, depending on the implementation
 }

 @Test
 public void testAppendSuper() {
 // Test for basic functionality
 CompareToBuilder compareToBuilder = new CompareToBuilder();
 int superCompareTo = 5;
 compareToBuilder.appendSuper(superCompareTo);
 int result = compareToBuilder.toComparison();
 assertEquals(superCompareTo, result);

 // Test for edge cases
 compareToBuilder = new CompareToBuilder();
 int superCompareToNegative = -5;
 compareToBuilder.appendSuper(superCompareToNegative);
 result = compareToBuilder.toComparison();
 assertEquals(superCompareToNegative, result);

 compareToBuilder = new CompareToBuilder();
 int superCompareToPositive = 5;
 compareToBuilder.appendSuper(superCompareToPositive);
 result = compareToBuilder.toComparison();
 assertEquals(superCompareToPositive, result);
 }
}