package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private Element testElement;

 @Before
 public void setup() {
 testElement = new Element.Builder("testElement").namespaceURI("http://test.com")
 .build();
 parentNode = new NodePointer(testElement);
 }

 @Test
 public void testDOMAttributeIteratorWithEmptyList() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorWithOneAttribute() {
 Attr testAttribute = new Attr.Builder("testAttribute", "testValue", new QName("", "testAttribute"))
 .namespaceURI("http://test.com").build();
 testElement.setAttributeNode(testAttribute);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "testAttribute"));
 assertTrue(iterator.hasNext());
 NodePointer nextNode = iterator.next();
 assertEquals(testAttribute, nextNode.getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorWithMultipleAttributes() {
 Attr attribute1 = new Attr.Builder("attribute1", "value1", new QName("", "attribute1"))
 .namespaceURI("http://test.com").build();
 Attr attribute2 = new Attr.Builder("attribute2", "value2", new QName("", "attribute2"))
 .namespaceURI("http://test.com").build();
 testElement.setAttributeNode(attribute1);
 testElement.setAttributeNode(attribute2);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "*"));
 assertTrue(iterator.hasNext());
 iterator.next();
 assertTrue(iterator.hasNext());
 NodePointer nextNode = iterator.next();
 assertEquals(attribute2, nextNode.getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorWithNamespaceAttribute() {
 Attr namespaceAttribute = new Attr.Builder("xmlns:test", "http://test.com", new QName("", "xmlns:test"))
 .namespaceURI("http://test.com").build();
 testElement.setNamespaceID("http://test.com");
 testElement.setAttributeNodeNS(namespaceAttribute);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, new QName("", "xmlns:test"));
 assertTrue(iterator.hasNext());
 NodePointer nextNode = iterator.next();
 assertEquals(namespaceAttribute, nextNode.getNode());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDOMAttributeIteratorWithNullParent() {
 new DOMAttributeIterator(null, new QName("", "*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDOMAttributeIteratorWithNullName() {
 new DOMAttributeIterator(parentNode, null);
 }
}