import org.junit.Assert;import org.junit.Test;import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(3, denseInstance.noAttributes());
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 Assert.assertEquals("ClassValue", denseInstance.classValue());
 Assert.assertEquals(3, denseInstance.noAttributes());
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(5);
 Assert.assertEquals(5, denseInstance.noAttributes());
 Assert.assertArrayEquals(new double[5], denseInstance.attributes);
 }

 @Test
 public void testValueMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.value(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testClearMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.clear();
 Assert.assertArrayEquals(new double[0], denseInstance.attributes);
 }

 @Test
 public void testContainsKeyMethodWithValidKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertTrue(denseInstance.containsKey(0));
 Assert.assertTrue(denseInstance.containsKey(1));
 Assert.assertTrue(denseInstance.containsKey(2));
 }

 @Test
 public void testContainsKeyMethodWithInvalidKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertFalse(denseInstance.containsKey(-1));
 Assert.assertFalse(denseInstance.containsKey(3));
 }

 @Test
 public void testContainsValueMethodWithValidValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 1.5, 2.0, 2.5});
 Assert.assertTrue(denseInstance.containsValue(1.0));
 Assert.assertTrue(denseInstance.containsValue(2.5));
 }

 @Test
 public void testContainsValueMethodWithInvalidValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 1.5, 2.0, 2.5});
 Assert.assertFalse(denseInstance.containsValue(3.0));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
 Assert.assertEquals(3, entries.size());
 }

 @Test
 public void testGetMethodWithValidKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(1.0, denseInstance.get(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.get(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.get(2), 0.0);
 }

 @Test
 public void testGetMethodWithInvalidKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 denseInstance.get(-1);
 Assert.fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 SortedSet<Integer> keys = denseInstance.keySet();
 Assert.assertEquals(3, keys.size());
 }

 @Test
 public void testPutMethodWithValidKeyAndValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0});
 denseInstance.put(0, 3.0);
 Assert.assertEquals(3.0, denseInstance.value(0), 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutMethodWithInvalidKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0});
 denseInstance.put(-1, 3.0);
 }

 @Test
 public void testPutAllMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0});
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 3.0);
 map.put(1, 4.0);
 denseInstance.putAll(map);
 Assert.assertEquals(3.0, denseInstance.value(0), 0.0);
 Assert.assertEquals(4.0, denseInstance.value(1), 0.0);
 }

 @Test
 public void testRemoveAttributeMethodWithValidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.removeAttribute(0);
 Assert.assertArrayEquals(new double[] {2.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testRemoveAttributeMethodWithInvalidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 denseInstance.removeAttribute(-1);
 Assert.fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testHashCodeMethod() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameObject() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(denseInstance1, denseInstance1);
 }

 @Test
 public void testEqualsMethodWithDifferentObjects() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 4.0});
 Assert.assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithSameAttributesAndClassValue() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 Assert.assertEquals(denseInstance1, denseInstance2);
 }

 @Test
 public void testEqualsMethodWithDifferentAttributesOrClassValue() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue1");
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 4.0}, "ClassValue2");
 Assert.assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testCopyMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance copiedInstance = denseInstance.copy();
 Assert.assertArrayEquals(denseInstance.attributes, copiedInstance.attributes);
 Assert.assertEquals(denseInstance.classValue(), copiedInstance.classValue());
 }

 @Test
 public void testRemoveAttributesMethodWithValidIndices() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indicesToRemove = new HashSet<Integer>();
 indicesToRemove.add(2);
 indicesToRemove.add(4);
 denseInstance.removeAttributes(indicesToRemove);
 Assert.assertArrayEquals(new double[] {1.0, 3.0, 5.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testRemoveAttributesMethodWithInvalidIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indicesToRemove = new HashSet<Integer>();
 indicesToRemove.add(-1);
 try {
 denseInstance.removeAttributes(indicesToRemove);
 Assert.fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }
}