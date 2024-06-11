package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;
 private AbstractInstance instance5;

 @Before
 public void setUp() {
 // Create instances with different IDs
 instance1 = new AbstractInstance();
 instance2 = new AbstractInstance();
 instance3 = new AbstractInstance(new Object());
 instance4 = new AbstractInstance(new Object(), 1.0);
 instance5 = new AbstractInstance(new Object(), 2.0);
 }

 @Test
 public void testGetID() {
 // Test getID() method
 Assert.assertEquals(instance1.getID(), 0);
 Assert.assertEquals(instance2.getID(), 1);
 Assert.assertEquals(instance3.getID(), 2);
 Assert.assertEquals(instance4.getID(), 3);
 Assert.assertEquals(instance5.getID(), 4);
 }

 @Test
 public void testIterator() {
 // Test iterator() method
 Map<String, Double> map1 = new HashMap<>();
 map1.put("Attribute1", 1.0);
 map1.put("Attribute2", 2.0);
 instance1 = new AbstractInstance(map1);

 Map<String, Double> map2 = new HashMap<>();
 map2.put("Attribute1", 3.0);
 map2.put("Attribute2", 4.0);
 instance2 = new AbstractInstance(map2);

 Iterator<Double> iterator = instance1.iterator();

 // Test hasNext() method
 Assert.assertTrue(iterator.hasNext());

 // Test next() method
 Double value1 = iterator.next();
 Assert.assertEquals(value1, 1.0, 0.0);

 // Test hasNext() and next() methods
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(iterator.next(), 2.0, 0.0);

 // Test hasNext() method after next() method call
 Assert.assertFalse(iterator.hasNext());

 // Test next() method after hasNext() method call returns false
 Assert.assertNull(iterator.next());
 }

 @Test
 public void testConstruction() {
 // Test AbstractInstance() constructor
 AbstractInstance instance6 = new AbstractInstance();
 Assert.assertEquals(instance6.getID(), 5);
 Assert.assertNull(instance6.classValue());

 // Test AbstractInstance(Object classValue) constructor
 AbstractInstance instance7 = new AbstractInstance(new Object());
 Assert.assertEquals(instance7.getID(), 6);
 Assert.assertEquals(instance7.classValue(), instance7.classValue());
 }

 @Test
 public void testClassValue() {
 // Test classValue() method
 Map<String, Double> map3 = new HashMap<>();
 map3.put("Attribute1", 1.0);
 map3.put("Attribute2", 2.0);
 instance1 = new AbstractInstance(map3);
 instance1.setClassValue("TestClass");

 Assert.assertEquals(instance1.classValue(), "TestClass");
 }

 @Test
 public void testSetClassValue() {
 // Test setClassValue(Object classValue) method
 Map<String, Double> map4 = new HashMap<>();
 map4.put("Attribute1", 1.0);
 map4.put("Attribute2", 2.0);
 instance1 = new AbstractInstance(map4);

 instance1.setClassValue("TestClass");
 Assert.assertEquals(instance1.classValue(), "TestClass");
 }

 @Test
 public void testMinus() {
 // Test minus(Instance min) method
 instance2.put(0, 3.0);
 instance2.put(1, 4.0);

 AbstractInstance result1 = instance1.minus(instance2);

 Map<String, Double> map5 = new HashMap<>();
 map5.put("Attribute1", -2.0);
 map5.put("Attribute2", -1.0);
 AbstractInstance expected1 = new AbstractInstance(map5);

 Assert.assertEquals(result1, expected1);

 // Test minus(double min) method
 instance1.put(0, 5.0);
 instance1.put(1, 6.0);

 AbstractInstance result2 = instance1.minus(2.0);

 Map<String, Double> map6 = new HashMap<>();
 map6.put("Attribute1", 3.0);
 map6.put("Attribute2", 4.0);
 AbstractInstance expected2 = new AbstractInstance(map6);

 Assert.assertEquals(result2, expected2);
 }

 @Test
 public void testDivide() {
 // Test divide(double min) method
 instance1.put(0, 6.0);
 instance1.put(1, 8.0);

 AbstractInstance result1 = instance1.divide(2.0);

 Map<String, Double> map7 = new HashMap<>();
 map7.put("Attribute1", 3.0);
 map7.put("Attribute2", 4.0);
 AbstractInstance expected1 = new AbstractInstance(map7);

 Assert.assertEquals(result1, expected1);
 }

 @Test
 public void testMultiply() {
 // Test multiply(double value) method
 instance1.put(0, 2.0);
 instance1.put(1, 3.0);

 AbstractInstance result1 = instance1.multiply(3.0);

 Map<String, Double> map8 = new HashMap<>();
 map8.put("Attribute1", 6.0);
 map8.put("Attribute2", 9.0);
 AbstractInstance expected1 = new AbstractInstance(map8);

 Assert.assertEquals(result1, expected1);
 }

 @Test
 public void testEquals() {
 // Test equals(Object obj) method
 AbstractInstance instance8 = new AbstractInstance();
 instance8.put(0, 2.0);
 instance8.put(1, 3.0);

 // Same instance
 Assert.assertTrue(instance1.equals(instance1));

 // Different instance with the same values
 instance2.put(0, 2.0);
 instance2.put(1, 3.0);
 Assert.assertTrue(instance1.equals(instance2));

 // Different instance with different values
 instance2.put(0, 4.0);
 instance2.put(1, 5.0);
 Assert.assertFalse(instance1.equals(instance2));

 // Different class
 Assert.assertFalse(instance1.equals(new Object()));

 // Null
 Assert.assertFalse(instance1.equals(null));

 // Same instance but different ID
 instance3.put(0, 2.0);
 instance3.put(1, 3.0);
 instance3.ID = instance1.getID();
 Assert.assertTrue(instance1.equals(instance3));
 }

 @Test
 public void testHashCode() {
 // Test hashCode() method
 AbstractInstance instance9 = new AbstractInstance();
 instance9.put(0, 2.0);
 instance9.put(1, 3.0);
 int hashCode1 = instance1.hashCode();
 int hashCode2 = instance9.hashCode();

 // Same instance
 Assert.assertEquals(instance1.hashCode(), hashCode1);
 Assert.assertEquals(hashCode1, hashCode1);

 // Different instances
 Assert.assertNotSame(instance1.hashCode(), hashCode2);
 }

 @Test
 public void testMinusInstance() {
 // Test minus(Instance min) method
 instance2.put(0, 3.0);
 instance2.put(1, 4.0);

 AbstractInstance result1 = instance1.minus(instance2);

 Map<String, Double> map9 = new HashMap<>();
 map9.put("Attribute1", -2.0);
 map9.put("Attribute2", -1.0);
 AbstractInstance expected1 = new AbstractInstance(map9);

 Assert.assertEquals(result1, expected1);
 }

 @Test
 public void testDivideInstance() {
 // Test divide(Instance min) method
 instance2.put(0, 3.0);
 instance2.put(1, 4.0);

 AbstractInstance result1 = instance1.divide(instance2);

 Map<String, Double> map10 = new HashMap<>();
 map10.put("Attribute1", 0.5);
 map10.put("Attribute2", 0.6666666666666666);
 AbstractInstance expected1 = new AbstractInstance(map10);

 Assert.assertEquals(result1, expected1);
 }

 @Test
 public void testMultiplyInstance() {
 // Test multiply(Instance value) method
 instance2.put(0, 3.0);
 instance2.put(1, 4.0);

 AbstractInstance result1 = instance1.multiply(instance2);

 Map<String, Double> map11 = new HashMap<>();
 map11.put("Attribute1", 6.0);
 map11.put("Attribute2", 12.0);
 AbstractInstance expected1 = new AbstractInstance(map11);

 Assert.assertEquals(result1, expected1);
 }

 @Test
 public void testAddInstance() {
 // Test add(Instance min) method
 instance2.put(0, 3.0);
 instance2.put(1, 4.0);

 AbstractInstance result1 = instance1.add(instance2);

 Map<String, Double> map12 = new HashMap<>();
 map12.put("Attribute1", 5.0);
 map12.put("Attribute2", 8.0);
 AbstractInstance expected1 = new AbstractInstance(map12);

 Assert.assertEquals(result1, expected1);
 }

 @Test
 public void testAddDouble() {
 // Test add(double min) method
 instance1.put(0, 2.0);
 instance1.put(1, 3.0);

 AbstractInstance result1 = instance1.add(1.0);

 Map<String, Double> map13 = new HashMap<>();
 map13.put("Attribute1", 3.0);
 map13.put("Attribute2", 4.0);
 AbstractInstance expected1 = new AbstractInstance(map13);

 Assert.assertEquals(result1, expected1);
 }

 @Test
 public void testSqrt() {
 // Test sqrt() method
 instance1.put(0, 16.0);
 instance1.put(1, 25.0);

 AbstractInstance result1 = instance1.sqrt();

 Map<String, Double> map14 = new HashMap<>();
 map14.put("Attribute1", 4.0);
 map14.put("Attribute2", 5.0);
 AbstractInstance expected1 = new AbstractInstance(map14);

 Assert.assertEquals(result1, expected1);
 }
}