import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import org.junit.Test;

public class DenseInstanceTest {

 private double[] testData = {1.0, 2.0, 3.0, 4.0};
 private DenseInstance denseInstance;

 @Test
 public void testConstructorWithArray() {
 denseInstance = new DenseInstance(testData);
 assertArrayEquals(testData, denseInstance.attributes);
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 denseInstance = new DenseInstance(testData, "ClassValue");
 assertArrayEquals(testData, denseInstance.attributes);
 assertSame("ClassValue", denseInstance.classValue());
 }

 @Test
 public void testConstructorWithSize() {
 denseInstance = new DenseInstance(5);
 assertNotNull(denseInstance.attributes);
 assertArrayEquals(new double[5], denseInstance.attributes);
 }

 @Test
 public void testValueMethod() {
 denseInstance = new DenseInstance(testData);
 for (int i = 0; i < testData.length; i++) {
 assertEquals(testData[i], denseInstance.value(i), 0.0);
 }
 }

 @Test
 public void testClearMethod() {
 denseInstance = new DenseInstance(testData);
 denseInstance.clear();
 assertArrayEquals(new double[testData.length], denseInstance.attributes);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsKeyWithNegativeInteger() {
 denseInstance = new DenseInstance(testData);
 assertFalse(denseInstance.containsKey(-1));
 }

 @Test
 public void testContainsKeyWithIntegerInsideArrayBounds() {
 denseInstance = new DenseInstance(testData);
 assertTrue(denseInstance.containsKey(0));
 }

 @Test
 public void testContainsValueWithDoubleInsideArrayBounds() {
 denseInstance = new DenseInstance(testData);
 for (double value : testData) {
 assertTrue(denseInstance.containsValue(value));
 }
 }

 @Test
 public void testContainsValueWithDoubleOutsideArrayBounds() {
 denseInstance = new DenseInstance(testData);
 assertFalse(denseInstance.containsValue(5.0));
 }

 @Test
 public void testEntrySetMethod() {
 denseInstance = new DenseInstance(testData);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(testData.length, entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 assertEquals(testData[entry.getKey()], entry.getValue(), 0.0);
 }
 }

 @Test
 public void testGetMethodWithIntegerKey() {
 denseInstance = new DenseInstance(testData);
 for (int i = 0; i < testData.length; i++) {
 assertEquals(testData[i], denseInstance.get(i), 0.0);
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithOutOfBoundsIntegerKey() {
 denseInstance = new DenseInstance(testData);
 denseInstance.get(-1);
 }

 @Test
 public void testIsEmptyMethod() {
 denseInstance = new DenseInstance(new double[0]);
 assertTrue(denseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 denseInstance = new DenseInstance(testData);
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(testData.length, keySet.size());
 for (int i = 0; i < testData.length; i++) {
 assertTrue(keySet.contains(i));
 }
 }

 @Test
 public void testPutMethodWithIntegerKeyAndDoubleValue() {
 denseInstance = new DenseInstance(testData);
 double newValue = 5.0;
 denseInstance.put(0, newValue);
 assertEquals(newValue, denseInstance.value(0), 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutMethodWithDuplicateIntegerKey() {
 denseInstance = new DenseInstance(testData);
 denseInstance.put(0, 5.0);
 try {
 denseInstance.put(0, 6.0);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testPutAllMethod() {
 denseInstance = new DenseInstance(testData);
 double[] newData = {5.0, 6.0, 7.0};
 Map<Integer, Double> newMap = new HashMap<>();
 newMap.put(1, 5.0);
 newMap.put(2, 6.0);
 newMap.put(3, 7.0);
 denseInstance.putAll(newMap);
 assertArrayEquals(new double[testData.length + 3], denseInstance.attributes);
 }

 @Test
 public void testRemoveAttributeMethodWithIntegerIndex() {
 denseInstance = new DenseInstance(testData);
 denseInstance.removeAttribute(0);
 assertArrayEquals(new double[testData.length - 1], denseInstance.attributes);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributeMethodWithOutOfBoundsIntegerIndex() {
 denseInstance = new DenseInstance(testData);
 denseInstance.removeAttribute(-1);
 }
}