import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link PropertyPointer}.
 */
public class PropertyPointerTest {

    private static final Object TEST_BEAN = new Object() {
        String property1 = "value1";
        String property2 = "value2";

        String getProperty1() {
            return property1;
        }

        String getProperty2() {
            return property2;
        }
    };

    private JXPathContext context;
    private PropertyPointer propertyPointer1;
    private PropertyPointer propertyPointer2;

    @Before
    public void setUp() {
        context = JXPathContext.newContext(TEST_BEAN);
        propertyPointer1 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property1";
            }
        };
        propertyPointer2 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property2";
            }
        };
        propertyPointer1.setParent(context.getDocumentNode());
        propertyPointer2.setParent(context.getDocumentNode());
    }

    @Test
    public void testGetPropertyIndex() {
        // propertyPointer1 has no specified property index
        assertEquals(-1, propertyPointer1.getPropertyIndex());

        // propertyPointer2 has a specified property index of 0
        propertyPointer2.setPropertyIndex(0);
        assertEquals(0, propertyPointer2.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertEquals(TEST_BEAN, propertyPointer1.getBean());
        assertEquals(TEST_BEAN, propertyPointer2.getBean());
    }

    @Test
    public void testIsActualProperty() {
        // propertyPointer1 is an abstract PropertyPointer and does not represent an actual property
        assertFalse(propertyPointer1.isActualProperty());

        // propertyPointer2 represents an actual property
        propertyPointer2.setPropertyName("property1");
        assertTrue(propertyPointer2.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        // propertyPointer1 has no immediate node
        assertNull(propertyPointer1.getImmediateNode());

        // propertyPointer2 represents the property "property1" of TEST_BEAN and has the value "value1" as its immediate node
        propertyPointer2.setPropertyName("property1");
        assertEquals("value1", propertyPointer2.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        // propertyPointer1 is not a collection
        assertFalse(propertyPointer1.isCollection());

        // propertyPointer2 represents the property "property1" of TEST_BEAN, which is a String, and is not a collection
        propertyPointer2.setPropertyName("property1");
        assertFalse(propertyPointer2.isCollection());

        // propertyPointer3 represents the property "property2" of TEST_BEAN, which is also a String but could be a collection if TEST_BEAN had a list or array property with that name
        PropertyPointer propertyPointer3 = new PropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property2";
            }
        };
        propertyPointer3.setParent(context.getDocumentNode());
        assertFalse(propertyPointer3.isCollection());

        // propertyPointer4 represents an array property "property3" of TEST_BEAN with 3 elements
        PropertyPointer propertyPointer4 = new ArrayPropertyPointer(null) {
            @Override
            public String getPropertyName() {
                return "property3";
            }
        };
        propertyPointer4.setParent(context.getDocumentNode());
        assertTrue(propertyPointer4.isCollection());
    }

    @Test
    public void testGetLength() {
        // propertyPointer1 has no length
        assertEquals(-1, propertyPointer1.getLength());

        // propertyPointer2 represents a property with a single value and has a length of 1
        propertyPointer2.setPropertyName("property1");
        assertEquals(1, propertyPointer2.getLength());

        // propertyPointer3 represents an array property "property3" of TEST_BEAN with 3 elements and has a length of 3
        propertyPointer3.setPropertyName("property3");
        propertyPointer3.setIndex(0);
        assertEquals(3, propertyPointer3.getLength());
    }

    @Test
    public void testCreatePath() {
        // propertyPointer1 creates a path to itself
        NodePointer nodePointer1 = propertyPointer1.createPath(context);
        assertEquals(propertyPointer1, nodePointer1);

        // propertyPointer2 creates a path to the property "property1" of TEST_BEAN
        propertyPointer2.setPropertyName("property1");
        NodePointer nodePointer2 = propertyPointer2.createPath(context);
        assertEquals("property1", nodePointer2.getName().getLocalPart());
        assertEquals("value1", nodePointer2.getValue());

        // propertyPointer3 creates a path to the property "property2" of TEST_BEAN
        propertyPointer3.setPropertyName("property2");
        NodePointer nodePointer3 = propertyPointer3.createPath(context);
        assertEquals("property2", nodePointer3.getName().getLocalPart());
        assertNull(nodePointer3.getValue());

        // propertyPointer4 creates a path to an element with the name "elementName" in the list property "property3" of TEST_BEAN
        propertyPointer4.setPropertyName("property3");
        propertyPointer4.setIndex(1);
        NodePointer nodePointer4 = propertyPointer4.createPath(context);
        assertEquals("property3[1]", nodePointer4.getName().getLocalPart());
        assertNull(nodePointer4.getValue());
    }

    @Test
    public void testCreateChild() {
        // propertyPointer1 cannot create a child
        assertNull(propertyPointer1.createChild(context, null, 0, null));

        // propertyPointer2 can create a child with the name "childProperty" and the value "childValue"
        propertyPointer2.setPropertyName("property1");
        NodePointer childPointer1 = propertyPointer2.createChild(context, null, 0, "childValue");
        assertEquals("childValue", childPointer1.getValue());
        assertFalse(childPointer1.isCollection());

        // propertyPointer2 can create a child with the name "childProperty" and the value [1, 2, 3]
        propertyPointer2.setPropertyName("property2");
        NodePointer childPointer2 = propertyPointer2.createChild(context, null, 0, new int[]{1, 2, 3});
        assertEquals("[1, 2, 3]", childPointer2.getValue().toString());
        assertTrue(childPointer2.isCollection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildWithNullName() {
        propertyPointer1.createChild(context, null, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildWithNullValue() {
        propertyPointer1.createChild(context, new QName("childProperty"), 0, null);
    }
}

class ArrayPropertyPointer extends PropertyPointer {
    // implementation of the ArrayPropertyPointer class goes here
}