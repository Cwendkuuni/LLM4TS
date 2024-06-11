import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private QName attributeName;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 Element root = new Element("root");
 root.setAttribute("attr1", "value1");
 root.setAttributeNS("xmlns:x", "x:attr1", "value1");
 root.setAttributeNS("xmlns:y", "y:attr1", "value1");
 root.setAttribute("attr2", "value2");
 root.setAttribute("attr3", "value3");

 parentNode = new NodePointer(root);
 attributeName = new QName("*"); // test for all attributes
 }

 @Test
 public void testEmpty() {
 iterator = new DOMAttributeIterator(new NodePointer(null), attributeName);
 assertTrue(iterator.isEmpty());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testSingleAttribute() {
 iterator = new DOMAttributeIterator(parentNode, attributeName);
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("attr1", nodePointer.getNodeName().getLocalPart());
 assertEquals("value1", nodePointer.getNodeValue().getNodeValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNamespaceAttribute() {
 attributeName = new QName("xmlns:x", "x:attr1");
 iterator = new DOMAttributeIterator(parentNode, attributeName);
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("x:attr1", nodePointer.getNodeName().getLocalPart());
 assertEquals("value1", nodePointer.getNodeValue().getNodeValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNamespacePrefix() {
 attributeName = new QName("x", "attr1");
 iterator = new DOMAttributeIterator(parentNode, attributeName);
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("x:attr1", nodePointer.getNodeName().getQualifiedName());
 assertEquals("value1", nodePointer.getNodeValue().getNodeValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultipleAttributes() {
 iterator = new DOMAttributeIterator(parentNode, attributeName);
 assertTrue(iterator.hasNext());
 NodePointer nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("attr1", nodePointer.getNodeName().getLocalPart());
 assertEquals("value1", nodePointer.getNodeValue().getNodeValue());
 assertTrue(iterator.hasNext());
 nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("attr2", nodePointer.getNodeName().getLocalPart());
 assertEquals("value2", nodePointer.getNodeValue().getNodeValue());
 assertTrue(iterator.hasNext());
 nodePointer = iterator.next();
 assertNotNull(nodePointer);
 assertEquals("attr3", nodePointer.getNodeName().getLocalPart());
 assertEquals("value3", nodePointer.getNodeValue().getNodeValue());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPosition() {
 iterator = new DOMAttributeIterator(parentNode, attributeName);
 assertFalse(iterator.hasNext());
 iterator.setPosition(1);
 }
}