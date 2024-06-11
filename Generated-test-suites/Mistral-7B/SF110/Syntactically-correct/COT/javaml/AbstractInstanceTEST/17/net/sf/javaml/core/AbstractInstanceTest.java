package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance instance1;
 private AbstractInstance instance2;
 private AbstractInstance instance3;

 @Before
 public void setUp() {
 Map<String, Double> map1 = new HashMap<String, Double>();
 map1.put("attribute1", 1.0);
 map1.put("attribute2", 2.0);
 instance1 = new AbstractInstance() {
 @Override
 public Map<String, Double> asMap() {
 return map1;
 }
 }.setClassValue("Class1");

 Map<String, Double> map2 = new HashMap<String, Double>();
 map2.put("attribute1", 3.0);
 map2.put("attribute2", 4.0);
 instance2 = new AbstractInstance() {
 @Override
 public Map<String, Double> asMap() {
 return map2;
 }
 }.setClassValue("Class2");

 Map<String, Double> map3 = new HashMap<String, Double>();
 map3.put("attribute1", 5.0);
 map3.put("attribute2", 6.0);
 instance3 = new AbstractInstance() {
 @Override
 public Map<String, Double> asMap() {
 return map3;
 }
 }.setClassValue("Class3");
 }

 @Test
 public void testGetID() {
 assertEquals(0, instance1.getID());
 assertEquals(1, instance2.getID());
 assertEquals(2, instance3.getID());
 }

 @Test
 public void testIterator() {
 Double[] expected = {1.0, 2.0};
 Double[] actual = new Double[instance1.noAttributes()];
 Iterator<Double> iterator = instance1.iterator();
 for (int i = 0; i < instance1.noAttributes(); i++) {
 actual[i] = iterator.next();
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorRemove() {
 Iterator<Double> iterator = instance1.iterator();
 iterator.next();
 iterator.remove();
 }

 @Test
 public void testConstructorNoArguments() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());
 assertNull(instance.classValue());
 }

 @Test
 public void testConstructorWithClassValue() {
 AbstractInstance instance = new AbstractInstance("ClassValue");
 assertEquals(0, instance.getID());
 assertEquals("ClassValue", instance.classValue());
 }

 @Test
 public void testClassValue() {
 assertEquals("Class1", instance1.classValue());
 assertEquals("Class2", instance2.classValue());
 assertEquals("Class3", instance3.classValue());
 }

 @Test
 public void testSetClassValue() {
 instance1.setClassValue("ClassNew");
 assertEquals("ClassNew", instance1.classValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetClassValueNull() {
 instance1.setClassValue(null);
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance result = instance1.minus(instance2);
 Double[] expected = {-2.0, -2.0};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNull() {
 instance1.minus(null);
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance result = instance1.minus(1.0);
 Double[] expected = {0.0, 1.0};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance result = instance1.divide(instance2);
 Double[] expected = {0.5, 0.5};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNull() {
 instance1.divide(null);
 }

 @Test(expected = ArithmeticException.class)
 public void testDivideByZero() {
 instance1.divide(0.0);
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance result = instance1.multiply(instance2);
 Double[] expected = {3.0, 4.0};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNull() {
 instance1.multiply(null);
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance result = instance1.add(instance2);
 Double[] expected = {2.0, 6.0};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNull() {
 instance1.add(null);
 }

 @Test
 public void testSqrt() {
 AbstractInstance result = instance1.sqrt();
 Double[] expected = {1.0, 1.4142135623730955};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testMinusWithDoubleAndInstance() {
 AbstractInstance result = instance1.minus(instance2, 1.0);
 Double[] expected = {-3.5, -1.5};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithDoubleAndNull() {
 instance1.minus(1.0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullAndInstance() {
 instance1.minus(null, instance2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithNullAndDouble() {
 instance1.minus(null, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInstanceAndDouble() {
 instance1.minus(instance2, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInstanceAndNull() {
 instance1.minus(instance2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithDoubleAndDouble() {
 instance1.minus(1.0, 1.0);
 }

 @Test
 public void testDivideWithInstanceAndInstance() {
 AbstractInstance result = instance1.divide(instance2);
 Double[] expected = {0.6666666666666667, 0.6666666666666667};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithInstanceAndNull() {
 instance1.divide(instance2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullAndInstance() {
 instance1.divide(null, instance2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithNullAndNull() {
 instance1.divide(null, null);
 }

 @Test
 public void testMultiplyWithInstanceAndInstance() {
 AbstractInstance result = instance1.multiply(instance2);
 Double[] expected = {3.0, 8.0};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithInstanceAndNull() {
 instance1.multiply(instance2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNullAndInstance() {
 instance1.multiply(null, instance2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNullAndNull() {
 instance1.multiply(null, null);
 }

 @Test
 public void testAddWithInstanceAndInstance() {
 AbstractInstance result = instance1.add(instance2);
 Double[] expected = {3.0, 8.0};
 Double[] actual = new Double[result.noAttributes()];
 for (int i = 0; i < result.noAttributes(); i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithInstanceAndNull() {
 instance1.add(instance2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNullAndInstance() {
 instance1.add(null, instance2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddWithNullAndNull() {
 instance1.add(null, null);
 }

 @Test
 public void testHashCode() {
 int hashCode1 = instance1.hashCode();
 int hashCode2 = instance2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testEquals() {
 assertFalse(instance1.equals(instance2));
 assertFalse(instance1.equals(new Object()));
 assertTrue(instance1.equals(instance1));
 }

 @Test
 public void testEqualsWithSameInstanceValueMap() {
 Map<String, Double> map = new HashMap<String, Double>();
 map.putAll(instance1.asMap());
 AbstractInstance newInstance = new AbstractInstance() {
 @Override
 public Map<String, Double> asMap() {
 return map;
 }
 }.setClassValue(instance1.getClassValue());
 assertTrue(instance1.equals(newInstance));
 }

 @Test
 public void testEqualsWithDifferentClassValue() {
 Map<String, Double> map = new HashMap<String, Double>();
 map.putAll(instance1.asMap());
 AbstractInstance newInstance = new AbstractInstance() {
 @Override
 public Map<String, Double> asMap() {
 return map;
 }
 }.setClassValue("ClassDifferent");
 assertFalse(instance1.equals(newInstance));
 }
}