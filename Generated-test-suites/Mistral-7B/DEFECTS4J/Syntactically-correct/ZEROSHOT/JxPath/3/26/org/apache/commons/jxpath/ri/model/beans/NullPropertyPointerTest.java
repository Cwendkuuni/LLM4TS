import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerInstance() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Property pointer instance created successfully", null, nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testGetNameMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 QName expectedName = new QName("*");
 QName actualName = nullPropertyPointer.getName();
 assertEquals("Expected and actual property names should match", expectedName, actualName);
 }

 @Test
 public void testSetPropertyIndexMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyIndex(1);
 assertEquals("Property index should be set to zero as per implementation", 0, nullPropertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetLengthMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Length of null property pointer should be zero", 0, nullPropertyPointer.getLength());
 }

 @Test
 public void testGetBaseValueMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Base value of null property pointer should be null", null, nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNodeMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Immediate node of null property pointer should be null", null, nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeafMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue("Null property pointer should be a leaf", nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetValuePointerMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer expectedValuePointer = new NullPointer(nullPointer, new QName("*"));
 NodePointer actualValuePointer = nullPropertyPointer.getValuePointer();
 assertEquals("Expected and actual value pointers should match", expectedValuePointer, actualValuePointer);
 }

 @Test
 public void testIsActualPropertyMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Null property pointer should not be an actual property", nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActualMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertFalse("Null property pointer should not be actual", nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainerMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertTrue("Null property pointer should be a container", nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValueMethod() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 Object value = new Object();
 try {
 nullPropertyPointer.setValue(value);
 fail("Setting value of null property pointer should throw an exception");
 } catch (JXPathInvalidAccessException e) {
 // Expected exception
 }
 }

 @Test
 public void testCreatePathMethod() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer expectedPath = new NullPointer(nullPointer, new QName("*"));
 NodePointer actualPath = nullPropertyPointer.createPath(context);
 assertEquals("Expected and actual paths should match", expectedPath, actualPath);
 }

 @Test
 public void testCreatePathWithValueMethod() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer value = new NodePointer(null, new QName("value"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer expectedPath = new NodePointer(nullPointer, new QName("value"), 0, value);
 NodePointer actualPath = nullPropertyPointer.createPath(context, value);
 assertEquals("Expected and actual paths should match", expectedPath, actualPath);
 }

 @Test
 public void testCreateChildMethod() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer expectedChild = null;
 NodePointer actualChild = nullPropertyPointer.createChild(context, new QName("child"), 0);
 assertEquals("Expected and actual children should be null", expectedChild, actualChild);
 }

 @Test
 public void testCreateChildWithValueMethod() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer value = new NodePointer(null, new QName("value"));
 NodePointer expectedChild = null;
 NodePointer actualChild = nullPropertyPointer.createChild(context, new QName("child"), 0, value);
 assertEquals("Expected and actual children should be null", expectedChild, actualChild);
 }

 @Test
 public void testCreateChildWithNameAttributeValueMethod() {
 JXPathContext context = new JXPathContext(new Object());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 NodePointer expectedChild = null;
 NodePointer actualChild = nullPropertyPointer.createChild(context, new QName("child"), 0, null, "attrName", "attrValue");
 assertEquals("Expected and actual children should be null", expectedChild, actualChild);
 }
}