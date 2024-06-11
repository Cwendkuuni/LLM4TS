import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerCreation() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Property name should be set to * by default", "*", nullPropertyPointer.getPropertyName());
        Assert.assertFalse("ByName attribute should be false by default", nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testNullPropertyPointerGetName() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        QName expectedName = new QName("testProperty");
        QName actualName = nullPropertyPointer.getName();
        Assert.assertEquals("getName should return the expected QName", expectedName, actualName);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullPropertyPointerSetPropertyIndex() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyIndex(42);
    }

    @Test
    public void testNullPropertyPointerGetLength() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Length should be 0", 0, nullPropertyPointer.getLength());
    }

    @Test
    public void testNullPropertyPointerGetBaseValue() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNull("Base value should be null", nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testNullPropertyPointerGetImmediateNode() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNull("Immediate node should be null", nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testNullPropertyPointerIsLeaf() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertTrue("Should be a leaf", nullPropertyPointer.isLeaf());
    }

    @Test
    public void testNullPropertyPointerValuePointer() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expectedValuePointer = new NullPointer(nullPropertyPointer, new QName("*"));
        NodePointer actualValuePointer = nullPropertyPointer.getValuePointer();
        Assert.assertSame("Value pointer should be the expected NullPointer", expectedValuePointer, actualValuePointer);
    }

    @Test
    public void testNullPropertyPointerIsActualProperty() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse("Is actual property should be false", nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testNullPropertyPointerIsActual() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse("Is actual should be false", nullPropertyPointer.isActual());
    }

    @Test
    public void testNullPropertyPointerIsContainer() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertTrue("Should be a container", nullPropertyPointer.isContainer());
    }

    @Test
    public void testNullPropertyPointerSetValue() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        try {
            nullPropertyPointer.setValue(value);
            Assert.fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }
    }

    @Test
    public void testNullPropertyPointerCreatePath() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        NodePointer actualPath = nullPropertyPointer.createPath(context);
        Assert.assertNull("Path should be null", actualPath);
    }

    @Test
    public void testNullPropertyPointerCreatePathWithValue() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        Object value = new Object();
        NodePointer actualPath = nullPropertyPointer.createPath(context, value);
        Assert.assertNull("Path should be null", actualPath);
    }

    @Test
    public void testNullPropertyPointerCreateChild() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        QName name = new QName("testName");
        NodePointer actualChild = nullPropertyPointer.createChild(context, name);
        Assert.assertNull("Child should be null", actualChild);
    }

    @Test
    public void testNullPropertyPointerCreateChildWithValue() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        QName name = new QName("testName");
        Object value = new Object();
        NodePointer actualChild = nullPropertyPointer.createChild(context, name, 0, value);
        Assert.assertNull("Child should be null", actualChild);
    }

    @Test
    public void testNullPropertyPointerGetPropertyName() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        String expectedPropertyName = "testProperty";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        Assert.assertEquals("getPropertyName should return the expected property name", expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testNullPropertyPointerSetPropertyName() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String newPropertyName = "newPropertyName";
        nullPropertyPointer.setPropertyName(newPropertyName);
        String expectedPropertyName = "newPropertyName";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        Assert.assertEquals("getPropertyName should return the newly set property name", expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testNullPropertyPointerSetNameAttributeValue() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("testAttributeValue");
        String expectedPropertyName = "testAttributeValue";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        Assert.assertEquals("getPropertyName should return the expected name attribute value", expectedPropertyName, actualPropertyName);
        Assert.assertTrue("ByName attribute should be true", nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testNullPropertyPointerIsCollection() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse("Should not be a collection", nullPropertyPointer.isCollection());
    }

    @Test
    public void testNullPropertyPointerPropertyCount() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Property count should be 0", 0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testNullPropertyPointerPropertyNames() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] expectedNames = new String[0];
        String[] actualNames = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals("getPropertyNames should return an empty array", expectedNames, actualNames);
    }

    @Test
    public void testNullPropertyPointerAsPath() {
        NodePointer parent = new NodePointer(null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("As path should be the empty string by default", "", nullPropertyPointer.asPath());

        nullPropertyPointer.setPropertyName("testProperty");
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        nullPropertyPointer.setIndex(42);
        String expectedPath = "[@testAttribute='']/testProperty[42]";
        String actualPath = nullPropertyPointer.asPath();
        Assert.assertEquals("As path should return the expected path", expectedPath, actualPath);
    }
}