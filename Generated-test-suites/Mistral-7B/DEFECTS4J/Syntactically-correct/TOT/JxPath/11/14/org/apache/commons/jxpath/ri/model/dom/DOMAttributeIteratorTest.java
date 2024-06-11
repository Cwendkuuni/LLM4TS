package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer parentNodePointer;
 private DOMAttributeIterator iterator;
 private Element testElement;

 @Before
 public void setUp() throws Exception {
 Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 testElement = document.createElement("test");
 testElement.setAttribute("test1", "value1");
 testElement.setAttribute("test2", "value2");
 testElement.setAttribute("xmlns:prefix1", "http://test.com/prefix1");
 testElement.setAttribute("xmlns:prefix2", "http://test.com/prefix2");
 testElement.setAttributeNS("http://test.com", "test3", "value3");
 testElement.setAttributeNS("http://test.com", "test4", "value4");
 document.appendChild(testElement);
 parentNodePointer = new DOMPointer(document, "/");
 }

 @Test
 public void testAttributeIteratorWithName() {
 QName name = new QName("*,prefix1:test1");
 iterator = new DOMAttributeIterator(parentNodePointer, name);
 NodePointer nodePointer = iterator.next();
 assertEquals(name, nodePointer.getQName());
 assertEquals("value1", nodePointer.getNodeValue());
 }

 @Test
 public void testAttributeIteratorWithPrefix() {
 QName name = new QName("prefix1:test1", null);
 iterator = new DOMAttributeIterator(parentNodePointer, name);
 NodePointer nodePointer = iterator.next();
 assertEquals(name, nodePointer.getQName());
 assertEquals("value1", nodePointer.getNodeValue());
 }

 @Test
 public void testAttributeIteratorWithNamespace() {
 QName name = new QName("http://test.com", "test3");
 iterator = new DOMAttributeIterator(parentNodePointer, name);
 NodePointer nodePointer = iterator.next();
 assertEquals(name, nodePointer.getQName());
 assertEquals("value3", nodePointer.getNodeValue());
 }

 @Test
 public void testAttributeIteratorWithWildcard() {
 QName name = new QName("*");
 iterator = new DOMAttributeIterator(parentNodePointer, name);
 NodePointer nodePointer = null;
 while ((nodePointer = iterator.next()) != null) {
 assertEquals(nodePointer.getQName().getName(), "test1");
 assertEquals(nodePointer.getNodeValue(), "value1");
 }
 }

 @Test
 public void testAttributeIteratorWithEmptyList() {
 parentNodePointer = new DOMPointer(testElement.getOwnerDocument(), "/");
 iterator = new DOMAttributeIterator(parentNodePointer, new QName("*,prefix1:nonexistent"));
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithNullName() {
 iterator = new DOMAttributeIterator(parentNodePointer, null);
 }
}