import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.util.Pair;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DenseInstanceTest {

 @Test
 public void testDenseInstanceWithEmptyArray() {
 DenseInstance instance = new DenseInstance();
 assertEquals(0, instance.noAttributes());
 assertNotNull(instance.getClassValue());
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testDenseInstanceWithValidAttributes() {
 double[] attributes = {1.2, 3.4, 5.6};
 DenseInstance instance = new DenseInstance(attributes);
 assertEquals(3, instance.noAttributes());
 assertNotNull(instance.getClassValue());
 assertFalse(instance.isEmpty());
 for (int i = 0; i < attributes.length; i++) {
 assertEquals(attributes[i], instance.value(i), 0.0);
 }
 }

 @Test
 public void testDenseInstanceWithValidKeyAndValue() {
 DenseInstance instance = new DenseInstance();
 instance.put(0, 1.2);
 assertEquals(1.2, instance.get(0), 0.0);
 }

 @Test
 public void testDenseInstanceWithInvalidKey() {
 DenseInstance instance = new DenseInstance();
 assertThrows(IllegalArgumentException.class, () -> instance.put(-1, 1.2));
 }

 @Test
 public void testDenseInstanceWithEmptyMap() {
 DenseInstance instance = new DenseInstance();
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertTrue(entries.isEmpty());
 }

 @Test
 public void testDenseInstanceWithValidEntries() {
 DenseInstance instance = new DenseInstance();
 instance.put(0, 1.2);
 instance.put(1, 3.4);
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(2, entries.size());
 for (Map.Entry<Integer, Double> entry : entries) {
 assertEquals(entry.getKey(), instance.getKey(entry.getKey()));
 assertEquals(entry.getValue(), instance.getValue(entry.getKey()), 0.0);
 }
 }

 @Test
 public void testDenseInstanceWithInvalidValueType() {
 DenseInstance instance = new DenseInstance();
 assertThrows(IllegalArgumentException.class, () -> instance.put(0, new Object()));
 }

 @Test
 public void testDenseInstanceWithSetKey() {
 DenseInstance instance = new DenseInstance();
 instance.put(0, 1.2);
 instance.put(1, 3.4);
 Set<Integer> keys = instance.keySet();
 TreeSet<Integer> expectedKeys = new TreeSet<>(Arrays.asList(0, 1));
 assertEquals(expectedKeys, keys);
 }

 @Test
 public void testDenseInstanceWithValidValues() {
 DenseInstance instance = new DenseInstance();
 instance.putAll(Collections.singletonMap(1, 1.2));
 instance.putAll(Collections.singletonMap(2, 3.4));
 Collection<Double> values = instance.values();
 List<Double> expectedValues = Arrays.asList(1.2, 3.4);
 assertEquals(expectedValues, values);
 }

 @Test
 public void testDenseInstanceWithRemoveAttribute() {
 DenseInstance instance = new DenseInstance(new double[]{1.2, 2.3, 3.4});
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDenseInstanceWithRemoveValue() {
 DenseInstance instance = new DenseInstance();
 instance.put(0, 1.2);
 instance.remove(0);
 }

 @Test
 public void testDenseInstanceWithContainsKey() {
 DenseInstance instance = new DenseInstance(new double[]{1.2, 2.3, 3.4});
 assertTrue(instance.containsKey(0));
 assertFalse(instance.containsKey(4));
 }

 @Test
 public void testDenseInstanceWithContainsValue() {
 DenseInstance instance = new DenseInstance(new double[]{1.2, 2.3, 3.4});
 assertTrue(instance.containsValue(1.2));
 assertFalse(instance.containsValue(5.6));
 }

 @Test
 public void testDenseInstanceWithClone() {
 DenseInstance original = new DenseInstance(new double[]{1.2, 2.3, 3.4});
 DenseInstance copy = original.copy();
 assertEquals(original.noAttributes(), copy.noAttributes());
 assertArrayEquals(original.attributes, copy.attributes);
 assertNull(copy.getClassValue());
 }

 @Test
 public void testDenseInstanceWithInvalidValueIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.2, 2.3, 3.4});
 assertThrows(IndexOutOfBoundsException.class, () -> instance.value(-1));
 }

 @Test
 public void testDenseInstanceWithOutOfBoundsSetAttribute() {
 DenseInstance instance = new DenseInstance();
 assertThrows(IndexOutOfBoundsException.class, () -> instance.put(10, 1.2));
 }

 @Test
 public void testDenseInstanceWithSetAttributeOutOfBounds() {
 DenseInstance instance = new DenseInstance(new double[10]);
 instance.put(10, 1.2);
 assertEquals(10, instance.noAttributes());
 assertNull(instance.getValue(10));
 }
}