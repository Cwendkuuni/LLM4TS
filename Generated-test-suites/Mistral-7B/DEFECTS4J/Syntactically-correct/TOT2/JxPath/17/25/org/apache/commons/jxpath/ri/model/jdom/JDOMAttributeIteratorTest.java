
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor_ValidArguments() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        // Test other functionalities of the iterator here
    }

    @Test
    public void testConstructor_NullNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("*"));
    }

    @Test
    public void testConstructor_EmptyQName() {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName());
        // Test other functionalities of the iterator here
    }

    @Test
    public void testConstructor_NullDocument() {
        NodePointer nodePointer = new NodePointer(null, new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
    }

    @Test
    public void testConstructor_InvalidElement() {
        NodePointer nodePointer = new NodePointer(new Document(), null);
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
    }

    @Test
    public void testGetNodePointer_FirstAttribute() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Attribute attribute = (Attribute) attributePointer.getNode();
        String attributeValue = attribute.getValue();
        // Assert the attributeValue is as expected
        assertEquals("firstAttributeValue", attributeValue);
    }

    @Test
    public void testGetNodePointer_LastAttribute() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        iterator.next();
        // Test other functionalities of the iterator here
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Attribute attribute = (Attribute) attributePointer.getNode();
        String attributeValue = attribute.getValue();
        // Assert the attributeValue is as expected
        assertEquals("lastAttributeValue", attributeValue);
    }

    @Test
    public void testGetPosition() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        int initialPosition = iterator.getPosition();
        iterator.next();
        int nextPosition = iterator.getPosition();
        // Assert the nextPosition is greater than the initialPosition
        assertEquals(initialPosition + 1, nextPosition);
    }

    @Test
    public void testSetPosition_ValidIndex() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        iterator.next();
        iterator.next();
        int validIndex = iterator.getPosition() - 2;
        iterator.setPosition(validIndex);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Attribute attribute = (Attribute) attributePointer.getNode();
        String attributeValue = attribute.getValue();
        // Assert the attributeValue is as expected
        assertEquals("validValue", attributeValue);
    }

    @Test
    public void testSetPosition_FirstIndex() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        int firstIndex = iterator.getPosition() - 1;
        iterator.setPosition(firstIndex);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Attribute attribute = (Attribute) attributePointer.getNode();
        String attributeValue = attribute.getValue();
        // Assert the attributeValue is as expected
        assertEquals("firstValue", attributeValue);
    }

    @Test
    public void testSetPosition_LastIndex() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        int lastIndex = iterator.getPosition() + iterator.size() - 1;
        iterator.setPosition(lastIndex);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Attribute attribute = (Attribute) attributePointer.getNode();
        String attributeValue = attribute.getValue();
        // Assert the attributeValue is as expected
        assertEquals("lastValue", attributeValue);
    }

    @Test
    public void testSetPosition_InvalidIndex() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        iterator.next();
        iterator.next();
        int negativeIndex = iterator.getPosition() - 2;
        iterator.setPosition(negativeIndex);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_NegativeIndex() throws Exception {
        NodePointer nodePointer = new NodePointer(new Document(), new Element("root"));
        QName qname = new QName("*, xmlns:test='http://test.com'");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
        iterator.next();
        iterator.next();
        int negativeIndex = iterator.getPosition() - 2;
        iterator.setPosition(negativeIndex);
    }
}

