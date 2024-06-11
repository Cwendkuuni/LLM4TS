import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerInstance() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getBean());
 assertFalse(propertyPointer.isActual());
 assertEquals(-1, propertyPointer.getLength());
 }

 @Test
 public void testPropertyPointerInstanceWithNameAndParent() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 NodePointer parent = new NodePointer(null);
 propertyPointer.setParent(parent);
 assertEquals("testProperty", propertyPointer.getPropertyName());
 assertSame(parent, propertyPointer.getImmediateParentPointer());
 assertFalse(propertyPointer.isActual());
 assertEquals(-1, propertyPointer.getLength());
 }

 @Test
 public void testSetAndGetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(5);
 assertEquals(5, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
 assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 Object bean = new Object();
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 assertEquals(new QName(null, "testProperty"), propertyPointer.getName());
 }

 @Test
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public abstract String getPropertyName();

 @Override
 public abstract int getPropertyCount();

 @Override
 public abstract String[] getPropertyNames();

 @Override
 protected abstract boolean isActualProperty();

 @Override
 public int getPropertyIndex() {
 return 0;
 }

 @Override
 public void setPropertyIndex(int index) {
 // empty implementation
 }

 @Override
 public Object getBean() {
 return new Object();
 }

 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 2;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty1", "testProperty2"};
 }

 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertEquals(2, propertyPointer.getPropertyCount());
 }

 @Test
 public void testIsActual() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 assertEquals(1, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return "test";
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 NodePointer nodePointer = propertyPointer.getImmediateValuePointer();
 assertFalse(nodePointer.isActual());
 assertNull(nodePointer.getName());
 assertEquals(1, nodePointer.getPropertyIndex());
 assertEquals(propertyPointer.getBean(), nodePointer.getBean());
 }

 @Test
 public void testCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object(){
 public String toString() {
 return "testValue";
 }
 };
 };
 };
 NodePointer nodePointer = propertyPointer.createPath(null);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 NodePointer nodePointer = propertyPointer.createPath(null, "testValue");
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithValueAndIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }
 };
 NodePointer nodePointer = propertyPointer.createPath(null, "testValue", 1);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithValueAndIndexWithParent() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }

 @Override
 public NodePointer createPath(JXPathContext context) {
 return null;
 }
 };
 NodePointer parentPointer = new NodePointer(null);
 NodePointer nodePointer = propertyPointer.createPath(null, "testValue", 1, parentPointer);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithValueAndIndexWithParentAndValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }

 @Override
 public NodePointer createPath(JXPathContext context, Object value) {
 return null;
 }
 };
 NodePointer parentPointer = new NodePointer(null);
 NodePointer nodePointer = propertyPointer.createPath(null, "testValue", 1, parentPointer, new Object());
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithNameAndIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }

 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return null;
 }
 };
 NodePointer childPointer = propertyPointer.createChild(null, "testChild", 1);
 assertNull(childPointer);
 }

 @Test
 public void testCreateChildWithName() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }

 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index) {
 return null;
 }
 };
 NodePointer childPointer = propertyPointer.createChild(null, "testChild");
 assertNull(childPointer);
 }

 @Test
 public void testCreateChildWithNameAndValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }

 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return null;
 }
 };
 NodePointer childPointer = propertyPointer.createChild(null, "testChild", new Object());
 assertNull(childPointer);
 }

 @Test
 public void testCreateChildWithNameAndValueAndParent() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public AbstractFactory getAbstractFactory(JXPathContext context) {
 return null;
 }

 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 }

 @Override
 public NodePointer createChild(
 JXPathContext context,
 QName name,
 int index,
 Object value) {
 return null;
 }
 };
 NodePointer parentPointer = new NodePointer(null);
 NodePointer childPointer = propertyPointer.createChild(null, "testChild", new Object(), parentPointer);
 assertNull(childPointer);
 }
}