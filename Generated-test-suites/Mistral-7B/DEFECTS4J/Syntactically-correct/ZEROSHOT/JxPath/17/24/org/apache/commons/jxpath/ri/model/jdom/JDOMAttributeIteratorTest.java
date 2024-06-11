import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), null);
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSimpleAttributeIterator() throws JDOMException {
 Element element = new Element("root", "prefix");
 Attribute attribute = new Attribute("name", "value", "namespace-uri");
 attribute.setNamespace(new Namespace("prefix", "namespace-uri"));
 element.addContent(attribute);

 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("prefix:name"));
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), attribute);
 assertEquals("value", next.getValue());
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testWildcardAttributeIterator() throws JDOMException {
 Element element = new Element("root", "prefix");
 Attribute attribute1 = new Attribute("name1", "value1", "namespace-uri1");
 attribute1.setNamespace(new Namespace("prefix", "namespace-uri1"));
 Attribute attribute2 = new Attribute("name2", "value2", "namespace-uri2");
 attribute2.setNamespace(new Namespace("prefix", "namespace-uri2"));
 element.addContent(attribute1);
 element.addContent(attribute2);

 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("prefix:*"));
 assertEquals(2, iterator.getPosition());
 assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 assertEquals(next.getNode(), attribute1);
 assertEquals("value1", next.getValue());
 assertEquals(1, iterator.getPosition());
 assertTrue(iterator.hasNext());
 next = iterator.next();
 assertEquals(next.getNode(), attribute2);
 assertEquals("value2", next.getValue());
 assertEquals(0, iterator.getPosition());
 assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParent() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("prefix:name"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("root", "prefix")), null);
 }
}