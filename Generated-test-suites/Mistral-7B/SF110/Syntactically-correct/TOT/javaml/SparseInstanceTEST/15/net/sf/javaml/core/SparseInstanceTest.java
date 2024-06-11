import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.util.SimpleDataset;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;
 private AbstractInstance abstractInstance;

 @Before
 public void setUp() {
 sparseInstance = new SparseInstance(new int[]{-1, 3, 2, 1, 0});
 abstractInstance = new AbstractInstance("ClassLabel");
 }

 @Test
 public void testConstructorWithDefaultValueAndNoAttributes() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance instance = new SparseInstance(0);
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 SparseInstance instance = new SparseInstance(5, "ClassLabel");
 assertEquals("ClassLabel", instance.classValue());
 }

 @Test
 public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
 SparseInstance instance = new SparseInstance(5, 0.0, "ClassLabel");
 assertEquals(0, instance.noAttributes());
 assertEquals("ClassLabel", instance.classValue());
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, 0.0);
 assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testConstructorWithDatavectorAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, "ClassLabel");
 assertEquals("ClassLabel", instance.classValue());
 }

 @Test
 public void testConstructorWithDatavectorDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassLabel");
 assertEquals(5, instance.noAttributes());
 assertEquals("ClassLabel", instance.classValue());
 }

 @Test
 public void testValueOfPositiveIndex() {
 assertEquals(2.0, sparseInstance.value(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueOfNegativeIndexOrOutOfBoundsIndex() {
 sparseInstance.value(-1);
 sparseInstance.value(5);
 }

 @Test
 public void testClear() {
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 assertFalse(sparseInstance.containsKey(Integer.valueOf(6)));
 }

 @Test
 public void testContainsValue() {
 assertTrue(sparseInstance.containsValue(Double.valueOf(3.0)));
 assertFalse(sparseInstance.containsValue(Double.valueOf(6.0)));
 }

 @Test
 public void testEntrySet() {
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(1)));
 assertEquals(0.0, sparseInstance.get(Integer.valueOf(3)));
 }

 @Test
 public void testIsEmpty() {
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance.put(Integer.valueOf(6), Double.valueOf(6.0));
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testPutAll() {
 SparseInstance other = new SparseInstance(new int[]{1, 2, 3, 4, 5, 6});
 HashMap<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(Integer.valueOf(1), Double.valueOf(1.1));
 map.put(Integer.valueOf(3), Double.valueOf(3.1));
 sparseInstance.putAll(map);
 assertEquals(6, sparseInstance.noAttributes());
 assertEquals(1.1, sparseInstance.get(Integer.valueOf(1)), 0.001);
 assertEquals(3.1, sparseInstance.get(Integer.valueOf(3)), 0.001);
 }

 @Test
 public void testRemove() {
 sparseInstance.remove(Integer.valueOf(1));
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 sparseInstance.remove(null);
 }

 @Test
 public void testSize() {
 assertEquals(3, sparseInstance.size());
 }

 @Test
 public void testValues() {
 Collection<Double> values = sparseInstance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance.removeAttribute(1);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 String expected = "{1=1.0, 2=2.0, 3=0.0}ClassLabel";
 assertEquals(expected, sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = new SparseInstance();
 SparseInstance instance2 = new SparseInstance();
 instance1.put(Integer.valueOf(1), Double.valueOf(1.0));
 instance2.put(Integer.valueOf(1), Double.valueOf(1.0));
 assertEquals(instance1.hashCode(), instance2.hashCode());
 instance2.put(Integer.valueOf(2), Double.valueOf(2.0));
 assertNotSame(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance instance1 = new SparseInstance();
 SparseInstance instance2 = new SparseInstance();
 assertFalse(instance1.equals(null));
 assertFalse(instance1.equals(instance2));
 instance2.put(Integer.valueOf(1), Double.valueOf(1.0));
 assertTrue(instance1.equals(instance2));
 instance2.put(Integer.valueOf(2), Double.valueOf(2.0));
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testCopy() {
 SparseInstance instance1 = new SparseInstance();
 instance1.put(Integer.valueOf(1), Double.valueOf(1.0));
 SparseInstance instance2 = instance1.copy();
 assertEquals(instance1, instance2);
 }

 @Test
 public void testRemoveAttributes() {
 Set<Integer> indices = new HashSet<Integer>();
 indices.add(1);
 indices.add(2);
 sparseInstance.removeAttributes(indices);
 assertEquals(1, sparseInstance.noAttributes());
 }
}