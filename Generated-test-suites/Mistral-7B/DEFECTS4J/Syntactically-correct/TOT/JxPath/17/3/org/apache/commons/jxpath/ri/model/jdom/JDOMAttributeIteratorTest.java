import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JDOMAttributeIteratorTest {

 private NodePointer nodePointer;
 private JDOMAttributeIterator iterator;

 @BeforeEach
 public void setUp() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2", "xmlns:x");
 root.addAttribute("attr3", "value3", "xmlns:y");

 nodePointer = new NodePointer(root);
 iterator = new JDOMAttributeIterator(nodePointer, null);
 }

 @Test
 public void testEmptyIterator() {
 NodePointer nodePointer = new NodePointer(new Element("root"));;
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("value1", attribute.getValue());
 assertEquals("attr1", attribute.getName());
 assertEquals("", attribute.getNamespaceURI());
 assertNull(iterator.next());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Attribute firstAttribute = (Attribute) iterator.next();
 assertEquals("value1", firstAttribute.getValue());
 assertEquals("attr1", firstAttribute.getName());
 assertEquals("", firstAttribute.getNamespaceURI());

 Attribute secondAttribute = (Attribute) iterator.next();
 assertEquals("value2", secondAttribute.getValue());
 assertEquals("attr2", secondAttribute.getName());
 assertEquals("xmlns:x", secondAttribute.getNamespaceURI());

 Attribute thirdAttribute = (Attribute) iterator.next();
 assertEquals("value3", thirdAttribute.getValue());
 assertEquals("attr3", thirdAttribute.getName());
 assertEquals("xmlns:y", thirdAttribute.getNamespaceURI());

 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithEmptyNamespace() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attr1"));
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("value1", attribute.getValue());
 assertEquals("attr1", attribute.getName());
 assertEquals("", attribute.getNamespaceURI());
 }

 @Test
 public void testIteratorWithNamespace() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("x", "attr2"));
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("value2", attribute.getValue());
 assertEquals("attr2", attribute.getName());
 assertEquals("xmlns:x", attribute.getNamespaceURI());
 }

 @Test
 public void testIteratorWithEmptyList() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("z", "*"));
 assertFalse(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testSetPosition() {
 iterator.setPosition(2);
 Attribute attribute = (Attribute) iterator.next();
 assertEquals("value3", attribute.getValue());
 }

 @Test
 public void testSetPositionOutOfBounds() {
 iterator.setPosition(4);
 assertNull(iterator.next());
 }

 @Test
 public void testSetPositionNegative() {
 iterator.setPosition(-1);
 assertNull(iterator.next());
 }
}