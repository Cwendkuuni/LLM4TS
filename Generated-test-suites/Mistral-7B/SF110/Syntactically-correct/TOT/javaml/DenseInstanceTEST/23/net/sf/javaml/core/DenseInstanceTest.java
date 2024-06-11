import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class DenseInstanceTest {

 @Test
 public void testConstructorWithArray() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, instance.noAttributes());
 for (int i = 0; i < 3; i++) {
 assertEquals(i + 1.0, instance.value(i), 0.001);
 }
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 5.0);
 assertEquals(3, instance.noAttributes());
 assertEquals(5.0, instance.classValue());
 for (int i = 0; i < 3; i++) {
 assertEquals(i + 1.0, instance.value(i), 0.001);
 }
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance instance = new DenseInstance(3);
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testValue() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(2.0, instance.value(1), 0.001);
 assertEquals(3.0, instance.value(2), 0.001);
 }

 @Test
 public void testClear() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(instance.containsKey((Object)0));
 assertTrue(instance.containsKey((Object)1));
 assertTrue(instance.containsKey((Object)2));
 assertFalse(instance.containsKey((Object)3));
 }

 @Test
 public void testContainsValue() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(instance.containsValue(1.0));
 assertTrue(instance.containsValue(2.0));
 assertTrue(instance.containsValue(3.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(3, entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 }

 @Test
 public void testGet() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, instance.get((Object)0), 0.001);
 assertEquals(2.0, instance.get((Object)1), 0.001);
 assertEquals(3.0, instance.get((Object)2), 0.001);
 }

 @Test
 public void testIsEmpty() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 SortedSet<Integer> keySet = instance.keySet();
 assertEquals(3, keySet.size());
 for (Integer key : keySet) {
 assertEquals(key, key.intValue());
 }
 }

 @Test
 public void testPut() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.put((Object)4, 4.0);
 assertEquals(4, instance.noAttributes());
 assertEquals(4.0, instance.value(3), 0.001);
 }

 @Test
 public void testPutAll() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Map<Integer, Double> map = new HashMap<>();
 map.put(4, 4.0);
 map.put(5, 5.0);
 instance.putAll(map);
 assertEquals(5, instance.noAttributes());
 assertEquals(4.0, instance.value(3), 0.001);
 assertEquals(5.0, instance.value(4), 0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.remove(0);
 fail("Should throw UnsupportedOperationException");
 }

 @Test
 public void testSize() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, instance.size());
 }

 @Test
 public void testValues() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Collection<Double> values = instance.values();
 assertEquals(3, values.size());
 for (Double value : values) {
 assertEquals(value, value.doubleValue());
 }
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testHashCode() {
 DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance instance3 = new DenseInstance(new double[] {2.0, 2.0, 3.0});
 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertEquals(instance1.hashCode(), instance2.hashCode());
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEquals() {
 DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance instance3 = new DenseInstance(new double[] {1.0, 2.0, 3.1});
 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testCopy() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance copiedInstance = instance.copy();
 assertEquals(instance, copiedInstance);
 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i), copiedInstance.value(i), 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 instance.removeAttributes(indices);
 assertEquals(2, instance.noAttributes());
 }
}