package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithDoubleArray() {
 double[] attributes = new double[] {1.0, 2.0, 3.0};
 DenseInstance denseInstance = createDenseInstance(attributes, null);
 assertEquals(3, denseInstance.noAttributes());
 assertArrayEquals(attributes, denseInstance.getAttributes(), 0.0);
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 double[] attributes = new double[] {1.0, 2.0, 3.0};
 DenseInstance denseInstance = createDenseInstance(attributes, "ClassValue");
 assertEquals("ClassValue", denseInstance.classValue().toString());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(3);
 assertEquals(3, denseInstance.noAttributes());
 assertNotNull(denseInstance.getAttributes());
 assertArrayIsNull(denseInstance.getAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSize() {
 new DenseInstance(-3);
 }

 @Test
 public void testGetAttribute() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testGetAttributeOutOfBounds() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 try {
 denseInstance.value(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 try {
 denseInstance.value(3);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
}

 @Test
 public void testClear() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 assertArrayIsNull(denseInstance.getAttributes());
 }

 @Test
 public void testContainsKey() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 assertFalse(denseInstance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsValue(3.0));
 assertFalse(denseInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 }

 @Test
 public void testGet() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.get(0), 0.0);
 assertEquals(2.0, denseInstance.get(1), 0.0);
 assertEquals(3.0, denseInstance.get(2), 0.0);
 try {
 denseInstance.get(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 try {
 denseInstance.get(3);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
}

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = new DenseInstance();
 assertTrue(denseInstance.isEmpty());

 denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertFalse(denseInstance.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 try {
 denseInstance.remove(0);
 fail();
 } catch (UnsupportedOperationException e) {
 // Expected
 }
}

 @Test
 public void testSize() {
 DenseInstance denseInstance = new DenseInstance();
 assertEquals(0, denseInstance.noAttributes());

 denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 List<Double> values = new ArrayList<>(denseInstance.values());
 assertEquals(3, values.size());
 assertEquals(1.0, values.get(0), 0.0);
 assertEquals(2.0, values.get(1), 0.0);
 assertEquals(3.0, values.get(2), 0.0);
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Double oldValue = denseInstance.put(0, 4.0);
 assertEquals(1.0, oldValue, 0.0);
 assertEquals(4.0, denseInstance.value(0), 0.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {4.0, 5.0, 6.0}, null);
 denseInstance1.putAll(denseInstance2.entrySet());
 assertEquals(6, denseInstance1.noAttributes());
 assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, denseInstance1.getAttributes(), 0.0);
 }

 @Test
 public void testToString() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals("{1.0, 2.0, 3.0;null}", denseInstance.toString());
 denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 assertEquals("{1.0, 2.0, 3.0;ClassValue}", denseInstance.toString());
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.removeAttribute(1);
 assertEquals(2, denseInstance.noAttributes());
 assertArrayNull(new double[] {1.0, 3.0}); // assert sub-array
 }

 @Test
 public void testHashCode() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[] {1.1, 2.1, 3.1}, null);
 assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode()); // Self-identity
 assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode()); // Equal instances
 assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode()); // Not equal instances
 }

 @Test
 public void testEquals() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[] {1.1, 2.1, 3.1}, null);
 DenseInstance denseInstance4 = denseInstance1.copy();
 assertTrue(denseInstance1.equals(denseInstance1));
 assertTrue(denseInstance1.equals(denseInstance4));
 assertFalse(denseInstance1.equals(denseInstance2));
 assertFalse(denseInstance1.equals(denseInstance3));
 }

 @Test
 public void testCopy() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance copiedInstance = denseInstance.copy();
 assertEquals(denseInstance, copiedInstance);
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0}, null);
 Set<Integer> indices = new HashSet<Integer>() {{ add(2); add(4); }};
 denseInstance.removeAttributes(indices);
 assertEquals(3, denseInstance.noAttributes());
 assertArrayNull(new double[] {1.0, 3.0}); // assert sub-array
 }
}