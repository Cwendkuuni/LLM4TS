import org.apache.commons.jxpath.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.AbstractFactory;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerConstruction() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testPropertyPointerWithParent() {
 NodePointer parentPointer = new NodePointer(null);
 PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
 assertEquals(parentPointer, propertyPointer.getImmediateParentPointer());
 assertNull(propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexNegativeNumber() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetBean() {
 Object bean = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testSetBean() {
 Object bean1 = new Object();
 Object bean2 = new Object();
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(bean1);
 propertyPointer.setBean(bean2);
 assertEquals(bean2, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", propertyPointer.getPropertyName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetPropertyCount() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.getPropertyCount();
 }

 @Test
 public void testGetPropertyNames() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String[] getPropertyNames() {
 return new String[]{"name1", "name2"};
 }
 };
 assertArrayEquals(new String[]{"name1", "name2"}, propertyPointer.getPropertyNames());
 }

 @Test
 public void testIsActualProperty() {
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
 return new Object(){
 @Override
 public String toString() {
 return "[1, 2, 3]";
 }
 };
 };
 };
 propertyPointer.setIndex(1);
 assertEquals("2", propertyPointer.getImmediateNode().toString());
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
 return 42;
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
 propertyPointer.setIndex(1);
 NodePointer expectedValuePointer = NodePointer.newChildNodePointer(propertyPointer, "value", 1);
 NodePointer actualValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(expectedValuePointer, actualValuePointer);
 }

 @Test
 public void testCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object(){
 @Override
 public String toString() {
 return "[1, 2, 3]";
 }
 };
 };
 };
 propertyPointer.setIndex(1);
 JXPathContext context = new JXPathContext();
 AbstractFactory factory = new AbstractFactory() {};
 NodePointer actualPath = propertyPointer.createPath(context);
 NodePointer expectedPath = NodePointer.newChildNodePointer(
 NodePointer.newChildNodePointer(NodePointer.newChildNodePointer(NodePointer.newRootNodePointer(), "parent"), "property"), 1);
 assertEquals(expectedPath, actualPath);
 }

 @Test
 public void testCreatePathWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public Object getBaseValue() {
 return new Object[]{1, 2, 3};
 };
 };
 propertyPointer.setIndex(1);
 JXPathContext context = new JXPathContext();
 Object value = new Object();
 NodePointer actualPath = propertyPointer.createPath(context, value);
 NodePointer expectedPath = NodePointer.newChildNodePointer(
 NodePointer.newChildNodePointer(NodePointer.newChildNodePointer(NodePointer.newRootNodePointer(), "parent"), "property"), 1);
 expectedPath.setValue(value);
 assertEquals(expectedPath, actualPath);
 }
}