import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

 @Test
 public void testConstructorWithArray() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(2.0, instance.value(1), 0.0);
 assertEquals(3.0, instance.value(2), 0.0);
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 assertEquals("ClassValue", instance.getClassValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance instance = new DenseInstance(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueWithNegativeIndex() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 instance.value(-1);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testClear() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsKeyWithNonInteger() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 instance.containsKey("key");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNegativeIndex() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 instance.put(-1, 4.0);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testContainsValue() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 1.0, 2.0, 1.0, 1.0});
 assertTrue(instance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(3.0, instance.get(2), 0.0);
 }

 @Test
 public void testIsEmpty() {
 DenseInstance instance = new DenseInstance(new double[] {});
 assertTrue(instance.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 instance.remove(0);
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 @Test
 public void testSize() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, instance.size());
 }

 @Test
 public void testValues() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 List<Double> values = new ArrayList<>(instance.values());
 assertEquals(3, values.size());
 assertEquals(1.0, values.get(0), 0.0);
 assertEquals(2.0, values.get(1), 0.0);
 assertEquals(3.0, values.get(2), 0.0);
 }

 @Test
 public void testNoAttributes() {
 DenseInstance instance = new DenseInstance(new double[] {});
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testToString() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals("{1.0, 2.0, 3.0;ClassValue}", instance.toString());
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testHashCodeAndEquals() {
 DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance instance3 = new DenseInstance(new double[] {1.0, 2.0, 4.0});
 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertEquals(instance1, instance1);
 assertEquals(instance1, instance2);
 assertNotEquals(instance1, instance3);
 }

 @Test
 public void testCopy() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance copiedInstance = instance.copy();
 assertEquals(instance, copiedInstance);
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(2, 4));
 instance.removeAttributes(indicesToRemove);
 assertEquals(3, instance.noAttributes());
 }
}