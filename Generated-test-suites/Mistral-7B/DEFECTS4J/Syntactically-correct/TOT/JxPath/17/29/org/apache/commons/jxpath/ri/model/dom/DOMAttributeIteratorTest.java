import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_EmptyElement() {
        Document document = createDocument("", "<element/>");
        Element element = (Element) document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document), new QName("*"));
        assertNull(iterator.getNodePointer().getNode());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        Document document = createDocument("", "<element id='test'/>");
        Element element = (Element) document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document), new QName("id"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer.getNode());
        Assert.assertEquals("test", nodePointer.getNode().getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        Document document = createDocument("", "<element id='test1' class='test2' xml:lang='en'/>");
        Element element = (Element) document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document), new QName("*"));
        List<NodePointer> nodePointers = new ArrayList<>();
        while (iterator.hasNext()) {
            NodePointer nodePointer = iterator.next();
            nodePointers.add(nodePointer);
        }
        Assert.assertEquals(4, iterator.getPosition());
        Assert.assertEquals(4, nodePointers.size());
        for (NodePointer nodePointer : nodePointers) {
            Attribute attribute = ((Attr) nodePointer.getNode());
            String nodeName = attribute.getNodeName();
            String nodeValue = attribute.getNodeValue();
            switch (nodeName) {
                case "id":
                    Assert.assertEquals("test1", nodeValue);
                    break;
                case "class":
                    Assert.assertEquals("test2", nodeValue);
                    break;
                case "xml:lang":
                    Assert.assertEquals("en", nodeValue);
                    break;
                default:
                    Assert.fail("Unexpected attribute: " + nodeName);
            }
        }
    }

    @Test
    public void testDOMAttributeIterator_NamespaceAttribute() {
        Document document = createDocument("", "<element xmlns:x='http://example.com' x:id='test'/>");
        Element element = (Element) document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document), new QName("{http://example.com}id"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer.getNode());
        Assert.assertEquals("test", nodePointer.getNode().getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_EmptyDocument() {
        Document document = createDocument("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document), new QName("*"));
        assertNull(iterator.getNodePointer().getNode());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIterator_CommentNode() {
        Document document = createDocument("", "<!DOCTYPE element [ <!ATTLIST element id ID #IMPLIED> ]><!--comment-->");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document), new QName("id"));
        assertNull(iterator.getNodePointer().getNode());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIterator_NullNodeIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIterator_NullQName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(createDocument("", "<element/>")), null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIterator_NegativePosition() {
        Document document = createDocument("", "<element id='test'/>");
        Element element = (Element) document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document), new QName("id"));
        iterator.setPosition(-1);
    }

    private Document createDocument(String systemId, String documentElementString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document = null;
        try {
            document = builder.parseFromString(documentElementString);
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        if (systemId != null) {
            document.setDocumentURI(systemId);
        }
        return document;
    }
}