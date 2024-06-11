import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("testProperty");
        QName name = pointer.getName();
        Assert.assertEquals("testProperty", name.getLocalPart());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue(new Object());
    }

    @Test
    public void testCreatePath_EmptyContext() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();
        NodePointer createdPath = pointer.createPath(context);
        Assert.assertNull(createdPath);
    }

    @Test
    public void testCreatePath_PropertyOwnerParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        JXPathContext context = new JXPathContext();
        NodePointer createdPath = pointer.createPath(context);
        Assert.assertNotNull(createdPath);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NonPropertyParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(new DOMNodePointer(null, null, null, null));
        pointer.setValue(new Object());
    }

    @Test
    public void testCreatePath_NonPropertyParent_CreateAttribute() {
        NullPropertyPointer pointer = new NullPropertyPointer(new DOMNodePointer(null, null, null, null));
        JXPathContext context = new JXPathContext();
        NodePointer createdPath = pointer.createPath(context);
        Assert.assertNotNull(createdPath);
        Assert.assertTrue(createdPath instanceof AttributeNodePointer);
    }

    @Test
    public void testSetValue_PropertyOwnerParent_DynamicProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        JXPathContext context = new JXPathContext();
        pointer.setValue(new Object());
        NodePointer createdPath = pointer.createPath(context);
        Assert.assertNotNull(createdPath);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_PropertyOwnerParent_NonDynamicProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        pointer.setPropertyName("nonExistentProperty");
        pointer.setValue(new Object());
    }

    @Test
    public void testCreatePath_PropertyOwnerParent_CreateChild() {
        NullPropertyPointer pointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        JXPathContext context = new JXPathContext();
        NodePointer createdPath = pointer.createPath(context);
        Assert.assertNotNull(createdPath);
        Assert.assertTrue(createdPath instanceof PropertyPointer);
        PropertyPointer propertyPointer = (PropertyPointer) createdPath;
        Assert.assertEquals("", propertyPointer.getPropertyName());
        propertyPointer.setPropertyName("testProperty");
        NodePointer createdChild = propertyPointer.createChild(context, new QName("testChild"));
        Assert.assertNotNull(createdChild);
        Assert.assertEquals("testProperty", createdChild.asPath());
    }

    @Test
    public void testCreatePath_PropertyOwnerParent_CreateChild_Value() {
        NullPropertyPointer pointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        JXPathContext context = new JXPathContext();
        NodePointer createdPath = pointer.createPath(context);
        Assert.assertNotNull(createdPath);
        Assert.assertTrue(createdPath instanceof PropertyPointer);
        PropertyPointer propertyPointer = (PropertyPointer) createdPath;
        Assert.assertEquals("", propertyPointer.getPropertyName());
        propertyPointer.setPropertyName("testProperty");
        NodePointer createdChild = propertyPointer.createChild(context, new QName("testChild"), new Object());
        Assert.assertNotNull(createdChild);
        Assert.assertEquals("testProperty[testChild]", createdChild.asPath());
    }

    @Test
    public void testCreateChild_PropertyOwnerParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        JXPathContext context = new JXPathContext();
        NodePointer createdChild = pointer.createChild(context, new QName("testChild"));
        Assert.assertNotNull(createdChild);
        Assert.assertEquals("testChild", createdChild.asPath());
    }

    @Test
    public void testCreateChild_PropertyOwnerParent_Value() {
        NullPropertyPointer pointer = new NullPropertyPointer(new PropertyOwnerPointer(null));
        JXPathContext context = new JXPathContext();
        NodePointer createdChild = pointer.createChild(context, new QName("testChild"), new Object());
        Assert.assertNotNull(createdChild);
        Assert.assertEquals("testChild", createdChild.asPath());
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);       
        Assert.assertEquals("*", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("testProperty");
        Assert.assertEquals("testProperty", pointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("testProperty");
        Assert.assertEquals("testProperty", pointer.getPropertyName());
        Assert.assertTrue(pointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertFalse(pointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String[] propertyNames = pointer.getPropertyNames();
        Assert.assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Assert.assertEquals("", pointer.asPath());
        pointer.setPropertyName("testProperty");
        Assert.assertEquals("testProperty", pointer.asPath());
        pointer.setNameAttributeValue("testProperty");
        Assert.assertEquals("[@name='testProperty']", pointer.asPath());
    }
}