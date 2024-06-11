import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithArray() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertNotNull(denseInstance);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithClassValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertNotNull(denseInstance);
 assertEquals("ClassValue", denseInstance.classValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(5);
 assertNotNull(denseInstance);
 assertEquals(5, denseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0}, null);
 denseInstance.value(2);
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 assertFalse(denseInstance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsValue(3.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.get(0), 0.001);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithInvalidKeyType() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.get("InvalidKeyType");
 }

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0}, null);
 double newValue = 4.0;
 denseInstance.put(1, newValue);
 assertEquals(newValue, denseInstance.value(1), 0.001);
 }

 @Test(expected = ClassCastException.class)
 public void testPutWithInvalidKeyType() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0}, null);
 denseInstance.put("InvalidKeyType", 4.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0}, null);
 Map<Integer, Double> mapToAdd = new HashMap<>();
 mapToAdd.put(0, 3.0);
 mapToAdd.put(1, 4.0);
 denseInstance.putAll(mapToAdd);
 assertEquals(3.0, denseInstance.value(0), 0.001);
 assertEquals(4.0, denseInstance.value(1), 0.001);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.remove(0);
 }

 @Test
 public void testSize() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertEquals(3, denseInstance.size());
 }

 @Test
 public void testValues() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Collection<Double> values = denseInstance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, null);
 denseInstance.removeAttribute(2);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testHashCode() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[]{1.0, 2.0, 3.1}, null);
 assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
 }

 @Test
 public void testEquals() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[]{1.0, 2.0, 3.1}, null);
 DenseInstance denseInstance4 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertTrue(denseInstance1.equals(denseInstance1));
 assertTrue(denseInstance1.equals(denseInstance2));
 assertFalse(denseInstance1.equals(denseInstance3));
 assertFalse(denseInstance1.equals(denseInstance4));
 }

 @Test
 public void testCopy() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance copy = denseInstance.copy();
 assertEquals(denseInstance, copy);
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, null);
 Set<Integer> indicesToRemove = new HashSet<>();
 indicesToRemove.add(2);
 indicesToRemove.add(3);
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(2, denseInstance.noAttributes());
 }
}