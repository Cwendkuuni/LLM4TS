package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

/**
 * Test class for DOMAttributeIterator.
 */
public class DOMAttributeIteratorTest {

 private NodePointer root;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 Element rootElement = createRootElement();
 root = new DOMPointer(rootElement);
 }

 /**
 * Creates root element with some attributes.
 *
 * @return root element.
 */
 private Element createRootElement() {
 Element root = document.createElement("root");
 root.setAttribute("attr1", "value1");
 root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://example.com/x");
 root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:y", "http://example.com/y");
 Element child = document.createElement("child");
 child.setAttribute("attr2", "value2");
 child.setAttribute("attr3", "value3");
 root.appendChild(child);
 return root;
 }

 /**
 * Tests constructor with wildcard name.
 */
 @Test
 public void testConstructorWithWildcardName() {
 iterator = new DOMAttributeIterator(root, new QName("*"));
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("attr1", nodePointer.getNodeName().getLocalPart());
 nodePointer = iterator.next();
 assertEquals("xmlns:x", nodePointer.getNodeName().getLocalPart());
 nodePointer = iterator.next();
 assertEquals("xmlns:y", nodePointer.getNodeName().getLocalPart());
 nodePointer = iterator.next();
 assertNull(nodePointer);
 }

 /**
 * Tests constructor with specific name.
 */
 @Test
 public void testConstructorWithSpecificName() {
 iterator = new DOMAttributeIterator(root, new QName("attr1"));
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("attr1", nodePointer.getNodeName().getLocalPart());
 nodePointer = iterator.next();
 assertNull(nodePointer);
 }

 /**
 * Tests constructor with specific namespace and name.
 */
 @Test
 public void testConstructorWithSpecificNamespaceAndName() {
 iterator = new DOMAttributeIterator(root, new QName("http://example.com/x", "xmlns"));
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("xmlns", nodePointer.getNodeName().getLocalPart());
 nodePointer = iterator.next();
 assertNull(nodePointer);
 }

 /**
 * Tests next method.
 */
 @Test
 public void testNext() {
 iterator = new DOMAttributeIterator(root, new QName("attr1"));
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 nodePointer = iterator.next();
 assertNull(nodePointer);
 }

 /**
 * Tests hasNext method.
 */
 @Test
 public void testHasNext() {
 iterator = new DOMAttributeIterator(root, new QName("attr1"));
 assertTrue(iterator.hasNext());
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 /**
 * Tests constructor with null root or name.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullRootOrName() {
 new DOMAttributeIterator(null, new QName(""));
 new DOMAttributeIterator(root, null);
 }

 /**
 * Tests getNodeName method.
 */
 @Test
 public void testgetNodeName() {
 iterator = new DOMAttributeIterator(root, new QName("attr1"));
 NodePointer nodePointer = iterator.next();
 assertEquals("attr1", nodePointer.getNodeName().getLocalPart());
 }

 /**
 * Tests getNodeValue method.
 */
 @Test
 public void testgetNodeValue() {
 iterator = new DOMAttributeIterator(root, new QName("attr1"));
 NodePointer nodePointer = iterator.next();
 assertEquals("value1", nodePointer.getNodeValue());
 }

 /**
 * Tests getNamespaceURI method.
 */
 @Test
 public void testgetNamespaceURI() {
 iterator = new DOMAttributeIterator(root, new QName("xmlns:x"));
 NodePointer nodePointer = iterator.next();
 assertEquals("http://example.com/x", nodePointer.getNamespaceURI());
 }
}