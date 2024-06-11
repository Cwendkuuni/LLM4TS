package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AbstractInstanceTest {

 @Test
 public void testInstanceConstructor() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance("TestClass");

 assertEquals(0, instance1.getID());
 assertEquals(null, instance1.classValue());

 assertEquals(-1, instance2.getID());
 assertEquals("TestClass", instance2.classValue());
 }

 @Test
 public void testGetID() {
 AbstractInstance instance = new AbstractInstance();
 instance.setID(1);

 assertEquals(1, instance.getID());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorRemove() {
 AbstractInstance instance = new AbstractInstance();
 Iterator<Double> iterator = instance.iterator();
 iterator.next();
 iterator.remove();
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 instance1.put(0.0);
 instance1.put(1.0);

 AbstractInstance instance2 = new AbstractInstance();
 instance2.put(0.5);
 instance2.put(1.5);

 AbstractInstance result = instance1.minus(instance2);

 assertEquals(2, result.size());

 Double[] expectedValues = {-0.5, -0.5};
 assertArrayEquals(expectedValues, result.toArray(new Double[2]));
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance = new AbstractInstance();
 instance.put(0.5);

 AbstractInstance result = instance.minus(1.0);

 assertEquals(1, result.size());
 assertEquals(-0.5, result.get(0).doubleValue(), 0.001);
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 instance1.put(2.0);
 instance1.put(3.0);

 AbstractInstance instance2 = new AbstractInstance();
 instance2.put(0.5);
 instance2.put(0.5);

 AbstractInstance result = instance1.divide(instance2);

 assertEquals(2, result.size());

 Double[] expectedValues = {4.0, 6.0};
 assertArrayEquals(expectedValues, result.toArray(new Double[2]));
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideWithZero() {
 AbstractInstance instance = new AbstractInstance();
 instance.put(2.0);

 AbstractInstance zeroInstance = new AbstractInstance();
 zeroInstance.put(0.0);

 instance.divide(zeroInstance);
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 instance1.put(2.0);
 instance1.put(3.0);

 AbstractInstance instance2 = new AbstractInstance();
 instance2.put(0.5);
 instance2.put(0.5);

 AbstractInstance result = instance1.multiply(instance2);

 assertEquals(2, result.size());

 Double[] expectedValues = {1.0, 1.5};
 assertArrayEquals(expectedValues, result.toArray(new Double[2]));
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = new AbstractInstance();
 instance.put(2.0);

 AbstractInstance result = instance.multiply(3.0);

 assertEquals(1, result.size());
 assertEquals(6.0, result.get(0).doubleValue(), 0.001);
 }

 @Test
 public void testHashCodeAndEquals() {
 AbstractInstance instance1 = new AbstractInstance();
 instance1.put(1.0);
 instance1.put(2.0);

 AbstractInstance instance2 = new AbstractInstance();
 instance2.put(1.0);
 instance2.put(2.0);

 AbstractInstance instance3 = new AbstractInstance();
 instance3.put(1.0);
 instance3.put(3.0);

 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertEquals(instance1, instance1);
 assertEquals(instance1, instance1);
 assertEquals(instance1, instance2);
 assertNotEquals(instance1, instance3);
 }

 @Test
 public void testMinusWithDoubleAndAddWithInstance() {
 AbstractInstance instance = new AbstractInstance();
 instance.put(2.0);

 AbstractInstance instance2 = new AbstractInstance();
 instance2.put(1.0);
 instance2.put(1.0);

 AbstractInstance result = instance.minus(instance2).add(instance2);

 assertEquals(1, result.size());
 assertEquals(1.0, result.get(0).doubleValue(), 0.001);
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 instance1.put(1.0);
 instance1.put(2.0);

 AbstractInstance instance2 = new AbstractInstance();
 instance2.put(1.0);
 instance2.put(1.0);

 AbstractInstance result = instance1.add(instance2);

 assertEquals(2, result.size());

 Double[] expectedValues = {2.0, 3.0};
 assertArrayEquals(expectedValues, result.toArray(new Double[2]));
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance instance = new AbstractInstance();
 instance.put(2.0);

 AbstractInstance result = instance.add(1.0);

 assertEquals(1, result.size());
 assertEquals(3.0, result.get(0).doubleValue(), 0.001);
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new AbstractInstance();
 instance.put(9.0);

 AbstractInstance result = instance.sqrt();

 assertEquals(1, result.size());
 assertEquals(3.0, result.get(0).doubleValue(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrtWithNegativeNumber() {
 AbstractInstance instance = new AbstractInstance();
 instance.put(-1.0);

 instance.sqrt();
 }

 @Test
 public void testSetClassValue() {
 AbstractInstance instance = new AbstractInstance();
 String testClass = "TestClass";

 instance.setClassValue(testClass);
 assertEquals(testClass, instance.classValue());
 }

 @Test
 public void testGetClassValue() {
 AbstractInstance instance = new AbstractInstance();
 String testClass = "TestClass";
 instance.setClassValue(testClass);

 assertEquals(testClass, instance.classValue());
 }
}