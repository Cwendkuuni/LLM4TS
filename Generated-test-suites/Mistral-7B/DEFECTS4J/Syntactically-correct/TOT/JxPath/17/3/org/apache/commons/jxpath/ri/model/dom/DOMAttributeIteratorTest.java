import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attribute;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.DOMException;
import org.junit.Assert;

public class DOMAttributeIteratorTest {

    @org.junit.Test
    public void testEmptyElement() {
        NodePointer root = new DOMNodePointer("", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("", ""));

        Assert.assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeWithEmptyNS() {
        String xml = "<root xmlns:x='http://test.com'><elem x:attr='value' /></root>";
        NodePointer root = new DOMNodePointer(xml, "http://test.com/");
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("", "attr"));
        DOMAttributePointer attribute = (DOMAttributePointer) iterator.next();
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertEquals("http://test.com", attribute.getNamespaceURI());
        Assert.assertEquals("x:attr", attribute.getLocalName());
        Assert.assertEquals("attr", attribute.getNodeName());
        Assert.assertNull(iterator.next());
    }

    @org.junit.Test
    public void testAttributeWithPrefix() {
        String xml = "<root xmlns:x='http://test.com' xmlns:y='http://other.com'><elem x:attr='value' y:attr2='anotherValue' /></root>";
        NodePointer root = new DOMNodePointer(xml, "http://test.com/");
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("http://other.com", "attr"));
        DOMAttributePointer attribute = (DOMAttributePointer) iterator.next();
        Assert.assertEquals("anotherValue", attribute.getValue());
        Assert.assertEquals("http://other.com", attribute.getNamespaceURI());
        Assert.assertEquals("attr2", attribute.getLocalName());
        Assert.assertEquals("attr", attribute.getNodeName());
        Assert.assertNull(iterator.next());
    }

    @org.junit.Test
    public void testAttributeWithoutPrefix() {
        String xml = "<root xmlns:x='http://test.com' xmlns:y='http://other.com'><elem x:attr='value' y:attr2='anotherValue' /></root>";
        NodePointer root = new DOMNodePointer(xml, "http://test.com/");
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr"));
        DOMAttributePointer attribute1 = (DOMAttributePointer) iterator.next();
        Assert.assertEquals("value", attribute1.getValue());
        Assert.assertNull(attribute1.getNamespaceURI());
        Assert.assertEquals("attr", attribute1.getLocalName());
        Assert.assertEquals("attr", attribute1.getNodeName());

        Attribute attribute2 = (Attribute) root.getNode().getAttributes().getNamedItem("xmlns:x");
        DOMAttributePointer attribute2Pointer = new DOMAttributePointer(root, attribute2);
        iterator.setPosition(1);
        DOMAttributePointer attribute3 = (DOMAttributePointer) iterator.next();
        Assert.assertEquals(attribute2Pointer, attribute3);

        Assert.assertNull(iterator.next());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSetPositionAfterEnd() {
        String xml = "<root><elem attr='value' /></root>";
        NodePointer root = new DOMNodePointer(xml);
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr"));
        iterator.next();
        iterator.setPosition(2);
    }

    @org.junit.Test(expected = DOMException.class)
    public void testGetPositionAfterEnd() {
        String xml = "<root><elem attr='value' /></root>";
        NodePointer root = new DOMNodePointer(xml);
        DOMAttributeIterator iterator = new DOMAttributeIterator(root, new QName("attr"));
        iterator.next();
        iterator.setPosition(2);
        iterator.next();
        iterator.next();
        iterator.next();
    }
}