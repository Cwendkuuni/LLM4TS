import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerConstructor() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getPropertyName());
 assertEquals(-1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 propertyPointer.setPropertyIndex(2);
 assertEquals(2, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 Object bean = new Object();
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testSetBean() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 Object bean = new Object();
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetBeanAfterSetPropertyName() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("propertyName");
 Object bean = new Object();
 propertyPointer.setBean(bean);
 }

 @Test
 public void testgetName() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("propertyName");
 assertEquals("propertyName", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("propertyName");
 assertEquals("propertyName", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testGetPropertyNames() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testIsActualProperty() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testGetLength() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testGetImmediateNode() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testIsCollection() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testIsLeaf() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testGetImmediateValuePointer() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testCreatePath() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testCreatePathWithValue() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testCreateChild() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testCreateChildWithName() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testCreateChildWithIndex() {
 throw new UnsupportedOperationException();
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(null);
 PropertyPointer propertyPointer2 = new PropertyPointer(null);
 PropertyPointer propertyPointer3 = new PropertyPointer(null);
 propertyPointer1.setPropertyName("propertyName1");
 propertyPointer1.setPropertyIndex(1);
 propertyPointer1.setIndex(1);
 propertyPointer2.setPropertyName("propertyName2");
 propertyPointer2.setPropertyIndex(2);
 propertyPointer2.setIndex(2);
 propertyPointer3.setPropertyName("propertyName1");
 propertyPointer3.setPropertyIndex(1);
 propertyPointer3.setIndex(1);
 assertEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
 assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(null);
 PropertyPointer propertyPointer2 = new PropertyPointer(null);
 PropertyPointer propertyPointer3 = new PropertyPointer(null);
 PropertyPointer propertyPointer4 = new PropertyPointer(null);
 PropertyPointer propertyPointer5 = new PropertyPointer(null);
 propertyPointer1.setPropertyName("propertyName1");
 propertyPointer1.setPropertyIndex(1);
 propertyPointer1.setIndex(1);propertyPointer2.setPropertyName("propertyName1");
 propertyPointer2.setPropertyIndex(1);
 propertyPointer2.setIndex(1);
 propertyPointer3.setPropertyName("propertyName2");
 propertyPointer3.setPropertyIndex(2);
 propertyPointer3.setIndex(2);
 propertyPointer4.setPropertyName("propertyName1");
 propertyPointer4.setPropertyIndex(1);
 propertyPointer4.setIndex(1);
 propertyPointer5.setPropertyName("propertyName1");
 propertyPointer5.setPropertyIndex(1);
 propertyPointer5.setIndex(2);
 assertTrue(propertyPointer1.equals(propertyPointer1));
 assertTrue(propertyPointer1.equals(propertyPointer4));
 assertFalse(propertyPointer1.equals(propertyPointer2));
 assertFalse(propertyPointer1.equals(propertyPointer3));
 assertFalse(propertyPointer1.equals(propertyPointer5));
 }

 @Test
 public void testCompareChildNodePointers() {
 PropertyPointer propertyPointer1 = new PropertyPointer(null);
 PropertyPointer propertyPointer2 = new PropertyPointer(null);
 PropertyPointer propertyPointer3 = new PropertyPointer(null);
 PropertyPointer propertyPointer4 = new PropertyPointer(null);
 PropertyPointer propertyPointer5 = new PropertyPointer(null);
 propertyPointer1.setPropertyName("propertyName1");
 propertyPointer1.setPropertyIndex(1);
 propertyPointer1.setIndex(1);
 propertyPointer2.setPropertyName("propertyName1");
 propertyPointer2.setPropertyIndex(1);
 propertyPointer2.setIndex(2);
 propertyPointer3.setPropertyName("propertyName2");
 propertyPointer3.setPropertyIndex(2);
 propertyPointer3.setIndex(2);
 propertyPointer4.setPropertyName("propertyName1");
 propertyPointer4.setPropertyIndex(1);
 propertyPointer4.setIndex(1);
 propertyPointer5.setPropertyName("propertyName1");
 propertyPointer5.setPropertyIndex(1);
 propertyPointer5.setIndex(3);
 assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer4));
 assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer2));
 assertEquals(1, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1));
 assertEquals(0, propertyPointer1.compareChildNodePointers(propertyPointer4, propertyPointer4));
 assertEquals(1, propertyPointer4.compareChildNodePointers(propertyPointer1, propertyPointer4));
 assertEquals(-1, propertyPointer4.compareChildNodePointers(propertyPointer4, propertyPointer1));
 assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer1, propertyPointer5));
 assertEquals(-1, propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer5));
 assertEquals(-1, propertyPointer2.compareChildNodePointers(propertyPointer1, propertyPointer5));
 assertEquals(-1, propertyPointer3.compareChildNodePointers(propertyPointer1, propertyPointer5));
 assertEquals(-1, propertyPointer3.compareChildNodePointers(propertyPointer2, propertyPointer5));
 assertEquals(-1, propertyPointer4.compareChildNodePointers(propertyPointer1, propertyPointer5));
 assertEquals(-1, propertyPointer4.compareChildNodePointers(propertyPointer2, propertyPointer5));
 assertEquals(-1, propertyPointer5.compareChildNodePointers(propertyPointer1, propertyPointer1));
 assertEquals(-1, propertyPointer5.compareChildNodePointers(propertyPointer2, propertyPointer1));
 assertEquals(-1, propertyPointer5.compareChildNodePointers(propertyPointer4, propertyPointer1));
 }

}