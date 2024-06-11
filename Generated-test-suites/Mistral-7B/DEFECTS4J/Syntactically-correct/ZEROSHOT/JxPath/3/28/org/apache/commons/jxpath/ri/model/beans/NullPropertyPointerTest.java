import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName qname = nullPropertyPointer.getName();
        assertEquals("*", qname.getLocalPart());
        assertEquals("", qname.getNamespaceURI());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(1);
        assertEquals(1, nullPropertyPointer.getPropertyIndex());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.getLength();
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testgetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.getValuePointer();
        assertInstanceOf(NullPointer.class, nodePointer);
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testSetValue() {
        // Cannot set value for NullPropertyPointer
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        assertNull(nodePointer);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createPath(context, new Object());
        assertNull(nodePointer);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 1);
        assertNull(nodePointer);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 1, new Object());
        assertNull(nodePointer);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getPropertyName().getLocalPart());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName().getLocalPart());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName().getLocalPart());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("", nullPropertyPointer.asPath());
    }

    @Test
    public void testEscape() {
        String string = "test'string\"";
        String escapedString = NullPropertyPointer.escape(string);
        String expected = "test&apos;string&quot;";
        assertEquals(expected, escapedString);
    }
}