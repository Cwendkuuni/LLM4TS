package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

 private Instance instance;

 @Before
 public void setUp() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 instance = new SparseInstance(datavector, 0.0, "ClassValue");
 }

 @Test
 public void testEmptyConstructor() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.5);
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals(0.5, sparseInstance.defaultValue, 0.001);
 }

 @Test
 public void testConstructorWithClassValue() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.5, "ClassValue");
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(datavector.length, sparseInstance.noAttributes());
 }

 @Test
 public void testGetValue() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 Double value = instance.value(i);
 assertEquals(instance.get(i), value, 0.001);
 }
 }

 @Test
 public void testClear() {
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertTrue(instance.containsKey(Integer.valueOf(i)));
 }
 assertTrue(instance.containsKey(Integer.valueOf(-1)));
 }

 @Test
 public void testContainsValue() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 Double value = instance.get(i);
 assertTrue(instance.containsValue(value));
 }
 }

 @Test
 public void testEntrySet() {
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(instance.noAttributes() + 1, entrySet.size());
 }

 @Test
 public void testGet() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 Double value = instance.get(Integer.valueOf(i));
 assertEquals(instance.value(i), value, 0.001);
 }
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(-1)), 0.001);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(instance.isEmpty());
 instance.clear();
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 Set<Integer> keySet = instance.keySet();
 assertEquals(instance.noAttributes(), keySet.size());
 }

 @Test
 public void testPut() {
 Double oldValue = instance.get(Integer.valueOf(-1));
 instance.put(Integer.valueOf(instance.noAttributes()), 10.0);
 assertEquals(instance.noAttributes() + 1, instance.noAttributes());
 assertEquals(10.0, instance.get(Integer.valueOf(instance.noAttributes())), 0.001);
 assertEquals(oldValue, instance.get(Integer.valueOf(-1)), 0.001);
 }

 @Test
 public void testPutAll() {
 SparseInstance otherInstance = new SparseInstance();
 otherInstance.put(Integer.valueOf(0), 5.0);
 otherInstance.put(Integer.valueOf(1), 6.0);
 instance.putAll(otherInstance.data);
 assertEquals(instance.noAttributes() + otherInstance.noAttributes(), instance.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 if (i < otherInstance.noAttributes()) {
 assertEquals(otherInstance.get(Integer.valueOf(i)), instance.get(Integer.valueOf(i)), 0.001);
 } else {
 assertNull(instance.get(Integer.valueOf(i)));
 }
 }
 }

 @Test
 public void testRemove() {
 Double oldValue = instance.get(Integer.valueOf(0));
 instance.removeAttribute(0);
 assertEquals(instance.noAttributes() - 1, instance.noAttributes());
 assertNull(instance.get(Integer.valueOf(0)));
 assertEquals(oldValue, instance.get(Integer.valueOf(1)), 0.001);
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testSize() {
 int size = instance.size();
 fail("Method size is deprecated.");
 }

 @Test
 public void testValues() {
 Set<Double> values = new HashSet<Double>(instance.values());
 assertEquals(instance.noAttributes(), values.size());
 for (Double value : values) {
 assertTrue(instance.containsValue(value));
 }
 }

 @Test
 public void testNoAttributes() {
 assertEquals(instance.noAttributes(), instance.noAttributes());
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 int attributeToRemove = 2;
 instance.removeAttribute(attributeToRemove);
 assertEquals(instance.noAttributes() - 1, instance.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 if (i != attributeToRemove) {
 assertEquals(instance.get(Integer.valueOf(i)), instance.get(Integer.valueOf(i + 1)), 0.001);
 }
 }
 }

 @Test
 public void testToString() {
 String toString = instance.toString();
 assertTrue(toString.contains("{"));
 assertTrue(toString.contains("ClassValue"));
 }

 @Test
 public void testHashCode() {
 int hashcode = instance.hashCode();
 SparseInstance otherInstance = new SparseInstance(instance);
 assertEquals(hashcode, otherInstance.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance otherInstance = new SparseInstance(instance);
 assertTrue(instance.equals(otherInstance));
 SparseInstance otherInstance2 = new SparseInstance();
 assertFalse(instance.equals(otherInstance2));
 }

 @Test
 public void testCopy() {
 Instance copy = instance.copy();
 assertEquals(instance.noAttributes(), copy.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.get(Integer.valueOf(i)), copy.get(Integer.valueOf(i)), 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 Set<Integer> indices = new HashSet<Integer>(Arrays.asList(1, 3));
 instance.removeAttributes(indices);
 assertEquals(instance.noAttributes() - 2, instance.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 if (!indices.contains(i)) {
 assertEquals(instance.get(Integer.valueOf(i)), instance.get(Integer.valueOf(i)), 0.001);
 } else {
 assertNull(instance.get(Integer.valueOf(i)));
 }
 }
 }
}