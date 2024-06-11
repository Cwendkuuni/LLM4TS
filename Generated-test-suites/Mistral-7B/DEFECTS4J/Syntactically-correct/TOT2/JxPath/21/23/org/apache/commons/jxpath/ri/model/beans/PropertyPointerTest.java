import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 @Test
 public void testConstructor() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 assertNull(propertyPointer.getBean());
 assertNull(propertyPointer.getName());
 assertEquals(-1, propertyPointer.getPropertyIndex());
 assertEquals(-1, propertyPointer.getIndex());
 }

 @Test
 public void testGetBean() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 Object bean = new Object();
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test(expected = NullPointerException.class)
 public void testGetBeanWhenNullParent() {
 PropertyPointer propertyPointer = new PropertyPointer(null);
 propertyPointer.setBean(new Object());
 }

 @Test
 public void testGetPropertyIndex() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 propertyPointer.setPropertyIndex(1);
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 propertyPointer.setPropertyIndex(1);
 propertyPointer.setPropertyIndex(2);
 assertEquals(2, propertyPointer.getPropertyIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexWhenNegative() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 propertyPointer.setPropertyIndex(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPropertyIndexWhenGreaterThanPropertyCount() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }
 };
 propertyPointer.setPropertyIndex(2);
 }

 @Test
 public void testGetName() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testSetName() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 propertyPointer.setPropertyName("newTestProperty");
 assertEquals("newTestProperty", propertyPointer.getName().getLocalPart());
 }

 @Test(expected = NullPointerException.class)
 public void testSetNameWhenNull() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 propertyPointer.setPropertyName(null);
 }

 @Test
 public void testIsActualProperty() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testIsActualWhenFalse() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 protected boolean isActualProperty() {
 return false;
 }
 };
 assertFalse(propertyPointer.isActual());
 }

 @Test
 public void testGetBaseValue() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public Object getBaseValue() {
 return new Object();
 }
 };
 assertSame(propertyPointer.getBaseValue(), propertyPointer.getBean());
 }

 @Test
 public void testGetImmediateNode() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public Object getBaseValue() {
 return new Object();
 }
 };
 assertSame(new Object(), propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public Object getBaseValue() {
 return new Object()[0];
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public Object getBaseValue() {
 return new String("test");
 }
 };
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 @Override
 public Object getBaseValue() {
 return new Object()[3];
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testCreatePath() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = new JXPathContext();
 context.setContextItem(new Object());
 NodePointer childNodePointer = propertyPointer.createPath(context);
 assertEquals(propertyPointer.getName().getLocalPart(), childNodePointer.getName().getLocalPart());
 assertEquals(propertyPointer, childNodePointer.getParent());
 }

 @Test
 public void testCreatePathWithValue() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = new JXPathContext();
 context.setContextItem(new Object());
 NodePointer childNodePointer = propertyPointer.createPath(context, new Object());
 assertEquals(propertyPointer.getName().getLocalPart(), childNodePointer.getName().getLocalPart());
 assertEquals(propertyPointer, childNodePointer.getParent());
 }

 @Test
 public void testCreateChild() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = new JXPathContext();
 context.setContextItem(new Object());
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, new Object());
 assertEquals(propertyPointer.getName().getLocalPart(), childNodePointer.getName().getLocalPart());
 assertEquals(propertyPointer, childNodePointer.getParent());
 }

 @Test
 public void testCreateChildWithName() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = new JXPathContext();
 context.setContextItem(new Object());
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("testChild"), 0, new Object());
 assertEquals("testChild", childNodePointer.getName().getLocalPart());
 assertEquals(propertyPointer, childNodePointer.getParent());
 }

 @Test
 public void testCreateChildWithNameAndIndex() {
 AbstractPropertyPointer propertyPointer = new AbstractPropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };
 JXPathContext context = new JXPathContext();
 context.setContextItem(new Object());
 NodePointer childNodePointer = propertyPointer.createChild(context, new QName("testChild"), 1, new Object());
 assertEquals("testChild", childNodePointer.getName().getLocalPart());
 assertEquals(propertyPointer, childNodePointer.getParent());
 }
}