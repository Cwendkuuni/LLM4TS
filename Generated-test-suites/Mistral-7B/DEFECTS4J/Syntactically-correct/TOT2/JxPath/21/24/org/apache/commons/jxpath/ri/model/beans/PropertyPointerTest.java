
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertEquals(parent, propertyPointer.getImmediateParentPointer());
    }

    // Test methods that return booleans
    @Test
    public void testIsActual() {
        PropertyPointer propertyPointer = new PropertyPointerStub(true);
        assertTrue(propertyPointer.isActual());
        PropertyPointer propertyPointer2 = new PropertyPointerStub(false);
        assertFalse(propertyPointer2.isActual());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        assertTrue(propertyPointer.isCollection());
        PropertyPointer propertyPointer2 = new PropertyPointerStub(1);
        assertFalse(propertyPointer2.isCollection());
    }

    // Test methods that return ints
    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointerStub(-1);
        assertEquals(-1, propertyPointer.getPropertyIndex());
        PropertyPointer propertyPointer2 = new PropertyPointerStub(5);
        assertEquals(5, propertyPointer2.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new int[]{1, 2, 3});
        assertEquals(3, propertyPointer.getLength());
        PropertyPointer propertyPointer2 = new PropertyPointerStub(1);
        assertEquals(-1, propertyPointer2.getLength());
    }

    // Test methods that return Objects
    @Test
    public void testGetBean() {
        Object bean = new Object();
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(), bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullBean() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(), null);
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        assertNull(propertyPointer.getImmediateNode());
        PropertyPointer propertyPointer2 = new PropertyPointerStub(1);
        assertEquals(1, propertyPointer2.getImmediateNode().intValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsIndex() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        propertyPointer.setIndex(3);
    }

    // Test methods that return QName
    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointerStub("testProperty");
        assertEquals("testProperty", propertyPointer.getName().getLocalPart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyName() {
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(), "", null);
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        assertNotNull(propertyPointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer2 = new PropertyPointerStub(5);
        assertEquals(propertyPointer2, propertyPointer.createPath(null, 5));
    }

    @Test(expected = NullPointerException.class)
    public void testNullContext() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        propertyPointer.createPath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        propertyPointer.createPath(null, null);
    }

    // Test methods that return NodePointer
    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        assertEquals(propertyPointer, propertyPointer.getImmediateValuePointer());
    }

    // Test methods that create a new NodePointer
    @Test
    public void testCreateChildWithNullName() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        assertNull(propertyPointer.createChild(null, null, 0, null));
    }

    @Test(expected = NullPointerException.class)
    public void testNullContextForCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        propertyPointer.createChild(null, null, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValueForCreateChild() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        propertyPointer.createChild(null, null, 0, null);
    }

    @Test
    public void testCreateChildWithIndexOutOfBounds() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        propertyPointer.createChild(null, null, 3, null);
    }

    @Test
    public void testCreateChildWithValidNameAndIndex() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer2 = new PropertyPointerStub("testProperty");
        assertEquals(propertyPointer2, propertyPointer.createChild(null, new QName("testNameSpace", "testProperty"), 0, null));
    }

    @Test
    public void testCreateChildWithValidName() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer2 = new PropertyPointerStub("testProperty");
        assertEquals(propertyPointer2, propertyPointer.createChild(null, new QName("testNameSpace", "testProperty"), 0));
    }

    @Test
    public void testCreateChildWithValidNameAndValue() {
        PropertyPointer propertyPointer = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer2 = new PropertyPointerStub("testProperty");
        Object value = new Object();
        assertEquals(propertyPointer2, propertyPointer.createChild(null, new QName("testNameSpace", "testProperty"), 0, value));
    }

    // Test methods that override Object's methods
    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer1 = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer2 = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer3 = new PropertyPointerStub(new Object[]{1, 2, 4});
        assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        assertNotEquals(propertyPointer1.hashCode(), propertyPointer3.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer2 = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer3 = new PropertyPointerStub(new Object[]{1, 2, 4});
        PropertyPointer propertyPointer4 = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer5 = new PropertyPointerStub(new Object[]{1, 2, 3}, null);
        PropertyPointer propertyPointer6 = new PropertyPointerStub(new Object[]{1, 2, 3});
        assertTrue(propertyPointer1.equals(propertyPointer1));
        assertTrue(propertyPointer1.equals(propertyPointer2));
        assertFalse(propertyPointer1.equals(propertyPointer3));
        assertFalse(propertyPointer1.equals(propertyPointer4));
        assertFalse(propertyPointer1.equals(propertyPointer5));
        assertFalse(propertyPointer1.equals(propertyPointer6));
        assertFalse(propertyPointer1.equals(new Object()));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer1 = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer2 = new PropertyPointerStub(new Object[]{1, 2, 3});
        PropertyPointer propertyPointer3 = new PropertyPointerStub(new Object[]{1, 2, 4});
        int comparisonResult = propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer3);
        assertEquals(0, comparisonResult);
        comparisonResult = propertyPointer1.compareChildNodePointers(propertyPointer2, propertyPointer1);
        assertLessThan(0, comparisonResult);
        comparisonResult = propertyPointer1.compareChildNodePointers(propertyPointer3, propertyPointer2);
        assertGreaterThan(0, comparisonResult);
    }

}

// Helper class to create PropertyPointer objects for testing
class PropertyPointerStub extends PropertyPointer {
    private Object[] properties;
    private String propertyName;

    public PropertyPointerStub(Object... properties) {
        super(new NodePointer());
        this.properties = properties;
    }

    public PropertyPointerStub(String propertyName) {
        super(new NodePointer());
        this.propertyName = propertyName;
    }

    @Override
    public int getPropertyIndex() {
        return indexOfProperty(properties, propertyName);
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public int getPropertyCount() {
        return properties.length;
    }

    @Override
    public String[] getPropertyNames() {
        String[] propertyNames = new String[properties.length];
        for (int i = 0; i < properties.length; i++) {
            propertyNames[i] = Integer.toString(i);
        }
        return propertyNames;
    }

    @Override
    public boolean isActualProperty() {
        return true;
    }

    @Override
    public Object getBaseValue() {
        return properties;
    }

    private int indexOfProperty(Object[] array, String propertyName) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] instanceof Map) {
                Map map = (Map) array[i];
                if (map.containsKey(propertyName)) {
                    return i;
                }
            } else if (propertyName.equals(Integer.toString(i))) {
                return i;
            }
        }
        return -1;
    }
}