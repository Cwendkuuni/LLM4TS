import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JDOMAttributeIterator.class})
public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = mock(Element.class);
        when(element.getNamespace("xml")).thenReturn(Namespace.XML_NAMESPACE);
        when(element.getAttribute("testAttr"))
                .thenReturn(new Attribute("testAttr", "testValue", Namespace.XML_NAMESPACE));
        when(element.getAttributes()).thenReturn(Collections.singletonList(new Attribute("testAttr2", "testValue2", null)));

        parent = new NodePointer(null, element);
        name = new QName("*");
        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testIteratorNext() {
        NodePointer attributePointer1 = iterator.getNodePointer();
        assertEquals("testValue", attributePointer1.getNode().getText());

        NodePointer attributePointer2 = iterator.getNodePointer();
        assertEquals("testValue2", attributePointer2.getNode().getText());

        assertNull(iterator.getNodePointer());

        iterator.setPosition(1);
        NodePointer attributePointer3 = iterator.getNodePointer();
        assertEquals("testValue2", attributePointer3.getNode().getText());
    }

    @Test
    public void testIteratorFirst() {
        NodePointer attributePointer1 = iterator.getNodePointer();
        assertEquals("testValue", attributePointer1.getNode().getText());

        assertSame(iterator, iterator.setPosition(0));
        NodePointer attributePointer1Again = iterator.getNodePointer();
        assertEquals("testValue", attributePointer1Again.getNode().getText());

        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorOutOfBounds() {
        iterator.setPosition(10);
    }

    @Test
    public void testIteratorEmpty() {
        parent = new NodePointer(null, null);
        name = new QName("test");
        iterator = new JDOMAttributeIterator(parent, name);

        assertNull(iterator.getNodePointer());
    }
}