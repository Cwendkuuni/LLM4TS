package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.jupiter.api.*;
import org.w3c.dom.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DOMAttributeIteratorTest {

 private NodePointer root;
 private Element element;
 private DOMAttributeIterator iterator;

 @BeforeEach
 public void setUp() {
 String xml = "<root xmlns:x='http://example.com' xmlns:y='http://example.org'><element x:a='1' y:b='2' /></root>";
 Document doc = JAXPDocument.parse(new StringReader(xml));
 root = new DOMPointNode(doc.getDocumentElement());
 element = (Element) doc.getDocumentElement().getElementsByTagName("element").item(0);
 }

 @Test
 public void testAttributeIterator() {
 // Test empty iterator
 DOMAttributeIterator emptyIterator = new DOMAttributeIterator(root, null);
 assertFalse(emptyIterator.hasNext());

 // Test iterator for single attribute
 DOMAttributeIterator attributeIterator = new DOMAttributeIterator(element, new QName("x:a"));
 assertTrue(attributeIterator.hasNext());
 NodePointer attributePointer = attributeIterator.next();
 assertEquals("1", attributePointer.getNodeValue());
 assertFalse(attributeIterator.hasNext());

 // Test iterator for multiple attributes
 DOMAttributeIterator multipleAttributesIterator = new DOMAttributeIterator(element, new QName("*"));
 assertTrue(multipleAttributesIterator.hasNext());
 NodePointer attribute1Pointer = multipleAttributesIterator.next();
 assertEquals("1", attribute1Pointer.getNodeValue());
 assertTrue(attributeIterator.hasNext());
 NodePointer attribute2Pointer = multipleAttributesIterator.next();
 assertEquals("2", attribute2Pointer.getNodeValue());
 assertFalse(multipleAttributesIterator.hasNext());
 }

 @Test
 public void testIteratorWithPrefix() {
 // Test iterator for attribute with prefix in name
 DOMAttributeIterator prefixIterator = new DOMAttributeIterator(element, new QName("x:a"));
 assertTrue(prefixIterator.hasNext());
 NodePointer attributePointer = prefixIterator.next();
 assertEquals("1", attributePointer.getNodeValue());
 assertFalse(prefixIterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespaceURI() {
 // Test iterator for attribute with namespace URI in name
 DOMAttributeIterator namespaceURIIterator = new DOMAttributeIterator(element, new QName("{http://example.com}a"));
 assertTrue(namespaceURIIterator.hasNext());
 NodePointer attributePointer = namespaceURIIterator.next();
 assertEquals("1", attributePointer.getNodeValue());
 assertFalse(namespaceURIIterator.hasNext());
 }

 @Test
 public void testIteratorWithEmptyName() {
 // Test iterator for all attributes
 DOMAttributeIterator allAttributesIterator = new DOMAttributeIterator(element, new QName(""));
 assertTrue(allAttributesIterator.hasNext());
 NodePointer attributePointer = allAttributesIterator.next();
 assertEquals("1", attributePointer.getNodeValue());
 assertTrue(allAttributesIterator.hasNext());
 NodePointer attribute2Pointer = allAttributesIterator.next();
 assertEquals("2", attribute2Pointer.getNodeValue());
 assertFalse(allAttributesIterator.hasNext());
 }

 @Test
 public void testIteratorWithWrongAttribute() {
 // Test iterator for non-existent attribute
 DOMAttributeIterator wrongAttributeIterator = new DOMAttributeIterator(element, new QName("z:c"));
 assertFalse(wrongAttributeIterator.hasNext());
 }
}