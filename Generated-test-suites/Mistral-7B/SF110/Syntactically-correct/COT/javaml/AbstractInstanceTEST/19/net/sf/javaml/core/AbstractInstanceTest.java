package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance createInstance(int noAttributes, Object classValue) {
  return new DenseInstance(new double[noAttributes], classValue);
 }

 @Test
 public void testGetID() {
  AbstractInstance instance = createInstance(3, "Class1");
  assertEquals(AbstractInstance.nextID, instance.getID());
  assertEquals(AbstractInstance.nextID + 1, createInstance(3, "Class1").getID());
 }

 @Test
 public void testIterator() {
  AbstractInstance instance = createInstance(3, "Class1");
  instance.put(0, 1.0);
  instance.put(1, 2.0);
  instance.put(2, 3.0);
  Double value;
  Iterator<Double> iterator = instance.iterator();
  assertEquals(3, iterator.next());
  assertEquals(2.0, iterator.next());
  assertEquals(1.0, iterator.next());
  assertNull(iterator.next());
 }

 @Test
 public void testEmptyConstructor() {
  AbstractInstance instance = new AbstractInstance();
  assertEquals(0, instance.getID());
  assertNull(instance.classValue());
 }

 @Test
 public void testConstructorWithClassValue() {
  AbstractInstance instance = new AbstractInstance("Class1");
  assertEquals(AbstractInstance.nextID, instance.getID());
  assertEquals("Class1", instance.classValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullInstance() {
  AbstractInstance instance = new AbstractInstance();
  assertNull(instance.minus(null));
 }

 @Test
 public void testMinusWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  AbstractInstance instance2 = new DenseInstance(new double[] { 0.5, 1.5, 2.5 }, null);
  AbstractInstance result = instance1.minus(instance2);
  assertEquals(3, result.noAttributes());
  assertEquals(-0.5, result.value(0), 0.001);
  assertEquals(-1.5, result.value(1), 0.001);
  assertEquals(-1.5, result.value(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithDoubleAndNullInstance() {
  AbstractInstance instance = new AbstractInstance();
  assertNull(instance.minus(null));
  assertNull(instance.minus(1.0));
 }

 @Test
 public void testMinusWithDouble() {
  AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  AbstractInstance result = instance.minus(1.0);
  assertEquals(3, result.noAttributes());
  assertEquals(0.0, result.value(0), 0.001);
  assertEquals(1.0, result.value(1), 0.001);
  assertEquals(2.0, result.value(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullInstance() {
  AbstractInstance instance = new AbstractInstance();
  assertNull(instance.divide(null));
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
  AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  assertNull(instance.divide(0));
 }

 @Test
 public void testDivideWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  AbstractInstance instance2 = new DenseInstance(new double[] { 2.0, 3.0, 4.0 }, null);
  AbstractInstance result = instance1.divide(instance2);
  assertEquals(3, result.noAttributes());
  assertEquals(0.5, result.value(0), 0.001);
  assertEquals(0.6666666666666666, result.value(1), 0.001);
  assertEquals(0.75, result.value(2), 0.001);
 }

 @Test
 public void testMultiplyWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  AbstractInstance instance2 = new DenseInstance(new double[] { 2.0, 3.0, 4.0 }, null);
  AbstractInstance result = instance1.multiply(instance2);
  assertEquals(3, result.noAttributes());
  assertEquals(2.0, result.value(0), 0.001);
  assertEquals(6.0, result.value(1), 0.001);
  assertEquals(12.0, result.value(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNullInstance() {
  AbstractInstance instance = new AbstractInstance();
  AbstractInstance result = instance.multiply(null);
  assertNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithDoubleAndNullInstance() {
  AbstractInstance instance = new AbstractInstance();
  assertNull(instance.multiply(null));
  assertNull(instance.multiply(1.0));
 }

 @Test
 public void testMultiplyWithDouble() {
  AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  AbstractInstance result = instance.multiply(2.0);
  assertEquals(3, result.noAttributes());
  assertEquals(2.0, result.value(0), 0.001);
  assertEquals(4.0, result.value(1), 0.001);
  assertEquals(6.0, result.value(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithDoubleAndNullInstance() {
  AbstractInstance instance = new AbstractInstance();
  assertNull(instance.divide(null));
  assertNull(instance.divide(1.0));
 }

 @Test
 public void testDivideWithDouble() {
  AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  AbstractInstance result = instance.divide(2.0);
  assertEquals(3, result.noAttributes());
  assertEquals(0.5, result.value(0), 0.001);
  assertEquals(1.0, result.value(1), 0.001);
  assertEquals(1.5, result.value(2), 0.001);
 }

 @Test
 public void testAddWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  AbstractInstance instance2 = new DenseInstance(new double[] { 0.5, 1.5, 2.5 }, null);
  AbstractInstance result = instance1.add(instance2);
  assertEquals(3, result.noAttributes());
  assertEquals(1.5, result.value(0), 0.001);
  assertEquals(3.5, result.value(1), 0.001);
  assertEquals(5.5, result.value(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNullInstance() {
  AbstractInstance instance = new AbstractInstance();
  assertNull(instance.add(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithDoubleAndNullInstance() {
  AbstractInstance instance = new AbstractInstance();
  assertNull(instance.add(null));
  assertNull(instance.add(1.0));
 }

 @Test
 public void testAddWithDouble() {
  AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, null);
  AbstractInstance result = instance.add(1.0);
  assertEquals(3, result.noAttributes());
  assertEquals(2.0, result.value(0), 0.001);
  assertEquals(3.0, result.value(1), 0.001);
  assertEquals(4.0, result.value(2), 0.001);
 }

 @Test
 public void testSqrt() {
  AbstractInstance instance = new DenseInstance(new double[] { 1.0, 4.0, 9.0 }, null);
  AbstractInstance result = instance.sqrt();
  assertEquals(3, result.noAttributes());
  assertEquals(1.0, result.value(0), 0.001);
  assertEquals(2.0, result.value(1), 0.001);
  assertEquals(3.0, result.value(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeInstance() {
  AbstractInstance instance = new DenseInstance(new double[] { -1.0, -4.0, -9.0 }, null);
  assertNull(instance.sqrt());
 }
}