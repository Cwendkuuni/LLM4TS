import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertNull(propertyPointer.getBean());
        assertNull(propertyPointer.getPropertyName());
        assertFalse(propertyPointer.isActual());
        assertEquals(WHOLE_COLLECTION, propertyPointer.getIndex());
        assertNull(propertyPointer.getNode());
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(2);
        assertEquals(2, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPropertyIndexWithSameValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", propertyPointer.getName().getLocalPart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyNameWithNull() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyName(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPropertyNameWithSameValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setPropertyName("testProperty");
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyCount(3);
        assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPropertyCountWithNegativeValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyCount(-1);
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        String[] propertyNames = new String[]{"testProperty1", "testProperty2"};
        propertyPointer.setPropertyNames(propertyNames);
        assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPropertyNamesWithNull() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        String[] propertyNames = null;
        propertyPointer.setPropertyNames(propertyNames);
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setIndex(1);
        Object node = new Object();
        propertyPointer.setValue(node);
        assertEquals(node, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object collection = new Object()[3];
        propertyPointer.setBaseValue(collection);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object leaf = new String("test");
        propertyPointer.setValue(leaf);
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object collection = new Object[4];
        propertyPointer.setBaseValue(collection);
        assertEquals(4, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object node = new Object();
        propertyPointer.setValue(node);
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(node, immediateValuePointer.getNode());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        NodePointer nodePointer = propertyPointer.createPath(null);
        assertEquals(propertyName, nodePointer.getName().getLocalPart());
        assertEquals(bean, nodePointer.getNode());
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        Object value = new Object();
        NodePointer nodePointer = propertyPointer.createPath(null, value);
        assertEquals(propertyName, nodePointer.getName().getLocalPart());
        assertEquals(value, nodePointer.getNode());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        NodePointer nodePointer1 = propertyPointer.createChild(null, null, 0, null);
        NodePointer nodePointer2 = propertyPointer.createChild(null, null, 1, null);
        assertEquals(propertyName, nodePointer1.getName().getLocalPart());
        assertEquals(bean, nodePointer1.getNode());
        assertEquals(propertyName, nodePointer2.getName().getLocalPart());
        assertEquals(bean, nodePointer2.getNode());
    }

    @Test
    public void testCreateChildWithName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        String childPropertyName = "testChildProperty";
        NodePointer nodePointer = propertyPointer.createChild(null, new QName(null, childPropertyName), 0, null);
        assertEquals(childPropertyName, nodePointer.getName().getLocalPart());
        assertEquals(bean, nodePointer.getNode());
    }
}