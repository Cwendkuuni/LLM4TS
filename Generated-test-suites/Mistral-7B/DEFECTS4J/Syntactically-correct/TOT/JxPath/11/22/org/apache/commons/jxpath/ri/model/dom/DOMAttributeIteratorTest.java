import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Element;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 @Test
 public void testDOMAttributeIterator_SingleAttribute() {
 Element element = new Element("root");
 Attr attr = new Attr("xmlns:x", "http://example.com/x");
 element.setNodeValue("Root Element");
 element.setAttributeNode(attr);

 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("xmlns:x"));
 NodePointer nodePointer = iterator.getNodePointer();
 assertTrue(nodePointer instanceof DOMAttributePointer);
 assertEquals(attr, nodePointer.getNode());
 }

 @Test
 public void testDOMAttributeIterator_MultipleAttributes() {
 Element element = new Element("root");
 element.setNodeValue("Root Element");
 Attr attr1 = new Attr("xmlns:x1", "http://example.com/x1");
 Attr attr2 = new Attr("xmlns:x2", "http://example.com/x2");
 element.setAttributeNode(attr1);
 element.setAttributeNode(attr2);

 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("xmlns:*"));
 NodePointer nodePointer1 = iterator.getNodePointer();
 assertTrue(nodePointer1 instanceof DOMAttributePointer);
 assertEquals(attr1, nodePointer1.getNode());

 NodePointer nodePointer2 = iterator.getNodePointer();
 assertTrue(nodePointer2 instanceof DOMAttributePointer);
 assertEquals(attr2, nodePointer2.getNode());

 assertFalse(iterator.getNodePointer().isPresent());
 }

 @Test(expected = IllegalStateException.class)
 public void testDOMAttributeIterator_ParentNodeNotElement() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(new TextNode("Some text")), new QName("xmlns:x"));
 }

 @Test
 public void testDOMAttributeIterator_EmptyNode() {
 Element element = new Element("root");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("xmlns:x"));
 assertFalse(iterator.getNodePointer().isPresent());
 }

 @Test
 public void testDOMAttributeIterator_NamespaceNotDeclared() {
 Element element = new Element("root");
 element.setNodeValue("Root Element");
 Attr attr = new Attr("x:x", "Value");
 element.setAttributeNode(attr);

 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("y:x"));
 assertFalse(iterator.getNodePointer().isPresent());
 }

 @Test
 public void testDOMAttributeIterator_NamespaceMismatch() {
 Element element = new Element("root");
 element.setNodeValue("Root Element");
 Attr attr = new Attr("xmlns:x", "http://example.com/x");
 element.setAttributeNode(attr);

 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("xmlns:y", "http://example.com/y"));
 assertFalse(iterator.getNodePointer().isPresent());
 }

 @Test
 public void testDOMAttributeIterator_NullName() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(new Element("root")), null);
 assertFalse(iterator.getNodePointer().isPresent());
 }
}