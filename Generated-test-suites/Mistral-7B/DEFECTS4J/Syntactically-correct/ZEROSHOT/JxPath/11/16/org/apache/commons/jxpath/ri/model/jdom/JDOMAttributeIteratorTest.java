package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyAttributeIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null), new QName("", ""));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testAttributeIteratorWithSingleAttribute() {
 JDOMElement element = new JDOMElement("root", new Namespace("ns", "http://example.com"));
 element.addAttribute("attr", "value", "ns");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("ns", "attr"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 NodePointer nextNode = iterator.next();
 assertEquals(nextNode.getNode(), element.getAttribute("attr"));
 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testAttributeIteratorWithMultipleAttributes() {
 JDOMElement element = new JDOMElement("root", new Namespace("ns", "http://example.com"));
 element.addAttribute("attr1", "value1", "ns");
 element.addAttribute("attr2", "value2", "ns");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("ns", ""));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 NodePointer nextNode = iterator.next();
 assertEquals(nextNode.getNode(), element.getAttribute("attr1"));
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer nextNode2 = iterator.next();
 assertEquals(nextNode2.getNode(), element.getAttribute("attr2"));
 assertEquals(3, iterator.getPosition());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testAttributeIteratorWithNamespaceAndWildcard() {
 JDOMElement element = new JDOMElement("root", new Namespace("ns", "http://example.com"));
 element.addAttribute("attr1", "value1", new Namespace("prefix1", "http://prefix1.com"));
 element.addAttribute("attr2", "value2", new Namespace("prefix2", "http://prefix2.com"));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("", "*"));
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer nextNode = iterator.next();
 assertEquals(nextNode.getNode(), element.getAttribute("attr1"));
 assertEquals(3, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer nextNode2 = iterator.next();
 assertEquals(nextNode2.getNode(), element.getAttribute("attr2"));
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testAttributeIteratorWithEmptyNamespace() {
 JDOMElement element = new JDOMElement("root");
 element.addAttribute("attr", "value");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("", ""));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 NodePointer nextNode = iterator.next();
 assertEquals(nextNode.getNode(), element.getAttribute("attr"));
 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testAttributeIteratorWithEmptyNamespaceAndWildcard() {
 JDOMElement element = new JDOMElement("root");
 element.addAttribute("attr1", "value1");
 element.addAttribute("attr2", "value2");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("", "*"));
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer nextNode = iterator.next();
 assertEquals(nextNode.getNode(), element.getAttribute("attr1"));
 assertEquals(3, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer nextNode2 = iterator.next();
 assertEquals(nextNode2.getNode(), element.getAttribute("attr2"));
 assertFalse(iterator.hasPrevious());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithNullParent() {
 new JDOMAttributeIterator(null, new QName("", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithNullName() {
 new JDOMAttributeIterator(new JDOMNodePointer(new JDOMDocument()), null);
 }
}