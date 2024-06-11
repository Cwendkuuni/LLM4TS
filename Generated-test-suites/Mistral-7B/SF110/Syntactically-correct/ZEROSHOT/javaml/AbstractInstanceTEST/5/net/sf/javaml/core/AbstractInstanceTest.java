package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;
 private AbstractInstance instance4;

 @Test
 public void testConstructor() {
 // Test default constructor
 instance1 = new AbstractInstance();
 // Test constructor with classValue
 instance2 = new AbstractInstance("classValue1");
 }

 @Test
 public void testGetID() {
 // Test ID getter
 assertEquals(0, instance1.getID());
 assertEquals(AbstractInstance.nextID, instance2.getID());
 }

 @Test
 public void testIterator() {
 // Test iterator method
 instance1 = new AbstractInstance();
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 Iterator<Double> iterator = instance1.iterator();
 assertEquals(2, iterator.next());
 assertEquals(1.0, iterator.next());
 }

 @Test
 public void testMinusWithInstance() {
 // Test minus method with another instance
 instance3 = new AbstractInstance();
 instance3.put(0, 3.0);
 instance3.put(1, 4.0);
 instance4 = instance1.minus(instance3);
 assertEquals(new Double[] {-2.0, -1.0}, instance4.toArray());
 }

 @Test
 public void testMinusWithDouble() {
 // Test minus method with double
 instance4 = instance1.minus(2.0);
 assertEquals(new Double[] {-1.0, -0.5}, instance4.toArray());
 }

 @Test
 public void testDivideWithDouble() {
 // Test divide method with double
 instance4 = instance1.divide(2.0);
 assertEquals(new Double[] {0.5, 1.0}, instance4.toArray());
 }

 @Test
 public void testMultiplyWithDouble() {
 // Test multiply method with double
 instance4 = instance1.multiply(2.0);
 assertEquals(new Double[] {2.0, 4.0}, instance4.toArray());
 }

 @Test
 public void testHashCode() {
 // Test hashCode method
 AbstractInstance instance5 = new AbstractInstance();
 instance5.put(0, 1.0);
 instance5.put(1, 2.0);
 assertEquals(instance1.hashCode(), instance5.hashCode());
 }

 @Test
 public void testEquals() {
 // Test equals method
 AbstractInstance instance6 = new AbstractInstance();
 instance6.put(0, 1.0);
 instance6.put(1, 2.0);
 assertTrue(instance1.equals(instance6));
 }

 @Test
 public void testMultiplyWithInstance() {
 // Test multiply method with another instance
 AbstractInstance instance7 = new AbstractInstance();
 instance7.put(0, 3.0);
 instance7.put(1, 4.0);
 instance4 = instance1.multiply(instance7);
 assertEquals(new Double[] {3.0, 8.0}, instance4.toArray());
 }

 @Test
 public void testDivideWithInstance() {
 // Test divide method with another instance
 AbstractInstance instance8 = new AbstractInstance();
 instance8.put(0, 3.0);
 instance8.put(1, 4.0);
 instance4 = instance1.divide(instance8);
 assertEquals(new Double[] {0.3333333333333333, 0.5}, instance4.toArray());
 }

 @Test
 public void testAddWithDouble() {
 // Test add method with double
 instance4 = instance1.add(2.0);
 assertEquals(new Double[] {2.5, 3.5}, instance4.toArray());
 }

 @Test
 public void testAddWithInstance() {
 // Test add method with another instance
 AbstractInstance instance9 = new AbstractInstance();
 instance9.put(0, 3.0);
 instance9.put(1, 4.0);
 instance4 = instance1.add(instance9);
 assertEquals(new Double[] {5.0, 7.5}, instance4.toArray());
 }

 @Test
 public void testSqrt() {
 // Test sqrt method
 instance4 = instance1.sqrt();
 assertEquals(new Double[] {Math.sqrt(1.0), Math.sqrt(2.0)}, instance4.toArray());
 }

}