import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private JXPathContext context;

 @Test
 public void testPropertyPointer() {
 // Test constructor
 propertyPointer = new PropertyPointer(null) {
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
 // Test getPropertyIndex
 assertEquals(-1, propertyPointer.getPropertyIndex());
 propertyPointer.setPropertyIndex(0);
 assertEquals(0, propertyPointer.getPropertyIndex());

 // Test getBean
 propertyPointer.setBean(new Object());
 assertEquals(propertyPointer.getBean(), propertyPointer.getBean());

 // Test getName
 assertEquals("testProperty", propertyPointer.getName().getLocalPart());

 // Test getLength
 propertyPointer.setValue(new Object[]{1, 2, 3});
 assertEquals(3, propertyPointer.getLength());

 // Test isCollection
 assertTrue(propertyPointer.isCollection());

 // Test isLeaf
 propertyPointer.setValue(1);
 assertFalse(propertyPointer.isCollection());

 // Test getImmediateNode
 propertyPointer.setValue("test");
 assertEquals("test", propertyPointer.getImmediateNode());

 // Test getImmediateValuePointer
 propertyPointer.setValue(new Object[]{1, 2, 3});
 NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(2, immediateValuePointer.getNode().hashCode());

 // Test createPath
 context = new JXPathContext(new Object[]{new Object[]{1, 2, 3}});
 propertyPointer.createPath(context);
 assertEquals(propertyPointer.hashCode(), context.getCurrentNode().hashCode());

 // Test createPath with value
 Object value = new Object();
 propertyPointer.createPath(context, value);
 assertEquals(value.hashCode(), context.getCurrentNode().getNode().hashCode());

 // Test createChild
 PropertyPointer childPropertyPointer = new PropertyPointer(propertyPointer) {
 @Override
 public String getPropertyName() {
 return "testChildProperty";
 }

 @Override
 public int getPropertyCount() {
 return 1;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testChildProperty"};
 }

 @Override
 public boolean isActualProperty() {
 return true;
 }
 };
 NodePointer childNodePointer = propertyPointer.createChild(context, null, 0, value);
 assertEquals(childPropertyPointer.hashCode(), childNodePointer.getParent().hashCode());
 assertEquals("testChildProperty", childNodePointer.getName().getLocalPart());
 }
}