package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;

 @org.junit.Before
 public void setUp() {
 // Create instances for testing
 instance1 = new DenseInstance(new double[]{1.0, 2.0});
 instance2 = new DenseInstance(new double[]{3.0, 4.0});
 instance3 = new DenseInstance(new double[]{5.0, 6.0});
 instance4 = new DenseInstance(new double[]{7.0, 8.0});
 }

 @org.junit.Test
 public void testGetID() {
 AbstractInstance testInstance = instance1;
 int id = testInstance.getID();
 assertEquals("ID should be set correctly", 0, id);
 }

 @org.junit.Test
 public void testIterator() {
 AbstractInstance testInstance = instance1;
 Iterator<Double> iterator = testInstance.iterator();
 List<Double> values = new ArrayList<>();
 while (iterator.hasNext()) {
 values.add(iterator.next());
 }
 List<Double> expectedValues = Arrays.asList(1.0, 2.0);
 assertEquals("Iterator should return the correct values", expectedValues, values);
 }

 @org.junit.Test
 public void testConstructorNoArgs() {
 AbstractInstance testInstance = new AbstractInstance();
 assertEquals("ID should be set correctly when using default constructor", 0, testInstance.getID());
 }

 @org.junit.Test
 public void testConstructorWithArg() {
 Object classValue = new Object();
 AbstractInstance testInstance = new AbstractInstance(classValue);
 assertEquals("ID should be set correctly when using constructor with arg", 1, testInstance.getID());
 assertEquals("Class value should be set correctly", classValue, testInstance.classValue());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullInstance() {
 AbstractInstance testInstance = instance1;
 Instance min = null;
 Instance result = testInstance.minus(min);
 fail("Should throw IllegalArgumentException");
 }

 @org.junit.Test
 public void testMinusWithInstance() {
 AbstractInstance testInstance = instance1;
 Instance min = instance2;
 Instance result = testInstance.minus(min);
 List<Double> testValues = new ArrayList<>();
 testValues.add(1.0 - 3.0);
 testValues.add(2.0 - 4.0);
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Subtraction should work correctly", expectedResult, result);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testMinusWithDoubleAndNullInstance() {
 AbstractInstance testInstance = instance1;
 double min = 0.0;
 Instance result = testInstance.minus(min, null);
 fail("Should throw IllegalArgumentException when second argument is null");
 }

 @org.junit.Test
 public void testMinusWithDouble() {
 AbstractInstance testInstance = instance1;
 double min = 2.0;
 Instance result = testInstance.minus(min);
 List<Double> testValues = new ArrayList<>();
 testValues.add(1.0 - 2.0);
 testValues.add(2.0 - 2.0);
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Subtraction with double should work correctly", expectedResult, result);
 }

 @org.junit.Test(expected = ArithmeticException.class)
 public void testDivideWithZero() {
 AbstractInstance testInstance = instance1;
 double min = 0.0;
 Instance result = testInstance.divide(min);
 fail("Should throw ArithmeticException when dividing by zero");
 }

 @org.junit.Test
 public void testDivideWithInstance() {
 AbstractInstance testInstance = instance1;
 Instance min = instance2;
 Instance result = testInstance.divide(min);
 List<Double> testValues = new ArrayList<>();
 testValues.add(1.0 / 3.0);
 testValues.add(2.0 / 4.0);
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Division should work correctly", expectedResult, result);
 }

 @org.junit.Test(expected = ArithmeticException.class)
 public void testMultiplyWithZero() {
 AbstractInstance testInstance = instance1;
 double value = 0.0;
 Instance result = testInstance.multiply(value);
 fail("Should throw ArithmeticException when multiplying by zero");
 }

 @org.junit.Test
 public void testMultiplyWithInstance() {
 AbstractInstance testInstance = instance1;
 Instance value = instance2;
 Instance result = testInstance.multiply(value);
 List<Double> testValues = new ArrayList<>();
 testValues.add(1.0 * 3.0);
 testValues.add(2.0 * 4.0);
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Multiplication should work correctly", expectedResult, result);
 }

 @org.junit.Test
 public void testHashCode() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 int hashCode1 = testInstance1.hashCode();
 int hashCode2 = testInstance2.hashCode();
 assertNotEquals("Hashcodes should be different for different instances", hashCode1, hashCode2);
 }

 @org.junit.Test
 public void testEquals() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 boolean equals = testInstance1.equals(testInstance2);
 assertFalse("Different instances should not be equal", equals);
 }

 @org.junit.Test
 public void testEqualsWithSameInstance() {
 AbstractInstance testInstance = instance1;
 boolean equals = testInstance.equals(testInstance);
 assertTrue("The same instance should be equal to itself", equals);
 }

 @org.junit.Test
 public void testEqualsWithSameClassValue() {
 AbstractInstance testInstance1 = new DenseInstance(new double[]{1.0, 2.0});
 AbstractInstance testInstance2 = new DenseInstance(new double[]{1.0, 2.0});
 boolean equals = testInstance1.equals(testInstance2);
 assertTrue("Two instances with the same class value should be equal", equals);
 }

 @org.junit.Test
 public void testMinusWithInstanceAndDouble() {
 AbstractInstance testInstance = instance1;
 Instance min = instance2;
 double minusValue = 3.0;
 Instance result = testInstance.minus(min, minusValue);
 List<Double> testValues = new ArrayList<>();
 testValues.add(1.0 - 3.0);
 testValues.add(2.0 - 3.0);
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Subtraction with instance and double should work correctly", expectedResult, result);
 }

 @org.junit.Test
 public void testDivideWithInstanceAndDouble() {
 AbstractInstance testInstance = instance1;
 Instance min = instance2;
 double divisor = 3.0;
 Instance result = testInstance.divide(min, divisor);
 List<Double> testValues = new ArrayList<>();
 testValues.add(1.0 / (3.0 * 3.0));
 testValues.add(2.0 / (3.0 * 4.0));
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Division with instance and double should work correctly", expectedResult, result);
 }

 @org.junit.Test
 public void testAddWithInstanceAndDouble() {
 AbstractInstance testInstance = instance1;
 Instance min = instance2;
 double addValue = 3.0;
 Instance result = testInstance.add(min, addValue);
 List<Double> testValues = new ArrayList<>();
 testValues.add(1.0 + 3.0);
 testValues.add(2.0 + 3.0);
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Addition with instance and double should work correctly", expectedResult, result);
 }

 @org.junit.Test
 public void testAddWithDoubleAndInstance() {
 AbstractInstance testInstance = instance1;
 double addValue = 3.0;
 Instance min = instance2;
 Instance result = testInstance.add(addValue, min);
 List<Double> testValues = new ArrayList<>();
 testValues.add(1.0 + 3.0);
 testValues.add(2.0 + 3.0);
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Addition with double and instance should work correctly", expectedResult, result);
 }

 @org.junit.Test
 public void testSqrt() {
 AbstractInstance testInstance = new DenseInstance(new double[]{1.0, 4.0});
 Instance result = testInstance.sqrt();
 List<Double> testValues = new ArrayList<>();
 testValues.add(Math.sqrt(1.0));
 testValues.add(Math.sqrt(16.0));
 List<Double> expectedValues = Arrays.asList(testValues.get(0), testValues.get(1));
 Instance expectedResult = new DenseInstance(new double[]{testValues.get(0), testValues.get(1)});
 assertEquals("Square root should work correctly", expectedResult, result);
 }
}