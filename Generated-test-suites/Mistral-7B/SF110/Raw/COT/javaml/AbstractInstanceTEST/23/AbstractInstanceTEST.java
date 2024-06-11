package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

public class AbstractInstanceTest {

 private AbstractInstance createInstanceWithID(int id, Object classValue) {
 return new AbstractInstance() {
 @Override
 public int getID() {
 return id;
 }

 @Override
 public Iterator<Double> iterator() {
 return new InstanceValueIterator();
 }

 @Override
 protected AbstractInstance(Object classValue) {
 super();
 this.ID = id;
 this.classValue = classValue;
 }

 @Override
 public Object classValue() {
 return classValue;
 }

 @Override
 public void setClassValue(Object classValue) {
 // Do nothing in this test case
 }
 }.newInstance(classValue);
 }

 @Test
 public void testGetID() {
 AbstractInstance instance = createInstanceWithID(1, "class1");
 assertEquals(1, instance.getID());
 }

 @Test
 public void testIterator() {
 AbstractInstance instance = createInstanceWithID(2, "class2");
 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map.put(i, (double) i);
 instance.put(i, map.get(i));
 }
 Iterator<Double> iterator = instance.iterator();
 List<Double> expectedList = Arrays.asList(map.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveFromIterator() {
 AbstractInstance instance = createInstanceWithID(3, "class3");
 Iterator<Double> iterator = instance.iterator();
 iterator.next();
 iterator.remove();
 }

 @Test
 public void testConstructor() {
 AbstractInstance instance = new AbstractInstance();
 assertNull(instance.classValue);
 instance = new AbstractInstance(null);
 assertNull(instance.classValue);
 instance = new AbstractInstance("class1");
 assertEquals("class1", instance.classValue.toString());
 }

 @Test
 public void testEquals() {
 AbstractInstance instance1 = createInstanceWithID(4, "class4");
 AbstractInstance instance2 = createInstanceWithID(4, "class4");
 assertTrue(instance1.equals(instance2));

 AbstractInstance instance3 = new AbstractInstance();
 assertFalse(instance1.equals(instance3));

 AbstractInstance instance4 = new AbstractInstance(null);
 assertFalse(instance1.equals(instance4));

 AbstractInstance instance5 = new AbstractInstance("class5");
 assertFalse(instance1.equals(instance5));

 AbstractInstance instance6 = new AbstractInstance(new HashMap<>());
 assertFalse(instance1.equals(instance6));
 }

 @Test
 public void testMinusWithInstance() {
 AbstractInstance instance1 = createInstanceWithID(5, "class5");
 Map<Integer, Double> map1 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map1.put(i, (double) i);
 instance1.put(i, map1.get(i));
 }

 AbstractInstance instance2 = createInstanceWithID(6, "class6");
 Map<Integer, Double> map2 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map2.put(i, (double) (-i));
 instance2.put(i, map2.get(i));
 }

 AbstractInstance instance3 = instance1.minus(instance2);
 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 expectedMap.put(i, 2.0 * i);
 }
 Iterator<Double> iterator = instance3.iterator();
 List<Double> expectedList = Arrays.asList(expectedMap.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMinusWithInvalidArgument() {
 AbstractInstance instance = createInstanceWithID(7, "class7");
 assertNull(instance.classValue);
 try {
 instance.minus(null);
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testMinusWithDouble() {
 AbstractInstance instance1 = createInstanceWithID(8, "class8");
 Map<Integer, Double> map1 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map1.put(i, (double) i);
 instance1.put(i, map1.get(i));
 }

 double min = 2.5;
 AbstractInstance instance2 = instance1.minus(min);
 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 expectedMap.put(i, (double) (instance1.get(i) - min));
 }
 Iterator<Double> iterator = instance2.iterator();
 List<Double> expectedList = Arrays.asList(expectedMap.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDivideWithInstance() {
 AbstractInstance instance1 = createInstanceWithID(9, "class9");
 Map<Integer, Double> map1 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map1.put(i, (double) (i + 1));
 instance1.put(i, map1.get(i));
 }

 AbstractInstance instance2 = createInstanceWithID(10, "class10");
 Map<Integer, Double> map2 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map2.put(i, (double) (i + 2));
 instance2.put(i, map2.get(i));
 }

 AbstractInstance instance3 = instance1.divide(instance2);
 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 expectedMap.put(i, instance1.get(i) / instance2.get(i));
 }
 Iterator<Double> iterator = instance3.iterator();
 List<Double> expectedList = Arrays.asList(expectedMap.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDivideWithZero() {
 AbstractInstance instance = createInstanceWithID(11, "class11");
 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map.put(i, (double) (i + 1));
 instance.put(i, map.get(i));
 }
 try {
 instance.divide(new DenseInstance(new double[instance.noAttributes()]));
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testDivideWithDouble() {
 AbstractInstance instance1 = createInstanceWithID(12, "class12");
 Map<Integer, Double> map1 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map1.put(i, (double) (i + 1));
 instance1.put(i, map1.get(i));
 }

 double min = 2.0;
 AbstractInstance instance2 = instance1.divide(min);
 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 expectedMap.put(i, instance1.get(i) / min);
 }
 Iterator<Double> iterator = instance2.iterator();
 List<Double> expectedList = Arrays.asList(expectedMap.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultiplyWithInstance() {
 AbstractInstance instance1 = createInstanceWithID(13, "class13");
 Map<Integer, Double> map1 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map1.put(i, (double) (i + 1));
 instance1.put(i, map1.get(i));
 }

 AbstractInstance instance2 = createInstanceWithID(14, "class14");
 Map<Integer, Double> map2 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map2.put(i, (double) (i + 2));
 instance2.put(i, map2.get(i));
 }

 AbstractInstance instance3 = instance1.multiply(instance2);
 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 expectedMap.put(i, instance1.get(i) * instance2.get(i));
 }
 Iterator<Double> iterator = instance3.iterator();
 List<Double> expectedList = Arrays.asList(expectedMap.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiplyWithNull() {
 AbstractInstance instance = createInstanceWithID(15, "class15");
 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map.put(i, (double) (i + 1));
 instance.put(i, map.get(i));
 }
 try {
 instance.multiply(null);
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testMultiplyWithDouble() {
 AbstractInstance instance1 = createInstanceWithID(16, "class16");
 Map<Integer, Double> map1 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map1.put(i, (double) (i + 1));
 instance1.put(i, map1.get(i));
 }

 double value = 2.5;
 AbstractInstance instance2 = instance1.multiply(value);
 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 expectedMap.put(i, instance1.get(i) * value);
 }
 Iterator<Double> iterator = instance2.iterator();
 List<Double> expectedList = Arrays.asList(expectedMap.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAddWithInstance() {
 AbstractInstance instance1 = createInstanceWithID(17, "class17");
 Map<Integer, Double> map1 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map1.put(i, (double) i);
 instance1.put(i, map1.get(i));
 }

 AbstractInstance instance2 = createInstanceWithID(18, "class18");
 Map<Integer, Double> map2 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map2.put(i, (double) i);
 instance2.put(i, map2.get(i));
 }

 AbstractInstance instance3 = instance1.add(instance2);
 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 expectedMap.put(i, instance1.get(i) + instance2.get(i));
 }
 Iterator<Double> iterator = instance3.iterator();
 List<Double> expectedList = Arrays.asList(expectedMap.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAddWithDouble() {
 AbstractInstance instance1 = createInstanceWithID(19, "class19");
 Map<Integer, Double> map1 = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map1.put(i, (double) i);
 instance1.put(i, map1.get(i));
 }

 double min = 2.5;
 AbstractInstance instance2 = instance1.add(min);
 Map<Integer, Double> expectedMap = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 expectedMap.put(i, instance1.get(i) + min);
 }
 Iterator<Double> iterator = instance2.iterator();
 List<Double> expectedList = Arrays.asList(expectedMap.values().toArray(new Double[0]));
 while (iterator.hasNext()) {
 Double next = iterator.next();
 assertEquals(expectedList.get(0), next);
 expectedList = expectedList.subList(1, expectedList.size());
 }
 assertFalse(iterator.hasNext());
 }

 @Test(expected = NoSuchElementException.class)
 public void testIteratorNoElements() {
 AbstractInstance instance = createInstanceWithID(20, "class20");
 Iterator<Double> iterator = instance.iterator();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 try {
 iterator.next();
 fail();
 } catch (NoSuchElementException e) {
 // pass
 }
 }

 @Test
 public void testSqrt() {
 AbstractInstance instance = createInstanceWithID(21, "class21");
 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < 10; ++i) {
 map.put(i, Math.pow((double) i, 2.0));
 instance.put(i, map.get(i));
 }

 AbstractInstance instance2 = instance.sqrt();
 Set<Double> expectedSet = new HashSet<>();
 for (int i = 0; i < 10; ++i) {
 expectedSet.add(Math.sqrt(instance.get(i)));
 }
 Iterator<Double> iterator = instance2.iterator();
 Set<Double> actualSet = new HashSet<>();
 while (iterator.hasNext()) {
 Double next = iterator.next();
 actualSet.add(next);
 }
 assertEquals(expectedSet, actualSet);
 }

}