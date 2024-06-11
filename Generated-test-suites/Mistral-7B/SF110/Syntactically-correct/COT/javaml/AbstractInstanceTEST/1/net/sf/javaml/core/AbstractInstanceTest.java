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
 Map<Integer, Double> map1 = new HashMap<>();
 map1.put(0, 1.0);
 map1.put(1, 2.0);
 instance1 = new DenseInstance(map1);

 Map<Integer, Double> map2 = new HashMap<>();
 map2.put(0, 3.0);
 map2.put(1, 4.0);
 instance2 = new DenseInstance(map2);

 Map<Integer, Double> map3 = new HashMap<>();
 map3.put(0, 5.0);
 map3.put(1, 6.0);
 instance3 = new DenseInstance(map3);
 }

 @org.junit.Test
 public void testGetID() {
 AbstractInstance testInstance = instance1;
 assertEquals(testInstance.getID(), instance1.getID());
 }

 @org.junit.Test
 public void testGetIDDifferentInstances() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 assertNotEquals(testInstance1.getID(), testInstance2.getID());
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
 for (int i = 0; i < instance1.noAttributes(); ++i) {
 assertEquals(list.get(i), instance1.value(i));
 }
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testRemoveWithIterator() {
 AbstractInstance testInstance = instance1;
 Iterator<Double> iterator = testInstance.iterator();
 iterator.remove();
 fail("Remove method should not be supported when using the iterator.");
 }

 @org.junit.Test
 public void testConstructorWithoutClassValue() {
 AbstractInstance testInstance = new AbstractInstance();
 assertEquals(testInstance.getID(), AbstractInstance.nextID);
 ++AbstractInstance.nextID;
 assertNull(testInstance.classValue());
 }

 @org.junit.Test
 public void testConstructorWithClassValue() {
 Object classValue = new Object();
 AbstractInstance testInstance = new AbstractInstance(classValue);
 assertEquals(testInstance.getID(), AbstractInstance.nextID);
 ++AbstractInstance.nextID;
 assertEquals(testInstance.classValue(), classValue);
 }

 @org.junit.Test
 public void testClassValue() {
 AbstractInstance testInstance = instance1;
 assertNotNull(testInstance.classValue());
 }

 @org.junit.Test
 public void testSetClassValue() {
 AbstractInstance testInstance = instance1;
 Object newClassValue = new Object();
 testInstance.setClassValue(newClassValue);
 assertEquals(testInstance.classValue(), newClassValue);
 }

 @org.junit.Test
 public void testMinusWithInstance() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.minus(instance2);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(result.value(i), instance1.value(i) - instance2.value(i));
 }
 }

 @org.junit.Test
 public void testMinusWithDouble() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.minus(1.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(result.value(i), instance1.value(i) - 1.0, 0.01);
 }
 }

 @org.junit.Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
 AbstractInstance testInstance = new DenseInstance(new double[] { 1.0, 2.0 });
 Instance result = testInstance.divide(0.0);
 fail("Division by zero should throw an ArithmeticException.");
 }

 @org.junit.Test
 public void testDivide() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.divide(2.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(result.value(i), instance1.value(i) / 2.0, 0.01);
 }
 }

 @org.junit.Test
 public void testMultiplyWithInstance() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.multiply(instance2);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(result.value(i), instance1.value(i) * instance2.value(i), 0.01);
 }
 }

 @org.junit.Test
 public void testMultiplyWithDouble() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.multiply(2.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(result.value(i), instance1.value(i) * 2.0, 0.01);
 }
 }

 @org.junit.Test
 public void testHashCode() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance2;
 assertEquals(testInstance1.hashCode(), testInstance1.hashCode());
 assertEquals(testInstance1.hashCode(), instance1.hashCode());
 assertNotEquals(testInstance1.hashCode(), testInstance2.hashCode());
 assertNotEquals(testInstance1.hashCode(), instance2.hashCode());
 }

 @org.junit.Test
 public void testEquals() {
 AbstractInstance testInstance1 = instance1;
 AbstractInstance testInstance2 = instance1;
 assertTrue(testInstance1.equals(testInstance2));

 AbstractInstance testInstance3 = new DenseInstance(new HashMap<>());
 assertFalse(testInstance1.equals(testInstance3));

 AbstractInstance testInstance4 = new DenseInstance(Map.of(0, 1.0, 1, 2.0));
 assertFalse(testInstance1.equals(testInstance4));
 }

 @org.junit.Test
 public void testAddWithDouble() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.add(1.0);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(result.value(i), instance1.value(i) + 1.0, 0.01);
 }
 }

 @org.junit.Test
 public void testAddWithInstance() {
 AbstractInstance testInstance = instance1;
 Instance result = testInstance.add(instance2);
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(result.value(i), instance1.value(i) + instance2.value(i), 0.01);
 }
 }

 @org.junit.Test
 public void testSqrt() {
 AbstractInstance testInstance = new DenseInstance(new double[] { 4.0, 9.0 });
 Instance result = testInstance.sqrt();
 for (int i = 0; i < result.noAttributes(); ++i) {
 assertEquals(Math.sqrt(testInstance.value(i)), result.value(i), 0.01);
 }
 }
}