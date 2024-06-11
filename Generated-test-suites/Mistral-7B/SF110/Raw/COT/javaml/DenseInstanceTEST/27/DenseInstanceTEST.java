import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.util.Arrays;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithAttributes() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 Assert.assertNull(denseInstance.classValue());
 }

 @Test
 public void testConstructorWithAttributesAndClassValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 Assert.assertEquals("ClassValue", denseInstance.classValue());
 }

 @Test
 public void testConstructorWithEmptyArray() {
 DenseInstance denseInstance = new DenseInstance(new double[0]);
 Assert.assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
 Assert.assertNull(denseInstance.classValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(5);
 Assert.assertArrayEquals(new double[5], denseInstance.attributes, 0.0);
 Assert.assertNull(denseInstance.classValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.value(-1);
 }

 @Test
 public void testValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.value(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.clear();
 Assert.assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
 }

 @Test
 public void testContainsKeyWithInteger() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertTrue(denseInstance.containsKey(0));
 Assert.assertTrue(denseInstance.containsKey(1));
 Assert.assertTrue(denseInstance.containsKey(2));
 Assert.assertFalse(denseInstance.containsKey(3));
 }

 @Test
 public void testContainsValueWithNumber() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertTrue(denseInstance.containsValue(1.0));
 Assert.assertTrue(denseInstance.containsValue(2.0));
 Assert.assertTrue(denseInstance.containsValue(3.0));
 }

 @Test
 public void testContainsValueWithNaN() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertFalse(denseInstance.containsValue(Double.NaN));
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 Assert.assertEquals(3, entrySet.size());
 }

 @Test
 public void testGetWithInteger() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertEquals(1.0, denseInstance.get(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.get(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.get(2), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInvalidKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.get(-1);
 }

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = new DenseInstance(new double[0]);
 Assert.assertTrue(denseInstance.isEmpty());
 DenseInstance notEmptyInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertFalse(notEmptyInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 SortedSet<Integer> keySet = denseInstance.keySet();
 Assert.assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 double newValue = 4.0;
 double oldValue = denseInstance.put(0, newValue);
 Assert.assertEquals(1.0, oldValue, 0.0);
 Assert.assertEquals(4.0, denseInstance.value(0), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.put(-1, 4.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Map<Integer, Double> map = new HashMap<>();
 map.put(1, 4.0);
 map.put(2, 5.0);
 denseInstance.putAll(map);
 Assert.assertEquals(4.0, denseInstance.value(1), 0.0);
 Assert.assertEquals(5.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, null);
 denseInstance.removeAttribute(2);
 Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.remove(0);
 }

 @Test
 public void testSize() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertEquals(3, denseInstance.size());
 }

 @Test
 public void testValues() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Collection<Double> values = denseInstance.values();
 Assert.assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 String toString = denseInstance.toString();
 Assert.assertEquals("{1.0, 2.0, 3.0;null}", toString);
 }
}