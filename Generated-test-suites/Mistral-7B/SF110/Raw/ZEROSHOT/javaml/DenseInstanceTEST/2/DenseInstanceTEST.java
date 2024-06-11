import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;

public class DenseInstanceTest {

 @Test
 public void testDenseInstanceWithNullAttributeArray() {
 DenseInstance denseInstance = new DenseInstance(null);
 assertNull(denseInstance.attributes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDenseInstanceWithNegativeSize() {
 new DenseInstance(-1);
 }

 @Test
 public void testDenseInstanceWithEmptyAttributeArray() {
 DenseInstance denseInstance = new DenseInstance();
 assertTrue(denseInstance.isEmpty());
 assertEquals(0, denseInstance.noAttributes());
 assertNull(denseInstance.getClassValue());
}

 @Test
 public void testDenseInstanceWithSingleAttribute() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0});
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(1.0, denseInstance.attributes[0], 0.0);
}

 @Test
 public void testDenseInstanceWithMultipleAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(3.0, denseInstance.value(2), 0.0);
}

 @Test
 public void testDenseInstanceWithClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "classValue");
 assertEquals("classValue", denseInstance.getClassValue().toString());
}

 @Test
 public void testDenseInstanceClear() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
}

 @Test
 public void testDenseInstanceContainsKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
}

 @Test
 public void testDenseInstanceContainsValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsValue(3.0));
}

 @Test
 public void testDenseInstanceEntrySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 int key = entry.getKey();
 double value = entry.getValue();
 assertEquals(key, denseInstance.attributes.length - 1 - indexOf(denseInstance.attributes, value));
 }
}

 @Test
 public void testDenseInstanceGet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, denseInstance.get(0), 0.0);
 assertEquals(2.0, denseInstance.get(1), 0.0);
 assertEquals(3.0, denseInstance.get(2), 0.0);
}

 @Test
 public void testDenseInstanceIsEmpty() {
 DenseInstance denseInstance = new DenseInstance();
 assertTrue(denseInstance.isEmpty());
}

 @Test
 public void testDenseInstanceKeySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(3, keySet.size());
 for (Integer key : keySet) {
 assertEquals(key, denseInstance.attributes.length - 1 - indexOf(denseInstance.attributes, denseInstance.value(key)));
 }
}

 @Test
 public void testDenseInstancePut() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 double newValue = 4.0;
 denseInstance.put(0, newValue);
 assertEquals(newValue, denseInstance.value(0), 0.0);
 equal(1.0, denseInstance.attributes[1], 0.0);
 equal(2.0, denseInstance.attributes[2], 0.0);
}

 @Test
 public void testDenseInstancePutAll() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstanceToAdd = new DenseInstance(new double[] {4.0, 5.0, 6.0});
 denseInstance.putAll(denseInstanceToAdd.entrySet());
 assertEquals(6, denseInstance.noAttributes());
}

 @Test
 public void testDenseInstanceRemove() {
 throw new UnsupportedOperationException("Cannot unset values from a dense instance.");
}

 @Test
 public void testDenseInstanceSize() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
}

 @Test
 public void testDenseInstanceValues() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Collection<Double> values = denseInstance.values();
 assertEquals(3, values.size());
 for (Double value : values) {
 assertEquals(value, denseInstance.attributes[indexOf(denseInstance.attributes, value)], 0.0);
 }
}

 @Test
 public void testDenseInstanceHashcodeAndEquals() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
 assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 assertEquals(denseInstance1, denseInstance1);
 assertNotEquals(denseInstance1, denseInstance2);
 assertNotEquals(denseInstance1, denseInstance3);
}

 @Test
 public void testDenseInstanceCopy() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance copiedInstance = denseInstance.copy();
 assertEquals(denseInstance, copiedInstance);
}

 @Test
 public void testDenseInstanceRemoveAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indicesToRemove = new HashSet<Integer>() {{
 add(3);
 add(4);
 }};
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(3, denseInstance.noAttributes());
}

 private static int indexOf(double[] array, double target) {
 int index = 0;
 for (; index < array.length; ++index) {
 if (array[index] == target) {
 return index;
 }
 }
 return -1;
 }
}