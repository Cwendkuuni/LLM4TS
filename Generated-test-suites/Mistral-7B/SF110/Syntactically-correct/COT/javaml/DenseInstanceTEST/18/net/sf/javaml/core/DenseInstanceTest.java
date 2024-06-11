import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.NoSuchAttributeException;
import net.sf.javaml.util.Pair;
import net.sf.javaml.util.SimplePair;
import java.util.*;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithArray() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance denseInstance = createDenseInstance(attributes, null);
 Assert.assertArrayEquals(attributes, denseInstance.getAttributes(), 0.0);
 Assert.assertNull(denseInstance.getClassValue());
 Assert.assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 double[] attributes = new double[]{1.0, 2.0, 3.0};
 DenseInstance denseInstance = createDenseInstance(attributes, "classValue");
 Assert.assertArrayEquals(attributes, denseInstance.getAttributes(), 0.0);
 Assert.assertEquals("classValue", denseInstance.getClassValue().toString());
 Assert.assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(3);
 Assert.assertArrayEquals(new double[3], denseInstance.getAttributes(), 0.0);
 Assert.assertNull(denseInstance.getClassValue());
 Assert.assertEquals(3, denseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeSize() {
 new DenseInstance(-1);
 }

 @Test
 public void testValueMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertEquals(1.0, denseInstance.value(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.value(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.value(-1);
 }

 @Test
 public void testClearMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.clear();
 Assert.assertArrayEquals(new double[0], denseInstance.getAttributes(), 0.0);
 Assert.assertNull(denseInstance.getClassValue());
 Assert.assertEquals(0, denseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsKeyMethodWithInvalidKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.containsKey(-1);
 }

 @Test
 public void testContainsKeyMethodWithValidKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertTrue(denseInstance.containsKey(0));
 Assert.assertTrue(denseInstance.containsKey(1));
 Assert.assertTrue(denseInstance.containsKey(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsValueMethodWithInvalidValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.containsValue(new Object(){});
 }

 @Test
 public void testContainsValueMethodWithValidValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertTrue(Math.abs(1.0 - denseInstance.attributes[0]) < 1e-8);
 Assert.assertTrue(Math.abs(2.0 - denseInstance.attributes[1]) < 1e-8);
 Assert.assertTrue(Math.abs(3.0 - denseInstance.attributes[2]) < 1e-8);
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 Assert.assertEquals(3, entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 Assert.assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 }

 @Test
 public void testGetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertEquals(1.0, denseInstance.get(0), 0.0);
 Assert.assertEquals(2.0, denseInstance.get(1), 0.0);
 Assert.assertEquals(3.0, denseInstance.get(2), 0.0);
 }

 @Test(expected = NoSuchAttributeException.class)
 public void testGetMethodWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.get(-1);
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[0], null);
 Assert.assertTrue(denseInstance.isEmpty());
 DenseInstance nonEmptyInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertFalse(nonEmptyInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 SortedSet<Integer> keySet = denseInstance.keySet();
 Assert.assertEquals(3, keySet.size());
 for (int key : keySet) {
 Assert.assertEquals(key, denseInstance.getValue(key).intValue());
 }
 }

 @Test
 public void testPutMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 double newValue = 4.0;
 denseInstance.put(0, newValue);
 Assert.assertEquals(newValue, denseInstance.getValue(0), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithInvalidKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.put(-1, 4.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithInvalidValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.put(0, new Object(){});
 }

 @Test
 public void testPutAllMethod() {
 DenseInstance firstInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance secondInstance = createDenseInstance(new double[]{4.0, 5.0, 6.0}, "classValue");
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 4.0);
 map.put(1, 5.0);
 map.put(2, 6.0);
 firstInstance.putAll(map);
 Assert.assertEquals(6, firstInstance.noAttributes());
 for (int i = 0; i < 6; ++i) {
 Assert.assertEquals(firstInstance.getValue(i), i < 3 ? firstInstance.attributes[i] : secondInstance.attributes[i - 3], 0.0);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethodWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.remove(-1);
 }

 @Test
 public void testRemoveAttributesMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, null);
 Set<Integer> indices = new HashSet<>();
 indices.add(2);
 indices.add(4);
 denseInstance.removeAttributes(indices);
 Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, denseInstance.getAttributes(), 0.0);
 Assert.assertEquals(3, denseInstance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributesMethodWithInvalidIndices() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, null);
 Set<Integer> indices = new HashSet<>();
 indices.add(-1);
 denseInstance.removeAttributes(indices);
 }

 @Test
 public void testSizeMethodDeprecated() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertEquals(3, denseInstance.size());
 }

 @Test
 public void testValuesMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Collection<Double> values = denseInstance.values();
 Assert.assertEquals(3, values.size());
 for (Double value : values) {
 Assert.assertEquals(value.doubleValue(), denseInstance.value(values.indexOf(value)), 0.0);
 }
 }

 @Test
 public void testRemoveAttributeMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, null);
 denseInstance.removeAttribute(2);
 Assert.assertArrayEquals(new double[]{1.0, 2.0, 4.0, 5.0}, denseInstance.getAttributes(), 0.0);
 Assert.assertEquals(4, denseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, null);
 denseInstance.removeAttribute(-1);
 }

 @Test
 public void testHashCodeMethod() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, null);
 Assert.assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 Assert.assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 Assert.assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameInstance() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertTrue(denseInstance.equals(denseInstance));
 }

 @Test
 public void testEqualsMethodWithDifferentClass() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Object other = new Object();
 Assert.assertFalse(denseInstance.equals(other));
 }

 @Test
 public void testEqualsMethodWithDifferentAttributes() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.1, 2.1, 3.1}, null);
 Assert.assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithSameAttributesButDifferentClassValue() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "classValue2");
 Assert.assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithSameAttributesAndSameClassValue() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Assert.assertTrue(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testCopyMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance copiedInstance = denseInstance.copy();
 Assert.assertArrayEquals(denseInstance.getAttributes(), copiedInstance.getAttributes(), 0.0);
 Assert.assertEquals(denseInstance.getClassValue(), copiedInstance.getClassValue());
 Assert.assertEquals(denseInstance.noAttributes(), copiedInstance.noAttributes());
 }

 @Test
 public void testCopyMethodWithClassValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance copiedInstance = denseInstance.copy();
 Assert.assertArrayEquals(denseInstance.getAttributes(), copiedInstance.getAttributes(), 0.0);
 Assert.assertEquals(denseInstance.getClassValue(), copiedInstance.getClassValue());
 Assert.assertEquals(denseInstance.getClassValue().toString(), copiedInstance.getClassValue().toString());
 }

 @Test
 public void testCopyMethodWithCustomClassValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance copiedInstance = denseInstance.copy();
 copiedInstance.setClassValue("customClassValue");
 Assert.assertNotEquals(denseInstance.getClassValue(), copiedInstance.getClassValue());
 }
}