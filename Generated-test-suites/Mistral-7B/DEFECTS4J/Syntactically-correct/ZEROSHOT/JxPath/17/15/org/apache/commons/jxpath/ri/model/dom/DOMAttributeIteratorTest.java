package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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

 private NodePointer nodePointer;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 Node node = createTestElement();
 nodePointer = new NodePointer(null, node);
 QName name = new QName("testPrefix:testLocalName");
 iterator = new DOMAttributeIterator(nodePointer, name);
 }

 @Test
 public void testEmptyIterator() {
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorHasNext() {
 assertNotNull(iterator.getNodePointer());
 assertEquals(1, iterator.getPosition());
 iterator.next();
 assertEquals(0, iterator.getPosition());
 }

 @Test
 public void testIteratorHasPrevious() {
 assertNotNull(iterator.getNodePointer());
 assertEquals(1, iterator.getPosition());
 iterator.next();
 assertEquals(0, iterator.getPosition());
 iterator.previous();
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testIteratorMoveToNext() {
 assertNotNull(iterator.getNodePointer());
 assertEquals(1, iterator.getPosition());
 iterator.next();
 assertEquals(2, iterator.getPosition());
 }

 @Test(expected = IllegalStateException.class)
 public void testIteratorMoveToNextBeyondEnd() {
 assertNotNull(iterator.getNodePointer());
 assertEquals(2, iterator.getPosition());
 iterator.next();
 }

 @Test(expected = IllegalStateException.class)
 public void testIteratorMoveToPreviousBeyondBegin() {
 assertNotNull(iterator.getNodePointer());
 assertEquals(0, iterator.getPosition());
 iterator.previous();
 }

 @Test
 public void testIteratorGetNode() {
 Node node = iterator.getNodePointer().getNode();
 assertInstanceOf(Attr.class, node);
 }

 @Test
 public void testIteratorGetNodeWithPrefix() {
 Node node = new DOMAttributePointer(nodePointer, iterator.getNodePointer().getNode())
 .getNodePointer().getNode();
 QName name = new QName("testPrefix:testLocalName");
 assertInstanceOf(Attr.class, node);
 assertEquals(name.getName(), node.getNodeName());
 assertEquals(nodePointer.getNamespaceURI("testPrefix"), node.getNamespaceURI());
 }

 @Test
 public void testIteratorGetNodeWithoutPrefix() {
 Node node = iterator.getNodePointer().getNode();
 assertEquals(node.getNodeName(), iterator.getNodePointer().getName().getName());
 }

 @Test
 public void testIteratorGetNextNodeWithSameNameAndPrefix() {
 Node node1 = iterator.getNodePointer().getNode();
 iterator.next();
 Node node2 = iterator.getNodePointer().getNode();
 assertEquals(node1, node2);
 }

 @Test
 public void testIteratorGetNextNodeWithSameNameButDifferentPrefix() {
 Node node1 = new DOMAttributePointer(nodePointer, iterator.getNodePointer().getNode())
 .getNodePointer().getNode();
 iterator.next();
 Node node2 = iterator.getNodePointer().getNode();
 assertNotSame(node1, node2);
 }

 @Test
 public void testIteratorGetNextNodeWithDifferentName() {
 Node node1 = iterator.getNodePointer().getNode();
 QName name2 = new QName("testPrefix2:testLocalName2");
 iterator.next();
 Node node2 = iterator.getNodePointer().getNode();
 assertNotSame(node1, node2);
 }

 @Test
 public void testIteratorGetNextNodeWithDifferentPrefixAndName() {
 Node node1 = new DOMAttributePointer(nodePointer, iterator.getNodePointer().getNode())
 .getNodePointer().getNode();
 QName name2 = new QName("testPrefix2:testLocalName2");
 iterator.next();
 Node node2 = iterator.getNodePointer().getNode();
 assertNotSame(node1, node2);
 }

 @Test
 public void testIteratorGetNodeWithXPath() {
 Node node = createTestElement().getElementsByTagName("testElement")
 .item(0).getAttributes().getNamedItem("testAttribute");
 QName name = new QName("", "testAttribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
 Node nodePointerNode = iterator.getNodePointer().getNode();
 assertEquals(node, nodePointerNode);
 }

 @Test
 public void testIteratorGetNextNodeWithXPath() {
 Node node1 = createTestElement().getElementsByTagName("testElement")
 .item(0).getAttributes().getNamedItem("testAttribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node1),
 new QName("", "testAttribute"));
 iterator.next();
 Node node2 = iterator.getNodePointer().getNode();
 assertNotSame(node1, node2);
 }

 private Element createTestElement() {
 Element element = new Element("testElement");
 element.setAttributeNS("testPrefix", "testLocalName", "testValue");
 element.setAttribute("testAttribute", "testValue2");
 return element;
 }

}