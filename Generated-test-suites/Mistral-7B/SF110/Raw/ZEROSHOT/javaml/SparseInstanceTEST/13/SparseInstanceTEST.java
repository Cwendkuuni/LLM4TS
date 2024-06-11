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
 private static final Object CLASS_VALUE = "testClass";

 @Test
 public void testSparseInstanceWithNoParameters() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributes() {
 SparseInstance instance = new SparseInstance(0);
 assertEquals(0, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance instance = new SparseInstance(10, DEFAULT_VALUE);
 assertEquals(10, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithClassValue() {
 SparseInstance instance = new SparseInstance(10, CLASS_VALUE);
 assertEquals(10, instance.noAttributes());
 assertEquals(CLASS_VALUE, instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDataVector() {
 double[] datavector = new double[] { 1.0, 2.0, 3.0 };
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 assertNull(instance.getClassValue());

 Map<Integer, Double> expectedData = new HashMap<Integer, Double>();
 expectedData.put(0, 1.0);
 expectedData.put(1, 2.0);
 expectedData.put(2, 3.0);
 assertEquals(expectedData, instance.data);
 }

 @Test
 public void testSparseInstanceWithDefaultValueAndDataVector() {
 double[] datavector = new double[] { 1.0, 2.0, 3.0 };
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(3, instance.noAttributes());
 assertNull(instance.getClassValue());

 Map<Integer, Double> expectedData = new HashMap<Integer, Double>();
 expectedData.put(0, 1.0);
 expectedData.put(1, 2.0);
 expectedData.put(2, 3.0);
 assertEquals(expectedData, instance.data);
 }

 @Test
 public void testSparseInstanceWithClassValueAndDataVector() {
 double[] datavector = new double[] { 1.0, 2.0, 3.0 };
 SparseInstance instance = new SparseInstance(datavector, CLASS_VALUE);
 assertEquals(3, instance.noAttributes());
 assertEquals(CLASS_VALUE, instance.getClassValue());

 Map<Integer, Double> expectedData = new HashMap<Integer, Double>();
 expectedData.put(0, 1.0);
 expectedData.put(1, 2.0);
 expectedData.put(2, 3.0);
 assertEquals(expectedData, instance.data);
 }

 @Test
 public void testSparseInstanceCopy() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 SparseInstance copiedInstance = instance.copy();
 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 assertEquals(instance.getClassValue(), copiedInstance.getClassValue());
 assertEquals(instance.data, copiedInstance.data);
 }

 @Test
 public void testSparseInstanceGet() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(3.0, instance.get(2), 0.0);
 assertEquals(DEFAULT_VALUE, instance.get(3), 0.0);
 }

 @Test(expected = NoSuchElementException.class)
 public void testSparseInstanceGetInvalidKey() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.get(-1);
 }

 @Test
 public void testSparseInstanceClear() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertTrue(instance.data.isEmpty());
 }

 @Test
 public void testSparseInstanceContainsKey() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testSparseInstanceContainsValue() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertTrue(instance.containsValue(1.0));
 assertTrue(instance.containsValue(2.0));
 assertTrue(instance.containsValue(3.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testSparseInstanceEntrySet() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testSparseInstancePut() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.put(Integer.valueOf(4), Double.valueOf(4.0));
 assertEquals(4, instance.noAttributes());
 assertEquals(4.0, instance.get(4), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstancePutNullKey() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.put(null, Double.valueOf(4.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstancePutNullValue() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.put(Integer.valueOf(4), null);
 }

 @Test
 public void testSparseInstancePutAll() {
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(Integer.valueOf(0), Double.valueOf(0.0));
 map.put(Integer.valueOf(1), Double.valueOf(1.0));
 map.put(Integer.valueOf(2), Double.valueOf(2.0));
 map.put(Integer.valueOf(3), Double.valueOf(3.0));

 SparseInstance instance = new SparseInstance();
 instance.putAll(map);
 assertEquals(4, instance.noAttributes());
 assertEquals(map, instance.data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstancePutAllNullMap() {
 SparseInstance instance = new SparseInstance();
 instance.putAll(null);
 }

 @Test
 public void testSparseInstanceRemove() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 instance.remove(0);
 assertEquals(3, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 }

 @Test(expected = NoSuchElementException.class)
 public void testSparseInstanceRemoveInvalidKey() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.remove(3);
 }

 @Test
 public void testSparseInstanceSize() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(3, instance.size());
 }

 @Deprecated
 @Test
 public void testSparseInstanceSizeDeprecated() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(3, instance.size());
 }

 @Test
 public void testSparseInstanceValues() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 List<Double> values = new ArrayList<Double>(instance.values());
 assertEquals(3, values.size());
 assertEquals(Arrays.asList(1.0, 2.0, 3.0), values);
 }

 @Test
 public void testSparseInstanceNoAttributes() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testSparseInstanceRemoveAttribute() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 instance.removeAttribute(1);
 assertEquals(3, instance.noAttributes());
 assertFalse(instance.containsKey(1));
 }

 @Test
 public void testSparseInstanceEquals() {
 SparseInstance instance1 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 SparseInstance instance2 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 SparseInstance instance3 = new SparseInstance(new double[] { 1.0, 2.0, 4.0 });
 SparseInstance instance4 = new SparseInstance(instance1);

 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 assertTrue(instance1.equals(instance4));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testSparseInstanceHashCode() {
 SparseInstance instance1 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 SparseInstance instance2 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 SparseInstance instance3 = new SparseInstance(new double[] { 1.0, 2.0, 4.0 });

 int hashcode1 = instance1.hashCode();
 int hashcode2 = instance2.hashCode();
 int hashcode3 = instance3.hashCode();

 assertTrue(instance1.hashCode() == hashcode1);
 assertTrue(instance2.hashCode() == hashcode1);
 assertFalse(instance1.hashCode() == hashcode2);
 assertFalse(instance1.hashCode() == hashcode3);
 }
}