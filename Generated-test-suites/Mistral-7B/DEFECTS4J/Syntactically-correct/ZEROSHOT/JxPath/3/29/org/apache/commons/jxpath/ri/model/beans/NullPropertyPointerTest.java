import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NullPropertyPointerTest {

 @Test
 public void testNewNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("actual property: false", false, nullPropertyPointer.isActual());
 assertEquals("container: true", true, nullPropertyPointer.isContainer());
 assertEquals("immediate node: null", null, nullPropertyPointer.getImmediateNode());
 assertEquals("is leaf: true", true, nullPropertyPointer.isLeaf());
 assertEquals("value pointer: null", null, nullPropertyPointer.getValuePointer());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue("test value");
 }

 @Test
 public void testSetValuePropertyOwnerParent() {
 // TODO: add test implementation for PropertyOwnerPointer
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueNonPropertyParent() {
 // TODO: add test implementation for non-PropertyOwnerPointer parent
 }

 @Test
 public void testCreatePathNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull("path: null", nullPropertyPointer.createPath(null));
 }

 @Test
 public void testCreatePathPropertyOwnerParent() {
 // TODO: add test implementation for PropertyOwnerPointer parent
 }

 @Test
 public void testCreatePathNonPropertyParent() {
 // TODO: add test implementation for non-PropertyOwnerPointer parent
 }

 @Test
 public void testCreateChildNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull("child: null", nullPropertyPointer.createChild(null, null, 0));
 }

 @Test
 public void testCreateChildPropertyOwnerParent() {
 // TODO: add test implementation for PropertyOwnerPointer parent
 }

 @Test
 public void testCreateChildNonPropertyParent() {
 // TODO: add test implementation for non-PropertyOwnerPointer parent
 }

 @Test
 public void testGetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("property name: testProperty", "testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setNameAttributeValue("testAttribute");
 assertEquals("property name: testAttribute", "testAttribute", nullPropertyPointer.getPropertyName());
 assertEquals("by name attribute: true", true, nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollection() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("is collection: false", nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("property count: 0", 0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertArrayEquals("property names: []", new String[0], nullPropertyPointer.getPropertyNames());
 }

 @Test
 public void testAsPath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertNull("as path: null", nullPropertyPointer.asPath());
 NullPropertyPointer nullPropertyPointerByNameAttribute = new NullPropertyPointer(null);
 nullPropertyPointerByNameAttribute.setNameAttributeValue("testAttribute");
 JXPathContext context = new JXPathContext(new Object());
 NodePointer parent = new NodePointer(context, "testParent");
 NullPropertyPointer child = new NullPropertyPointer(parent);
 nullPropertyPointerByNameAttribute.setParent(child);
 assertEquals("as path: testParent[@name='testAttribute']", "testParent[@name='testAttribute']", nullPropertyPointerByNameAttribute.asPath());
 }
}