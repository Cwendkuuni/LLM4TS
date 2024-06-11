package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance instance;

 @Before
 public void setUp() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 instance = new SparseInstance(datavector, 0.0, "ClassValue");
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 SparseInstance sparseInstance1 = new SparseInstance();
 assertEquals(0, sparseInstance1.noAttributes());
 // Test constructor with noAttributes
 SparseInstance sparseInstance2 = new SparseInstance(10);
 assertEquals(10, sparseInstance2.noAttributes());
 // Test constructor with defaultValue
 SparseInstance sparseInstance3 = new SparseInstance(10, 2.5);
 assertEquals(10, sparseInstance3.noAttributes());
 // Test constructor with classValue
 SparseInstance sparseInstance4 = new SparseInstance(10, 2.5, "ClassValue");
 assertEquals("ClassValue", sparseInstance4.getClassValue());
 }

 @Test
 public void testInitiate() {
 // Test initiate with empty array
 instance.clear();
 instance.initiate(new double[0]);
 assertEquals(0, instance.noAttributes());
 // Test initiate with non-empty array
 instance.clear();
 instance.initiate(datavector);
 assertEquals(4, instance.noAttributes());
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 Iterator<Map.Entry<Integer, Double>> iterator = entrySet.iterator();
 Map.Entry<Integer, Double> entry;
 while (iterator.hasNext()) {
 entry = iterator.next();
 assertEquals(entry.getKey().intValue(), entry.getKey().hashCode());
 assertEquals(entry.getValue().doubleValue(), datavector[entry.getKey().intValue()]);
 }
 }

 @Test
 public void testValue() {
 for (int i = 0; i < instance.noAttributes(); ++i) {
 assertEquals(datavector[i], instance.value(i));
 }
 assertEquals(instance.defaultValue, instance.value(-1));
 }

 @Test
 public void testClear() {
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 for (int i = 0; i < instance.noAttributes(); ++i) {
 assertTrue(instance.containsKey(Integer.valueOf(i)));
 }
 assertFalse(instance.containsKey(Integer.valueOf(-1)));
 }

 @Test
 public void testContainsValue() {
 for (int i = 0; i < instance.noAttributes(); ++i) {
 assertTrue(instance.containsValue(Double.valueOf(datavector[i])));
 }
 assertFalse(instance.containsValue(Double.valueOf(-1.0)));
 }

 @Test
 public void testEntrySet() {
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(instance.noAttributes(), entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 assertEquals(entry.getKey().intValue(), entry.getKey().hashCode());
 assertEquals(entry.getValue().doubleValue(), datavector[entry.getKey().intValue()]);
 }
 }

 @Test
 public void testGet() {
 for (int i = 0; i < instance.noAttributes(); ++i) {
 assertEquals(datavector[i], instance.get(Integer.valueOf(i)));
 }
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(-1)));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(instance.isEmpty());
 instance.clear();
 assertTrue(instance.isEmpty());
 instance.initiate(datavector);
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 Set<Integer> keySet = instance.keySet();
 assertEquals(instance.noAttributes(), keySet.size());
 for (Integer key : keySet) {
 assertEquals(key.intValue(), key.hashCode());
 }
 }

 @Test
 public void testPut() {
 // Test put with existing key
 instance.put(Integer.valueOf(2), Double.valueOf(5.5));
 assertEquals(instance.noAttributes() + 1, instance.noAttributes());
 assertEquals(5.5, instance.get(Integer.valueOf(2)));
 // Test put with non-existing key
 instance.put(Integer.valueOf(-1), Double.valueOf(5.5));
 assertEquals(instance.noAttributes() + 1, instance.noAttributes());
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(-1)));
 }

 @Test
 public void testPutAll() {
 // Test putAll with empty map
 Map<Integer, Double> map = new HashMap<>();
 instance.putAll(map);
 assertEquals(instance.noAttributes(), instance.noAttributes());
 // Test putAll with non-empty map
 Map<Integer, Double> map2 = new HashMap<>();
 map2.put(Integer.valueOf(5), Double.valueOf(5.5));
 instance.putAll(map2);
 assertEquals(instance.noAttributes() + 1, instance.noAttributes());
 assertEquals(5.5, instance.get(Integer.valueOf(5)));
 }

 @Test
 public void testRemove() {
 // Test remove with existing key
 instance.put(Integer.valueOf(2), Double.valueOf(5.5));
 instance.remove(Integer.valueOf(2));
 assertEquals(instance.noAttributes(), instance.noAttributes() - 1);
 assertFalse(instance.containsKey(Integer.valueOf(2)));
 // Test remove with non-existing key
 instance.remove(Integer.valueOf(-1));
 assertEquals(instance.noAttributes(), instance.noAttributes());
 }

 @Test
 public void testSize() {
 // Deprecated method, not using it
 }

 @Test
 public void testValues() {
 List<Double> values = new ArrayList<>(instance.values());
 assertEquals(instance.noAttributes(), values.size());
 for (int i = 0; i < values.size(); ++i) {
 assertEquals(datavector[i], values.get(i));
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(4, instance.noAttributes());
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 // Test removeAttribute with existing index
 instance.removeAttribute(2);
 assertEquals(instance.noAttributes() - 1, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(2)));
 // Test removeAttribute with non-existing index
 instance.removeAttribute(-1);
 assertEquals(instance.noAttributes(), instance.noAttributes());
 }

 @Test
 public void testToString() {
 String toString = instance.toString();
 String expectedToString = "{" + Arrays.toString(datavector) + ";ClassValue}";
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testHashCode() {
 int hashCode = instance.hashCode();
 SparseInstance sparseInstance5 = new SparseInstance();
 sparseInstance5.initiate(datavector);
 sparseInstance5.setClassValue("ClassValue");
 int hashCode2 = sparseInstance5.hashCode();
 assertEquals(hashCode, hashCode2);
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance5 = new SparseInstance();
 sparseInstance5.initiate(datavector);
 sparseInstance5.setClassValue("ClassValue");
 // Test equals with same object
 assertTrue(instance.equals(instance));
 // Test equals with different class
 assertFalse(instance.equals(new Object()));
 // Test equals with different SparseInstance
 assertFalse(instance.equals(sparseInstance5));
 // Test equals with null
 assertFalse(instance.equals(null));
 }

 @Test
 public void testCopy() {
 SparseInstance copiedInstance = instance.copy();
 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 for (int i = 0; i < instance.noAttributes(); ++i) {
 assertEquals(instance.value(i), copiedInstance.value(i));
 }
 }

 @Test
 public void testRemoveAttributes() {
 Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3));
 instance.removeAttributes(indices);
 assertEquals(instance.noAttributes() - 2, instance.noAttributes());
 for (int i = 0; i < instance.noAttributes(); ++i) {
 if (instance.noAttributes() > i && !indices.contains(i)) {
 assertEquals(instance.value(i), datavector[i]);
 } else {
 assertEquals(instance.defaultValue, instance.value(i));
 }
 }
 }
}