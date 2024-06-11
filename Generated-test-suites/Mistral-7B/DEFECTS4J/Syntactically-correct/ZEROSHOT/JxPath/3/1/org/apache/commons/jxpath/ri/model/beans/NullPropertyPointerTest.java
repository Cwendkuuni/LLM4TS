import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testCreatePath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object());
 NodePointer result = nullPropertyPointer.createPath(context);
 assertNull(result);
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testPropertyName");
 assertEquals("testPropertyName", nullPropertyPointer.getPropertyName());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetPropertyNameWithParentIsNull() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testPropertyName");
 nullPropertyPointer.setParent(null);
 nullPropertyPointer.setValue(new Object());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetPropertyNameWithParentNotContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testPropertyName");
 NodePointer parent = new DOMNodePointer(null, 1);
 nullPropertyPointer.setParent(parent);
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isLeaf());
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
 public void testCreatePathWithParentPropertyOwner() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 PropertyOwnerPointer parentPointer = new PropertyOwnerPointer(null);
 parentPointer.setPropertyName("testPropertyName");
 nullPropertyPointer.setParent(parentPointer);
 NodePointer result = nullPropertyPointer.createPath(null);
 assertEquals(parentPointer, result);
 }

 @Test
 public void testCreatePathWithParentNullPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NullPointer parentPointer = new NullPointer(null, new QName("testQName"));
 nullPropertyPointer.setParent(parentPointer);
 NodePointer result = nullPropertyPointer.createPath(null);
 assertNull(result);
 }

 @Test
 public void testCreatePathWithAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 PropertyOwnerPointer parentPointer = new PropertyOwnerPointer(null);
 parentPointer.setPropertyName("testPropertyName");
 nullPropertyPointer.setParent(parentPointer);
 NodePointer result = nullPropertyPointer.createPath(null, "testAttributeName");
 assertInstanceOf(NodePointer.class, result);
 }

 @Test
 public void testCreatePathWithChild() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 PropertyOwnerPointer parentPointer = new PropertyOwnerPointer(null);
 parentPointer.setPropertyName("testPropertyName");
 nullPropertyPointer.setParent(parentPointer);
 NodePointer childPointer = new NodePointer(null, 1);
 nullPropertyPointer.setParent(childPointer);
 NodePointer result = nullPropertyPointer.createPath(null, "testChildName");
 assertInstanceOf(NodePointer.class, result);
 }

 @Test
 public void testSetValueWithParentIsNull() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue(new Object());
 assertNull(nullPropertyPointer.getParent());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithParentNotContainer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer parentPointer = new DOMNodePointer(null, 1);
 nullPropertyPointer.setParent(parentPointer);
 nullPropertyPointer.setValue(new Object());
 }

 @Test
 public void testSetValueWithParentPropertyOwner() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 PropertyOwnerPointer parentPointer = new PropertyOwnerPointer(null);
 parentPointer.setPropertyName("testPropertyName");
 nullPropertyPointer.setParent(parentPointer);
 nullPropertyPointer.setValue(new Object());
 assertEquals(nullPropertyPointer, parentPointer.getPropertyPointer());
 }

 @Test
 public void testSetValueWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setParent(null);
 nullPropertyPointer.setValue(new Object());
 assertNull(nullPropertyPointer.getParent());
 }

 @Test
 public void testSetValueWithPropertyOwnerParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 PropertyOwnerPointer parentPointer = new PropertyOwnerPointer(null);
 parentPointer.setPropertyName("testPropertyName");
 nullPropertyPointer.setParent(parentPointer);
 nullPropertyPointer.setValue(new Object());
 assertEquals(nullPropertyPointer, parentPointer.getPropertyPointer());
 }

 @Test
 public void testSetValueWithNullValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue(null);
 assertNull(nullPropertyPointer.getValue());
 }

 @Test
 public void testSetValueWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue(new Object());
 assertEquals(new Object(), nullPropertyPointer.getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testCreatePathWithParentNotPropertyOwner() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 DOMNodePointer parentPointer = new DOMNodePointer(null, 1);
 nullPropertyPointer.setParent(parentPointer);
 NodePointer result = nullPropertyPointer.createPath(null);
 assertNull(result);
 }

 @Test
 public void testCreatePathWithParentNullPointerAndAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NullPointer parentPointer = new NullPointer(null, new QName("testQName"));
 nullPropertyPointer.setParent(parentPointer);
 NodePointer result = nullPropertyPointer.createPath(null, "testAttributeName");
 assertNull(result);
 }

 @Test
 public void testCreatePathWithParentNullPointerAndChild() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NullPointer parentPointer = new NullPointer(null, new QName("testQName"));
 nullPropertyPointer.setParent(parentPointer);
 NodePointer result = nullPropertyPointer.createPath(null, "testChildName");
 assertNull(result);
 }

 @Test
 public void testCreatePathWithParentDOMNodePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 DOMNodePointer parentPointer = new DOMNodePointer(null, 1);
 nullPropertyPointer.setParent(parentPointer);
 NodePointer result = nullPropertyPointer.createPath(null);
 assertInstanceOf(NodePointer.class, result);
 }
}