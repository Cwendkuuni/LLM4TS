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

 private SparseInstance createInstance(int noAttributes, double[] datavector, double defaultValue, Object classValue) {
 if (datavector == null) {
 return new SparseInstance(noAttributes, defaultValue, classValue);
 } else {
 return new SparseInstance(datavector, defaultValue, classValue);
 }
 }

 @Test
 public void testEmptyConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testConstructorWithDefaultValue() {
 SparseInstance instance = new SparseInstance(5, 0.0);
 assertEquals(5, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testConstructorWithClassValue() {
 SparseInstance instance = new SparseInstance(5, "ClassValue");
 assertEquals(5, instance.noAttributes());
 assertEquals("ClassValue", instance.classValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(datavector.length, instance.noAttributes());
 assertFalse(instance.isEmpty());

 for (int i = 0; i < datavector.length; ++i) {
 assertEquals(datavector[i], instance.value(i), 0.001);
 }
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, 0.0);
 assertEquals(datavector.length, instance.noAttributes());
 assertFalse(instance.isEmpty());

 for (int i = 0; i < datavector.length; ++i) {
 if (datavector[i] != 0.0) {
 assertEquals(datavector[i], instance.value(i), 0.001);
 } else {
 assertEquals(0.0, instance.value(i), 0.001);
 }
 }
 }

 @Test
 public void testConstructorWithDatavectorAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, "ClassValue");
 assertEquals(datavector.length, instance.noAttributes());
 assertEquals("ClassValue", instance.classValue());

 for (int i = 0; i < datavector.length; ++i) {
 if (datavector[i] != 0.0) {
 assertEquals(datavector[i], instance.value(i), 0.001);
 } else {
 assertEquals(0.0, instance.value(i), 0.001);
 }
 }
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.setNoAttributes(10);
 assertEquals(10, instance.noAttributes());
 }

 @Test
 public void testValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 for (int i = 0; i < datavector.length; ++i) {
 assertEquals(datavector[i], instance.value(i), 0.001);
 }

 assertEquals(0.0, instance.value(-1), 0.001);
 }

 @Test
 public void testClear() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.clear();

 assertEquals(0, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 for (int i = 0; i < datavector.length; ++i) {
 assertTrue(instance.containsKey(Integer.valueOf(i)));
 }

 assertTrue(instance.containsKey(Integer.valueOf(-1)));
 }

 @Test
 public void testContainsValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 for (int i = 0; i < datavector.length; ++i) {
 assertTrue(instance.containsValue(Double.valueOf(datavector[i])));
 }

 assertTrue(instance.containsValue(Double.valueOf(0.0)));
 }

 @Test
 public void testEntrySet() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<Map.Entry<Integer, Double>>(entrySet);

 assertEquals(datavector.length, entryList.size());

 for (int i = 0; i < datavector.length; ++i) {
 Map.Entry<Integer, Double> entry = entryList.get(i);
 assertEquals(Integer.valueOf(i), entry.getKey());
 assertEquals(Double.valueOf(datavector[i]), entry.getValue());
 }
 }

 @Test
 public void testGet() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 for (int i = 0; i < datavector.length; ++i) {
 assertEquals(datavector[i], instance.get(Integer.valueOf(i)).doubleValue(), 0.001);
 }

 assertEquals(0.0, instance.get(Integer.valueOf(-1)).doubleValue(), 0.001);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());

 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 instance = new SparseInstance(datavector);
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 Set<Integer> keySet = instance.keySet();
 List<Integer> keyList = new ArrayList<Integer>(keySet);

 assertEquals(datavector.length, keyList.size());

 for (int i = 0; i < datavector.length; ++i) {
 assertEquals(Integer.valueOf(i), keyList.get(i));
 }
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, instance.noAttributes());
 assertEquals(1.0, instance.value(0), 0.001);

 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(2.0, instance.value(1), 0.001);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));

 instance.putAll(map);
 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(2.0, instance.value(1), 0.001);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));

 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(2.0, instance.value(1), 0.001);

 instance.remove(Integer.valueOf(0));
 assertEquals(1, instance.noAttributes());
 assertEquals(2.0, instance.value(0), 0.001);
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));

 assertEquals(2, instance.noAttributes());
 assertEquals(2, instance.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSizeDeprecated() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));

 instance.size();
 }

 @Test
 public void testValues() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 List<Double> valueList = new ArrayList<Double>(instance.values());
 assertEquals(datavector.length, valueList.size());

 for (int i = 0; i < datavector.length; ++i) {
 assertEquals(datavector[i], valueList.get(i), 0.001);
 }
 }

 @Test
 public void testNoAttributes() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 assertEquals(datavector.length, instance.noAttributes());

 instance.removeAttribute(0);
 assertEquals(4, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 instance.removeAttribute(0);
 assertEquals(4, instance.noAttributes());
 assertEquals(2.0, instance.value(0), 0.001);
 }

 @Test
 public void testToString() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);

 assertEquals("{" + Arrays.toString(datavector) + ";ClassValue}", instance.toString());
 }

 @Test
 public void testHashCode() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance1 = new SparseInstance(datavector);
 SparseInstance instance2 = new SparseInstance(datavector);

 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEquals() {
 double[] datavector1 = {1.0, 2.0, 3.0, 4.0, 5.0};
 double[] datavector2 = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance1 = new SparseInstance(datavector1);
 SparseInstance instance2 = new SparseInstance(datavector2);
 SparseInstance instance3 = new SparseInstance();

 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testCopy() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 SparseInstance copiedInstance = instance.copy();

 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 assertEquals(instance.size(), copiedInstance.size());

 for (int i = 0; i < datavector.length; ++i) {
 assertEquals(instance.value(i), copiedInstance.value(i), 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 Set<Integer> indices = new HashSet<Integer>(Arrays.asList(2, 4));
 SparseInstance instance = new SparseInstance(datavector);

 instance.removeAttributes(indices);
 assertEquals(3, instance.noAttributes());
 }
}