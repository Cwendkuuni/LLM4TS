package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

 // Test constructor with valid arguments
 @Test
 public void testDenseInstanceValidConstruction() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
 for (int i = 0; i < 3; i++) {
 assertEquals(i + 1.0, denseInstance.value(i), 0.001);
 }
 }

 // Test constructor with null arguments
 @Test(expected = IllegalArgumentException.class)
 public void testDenseInstanceNullArguments() {
 DenseInstance denseInstance = new DenseInstance(null);
 }

 // Test get method for valid index
 @Test
 public void testGetValidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(2.0, denseInstance.get(1), 0.001);
 }

 // Test get method for invalid index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInvalidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.get(4);
 }

 // Test set method with valid arguments
 @Test
 public void testSetValidArguments() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.set(1, 4.0);
 assertEquals(4.0, denseInstance.get(1), 0.001);
 }

 // Test set method with invalid index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetInvalidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.set(4, 4.0);
 }

 // Test putAll method with valid Map
 @Test
 public void testPutAllValidMap() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(0, 0.5);
 map.put(2, 5.0);
 denseInstance.putAll(map);
 assertEquals(0.5, denseInstance.get(0), 0.001);
 assertEquals(5.0, denseInstance.get(2), 0.001);
 }

 // Test putAll method with invalid Map size
 @Test(expected = IllegalArgumentException.class)
 public void testPutAllInvalidMapSize() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(0, 0.5);
 map.put(4, 5.0);
 denseInstance.putAll(map);
 }

 // Test removeAttribute method with valid index
 @Test
 public void testRemoveAttributeValidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 denseInstance.removeAttribute(2);
 assertEquals(4, denseInstance.noAttributes());
 assertFalse(denseInstance.containsKey(2));
 }

 // Test removeAttribute method with invalid index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeInvalidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 denseInstance.removeAttribute(6);
 }

 // Test clear method
 @Test
 public void testClear() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }

 // Test containsKey method with valid index
 @Test
 public void testContainsKeyValidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 }

 // Test containsKey method with invalid index
 @Test
 public void testContainsKeyInvalidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertFalse(denseInstance.containsKey(3));
 }

 // Test containsValue method with valid value
 @Test
 public void testContainsValueValidValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 double value = 2.0;
 assertTrue(denseInstance.containsValue(value));
 }

 // Test containsValue method with invalid value
 @Test
 public void testContainsValueInvalidValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 double value = 4.0;
 assertFalse(denseInstance.containsValue(value));
 }

 // Test entrySet method
 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
 assertEquals(3, entries.size());
 }

 // Test hashCode and equals methods
 @Test
 public void testHashCodeAndEquals() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
 assertEquals(denseInstance1, denseInstance1);
 assertEquals(denseInstance1, denseInstance2);
 assertNotEquals(denseInstance1, denseInstance3);
 }

 // Test noAttributes method
 @Test
 public void testNoAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
 }

 // Test size method (deprecated)
 @Test
 public void testSize() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.size());
 }

 // Test values method
 @Test
 public void testValues() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 List<Double> values = new ArrayList<>(denseInstance.values());
 assertEquals(3, values.size());
 }
}