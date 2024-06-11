import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

 @Test
 public void testConstructorWithArray() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
 for (int i = 0; i < 3; i++) {
 assertEquals(i+1.0, denseInstance.value(i), 0.0);
 }
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 assertEquals("ClassValue", denseInstance.classValue());
 for (int i = 0; i < 3; i++) {
 assertEquals(i+1.0, denseInstance.value(i), 0.0);
 }
 }

 @Test
 public void testEmptyConstructor() {
 DenseInstance denseInstance = new DenseInstance();
 assertEquals(0, denseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(5);
 assertEquals(5, denseInstance.noAttributes());
 }

 @Test
 public void testValueMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testClearMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }

 @Test
 public void testContainsKeyMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 }

 @Test
 public void testContainsValueMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsValue(3.0));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
 assertEquals(3, entries.size());
 }

 @Test
 public void testGetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, denseInstance.get(0), 0.0);
 assertEquals(2.0, denseInstance.get(1), 0.0);
 assertEquals(3.0, denseInstance.get(2), 0.0);
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 SortedSet<Integer> keys = denseInstance.keySet();
 assertEquals(3, keys.size());
 }

 @Test
 public void testPutMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.put(0, 1.5);
 assertEquals(1.5, denseInstance.value(0), 0.0);
 }

 @Test
 public void testPutAllMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance anotherDenseInstance = new DenseInstance(new double[] {4.0, 5.0, 6.0});
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 1.5);
 map.put(1, 2.5);
 denseInstance.putAll(map);
 assertEquals(1.5, denseInstance.value(0), 0.0);
 assertEquals(2.5, denseInstance.value(1), 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.remove(0);
 fail("Remove method should throw an UnsupportedOperationException");
 }

 @Test
 public void testSizeMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testValuesMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Collection<Double> values = denseInstance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testRemoveAttributeMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.removeAttribute(1);
 assertFalse(denseInstance.containsKey(1));
 assertEquals(2, denseInstance.noAttributes());
 }

 @Test
 public void testHashCodeMethod() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 int hashCode1 = denseInstance1.hashCode();
 int hashCode2 = denseInstance2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testEqualsMethod() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
 assertTrue(denseInstance1.equals(denseInstance2));
 assertFalse(denseInstance1.equals(denseInstance3));
 }

 @Test
 public void testCopyMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance copiedInstance = denseInstance.copy();
 assertEquals(denseInstance.noAttributes(), copiedInstance.noAttributes());
 for (int i = 0; i < denseInstance.noAttributes(); i++) {
 assertEquals(denseInstance.value(i), copiedInstance.value(i), 0.0);
 }
 }

 @Test
 public void testRemoveAttributesMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 Set<Integer> indicesToRemove = new HashSet<>();
 indicesToRemove.add(2);
 indicesToRemove.add(3);
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(2, denseInstance.noAttributes());
 assertFalse(denseInstance.containsKey(2));
 assertFalse(denseInstance.containsKey(3));
 }
}