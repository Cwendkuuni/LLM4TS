import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", "*"));
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() throws JDOMException {
 Element element = new Element("root");
 Attribute attribute = new Attribute("name1", "value1", new Namespace("ns1", "http://test.com"));
 attribute.setText("value1");
 element.addContent(attribute);

 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("ns1:name1"));
 assertEquals(element, iterator.getNodePointer().getNode().getParent());
 assertEquals("name1", iterator.getNodePointer().getNodeName().getLocalPart());
 assertEquals("value1", iterator.getNodePointer().getNode().getText());
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());

 iterator.next();
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultipleAttributesIterator() throws JDOMException {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("name1", "value1", new Namespace("ns1", "http://test.com"));
 attribute1.setText("value1");
 Attribute attribute2 = new Attribute("name2", "value2", new Namespace("ns2", "http://test.com"));
 attribute2.setText("value2");
 element.addContent(attribute1);
 element.addContent(attribute2);

 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("ns1:name*"));
 assertEquals(element, iterator.getNodePointer().getNode().getParent());
 assertEquals("name1", iterator.getNodePointer().getNodeName().getLocalPart());
 assertEquals("value1", iterator.getNodePointer().getNode().getText());
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());

 iterator.next();
 assertEquals("name2", iterator.getNodePointer().getNodeName().getLocalPart());
 assertEquals("value2", iterator.getNodePointer().getNode().getText());
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());

 iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithWildcardNamespace() throws JDOMException {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("name1", "value1", new Namespace("ns1", "http://test.com"));
 attribute1.setText("value1");
 Attribute attribute2 = new Attribute("name2", "value2", new Namespace("ns2", "http://test.com"));
 attribute2.setText("value2");
 Attribute attribute3 = new Attribute("name3", "value3", new Namespace("", ""));
 attribute3.setText("value3");
 element.addContent(attribute1);
 element.addContent(attribute2);
 element.addContent(attribute3);

 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("*", "*"));
 assertEquals(element, iterator.getNodePointer().getNode().getParent());
 assertEquals("name1", iterator.getNodePointer().getNodeName().getLocalPart());
 assertEquals("value1", iterator.getNodePointer().getNode().getText());
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());

 iterator.next();
 assertEquals("name2", iterator.getNodePointer().getNodeName().getLocalPart());
 assertEquals("value2", iterator.getNodePointer().getNode().getText());
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());

 iterator.next();
 assertEquals("name3", iterator.getNodePointer().getNodeName().getLocalPart());
 assertEquals("value3", iterator.getNodePointer().getNode().getText());
 assertEquals(3, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNoNamespace() throws JDOMException {
 Element element = new Element("root");
 Attribute attribute1 = new Attribute("name1", "value1");
 attribute1.setText("value1");
 Attribute attribute2 = new Attribute("name2", "value2");
 attribute2.setText("value2");
 element.addContent(attribute1);
 element.addContent(attribute2);

 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "name*"));
 assertEquals(element, iterator.getNodePointer().getNode().getParent());
 assertEquals("name1", iterator.getNodePointer().getNodeName().getLocalPart());
 assertEquals("value1", iterator.getNodePointer().getNode().getText());
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());

 iterator.next();
 assertEquals("name2", iterator.getNodePointer().getNodeName().getLocalPart());
 assertEquals("value2", iterator.getNodePointer().getNode().getText());
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());

 iterator.next();
 assertFalse(iterator.hasNext());
 }
}