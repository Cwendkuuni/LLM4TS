import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.AbstractPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private NodePointer parentPointer;
 private PropertyPointer propertyPointer;

 @Test
 public void testPropertyPointerConstruction() {
 parentPointer = new NodePointer(null);
 propertyPointer = new PropertyPointer(parentPointer);
 assertNull(propertyPointer.getPropertyName());
 assertEquals(-1, propertyPointer.getPropertyIndex());
 assertNull(propertyPointer.getBean());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPropertyPointerNullParent() {
 propertyPointer = new PropertyPointer(null);
 }

 @Test
 public void testGetPropertyIndex() {
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public int getPropertyCount() {
 return 3;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty1", "testProperty2", "testProperty3"};
 }

 @Override
 public String getPropertyName() {
 return "testPropertyName";
 }

 @Override
 public int getPropertyIndex() {
 return 1;
 }
 };
 assertEquals(1, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testSetPropertyIndex() {
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public int getPropertyCount() {
 return 3;
 }

 @Override
 public String[] getPropertyNames() {
 return new String[]{"testProperty1", "testProperty2", "testProperty3"};
 }

 @Override
 public String getPropertyName() {
 return "testPropertyName";
 }
 };
 propertyPointer.setPropertyIndex(2);
 assertEquals(2, propertyPointer.getPropertyIndex());
 }

 @Test
 public void testGetBean() {
 Object testObject = new Object() {
 };
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public Object getBaseValue() {
 return testObject;
 }
 };
 assertEquals(testObject, propertyPointer.getBean());
 }

 @Test
 public void testGetName() {
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public String getPropertyName() {
 return "testPropertyName";
 }
 };
 assertEquals("testPropertyName", propertyPointer.getName().getLocalPart());
 }

 @Test
 public void testIsActualProperty() {
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 protected boolean isActualProperty() {
 return true;
 }
 };
 assertTrue(propertyPointer.isActual());
 }

 @Test
 public void testGetImmediateNode() {
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public int value() {
 return 42;
 }
 };
 }
 };
 assertEquals(42, propertyPointer.getImmediateNode());
 }

 @Test
 public void testIsCollection() {
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public boolean isCollection() {
 return true;
 }
 };
 }
 };
 assertTrue(propertyPointer.isCollection());
 }

 @Test
 public void testGetLength() {
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public Object getBaseValue() {
 return new Object[] {1, 2, 3};
 }

 @Override
 public int getLength() {
 return 3;
 }
 };
 assertEquals(3, propertyPointer.getLength());
 }

 @Test
 public void testGetImmediateValuePointer() {
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public Object getBaseValue() {
 return new Object() {
 @Override
 public int value() {
 return 42;
 }
 };
 }
 };
 NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
 assertEquals(propertyPointer, valuePointer.getParentPointer());
 assertEquals("testPropertyName", valuePointer.getName().getLocalPart());
 assertEquals(42, valuePointer.getNode().value());
 }

 @Test
 public void testCreatePath() {
 Object testObject = new Object() {
 };
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public Object getBaseValue() {
 return testObject;
 }
 };
 JXPathContext context = new JXPathContext(testObject);
 propertyPointer.createPath(context);
 assertEquals(testObject, propertyPointer.getNode().getNode());
 }

 @Test
 public void testCreatePathWithValue() {
 Object testObject = new Object() {
 int testValue = 42;
 };
 propertyPointer = new AbstractPropertyPointer("testPropertyName") {
 @Override
 public Object getBaseValue() {
 return testObject;
 }
 };
 JXPathContext context = new JXPathContext(testObject);
 propertyPointer.createPath(context, 1);
 assertEquals(1, ((Number) propertyPointer.getNode().getNode()).intValue());
 }
}