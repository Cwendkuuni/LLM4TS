package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AbstractInstanceTest {

 private AbstractInstance instance;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private static final double DELTA = 0.01;

 @Test
 public void testGetID() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Assert.assertEquals(1, instance.getID());
 }

 @Test
 public void testIterator() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Iterator<Double> iterator = instance.iterator();
     Assert.assertTrue(iterator.hasNext());
     Assert.assertEquals(1.0, iterator.next(), DELTA);
     Assert.assertTrue(iterator.hasNext());
     Assert.assertEquals(2.0, iterator.next(), DELTA);
     Assert.assertTrue(iterator.hasNext());
     Assert.assertEquals(3.0, iterator.next(), DELTA);
     Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructorWithoutClassValue() {
     instance = new AbstractInstance();
     Assert.assertNull(instance.classValue());
 }

 @Test
 public void testConstructorWithClassValue() {
     String expectedClassValue = "TestClassValue";
     instance = new AbstractInstance(expectedClassValue);
     Assert.assertEquals(expectedClassValue, instance.classValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance min = null;
     instance.minus(min);
 }

 @Test
 public void testMinusWithInstance() {
     double[] instanceValues = {1.0, 2.0, 3.0};
     double[] minValues = {-1.0, -2.0, -3.0};
     instance = new DenseInstance(instanceValues);
     instance2 = new DenseInstance(minValues);
     Instance result = instance.minus(instance2);
     Double[] resultValues = result.toArray();
     Assert.assertArrayEquals(new Double[]{0.0, 0.0, 0.0}, resultValues, DELTA);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInvalidTypeInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     String min = "TestString";
     instance.minus(min);
 }

 @Test
 public void testDivideWithZero() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Assert.assertNull(instance.divide(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance min = null;
     instance.divide(min);
 }

 @Test
 public void testDivide() {
     double[] instanceValues = {1.0, 2.0, 3.0};
     double divisor = 2.0;
     instance = new DenseInstance(instanceValues);
     Instance result = instance.divide(divisor);
     Double[] resultValues = result.toArray();
     Assert.assertArrayEquals(new Double[]{0.5, 1.0, 1.5}, resultValues, DELTA);
 }

 @Test
 public void testMultiplyWithZero() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Assert.assertNull(instance.multiply(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNullInstance() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance value = null;
     instance.multiply(value);
 }

 @Test
 public void testMultiply() {
     double[] instanceValues = {1.0, 2.0, 3.0};
     double multiplier = 2.0;
     instance = new DenseInstance(instanceValues);
     Instance result = instance.multiply(multiplier);
     Double[] resultValues = result.toArray();
     Assert.assertArrayEquals(new Double[]{2.0, 4.0, 6.0}, resultValues, DELTA);
 }

 @Test(expected = IllegalStateException.class)
 public void testHashCode() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Assert.assertEquals(instance.hashCode(), instance2.hashCode());
     instance.setID(10);
     Assert.assertNotEquals(instance.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEquals() {
     double[] instanceValues = {1.0, 2.0, 3.0};
     double[] instance2Values = {1.0, 2.0, 3.0};
     instance = new DenseInstance(instanceValues);
     instance2 = new DenseInstance(instance2Values);
     AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.0, 4.0});
     Assert.assertTrue(instance.equals(instance));
     Assert.assertTrue(instance.equals(instance2));
     Assert.assertFalse(instance.equals(instance3));
 }

 @Test
 public void testMinusWithInstanceAndNegativeValue() {
     double[] instanceValues = {1.0, 2.0, 3.0};
     double[] minValues = {-1.0, -2.0, -3.0};
     instance = new DenseInstance(instanceValues);
     instance2 = new DenseInstance(minValues);
     Instance result = instance.minus(instance2);
     Double[] resultValues = result.toArray();
     Assert.assertArrayEquals(new Double[]{2.0, 0.0, 0.0}, resultValues, DELTA);
 }

 @Test
 public void testDivideWithInstanceAndZero() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     AbstractInstance min = new DenseInstance(new double[]{0.0, 0.0, 0.0});
     try {
         instance.divide(min);
     } catch (ArithmeticException e) {
         Assert.assertEquals(" / by zero", e.getMessage());
     }
 }

 @Test
 public void testDivideWithInstanceAndNegativeValue() {
     double[] instanceValues = {1.0, 2.0, 3.0};
     double[] minValues = {-1.0, -2.0, -3.0};
     instance = new DenseInstance(instanceValues);
     instance2 = new DenseInstance(minValues);
     Instance result = instance.divide(instance2);
     Double[] resultValues = result.toArray();
     Assert.assertArrayEquals(new Double[]{2.0, 0.5, 0.6666666666666666}, resultValues, DELTA);
 }

 @Test
 public void testAddWithZero() {
     instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     Instance result = instance.add(0);
     Double[] resultValues = result.toArray();
     Assert.assertArrayEquals(new Double[]{1.0, 2.0, 3.0}, resultValues, DELTA);
 }

 @Test
 public void testAddWithInstance() {
     double[] instanceValues = {1.0, 2.0, 3.0};
     double[] minValues = {0.5, 1.0, 1.5};
     instance = new DenseInstance(instanceValues);
     instance2 = new DenseInstance(minValues);
     Instance result = instance.add(instance2);
     Double[] resultValues = result.toArray();
     Assert.assertArrayEquals(new Double[]{1.5, 3.0, 4.5}, resultValues, DELTA);
 }

 @Test
 public void testAddAndMinusWithInstance() {
     double[] instanceValues = {1.0, 2.0, 3.0};
     double[] minValues = {0.5, 1.0, 1.5};
     instance = new DenseInstance(instanceValues);
     instance2 = new DenseInstance(minValues);
     Instance resultAdd = instance.add(instance2);
     Instance resultSubtract = instance.minus(resultAdd);
     Double[] resultValuesAdd = resultAdd.toArray();
     Double[] resultValuesSubtract = resultSubtract.toArray();
     Assert.assertArrayEquals(new Double[]{1.5, 3.0, 4.5}, resultValuesAdd);
     Assert.assertArrayEquals(new Double[]{0.0, 1.0, 2.5}, resultValuesSubtract);
 }

 @Test
 public void testSqrt() {
     double[] instanceValues = {1.0, 4.0, 9.0};
     instance = new DenseInstance(instanceValues);
     Instance result = instance.sqrt();
     Double[] resultValues = result.toArray();
     Assert.assertArrayEquals(new Double[]{1.0, 2.0, 3.0}, resultValues, DELTA);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeValue() {
     instance = new DenseInstance(new double[]{-1.0, 4.0, 9.0});
     instance.sqrt();
 }
}