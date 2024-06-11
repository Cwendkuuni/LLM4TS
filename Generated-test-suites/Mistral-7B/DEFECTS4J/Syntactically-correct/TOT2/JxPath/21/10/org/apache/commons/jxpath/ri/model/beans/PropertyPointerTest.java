import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private Object parentObject;

 @Before
 public void setup() {
 parentObject = new Object() {};
 propertyPointer = new PropertyPointer(NodePointer.newChildNodePointer(
 null, null, parentObject));
 }

 @Test
 public void testGetPropertyIndex() {
 int expectedPropertyIndex = 0;
 assertEquals(expectedPropertyIndex, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 int newPropertyIndex = 1;
 propertyPointer.setPropertyIndex(newPropertyIndex);
 assertEquals(newPropertyIndex, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 assertSame(parentObject, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 fail("Abstract method 'getPropertyName()' must be implemented");
 }

 @Test
 public void testSetPropertyName() {
 fail("Abstract method 'setPropertyName(String)' must be implemented");
 }

 @Test
 public void testGetPropertyCount() {
 fail("Abstract method 'getPropertyCount()' must be implemented");
 }

 @Test
 public void testGetPropertyNames() {
 fail("Abstract method 'getPropertyNames()' must be implemented");
 }

 @Test
 public void testIsActualProperty() {
 fail("Abstract method 'isActualProperty()' must be implemented");
 }

 @Test
 public void testIsActual() {
 fail("Abstract subclass method 'isActual()' must be implemented");
 }

 @Test
 public void testGetImmediateNode() {
 fail("Abstract method 'getImmediateNode()' must be implemented");
 }

 @Test
 public void testIsCollection() {
 fail("Abstract method 'isCollection()' must be implemented");
 }

 @Test
 public void testIsLeaf() {
 fail("Abstract method 'isLeaf()' must be implemented");
 }

 @Test
 public void testGetLength() {
 fail("Abstract method 'getLength()' must be implemented");
 }

 @Test
 public void testGetImmediateValuePointer() {
 fail("Abstract method 'getImmediateValuePointer()' must be implemented");
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext(parentObject);
 PropertyPointer createdPropertyPointer = propertyPointer.createPath(context);
 assertSame(createdPropertyPointer, propertyPointer);
 }

 @Test(expected = JXPathAbstractFactoryException.class)
 public void testCreatePathWithInvalidFactory() {
 JXPathContext context = new JXPathContext(parentObject);
 PropertyPointer propertyPointerWithInvalidFactory = new PropertyPointerWithInvalidFactory(
 NodePointer.newChildNodePointer(null, null, parentObject));
 try {
 propertyPointerWithInvalidFactory.createPath(context);
 } catch (JXPathAbstractFactoryException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePathWithInvalidBean() {
 JXPathContext context = new JXPathContext(null);
 try {
 propertyPointer.createPath(context);
 fail("Expected JXPathAbstractFactoryException");
 } catch (JXPathAbstractFactoryException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePathWithInvalidName() {
 JXPathContext context = new JXPathContext(parentObject);
 try {
 propertyPointer.createPath(context, null);
 fail("Expected JXPathAbstractFactoryException");
 } catch (JXPathAbstractFactoryException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePathWithInvalidIndex() {
 JXPathContext context = new JXPathContext(parentObject);
 try {
 propertyPointer.createPath(context, "invalidIndex");
 fail("Expected JXPathAbstractFactoryException");
 } catch (JXPathAbstractFactoryException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePathWithInvalidValue() {
 JXPathContext context = new JXPathContext(parentObject);
 try {
 propertyPointer.createPath(context, new Object() {});
 fail("Expected JXPathAbstractFactoryException");
 } catch (JXPathAbstractFactoryException e) {
 // Expected
 }
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext(parentObject);
 String childName = "childProperty";
 PropertyPointer childPropertyPointer = propertyPointer.createChild(context, new QName(null, childName), 0, null);
 assertNotSame(propertyPointer, childPropertyPointer);
 }

 @Test
 public void testCreateChildWithInvalidName() {
 JXPathContext context = new JXPathContext(parentObject);
 try {
 propertyPointer.createChild(context, null, 0, null);
 fail("Expected JXPathAbstractFactoryException");
 } catch (JXPathAbstractFactoryException e) {
 // Expected
 }
 }

 @Test
 public void testCreateChildWithInvalidIndex() {
 JXPathContext context = new JXPathContext(parentObject);
 String childName = "childProperty";
 try {
 propertyPointer.createChild(context, new QName(null, childName), -1, null);
 fail("Expected JXPathAbstractFactoryException");
 } catch (JXPathAbstractFactoryException e) {
 // Expected
 }
 }

 @Test
 public void testCreateChildWithInvalidValue() {
 JXPathContext context = new JXPathContext(parentObject);
 String childName = "childProperty";
 try {
 propertyPointer.createChild(context, new QName(null, childName), 0, new Object() {});
 fail("Expected JXPathAbstractFactoryException");
 } catch (JXPathAbstractFactoryException e) {
 // Expected
 }
 }

}