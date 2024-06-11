package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance createInstance(int size, Object classValue) {
  Map<Integer, Double> map = new HashMap<Integer, Double>();
  for (int i = 0; i < size; i++) {
   map.put(i, (double) i);
  }
  return new DenseInstance(map, classValue);
 }

 @Test
 public void testGetID() {
  AbstractInstance instance = new DenseInstance(new HashMap<Integer, Double>(), "TestClass");
  assertEquals(0, instance.getID());

  AbstractInstance instance2 = new DenseInstance(new HashMap<Integer, Double>(), "TestClass");
  instance2.setID(1);
  assertEquals(1, instance2.getID());
 }

 @Test
 public void testIterator() {
  AbstractInstance instance = createInstance(5, "TestClass");

  double[] expected = { 0.0, 1.0, 2.0, 3.0, 4.0 };
  Double actual[] = instance.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testEmptyConstructor() {
  AbstractInstance instance = new AbstractInstance();
  assertNull(instance.classValue);
  assertEquals(-1, instance.getID());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullClassValue() {
  new AbstractInstance(null);
 }

 @Test
 public void testGetClassValue() {
  AbstractInstance instance = createInstance(5, "TestClass");
  assertEquals("TestClass", instance.getClassValue());
 }

 @Test
 public void testSetClassValue() {
  AbstractInstance instance = createInstance(5, "TestClass");
  instance.setClassValue("NewTestClass");
  assertEquals("NewTestClass", instance.getClassValue());
 }

 @Test
 public void testMinusWithInstance() {
  AbstractInstance instance1 = createInstance(5, "Class1");
  AbstractInstance instance2 = createInstance(5, "Class2");

  AbstractInstance result = instance1.minus(instance2);

  double[] expected = new double[5];
  Arrays.fill(expected, -1.0);

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testMinusWithDouble() {
  AbstractInstance instance = createInstance(5, "Class");

  AbstractInstance result = instance.minus(2.5);

  double[] expected = new double[5];
  Arrays.fill(expected, -2.5);

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNegativeDouble() {
  AbstractInstance instance = createInstance(5, "Class");
  instance.minus(-2.5);
 }

 @Test
 public void testDivideWithDouble() {
  AbstractInstance instance = createInstance(5, "Class");

  AbstractInstance result = instance.divide(2.5);

  double[] expected = new double[5];
  Arrays.fill(expected, 0.4);

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithZero() {
  AbstractInstance instance = createInstance(5, "Class");
  instance.divide(0);
 }

 @Test
 public void testMultiplyWithDouble() {
  AbstractInstance instance = createInstance(5, "Class");

  AbstractInstance result = instance.multiply(2.5);

  double[] expected = new double[5];
  Arrays.fill(expected, 1.25);

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testEquals() {
  AbstractInstance instance1 = createInstance(5, "Class1");
  AbstractInstance instance2 = createInstance(5, "Class2");
  AbstractInstance instance3 = instance1;

  assertFalse(instance1.equals(instance2));
  assertTrue(instance1.equals(instance3));
 }

 @Test
 public void testHashCode() {
  AbstractInstance instance1 = createInstance(5, "Class1");
  AbstractInstance instance2 = createInstance(5, "Class2");

  assertEquals(instance1.hashCode(), instance1.hashCode());
  assertNotEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testMinusWithInstanceAndMinusWithDoubleShouldReturnSameResult() {
  AbstractInstance instance = createInstance(5, "Class");

  AbstractInstance instanceMinusDouble = instance.minus(2.5);
  AbstractInstance instanceMinusInstance = instance.minus(new DenseInstance(new HashMap<Integer, Double>(), 2.5));

  assertTrue(instanceMinusDouble.equals(instanceMinusInstance));
 }

 @Test
 public void testMultiplyWithInstance() {
  AbstractInstance instance1 = createInstance(5, "Class1");
  AbstractInstance instance2 = createInstance(5, "Class2");

  AbstractInstance result = instance1.multiply(instance2);

  double[] expected = new double[5];
  Arrays.fill(expected, 0.0);

  for (int i = 0; i < 5; i++) {
   expected[i] *= instance2.get(i);
  }

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testDivideWithInstance() {
  AbstractInstance instance1 = createInstance(5, "Class1");
  AbstractInstance instance2 = createInstance(5, "Class2");

  AbstractInstance result = instance1.divide(instance2);

  double[] expected = new double[5];
  Arrays.fill(expected, 0.0);

  for (int i = 0; i < 5; i++) {
   expected[i] /= instance2.get(i);
  }

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddWithDouble() {
  AbstractInstance instance = createInstance(5, "Class");

  AbstractInstance result = instance.add(2.5);

  double[] expected = new double[5];
  Arrays.fill(expected, 2.5);

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddWithInstance() {
  AbstractInstance instance1 = createInstance(5, "Class1");
  AbstractInstance instance2 = createInstance(5, "Class2");

  AbstractInstance result = instance1.add(instance2);

  double[] expected = new double[5];
  Arrays.fill(expected, 1.0);

  for (int i = 0; i < 5; i++) {
   expected[i] += instance2.get(i);
  }

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test
 public void testSqrt() {
  AbstractInstance instance = createInstance(5, "Class");

  AbstractInstance result = instance.sqrt();

  double[] expected = new double[5];
  Arrays.fill(expected, Math.sqrt(0.0));

  Double actual[] = result.iterator().toArray(new Double[5]);
  assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeValue() {
  AbstractInstance instance = createInstance(5, "Class");
  instance.get(0).setValue(-1.0);
  instance.sqrt();
 }
}