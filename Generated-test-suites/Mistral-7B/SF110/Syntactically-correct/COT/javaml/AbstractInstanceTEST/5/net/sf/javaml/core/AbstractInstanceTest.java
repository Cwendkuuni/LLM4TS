package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private AbstractInstance createInstance(int noAttributes, Object classValue) {
  return new DenseInstance(new double[noAttributes], classValue);
 }

 @Test
 public void testGetID() {
  AbstractInstance instance = new DenseInstance(new double[0], null);
  assertEquals(0, instance.getID());
  instance = new DenseInstance(new double[]{1.0}, null);
  assertEquals(1, instance.getID());
 }

 @Test
 public void testIterator() {
  AbstractInstance instance = createInstance(5, null);
  Double[] expected = {0.0, 1.0, 2.0, 3.0, 4.0};
  Iterator<Double> iterator = instance.iterator();
  assertArrayEquals(expected, iterator.toArray(new Double[0]));
 }

 @Test
 public void testConstructorWithNoArgument() {
  AbstractInstance instance = new AbstractInstance();
  assertEquals(0, instance.getID());
  assertNull(instance.classValue());
 }

 @Test
 public void testConstructorWithClassValue() {
  AbstractInstance instance = new AbstractInstance("testClassValue");
  assertEquals(1, instance.getID());
  assertEquals("testClassValue", instance.classValue().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullInstance() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.minus(null);
 }

 @Test
 public void testMinusWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0}, null);
  AbstractInstance result = instance1.minus(instance2);
  Double[] expected = {-2.0, -2.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithDoubleLessThanZero() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.minus(-1.0);
 }

 @Test
 public void testMinusWithDouble() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  AbstractInstance result = instance.minus(2.0);
  Double[] expected = {-1.0, -1.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullInstance() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.divide(null);
 }

 @Test
 public void testDivideWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0}, null);
  AbstractInstance result = instance1.divide(instance2);
  Double[] expected = {0.3333333333333333, 0.5};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual, 0.00001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithZero() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.divide(0.0);
 }

 @Test
 public void testMultiplyWithDouble() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  AbstractInstance result = instance.multiply(2.0);
  Double[] expected = {2.0, 2.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testMultiplyWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0}, null);
  AbstractInstance result = instance1.multiply(instance2);
  Double[] expected = {2.0, 6.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHashCodeWithNullObject() {
  AbstractInstance instance = new DenseInstance(new double[0], null);
  instance.hashCode();
  assertNull(instance);
 }

 @Test
 public void testEqualsWithSameObject() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = instance1;
  assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testEqualsWithNullObject() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = null;
  assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testEqualsWithDifferentClass() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  Object instance2 = new Object();
  assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testEqualsWithSameID() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0}, null);
  assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testEqualsWithDifferentID() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 2.0}, null);
  assertFalse(instance1.equals(instance2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubtractWithNullInstance() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.subtract(null);
 }

 @Test
 public void testSubtractWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0}, null);
  AbstractInstance result = instance1.subtract(instance2);
  Double[] expected = {-2.0, -2.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testSubtractWithDouble() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  AbstractInstance result = instance.subtract(2.0);
  Double[] expected = {-1.0, -1.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithDoubleLessThanZero() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.divide(-2.0);
 }

 @Test
 public void testDivideWithZeroDenominator() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.divide(0.0);
 }

 @Test
 public void testAddWithNullInstance() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.add(null);
 }

 @Test
 public void testAddWithInstance() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0}, null);
  AbstractInstance result = instance1.add(instance2);
  Double[] expected = {4.0, 6.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddWithDouble() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  AbstractInstance result = instance.add(2.0);
  Double[] expected = {3.0, 3.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testSqrtWithNegativeNumber() {
  AbstractInstance instance = new DenseInstance(new double[]{-1.0}, null);
  AbstractInstance result = instance.sqrt();
  Double[] expected = {-1.0, -1.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual, 0.00001);
 }

 @Test
 public void testSqrtWithZero() {
  AbstractInstance instance = new DenseInstance(new double[]{0.0}, null);
  AbstractInstance result = instance.sqrt();
  Double[] expected = {0.0, 0.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testSqrtWithPositiveNumber() {
  AbstractInstance instance = new DenseInstance(new double[]{4.0}, null);
  AbstractInstance result = instance.sqrt();
  Double[] expected = {2.0, 2.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual, 0.00001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNegativeNumber() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.minus(-2.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullMap() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  instance.divide((Map<String, Double>)null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithEmptyMap() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  Map<String, Double> emptyMap = new HashMap<String, Double>();
  instance.divide(emptyMap);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithEmptyMap() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  Map<String, Double> emptyMap = new HashMap<String, Double>();
  instance.minus(emptyMap);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullKey() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  Map<String, Double> map = new HashMap<String, Double>() {{
    put("key", 1.0);
    put(null, 2.0);
  }};
  instance.minus(map);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNonExistentKey() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  Map<String, Double> map = new HashMap<String, Double>() {{
    put("key", 1.0);
  }};
  instance.minus(map);
 }

 @Test
 public void testMinusWithKeyExists() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0}, null);
  Map<String, Double> map = new HashMap<String, Double>() {{
    put("key", 1.0);
  }};
  AbstractInstance result = instance.minus(map);
  Double[] expected = {0.0, 1.0};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullKey() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  Map<String, Double> map = new HashMap<String, Double>() {{
    put("key", 1.0);
    put(null, 2.0);
  }};
  instance.divide(map);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNonExistentKey() {
  AbstractInstance instance = new DenseInstance(new double[]{1.0}, null);
  Map<String, Double> map = new HashMap<String, Double>() {{
    put("key", 1.0);
  }};
  instance.divide(map);
 }

 @Test
 public void testDivideWithKeyExists() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0}, null);
  Map<String, Double> map = new HashMap<String, Double>() {{
    put("key", 2.0);
  }};
  AbstractInstance result = instance1.divide(map, instance2);
  Double[] expected = {0.5, 0.5};
  Double[] actual = result.toArray();
  assertArrayEquals(expected, actual, 0.00001);
 }

 @Test
 public void testDivideWithDifferentNumberOfAttributes() {
  AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, null);
  AbstractInstance instance2 = new DenseInstance(new double[]{1.0}, null);
  Map<String, Double> map = new HashMap<String, Double>() {{
    put("key", 2.0);
  }};
  try {
    instance1.divide(map, instance2);
    fail("IllegalArgumentException expected");
  } catch (IllegalArgumentException e) {
    // Expected
  }
 }
}