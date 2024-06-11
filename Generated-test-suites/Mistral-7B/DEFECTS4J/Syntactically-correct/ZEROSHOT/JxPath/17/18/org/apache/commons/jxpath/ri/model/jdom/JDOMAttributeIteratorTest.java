import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private JDOMAttributeIterator attributeIterator;

 @Before
 public void setup() {
 JDOMContext context = new JDOMContext();
 Element rootElement = new Element("root");
 rootElement.addAttribute("xmlns:test", "http://test.com");
 rootElement.addAttribute("attr1", "value1");
 rootElement.addAttribute("attr2", "value2", "test:namespace");
 rootElement.addAttribute("attr3", "value3", "test:namespace");
 rootElement.addAttribute("attr4", "value4");
 Element element = new Element("element");
 rootElement.addContent(element);
 element.addAttribute("attr5", "value5");
 element.addAttribute("attr6", "value6", "test:namespace");
 element.addAttribute("attr7", "value7");
 parentNode = new NodePointer(context, rootElement);
 }

 @Test
 public void testEmptyIterator() {
 QName name = new QName("", "*");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);
 assertEquals(0, attributeIterator.getPosition());
 assertFalse(attributeIterator.hasNext());
 assertFalse(attributeIterator.hasPrevious());
 }

 @Test
 public void testIteratorWithWildcard() {
 QName name = new QName("", "*");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);
 assertEquals(6, attributeIterator.getPosition());
 assertTrue(attributeIterator.hasNext());
 assertTrue(attributeIterator.hasPrevious());
 }

 @Test
 public void testIteratorWithSpecificName() {
 QName name = new QName("test", "attr2");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);
 assertEquals(2, attributeIterator.getPosition());
 assertTrue(attributeIterator.hasNext());
 assertTrue(attributeIterator.hasPrevious());
 }

 @Test
 public void testGetAttributePointer() {
 QName name = new QName("test", "attr2");
 NodePointer attributePointer = attributeIterator.getNodePointer();
 assertNotNull(attributePointer);
 assertNotNull(attributePointer.getNode());
 assertInstanceOf(JDOMAttributePointer.class, attributePointer);
 Attribute attribute = attributePointer.getNode().getAttribute("name");
 assertEquals("attr2", attribute.getName());
 assertEquals("value2", attribute.getValue());
 }

 @Test
 public void testGetAttributePointerForNonExistingAttribute() {
 QName name = new QName("test", "notExistingAttribute");
 NodePointer attributePointer = attributeIterator.getNodePointer();
 assertNull(attributePointer);
 }

 @Test
 public void testSetPositionToFirstAttribute() {
 QName name = new QName("", "*");
 attributeIterator.setPosition(1);
 NodePointer attributePointer = attributeIterator.getNodePointer();
 assertNotNull(attributePointer);
 assertNotNull(attributePointer.getNode());
 assertInstanceOf(JDOMAttributePointer.class, attributePointer);
 assertEquals("attr1", attributePointer.getNode().getName());
 assertEquals("value1", attributePointer.getNode().getValue());
 }

 @Test
 public void testSetPositionToLastAttribute() {
 QName name = new QName("", "*");
 attributeIterator.setPosition(6);
 NodePointer attributePointer = attributeIterator.getNodePointer();
 assertNotNull(attributePointer);
 assertInstanceOf(JDOMAttributePointer.class, attributePointer);
 assertEquals("attr7", attributePointer.getNode().getName());
 assertEquals("value7", attributePointer.getNode().getValue());
 }

 @Test
 public void testSetPositionToNonExistingPosition() {
 QName name = new QName("", "*");
 attributeIterator.setPosition(7);
 assertFalse(attributeIterator.hasNext());
 assertFalse(attributeIterator.hasPrevious());
 assertNull(attributeIterator.getNodePointer());
 }

 @Test
 public void testSetPositionToInvalidPosition() {
 QName name = new QName("", "");
 attributeIterator.setPosition(-1);
 assertFalse(attributeIterator.hasNext());
 assertFalse(attributeIterator.hasPrevious());
 assertNull(attributeIterator.getNodePointer());
 }

 @Test
 public void testHasNext() {
 QName name = new QName("test", "attr2");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);
 attributeIterator.next();
 assertEquals(3, attributeIterator.getPosition());
 assertTrue(attributeIterator.hasNext());
 }

 @Test
 public void testHasPrevious() {
 QName name = new QName("test", "attr2");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);
 attributeIterator.next();
 attributeIterator.next();
 assertEquals(5, attributeIterator.getPosition());
 assertTrue(attributeIterator.hasPrevious());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionToInvalidPositionWithNext() {
 QName name = new QName("test", "attr2");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);
 attributeIterator.next();
 attributeIterator.next();
 attributeIterator.setPosition(5);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionToInvalidPositionWithPrevious() {
 QName name = new QName("test", "attr2");
 attributeIterator = new JDOMAttributeIterator(parentNode, name);
 attributeIterator.previous();
 attributeIterator.previous();
 attributeIterator.setPosition(4);
 }
}