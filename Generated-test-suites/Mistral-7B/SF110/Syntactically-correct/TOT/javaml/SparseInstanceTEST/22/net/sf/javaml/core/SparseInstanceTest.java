import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.core.AbstractInstance;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

public class SparseInstanceTest {

 @Test
 public void testEmptyConstructor() {
 SparseInstance emptyInstance = new SparseInstance();
 assertEquals(0, emptyInstance.noAttributes());
 assertFalse(emptyInstance.containsKey(0));
 assertTrue(emptyInstance.isEmpty());
 }

 @Test
 public void testDefaultValueConstructor() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertEquals(0.0, instance.get(0), 0.0);
 }

 @Test
 public void testClassValueConstructor() {
 SparseInstance instance = new SparseInstance(5, "TestClass");
 assertEquals(5, instance.noAttributes());
 assertEquals("TestClass", instance.classValue());
 assertFalse(instance.containsKey(0));
 assertEquals(0.0, instance.get(0), 0.0);
 }

 @Test
 public void testDataVectorConstructor() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, 0.0, "TestClass");
 assertEquals(5, instance.noAttributes());
 assertEquals("TestClass", instance.classValue());
 for (int i = 0; i < datavector.length; ++i) {
 assertEquals(datavector[i], instance.get(i), 0.0);
 }
 for (int i = 5; i < datavector.length; ++i) {
 assertNull(instance.get(i));
 assertEquals(0.0, instance.get(i), 0.0);
 }
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.setNoAttributes(10);
 assertEquals(10, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNoAttributes() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.setNoAttributes(-1);
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(0.0, instance.get(2), 0.0);
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(3.0, instance.get(2), 0.0);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertFalse(instance.containsKey(1));
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Integer> keys = instance.keySet();
 TreeSet<Integer> expected = new TreeSet<Integer>();
 expected.add(0);
 expected.add(1);
 assertTrue(keys.containsAll(expected));
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertFalse(instance.containsKey(2));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertTrue(instance.containsValue(1.0));
 assertFalse(instance.containsValue(3.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 List<Map.Entry<Integer, Double>> expected = Arrays.asList(
 new Map.Entry<Integer, Double>() {
 @Override
 public Integer getKey() {
 return 0;
 }

 @Override
 public Double getValue() {
 return 1.0;
 }
 },
 new Map.Entry<Integer, Double>() {
 @Override
 public Integer getKey() {
 return 1;
 }

 @Override
 public Double getValue() {
 return 2.0;
 }
 }
 );
 assertTrue(entries.containsAll(expected));
 }

 @Test
 public void testInitiate() {
 double[] datavector = {1.0, 2.0, 0.0, 3.0, 4.0};
 SparseInstance instance = new SparseInstance();
 instance.initiate(datavector);
 assertEquals(5, instance.noAttributes());
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(0.0, instance.get(2), 0.0);
 assertEquals(3.0, instance.get(3), 0.0);
 assertEquals(4.0, instance.get(4), 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testInitiateNullDatavector() {
 SparseInstance instance = new SparseInstance();
 instance.initiate(null);
 }

 @Test
 public void testValue() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(1.0, instance.value(0), 0.0);
 assertNull(instance.value(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueNegativeIndex() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(0.0, instance.value(-1), 0.0);
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.get(0), 0.0);
 assertNull(instance.get(1));
 assertEquals(3.0, instance.get(2), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeNegativeIndex() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.removeAttribute(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeOutOfBoundsIndex() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.removeAttribute(3);
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 String expected = "{1.0=1.0, 2.0=2.0, 3.0=3.0;TestClass}";
 assertEquals(expected, instance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = new SparseInstance();
 instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance instance2 = new SparseInstance();
 instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance2.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance2.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance instance1 = new SparseInstance();
 instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance instance2 = new SparseInstance();
 instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance2.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance2.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertFalse(instance1.equals(null));
 assertFalse(instance1.equals("TestString"));
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testCopy() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance copy = instance.copy();
 assertEquals(instance.noAttributes(), copy.noAttributes());
 assertEquals(instance.getClassValue(), copy.getClassValue());
 for (Object key : instance.keySet()) {
 Object copyKey = copy.get(key);
 assertEquals(instance.get(key), copyKey);
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Set<Integer> indicesToRemove = new HashSet<Integer>();
 indicesToRemove.add(1);
 indicesToRemove.add(2);
 instance.removeAttributes(indicesToRemove);
 assertEquals(1, instance.noAttributes());
 assertNull(instance.get(1));
 assertNull(instance.get(2));
 }

 @Test
 public void testRemoveAttributesEmptySet() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Set<Integer> indicesToRemove = new HashSet<Integer>();
 instance.removeAttributes(indicesToRemove);
 assertEquals(3, instance.noAttributes());
 }
}