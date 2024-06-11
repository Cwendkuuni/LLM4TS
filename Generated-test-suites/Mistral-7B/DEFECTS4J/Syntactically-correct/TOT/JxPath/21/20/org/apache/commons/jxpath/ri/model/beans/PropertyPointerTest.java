package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.util.SimpleConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PropertyPointerTest {

    @Test
    public void testPropertyPointer() {
        Object parent = new Object() {};
        NodePointer parentPointer = NodePointer.newChildNodePointer(
                null,
                null,
                parent);
        PropertyPointer propertyPointer1 = new MyPropertyPointer(parentPointer);
        PropertyPointer propertyPointer2 = new MyPropertyPointer(parentPointer);

        // Test equals
        Assert.assertEquals(propertyPointer1, propertyPointer2);

        // Test hashCode
        Assert.assertEquals(propertyPointer1.hashCode(), propertyPointer2.hashCode());

        // Test getPropertyIndex and setPropertyIndex
        propertyPointer1.setPropertyIndex(0);
        Assert.assertEquals(0, propertyPointer1.getPropertyIndex());
        propertyPointer2.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer2.getPropertyIndex());
        Assert.assertNotEquals(propertyPointer1, propertyPointer2);

        // Test getBean
        Assert.assertEquals(parent, propertyPointer1.getBean());
        Assert.assertEquals(parent, propertyPointer2.getBean());

        // Test createPath
        JXPathContext context = new JXPathContext();
        Object bean = new TestBean();
        propertyPointer1.setPropertyName("property1");
        NodePointer actual = propertyPointer1.createPath(context, bean);
        NodePointer expected = NodePointer.newChildNodePointer(
                propertyPointer1, null, bean, "property1");
        Assert.assertEquals(expected, actual);

        // Test isActual
        Assert.assertTrue(propertyPointer1.isActual());
        propertyPointer2.setPropertyName(null);
        Assert.assertFalse(propertyPointer2.isActual());

        // Test getImmediateNode
        Assert.assertEquals(bean, propertyPointer1.getImmediateNode());

        // Test getLength
        TestBean testBean = new TestBean();
        testBean.setList(Arrays.asList("one", "two", "three"));
        propertyPointer1.setPropertyName("list");
        int length = propertyPointer1.getLength();
        Assert.assertEquals(3, length);

        // Test getImmediateValuePointer
        NodePointer immediateValuePointer = propertyPointer1.getImmediateValuePointer();
        Assert.assertEquals(testBean.getList().get(0), immediateValuePointer.getNode());

        // Test createPath with value
        propertyPointer1.setIndex(1);
        Object value = "two";
        NodePointer createdPath = propertyPointer1.createPath(context, value);
        Assert.assertEquals(testBean.getList().get(1), createdPath.getNode());

        // Test createChild
        NodePointer child = propertyPointer1.createChild(context, null, 1, "three");
        Assert.assertEquals(testBean.getList().get(2), child.getNode());

        // Test isCollection
        Assert.assertTrue(propertyPointer1.isCollection());
        propertyPointer1.setPropertyName("booleanProperty");
        Assert.assertFalse(propertyPointer1.isCollection());

        // Test isLeaf
        Assert.assertFalse(propertyPointer1.isLeaf());
        propertyPointer1.setPropertyName("property2");
        propertyPointer1.setIndex(2);
        Assert.assertTrue(propertyPointer1.isLeaf());

        // Test createPath with collection
        propertyPointer1.setPropertyName("list");
        Object list = testBean.getList();
        NodePointer nodePointer = propertyPointer1.createPath(context);
        Assert.assertEquals(list, nodePointer.getNode());

        // Test createPath with null bean
        propertyPointer1.setBean(null);
        Assert.assertNull(propertyPointer1.getBean());
        propertyPointer1.createPath(context);
        Assert.assertNull(propertyPointer1.getNode());
    }

    public static class MyPropertyPointer extends PropertyPointer {
        public MyPropertyPointer(NodePointer parent) {
            super(parent);
        }

        @Override
        public String getPropertyName() {
            return "property";
        }

        @Override
        public int getPropertyCount() {
            return SimpleConstants.SIMPLE_VALUE_PROPERTY_COUNT;
        }

        @Override
        public String[] getPropertyNames() {
            return new String[]{"property1", "property2"};
        }

        @Override
        public boolean isActualProperty() {
            return true;
        }
    }

    public static class TestBean {
        private Integer integerProperty;
        private String stringProperty;
        private boolean booleanProperty;
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public Integer getIntegerProperty() {
            return integerProperty;
        }

        public void setIntegerProperty(Integer integerProperty) {
            this.integerProperty = integerProperty;
        }

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }

        public boolean isBooleanProperty() {
            return booleanProperty;
        }

        public void setBooleanProperty(boolean booleanProperty) {
            this.booleanProperty = booleanProperty;
        }
    }
}