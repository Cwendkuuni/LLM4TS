package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparseInstanceTest {

 @Test
 public void testEmptySparseInstance() {
 SparseInstance emptyInstance = new SparseInstance();
 Assert.assertEquals(0, emptyInstance.noAttributes());
 Assert.assertTrue(emptyInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance instance = new SparseInstance(5, 0.0);
 Assert.assertEquals(5, instance.noAttributes());
 Assert.assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithData() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals(5, instance.noAttributes());
 Assert.assertFalse(instance.isEmpty());
 Assert.assertEquals(1.0, instance.get(0), 0.001);
 Assert.assertEquals(2.0, instance.get(1), 0.001);
 Assert.assertEquals(3.0, instance.get(2), 0.001);
 Assert.assertEquals(4.0, instance.get(3), 0.001);
 Assert.assertEquals(5.0, instance.get(4), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoAttributes() {
 SparseInstance instance = new SparseInstance(-1);
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.setNoAttributes(10);
 Assert.assertEquals(10, instance.noAttributes());
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 instance.clear();
 Assert.assertEquals(0, instance.noAttributes());
 Assert.assertTrue(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertTrue(instance.containsKey(0));
 Assert.assertTrue(instance.containsKey(1));
 Assert.assertTrue(instance.containsKey(2));
 Assert.assertTrue(instance.containsKey(3));
 Assert.assertTrue(instance.containsKey(4));
 Assert.assertFalse(instance.containsKey(5));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertTrue(instance.containsValue(1.0));
 Assert.assertFalse(instance.containsValue(6.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Map.Entry<Integer, Double> entry = instance.entrySet().iterator().next();
 Assert.assertEquals(0, entry.getKey().intValue());
 Assert.assertEquals(1.0, entry.getValue(), 0.001);
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals(1.0, instance.get(0), 0.001);
 Assert.assertEquals(2.0, instance.get(1), 0.001);
 Assert.assertEquals(3.0, instance.get(2), 0.001);
 Assert.assertEquals(4.0, instance.get(3), 0.001);
 Assert.assertEquals(5.0, instance.get(4), 0.001);
 Assert.assertEquals(0.0, instance.get(5), 0.001);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance emptyInstance = new SparseInstance();
 Assert.assertTrue(emptyInstance.isEmpty());
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 List<Integer> keys = new ArrayList<Integer>(instance.keySet());
 Assert.assertEquals(5, keys.size());
 Assert.assertEquals(0, keys.get(0).intValue());
 Assert.assertEquals(1, keys.get(1).intValue());
 Assert.assertEquals(2, keys.get(2).intValue());
 Assert.assertEquals(3, keys.get(3).intValue());
 Assert.assertEquals(4, keys.get(4).intValue());
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Assert.assertEquals(3, instance.noAttributes());
 Assert.assertEquals(1.0, instance.get(0), 0.001);
 Assert.assertEquals(2.0, instance.get(1), 0.001);
 Assert.assertEquals(3.0, instance.get(2), 0.001);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 map.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.putAll(map);
 Assert.assertEquals(3, instance.noAttributes());
 Assert.assertEquals(1.0, instance.get(0), 0.001);
 Assert.assertEquals(2.0, instance.get(1), 0.001);
 Assert.assertEquals(3.0, instance.get(2), 0.001);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals(5, instance.noAttributes());
 instance.remove(2);
 Assert.assertEquals(4, instance.noAttributes());
 Assert.assertEquals(1.0, instance.get(0), 0.001);
 Assert.assertEquals(4.0, instance.get(1), 0.001);
 Assert.assertEquals(5.0, instance.get(3), 0.001);
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 List<Double> values = new ArrayList<Double>(instance.values());
 Assert.assertEquals(5, values.size());
 Assert.assertEquals(1.0, values.get(0), 0.001);
 Assert.assertEquals(2.0, values.get(1), 0.001);
 Assert.assertEquals(3.0, values.get(2), 0.001);
 Assert.assertEquals(4.0, values.get(3), 0.001);
 Assert.assertEquals(5.0, values.get(4), 0.001);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals(5, instance.noAttributes());
 instance.removeAttribute(0);
 Assert.assertEquals(4, instance.noAttributes());
 instance.removeAttribute(4);
 Assert.assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals("{1.0=1.0, 2.0=2.0, 3.0=3.0, 4.0=4.0, 5.0=5.0}", instance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance instance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance instance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance instance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertTrue(instance1.equals(instance2));
 SparseInstance instance3 = new SparseInstance(new double[]{1.0, 2.1, 3.0, 4.0, 5.0});
 Assert.assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testCopy() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance copy = instance.copy();
 Assert.assertEquals(instance.noAttributes(), copy.noAttributes());
 Assert.assertEquals(instance.data, copy.data);
 Assert.assertEquals(instance.defaultValue, copy.defaultValue);
 Assert.assertEquals(instance.classValue, copy.classValue);
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<Integer>(Arrays.asList(2, 3));
 instance.removeAttributes(indices);
 Assert.assertEquals(3, instance.noAttributes());
 Assert.assertEquals(1.0, instance.get(0), 0.001);
 Assert.assertEquals(4.0, instance.get(1), 0.001);
 }

 @Test
 public void testInitiate() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 1.5, 2.0, 2.5, 3.0});
 Assert.assertEquals(5, instance.noAttributes());
 Assert.assertEquals(1.0, instance.get(0), 0.001);
 Assert.assertEquals(1.5, instance.get(1), 0.001);
 Assert.assertEquals(2.0, instance.get(2), 0.001);
 Assert.assertEquals(2.5, instance.get(3), 0.001);
 Assert.assertEquals(3.0, instance.get(4), 0.001);
 Assert.assertEquals(0.0, instance.get(5), 0.001);
 }

}