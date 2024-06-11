import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import java.util.*;

public class SparseInstanceTest {

 @Test
 public void testEmptySparseInstance() {
 SparseInstance emptySparseInstance = new SparseInstance();
 Assert.assertEquals(0, emptySparseInstance.noAttributes());
 Assert.assertTrue(emptySparseInstance.isEmpty());
 }

 @Test
 public void testDefaultValueSparseInstance() {
 SparseInstance defaultValueSparseInstance = new SparseInstance(5);
 Assert.assertEquals(5, defaultValueSparseInstance.noAttributes());
 for (int i = 0; i < 5; ++i) {
 Assert.assertEquals(defaultValueSparseInstance.getDefaultValue(), defaultValueSparseInstance.value(i));
 }
 }

 @Test
 public void testSparseInstanceWithData() {
 SparseInstance sparseInstance = new SparseInstance(5, new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 Assert.assertEquals(5, sparseInstance.noAttributes());
 for (int i = 0; i < 5; ++i) {
 Assert.assertEquals(sparseInstance.get(i), i + 1.1, 0.001);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(-1);
 }

 @Test
 public void testGetMethodWhenKeyIsNotPresent() {
 SparseInstance sparseInstance = new SparseInstance(5, new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 Assert.assertEquals(5.5, sparseInstance.get(-1), 0.001);
 }

 @Test
 public void testSetMethod() {
 SparseInstance sparseInstance = new SparseInstance(5, new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 sparseInstance.put(Integer.valueOf(3), Double.valueOf(6.6));
 Assert.assertEquals(6.6, sparseInstance.get(3), 0.001);
 }

 @Test
 public void testPutAllMethod() {
 SparseInstance instance1 = new SparseInstance(5, new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 SparseInstance instance2 = new SparseInstance(5, new double[]{6.6, 7.7, 8.8, 9.9, 10.10});
 instance1.putAll(instance2.entrySet());
 for (int i = 0; i < 10; ++i) {
 Assert.assertEquals(instance2.get(i), instance1.get(i), 0.001);
 }
 }

 @Test
 public void testRemoveAttributeMethod() {
 SparseInstance sparseInstance = new SparseInstance(5, new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 sparseInstance.removeAttribute(2);
 Assert.assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAllAttributesMethod() {
 SparseInstance sparseInstance = new SparseInstance(5, new double[]{1.1, 2.2, 3.3, 4.4, 5.5});
 Set<Integer> indices = new TreeSet<Integer>();
 for (int i = 0; i < 5; ++i) {
 indices.add(i);
 }
 sparseInstance.removeAttributes(indices);
 Assert.assertEquals(0, sparseInstance.noAttributes());
 }
}