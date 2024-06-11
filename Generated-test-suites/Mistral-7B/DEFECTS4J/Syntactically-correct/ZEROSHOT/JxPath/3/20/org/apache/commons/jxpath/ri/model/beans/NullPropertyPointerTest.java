package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.QName;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    private NodePointer parent;
    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setUp() {
        parent = new NodePointer(new Object());
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();
        assertTrue(expected.equals(actual));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        int actual = nullPropertyPointer.getLength();
        assertEquals(0, actual);
    }

    @Test
    public void testGetBaseValue() {
        Object actual = nullPropertyPointer.getBaseValue();
        assertNull(actual);
    }

    @Test
    public void testGetImmediateNode() {
        Object actual = nullPropertyPointer.getImmediateNode();
        assertNull(actual);
    }

    @Test
    public void testIsLeaf() {
        boolean actual = nullPropertyPointer.isLeaf();
        assertTrue(actual);
    }

    @Test
    public void testgetValuePointer() {
        NullPointer nullPointer = nullPropertyPointer.getValuePointer();
        assertNotNull(nullPointer);
    }

    @Test
    public void testIsActualProperty() {
        boolean actual = nullPropertyPointer.isActualProperty();
        assertFalse(actual);
    }

    @Test
    public void testIsActual() {
        boolean actual = nullPropertyPointer.isActual();
        assertFalse(actual);
    }

    @Test
    public void testIsContainer() {
        boolean actual = nullPropertyPointer.isContainer();
        assertTrue(actual);
    }

    @Test
    public void testSetValue_NullParent() {
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullTarget() {
        nullPropertyPointer.setValue(null);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actual = nullPropertyPointer.createPath(context);
        assertNull(actual);
    }

    @Test
    public void testCreatePath_Value() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actual = nullPropertyPointer.createPath(context, new Object());
        assertNull(actual);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actual = nullPropertyPointer.createChild(context, new QName(""));
        assertNull(actual);
    }

    @Test
    public void testCreateChild_NameIndex() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actual = nullPropertyPointer.createChild(context, new QName(""), 1);
        assertNull(actual);
    }

    @Test
    public void testCreateChild_NameIndexValue() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer actual = nullPropertyPointer.createChild(context, new QName(""), 1, new Object());
        assertNull(actual);
    }

    @Test
    public void testGetPropertyName() {
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals("", actual);
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("test");
        String actual = nullPropertyPointer.getPropertyName();
        assertEquals("test", actual);
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("test");
        String actual = nullPropertyPointer.asPath();
        String expected = "[@name='test']";
        assertEquals(expected, actual);
    }

    @Test
    public void testIsCollection() {
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        int actual = nullPropertyPointer.getPropertyCount();
        assertEquals(0, actual);
    }

    @Test
    public void testGetPropertyNames() {
        String[] actual = nullPropertyPointer.getPropertyNames();
        assertEquals(new String[0], actual);
    }

    @Test
    public void testAsPath() {
        String actual = nullPropertyPointer.asPath();
        assertNull(actual);
    }

    @Test
    public void testEscape() {
        String string = "test'test\"test";
        String actual = nullPropertyPointer.escape(string);
        String expected = "test&apos;test&quot;test";
        assertEquals(expected, actual);
    }
}