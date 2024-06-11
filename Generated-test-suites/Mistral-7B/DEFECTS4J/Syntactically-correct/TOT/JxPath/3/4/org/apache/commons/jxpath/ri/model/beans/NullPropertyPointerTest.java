import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPointerCreation() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNull(nullPropertyPointer.getBaseValue());
        Assert.assertFalse(nullPropertyPointer.isActual());
        Assert.assertFalse(nullPropertyPointer.isActualProperty());
        Assert.assertTrue(nullPropertyPointer.isContainer());
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        try {
            nullPropertyPointer.setValue("test");
            Assert.fail();
        } catch (JXPathInvalidAccessException e) {
            Assert.assertEquals(
                "Cannot set property " + nullPropertyPointer.asPath() + ", the target object is null", e.getMessage());
        }
    }

    @Test
    public void testSetValueWithNonContainerParent() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        try {
            nullPropertyPointer.setValue(123);
            Assert.fail();
        } catch (JXPathInvalidAccessException e) {
            Assert.assertEquals(
                "Cannot set property " + nullPropertyPointer.asPath() + ", path does not match a changeable location", e.getMessage());
        }
    }

    @Test
    public void testSetValueWithDynamicPropertyOwnerParent() {
        NodePointer parent = new PropertyOwnerPointer(new NodePointer(null, null), "testProperty");
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("testValue");
        Assert.assertEquals("testValue", parent.getPropertyPointer().getValue());
    }

    @Test
    public void testCreatePathWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext());
        Assert.assertNull(result);
    }

    @Test
    public void testCreatePathWithAttribute() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext());
        Assert.assertInstanceOf(NodePointer.class, result);
        Assert.assertInstanceOf(AttributePointer.class, result);
        Assert.assertEquals("nullPropertyPointer[@name='*']", result.asPath());
    }

    @Test
    public void testCreatePathWithChild() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext(), "child");
        Assert.assertInstanceOf(NodePointer.class, result);
        Assert.assertInstanceOf(ElementPointer.class, result);
        Assert.assertEquals("nullPropertyPointer/child", result.asPath());
    }

    @Test
    public void testCreatePathWithChildAndValue() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext(), "child", 1, "value");
        Assert.assertInstanceOf(NodePointer.class, result);
        Assert.assertInstanceOf(ElementPointer.class, result);
        Assert.assertEquals("nullPropertyPointer/child[1]/@value", result.asPath());
    }
}