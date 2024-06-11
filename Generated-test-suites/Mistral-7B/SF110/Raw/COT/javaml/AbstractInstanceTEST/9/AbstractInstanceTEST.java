package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private AbstractInstance emptyInstance;

 @Test
 public void testGetID() {
     instance = new DenseInstance(new double[5]);
     assertEquals(AbstractInstance.nextID, instance.getID());
     instance2 = new DenseInstance(new double[5]);
     assertNotSame(instance.getID(), instance2.getID());
 }

 @Test
 public void testIterator() {
     instance = new DenseInstance(new double[5]);
     Iterator<Double> iterator = instance.iterator();
     int count = 0;
     while (iterator.hasNext()) {
         iterator.next();
         count++;
     }
     assertEquals(instance.noAttributes(), count);
 }

 @Test
 public void testConstructor() {
     instance = new DenseInstance(new double[5]);
     assertNotNull(instance);
     instance2 = new DenseInstance(new double[5], new Object());
     assertNotNull(instance2);
     emptyInstance = new DenseInstance(new double[0]);
     assertNotNull(emptyInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
     new AbstractInstance(null);
 }

 @Test
 public void testClassValue() {
     instance = new DenseInstance(new double[5], new Object());
     assertEquals(instance.classValue(), instance.classValue());
     assertNotSame(instance.classValue(), new Object());
 }

 @Test
 public void testSetClassValue() {
     instance = new DenseInstance(new double[5]);
     instance.setClassValue(new Object());
     assertEquals(instance.classValue(), new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullInstance() {
     instance = new DenseInstance(new double[5]);
     AbstractInstance min = null;
     instance.minus(min);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testMinusWithInvalidIndex() {
     instance = new DenseInstance(new double[5]);
     int index = instance.noAttributes();
     instance.minus(index);
 }

 @Test
 public void testMinus() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     instance2 = new DenseInstance(new double[]{1.1, 1.9, 2.1, 3.1, 4.1});
     AbstractInstance result = instance.minus(instance2);
     assertArrayEquals(new double[]{0.1, -0.9, 0.9, 0.9, 1.0}, result.asArray(), 0.001);
 }

 @Test
 public void testMinusWithDouble() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance result = instance.minus(2.0);
     assertArrayEquals(new double[]{0.0, -1.0, -1.0, -1.0, -1.0}, result.asArray(), 0.001);
 }

 @Test
 public void testDivide() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance result = instance.divide(2.0);
     assertArrayEquals(new double[]{0.5, 1.0, 1.5, 2.0, 2.5}, result.asArray(), 0.001);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance result = instance.divide(0.0);
 }

 @Test
 public void testMultiply() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance result = instance.multiply(2.0);
     assertArrayEquals(new double[]{2.0, 4.0, 6.0, 8.0, 10.0}, result.asArray(), 0.001);
 }

 @Test
 public void testHashCode() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance instance3 = new DenseInstance(new double[]{1.1, 2.1, 3.1, 4.1, 5.1});
     assertEquals(instance.getID(), instance.hashCode());
     assertEquals(instance2.getID(), instance2.hashCode());
     assertNotEquals(instance.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEquals() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance instance3 = new DenseInstance(new double[]{1.1, 2.1, 3.1, 4.1, 5.1});
     assertTrue(instance.equals(instance));
     assertTrue(instance2.equals(instance2));
     assertFalse(instance.equals(instance3));
 }

 @Test
 public void testMultiplyWithNullInstance() {
     instance = new DenseInstance(new double[5]);
     AbstractInstance min = null;
     instance.multiply(min);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullValue() {
     instance = new DenseInstance(new double[5]);
     instance.minus((double) null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithZeroInstance() {
     instance = new DenseInstance(new double[5]);
     AbstractInstance min = new DenseInstance(new double[5], new Object());
     instance.divide(min);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithZeroValue() {
     instance = new DenseInstance(new double[5]);
     AbstractInstance result = instance.divide(0.0);
 }

 @Test
 public void testAdd() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance result = instance.add(2.0);
     assertArrayEquals(new double[]{3.0, 4.0, 5.0, 6.0, 7.0}, result.asArray(), 0.001);
 }

 @Test
 public void testAddWithNullInstance() {
     instance = new DenseInstance(new double[5]);
     AbstractInstance min = null;
     instance.add(min);
 }

 @Test
 public void testAddWithInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
     AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0, 6.0});
     AbstractInstance result = instance.add(instance2);
     assertArrayEquals(new double[]{3.0, 5.0, 7.0, 9.0, 11.0}, result.asArray(), 0.001);
 }

 @Test
 public void testSqrt() {
     instance = new DenseInstance(new double[]{1.0, 4.0, 9.0, 16.0, 25.0});
     AbstractInstance result = instance.sqrt();
     assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, result.asArray(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeInstance() {
     instance = new DenseInstance(new double[]{-1.0, 4.0, 9.0, 16.0, 25.0});
     instance.sqrt();
 }

}