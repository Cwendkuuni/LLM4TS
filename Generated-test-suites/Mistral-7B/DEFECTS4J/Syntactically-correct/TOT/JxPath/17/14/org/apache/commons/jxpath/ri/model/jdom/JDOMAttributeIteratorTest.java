import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JDOMAttributeIteratorTest class contains test cases for JDOMAttributeIterator.
 */
public class JDOMAttributeIteratorTest {

 private NodePointer rootNodePointer;
 private Element rootElement;

 @Before
 public void setUp() throws JDOMException {
 rootElement = new Element("root");
 rootElement.addAttribute("attr1", "value1");
 rootElement.addAttribute("attr2", "value2", "xmlns:x", "http://xmlns.org/xpaths");
 rootElement.addAttribute("attr3", "value3");
 }

 @Test
 public void testEmptyIterator() {
 JDOMAttributeIterator emptyIterator = new JDOMAttributeIterator(null, null);
 Assert.assertFalse(emptyIterator.hasNext());
 }

 @Test
 public void testSingleAttributeIterator() {
 rootNodePointer = new NodePointer(rootElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, new QName("attr1"));
 JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.next();
 Assert.assertEquals("attr1", attributePointer.getNodeName().getName());
 Assert.assertEquals("value1", attributePointer.getNodeName().getValue());
 Assert.assertEquals("xmlns:root", attributePointer.getNodeName().getNamespace().getURI());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testMultipleAttributeIterator() {
 rootNodePointer = new NodePointer(rootElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, new QName("*"));
 JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) iterator.next();
 Assert.assertEquals("attr1", attributePointer1.getNodeName().getName());
 Assert.assertEquals("value1", attributePointer1.getNodeName().getValue());
 Assert.assertEquals("xmlns:root", attributePointer1.getNodeName().getNamespace().getURI());

 JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) iterator.next();
 Assert.assertEquals("attr2", attributePointer2.getNodeName().getName());
 Assert.assertEquals("value2", attributePointer2.getNodeName().getValue());
 Assert.assertEquals("xmlns:x", attributePointer2.getNodeName().getNamespace().getURI());

 JDOMAttributePointer attributePointer3 = (JDOMAttributePointer) iterator.next();
 Assert.assertEquals("attr3", attributePointer3.getNodeName().getName());
 Assert.assertEquals("value3", attributePointer3.getNodeName().getValue());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullQName() {
 rootNodePointer = new NodePointer(rootElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullParentPointer() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("attr1"));
 }
}