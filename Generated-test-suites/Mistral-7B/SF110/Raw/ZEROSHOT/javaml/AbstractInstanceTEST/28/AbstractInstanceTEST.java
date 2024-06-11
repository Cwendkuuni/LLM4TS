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
 private AbstractInstance instance5;

 @Test
 public void testConstructor() {
 // Test default constructor
 instance1 = new AbstractInstance();
 assertEquals(0, instance1.getID());
 assertNull(instance1.classValue());

 // Test constructor with classValue
 instance2 = new AbstractInstance("classValue");
 assertEquals(1, instance2.getID());
 assertEquals("classValue", instance2.classValue());
 }

 @Test
 public void testGetID() {
 instance1 = new AbstractInstance();
 instance2 = new AbstractInstance();
 instance3 = new AbstractInstance();
 assertEquals(0, instance1.getID());
 assertEquals(1, instance2.getID());
 assertEquals(2, instance3.getID());
 }

 @Test
 public void testIterator() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 List<Double> expectedList = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
 List<Double> actualList = new ArrayList<>();
 for (Double value : instance1) {
 actualList.add(value);
 }
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testMinusWithInstance() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance2 = new DenseInstance(new double[5]);
 instance2.put(0, 2.0);
 instance2.put(1, 1.0);
 instance2.put(2, 3.0);
 instance2.put(3, 1.0);
 instance2.put(4, 4.0);

 instance3 = instance1.minus(instance2);
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(instance1.value(i) - instance2.value(i), instance3.value(i), 0.0);
 }
 }

 @Test
 public void testMinusWithDouble() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance3 = instance1.minus(2.5);
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(instance1.value(i) - 2.5, instance3.value(i), 0.0);
 }
 }

 @Test
 public void testDivideWithDouble() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance3 = instance1.divide(2.5);
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(instance1.value(i) / 2.5, instance3.value(i), 0.0);
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance3 = instance1.multiply(2.5);
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(instance1.value(i) * 2.5, instance3.value(i), 0.0);
 }
 }

 @Test
 public void testEqualsObject() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance2 = new DenseInstance(new double[5]);
 instance2.put(0, 1.0);
 instance2.put(1, 2.0);
 instance2.put(2, 3.0);
 instance2.put(3, 4.0);
 instance2.put(4, 5.0);

 instance3 = new DenseInstance(new double[5]);
 instance3.put(0, 1.0);
 instance3.put(1, 2.0);
 instance3.put(2, 3.0);
 instance3.put(3, 4.0);
 instance3.put(4, 5.1);

 assertFalse(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 assertTrue(instance1.equals(instance1));
 }

 @Test
 public void testHashCode() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 AbstractInstance instance6 = instance1;
 assertEquals(instance1.hashCode(), instance6.hashCode());

 instance2 = new DenseInstance(new double[5]);
 instance2.put(0, 1.0);
 instance2.put(1, 2.0);
 instance2.put(2, 3.0);
 instance2.put(3, 4.0);
 instance2.put(4, 5.0);

 assertNotSame(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testMultiplyWithInstance() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance2 = new DenseInstance(new double[5]);
 instance2.put(0, 1.0);
 instance2.put(1, 2.0);
 instance2.put(2, 3.0);
 instance2.put(3, 4.0);
 instance2.put(4, 5.0);

 instance3 = instance1.multiply(instance2);
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(instance1.value(i) * instance2.value(i), instance3.value(i), 0.0);
 }
 }

 @Test
 public void testDivideWithInstance() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance2 = new DenseInstance(new double[5]);
 instance2.put(0, 2.0);
 instance2.put(1, 1.0);
 instance2.put(2, 3.0);
 instance2.put(3, 2.0);
 instance2.put(4, 4.0);

 instance3 = instance1.divide(instance2);
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(instance1.value(i) / instance2.value(i), instance3.value(i), 0.0);
 }
 }

 @Test
 public void testAddWithInstance() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance2 = new DenseInstance(new double[5]);
 instance2.put(0, 1.0);
 instance2.put(1, 2.0);
 instance2.put(2, 3.0);
 instance2.put(3, 4.0);
 instance2.put(4, 5.0);

 instance3 = instance1.add(instance2);
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(instance1.value(i) + instance2.value(i), instance3.value(i), 0.0);
 }
 }

 @Test
 public void testAddWithDouble() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 2.0);
 instance1.put(2, 3.0);
 instance1.put(3, 4.0);
 instance1.put(4, 5.0);

 instance3 = instance1.add(2.5);
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(instance1.value(i) + 2.5, instance3.value(i), 0.0);
 }
 }

 @Test
 public void testSqrt() {
 instance1 = new DenseInstance(new double[5]);
 instance1.put(0, 1.0);
 instance1.put(1, 4.0);
 instance1.put(2, 9.0);
 instance1.put(3, 16.0);
 instance1.put(4, 25.0);

 instance3 = instance1.sqrt();
 for (int i = 0; i < instance3.noAttributes(); i++) {
 assertEquals(Math.sqrt(instance1.value(i)), instance3.value(i), 0.0);
 }
 }

}