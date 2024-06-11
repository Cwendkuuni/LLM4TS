package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private AbstractInstance createInstance(int numberOfAttributes, Object classValue) {
 return new AbstractInstance(classValue) {
 @Override
 public Map<Integer, Double> getAttributeMap() {
 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < numberOfAttributes; i++) {
 map.put(i, (double) i);
 }
 return map;
 }
 };
 }

 @Test
 public void testConstructor() {
 AbstractInstance instance = new AbstractInstance();
 assertNotNull(instance);
 instance = new AbstractInstance(new Object());
 assertNotNull(instance);
 }

 @Test
 public void testGetID() {
 AbstractInstance instance = new AbstractInstance();
 assertEquals(0, instance.getID());
 instance = new AbstractInstance(null);
 assertEquals(1, instance.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = createInstance(5, null);
 Iterator<Double> iterator = instance.iterator();
 Double[] expected = {0.0, 1.0, 2.0, 3.0, 4.0};
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = iterator.next();
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance1 = createInstance(5, null);
 AbstractInstance instance2 = createInstance(5, null);
 AbstractInstance result = instance1.minus(instance2);
 Double[] expected = new Double[5];
 Arrays.fill(expected, -1.0);
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance = createInstance(5, null);
 AbstractInstance result = instance.minus(2.5);
 Double[] expected = new Double[5];
 Arrays.stream(expected).forEach(d -> expected[d.intValue()] = -2.5);
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance instance = createInstance(5, null);
 AbstractInstance result = instance.divide(2.5);
 Double[] expected = new Double[5];
 Arrays.stream(expected).forEach(d -> expected[d.intValue()] = 0.4);
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance = createInstance(5, null);
 AbstractInstance result = instance.multiply(2.5);
 Double[] expected = new Double[5];
 Arrays.stream(expected).forEach(d -> expected[d.intValue()] = 2.5);
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testHashCode() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 assertEquals(0, instance1.hashCode());
 assertEquals(0, instance2.hashCode());
 AbstractInstance instance3 = new AbstractInstance(new Object());
 AbstractInstance instance4 = new AbstractInstance(new Object());
 assertEquals(1, instance3.hashCode());
 assertEquals(1, instance4.hashCode());
 }

 @Test
 public void testEquals() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 assertFalse(instance1.equals(instance2));
 AbstractInstance instance3 = new AbstractInstance(null);
 AbstractInstance instance4 = new AbstractInstance(null);
 assertTrue(instance3.equals(instance4));
 AbstractInstance instance5 = new AbstractInstance(new Object());
 AbstractInstance instance6 = new AbstractInstance(new Object());
 assertFalse(instance5.equals(instance6));
 AbstractInstance instance7 = new AbstractInstance();
 AbstractInstance instance8 = instance7;
 assertTrue(instance7.equals(instance8));
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance1 = createInstance(5, null);
 AbstractInstance instance2 = createInstance(5, null);
 AbstractInstance result = instance1.multiply(instance2);
 Double[] expected = new Double[5];
 Arrays.fill(expected, 0.0, 5, 1.0);
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance1 = createInstance(5, null);
 AbstractInstance instance2 = createInstance(5, null);
 AbstractInstance result = instance1.divide(instance2);
 Double[] expected = new Double[5];
 Arrays.fill(expected, 1.0, 5, 1.0 / 5.0);
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance instance = createInstance(5, null);
 AbstractInstance result = instance.add(2.5);
 Double[] expected = new Double[5];
 Arrays.stream(expected).forEach(d -> expected[d.intValue()] = 2.5);
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance instance1 = createInstance(5, null);
 AbstractInstance instance2 = createInstance(5, null);
 AbstractInstance result = instance1.add(instance2);
 Double[] expected = new Double[5];
 Arrays.fill(expected, 0.0, 5, 2.0);
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = createInstance(5, null);
 AbstractInstance result = instance.sqrt();
 Double[] expected = new Double[5];
 Arrays.stream(expected).forEach(d -> expected[d.intValue()] = Math.sqrt(d.doubleValue()));
 Double[] actual = new Double[5];
 for (int i = 0; i < 5; i++) {
 actual[i] = result.get(i);
 }
 assertArrayEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteratorRemove() {
 AbstractInstance instance = createInstance(5, null);
 Iterator<Double> iterator = instance.iterator();
 iterator.remove();
 }
}