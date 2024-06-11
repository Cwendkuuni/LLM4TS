package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private Object bean;

    @Before
    public void setup() {
        bean = new Object() {
            private String property1 = "test";
            private String[] property2 = {"test1", "test2"};

            public String getProperty1() {
                return property1;
            }

            public void setProperty1(String property1) {
                this.property1 = property1;
            }

            public String[] getProperty2() {
                return property2;
            }

            public void setProperty2(String[] property2) {
                this.property2 = property2;
            }
        };

        propertyPointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // noop
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
    }

    @Test
    public void testGetPropertyValue() {
        JXPathContext context = new JXPathContext(bean);
        propertyPointer.createPath(context);

        Assert.assertEquals("test", propertyPointer.getValue(context));
        propertyPointer.setPropertyName("property2");
        propertyPointer.setIndex(1);
        Assert.assertEquals("test2", propertyPointer.getValue(context));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPropertyValueInvalidIndex() {
        JXPathContext context = new JXPathContext(bean);
        propertyPointer.createPath(context);
        propertyPointer.setIndex(2);
        propertyPointer.getValue(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPropertyValueInvalidName() {
        JXPathContext context = new JXPathContext(bean);
        propertyPointer.createPath(context);
        propertyPointer.setPropertyName("invalidProperty");
        propertyPointer.getValue(context);
    }

    @Test
    public void testIsCollection() {
        JXPathContext context = new JXPathContext(bean);
        propertyPointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "property2";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // noop
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1", "property2"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
        propertyPointer.createPath(context);
        Assert.assertTrue(propertyPointer.isCollection());

        propertyPointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // noop
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
        propertyPointer.createPath(context);
        Assert.assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyNames() {
        JXPathContext context = new JXPathContext(bean);
        propertyPointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return null;
            }

            @Override
            public void setPropertyName(String propertyName) {
                // noop
            }

            @Override
            public int getPropertyCount() {
                return -1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[0];
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
        propertyPointer.createPath(context);
        Assert.assertArrayEquals(new String[0], propertyPointer.getPropertyNames(context));

        propertyPointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // noop
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
        propertyPointer.createPath(context);
        List<String> expected = Collections.singletonList("property1");
        List<String> actual = Arrays.asList(propertyPointer.getPropertyNames(context));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(bean);
        propertyPointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "property1";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // noop
            }

            @Override
            public int getPropertyCount() {
                return 1;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
        NodePointer result = propertyPointer.createPath(context);
        Assert.assertEquals("PropertyPointer", result.getClass().getName());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext(bean);
        propertyPointer = new PropertyPointer(NodePointer.ROOT) {
            @Override
            public String getPropertyName() {
                return "property2";
            }

            @Override
            public void setPropertyName(String propertyName) {
                // noop
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"property1", "property2"};
            }

            @Override
            public boolean isActualProperty() {
                return true;
            }
        };
        propertyPointer.createPath(context);
        propertyPointer.setValue("test3");
        NodePointer result = propertyPointer.createPath(context);
        Assert.assertEquals("PropertyPointer", result.getClass().getName());
    }
}