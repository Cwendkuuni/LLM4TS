import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private Object bean;

 @Before
 public void setUp() {
 bean = new MyClass();
 propertyPointer = new MyPropertyPointer(new NodePointer(new NodePointer(null), ".", 0), 0);
 propertyPointer.setBean(bean);
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
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 assertEquals("myProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyName() {
 assertEquals("myProperty", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyCount() {
 assertEquals(2, ((MyPropertyPointer) propertyPointer).getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] expected = {"property1", "property2"};
 String[] actual = ((MyPropertyPointer) propertyPointer).getPropertyNames();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testIsActualProperty() {
 assertTrue(((MyPropertyPointer) propertyPointer).isActualProperty());
 }

 @Test
 public void testGetImmediateNode() {
 assertNotNull(propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 assertFalse(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 assertEquals(1, propertyPointer.getLength());
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(bean);
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertEquals(bean.getClass(), nodePointer.getNode().getClass());
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathWithInvalidFactory() {
 JXPathContext context = new JXPathContext(null);
 PropertyPointer propertyPointer = new MyInvalidPropertyPointer(new NodePointer(null, ".", 0), 0);
 propertyPointer.setBean(bean);
 propertyPointer.createPath(context);
 fail("Expected JXPathAbstractFactoryException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithInvalidIndex() {
 JXPathContext context = new JXPathContext(bean);
 PropertyPointer propertyPointer = new MyInvalidPropertyPointer(new NodePointer(null, ".", 0), -1);
 propertyPointer.setBean(bean);
 propertyPointer.createPath(context);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext(bean);
 propertyPointer.createPath(context, new Object());
 assertNotNull(propertyPointer.getNode());
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(bean);
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("childProperty"), 1, new Object());
 assertEquals("childProperty", childNodePointer.getName().getLocalPart());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildWithNullContext() {
 NodePointer childNodePointer = propertyPointer.createChild(null, new QName("childProperty"), 1, new Object());
 fail("Expected NullPointerException");
 }

 @Test
 public void testCreateChildWithNullQName() {
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 1, new Object());
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithNegativeIndex() {
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("childProperty"), -1, new Object());
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testEquals() {
 PropertyPointer otherPropertyPointer = new MyPropertyPointer(new NodePointer(null, ".", 0), 0);
 otherPropertyPointer.setBean(bean);
 assertTrue(propertyPointer.equals(otherPropertyPointer));
 }

 @Test
 public void testHashCode() {
 NodePointer parent = new NodePointer(null, ".", 0);
 PropertyPointer otherPropertyPointer1 = new MyPropertyPointer(parent, 0);
 PropertyPointer otherPropertyPointer2 = new MyPropertyPointer(parent, 1);
 int expected1 = parent.hashCode() + 0 + 0;
 int expected2 = parent.hashCode() + 0 + 1;
 assertEquals(expected1, propertyPointer.hashCode());
 assertEquals(expected1, otherPropertyPointer1.hashCode());
 assertEquals(expected2, otherPropertyPointer2.hashCode());
 }
}