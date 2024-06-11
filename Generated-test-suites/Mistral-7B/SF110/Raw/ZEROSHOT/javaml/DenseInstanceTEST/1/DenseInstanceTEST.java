import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import org.junit.Test;

public class DenseInstanceTest {

 @Test
 public void testDenseInstanceWithEmptyAttributeArray() {
 DenseInstance denseInstance = new DenseInstance();
 assertTrue(denseInstance.isEmpty());
 assertEquals(0, denseInstance.noAttributes());
 assertEquals(0, denseInstance.size());
 assertNull(denseInstance.getClassValue());
 assertEmpty(denseInstance.keySet());
 assertFalse(denseInstance.containsKey(1));
 assertFalse(denseInstance.containsValue(1.0));
 assertFalse(denseInstance.containsValue(new Double(1.0)));
 assertFalse(denseInstance.isInstanceOf(DenseInstance.class));
 assertEquals("{}", denseInstance.toString());
 }

 @Test
 public void testDenseInstanceWithSingleAttribute() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0});
 assertEquals(1, denseInstance.noAttributes());
 assertEquals(1, denseInstance.size());
 assertNull(denseInstance.getClassValue());
 assertFalse(denseInstance.isEmpty());
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsValue(1.0));
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(1.0, denseInstance.get(0));
 assertEquals(Arrays.asList(1.0), denseInstance.values());
 assertEquals(1, denseInstance.keySet().size());
 assertEquals(1, denseInstance.hashCode());
 assertTrue(denseInstance.equals(denseInstance));
 DenseInstance copy = denseInstance.copy();
 assertTrue(denseInstance.equals(copy));
 }

 @Test
 public void testDenseInstanceWithMultipleAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
 assertEquals(3, denseInstance.size());
 assertNull(denseInstance.getClassValue());
 assertFalse(denseInstance.isEmpty());
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsKey(2));
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(2.0, denseInstance.get(1));
 assertEquals(Arrays.asList(1.0, 2.0, 3.0), denseInstance.values());
 assertEquals(3, denseInstance.keySet().size());
 assertEquals(Arrays.asList(0, 1, 2), denseInstance.keySet());
 assertEquals(3, denseInstance.hashCode());
 assertTrue(denseInstance.equals(denseInstance));
 DenseInstance copy = denseInstance.copy();
 assertTrue(denseInstance.equals(copy));
 }

 @Test
 public void testSettingClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.setClassValue("ClassValue");
 assertEquals("ClassValue", denseInstance.getClassValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemovingValue() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.remove(1);
 }

 @Test
 public void testSettingValue() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.put(1, 4.0);
 assertEquals(4.0, denseInstance.value(1), 0.0);
 assertEquals(4.0, denseInstance.get(1));
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 denseInstance1.putAll(denseInstance2.entrySet());
 assertEquals(6, denseInstance1.size());
 assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0), denseInstance1.values());
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 System.out.println(entry.getKey() + " : " + entry.getValue());
 }
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 denseInstance.clear();
 assertEquals(0, denseInstance.size());
 assertFalse(denseInstance.containsKey(1));
 assertFalse(denseInstance.containsValue(1.0));
 assertFalse(denseInstance.isInstanceOf(DenseInstance.class));
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(3, denseInstance.size());
 assertFalse(denseInstance.containsKey(1));
 assertFalse(denseInstance.containsValue(2.0));
 assertFalse(denseInstance.containsKey(3));
 assertFalse(denseInstance.containsValue(3.0));
 }

 @Test
 public void testRemoveAttributeByIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 denseInstance.removeAttribute(2);
 assertEquals(4, denseInstance.size());
 assertFalse(denseInstance.containsKey(2));
 assertFalse(denseInstance.containsValue(3.0));
 }
}