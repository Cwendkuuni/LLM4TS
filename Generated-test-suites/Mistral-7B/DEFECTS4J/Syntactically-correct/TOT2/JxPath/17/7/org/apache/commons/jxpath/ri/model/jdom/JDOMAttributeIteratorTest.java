
package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.*;
import static org.mockito.Mockito.*;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private Element element;
    private QName name;
    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        element = mock(Element.class);
        parent = new NodePointer(element);
        name = new QName("testName");
    }

    @Test
    public void testConstructor() {
        when(element.getAttributes()).thenReturn(new ArrayList<>());

        iterator = new JDOMAttributeIterator(parent, name);

        verify(element, times(1)).getAttributes();
    }

    @Test
    public void testGetNodePointer() {
        Attribute testAttribute = new Attribute("testAttribute", "testValue");
        when(element.getAttribute("testName", Namespace.NO_NAMESPACE)).thenReturn(testAttribute);
        when(element.getAttribute("testName", mock(Namespace.class))).thenReturn(testAttribute);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(testAttribute, nodePointer.getNode());
    }

    @Test
    public void testGetPosition() {
        Attribute attribute1 = new Attribute("position1", "value1");
        Attribute attribute2 = new Attribute("position2", "value2");

        when(element.getAttribute("testName", Namespace.NO_NAMESPACE)).thenReturn(attribute1);
        when(element.getAttribute("testName", mock(Namespace.class))).thenReturn(attribute2);

        iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(0, iterator.getPosition());

        iterator.next();

        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Attribute attribute1 = new Attribute("position1", "value1");
        Attribute attribute2 = new Attribute("position2", "value2");

        iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertTrue(iterator.setPosition(1));

        iterator.next();

        Assert.assertFalse(iterator.setPosition(1));

        iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertFalse(iterator.setPosition(-1));
    }

    @Test
    public void testEmptyList() {
        when(element.getAttributes()).thenReturn(Collections.EMPTY_LIST);

        iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testNullAttribute() {
        when(element.getAttribute("testName", Namespace.NO_NAMESPACE)).thenReturn(null);

        iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testUnsupportedNamespace() {
        when(element.getAttribute("testName", mock(Namespace.class))).thenReturn(null);

        iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidPosition() {
        Attribute testAttribute = new Attribute("position1", "value1");

        iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOutOfBoundsPosition() {
        Attribute attribute1 = new Attribute("position1", "value1");
        Attribute attribute2 = new Attribute("position2", "value2");

        iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(3);
    }
}
