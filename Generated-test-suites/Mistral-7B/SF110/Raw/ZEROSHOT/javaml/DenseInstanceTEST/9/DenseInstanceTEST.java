import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DenseInstanceTest {

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.0);
 Assert.assertEquals("ClassValue", denseInstance.classValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(5);
 Assert.assertArrayEquals(new double[5], denseInstance.attributes);
 }

 @Test
 public void testValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.value(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.clear();
 Assert.assertArrayEquals(new double[0], denseInstance.attributes);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsKeyWithNegativeInteger() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.containsKey(-1);
 }

 @Test
 public void testContainsKeyWithValidInteger() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertTrue(denseInstance.containsKey(0));
 Assert.assertTrue(denseInstance.containsKey(1));
 Assert.assertTrue(denseInstance.containsKey(2));
 }

 @Test
 public void testContainsValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertTrue(denseInstance.containsValue(1.0));
 Assert.assertTrue(denseInstance.containsValue(2.0));
 Assert.assertTrue(denseInstance.containsValue(3.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 int index = 0;
 for (Map.Entry<Integer, Double> entry : entrySet) {
 Assert.assertEquals(index++, entry.getKey());
 Assert.assertEquals(denseInstance.value(index), entry.getValue(), 0.0);
 }
 }

 @Test
 public void testGet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(1.0, denseInstance.get(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.get(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.get(2), 0.0);
 }

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = new DenseInstance();
 Assert.assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Integer> keySet = denseInstance.keySet();
 int index = 0;
 for (Integer key : keySet) {
 Assert.assertEquals(index++, key);
 }
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 double newValue = 4.0;
 denseInstance.put(0, newValue);
 Assert.assertEquals(newValue, denseInstance.value(0), 0.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance anotherDenseInstance = new DenseInstance(new double[] {4.0, 5.0, 6.0});
 Set<Map.Entry<Integer, Double>> anotherEntrySet = anotherDenseInstance.entrySet();
 for (Map.Entry<Integer, Double> entry : anotherEntrySet) {
 denseInstance.put(entry.getKey(), entry.getValue());
 }
 Assert.assertArrayEquals(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, denseInstance.attributes, 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.remove(0);
 }

 @Test
 public void testSize() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(3, denseInstance.size());
 }

 @Test
 public void testValues() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Collection<Double> values = denseInstance.values();
 int index = 0;
 for (Double value : values) {
 Assert.assertEquals(denseInstance.value(index), value, 0.0);
 index++;
 }
 }

 @Test
 public void testNoAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 String expectedString = "{" + Arrays.toString(denseInstance.attributes) + ";ClassValue}";
 Assert.assertEquals(expectedString, denseInstance.toString());
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0});
 denseInstance.removeAttribute(1);
 Assert.assertArrayEquals(new double[] {1.0, 3.0, 4.0, 5.0, 6.0}, denseInstance.attributes, 0.0);
 }

 @Test
 public void testHashCode() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Assert.assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 }

 @Test
 public void testEquals() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance3 = new DenseInstance(new double[] {1.0, 2.1, 3.0});
 Assert.assertTrue(denseInstance1.equals(denseInstance2));
 Assert.assertFalse(denseInstance1.equals(denseInstance3));
 }

 @Test
 public void testCopy() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance copy = denseInstance.copy();
 Assert.assertArrayEquals(denseInstance.attributes, copy.attributes, 0.0);
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0});
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
 denseInstance.removeAttributes(indicesToRemove);
 Assert.assertArrayEquals(new double[] {1.0, 4.0, 5.0, 6.0}, denseInstance.attributes, 0.0);
 }
}