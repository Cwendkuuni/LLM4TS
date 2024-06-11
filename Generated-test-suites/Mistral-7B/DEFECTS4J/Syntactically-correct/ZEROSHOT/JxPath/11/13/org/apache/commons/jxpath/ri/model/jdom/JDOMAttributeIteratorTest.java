package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer parent;
 private JDOMAttributeIterator iterator;

 @Before
 public void setUp() {
 String xml = "<root xmlns:x='http://test.com'>" +
 "<child xmlns:y='http://test.com' attr1='value1' attr2='value2'/>" +
 "</root>";
 Element root = new Element("root", "http://test");
 root.addContent(new Element("child", "http://test")
 .addAttribute("attr1", "value1")
 .addAttribute("attr2", "value2"));
 parent = new NodePointer(root);
 }

 @Test
 public void testAttributeIteratorWithName() {
 QName name = new QName("*:attr1");
 iterator = new JDOMAttributeIterator(parent, name);
 NodePointer nodePointer = iterator.next();
 assertTrue(nodePointer != null);
 Attribute attribute = (Attribute) nodePointer.getNode();
 assertEquals("attr1", attribute.getName());
 assertEquals("value1", attribute.getValue());
 }

 @Test
 public void testAttributeIteratorWithNamespace() {
 QName name = new QName("x:attr1");
 iterator = new JDOMAttributeIterator(parent, name);
 NodePointer nodePointer = iterator.next();
 assertTrue(nodePointer != null);
 Attribute attribute = (Attribute) nodePointer.getNode();
 assertEquals("attr1", attribute.getName().getLocalPart());
 assertEquals("value1", attribute.getValue());
 assertEquals("http://test.com", attribute.getNamespaceURI());
 }

 @Test
 public void testAttributeIteratorWithWildcardName() {
 QName name = new QName("*:*");
 iterator = new JDOMAttributeIterator(parent, name);
 NodePointer nodePointer = iterator.next();
 assertTrue(nodePointer != null);
 Attribute attribute = (Attribute) nodePointer.getNode();
 assertEquals("attr1", attribute.getName().getLocalPart());
 assertEquals("value1", attribute.getValue());
 nodePointer = iterator.next();
 assertTrue(nodePointer != null);
 attribute = (Attribute) nodePointer.getNode();
 assertEquals("attr2", attribute.getName().getLocalPart());
 assertEquals("value2", attribute.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithNullParent() {
 iterator = new JDOMAttributeIterator(null, new QName("*:*"));
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithNullName() {
 iterator = new JDOMAttributeIterator(parent, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalStateException.class)
 public void testAttributeIteratorPastEnd() {
 iterator = new JDOMAttributeIterator(parent, new QName("*:*"));
 iterator.next();
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.next();
 assertNull(nodePointer);
 }
}