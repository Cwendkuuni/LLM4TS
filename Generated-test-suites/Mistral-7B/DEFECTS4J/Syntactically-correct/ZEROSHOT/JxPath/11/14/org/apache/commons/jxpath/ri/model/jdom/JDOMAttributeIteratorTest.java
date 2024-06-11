package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 @Test
 public void testSimpleAttributeIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");

 NodePointer rootPointer = new JDOMNodePointer(new Document(), root);
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(rootPointer, new QName("*:attr1"));

 NodePointer attributePointer = attributeIterator.getNodePointer();
 assertEquals("attr1", attributePointer.getNodeName().getLocalPart());
 assertEquals("value1", attributePointer.getNodeValue());
 assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testMultipleAttributesIterator() throws JDOMException {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 NodePointer rootPointer = new JDOMNodePointer(new Document(), root);
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(rootPointer, new QName("*:attr*"));

 NodePointer attributePointer1 = attributeIterator.getNodePointer();
 assertEquals("attr1", attributePointer1.getNodeName().getLocalPart());
 assertEquals("value1", attributePointer1.getNodeValue());

 NodePointer attributePointer2 = attributeIterator.getNodePointer();
 assertEquals("attr2", attributePointer2.getNodeName().getLocalPart());
 assertEquals("value2", attributePointer2.getNodeValue());

 assertFalse(attributeIterator.hasNext());
 }

 @Test
 public void testEmptyAttributeIterator() throws JDOMException {
 Element root = new Element("root");

 NodePointer rootPointer = new JDOMNodePointer(new Document(), root);
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(rootPointer, new QName("*:attr"));

 assertNull(attributeIterator.getNodePointer());
 assertFalse(attributeIterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParentPointer() {
 NodePointer parentPointer = null;
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, new QName("*:attr1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullQName() {
 NodePointer parentPointer = new JDOMNodePointer(new Document(), new Element("root"));
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidPosition() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 NodePointer rootPointer = new JDOMNodePointer(new Document(), root);
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(rootPointer, new QName("*:attr1"));

 attributeIterator.setPosition(-1);
 }

 @Test(expected = IllegalStateException.class)
 public void testPositionOutOfBounds() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");

 NodePointer rootPointer = new JDOMNodePointer(new Document(), root);
 JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(rootPointer, new QName("*:attr1"));

 attributeIterator.setPosition(3);
 }
}