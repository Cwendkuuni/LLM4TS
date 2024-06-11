import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerConstruction() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        Assert.assertNotNull(propertyPointer);
    }

    @Test
    public void testGetPropertyIndex() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        Assert.assertEquals(-1, propertyPointer.getPropertyIndex());

        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        Assert.assertNotNull(propertyPointer.getBean());

        propertyPointer.setBean(new Object());
        Assert.assertEquals(propertyPointer.getBean(), propertyPointer.getBean());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetBeanWhenUninitialized() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.getBean();
    }

    @Test
    public void testGetName() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        String expectedName = "testProperty";
        propertyPointer.setPropertyName(expectedName);
        String actualName = propertyPointer.getName().getLocalPart();
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        Assert.assertNull(propertyPointer.getPropertyName());

        String expectedName = "testProperty";
        propertyPointer.setPropertyName(expectedName);
        String actualName = propertyPointer.getPropertyName();
        Assert.assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parentPointer = new NodePointer(null);
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        String expectedName = "testProperty";
        propertyPointer.setPropertyName(expectedName);
        Assert.assertEquals(expectedName, propertyPointer.getPropertyName());

        String newName = "anotherTestProperty";
        propertyPointer.setPropertyName(newName);
        Assert.assertEquals(newName, propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int testProperty1;
            int testProperty2;

            public int getTestProperty1() {
                return testProperty1;
            }

            public int getTestProperty2() {
                return testProperty2;
            }
        });
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
        propertyPointer1.setPropertyName("testProperty1");
        propertyPointer2.setPropertyName("testProperty2");

        Assert.assertEquals(2, propertyPointer1.getPropertyCount());
        Assert.assertEquals(2, propertyPointer2.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int testProperty1;
            int testProperty2;

            public int getTestProperty1() {
                return testProperty1;
            }

            public int getTestProperty2() {
                return testProperty2;
            }
        });
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
        propertyPointer1.setPropertyName("testProperty1");
        propertyPointer2.setPropertyName("testProperty2");

        String[] expectedPropertyNames = {"testProperty1", "testProperty2"};
        String[] actualPropertyNames = propertyPointer1.getPropertyNames();
        Assert.assertArrayEquals(expectedPropertyNames, actualPropertyNames);
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int testProperty;
        });
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");
        Assert.assertTrue(propertyPointer.isActualProperty());

        NodePointer nonPropertyPointer = new NodePointer(new Object());
        PropertyPointer nonPropertyPropertyPointer = new PropertyPointer(nonPropertyPointer);
        Assert.assertFalse(nonPropertyPropertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNodeWhenActualProperty() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int testProperty;
        });
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        int expectedValue = 42;
        Object actualValue = propertyPointer.getImmediateNode();
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetImmediateNodeWhenCollection() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int[] testProperty;
        });
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setIndex(1);

        int[] expectedValue = {1, 2, 3};
        Object actualValue = propertyPointer.getImmediateNode();
        Assert.assertArrayEquals(expectedValue, (int[]) actualValue);
    }

    @Test
    public void testGetImmediateNodeWhenUninitialized() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        Assert.assertNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int[] testProperty;
        });
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        Assert.assertTrue(propertyPointer.isCollection());

        NodePointer parentPointer2 = new NodePointer(new Object());
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer2);
        propertyPointer2.setPropertyName("testProperty");
        propertyPointer2.setIndex(0);

        Assert.assertFalse(propertyPointer2.isCollection());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        Assert.assertFalse(propertyPointer.isLeaf());

        NodePointer leafNodePointer = new NodePointer(new Object());
        PropertyPointer leafPropertyPointer = new PropertyPointer(leafNodePointer);
        leafPropertyPointer.setPropertyName("testProperty");

        Assert.assertTrue(leafPropertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int[] testProperty;
        });
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        int expectedLength = 3;
        int actualLength = propertyPointer.getLength();
        Assert.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testCreatePath() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        JXPathContext context = new JXPathContext(new Object());
        NodePointer expectedPath = new NodePointer(new Object() {
            int testProperty;
        });

        NodePointer actualPath = propertyPointer.createPath(context);
        Assert.assertEquals(expectedPath, actualPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePathWithInvalidIndex() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int[] testProperty;
        });
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        JXPathContext context = new JXPathContext(new Object());
        propertyPointer.createPath(context, 5);
    }

    @Test
    public void testCreateChild() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        JXPathContext context = new JXPathContext(new Object());
        NodePointer expectedChild = new NodePointer(new Object() {
            int testChildProperty;
        });

        NodePointer actualChild = propertyPointer.createChild(context, null, 0, null);
        Assert.assertEquals(expectedChild, actualChild);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateChildWithNullName() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        JXPathContext context = new JXPathContext(new Object());
        propertyPointer.createChild(context, null, 0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildWithInvalidIndex() {
        NodePointer parentPointer = new NodePointer(new Object() {
            int[] testProperty;
        });
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        JXPathContext context = new JXPathContext(new Object());
        propertyPointer.createChild(context, null, 3, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildWithNullValue() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
        propertyPointer.setPropertyName("testProperty");

        JXPathContext context = new JXPathContext(new Object());
        propertyPointer.createChild(context, null, 0, null);
        propertyPointer.setValue(42);

        try {
            propertyPointer.createChild(context, null, 0, null);
        } catch (InvocationTargetException e) {
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testHashCode() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
        propertyPointer1.setPropertyName("testProperty1");
        propertyPointer2.setPropertyName("testProperty2");

        int expectedHashCode1 = propertyPointer1.hashCode();
        int expectedHashCode2 = propertyPointer2.hashCode();

        int actualHashCode1 = propertyPointer1.hashCode();
        int actualHashCode2 = propertyPointer2.hashCode();

        Assert.assertNotEquals(expectedHashCode1, expectedHashCode2);
        Assert.assertEquals(expectedHashCode1, actualHashCode1);
        Assert.assertEquals(expectedHashCode2, actualHashCode2);
    }

    @Test
    public void testEquals() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
        propertyPointer1.setPropertyName("testProperty1");
        propertyPointer2.setPropertyName("testProperty2");

        PropertyPointer propertyPointer3 = new PropertyPointer(parentPointer);
        propertyPointer3.setPropertyName("testProperty1");

        NodePointer parentPointer2 = new NodePointer(new Object());
        PropertyPointer propertyPointer4 = new PropertyPointer(parentPointer2);
        propertyPointer4.setPropertyName("testProperty1");

        Assert.assertTrue(propertyPointer1.equals(propertyPointer1));
        Assert.assertTrue(propertyPointer1.equals(propertyPointer3));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer2));
        Assert.assertFalse(propertyPointer1.equals(propertyPointer4));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer parentPointer = new NodePointer(new Object());
        PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
        NodePointer valueNodePointer1 = new NodePointer(42);
        NodePointer valueNodePointer2 = new NodePointer(42);

        int expectedValue = 0;
        int actualValue = propertyPointer1.compareChildNodePointers(propertyPointer2, valueNodePointer1);
        Assert.assertEquals(expectedValue, actualValue);

        expectedValue = -1;
        actualValue = propertyPointer1.compareChildNodePointers(propertyPointer2, valueNodePointer2);
        Assert.assertEquals(expectedValue, actualValue);

        expectedValue = 1;
        actualValue = propertyPointer1.compareChildNodePointers(propertyPointer2, valueNodePointer1);
        Assert.assertEquals(expectedValue, actualValue);
    }
}