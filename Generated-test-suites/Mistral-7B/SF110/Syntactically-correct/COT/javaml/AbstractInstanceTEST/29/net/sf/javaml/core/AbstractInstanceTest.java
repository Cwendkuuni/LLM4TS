package net.sf.javaml.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private static final int NUM_ATTRIBUTES = 3;

 @Test
 public void testConstructorWithNullClassValue() {
 AbstractInstance instance = new AbstractInstance();
 assertNull(instance.classValue());
 }

 @Test
 public void testConstructorWithClassValue() {
 Object classValue = new Object();
 AbstractInstance instance = new AbstractInstance(classValue);
 assertEquals(classValue, instance.classValue());
 }

 @Test
 public void testGetID() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());
 instance = new AbstractInstance();
 assertEquals(1, instance.getID());
 instance = new AbstractInstance();
 assertEquals(2, instance.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new DenseInstance(new double[NUM_ATTRIBUTES]);
 List<Double> expectedValues = Arrays.asList(1.0, 2.0, 3.0);
 List<Double> actualValues = new ArrayList<>();
 for (Double value : instance.iterator()) {
 actualValues.add(value);
 }
 assertEquals(expectedValues, actualValues);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveUsingIterator() {
 AbstractInstance instance = new DenseInstance(new double[NUM_ATTRIBUTES]);
 InstanceValueIterator iterator = instance.new InstanceValueIterator();
 iterator.next();
 iterator.remove();
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 AbstractInstance result = instance1.minus(instance2);
 AbstractInstance expectedResult = new DenseInstance(new double[]{-1.0, -1.0, -1.0});
 assertEquals(expectedResult, result);
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.minus(5.0);
 AbstractInstance expectedResult = new DenseInstance(new double[]{-4.0, -4.0, -4.0});
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInvalidArgument() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.minus(null);
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 AbstractInstance result = instance1.divide(instance2);
 AbstractInstance expectedResult = new DenseInstance(new double[]{0.3333333333333333, 0.6666666666666666, 0.75});
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithZero() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Instance result = instance.divide(0.0);
 assertNull(result);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Instance result = instance.divide(new DenseInstance(new double[]{-1.0, -1.0, -1.0}));
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 AbstractInstance result = instance1.multiply(instance2);
 AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, 6.0, 12.0});
 assertEquals(expectedResult, result);
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.multiply(5.0);
 AbstractInstance expectedResult = new DenseInstance(new double[]{5.0, 10.0, 15.0});
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithInvalidArgument() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.multiply(null);
 }

 @Test
 public void testHashCode() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance3 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 assertEquals(instance1.getID(), instance1.hashCode());
 assertEquals(instance1.getID(), instance1.hashCode());
 assertNotEquals(instance1.hashCode(), instance2.hashCode());
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEqualsWithSameInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = instance1;
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testEqualsWithDifferentInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 4.0});
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testEqualsWithNull() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.equals(null));
 }

 @Test
 public void testEqualsWithDifferentType() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Object other = new Object();
 assertFalse(instance.equals(other));
 }

 @Test
 public void testEqualsWithSameID() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testMinusWithNegativeDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.minus(-5.0);
 AbstractInstance expectedResult = new DenseInstance(new double[]{-6.0, -6.0, -6.0});
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDivideWithNegativeInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{-1.0, -2.0, -3.0});
 AbstractInstance result = instance1.divide(instance2);
 AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, -1.0, -0.5});
 assertEquals(expectedResult, result);
 }

 @Test
 public void testDivideWithZeroInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.divide(new DenseInstance());
 assertNull(result);
 }

 @Test
 public void testDivideWithZeroValue() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Instance result = instance.divide(new DenseInstance(new double[]{0.0, 0.0, 0.0}));
 assertNull(result);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideWithZeroValue2() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 0.0});
 AbstractInstance result = instance.divide(instance2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{-1.0, -2.0, -3.0});
 assertThrows(IllegalArgumentException.class, () -> instance.sqrt());
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new DenseInstance(new double[]{4.0, 9.0, 16.0});
 AbstractInstance result = instance.sqrt();
 AbstractInstance expectedResult = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddWithNegativeDouble() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.add(-5.0);
 AbstractInstance expectedResult = new DenseInstance(new double[]{-4.0, -4.0, -4.0});
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 AbstractInstance result = instance1.add(instance2);
 AbstractInstance expectedResult = new DenseInstance(new double[]{3.0, 5.0, 7.0});
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithInvalidArgument() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.add(null);
 }

 @Test
 public void testDivideWithZeroInstance2() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance();
 AbstractInstance result = instance.divide(instance2);
 assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = null;
 AbstractInstance result = instance.divide(instance2);
 assertNull(result);
 }

 @Test
 public void testMinusWithEmptyInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.minus(new DenseInstance());
 assertNull(result);
 }

 @Test
 public void testMinusWithSameInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = instance;
 AbstractInstance result = instance.minus(instance2);
 assertNull(result);
 }

 @Test
 public void testAddWithEmptyInstance() {
 AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance result = instance.add(new DenseInstance());
 AbstractInstance expectedResult = instance;
 assertEquals(expectedResult, result);
 }
}