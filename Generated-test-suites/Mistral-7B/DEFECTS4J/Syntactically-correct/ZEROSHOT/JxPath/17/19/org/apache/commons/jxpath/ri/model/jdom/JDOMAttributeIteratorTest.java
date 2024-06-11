import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName("", "*"));
 Assert.assertEquals(0, iterator.getPosition());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() {
 Element root = new Element("root");
 root.setAttribute("single-attribute", "value");
 NodePointer rootPointer = new NodePointer(root);
 QName attributeName = new QName("", "single-attribute");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootPointer, attributeName);

 Assert.assertEquals(1, iterator.getPosition());
 Assert.assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 Assert.assertNotNull(next);
 Assert.assertEquals(QName.createQName("", "single-attribute"), next.getNodeName());
 Assert.assertEquals("value", next.getNodeValue());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultipleAttributesIterator() {
 Element root = new Element("root");
 root.addAttribute("attr1", "value1");
 root.addAttribute("attr2", "value2");
 root.addAttribute("attr3", "value3");
 NodePointer rootPointer = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootPointer, new QName("", "*"));

 Assert.assertEquals(1, iterator.getPosition());
 Assert.assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 Assert.assertNotNull(next);
 Assert.assertEquals(QName.createQName("", "attr1"), next.getNodeName());
 Assert.assertEquals("value1", next.getNodeValue());
 Assert.assertTrue(iterator.hasNext());
 next = iterator.next();
 Assert.assertNotNull(next);
 Assert.assertEquals(QName.createQName("", "attr2"), next.getNodeName());
 Assert.assertEquals("value2", next.getNodeValue());
 Assert.assertTrue(iterator.hasNext());
 next = iterator.next();
 Assert.assertNotNull(next);
 Assert.assertEquals(QName.createQName("", "attr3"), next.getNodeName());
 Assert.assertEquals("value3", next.getNodeValue());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespace() {
 Element root = new Element("root");
 root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
 root.addAttribute("xml:attr1", "value1");
 NodePointer rootPointer = new NodePointer(root);
 QName attributeName = new QName("xml", "attr1");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootPointer, attributeName);

 Assert.assertEquals(1, iterator.getPosition());
 Assert.assertTrue(iterator.hasNext());
 NodePointer next = iterator.next();
 Assert.assertNotNull(next);
 Assert.assertEquals(attributeName, next.getNodeName());
 Assert.assertEquals("value1", next.getNodeValue());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testIteratorWithNonExistingAttribute() {
 Element root = new Element("root");
 NodePointer rootPointer = new NodePointer(root);
 QName attributeName = new QName("", "non-existing-attribute");
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootPointer, attributeName);
 iterator.next();
 }

}