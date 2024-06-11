package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    // Test case for PropertyPointer constructor
    @Test
    public void testPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertNull(propertyPointer.getPropertyIndex());
        assertNull(propertyPointer.getBean());
    }

    // Test case for getPropertyIndex method
    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    // Test case for setPropertyIndex method
    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setPropertyIndex(2);
        assertEquals(2, propertyPointer.getPropertyIndex());
    }

    // Test case for getBean method
    @Test
    public void testGetBean() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    // Test case for getName method
    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        QName name = propertyPointer.getName();
        assertEquals(propertyName, name.getLocalPart());
    }

    // Test case for getPropertyName method
    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, propertyPointer.getPropertyName());
    }

    // Test case for setPropertyName method
    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String oldPropertyName = "oldPropertyName";
        String newPropertyName = "newPropertyName";
        propertyPointer.setPropertyName(oldPropertyName);
        propertyPointer.setPropertyName(newPropertyName);
        assertEquals(newPropertyName, propertyPointer.getPropertyName());
        assertNotEquals(oldPropertyName, propertyPointer.getPropertyName());
    }

    // Test case for getPropertyCount method
    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyCount(5);
        assertEquals(5, propertyPointer.getPropertyCount());
    }

    // Test case for getPropertyNames method
    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String[] propertyNames = new String[]{"property1", "property2", "property3"};
        propertyPointer.setPropertyNames(propertyNames);
        assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
    }

    // Test case for isActualProperty method
    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertTrue(propertyPointer.isActualProperty());
    }

    // Test case for isActual method
    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertTrue(propertyPointer.isActual());
    }

    // Test case for getImmediateNode method
    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object immediateNode = new Object();
        propertyPointer.setValue(immediateNode);
        assertEquals(immediateNode, propertyPointer.getImmediateNode());
    }

    // Test case for isCollection method
    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object collection = new Object()[5];
        propertyPointer.setValue(collection);
        assertTrue(propertyPointer.isCollection());
    }

    // Test case for isLeaf method
    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String leafValue = "testValue";
        propertyPointer.setValue(leafValue);
        assertTrue(propertyPointer.isLeaf());
    }

    // Test case for getLength method
    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object collection = new Object[5];
        propertyPointer.setValue(collection);
        assertEquals(5, propertyPointer.getLength());
    }

    // Test case for getImmediateValuePointer method
    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object immediateNode = new Object();
        propertyPointer.setValue(immediateNode);
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertEquals(immediateNode, immediateValuePointer.getNode());
    }

    // Test case for createPath method with valid arguments
    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int propertyIndex = 0;
        propertyPointer.setPropertyIndex(propertyIndex);
        NodePointer result = propertyPointer.createPath(context);
        assertEquals(propertyName, result.getName().getLocalPart());
        assertEquals(bean, result.getNode());
    }

    // Test case for createPath method with invalid bean argument
    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathInvalidBean() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = null;
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int propertyIndex = 0;
        propertyPointer.setPropertyIndex(propertyIndex);
        propertyPointer.createPath(context);
    }

    // Test case for createPath method with invalid propertyName argument
    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathInvalidPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = null;
        propertyPointer.setPropertyName(propertyName);
        int propertyIndex = 0;
        propertyPointer.setPropertyIndex(propertyIndex);
        propertyPointer.createPath(context);
    }

    // Test case for createPath method with invalid propertyIndex argument
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreatePathInvalidPropertyIndex() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        Object collection = new Object[5];
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int propertyIndex = 6;
        propertyPointer.setPropertyIndex(propertyIndex);
        propertyPointer.createPath(context, collection[0]);
    }

    // Test case for createPath method with valid arguments and value
    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int propertyIndex = 0;
        propertyPointer.setPropertyIndex(propertyIndex);
        Object value = new Object();
        propertyPointer.createPath(context, value);
        assertEquals(value, propertyPointer.getNode());
    }

    // Test case for createChild method with valid arguments and null name
    @Test
    public void testCreateChildNullName() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = null;
        int index = 0;
        Object value = new Object();
        NodePointer child = propertyPointer.createChild(context, null, index, value);
        assertNull(child);
    }

    // Test case for createChild method with valid arguments and empty name
    @Test
    public void testCreateChildEmptyName() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "";
        int index = 0;
        Object value = new Object();
        NodePointer child = propertyPointer.createChild(context, new QName(propertyName), index, value);
        assertNull(child);
    }

    // Test case for createChild method with valid arguments and name
    @Test
    public void testCreateChildValidName() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        int index = 0;
        Object value = new Object();
        NodePointer child = propertyPointer.createChild(context, new QName(propertyName), index, value);
        assertEquals(propertyName, child.getName().getLocalPart());
        assertEquals(value, child.getNode());
    }

    // Test case for createChild method with valid arguments and index out of bounds
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildIndexOutOfBounds() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object bean = new Object();
        propertyPointer.setBean(bean);
        String propertyName = "testProperty";
        int index = 6;
        Object value = new Object();
        NodePointer child = propertyPointer.createChild(context, new QName(propertyName), index, value);
        assertNull(child);
    }
}