package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

 private static final double EPSILON = 1e-9;

 @Test
 public void testConstructor() {
 SparseInstance sparseInstance = new SparseInstance();
 assertNotNull(sparseInstance);
 SparseInstance sparseInstance1 = new SparseInstance(5);
 assertNotNull(sparseInstance1);
 SparseInstance sparseInstance2 = new SparseInstance(5, 2.5);
 assertNotNull(sparseInstance2);
 SparseInstance sparseInstance3 = new SparseInstance(5, "StringClass");
 assertNotNull(sparseInstance3);
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testEmptyConstructor() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 assertEquals(0, sparseInstance.size());
 }

 @Test
 public void testConstructFromArray() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance sparseInstance = new SparseInstance(datavector);
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals(5, sparseInstance.size());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], sparseInstance.value(i), EPSILON);
 }
 }

 @Test
 public void testConstructFromArrayWithDefaultValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, 0.5);
 assertEquals(4, sparseInstance.noAttributes());
 assertEquals(4, sparseInstance.size());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != 0.5) {
 assertEquals(datavector[i], sparseInstance.value(i), EPSILON);
 }
 }
 }

 @Test
 public void testConstructFromArrayWithClassValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, 0.5, "StringClass");
 assertEquals("StringClass", sparseInstance.classValue());
 }

 @Test
 public void testInitiate() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, 0.5);
 sparseInstance.initiate(datavector);
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals(5, sparseInstance.size());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != 0.5) {
 assertEquals(datavector[i], sparseInstance.value(i), EPSILON);
 }
 }
 }

 @Test
 public void testGetValue() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, sparseInstance.value(0), EPSILON);
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.clear();
 assertEquals(0, sparseInstance.size());
 }

 @Test
 public void testContainsKey() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testContainsValue() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 }

 @Test
 public void testEntrySet() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(1, entrySet.size());
 }

 @Test
 public void testGet() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), EPSILON);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(1, keySet.size());
 }

 @Test
 public void testPut() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(null, sparseInstance.get(Integer.valueOf(0)));
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), EPSILON);
 }

 @Test
 public void testPutAll() {
 SparseInstance sparseInstance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.size());
 assertEquals(1.0, sparseInstance.value(0), EPSILON);
 assertEquals(2.0, sparseInstance.value(1), EPSILON);
 }

 @Test
 public void testRemove() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), EPSILON);
 sparseInstance.remove(Integer.valueOf(0));
 assertEquals(0, sparseInstance.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testThrowExceptionWhenRemoveKeyWithEmptyMap() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.remove(Integer.valueOf(0));
 }

 @Test(expected = NullPointerException.class)
 public void testThrowExceptionWhenPutNullKey() {
 SparseInstance sparseInstance = new SparseInstance();
 try {
 sparseInstance.put(null, Double.valueOf(1.0));
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = NullPointerException.class)
 public void testThrowExceptionWhenPutNullValue() {
 SparseInstance sparseInstance = new SparseInstance();
 try {
 sparseInstance.put(Integer.valueOf(0), null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testSize() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.size());
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.size());
 }

 @Test
 public void testValues() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Collection<Double> values = sparseInstance.values();
 assertEquals(2, values.size());
 }

 @Test
 public void testNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.removeAttribute(0);
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(1, sparseInstance.size());
 }

 @Test
 public void testToString() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 String expected = "{" + "[1=1.0, 2=2.0]" + ";Classname}";
 assertEquals(expected, sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance sparseInstance1 = new SparseInstance();
 SparseInstance sparseInstance2 = new SparseInstance();
 sparseInstance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertNotEquals(sparseInstance1.hashCode(), sparseInstance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance();
 SparseInstance sparseInstance2 = new SparseInstance();
 SparseInstance sparseInstance3 = new SparseInstance();
 sparseInstance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance3.put(Integer.valueOf(1), Double.valueOf(1.0));
 assertFalse(sparseInstance1.equals(sparseInstance2));
 assertFalse(sparseInstance1.equals(sparseInstance3));
 assertTrue(sparseInstance1.equals(sparseInstance1));
 }

 @Test
 public void testCopy() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance copiedInstance = sparseInstance.copy();
 assertEquals(sparseInstance.noAttributes(), copiedInstance.noAttributes());
 assertEquals(sparseInstance.size(), copiedInstance.size());
 for (int i = 0; i < sparseInstance.noAttributes(); i++) {
 assertEquals(sparseInstance.value(i), copiedInstance.value(i), EPSILON);
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Integer> indices = new HashSet<>();
 indices.add(0);
 indices.add(1);
 sparseInstance.removeAttributes(indices);
 assertEquals(0, sparseInstance.noAttributes());
 assertEquals(0, sparseInstance.size());
 }

}