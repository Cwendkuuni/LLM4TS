import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 private NullPropertyPointer nullPropertyPointer;
 private NodePointer parentNodePointer;

 @Before
 public void setUp() {
 parentNodePointer = new NodePointer(null);
 nullPropertyPointer = new NullPropertyPointer(parentNodePointer);
 }

 @Test
 public void testGetName() {
 QName expectedName = new QName("*");
 QName actualName = nullPropertyPointer.getName();
 assertEquals(expectedName, actualName);
 }

 @Test
 public void testSetPropertyIndex() {
 nullPropertyPointer.setPropertyIndex(1);
 fail("Method should not be called");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLength() {
 nullPropertyPointer.getLength();
 }

 @Test
 public void testGetBaseValue() {
 assertNull(nullPropertyPointer.getBaseValue());
 }

 @Test
 public void testGetImmediateNode() {
 assertNull(nullPropertyPointer.getImmediateNode());
 }

 @Test
 public void testIsLeaf() {
 assertTrue(nullPropertyPointer.isLeaf());
 }

 @Test
 public void testGetValuePointer() {
 NodePointer expectedValuePointer = new NullPointer(nullPropertyPointer, new QName("*"));
 NodePointer actualValuePointer = nullPropertyPointer.getValuePointer();
 assertEquals(expectedValuePointer, actualValuePointer);
 }

 @Test
 public void testIsActualProperty() {
 assertFalse(nullPropertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 assertFalse(nullPropertyPointer.isActual());
 }

 @Test
 public void testIsContainer() {
 assertTrue(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValue() {
 try {
 nullPropertyPointer.setValue("testValue");
 fail("Should have thrown JXPathInvalidAccessException");
 } catch (JXPathInvalidAccessException e) {
 // Expected exception
 }
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 NodePointer expectedPath = new NodePointer(null);
 NodePointer actualPath = nullPropertyPointer.createPath(context);
 assertEquals(expectedPath, actualPath);
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext();
 NodePointer expectedPath = new NodePointer(null);
 NodePointer actualPath = nullPropertyPointer.createPath(context, "testValue");
 assertEquals(expectedPath, actualPath);
 }

 @Test
 public void testCreateChild() {
 JXPathContext context = new JXPathContext();
 NodePointer expectedChild = new NodePointer(null);
 NodePointer actualChild = nullPropertyPointer.createChild(context, new QName("childName"), 1);
 assertEquals(expectedChild, actualChild);
 }

 @Test
 public void testCreateChildWithValue() {
 JXPathContext context = new JXPathContext();
 NodePointer expectedChild = new NodePointer(null);
 NodePointer actualChild = nullPropertyPointer.createChild(context, new QName("childName"), 1, "testValue");
 assertEquals(expectedChild, actualChild);
 }

 @Test
 public void testGetPropertyName() {
 String expectedPropertyName = "*";
 String actualPropertyName = nullPropertyPointer.getPropertyName();
 assertEquals(expectedPropertyName, actualPropertyName);
 }

 @Test
 public void testSetPropertyName() {
 nullPropertyPointer.setPropertyName("newPropertyName");
 assertEquals("newPropertyName", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetNameAttributeValue() {
 nullPropertyPointer.setNameAttributeValue("newPropertyName");
 assertEquals("newPropertyName", nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testIsCollection() {
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testGetPropertyCount() {
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] expectedPropertyNames = new String[0];
 String[] actualPropertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals(expectedPropertyNames, actualPropertyNames);
 }

 @Test
 public void testAsPath() {
 String expectedPath = "";
 String actualPath = nullPropertyPointer.asPath();
 assertEquals(expectedPath, actualPath);
 }
}