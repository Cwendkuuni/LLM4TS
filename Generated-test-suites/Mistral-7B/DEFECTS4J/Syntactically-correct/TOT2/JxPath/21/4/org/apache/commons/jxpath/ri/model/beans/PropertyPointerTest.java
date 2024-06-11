import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.SimplePropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerConstructor() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new SimplePropertyPointer(parent);
        Assert.assertNotNull(propertyPointer);
        Assert.assertEquals(PropertyPointer.class, propertyPointer.getClass());
        Assert.assertEquals(parent, propertyPointer.getImmediateParentPointer());
        Assert.assertEquals(UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
        Assert.assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetPropertyIndex() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer());
        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyIndexNegativeValue() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer());
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetBean() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer());
        Object bean = new Object();
        propertyPointer.setBean(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testSetBeanNull() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer());
        propertyPointer.setBean(null);
        Assert.assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer());
        propertyPointer.setPropertyName("testProperty");
        Assert.assertEquals("testProperty", propertyPointer.getName().getLocalPart());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyNameNull() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer());
        propertyPointer.setPropertyName(null);
    }

    @Test
    public void testGetPropertyCount() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public int getPropertyCount() {
                return 3;
            }
        };
        Assert.assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public String[] getPropertyNames() {
                return new String[]{"testProperty1", "testProperty2", "testProperty3"};
            }
        };
        List<String> propertyNames = Arrays.asList(propertyPointer.getPropertyNames());
        Assert.assertEquals(3, propertyNames.size());
        Assert.assertTrue(propertyNames.contains("testProperty1"));
        Assert.assertTrue(propertyNames.contains("testProperty2"));
        Assert.assertTrue(propertyNames.contains("testProperty3"));
    }

    @Test
    public void testIsActualProperty() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public Object getImmediateNode() {
                return "testValue";
            }
        };
        Assert.assertEquals("testValue", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public boolean isCollection() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public boolean isLeaf() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public int getLength() {
                return 5;
            }
        };
        Assert.assertEquals(5, propertyPointer.getLength());
    }

    @Test
    public void testCreatePath() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public NodePointer createPath(JXPathContext context) {
                return new NodePointer();
            }
        };
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = propertyPointer.createPath(context);
        Assert.assertNotNull(nodePointer);
    }

    @Test
    public void testCreatePathWithValue() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public NodePointer createPath(JXPathContext context, Object value) {
                return new NodePointer();
            }
        };
        JXPathContext context = new JXPathContext();
        NodePointer nodePointer = propertyPointer.createPath(context, new Integer(42));
        Assert.assertNotNull(nodePointer);
    }

    @Test
    public void testCreateChild() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
                return new NodePointer();
            }
        };
        JXPathContext context = new JXPathContext();
        NodePointer childNodePointer = propertyPointer.createChild(context, new QName("child"), 1, new Integer(42));
        Assert.assertNotNull(childNodePointer);
    }

    @Test
    public void testCreateChildWithName() {
        SimplePropertyPointer propertyPointer = new SimplePropertyPointer(new NodePointer()) {
            @Override
            public NodePointer createChild(JXPathContext context, QName name) {
                return new NodePointer();
            }
        };
        JXPathContext context = new JXPathContext();
        NodePointer childNodePointer = propertyPointer.createChild(context, new QName("child"));
        Assert.assertNotNull(childNodePointer);
    }
}