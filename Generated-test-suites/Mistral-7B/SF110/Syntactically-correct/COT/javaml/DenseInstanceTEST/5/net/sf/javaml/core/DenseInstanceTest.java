import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;

public class DenseInstanceTest {

 private double[] attributeValues = new double[]{1.0, 2.0, 3.0};

 @Test
 public void testConstructorWithArray() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 assertNotNull(denseInstance);
 assertArrayEquals(attributeValues, denseInstance.attributes);
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 Object classValue = "ClassValue";
 DenseInstance denseInstance = new DenseInstance(attributeValues, classValue);
 assertNotNull(denseInstance);
 assertArrayEquals(attributeValues, denseInstance.attributes);
 assertEquals(classValue, denseInstance.classValue());
 }

 @Test
 public void testEmptyConstructor() {
 DenseInstance denseInstance = new DenseInstance();
 assertNotNull(denseInstance);
 assertArrayEquals(new double[0], denseInstance.attributes);
 }

 @Test
 public void testConstructorWithSize() {
 int size = 3;
 DenseInstance denseInstance = new DenseInstance(size);
 assertNotNull(denseInstance);
 assertArrayEquals(new double[size], denseInstance.attributes);
 }

 @Test
 public void testValueMethod() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 for (int i = 0; i < attributeValues.length; i++) {
 assertEquals(attributeValues[i], denseInstance.value(i), 0.0);
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithNegativeIndex() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.value(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithIndexLargerThanSize() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.value(attributeValues.length);
 }

 @Test
 public void testClearMethod() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.clear();
 assertArrayEquals(new double[0], denseInstance.attributes);
 }

 @Test
 public void testContainsKeyMethodWithValidIntegerKey() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 for (int i = 0; i < attributeValues.length; i++) {
 assertTrue(denseInstance.containsKey(i));
 }
 }

 @Test
 public void testContainsKeyMethodWithInvalidObjectKey() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 assertFalse(denseInstance.containsKey("Key"));
 }

 @Test
 public void testContainsValueMethodWithValidDoubleValue() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 for (int i = 0; i < attributeValues.length; i++) {
 assertTrue(denseInstance.containsValue(attributeValues[i]));
 }
 }

 @Test
 public void testContainsValueMethodWithInvalidDoubleValue() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 double invalidValue = 4.0;
 assertFalse(denseInstance.containsValue(invalidValue));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(attributeValues.length, entrySet.size());
 }

 @Test
 public void testGetMethodWithValidIntegerKey() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 for (int i = 0; i < attributeValues.length; i++) {
 assertEquals(attributeValues[i], denseInstance.get(i));
 }
 }

 @Test(expected = ClassCastException.class)
 public void testGetMethodWithInvalidObjectKey() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.get("Key");
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(attributeValues.length, keySet.size());
 }

 @Test
 public void testPutMethodWithValidKeyAndValue() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 double newValue = 5.0;
 denseInstance.put(0, newValue);
 assertEquals(newValue, denseInstance.attributes[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithInvalidKey() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.put("Key", 5.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithExistingKey() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.put(0, 5.0);
 try {
 denseInstance.put(0, 6.0);
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testPutAllMethodWithValidMap() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(1, 5.0);
 map.put(2, 6.0);
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.putAll(map);
 assertEquals(7, denseInstance.noAttributes());
 for (int i = 0; i < 7; i++) {
 if (i < 3) {
 assertEquals(attributeValues[i], denseInstance.attributes[i]);
 } else {
 assertEquals(map.get(i - 3), denseInstance.attributes[i]);
 }
 }
 }

 @Test
 public void testRemoveAttributeMethodWithValidIndex() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.removeAttribute(0);
 assertArrayEquals(Arrays.copyOfRange(attributeValues, 1, attributeValues.length), denseInstance.attributes);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithInvalidIndex() {
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.removeAttribute(attributeValues.length);
 }

 @Test
 public void testHashCodeMethod() {
 DenseInstance denseInstance1 = new DenseInstance(attributeValues);
 DenseInstance denseInstance2 = new DenseInstance(attributeValues);
 assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameInstance() {
 DenseInstance denseInstance1 = new DenseInstance(attributeValues);
 assertTrue(denseInstance1.equals(denseInstance1));
 }

 @Test
 public void testEqualsMethodWithDifferentInstanceButSameAttributes() {
 DenseInstance denseInstance1 = new DenseInstance(attributeValues);
 DenseInstance denseInstance2 = new DenseInstance(attributeValues.clone());
 assertTrue(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithDifferentInstanceButDifferentAttributes() {
 DenseInstance denseInstance1 = new DenseInstance(attributeValues);
 DenseInstance denseInstance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testCopyMethod() {
 DenseInstance denseInstance1 = new DenseInstance(attributeValues);
 DenseInstance denseInstance2 = denseInstance1.copy();
 assertEquals(denseInstance1.attributes, denseInstance2.attributes);
 assertEquals(denseInstance1.classValue(), denseInstance2.classValue());
 }

 @Test
 public void testRemoveAttributesMethodWithValidSetOfIndices() {
 Set<Integer> indicesToRemove = new HashSet<>();
 indicesToRemove.add(1);
 indicesToRemove.add(2);
 DenseInstance denseInstance = new DenseInstance(attributeValues);
 denseInstance.removeAttributes(indicesToRemove);
 assertArrayEquals(Arrays.copyOfRange(attributeValues, 0, 1), denseInstance.attributes);
 assertArrayEquals(Arrays.copyOfRange(attributeValues, 3, attributeValues.length), denseInstance.attributes, 3);
 }

}