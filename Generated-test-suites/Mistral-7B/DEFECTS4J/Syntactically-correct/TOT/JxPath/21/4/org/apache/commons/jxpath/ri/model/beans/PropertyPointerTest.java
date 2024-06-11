package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointerWithActualProperty() {
        Object parentBean = new Object() {
            public String getProperty1() {
                return "parentProperty1";
            }

            public String getProperty2() {
                return "parentProperty2";
            }
        };

        PropertyPointer propertyPointer = new PropertyPointer(
                NodePointer.newChildNodePointer(
                        null,
                        new QName("", "property1"),
                        null,
                        parentBean)) {
            @Override
            public String getPropertyName() {
                return "property1";
            }
        };

        JXPathContext context = new JXPathContext(parentBean);
        Object value = propertyPointer.createPath(context).getValue();
        Assert.assertEquals("parentProperty1", value);
    }

    @Test
    public void testPropertyPointerWithCollectionProperty() {
        Object parentBean = new Object() {
            Object[] array = new Object[]{"element1", "element2"};

            public Object[] getArrayProperty() {
                return array;
            }
        };

        PropertyPointer propertyPointer = new PropertyPointer(
                NodePointer.newChildNodePointer(
                        null,
                        new QName("", "arrayProperty"),
                        null,
                        parentBean)) {
            @Override
            public String getPropertyName() {
                return "arrayProperty";
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"element1", "element2"};
            }

            @Override
            public boolean isActualProperty() {
                return false;
            }
        };

        JXPathContext context = new JXPathContext(parentBean);
        Object firstElement = propertyPointer.createPath(context, 0).getValue();
        Assert.assertEquals("element1", firstElement);
        Object secondElement = propertyPointer.createPath(context, 1).getValue();
        Assert.assertEquals("element2", secondElement);
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testPropertyPointerWithInvalidPropertyName() {
        Object parentBean = new Object();

        PropertyPointer propertyPointer = new PropertyPointer(
                NodePointer.newChildNodePointer(
                        null,
                        new QName("", "invalidPropertyName"),
                        null,
                        parentBean)) {
            @Override
            public String getPropertyName() {
                return "invalidPropertyName";
            }
        };

        JXPathContext context = new JXPathContext(parentBean);
        propertyPointer.createPath(context);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPropertyPointerWithOutOfBoundsIndex() {
        Object parentBean = new Object() {
            String[] stringArray = new String[]{"element1", "element2"};

            public String[] getStringArrayProperty() {
                return stringArray;
            }
        };

        PropertyPointer propertyPointer = new PropertyPointer(
                NodePointer.newChildNodePointer(
                        null,
                        new QName("", "stringArrayProperty"),
                        null,
                        parentBean)) {
            @Override
            public String getPropertyName() {
                return "stringArrayProperty";
            }

            @Override
            public int getPropertyCount() {
                return 2;
            }

            @Override
            public String[] getPropertyNames() {
                return new String[]{"element1", "element2"};
            }

            @Override
            public boolean isActualProperty() {
                return false;
            }
        };

        JXPathContext context = new JXPathContext(parentBean);
        propertyPointer.createPath(context, 3);
    }

    @Test
    public void testPropertyPointerWithParentPointerNull() {
        Object parentBean = new Object();

        PropertyPointer propertyPointer = new PropertyPointer(null) {
            @Override
            public Object getBean() {
                return parentBean;
            }

            @Override
            public String getPropertyName() {
                throw new UnsupportedOperationException();
            }
        };

        JXPathContext context = new JXPathContext(parentBean);
        propertyPointer.createPath(context);
    }
}