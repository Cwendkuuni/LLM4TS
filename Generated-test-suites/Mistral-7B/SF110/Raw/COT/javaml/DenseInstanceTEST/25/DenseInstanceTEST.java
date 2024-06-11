import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;
import java.util.*;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithArray() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 Assert.assertNull(denseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, "Class Value");
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 Assert.assertEquals("Class Value", denseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = createDenseInstance(new double[5], null);
 Assert.assertArrayEquals(new double[5], denseInstance.attributes, 0.0);
 Assert.assertNull(denseInstance.getClassValue());
 }

 @Test
 public void testValueMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.value(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.value(-1);
 }

 @Test
 public void testClearMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.clear();
 Assert.assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
 }

 @Test
 public void testContainsKeyMethodWithInteger() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Assert.assertTrue(denseInstance.containsKey(0));
 Assert.assertTrue(denseInstance.containsKey(1));
 Assert.assertTrue(denseInstance.containsKey(2));
 Assert.assertFalse(denseInstance.containsKey(-1));
 Assert.assertFalse(denseInstance.containsKey(3));
 }

 @Test
 public void testContainsValueMethodWithNumber() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Assert.assertTrue(denseInstance.containsValue(1.0));
 Assert.assertTrue(denseInstance.containsValue(2.0));
 Assert.assertTrue(denseInstance.containsValue(3.0));
 Assert.assertFalse(denseInstance.containsValue(0.5));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
 List<Map.Entry<Integer, Double>> entriesList = new ArrayList<>(entries);
 Assert.assertEquals(3, entriesList.size());
 // Check the first entry
 Map.Entry<Integer, Double> entry1 = entriesList.get(0);
 Assert.assertEquals(0, entry1.getKey().intValue());
 Assert.assertEquals(1.0, entry1.getValue().doubleValue(), 0.0);
 // Check the second entry
 Map.Entry<Integer, Double> entry2 = entriesList.get(1);
 Assert.assertEquals(1, entry2.getKey().intValue());
 Assert.assertEquals(2.0, entry2.getValue().doubleValue(), 0.0);
 // Check the third entry
 Map.Entry<Integer, Double> entry3 = entriesList.get(2);
 Assert.assertEquals(2, entry3.getKey().intValue());
 Assert.assertEquals(3.0, entry3.getValue().doubleValue(), 0.0);
 }

 @Test
 public void testGetMethodWithInteger() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Assert.assertEquals(1.0, denseInstance.get(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.get(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.get(2), 0.0);
 Assert.assertNull(denseInstance.get(-1));
 }

 @Test(expected = ClassCastException.class)
 public void testGetMethodWithNonInteger() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.get("Key");
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = new DenseInstance();
 Assert.assertTrue(denseInstance.isEmpty());
 DenseInstance denseInstanceWithData = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Assert.assertFalse(denseInstanceWithData.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 SortedSet<Integer> keys = denseInstance.keySet();
 List<Integer> keysList = new ArrayList<>(keys);
 Assert.assertEquals(3, keysList.size());
 // Check the first key
 Integer key1 = keysList.get(0);
 Assert.assertEquals(0, key1.intValue());
 // Check the second key
 Integer key2 = keysList.get(1);
 Assert.assertEquals(1, key2.intValue());
 // Check the third key
 Integer key3 = keysList.get(2);
 Assert.assertEquals(2, key3.intValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutMethodWithInvalidKey() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.put(-1, 4.0);
 }

 @Test(expected = ClassCastException.class)
 public void testPutMethodWithNonIntegerKey() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.put("Key", 4.0);
 }

 @Test
 public void testPutMethodWithExistingKey() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.put(0, 4.0);
 Assert.assertEquals(4.0, denseInstance.value(0), 0.0);
 }

 @Test
 public void testPutAllMethodWithNullMap() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.putAll(null);
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testPutAllMethodWithEmptyMap() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Map<Integer, Double> map = new HashMap<>();
 denseInstance.putAll(map);
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testPutAllMethodWithMapContainingData() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 4.0);
 map.put(1, 5.0);
 denseInstance.putAll(map);
 Assert.assertArrayEquals(new double[] {1.0, 4.0, 5.0}, denseInstance.attributes, 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethodWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.remove(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.removeAttribute(-1);
 }

 @Test
 public void testRemoveAttributeMethodWithValidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0, 4.0}, null);
 denseInstance.removeAttribute(2);
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 4.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testHashCodeMethod() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[] {1.0, 2.0, 4.0}, null);
 Assert.assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 Assert.assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 Assert.assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameInstance() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = denseInstance1;
 Assert.assertTrue(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithDifferentInstanceButSameData() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = new DenseInstance();
 System.arraycopy(denseInstance1.attributes, 0, denseInstance2.attributes, 0, denseInstance1.attributes.length);
 Assert.assertTrue(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithDifferentInstanceButDifferentData() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {1.0, 2.0, 4.0}, null);
 Assert.assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testCopyMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance copiedInstance = denseInstance.copy();
 Assert.assertArrayEquals(denseInstance.attributes, copiedInstance.attributes, 0.0);
 Assert.assertEquals(denseInstance.getClassValue(), copiedInstance.getClassValue());
 }

 @Test
 public void testRemoveAttributesMethodWithEmptySet() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Set<Integer> emptySet = new HashSet<>();
 denseInstance.removeAttributes(emptySet);
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testRemoveAttributesMethodWithValidIndices() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0, 4.0}, null);
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
 denseInstance.removeAttributes(indicesToRemove);
 Assert.assertArrayEquals(new double[] {1.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testRemoveAttributesMethodWithInvalidIndices() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0, 4.0}, null);
 Set<Integer> invalidIndices = new HashSet<>(Arrays.asList(-1, 5));
 denseInstance.removeAttributes(invalidIndices);
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0}, denseInstance.attributes, 0.0);
 }
}