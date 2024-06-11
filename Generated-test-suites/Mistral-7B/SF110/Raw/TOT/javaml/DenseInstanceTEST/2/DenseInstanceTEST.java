import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;
import org.junit.Test;

public class DenseInstanceTest {

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, instance.noAttributes());
 assertThat(instance.attributes, hasSize(3));
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i), i+1.0, 0.0);
 }
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, 0.0);
 assertEquals(3, instance.noAttributes());
 assertThat(instance.attributes, hasSize(3));
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i), i+1.0, 0.0);
 }
 assertEquals(0.0, instance.classValue());
 }

 @Test
 public void testConstructorWithEmptyDoubleArray() {
 DenseInstance instance = new DenseInstance(new double[]{});
 assertEquals(0, instance.noAttributes());
 assertNull(instance.classValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullDoubleArray() {
 DenseInstance instance = new DenseInstance((double[]) null);
 }

 @Test
 public void testValueMethod() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(2.0, instance.value(1), 0.0);
 assertEquals(3.0, instance.value(2), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithOutOfBoundsIndex() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.value(-1);
 }

 @Test
 public void testClearMethod() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.clear();
 assertNull(instance.classValue());
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKeyMethodWithIntegerKey() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 assertFalse(instance.containsKey(-1));
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testContainsValueMethodWithDoubleValue() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(instance.containsValue(1.0));
 assertTrue(instance.containsValue(2.0));
 assertTrue(instance.containsValue(3.0));
 assertFalse(instance.containsValue(0.5));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(3, entries.size());
 for (Map.Entry<Integer, Double> entry : entries) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 }

 @Test
 public void testGetMethodWithIntegerKey() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(3.0, instance.get(2), 0.0);
 assertNull(instance.get(-1));
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 SortedSet<Integer> keys = instance.keySet();
 assertEquals(3, keys.size());
 for (Integer key : keys) {
 assertEquals(key, key.intValue());
 }
 }

 @Test
 public void testPutMethod() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0});
 instance.put(2, 3.0);
 assertEquals(3, instance.noAttributes());
 assertEquals(3.0, instance.value(2), 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutMethodWithNullKey() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0});
 instance.put(null, 3.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithInvalidKey() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0});
 instance.put(-1, 3.0);
 }

 @Test
 public void testPutAllMethod() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0});
 DenseInstance anotherInstance = new DenseInstance(new double[] {3.0, 4.0, 5.0});
 instance.putAll(anotherInstance.entrySet());
 assertEquals(5, instance.noAttributes());
 for (int i = 0; i < 5; i++) {
 assertEquals(instance.value(i), i+3.0, 0.0);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethodWithKey() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributesMethodWithCollectionOfKeys() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<>();
 indices.add(0);
 indices.add(3);
 instance.removeAttributes(indices);
 }
}