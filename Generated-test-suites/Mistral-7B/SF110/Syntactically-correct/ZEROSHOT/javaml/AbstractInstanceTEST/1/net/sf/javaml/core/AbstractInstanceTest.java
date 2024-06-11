package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;

 @Before
 public void setup() {
 // Create an instance with no class value
 instance1 = new AbstractInstance();
 // Create an instance with class value
 instance2 = new AbstractInstance("TestClass");
 // Create two instances with the same attributes but different values
 instance3 = new AbstractInstance();
 instance3.put(0, 1.0);
 instance3.put(1, 2.0);
 instance4 = new AbstractInstance();
 instance4.put(0, 1.1);
 instance4.put(1, 2.1);
 }

 @Test
 public void testGetID() {
 // Test getting ID of an instance
 assertEquals(0, instance1.getID());
 // ID should be unique for each instance
 assertEquals(1, instance2.getID());
 // ID should be incremented for each new instance
 assertEquals(2, instance3.getID());
 }

 @Test
 public void testIterator() {
 // Test getting iterator for an instance
 List<Double> list1 = new ArrayList<>();
 Iterator<Double> iterator1 = instance1.iterator();
 while (iterator1.hasNext()) {
 list1.add(iterator1.next());
 }
 // Iterator should return an empty list for an instance with no attributes
 assertEquals(0, list1.size());

 // Test getting iterator for an instance with attributes
 list1.clear();
 iterator1 = instance3.iterator();
 while (iterator1.hasNext()) {
 list1.add(iterator1.next());
 }
 // List should contain the attributes of the instance
 assertEquals(2, list1.size());
 assertEquals(1.0, list1.get(0), 0.001);
 assertEquals(2.0, list1.get(1), 0.001);
 }

 @Test
 public void testMinus() {
 // Test subtracting a double value from an instance
 instance1.put(0, 3.0);
 instance1.put(1, 4.0);
 AbstractInstance result1 = instance1.minus(1.5);
 List<Double> list1_1 = new ArrayList<>();
 Iterator<Double> iterator1_1 = result1.iterator();
 while (iterator1_1.hasNext()) {
 list1_1.add(iterator1_1.next());
 }
 // Result should contain the attributes of the instance with the given value subtracted
 assertEquals(2, list1_1.size());
 assertEquals(1.5, list1_1.get(0), 0.001);
 assertEquals(2.5, list1_1.get(1), 0.001);

 // Test subtracting an instance from an instance
 AbstractInstance result2 = instance3.minus(instance4);
 List<Double> list2 = new ArrayList<>();
 Iterator<Double> iterator2 = result2.iterator();
 while (iterator2.hasNext()) {
 list2.add(iterator2.next());
 }
 // Result should contain the difference between the corresponding attributes of the instances
 assertEquals(2, list2.size());
 assertEquals(-0.1, list2.get(0), 0.001);
 assertEquals(-0.1, list2.get(1), 0.001);
 }

 @Test
 public void testDivide() {
 // Test dividing a double value by an instance
 instance1.put(0, 6.0);
 instance1.put(1, 8.0);
 AbstractInstance result1 = instance1.divide(2.0);
 List<Double> list1_2 = new ArrayList<>();
 Iterator<Double> iterator1_2 = result1.iterator();
 while (iterator1_2.hasNext()) {
 list1_2.add(iterator1_2.next());
 }
 // Result should contain the attributes of the instance with the given value divided by each attribute
 assertEquals(2, list1_2.size());
 assertEquals(3.0, list1_2.get(0), 0.001);
 assertEquals(4.0, list1_2.get(1), 0.001);

 // Test dividing an instance by a double value
 AbstractInstance result2 = instance3.divide(2.0);
 List<Double> list2_1 = new ArrayList<>();
 Iterator<Double> iterator2_1 = result2.iterator();
 while (iterator2_1.hasNext()) {
 list2_1.add(iterator2_1.next());
 }
 // Result should contain the attributes of the instance with each attribute divided by the given value
 assertEquals(2, list2_1.size());
 assertEquals(0.5, list2_1.get(0), 0.001);
 assertEquals(1.0, list2_1.get(1), 0.001);
 }

 @Test
 public void testMultiply() {
 // Test multiplying a double value by an instance
 instance1.put(0, 2.0);
 instance1.put(1, 3.0);
 AbstractInstance result1 = instance1.multiply(3.0);
 List<Double> list1_3 = new ArrayList<>();
 Iterator<Double> iterator1_3 = result1.iterator();
 while (iterator1_3.hasNext()) {
 list1_3.add(iterator1_3.next());
 }
 // Result should contain the attributes of the instance with each attribute multiplied by the given value
 assertEquals(2, list1_3.size());
 assertEquals(6.0, list1_3.get(0), 0.001);
 assertEquals(9.0, list1_3.get(1), 0.001);

 // Test multiplying two instances
 AbstractInstance result2 = instance3.multiply(instance4);
 List<Double> list2_2 = new ArrayList<>();
 Iterator<Double> iterator2_2 = result2.iterator();
 while (iterator2_2.hasNext()) {
 list2_2.add(iterator2_2.next());
 }
 // Result should contain the product of the corresponding attributes of the instances
 assertEquals(2, list2_2.size());
 assertEquals(1.1 * 1.0, list2_2.get(0), 0.001);
 assertEquals(2.1 * 2.0, list2_2.get(1), 0.001);
 }

 @Test
 public void testHashCode() {
 // Test that two instances with the same attributes have the same hashcode
 AbstractInstance instance5 = new AbstractInstance();
 instance5.put(0, 1.0);
 instance5.put(1, 2.0);
 assertEquals(instance3.hashCode(), instance5.hashCode());

 // Test that two instances with different attributes have different hashcodes
 AbstractInstance instance6 = new AbstractInstance();
 instance6.put(0, 1.1);
 instance6.put(1, 2.1);
 assertNotEquals(instance3.hashCode(), instance6.hashCode());
 }

 @Test
 public void testEquals() {
 // Test that an instance is equal to itself
 assertTrue(instance1.equals(instance1));

 // Test that two instances with the same attributes are equal
 assertTrue(instance3.equals(instance5));

 // Test that two instances with different attributes are not equal
 assertFalse(instance3.equals(instance6));

 // Test that an instance is not equal to a null object
 assertFalse(instance3.equals(null));
 }

 @Test
 public void testMinusWithInstance() {
 // Test subtracting an instance from an instance
 AbstractInstance result1 = instance3.minus(instance4);
 List<Double> list1_4 = new ArrayList<>();
 Iterator<Double> iterator1_4 = result1.iterator();
 while (iterator1_4.hasNext()) {
 list1_4.add(iterator1_4.next());
 }
 // Result should contain the difference between the corresponding attributes of the instances
 assertEquals(2, list1_4.size());
 assertEquals(-0.1, list1_4.get(0), 0.001);
 assertEquals(-0.1, list1_4.get(1), 0.001);
 }

 @Test
 public void testDivideWithInstance() {
 // Test dividing an instance by an instance
 AbstractInstance result1 = instance3.divide(instance4);
 List<Double> list1_5 = new ArrayList<>();
 Iterator<Double> iterator1_5 = result1.iterator();
 while (iterator1_5.hasNext()) {
 list1_5.add(iterator1_5.next());
 }
 // Result should contain the quotient of the corresponding attributes of the instances
 assertEquals(2, list1_5.size());
 assertEquals(1.0 / 1.1, list1_5.get(0), 0.001);
 assertEquals(2.0 / 2.1, list1_5.get(1), 0.001);
 }

 @Test
 public void testAddWithInstance() {
 // Test adding an instance to an instance
 AbstractInstance result1 = instance3.add(instance4);
 List<Double> list1_6 = new ArrayList<>();
 Iterator<Double> iterator1_6 = result1.iterator();
 while (iterator1_6.hasNext()) {
 list1_6.add(iterator1_6.next());
 }
 // Result should contain the sum of the corresponding attributes of the instances
 assertEquals(2, list1_6.size());
 assertEquals(1.1, list1_6.get(0), 0.001);
 assertEquals(2.1, list1_6.get(1), 0.001);
 }

 @Test
 public void testSqrt() {
 // Test taking the square root of an instance
 instance1.put(0, 9.0);
 instance1.put(1, 16.0);
 AbstractInstance result1 = instance1.sqrt();
 List<Double> list1_7 = new ArrayList<>();
 Iterator<Double> iterator1_7 = result1.iterator();
 while (iterator1_7.hasNext()) {
 list1_7.add(iterator1_7.next());
 }
 // Result should contain the square root of each attribute of the instance
 assertEquals(2, list1_7.size());
 assertEquals(3.0, list1_7.get(0), 0.001);
 assertEquals(4.0, list1_7.get(1), 0.001);
 }
}