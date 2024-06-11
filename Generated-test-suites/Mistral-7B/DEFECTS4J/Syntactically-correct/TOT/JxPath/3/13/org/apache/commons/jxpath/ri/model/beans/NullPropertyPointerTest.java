import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals(QName.valueOf("*"), nullPropertyPointer.getName());
 assertFalse(nullPropertyPointer.isActual());
 assertTrue(nullPropertyPointer.isLeaf());
 assertNull(nullPropertyPointer.getBaseValue());
 assertNull(nullPropertyPointer.getImmediateNode());
 assertFalse(nullPropertyPointer.isContainer());
 }

 @Test
 public void testSetValueOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 try {
 nullPropertyPointer.setValue("testValue");
 fail("Expected JXPathInvalidAccessException to be thrown");
 } catch (JXPathInvalidAccessException e) {
 // Expected exception
 }
 }

 @Test
 public void testCreatePathOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext(new NodePointer(null));
 assertNull(nullPropertyPointer.createPath(context));
 }

 @Test
 public void testSettingPropertyNameOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testPropertyName");
 assertEquals("testPropertyName", nullPropertyPointer.getPropertyName().getValue());
 }

 @Test
 public void testIsCollectionOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse(nullPropertyPointer.isCollection());
 }

 @Test
 public void testPropertyCountOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals(0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testPropertyNamesOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals(new String[0], propertyNames);
 }

 @Test
 public void testAsPathOnNullPropertyPointer() {
 PropertyPointer parent = new PropertyPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("", nullPropertyPointer.asPath());
 }
}