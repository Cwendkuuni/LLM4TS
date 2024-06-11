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

 // Test constructor
 @Test
 public void testAbstractInstance() {
 AbstractInstance instance = new AbstractInstance();
 assertNotNull(instance);
 AbstractInstance instanceWithValue = new AbstractInstance("testValue");
 assertNotNull(instanceWithValue);
 }

 // Test getID method
 @Test
 public void testGetID() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());
 AbstractInstance instanceWithID = new AbstractInstance();
 instanceWithID.setID(10);
 assertEquals(10, instanceWithID.getID());
 }

 // Test iterator method
 @Test
 public void testIterator() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 Iterator<Double> iterator = instance.iterator();
 assertEquals(3, iterator.next());
 assertNull(iterator.next());
 }

 // Test minus method with Instance
 @Test
 public void testMinusInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance instance2 = new DenseInstance(new double[] { 0.5, 1.5, 2.5 });
 AbstractInstance result = instance1.minus(instance2);
 assertArrayEquals(new double[] { 0.5, 0.5, 0.5 }, result.toArray(), 0.01);
 }

 // Test minus method with double
 @Test
 public void testMinusDouble() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance result = instance.minus(1.0);
 assertArrayEquals(new double[] { 0.0, 1.0, 2.0 }, result.toArray(), 0.01);
 }

 // Test divide method with double
 @Test
 public void testDivideDouble() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance result = instance.divide(2.0);
 assertArrayEquals(new double[] { 0.5, 1.0, 1.5 }, result.toArray(), 0.01);
 }

 // Test multiply method with double
 @Test
 public void testMultiplyDouble() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance result = instance.multiply(2.0);
 assertArrayEquals(new double[] { 2.0, 4.0, 6.0 }, result.toArray(), 0.01);
 }

 // Test equals method
 @Test
 public void testEquals() {
 AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertTrue(instance1.equals(instance2));
 AbstractInstance instance3 = new DenseInstance(new double[] { 1.1, 2.1, 3.1 });
 assertFalse(instance1.equals(instance3));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(instance1.hashCode(), instance2.hashCode());
 AbstractInstance instance3 = new DenseInstance(new double[] { 1.1, 2.1, 3.1 });
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 // Test minus method with null
 @Test(expected = NullPointerException.class)
 public void testMinusNullInstance() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.minus(null);
 }

 // Test divide method with null
 @Test(expected = NullPointerException.class)
 public void testDivideNullInstance() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.divide(null);
 }

 // Test multiply method with null
 @Test(expected = NullPointerException.class)
 public void testMultiplyNullInstance() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.multiply(null);
 }

 // Test minus method with different sizeInstances
 @Test
 public void testMinusDifferentSizeInstances() {
 AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance instance2 = new SparseInstance(new int[] { 0, 1, 2 }, new double[] { 4.0, 5.0 });
 AbstractInstance result = instance1.minus(instance2);
 assertArrayEquals(new double[] { -3.0, 1.0, 2.0 }, result.toArray(), 0.01);
 }

 // Test divide method with zero
 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.divide(0.0);
 }

 // Test multiply method with zero
 @Test
 public void testMultiplyByZero() {
 AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance result = instance.multiply(0.0);
 assertArrayEquals(new double[] { 0.0, 0.0, 0.0 }, result.toArray(), 0.01);
 }

 // Test add method with null
 @Test(expected = NullPointerException.class)
 public void testAddNullInstance() {
 AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance instance2 = null;
 instance1.add(instance2);
 }

 // Test add method with different sizeInstances
 @Test
 public void testAddDifferentSizeInstances() {
 AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 AbstractInstance instance2 = new SparseInstance(new int[] { 0, 1, 2 }, new double[] { 0.5, 1.5, 2.5 });
 AbstractInstance result = instance1.add(instance2);
 assertArrayEquals(new double[] { 1.5, 3.5, 5.5 }, result.toArray(), 0.01);
 }

 // Test sqrt method with negative values
 @Test(expected = IllegalArgumentException.class)
 public void testSqrtNegativeValues() {
 AbstractInstance instance = new DenseInstance(new double[] { -1.0, -2.0, -3.0 });
 instance.sqrt();
 }

 // Test sqrt method with zero
 @Test(expected = IllegalArgumentException.class)
 public void testSqrtZero() {
 AbstractInstance instance = new DenseInstance(new double[] { 0.0, 0.0, 0.0 });
 instance.sqrt();
 }

}