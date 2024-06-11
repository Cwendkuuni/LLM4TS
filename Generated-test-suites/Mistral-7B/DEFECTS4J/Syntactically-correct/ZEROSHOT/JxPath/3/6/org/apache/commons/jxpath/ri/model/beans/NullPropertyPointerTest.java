package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        QName name = pointer.getName();
        assertNull(name);
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        int length = pointer.getLength();
        assertEquals(0, length);
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Object baseValue = pointer.getBaseValue();
        assertNull(baseValue);
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer immediateNode = pointer.getImmediateNode();
        assertNull(immediateNode);
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean isLeaf = pointer.isLeaf();
        assertTrue(isLeaf);
    }

    @Test
    public void testgetValuePointer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer valuePointer = pointer.getValuePointer();
        assertInstanceOf(NullPointer.class, valuePointer);
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean isActualProperty = pointer.isActualProperty();
        assertFalse(isActualProperty);
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean isActual = pointer.isActual();
        assertFalse(isActual);
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean isContainer = pointer.isContainer();
        assertTrue(isContainer);
    }

    @Test
    public void testSetValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        try {
            pointer.setValue("test");
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }
    }

    @Test
    public void testCreatePath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        try {
            pointer.createPath(new JXPathContext());
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }
    }

    @Test
    public void testCreatePathWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        try {
            pointer.createPath(new JXPathContext(), "test");
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }
    }

    @Test
    public void testCreateChild() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        try {
            pointer.createChild(new JXPathContext(), new QName("test"));
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }
    }

    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        try {
            pointer.createChild(new JXPathContext(), new QName("test"), 1, "test");
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // expected
        }
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("test");
        assertEquals("test", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("test");
        assertEquals("test", pointer.getPropertyName());
        assertTrue(pointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testgetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        int propertyCount = pointer.getPropertyCount();
        assertEquals(0, propertyCount);
    }

    @Test
    public void testgetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String[] propertyNames = pointer.getPropertyNames();
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String asPath = pointer.asPath();
        assertEquals("", asPath);
    }

    @Test
    public void testEscape() {
        String string = "test'test\"test";
        String escapedString = NullPropertyPointer.escape(string);
        String expected = "test&apos;test&quot;test";
        assertEquals(expected, escapedString);
    }
}