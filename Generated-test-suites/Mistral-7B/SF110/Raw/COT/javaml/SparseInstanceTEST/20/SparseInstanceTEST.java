package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceDefaultConstructor() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceConstructorWithNoAttributes() {
 sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceConstructorWithNoAttributesAndDefaultValue() {
 sparseInstance = new SparseInstance(0, 0.0);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceConstructorWithNoAttributesAndClassValue() {
 sparseInstance = new SparseInstance(0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 }

 @Test
 public void testSparseInstanceConstructorWithNoAttributesDefaultValueAndClassValue() {
 sparseInstance = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 }

 @Test
 public void testSparseInstanceConstructorWithDatavector() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceConstructorWithDatavectorAndDefaultValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceConstructorWithDatavectorAndClassValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 }

 @Test
 public void testSparseInstanceConstructorWithDatavectorDefaultValueAndClassValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(-1);
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 }

 @Test
 public void testValueOutOfBounds() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(0.0, sparseInstance.value(3), 0.001);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(sparseInstance.isEmpty());
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 assertFalse(sparseInstance.containsKey(Integer.valueOf(4)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 assertFalse(sparseInstance.containsValue(Double.valueOf(4.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertEquals(3, entries.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(0.0, sparseInstance.get(Integer.valueOf(3)), 0.001);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetOutOfBounds() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.get(Integer.valueOf(4));
 }

 @Test
 public void testInitiate() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.initiate(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(null, Double.valueOf(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutSameKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 }

 @Test
 public void testPutAll() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{4.0, 5.0, 6.0});
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(3), Double.valueOf(3.0));
 sparseInstance1.putAll(map);
 assertEquals(6, sparseInstance1.noAttributes());
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllNullMap() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.remove(Integer.valueOf(1));
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveOutOfBounds() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.remove(Integer.valueOf(4));
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.removeAttribute(1);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 String result = sparseInstance.toString();
 assertTrue(result.contains("{"));
 assertTrue(result.contains("}"));
 }

 @Test
 public void testHashCode() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertNotSame(sparseInstance1.hashCode(), sparseInstance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance1.equals(sparseInstance2));
 }

 @Test
 public void testEqualsSelf() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.equals(sparseInstance));
 }

 @Test
 public void testEqualsDifferentClasses() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Object obj = new Object();
 assertFalse(sparseInstance.equals(obj));
 }

 @Test
 public void testEqualsDifferentData() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{2.0, 3.0, 4.0});
 assertFalse(sparseInstance1.equals(sparseInstance2));
 }

 @Test
 public void testEqualsSameDataDifferentDefaultValue() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0}, 0.1);
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0}, 0.2);
 assertFalse(sparseInstance1.equals(sparseInstance2));
 }

 @Test
 public void testCopy() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance copiedInstance = sparseInstance.copy();
 assertEquals(sparseInstance, copiedInstance);
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<>();
 indices.add(2);
 indices.add(4);
 sparseInstance.removeAttributes(indices);
 assertEquals(3, sparseInstance.noAttributes());
 }

}