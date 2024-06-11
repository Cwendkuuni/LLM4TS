import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private DOMAttributeIterator iterator;

 @Test
 public void testDOMAttributeIterator_emptyNode() {
 nodePointer = new NodePointer(null);
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "*"));
 assertTrue(iterator.isEmpty());
 }

 @Test
 public void testDOMAttributeIterator_singleAttribute() {
 final String xml = "<root xmlns:x='http://example.com/ns' xmlns='http://example.com/ns' id='test'/>";
 nodePointer = new NodePointer(DOMUtil.parse(xml));
 Element root = (Element) nodePointer.getNode();
 iterator = new DOMAttributeIterator(nodePointer, new QName("x:id"));
 NodePointer attributePointer = iterator.next();
 assertEquals("test", attributePointer.getNodeValue());
 assertNull(iterator.next());
 }

 @Test
 public void testDOMAttributeIterator_multipleAttributes() {
 final String xml = "<root xmlns:x='http://example.com/ns' xmlns='http://example.com/ns' id='test1' class='test2'/>";
 nodePointer = new NodePointer(DOMUtil.parse(xml));
 Element root = (Element) nodePointer.getNode();
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "class"));
 NodePointer attributePointer = iterator.next();
 assertEquals("test2", attributePointer.getNodeValue());
 assertNull(iterator.next());

 iterator = new DOMAttributeIterator(nodePointer, new QName("x:id"));
 attributePointer = iterator.next();
 assertEquals("test1", attributePointer.getNodeValue());
 assertNull(iterator.next());
 }

 @Test
 public void testDOMAttributeIterator_multipleAttributesWithPrefixedName() {
 final String xml = "<root xmlns:x='http://example.com/ns' xmlns='http://example.com/ns' id='test1' xml:id='test2'/>";
 nodePointer = new NodePointer(DOMUtil.parse(xml));
 Element root = (Element) nodePointer.getNode();
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "xml:id"));
 NodePointer attributePointer = iterator.next();
 assertEquals("test2", attributePointer.getNodeValue());
 assertNull(iterator.next());

 iterator = new DOMAttributeIterator(nodePointer, new QName("x:id"));
 attributePointer = iterator.next();
 assertEquals("test1", attributePointer.getNodeValue());
 assertNull(iterator.next());
 }

 @Test
 public void testDOMAttributeIterator_namespaceNode() {
 final String xml = "<root xmlns:x='http://example.com/ns' xmlns='http://example.com/ns' xmlns:xml='http://www.w3.org/XML/1998/namespace'><x:a xmlns:y='http://example.com/other'/></root>";
 nodePointer = new NodePointer(DOMUtil.parse(xml));
 Element root = (Element) nodePointer.getNode();
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "xml:namespace"));
 NodePointer namespacePointer = iterator.next();
 assertEquals("http://www.w3.org/XML/1998/namespace", namespacePointer.getNodeValue());
 assertNull(iterator.next());
 }

 @Test (expected = IllegalStateException.class)
 public void testDOMAttributeIterator_outOfRange() {
 nodePointer = new NodePointer(DOMUtil.parse("<root/>"));
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "*"));
 iterator.next();
 iterator.next(); // move to the next attribute
 iterator.next(); // move to the next attribute
 iterator.next(); // move to the next attribute (out of range)
 }

 @Test
 public void testDOMAttributeIterator_emptyElement() {
 nodePointer = new NodePointer(DOMUtil.parse("<root></root>"));
 iterator = new DOMAttributeIterator(nodePointer, new QName("", "*"));
 assertTrue(iterator.isEmpty());
 }

 private static NodeUtil DOMUtil = new NodeUtil();
}