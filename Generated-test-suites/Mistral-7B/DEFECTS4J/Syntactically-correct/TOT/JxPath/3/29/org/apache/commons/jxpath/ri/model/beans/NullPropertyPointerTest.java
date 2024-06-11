import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected null property pointer to have null parent", null, nullPropertyPointer.getParent());
 assertEquals("Expected null property pointer to have empty name", "*", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testNullPropertyPointerGetName() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull("Expected null property pointer to have no name", nullPropertyPointer.getName());
 }

 @Test
 public void testNullPropertyPointerSetPropertyName() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("Expected null property pointer to have set property name to testProperty", "testProperty", nullPropertyPointer.getPropertyName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPropertyPointerSetNameAttributeValue() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setNameAttributeValue(null);
 }

 @Test
 public void testNullPropertyPointerIsCollection() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertFalse("Expected null property pointer to be not a collection", nullPropertyPointer.isCollection());
 }

 @Test
 public void testNullPropertyPointerGetPropertyCount() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected null property pointer to have 0 properties", 0, nullPropertyPointer.getPropertyCount());
 }

 @Test
 public void testNullPropertyPointerGetPropertyNames() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 String[] propertyNames = nullPropertyPointer.getPropertyNames();
 assertArrayEquals("Expected null property pointer to have empty property names array", new String[0], propertyNames);
 }

 @Test
 public void testNullPropertyPointerAsPath() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("Expected null property pointer to have empty asPath", "", nullPropertyPointer.asPath());
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createPath(context);
 assertNull("Expected null property pointer to create null node pointer", nodePointer);
 }

 @Test
 public void testNullPropertyPointerSetValue() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue(new String("testValue"));
 fail("Expected JXPathInvalidAccessException to be thrown when setting value on null property pointer");
 }

 @Test
 public void testNullPropertyPointerCreateChild() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testChild"), 0);
 assertNull("Expected null property pointer to create null node pointer", nodePointer);
 }

 @Test
 public void testNullPropertyPointerCreatePathWithValue() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createPath(context, new String("testValue"));
 assertNull("Expected null property pointer to create null node pointer", nodePointer);
 }

 @Test
 public void testNullPropertyPointerCreateChildWithValue() {
 PropertyPointer parent = new PropertyOwnerPointer(new DOMNodePointer(null, null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 JXPathContext context = new JXPathContext();
 NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("testChild"), 0, new String("testValue"));
 assertNull("Expected null property pointer to create null node pointer", nodePointer);
 }
}