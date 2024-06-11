package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

 private static final String XML_NS = "xml";
 private static final String PREFIX = "tst";
 private static final String LOCAL_NAME = "attr";

 @SuppressWarnings("unchecked")
 @org.junit.Before
 public void setUp() {
 String xml = "<root xmlns:xml=\"" + XML_NS + "\">"
         + "<elem xmlns:tst=\"tst\">"
         + "<attr name=\"a\" />"
         + "<attr name=\"b\" />"
         + "<attr name=\"c\" />"
         + "</elem>"
         + "</root>";
 JDOMContext context = new JDOMContext();
 Document doc = context.build(new StringReader(xml));
 Element root = doc.getRootElement();
 QName name = new QName(new QName(XML_NS, "*").getNamespaceURI(), LOCAL_NAME);
 this.iterator = new JDOMAttributeIterator(new JDOMPointer(root, 0), name);
 }

 private NodeIterator iterator;

 @org.junit.Test
 public void testEmptyIterator() {
 NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(null, null), null);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 }

 @org.junit.Test
 public void testIteratorWithOneAttribute() {
 Attribute attribute = this.iterator.next().getNode().getObject();
 assertEquals(LOCAL_NAME, attribute.getName());
 assertEquals("", attribute.getValue());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testIteratorWithMultipleAttributes() {
 Attribute firstAttribute = this.iterator.next().getNode().getObject();
 assertEquals(LOCAL_NAME, firstAttribute.getName());
 assertEquals("", firstAttribute.getValue());
 Attribute secondAttribute = this.iterator.next().getNode().getObject();
 assertEquals(LOCAL_NAME, secondAttribute.getName());
 assertEquals("", secondAttribute.getValue());
 Attribute thirdAttribute = this.iterator.next().getNode().getObject();
 assertEquals(LOCAL_NAME, thirdAttribute.getName());
 assertEquals("", thirdAttribute.getValue());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testSetPositionWithValidIndex() {
 for (int i = 0; i < 3; i++) {
 this.iterator.next();
 }
 this.iterator.setPosition(1);
 NodePointer nodePointer = this.iterator.next();
 Attribute attribute = nodePointer.getNode().getObject();
 assertEquals(LOCAL_NAME, attribute.getName());
 assertEquals("", attribute.getValue());
 }

 @org.junit.Test
 public void testSetPositionWithInvalidIndex() {
 for (int i = 0; i < 3; i++) {
 this.iterator.next();
 }
 this.iterator.setPosition(5);
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testSetPositionWithZeroIndex() {
 this.iterator.setPosition(0);
 NodePointer nodePointer = this.iterator.next();
 Attribute attribute = nodePointer.getNode().getObject();
 assertEquals(LOCAL_NAME, attribute.getName());
 assertEquals("", attribute.getValue());
 }

 @org.junit.Test
 public void testSetPositionWithLastIndex() {
 List<Attribute> attributes = (List<Attribute>) this.iterator.toList();
 int lastIndex = attributes.size() - 1;
 this.iterator.setPosition(lastIndex + 1);
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @org.junit.Test
 public void testToListWhenNoAttributes() {
 List<NodePointer> list = this.iterator.toList();
 assertEquals(0, list.size());
 }

 @org.junit.Test
 public void testToList() {
 List<NodePointer> list = this.iterator.toList();
 List<Attribute> attributes = (List<Attribute>) this.iterator.toList();
 assertEquals(3, list.size());
 assertEquals(3, attributes.size());
 for (int i = 0; i < 3; i++) {
 NodePointer nodePointer = list.get(i);
 Attribute attribute = attributes.get(i);
 assertEquals(nodePointer.getNode().getObject(), attribute);
 }
 }
}