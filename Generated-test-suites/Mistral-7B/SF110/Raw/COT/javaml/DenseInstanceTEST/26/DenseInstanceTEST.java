import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.util.Pair;

import org.junit.Test;

public class DenseInstanceTest {

 private double[] createDoubleArray(int length) {
 return new double[length];
 }

 @Test
 public void testDenseInstanceWithValidArguments() {
 // Test constructor with valid double array and null classValue
 DenseInstance denseInstance1 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(3, denseInstance1.noAttributes());

 // Test constructor with valid double array and non-null classValue
 Object classValue = "A";
 DenseInstance denseInstance2 = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, classValue);
 assertEquals(classValue, denseInstance2.classValue());

 // Test constructor with valid size
 DenseInstance denseInstance3 = new DenseInstance(5);
 assertEquals(5, denseInstance3.noAttributes());
 }

 @Test
 public void testValueMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 for (int i = 0; i < denseInstance.noAttributes(); i++) {
 assertEquals(denseInstance.value(i), denseInstance.attributes[i], 0.0);
 }
 }

 @Test
 public void testClearMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 denseInstance.clear();
 assertTrue(denseInstance.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsKeyMethodWithIntegerKeyOutOfBounds() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 int key = denseInstance.noAttributes() + 1;
 assertFalse(denseInstance.containsKey(key));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsKeyMethodWithNonIntegerKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 Object nonIntegerKey = new Object();
 assertFalse(denseInstance.containsKey(nonIntegerKey));
 }

 @Test
 public void testContainsValueMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 double valueToFind = 2.0;
 assertTrue(denseInstance.containsValue(valueToFind));

 double valueToFindOutsideRange = 4.0;
 assertFalse(denseInstance.containsValue(valueToFindOutsideRange));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
 int entrySize = entries.size();
 for (Map.Entry<Integer, Double> entry : entries) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 assertEquals(entrySize, denseInstance.noAttributes());
 }

 @Test
 public void testGetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 for (int i = 0; i < denseInstance.noAttributes(); i++) {
 assertEquals(denseInstance.get(Integer.valueOf(i)), denseInstance.attributes[i]);
 }
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertFalse(denseInstance.isEmpty());

 DenseInstance emptyDenseInstance = new DenseInstance();
 assertTrue(emptyDenseInstance.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testKeySetMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 denseInstance.keySet(); // Do not assign the result to a variable
 }

 @Test
 public void testPutMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 double newValue = 4.0;
 int indexToReplace = 1;
 denseInstance.put(Integer.valueOf(indexToReplace), newValue);
 assertEquals(newValue, denseInstance.value(indexToReplace));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutAllMethodWithNullMap() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 denseInstance.putAll(null);
 }

 @Test
 public void testPutAllMethod() {
 DenseInstance originalInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 DenseInstance instanceToAdd = new DenseInstance(new double[] { 4.0, 5.0, 6.0 });
 DenseInstance expectedInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
 DenseInstance denseInstance = new DenseInstance(originalInstance.noAttributes());
 denseInstance.putAll(instanceToAdd.entrySet());
 assertEquals(expectedInstance.toString(), denseInstance.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethod() {
 DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 denseInstance.remove(0); // Do not assign the result to a variable
 }

 @Test
 public void testRemoveAttributesMethod() {
 DenseInstance originalInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
 Set<Integer> indicesToRemove = new HashSet<Integer>();
 indicesToRemove.add(3);
 indicesToRemove.add(5);
 DenseInstance expectedInstance = new DenseInstance(new double[] { 1.0, 2.0, 4.0 });
 DenseInstance denseInstance = new DenseInstance(originalInstance.noAttributes());
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(expectedInstance.toString(), denseInstance.toString());
 }

}