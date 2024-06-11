package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;

 @org.junit.Before
 public void setUp() {
 Map<String, Double> map1 = new HashMap<>();
 map1.put("Attribute1", 1.0);
 map1.put("Attribute2", 2.0);
 instance1 = new AbstractInstance(map1);

 Map<String, Double> map2 = new HashMap<>();
 map2.put("Attribute1", 3.0);
 map2.put("Attribute2", 4.0);
 instance2 = new AbstractInstance(map2);

 Map<String, Double> map3 = new HashMap<>();
 map3.put("Attribute1", 5.0);
 map3.put("Attribute2", 6.0);
 instance3 = new AbstractInstance(map3);
 }

 @org.junit.Test
 public void testGetID() {
 AbstractInstance testInstance = instance1;
 assertEquals(testInstance.getID(), instance1.getID());
 }

 @org.junit.Test
 public void testIterator() {
 AbstractInstance testInstance = instance1;
 Iterator<Double> iterator = testInstance.iterator();
 List<Double> list = new ArrayList<>();
 while (iterator.hasNext()) {
 list.add(iterator.next());
 }
 assertEquals(list.size(), instance1.noAttributes());
 }

 @org.junit.Test
 public void testConstructorNoArgs() {
 AbstractInstance testInstance = new AbstractInstance();
 assertNotNull(testInstance);
 }

 @org.junit.Test
 public void testConstructorWithArgs() {
 AbstractInstance testInstance = new AbstractInstance(instance1.classValue);
 assertEquals(testInstance.getClassValue(), instance1.classValue);
 }

 @org.junit.Test
 public void testClassValue() {
 AbstractInstance testInstance = instance1;
 assertEquals(testInstance.classValue(), instance1.classValue());
 }

 @org.junit.Test
 public void testSetClassValue() {
 AbstractInstance testInstance = instance1;
 testInstance.setClassValue("Test Class");
 assertEquals("Test Class", testInstance.classValue());
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testMinusWithNullInstance() {
 AbstractInstance testInstance = instance1;
 Instance min = null;
 testInstance.minus(min);
 }

 @org.junit.Test
 public void testMinusWithInstance() {
 AbstractInstance testInstance = instance1;
 Instance min = instance2;
 Instance result = testInstance.minus(min);
 Instance expected = new DenseInstance(new double[testInstance.noAttributes()]);
 for (int i = 0; i < testInstance.noAttributes(); ++i) {
 expected.put(i, testInstance.value(i) - min.value(i));
 }
 assertEquals(expected, result);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testMinusWithDoubleAndNullIndex() {
 AbstractInstance testInstance = instance1;
 testInstance.minus((double) 0.0, null);
 }

 @org.junit.Test
 public void testMinusWithDouble() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.minus((double) 2.0);
 Instance expected = new DenseInstance(new double[testInstance.noAttributes()]);
 for (int i = 0; i < testInstance.noAttributes(); ++i) {
 expected.put(i, testInstance.value(i) - 2.0);
 }
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testDivideWithDouble() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.divide((double) 2.0);
 Instance expected = new DenseInstance(new double[testInstance.noAttributes()]);
 for (int i = 0; i < testInstance.noAttributes(); ++i) {
 expected.put(i, testInstance.value(i) / 2.0);
 }
 assertEquals(expected, result);
 }

 @org.junit.Test(expected = ArithmeticException.class)
 public void testDivideWithZero() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.divide((double) 0.0);
 }

 @org.junit.Test
 public void testMultiplyWithDouble() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.multiply((double) 2.0);
 Instance expected = new DenseInstance(new double[testInstance.noAttributes()]);
 for (int i = 0; i < testInstance.noAttributes(); ++i) {
 expected.put(i, testInstance.value(i) * 2.0);
 }
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testEqualsWithSameInstance() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = testInstance1;
 assertTrue(testInstance1.equals(testInstance2));
 }

 @org.junit.Test
 public void testEqualsWithDifferentInstance() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 assertFalse(testInstance1.equals(testInstance2));
 }

 @org.junit.Test
 public void testEqualsWithNullObject() {
 AbstractInstance testInstance1 = instance1;
 assertFalse(testInstance1.equals(null));
 }

 @org.junit.Test
 public void testEqualsWithDifferentClass() {
 AbstractInstance testInstance1 = instance1;
 Object testInstance2 = new Object();
 assertFalse(testInstance1.equals(testInstance2));
 }

 @org.junit.Test
 public void testHashCode() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 assertNotSame(testInstance1.hashCode(), testInstance2.hashCode());
 }

 @org.junit.Test
 public void testMultiplyWithInstance() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 Instance result = testInstance1.multiply(testInstance2);
 Instance expected = new DenseInstance(new double[testInstance1.noAttributes()]);
 for (int i = 0; i < testInstance1.noAttributes(); ++i) {
 expected.put(i, testInstance1.value(i) * testInstance2.value(i));
 }
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testDivideWithInstance() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 Instance result = testInstance1.divide(testInstance2);
 Instance expected = new DenseInstance(new double[testInstance1.noAttributes()]);
 for (int i = 0; i < testInstance1.noAttributes(); ++i) {
 expected.put(i, testInstance1.value(i) / testInstance2.value(i));
 }
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testAddWithDouble() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.add((double) 2.0);
 Instance expected = new DenseInstance(new double[testInstance.noAttributes()]);
 for (int i = 0; i < testInstance.noAttributes(); ++i) {
 expected.put(i, testInstance.value(i) + 2.0);
 }
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testAddWithInstance() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 Instance result = testInstance1.add(testInstance2);
 Instance expected = new DenseInstance(new double[testInstance1.noAttributes()]);
 for (int i = 0; i < testInstance1.noAttributes(); ++i) {
 expected.put(i, testInstance1.value(i) + testInstance2.value(i));
 }
 assertEquals(expected, result);
 }

 @org.junit.Test
 public void testSqrt() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.sqrt();
 for (int i = 0; i < testInstance.noAttributes(); ++i) {
 double expected = Math.sqrt(testInstance.value(i));
 assertEquals(expected, result.value(i), 0.0);
 }
 }

}