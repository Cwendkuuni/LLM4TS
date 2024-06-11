import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyAttributeIterator() throws JDOMException {
        NodePointer parent = new JDOMPoint(new Document());
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleAttributeIterator() throws JDOMException {
        String xmlns = "http://www.w3.org/XML/1998/namespace";
        String prefix = "xml";
        String localName = "id";

        Namespace namespace = new Namespace(xmlns, prefix);
        QName name = new QName(prefix, localName);

        String xml = "<root xmlns:xml=\"{0}\" id=\"test\">test</root>";
        Document document = new Document(String.format(xml, xmlns));
        NodePointer parent = new JDOMPoint(document.getRootElement());

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertTrue(iterator.hasNext());
        NodePointer attributePointer = iterator.next();
        Assert.assertTrue(attributePointer instanceof JDOMAttributePointer);
        Attribute attribute = ((JDOMAttributePointer) attributePointer).getAttribute();
        Assert.assertEquals("test", attribute.getValue());
        Assert.assertEquals(namespace, attribute.getNamespace());
        Assert.assertEquals(localName, attribute.getName());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() throws JDOMException {
        String xmlns = "http://www.w3.org/XML/1998/namespace";
        String prefix = "xml";

        String xml = "<root xmlns:xml=\"{0}\">" +
                "<element xml:id=\"test1\" xml:foo=\"bar\" xml:bar=\"baz\"/>" +
                "</root>";
        Document document = new Document(new StringReader(xml));
        NodePointer parent = new JDOMPoint(document.getRootElement());

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));
        int count = 0;
        while (iterator.hasNext()) {
            NodePointer attributePointer = iterator.next();
            Attribute attribute = ((JDOMAttributePointer) attributePointer).getAttribute();
            count++;
        }
        Assert.assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParent() {
        new JDOMAttributeIterator(null, new QName("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        NodePointer parent = new JDOMPoint(new Document());
        new JDOMAttributeIterator(parent, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAttribute() {
        NodePointer parent = new JDOMPoint(new Document());
        new JDOMAttributeIterator(parent, new QName("", ""));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) parent.getNodePointer();
        attributePointer.setAttribute(null);
    }
}

class JDOMPoint {
    private NodePointer nodePointer;

    public JDOMPoint(NodePointer nodePointer) {
        this.nodePointer = nodePointer;
    }

    public NodePointer getNodePointer() {
        return nodePointer;
    }
}