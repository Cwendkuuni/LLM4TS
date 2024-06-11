package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private Element testElement;
 private NodeIterator attributeIterator;

 @Before
 public void setUp() {
 testElement = createTestElement();
 }

 @Test
 public void testEmptyIterator() {
 NodePointer parent = new DOMPoint(testElement);
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 NodePointer parent = new DOMPoint(testElement);
 QName attributeName = new QName("", "attr1");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName);
 assertEquals(attributeName.toString(), iterator.next().getNodeName());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 NodePointer parent = new DOMPoint(testElement);
 QName attributeName = new QName("", "attr1");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName);
 iterator.next();
 iterator.next();
 assertEquals(attributeName.toString(), iterator.next().getNodeName());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithDifferentNamespaces() {
 NodePointer parent = new DOMPoint(testElement);
 QName attributeName1 = new QName("ns1:", "attr1");
 QName attributeName2 = new QName("ns2:", "attr2");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName1);
 iterator.next(); // skip the default namespace attribute
 iterator.next();
 Attr attr1 = (Attr) iterator.next().getNode();
 assertEquals(attributeName1.toString(), attr1.getNodeName());
 iterator.reset();
 iterator = new DOMAttributeIterator(parent, attributeName2);
 iterator.next(); // skip the default namespace attribute
 Attr attr2 = (Attr) iterator.next().getNode();
 assertEquals(attributeName2.toString(), attr2.getNodeName());
 }

 @Test
 public void testIteratorWithSameNamespaceButDifferentLocalNames() {
 NodePointer parent = new DOMPoint(testElement);
 String ns = "ns1:";
 QName attributeName1 = new QName(ns, "attr1");
 QName attributeName2 = new QName(ns, "attr2");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName1);
 iterator.next(); // skip the default namespace attribute
 Attr attr1 = (Attr) iterator.next().getNode();
 assertEquals(attributeName1.toString(), attr1.getNodeName());
 iterator.reset();
 iterator = new DOMAttributeIterator(parent, attributeName2);
 iterator.next(); // skip the default namespace attribute
 Attr attr2 = (Attr) iterator.next().getNode();
 assertEquals(attributeName2.toString(), attr2.getNodeName());
 }

 @Test
 public void testIteratorWithSameNameButDifferentNamespaces() {
 NodePointer parent = new DOMPoint(testElement);
 String ns1 = "ns1:";
 String ns2 = "ns2:";
 QName attributeName1 = new QName(ns1, "attr1");
 QName attributeName2 = new QName(ns2, "attr1");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName1);
 iterator.next(); // skip the default namespace attribute
 Attr attr1 = (Attr) iterator.next().getNode();
 assertEquals(attributeName1.toString(), attr1.getNodeName());
 iterator.reset();
 iterator = new DOMAttributeIterator(parent, attributeName2);
 iterator.next(); // skip the default namespace attribute
 Attr attr2 = (Attr) iterator.next().getNode();
 assertEquals(attributeName2.toString(), attr2.getNodeName());
 assertNotEquals(attr1.getOwnerElement(), attr2.getOwnerElement());
 }

 @Test
 public void testIteratorWithMixedNamespaces() {
 NodePointer parent = new DOMPoint(testElement);
 String ns1 = "ns1:";
 String ns2 = "ns2:";
 QName attributeName1 = new QName(ns1, "attr1");
 QName attributeName2 = new QName(ns2, "attr2");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName1);
 iterator.next(); // skip the default namespace attribute
 Attr attr1 = (Attr) iterator.next().getNode();
 assertEquals(attributeName1.toString(), attr1.getNodeName());
 iterator.reset();
 iterator = new DOMAttributeIterator(parent, attributeName2);
 iterator.next(); // skip the default namespace attribute
 Attr attr2 = (Attr) iterator.next().getNode();
 assertEquals(attributeName2.toString(), attr2.getNodeName());
 assertNotEquals(attr1.getOwnerElement(), attr2.getOwnerElement());
 }

 @Test
 public void testIteratorWithNoMatchingAttributes() {
 NodePointer parent = new DOMPoint(testElement);
 QName attributeName = new QName("ns1:", "attr1");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, attributeName);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNullParent() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("", ""));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNullAttributeName() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testElement), null);
 assertFalse(iterator.hasNext());
 }

 private Element createTestElement() {
 Element element = testDocument.createElement("testElement");
 element.setAttributeNS("ns1:", "attr1", "value1");
 element.setAttributeNS("ns1:", "attr2", "value2");
 element.setAttributeNS("ns2:", "attr3", "value3");
 element.setAttributeNode(testDocument.createAttribute("attr4"));
 element.setAttributeNode("attr5");
 testDocument.appendChild(element);
 return element;
 }

 private Document testDocument = createTestDocument();
}