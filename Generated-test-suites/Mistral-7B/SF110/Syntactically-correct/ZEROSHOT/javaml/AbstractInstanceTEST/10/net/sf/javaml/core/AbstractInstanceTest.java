package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;

 @Test
 public void testAbstractInstanceConstructor() {
     instance1 = new AbstractInstance();
     instance2 = new AbstractInstance(null);
     instance3 = new AbstractInstance("testClassValue");

     assertNotNull(instance1);
     assertNotNull(instance2);
     assertNotNull(instance3);
     assertEquals(instance1.getClass(), AbstractInstance.class);
     assertEquals(instance2.getClass(), AbstractInstance.class);
     assertEquals(instance3.getClass(), AbstractInstance.class);
     assertEquals(instance1.getID(), 0);
     assertEquals(instance2.getID(), 1);
     assertEquals(instance3.getID(), 2);
     assertNull(instance1.classValue());
     assertNull(instance2.classValue());
     assertNotNull(instance3.classValue());
     assertEquals(instance3.classValue().toString(), "testClassValue");
 }

 @Test
 public void testGetID() {
     instance1 = new AbstractInstance();
     assertEquals(instance1.getID(), 0);
     instance1 = new AbstractInstance(null);
     assertEquals(instance1.getID(), 1);
     instance1 = new AbstractInstance("testClassValue");
     assertEquals(instance1.getID(), 2);
 }

 @Test
 public void testIterator() {
     instance1 = new AbstractInstance();
     instance1.put(0, 1.0);
     instance1.put(1, 2.0);
     instance1.put(2, 3.0);

     double[] expectedValues = {1.0, 2.0, 3.0};
     Iterator<Double> iterator = instance1.iterator();

     int i = 0;
     while (iterator.hasNext()) {
         Double next = iterator.next();
         assertEquals(next, expectedValues[i++]);
     }
 }

 @Test
 public void testMinusWithInstance() {
     instance1 = new AbstractInstance();
     instance1.put(0, 3.0);
     instance1.put(1, 2.0);

     instance2 = new AbstractInstance();
     instance2.put(0, 1.0);
     instance2.put(1, 1.0);

     AbstractInstance result = instance1.minus(instance2);

     Double[] expectedValues = {2.0, 1.0};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }

 @Test
 public void testMinusWithNumber() {
     instance1 = new AbstractInstance();
     instance1.put(0, 3.0);
     instance1.put(1, 2.0);

     AbstractInstance result = instance1.minus(2.0);

     Double[] expectedValues = {1.0, 0.0};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }

 @Test
 public void testDivideWithNumber() {
     instance1 = new AbstractInstance();
     instance1.put(0, 6.0);
     instance1.put(1, 3.0);

     AbstractInstance result = instance1.divide(2.0);

     Double[] expectedValues = {3.0, 1.5};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }

 @Test
 public void testMultiplyWithNumber() {
     instance1 = new AbstractInstance();
     instance1.put(0, 3.0);
     instance1.put(1, 2.0);

     AbstractInstance result = instance1.multiply(2.0);

     Double[] expectedValues = {6.0, 4.0};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }

 @Test
 public void testEquals() {
     instance1 = new AbstractInstance();
     instance1.put(0, 3.0);
     instance1.put(1, 2.0);

     instance2 = new AbstractInstance();
     instance2.put(0, 3.0);
     instance2.put(1, 2.0);

     assertTrue(instance1.equals(instance2));
     assertFalse(instance1.equals(null));
     assertFalse(instance1.equals("test"));
 }

 @Test
 public void testHashCode() {
     instance1 = new AbstractInstance();
     instance1.put(0, 3.0);
     instance1.put(1, 2.0);

     AbstractInstance instance4 = new AbstractInstance();
     instance4.put(0, 3.0);
     instance4.put(1, 2.0);

     int expectedHashCode = instance1.getID();
     int actualHashCode = instance1.hashCode();

     assertEquals(expectedHashCode, actualHashCode);
     assertEquals(instance1.hashCode(), instance4.hashCode());
 }

 @Test
 public void testMultiplyWithInstance() {
     instance1 = new AbstractInstance();
     instance1.put(0, 3.0);
     instance1.put(1, 2.0);

     instance2 = new AbstractInstance();
     instance2.put(0, 2.0);
     instance2.put(1, 3.0);

     AbstractInstance result = instance1.multiply(instance2);

     Double[] expectedValues = {6.0, 6.0};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }

 @Test
 public void testDivideWithInstance() {
     instance1 = new AbstractInstance();
     instance1.put(0, 6.0);
     instance1.put(1, 3.0);

     instance2 = new AbstractInstance();
     instance2.put(0, 2.0);
     instance2.put(1, 3.0);

     AbstractInstance result = instance1.divide(instance2);

     Double[] expectedValues = {3.0, 2.0};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }

 @Test
 public void testAddWithNumber() {
     instance1 = new AbstractInstance();
     instance1.put(0, 3.0);
     instance1.put(1, 2.0);

     AbstractInstance result = instance1.add(2.0);

     Double[] expectedValues = {5.0, 4.0};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }

 @Test
 public void testAddWithInstance() {
     instance1 = new AbstractInstance();
     instance1.put(0, 3.0);
     instance1.put(1, 2.0);

     instance2 = new AbstractInstance();
     instance2.put(0, 2.0);
     instance2.put(1, 1.0);

     AbstractInstance result = instance1.add(instance2);

     Double[] expectedValues = {5.0, 3.0};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }

 @Test
 public void testSqrt() {
     instance1 = new AbstractInstance();
     instance1.put(0, 9.0);
     instance1.put(1, 16.0);

     AbstractInstance result = instance1.sqrt();

     Double[] expectedValues = {3.0, 4.0};
     for (int i = 0; i < result.noAttributes(); ++i) {
         assertEquals(result.get(i), expectedValues[i]);
     }
 }
}