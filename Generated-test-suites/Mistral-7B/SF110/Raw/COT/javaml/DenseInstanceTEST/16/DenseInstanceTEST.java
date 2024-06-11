import org.junit.Test;
import org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;
import java.util.*;

public class DenseInstanceTest {

 private static final double DELTA = 1e-9; // for comparisons with double values

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, denseInstance.attributes, DELTA);
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, denseInstance.attributes, DELTA);
 Assert.assertEquals("ClassValue", denseInstance.classValue());
 }

 @Test
 public void testConstructorWithEmptyDoubleArray() {
 DenseInstance denseInstance = new DenseInstance(new double[]{});
 Assert.assertEquals(0, denseInstance.attributes.length);
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(5);
 Assert.assertEquals(5, denseInstance.attributes.length);
 }

 @Test
 public void testValueMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertEquals(1.0, denseInstance.value(0), DELTA);
 Assert.assertEquals(2.0, denseInstance.value(1), DELTA);
 Assert.assertEquals(3.0, denseInstance.value(2), DELTA);
 }

 @Test
 public void testClearMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.clear();
 Assert.assertEquals(0, denseInstance.attributes.length);
 }

 @Test
 public void testContainsKeyMethodWithInteger() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertTrue(denseInstance.containsKey(0));
 Assert.assertTrue(denseInstance.containsKey(1));
 Assert.assertTrue(denseInstance.containsKey(2));
 Assert.assertFalse(denseInstance.containsKey(-1));
 Assert.assertFalse(denseInstance.containsKey(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsValueMethodWithNonNumber() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.containsValue("NotANumber");
 }

 @Test
 public void testContainsValueMethodWithDouble() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertTrue(Math.abs(1.5 - denseInstance.attributes[1]) < DELTA);
 Assert.assertFalse(Math.abs(2.5 - denseInstance.attributes[1]) < DELTA);
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
 Assert.assertEquals(3, entryList.size());
 }

 @Test
 public void testGetMethodWithInteger() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertEquals(1.0, denseInstance.get(0), DELTA);
 Assert.assertEquals(2.0, denseInstance.get(1), DELTA);
 Assert.assertEquals(3.0, denseInstance.get(2), DELTA);
 Assert.assertNull(denseInstance.get(-1));
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 SortedSet<Integer> keySet = denseInstance.keySet();
 Assert.assertEquals(3, keySet.size());
 }

 @Test
 public void testPutMethodWithIntegerAndDouble() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.put(0, 4.0);
 Assert.assertEquals(4.0, denseInstance.attributes[0], DELTA);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutMethodWithKeyValuePair() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.put(new Integer(0), new Double(4.0));
 }

 @Test
 public void testPutAllMethodWithMap() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 4.0);
 map.put(1, 5.0);
 denseInstance.putAll(map);
 Assert.assertEquals(4.0, denseInstance.attributes[0], DELTA);
 Assert.assertEquals(5.0, denseInstance.attributes[1], DELTA);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethodWithObject() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.remove(new Integer(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSizeMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertEquals(3, denseInstance.size());
 }

 @Test
 public void testValuesMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Collection<Double> values = denseInstance.values();
 List<Double> valuesList = new ArrayList<>(values);
 Assert.assertEquals(3, valuesList.size());
 }

 @Test
 public void testNoAttributesMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testToStringMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 String expectedString = "{" + Arrays.toString(new double[]{1.0, 2.0, 3.0}) + ";ClassValue}";
 Assert.assertEquals(expectedString, denseInstance.toString());
 }

 @Test
 public void testRemoveAttributeMethodWithInt() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 denseInstance.removeAttribute(2);
 Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, denseInstance.attributes, DELTA);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithOutOfBoundsIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.removeAttribute(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNullKey() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.put(null, 4.0);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullDoubleArray() {
 DenseInstance denseInstance = new DenseInstance(null);
 }
}