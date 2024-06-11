import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;

public class DOMAttributeIteratorTest {
    private NodePointer nodePointer;
    private DOMAttributeIterator iterator;

    @BeforeEach
    public void setup() {
        Document document = createDocument();
        Element root = document.getDocumentElement();
        nodePointer = new DOMNodePointer(document, root);
        iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
    }

    @Test
    public void testEmptyIterator() {
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSingleAttribute() {
        Element testElement = document.createElement("testElement");
        testElement.setAttribute("testAttribute", "testValue");
        root.appendChild(testElement);
        nodePointer = new DOMNodePointer(testElement);
        iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
        Assertions.assertEquals("testValue", attributePointer.getValue());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        Element testElement = document.createElement("testElement");
        testElement.setAttribute("testAttribute1", "testValue1");
        testElement.setAttribute("testAttribute2", "testValue2");
        root.appendChild(testElement);
        nodePointer = new DOMNodePointer(testElement);
        iterator = new DOMAttributeIterator(nodePointer, new QName("test"));
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.next();
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.next();
        Assertions.assertEquals("testValue1", attributePointer1.getValue());
        Assertions.assertEquals("testValue2", attributePointer2.getValue());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithDifferentNamespaces() {
        Element rootElement = document.createElement("root");
        rootElement.setAttributeNS("http://test.com", "testNamespace:testAttribute", "testValue");
        document.appendChild(rootElement);
        nodePointer = new DOMNodePointer(rootElement);
        iterator = new DOMAttributeIterator(nodePointer, new QName("http://test.com:testAttribute"));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
        Assertions.assertEquals("testValue", attributePointer.getValue());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNullName() {
        iterator = new DOMAttributeIterator(nodePointer, null);
        Assertions.assertFalse(iterator.hasNext());
    }

    // Add remaining test cases for other methods here
}