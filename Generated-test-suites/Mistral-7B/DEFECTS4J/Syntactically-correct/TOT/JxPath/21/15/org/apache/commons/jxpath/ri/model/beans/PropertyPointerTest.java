package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    // Assuming we have some test beans and factories set up

    private PropertyPointer propertyPointer;
    private JXPathContext context;

    @Before
    public void setUp() {
        context = new JXPathContext(new MyTestBean(), new MyTestBeanFactory());
        propertyPointer = new MyPropertyPointer(new MyParentPointer(context));
        propertyPointer.setPropertyName("testProperty");
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(-1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertNotNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        QName name = propertyPointer.getName();
        assertEquals("testProperty", name.getLocalPart());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("testProperty", propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        propertyPointer.setPropertyName("newPropertyName");
        assertEquals("newPropertyName", propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        // Assuming MyTestBean has a property count
        MyTestBean bean = (MyTestBean) propertyPointer.getBean();
        assertEquals(2, bean.getPropertyCount("myBean"));
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
        assertEquals(1, propertyPointer.getLength());
        assertEquals("newPropertyName2", propertyPointer.getPropertyName());
        assertEquals("newPropertyValue2", propertyPointer.getStringValue(context));
    }

    @Test
    public void testGetPropertyNames() {
        String[] names = propertyPointer.getPropertyNames();
        assertEquals(2, names.length);
        assertEquals("property1", names[0]);
        assertEquals("property2", names[1]);
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        Object node = propertyPointer.getImmediateNode();
        assertNotNull(node);
        assertEquals("testValue", node.toString());
    }

    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer newPropertyPointer = propertyPointer.createPath(context);
        assertEquals(propertyPointer.asPath(), newPropertyPointer.asPath());
        newPropertyPointer.setValue("newValue");
        assertNotEquals(propertyPointer.asPath(), newPropertyPointer.asPath());
    }

    @Test
    public void testCreateChild() {
        PropertyPointer childPropertyPointer = propertyPointer.createChild(context, null, 0, "newChildValue");
        assertEquals(propertyPointer.asPath() + "/0", childPropertyPointer.asPath());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testCreatePathWithInvalidFactory() {
        JXPathContext contextWithInvalidFactory = new JXPathContext(new MyInvalidTestBean(), new MyTestBeanFactory());
        PropertyPointer propertyPointerWithInvalidFactory = new MyPropertyPointer(new MyParentPointer(contextWithInvalidFactory));
        propertyPointerWithInvalidFactory.createPath(contextWithInvalidFactory);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildWithInvalidIndex() {
        PropertyPointer propertyPointerWithInvalidIndex = propertyPointer.clone();
        propertyPointerWithInvalidIndex.setIndex(10);
        propertyPointerWithInvalidIndex.createChild(context, null, 10, "newChildValue");
    }
}