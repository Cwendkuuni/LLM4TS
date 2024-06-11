import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private PropertyPointer propertyPointer;
 private NodePointer parentPointer;

 @Test
 public void testPropertyPointerInstance() {
 propertyPointer = new PropertyPointer(parentPointer) {
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

 assertTrue(propertyPointer instanceof PropertyPointer);
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 assertEquals(-1, propertyPointer.getPropertyIndex());

 propertyPointer.setPropertyIndex(5);
 assertEquals(5, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBean() {
 return new Object();
 }
 };

 assertEquals(null, propertyPointer.getBean());

 propertyPointer.setBean(new Object());
 assertEquals(propertyPointer.getBean(), propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public String getPropertyName() {
 return "testProperty";
 }
 };

 assertEquals("testProperty", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testIsActual() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public boolean isActualProperty() {
 return false;
 }
 };

 assertFalse(propertyPointer.isActual());

 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public boolean isActualProperty() {
 return true;
 }
 };

 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new String[]{"testValue"};
 }
 };

 assertEquals("testValue", propertyPointer.getImmediateNode().asString());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2"};
 }
 };

 assertTrue(propertyPointer.isCollection());

 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new Object();
 }
 };

 assertFalse(propertyPointer.isCollection());
 }

 @Test
 public void testGetLength() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new String[]{"testValue1", "testValue2", "testValue3"};
 }
 };

 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testCreatePath() {
 JXPathContext context = new JXPathContext();
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new Object();
 }
 };

 propertyPointer.createPath(context);
 assertEquals(propertyPointer, context.getCurrentNode());
 }

 @Test
 public void testCreatePathWithValue() {
 JXPathContext context = new JXPathContext();
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new Object();
 }
 };

 propertyPointer.createPath(context, new Object());
 assertEquals(propertyPointer, context.getCurrentNode());
 }

 @Test
 public void testCreateChildNodePointer() {
 JXPathContext context = new JXPathContext();
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new Object();
 }
 };

 NodePointer childPointer = propertyPointer.createChild(context, null, 0, new Object());
 assertEquals(childPointer, context.getCurrentNode());
 }

 @Test
 public void testCreateChildNodePointerWithName() {
 JXPathContext context = new JXPathContext();
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new Object();
 }
 };

 NodePointer childPointer = propertyPointer.createChild(context, new QName("testName"), 0, new Object());
 assertEquals("testName", childPointer.getName().getLocalPart());
 assertEquals(childPointer, context.getCurrentNode());
 }

 @Test
 public void testCreateChildNodePointerWithIndex() {
 JXPathContext context = new JXPathContext();
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public int length() {
 return 3;
 }
 };
 }
 };

 NodePointer childPointer = propertyPointer.createChild(context, null, 1, null);
 assertEquals(1, childPointer.getIndex());
 assertEquals(childPointer, context.getCurrentNode());
 }

 @Test
 public void testHashCode() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 NodePointer parentPointer1 = new NodePointer() {};
 NodePointer parentPointer2 = new NodePointer() {};
 PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer1) {
 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer2) {
 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 assertEquals(propertyPointer.hashCode(), propertyPointer1.hashCode());
 assertEquals(propertyPointer.hashCode(), propertyPointer2.hashCode());

 propertyPointer.setIndex(5);
 assertEquals(propertyPointer.hashCode(), propertyPointer1.hashCode());
 assertEquals(propertyPointer.hashCode(), propertyPointer2.hashCode());

 propertyPointer1.setIndex(10);
 assertNotEquals(propertyPointer.hashCode(), propertyPointer1.hashCode());
 assertNotEquals(propertyPointer.hashCode(), propertyPointer2.hashCode());
 }

 @Test
 public void testEquals() {
 propertyPointer = new PropertyPointer(parentPointer) {
 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 NodePointer parentPointer1 = new NodePointer() {};
 NodePointer parentPointer2 = new NodePointer() {};
 PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer1) {
 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer2) {
 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer) {
 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };

 PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer) {
 @Override
 public int getPropertyIndex() {
 return 0;
 }
 };

 assertEquals(propertyPointer, propertyPointer);
 assertEquals(propertyPointer, propertyPointer1);
 assertEquals(propertyPointer, propertyPointer2);

 assertNotEquals(propertyPointer, propertyPointer3);
 assertNotEquals(propertyPointer, propertyPointer4);
 assertNotEquals(propertyPointer, new Object());
 }

 @Test
 public void testCompareChildNodePointers() {
 NodePointer nodePointer1 = new NodePointer() {};
 NodePointer nodePointer2 = new NodePointer() {};
 PropertyPointer propertyPointer1 = new PropertyPointer(new NodePointer() {}) {
 @Override
 public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
 return nodePointer1.compareTo(pointer1) - nodePointer1.compareTo(pointer2);
 }
 };
 PropertyPointer propertyPointer2 = new PropertyPointer(new NodePointer() {}) {
 @Override
 public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
 return nodePointer2.compareTo(pointer1) - nodePointer2.compareTo(pointer2);
 }
 };

 assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer1));
 assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2));
 assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer2, nodePointer1));
 assertEquals(0, propertyPointer1.compareChildNodePointers(nodePointer2, nodePointer2));

 assertLessThan(0, propertyPointer1.compareChildNodePointers(nodePointer1, nodePointer2));
 assertLessThan(0, propertyPointer1.compareChildNodePointers(nodePointer2, nodePointer1));
 }
}