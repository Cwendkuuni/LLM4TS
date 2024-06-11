import org.apache.commons.jxpath.Node;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(new Node("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer().getNode());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        String name = nullPropertyPointer.getName().getLocalPart();
        assertEquals("", name);
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        nullPropertyPointer.setPropertyName("test");
        String name = nullPropertyPointer.getName().getLocalPart();
        assertEquals("test", name);
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathThrowsException() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        nullPropertyPointer.createPath(new JXPathContext());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueThrowsExceptionParentIsNull() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Node("test"));
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueThrowsExceptionParentNotPropertyOwnerPointer() {
        NodePointer parent = new NodePointer(new Node("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Node("test"));
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathThrowsExceptionParentNotPropertyOwnerPointer() {
        NodePointer parent = new NodePointer(new Node("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createPath(new JXPathContext());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testGetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        assertEquals(null, nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testgetValuePointerReturnsNullPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        assertNull(nullPropertyPointer.getValuePointer().getNode());
    }

    @Test
    public void testCreatePathReturnsNullPropertyPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext());
        assertNull(result);
    }

    @Test
    public void testCreatePathWithValueSetsPropertyValue() {
        NodePointer parent = new NodePointer(new Node("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Node("test");
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext(), value);
        assertEquals(value, result.getNode());
    }

    @Test
    public void testCreateChildMehods() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer(new Node("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        NodePointer child1 = nullPropertyPointer.createChild(context, new QName("child1"));
        NodePointer child2 = nullPropertyPointer.createChild(context, new QName("child2"), 1);

        assertNull(child1.getNode());
        assertNull(child2.getNode());
    }

    @Test
    public void testSetPropertyNameWithEmptyString() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        nullPropertyPointer.setPropertyName("");
        String name = nullPropertyPointer.getName().getLocalPart();
        assertEquals("", name);
    }

    @Test
    public void testSetPropertyNameWithNullString() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        nullPropertyPointer.setPropertyName(null);
        String name = nullPropertyPointer.getName().getLocalPart();
        assertNull(name);
    }

    @Test
    public void testAsPathWithEmptyPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        nullPropertyPointer.setPropertyName("");
        String result = nullPropertyPointer.asPath();
        assertEquals("", result);
    }

    @Test
    public void testAsPathWithSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Node("root")));
        nullPropertyPointer.setPropertyName("test");
        String result = nullPropertyPointer.asPath();
        assertEquals("/test", result);
    }
}