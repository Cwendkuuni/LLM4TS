package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 private static final int DEFAULT_NO_ATTRIBUTES = 5;
 private static final double DEFAULT_VALUE = 0.5;

 @Test
 public void testSparseInstanceEmptyConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(DEFAULT_VALUE, instance.getDefaultValue());
 assertFalse(instance.containsKey(0));
 assertNull(instance.getData());
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithNumberOfAttributes() {
 SparseInstance instance = new SparseInstance(DEFAULT_NO_ATTRIBUTES);
 assertEquals(DEFAULT_VALUE, instance.getDefaultValue());
 assertFalse(instance.containsKey(0));
 assertNull(instance.getData());
 assertEquals(DEFAULT_NO_ATTRIBUTES, instance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance instance = new SparseInstance(DEFAULT_NO_ATTRIBUTES, DEFAULT_VALUE);
 assertEquals(DEFAULT_VALUE, instance.getDefaultValue());
 assertFalse(instance.containsKey(0));
 assertNull(instance.getData());
 assertEquals(DEFAULT_NO_ATTRIBUTES, instance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithClassValue() {
 SparseInstance instance = new SparseInstance(DEFAULT_NO_ATTRIBUTES, Object.class);
 assertEquals(Object.class, instance.getClassValue());
 assertFalse(instance.containsKey(0));
 assertNull(instance.getData());
 assertEquals(DEFAULT_NO_ATTRIBUTES, instance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithInitialDataVector() {
 double[] datavector = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(0.0, instance.getDefaultValue());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0), 0.001);
 assertEquals(Defaults.defaultNoAttributes(), instance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithInitialDataVectorAndDefaultValue() {
 double[] datavector = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, 2.0);
 assertEquals(2.0, instance.getDefaultValue());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0), 0.001);
 assertEquals(Defaults.defaultNoAttributes(), instance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithInitialDataVectorAndClassValue() {
 double[] datavector = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, Object.class);
 assertEquals(Object.class, instance.getClassValue());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0), 0.001);
 assertEquals(Defaults.defaultNoAttributes(), instance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithInitialDataVectorAndDefaultValueAndClassValue() {
 double[] datavector = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, 2.0, Object.class);
 assertEquals(2.0, instance.getDefaultValue());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0), 0.001);
 assertEquals(Defaults.defaultNoAttributes(), instance.noAttributes());
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.setNoAttributes(10);
 assertEquals(10, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeNumber() {
 SparseInstance instance = new SparseInstance(5);
 instance.setNoAttributes(-1);
 }

 @Test
 public void testValue() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertEquals(1.0, instance.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueNegativeIndex() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.value(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueBeyondAttributesNumber() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.value(6));
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 instance.clear();
 assertFalse(instance.containsKey(0));
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKeyNullKey() {
 SparseInstance instance = new SparseInstance(5);
 assertFalse(instance.containsKey(null));
 }

 @Test
 public void testContainsKeyExistingKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertTrue(instance.containsKey(0));
 }

 @Test
 public void testContainsValueNullValue() {
 SparseInstance instance = new SparseInstance(5);
 assertFalse(instance.containsValue(null));
 }

 @Test
 public void testContainsValueExistingValue() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertTrue(instance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(1, entrySet.size());
 }

 @Test
 public void testGetNullKey() {
 SparseInstance instance = new SparseInstance(5);
 assertNull(instance.get(null));
 }

 @Test
 public void testGetExistingKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertEquals(1.0, instance.get(0), 0.001);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testIsEmptyAfterPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(5);
 Set<Integer> keySet = instance.keySet();
 assertTrue(keySet.isEmpty());
 }

 @Test
 public void testKeySetAfterPut() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 Set<Integer> keySet = instance.keySet();
 assertEquals(1, keySet.size());
 }

 @Test
 public void testPutNullKey() {
 SparseInstance instance = new SparseInstance(5);
 assertNull(instance.put(null, 1.0));
 }

 @Test
 public void testPutExistingKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 Double previousValue = instance.put(0, 2.0);
 assertEquals(1.0, previousValue.doubleValue(), 0.001);
 }

 @Test
 public void testPutNewKey() {
 SparseInstance instance = new SparseInstance(5);
 Double previousValue = instance.put(1, 2.0);
 assertNull(previousValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 SparseInstance instance = new SparseInstance(5);
 assertThrows(NullPointerException.class, () -> instance.put(0, null));
 }

 @Test
 public void testPutAllEmptyMap() {
 Map<Integer, Double> map = new HashMap<>();
 SparseInstance instance = new SparseInstance();
 instance.putAll(map);
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testPutAllMapWithData() {
 SparseInstance instance = new SparseInstance(5);
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 1.0);
 map.put(1, 2.0);
 instance.putAll(map);
 assertEquals(2, instance.noAttributes());
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 }

 @Test
 public void testRemoveNullKey() {
 SparseInstance instance = new SparseInstance(5);
 assertNull(instance.remove(null));
 }

 @Test(expected = KeyNotFoundException.class)
 public void testRemoveNonExistentKey() {
 SparseInstance instance = new SparseInstance(5);
 assertThrows(KeyNotFoundException.class, () -> instance.remove(0));
 }

 @Test
 public void testRemoveExistentKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 Double removedValue = instance.remove(0);
 assertEquals(1.0, removedValue.doubleValue(), 0.001);
 assertFalse(instance.containsKey(0));
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(0, instance.size());
 }

 @Test
 public void testSizeAfterPut() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertEquals(1, instance.size());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 List<Double> values = new ArrayList<>(instance.values());
 assertEquals(1, values.size());
 assertEquals(1.0, values.get(0).doubleValue(), 0.001);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 instance.removeAttribute(0);
 assertFalse(instance.containsKey(0));
 assertEquals(4, instance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeNegativeIndex() {
 SparseInstance instance = new SparseInstance(5);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.removeAttribute(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeBeyondAttributesNumber() {
 SparseInstance instance = new SparseInstance(5);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.removeAttribute(6));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributeFrozenSet() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
 assertThrows(UnsupportedOperationException.class, () -> instance.removeAttribute(2));
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 String toString = instance.toString();
 assertTrue(toString.contains("{"));
 assertTrue(toString.contains("}"));
 assertTrue(toString.contains(":"));
 assertTrue(toString.contains("value"));
 assertTrue(toString.contains("classValue"));
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = new SparseInstance(5);
 instance1.put(0, 1.0);
 SparseInstance instance2 = new SparseInstance(5);
 instance2.put(0, 1.0);
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEqualsNullObject() {
 SparseInstance instance = new SparseInstance(5);
 assertFalse(instance.equals(null));
 }

 @Test
 public void testEqualsSameObject() {
 SparseInstance instance = new SparseInstance(5);
 assertTrue(instance.equals(instance));
 }

 @Test
 public void testEqualsDifferentClass() {
 SparseInstance instance = new SparseInstance(5);
 assertFalse(instance.equals(new Object()));
 }

 @Test
 public void testEqualsDifferentInstance() {
 SparseInstance instance1 = new SparseInstance(5);
 instance1.put(0, 1.0);
 SparseInstance instance2 = new SparseInstance(5);
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testCopy() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 SparseInstance copy = instance.copy();
 assertEquals(instance.noAttributes(), copy.noAttributes());
 assertEquals(instance.getDefaultValue(), copy.getDefaultValue());
 assertEquals(instance.getClassValue(), copy.getClassValue());
 for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
 Map.Entry<Integer, Double> copyEntry = copy.entrySet().iterator().next();
 assertEquals(entry.getKey(), copyEntry.getKey());
 assertEquals(entry.getValue(), copyEntry.getValue(), 0.001);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributesNullSet() {
 SparseInstance instance = new SparseInstance(5);
 assertThrows(UnsupportedOperationException.class, () -> instance.removeAttributes(null));
 }

 @Test
 public void testRemoveAttributesEmptySet() {
 SparseInstance instance = new SparseInstance(5);
 Set<Integer> emptySet = new HashSet<>();
 instance.removeAttributes(emptySet);
 assertTrue(instance.noAttributes() > 0);
 }

 @Test
 public void testRemoveAttributesExistingIndices() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(0));
 instance.removeAttributes(indicesToRemove);
 assertFalse(instance.containsKey(0));
 assertEquals(4, instance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributesNegativeIndex() {
 SparseInstance instance = new SparseInstance(5);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.removeAttribute(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributesBeyondAttributesNumber() {
 SparseInstance instance = new SparseInstance(5);
 assertThrows(IndexOutOfBoundsException.class, () -> instance.removeAttribute(6));
 }
}