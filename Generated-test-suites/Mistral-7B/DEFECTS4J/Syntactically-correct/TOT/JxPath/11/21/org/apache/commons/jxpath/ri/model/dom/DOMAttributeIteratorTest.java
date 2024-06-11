package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

 private NodeIterator iterator;
 private Element element;

 @Before
 public void setup() {
 // Create an example XML document with an Element and some Attributes
 String xml = "<root xmlns:x=\"http://example.com/\" xmlns:y=\"http://example.net/\">" +
 "<child x:attr1=\"value1\" y:attr2=\"value2\" data=\"data\"/>" +
 "</root>";
 Document document = JAXPUtils.parseToDocument(xml);
 element = (Element) document.getElementsByTagName("child").item(0);

 // Create a DOMAttributeIterator for the Element
 iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(document), new QName("*"));
 }

 @Test
 public void testIteratorNext() {
 // Test iterator next method with all attributes
 Attr attr = iterator.getNodePointer().getNode();
 assertEquals("attr1", attr.getName().getNodeValue());
 assertTrue(iterator.hasNext());
 attr = iterator.getNodePointer().getNode();
 assertEquals("attr2", attr.getName().getNodeValue());
 assertTrue(iterator.hasNext());
 attr = iterator.getNodePointer().getNode();
 assertNull(attr.getNodeValue()); // empty attribute
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testIteratorHasNext() {
 // Test iterator hasNext method with empty iterator
 iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(element.getOwnerDocument()), new QName("*", "nonexistent:prefix"));
 assertFalse(iterator.hasNext());

 // Test iterator hasNext method with all attributes
 iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(element), new QName("*"));
 attr = iterator.getNodePointer().getNode();
 assertTrue(iterator.hasNext());
 attr = iterator.getNodePointer().getNode();
 assertTrue(iterator.hasNext());
 attr = iterator.getNodePointer().getNode();
 assertTrue(iterator.hasNext());
 attr = iterator.getNodePointer().getNode(); // empty attribute
 assertTrue(iterator.hasNext());
 attr = iterator.getNodePointer().getNode(); // last attribute
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorEmptyIterator() {
 // Test iterator with empty iterator
 iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(element.getOwnerDocument()), new QName("nonexistent:prefix"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithFilteredName() {
 // Test iterator with filtered name
 iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(element), new QName("x:attr1"));
 Attr attr = iterator.getNodePointer().getNode();
 assertEquals("attr1", attr.getName().getNodeValue());
 assertTrue(iterator.hasNext());
 assertNull(iterator.getNodePointer().getNode()); // empty attribute
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespacePrefix() {
 // Test iterator with name in namespace
 iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(element), new QName("x:attr1"));
 Attr attr = iterator.getNodePointer().getNode();
 assertEquals("attr1", attr.getName().getNodeValue());
 assertTrue(iterator.hasNext());
 assertNull(iterator.getNodePointer().getNode()); // empty attribute
 assertFalse(iterator.hasNext());

 // Test iterator with prefix and name
 iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(element), new QName("x", "attr1"));
 attr = iterator.getNodePointer().getNode();
 assertEquals("attr1", attr.getName().getNodeValue());
 assertTrue(iterator.hasNext());
 assertNull(iterator.getNodePointer().getNode()); // empty attribute
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithFilteredNameAndNamespacePrefix() {
 // Test iterator with filtered name and namespace prefix
 iterator = new DOMAttributeIterator(new DOMPointers.DOMPointer(element), new QName("x", "attr1"));
 Attr attr = iterator.getNodePointer().getNode();
 assertEquals("attr1", attr.getName().getNodeValue());
 assertTrue(iterator.hasNext());
 assertNull(iterator.getNodePointer().getNode()); // empty attribute
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullIterator() {
 // Test iterator with null iterator
 new DOMAttributeIterator(null, new QName("*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullName() {
 // Test iterator with null name
 new DOMAttributeIterator(new DOMPointers.DOMPointer(element), null);
 }
}