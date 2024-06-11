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
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.0);
 Assert.assertEquals(5, sparseInstance.noAttributes());
 Assert.assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithData() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals(5, sparseInstance.noAttributes());
 Assert.assertFalse(sparseInstance.isEmpty());
 Assert.assertEquals(1.0, sparseInstance.get(0), 0.001);
 Assert.assertEquals(2.0, sparseInstance.get(1), 0.001);
 Assert.assertEquals(3.0, sparseInstance.get(2), 0.001);
 Assert.assertEquals(4.0, sparseInstance.get(3), 0.001);
 Assert.assertEquals(5.0, sparseInstance.get(4), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(-1);
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.0);
 sparseInstance.setNoAttributes(3);
 Assert.assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testGet() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertEquals(1.0, sparseInstance.get(0), 0.001);
 Assert.assertEquals(2.0, sparseInstance.get(1), 0.001);
 Assert.assertEquals(3.0, sparseInstance.get(2), 0.001);
 Assert.assertEquals(4.0, sparseInstance.get(3), 0.001);
 Assert.assertEquals(5.0, sparseInstance.get(4), 0.001);
 Assert.assertEquals(0.0, sparseInstance.get(-1), 0.001);
 }

 @Test
 public void testPut() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.0);
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(6.0));
 Assert.assertEquals(6.0, sparseInstance.get(2), 0.001);
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.clear();
 Assert.assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertTrue(sparseInstance.containsKey(Integer.valueOf(1)));
 Assert.assertTrue(sparseInstance.containsKey(Integer.valueOf(2)));
 Assert.assertTrue(sparseInstance.containsKey(Integer.valueOf(3)));
 Assert.assertTrue(sparseInstance.containsKey(Integer.valueOf(4)));
 Assert.assertTrue(sparseInstance.containsKey(Integer.valueOf(5)));
 Assert.assertFalse(sparseInstance.containsKey(Integer.valueOf(6)));
 }

 @Test
 public void testContainsValue() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Assert.assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 Assert.assertTrue(sparseInstance.containsValue(Double.valueOf(2.0)));
 Assert.assertTrue(sparseInstance.containsValue(Double.valueOf(3.0)));
 Assert.assertTrue(sparseInstance.containsValue(Double.valueOf(4.0)));
 Assert.assertTrue(sparseInstance.containsValue(Double.valueOf(5.0)));
 Assert.assertFalse(sparseInstance.containsValue(Double.valueOf(6.0)));
 }

 @Test
 public void testEntrySet() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Map.Entry<Integer, Double>> set = sparseInstance.entrySet();
 Assert.assertEquals(5, set.size());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.removeAttribute(2);
 Assert.assertEquals(4, sparseInstance.noAttributes());
 }
}