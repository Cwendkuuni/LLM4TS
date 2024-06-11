import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerConstructor() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        Assert.assertNotNull(propertyPointer);
        Assert.assertNull(propertyPointer.getPropertyName());
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());
        Assert.assertNull(propertyPointer.getBean());
    }

    @Test
    public void testGetPropertyIndex() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyIndex(123);
        Assert.assertEquals(123, propertyPointer.getPropertyIndex());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPropertyIndexInvalidIndex() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyIndex(-1);
    }

    @Test
    public void testGetBean() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        Object bean = new Object();
        propertyPointer.setBean(bean);
        Assert.assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        String propertyName = "propertyName";
        propertyPointer.setPropertyName(propertyName);
        Assert.assertEquals(new QName("name", "propertyName"), propertyPointer.getName());
    }

    @Test
    public void testGetPropertyCount() throws Exception {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public int getPropertyCount() {
                return 3;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"p1", "p2", "p3"};
            }
        };
        Assert.assertEquals(3, propertyPointer.getPropertyCount());
        Assert.assertArrayEquals(new String[]{"p1", "p2", "p3"}, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            protected boolean isActualProperty() {
                return true;
            }
        };
        Assert.assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return new String[]{"value1", "value2"};
            }
        };
        Assert.assertEquals("value1", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return new String[]{"value1", "value2"};
            }
        };
        Assert.assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return "value";
            }
        };
        Assert.assertFalse(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return new String[]{"value1", "value2"};
            }
        };
        Assert.assertEquals(2, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return new String[]{"value1", "value2"};
            }
        };
        NodePointer immediateValuePointer = propertyPointer.getImmediateValuePointer();
        Assert.assertNotNull(immediateValuePointer);
        Assert.assertEquals(new QName("name", "propertyName"), immediateValuePointer.getName());
        Assert.assertEquals("value1", immediateValuePointer.getNode());
    }

    @Test
    public void testCreatePath() throws Exception {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return new String[]{"value1", "value2"};
            }
        };
        JXPathContext context = new JXPathContext(new Object(), null);
        NodePointer result = propertyPointer.createPath(context);
        Assert.assertEquals(new PropertyPointer(parentPointer, 0, "value1"), result);
    }

    @Test
    public void testCreatePathWithValue() throws Exception {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return new String[]{"value1", "value2"};
            }
        };
        JXPathContext context = new JXPathContext(new Object(), null);
        NodePointer result = propertyPointer.createPath(context, "newValue");
        Assert.assertEquals(new PropertyPointer(parentPointer, 0, "newValue"), result);
    }

    @Test
    public void testCreateChildNodePointer() throws Exception {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return new String[]{"value1", "value2"};
            }
        };
        JXPathContext context = new JXPathContext(new Object(), null);
        NodePointer child1 = propertyPointer.createChild(context, null, 0, "newValue1");
        NodePointer child2 = propertyPointer.createChild(context, null, 1, "newValue2");
        Assert.assertEquals(new PropertyPointer(parentPointer, 0, "newValue1"), child1);
        Assert.assertEquals(new PropertyPointer(parentPointer, 0, "newValue2"), child2);
    }

    @Test
    public void testCreateChildNodePointerWithName() throws Exception {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public Object getBaseValue() {
                return new String[]{"value1", "value2"};
            }
        };
        JXPathContext context = new JXPathContext(new Object(), null);
        NodePointer child1 = propertyPointer.createChild(context, new QName("namespace", "newName1"), 0, "newValue1");
        NodePointer child2 = propertyPointer.createChild(context, new QName("namespace", "newName2"), 1, "newValue2");
        Assert.assertEquals(new PropertyPointer(parentPointer, 0, "newName1", "newValue1"), child1);
        Assert.assertEquals(new PropertyPointer(parentPointer, 0, "newName2", "newValue2"), child2);
    }

    @Test
    public void testHashCode() {
        NodePointer parentPointer1 = new NodePointer(null, "bean1", "name1");
        NodePointer parentPointer2 = new NodePointer(null, "bean2", "name2");
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer1) {
            @Override
            public int getPropertyIndex() {
                return 123;
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer2) {
            @Override
            public int getPropertyIndex() {
                return 456;
            }
        };
        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer1.hashCode());
        Assert.assertEquals(propertyPointer1.hashCode(), parentPointer1.hashCode() + 123 + Integer.MIN_VALUE);
        Assert.assertEquals(propertyPointer2.hashCode(), propertyPointer2.hashCode());
        Assert.assertEquals(propertyPointer2.hashCode(), parentPointer2.hashCode() + 456 + Integer.MIN_VALUE);
        Assert.assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
        Assert.assertNotEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer parentPointer1 = new NodePointer(null, "bean1", "name1");
        NodePointer parentPointer2 = new NodePointer(null, "bean2", "name2");
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer1) {
            @Override
            public int getPropertyIndex() {
                return 123;
            }
        };
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer2) {
            @Override
            public int getPropertyIndex() {
                return 123;
            }
        };
        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer1) {
            @Override
            public int getPropertyIndex() {
                return 456;
            }
        };
        PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer2) {
            @Override
            public int getPropertyIndex() {
                return 456;
            }
        };
        Assert.assertEquals(propertyPointer1, propertyPointer1);
        Assert.assertTrue(propertyPointer1.equals(propertyPointer1));
        Assert.assertFalse(propertyPointer1.equals(null));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer2));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer3));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer4));
        Assert.assertTrue(propertyPointer2.equals(propertyPointer1));
        Assert.assertTrue(propertyPointer1.equals(propertyPointer2));
        Assert.assertTrue(propertyPointer3.equals(propertyPointer4));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer parentPointer = new NodePointer(null, "bean", "name");
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
        NodePointer child1 = new NodePointer(propertyPointer1, null, "child1");
        NodePointer child2 = new NodePointer(propertyPointer2, null, "child2");
        Assert.assertEquals(0, propertyPointer1.compareChildNodePointers(child1, child2));
        Assert.assertEquals(0, propertyPointer2.compareChildNodePointers(child1, child2));
        Assert.assertEquals(-1, propertyPointer1.compareChildNodePointers(child2, child1));
        Assert.assertEquals(1, propertyPointer2.compareChildNodePointers(child1, child2));
    }
}