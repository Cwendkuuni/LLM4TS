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
 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertSame;

 public class DOMAttributeIteratorTest {

 private NodePointer root;
 private DOMAttributeIterator iterator;

 @Before
 public void setup() {
 Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 Element rootElement = doc.createElement("root");
 doc.appendChild(rootElement);

 root = new DOMPointNodePointer(doc, rootElement);

 String testName = new QName("test", "http://test.com/test");
 Attr attr1 = doc.createAttribute("attr1");
 attr1.setSpecified(true);
 attr1.setValue("value1");
 rootElement.setAttributeNodeNS(attr1, "http://test.com/test", "test1");
 Attr attr2 = doc.createAttribute("attr2");
 attr2.setSpecified(false);
 attr2.setValue("value2");
 rootElement.setAttributeNode(attr2);
 }

 @Test
 public void testEmptyIterator() {
 iterator = new DOMAttributeIterator(null, null);
 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer().getNode());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 iterator = new DOMAttributeIterator(root, new QName("test1", "*"));
 assertEquals(1, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 assertEquals("attr1", attrPointer.getNodeName());
 assertEquals("value1", attrPointer.getNodeValue());
 assertEquals("http://test.com/test", attrPointer.getNamespaceURI());
 }

 @Test
 public void testIteratorWithAttributeInDefaultNamespace() {
 iterator = new DOMAttributeIterator(root, new QName("test1", "*"));
 iterator.next();
 iterator = new DOMAttributeIterator(root, new QName("attr2", "*"));
 assertEquals(2, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 assertEquals("attr2", attrPointer.getNodeName());
 assertNull(attrPointer.getNodeValue());
 assertSame(null, attrPointer.getNamespaceURI());
 }

 @Test
 public void testIteratorWithAttributeInNamespace() {
 iterator = new DOMAttributeIterator(root, new QName("test", "http://test.com/test", "test1"));
 assertEquals(1, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 assertEquals("attr1", attrPointer.getNodeName());
 assertEquals("value1", attrPointer.getNodeValue());
 assertEquals("http://test.com/test", attrPointer.getNamespaceURI());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithAttributeInWrongNamespace() {
 iterator = new DOMAttributeIterator(root, new QName("test", "http://wrong.com/test", "test1"));
 }

 @Test
 public void testIteratorWithEmptyNamespaceURI() {
 iterator = new DOMAttributeIterator(root, new QName("test1", ""));
 assertEquals(2, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 assertEquals("attr2", attrPointer.getNodeName());
 assertNull(attrPointer.getNodeValue());
 assertNull(attrPointer.getNamespaceURI());
 }

 @Test
 public void testIteratorWithWildcardName() {
 iterator = new DOMAttributeIterator(root, new QName("*", "*"));
 assertEquals(2, iterator.getPosition());
 NodePointer attrPointer = iterator.getNodePointer();
 if (attrPointer.getNodeName().getNodeValue().equals("attr1")) {
 assertEquals("value1", attrPointer.getNodeValue());
 assertEquals("http://test.com/test", attrPointer.getNamespaceURI());
 }
 else {
 assertEquals("attr2", attrPointer.getNodeName().getNodeValue());
 assertNull(attrPointer.getNodeValue());
 assertNull(attrPointer.getNamespaceURI());
 }
 }

 @Test
 public void testIteratorWithWrongName() {
 iterator = new DOMAttributeIterator(root, new QName("wrongName", "*"));
 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer().getNode());
 }

 @Test
 public void testNext() {
 iterator = new DOMAttributeIterator(root, new QName("test1", "*"));
 iterator.next();
 iterator.next();
 NodePointer attrPointer = iterator.getNodePointer();
 assertSame(attrPointer, iterator.getNodePointer());
 assertEquals("attr2", attrPointer.getNodeName().getNodeValue());
 assertNull(attrPointer.getNodeValue());
 assertNull(attrPointer.getNamespaceURI());
 }

 @Test
 public void testSetPosition() {
 iterator = new DOMAttributeIterator(root, new QName("test1", "*"));
 iterator.setPosition(2);
 NodePointer attrPointer = iterator.getNodePointer();
 assertEquals("attr2", attrPointer.getNodeName().getNodeValue());
 assertNull(attrPointer.getNodeValue());
 assertNull(attrPointer.getNamespaceURI());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetPositionNegative() {
 iterator = new DOMAttributeIterator(root, new QName("test1", "*"));
 iterator.setPosition(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionPastEnd() {
 iterator = new DOMAttributeIterator(root, new QName("test1", "*"));
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.setPosition(5);
 }
 }