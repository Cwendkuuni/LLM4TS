import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyPointerTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Assert.assertNotNull(propertyPointer);
    }

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());

        propertyPointer = new PropertyPointer(new NodePointer(null));
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());

        propertyPointer = new PropertyPointer(new NodePointer(null), 1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBean_beforeSet() {
        NodePointer parent = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        propertyPointer.getBean();
    }

    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Assert.assertNull(propertyPointer.getName());

        propertyPointer = new PropertyPointer(new NodePointer(null), "name");
        Assert.assertEquals("name", propertyPointer.getName().getLocalPart());
    }

    @Test
    public void testGetPropertyCount() throws Exception {
        PropertyPointer propertyPointer = new PropertyPointer(null);
        Assert.assertEquals(-1, propertyPointer.getPropertyCount());

        Object bean = new Object() {
            public String property1;
            public String property2;
        };
        NodePointer parent = new NodePointer(bean);
        propertyPointer = new PropertyPointer(parent, "property1");
        Assert.assertEquals(1, propertyPointer.getPropertyCount());

        propertyPointer = new PropertyPointer(parent, "nonExistentProperty");
        Assert.assertEquals(-1, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() throws Exception {
        Object bean = new Object() {
            public String property1;
            public String property2;
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer1 = new PropertyPointer(parent, "property1");
        PropertyPointer propertyPointer2 = new PropertyPointer(parent, "property2");

        String[] expected = {"property1", "property2"};
        Assert.assertArrayEquals(expected, propertyPointer1.getPropertyNames());
        Assert.assertArrayEquals(expected, propertyPointer2.getPropertyNames());

        PropertyPointer propertyPointer3 = new PropertyPointer(parent, "nonExistentProperty");
        Assert.assertNull(propertyPointer3.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isActual());

        propertyPointer = new PropertyPointer(null) {
            @Override
            protected boolean isActualProperty() {
                return false;
            }
        };
        Assert.assertFalse(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() throws Exception {
        Object bean = new Object() {
            public String property = "value";
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "property");

        Assert.assertEquals("value", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() throws Exception {
        Object bean = new Object() {
            private List<String> list = Collections.singletonList("value");
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "list");

        Assert.assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() throws Exception {
        Object bean = new Object() {
            public String property = "value";
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "property");

        Assert.assertFalse(propertyPointer.isLeaf());

        propertyPointer = new PropertyPointer(parent, "property.value");
        Assert.assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() throws Exception {
        Object bean = new Object() {
            private List<String> list = Arrays.asList("value1", "value2");
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "list");

        Assert.assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() throws Exception {
        Object bean = new Object() {
            public String property = "value";
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "property");

        NodePointer expected = new NodePointer(propertyPointer.getImmediateNode());
        Assert.assertEquals(expected, propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() throws Exception {
        Object bean = new Object() {
            public String property = "value";
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "property");

        JXPathContext context = new JXPathContext(bean);
        NodePointer actual = propertyPointer.createPath(context);
        Assert.assertEquals(propertyPointer, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePath_invalidIndex() throws Exception {
        Object bean = new Object() {
            public String property = "value";
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "property");

        JXPathContext context = new JXPathContext(bean);
        propertyPointer.createPath(context, 10);
    }

    @Test
    public void testCreatePath_value() throws Exception {
        Object bean = new Object() {
            public String property = "value";
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "property");

        JXPathContext context = new JXPathContext(bean);
        NodePointer expected = new NodePointer(bean, "property", 0);
        NodePointer actual = propertyPointer.createPath(context, "newValue");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateChild() throws Exception {
        Object bean = new Object() {
            public String property = "value";
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "property");

        JXPathContext context = new JXPathContext(bean);
        NodePointer expected = new PropertyPointer(context, "newProperty");
        NodePointer actual = propertyPointer.createChild(context, null, 0, "newProperty");
        Assert.assertEquals(expected, actual);

        expected = new PropertyPointer(context, "property.newProperty");
        actual = propertyPointer.createChild(context, null, 0);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChild_invalidIndex() throws Exception {
        Object bean = new Object() {
            public String property = "value";
        };
        NodePointer parent = new NodePointer(bean);
        PropertyPointer propertyPointer = new PropertyPointer(parent, "property");

        JXPathContext context = new JXPathContext(bean);
        propertyPointer.createChild(context, null, 10, "newProperty");
    }
}