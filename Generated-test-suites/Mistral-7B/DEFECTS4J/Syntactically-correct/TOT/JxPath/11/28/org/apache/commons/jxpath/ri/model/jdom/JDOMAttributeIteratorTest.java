import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testSingleAttributeIterator() {
        NodePointer parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        QName name = new QName("test", "");
        Attribute attribute = mock(Attribute.class);
        when(parent.getNode()).thenReturn(element);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(attribute, iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(attribute, iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());

        Attribute anotherAttribute = mock(Attribute.class);
        when(element.getAttribute(name.getName())).thenReturn(anotherAttribute);
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(2));
    }

    @Test
    public void testMultipleAttributesIterator() {
        NodePointer parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        QName name = new QName("test", "");
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);
        when(parent.getNode()).thenReturn(element);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(null, iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(attribute1, iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());

        Assert.assertEquals(attribute2, iterator.getNodePointer());
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertEquals(attribute2, iterator.getNodePointer());
        Assert.assertEquals(2, iterator.getPosition());

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(3, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testNamespaceIterator() {
        NodePointer parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        QName name = new QName("test", "xml");
        Attribute attribute = mock(Attribute.class);
        when(parent.getNode()).thenReturn(element);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(null, iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(1));

        when(element.getAttribute(name.getName(), Namespace.XML_NAMESPACE)).thenReturn(attribute);
        Assert.assertEquals(attribute, iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(attribute, iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());

        Attribute anotherAttribute = mock(Attribute.class);
        when(element.getAttributes()).thenReturn(Collections.singletonList(anotherAttribute));
        when(anotherAttribute.getNamespace()).thenReturn(Namespace.XML_NAMESPACE);
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativePosition() {
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPositionBeyondEnd() {
        NodePointer parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        QName name = new QName("test", "");
        Attribute attribute = mock(Attribute.class);
        when(parent.getNode()).thenReturn(element);

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        NodePointer parent = mock(NodePointer.class);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
    }
}