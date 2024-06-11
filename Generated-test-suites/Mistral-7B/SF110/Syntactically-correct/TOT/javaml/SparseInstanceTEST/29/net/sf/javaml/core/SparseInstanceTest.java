package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

 @Test
 public void testSparseInstanceEmpty() {
 SparseInstance emptyInstance = new SparseInstance();
 assertEquals(0, emptyInstance.noAttributes());
 assertTrue(emptyInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceDefaultValue() {
 SparseInstance instance = new SparseInstance(5);
 instance.setDefaultValue(1.0);
 assertEquals(5, instance.noAttributes());
 for (int i = 0; i < 5; i++) {
 assertEquals(1.0, instance.get(i), 0.01);
 }
 }

 @Test
 public void testSparseInstanceWithValues() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 assertEquals(5, instance.noAttributes());
 for (int i = 0; i < 5; i++) {
 assertEquals(i + 1.1, instance.get(i), 0.01);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstanceNegativeAttributes() {
 SparseInstance instance = new SparseInstance(-1);
 fail("Expected an IllegalArgumentException but was not thrown");
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(5);
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 assertTrue(instance.containsKey(Integer.valueOf(0)));
 assertFalse(instance.containsKey(Integer.valueOf(6)));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 1.1, 2.2, 2.2, 3.3});
 assertTrue(instance.containsValue(1.1));
 assertFalse(instance.containsValue(4.4));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(5, entries.size());
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 for (int i = 0; i < 5; i++) {
 assertEquals(i + 1.1, instance.get(Integer.valueOf(i)), 0.01);
 }
 assertEquals(instance.getDefaultValue(), instance.get(new Integer(-1)));
 }

 @Test
 public void testIsEmpty() {
 SparseInstance emptyInstance = new SparseInstance();
 assertTrue(emptyInstance.isEmpty());
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 Set<Integer> keys = instance.keySet();
 assertEquals(5, keys.size());
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.1));
 instance.put(Integer.valueOf(1), Double.valueOf(2.2));
 assertEquals(2, instance.noAttributes());
 assertEquals(1.1, instance.get(Integer.valueOf(0)), 0.01);
 assertEquals(2.2, instance.get(Integer.valueOf(1)), 0.01);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.1));
 map.put(Integer.valueOf(1), Double.valueOf(2.2));
 instance.putAll(map);
 assertEquals(2, instance.noAttributes());
 assertEquals(1.1, instance.get(Integer.valueOf(0)), 0.01);
 assertEquals(2.2, instance.get(Integer.valueOf(1)), 0.01);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 Double value = instance.remove(Integer.valueOf(2));
 assertEquals(2.2, value, 0.01);
 assertEquals(4, instance.noAttributes());
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 assertEquals(5, instance.size());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 Collection<Double> values = instance.values();
 assertEquals(5, values.size());
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 SparseInstance instanceWithData = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 assertEquals(5, instanceWithData.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 instance.removeAttribute(2);
 assertEquals(4, instance.noAttributes());
 }
}