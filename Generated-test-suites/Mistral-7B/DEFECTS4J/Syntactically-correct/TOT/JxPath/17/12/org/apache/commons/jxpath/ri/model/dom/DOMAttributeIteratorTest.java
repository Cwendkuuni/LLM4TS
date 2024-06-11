package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer rootNodePointer;
 private NodeIterator attributeIterator;

 @Before
 public void setup() {
 String xml = "<root xmlns:x='http://test.com/'><elem xmlns:y='http://test.com/' attr1='value1' attr2='value2'/></root>";
 Document document = JaxpFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
 Element rootElement = (Element) document.getDocumentElement();
 rootNodePointer = new DOMPointNodePointer(document, rootElement);
 attributeIterator = new DOMAttributeIterator(rootNodePointer, new QName("*"));
 }

 @Test
 public void testEmptyIterator() {
 assertNull(attributeIterator.getNodePointer());
 assertTrue(attributeIterator.getPosition() == 0);
 }

 @Test
 public void testIteratorHasAttributes() {
 Element elem = (Element) attributeIterator.getNodePointer().getNode();
 List<Attr> attributes = new ArrayList<>();
 attributes.add(elem.getAttributeNode("attr1"));
 attributes.add(elem.getAttributeNode("attr2"));
 for (int i = 0; i < attributes.size(); i++) {
 Attr testAttr = (Attr) attributeIterator.getNodePointer().getNode();
 assertEquals(attributes.get(i), testAttr);
 assertTrue(attributeIterator.setPosition(attributeIterator.getPosition() + 1));
 }
 assertNull(attributeIterator.getNodePointer());
 assertTrue(attributeIterator.getPosition() == 0);
 }

 @Test
 public void testIteratorHasNoMatchingAttributes() {
 attributeIterator = new DOMAttributeIterator(rootNodePointer, new QName("attr3"));
 for (int i = 0; i < 10; i++) {
 assertNull(attributeIterator.getNodePointer());
 assertTrue(attributeIterator.getPosition() == 0);
 assertTrue(attributeIterator.setPosition(attributeIterator.getPosition() + 1));
 }
 }

 @Test
 public void testIteratorHasNoAttributes() {
 DocumentType docType = (DocumentType) rootNodePointer.getNode();
 attributeIterator = new DOMAttributeIterator(rootNodePointer, new QName("*"));
 for (int i = 0; i < 10; i++) {
 assertNull(attributeIterator.getNodePointer());
 assertTrue(attributeIterator.getPosition() == 0);
 assertTrue(attributeIterator.setPosition(attributeIterator.getPosition() + 1));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParentNodePointer() {
 attributeIterator = new DOMAttributeIterator(null, new QName("*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNameQName() {
 attributeIterator = new DOMAttributeIterator(rootNodePointer, null);
 }
}