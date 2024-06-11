import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class DenseInstanceTest {

 @Test
 public void testEmptyDenseInstance() {
 DenseInstance instance = new DenseInstance();
 assertEquals(0, instance.noAttributes());
 assertNull(instance.classValue());
 assertNotNull(instance);
 assertFalse(instance.isEmpty());
 assertFalse(instance.containsKey(0));
 assertFalse(instance.containsValue(0.0));
 assertFalse(instance.keySet().isEmpty());
 assertTrue(instance.entrySet().isEmpty());
 assertFalse(instance.values().isEmpty());
 }

 @Test
 public void testDenseInstanceWithAttributes() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertEquals(3, instance.noAttributes());
 assertNull(instance.classValue());
 assertNotNull(instance);
 assertFalse(instance.isEmpty());
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(2.0, instance.value(1), 0.0);
 assertEquals(3.0, instance.value(2), 0.0);
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 assertFalse(instance.containsValue(4.0));
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(3, entrySet.size());
 for (Map.Entry<Integer, Double> entry : entrySet) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 Set<Integer> keySet = instance.keySet();
 assertEquals(3, keySet.size());
 for (Integer key : keySet) {
 assertEquals(key, instance.getValue(key));
 }
 Collection<Double> values = instance.values();
 assertEquals(3, values.size());
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testDenseInstanceWithClassValue() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 }, "ClassValue");
 assertEquals(3, instance.noAttributes());
 assertEquals("ClassValue", instance.classValue());
 }

 @Test
 public void testDenseInstanceWithNegativeSize() {
 assertThrows(IllegalArgumentException.class, () -> new DenseInstance(-1));
 }

 @Test
 public void testClearDenseInstance() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertNull(instance.classValue());
 assertFalse(instance.isEmpty());
 assertFalse(instance.containsKey(0));
 assertFalse(instance.containsValue(0.0));
 assertFalse(instance.keySet().isEmpty());
 assertTrue(instance.entrySet().isEmpty());
 assertFalse(instance.values().isEmpty());
 }

 @Test
 public void testDenseInstanceWithNullAttributes() {
 assertThrows(NullPointerException.class, () -> new DenseInstance(null));
 }

 @Test
 public void testDenseInstanceWithDuplicateKey() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 2.0 });
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(2, entrySet.size());
 assertFalse(instance.containsValue(3.0));
 }

 @Test
 public void testDenseInstanceWithInvalidKey() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertThrows(IndexOutOfBoundsException.class, () -> instance.value(-1));
 }

 @Test
 public void testDenseInstanceWithInvalidKey2() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertThrows(IndexOutOfBoundsException.class, () -> instance.removeAttribute(-1));
 }

 @Test
 public void testDenseInstanceWithInvalidKey3() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 assertThrows(IndexOutOfBoundsException.class, () -> instance.removeAttribute(3));
 }

 @Test
 public void testDenseInstanceWithInvalidKey4() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 Set<Integer> indices = new HashSet<Integer>(Arrays.asList(0, 1));
 instance.removeAttributes(indices);
 assertEquals(1, instance.noAttributes());
 assertTrue(instance.containsKey(2));
 }

 @Test
 public void testDenseInstanceWithEmptySet() {
 DenseInstance instance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
 Set<Integer> indices = Collections.emptySet();
 instance.removeAttributes(indices);
 assertEquals(0, instance.noAttributes());
 }
}