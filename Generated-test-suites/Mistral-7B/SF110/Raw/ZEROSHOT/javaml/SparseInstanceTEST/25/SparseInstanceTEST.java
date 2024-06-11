package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;
 private static final Object CLASS_VALUE = "classValue";

 @Test
 public void testSparseInstanceEmpty() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertNull(instance.get(0));
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertEquals(DEFAULT_VALUE, instance.get(0), 0.001);
 }

 @Test
 public void testSparseInstanceWithData() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(5, instance.noAttributes());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0).doubleValue(), 0.001);
 assertTrue(instance.containsKey(1));
 assertEquals(2.0, instance.get(1).doubleValue(), 0.001);
 // test get with non-existent key
 assertNull(instance.get(6));
 }

 @Test
 public void testSparseInstanceSetValue() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 instance.put(1, Double.valueOf(100.0));
 assertEquals(100.0, instance.get(1).doubleValue(), 0.001);
 }

 @Test
 public void testSparseInstanceClear() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertNull(instance.get(0));
 }

 @Test
 public void testSparseInstanceTrueContansKey() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertTrue(instance.containsKey(1));
 }

 @Test
 public void testSparseInstanceFalseContansKey() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertFalse(instance.containsKey(6));
 }

 @Test
 public void testSparseInstanceTrueContansValue() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertTrue(instance.containsValue(Double.valueOf(1.0)));
 }

 @Test
 public void testSparseInstanceFalseContansValue() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertFalse(instance.containsValue(Double.valueOf(6.0)));
 }

 @Test
 public void testSparseInstanceEntrySet() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
 assertEquals(5, entryList.size());
 assertEquals(1, entryList.get(0).getKey().intValue());
 assertEquals(1.0, entryList.get(0).getValue().doubleValue(), 0.001);
 }

 @Test
 public void testSparseInstanceSize() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(5, instance.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSparseInstanceUnsupportedOperationException() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 instance.put(null, Double.valueOf(1.0));
 }

 @Test
 public void testSparseInstanceNoAttributes() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testSparseInstanceRemoveAttribute() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 instance.removeAttribute(1);
 assertEquals(4, instance.noAttributes());
 }

 @Test
 public void testSparseInstanceCopy() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance copy = instance.copy();
 assertEquals(instance.noAttributes(), copy.noAttributes());
 assertArrayEquals(instance.getClassValue(), copy.getClassValue());
 for (int i = 0; i < instance.noAttributes(); i++) {
 Double value1 = instance.get(i);
 Double value2 = copy.get(i);
 assertEquals(value1, value2);
 }
 }

 @Test
 public void testSparseInstanceRemoveAttributes() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3, 4));
 instance.removeAttributes(indices);
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entryList);
 assertEquals(2, entryList.size());
 assertEquals(0, entryList.get(0).getKey().intValue());
 assertEquals(1.0, entryList.get(0).getValue().doubleValue(), 0.001);
 assertEquals(2, entryList.get(1).getKey().intValue());
 assertEquals(3.0, entryList.get(1).getValue().doubleValue(), 0.001);
 }
}