import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;

public class DenseInstanceTest {

 private DenseInstance denseInstance;

 @Before
 public void setUp() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 denseInstance = new DenseInstance(attributes);
 }

 // Test constructor with empty array
 @Test
 public void testDenseInstanceEmptyConstructor() {
 DenseInstance emptyInstance = new DenseInstance();
 assertEquals(0, emptyInstance.noAttributes());
 }

 // Test constructor with double array
 @Test
 public void testDenseInstanceConstructorWithDoubleArray() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, instance.noAttributes());
 }

 // Test constructor with double array and class value
 @Test
 public void testDenseInstanceConstructorWithDoubleArrayAndClassValue() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertEquals(3, instance.noAttributes());
 assertEquals("ClassValue", instance.classValue());
 }

 // Test value method
 @Test
 public void testValueMethod() {
 assertEquals(1.0, denseInstance.value(0), 0.0);
 }

 // Test clear method
 @Test
 public void testClearMethod() {
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }

 // Test containsKey method with valid key
 @Test
 public void testContainsKeyMethodWithValidKey() {
 assertTrue(denseInstance.containsKey(0));
 }

 // Test containsKey method with invalid key
 @Test (expected = ClassCastException.class)
 public void testContainsKeyMethodWithInvalidKey() {
 denseInstance.containsKey("InvalidKey");
 }

 // Test containsValue method with valid value
 @Test
 public void testContainsValueMethodWithValidValue() {
 assertTrue(denseInstance.containsValue(2.0));
 }

 // Test containsValue method with invalid value
 @Test
 public void testContainsValueMethodWithInvalidValue() {
 assertFalse(denseInstance.containsValue(4.0));
 }

 // Test entrySet method
 @Test
 public void testEntrySetMethod() {
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 // Test get method with valid key
 @Test
 public void testGetMethodWithValidKey() {
 assertEquals(1.0, denseInstance.get(0), 0.0);
 }

 // Test get method with invalid key
 @Test (expected = ClassCastException.class)
 public void testGetMethodWithInvalidKey() {
 denseInstance.get("InvalidKey");
 }

 // Test isEmpty method
 @Test
 public void testIsEmptyMethod() {
 assertFalse(denseInstance.isEmpty());
 }

 // Test keySet method
 @Test
 public void testKeySetMethod() {
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 // Test put method with valid key and value
 @Test
 public void testPutMethodWithValidKeyAndValue() {
 denseInstance.put(1, 4.0);
 assertEquals(4.0, denseInstance.get(1), 0.0);
 }

 // Test put method with duplicate key
 @Test (expected = IllegalArgumentException.class)
 public void testPutMethodWithDuplicateKey() {
 denseInstance.put(0, 4.0);
 denseInstance.put(0, 5.0);
 }

 // Test putAll method with valid map
 @Test
 public void testPutAllMethodWithValidMap() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(1, 4.0);
 map.put(2, 5.0);
 denseInstance.putAll(map);
 assertEquals(5.0, denseInstance.get(2), 0.0);
 }

 // Test remove method (unsupported)
 @Test (expected = UnsupportedOperationException.class)
 public void testRemoveMethod() {
 denseInstance.remove(0);
 }

 // Test size method (deprecated)
 @Test
 public void testSizeMethod() {
 assertEquals(3, denseInstance.size());
 }

 // Test values method
 @Test
 public void testValuesMethod() {
 List<Double> values = new ArrayList<Double>();
 for (Double value : denseInstance.values()) {
 values.add(value);
 }
 assertEquals(3, values.size());
 }

 // Test noAttributes method
 @Test
 public void testNoAttributesMethod() {
 assertEquals(3, denseInstance.noAttributes());
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
 String toString = denseInstance.toString();
 assertTrue(toString.contains("[1.0, 2.0, 3.0;ClassValue]"));
 }

 // Test removeAttribute method with valid index
 @Test
 public void testRemoveAttributeMethodWithValidIndex() {
 denseInstance.removeAttribute(1);
 assertEquals(2, denseInstance.noAttributes());
 }

 // Test removeAttribute method with invalid index
 @Test (expected = ArrayIndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithInvalidIndex() {
 denseInstance.removeAttribute(4);
 }

 // Test hashCode method
 @Test
 public void testHashCodeMethod() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 // Test equals method with same instance
 @Test
 public void testEqualsMethodWithSameInstance() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance1.equals(instance1));
 }

 // Test equals method with different classes
 @Test
 public void testEqualsMethodWithDifferentClasses() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Object obj = new Object();
 assertFalse(instance1.equals(obj));
 }

 // Test equals method with different attributes
 @Test
 public void testEqualsMethodWithDifferentAttributes() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 DenseInstance instance2 = new DenseInstance(new double[]{1.0, 3.0, 3.0});
 assertFalse(instance1.equals(instance2));
 }

 // Test equals method with different classValue
 @Test
 public void testEqualsMethodWithDifferentClassValue() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue1");
 DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue2");
 assertFalse(instance1.equals(instance2));
 }

 // Test copy method
 @Test
 public void testCopyMethod() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 DenseInstance instance2 = instance1.copy();
 assertEquals(instance1, instance2);
 }

 // Test removeAttributes method with valid indices
 @Test
 public void testRemoveAttributesMethodWithValidIndices() {
 Set<Integer> indicesToRemove = new HashSet<>();
 indicesToRemove.add(1);
 indicesToRemove.add(2);
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(1, denseInstance.noAttributes());
 }

 // Test removeAttributes method with empty set
 @Test
 public void testRemoveAttributesMethodWithEmptySet() {
 denseInstance.removeAttributes(new HashSet<>());
 assertEquals(3, denseInstance.noAttributes());
 }

 // Test removeAttributes method with invalid indices
 @Test (expected = UnsupportedOperationException.class)
 public void testRemoveAttributesMethodWithInvalidIndices() {
 Set<Integer> indicesToRemove = new HashSet<>();
 indicesToRemove.add(4);
 denseInstance.removeAttributes(indicesToRemove);
 }

}