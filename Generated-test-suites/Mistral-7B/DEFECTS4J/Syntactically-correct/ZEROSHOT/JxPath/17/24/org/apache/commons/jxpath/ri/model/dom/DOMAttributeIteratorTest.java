import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

 private static final QName NAME_TEST = new QName("", "test");
 private static final QName NAME_XMLNS = new QName("", "xmlns");

 @org.junit.Before
 public void setUp() {
 }

 @org.junit.After
 public void tearDown() {
 }

 @org.junit.Test
 public void testDOMAttributeIteratorBasic() {
 Node pointerNode = createTestNode();
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(pointerNode), NAME_TEST);

 assertNotNull(iterator.getNodePointer());
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());

 NodePointer next = iterator.next();
 assertNotNull(next.getNode());
 assertSame(pointerNode, next.getNode().getParentNode());
 assertEquals(NAME_TEST, next.getQName());

 assertEquals(1, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithMultipleAttributes() {
 Node pointerNode = createTestNode();
 pointerNode.setAttributeNode(createTestAttr("attr1", "value1"));
 pointerNode.setAttributeNode(createTestAttr("attr2", "value2"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(pointerNode), NAME_TEST);

 assertNotNull(iterator.getNodePointer());
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());

 NodePointer next1 = iterator.next();
 assertNotNull(next1.getNode());
 assertSame(pointerNode, next1.getNode().getParentNode());
 assertEquals(new QName("", "attr1"), next1.getQName());

 NodePointer next2 = iterator.next();
 assertNotNull(next2.getNode());
 assertSame(pointerNode, next2.getNode().getParentNode());
 assertEquals(new QName("", "attr2"), next2.getQName());

 assertEquals(2, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithEmptyNode() {
 Node pointerNode = createTestNode();
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(pointerNode), NAME_TEST);

 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithEmptyList() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null), NAME_TEST);

 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithNSAttr() {
 Element element = createTestElement();
 element.setAttributeNodeNS(createTestAttrNS("xmlns:test", "value"), "http://test.com");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), NAME_XMLNS);

 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithNSAttr1() {
 Element element = createTestElement();
 element.setAttributeNodeNS(createTestAttrNS("xmlns:test", "value"), "http://test.com");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test.com", "test"));

 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithNSAttr2() {
 Element element = createTestElement();
 element.setAttributeNodeNS(createTestAttrNS("xmlns:test", "value"), "http://test.com");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("", "test"));

 assertFalse(iterator.hasNext());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithNSAttr3() {
 Element element = createTestElement();
 element.setAttributeNodeNS(createTestAttrNS("xmlns:test", "value"), "http://test.com");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test.com", "test"));

 assertNotNull(iterator.next().getNode());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithMixedAttributes() {
 Element element = createTestElement();
 element.setAttributeNode(createTestAttr("attr1", "value1"));
 element.setAttributeNodeNS(createTestAttrNS("xmlns:test", "value"), "http://test.com");
 element.setAttributeNode(createTestAttr("attr2", "value2"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), NAME_TEST);

 assertNotNull(iterator.next().getNode());
 assertNotNull(iterator.next().getNode());
 assertNull(iterator.next().getNode());
 }

 @org.junit.Test
 public void testDOMAttributeIteratorWithMixedAttributes2() {
 Element element = createTestElement();
 element.setAttributeNode(createTestAttr("attr1", "value1"));
 element.setAttributeNodeNS(createTestAttrNS("xmlns:test", "value"), "http://test.com");
 element.setAttributeNode(createTestAttr("attr3", "value3"));
 DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), NAME_TEST);

 assertNotNull(iterator.next().getNode());
 assertNotNull(iterator.next().getNode());
 assertNull(iterator.next().getNode());
 assertNull(iterator.next().getNode());
 }

 private Node createTestNode() {
 Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");
 return element;
 }

 private Attr createTestAttr(String name, String value) {
 return new Attr("test", name, null, value);
 }

 private Attr createTestAttrNS(String prefix, String localName) {
 Attr attr = new Attr("test", localName, null, "");
 attr.setNamespaceURI("http://test.com");
 attr.setPrefix(prefix);
 return attr;
 }

 private Element createTestElement() {
 Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");
 return element;
 }
}