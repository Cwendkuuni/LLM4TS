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
 public void testConstructor_NoArgs() {
     instance1 = new AbstractInstance();
     assertEquals(0, instance1.getID());
     assertNull(instance1.classValue());
 }

 @Test
 public void testConstructor_WithArgs() {
     instance2 = new AbstractInstance(new Object());
     assertNotSame(instance1.getID(), instance2.getID());
     assertEquals(instance2.classValue(), new Object());
 }

 @Test
 public void testGetID() {
     instance3 = new AbstractInstance();
     instance4 = new AbstractInstance();
     assertEquals(instance3.getID(), instance3.getID());
     assertEquals(instance3.getID(), instance4.getID()); // ID should be same for same object
     assertEquals(instance3.getID(), instance3.getID()); // ID should be same for same object
     assertNotSame(instance3.getID(), instance2.getID()); // ID should be different for different objects
 }

 @Test
 public void testIterator() {
     Map<String, Double> map = new HashMap<>();
     instance1 = new AbstractInstance() {
         @Override
         public Iterator<Double> iterator() {
             return map.values().iterator();
         }
     };
     map.put("key1", 1.0);
     map.put("key2", 2.0);
     Iterator<Double> iterator = instance1.iterator();
     assertEquals(iterator.next(), 1.0);
     assertEquals(iterator.next(), 2.0);
     assertFalse(iterator.hasNext());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetClassValue_NullArgument() {
     instance1.setClassValue(null);
 }

 @Test
 public void testMinus_Instance() {
     Map<String, Double> map1 = new HashMap<>();
     map1.put("key1", 1.0);
     map1.put("key2", 2.0);
     instance1 = new AbstractInstance(map1);

     Map<String, Double> map2 = new HashMap<>();
     map2.put("key1", 0.5);
     map2.put("key2", -1.0);
     instance2 = new AbstractInstance(map2);

     AbstractInstance result = instance1.minus(instance2);
     Map<String, Double> expected = new HashMap<>();
     expected.put("key1", 0.5);
     expected.put("key2", 1.0);
     assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinus_Double_NegativeValue() {
     Map<String, Double> map = new HashMap<>();
     map.put("key", 1.0);
     instance1 = new AbstractInstance(map);
     instance1.minus(-1.0);
 }

 @Test
 public void testMinus_Double() {
     Map<String, Double> map = new HashMap<>();
     map.put("key", 1.0);
     instance1 = new AbstractInstance(map);
     AbstractInstance result = instance1.minus(1.0);
     Map<String, Double> expected = new HashMap<>();
     expected.put("key", 0.0);
     assertEquals(expected, result);
 }

 @Test
 public void testDivide_Double() {
     Map<String, Double> map = new HashMap<>();
     map.put("key", 1.0);
     instance1 = new AbstractInstance(map);
     AbstractInstance result = instance1.divide(1.0);
     Map<String, Double> expected = new HashMap<>();
     expected.put("key", 1.0);
     assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivide_Double_ZeroDivision() {
     Map<String, Double> map = new HashMap<>();
     map.put("key", 1.0);
     instance1 = new AbstractInstance(map);
     instance1.divide(0.0);
 }

 @Test
 public void testMultiply_Double() {
     Map<String, Double> map = new HashMap<>();
     map.put("key", 1.0);
     instance1 = new AbstractInstance(map);
     AbstractInstance result = instance1.multiply(2.0);
     Map<String, Double> expected = new HashMap<>();
     expected.put("key", 2.0);
     assertEquals(expected, result);
 }

 @Test
 public void testEquals() {
     Map<String, Double> map1 = new HashMap<>();
     map1.put("key1", 1.0);
     map1.put("key2", 2.0);
     instance1 = new AbstractInstance(map1);

     Map<String, Double> map2 = new HashMap<>();
     map2.put("key1", 1.0);
     map2.put("key2", 2.0);
     instance2 = new AbstractInstance(map2);

     Map<String, Double> map3 = new HashMap<>();
     map3.put("key1", 1.0);
     map3.put("key3", 2.0);
     instance3 = new AbstractInstance(map3);
     assertFalse(instance1.equals(instance2));
     assertFalse(instance1.equals(instance3));
     assertTrue(instance1.equals(instance1));
 }

 @Test
 public void testHashcode() {
     Map<String, Double> map1 = new HashMap<>();
     map1.put("key1", 1.0);
     map1.put("key2", 2.0);
     instance1 = new AbstractInstance(map1);

     Map<String, Double> map2 = new HashMap<>();
     map2.put("key1", 1.0);
     map2.put("key2", 2.0);
     instance2 = new AbstractInstance(map2);
     assertEquals(instance1.hashCode(), instance2.hashCode());
     assertNotSame(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testMultiply_Instance() {
     Map<String, Double> map1 = new HashMap<>();
     map1.put("key1", 1.0);
     map1.put("key2", 2.0);
     instance1 = new AbstractInstance(map1);

     Map<String, Double> map2 = new HashMap<>();
     map2.put("key1", 2.0);
     map2.put("key2", 3.0);
     instance2 = new AbstractInstance(map2);

     AbstractInstance result = instance1.multiply(instance2);
     Map<String, Double> expected = new HashMap<>();
     expected.put("key1", 2.0 * 2.0);
     expected.put("key2", 1.0 * 3.0);
     assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivide_Instance_ZeroDivision() {
     Map<String, Double> map1 = new HashMap<>();
     map1.put("key1", 1.0);
     instance1 = new AbstractInstance(map1);

     Map<String, Double> map2 = new HashMap<>();
     map2.put("key1", 0.0);
     instance2 = new AbstractInstance(map2);
     instance1.divide(instance2);
 }

 @Test
 public void testAdd_Double() {
     Map<String, Double> map = new HashMap<>();
     map.put("key", 1.0);
     instance1 = new AbstractInstance(map);
     AbstractInstance result = instance1.add(1.0);
     Map<String, Double> expected = new HashMap<>();
     expected.put("key", 2.0);
     assertEquals(expected, result);
 }

 @Test
 public void testAdd_Instance() {
     Map<String, Double> map1 = new HashMap<>();
     map1.put("key1", 1.0);
     map1.put("key2", 2.0);
     instance1 = new AbstractInstance(map1);

     Map<String, Double> map2 = new HashMap<>();
     map2.put("key1", 2.0);
     map2.put("key2", 3.0);
     instance2 = new AbstractInstance(map2);

     AbstractInstance result = instance1.add(instance2);
     Map<String, Double> expected = new HashMap<>();
     expected.put("key1", 3.0);
     expected.put("key2", 5.0);
     assertEquals(expected, result);
 }

 @Test
 public void testSqrt() {
     Map<String, Double> map = new HashMap<>();
     map.put("key", 4.0);
     instance1 = new AbstractInstance(map);
     AbstractInstance result = instance1.sqrt();
     Map<String, Double> expected = new HashMap<>();
     expected.put("key", 2.0);
     assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSqrt_NegativeValue() {
     Map<String, Double> map = new HashMap<>();
     map.put("key", -1.0);
     instance1 = new AbstractInstance(map);
     instance1.sqrt();
 }
}