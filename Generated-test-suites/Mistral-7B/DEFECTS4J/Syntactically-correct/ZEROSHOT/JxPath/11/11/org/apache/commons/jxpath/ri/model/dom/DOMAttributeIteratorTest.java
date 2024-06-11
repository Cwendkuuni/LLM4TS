package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

 private static final String PREFIX_NS = "xmlns";
 private static final String LOCAL_NAME_XMLNS = "xmlns";
 private static final String PREFIX_TEST = "test";
 private static final String LOCAL_NAME_TEST = "test";
 private static final String PREFIX_TEST_NS = "test-ns";
 private static final String URI_TEST_NS = "test-ns:test";
 private static final String URI_VALID_NAMESPACE = "http://www.w3.org/2000/xmlns/";
 private static final String NAME_VALID_NAMESPACE = "xmlns:test";
 private static final String NAME_TEST = new QName(URI_TEST_NS, LOCAL_NAME_TEST);
 private static final String NAME_VALID_NAMESPACE_PREFIX = "test";
 private static final String NAME_VALID_NAMESPACE_LOCAL = "test";
 private static final String NAME_TEST_PREFIX = "test";
 private static final String NAME_TEST_LOCAL = "test";
 private static final String NAME_OTHER = new QName("other:other");
 private static final String XPATH_TEST = "/test:test";
 private NodePointer rootNodePointer;
 private Element rootElement;

 @Before
 public void setUp() {
 rootElement = createTestDocument();
 rootNodePointer = new DOMPoint(rootElement);
 }

 @Test
 public void testDOMAttributeIterator_Empty() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, null);
 assertFalse("Iterator should be empty", iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_SingleAttribute_SameName() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, NAME_TEST);
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNode(NAME_TEST.getName())));
 assertFalse("Iterator should not have next element", iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_SingleAttribute_DifferentName() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, NAME_OTHER);
 assertFalse("Iterator should be empty", iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_MultipleAttributes_SameName_SameNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, NAME_TEST);
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer1 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer1, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNode(NAME_TEST.getName())));
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer2 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer2, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNode(NAME_TEST.getName())));
 assertFalse("Iterator should not have next element", iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_MultipleAttributes_SameName_DifferentNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, new QName(URI_TEST_NS, LOCAL_NAME_TEST));
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer1 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer1, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNodeNS(URI_TEST_NS, LOCAL_NAME_TEST.getName())));
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer2 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer2, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNodeNS(URI_VALID_NAMESPACE, LOCAL_NAME_VALID_NAMESPACE.getName())));
 assertFalse("Iterator should not have next element", iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_MultipleAttributes_DifferentName_SameNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, NAME_TEST);
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer1 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer1, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNode(NAME_TEST.getName())));
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer2 = iterator.next();
 assertFalse("Node pointers should not be equal", nodePointer1.equals(nodePointer2));
 }

 @Test
 public void testDOMAttributeIterator_MultipleAttributes_DifferentName_DifferentNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, NAME_TEST);
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer1 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer1, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNode(NAME_TEST.getName())));
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer2 = iterator.next();
 assertFalse("Node pointers should not be equal", nodePointer1.equals(nodePointer2));
 }

 @Test
 public void testDOMAttributeIterator_MultipleAttributes_SameName_MixedNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, NAME_TEST);
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer1 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer1, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNodeNS(URI_TEST_NS, LOCAL_NAME_TEST.getName())));
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer2 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer2, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNodeNS(URI_VALID_NAMESPACE, LOCAL_NAME_VALID_NAMESPACE.getName())));
 assertFalse("Iterator should not have next element", iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_MultipleAttributes_DifferentName_MixedNamespace() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, NAME_TEST);
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer1 = iterator.next();
 assertEquals("Node pointers should be equal", nodePointer1, new DOMAttributePointer(rootNodePointer,
 rootElement.getAttributeNodeNS(URI_TEST_NS, LOCAL_NAME_TEST.getName())));
 assertTrue("Iterator should have next element", iterator.hasNext());
 NodePointer nodePointer2 = iterator.next();
 assertFalse("Node pointers should not be equal", nodePointer1.equals(nodePointer2));
 }

 @Test
 public void testDOMAttributeIterator_NoAttributes() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(rootNodePointer, NAME_TEST);
 assertFalse("Iterator should be empty", iterator.hasNext());
 }

 @After
 public void tearDown() {
 // Empty
 }

 private Element createTestDocument() {
 Element root = document.createElement("root");
 root.setAttributeNS(URI_TEST_NS, LOCAL_NAME_TEST, "test1");
 root.setAttributeNS(URI_VALID_NAMESPACE, LOCAL_NAME_VALID_NAMESPACE, "test2");
 root.setAttributeNode(document.createAttribute("test3"));
 root.setAttributeNode(document.createAttributeNS(URI_VALID_NAMESPACE, "test4"));
 document.appendChild(root);
 return root;
 }
}