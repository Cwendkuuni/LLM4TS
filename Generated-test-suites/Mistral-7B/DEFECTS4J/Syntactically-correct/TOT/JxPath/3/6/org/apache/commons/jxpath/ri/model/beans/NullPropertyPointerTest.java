import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 PropertyPointer parent = new PropertyOwnerPointer(new NodePointer());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertNull(nullPropertyPointer.getValuePointer().getNode());
 assertNull(nullPropertyPointer.getValue());
 }

 @Test
 public void testSettingValueThroughParentPropertyOwner() {
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer());
 PropertyPointer propertyPointer = parent.getPropertyPointer();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("Test Value");
 assertSame("Test Value", propertyPointer.getValue());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testSettingValueThroughWrongParent() {
 PropertyPointer parent = new NodePointer();
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("Test Value");
 }

 @Test
 public void testCreatingPath() {
 JXPathContext context = new JXPathContext(new NodePointer());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer path = nullPropertyPointer.createPath(context);
 assertNull(path.getNode());
 }

 @Test
 public void testCreatingPathWithValue() {
 JXPathContext context = new JXPathContext(new NodePointer());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer path = nullPropertyPointer.createPath(context, "Test Value");
 assertNull(path.getNode());
 assertSame("Test Value", path.getValue());
 }

 @Test
 public void testCreatingChild() {
 JXPathContext context = new JXPathContext(new NodePointer());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer childPath = nullPropertyPointer.createChild(context, new QName("testChild"), 0);
 assertNull(childPath.getNode());
 }

 @Test
 public void testCreatingChildWithValue() {
 JXPathContext context = new JXPathContext(new NodePointer());
 PropertyOwnerPointer parent = new PropertyOwnerPointer(new NodePointer());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 NodePointer childPath = nullPropertyPointer.createChild(context, new QName("testChild"), 0, "Test Value");
 assertNull(childPath.getNode());
 assertSame("Test Value", childPath.getValue());
 }

 @Test
 public void testSettingNameAttributeValue() {
 PropertyPointer parent = new PropertyOwnerPointer(new NodePointer());
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setNameAttributeValue("TestName");
 assertSame("TestName", nullPropertyPointer.getImmediateParentPointer().asPath() + "[@name='TestName']");
 }
}