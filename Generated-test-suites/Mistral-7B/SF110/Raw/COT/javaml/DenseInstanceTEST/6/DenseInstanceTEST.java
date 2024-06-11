import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;
import java.util.*;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithArray() {
 double[] attributes = {1.0, 2.0, 3.0};
 DenseInstance denseInstance = createDenseInstance(attributes, null);
 assertNotNull(denseInstance);
 assertArrayEquals(attributes, denseInstance.attributes);
 }

 @Test
 public void testConstructorWithClassValue() {
 double[] attributes = {1.0, 2.0, 3.0};
 DenseInstance denseInstance = createDenseInstance(attributes, "classValue");
 assertNotNull(denseInstance);
 assertArrayEquals(attributes, denseInstance.attributes);
 assertNotNull(denseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(3);
 assertNotNull(denseInstance);
 assertNotNull(denseInstance.attributes);
 assertArrayEquals(new double[3], denseInstance.attributes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIndexForValueMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[10], null);
 try {
 denseInstance.value(-1);
 } catch (IndexOutOfBoundsException e) {
 throw new IllegalArgumentException("Expecting IllegalArgumentException", e);
 }
 }

 @Test
 public void testValueMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.value(0), 1e-8);
 assertEquals(2.0, denseInstance.value(1), 1e-8);
 assertEquals(3.0, denseInstance.value(2), 1e-8);
 }

 @Test
 public void testClearMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.clear();
 assertArrayEquals(new double[0], denseInstance.attributes);
 }

 @Test
 public void testContainsKeyMethodWithValidInteger() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 }

 @Test
 public void testContainsKeyMethodWithInvalidObject() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertFalse(denseInstance.containsKey(new Object()));
 }

 @Test
 public void testContainsValueMethodWithValidNumber() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsValue(3.0));
 }

 @Test
 public void testContainsValueMethodWithInvalidObject() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertFalse(denseInstance.containsValue(new Object()));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGetMethodWithValidInteger() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.get(0), 1e-8);
 assertEquals(2.0, denseInstance.get(1), 1e-8);
 assertEquals(3.0, denseInstance.get(2), 1e-8);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithInvalidInteger() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 try {
 denseInstance.get(-1);
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[0], null);
 assertTrue(denseInstance.isEmpty());
 DenseInstance nonEmptyInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertFalse(nonEmptyInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPutMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 double newValue = 4.0;
 denseInstance.put(0, newValue);
 assertEquals(newValue, denseInstance.get(0), 1e-8);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutMethodWithUnsupportedOperation() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 try {
 denseInstance.put(null, null);
 } catch (NullPointerException e) {
 throw new UnsupportedOperationException("Expecting UnsupportedOperationException", e);
 }
 }

 @Test
 public void testPutAllMethod() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {4.0, 5.0, 6.0}, null);
 denseInstance1.putAll(denseInstance2.entrySet());
 assertEquals(6, denseInstance1.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethodWithUnsupportedOperation() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 try {
 denseInstance.remove(0);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributesMethodWithUnsupportedOperation() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 try {
 denseInstance.removeAttributes(indices);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testSizeMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[0], null);
 assertEquals(0, denseInstance.noAttributes());
 DenseInstance nonEmptyInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(3, nonEmptyInstance.noAttributes());
 }

 @Test
 public void testValuesMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 List<Double> values = new ArrayList<>(denseInstance.values());
 assertEquals(3, values.size());
 }

 @Test
 public void testHashCodeMethod() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[] {1.1, 2.1, 3.1}, null);
 assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameObjects() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = denseInstance1;
 assertTrue(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithDifferentObjectsButEqualAttributes() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = new DenseInstance();
 denseInstance2.attributes = denseInstance1.attributes.clone();
 assertTrue(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithDifferentObjectsButDifferentAttributes() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = new DenseInstance();
 denseInstance2.attributes = new double[] {1.1, 2.1, 3.1};
 assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithInstanceOfDifferentClass() {
 Object other = new Object();
 assertFalse(denseInstance1.equals(other));
 }

 @Test
 public void testCopyMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance copiedDenseInstance = denseInstance.copy();
 assertEquals(denseInstance, copiedDenseInstance);
 }

 @Test
 public void testRemoveAttributeMethodWithValidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.removeAttribute(1);
 assertEquals(2, denseInstance.noAttributes());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 try {
 denseInstance.removeAttribute(3);
 } catch (ArrayIndexOutOfBoundsException e) {
 // Expected
 }
 }

}