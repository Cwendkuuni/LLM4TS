package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private Instance emptyInstance;

 @Test
 public void testGetID() {
     instance = new DenseInstance(new double[0]);
     assertEquals(0, instance.getID());

     instance = new DenseInstance(new double[] { 1.0 });
     assertEquals(1, instance.getID());
 }

 @Test
 public void testIterator() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     Iterator<Double> iterator = instance.iterator();
     assertEquals(2, iterator.nextCount());
     assertEquals(1.0, iterator.next(), 0.0);
     assertEquals(2.0, iterator.next(), 0.0);
 }

 @Test
 public void testConstructorWithNullClassValue() {
     instance = new AbstractInstance();
     assertNull(instance.classValue());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullClassValueThrowsException() {
     new AbstractInstance(null);
 }

 @Test
 public void testGetClassValue() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 }, new Integer[0]);
     instance.setClassValue("Test");
     assertEquals("Test", instance.classValue());
 }

 @Test
 public void testSetClassValue() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance.setClassValue("Test");
     assertEquals("Test", instance.classValue());
 }

 @Test
 public void testMinusWithInstance() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance2 = new DenseInstance(new double[] { 0.5, 1.5 });
     Instance result = instance.minus(instance2);
     assertEquals(2.0, result.get(0), 0.0);
     assertEquals(0.5, result.get(1), 0.0);
 }

 @Test
 public void testMinusWithDouble() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     Instance result = instance.minus(3.0);
     assertEquals(-2.0, result.get(0), 0.0);
     assertEquals(-1.0, result.get(1), 0.0);
 }

 @Test
 public void testDivideWithDouble() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     Instance result = instance.divide(2.0);
     assertEquals(0.5, result.get(0), 0.0);
     assertEquals(1.0, result.get(1), 0.0);
 }

 @Test
 public void testMultiplyWithDouble() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     Instance result = instance.multiply(2.0);
     assertEquals(2.0, result.get(0), 0.0);
     assertEquals(4.0, result.get(1), 0.0);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZeroThrowsException() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     Instance result = instance.divide(0.0);
 }

 @Test(expected = ArithmeticException.class)
 public void testSubtractWithNegativeNumberThrowsException() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance.minus(-3.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNullInstanceThrowsException() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance.add((Instance)null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubtractWithNullInstanceThrowsException() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance.minus((Instance)null);
 }

 @Test
 public void testAddWithInstance() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance2 = new DenseInstance(new double[] { 0.5, 1.5 });
     Instance result = instance.add(instance2);
     assertEquals(1.5, result.get(0), 0.0);
     assertEquals(3.5, result.get(1), 0.0);
 }

 @Test
 public void testAddWithDouble() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     Instance result = instance.add(3.0);
     assertEquals(4.0, result.get(0), 0.0);
     assertEquals(5.0, result.get(1), 0.0);
 }

 @Test
 public void testSqrt() {
     instance = new DenseInstance(new double[] { 4.0 });
     Instance result = instance.sqrt();
     assertEquals(2.0, result.get(0), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeNumberThrowsException() {
     instance = new DenseInstance(new double[] { -1.0 });
     instance.sqrt();
 }

 @Test
 public void testNonExistentMethod() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     assertNull(instance.nonExistentMethod());
 }

 @Test
 public void testHashCode() {
     instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
     instance2 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
     instance3 = new DenseInstance(new double[] { 1.0, 2.0, 4.0 });
     assertEquals(instance.hashCode(), instance.hashCode());
     assertNotEquals(instance.hashCode(), instance2.hashCode());
     assertNotEquals(instance.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEqualsWithSameInstance() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     assertTrue(instance.equals(instance));
 }

 @Test
 public void testEqualsWithDifferentInstance() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance2 = new DenseInstance(new double[] { 1.0, 3.0 });
     assertFalse(instance.equals(instance2));
 }

 @Test
 public void testEqualsWithNullObject() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     assertFalse(instance.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClass() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     assertFalse(instance.equals(new Object()));
 }

 @Test
 public void testMinusWithEmptyInstance() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     emptyInstance = new DenseInstance(new double[0]);
     Instance result = instance.minus(emptyInstance);
     assertEquals(1.0, result.get(0), 0.0);
     assertEquals(2.0, result.get(1), 0.0);
 }

 @Test
 public void testDivideWithEmptyInstanceThrowsException() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     try {
         instance.divide(emptyInstance);
         fail("Division by empty instance should throw an exception.");
     } catch (ArithmeticException e) {
         // Expected
     }
 }

 @Test
 public void testMultiplyWithEmptyInstance() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     emptyInstance = new DenseInstance(new double[0]);
     Instance result = instance.multiply(emptyInstance);
     assertEquals(new DenseInstance(new double[0]), result);
 }

 @Test
 public void testAddWithEmptyInstance() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     emptyInstance = new DenseInstance(new double[0]);
     Instance result = instance.add(emptyInstance);
     assertEquals(instance, result);
 }

 @Test
 public void testSubtractWithEmptyInstance() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     emptyInstance = new DenseInstance(new double[0]);
     Instance result = instance.minus(emptyInstance);
     assertEquals(instance, result);
 }

 @Test
 public void testSqrtWithZeroValue() {
     instance = new DenseInstance(new double[] { 0.0 });
     Instance result = instance.sqrt();
     assertEquals(0.0, result.get(0), 0.0);
 }

 @Test
 public void testMinusWithInstanceWithDifferentSize() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
     Instance result = instance.minus(instance2);
     assertArrayEquals(new double[0], result.toArray(), 0.0);
 }

 @Test
 public void testAddWithInstanceWithDifferentSizeThrowsException() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
     try {
         instance.add(instance2);
         fail("Addition with instances of different sizes should throw an exception.");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testDivideWithInstanceWithDifferentSizeThrowsException() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
     try {
         instance.divide(instance2);
         fail("Division with instances of different sizes should throw an exception.");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testMultiplyWithInstanceWithDifferentSizeThrowsException() {
     instance = new DenseInstance(new double[] { 1.0, 2.0 });
     instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
     try {
         instance.multiply(instance2);
         fail("Multiplication with instances of different sizes should throw an exception.");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }
}