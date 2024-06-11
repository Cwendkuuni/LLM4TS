import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathContextFactory;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer().getNode());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testNullParentPointer() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParentPointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParentPointerIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        nullPropertyPointer.setValue("test");
        ParentPointer parentPointer = new ParentPointer(nullPropertyPointer);
        nullPropertyPointer.setValue("test", parentPointer);
    }

    @Test
    public void testSetValuePropertyOwnerPointer() {
        NodePointer parent = new NodePointer(new QName("root"));
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        String propertyName = "test";
        String value = "testValue";
        nullPropertyPointer.setValue(value);
        Object expected = value;
        Object actual = propertyOwnerPointer.getPropertyValue(propertyName);
        assertEquals(expected, actual);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathNullParentPointer() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        nullPropertyPointer.createPath(context);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathNullParentPointerIsContainer() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        nullPropertyPointer.createPath(context);
        PropertyPointer propertyPointer = new PropertyPointer(new NodePointer(new QName("test")));
        nullPropertyPointer.createPath(context, propertyPointer);
    }

    @Test
    public void testCreatePathPropertyOwnerPointer() {
        NodePointer parent = new NodePointer(new QName("root"));
        PropertyOwnerPointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(propertyOwnerPointer);
        JXPathContext context = new JXPathContext();
        NodePointer expected = new NodePointer(new QName("test"));
        NodePointer actual = nullPropertyPointer.createPath(context).createChild(context, new QName("test"), 0);
        assertEquals(expected, actual);
    }
}