package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer root;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 String xml = "<root xmlns:x='http://test.com/x' xmlns:y='http://test.com/y'>" +
 "<child attr1='value1' attr2='value2' xmlns:z='http://test.com/z'>" +
 "<subchild attr3='value3'/>" +
 "</child>" +
 "</root>";
 Document doc = JAXPDocumentFactory.newDocument(xml);
 root = new DOMPointNode(doc.getDocumentElement());
 }

 @Test
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(root, new QName("", "*"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttribute() {
 iterator = new DOMAttributeIterator(root, new QName("root", "attr1"));
 NodePointer nodePointer = iterator.next();
 assertEquals("child", nodePointer.getNode().getTagName());
 assertEquals("value1", nodePointer.getNode().getNodeValue());
 assertNull(iterator.next());
 }

 @Test
 public void testMultipleAttributes() {
 iterator = new DOMAttributeIterator(root, new QName("root", "attr2"));
 NodePointer nodePointer1 = iterator.next();
 assertEquals("child", nodePointer1.getNode().getTagName());
 assertEquals("value2", nodePointer1.getNode().getNodeValue());
 NodePointer nodePointer2 = iterator.next();
 assertNull(nodePointer2);
 }

 @Test
 public void testNamespacePrefix() {
 iterator = new DOMAttributeIterator(root, new QName("root", "x:attr1"));
 NodePointer nodePointer = iterator.next();
 assertEquals("child", nodePointer.getNode().getTagName());
 assertEquals("value1", nodePointer.getNode().getNodeValue());
 }

 @Test
 public void testNamespaceURI() {
 iterator = new DOMAttributeIterator(root, new QName("", "http://test.com/x:attr1"));
 NodePointer nodePointer = iterator.next();
 assertEquals("child", nodePointer.getNode().getTagName());
 assertEquals("value1", nodePointer.getNode().getNodeValue());
 }

 @Test
 public void testNoMatchingAttribute() {
 iterator = new DOMAttributeIterator(root, new QName("root", "notExist"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSubChildIterator() {
 Node child = root.getNode().getElementsByTagName("child").item(0);
 iterator = new DOMAttributeIterator(new DOMPointNode(child), new QName("", "*"));
 NodePointer nodePointer = iterator.next();
 assertEquals("subchild", nodePointer.getNode().getTagName());
 assertNull(iterator.next());
 }

 @Test
 public void testEmptyElement() {
 String xml = "<root xmlns:x='http://test.com/x' xmlns:y='http://test.com/y'>" +
 "<empty/>" +
 "</root>";
 Document doc = JAXPDocumentFactory.newDocument(xml);
 root = new DOMPointNode(doc.getDocumentElement());
 iterator = new DOMAttributeIterator(root, new QName("", "*"));
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullIterator() {
 new DOMAttributeIterator(null, new QName("", "*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullQName() {
 new DOMAttributeIterator(root, null);
 }
}