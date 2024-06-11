import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NullPropertyPointerTest {

 @Test
 public void testNewInstance() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("*", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testGetName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 QName name = nullPropertyPointer.getName();
 assertEquals("*", name.getLocalPart());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testIsLeaf() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetValuePointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer valuePointer = nullPropertyPointer.getValuePointer();
 assertNull(valuePointer);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSetValueWithNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue("testValue");
 }

 @Test
 public void testCreatePathWithNullContext() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer nodePointer = nullPropertyPointer.createPath(null);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithNullParentAndAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithNullParentAndChild() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 NodePointer parentNodePointer = new Object[0];
 NodePointer nodePointer = nullPropertyPointer.createPath(context, parentNodePointer);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithParentAndAttribute() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 Object parentNode = new Object();
 NodePointer nodePointer = nullPropertyPointer.createPath(context, parentNode);
 assertNull(nodePointer);
 }

 @Test
 public void testCreatePathWithParentAndChild() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 Object parentNode = new Object();
 NodePointer nodePointer = nullPropertyPointer.createPath(context, parentNode);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithNullContextAndName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, null, 0);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithNullContextAndNameAndIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, null, 0, null);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithNullContextAndNameAndIndexAndValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, null, 0, "testValue");
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithContextAndName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, null, 0);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithContextAndNameAndIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, null, 0, null);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithContextAndNameAndIndexAndValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 NodePointer nodePointer = nullPropertyPointer.createChild(context, null, 0, "testValue");
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithParentAndName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 Object parentNode = new Object();
 NodePointer nodePointer = nullPropertyPointer.createChild(context, parentNode, null);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithParentAndNameAndIndex() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 Object parentNode = new Object();
 NodePointer nodePointer = nullPropertyPointer.createChild(context, parentNode, 0, null);
 assertNull(nodePointer);
 }

 @Test
 public void testCreateChildWithParentAndNameAndIndexAndValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 JXPathContext context = new JXPathContext(new Object[0]);
 Object parentNode = new Object();
 NodePointer nodePointer = nullPropertyPointer.createChild(context, parentNode, 0, "testValue");
 assertNull(nodePointer);
 }

}