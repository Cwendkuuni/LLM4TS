import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

 @Test
 public void testEmptySparseInstance() {
 SparseInstance emptySparseInstance = new SparseInstance();
 assertEquals(0, emptySparseInstance.noAttributes());
 assertFalse(emptySparseInstance.containsKey(0));
 assertTrue(emptySparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance(3, 0.0);
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(0));
 assertEquals(0.0, sparseInstance.get(0), 0.0);
 }

 @Test
 public void testSparseInstanceWithData() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(3, sparseInstance.noAttributes());
 assertTrue(sparseInstance.containsKey(0));
 assertTrue(sparseInstance.containsKey(1));
 assertTrue(sparseInstance.containsKey(2));
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 assertEquals(2.0, sparseInstance.get(1), 0.0);
 assertEquals(3.0, sparseInstance.get(2), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoAttributes() {
 new SparseInstance(-1);
 }

 @Test
 public void testInitializeWithDataVector() {
 double[] datavector = {0.1, 0.2, 0.3, 0.4};
 SparseInstance sparseInstance = new SparseInstance(datavector);
 assertEquals(4, sparseInstance.noAttributes());
 assertTrue(sparseInstance.containsKey(0));
 assertTrue(sparseInstance.containsKey(1));
 assertTrue(sparseInstance.containsKey(2));
 assertTrue(sparseInstance.containsKey(3));
 assertEquals(0.1, sparseInstance.get(0), 0.0);
 assertEquals(0.2, sparseInstance.get(1), 0.0);
 assertEquals(0.3, sparseInstance.get(2), 0.0);
 assertEquals(0.4, sparseInstance.get(3), 0.0);
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance(3, 0.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(0));
 assertFalse(sparseInstance.containsKey(1));
 assertFalse(sparseInstance.containsKey(2));
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance sparseInstance = new SparseInstance(3, 0.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance.removeAttribute(1);
 assertEquals(2, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(1));
 }

 @Test
 public void testToString() {
 SparseInstance sparseInstance = new SparseInstance(3, 0.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 String expected = "{" + "[0=1.0, 1=2.0, 2=3.0]" + ";" + "class java.lang.Object" + "}";
 assertEquals(expected, sparseInstance.toString());
 }

 @Test
 public void testClone() {
 SparseInstance sparseInstance = new SparseInstance(3, 0.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 SparseInstance clonedSparseInstance = (SparseInstance) sparseInstance.copy();
 assertEquals(sparseInstance, clonedSparseInstance);
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(5, 0.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance.put(Integer.valueOf(3), Double.valueOf(4.0));
 sparseInstance.put(Integer.valueOf(4), Double.valueOf(5.0));
 HashSet<Integer> indices = new HashSet<Integer>();
 indices.add(1);
 indices.add(4);
 sparseInstance.removeAttributes(indices);
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(1));
 assertFalse(sparseInstance.containsKey(4));
 }
}