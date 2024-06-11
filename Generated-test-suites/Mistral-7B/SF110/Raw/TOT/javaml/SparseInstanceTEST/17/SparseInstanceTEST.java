package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

 private SparseInstance instance;

 @Before
 public void setUp() {
 double[] datavector = {1.0, 2.0, 3.0};
 instance = new SparseInstance(datavector, 0.0, Instance.class);
 }

 @Test
 public void testConstructorWithDatavector() {
 assertEquals(3, instance.noAttributes());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0), 0.001);
 }

 @Test
 public void testSettingNoAttributes() {
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testValueMethod() {
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(0.0, instance.value(-1), 0.001);
 }

 @Test
 public void testClearMethod() {
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 }

 @Test
 public void testContainsKeyMethod() {
 assertTrue(instance.containsKey(0));
 assertFalse(instance.containsKey(4));
 }

 @Test
 public void testContainsValueMethod() {
 assertTrue(instance.containsValue(1.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testEntrySetMethod() {
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(3, entries.size());
 }

 @Test
 public void testGetMethod() {
 assertEquals(1.0, instance.get(0), 0.001);
 assertEquals(0.0, instance.get(-1), 0.001);
 }

 @Test
 public void testIsEmptyMethod() {
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 Set<Integer> keys = instance.keySet();
 assertEquals(3, keys.size());
 }

 @Test
 public void testPutMethod() {
 instance.put(1, 4.0);
 assertEquals(4.0, instance.get(1), 0.001);
 }

 @Test
 public void testPutAllMethod() {
 SparseInstance otherInstance = new SparseInstance();
 otherInstance.put(1, 5.0);
 otherInstance.put(2, 6.0);
 instance.putAll(otherInstance.entrySet());
 assertEquals(5, instance.noAttributes());
 assertEquals(6.0, instance.get(2), 0.001);
 }

 @Test
 public void testRemoveMethod() {
 instance.remove(1);
 assertFalse(instance.containsKey(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSizeMethodDeprecated() {
 int size = instance.size();
 fail("Method size() is deprecated");
 }

 @Test
 public void testValuesMethod() {
 Collection<Double> values = instance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributesMethod() {
 assertEquals(3, instance.noAttributes());
 instance.removeAttribute(0);
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testRemoveAttributeMethod() {
 instance.removeAttribute(1);
 assertFalse(instance.containsKey(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeOutOfBounds() {
 instance.removeAttribute(4);
 }
}