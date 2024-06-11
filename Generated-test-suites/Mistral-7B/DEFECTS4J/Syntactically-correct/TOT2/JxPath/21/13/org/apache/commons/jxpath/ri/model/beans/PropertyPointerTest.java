import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private Object parentBean;

 @Before
 public void setup() {
 parentBean = new Object() {};
 propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean));
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setPropertyName("testProperty");
 }

 @Test
 public void testGetPropertyIndex() {
 assertEquals(0, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 assertEquals(parentBean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 QName name = propertyPointer.getName();
 assertEquals("test:parent/testProperty", name.toString());
 }

 @Test
 public void testGetPropertyName() {
 assertEquals("testProperty", propertyPointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 propertyPointer.setPropertyName("newPropertyName");
 assertEquals("newPropertyName", propertyPointer.getPropertyName());
 }

 @Test
 public void testgetPropertyCount() {
 try {
 propertyPointer.getPropertyCount();
 fail("PropertyPointer abstract class doesn't implement getPropertyCount() method.");
 } catch (UnsupportedOperationException e) {}
 }

 @Test
 public void testGetPropertyNames() {
 try {
 String[] propertyNames = propertyPointer.getPropertyNames();
 fail("PropertyPointer abstract class doesn't implement getPropertyNames() method.");
 } catch (UnsupportedOperationException e) {}
 }

 @Test
 public void testIsActualProperty() {
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 fail("testGetImmediateNode should be implemented in a subclass.");
 }

 @Test
 public void testIsCollection() {
 fail("testIsCollection should be implemented in a subclass.");
 }

 @Test
 public void testIsLeaf() {
 fail("testIsLeaf should be implemented in a subclass.");
 }

 @Test
 public void testGetLength() {
 fail("testGetLength should be implemented in a subclass.");
 }

 @Test
 public void testGetImmediateValuePointer() {
 fail("testGetImmediateValuePointer should be implemented in a subclass.");
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(parentBean);
 propertyPointer.createPath(context);
 NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
 assertNotNull(nodePointer);
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathWithInvalidFactory() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 protected boolean isActualProperty() {
 return false;
 }
 };
 propertyPointer.setPropertyIndex(0);
 propertyPointer.setPropertyName("testProperty");
 AbstractFactory factory = new AbstractFactory() {};
 propertyPointer.createPath(context, factory);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreatePathWithIndexOutOfBoundsException() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getLength() {
 return 2;
 }
 };
 propertyPointer.setPropertyIndex(3);
 propertyPointer.createPath(context);
 }

 @Test(expected = NullPointerException.class)
 public void testCreatePathWithNullBean() {
 JXPathContext context = new JXPathContext(null);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), null)) {
 @Override
 public Object getBean() {
 return null;
 }
 };
 propertyPointer.setPropertyIndex(0);
 propertyPointer.createPath(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNullPropertyName() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, null, parentBean)) {
 @Override
 public String getPropertyName() {
 return null;
 }
 };
 propertyPointer.setPropertyIndex(0);
 propertyPointer.createPath(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithEmptyPropertyName() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, "", parentBean)) {
 @Override
 public String getPropertyName() {
 return "";
 }
 };
 propertyPointer.setPropertyIndex(0);
 propertyPointer.createPath(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNullValue() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public Object getBaseValue() {
 return null;
 }
 };
 propertyPointer.setPropertyIndex(0);
 propertyPointer.createPath(context, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithInvalidValueType() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public Object getBaseValue() {
 return new Object() {};
 }
 };
 propertyPointer.setPropertyIndex(0);
 propertyPointer.createPath(context, new Object() {});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithNegativeIndex() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getLength() {
 return 2;
 }
 };
 propertyPointer.setPropertyIndex(-1);
 propertyPointer.createPath(context);
 }

 @Test
 public void testCreatePathWithValidIndex() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getLength() {
 return 3;
 }
 };
 propertyPointer.setPropertyIndex(1);
 propertyPointer.createPath(context);
 }

 @Test
 public void testCreatePathWithValidValue() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public Object getBaseValue() {
 return new Object[3];
 }
 };
 propertyPointer.setPropertyIndex(1);
 propertyPointer.createPath(context, new Object[2]);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(parentBean);
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, null);
 assertNotNull(childNodePointer);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildWithNullName() {
 JXPathContext context = new JXPathContext(parentBean);
 propertyPointer.createChild(context, null, 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithEmptyName() {
 JXPathContext context = new JXPathContext(parentBean);
 propertyPointer.createChild(context, "", 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithNullValue() {
 JXPathContext context = new JXPathContext(parentBean);
 propertyPointer.createChild(context, new QName("test", "name"), 0, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithInvalidValueType() {
 JXPathContext context = new JXPathContext(parentBean);
 propertyPointer.createChild(context, new QName("test", "name"), 0, new Object() {});
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCreateChildWithIndexOutOfBoundsException() {
 JXPathContext context = new JXPathContext(parentBean);
 PropertyPointer propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getLength() {
 return 2;
 }
 };
 propertyPointer.createChild(context, null, 3, null);
 }

 @Test
 public void testCreateChildWithValidIndexAndValue() {
 JXPathContext context = new JXPathContext(parentBean);
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("test", "name"), 0, new Object[2]);
 assertNotNull(childNodePointer);
 }

 @Test
 public void testCreateChildWithValidIndex() {
 JXPathContext context = new JXPathContext(parentBean);
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, null);
 assertNotNull(childNodePointer);
 }

 @Test
 public void testCreateChildWithValidValue() {
 JXPathContext context = new JXPathContext(parentBean);
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("test", "name"), 0, new Object[2]);
 assertNotNull(childNodePointer);
 }

 @Test
 public void testHashCode() {
 PropertyPointer propertyPointer1 = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getPropertyIndex() {
 return 2;
 }
 };
 int hashCode1 = propertyPointer1.hashCode();
 int hashCode2 = propertyPointer2.hashCode();
 int hashCode3 = propertyPointer3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testEquals() {
 PropertyPointer propertyPointer1 = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 PropertyPointer propertyPointer3 = new PropertyPointer(NodePointer.newChildNodePointer(
 null, new QName("test", "parent"), parentBean)) {
 @Override
 public int getPropertyIndex() {
 return 2;
 }
 };
 assertTrue(propertyPointer1.equals(propertyPointer2));
 assertFalse(propertyPointer1.equals(propertyPointer3));
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer nodePointer1 = NodePointer.newChildNodePointer(null, null, null);
 NodePointer nodePointer2 = NodePointer.newChildNodePointer(null, null, null);
 NodePointer nodePointer3 = NodePointer.newChildNodePointer(null, null, null);
 NodePointer nodePointer4 = NodePointer.newChildNodePointer(null, null, null);
 assertEquals(0, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2));
 assertEquals(1, propertyPointer.compareChildNodePointers(nodePointer1, nodePointer3));
 assertEquals(-1, propertyPointer.compareChildNodePointers(nodePointer2, nodePointer1));
 assertEquals(0, propertyPointer.compareChildNodePointers(nodePointer3, nodePointer3));
 assertEquals(1, propertyPointer.compareChildNodePointers(nodePointer4, nodePointer3));
 }
}