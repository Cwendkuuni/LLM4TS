package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit. Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private QName attributeName;
 private NodeIterator iterator;

 @Before
 public void setUp() {
 // Create test data
 Element root = new Element("root");
 Element parent = root.addElement("parent");
 parent.setAttribute("test-attr", "test-value");
 parent.addAttribute("another-attr", "another-value");

 parentNode = new NodePointer(root, parent);
 attributeName = new QName("*"); // wildcard QName for all attributes
 }

 @Test
 public void testEmptyIterator() {
 iterator = new JDOMAttributeIterator(new NodePointer(null), null);
 Assert.assertNull(iterator.getNodePointer());
 }

 @Test
 public void testSingleAttributeIterator() {
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 NodePointer nodePointer = iterator.getNodePointer();
 Element attributeElement = (Element) nodePointer.getNode();
 assertEquals("test-attr", attributeElement.getName());
 assertEquals("test-value", attributeElement.getValue());
 assertNull(iterator.getNodePointer());

 // Test repositioning
 iterator.setPosition(1);
 nodePointer = iterator.getNodePointer();
 attributeElement = (Element) nodePointer.getNode();
 assertEquals("another-attr", attributeElement.getName());
 assertEquals("another-value", attributeElement.getValue());
 }

 @Test
 public void testMultipleAttributesIterator() {
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 NodePointer nodePointer1 = iterator.getNodePointer();
 Element attributeElement1 = (Element) nodePointer1.getNode();
 assertEquals("test-attr", attributeElement1.getName());
 assertEquals("test-value", attributeElement1.getValue());
 assertNull(iterator.getNodePointer());

 NodePointer nodePointer2 = iterator.getNodePointer();
 Element attributeElement2 = (Element) nodePointer2.getNode();
 assertEquals("another-attr", attributeElement2.getName());
 assertEquals("another-value", attributeElement2.getValue());
 }

 @Test
 public void testSetPositionOutOfBounds() {
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 iterator.setPosition(3);
 assertFalse(iterator.getNodePointer() != null);
 }

 @Test
 public void testSetPositionNegative() {
 iterator = new JDOMAttributeIterator(parentNode, attributeName);
 iterator.setPosition(-1);
 assertFalse(iterator.getNodePointer() != null);
 }
}