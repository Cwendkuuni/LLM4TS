import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private JXPathContext context;
 private PropertyPointer propertyPointer;

 @Before
 public void setUp() {
 context = new JXPathContext(new MyBean(), null, null);
 propertyPointer = new MyPropertyPointer(context.getBasePointer());
 }

 @Test
 public void testGetPropertyIndex() {
 assertEquals(-1, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 MyBean bean = (MyBean) propertyPointer.getBean();
 assertEquals("John Doe", bean.getName());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetBeanWhenPropertyPointerIsNotSet() {
 propertyPointer.getBean();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testGetName() {
 assertEquals("age", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testGetPropertyName() {
 assertEquals("age", propertyPointer.getPropertyName());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPropertyName() {
 propertyPointer.setPropertyName("newName");
 fail("Expected UnsupportedOperationException");
 }

 @Test
 public void testGetPropertyCount() {
 assertEquals(2, propertyPointer.getPropertyCount());
 }

 @Test
 public void testGetPropertyNames() {
 String[] propertyNames = propertyPointer.getPropertyNames();
 assertEquals(2, propertyNames.length);
 assertEquals("name", propertyNames[0]);
 assertEquals("age", propertyNames[1]);
 }

 @Test
 public void testIsActualProperty() {
 assertTrue(propertyPointer.isActualProperty());
 }

 @Test
 public void testIsActual() {
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 assertEquals(28, propertyPointer.getImmediateNode().hashCode());
 }

 @Test
 public void testIsCollection() {
 assertFalse(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer valuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(3, valuePointer.getIndex());
 assertEquals("John Doe", valuePointer.getNode().toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetImmediateValuePointerWhenPropertyPointerIsNotSet() {
 propertyPointer.getImmediateValuePointer();
 fail("Expected IllegalStateException");
 }

 @Test
 public void testCreatePath() {
 NodePointer nodePointer = propertyPointer.createPath(context);
 assertEquals("age", nodePointer.getName().getLocalPart());
 }

 @Test(expected = NullPointerException.class)
 public void testCreatePathWhenContextIsNull() {
 propertyPointer.createPath(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatePathWithInvalidIndex() {
 propertyPointer.createPath(context, 5);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testCreatePathWithValidIndex() {
 NodePointer nodePointer = propertyPointer.createPath(context, 1);
 assertEquals("John Doe", nodePointer.getNode().toString());
 }

 @Test
 public void testCreateChild() {
 PropertyPointer childPropertyPointer = (PropertyPointer) propertyPointer.createChild(context, null, 1, "New Value");
 assertEquals("John Doe", childPropertyPointer.getBean().getName());
 assertEquals("New Value", childPropertyPointer.getNode().toString());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildWhenContextIsNull() {
 propertyPointer.createChild(null, null, 0, "New Value");
 fail("Expected NullPointerException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithInvalidIndex() {
 propertyPointer.createChild(context, null, 5, "New Value");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testCreateChildWithNullName() {
 propertyPointer.createChild(context, null, 0, "New Value");
 fail("Expected NullPointerException");
 }

 @Test
 public void testCreateChildWithValidNameAndIndex() {
 PropertyPointer childPropertyPointer = (PropertyPointer) propertyPointer.createChild(context, new QName("my:newName"), 0, "New Value");
 assertEquals("John Doe", childPropertyPointer.getBean().getName());
 assertEquals("New Value", childPropertyPointer.getNode().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateChildWithDuplicateName() {
 propertyPointer.createChild(context, new QName("my:newName"), 0, "New Value");
 propertyPointer.createChild(context, new QName("my:newName"), 0, "Another Value");
 fail("Expected IllegalArgumentException");
 }

}

class MyBean {
 private String name;
 private int age;

 public MyBean() {
 }

 public MyBean(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }
}

class MyPropertyPointer extends PropertyPointer {
 protected MyPropertyPointer(NodePointer parent) {
 super(parent);
 }

 @Override
 public String getPropertyName() {
 return "name";
 }
}