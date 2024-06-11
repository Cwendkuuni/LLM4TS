import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

public class DOMAttributeIteratorTest {

 @Test
 public void testDOMAttributeIterator_SingleMatch() {
 NodePointer parent = new DOMPointer("/root/element[@id='test']");
 QName name = new QName("test:attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

 NodePointer nodePointer = iterator.next();
 Assert.assertNotNull(nodePointer);
 Assert.assertEquals(nodePointer.getNode().getNodeName(), "attribute");
 Assert.assertEquals(nodePointer.getNode().getNodeValue(), "testValue");
 }

 @Test
 public void testDOMAttributeIterator_MultipleMatches() {
 NodePointer parent = new DOMPointer("/root/element");
 QName name = new QName("*");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

 List<NodePointer> nodes = new ArrayList<>();
 while (iterator.hasNext()) {
 nodes.add(iterator.next());
 }

 Assert.assertEquals(nodes.size(), 5); // Assuming that the element has 5 attributes
 }

 @Test
 public void testDOMAttributeIterator_NoMatch() {
 NodePointer parent = new DOMPointer("/root/element");
 QName name = new QName("nonExistent:attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIterator_MixedNamespace() {
 NodePointer parent = new DOMPointer("/root/element");
 QName testName = new QName("test:attribute");
 QName prefixName = new QName("prefix:attribute");

 DOMAttributeIterator testIterator = new DOMAttributeIterator(parent, testName);
 DOMAttributeIterator prefixIterator = new DOMAttributeIterator(parent, prefixName);

 NodePointer testNode = testIterator.next();
 Assert.assertNotNull(testNode);
 Assert.assertEquals(testNode.getNode().getNodeName(), "attribute");

 NodePointer prefixNode = prefixIterator.next();
 Assert.assertNotNull(prefixNode);
 Assert.assertEquals(prefixNode.getNode().getNodeName(), "attribute2");
 }

 @Test
 public void testDOMAttributeIterator_XmlnsAttribute() {
 NodePointer parent = new DOMPointer("/root/element");
 QName name = new QName("xmlns:test");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

 Assert.assertFalse(iterator.hasNext());
 }
}

class DOMPointer {
 // Implementation of DOMPointer class goes here
}