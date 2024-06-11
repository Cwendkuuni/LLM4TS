import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private NodePointer parentPointer;
 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstruction() {
 parentPointer = NodePointer.newRootNodePointer();
 propertyPointer = new PropertyPointer(parentPointer);
 assertNull(propertyPointer.getNode());
 assertEquals(parentPointer, propertyPointer.getImmediateParentPointer());
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 assertEquals(-1, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 propertyPointer = new PropertyPointer(parentPointer);
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 assertEquals("propertyName", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "oldPropertyName");
 propertyPointer.setPropertyName("newPropertyName");
 assertEquals("newPropertyName", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer.setPropertyCount(5);
 assertEquals(5, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = new String[]{"prop1", "prop2", "prop3"};
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer.setPropertyNames(propertyNames);
 assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 // Subclass must implement this method
 fail("Method 'isActualProperty()' not implemented.");
 }

 @Test
 public void testIsActual() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer.setValue(new Integer(42));
 assertEquals(new Integer(42), propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer.setValue(new String[]{"str1", "str2"});
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer.setValue(new Integer(42));
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer.setValue(new String[]{"str1", "str2"});
 assertEquals(2, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer.setValue(new Integer(42));
 NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
 assertNotNull(valuePointer);
 assertEquals(propertyPointer, valuePointer.getParentPointer());
 }

 @Test
 public void testCreatePath() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 Object bean = new Object();
 propertyPointer.setBean(bean);
JXPathContext context = new JXPathContext(bean);
 nodePointer = propertyPointer.createPath(context);
 assertEquals(propertyPointer, nodePointer);
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathWithInvalidFactory() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 JXPathContext context = new JXPathContext(new Object());
 AbstractFactory factory = new FailingFactory();
 nodePointer = propertyPointer.createPath(context, factory);
 fail("Exception not thrown.");
 }

 @Test
 public void testCreatePathWithNullValue() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 JXPathContext context = new JXPathContext(new Object());
 nodePointer = propertyPointer.createPath(context, null);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChild() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 JXPathContext context = new JXPathContext(new Object());
 NodePointer childPointer = propertyPointer.createChild(context, null, 0, "childPropertyName");
 assertEquals(propertyPointer, childPointer.getParentPointer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithNegativeIndex() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 JXPathContext context = new JXPathContext(new Object());
 propertyPointer.createChild(context, null, -1, "childPropertyName");
 fail("Exception not thrown.");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCreateChildWithIndexOutOfBounds() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 JXPathContext context = new JXPathContext(new Object());
 String[] propertyNames = new String[]{"prop1", "prop2"};
 propertyPointer.setPropertyNames(propertyNames);
 propertyPointer.createChild(context, null, 2, "childPropertyName");
 fail("Exception not thrown.");
 }

 @Test
 public void testHashCode() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer.setIndex(1);
 NodePointer otherPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 otherPointer.setIndex(2);
 assertEquals(propertyPointer.hashCode(), otherPointer.hashCode());
 otherPointer.setIndex(1);
 assertEquals(propertyPointer.hashCode(), otherPointer.hashCode());
 otherPointer = new AbstractPropertyPointer(parentPointer, 0, "otherPropertyName");
 assertEquals(propertyPointer.hashCode(), otherPointer.hashCode());
 otherPointer = new AbstractPropertyPointer(parentPointer2, 0, "propertyName");
 assertNotEquals(propertyPointer.hashCode(), otherPointer.hashCode());
 }

 @Test
 public void testEquals() {
 propertyPointer = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer2 = new AbstractPropertyPointer(parentPointer, 0, "propertyName");
 propertyPointer3 = new AbstractPropertyPointer(parentPointer, 0, "otherPropertyName");
 propertyPointer4 = new AbstractPropertyPointer(parentPointer2, 0, "propertyName");
 assertEquals(propertyPointer, propertyPointer);
 assertEquals(propertyPointer, propertyPointer2);
 assertNotEquals(propertyPointer, propertyPointer3);
 assertNotEquals(propertyPointer, propertyPointer4);
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer pointer1 = NodePointer.newChildNodePointer(propertyPointer, 0, "child1");
 NodePointer pointer2 = NodePointer.newChildNodePointer(propertyPointer, 0, "child2");
 int result = propertyPointer.compareChildNodePointers(pointer1, pointer2);
 assertEquals(0, result);
 propertyPointer = new AbstractPropertyPointer(parentPointer, 1, "propertyName");
 result = propertyPointer.compareChildNodePointers(pointer1, pointer2);
 assertTrue(result < 0);
 result = propertyPointer.compareChildNodePointers(pointer2, pointer1);
 assertTrue(result > 0);
 }
}

class FailingFactory implements AbstractFactory {
 @Override
 public Object createObject(JXPathContext context, NodePointer nodePointer, Object bean, String name, int index) {
 throw new RuntimeException();
 }
}