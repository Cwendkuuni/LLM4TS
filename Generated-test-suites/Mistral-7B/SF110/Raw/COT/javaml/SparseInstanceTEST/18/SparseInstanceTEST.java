package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstance_NoArguments() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstance_OneArgument() {
 sparseInstance = new SparseInstance(1);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstance_TwoArguments() {
 sparseInstance = new SparseInstance(2, 0.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstance_ThreeArguments() {
 sparseInstance = new SparseInstance(3, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.getClassValue());
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstance_DoubleArray_NoDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], sparseInstance.get(i), 0.0);
 }
 }

 @Test
 public void testSparseInstance_DoubleArray_WithDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.5);
 assertEquals(3, sparseInstance.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != 0.5) {
 assertEquals(datavector[i], sparseInstance.get(i), 0.0);
 } else {
 assertEquals(0.5, sparseInstance.get(i), 0.0);
 }
 }
 }

 @Test
 public void testSparseInstance_DoubleArray_WithClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.5, "ClassValue");
 assertEquals("ClassValue", sparseInstance.getClassValue());
 assertEquals(3, sparseInstance.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != 0.5) {
 assertEquals(datavector[i], sparseInstance.get(i), 0.0);
 } else {
 assertEquals(0.5, sparseInstance.get(i), 0.0);
 }
 }
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.value(0), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValue_OutOfBounds() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstance.value(3);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertEquals(3, entries.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.0);
 }

 @Test
 public void testGet_DefaultValue() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(0.0, sparseInstance.get(Integer.valueOf(3)), 0.0);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Integer> keys = sparseInstance.keySet();
 assertEquals(3, keys.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(5.0));
 assertEquals(5.0, sparseInstance.get(Integer.valueOf(0)), 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testPut_NullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(null, Double.valueOf(5.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPut_NullValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), null);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(5.0));
 map.put(Integer.valueOf(1), Double.valueOf(6.0));
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(5.0, sparseInstance.get(Integer.valueOf(0)), 0.0);
 assertEquals(6.0, sparseInstance.get(Integer.valueOf(1)), 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAll_NullMap() {
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(5.0));
 Double removedValue = sparseInstance.remove(Integer.valueOf(1));
 assertEquals(5.0, removedValue, 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testRemove_NullKey() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstance.remove(null);
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(5.0));
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = DeprecatedWarning.class)
 public void testSize_Deprecated() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(2, sparseInstance.size());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 Collection<Double> values = sparseInstance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 sparseInstance.removeAttribute(1);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(2.0, sparseInstance.get(Integer.valueOf(0)), 0.0);
 assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttribute_OutOfBounds() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 sparseInstance.removeAttribute(4);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(5.0));
 assertEquals("{" + sparseInstance.data.toString() + ";" + sparseInstance.getClassValue() + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(5.0));
 SparseInstance other = new SparseInstance();
 other.put(Integer.valueOf(0), Double.valueOf(5.0));
 assertEquals(sparseInstance.hashCode(), other.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(5.0));
 SparseInstance other = new SparseInstance();
 other.put(Integer.valueOf(0), Double.valueOf(5.0));
 assertTrue(sparseInstance.equals(other));
 }

 @Test
 public void testEquals_NotSameObject() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(5.0));
 SparseInstance other = new SparseInstance();
 assertFalse(sparseInstance.equals(other));
 }

 @Test
 public void testEquals_DifferentData() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(5.0));
 SparseInstance other = new SparseInstance();
 other.put(Integer.valueOf(1), Double.valueOf(5.0));
 assertFalse(sparseInstance.equals(other));
 }

 @Test
 public void testCopy() {
 SparseInstance original = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 original.put(Integer.valueOf(1), Double.valueOf(5.0));
 SparseInstance copy = original.copy();
 assertEquals(original.noAttributes(), copy.noAttributes());
 assertEquals(original.getData(), copy.getData());
 assertEquals(original.defaultValue, copy.defaultValue);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 sparseInstance.removeAttributes(indices);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(2.0, sparseInstance.get(Integer.valueOf(0)), 0.0);
 assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)), 0.0);
 }
}