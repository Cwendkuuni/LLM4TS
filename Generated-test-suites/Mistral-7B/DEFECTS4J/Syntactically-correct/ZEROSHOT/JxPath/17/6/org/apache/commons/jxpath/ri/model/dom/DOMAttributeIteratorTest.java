package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

 private static final String TEST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:test=\"http://test\">\n" +
         "<element test:attr1=\"value1\" test:attr2=\"value2\" xsi:id=\"e1\"></element>\n" +
         "</root>";

 @Test
 public void testCreateIteratorWithEmptyNode() {
     DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointNodePointer(null), null);
     assertTrue(iterator.hasNext());
     assertNull(iterator.next());
 }

 @Test
 public void testCreateIteratorWithNullName() {
     DOMPointNodePointer parent = new DOMPointNodePointer(createDocument(TEST_XML), 1);
     DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
     assertTrue(iterator.hasNext());
     assertNull(iterator.next());
 }

 @Test
 public void testCreateIteratorWithEmptyElement() {
     DOMPointNodePointer parent = new DOMPointNodePointer(createDocument(TEST_XML), 1);
     DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", ""));
     assertTrue(iterator.hasNext());
     assertNull(iterator.next());
 }

 @Test
 public void testCreateIteratorWithOneAttribute() {
     DOMPointNodePointer parent = new DOMPointNodePointer(createDocument(TEST_XML), 1);
     DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "attr1"));
     assertTrue(iterator.hasNext());
     Attr attr = (Attr) iterator.next();
     assertEquals("value1", attr.getValue());
     assertEquals("test:attr1", attr.getNodeName().getLocalPart());
     assertEquals("test", attr.getNamespaceURI());
     assertEquals("value1", iterator.next().getValue());
     assertNull(iterator.next());
 }

 @Test
 public void testCreateIteratorWithAttributesNS() {
     DOMPointNodePointer parent = new DOMPointNodePointer(createDocument(TEST_XML), 1);
     DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "attr2"));
     assertTrue(iterator.hasNext());
     Attr attr = (Attr) iterator.next();
     assertEquals("value2", attr.getValue());
     assertEquals("test:attr2", attr.getNodeName().getLocalPart());
     assertEquals("http://test", attr.getNamespaceURI());
     assertNull(iterator.next());
 }

 @Test
 public void testCreateIteratorWithAttributesPrefix() {
     DOMPointNodePointer parent = new DOMPointNodePointer(createDocument(TEST_XML), 1);
     DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xsi", "id"));
     assertTrue(iterator.hasNext());
     Attr attr = (Attr) iterator.next();
     assertEquals("e1", attr.getNodeValue());
     assertEquals("id", attr.getLocalName());
     assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNamespaceURI());
     assertNull(iterator.next());
 }

 @Test
 public void testCreateIteratorWithAttributesPrefixAndNS() {
     DOMPointNodePointer parent = new DOMPointNodePointer(createDocument(TEST_XML), 1);
     DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xsi", "xmlns:test"));
     assertTrue(iterator.hasNext());
     Attr attr = (Attr) iterator.next();
     assertEquals("http://test", attr.getNodeValue());
     assertEquals("xmlns:test", attr.getLocalName());
     assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNamespaceURI());
     assertNull(iterator.next());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateIteratorWithInvalidName() {
     DOMPointNodePointer parent = new DOMPointNodePointer(createDocument(TEST_XML), 1);
     new DOMAttributeIterator(parent, new QName("", "xmlns:test:attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateIteratorWithNullParent() {
     new DOMAttributeIterator(null, new QName("test", "attr1"));
 }

 private Node createDocument(String xml) {
     org.w3c.dom.Document document = org.apache.commons.jxpath.DocumentBuilderFactory.newDocumentBuilder().parse(new org.apache.commons.io.FileInputStream(new java.io.ByteArrayInputStream(xml.getBytes())));
     document.getDocumentElement().setNamespacePrefixes(new org.w3c.dom.NSMap());
     return document;
 }

}