import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName name = nullPropertyPointer.getName();
        assertEquals("*", name.getLocalPart());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(1);
        assertEquals(0, nullPropertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testGetValuePointerWithInvalidParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.getValuePointer();
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

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null, null, null) {
            @Override
            public boolean isContainer() {
                return false;
            }
        });
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createPath(context);
        assertEquals(null, nodePointer);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createPath(context, new Object());
        assertEquals(null, nodePointer);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer = nullPropertyPointer.createChild(context, new QName("test"), 1);
        assertEquals(null, nodePointer);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nodePointer =
            nullPropertyPointer.createChild(context, new QName("test"), 1, new Object());
        assertEquals(null, nodePointer);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
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
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String path = nullPropertyPointer.asPath();
        assertEquals("", path);

        NullPropertyPointer attributeNullPropertyPointer = new NullPropertyPointer(null);
        attributeNullPropertyPointer.setNameAttributeValue("test");
        attributeNullPropertyPointer.setIndex(1);
        path = attributeNullPropertyPointer.asPath();
        assertEquals("[@name='test'][1]", path);
    }

    @Test
    public void testEscape() {
        String string = "\"'test\"";
        String escapedString = NullPropertyPointer.escape(string);
        assertEquals("&quot;&#39;test&quot;", escapedString);
    }
}