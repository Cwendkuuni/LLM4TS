package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class DenseInstanceTest {

 private double[] attributeValues = new double[] {1.0, 2.0, 3.0};
 private DenseInstance denseInstance;

 @Test
 public void testDenseInstanceWithAttributeValues() {
  denseInstance = new DenseInstance(attributeValues);
  assertNotNull(denseInstance);
  assertEquals(attributeValues.length, denseInstance.noAttributes());
  for (int i = 0; i < attributeValues.length; i++) {
   assertEquals(attributeValues[i], denseInstance.value(i), 0.0);
  }
 }

 @Test
 public void testDenseInstanceWithEmptyArray() {
  denseInstance = new DenseInstance();
  assertTrue(denseInstance.isEmpty());
  assertEquals(0, denseInstance.noAttributes());
 }

 @Test
 public void testDenseInstanceWithClassValue() {
  denseInstance = new DenseInstance(attributeValues, "classValue");
  assertEquals("classValue", denseInstance.getClassValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDenseInstanceRemoveAttribute() {
  denseInstance = new DenseInstance(attributeValues);
  denseInstance.removeAttribute(0);
 }

 @Test
 public void testDenseInstanceEntrySet() {
  denseInstance = new DenseInstance(attributeValues);
  Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
  assertEquals(attributeValues.length, entries.size());
  for (Map.Entry<Integer, Double> entry : entries) {
   assertEquals(entry.getKey(), entry.getValue().intValue());
  }
 }

 @Test
 public void testDenseInstanceKeySet() {
  denseInstance = new DenseInstance(attributeValues);
  SortedSet<Integer> keys = denseInstance.keySet();
  assertEquals(attributeValues.length, keys.size());
  for (Integer key : keys) {
   assertEquals(key, denseInstance.value(key.intValue()));
  }
 }

 @Test
 public void testDenseInstancePut() {
  denseInstance = new DenseInstance(attributeValues);
  Double valueToPut = 4.0;
  denseInstance.put(0, valueToPut);
  assertEquals(valueToPut, denseInstance.value(0), 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDenseInstanceRemove() {
  denseInstance = new DenseInstance(attributeValues);
  denseInstance.remove(0);
 }

 @Test
 public void testDenseInstancePutAll() {
  denseInstance = new DenseInstance(attributeValues);
  DenseInstance anotherInstance = new DenseInstance(new double[] {0.5, 0.8, 1.2});
  Map<Integer, Double> map = new HashMap<>();
  map.put(0, 0.5);
  map.put(1, 0.8);
  map.put(2, 1.2);
  denseInstance.putAll(map);
  for (int i = 0; i < attributeValues.length + anotherInstance.noAttributes(); i++) {
   if (i < attributeValues.length) {
    assertEquals(attributeValues[i], denseInstance.value(i), 0.0);
   } else {
    assertEquals(anotherInstance.value(i - attributeValues.length), denseInstance.value(i), 0.0);
   }
  }
 }

 @Test
 public void testDenseInstanceHashCodeAndEquals() {
  denseInstance = new DenseInstance(attributeValues);
  DenseInstance anotherInstance = new DenseInstance(attributeValues.clone());
  DenseInstance anotherInstanceDifferentValues = new DenseInstance(new double[] {1.1, 2.1, 3.1});
  DenseInstance anotherInstanceDifferentType = new Object() {};
  assertEquals(denseInstance.hashCode(), anotherInstance.hashCode());
  assertTrue(denseInstance.equals(anotherInstance));
  assertFalse(denseInstance.equals(anotherInstanceDifferentValues));
  assertFalse(denseInstance.equals(anotherInstanceDifferentType));
 }

 @Test
 public void testDenseInstanceCopy() {
  denseInstance = new DenseInstance(attributeValues);
  DenseInstance copy = denseInstance.copy();
  assertEquals(attributeValues.length, copy.noAttributes());
  for (int i = 0; i < attributeValues.length; i++) {
   assertEquals(attributeValues[i], copy.value(i), 0.0);
  }
 }

 @Test
 public void testDenseInstanceRemoveAttributes() {
  denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
  Set<Integer> indicesToRemove = new HashSet<>();
  indicesToRemove.add(1);
  indicesToRemove.add(3);
  denseInstance.removeAttributes(indicesToRemove);
  assertEquals(3, denseInstance.noAttributes());
  for (int i = 0; i < denseInstance.noAttributes(); i++) {
   if (i < 1 || i > 3) {
    assertEquals(denseInstance.value(i), attributeValues[i], 0.0);
   }
  }
 }
}