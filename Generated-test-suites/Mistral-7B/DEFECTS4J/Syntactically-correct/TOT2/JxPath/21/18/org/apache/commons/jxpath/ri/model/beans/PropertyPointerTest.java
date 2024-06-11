import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerConstructor() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertNull(propertyPointer.getBean());
        assertNull(propertyPointer.getPropertyName());
        assertFalse(propertyPointer.isActual());
        assertEquals(-1, propertyPointer.getIndex());
        assertEquals(-1, propertyPointer.getPropertyIndex());
        assertNotNull(propertyPointer.getValue());
        assertEquals(UNINITIALIZED, propertyPointer.getValue());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexNegative() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
        assertEquals(-1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", propertyPointer.getPropertyName());
    }

    @Test(expected = NullPointerException.class)
    public void testSetPropertyNameNull() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyName(null);
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyCount(3);
        assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyCountNegative() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyCount(-1);
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = {"testProperty1", "testProperty2", "testProperty3"};
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyNames(propertyNames);
        assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
    }

    @Test(expected = NullPointerException.class)
    public void testSetPropertyNamesNull() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyNames(null);
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        assertFalse(propertyPointer.isActualProperty());
        PropertyPointer actualProperty = new ActualPropertyPointer(null);
        assertTrue(actualProperty.isActualProperty());
    }

    @Test
    public void testIsActual() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        assertFalse(propertyPointer.isActual());
        PropertyPointer actualProperty = new ActualPropertyPointer(null);
        assertTrue(actualProperty.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setValue(new String("test"));
        assertEquals("test", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBaseValue(new String[]{"test1", "test2"});
        assertTrue(propertyPointer.isCollection());
        propertyPointer.setBaseValue(new String("test"));
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setValue(new String("test"));
        assertFalse(propertyPointer.isLeaf());
        propertyPointer.setValue(42);
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBaseValue(new String[]{"test1", "test2"});
        assertEquals(2, propertyPointer.getLength());
        propertyPointer.setBaseValue(new String("test"));
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setValue(new String("test"));
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals("test", immediateValuePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.createPath(null);
        assertEquals(bean, propertyPointer.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathNullBean() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBean(null);
        propertyPointer.createPath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathNullPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBean(new Object());
        propertyPointer.createPath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathNullValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.createPath(null, null);
    }

    @Test
    public void testCreatePathValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.createPath(null, new String("test"));
        assertEquals("test", propertyPointer.getNode());
    }

    @Test
    public void testCreatePathCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.createPath(null, new String[]{"test1", "test2"});
        assertEquals(bean, propertyPointer.getNode());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        propertyPointer.setPropertyName("testProperty");
        NodePointer child = propertyPointer.createChild(null, null, 0, "testChild");
        assertEquals("testChild", child.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildNullBean() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.createChild(null, null, 0, "testChild");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildNullPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBean(new Object());
        propertyPointer.createChild(null, null, 0, "testChild");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildNullName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.createChild(null, null, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildNegativeIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setBean(new Object());
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.createChild(null, null, -1, "testChild");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCreateChildOutOfBounds() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        propertyPointer.setPropertyName("testProperty");
        String[] propertyNames = {"testProperty1", "testProperty2"};
        propertyPointer.setPropertyNames(propertyNames);
        propertyPointer.createChild(null, null, propertyNames.length, "testChild");
    }
}