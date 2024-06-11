package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerCreation() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertNotNull(propertyPointer);
        assertEquals(parent, propertyPointer.getImmediateParentPointer());
        assertNull(propertyPointer.getValue());
        assertEquals(-1, propertyPointer.getLength());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerPropertyIndex() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
        assertEquals(WHOLE_COLLECTION, propertyPointer.getIndex());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerBean() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setBean(new Object());
        assertNotNull(propertyPointer.getBean());
        assertNotSame(bean, propertyPointer.getBean());
        assertEquals(bean, propertyPointer.getImmediateParentPointer().getNode());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, propertyPointer.getPropertyName());
        assertNull(propertyPointer.getName());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerPropertyCount() {
        NodePointer parent = new NodePointer(new Object()[3]);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertEquals(3, propertyPointer.getPropertyCount());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerPropertyNames() {
        NodePointer parent = new NodePointer(new String[]{"test1", "test2", "test3"});
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String[] propertyNames = {"test1", "test2", "test3"};
        propertyPointer.setPropertyNames(propertyNames);
        assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerIsActualProperty() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertFalse(propertyPointer.isActualProperty());
        assertFalse(propertyPointer.isActual());

        PropertyPointer actualProperty = new ActualPropertyPointer(parent);
        assertTrue(actualProperty.isActualProperty());
        assertTrue(actualProperty.isActual());
    }

    @Test
    public void testPropertyPointerGetImmediateNode() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Object expected = new Object();
        propertyPointer.setValue(expected);
        assertEquals(expected, propertyPointer.getImmediateNode());
        assertNull(propertyPointer.getBaseValue());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerIsLeaf() {
        NodePointer parent = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.setValue(new Object());
        assertTrue(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerIsCollection() {
        NodePointer parent = new NodePointer(new Object()[3]);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertTrue(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerGetLength() {
        NodePointer parent = new NodePointer(new Object()[3]);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        assertEquals(3, propertyPointer.getLength());
        assertFalse(propertyPointer.isCollection());
        assertFalse(propertyPointer.isLeaf());
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testPropertyPointerCreatePath() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int index = 1;
        propertyPointer.setPropertyIndex(index);
        Object expectedValue = new Object();
        propertyPointer.setValue(expectedValue);
        JXPathContext context = new JXPathContext();
        NodePointer result = propertyPointer.createPath(context);
        assertNotNull(result);
        assertEquals(propertyPointer, result);
        assertEquals(bean, result.getNode());
        assertEquals(propertyName, result.getName().getLocalPart());
        assertEquals(expectedValue, result.getImmediateNode());
    }

    @Test
    public void testPropertyPointerCreatePathWithValue() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int index = 1;
        propertyPointer.setPropertyIndex(index);
        Object expectedValue = new Object();
        propertyPointer.setValue(expectedValue);
        JXPathContext context = new JXPathContext();
        NodePointer result = propertyPointer.createPath(context, new Object());
        assertNotNull(result);
        assertEquals(propertyPointer, result);
        assertEquals(bean, result.getNode());
        assertEquals(propertyName, result.getName().getLocalPart());
        assertNotSame(expectedValue, result.getImmediateNode());
        assertEquals(expectedValue, result.getValue());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testPropertyPointerCreatePathWithInvalidFactory() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int index = 1;
        propertyPointer.setPropertyIndex(index);
        JXPathContext context = new JXPathContext();
        PropertyPointer result = propertyPointer.createPath(context);
        assertNotNull(result);
        assertEquals(propertyPointer, result);
        assertEquals(bean, result.getNode());
        assertEquals(propertyName, result.getName().getLocalPart());
        // This line should throw an exception
        propertyPointer.createPath(context, new Object());
    }

    @Test
    public void testPropertyPointerCreateChild() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int index = 1;
        propertyPointer.setPropertyIndex(index);
        String childPropertyName = "testChildProperty";
        propertyPointer.createChild(
            new JXPathContext(),
            new QName(null, childPropertyName),
            index,
            new Object());
        NodePointer result = propertyPointer.createPath(new JXPathContext());
        assertEquals(bean, result.getNode());
        assertEquals(childPropertyName, result.getName().getLocalPart());
        assertEquals(new Object(), result.getImmediateNode());
    }

    @Test
    public void testPropertyPointerCompareChildNodePointers() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int index = 1;
        propertyPointer.setPropertyIndex(index);
        NodePointer child1 = propertyPointer.createChild(
            new JXPathContext(),
            new QName(null, "testChildProperty1"),
            index,
            new Object());
        NodePointer child2 = propertyPointer.createChild(
            new JXPathContext(),
            new QName(null, "testChildProperty2"),
            index,
            new Object());
        int comparisonResult = propertyPointer.compareChildNodePointers(child1, child2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testPropertyPointerCompareChildNodePointersDifferentIndexes() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        int index1 = 1;
        propertyPointer.setPropertyIndex(index1);
        int index2 = 2;
        NodePointer child1 = propertyPointer.createChild(
            new JXPathContext(),
            new QName(null, "testChildProperty1"),
            index1,
            new Object());
        NodePointer child2 = propertyPointer.createChild(
            new JXPathContext(),
            new QName(null, "testChildProperty2"),
            index2,
            new Object());
        int comparisonResult = propertyPointer.compareChildNodePointers(child1, child2);
        assertFalse(comparisonResult < 0);
        assertFalse(comparisonResult > 0);
    }

    @Test
    public void testPropertyPointerCompareChildNodePointersDifferentParents() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        NodePointer parent1 = new NodePointer(bean1);
        NodePointer parent2 = new NodePointer(bean2);
        PropertyPointer propertyPointer1 = new PropertyPointer(parent1);
        String propertyName = "testProperty";
        propertyPointer1.setPropertyName(propertyName);
        int index = 1;
        propertyPointer1.setPropertyIndex(index);
        NodePointer child1 = propertyPointer1.createChild(
            new JXPathContext(),
            new QName(null, "testChildProperty1"),
            index,
            new Object());

        PropertyPointer propertyPointer2 = new PropertyPointer(parent2);
        propertyPointer2.setPropertyName(propertyName);
        NodePointer child2 = propertyPointer2.createChild(
            new JXPathContext(),
            new QName(null, "testChildProperty2"),
            index,
            new Object());

        int comparisonResult = propertyPointer1.compareChildNodePointers(child1, child2);
        assertFalse(comparisonResult < 0);
        assertFalse(comparisonResult > 0);
    }
}