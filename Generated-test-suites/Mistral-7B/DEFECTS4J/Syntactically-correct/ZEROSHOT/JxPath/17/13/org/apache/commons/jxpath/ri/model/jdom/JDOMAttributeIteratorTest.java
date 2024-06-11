import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("root")), new QName("", "*"));
 assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 Element root = new Element("root");
 root.addAttribute(new Attribute("attr1", "value1", Namespace.NO_NAMESPACE));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("", "attr1"));
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(1, attributes.size());
 assertEquals("attr1", attributes.get(0).getName());
 assertEquals("value1", attributes.get(0).getValue());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element root = new Element("root");
 root.addAttribute(new Attribute("attr1", "value1", Namespace.NO_NAMESPACE));
 root.addAttribute(new Attribute("attr2", "value2", Namespace.NO_NAMESPACE));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("", "*"));
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(2, attributes.size());
 assertFalse(iterator.hasPrevious());

 Iterator<Attribute> iterator2 = attributes.iterator();
 Attribute attr1 = iterator2.next();
 assertEquals("attr1", attr1.getName());
 assertEquals("value1", attr1.getValue());

 Attribute attr2 = iterator2.next();
 assertEquals("attr2", attr2.getName());
 assertEquals("value2", attr2.getValue());

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespace() {
 Element root = new Element("root");
 root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
 root.addAttribute(new Attribute("xml:attr1", "value1", new Namespace("xml", "http://www.w3.org/XML/1998/namespace")));
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(root), new QName("xml", "attr1"));
 List<Attribute> attributes = iterator.getAttributes();
 assertEquals(1, attributes.size());
 assertEquals("xml:attr1", attributes.get(0).getName().getLocalPart());
 assertEquals("value1", attributes.get(0).getValue());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullParent() {
 new JDOMAttributeIterator(null, new QName("", "*"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullName() {
 new JDOMAttributeIterator(new NodePointer(new Element("root")), null);
 }
}