import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.DenseInstance;
import static org.junit.Assert.*;

public class DenseInstanceTest {

 private double[] attributes;
 private DenseInstance denseInstance;

 @Before
 public void setUp() {
 double[] arr = new double[]{1.0, 2.0, 3.0, 4.0};
 this.attributes = arr.clone();
 this.denseInstance = new DenseInstance(this.attributes);
 }

 @Test
 public void testConstructorWithArray() {
 DenseInstance denseInstance = new DenseInstance(this.attributes);
 assertEquals(this.attributes.length, denseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 Object classValue = "classValue";
 DenseInstance denseInstance = new DenseInstance(this.attributes, classValue);
 assertEquals(classValue, denseInstance.classValue());
 }

 @Test
 public void testConstructorWithSize() {
 int size = 5;
 DenseInstance denseInstance = new DenseInstance(size);
 assertEquals(size, denseInstance.noAttributes());
 }

 @Test
 public void testValueMethod() {
 int pos = 1;
 double expectedValue = this.attributes[pos];
 double actualValue = this.denseInstance.value(pos);
 assertEquals(expectedValue, actualValue, 1e-8);
 }

 @Test
 public void testClearMethod() {
 this.denseInstance.clear();
 Assert.assertArrayEquals(new double[0], this.denseInstance.attributes, 0.0);
 }

 @Test
 public void testContainsKeyMethodWithValidKey() {
 int key = 1;
 boolean expectedResult = true;
 boolean actualResult = this.denseInstance.containsKey(key);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testContainsKeyMethodWithInvalidKey() {
 Object invalidKey = new Object();
 boolean expectedResult = false;
 boolean actualResult = this.denseInstance.containsKey(invalidKey);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testContainsValueMethodWithValidValue() {
 double validValue = 2.0;
 boolean expectedResult = true;
 boolean actualResult = this.denseInstance.containsValue(validValue);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testContainsValueMethodWithInvalidValue() {
 double invalidValue = 5.0;
 boolean expectedResult = false;
 boolean actualResult = this.denseInstance.containsValue(invalidValue);
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethod() {
 this.denseInstance.remove(1);
 fail("This method should throw UnsupportedOperationException.");
 }

 @Test
 public void testPutMethod() {
 int key = 5;
 double value = 5.5;
 double oldValue = this.denseInstance.attributes[key];
 this.denseInstance.put(key, value);
 double newValue = this.denseInstance.attributes[key];
 assertEquals(value, newValue, 1e-8);
 assertEquals(oldValue, this.attributes[key], 1e-8);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutAllMethod() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(1, 1.0);
 map.put(2, 2.0);
 this.denseInstance.putAll(map);
 fail("This method should throw UnsupportedOperationException.");
 }

 @Test
 public void testGetMethodWithValidKey() {
 int key = 1;
 double expectedValue = this.attributes[key];
 double actualValue = this.denseInstance.get(key);
 assertEquals(expectedValue, actualValue, 1e-8);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithInvalidKey() {
 Object invalidKey = new Object();
 this.denseInstance.get(invalidKey);
 fail("This method should throw IllegalArgumentException.");
 }

 @Test
 public void testEntrySetMethod() {
 Set<Map.Entry<Integer, Double>> entrySet = this.denseInstance.entrySet();
 assertEquals(this.attributes.length, entrySet.size());
 }

 @Test
 public void testIsEmptyMethod() {
 assertFalse(this.denseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 SortedSet<Integer> keySet = this.denseInstance.keySet();
 assertEquals(this.attributes.length, keySet.size());
 }

 @Test
 public void testPutAllMethodWithValidMap() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(1, 1.0);
 map.put(2, 2.0);
 this.denseInstance.putAll(map);
 for (int i = 0; i < this.attributes.length; ++i) {
 double expectedValue = map.get(i);
 double actualValue = this.denseInstance.attributes[i];
 assertEquals(expectedValue, actualValue, 1e-8);
 }
 }

 @Test
 public void testRemoveAttributesMethodWithEmptySet() {
 Set<Integer> indices = new HashSet<>();
 this.denseInstance.removeAttributes(indices);
 assertArrayEquals(this.attributes, this.denseInstance.attributes, 0.0);
 }

 @Test
 public void testRemoveAttributesMethodWithValidSet() {
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 this.denseInstance.removeAttributes(indices);
 assertArrayEquals(new double[]{1.0, 4.0}, this.denseInstance.attributes, 0.0);
 }

 @Test
 public void testHashCodeMethod() {
 DenseInstance otherInstance = new DenseInstance(this.attributes.clone());
 assertEquals(this.denseInstance.hashCode(), otherInstance.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameObject() {
 DenseInstance otherInstance = new DenseInstance(this.attributes.clone());
 assertTrue(this.denseInstance.equals(otherInstance));
 }

 @Test
 public void testEqualsMethodWithDifferentObject() {
 DenseInstance otherInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 assertFalse(this.denseInstance.equals(otherInstance));
 }

 @Test
 public void testEqualsMethodWithNullObject() {
 assertFalse(this.denseInstance.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentClass() {
 Object otherInstance = new Object();
 assertFalse(this.denseInstance.equals(otherInstance));
 }

 @Test
 public void testCopyMethod() {
 DenseInstance copiedInstance = this.denseInstance.copy();
 assertEquals(this.denseInstance, copiedInstance);
 }
}