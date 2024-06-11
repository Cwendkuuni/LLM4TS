import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private JXPathContext context;

 @Test
 public void testPropertyPointerConstruction() {
 propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getValue());
 assertNull(propertyPointer.getName());
 }

 @Test
 public void testPropertyPointerWithParent() {
 NodePointer parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 assertNotNull(propertyPointer.getImmediateParentPointer());
 assertSame(propertyPointer.getImmediateParentPointer(), parentPointer);
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getValue());
 assertNull(propertyPointer.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPropertyPointerWithNullParent() {
 propertyPointer = new PropertyPointer(null);
 }

 @Test
 public void testGetPropertyIndex() {
 int propertyIndex = 1;
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(propertyIndex);
 assertEquals(propertyIndex, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 int propertyIndex1 = 1;
 int propertyIndex2 = 2;
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(propertyIndex1);
 propertyPointer.setPropertyIndex(propertyIndex2);
 assertEquals(propertyIndex2, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testSetBean() {
 Object bean1 = new Object();
 Object bean2 = new Object();
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(bean1);
 propertyPointer.setBean(bean2);
 assertEquals(bean2, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 String propertyName = "propertyName";
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName(propertyName);
 assertEquals(propertyName, propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetName() {
 String propertyName1 = "propertyName1";
 String propertyName2 = "propertyName2";
 propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName(propertyName1);
 propertyPointer.setPropertyName(propertyName2);
 assertEquals(propertyName2, propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyCount() {
 int propertyCount = 5;
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyCount(propertyCount);
 assertEquals(propertyCount, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = new String[]{"propertyName1", "propertyName2"};
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyNames(propertyNames);
 assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
 boolean isActualProperty = true;
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 protected boolean isActualProperty() {
 return isActualProperty;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testIsActualPropertyFalse() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 assertFalse(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 Object immediateNode = new Object();
 propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getImmediateNode() {
 return immediateNode;
 }
 };
 assertEquals(immediateNode, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 boolean isCollection = true;
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public boolean isCollection() {
 return isCollection;
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 boolean isLeaf = true;
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public boolean isLeaf() {
 return isLeaf;
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 int length = 5;
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public int getLength() {
 return length;
 }
 };
 assertEquals(length, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 Object immediateNode = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getImmediateNode() {
 return immediateNode;
 }
 };
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(propertyPointer, immediateValuePointer.getParentPointer());
 assertEquals(immediateNode, immediateValuePointer.getNode());
 }

 @Test
 public void testCreatePath() {
 context = JXPathContext.newContext(new Object());
 PropertyPointer propertyPointer = new PropertyPointer(null);
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertNotNull(nodePointer);
 assertEquals(propertyPointer, nodePointer.getParentPointer());
 }

 @Test
 public void testCreatePathWithValue() {
 context = JXPathContext.newContext(new Object());
 Object value = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(null);
 NodePointer nodePointer = propertyPointer.createPath(context, value);
 assertNotNull(nodePointer);
 assertEquals(propertyPointer, nodePointer.getParentPointer());
 assertEquals(value, nodePointer.getNode());
 }

 @Test
 public void testCreateChildWithName() {
 context = JXPathContext.newContext(new Object());
 String propertyName = "propertyName";
 PropertyPointer propertyPointer = new PropertyPointer(null);
 NodePointer nodePointer = propertyPointer.createChild(context, new QName(null, propertyName), 0);
 assertEquals(propertyName, nodePointer.getName().getLocalPart());
 }

 @Test
 public void testCreateChildWithNameAndIndex() {
 context = JXPathContext.newContext(new Object());
 String propertyName = "propertyName";
 int index = 1;
 PropertyPointer propertyPointer = new PropertyPointer(null);
 NodePointer nodePointer = propertyPointer.createChild(context, new QName(null, propertyName), index);
 assertEquals(propertyName, nodePointer.getName().getLocalPart());
 assertEquals(index, nodePointer.getIndex());
 }

 @Test
 public void testCreateChildWithoutName() {
 context = JXPathContext.newContext(new Object());
 int index = 1;
 PropertyPointer propertyPointer = new PropertyPointer(null);
 NodePointer nodePointer = propertyPointer.createChild(context, null, index);
 assertNull(nodePointer.getName());
 assertEquals(index, nodePointer.getIndex());
 }

}