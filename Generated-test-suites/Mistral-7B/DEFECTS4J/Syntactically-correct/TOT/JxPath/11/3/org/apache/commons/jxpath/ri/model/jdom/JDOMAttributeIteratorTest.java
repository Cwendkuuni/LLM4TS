import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer rootNode;
 private Element testElement;

 @Before
 public void setUp() {
 testElement = new Element("testElement");
 testElement.addAttribute("attribute1", "value1");
 testElement.addAttribute("attribute2", "value2", "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
 testElement.addAttribute("attribute3", "value3", "xmlns:myNs", "http://mynamespace.com");
 rootNode = new NodePointer(testElement);
 }

 @Test
 public void testEmptyAttributeIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNode, null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorWithSingleAttribute() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNode, new QName("attribute1"));
 NodePointer attributePointer = iterator.next();
 assertEquals("attribute1", attributePointer.getNodeName().getLocalPart());
 assertEquals("value1", attributePointer.getNodeValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorWithMultipleAttributes() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNode, new QName("*"));
 NodePointer attributePointer1 = iterator.next();
 assertEquals("attribute1", attributePointer1.getNodeName().getLocalPart());
 assertEquals("value1", attributePointer1.getNodeValue());

 NodePointer attributePointer2 = iterator.next();
 assertEquals("attribute2", attributePointer2.getNodeName().getLocalPart());
 assertEquals("value2", attributePointer2.getNodeValue());

 NodePointer attributePointer3 = iterator.next();
 assertEquals("attribute3", attributePointer3.getNodeName().getLocalPart());
 assertEquals("value3", attributePointer3.getNodeValue());

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorWithPrefix() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNode, new QName("myNs:attribute3"));
 NodePointer attributePointer = iterator.next();
 assertEquals("attribute3", attributePointer.getNodeName().getLocalPart());
 assertEquals("value3", attributePointer.getNodeValue());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithNullParent() {
 new JDOMAttributeIterator(null, new QName("attribute1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAttributeIteratorWithNullName() {
 new JDOMAttributeIterator(rootNode, null);
 }

 @Test
 public void testAttributeIteratorWithNonExistentAttribute() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNode, new QName("nonExistentAttribute"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testAttributeIteratorWithEmptyElement() {
 Element emptyElement = new Element("emptyElement");
 rootNode = new NodePointer(emptyElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNode, new QName("*"));
 assertFalse(iterator.hasNext());
 }
}