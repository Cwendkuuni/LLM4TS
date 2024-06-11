package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

 private Node testNode;
 private Element testElement;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 testNode = createTestNode();
 testElement = (Element) testNode;
 testElement.setAttributeNS("http://example.com/test", "test:attribute1", "value1");
 testElement.setAttributeNS("http://example.com/test", "test:attribute2", "value2");
 }

 @Test
 public void testEmptyIterator() {
 DOMAttributeIterator emptyIterator = new DOMAttributeIterator(null, null);
 Assert.assertFalse(emptyIterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() {
 DOMAttributeIterator singleAttributeIterator = new DOMAttributeIterator(new DOMPoint(testNode), new QName("test:attribute1"));
 Assert.assertTrue(singleAttributeIterator.hasNext());
 NodePointer nodePointer = singleAttributeIterator.next();
 Assert.assertNotNull(nodePointer);
 Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
 }

 @Test
 public void testMultipleAttributesIterator() {
 DOMAttributeIterator multipleAttributesIterator = new DOMAttributeIterator(new DOMPoint(testElement), new QName("*"));
 Assert.assertTrue(multipleAttributesIterator.hasNext());
 NodePointer nodePointer = multipleAttributesIterator.next();
 Assert.assertNotNull(nodePointer);
 Assert.assertTrue(nodePointer instanceof DOMAttributePointer);

 NodePointer nextNodePointer = multipleAttributesIterator.next();
 Assert.assertNotNull(nextNodePointer);
 Assert.assertTrue(nextNodePointer instanceof DOMAttributePointer);
 }

 @Test(expected = IllegalStateException.class)
 public void testIteratorBeyondEnd() {
 DOMAttributeIterator multipleAttributesIterator = new DOMAttributeIterator(new DOMPoint(testElement), new QName("*"));
 multipleAttributesIterator.next();
 multipleAttributesiterator.next();
 }

 @Test
 public void testIteratorToBeginning() {
 DOMAttributeIterator multipleAttributesIterator = new DOMAttributeIterator(new DOMPoint(testElement), new QName("*"));
 multipleAttributesIterator.next();
 Assert.assertTrue(multipleAttributesIterator.hasPrevious());
 NodePointer nodePointer = multipleAttributesIterator.previous();
 Assert.assertNotNull(nodePointer);
 Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
 }

 @Test
 public void testIteratorMixedNamespaces() {
 Element testElement2 = createTestNodeWithMixedNamespaces();
 DOMAttributeIterator multipleAttributesIterator = new DOMAttributeIterator(new DOMPoint(testElement2), new QName("*", "http://example.com/test"));
 Assert.assertTrue(multipleAttributesIterator.hasNext());
 NodePointer nodePointer = multipleAttributesIterator.next();
 Assert.assertNotNull(nodePointer);
 Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
 }

 @Test
 public void testIteratorEmptyNamespace() {
 Element testElement3 = createTestNodeWithEmptyNamespace();
 DOMAttributeIterator multipleAttributesIterator = new DOMAttributeIterator(new DOMPoint(testElement3), new QName("test:attribute1"));
 Assert.assertFalse(multipleAttributesIterator.hasNext());
 }

 @Test
 public void testIteratorNullArgument() {
 Assert.assertThrows(NullPointerException.class, () -> new DOMAttributeIterator(null, new QName("test:attribute1")));
 Assert.assertThrows(NullPointerException.class, () -> new DOMAttributeIterator(new DOMPoint(null), new QName("test:attribute1")));
 }

 private Node createTestNode() {
 Element element = document.createElement("test");
 document.appendChild(element);
 return element;
}

 private Element createTestNodeWithMixedNamespaces() {
 Element element = document.createElement("test");
 element.setAttributeNS("http://example.com/test1", "test:attribute1", "value1");
 element.setAttributeNS("http://example.com/test2", "test:attribute2", "value2");
 document.appendChild(element);
 return element;
}

 private Element createTestNodeWithEmptyNamespace() {
 Element element = document.createElement("test");
 element.setAttributeNode(document.createAttribute("test:attribute1"));
 element.setAttributeNode(document.createAttribute("test:attribute2"));
 document.appendChild(element);
 return element;
}

}