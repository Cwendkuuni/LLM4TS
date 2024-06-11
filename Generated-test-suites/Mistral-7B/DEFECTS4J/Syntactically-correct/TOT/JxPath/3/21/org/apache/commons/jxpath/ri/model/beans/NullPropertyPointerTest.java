import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NodePointer parent = new NodePointer(null, null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected null for base value", null, nullPropertyPointer.getBaseValue());
 assertTrue("Expected true for isLeaf", nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("Expected 'testProperty' for propertyName", "testProperty", nullPropertyPointer.getPropertyName().getName().getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("Expected 'testProperty' for propertyName", "testProperty", nullPropertyPointer.getPropertyName().getName().getLocalPart());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyNameWithNull() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName(null);
 }

 @Test
 public void testIsAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Expected false for isAttribute", nullPropertyPointer.isAttribute());
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(context, null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer path = nullPropertyPointer.createPath(context);
 assertNull("Expected null for createPath", path);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(context, null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer path = nullPropertyPointer.createPath(context, "testValue");
 assertNull("Expected null for createPath with value", path);
 }

 @Test
 public void testSetValue() {
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(context, null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 try {
 nullPropertyPointer.setValue("testValue");
 fail("Expected JXPathInvalidAccessException for setValue");
 } catch (JXPathInvalidAccessException e) {
 // Expected
 }
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(context, null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer child = nullPropertyPointer.createChild(context, new QName("testChild"));
 assertNull("Expected null for createChild", child);
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext context = new JXPathContext();
 NodePointer parent = new NodePointer(context, null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer child = nullPropertyPointer.createChild(context, new QName("testChild"), "testValue");
 assertNull("Expected null for createChild with value", child);
 }
}