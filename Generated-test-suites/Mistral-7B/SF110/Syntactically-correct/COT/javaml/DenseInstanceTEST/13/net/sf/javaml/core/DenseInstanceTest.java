import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;

public class DenseInstanceTest {

 private double[] testAttributes = new double[]{1.0, 2.0, 3.0};
 private DenseInstance testInstance;

 @Test
 public void testConstructorWithArray() {
 testInstance = new DenseInstance(testAttributes);
 assertArrayEquals(testAttributes, testInstance.attributes);
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 testInstance = new DenseInstance(testAttributes, "ClassValue");
 assertArrayEquals(testAttributes, testInstance.attributes);
 assertNotNull(testInstance.getClassValue());
 }

 @Test
 public void testConstructorWithEmptyArray() {
 testInstance = new DenseInstance(new double[0]);
 assertEquals(0, testInstance.noAttributes());
 }

 @Test
 public void testConstructorWithSize() {
 testInstance = new DenseInstance(5);
 assertEquals(5, testInstance.noAttributes());
 }

 @Test
 public void testValue() {
 testInstance = new DenseInstance(testAttributes);
 assertEquals(1.0, testInstance.value(0), 1e-8);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 testInstance = new DenseInstance(testAttributes);
 testInstance.value(-1);
 }

 @Test
 public void testClear() {
 testInstance = new DenseInstance(testAttributes);
 testInstance.clear();
 assertEquals(0, testInstance.noAttributes());
 }

 @Test
 public void testContainsKeyWithInteger() {
 testInstance = new DenseInstance(testAttributes);
 assertTrue(testInstance.containsKey(0));
 assertTrue(testInstance.containsKey(1));
 assertTrue(testInstance.containsKey(2));
 assertFalse(testInstance.containsKey("InvalidKey"));
 }

 @Test
 public void testContainsValue() {
 testInstance = new DenseInstance(testAttributes);
 for (int i = 0; i < testAttributes.length; ++i) {
 assertTrue(testInstance.containsValue(testAttributes[i]));
 }
 assertFalse(testInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 testInstance = new DenseInstance(testAttributes);
 Set<Map.Entry<Integer, Double>> entrySet = testInstance.entrySet();
 assertEquals(testAttributes.length, entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 }

 @Test
 public void testGet() {
 testInstance = new DenseInstance(testAttributes);
 assertEquals(testAttributes[0], testInstance.get(0));
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithNonIntegerKey() {
 testInstance = new DenseInstance(testAttributes);
 testInstance.get("InvalidKey");
 }

 @Test
 public void testIsEmpty() {
 testInstance = new DenseInstance(testAttributes);
 assertFalse(testInstance.isEmpty());
 testInstance.clear();
 assertTrue(testInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 testInstance = new DenseInstance(testAttributes);
 SortedSet<Integer> keySet = testInstance.keySet();
 assertEquals(testAttributes.length, keySet.size());
 for (int i = 0; i < testAttributes.length; ++i) {
 assertTrue(keySet.contains(i));
 }
 }

 @Test
 public void testPut() {
 testInstance = new DenseInstance(testAttributes);
 double newValue = 4.5;
 testInstance.put(0, newValue);
 assertEquals(newValue, testInstance.attributes[0], 1e-8);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKey() {
 testInstance = new DenseInstance(testAttributes);
 testInstance.put(-1, 4.5);
 }

 @Test
 public void testPutAll() {
 testInstance = new DenseInstance(testAttributes);
 double[] newAttributes = new double[]{4.0, 5.0, 6.0};
 Map<Integer, Double> newMap = new HashMap<>();
 newMap.put(0, 4.0);
 newMap.put(1, 5.0);
 newMap.put(2, 6.0);
 testInstance.putAll(newMap);
 assertArrayEquals(newAttributes, testInstance.attributes);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 testInstance = new DenseInstance(testAttributes);
 testInstance.remove(0);
 }

 @Test
 public void testSize() {
 testInstance = new DenseInstance(testAttributes);
 assertEquals(testAttributes.length, testInstance.noAttributes());
 }

 @Test
 public void testValues() {
 testInstance = new DenseInstance(testAttributes);
 Collection<Double> values = testInstance.values();
 assertEquals(testAttributes.length, values.size());
 for (Double value : values) {
 assertEquals(value, testAttributes[value.intValue()], 1e-8);
 }
 }

 @Test
 public void testNoAttributes() {
 testInstance = new DenseInstance(testAttributes);
 assertEquals(testAttributes.length, testInstance.noAttributes());
 testInstance.removeAttribute(0);
 assertEquals(testAttributes.length - 1, testInstance.noAttributes());
 }

 @Test
 public void testHashCode() {
 testInstance = new DenseInstance(testAttributes);
 DenseInstance otherInstance = new DenseInstance(testAttributes.clone());
 assertEquals(testInstance.hashCode(), otherInstance.hashCode());
 }

 @Test
 public void testEquals() {
 testInstance = new DenseInstance(testAttributes);
 DenseInstance otherInstance = new DenseInstance(testAttributes.clone());
 assertTrue(testInstance.equals(otherInstance));
 testInstance.attributes[0] += 0.1;
 assertFalse(testInstance.equals(otherInstance));
 }

 @Test
 public void testCopy() {
 testInstance = new DenseInstance(testAttributes);
 DenseInstance copiedInstance = testInstance.copy();
 assertArrayEquals(testAttributes, copiedInstance.attributes);
 }

 @Test
 public void testRemoveAttributes() {
 testInstance = new DenseInstance(testAttributes);
 Set<Integer> indicesToRemove = new HashSet<Integer>() {{
 add(1);
 add(2);
 }};
 testInstance.removeAttributes(indicesToRemove);
 assertArrayEquals(new double[]{1.0}, testInstance.attributes);
 }
}