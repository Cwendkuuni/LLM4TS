package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

 private DenseInstance createInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithArray() {
 DenseInstance instance = createInstance(new double[]{}, null);
 assertNotNull(instance);
 assertEmpty(instance);

 DenseInstance instanceWithValues = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 assertEquals(3, instanceWithValues.noAttributes());
 for (int i = 0; i < 3; ++i) {
 assertEquals(i + 1.0, instanceWithValues.value(i), 0.0);
 }
 assertEquals(5.0, instanceWithValues.classValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance instance = createInstance(new double[0]);
 assertEmpty(instance);

 DenseInstance instanceWithValues = createInstance(5);
 assertEquals(5, instanceWithValues.noAttributes());
 for (int i = 0; i < 5; ++i) {
 assertTrue(instanceWithValues.containsKey(i));
 assertEquals(0.0, instanceWithValues.value(i), 0.0);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorWithNegativeSize() {
 new DenseInstance(-1);
 }

 @Test
 public void testGetValue() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 for (int i = 0; i < 3; ++i) {
 assertEquals(i + 1.0, instance.value(i), 0.0);
 }
 assertEquals(5.0, instance.value(-1), 0.0); // Should return the last value
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithInvalidIndex() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 instance.value(-1);
 }

 @Test
 public void testSetValue() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 instance.put(0, 4.0);
 assertEquals(4.0, instance.value(0), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetValueWithInvalidKey() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 instance.put(-1, 4.0);
 }

 @Test
 public void testClear() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 instance.clear();
 assertEmpty(instance);
 }

 @Test
 public void testContainsKeyWithIntegerKey() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 for (int i = 0; i < 3; ++i) {
 assertTrue(instance.containsKey(i));
 }
 assertFalse(instance.containsKey(-1));
 }

 @Test
 public void testContainsValueWithDoubleValue() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 assertTrue(instance.containsValue((double)1.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(3, entries.size());
 for (Map.Entry<Integer, Double> entry : entries) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 }

 @Test
 public void testGet() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 assertEquals(1.0, instance.get(0), 0.0);
 assertNull(instance.get(-1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidKey() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 instance.get(-1);
 }

 @Test
 public void testIsEmpty() {
 DenseInstance instance = new DenseInstance();
 assertTrue(instance.isEmpty());

 DenseInstance instanceWithValues = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 assertFalse(instanceWithValues.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 SortedSet<Integer> keys = instance.keySet();
 assertEquals(3, keys.size());
 for (int i = 0; i < 3; ++i) {
 assertEquals(i, keys.iterator().next());
 }
 }

 @Test
 public void testPut() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 instance.put(0, 4.0);
 assertEquals(4.0, instance.value(0), 0.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 4.0);
 map.put(1, 5.0);
 instance.putAll(map);
 assertEquals(4.0, instance.value(0), 0.0);
 assertEquals(5.0, instance.value(1), 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 instance.remove(0);
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 5.0);
 Set<Integer> indicesToRemove = new HashSet<>();
 indicesToRemove.add(2);
 indicesToRemove.add(4);
 instance.removeAttributes(indicesToRemove);
 assertEquals(3, instance.noAttributes());
 for (int i = 0; i < 3; ++i) {
 if (i != 1 && i != 2) {
 assertEquals(instance.value(i), i + 1.0, 0.0);
 } else {
 assertTrue(Double.isNaN(instance.value(i)));
 }
 }
 }

 @Test
 public void testToString() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 assertEquals("{1.0, 2.0, 3.0;5.0}", instance.toString());
 }

 @Test
 public void testHashCode() {
 DenseInstance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 DenseInstance instance2 = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 DenseInstance instance3 = createInstance(new double[]{1.1, 2.1, 3.1}, 5.0);
 assertEquals(hashCode(instance1), hashCode(instance2));
 assertNotEquals(hashCode(instance1), hashCode(instance3));
 }

 @Test
 public void testEquals() {
 DenseInstance instance1 = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 DenseInstance instance2 = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 DenseInstance instance3 = createInstance(new double[]{1.1, 2.1, 3.1}, 5.0);
 DenseInstance instance4 = createInstance(new double[]{1.0, 2.0, 3.0}, 6.0);
 assertTrue(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 assertFalse(instance1.equals(instance4));
 }

 @Test
 public void testCopy() {
 DenseInstance instance = createInstance(new double[]{1.0, 2.0, 3.0}, 5.0);
 DenseInstance copiedInstance = instance.copy();
 assertEquals(instance, copiedInstance);
 }

 private int hashCode(DenseInstance instance) {
 return instance.hashCode();
 }

 private void assertEmpty(DenseInstance instance) {
 assertTrue(instance.isEmpty());
 for (int i = 0; i < instance.noAttributes(); ++i) {
 assertEquals(0.0, instance.value(i), 0.0);
 }
 }
}