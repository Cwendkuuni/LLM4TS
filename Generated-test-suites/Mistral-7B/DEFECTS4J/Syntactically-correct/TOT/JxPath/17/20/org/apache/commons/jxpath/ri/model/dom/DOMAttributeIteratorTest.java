package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodeIterator iterator;
 private Document document;
 private Element rootElement;

 @Before
 public void setUp() {
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 rootElement = document.createElement("root");
 document.appendChild(rootElement);

 // Add elements with attributes
 Element childElement1 = document.createElement("child1");
 childElement1.setAttribute("attr1", "value1");
 childElement1.setAttribute("xmlns:x", "http://example.com/namespace1");
 rootElement.appendChild(childElement1);

 Element childElement2 = document.createElement("child2");
 childElement2.setAttribute("attr2", "value2");
 rootElement.appendChild(childElement2);

 Element childElement3 = document.createElement("child3");
 childElement3.setAttribute("xmlns:x", "http://example.com/namespace1");
 childElement3.setAttribute("attr3", "value3");
 rootElement.appendChild(childElement3);
 }

 @Test
 public void testIteratorEmpty() {
 iterator = new DOMAttributeIterator(null, null);
 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorSingleAttribute() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, rootElement), new QName("*"));
 iterator.next();
 NodePointer nodePointer = iterator.getNodePointer();
 assertEquals("attr1", nodePointer.getNodeName().getLocalPart());
 assertEquals("value1", nodePointer.getNodeValue());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorMultipleAttributes() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, rootElement), new QName("*"));
 iterator.next(); // skip root element
 iterator.next(); // skip childElement1 with non-matching namespace
 iterator.next(); // childElement2 with matching namespace and attribute
 NodePointer nodePointer = iterator.getNodePointer();
 assertEquals("attr2", nodePointer.getNodeName().getLocalPart());
 assertEquals("value2", nodePointer.getNodeValue());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorWithNamespace() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, rootElement), new QName("x:attr3"));
 iterator.next(); // skip childElement1 with different namespace
 iterator.next(); // childElement2 with different namespace
 iterator.next(); // childElement3 with matching namespace and attribute
 NodePointer nodePointer = iterator.getNodePointer();
 assertEquals("attr3", nodePointer.getNodeName().getLocalPart());
 assertEquals("value3", nodePointer.getNodeValue());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorError() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, null), new QName("attr1")); // Null parent
 assertNull(iterator);
 }

 @Test
 public void testIteratorEndOfList() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, rootElement), new QName("*"));
 iterator.next(); // childElement1
 iterator.next(); // childElement2
 iterator.next(); // childElement3
 iterator.next(); // past the end of the list
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testIteratorReset() {
 iterator = new DOMAttributeIterator(new DOMPointer(document, rootElement), new QName("*"));
 iterator.next(); // childElement1
 iterator.next(); // childElement2
 iterator.next(); // childElement3
 iterator.reset();
 iterator.next(); // childElement1
 assertEquals("attr1", iterator.getNodePointer().getNodeName().getLocalPart());
 }
}