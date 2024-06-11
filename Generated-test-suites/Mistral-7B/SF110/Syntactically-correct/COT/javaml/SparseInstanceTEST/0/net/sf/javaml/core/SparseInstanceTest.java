package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertEmpty(instance.data);
 }

 @Test
 public void testConstructorWithOneArgument() {
 SparseInstance instance = new SparseInstance(1);
 assertEquals(1, instance.noAttributes());
 assertEmpty(instance.data);
 }

 @Test
 public void testConstructorWithTwoArguments() {
 SparseInstance instance = new SparseInstance(1, DEFAULT_VALUE);
 assertEquals(1, instance.noAttributes());
 assertEmpty(instance.data);
 }

 @Test
 public void testConstructorWithThreeArguments() {
 SparseInstance instance = new SparseInstance(1, DEFAULT_VALUE, "classValue");
 assertEquals(1, instance.noAttributes());
 assertEmpty(instance.data);
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 assertEquals(datavector.length, instance.data.size());
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance(1);
 instance.setNoAttributes(3);
 assertEquals(3, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeValue() {
 SparseInstance instance = new SparseInstance(1);
 instance.setNoAttributes(-1);
 }

 @Test
 public void testGetValue() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(2.0, instance.value(1), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueOutOfBounds() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 assertEquals(DEFAULT_VALUE, instance.value(2), 0.001);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 instance.clear();
 assertEquals(0, instance.data.size());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertFalse(instance.containsKey(2));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(1, 3.0);
 }});
 assertTrue(instance.containsValue(2.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(2, entrySet.size());
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 assertEquals(1.0, instance.get(0), 0.001);
 assertEquals(2.0, instance.get(1), 0.001);
 assertEquals(DEFAULT_VALUE, instance.get(2), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 instance.get(null);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 SparseInstance nonEmptyInstance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 assertFalse(nonEmptyInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 }});
 Set<Integer> keySet = instance.keySet();
 assertEquals(2, keySet.size());
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance(1);
 assertEquals(DEFAULT_VALUE, instance.get(0), 0.001);
 instance.put(0, 1.0);
 assertEquals(1.0, instance.get(0), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 SparseInstance instance = new SparseInstance(1);
 instance.put(null, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutDuplicateKey() {
 SparseInstance instance = new SparseInstance(1);
 instance.put(0, 1.0);
 instance.put(0, 2.0);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }};
 instance.putAll(map);
 assertEquals(3, instance.noAttributes());
 assertEquals(3, instance.data.size());
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllNullMap() {
 SparseInstance instance = new SparseInstance();
 instance.putAll(null);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }});
 assertEquals(3.0, instance.remove(2), 0.001);
 assertEquals(2, instance.noAttributes());
 assertEquals(2, instance.data.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }});
 instance.remove(null);
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }});
 assertEquals(3, instance.size());
 }

 @Test(expected = DeprecatedWarning.class)
 public void testSizeDeprecated() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }});
 assertEquals(3, instance.size());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }});
 Collection<Double> values = instance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 instance.setNoAttributes(3);
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }});
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 assertEquals(2, instance.data.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAttributeNegativeIndex() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }});
 instance.removeAttribute(-1);
 }

 @Test
 public void testRemoveAttributeOutOfBoundsIndex() {
 SparseInstance instance = new SparseInstance(1, new HashMap<Integer, Double>() {{
 put(0, 1.0);
 put(1, 2.0);
 put(2, 3.0);
 }});
 instance.removeAttribute(3);
 assertEquals(2, instance.noAttributes());
 assertEquals(2, instance.data.size());
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0));
 assertEquals("{1.0=1.0, 2.0=2.0, 3.0=3.0;classValue}", instance.toString());
 }

 private void assertEmpty(Map<Integer, Double> map) {
 assertEquals(0, map.size());
 }

}