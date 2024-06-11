import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 assertEquals("*", nullPropertyPointer.getPropertyName());
 assertFalse(nullPropertyPointer.isActualProperty());
 assertFalse(nullPropertyPointer.isActual());
 assertFalse(nullPropertyPointer.isContainer());
 assertNull(nullPropertyPointer.getValuePointer().getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueOnNullPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 nullPropertyPointer.setValue("Test Value");
 }

 @Test
 public void testCreatingPathForNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 NodePointer path = nullPropertyPointer.createPath(context);
 assertNull(path.getValue());
 }

 @Test
 public void testSettingNameAttributeValueForNullPropertyPointer() {
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 nullPropertyPointer.setNameAttributeValue("test");
 assertEquals("test", nullPropertyPointer.getPropertyName());
 assertTrue(nullPropertyPointer.isByNameAttribute());
 }

 @Test
 public void testCreatingPathWithValueForNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 NodePointer pathWithValue = nullPropertyPointer.createPath(context, "Test Value");
 assertNull(pathWithValue.getValue());
 }

 @Test
 public void testCreatingChildForNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 NodePointer child = nullPropertyPointer.createChild(context, new QName("testChild"));
 assertNull(child.getValue());
 }

 @Test
 public void testCreatingChildWithValueForNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 NodePointer childWithValue = nullPropertyPointer.createChild(context, new QName("testChild"), null, "Test Value");
 assertNull(childWithValue.getValue());
 }

 @Test
 public void testCreatingChildWithIndexForNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 NodePointer childWithIndex = nullPropertyPointer.createChild(context, new QName("testChild"), 1);
 assertNull(childWithIndex.getValue());
 }

 @Test
 public void testCreatingChildWithIndexAndValueForNullPropertyPointer() {
 JXPathContext context = new JXPathContext(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer(new NodePointer(null)));
 NodePointer childWithIndexAndValue = nullPropertyPointer.createChild(context, new QName("testChild"), 1, "Test Value");
 assertNull(childWithIndexAndValue.getValue());
 }

 @Test
 public void testSettingValueOnPropertyOwnerPointer() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new NodePointer(null));
 PropertyPointer propertyPointer = propertyOwnerPointer.getPropertyPointer();
 propertyPointer.setValue("Test Value");
 NodePointer path = propertyOwnerPointer.createPath(new JXPathContext(new NodePointer(null)));
 assertEquals("Test Value", path.getValue());
 }

 @Test
 public void testCreatingPathForPropertyOwnerPointer() {
 PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(new NodePointer(null));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
 NodePointer path = nullPropertyPointer.createPath(new JXPathContext(new NodePointer(null)));
 assertNull(path.getValue());
 }
}