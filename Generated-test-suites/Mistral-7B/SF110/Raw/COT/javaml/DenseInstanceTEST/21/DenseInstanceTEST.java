import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testDenseInstanceWithEmptyArray() {
 DenseInstance instance = new DenseInstance();
 assertFalse(instance.isEmpty());
 assertEquals(0, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertArrayEquals(new double[0], instance.getAttributes());
 }

 @Test
 public void testDenseInstanceWithAttributes() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = createDenseInstance(attributes, null);
 assertEquals(3, instance.noAttributes());
 assertArrayEquals(attributes, instance.getAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testDenseInstanceWithClassValue() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = createDenseInstance(attributes, "ClassValue");
 assertEquals("ClassValue", instance.getClassValue().toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveValueFromDenseInstance() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 instance.remove(0);
 }

 @Test
 public void testGetValue() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 assertEquals(1.0, instance.value(0), 0.0);
 }

 @Test
 public void testSetValue() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 instance.put(0, 5.0);
 assertEquals(5.0, instance.value(0), 0.0);
 }

 @Test
 public void testPutAll() {
 double[] firstAttributes = new double[]{1.0, 2.0, 3.0};
 double[] secondAttributes = new double[]{4.0, 5.0, 6.0};
 DenseInstance instance = new DenseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 5.0);
 map.put(1, 6.0);
 map.put(2, 7.0);
 instance.putAll(map);
 assertArrayEquals(new double[]{5.0, 6.0, 7.0}, instance.getAttributes());
 }

 @Test
 public void testContainsKey() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 }

 @Test
 public void testContainsValue() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 Double value = 2.0;
 assertTrue(instance.containsValue(value));
 }

 @Test
 public void testEntrySet() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(3, entries.size());
 }

 @Test
 public void testGet() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 assertEquals(1.0, instance.get(0), 0.0);
 }

 @Test
 public void testClear() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 instance.clear();
 assertArrayEquals(new double[0], instance.getAttributes());
 }

 @Test
 public void testKeySet() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 SortedSet<Integer> keys = instance.keySet();
 assertEquals(3, keys.size());
 }

 @Test
 public void testSize() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 assertEquals(3, instance.size());
 }

 @Test
 public void testValues() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 Collection<Double> values = instance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testHashcode() {
 double[] attributes1 = new double[]{1.0, 2.0, 3.0};
 double[] attributes2 = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance1 = new DenseInstance(attributes1);
 DenseInstance instance2 = new DenseInstance(attributes2);
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEquals() {
 double[] attributes1 = new double[]{1.0, 2.0, 3.0};
 double[] attributes2 = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance1 = new DenseInstance(attributes1);
 DenseInstance instance2 = new DenseInstance(attributes2);
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testCopy() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 DenseInstance copiedInstance = instance.copy();
 assertArrayEquals(attributes, copiedInstance.getAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributesWithNullIndices() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 Set<Integer> indices = null;
 instance.removeAttributes(indices);
 }

 @Test
 public void testRemoveAttributesWithEmptyIndices() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 Set<Integer> indices = new HashSet<>();
 instance.removeAttributes(indices);
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testRemoveAttributesWithOneIndex() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance instance = new DenseInstance(attributes);
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 instance.removeAttributes(indices);
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testRemoveAttributesWithMultipleIndices() {
 double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 DenseInstance instance = new DenseInstance(attributes);
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 instance.removeAttributes(indices);
 assertEquals(3, instance.noAttributes());
 }
}