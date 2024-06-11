import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerInstance() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Expected null, but was: " + nullPropertyPointer.getBaseValue(), null, nullPropertyPointer.getBaseValue());
        Assert.assertTrue("Expected true, but was: " + nullPropertyPointer.isLeaf(), nullPropertyPointer.isLeaf());
        Assert.assertNull("Expected null, but was: " + nullPropertyPointer.getValuePointer().getBaseValue(), nullPropertyPointer.getValuePointer().getBaseValue());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        Assert.assertEquals("Expected testProperty, but was: " + nullPropertyPointer.getPropertyName(), "testProperty", nullPropertyPointer.getPropertyName());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueThrowsExceptionWhenParentIsNull() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueThrowsExceptionWhenParentIsNotPropertyOwner() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testSetValueDoesNotChangeParentPropertyOwner() {
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
        Assert.assertFalse("Expected false, but was: " + parent.getValue("testProperty") == null, parent.getValue("testProperty") == null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathThrowsExceptionWhenParentIsNull() {
        NodePointer parent = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createPath(new JXPathContext());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathThrowsExceptionWhenParentIsNotPropertyOwner() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createPath(new JXPathContext());
    }

    @Test
    public void testCreatePathReturnsNullPointer() {
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext());
        Assert.assertEquals("Expected NullPointer, but was: " + result.getClass().getName(), NullPointer.class, result.getClass());
    }

    @Test
    public void testSetPropertyNameAndAttributeValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        Assert.assertEquals("Expected testAttribute, but was: " + nullPropertyPointer.getPropertyName(), "testAttribute", nullPropertyPointer.getPropertyName());
        Assert.assertEquals("Expected [@name='testAttribute'], but was: " + nullPropertyPointer.asPath(), "[@name='testAttribute']", nullPropertyPointer.asPath());
    }

    @Test
    public void testIsCollectionReturnsFalse() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertFalse("Expected false, but was: " + nullPropertyPointer.isCollection(), nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCountReturnsZero() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Expected 0, but was: " + nullPropertyPointer.getPropertyCount(), 0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNamesReturnsEmptyArray() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals("Expected empty array, but was: " + propertyNames.length, new String[0], propertyNames);
    }

    @Test
    public void testAsPathReturnsEmptyStringWhenByNameAttributeIsFalse() {
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertEquals("Expected '', but was: " + nullPropertyPointer.asPath(), "", nullPropertyPointer.asPath());
    }

    @Test
    public void testAsPathReturnsAttributePathWhenByNameAttributeIsTrue() {
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        Assert.assertEquals("Expected [@name='testAttribute'], but was: " + nullPropertyPointer.asPath(), "[@name='testAttribute']", nullPropertyPointer.asPath());
    }
}