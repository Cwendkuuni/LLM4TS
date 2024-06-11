package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;

 @Test
 public void testConstructor() {
 // Test default constructor
 instance1 = new AbstractInstance();
 assertEquals(0, instance1.getID());
 assertNull(instance1.classValue());

 // Test constructor with classValue
 instance2 = new AbstractInstance("testClassValue");
 assertEquals("testClassValue", instance2.classValue());
 }

 @Test
 public void testGetID() {
 // Test getID() for default instance
 assertEquals(0, instance1.getID());

 // Test getID() for instantiated instance
 instance3 = new AbstractInstance("testClassValue");
 assertEquals(1, instance3.getID());
 }

 @Test
 public void testIterator() {
 // Test empty iterator
 Iterator<Double> iterator1 = instance1.iterator();
 assertFalse(iterator1.hasNext());

 // Test non-empty iterator
 Map<String, Double> map = new HashMap<>();
 map.put("attribute1", 1.0);
 map.put("attribute2", 2.0);
 instance4 = new AbstractInstance(map);
 Iterator<Double> iterator2 = instance4.iterator();
 assertTrue(iterator2.hasNext());
 assertEquals(1.0, iterator2.next(), 0.0);
 assertTrue(iterator2.hasNext());
 assertEquals(2.0, iterator2.next(), 0.0);
 assertFalse(iterator2.hasNext());
 }

 @Test
 public void testMinusWithInstance() {
 // Test minus() with another instance of the same size
 Map<String, Double> map1 = new HashMap<>();
 map1.put("attribute1", 2.0);
 map1.put("attribute2", 3.0);
 instance1 = new AbstractInstance(map1);

 Map<String, Double> map2 = new HashMap<>();
 map2.put("attribute1", 1.0);
 map2.put("attribute2", 2.0);
 instance2 = new AbstractInstance(map2);

 AbstractInstance result = instance1.minus(instance2);
 Map<String, Double> expectedMap = new HashMap<>();
 expectedMap.put("attribute1", 1.0);
 expectedMap.put("attribute2", 1.0);
 AbstractInstance expectedInstance = new AbstractInstance(expectedMap);

 assertEquals(expectedInstance, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMinusWithDouble_remove() {
 // Test minus() with double value and removal
 instance1 = new AbstractInstance(new HashMap<>());
 instance1.put("attribute1", 3.0);
 instance1.put("attribute2", 3.0);

 instance1.minus(2.5); // No issue
 instance1.remove("attribute1"); // No issue

 instance1.minus(-2.5); // Exception is expected
 }

 @Test
 public void testMinusWithDouble() {
 // Test minus() with double value
 instance1 = new AbstractInstance(new HashMap<>());
 instance1.put("attribute1", 3.0);
 instance1.put("attribute2", 3.0);

 AbstractInstance result = instance1.minus(1.5);
 Map<String, Double> expectedMap = new HashMap<>();
 expectedMap.put("attribute1", 1.5);
 expectedMap.put("attribute2", 1.5);
 AbstractInstance expectedInstance = new AbstractInstance(expectedMap);

 assertEquals(expectedInstance, result);
 }

 @Test
 public void testDivideWithInstance() {
 // Test divide() with another instance of the same size
 Map<String, Double> map1 = new HashMap<>();
 map1.put("attribute1", 5.0);
 map1.put("attribute2", 5.0);
 instance1 = new AbstractInstance(map1);

 Map<String, Double> map2 = new HashMap<>();
 map2.put("attribute1", 2.0);
 map2.put("attribute2", 2.0);
 instance2 = new AbstractInstance(map2);

 AbstractInstance result = instance1.divide(instance2);
 Map<String, Double> expectedMap = new HashMap<>();
 expectedMap.put("attribute1", 2.5);
 expectedMap.put("attribute2", 2.5);
 AbstractInstance expectedInstance = new AbstractInstance(expectedMap);

 assertEquals(expectedInstance, result);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideWithInstance_divideByZero() {
 // Test divide() with another instance and division by zero
 Map<String, Double> map1 = new HashMap<>();
 map1.put("attribute1", 5.0);
 map1.put("attribute2", 0.0);
 instance1 = new AbstractInstance(map1);

 Map<String, Double> map2 = new HashMap<>();
 map2.put("attribute1", 2.0);
 map2.put("attribute2", 2.0);
 instance2 = new AbstractInstance(map2);

 instance1.divide(instance2); // Exception is expected
 }

 @Test
 public void testDivideWithDouble() {
 // Test divide() with double value
 instance1 = new AbstractInstance(new HashMap<>());
 instance1.put("attribute1", 5.0);
 instance1.put("attribute2", 5.0);

 AbstractInstance result = instance1.divide(2.0);
 Map<String, Double> expectedMap = new HashMap<>();
 expectedMap.put("attribute1", 2.5);
 expectedMap.put("attribute2", 2.5);
 AbstractInstance expectedInstance = new AbstractInstance(expectedMap);

 assertEquals(expectedInstance, result);
 }

 @Test
 public void testMultiplyWithInstance() {
 // Test multiply() with another instance of the same size
 Map<String, Double> map1 = new HashMap<>();
 map1.put("attribute1", 2.0);
 map1.put("attribute2", 3.0);
 instance1 = new AbstractInstance(map1);

 Map<String, Double> map2 = new HashMap<>();
 map2.put("attribute1", 3.0);
 map2.put("attribute2", 2.0);
 instance2 = new AbstractInstance(map2);

 AbstractInstance result = instance1.multiply(instance2);
 Map<String, Double> expectedMap = new HashMap<>();
 expectedMap.put("attribute1", 6.0);
 expectedMap.put("attribute2", 6.0);
 AbstractInstance expectedInstance = new AbstractInstance(expectedMap);

 assertEquals(expectedInstance, result);
 }

 @Test
 public void testMultiplyWithDouble() {
 // Test multiply() with double value
 instance1 = new AbstractInstance(new HashMap<>());
 instance1.put("attribute1", 3.0);
 instance1.put("attribute2", 3.0);

 AbstractInstance result = instance1.multiply(2.0);
 Map<String, Double> expectedMap = new HashMap<>();
 expectedMap.put("attribute1", 6.0);
 expectedMap.put("attribute2", 6.0);
 AbstractInstance expectedInstance = new AbstractInstance(expectedMap);

 assertEquals(expectedInstance, result);
 }

 @Test
 public void testEquals() {
 // Test equals() with same instance
 AbstractInstance instance5 = new AbstractInstance(instance1);
 assertTrue(instance1.equals(instance5));

 // Test equals() with null
 assertFalse(instance1.equals(null));

 // Test equals() with different class
 assertFalse(instance1.equals(new String("test")));

 // Test equals() with different instances
 AbstractInstance instance6 = new AbstractInstance(new HashMap<>());
 instance6.put("attribute1", 3.0);
 instance6.put("attribute2", 3.0);
 assertFalse(instance1.equals(instance6));
 }

 @Test
 public void testHashCode() {
 // Test hashCode() for same instance
 AbstractInstance instance5 = new AbstractInstance(instance1);
 assertEquals(instance1.hashCode(), instance5.hashCode());

 // Test hashCode() for different instances
 AbstractInstance instance6 = new AbstractInstance(new HashMap<>());
 instance6.put("attribute1", 3.0);
 instance6.put("attribute2", 3.0);
 assertNotEquals(instance1.hashCode(), instance6.hashCode());
 }
}