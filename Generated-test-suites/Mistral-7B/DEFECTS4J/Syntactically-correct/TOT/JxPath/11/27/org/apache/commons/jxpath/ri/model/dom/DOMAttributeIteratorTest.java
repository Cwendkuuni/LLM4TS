package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
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

 private NodeIterator iterator;
 private Document doc;

 @Before
 public void setUp() {
 doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 Element root = doc.createElement("root");
 root.setAttribute("attr1", "val1");
 root.setAttribute("attr2", "val2");
 root.setAttributeNS("xmlns:x", "x:attr1", "val3");
 doc.appendChild(root);
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 NodePointer parent = new DOMPointer(doc, root);
 QName name = new QName("*", "attr1");
 iterator = new DOMAttributeIterator(parent, name);
 NodePointer attributePointer = iterator.next();
 assertEquals(root.getAttributeNode("attr1"), attributePointer.getNode());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 NodePointer parent = new DOMPointer(doc, root);
 QName name = new QName("*", "attr2");
 iterator = new DOMAttributeIterator(parent, name);
 NodePointer attributePointer = iterator.next();
 assertEquals(root.getAttributeNode("attr2"), attributePointer.getNode());
 iterator.next(); // move to next attribute
 NodePointer emptyPointer = iterator.next();
 assertNull(emptyPointer.getNode());
 }

 @Test
 public void testIteratorWithNamespaceAttribute() {
 NodePointer parent = new DOMPointer(doc, root);
 QName name = new QName("x", "attr1");
 iterator = new DOMAttributeIterator(parent, name);
 NodePointer attributePointer = iterator.next();
 assertEquals(root.getAttributeNodeNS("xmlns:x", "x:attr1"), attributePointer.getNode());
 }

 @Test
 public void testIteratorWithEmptyIterator() {
 NodePointer parent = new DOMPointer(doc, root);
 QName name = new QName("*", "nonexistentattr");
 iterator = new DOMAttributeIterator(parent, name);
 NodePointer attributePointer = iterator.next();
 assertNull(attributePointer.getNode());
 }

 @Test
 public void testIteratorWithMixedAttributeNames() {
 NodePointer parent = new DOMPointer(doc, root);
 QName name1 = new QName("*", "attr1");
 QName name2 = new QName("xmlns", "x:attr1");
 iterator = new DOMAttributeIterator(parent, name1);
 NodePointer attr1Pointer = iterator.next();
 assertEquals(root.getAttributeNode("attr1"), attr1Pointer.getNode());
 iterator = new DOMAttributeIterator(parent, name2);
 NodePointer attr2Pointer = iterator.next();
 assertEquals(root.getAttributeNodeNS("xmlns:x", "x:attr1"), attr2Pointer.getNode());
 }
}