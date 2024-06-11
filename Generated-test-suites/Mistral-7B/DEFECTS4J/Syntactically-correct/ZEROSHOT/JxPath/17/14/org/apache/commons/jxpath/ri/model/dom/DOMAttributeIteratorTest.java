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

 private DOMAttributeIterator iterator;
 private Element element;

 @Before
 public void setUp() {
 Element parentElement = newElement("parent");
 Element childElement = newElement("child");
 parentElement.appendChild(childElement);

 String attrName = "data-test";
 String attrValue = "value";
 Attr attr = newAttr("data-test", attrValue);
 childElement.setAttributeNode(attr);
 parentElement.setAttributeNodeNS(newAttr("xmlns:x", "http://example.com"), "xmlns:x");

 element = parentElement;
 }

 @Test
 public void testNewInstance() {
 iterator = new DOMAttributeIterator(new NodePointer(element), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNewInstanceWithEmptyName() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNewInstanceWithName() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 assertTrue(iterator.hasNext());
 Attr expectedAttribute = (Attr) element.getAttributes().getNamedItem("data-test");
 NodePointer expectedNodePointer = new DOMAttributePointer(new NodePointer(element), expectedAttribute);
 NodePointer actualNodePointer = iterator.next();
 assertEquals(expectedNodePointer, actualNodePointer);
 }

 @Test
 public void testNewInstanceWithNameAndPrefix() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("{http://example.com}data-test"));
 assertTrue(iterator.hasNext());
 Attr expectedAttribute = (Attr) element.getAttributeNodeNS("http://example.com", "data-test");
 NodePointer expectedNodePointer = new DOMAttributePointer(new NodePointer(element), expectedAttribute);
 NodePointer actualNodePointer = iterator.next();
 assertEquals(expectedNodePointer, actualNodePointer);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 Attr attribute = iterator.next();
 iterator.remove();
 fail("Remove operation is not supported");
 }

 @Test
 public void testHasNext() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 assertTrue(iterator.hasNext());
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextAfterLast() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.next();
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextWithEmptyList() {
 iterator = new DOMAttributeIterator(new NodePointer(new Element("root")), new QName());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextWithNullName() {
 iterator = new DOMAttributeIterator(new NodePointer(new Element("root")), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextWithEmptyParent() {
 iterator = new DOMAttributeIterator(new NodePointer(null), new QName());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextWithNullParent() {
 iterator = new DOMAttributeIterator(new NodePointer(null), new QName());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNextWithNonExistentName() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("non-existent"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testGetPosition() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.next();
 iterator.next();
 assertEquals(1, iterator.getPosition());
 }

 @Test
 public void testSetPositionToFirst() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.setPosition(0);
 NodePointer expectedNodePointer = new DOMAttributePointer(new NodePointer(element), (Attr) element.getAttributes().getNamedItem("data-test"));
 NodePointer actualNodePointer = iterator.next();
 assertEquals(expectedNodePointer, actualNodePointer);
 }

 @Test
 public void testSetPositionToMiddle() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.next();
 iterator.next();
 iterator.setPosition(2);
 assertEquals(0, iterator.getPosition());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionToLast() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.next();
 iterator.next();
 iterator.setPosition(3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionToNegative() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.setPosition(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPositionToInvalidPosition() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.setPosition(Integer.MAX_VALUE);
 }

 @Test
 public void testGetNodePointer() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.next();
 NodePointer expectedNodePointer = new DOMAttributePointer(new NodePointer(element), (Attr) element.getAttributes().getNamedItem("data-test"));
 NodePointer actualNodePointer = iterator.getNodePointer();
 assertEquals(expectedNodePointer, actualNodePointer);
 }

 @Test
 public void testGetNodePointerAfterLast() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.next();
 iterator.next();
 NodePointer actualNodePointer = iterator.getNodePointer();
 assertNull(actualNodePointer);
 }

 @Test
 public void testIteratorAfterReset() {
 iterator = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.reset();
 NodePointer expectedNodePointer = new DOMAttributePointer(new NodePointer(element), (Attr) element.getAttributes().getNamedItem("data-test"));
 NodePointer actualNodePointer = iterator.next();
 assertEquals(expectedNodePointer, actualNodePointer);
 }

 @Test
 public void testIteratorWithEmptyIterator() {
 NodeIterator emptyIterator = new DOMAttributeIterator(new NodePointer(new Element("root")), new QName());
 assertFalse(emptyIterator.hasNext());
 }

 @Test
 public void testIteratorWithEmptyIteratorAndName() {
 NodeIterator emptyIterator = new DOMAttributeIterator(new NodePointer(new Element("root")), new QName());
 NodeIterator anotherEmptyIterator = new DOMAttributeIterator(new NodePointer(new Element("root")), new QName());
 assertEquals(emptyIterator, anotherEmptyIterator);
 }

 @Test
 public void testIteratorWithIteratorAndName() {
 NodeIterator iterator1 = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 NodeIterator iterator2 = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 assertEquals(iterator1, iterator2);
 }

 @Test
 public void testIteratorWithIteratorAndDifferentName() {
 NodeIterator iterator1 = new DOMAttributeIterator(new NodePointer(element), new QName("data-test"));
 NodeIterator iterator2 = new DOMAttributeIterator(new NodePointer(element), new QName("xmlns:x"));
 assertNotEquals(iterator1, iterator2);
 }

 @Test
 public void testIteratorWithIteratorAndDifferentParent() {
 Element parentElement = newElement("parent");
 Element childElement1 = newElement("child1");
 Element childElement2 = newElement("child2");
 parentElement.appendChild(childElement1);
 parentElement.appendChild(childElement2);

 NodeIterator iterator1 = new DOMAttributeIterator(new NodePointer(childElement1), new QName("data-test"));
 NodeIterator iterator2 = new DOMAttributeIterator(new NodePointer(childElement2), new QName("data-test"));
 assertNotEquals(iterator1, iterator2);
 }

 @Test
 public void testIteratorWithIteratorAndDifferentParentWithSameName() {
 Element parentElement = newElement("parent");
 Element childElement1 = newElement("child1");
 Element childElement2 = newElement("child2");
 parentElement.appendChild(childElement1);
 parentElement.appendChild(childElement2);

 String attrName = "data-test";
 String attrValue = "value";
 Attr attr1 = newAttr("data-test", attrValue);
 childElement1.setAttributeNode(attr1);
 Attr attr2 = newAttr("data-test", attrValue);
 childElement2.setAttributeNode(attr2);

 NodeIterator iterator1 = new DOMAttributeIterator(new NodePointer(childElement1), new QName(attrName));
 NodeIterator iterator2 = new DOMAttributeIterator(new NodePointer(childElement2), new QName(attrName));
 assertEquals(iterator1, iterator2);
 }

 private Element newElement(String tagName) {
 return new Element(tagName);
 }

 private Attr newAttr(String namespaceURI, String localName) {
 Attr attr = new Attr();
 attr.setNamespaceURI(namespaceURI);
 attr.setPrefix(namespaceURI.substring(0, 6));
 attr.setLocalName(localName);
 attr.setValue("value");
 return attr;
 }
}