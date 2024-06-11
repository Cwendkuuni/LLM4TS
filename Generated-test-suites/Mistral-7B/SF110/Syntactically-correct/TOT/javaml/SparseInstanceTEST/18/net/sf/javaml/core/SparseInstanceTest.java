import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import java.util.*;

public class SparseInstanceTest {

 @Test
 public void testEmptySparseInstance() {
 SparseInstance emptyInstance = new SparseInstance();
 Assert.assertEquals(0, emptyInstance.noAttributes());
 Assert.assertTrue(emptyInstance.isEmpty());
 }

 @Test
 public void testInitializationWithNoArguments() {
 SparseInstance instance = new SparseInstance();
 Assert.assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testInitializationWithOneArgument() {
 SparseInstance instance = new SparseInstance(1);
 Assert.assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testInitializationWithTwoArguments() {
 SparseInstance instance = new SparseInstance(1, 0.0);
 Assert.assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testInitializationWithThreeArguments() {
 SparseInstance instance = new SparseInstance(1, 0.0, "testClassValue");
 Assert.assertEquals("testClassValue", instance.getClassValue());
 }

 @Test
 public void testInitializationWithDoubleVector() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 Assert.assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testSettingNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.setNoAttributes(3);
 Assert.assertEquals(3, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNegativeNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.setNoAttributes(-3);
 }

 @Test
 public void testSetValue() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Assert.assertEquals(1.0, instance.get(0), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingKeyNull() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(null, Double.valueOf(1.0));
 }

 @Test
 public void testPutAll() {
 SparseInstance instance1 = new SparseInstance(5);
 instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance1.put(Integer.valueOf(2), Double.valueOf(2.0));

 SparseInstance instance2 = new SparseInstance(5);
 instance2.put(Integer.valueOf(1), Double.valueOf(1.5));
 instance2.put(Integer.valueOf(3), Double.valueOf(3.0));

 instance1.putAll(instance2.data);

 Assert.assertEquals(4, instance1.noAttributes());
 Assert.assertEquals(1.5, instance1.get(1), 0.0);
 Assert.assertEquals(3.0, instance1.get(3), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveKeyNull() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.remove(null);
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.removeAttribute(1);
 Assert.assertEquals(3, instance.noAttributes());
 Assert.assertEquals(1.0, instance.get(0), 0.0);
 Assert.assertEquals(3.0, instance.get(2), 0.0);
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 String expected = "{" +
 "1.0:0.0," +
 "2.0:1.0," +
 "3.0:2.0," +
 "null:0.0," +
 "null:0.0," +
 "null:0.0," +
 "null:0.0," +
 "null:0.0," +
 "null:0.0," +
 "testClassValue:{}" +
 "}";
 Assert.assertEquals(expected, instance.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClear() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.clear();
 Assert.assertTrue(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Assert.assertTrue(instance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Assert.assertTrue(instance.containsValue(Double.valueOf(1.0)));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 Assert.assertEquals(3, entrySet.size());
 }

 @Test
 public void testValue() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Assert.assertEquals(1.0, instance.value(0), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueOutOfBounds() {
 SparseInstance instance = new SparseInstance(5);
 Assert.assertEquals(0.0, instance.value(-1), 0.0);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Assert.assertEquals(3, instance.noAttributes());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoAttributesAfterRemovingAllAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.removeAttribute(0);
 instance.removeAttribute(1);
 instance.removeAttribute(2);
 }

 @Test
 public void testCopy() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));

 SparseInstance copiedInstance = instance.copy();
 Assert.assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 Assert.assertEquals(instance.getClassValue(), copiedInstance.getClassValue());

 for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
 Assert.assertEquals(entry.getKey(), copiedInstance.get(entry.getKey()));
 Assert.assertEquals(entry.getValue(), copiedInstance.get(entry.getKey()), 0.0);
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Set<Integer> indices = new HashSet<Integer>();
 indices.add(1);
 instance.removeAttributes(indices);
 Assert.assertEquals(2, instance.noAttributes());
 Assert.assertEquals(1.0, instance.get(0), 0.0);
 Assert.assertEquals(3.0, instance.get(2), 0.0);
 }
}