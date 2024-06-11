import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import net.sf.javaml.core.*;

import org.junit.Test;

public class DenseInstanceTest {

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
 that(denseInstance.attributes).isNotNull();
 that(denseInstance.attributes).hasSize(3);
 assertThat(denseInstance.attributes, hasItems(closeTo(1.0, 0.001), closeTo(2.0, 0.001), closeTo(3.0, 0.001)));
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 assertEquals("ClassValue", denseInstance.classValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructorWithNegativeSize() {
 new DenseInstance(-5);
 }

 @Test
 public void testValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.value(3);
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsKeyWithNonIntegerKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.containsKey('a');
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testContainsValueWithNegativeNumber() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.containsValue(-1.0);
 }

 @Test
 public void testContainsValueWithZeroTolerance() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 1.5, 2.0});
 assertTrue(denseInstance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
 assertEquals(3, entries.size());
 }

 @Test
 public void testGet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, denseInstance.get(0), 0.0);
 assertEquals(2.0, denseInstance.get(1), 0.0);
 assertEquals(3.0, denseInstance.get(2), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.get('a');
 }

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = new DenseInstance();
 assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 SortedSet<Integer> keys = denseInstance.keySet();
 assertEquals(3, keys.size());
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.put(2, 10.0);
 assertEquals(10.0, denseInstance.value(2), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.put('a', 10.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 10.0);
 map.put(1, 20.0);
 denseInstance.putAll(map);
 assertEquals(10.0, denseInstance.value(0), 0.0);
 assertEquals(20.0, denseInstance.value(1), 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.remove(0);
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<>();
 indices.add(2);
 indices.add(4);
 denseInstance.removeAttributes(indices);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributesWithEmptySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.removeAttributes(new HashSet<Integer>());
 }

 @Test
 public void testSize() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSizeWithDeprecatedMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.size();
 }

 @Test
 public void testValues() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.values().size());
 }
}