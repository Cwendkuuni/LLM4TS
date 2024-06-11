import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerConstruction() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null);
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getNode());
 assertEquals(-1, propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getPropertyName());
 }

 @Test
 public void testPropertyPointerPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testPropertyPointerBean() {
 Object bean = new Object();
 PropertyPointer propertyPointer = new PropertyPointerImpl(null);
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testPropertyPointerName() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null);
 propertyPointer.setPropertyName("propertyName");
 assertEquals("propertyName", propertyPointer.getPropertyName());
 }

 @Test
 public void testPropertyPointerPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public int getPropertyCount() {
 return 5;
 }
 };
 assertEquals(5, propertyPointer.getPropertyCount());
 }

 @Test
 public void testPropertyPointerPropertyNames() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public String[] getPropertyNames() {
 return new String[]{"name1", "name2", "name3"};
 }
 };
 assertEquals(3, propertyPointer.getPropertyNames().length);
 assertEquals("name1", propertyPointer.getPropertyNames()[0]);
 assertEquals("name2", propertyPointer.getPropertyNames()[1]);
 assertEquals("name3", propertyPointer.getPropertyNames()[2]);
 }

 @Test
 public void testPropertyPointerIsActualProperty() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testPropertyPointerIsCollection() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public boolean isCollection() {
 return true;
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testPropertyPointerIsLeaf() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public boolean isLeaf() {
 return true;
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testPropertyPointerGetLength() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public int getLength() {
 return 10;
 }
 };
 assertEquals(10, propertyPointer.getLength());
 }

 @Test
 public void testPropertyPointerGetImmediateValuePointer() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public NodePointer getImmediateValuePointer() {
 return new NodePointerImpl();
 }
 };
 assertNotNull(propertyPointer.getImmediateValuePointer());
 }

 @Test
 public void testPropertyPointerCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public NodePointer createPath(JXPathContext context) {
 return new NodePointerImpl();
 }
 };
 NodePointer nodePointer = propertyPointer.createPath(null);
 assertNotNull(nodePointer);
 }

 @Test
 public void testPropertyPointerCreatePathWithValue() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public NodePointer createPath(JXPathContext context, Object value) {
 return new NodePointerImpl();
 }
 };
 NodePointer nodePointer = propertyPointer.createPath(null, new Object());
 assertNotNull(nodePointer);
 }

 @Test
 public void testPropertyPointerClone() {
 PropertyPointer propertyPointer = new PropertyPointerImpl(null);
 PropertyPointer clonedPropertyPointer = propertyPointer.clone();
 assertNotSame(propertyPointer, clonedPropertyPointer);
 assertEquals(propertyPointer.getPropertyName(), clonedPropertyPointer.getPropertyName());
 assertEquals(propertyPointer.getPropertyIndex(), clonedPropertyPointer.getPropertyIndex());
 assertEquals(propertyPointer.getBean(), clonedPropertyPointer.getBean());
 }

 @Test
 public void testPropertyPointerHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointerImpl(null) {
 @Override
 public int hashCode() {return 1;}
 };
 PropertyPointer propertyPointer2 = new PropertyPointerImpl(null) {
 @Override
 public int hashCode() {return 2;}
 };
 assertEquals(1, propertyPointer1.hashCode());
 assertEquals(2, propertyPointer2.hashCode());
 }

 @Test
 public void testPropertyPointerEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointerImpl(null) {
 @Override
 public boolean equals(Object object) {
 return true;
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointerImpl(null) {
 @Override
 public boolean equals(Object object) {
 return true;
 }
 };
 assertTrue(propertyPointer1.equals(propertyPointer2));
 PropertyPointer propertyPointer3 = new PropertyPointerImpl(null) {
 @Override
 public boolean equals(Object object) {
 return false;
 }
 };
 assertFalse(propertyPointer1.equals(propertyPointer3));
 }

 @Test
 public void testPropertyPointerCompareChildNodePointers() {
 NodePointer nodePointer1 = new NodePointerImpl();
 NodePointer nodePointer2 = new NodePointerImpl();
 PropertyPointer propertyPointer = new PropertyPointerImpl(null) {
 @Override
 public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
 return 1;
 }
 };
 assertEquals(1, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2));
 }
}