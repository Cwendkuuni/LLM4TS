import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointer() {
 NodePointer parent = new NodePointer(null);
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 assertEquals(new QName("*"), pointer.getName());
 assertEquals(0, pointer.getLength());
 assertNull(pointer.getBaseValue());
 assertNull(pointer.getImmediateNode());
 assertTrue(pointer.isLeaf());
 assertNull(pointer.getValuePointer());
 assertFalse(pointer.isActualProperty());
 assertFalse(pointer.isActual());
 assertTrue(pointer.isContainer());

 JXPathContext context = new JXPathContext(new Object());
 NodePointer newParent = parent.createPath(context);
 NodePointer child = pointer.createPath(context);
 assertNull(child.getNode());
 }

 @Test
 public void testNullPropertyPointerSetValue() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 JXPathContext context = new JXPathContext(new Object());
 NodePointer newParent = parent.createPath(context);
 PropertyPointer propertyPointer = newPropertyPointer(newParent, "testProperty");
 propertyPointer.setValue("expectedValue");

 pointer.setValue("value");
 assertEquals("expectedValue", propertyPointer.getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerSetValueInvalid() {
 NodePointer parent = null;
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 pointer.setValue("value");
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 JXPathContext context = new JXPathContext(new Object());
 NodePointer newParent = parent.createPath(context);
 NodePointer child = pointer.createPath(context);
 NodePointer grandChild = child.createPath(context, new QName("grandChild"));
 assertNull(grandChild.getNode());
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 JXPathContext context = new JXPathContext(new Object());
 NodePointer newParent = parent.createPath(context);
 PropertyPointer propertyPointer = newPropertyPointer(newParent, "testProperty");
 propertyPointer.setValue("expectedValue");

 NodePointer child = pointer.createPath(context, "testProperty", "value");
 assertEquals("expectedValue", propertyPointer.getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerCreatePathInvalid() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 NodePointer child = pointer.createPath(null);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerCreatePathIndexInvalid() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 JXPathContext context = new JXPathContext(new Object());
 NodePointer newParent = parent.createPath(context);
 PropertyPointer propertyPointer = newPropertyPointer(newParent, "testProperty");
 propertyPointer.setValue("expectedValue");

 NodePointer child = pointer.createPath(context, "testProperty", -1);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerCreateChild() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 JXPathContext context = new JXPathContext(new Object());
 NodePointer newParent = parent.createPath(context);
 PropertyPointer propertyPointer = newPropertyPointer(newParent, "testProperty");
 propertyPointer.setValue("expectedValue");

 NodePointer child = pointer.createChild(context, new QName("testChild"), 1);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerCreateChildIndexInvalid() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 JXPathContext context = new JXPathContext(new Object());
 NodePointer newParent = parent.createPath(context);
 PropertyPointer propertyPointer = newPropertyPointer(newParent, "testProperty");
 propertyPointer.setValue("expectedValue");

 NodePointer child = pointer.createChild(context, new QName("testChild"), -1);
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerCreateChildValue() {
 NodePointer parent = new NodePointer(new Object());
 NullPropertyPointer pointer = new NullPropertyPointer(parent);

 JXPathContext context = new JXPathContext(new Object());
 NodePointer newParent = parent.createPath(context);
 PropertyPointer propertyPointer = newPropertyPointer(newParent, "testProperty");
 propertyPointer.setValue("expectedValue");

 NodePointer child = pointer.createChild(context, new QName("testChild"), 1, "value");
 }
}