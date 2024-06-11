package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        assertEquals(-1, propertyPointer.getPropertyIndex());

        PropertyPointer propertyPointerWithIndex = new PropertyPointer(null);
        propertyPointerWithIndex.setPropertyIndex(1);
        assertEquals(1, propertyPointerWithIndex.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndexWithInvalidIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        propertyPointer.setPropertyIndex(-1);
        assertEquals(-1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetPropertyIndexWithNullObject() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        assertEquals(-1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndexWithNegativeIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        try {
            propertyPointer.setPropertyIndex(-1);
            fail("Expected JXPathAbstractFactoryException");
        } catch (JXPathAbstractFactoryException e) {
            // Expected
        }
    }

    @Test
    public void testGetBeanWithNullParentPointer() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetBeanWithNullObject() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        assertNull(propertyPointer.getBean());
    }

    // Test other edge cases and exceptions here...

    @Test
    public void testCompareChildNodePointersWithEqualChildPointers() {
        PropertyPointer pointer1 = new PropertyPointer(null);
        PropertyPointer pointer2 = new PropertyPointer(null);
        NodePointer valuePointer1 = new NodePointer(null);
        NodePointer valuePointer2 = new NodePointer(null);

        pointer1.setValuePointer(valuePointer1);
        pointer2.setValuePointer(valuePointer2);

        int result = pointer1.compareChildNodePointers(pointer2, pointer2);

        assertEquals(0, result);
    }

    @Test
    public void testCompareChildNodePointersWithDifferentChildPointers() {
        PropertyPointer pointer1 = new PropertyPointer(null);
        PropertyPointer pointer2 = new PropertyPointer(null);
        NodePointer valuePointer1 = new NodePointer(null);
        NodePointer valuePointer2 = new NodePointer(null);

        pointer1.setValuePointer(valuePointer1);
        pointer2.setValuePointer(valuePointer2);

        int result = pointer1.compareChildNodePointers(pointer2, pointer1);

        assertTrue(result < 0);
    }
}