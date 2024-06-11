package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(new Element("root")), new QName("", "*"));
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorWithSingleAttribute() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("", "attr1"));
 NodePointer nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 assertNotNull(nodePointer.getNode());
 assertEquals("value1", nodePointer.getNode().getValue());
 }

 @Test
 public void testIteratorWithMultipleAttributes() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("", "*"));
 NodePointer nodePointer = null;
 for (int i = 0; i < 2; i++) {
 nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 }
 assertFalse(iterator.getNodePointer() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullParent() {
 new JDOMAttributeIterator(null, new QName("", "*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullQName() {
 new JDOMAttributeIterator(new JDOMNodePointer(new Element("root")), null);
 }

 @Test
 public void testIteratorWithEmptyQName() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(new Element("root")), new QName("", ""));
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorWithNonExistentAttribute() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("", "attr2"));
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorWithSameAttributeMultipleTimes() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr1", "value2");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("", "attr1"));
 NodePointer nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 assertEquals("value2", nodePointer.getNode().getValue()); // second call returns the last found attribute
 }

 @Test
 public void testIteratorWithXmlNamespace() throws JDOMException {
 Element root = new Element("root", "xml", "http://www.w3.org/XML/1998/namespace");
 root.addAttribute("xml:attr1", "value1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("xml", "attr1"));
 NodePointer nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 assertEquals("value1", nodePointer.getNode().getValue());
 }

 @Test
 public void testIteratorWithNamespaceURI() throws JDOMException {
 Element root = new Element("root");
 root.addNamespaceDeclaration("ns", "http://test.com/ns");
 root.addAttribute("ns:attr1", "value1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("ns", "attr1"));
 NodePointer nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 assertEquals("value1", nodePointer.getNode().getValue());
 }

 @Test
 public void testIteratorWithEmptyNamespace() throws JDOMException {
 Element root = new Element("root");
 root.addNamespaceDeclaration("", "");
 root.addAttribute("", "attr1", "value1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(root), new QName("", "attr1"));
 NodePointer nodePointer = iterator.getNodePointer();
 assertNotNull(nodePointer);
 assertEquals("value1", nodePointer.getNode().getValue());
 }
}