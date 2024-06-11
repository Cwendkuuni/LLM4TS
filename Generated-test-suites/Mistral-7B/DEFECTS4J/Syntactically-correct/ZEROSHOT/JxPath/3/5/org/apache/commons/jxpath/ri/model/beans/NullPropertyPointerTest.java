import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.PropertyOwnerPointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("*", nullPropertyPointer.getName().getLocalPart());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPropertyIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyIndex(0);
 }

 @Test
 public void testGetLength() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals(0, nullPropertyPointer.getLength());
 }

 @Test
 public void testGetBaseValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull(nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertInstanceof(NullPointer.class, nullPropertyPointer.getValuePointer());
 }

 @Test
 public void testIsActualProperty() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValue_NullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 try {
 nullPropertyPointer.setValue(new Object());
 fail("Expected JXPathInvalidAccessException");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testSetValue_PropertyOwnerParent() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 nullPropertyPointer.setValue(new Object());
 PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
 assertEquals(nullPropertyPointer.getPropertyName(), propertyPointer.getPropertyName());
 }

 @Test
 public void testSetValue_NonPropertyParent() {
 NodePointer nodePointer = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 try {
 nullPropertyPointer.setValue(new Object());
 fail("Expected JXPathInvalidAccessException");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePath_Value() {
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(context, new Object());
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new NodePointer(null));
 nullPropertyPointer.setParent(propertyOwnerPointer);
 NodePointer childNodePointer = nullPropertyPointer.createChild(context, new QName("test"), 0);
 assertNull(childNodePointer);
 }

 @Test
 public void testCreateChild_Value() {
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new NodePointer(null));
 nullPropertyPointer.setParent(propertyOwnerPointer);
 NodePointer childNodePointer = nullPropertyPointer.createChild(context, new QName("test"), 0, new Object());
 assertNull(childNodePointer);
 }

 @Test
 public void testCreateChild_PropertyOwnerParent() {
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new NodePointer(null));
 nullPropertyPointer.setParent(propertyOwnerPointer);
 PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
 NodePointer childNodePointer = nullPropertyPointer.createChild(context, new QName("test"), 0, null);
 assertInstanceof(PropertyPointer.class, childNodePointer);
 assertEquals(propertyPointer.getPropertyName(), childNodePointer.getPropertyName());
 }

 @Test
 public void testCreatePath_NullParent() {
 JXPathContext context = new JXPathContext();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePath_Attribute() {
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = new NodePointer(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nodePointer);
 NodePointer nodePointer2 = nullPropertyPointer.createPath(context);
 assertInstanceof(NodePointer.class, nodePointer2);
 NodePointer attributeNodePointer = nodePointer2.createAttribute(context, new QName("test"));
 assertInstanceof(NodePointer.class, attributeNodePointer);
 }

 @Test
 public void testGetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("*", nullPropertyPointer.getPropertyName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("test");
 assertEquals("test", nullPropertyPointer.getPropertyName().getLocalPart());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("test");
 assertEquals("test", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testIsCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testgetPropertyCount() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testgetPropertyNames() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals(new String[0], propertyNames);
 }

 @Test
 public void testAsPath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("", nullPropertyPointer.asPath());
 }

 @Test
 public void testAsPath_ByNameAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("attr");
 assertEquals("[@name='attr']", nullPropertyPointer.asPath());
 }

 @Test
 public void testAsPath_Index() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer parentNodePointer = new NodePointer(new Object());
 nullPropertyPointer.setParent(parentNodePointer);
 nullPropertyPointer.setPropertyName("test");
 nullPropertyPointer.setIndex(1);
 assertEquals("[test][1]", nullPropertyPointer.asPath());
 }

 @Test
 public void testEscape() {
 String input = "&'\"";
 String expected = "&amp;&apos;&quot;";
 String output = new NullPropertyPointer().escape(input);
 assertEquals(expected, output);
 }
}