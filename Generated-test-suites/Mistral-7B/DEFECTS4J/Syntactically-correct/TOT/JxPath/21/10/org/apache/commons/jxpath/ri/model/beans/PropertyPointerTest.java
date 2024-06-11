package org.apache.commons.jxpath.ri.model.beans;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private NodePointer parentPointer;
    private Object parentBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parentBean = new Object() {
            public String getProperty1() {
                return "property1";
            }

            public String getProperty2() {
                return "property2";
            }

            public List<String> getPropertyList() {
                return new ArrayList<>();
            }
        };
        parentPointer = new NodePointer(null) {
            @Override
            public Object getNode() {
                return parentBean;
            }
        };
        propertyPointer = new PropertyPointer(parentPointer) {
            @Override
            public String getPropertyName() {
                return "property1";
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
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(-1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(0);
        assertEquals(0, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertNotNull(propertyPointer.getBean());
        assertEquals(parentBean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        QName name = propertyPointer.getName();
        assertEquals("property1", name.getLocalPart());
    }

    @Test
    public void testGetPropertyCount() {
        assertEquals(2, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        String[] names = propertyPointer.getPropertyNames();
        assertEquals(2, names.length);
        assertEquals("property1", names[0]);
        assertEquals("property2", names[1]);
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        assertEquals("property1", propertyPointer.getImmediateNode());

        propertyPointer.setPropertyName("property2");
        assertEquals("property2", propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());

        propertyPointer.setPropertyName("getPropertyList");
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testGetLength() {
        propertyPointer.setPropertyName("getPropertyList");
        assertEquals(0, propertyPointer.getLength());

        propertyPointer.getBean().setPropertyList(new ArrayList<>());
        propertyPointer.getBean().getPropertyList().add("item1");
        assertEquals(1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
        assertNotNull(valuePointer);
        assertEquals(propertyPointer, valuePointer.getParentPointer());
        assertEquals("property1", valuePointer.getName().getLocalPart());
        assertEquals("property1", valuePointer.getNode().toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreatePathWithInvalidIndex() {
        propertyPointer.createPath(new JXPathContext(), 3);
    }

    @Test
    public void testCreatePathWithValidIndex() {
        Object value = new Object() {
            public String getValue() {
                return "value";
            }
        };
        propertyPointer.createPath(new JXPathContext(), value);
        assertEquals("value", propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithNullName() {
        assertNull(propertyPointer.createChild(new JXPathContext(), null, 0));
    }

    @Test
    public void testCreateChildWithValidName() {
        PropertyPointer childPropertyPointer = (PropertyPointer) propertyPointer.createChild(new JXPathContext(), "test", 0);
        assertEquals("test", childPropertyPointer.getPropertyName());
        assertEquals(propertyPointer, childPropertyPointer.getParentPointer());
        assertNull(childPropertyPointer.getNode());
    }

    @Test
    public void testCreateChildWithValidIndex() {
        List<String> list = new ArrayList<>();
        list.add("item1");
        propertyPointer.getBean().setPropertyList(list);
        PropertyPointer childPropertyPointer = (PropertyPointer) propertyPointer.createChild(new JXPathContext(), null, 0);
        assertEquals("property1.0", childPropertyPointer.getName().getLocalPart());
        assertEquals(propertyPointer, childPropertyPointer.getParentPointer());
        assertEquals("item1", childPropertyPointer.getImmediateNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildWithInvalidIndex() {
        List<String> list = new ArrayList<>();
        propertyPointer.createChild(new JXPathContext(), null, 1);
    }
}