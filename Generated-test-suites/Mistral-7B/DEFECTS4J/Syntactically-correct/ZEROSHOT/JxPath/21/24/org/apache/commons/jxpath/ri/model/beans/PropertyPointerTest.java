import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {

 @Test
 public void testPropertyPointer() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 assertEquals(1, propertyPointer.getPropertyCount());
 assertSame("testProperty", propertyPointer.getPropertyNames()[0]);
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testSetPropertyIndex() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 Object bean = new Object();
 propertyPointer.setBean(bean);
 assertEquals(bean, propertyPointer.getBean());
 }

 @Test
 public void testIsActual() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertTrue(propertyPointer.isActual());
 PropertyPointer nonActualPropertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return false;
 }
 };

 assertFalse(nonActualPropertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 propertyPointer.setBean(new Object());
 Object bean = new Object();
 propertyPointer.setValue(bean);
 assertEquals(bean, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 propertyPointer.setBean(new Object()[0]);
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testIsLeaf() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 propertyPointer.setValue(true);
 assertTrue(propertyPointer.isLeaf());
 }

 @Test
 public void testGetLength() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }

 @Override
 public int getPropertyCount() {
 return 5;
 }
 };

 assertEquals(5, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 Object bean = new Object();
 propertyPointer.setValue(bean);
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals("testProperty", immediateValuePointer.getName().getLocalPart());
 assertEquals(bean, immediateValuePointer.getNode());
 }

 @Test
 public void testCreatePath() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 Object bean = new Object();
 NodePointer nodePointer = propertyPointer.createPath(null);
 assertEquals(propertyPointer, nodePointer);
 propertyPointer.setValue(bean);
 assertEquals(bean, nodePointer.getNode());
 }

 @Test
 public void testCreatePathWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 Object bean = new Object();
 NodePointer nodePointer = propertyPointer.createPath(null, new Object());
 assertEquals(propertyPointer, nodePointer);
 propertyPointer.setValue(bean);
 assertEquals(bean, nodePointer.getNode());
 }

 @Test
 public void testCreateChild() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 NodePointer child1 = propertyPointer.createChild(null, null, 0, null);
 assertEquals(propertyPointer, child1.getParent());
 assertEquals("testProperty", child1.getName().getLocalPart());
 assertEquals(0, child1.getIndex());
 assertNull(child1.getNode());

 NodePointer child2 = propertyPointer.createChild(null, null, 1, null);
 assertEquals(propertyPointer, child2.getParent());
 assertEquals("testProperty", child2.getName().getLocalPart());
 assertEquals(1, child2.getIndex());
 assertNull(child2.getNode());
 }

 @Test
 public void testCreateChildWithValue() {
 PropertyPointer propertyPointer = new PropertyPointer(null) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 NodePointer child1 = propertyPointer.createChild(null, null, 0, null);
 assertEquals(propertyPointer, child1.getParent());
 assertEquals("testProperty", child1.getName().getLocalPart());
 assertEquals(0, child1.getIndex());
 assertNull(child1.getNode());

 Object bean = new Object();
 NodePointer child2 = propertyPointer.createChild(null, null, 1, bean);
 assertEquals(propertyPointer, child2.getParent());
 assertEquals("testProperty", child2.getName().getLocalPart());
 assertEquals(1, child2.getIndex());
 assertEquals(bean, child2.getNode());
 }
}