import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

 private static final String XML_NAMESPACE = "xml";
 private static final String PREFIX = "test";
 private static final String LOCAL_NAME = "testAttribute";
 private static final QName TEST_QNAME = new QName(PREFIX, LOCAL_NAME);

 @Test
 public void testIterationWithSingleAttribute() {
 Element rootElement = new Element("root");
 rootElement.addAttribute(new Attribute(XML_NAMESPACE, "xmlns:test", "http://test.com/test"));
 rootElement.addAttribute(new Attribute(new QName(XML_NAMESPACE, LOCAL_NAME), "value", "testValue"));

 NodePointer rootNodePointer = new NodePointer(new JDOMNodeWrapper(rootElement), null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, TEST_QNAME);

 NodePointer attributeNodePointer = iterator.getNodePointer();
 Assert.assertEquals(TEST_QNAME, attributeNodePointer.getQName());
 Assert.assertEquals("testValue", attributeNodePointer.getValue().getValue());

 // Check that we can iterate to the next attribute
 iterator.setPosition(1);
 attributeNodePointer = iterator.getNodePointer();
 Assert.assertEquals(TEST_QNAME, attributeNodePointer.getQName());
 Assert.assertNull(attributeNodePointer.getValue());
 }

 @Test
 public void testIterationWithMultipleAttributes() {
 Element rootElement = new Element("root");
 rootElement.addAttribute(new Attribute(XML_NAMESPACE, "xmlns:test", "http://test.com/test"));
 rootElement.addAttribute(new Attribute(new QName(XML_NAMESPACE, LOCAL_NAME, "namespace1"), "value1", "namespaceValue1"));
 rootElement.addAttribute(new Attribute(new QName(XML_NAMESPACE, LOCAL_NAME, "namespace2"), "value2", "namespaceValue2"));

 NodePointer rootNodePointer = new NodePointer(new JDOMNodeWrapper(rootElement), null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, TEST_QNAME);

 NodePointer firstAttributeNodePointer = iterator.getNodePointer();
 Assert.assertEquals(TEST_QNAME, firstAttributeNodePointer.getQName());
 Assert.assertNull(firstAttributeNodePointer.getValue());

 // Check that we can iterate to the next attribute with the same local name and namespace
 iterator.setPosition(1);
 NodePointer secondAttributeNodePointer = iterator.getNodePointer();
 Assert.assertEquals(TEST_QNAME, secondAttributeNodePointer.getQName());
 Assert.assertEquals("namespaceValue1", secondAttributeNodePointer.getValue().getValue());

 // Check that we can iterate to the next attribute with a different local name and namespace
 iterator.setPosition(2);
 NodePointer thirdAttributeNodePointer = iterator.getNodePointer();
 Assert.assertNotSame(TEST_QNAME, thirdAttributeNodePointer.getQName());
 Assert.assertEquals("namespaceValue2", thirdAttributeNodePointer.getValue().getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testIterationWithNoAttributes() {
 Element rootElement = new Element("root");
 NodePointer rootNodePointer = new NodePointer(new JDOMNodeWrapper(rootElement), null);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, TEST_QNAME);
 iterator.getNodePointer();
 }
}