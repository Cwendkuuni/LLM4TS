package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DELTA = 0.001;

 @Test
 public void testSparseInstanceConstruction() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertNull(instance.getClassValue());

 instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertNull(instance.getClassValue());

 instance = new SparseInstance(5, 0.0);
 assertEquals(5, instance.noAttributes());
 assertNull(instance.getClassValue());

 instance = new SparseInstance(5, "ClassValue");
 assertEquals(5, instance.noAttributes());
 assertSame("ClassValue", instance.getClassValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstanceNegativeNoAttributes() {
 new SparseInstance(-1);
 }

 @Test
 public void testSparseInstanceWithDefaultValueConstruction() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(3, instance.noAttributes());
 assertSame(0.0, instance.defaultValue());

 instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 2.0);
 assertEquals(3, instance.noAttributes());
 assertSame(2.0, instance.defaultValue());
 }

 @Test
 public void testSparseInstanceWithClassValueConstruction() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, "ClassValue");
 assertEquals(3, instance.noAttributes());
 assertSame("ClassValue", instance.getClassValue());
 }

 @Test
 public void testSparseInstanceCopy() {
 SparseInstance original = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 SparseInstance copy = original.copy();

 assertEquals(original.noAttributes(), copy.noAttributes());
 assertSame(original.getClassValue(), copy.getClassValue());

 for (int i = 0; i < original.noAttributes(); ++i) {
 Double valueOriginal = original.get(i);
 Double valueCopy = copy.get(i);

 assertEquals(valueOriginal, valueCopy);
 }
 }

 @Test
 public void testSparseInstanceSetNoAttributes() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstanceSetNoAttributesNegative() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.setNoAttributes(-1);
 }

 @Test
 public void testSparseInstanceGet() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });

 assertEquals(1.0, instance.get(0), DELTA);
 assertEquals(0.0, instance.get(3), DELTA);
 }

 @Test
 public void testSparseInstancePut() {
 SparseInstance instance = new SparseInstance();

 instance.put(1, 1.0);
 assertEquals(1, instance.noAttributes());
 assertEquals(1.0, instance.get(1), DELTA);

 instance.put(2, 2.0);
 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.get(1), DELTA);
 assertEquals(2.0, instance.get(2), DELTA);
 }

 @Test
 public void testSparseInstancePutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 0.0);
 map.put(1, 1.0);
 map.put(2, 2.0);

 instance.putAll(map);
 assertEquals(3, instance.noAttributes());
 assertEquals(0.0, instance.get(0), DELTA);
 assertEquals(1.0, instance.get(1), DELTA);
 assertEquals(2.0, instance.get(2), DELTA);
 }

 @Test
 public void testSparseInstanceRemove() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });

 instance.removeAttribute(0);
 assertEquals(3, instance.noAttributes());
 assertEquals(2.0, instance.get(0), DELTA);
 assertEquals(1.0, instance.get(1), DELTA);
 assertEquals(3.0, instance.get(2), DELTA);
 assertEquals(4.0, instance.get(3), DELTA);

 instance.removeAttribute(3);
 assertEquals(2, instance.noAttributes());
 assertEquals(2.0, instance.get(0), DELTA);
 assertEquals(3.0, instance.get(1), DELTA);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSparseInstanceRemoveInvalidIndex() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 instance.removeAttribute(-1);
 }

 @Test
 public void testSparseInstanceSize() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 assertEquals(4, instance.noAttributes());
 }

 @Deprecated
 @Test
 public void testSparseInstanceSizeDeprecated() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 assertEquals(4, instance.noAttributes());
 }

 @Test
 public void testSparseInstanceValues() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 Collection<Double> values = instance.values();
 List<Double> valuesList = new ArrayList<>(values);

 assertEquals(4, valuesList.size());
 assertEquals(1.0, valuesList.get(0), DELTA);
 assertEquals(2.0, valuesList.get(1), DELTA);
 assertEquals(3.0, valuesList.get(2), DELTA);
 assertEquals(4.0, valuesList.get(3), DELTA);
 }

 @Test
 public void testSparseInstanceKeySet() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 Set<Integer> keys = instance.keySet();
 List<Integer> keysList = new ArrayList<>(keys);

 assertEquals(4, keysList.size());
 assertEquals(0, keysList.get(0).intValue());
 assertEquals(1, keysList.get(1).intValue());
 assertEquals(2, keysList.get(2).intValue());
 assertEquals(3, keysList.get(3).intValue());
 }

 @Test
 public void testSparseInstanceEntrySet() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);

 assertEquals(4, entryList.size());
 assertEquals(0, entryList.get(0).getKey().intValue());
 assertEquals(1.0, entryList.get(0).getValue(), DELTA);
 assertEquals(1, entryList.get(1).getKey().intValue());
 assertEquals(2.0, entryList.get(1).getValue(), DELTA);
 assertEquals(2, entryList.get(2).getKey().intValue());
 assertEquals(3.0, entryList.get(2).getValue(), DELTA);
 assertEquals(3, entryList.get(3).getKey().intValue());
 assertEquals(4.0, entryList.get(3).getValue(), DELTA);
 }

 @Test
 public void testSparseInstanceClear() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceContainsKey() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });

 assertTrue(instance.containsKey(Integer.valueOf(0)));
 assertTrue(instance.containsKey(Integer.valueOf(1)));
 assertTrue(instance.containsKey(Integer.valueOf(2)));
 assertTrue(instance.containsKey(Integer.valueOf(3)));

 assertFalse(instance.containsKey(Integer.valueOf(4)));
 }

 @Test
 public void testSparseInstanceContainsValue() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });

 assertTrue(instance.containsValue(Double.valueOf(1.0)));
 assertTrue(instance.containsValue(Double.valueOf(2.0)));
 assertTrue(instance.containsValue(Double.valueOf(3.0)));
 assertTrue(instance.containsValue(Double.valueOf(4.0)));

 assertFalse(instance.containsValue(Double.valueOf(5.0)));
 }

 @Test
 public void testSparseInstanceIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());

 instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceToString() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });

 String expected = "{" + Arrays.toString(instance.toArray()) + ";ClassValue}";
 assertEquals(expected, instance.toString());
 }

 @Test
 public void testSparseInstanceEquals() {
 SparseInstance instance1 = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 SparseInstance instance2 = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 SparseInstance instance3 = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 5.0 });

 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testSparseInstanceHashCode() {
 SparseInstance instance1 = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 SparseInstance instance2 = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 });
 SparseInstance instance3 = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 5.0 });

 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertEquals(instance1.hashCode(), instance2.hashCode());
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }
}