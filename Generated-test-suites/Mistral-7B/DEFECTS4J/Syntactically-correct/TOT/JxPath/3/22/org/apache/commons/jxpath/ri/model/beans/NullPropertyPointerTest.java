import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 private NodePointer parent;

 @Test
 public void testNullPropertyPointerCreation() {
 parent = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getBaseValue());
 assertFalse(nullPropertyPointer.isLeaf());
 assertNotNull(nullPropertyPointer.getValuePointer());
 assertEquals(new QName("*"), nullPropertyPointer.getName());
 }

 @Test
 public void testNullPropertyPointerGetValue() {
 parent = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerSetValue() {
 parent = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue(new Object());
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 JXPathContext context = new JXPathContext(new Object());
 parent = new NodePointer(context.getBase(), null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.createPath(context));
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 parent = new NodePointer(context.getBase(), null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.createPath(context, new Object()));
 }

 @Test
 public void testNullPropertyPointerCreateChild() {
 JXPathContext context = new JXPathContext(new Object());
 parent = new NodePointer(context.getBase(), null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.createChild(context, new QName("test"), 0));
 }

 @Test
 public void testNullPropertyPointerCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 parent = new NodePointer(context.getBase(), null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.createChild(context, new QName("test"), 0, "test"));
 }

 @Test
 public void testNullPropertyPointerIsActualProperty() {
 parent = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testNullPropertyPointerIsActual() {
 parent = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testNullPropertyPointerIsContainer() {
 parent = new NodePointer(null);
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertTrue(nullPropertyPointer.isContainer());
 }
}