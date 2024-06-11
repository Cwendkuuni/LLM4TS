package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;

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
 // Test ID generation
 instance1 = new AbstractInstance();
 instance2 = new AbstractInstance();
 assertEquals(0, instance1.getID());
 assertEquals(1, instance2.getID());
 }

 @Test
 public void testIterator() {
 // Test empty instance
 instance1 = new AbstractInstance();
 assertFalse(instance1.iterator().hasNext());

 // Test populated instance
 Map<String, Double> values1 = new HashMap<>();
 values1.put("attribute1", 1.0);
 values1.put("attribute2", 2.0);
 instance2 = new AbstractInstance(values1);
 Double next;
 Iterator<Double> iterator = instance2.iterator();
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(1.0, next.doubleValue());
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(2.0, next.doubleValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMinus() {
 // Test minus double
 instance1 = new AbstractInstance(Collections.singletonMap("attribute", 5.0));
 double min = 2.0;
 instance2 = instance1.minus(min);
 Map<String, Double> values2 = new HashMap<>();
 values2.put("attribute", 3.0);
 assertEquals(instance2, new AbstractInstance(values2));

 // Test minus instance
 instance3 = new AbstractInstance(Collections.singletonMap("attribute", 3.0));
 instance1 = new AbstractInstance(Collections.singletonMap("attribute", 5.0));
 instance2 = instance1.minus(instance3);
 assertEquals(instance2, new AbstractInstance(Collections.singletonMap("attribute", 2.0)));
 }

 @Test
 public void testMinusWithDenseInstance() {
 // Test minus double
 instance1 = new DenseInstance(new double[]{5.0, 2.0, 3.0});
 double min = 2.0;
 instance2 = instance1.minus(min);
 double[] expectedArray = new double[]{3.0, 0.0, 1.0};
 assertArrayEquals(expectedArray, instance2.asDoubleArray());

 // Test minus instance
 instance3 = new DenseInstance(new double[]{3.0, 1.0, 2.0});
 instance1 = new DenseInstance(new double[]{5.0, 2.0, 3.0});
 instance2 = instance1.minus(instance3);
 double[] expectedArray2 = new double[]{2.0, 1.0, 1.0};
 assertArrayEquals(expectedArray2, instance2.asDoubleArray());
 }

 @Test
 public void testDivide() {
 // Test divide double
 instance1 = new AbstractInstance(Collections.singletonMap("attribute", 10.0));
 double min = 2.0;
 instance2 = instance1.divide(min);
 Map<String, Double> values3 = new HashMap<>();
 values3.put("attribute", 5.0);
 assertEquals(instance2, new AbstractInstance(values3));

 // Test divide instance
 instance3 = new AbstractInstance(Collections.singletonMap("attribute", 2.0));
 instance1 = new AbstractInstance(Collections.singletonMap("attribute", 10.0));
 instance2 = instance1.divide(instance3);
 assertEquals(instance2, new AbstractInstance(Collections.singletonMap("attribute", 5.0)));
 }

 @Test
 public void testDivideWithDenseInstance() {
 // Test divide double
 instance1 = new DenseInstance(new double[]{10.0, 20.0, 30.0});
 double min = 2.0;
 instance2 = instance1.divide(min);
 double[] expectedArray = new double[]{5.0, 10.0, 15.0};
 assertArrayEquals(expectedArray, instance2.asDoubleArray());

 // Test divide instance
 instance3 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 instance1 = new DenseInstance(new double[]{10.0, 20.0, 30.0});
 instance2 = instance1.divide(instance3);
 double[] expectedArray2 = new double[]{5.0, 6.666666666666667, 7.5};
 assertArrayEquals(expectedArray2, instance2.asDoubleArray());
 }

 @Test
 public void testMultiply() {
 // Test multiply double
 instance1 = new AbstractInstance(Collections.singletonMap("attribute", 5.0));
 double value = 2.0;
 instance2 = instance1.multiply(value);
 Map<String, Double> values4 = new HashMap<>();
 values4.put("attribute", 10.0);
 assertEquals(instance2, new AbstractInstance(values4));

 // Test multiply instance
 instance3 = new AbstractInstance(Collections.singletonMap("attribute", 2.0));
 instance1 = new AbstractInstance(Collections.singletonMap("attribute", 5.0));
 instance2 = instance1.multiply(instance3);
 assertEquals(instance2, new AbstractInstance(Collections.singletonMap("attribute", 10.0)));
 }

 @Test
 public void testMultiplyWithDenseInstance() {
 // Test multiply double
 instance1 = new DenseInstance(new double[]{5.0, 2.0, 3.0});
 double value = 2.0;
 instance2 = instance1.multiply(value);
 double[] expectedArray = new double[]{10.0, 4.0, 6.0};
 assertArrayEquals(expectedArray, instance2.asDoubleArray());

 // Test multiply instance
 instance3 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 instance1 = new DenseInstance(new double[]{5.0, 2.0, 3.0});
 instance2 = instance1.multiply(instance3);
 double[] expectedArray2 = new double[]{10.0, 6.0, 12.0};
 assertArrayEquals(expectedArray2, instance2.asDoubleArray());
 }

 @Test
 public void testEquals() {
 instance1 = new AbstractInstance(Collections.singletonMap("attribute1", 1.0));
 instance2 = new AbstractInstance(Collections.singletonMap("attribute1", 1.0));
 assertTrue(instance1.equals(instance2));

 instance3 = new AbstractInstance(Collections.singletonMap("attribute2", 1.0));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testHashCode() {
 instance1 = new AbstractInstance(Collections.singletonMap("attribute1", 1.0));
 instance2 = new AbstractInstance(Collections.singletonMap("attribute1", 1.0));
 assertEquals(instance1.hashCode(), instance2.hashCode());

 instance3 = new AbstractInstance(Collections.singletonMap("attribute2", 1.0));
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testMinusWithNullInstance() {
 instance1 = new AbstractInstance(new HashMap<>());
 assertNull(instance1.minus((Instance) null));
 }

 @Test
 public void testMinusWithNullDouble() {
 instance1 = new AbstractInstance(new HashMap<>());
 assertNull(instance1.minus((Double) null));
 }

 @Test
 public void testDivideWithNullInstance() {
 instance1 = new AbstractInstance(new HashMap<>());
 assertNull(instance1.divide((Instance) null));
 }

 @Test
 public void testDivideWithZeroDouble() {
 instance1 = new AbstractInstance(new HashMap<>());
 assertNull(instance1.divide(0.0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveWithInstanceValueIterator() {
 instance1 = new AbstractInstance(new HashMap<>());
 Iterator<Double> iterator = instance1.iterator();
 iterator.next();
 iterator.remove();
 }

 @Test
 public void testSquareRoot() {
 // Test square root double
 instance1 = new AbstractInstance(Collections.singletonMap("attribute", 16.0));
 instance2 = instance1.sqrt();
 Map<String, Double> values5 = new HashMap<>();
 values5.put("attribute", 4.0);
 assertEquals(instance2, new AbstractInstance(values5));

 // Test square root instance
 instance3 = new AbstractInstance(Collections.singletonMap("attribute", 16.0));
 instance1 = new AbstractInstance(Collections.singletonMap("attribute", 25.0));
 instance2 = instance1.sqrt();
 assertEquals(instance2, new AbstractInstance(Collections.singletonMap("attribute", 5.0)));
 }

 @Test
 public void testSquareRootWithDenseInstance() {
 // Test square root double
 instance1 = new DenseInstance(new double[]{16.0, 25.0, 36.0});
 instance2 = instance1.sqrt();
 double[] expectedArray3 = new double[]{4.0, 5.0, 6.0};
 assertArrayEquals(expectedArray3, instance2.asDoubleArray());

 // Test square root instance
 instance3 = new DenseInstance(new double[]{16.0, 25.0, 36.0});
 instance1 = new DenseInstance(new double[]{25.0, 36.0, 49.0});
 instance2 = instance1.sqrt();
 double[] expectedArray4 = new double[]{5.0, 6.0, 7.0};
 assertArrayEquals(expectedArray4, instance2.asDoubleArray());
 }

}