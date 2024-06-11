package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testJDOMAttributeIteratorEmpty() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", ""));
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testJDOMAttributeIteratorSingleAttribute() {
 Element element = new Element("root");
 Attribute attribute = new Attribute("name", "value", "ns");
 element.addAttribute(attribute);
 NodePointer nodePointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("ns", "name"));
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(new JDOMAttributePointer(nodePointer, attribute), next);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIteratorMultipleAttributes() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("attr1", "val1", "ns1");
 Attribute attribute2 = new Attribute("attr2", "val2", "ns1");
 Attribute attribute3 = new Attribute("attr3", "val3", "ns2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 NodePointer nodePointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "*"));
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(new JDOMAttributePointer(nodePointer, attribute1), next);
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(new JDOMAttributePointer(nodePointer, attribute2), next);
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(new JDOMAttributePointer(nodePointer, attribute3), next);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testJDOMAttributeIteratorNullParent() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", ""));
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIteratorNullName() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("root")), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIteratorEmptyElement() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("root")), new QName("", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJDOMAttributeIteratorUnknownNamespace() {
 Element element = new Element("root");
 Attribute attribute = new Attribute("name", "value", "ns");
 element.addAttribute(attribute);
 NodePointer nodePointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("unknown", "name"));
 }

}