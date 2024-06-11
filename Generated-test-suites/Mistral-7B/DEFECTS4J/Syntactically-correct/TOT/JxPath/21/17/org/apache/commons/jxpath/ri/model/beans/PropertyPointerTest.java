package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.jxpath.ri.model.beans.PropertyPointer.UNSPECIFIED_PROPERTY;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private NodePointer parentPointer;
    private JXPathContext context;

    @Before
    public void setup() {
        parentPointer = Mockito.mock(NodePointer.class);
        context = Mockito.mock(JXPathContext.class);
        propertyPointer = new PropertyPointer(parentPointer);
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(-1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setPropertyIndex(5);
        assertEquals(-1, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexNegative() {
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        String propertyName = "propertyName";
        propertyPointer.setPropertyName(propertyName);
        assertEquals(new QName(null, propertyName), propertyPointer.getName());
    }

    @Test
    public void testSetPropertyName() {
        String oldPropertyName = "oldPropertyName";
        String newPropertyName = "newPropertyName";
        propertyPointer.setPropertyName(oldPropertyName);
        propertyPointer.setPropertyName(newPropertyName);
        assertEquals(newPropertyName, propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() throws Exception {
        int propertyCount = 5;
        Mockito.when(parentPointer.getNode()).thenReturn(new Object() {
            Object[] properties = new Object[propertyCount];

            public Object getProperty(String name) {
                for (int i = 0; i < propertyCount; i++) {
                    if (name.equals(String.valueOf(i))) {
                        return properties[i];
                    }
                }
                throw new NoSuchFieldException();
            }
        });
        assertEquals(propertyCount, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() throws Exception {
        Object bean = new Object() {
            String[] propertyNames = new String[]{"name1", "name2", "name3"};

            public String[] getPropertyNames() {
                return propertyNames;
            }
        };
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        String[] propertyNames = propertyPointer.getPropertyNames();
        assertArrayEquals(new String[]{"name1", "name2", "name3"}, propertyNames);
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        Object bean = new Object();
        Object propertyValue = new Object();
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        Mockito.when(parentPointer.getNode(0)).thenReturn(propertyValue);
        assertEquals(propertyValue, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        Object collection = new Object()[5];
        Mockito.when(parentPointer.getNode()).thenReturn(collection);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        Object leaf = new Object();
        Mockito.when(parentPointer.getNode()).thenReturn(leaf);
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() throws Exception {
        Object collection = new Object[5];
        Mockito.when(parentPointer.getNode()).thenReturn(collection);
        assertEquals(5, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        Object bean = new Object();
        Object propertyValue = new Object();
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        Mockito.when(parentPointer.getNode(0)).thenReturn(propertyValue);
        NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(propertyValue, valuePointer.getNode());
    }

    @Test
    public void testCreatePath() throws Exception {
        Object bean = new Object();
        Object propertyValue = new Object();
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        Mockito.when(parentPointer.getNode(0)).thenReturn(propertyValue);
        NodePointer propertyNodePointer = propertyPointer.createPath(context);
        assertEquals(propertyValue, propertyNodePointer.getNode());
    }

    @Test
    public void testCreatePathWithValue() throws Exception {
        Object bean = new Object();
        Object propertyValue = new Object();
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        Mockito.when(parentPointer.getNode(0)).thenReturn(propertyValue);
        NodePointer propertyNodePointer = propertyPointer.createPath(context, new Object());
        assertNotSame(propertyValue, propertyNodePointer.getNode());
    }

    @Test
    public void testCreateChildWithName() throws Exception {
        Object bean = new Object();
        Object propertyValue = new Object();
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        Mockito.when(parentPointer.getNode(0)).thenReturn(propertyValue);
        NodePointer childNodePointer = propertyPointer.createChild(context, new QName("childName"), 0, new Object());
        assertNotSame(propertyValue, childNodePointer.getNode());
    }

    @Test
    public void testCreateChildWithIndex() throws Exception {
        Object bean = new Object()[3];
        Object propertyValue = bean[0];
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        NodePointer childNodePointer = propertyPointer.createChild(context, null, 1, new Object());
        assertNotSame(propertyValue, childNodePointer.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildWithNameNull() throws Exception {
        propertyPointer.createChild(context, null, 0, new Object());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildWithIndexOutOfBounds() throws Exception {
        Object bean = new Object()[3];
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        propertyPointer.createChild(context, null, 3, new Object());
    }

    @Test
    public void testHashCode() {
        NodePointer parentPointer1 = Mockito.mock(NodePointer.class);
        NodePointer parentPointer2 = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer1);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer2);
        int index1 = 1;
        int index2 = 2;
        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer1);
        PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer1);
        PropertyPointer propertyPointer5 = new PropertyPointer(parentPointer2);
        PropertyPointer propertyPointer6 = new PropertyPointer(parentPointer1);
        PropertyPointer propertyPointer7 = new PropertyPointer(parentPointer1);

        List<Integer> expectedHashCodes = Arrays.asList(
                17,
                37,
                53,
                71,
                parentPointer1.hashCode(),
                parentPointer2.hashCode(),
                2 * (31 + parentPointer1.hashCode()) + index1,
                2 * (31 + parentPointer1.hashCode()) + index2
        );

        assertEquals(expectedHashCodes.get(0), propertyPointer1.hashCode());
        assertEquals(expectedHashCodes.get(1), propertyPointer2.hashCode());
        assertEquals(expectedHashCodes.get(2), propertyPointer3.hashCode());
        assertEquals(expectedHashCodes.get(3), propertyPointer4.hashCode());
        assertEquals(expectedHashCodes.get(4), propertyPointer5.hashCode());
        assertEquals(expectedHashCodes.get(5), propertyPointer6.hashCode());
        assertEquals(expectedHashCodes.get(6), propertyPointer7.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer parentPointer1 = Mockito.mock(NodePointer.class);
        NodePointer parentPointer2 = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer1);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer2);
        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer1);
        PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer1);
        PropertyPointer propertyPointer5 = new PropertyPointer(parentPointer2);
        PropertyPointer propertyPointer6 = new PropertyPointer(parentPointer1);
        PropertyPointer propertyPointer7 = new PropertyPointer(parentPointer1);
        int index1 = 1;
        int index2 = 2;

        List<Boolean> expectedEquals = Arrays.asList(
                false,
                false,
                true,
                true,
                false,
                true,
                true
        );

        for (int i = 0; i < expectedEquals.size(); i++) {
            boolean result = propertyPointer1.equals(propertyPointers.get(i));
            assertTrue(result == expectedEquals.get(i));
        }
    }
}