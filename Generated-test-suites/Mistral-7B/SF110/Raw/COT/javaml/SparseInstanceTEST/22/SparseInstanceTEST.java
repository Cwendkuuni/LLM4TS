import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.SparseInstance;
import java.util.*;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testConstructorWithNoArguments() {
 sparseInstance = new SparseInstance();
 Assert.assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 sparseInstance = new SparseInstance(0);
 Assert.assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
 sparseInstance = new SparseInstance(0, "someClassValue");
 Assert.assertEquals("someClassValue", sparseInstance.getClassValue());
 Assert.assertEquals(0, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeNoAttributes() {
 sparseInstance = new SparseInstance(-1);
 }

 @Test
 public void testConstructorWithDoubleArrayAndNoDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 Assert.assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDoubleArrayAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0);
 Assert.assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDoubleArrayAndDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0, "someClassValue");
 Assert.assertEquals("someClassValue", sparseInstance.getClassValue());
 Assert.assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(3);
 sparseInstance.setNoAttributes(5);
 Assert.assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesWithNegativeNoAttributes() {
 sparseInstance = new SparseInstance(3);
 sparseInstance.setNoAttributes(-1);
 }

 @Test
 public void testGetValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertEquals(1.0, sparseInstance.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithInvalidIndex() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.value(4);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.clear();
 Assert.assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 Assert.assertFalse(sparseInstance.containsKey(Integer.valueOf(4)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 Assert.assertFalse(sparseInstance.containsValue(Double.valueOf(4.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 Assert.assertEquals(3, entries.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 Assert.assertEquals(0.0, sparseInstance.get(Integer.valueOf(3)), 0.001);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetWithInvalidKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.get(null);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 Assert.assertTrue(sparseInstance.isEmpty());
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Integer> keys = sparseInstance.keySet();
 Assert.assertEquals(3, keys.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Assert.assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(null, Double.valueOf(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), null);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 Assert.assertEquals(2, sparseInstance.noAttributes());
 Assert.assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 Assert.assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.remove(Integer.valueOf(1));
 Assert.assertEquals(2, sparseInstance.noAttributes());
 Assert.assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 Assert.assertNull(sparseInstance.get(Integer.valueOf(1)));
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveWithInvalidKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.remove(Integer.valueOf(4));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.removeAttribute(0);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Assert.assertTrue(sparseInstance.toString().contains("{"));
 Assert.assertTrue(sparseInstance.toString().contains("}"));
 }
}