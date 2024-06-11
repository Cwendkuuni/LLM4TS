import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {

 private JXPathContext context;
 private PropertyPointer propertyPointer;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 context = new JXPathContext(new MyTestBean(), "/");
 propertyPointer = new PropertyPointer(context.getDocumentNode()) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexWithNegativeNumber() {
 propertyPointer.setPropertyIndex(-1);
 }

 @Test
 public void testGetBean() {
 Object bean = new MyTestBean();
 propertyPointer.setBean(bean);
 assertSame(bean, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 assertEquals(new QName("", "testProperty"), propertyPointer.getName());
 }

 @Test
 public void testIsActual() {
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 String expected = "testValue";
 propertyPointer.setValue("testValue");
 assertEquals(expected, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 Object collection = new Object[] {"test1", "test2", "test3"};
 propertyPointer.setBaseValue(collection);
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 String leaf = "testValue";
 propertyPointer.setValue(leaf);
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 Object collection = new Object[] {"test1", "test2", "test3"};
 propertyPointer.setBaseValue(collection);
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testCreatePath() {
 String expected = "testValue";
 propertyPointer.setValue(expected);
 NodePointer expectedNodePointer = NodePointer.newChildNodePointer(
 context.getDocumentNode(), new QName("", "testProperty"), expected);
 assertEquals(expectedNodePointer, propertyPointer.createPath(context));
 }

 @Test
 public void testCreatePathWithValue() {
 String expected = "testValue";
 NodePointer expectedNodePointer = NodePointer.newChildNodePointer(
 context.getDocumentNode(), new QName("", "testProperty"), expected);
 propertyPointer.createPath(context, expected);
 assertEquals(expectedNodePointer, propertyPointer);
 }

 @Test
 public void testCreateChild() {
 String propertyName = "testChildProperty";
 NodePointer expectedNodePointer = NodePointer.newChildNodePointer(
 context.getDocumentNode(), new QName("", propertyName), 0, "testChildValue");
 PropertyPointer childPropertyPointer = propertyPointer.createChild(context, new QName(null, propertyName), 0, "testChildValue");
 assertEquals(expectedNodePointer, childPropertyPointer);
 }

 @Test
 public void testCreateChildWithNullName() {
 PropertyPointer childPropertyPointer = propertyPointer.createChild(context, null, 0);
 assertNull(childPropertyPointer);
 }

 @Test
 public void testHashCode() {
 PropertyPointer otherPropertyPointer = new PropertyPointer(context.getDocumentNode()) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 otherPropertyPointer.setIndex(1);
 otherPropertyPointer.setParent(propertyPointer);
 int thisHashCode = propertyPointer.hashCode();
 int otherHashCode = otherPropertyPointer.hashCode();
 assertEquals(thisHashCode, otherHashCode);
 }

 @Test
 public void testEquals() {
 PropertyPointer otherPropertyPointer = new PropertyPointer(context.getDocumentNode()) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 otherPropertyPointer.setIndex(1);
 otherPropertyPointer.setParent(propertyPointer);
 assertTrue(propertyPointer.equals(otherPropertyPointer));
 }

 @Test
 public void testCreateChildWithNullParent() {
 PropertyPointer childPropertyPointer = new PropertyPointer(context.getDocumentNode()) {
 @Override
 public String getPropertyName() {
 return "testChildProperty";
 }
 };
 assertFalse(childPropertyPointer.createChild(context, null, 0) instanceof PropertyPointer);
 }
}

class MyTestBean {
 private String testProperty;
 private String[] testArrayProperty;
}