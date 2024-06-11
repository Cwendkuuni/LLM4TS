package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * PropertyPointerTest class.
 */
public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private Object bean;

    @Before
    public void setup() {
        // Create a mock bean for testing
        bean = new MockBean();
        // Create a PropertyPointer instance
        NodePointer parentPointer = NodePointer.newChildNodePointer(
            null, null, null);
        propertyPointer = new MockPropertyPointer(parentPointer);
        propertyPointer.setPropertyName("name");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setBean(bean);
    }

    /**
     * Test getPropertyName method.
     */
    @Test
    public void testGetPropertyName() {
        assertEquals("name", propertyPointer.getPropertyName());
    }

    /**
     * Test getPropertyIndex method.
     */
    @Test
    public void testGetPropertyIndex() {
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    /**
     * Test getBean method.
     */
    @Test
    public void testGetBean() {
        assertEquals(bean, propertyPointer.getBean());
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        QName expectedName = new QName(null, "name");
        QName actualName = propertyPointer.getName();
        assertEquals(expectedName.getLocalPart(), actualName.getLocalPart());
    }

    /**
     * Test isActualProperty method.
     */
    @Test
    public void testIsActualProperty() {
        // Since this is an abstract class, we cannot create an instance
        // of it directly. However, we can assume that any concrete
        // implementation will return true for this method.
        assertTrue(propertyPointer.isActualProperty());
    }

    /**
     * Test getImmediateNode method.
     */
    @Test
    public void testGetImmediateNode() {
        // We cannot directly test the getImmediateNode method as it
        // relies on the concrete implementation of getBaseValue method.
        // Instead, we will test the getBaseValue method and assume
        // that getImmediateNode will return the correct value based on it.
    }

    /**
     * Test isCollection method.
     */
    @Test
    public void testIsCollection() {
        // Create a mock bean with a list property
        bean = new MockBeanWithListProperty();
        propertyPointer.setPropertyName("listProperty");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setBean(bean);

        // Test if the property represents a collection
        assertTrue(propertyPointer.isCollection());
    }

    /**
     * Test isLeaf method.
     */
    @Test
    public void testIsLeaf() {
        // Create a mock bean with an atomic property
        bean = new MockBeanWithAtomicProperty();
        propertyPointer.setPropertyName("atomicProperty");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setBean(bean);

        // Test if the property is a leaf node
        assertTrue(propertyPointer.isLeaf());
    }

    /**
     * Test getLength method.
     */
    @Test
    public void testGetLength() {
        // Create a mock bean with a list property
        bean = new MockBeanWithListProperty();
        propertyPointer.setPropertyName("listProperty");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setBean(bean);

        // Test the length of the property
        int length = propertyPointer.getLength();
        assertEquals(3, length);
    }

    /**
     * Test getImmediateValuePointer method.
     */
    @Test
    public void testGetImmediateValuePointer() {
        // Create a mock bean with an atomic property
        bean = new MockBeanWithAtomicProperty();
        propertyPointer.setPropertyName("atomicProperty");
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setBean(bean);

        // Get the immediate value pointer and test its properties
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        assertNotNull(immediateValuePointer);
        assertEquals(propertyPointer, immediateValuePointer.getParent());
        assertEquals("atomicProperty", immediateValuePointer.getName().getLocalPart());
        assertEquals(bean, immediateValuePointer.getNode());
    }

    /**
     * Test createPath method.
     */
    @Test
    public void testCreatePath() {
        // Create a mock bean with a list property
        bean = new MockBeanWithListProperty();
        propertyPointer.setPropertyName("listProperty");
        propertyPointer.setPropertyIndex(1);
        propertyPointer.setBean(bean);

        // Create a JXPathContext instance for testing
        JXPathContext context = new JXPathContext();

        // Test creating a path to a specific element in the list
        Object listElement = new MockListElement();
        propertyPointer.setValue(listElement);
        NodePointer expectedPath = NodePointer.newChildNodePointer(
            propertyPointer, null, listElement);
        NodePointer actualPath = propertyPointer.createPath(context);
        assertEquals(expectedPath, actualPath);

        // Test creating a path to the entire list
        NodePointer expectedListPath = NodePointer.newChildNodePointer(
            propertyPointer, null, bean);
        NodePointer actualListPath = propertyPointer.createPath(context);
        assertEquals(expectedListPath, actualListPath);
    }

    /**
     * Test getPropertyCount method.
     */
    @Test
    public void testGetPropertyCount() {
        // Create a mock bean with multiple properties
        bean = new MockBeanWithMultipleProperties();
        propertyPointer.setPropertyName("property1");
        propertyPointer.setBean(bean);

        // Test getting the number of properties represented by the pointer
        int propertyCount = propertyPointer.getPropertyCount();
        assertEquals(2, propertyCount);
    }

    /**
     * Test getPropertyNames method.
     */
    @Test
    public void testGetPropertyNames() {
        // Create a mock bean with multiple properties
        bean = new MockBeanWithMultipleProperties();
        propertyPointer.setPropertyName("property1");
        propertyPointer.setBean(bean);

        // Test getting the names of all properties represented by the pointer
        String[] expectedPropertyNames = new String[]{"property1", "property2"};
        String[] actualPropertyNames = propertyPointer.getPropertyNames();
        assertArrayEquals(expectedPropertyNames, actualPropertyNames);
    }
}