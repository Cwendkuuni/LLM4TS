package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;

 @Test
 public void testGetID() {
     instance1 = new DenseInstance(new double[1]);
     assertEquals(0, instance1.getID());

     instance2 = new DenseInstance(new double[2]);
     assertEquals(1, instance2.getID());
 }

 @Test
 public void testIterator() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);

     Iterator<Double> iterator = instance1.iterator();
     assertEquals(1.0, iterator.next());
     assertFalse(iterator.hasNext());
 }

 @Test
 public void testNoArgumentsConstructor() {
     instance1 = new AbstractInstance();
     assertNull(instance1.classValue);
     assertEquals(0, instance1.getID());
 }

 @Test
 public void testConstructorWithClassValue() {
     instance1 = new AbstractInstance("TestClass");
     assertEquals("TestClass", instance1.classValue.toString());
     assertEquals(1, instance1.getID());
 }

 @Test
 public void testClassValue() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);
     assertEquals(instance1.classValue(), null);

     instance2 = new AbstractInstance("TestClass");
     instance2.put(0, 3.0);
     instance2.put(1, 4.0);
     assertEquals(instance2.classValue(), "TestClass");
 }

 @Test
 public void testSetClassValue() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);
     instance1.setClassValue("TestClass");
     assertEquals("TestClass", instance1.classValue.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInvalidInstance() {
     instance1 = new DenseInstance(new double[1]);
     assertNull(instance1.getClassValue());
     try {
         instance1.minus(null);
     } catch (NullPointerException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testMinusWithSameInstance() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     AbstractInstance instance4 = new DenseInstance(new double[1]);
     instance4.put(0, 1.0);

     AbstractInstance result = instance1.minus(instance4);
     assertEquals(0, result.noAttributes());
     assertNull(result.classValue);
 }

 @Test
 public void testMinusWithDifferentInstances() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);

     instance2 = new SparseInstance(new double[] {1.5, 2.5}, new int[] {0, 1});
     instance3 = instance1.minus(instance2);

     assertEquals(2, instance3.noAttributes());
     assertEquals(-0.5, instance3.value(0), 0.001);
     assertEquals(-0.5, instance3.value(1), 0.001);
     assertNull(instance3.classValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInvalidDouble() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.minus(-1.0);
     } catch (IllegalArgumentException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testMinusWithValidDouble() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);

     AbstractInstance result = instance1.minus(1.0);
     assertEquals(2, result.noAttributes());
     assertEquals(0.0, result.value(0), 0.001);
     assertEquals(1.0, result.value(1), 0.001);
     assertNull(result.classValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithInvalidInstance() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.divide(null);
     } catch (NullPointerException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithDivisorZero() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.divide(0);
     } catch (ArithmeticException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testDivideWithSameInstance() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     AbstractInstance instance4 = new DenseInstance(new double[1]);
     instance4.put(0, 1.0);

     AbstractInstance result = instance1.divide(instance4);
     assertEquals(0, result.noAttributes());
     assertNull(result.classValue);
 }

 @Test
 public void testDivideWithDifferentInstances() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);

     instance2 = new DenseInstance(new double[2]);
     instance2.put(0, 3.0);
     instance2.put(1, 4.0);

     AbstractInstance result = instance1.divide(instance2);
     assertEquals(2, result.noAttributes());
     assertEquals(0.3333, result.value(0), 0.001);
     assertEquals(0.5, result.value(1), 0.001);
     assertNull(result.classValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithInvalidDouble() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.divide(-1.0);
     } catch (IllegalArgumentException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testDivideWithValidDouble() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);

     AbstractInstance result = instance1.divide(2.0);
     assertEquals(2, result.noAttributes());
     assertEquals(0.5, result.value(0), 0.001);
     assertEquals(1.0, result.value(1), 0.001);
     assertNull(result.classValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithInvalidInstance() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.multiply(null);
     } catch (NullPointerException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testMultiplyWithSameInstance() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     AbstractInstance instance4 = new DenseInstance(new double[1]);
     instance4.put(0, 1.0);

     AbstractInstance result = instance1.multiply(instance4);
     assertEquals(0, result.noAttributes());
     assertNull(result.classValue);
 }

 @Test
 public void testMultiplyWithDifferentInstances() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);

     instance2 = new DenseInstance(new double[2]);
     instance2.put(0, 3.0);
     instance2.put(1, 4.0);

     AbstractInstance result = instance1.multiply(instance2);
     assertEquals(4, result.noAttributes());
     assertEquals(3.0 * 1.0, result.value(0), 0.001);
     assertEquals(2.0 * 4.0, result.value(1), 0.001);
     assertEquals(3.0, result.value(2), 0.001);
     assertEquals(4.0, result.value(3), 0.001);
     assertNull(result.classValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithInvalidDouble() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.multiply(-1.0);
     } catch (IllegalArgumentException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testMultiplyWithValidDouble() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);

     AbstractInstance result = instance1.multiply(2.0);
     assertEquals(4, result.noAttributes());
     assertEquals(2.0 * 1.0, result.value(0), 0.001);
     assertEquals(2.0 * 2.0, result.value(1), 0.001);
     assertEquals(2.0, result.value(2), 0.001);
     assertEquals(2.0, result.value(3), 0.001);
     assertNull(result.classValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithInvalidInstance() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.sqrt();
     } catch (NullPointerException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeValue() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, -1.0);
     try {
         instance1.sqrt();
     } catch (IllegalArgumentException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testSqrtWithZeroValue() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 0.0);

     AbstractInstance result = instance1.sqrt();
     assertEquals(1, result.noAttributes());
     assertEquals(0.0, result.value(0), 0.001);
     assertNull(result.classValue);
 }

 @Test
 public void testSqrtWithPositiveValue() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 4.0);

     AbstractInstance result = instance1.sqrt();
     assertEquals(1, result.noAttributes());
     assertEquals(2.0, result.value(0), 0.001);
     assertNull(result.classValue);
 }

 @Test
 public void testAddWithInvalidInstance() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.add(null);
     } catch (NullPointerException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testAddWithSameInstance() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     AbstractInstance instance4 = new DenseInstance(new double[1]);
     instance4.put(0, 1.0);

     AbstractInstance result = instance1.add(instance4);
     assertEquals(0, result.noAttributes());
     assertNull(result.classValue);
 }

 @Test
 public void testAddWithDifferentInstances() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);

     instance2 = new DenseInstance(new double[2]);
     instance2.put(0, 3.0);
     instance2.put(1, 4.0);

     AbstractInstance result = instance1.add(instance2);
     assertEquals(4, result.noAttributes());
     assertEquals(4.0, result.value(0), 0.001);
     assertEquals(6.0, result.value(1), 0.001);
     assertEquals(3.0, result.value(2), 0.001);
     assertEquals(4.0, result.value(3), 0.001);
     assertNull(result.classValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithInvalidDouble() {
     instance1 = new DenseInstance(new double[1]);
     instance1.put(0, 1.0);
     try {
         instance1.add(-1.0);
     } catch (IllegalArgumentException e) {
         fail("Expected IllegalArgumentException");
     }
 }

 @Test
 public void testAddWithValidDouble() {
     instance1 = new DenseInstance(new double[2]);
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);

     AbstractInstance result = instance1.add(1.0);
     assertEquals(3, result.noAttributes());
     assertEquals(2.0, result.value(0), 0.001);
     assertEquals(3.0, result.value(1), 0.001);
     assertEquals(1.0, result.value(2), 0.001);
     assertEquals(2.0, result.value(3), 0.001);
     assertNull(result.classValue);
 }
}