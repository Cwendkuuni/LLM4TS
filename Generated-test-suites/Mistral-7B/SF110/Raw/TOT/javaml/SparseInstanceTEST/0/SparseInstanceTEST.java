import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import java.util.*;

public class SparseInstanceTest {

 @Test
 public void testEmptySparseInstance() {
 SparseInstance emptySparseInstance = new SparseInstance();
 Assert.assertEquals(0, emptySparseInstance.noAttributes());
 Assert.assertTrue(emptySparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.0);
 Assert.assertEquals(5, sparseInstance.noAttributes());
 Assert.assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithData() {
 SparseInstance sparseInstance = new SparseInstance();
 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);
 sparseInstance.putAll(testData);

 Assert.assertEquals(3, sparseInstance.noAttributes());
 Assert.assertFalse(sparseInstance.isEmpty());

 for (int i = 0; i < 3; i++) {
 Assert.assertEquals(testData.get(i), sparseInstance.value(i), 0.001);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(-1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullClassValue() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.0, null);
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 Assert.assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoAttributesInConstructor() {
 SparseInstance sparseInstance = new SparseInstance(-1, 0.0, null);
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);
 sparseInstance.putAll(testData);
 sparseInstance.clear();

 Assert.assertEquals(0, sparseInstance.noAttributes());
 Assert.assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);
 sparseInstance.putAll(testData);

 Assert.assertTrue(sparseInstance.containsKey(0));
 Assert.assertTrue(sparseInstance.containsKey(1));
 Assert.assertTrue(sparseInstance.containsKey(2));
 Assert.assertFalse(sparseInstance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);
 sparseInstance.putAll(testData);

 Assert.assertTrue(sparseInstance.containsValue(1.0));
 Assert.assertTrue(sparseInstance.containsValue(2.0));
 Assert.assertTrue(sparseInstance.containsValue(3.0));
 Assert.assertFalse(sparseInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);
 sparseInstance.putAll(testData);

 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();

 Assert.assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);
 sparseInstance.putAll(testData);

 for (int i = 0; i < 3; i++) {
 Assert.assertEquals(testData.get(i), sparseInstance.get(i), 0.001);
 }

 Assert.assertEquals(0.0, sparseInstance.get(-1), 0.001);
 }

 @Test
 public void testPut() {
 SparseInstance sparseInstance = new SparseInstance(5);

 Double valueToPut = 1.0;
 Integer keyToPut = 0;
 Double oldValue = sparseInstance.put(keyToPut, valueToPut);

 Assert.assertEquals(valueToPut, sparseInstance.get(keyToPut));
 Assert.assertNull(oldValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 SparseInstance sparseInstance = new SparseInstance(5);

 try {
 sparseInstance.put(null, 1.0);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 SparseInstance sparseInstance = new SparseInstance(5);

 try {
 sparseInstance.put(0, null);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testPutAll() {
 SparseInstance sparseInstance = new SparseInstance(5);

 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);

 sparseInstance.putAll(testData);

 for (int i = 0; i < 3; i++) {
 Assert.assertEquals(testData.get(i), sparseInstance.get(i), 0.001);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllNullMap() {
 SparseInstance sparseInstance = new SparseInstance(5);

 try {
 sparseInstance.putAll(null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testRemove() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);
 sparseInstance.putAll(testData);

 Double removedValue = sparseInstance.remove(1);

 Assert.assertEquals(2.0, removedValue.doubleValue());
 Assert.assertEquals(2, sparseInstance.noAttributes());
 Assert.assertFalse(sparseInstance.containsKey(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveInvalidKey() {
 SparseInstance sparseInstance = new SparseInstance(5);

 try {
 sparseInstance.remove(3);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesInConstructor() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.0, null);
 }

 @Test
 public void testCopy() {
 SparseInstance originalSparseInstance = new SparseInstance();
 originalSparseInstance.put(0, 1.0);
 originalSparseInstance.put(1, 2.0);
 originalSparseInstance.put(2, 3.0);

 SparseInstance copySparseInstance = originalSparseInstance.copy();

 Assert.assertEquals(originalSparseInstance.noAttributes(), copySparseInstance.noAttributes());
 Assert.assertEquals(originalSparseInstance.data.size(), copySparseInstance.data.size());

 for (int i = 0; i < 3; i++) {
 Assert.assertEquals(originalSparseInstance.get(i).doubleValue(), copySparseInstance.get(i).doubleValue());
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Map<Integer, Double> testData = new HashMap<>();
 testData.put(0, 1.0);
 testData.put(1, 2.0);
 testData.put(2, 3.0);
 testData.put(3, 4.0);
 testData.put(4, 5.0);
 sparseInstance.putAll(testData);

 Set<Integer> indicesToRemove = new HashSet<>();
 indicesToRemove.add(2);
 indicesToRemove.add(4);

 sparseInstance.removeAttributes(indicesToRemove);

 Assert.assertEquals(3, sparseInstance.noAttributes());
 Assert.assertFalse(sparseInstance.containsKey(2));
 Assert.assertFalse(sparseInstance.containsKey(4));
 }
}