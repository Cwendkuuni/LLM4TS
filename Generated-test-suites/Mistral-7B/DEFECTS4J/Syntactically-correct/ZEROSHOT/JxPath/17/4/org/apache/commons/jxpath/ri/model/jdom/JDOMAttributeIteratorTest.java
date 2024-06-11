import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.JDOMElement;
import org.apache.commons.jxpath.ri.model.JDOMNodePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @org.junit.Test
    public void testEmptyAttributeIterator() {
        JDOMElement element = new JDOMElement("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), null);
        assertFalse("Iterator should be empty", iterator.hasNext());
        assertNull("getCurrentNodePointer should return null", iterator.getCurrentNodePointer());
    }

    @org.junit.Test
    public void testAttributeIteratorWithOneAttribute() {
        String xmlns = "xml";
        String prefix = xmlns;
        String namespaceURI = "http://www.w3.org/XML/1998/namespace";
        String localName = "attr";

        Element element = new Element("element");
        element.addNamespaceDeclaration(prefix, namespaceURI);
        element.addAttribute(new Attribute(xmlns + ":attr", "value", "http://www.w3.org/XML/1998/namespace"));

        JDOMElement jdomElement = new JDOMElement(element);
        NodePointer parent = new JDOMNodePointer(jdomElement);
        QName attributeName = new QName(xmlns, "*", namespaceURI);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parent), attributeName);

        NodePointer nodePointer = iterator.next();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Attribute attribute = attributePointer.getNode();
        assertEquals("xml:attr", attribute.getName());
        assertEquals("value", attribute.getValue());

        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        NodePointer nextNodePointer = iterator.next();
        assertNull(nextNodePointer);
    }

    @org.junit.Test
    public void testAttributeIteratorWithMultipleAttributes() {
        String xmlns = "xml";
        String prefix = xmlns;
        String namespaceURI = "http://www.w3.org/XML/1998/namespace";
        String localName1 = "attr1";
        String localName2 = "attr2";

        Element element = new Element("element");
        element.addNamespaceDeclaration(prefix, namespaceURI);
        element.addAttribute(new Attribute(xmlns + ":attr1", "value1", "http://www.w3.org/XML/1998/namespace"));
        element.addAttribute(new Attribute(xmlns + ":attr2", "value2", "http://www.w3.org/XML/1998/namespace"));

        JDOMElement jdomElement = new JDOMElement(element);
        NodePointer parent = new JDOMNodePointer(jdomElement);
        QName attributeName = new QName(xmlns, "*", namespaceURI);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parent), attributeName);

        NodePointer nodePointer1 = iterator.next();
        JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) nodePointer1;
        Attribute attribute1 = attributePointer1.getNode();
        assertEquals("xml:attr1", attribute1.getName());
        assertEquals("value1", attribute1.getValue());

        NodePointer nodePointer2 = iterator.next();
        JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) nodePointer2;
        Attribute attribute2 = attributePointer2.getNode();
        assertEquals("xml:attr2", attribute2.getName());
        assertEquals("value2", attribute2.getValue());

        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.hasNext());
        NodePointer nextNodePointer = iterator.next();
        assertNull(nextNodePointer);
    }

    @org.junit.Test
    public void testAttributeIteratorWithNoMatchingAttributes() {
        String namespaceURI = "http://www.w3.org/XML/1998/namespace";
        String localName = "attr";
        String prefix = "prefix";

        Element element = new Element("element");
        element.addNamespaceDeclaration(prefix, namespaceURI);

        JDOMElement jdomElement = new JDOMElement(element);
        NodePointer parent = new JDOMNodePointer(jdomElement);
        QName attributeName = new QName(prefix, localName, namespaceURI);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parent), attributeName);

        assertFalse("Iterator should be empty", iterator.hasNext());
        assertNull("getCurrentNodePointer should return null", iterator.getCurrentNodePointer());
    }

    @org.junit.Test
    public void testAttributeIteratorWithPrefixAndLocalName() {
        String namespaceURI = "http://www.w3.org/XML/1998/namespace";
        String prefix = "xml";
        String localName = "attr";

        Element element = new Element("element");
        element.addNamespaceDeclaration(prefix, namespaceURI);
        element.addAttribute(new Attribute("xml:attr", "value", namespaceURI));

        JDOMElement jdomElement = new JDOMElement(element);
        NodePointer parent = new JDOMNodePointer(jdomElement);
        QName attributeName = new QName(prefix, localName, namespaceURI);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parent), attributeName);

        NodePointer nodePointer = iterator.next();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Attribute attribute = attributePointer.getNode();
        assertEquals("xml:attr", attribute.getName());
        assertEquals("value", attribute.getValue());

        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        NodePointer nextNodePointer = iterator.next();
        assertNull(nextNodePointer);
    }

    @org.junit.Test
    public void testAttributeIteratorWithWildcardNamespace() {
        String localName = "attr";

        Element element = new Element("element");
        Attribute attr1 = new Attribute("xmlns:ns1:attr1", "value1");
        Attribute attr2 = new Attribute("xmlns:ns1:attr2", "value2");
        Attribute attr3 = new Attribute("xmlns:ns2:attr3", "value3");
        element.addAttribute(attr1);
        element.addAttribute(attr2);
        element.addAttribute(attr3);

        JDOMElement jdomElement = new JDOMElement(element);
        NodePointer parent = new JDOMNodePointer(jdomElement);
        QName attributeName = new QName("*", "*", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parent), attributeName);

        List<NodePointer> nodePointers = new ArrayList<>();
        while (iterator.hasNext()) {
            NodePointer nodePointer = iterator.next();
            nodePointers.add(nodePointer);
        }

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(attr1.getValue());
        expectedValues.add(attr2.getValue());
        expectedValues.add(attr3.getValue());

        for (int i = 0; i < nodePointers.size(); i++) {
            JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointers.get(i);
            Attribute attribute = attributePointer.getNode();
            String value = attribute.getValue();
            assertEquals("Value should match", expectedValues.get(i), value);
        }

        assertEquals(3, iterator.getPosition());
        assertFalse("Iterator should not have next element", iterator.hasNext());
    }

    @org.junit.Test
    public void testAttributeIteratorWithNamespaceURI() {
        String namespaceURI = "http://www.w3.org/XML/1998/namespace";
        String localName = "attr";

        Element element = new Element("element");
        element.addNamespaceDeclaration("xml", namespaceURI);
        element.addAttribute(new Attribute("xml:attr", "value", namespaceURI));

        JDOMElement jdomElement = new JDOMElement(element);
        NodePointer parent = new JDOMNodePointer(jdomElement);
        QName attributeName = new QName("xml", localName, namespaceURI);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parent), attributeName);

        NodePointer nodePointer = iterator.next();
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Attribute attribute = attributePointer.getNode();
        assertEquals("xml:attr", attribute.getName());
        assertEquals("value", attribute.getValue());

        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.hasNext());
        NodePointer nextNodePointer = iterator.next();
        assertNull(nextNodePointer);
    }
}