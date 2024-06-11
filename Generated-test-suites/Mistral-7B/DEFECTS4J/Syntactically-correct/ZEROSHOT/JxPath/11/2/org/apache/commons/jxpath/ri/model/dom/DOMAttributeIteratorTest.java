package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeIterator;

public class DOMAttributeIteratorTest {

 private Element testElement;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 testElement = new Element.Builder("root").build();
 testElement.setAttribute("attr1", "val1");
 testElement.setAttribute("attr2", "val2");
 testElement.setAttributeNS("xmlns:x", "attr3", "val3");
 }

 @Test
 public void testEmpty() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null), null);
 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testOneAttributeNameMatch() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("attr1"));
 assertEquals(1, iterator.getPosition());
 assertSame(testElement.getAttributeNode("attr1"), iterator.getNodePointer().getDOMAttr());
 }

 @Test
 public void testOneAttributeNSMatch() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("xmlns:x", "attr3"));
 assertEquals(1, iterator.getPosition());
 assertSame(testElement.getAttributeNodeNS("xmlns:x", "attr3"), iterator.getNodePointer().getDOMAttr());
 }

 @Test
 public void testOneAttributeNameWildcardMatch() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("*"));
 assertEquals(3, iterator.getPosition());
 }

 @Test
 public void testOneAttributeNSWildcardMatch() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("xmlns:*", "*"));
 assertEquals(3, iterator.getPosition());
 }

 @Test
 public void testMultipleAttributesNameMatch() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("attr1"));
 iterator.next();
 DOMAttributeIterator nextIterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("attr1"));
 assertEquals(iterator.getNodePointer().getNode(), nextIterator.getNodePointer().getNode());
 assertSame(iterator.getNodePointer().getDOMAttr(), nextIterator.getNodePointer().getDOMAttr());
 }

 @Test
 public void testMultipleAttributesNSMatch() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("xmlns:x", "attr3"));
 iterator.next();
 DOMAttributeIterator nextIterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("xmlns:x", "attr3"));
 assertEquals(iterator.getNodePointer().getNode(), nextIterator.getNodePointer().getNode());
 assertSame(iterator.getNodePointer().getDOMAttr(), nextIterator.getNodePointer().getDOMAttr());
 }

 @Test
 public void testMultipleAttributesNameWildcardMatch() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("*"));
 NodePointer firstAttr = iterator.getNodePointer();
 iterator.next();
 NodePointer secondAttr = iterator.getNodePointer();
 assertSame(testElement, firstAttr.getNode().getParentNode());
 assertSame(testElement, secondAttr.getNode().getParentNode());
 assertSame(testElement, firstAttr.getNode().getPreviousSibling());
 assertSame(testElement.getAttributeNode("attr1"), firstAttr.getDOMAttr());
 assertSame(testElement.getAttributeNode("attr2"), secondAttr.getDOMAttr());
 }

 @Test
 public void testMultipleAttributesNSWildcardMatch() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("xmlns:*", "*"));
 NodePointer firstAttr = iterator.getNodePointer();
 iterator.next();
 NodePointer secondAttr = iterator.getNodePointer();
 assertSame(testElement, firstAttr.getNode().getParentNode());
 assertSame(testElement, secondAttr.getNode().getParentNode());
 assertSame(testElement, firstAttr.getNode().getPreviousSibling());
 assertSame(testElement.getAttributeNodeNS("xmlns:x", "attr3"), firstAttr.getDOMAttr());
 assertSame(testElement.getAttributeNodeNS("xmlns:x", "attr3"), secondAttr.getDOMAttr());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextOutOfBounds() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("*"));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextOutOfBoundsAfterLast() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("*"));
 iterator.next();
 iterator.next();
 iterator.next();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPreviousOutOfBounds() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("attr1"));
 iterator.next();
 iterator.previous();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPreviousOutOfBoundsBeforeFirst() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(testElement), new QName("attr1"));
 }
}