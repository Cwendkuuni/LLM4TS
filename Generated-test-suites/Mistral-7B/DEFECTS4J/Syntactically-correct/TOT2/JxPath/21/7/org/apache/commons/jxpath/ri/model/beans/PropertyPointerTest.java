import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.SimpleBean;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointerConstructor() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new SimpleBean())) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 assertEquals("name", propertyPointer.getPropertyName());
 }

 @Test
 public void testGetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new SimpleBean())) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 SimpleBean bean = new SimpleBean();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexWithInvalidIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new SimpleBean())) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetName() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new SimpleBean())) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 assertEquals(new QName(null, "name"), propertyPointer.getName());
 }

 @Test
 public void testIsActualProperty() {
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new SimpleBean())) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testGetImmediateNodeWithValue() {
 SimpleBean bean = new SimpleBean();
 bean.setName("Test");
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 assertEquals("Test", propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 SimpleBean bean = new SimpleBean();
 bean.setList(new String[]{"Test1", "Test2"});
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "list";
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 SimpleBean bean = new SimpleBean();
 bean.setName("Test");
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 assertFalse(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLengthWithCollection() {
 SimpleBean bean = new SimpleBean();
 bean.setList(new String[]{"Test1", "Test2", "Test3"});
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "list";
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 SimpleBean bean = new SimpleBean();
 bean.setName("Test");
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "Test";
 }
 }.getName(), immediateValuePointer.getName());
 }

 @Test
 public void testCreatePathWithContext() {
 SimpleBean bean = new SimpleBean();
 bean.setName("Test");
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 JXPathContext context = new JXPathContext(bean);
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertEquals(new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "Test";
 }
 }.getName(), nodePointer.getName());
 }

 @Test
 public void testCreatePathWithContextAndValue() {
 SimpleBean bean = new SimpleBean();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 JXPathContext context = new JXPathContext(bean);
 propertyPointer.createPath(context, "New Value");
 assertEquals("New Value", propertyPointer.getImmediateNode());
 }

 @Test
 public void testCreateChildWithContext() {
 SimpleBean bean = new SimpleBean();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 JXPathContext context = new JXPathContext(bean);
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, null);
 assertEquals(new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "New Property";
 }
 }.getName(), childNodePointer.getName());
 }

 @Test
 public void testCreateChildWithContextAndName() {
 SimpleBean bean = new SimpleBean();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 JXPathContext context = new JXPathContext(bean);
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("", "newProperty"), 0, null);
 assertEquals(new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "newProperty";
 }
 }.getName(), childNodePointer.getName());
 }

 @Test
 public void testCreateChildWithContextAndNameAndValue() {
 SimpleBean bean = new SimpleBean();
 PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(bean)) {
 @Override
 public String getPropertyName() {
 return "name";
 }
 };
 JXPathContext context = new JXPathContext(bean);
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("", "newProperty"), 0, "New Value");
 assertEquals("New Value", childNodePointer.getNode().getValue());
 }
}