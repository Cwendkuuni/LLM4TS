import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

 // Testing constructor with no arguments
 @Test
 public void testEmptyConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertNull(instance.getClassValue());
 }

 // Testing constructor with int argument
 @Test
 public void testConstructorWithNoAttributes() {
 SparseInstance instance = new SparseInstance(0);
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertNull(instance.getClassValue());
 }

 // Testing constructor with int and defaultValue argument
 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance instance = new SparseInstance(0, 0.0);
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertNull(instance.getClassValue());
 }

 // Testing constructor with int, defaultValue and classValue argument
 @Test
 public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
 SparseInstance instance = new SparseInstance(0, 0.0, "testClass");
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertEquals("testClass", instance.getClassValue().toString());
 }

 // Testing constructor with double array
 @Test
 public void testConstructorWithDoubleArray() {
 double[] dataVector = new double[] {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(dataVector);
 assertEquals(3, instance.noAttributes());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0), 0.001);
 assertFalse(instance.containsKey(4));
 assertNull(instance.getClassValue());
 }

 // Testing setters and getters
 @Test
 public void testSettersAndGetters() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 instance.setClassValue("testClass");
 assertEquals("testClass", instance.getClassValue().toString());
 }

 // Testing set method
 @Test
 public void testSetMethod() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 instance.put(Integer.valueOf(0), Double.valueOf(5.0));
 assertEquals(5.0, instance.get(0), 0.001);
 }

 // Testing putAll method
 @Test
 public void testPutAllMethod() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 SparseInstance instance2 = new SparseInstance(new double[] {5.0, 6.0, 7.0, 8.0});
 instance.putAll(instance2.data);
 assertEquals(8, instance.noAttributes());
 assertEquals(6.0, instance.get(4), 0.001);
 }

 // Testing remove method
 @Test
 public void testRemoveMethod() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 instance.removeAttribute(2);
 assertEquals(3, instance.noAttributes());
 assertFalse(instance.containsKey(3));
 }

 // Testing clear method
 @Test
 public void testClearMethod() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 }

 // Testing isEmpty method
 @Test
 public void testIsEmptyMethod() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 assertFalse(instance.isEmpty());
 instance.clear();
 assertTrue(instance.isEmpty());
 }

 // Testing containsKey and containsValue methods
 @Test
 public void testContainsKeyAndContainsValueMethods() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 assertTrue(instance.containsKey(3));
 assertFalse(instance.containsValue(5.0));
 }

 // Testing entrySet method
 @Test
 public void testEntrySetMethod() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(4, entries.size());
 }

 // Testing value method
 @Test
 public void testValueMethod() {
 SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 assertEquals(1.0, instance.value(0), 0.001);
 }
}