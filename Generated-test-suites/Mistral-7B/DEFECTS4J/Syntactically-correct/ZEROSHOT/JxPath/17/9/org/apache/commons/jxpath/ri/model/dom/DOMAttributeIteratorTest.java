package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

 private NodePointer parentNodePointer;
 private DOMAttributeIterator attributeIterator;
 private Element testElement;

 @Before
 public void setUp() {
 testElement = createTestElement("root", "xmlns:test=\"http://test.com\"",
 "attr1=\"value1\" attr2=\"value2\" attr3=\"value3\" attr4=\"value4\"");
 parentNodePointer = new DOMPointer("/", testElement.getOwnerDocument());
 }

 @Test
 public void testConstructorWithEmptyQName() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName());
 assertNull(attributeIterator.getNodePointer());
 assertTrue(attributeIterator.hasNext());
 }

 @Test
 public void testConstructorWithQNameMatchingAnAttribute() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("test", "attr1"));
 NodePointer nodePointer = attributeIterator.next();
 assertEquals("attr1", nodePointer.getNodeName());
 assertEquals("value1", nodePointer.getNodeValue());
 }

 @Test
 public void testConstructorWithQNameNotMatchingAnyAttribute() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("test", "attr5"));
 assertNull(attributeIterator.next());
 }

 @Test
 public void testConstructorWithWildcardQName() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("*"));
 NodePointer nodePointer = attributeIterator.next();
 assertEquals("xmlns", nodePointer.getNodeName());
 assertNull(nodePointer.getNodeValue());

 nodePointer = attributeIterator.next();
 assertEquals("attr1", nodePointer.getNodeName());
 assertEquals("value1", nodePointer.getNodeValue());
 }

 @Test
 public void testConstructorWithQNameMatchingNamespaceAttribute() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "xmlns:test"));
 NodePointer nodePointer = attributeIterator.next();
 assertEquals("xmlns:test", nodePointer.getNodeName());
 assertNull(nodePointer.getNodeValue());
 }

 @Test
 public void testHasNextWhenAtEnd() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 assertEquals(4, attributeIterator.size());
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.next();
 assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testHasNextWhenNotAtEnd() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 assertFalse(attributeIterator.hasNext());
 attributeIterator.next();
 assertTrue(attributeIterator.hasNext());
 }

 @Test
 public void testNextWhenAtEnd() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 assertFalse(attributeIterator.hasNext());
 assertNull(attributeIterator.next());
 }

 @Test
 public void testNextWhenNotAtEnd() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 NodePointer nodePointer = attributeIterator.next();
 assertEquals("xmlns", nodePointer.getNodeName());
 assertNull(nodePointer.getNodeValue());

 nodePointer = attributeIterator.next();
 assertEquals("attr1", nodePointer.getNodeName());
 assertEquals("value1", nodePointer.getNodeValue());
 }

 @Test
 public void testNextWithEmptyIterator() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", ""));
 assertNull(attributeIterator.next());
 }

 @Test
 public void testSetPositionWhenNegativeNumber() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.setPosition(-1);
 assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testSetPositionWithPositionGreaterThanSize() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.setPosition(5);
 assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testSetPositionWithPositionLessThanZero() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 attributeIterator.setPosition(-1);
 assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testSetPositionWithValidPosition() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.setPosition(2);
 assertEquals("attr3", attributeIterator.getNodePointer().getNodeName());
 }

 @Test
 public void testSizeWhenIteratorHasNoAttributes() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", ""));
 assertEquals(0, attributeIterator.size());
 }

 @Test
 public void testSizeWhenIteratorHasAttributes() {
 attributeIterator = new DOMAttributeIterator(parentNodePointer, new QName("", "*"));
 assertEquals(4, attributeIterator.size());
 }

 private Element createTestElement(String tagName, String xmlnsAttribute, String... attributeStrings) {
 Element element = testElement.getOwnerDocument().createElement(tagName);
 element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:test", "http://test.com");
 element.setAttributeNodeNS(element.getOwnerDocument().createProcessingInstruction("xml-model", "ignored"));
 element.setAttributeNode(createElementWithAttributeValue(xmlnsAttribute));
 for (String attributeString : attributeStrings) {
 String[] parts = attributeString.split("=");
 Element attr = testElement.getOwnerDocument().createAttribute(parts[0]);
 attr.setValue(parts[1]);
 element.setAttributeNode(attr);
 }
 return element;
 }

 private Attr createElementWithAttributeValue(String attribute) {
 return testElement.getOwnerDocument().createProcessingInstruction("xml-model", attribute);
 }

}