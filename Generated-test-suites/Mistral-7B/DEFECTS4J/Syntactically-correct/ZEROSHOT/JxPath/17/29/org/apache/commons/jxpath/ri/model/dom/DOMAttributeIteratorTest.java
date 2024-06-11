import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

 @Test
 public void testDOMAttributeIterator_elementWithOneAttribute_singleMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 QName attributeName = new QName("test:attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, attributeName);
 NodePointer attributePointer = iterator.getNodePointer();
 assertTrue(attributePointer instanceof DOMAttributePointer);
 assertNotNull(attributePointer.getNode());
 assertEquals(attributeName.getName(), attributePointer.getNode().getNodeName());
 }

 @Test
 public void testDOMAttributeIterator_elementWithOneAttribute_noMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 QName attributeName = new QName("test:notAttribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, attributeName);
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testDOMAttributeIterator_elementWithMultipleAttributes_singleMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 Element testElement = parentPointer.getNode().getOwnerDocument().createElement("test");
 Attr attribute1 = testElement.createAttribute("attribute1");
 attribute1.setValue("value1");
 testElement.setAttributeNode(attribute1);
 Attr attribute2 = testElement.createAttribute("attribute2");
 attribute2.setValue("value2");
 testElement.setAttributeNode(attribute2);
 QName attributeName = new QName("test:attribute1");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), attributeName);
 NodePointer attributePointer = iterator.getNodePointer();
 assertTrue(attributePointer instanceof DOMAttributePointer);
 assertNotNull(attributePointer.getNode());
 assertEquals(attributeName.getName(), attributePointer.getNode().getNodeName());
 }

 @Test
 public void testDOMAttributeIterator_elementWithMultipleAttributes_multipleMatches() {
 NodePointer parentPointer = new DOMPointer("//test");
 Element testElement = parentPointer.getNode().getOwnerDocument().createElement("test");
 Attr attribute1 = testElement.createAttribute("attribute1");
 attribute1.setValue("value1");
 testElement.setAttributeNode(attribute1);
 Attr attribute2 = testElement.createAttribute("attribute2");
 attribute2.setValue("value2");
 testElement.setAttributeNode(attribute2);
 QName attributeName = new QName("test:*");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), attributeName);
 NodePointer attributePointer1 = iterator.getNodePointer();
 NodePointer attributePointer2 = iterator.getNodePointer();
 assertTrue(attributePointer1 != null);
 assertTrue(attributePointer2 != null);
 assertEquals(attributePointer1.getNode().getNodeName(), "attribute1");
 assertEquals(attributePointer2.getNode().getNodeName(), "attribute2");
 }

 @Test
 public void testDOMAttributeIterator_elementWithNoAttributes_noMatches() {
 NodePointer parentPointer = new DOMPointer("//test");
 QName attributeName = new QName("test:attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, attributeName);
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testDOMAttributeIterator_elementWithEmptyNamespace_singleMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 Element testElement = parentPointer.getNode().getOwnerDocument().createElement("test");
 Attr attribute = testElement.createAttribute("attribute");
 attribute.setValue("value");
 testElement.setAttributeNode(attribute);
 QName attributeName = new QName("attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), attributeName);
 NodePointer attributePointer = iterator.getNodePointer();
 assertTrue(attributePointer instanceof DOMAttributePointer);
 assertNotNull(attributePointer.getNode());
 assertEquals(attributeName.getName(), attributePointer.getNode().getNodeName());
 }

 @Test
 public void testDOMAttributeIterator_elementWithEmptyNamespace_noMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 Element testElement = parentPointer.getNode().getOwnerDocument().createElement("test");
 QName attributeName = new QName("not:attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), attributeName);
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testDOMAttributeIterator_elementWithNamespace_singleMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 Element testElement = parentPointer.getNode().getOwnerDocument().createElementNS("test", "test");
 testElement.setPrefix("test");
 Attr attribute = testElement.createAttributeNS("test", "attribute");
 attribute.setValue("value");
 testElement.setAttributeNodeNS(attribute);
 QName attributeName = new QName("test:attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), attributeName);
 NodePointer attributePointer = iterator.getNodePointer();
 assertTrue(attributePointer instanceof DOMAttributePointer);
 assertNotNull(attributePointer.getNode());
 assertEquals(attributeName.getName(), attributePointer.getNode().getNodeName());
 }

 @Test
 public void testDOMAttributeIterator_elementWithNamespace_noMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 Element testElement = parentPointer.getNode().getOwnerDocument().createElementNS("test", "test");
 testElement.setPrefix("test");
 QName attributeName = new QName("not:test:attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), attributeName);
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testDOMAttributeIterator_elementWithPrefixAndDefaultNamespace_singleMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 Element testElement = parentPointer.getNode().getOwnerDocument().createElementNS("test", "test");
 testElement.setPrefix("test");
 Attr attribute = testElement.createAttributeNS("", "attribute");
 attribute.setValue("value");
 testElement.setAttributeNodeNS(attribute);
 QName attributeName = new QName("attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), attributeName);
 NodePointer attributePointer = iterator.getNodePointer();
 assertTrue(attributePointer instanceof DOMAttributePointer);
 assertNotNull(attributePointer.getNode());
 assertEquals(attributeName.getName(), attributePointer.getNode().getNodeName());
 }

 @Test
 public void testDOMAttributeIterator_elementWithPrefixAndDefaultNamespace_noMatch() {
 NodePointer parentPointer = new DOMPointer("//test");
 Element testElement = parentPointer.getNode().getOwnerDocument().createElementNS("test", "test");
 testElement.setPrefix("test");
 QName attributeName = new QName("not:attribute");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(testElement), attributeName);
 assertNull(iterator.getNodePointer());
 }
}