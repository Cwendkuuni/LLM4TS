import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointerFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private JXPathContext context;
 private PropertyPointer pointer;

 @Before
 public void setUp() {
 context = new JXPathContext();
 }

 @Test
 public void testPropertyPointerCreation() {
 PropertyPointer pointer = new PropertyPointer(null);
 assertNull(pointer.getBean());
 assertNull(pointer.getValue());
 assertNull(pointer.getName());
 assertEquals(-1, pointer.getLength());
 assertFalse(pointer.isActual());
 }

 @Test
 public void testPropertyPointerCreationWithParent() {
 NodePointer parent = NodePointer.newChildNodePointer(
 context, new QName("root"), null);
 PropertyPointer pointer = new PropertyPointer(parent);
 assertSame(parent, pointer.getImmediateParentPointer());
 assertNull(pointer.getBean());
 assertNull(pointer.getValue());
 assertNull(pointer.getName());
 assertEquals(-1, pointer.getLength());
 assertFalse(pointer.isActual());
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer pointer = new PropertyPointer(null);
 pointer.setPropertyIndex(0);
 assertEquals(0, pointer.getPropertyIndex());
 pointer.setPropertyIndex(-1);
 assertEquals(-1, pointer.getPropertyIndex());
 pointer.setPropertyIndex(Integer.MAX_VALUE);
 assertEquals(Integer.MAX_VALUE, pointer.getPropertyIndex());
 }

 @Test
 public void testSetBean() {
 Object bean = new Object();
 PropertyPointer pointer = new PropertyPointer(null);
 pointer.setBean(bean);
 assertEquals(bean, pointer.getBean());
 }

 @Test
 public void testSetPropertyName() {
 PropertyPointer pointer = new PropertyPointer(null);
 pointer.setPropertyName("propertyName");
 assertEquals("propertyName", pointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 assertEquals(1, pointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 String[] propertyNames = pointer.getPropertyNames();
 assertEquals(1, propertyNames.length);
 assertEquals("property", propertyNames[0]);
 }

 @Test
 public void testIsActualProperty() {
 PropertyPointer pointer = new PropertyPointer(null);
 assertFalse(pointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 PropertyPointer pointer = new PropertyPointer(null);
 assertFalse(pointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 assertNull(pointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 assertFalse(pointer.isCollection());
 }

 @Test
 public void testGetLength() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 assertEquals(-1, pointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 NodePointer valuePointer = pointer.getImmediateValuePointer();
 assertEquals(pointer, valuePointer.getParentPointer());
 assertNull(valuePointer.getName());
 assertNull(valuePointer.getNode());
 }

 @Test
 public void testCreatePath() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 Object bean = new TestBean();
 pointer.createPath(context, bean);
 assertEquals(bean, pointer.getBean());
 assertEquals("property", pointer.getPropertyName());
 assertEquals(bean, pointer.getNode().getNode());
 }

 @Test
 public void testCreatePathWithValue() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 Object bean = new TestBean();
 pointer.createPath(context, bean, "value");
 assertEquals(bean, pointer.getBean());
 assertEquals("property", pointer.getPropertyName());
 assertEquals("value", pointer.getNode().getName().getLocalPart());
 }

 @Test
 public void testCreateChild() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 NodePointer childPointer = pointer.createChild(context, null, 0, null);
 assertEquals(pointer, childPointer.getParentPointer());
 assertNull(childPointer.getName());
 assertNull(childPointer.getNode());
 }

 @Test
 public void testCreateChildWithName() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 NodePointer childPointer = pointer.createChild(context, new QName("TestBean", "child"), 0, null);
 assertEquals(pointer, childPointer.getParentPointer());
 assertEquals("child", childPointer.getName().getLocalPart());
 assertNull(childPointer.getNode());
 }

 @Test
 public void testCreateChildWithValue() throws Exception {
 PropertyPointer pointer = new PropertyPointerFactory().createPropertyPointer(
 context, "TestBean", "property");
 NodePointer childPointer = pointer.createChild(context, new QName("TestBean", "child"), 0, new Object());
 assertEquals(pointer, childPointer.getParentPointer());
 assertEquals("child", childPointer.getName().getLocalPart());
 assertNull(childPointer.getNode());
 }

 @Test
 public void testHashCode() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 PropertyPointer pointer3 = new PropertyPointer(pointer2);
 assertEquals(pointer1.hashCode(), pointer1.hashCode());
 assertEquals(pointer1.hashCode(), pointer2.hashCode());
 assertEquals(pointer1.hashCode(), pointer3.hashCode());
 }

 @Test
 public void testEquals() {
 PropertyPointer pointer1 = new PropertyPointer(null);
 PropertyPointer pointer2 = new PropertyPointer(null);
 PropertyPointer pointer3 = new PropertyPointer(pointer2);
 PropertyPointer pointer4 = new PropertyPointer(context, new Object(), "property");
 assertFalse(pointer1.equals(null));
 assertFalse(pointer1.equals(pointer2));
 assertTrue(pointer1.equals(pointer1));
 assertTrue(pointer1.equals(pointer3));
 assertFalse(pointer1.equals(pointer4));
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer pointer1 = NodePointer.newChildNodePointer(context, new QName("root"), null);
 NodePointer pointer2 = NodePointer.newChildNodePointer(context, new QName("root"), null);
 PropertyPointer propertyPointer1 = new PropertyPointerFactory().createPropertyPointer(context, "TestBean", "property");
 PropertyPointer propertyPointer2 = new PropertyPointerFactory().createPropertyPointer(context, "TestBean", "property");
 assertEquals(0, propertyPointer1.compareChildNodePointers(pointer1, pointer2));
 }

}

class TestBean {
 private String property;
}