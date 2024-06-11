import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", ""));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithOneAttribute() {
 Element element = new Element("root");
 Attribute attribute = new Attribute("attr", "value", "ns");
 element.addAttribute(attribute);
 NodePointer nodePointer = new NodePointer(element);
 QName qname = new QName("ns", "attr");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(1, attributes.size());
 Attribute attributeFromIterator = (Attribute) attributes.get(0);
 assertEquals(attribute, attributeFromIterator);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("attr1", "value1", "ns1");
 Attribute attribute2 = new Attribute("attr2", "value2", "ns1");
 Attribute attribute3 = new Attribute("attr3", "value3", "ns2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 NodePointer nodePointer = new NodePointer(element);
 QName qname = new QName("", "*");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(3, attributes.size());
 for (int i = 0; i < 3; i++) {
 Attribute attribute = (Attribute) attributes.get(i);
 if (i == 0) {
 assertEquals(attribute1, attribute);
 } else if (i == 1) {
 assertEquals(attribute2, attribute);
 } else {
 assertEquals(attribute3, attribute);
 }
 }
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithNextAndPrevious() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("attr1", "value1", "ns1");
 Attribute attribute2 = new Attribute("attr2", "value2", "ns2");
 Attribute attribute3 = new Attribute("attr3", "value3", "ns2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 NodePointer nodePointer = new NodePointer(element);
 QName qname = new QName("", "*");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
 List<Attribute> attributes = iterator.getAttributes();
 iterator.next();
 iterator.next();
 Attribute currentAttribute = iterator.previous();
 Attribute previousAttribute = iterator.previous();
 assertEquals(attribute2, currentAttribute);
 assertEquals(attribute1, previousAttribute);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorWithNegativePosition() {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("attr1", "value1", "ns1");
 Attribute attribute2 = new Attribute("attr2", "value2", "ns2");
 Attribute attribute3 = new Attribute("attr3", "value3", "ns2");
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 NodePointer nodePointer = new NodePointer(element);
 QName qname = new QName("", "*");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qname);
 iterator.setPosition(-1);
 }
}