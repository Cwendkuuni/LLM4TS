import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, null);
 assertTrue(iterator.hasNext());
 assertNull(iterator.next());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element element = new Element("root", "root");
 Attribute attribute = new Attribute("test", "testValue", "testNS");
 element.addAttribute(attribute);
 NodePointer nodePointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("testNS:test"));
 NodePointer next = iterator.next();
 assertTrue(next instanceof JDOMAttributePointer);
 assertEquals(attribute, next.getNode());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultipleAttributesIterator() {
 Element element = new Element("root", "root");
 Attribute attribute1 = new Attribute("test1", "testValue1", "testNS1");
 Attribute attribute2 = new Attribute("test2", "testValue2", "testNS1");
 Attribute attribute3 = new Attribute("test3", "testValue3", null);
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 NodePointer nodePointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("testNS1:test*"));
 NodePointer next = null;
 while (iterator.hasNext()) {
 next = iterator.next();
 assertTrue(next instanceof JDOMAttributePointer);
 }
 assertTrue(next == null);
 }

 @Test
 public void testNamespaceResolution() {
 Element element = new Element("root", "root");
 Attribute attribute = new Attribute("test", "testValue", "xml");
 element.addAttribute(attribute);
 NodePointer nodePointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("xml:test"));
 NodePointer next = iterator.next();
 assertTrue(next instanceof JDOMAttributePointer);
 assertEquals(attribute, next.getNode());
 }

 @Test
 public void testOutOfRangePosition() {
 Element element = new Element("root", "root");
 Attribute attribute1 = new Attribute("test1", "testValue1", "testNS1");
 Attribute attribute2 = new Attribute("test2", "testValue2", "testNS1");
 Attribute attribute3 = new Attribute("test3", "testValue3", null);
 element.addAttribute(attribute1);
 element.addAttribute(attribute2);
 element.addAttribute(attribute3);
 NodePointer nodePointer = new NodePointer(element);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("testNS1:test*"));
 iterator.setPosition(10);
 assertFalse(iterator.hasNext());
 }
}