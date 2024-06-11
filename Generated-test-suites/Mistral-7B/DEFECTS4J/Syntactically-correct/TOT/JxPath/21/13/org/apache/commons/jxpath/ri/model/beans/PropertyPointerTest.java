package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for PropertyPointer.
 */
public class PropertyPointerTest {

    @Test
    public void testPropertyPointerWithActualProperty() {
        // Given
        Object parent = new Object() {
            String name = "John Doe";
            String age = "30";
        };
        NodePointer parentPointer = NodePointer.newChildNodePointer(
            null,
            new QName("root", "parent"),
            parent);
        PropertyPointer propertyPointer = new MyPropertyPointer(parentPointer);

        // When
        int propertyIndex = 0;
        propertyPointer.setPropertyIndex(propertyIndex);
        String expectedPropertyName = "name";
        propertyPointer.setPropertyName(expectedPropertyName);

        // Then
        assertEquals(propertyIndex, propertyPointer.getPropertyIndex());
        assertEquals(expectedPropertyName, propertyPointer.getPropertyName());
        assertEquals(parent, propertyPointer.getBean());
        assertEquals(expectedPropertyName, propertyPointer.getName().getLocalPart());
        assertEquals("John Doe", propertyPointer.getValue(new JXPathContext()));

        // When setting value
        String newValue = "Jane Doe";
        propertyPointer.setValue(newValue);
        assertEquals(newValue, propertyPointer.getValue(new JXPathContext()));
        assertEquals(newValue, parent.getClass().getField(expectedPropertyName).get(parent));

        // When getting length
        assertEquals(1, propertyPointer.getLength());

        // When creating child node pointer
        NodePointer childNodePointer = propertyPointer.createChild(new JXPathContext(), new QName("root", "child"));
        assertEquals(childNodePointer.getNode(), null);
        assertEquals(childNodePointer.getName().getLocalPart(), "child");
        assertEquals(propertyPointer.getName().getLocalPart(), "name");

        // When creating path
        NodePointer pathNodePointer = propertyPointer.createPath(new JXPathContext());
        assertEquals(pathNodePointer.getNode(), parent);
        assertEquals(pathNodePointer.getName().toString(), expectedPropertyName);
    }

    @Test
    public void testPropertyPointerWithCollection() {
        // Given
        Object parent = new Object() {
            String[] names = {"John Doe", "Jane Doe"};
        };
        NodePointer parentPointer = NodePointer.newChildNodePointer(
            null,
            new QName("root", "parent"),
            parent);
        PropertyPointer propertyPointer = new MyCollectionPropertyPointer(parentPointer);

        // When
        int propertyIndex = 1;
        propertyPointer.setPropertyIndex(propertyIndex);
        String expectedPropertyName = "names";
        propertyPointer.setPropertyName(expectedPropertyName);

        // Then
        assertEquals(propertyIndex, propertyPointer.getPropertyIndex());
        assertEquals(expectedPropertyName, propertyPointer.getPropertyName());
        assertEquals(parent, propertyPointer.getBean());
        assertEquals(expectedPropertyName, propertyPointer.getName().getLocalPart());
        assertEquals("Jane Doe", propertyPointer.getValue(new JXPathContext(), 1));

        // When setting value
        String newValue = "Mary Doe";
        propertyPointer.setValue(newValue, 1);
        assertEquals(newValue, propertyPointer.getValue(new JXPathContext(), 1));
        assertEquals(newValue, parent.getClass().getField("names")
                .get(parent)[1]);

        // When getting length
        assertEquals(2, propertyPointer.getLength());

        // When creating child node pointer
        NodePointer childNodePointer = propertyPointer.createChild(new JXPathContext(), new QName("root", "child"));
        assertEquals(childNodePointer.getNode(), null);
        assertEquals(childNodePointer.getName().getLocalPart(), "child");
        assertEquals(propertyPointer.getName().getLocalPart(), "names");

        // When creating path
        NodePointer pathNodePointer = propertyPointer.createPath(new JXPathContext());
        assertEquals(pathNodePointer.getNode(), parent);
        assertEquals(pathNodePointer.getName().toString(), expectedPropertyName);
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testPropertyPointerWithInvalidParent() {
        // Given
        Object parent = new Object();
        NodePointer parentPointer = NodePointer.newChildNodePointer(
            null,
            new QName("root", "parent"),
            null);
        PropertyPointer propertyPointer = new MyPropertyPointer(parentPointer);

        // When
        propertyPointer.setPropertyName("name");

        // Then
        // Exception should be thrown
        propertyPointer.createPath(new JXPathContext());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testPropertyPointerWithNullPropertyName() {
        // Given
        Object parent = new Object();
        NodePointer parentPointer = NodePointer.newChildNodePointer(
            null,
            new QName("root", "parent"),
            parent);
        PropertyPointer propertyPointer = new MyPropertyPointer(parentPointer);

        // When
        propertyPointer.setPropertyIndex(0);
        propertyPointer.setPropertyName(null);

        // Then
        // Exception should be thrown
        propertyPointer.createPath(new JXPathContext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPropertyPointerWithInvalidIndex() {
        // Given
        Object parent = new Object() {
            String[] names = {"John Doe", "Jane Doe"};
        };
        NodePointer parentPointer = NodePointer.newChildNodePointer(
            null,
            new QName("root", "parent"),
            parent);
        PropertyPointer propertyPointer = new MyCollectionPropertyPointer(parentPointer);

        // When
        propertyPointer.setPropertyName("names");
        propertyPointer.setPropertyIndex(2);

        // Then
        // Exception should be thrown
        propertyPointer.createPath(new JXPathContext());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void testPropertyPointerWithNegativeIndex() {
        // Given
        Object parent = new Object() {
            String[] names = {"John Doe", "Jane Doe"};
        };
        NodePointer parentPointer = NodePointer.newChildNodePointer(
            null,
            new QName("root", "parent"),
            parent);
        PropertyPointer propertyPointer = new MyCollectionPropertyPointer(parentPointer);

        // When
        propertyPointer.setPropertyName("names");
        propertyPointer.setPropertyIndex(-1);

        // Then
        // Exception should be thrown
        propertyPointer.createPath(new JXPathContext());
    }

    private static class MyPropertyPointer extends PropertyPointer {
        public MyPropertyPointer(NodePointer parent) {
            super(parent);
        }

        @Override
        public String getPropertyName() {
            return "name";
        }

        @Override
        public void setPropertyName(String propertyName) {
            super.setPropertyName(propertyName);
        }

        @Override
        public int getPropertyCount() {
            return 1;
        }

        @Override
        public String[] getPropertyNames() {
            return new String[] {"name"};
        }

        @Override
        public boolean isActualProperty() {
            return true;
        }
    }

    private static class MyCollectionPropertyPointer extends PropertyPointer {
        public MyCollectionPropertyPointer(NodePointer parent) {
            super(parent);
        }

        @Override
        public String getPropertyName() {
            return "names";
        }

        @Override
        public void setPropertyName(String propertyName) {
            super.setPropertyName(propertyName);
        }

        @Override
        public int getPropertyCount() {
            return 2;
        }

        @Override
        public String[] getPropertyNames() {
            return new String[] {"names"};
        }

        @Override
        public boolean isActualProperty() {
            return false;
        }
    }
}