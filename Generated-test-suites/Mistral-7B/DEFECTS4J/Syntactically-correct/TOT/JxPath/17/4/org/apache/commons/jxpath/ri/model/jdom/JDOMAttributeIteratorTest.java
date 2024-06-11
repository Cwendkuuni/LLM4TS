import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

 private static final String XML_NAMESPACE = "xml";
 private static final String NAMESPACE_URI = "http://example.com/ns";
 private static final String PREFIX = "ns";
 private static final String LOCAL_NAME = "testAttribute";
 private static final String VALUE = "testValue";

 @Test
 public void testIterateThroughAttributes() {
 Element root = new Element("root");
 root.addAttribute(new Attribute(XML_NAMESPACE, "xmlns:" + PREFIX, NAMESPACE_URI));
 root.addAttribute(new Attribute(null, LOCAL_NAME, VALUE));
 root.addAttribute(new Attribute(PREFIX, "prefix:" + LOCAL_NAME, VALUE));

 NodePointer nodePointer = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(PREFIX, LOCAL_NAME));

 List<Attribute> expectedAttributes = new ArrayList<>();
 expectedAttributes.add(root.getAttribute(LOCAL_NAME, new Namespace(XML_NAMESPACE, XML_NAMESPACE)));
 expectedAttributes.add(root.getAttribute(LOCAL_NAME, new Namespace(null, null)));
 expectedAttributes.add(root.getAttribute("prefix:" + LOCAL_NAME, new Namespace(PREFIX, NAMESPACE_URI)));

 int position = 0;
 while (iterator.hasNext()) {
 NodePointer attributePointer = iterator.next();
 Assert.assertEquals("Unexpected node type.", Attribute.class, attributePointer.getNode().getClass());
 Attribute attribute = (Attribute) attributePointer.getNode();
 Assert.assertEquals("Unexpected attribute value.", VALUE, attribute.getValue());
 Assert.assertEquals("Unexpected local name.", LOCAL_NAME, attribute.getName());
 Assert.assertArrayEquals("Unexpected namespace URI.", new String[]{NAMESPACE_URI, null, NAMESPACE_URI}, new String[]{attribute.getNamespace().getURI(), attribute.getNamespace().getPrefix(), attribute.getNamespace().getPrefix()});
 position++;
 }

 Assert.assertEquals("Unexpected number of attributes.", 3, position);
 }

 @Test(expected = IllegalStateException.class)
 public void testEmptyAttributeList() {
 Element root = new Element("root");
 NodePointer nodePointer = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(XML_NAMESPACE, "*"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBadPosition() {
 Element root = new Element("root");
 root.addAttribute(new Attribute(XML_NAMESPACE, "xmlns:" + PREFIX, NAMESPACE_URI));
 root.addAttribute(new Attribute(null, LOCAL_NAME, VALUE));
 root.addAttribute(new Attribute(PREFIX, "prefix:" + LOCAL_NAME, VALUE));

 NodePointer nodePointer = new NodePointer(root);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(PREFIX, LOCAL_NAME));
 iterator.setPosition(4);
 }
}