package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.experimental.categories.Category;

@Category(IntegrationTests.class)
public class SparseInstanceTest {

private SparseInstance instance;

@Before
public void setUp() {
instance = new SparseInstance(5);
instance.put(Integer.valueOf(0), Double.valueOf(1.0));
instance.put(Integer.valueOf(2), Double.valueOf(2.0));
instance.put(Integer.valueOf(4), Double.valueOf(4.0));
}

@Test
public void testConstructorWithNoArguments() {
SparseInstance sparseInstance = new SparseInstance();
assertEquals(-1, sparseInstance.noAttributes());
}

@Test
public void testConstructorWithNoAttributes() {
SparseInstance sparseInstance = new SparseInstance(0);
assertEquals(0, sparseInstance.noAttributes());
}

@Test
public void testConstructorWithNoAttributesAndDefaultValue() {
SparseInstance sparseInstance = new SparseInstance(0, 0.0);
assertEquals(0, sparseInstance.noAttributes());
}

@Test
public void testConstructorWithNoAttributesAndClassValue() {
SparseInstance sparseInstance = new SparseInstance(0, "ClassValue");
assertEquals("ClassValue", sparseInstance.getClassValue());
}

@Test
public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
SparseInstance sparseInstance = new SparseInstance(0, 0.0, "ClassValue");
assertEquals("ClassValue", sparseInstance.getClassValue());
}

@Test
public void testConstructorWithDatavector() {
double[] datavector = {1.0, 2.0, 3.0, 4.0};
SparseInstance sparseInstance = new SparseInstance(datavector);
assertEquals(4, sparseInstance.noAttributes());
}

@Test
public void testConstructorWithDatavectorAndDefaultValue() {
double[] datavector = {1.0, 2.0, 3.0, 4.0};
SparseInstance sparseInstance = new SparseInstance(datavector, 0.0);
assertEquals(4, sparseInstance.noAttributes());
}

@Test
public void testConstructorWithDatavectorAndClassValue() {
double[] datavector = {1.0, 2.0, 3.0, 4.0};
SparseInstance sparseInstance = new SparseInstance(datavector, "ClassValue");
assertEquals("ClassValue", sparseInstance.getClassValue());
}

@Test
public void testConstructorWithDatavectorAndDefaultValueAndClassValue() {
double[] datavector = {1.0, 2.0, 3.0, 4.0};
SparseInstance sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
assertEquals("ClassValue", sparseInstance.getClassValue());
}

@Test
public void testSetNoAttributes() {
instance.setNoAttributes(3);
assertEquals(3, instance.noAttributes());
}

@Test
public void testValue() {
assertEquals(1.0, instance.value(0), 0.001);
assertEquals(2.0, instance.value(2), 0.001);
assertEquals(4.0, instance.value(4), 0.001);
assertEquals(0.0, instance.value(1), 0.001); // Default value
}

@Test
public void testClear() {
instance.clear();
assertEquals(0, instance.noAttributes());
assertEquals(0.0, instance.value(0), 0.001);
}

@Test
public void testContainsKey() {
assertTrue(instance.containsKey(Integer.valueOf(0)));
assertTrue(instance.containsKey(Integer.valueOf(2)));
assertTrue(instance.containsKey(Integer.valueOf(4)));
assertFalse(instance.containsKey(Integer.valueOf(1)));
}

@Test
public void testContainsValue() {
assertTrue(instance.containsValue(Double.valueOf(1.0)));
assertTrue(instance.containsValue(Double.valueOf(2.0)));
assertTrue(instance.containsValue(Double.valueOf(4.0)));
assertFalse(instance.containsValue(Double.valueOf(3.0)));
}

@Test
public void testEntrySet() {
Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
assertEquals(3, entrySet.size());
}

@Test
public void testGet() {
assertEquals(1.0, instance.get(Integer.valueOf(0)), 0.001);
assertEquals(2.0, instance.get(Integer.valueOf(2)), 0.001);
assertEquals(4.0, instance.get(Integer.valueOf(4)), 0.001);
assertEquals(0.0, instance.get(Integer.valueOf(1)), 0.001); // Default value
}

@Test
public void testIsEmpty() {
instance.clear();
assertTrue(instance.isEmpty());
instance.put(Integer.valueOf(0), Double.valueOf(1.0));
assertFalse(instance.isEmpty());
}

@Test
public void testKeySet() {
Set<Integer> keySet = instance.keySet();
assertEquals(4, keySet.size());
}

@Test
public void testPut() {
instance.put(Integer.valueOf(1), Double.valueOf(3.0));
assertEquals(3.0, instance.value(1), 0.001);
}

@Test(expected = NullPointerException.class)
public void testPutNullKey() {
instance.put(null, Double.valueOf(5.0));
}

@Test(expected = NullPointerException.class)
public void testPutNullValue() {
instance.put(Integer.valueOf(1), null);
}

@Test
public void testPutAll() {
SparseInstance otherInstance = new SparseInstance();
otherInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
otherInstance.put(Integer.valueOf(5), Double.valueOf(5.0));
instance.putAll(otherInstance.entrySet());
assertEquals(5, instance.noAttributes());
assertEquals(3.0, instance.value(1), 0.001);
assertEquals(5.0, instance.value(5), 0.001);
}

@Test
public void testRemove() {
instance.remove(Integer.valueOf(1));
assertEquals(3, instance.noAttributes());
assertEquals(0.0, instance.value(1), 0.001);
}

@Test(expected = NullPointerException.class)
public void testRemoveNullKey() {
instance.remove(null);
}

@Test
public void testSize() {
assertEquals(3, instance.size());
instance.put(Integer.valueOf(5), Double.valueOf(5.0));
assertEquals(4, instance.size());
}

@Test
public void testValues() {
Collection<Double> values = instance.values();
assertEquals(3, values.size());
}

@Test
public void testNoAttributes() {
assertEquals(5, instance.noAttributes());
instance.removeAttribute(0);
assertEquals(4, instance.noAttributes());
instance.removeAttribute(4);
assertEquals(3, instance.noAttributes());
instance.clear();
assertEquals(0, instance.noAttributes());
}

@Test
public void testRemoveAttribute() {
instance.removeAttribute(2);
assertEquals(3, instance.noAttributes());
assertEquals(0.0, instance.value(2), 0.001);
instance.put(Integer.valueOf(2), Double.valueOf(6.0));
assertEquals(6.0, instance.value(2), 0.001);
instance.removeAttribute(2);
assertEquals(3, instance.noAttributes());
assertEquals(0.0, instance.value(2), 0.001);
}

@Test
public void testToString() {
assertEquals("{1.0=1.0, 2.0=2.0, 4.0=4.0, 0.0=0.0}", instance.toString());
}

@Test
public void testHashCode() {
SparseInstance otherInstance = new SparseInstance();
otherInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
otherInstance.put(Integer.valueOf(2), Double.valueOf(2.0));
otherInstance.put(Integer.valueOf(4), Double.valueOf(4.0));
assertEquals(instance.hashCode(), otherInstance.hashCode());
instance.put(Integer.valueOf(1), Double.valueOf(3.0));
assertNotEquals(instance.hashCode(), otherInstance.hashCode());
}

@Test
public void testEquals() {
SparseInstance otherInstance = new SparseInstance();
otherInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
otherInstance.put(Integer.valueOf(2), Double.valueOf(2.0));
otherInstance.put(Integer.valueOf(4), Double.valueOf(4.0));
assertTrue(instance.equals(otherInstance));
otherInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
assertFalse(instance.equals(otherInstance));
}

@Test
public void testCopy() {
SparseInstance copiedInstance = instance.copy();
assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
assertEquals(instance.getClassValue(), copiedInstance.getClassValue());
assertEquals(instance.data, copiedInstance.data);
}

@Test
public void testRemoveAttributes() {
Set<Integer> indicesToRemove = new HashSet<Integer>();
indicesToRemove.add(1);
indicesToRemove.add(3);
instance.removeAttributes(indicesToRemove);
assertEquals(2, instance.noAttributes());
assertEquals(0.0, instance.value(1), 0.001);
assertEquals(0.0, instance.value(3), 0.001);
}
}