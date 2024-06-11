import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private static final String TEST_XML = "<root xmlns:x='http://test.com'><child attr1='value1' attr2='value2' attr3='value3'/></root>";
 private static final QName TEST_NAME = new QName("", "*");

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, TEST_NAME);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element root = new Element("root");
 root.addAttribute("namespace", "http://test.com");
 Attribute attr = new Attribute("attr1", "value1");
 root.addAttribute(attr);
 parentNode = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, TEST_NAME);

 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertSame(attr, iterator.next());
 assertNull(iterator.previous());
 }

 @Test
 public void testMultipleAttributesIterator() {
 Element root = new Element("root");
 root.addAttribute("namespace", "http://test.com");
 Attribute attr1 = new Attribute("attr1", "value1");
 Attribute attr2 = new Attribute("attr2", "value2");
 Attribute attr3 = new Attribute("attr3", "value3");
 root.setAdditionalAttribute(attr1);
 root.setAdditionalAttribute(attr2);
 root.setAdditionalAttribute(attr3);
 parentNode = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, TEST_NAME);

 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertSame(attr1, iterator.next());
 assertSame(attr2, iterator.next());
 assertSame(attr3, iterator.next());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 }

 @Test
 public void testNamespaceIterator() {
 Element root = new Element("root");
 root.addNamespaceDeclaration("x", "http://test.com");
 Attribute attr1 = new Attribute("attr1", "value1", "x");
 Attribute attr2 = new Attribute("attr2", "value2", "x");
 root.setAdditionalAttribute(attr1);
 root.setAdditionalAttribute(attr2);
 parentNode = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("x", "*"));

 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertSame(attr1, iterator.next());
 assertSame(attr2, iterator.next());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 }

 @Test
 public void testMultiNamespaceIterator() {
 Element root = new Element("root");
 root.addNamespaceDeclaration("x", "http://test.com");
 root.addNamespaceDeclaration("y", "http://test2.com");
 Attribute attr1 = new Attribute("attr1", "value1", "x");
 Attribute attr2 = new Attribute("attr2", "value2", "y");
 Attribute attr3 = new Attribute("attr3", "value3", "x");
 root.setAdditionalAttribute(attr1);
 root.setAdditionalAttribute(attr2);
 root.setAdditionalAttribute(attr3);
 parentNode = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));

 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertSame(attr1, iterator.next());
 assertSame(attr2, iterator.next());
 assertSame(attr3, iterator.next());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 }

 @Test
 public void testEmptyParentIterator() {
 parentNode = new NodePointer(null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, TEST_NAME);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 }

 @Test
 public void testNullParentIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, TEST_NAME);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.next());
 assertNull(iterator.previous());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNameIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, null);
 }
}