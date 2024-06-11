import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.SparseInstance;
import java.util.*;

public class SparseInstanceTest {

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance instance = new SparseInstance(0);
 assertEquals(0, instance.noAttributes());
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 SparseInstance instance = new SparseInstance(0, "ClassValue");
 assertEquals("ClassValue", instance.getClassValue());
 assertEquals(0, instance.noAttributes());
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
 SparseInstance instance = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals("ClassValue", instance.getClassValue());
 assertEquals(0, instance.noAttributes());
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 assertEquals(3, instance.keySet().size());
 assertEquals(1.0, instance.get(0), 0.01);
 assertEquals(2.0, instance.get(1), 0.01);
 assertEquals(3.0, instance.get(2), 0.01);
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, 0.0);
 assertEquals(3, instance.noAttributes());
 assertEquals(3, instance.keySet().size());
 assertEquals(1.0, instance.get(0), 0.01);
 assertEquals(2.0, instance.get(1), 0.01);
 assertEquals(3.0, instance.get(2), 0.01);
 }

 @Test
 public void testConstructorWithDatavectorAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, "ClassValue");
 assertEquals("ClassValue", instance.getClassValue());
 assertEquals(3, instance.noAttributes());
 assertEquals(3, instance.keySet().size());
 assertEquals(1.0, instance.get(0), 0.01);
 assertEquals(2.0, instance.get(1), 0.01);
 assertEquals(3.0, instance.get(2), 0.01);
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals("ClassValue", instance.getClassValue());
 assertEquals(3, instance.noAttributes());
 assertEquals(3, instance.keySet().size());
 assertEquals(1.0, instance.get(0), 0.01);
 assertEquals(2.0, instance.get(1), 0.01);
 assertEquals(3.0, instance.get(2), 0.01);
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testValueMethod() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertEquals(1.0, instance.value(0), 0.01);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 instance.clear();
 assertEmpty(instance.keySet());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertTrue(instance.containsKey(0));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertTrue(instance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 Map.Entry<Integer, Double> entry = instance.entrySet().iterator().next();
 assertEquals(0, entry.getKey().intValue());
 assertEquals(1.0, entry.getValue(), 0.01);
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertEquals(1.0, instance.get(0), 0.01);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 instance.put(0, 1.0);
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 Set<Integer> keys = instance.keySet();
 assertEquals(1, keys.size());
 assertEquals(0, keys.iterator().next().intValue());
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertEquals(1.0, instance.get(0), 0.01);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 1.0);
 map.put(1, 2.0);
 instance.putAll(map);
 assertEquals(2, instance.noAttributes());
 assertEquals(2, instance.keySet().size());
 assertEquals(1.0, instance.get(0), 0.01);
 assertEquals(2.0, instance.get(1), 0.01);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 instance.put(1, 2.0);
 assertEquals(2, instance.noAttributes());
 assertEquals(2, instance.keySet().size());
 instance.remove(0);
 assertEquals(1, instance.noAttributes());
 assertEquals(1, instance.keySet().size());
 assertEquals(2.0, instance.get(0), 0.01);
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.size());
 instance.put(0, 1.0);
 assertEquals(1, instance.size());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 instance.put(1, 2.0);
 List<Double> values = new ArrayList<>(instance.values());
 assertEquals(2, values.size());
 assertEquals(1.0, values.get(0), 0.01);
 assertEquals(2.0, values.get(1), 0.01);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 instance.put(0, 1.0);
 assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 instance.put(1, 2.0);
 assertEquals(2, instance.noAttributes());
 instance.removeAttribute(0);
 assertEquals(1, instance.noAttributes());
 assertEquals(1, instance.keySet().size());
 assertEquals(2.0, instance.get(0), 0.01);
 }
}