import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

 private NodePointer parentNode;
 private JDOMAttributeIterator iterator;

 @Before
 public void setUp() {
 Element root = new Element("root");
 Namespace xmlns = Namespace.getXmlNamespace();
 Namespace ns = Namespace.getNamespace("test");
 root.addNamespaceDeclaration(xmlns.getURI(), xmlns.getPrefix());
 root.addNamespaceDeclaration(ns.getURI(), ns.getPrefix());
 root.addAttribute("attr1", "val1", ns);
 root.addAttribute("attr2", "val2", xmlns);
 root.addAttribute("attr3", "val3", ns);
 root.addAttribute("attr4", "val4", xmlns);

 parentNode = new NodePointer(root);
 }

 @Test
 public void testEmptyName() {
 iterator = new JDOMAttributeIterator(parentNode, null);
 Assert.assertEquals(0, iterator.getPosition());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testEmptyNamespace() {
 iterator = new JDOMAttributeIterator(parentNode, new QName(""));
 Assert.assertEquals(0, iterator.getPosition());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttribute() {
 iterator = new JDOMAttributeIterator(parentNode, new QName("attr1", "test"));
 NodePointer node = iterator.next();
 Attribute attribute = (Attribute) node.getNode();
 assertEquals("val1", attribute.getValue());
 }

 @Test
 public void testSingleAttributeWithXmlNamespace() {
 iterator = new JDOMAttributeIterator(parentNode, new QName("xml:attr1"));
 NodePointer node = iterator.next();
 Attribute attribute = (Attribute) node.getNode();
 assertEquals("val1", attribute.getValue());
 }

 @Test
 public void testMultipleAttributesWithSameNamespace() {
 iterator = new JDOMAttributeIterator(parentNode, new QName("test:attr", "test"));
 NodePointer node = iterator.next();
 Attribute attribute1 = (Attribute) node.getNode();
 assertEquals("val1", attribute1.getValue());

 node = iterator.next();
 Attribute attribute2 = (Attribute) node.getNode();
 assertEquals("val3", attribute2.getValue());

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultipleAttributesWithDifferentNamespaces() {
 iterator = new JDOMAttributeIterator(parentNode, new QName("test:attr1", "test"));
 NodePointer node = iterator.next();
 Attribute attribute1 = (Attribute) node.getNode();
 assertEquals("val1", attribute1.getValue());

 iterator = new JDOMAttributeIterator(parentNode, new QName("xml:attr2", null));
 NodePointer node2 = iterator.next();
 Attribute attribute2 = (Attribute) node2.getNode();
 assertEquals("val2", attribute2.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParentNode() {
 iterator = new JDOMAttributeIterator(null, new QName("attr1", "test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 iterator = new JDOMAttributeIterator(parentNode, null);
 }
}