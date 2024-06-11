import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private static Element createTestElement(String name, String prefix, String... attributeNames) {
 Element element = new Element(name);
 Namespace ns = new Namespace(prefix, "http://test.com");
 element.setNamespace(ns);
 for (String attributeName : attributeNames) {
 Attribute attribute = new Attribute(attributeName, "value");
 attribute.setNamespace(ns);
 element.addAttribute(attribute);
 }
 return element;
 }

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("root")), null);
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 Element parent = createTestElement("parent", null, "attributeName:1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(parent), new QName("", "attributeName:1"));
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), parent.getAttribute("attributeName:1"));
 assertNull(iterator.next());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element parent = createTestElement("parent", null, "attributeName1:1", "attributeName2:2");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(parent), new QName("", "*"));
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), parent.getAttribute("attributeName1:1"));
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(next.getNode(), parent.getAttribute("attributeName2:2"));
 assertNull(iterator.next());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithNamespacedAttribute() {
 Element parent = createTestElement("parent", "xml", "xml:attributeName:1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(parent), new QName("xml", "attributeName:1"));
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), parent.getAttribute("xml:attributeName:1"));
 assertNull(iterator.next());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMixedAttributes() {
 Element parent = createTestElement("parent", null, "attributeName1:1", "attributeName2:2", "xml:attributeName:1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(parent), new QName("", "*"));
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), parent.getAttribute("attributeName1:1"));
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(next.getNode(), parent.getAttribute("attributeName2:2"));
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(next.getNode(), parent.getAttribute("xml:attributeName:1"));
 assertNull(iterator.next());
 assertFalse(iterator.hasPrevious());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithWrongPrefix() {
 Element parent = createTestElement("parent", null, "attributeName:1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(parent), new QName("wrongPrefix", "attributeName:1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithWrongLocalName() {
 Element parent = createTestElement("parent", null, "attributeName:1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(parent), new QName("", "wrongLocalName:1"));
 }
}