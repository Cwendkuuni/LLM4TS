package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

public class AbstractInstanceTest {

 private static final int NUM_ATTRIBUTES = 3;

 @Test
 public void testAbstractInstanceConstruction() {
 AbstractInstance instance = new AbstractInstance();
 AbstractInstance instanceWithValue = new AbstractInstance("classValue");

 assertEquals(0, instance.getID());
 assertEquals(null, instance.classValue());

 assertEquals(-1, instanceWithValue.getID());
 assertEquals("classValue", instanceWithValue.classValue());
 }

 @Test
 public void testGetID() {
 AbstractInstance instance = new AbstractInstance();
 AbstractInstance instanceWithValue = new AbstractInstance("classValue");

 assertEquals(0, instance.getID());
 assertEquals(-1, instanceWithValue.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = new AbstractInstance();
 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map.put(i, (double) i);
 instance.put(i, i);
 }

 AbstractInstance.InstanceValueIterator iterator = instance.iterator();

 assertTrue(iterator.hasNext());
 assertEquals(0.0, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(1.0, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(2.0, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMinus() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();

 Map<Integer, Double> map1 = new HashMap<>();
 Map<Integer, Double> map2 = new HashMap<>();

 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map1.put(i, (double) i);
 map2.put(i, (double) i);

 instance1.put(i, i);
 instance2.put(i, i);
 }

 AbstractInstance result = instance1.minus(instance2);

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, 0.0);
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

 @Test
 public void testMinusWithValue() {
 AbstractInstance instance = new AbstractInstance();

 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map.put(i, (double) i);
 instance.put(i, i);
 }

 AbstractInstance result = instance.minus((double) 1.0);

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, i - 1.0);
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

 @Test
 public void testDivide() {
 AbstractInstance instance = new AbstractInstance();

 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map.put(i, (double) i);
 instance.put(i, i);
 }

 AbstractInstance result = instance.divide((double) 2.0);

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, i / 2.0);
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

 @Test
 public void testMultiply() {
 AbstractInstance instance = new AbstractInstance();

 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map.put(i, (double) i);
 instance.put(i, i);
 }

 AbstractInstance result = instance.multiply((double) 2.0);

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, i * 2.0);
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

 @Test
 public void testHashcode() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();

 Map<Integer, Double> map1 = new HashMap<>();
 Map<Integer, Double> map2 = new HashMap<>();

 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map1.put(i, (double) i);
 map2.put(i, (double) i);

 instance1.put(i, i);
 instance2.put(i, i);
 }

 assertEquals(instance1.getID(), instance1.hashCode());
 assertEquals(instance2.getID(), instance2.hashCode());

 instance1.put(0, instance1.getValue(0) + 1);
 assertNotEquals(instance1.getID(), instance1.hashCode());
 }

 @Test
 public void testEquals() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();
 AbstractInstance instance3 = new AbstractInstance();
 AbstractInstance instance4 = instance1;

 Map<Integer, Double> map1 = new HashMap<>();
 Map<Integer, Double> map2 = new HashMap<>();
 Map<Integer, Double> map3 = new HashMap<>();
 Map<Integer, Double> map4 = new HashMap<>();

 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map1.put(i, (double) i);
 map2.put(i, (double) i);
 map3.put(i, (double) i);
 map4.put(i, (double) i);

 instance1.put(i, i);
 instance2.put(i, i);
 instance3.put(i, i);
 instance4.put(i, i);
 }

 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 assertTrue(instance1.equals(instance3));
 assertTrue(instance1.equals(instance4));

 assertFalse(instance1.equals(new Object()));
 assertFalse(instance1.equals(null));
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();

 Map<Integer, Double> map1 = new HashMap<>();
 Map<Integer, Double> map2 = new HashMap<>();

 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map1.put(i, (double) i);
 map2.put(i, (double) i);

 instance1.put(i, i);
 instance2.put(i, i);
 }

 AbstractInstance result = instance1.multiply(instance2);

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, i * i);
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();

 Map<Integer, Double> map1 = new HashMap<>();
 Map<Integer, Double> map2 = new HashMap<>();

 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map1.put(i, (double) i);
 map2.put(i, (double) i);

 instance1.put(i, i);
 instance2.put(i, i);
 }

 AbstractInstance result = instance1.divide(instance2);

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, 1.0);
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

 @Test
 public void testAddWithValue() {
 AbstractInstance instance = new AbstractInstance();

 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map.put(i, (double) i);
 instance.put(i, i);
 }

 AbstractInstance result = instance.add((double) 1.0);

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, i + 1.0);
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance instance1 = new AbstractInstance();
 AbstractInstance instance2 = new AbstractInstance();

 Map<Integer, Double> map1 = new HashMap<>();
 Map<Integer, Double> map2 = new HashMap<>();

 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map1.put(i, (double) i);
 map2.put(i, (double) i);

 instance1.put(i, i);
 instance2.put(i, i);
 }

 AbstractInstance result = instance1.add(instance2);

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, i + i);
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = new AbstractInstance();

 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 map.put(i, Math.pow((double) i, 2.0));
 instance.put(i, Math.pow((double) i, 2.0));
 }

 AbstractInstance result = instance.sqrt();

 Map<Integer, Double> mapResult = new HashMap<>();
 for (int i = 0; i < NUM_ATTRIBUTES; i++) {
 mapResult.put(i, Math.sqrt((double) i));
 result.put(i, result.getValue(i));
 }

 assertTrue(Arrays.equals(mapResult.values().toArray(), result.iterator().asList().toArray()));
 }

}