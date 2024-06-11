import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

 @Test
 public void testDenseInstanceWithValidAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testDenseInstanceWithEmptyAttributes() {
 DenseInstance denseInstance = new DenseInstance();
 assertEquals(0, denseInstance.noAttributes());
 assertNull(denseInstance.value(0));
 }

 @Test
 public void testDenseInstanceWithNegativeIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 denseInstance.value(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testDenseInstanceWithNullClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertNull(denseInstance.getClassValue());

 DenseInstance denseInstanceWithClassValue = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "classValue");
 assertEquals("classValue", denseInstanceWithClassValue.getClassValue());
 }

 @Test
 public void testDenseInstanceWithInvalidClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 denseInstance.setClassValue("notAValidClassValue");
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testDenseInstanceWithInvalidAttributeIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 denseInstance.put(-1, 4.0);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }

 try {
 denseInstance.put(3, "notAValidAttribute");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testDenseInstanceWithSet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(3, keySet.size());

 Map.Entry<Integer, Double> entry = denseInstance.entrySet().iterator().next();
 assertEquals(0, entry.getKey().intValue());
 assertEquals(1.0, entry.getValue().doubleValue(), 0.0);
 }

 @Test
 public void testDenseInstanceWithRemoveAttribute() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.removeAttribute(1);
 assertEquals(2, denseInstance.noAttributes());
 }

 @Test
 public void testDenseInstanceWithInvalidRemoveAttributeIndex() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 try {
 denseInstance.removeAttribute(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testDenseInstanceWithClear() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }
}