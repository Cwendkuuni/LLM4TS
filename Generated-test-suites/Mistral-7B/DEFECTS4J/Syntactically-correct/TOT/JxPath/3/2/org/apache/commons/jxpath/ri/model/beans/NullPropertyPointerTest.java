import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 assertEquals("Expected null property pointer to have empty name", "", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testEmptyPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("");
 assertEquals("Expected empty string property name", "", nullPropertyPointer.getPropertyName());
 }

 @Test
 public void testSetPropertyName() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setPropertyName("testName");
 assertEquals("Expected property name to be set", "testName", nullPropertyPointer.getPropertyName());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnNullParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
 nullPropertyPointer.setValue(new Object());
 fail("Expected JXPathInvalidAccessException to be thrown");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnNonPropertyParent() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 nullPropertyPointer.setValue(new Object());
 fail("Expected JXPathInvalidAccessException to be thrown");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnPropertyOwnerParentWithNoProperty() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(null);
 NullPropertyPointer nullPropertyPointer = propertyOwnerPointer.createPropertyPointer();
 nullPropertyPointer.setValue(new Object());
 fail("Expected JXPathInvalidAccessException to be thrown");
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnPropertyOwnerParentWithExistingProperty() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new NodePointer(null));
 propertyOwnerPointer.setPropertyName("testProperty");
 PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
 propertyPointer.setValue(new Object());
 NullPropertyPointer nullPropertyPointer = propertyOwnerPointer.createPropertyPointer();
 nullPropertyPointer.setValue(new Object());
 fail("Expected JXPathInvalidAccessException to be thrown");
 }

 @Test
 public void testCreatePath() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 JXPathContext context = new JXPathContext(new NodePointer(null));
 NodePointer path = nullPropertyPointer.createPath(context);
 assertNull("Expected null pointer to be created when calling createPath", path);
 }

 @Test
 public void testCreatePathWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 JXPathContext context = new JXPathContext(new NodePointer(null));
 Object value = new Object();
 NodePointer path = nullPropertyPointer.createPath(context, value);
 assertNull("Expected null pointer to be created when calling createPath with value", path);
 }

 @Test
 public void testCreateChild() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 JXPathContext context = new JXPathContext(new NodePointer(null));
 QName name = new QName("testName");
 NodePointer child = nullPropertyPointer.createChild(context, name, 0);
 assertNull("Expected null pointer to be created when calling createChild", child);
 }

 @Test
 public void testCreateChildWithValue() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
 JXPathContext context = new JXPathContext(new NodePointer(null));
 QName name = new QName("testName");
 Object value = new Object();
 NodePointer child = nullPropertyPointer.createChild(context, name, 0, value);
 assertNull("Expected null pointer to be created when calling createChild with value", child);
 }
}