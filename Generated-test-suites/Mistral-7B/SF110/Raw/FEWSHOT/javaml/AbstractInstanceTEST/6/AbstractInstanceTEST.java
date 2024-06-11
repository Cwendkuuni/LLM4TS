package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

 AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0});
 AbstractInstance instance3 = new DenseInstance(new double[]{3.0, 3.0, 3.0});
 AbstractInstance instance4 = new DenseInstance(new double[]{1.0, 1.0, 1.0});
 AbstractInstance instance5 = new SparseInstance(new int[]{0, 1}, new double[]{2.0, 3.0});

 @Test
 public void testNextID() {
     AbstractInstance abstractInstance = new AbstractInstance() {
     };
     assertEquals(0, abstractInstance.nextID);
     abstractInstance = new AbstractInstance(null);
     assertEquals(1, abstractInstance.nextID);
 }

 @Test
 public void testEmptyConstructor() {
     AbstractInstance emptyInstance = new AbstractInstance();
     assertEquals(0, emptyInstance.getID());
     assertNull(emptyInstance.classValue());
 }

 @Test
 public void testConstructorWithClassValue() {
     AbstractInstance instanceWithClassValue = new AbstractInstance("testClassValue");
     assertEquals(1, instanceWithClassValue.getID());
     assertEquals("testClassValue", instanceWithClassValue.classValue());
 }

 @Test
 public void testGetID() {
     assertEquals(0, instance1.getID());
     assertEquals(1, instance2.getID());
     assertEquals(2, instance3.getID());
     assertEquals(3, instance4.getID());
     assertEquals(4, instance5.getID());
 }

 @Test
 public void testIterator() {
     List<Double> values1 = new ArrayList<>();
     Iterator<Double> it1 = instance1.iterator();
     while (it1.hasNext()) {
         values1.add(it1.next());
     }
     assertEquals(Arrays.asList(1.0, 2.0, 3.0), values1);
     values1.clear();

     List<Double> values2 = new ArrayList<>();
     Iterator<Double> it2 = instance2.iterator();
     while (it2.hasNext()) {
         values2.add(it2.next());
     }
     assertEquals(Arrays.asList(2.0, 3.0, 4.0), values2);
 }

 @Test
 public void testMinusWithInstance() {
     AbstractInstance result = instance1.minus(instance2);
     List<Double> expectedValues = new ArrayList<>();
     expectedValues.add(instance1.value(0) - instance2.value(0));
     expectedValues.add(instance1.value(1) - instance2.value(1));
     expectedValues.add(instance1.value(2) - instance2.value(2));

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testMinusWithDouble() {
     AbstractInstance result = instance1.minus(1.0);
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         expectedValues.add(instance1.value(i) - 1.0);
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testDivideWithDouble() {
     AbstractInstance result = instance1.divide(2.0);
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         expectedValues.add(instance1.value(i) / 2.0);
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testMultiplyWithDouble() {
     AbstractInstance result = instance1.multiply(2.0);
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         expectedValues.add(instance1.value(i) * 2.0);
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testEquals() {
     AbstractInstance instance6 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     assertTrue(instance1.equals(instance1));
     assertTrue(instance1.equals(instance6));
     assertFalse(instance1.equals(instance2));
     assertFalse(instance1.equals(null));
     assertFalse(instance1.equals("testString"));
 }

 @Test
 public void testHashCode() {
     AbstractInstance instance6 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
     assertEquals(instance1.getID(), instance1.hashCode());
     assertEquals(instance1.getID(), instance6.hashCode());
 }

 @Test
 public void testMinusWithInstanceAndAddWithInstance() {
     AbstractInstance result = instance1.minus(instance2).add(instance3);
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         double value1 = instance1.value(i);
         double value2 = instance2.value(i);
         double value3 = instance3.value(i);
         expectedValues.add(value1 - value2 + value3);
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testMultiplyWithInstance() {
     AbstractInstance result = instance1.multiply(instance2);
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         expectedValues.add(instance1.value(i) * instance2.value(i));
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testDivideWithInstance() {
     AbstractInstance instance6 = new DenseInstance(new double[]{2.0, 2.0, 2.0});
     AbstractInstance result = instance1.divide(instance6);
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         expectedValues.add(instance1.value(i) / instance6.value(i));
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testAddWithDouble() {
     AbstractInstance result = instance1.add(1.0);
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         expectedValues.add(instance1.value(i) + 1.0);
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testAddWithInstance() {
     AbstractInstance result = instance1.add(instance2);
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         expectedValues.add(instance1.value(i) + instance2.value(i));
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }

 @Test
 public void testSqrt() {
     AbstractInstance result = instance1.sqrt();
     List<Double> expectedValues = new ArrayList<>();
     for (int i = 0; i < instance1.noAttributes(); i++) {
         expectedValues.add(Math.sqrt(instance1.value(i)));
     }

     List<Double> values = new ArrayList<>();
     Iterator<Double> it = result.iterator();
     while (it.hasNext()) {
         values.add(it.next());
     }
     assertEquals(expectedValues, values);
 }
}