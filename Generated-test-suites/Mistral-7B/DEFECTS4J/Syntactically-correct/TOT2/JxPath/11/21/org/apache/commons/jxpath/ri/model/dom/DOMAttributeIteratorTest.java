import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {
    private NodePointer nodePointer;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Document doc = createDocument();
        Element rootElement = doc.getDocumentElement();
        rootElement.setAttributeNode(createAttribute(doc, "test1", "value1"));
        rootElement.setAttributeNode(createAttribute(doc, "test2", "value2"));
        nodePointer = new NodePointer(doc, rootElement);
        attributeIterator = new DOMAttributeIterator(nodePointer, new QName("*"));
    }

    @Test
    public void testConstructorWithElementAndName() {
        Element element = createElement(new QName("testElement"), null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, element), new QName("testAttr"));
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testConstructorWithElementAndWildcardName() {
        AttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testHasNextAttribute() {
        Assert.assertTrue("Should have next attribute", attributeIterator.hasNext());
        Assert.assertFalse("Should not have next attribute", attributeIterator.hasNext());
        attributeIterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        Assert.assertTrue("Should have next attribute", attributeIterator.hasNext());
    }

    @Test
    public void testGetNextAttribute() {
        Attr nextAttribute = attributeIterator.next();
        Assert.assertEquals("test1", nextAttribute.getNodeName());
        Assert.assertEquals("value1", nextAttribute.getNodeValue());
        nextAttribute = attributeIterator.next();
        Assert.assertEquals("test2", nextAttribute.getNodeName());
        Assert.assertEquals("value2", nextAttribute.getNodeValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorException() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        iterator.next();
        iterator.next();
        iterator.next();
    }

    private Element createElement(QName name, String namespaceURI) {
        Document doc = createDocument();
        Element elem = doc.createElement(name.getName());
        if (namespaceURI != null) {
            elem.setNamespaceURI(namespaceURI);
        }
        return elem;
    }

    private Attr createAttribute(Document doc, String name, String value) {
        Element elem = doc.createElement("testElement");
        Attr attr = doc.createAttribute(name);
        attr.setValue(value);
        elem.setAttributeNode(attr);
        return attr;
    }

    private Document createDocument() {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        return doc;
    }
}