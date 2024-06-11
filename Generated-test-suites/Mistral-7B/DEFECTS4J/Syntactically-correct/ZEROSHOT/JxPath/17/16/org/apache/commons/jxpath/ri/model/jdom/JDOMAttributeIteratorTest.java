import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

 private static final String TEST_NAMESPACE_URI = "http://test.com";
 private static final String TEST_NAMESPACE_PREFIX = "test";
 private static final String TEST_ELEMENT_NAME = "testElement";
 private static final String TEST_ATTRIBUTE_NAME = "testAttribute";
 private static final String TEST_ATTRIBUTE_VALUE = "testValue";

 @Test
 public void testJDOMAttributeIterator_empty() {
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), new QName(TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX, TEST_ELEMENT_NAME));
 assertTrue(iterator.attributes.isEmpty());
 }

 @Test
 public void testJDOMAttributeIterator_single_attribute() {
 Element testElement = new Element(TEST_ELEMENT_NAME);
 testElement.addAttribute(new Attribute(TEST_ATTRIBUTE_NAME, TEST_ATTRIBUTE_VALUE, TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));
 NodePointer parentPointer = new NodePointer(testElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX, TEST_ATTRIBUTE_NAME));
 List<Attribute> attributes = iterator.attributes;

 assertTrue(attributes.size() == 1);
 Attribute attribute = attributes.get(0);
 assertSame(attribute, testElement.getAttribute(TEST_ATTRIBUTE_NAME, TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));
 }

 @Test
 public void testJDOMAttributeIterator_multiple_attributes() {
 Element testElement = new Element(TEST_ELEMENT_NAME);
 testElement.addAttribute(new Attribute(TEST_ATTRIBUTE_NAME + "_1", TEST_ATTRIBUTE_VALUE, TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));
 testElement.addAttribute(new Attribute(TEST_ATTRIBUTE_NAME + "_2", TEST_ATTRIBUTE_VALUE, TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));
 NodePointer parentPointer = new NodePointer(testElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX, "*"));
 List<Attribute> attributes = iterator.attributes;

 assertTrue(attributes.size() == 2);
 List<Attribute> expectedAttributes = new ArrayList<>();
 expectedAttributes.add(testElement.getAttribute(TEST_ATTRIBUTE_NAME + "_1", TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));
 expectedAttributes.add(testElement.getAttribute(TEST_ATTRIBUTE_NAME + "_2", TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));
 for (int i = 0; i < attributes.size(); i++) {
 Attribute currentAttribute = attributes.get(i);
 Attribute expectedAttribute = expectedAttributes.get(i);
 assertSame(currentAttribute, expectedAttribute);
 }
 }

 @Test
 public void testJDOMAttributeIterator_no_matching_attributes() {
 Element testElement = new Element(TEST_ELEMENT_NAME);
 NodePointer parentPointer = new NodePointer(testElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX, TEST_ATTRIBUTE_NAME));

 assertTrue(iterator.attributes.isEmpty());
 }

 @Test
 public void testJDOMAttributeIterator_next_node() {
 Element testElement = new Element(TEST_ELEMENT_NAME);
 testElement.addAttribute(new Attribute(TEST_ATTRIBUTE_NAME, TEST_ATTRIBUTE_VALUE, TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));
 NodePointer parentPointer = new NodePointer(testElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX, TEST_ATTRIBUTE_NAME));
 NodePointer currentNode = iterator.getNodePointer();

 assertSame(currentNode.getNode(), testElement.getAttribute(TEST_ATTRIBUTE_NAME, TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));

 iterator.setPosition(1);
 currentNode = iterator.getNodePointer();

 assertNull(currentNode.getNode());
 }

 @Test
 public void testJDOMAttributeIterator_out_of_bound_position() {
 Element testElement = new Element(TEST_ELEMENT_NAME);
 testElement.addAttribute(new Attribute(TEST_ATTRIBUTE_NAME, TEST_ATTRIBUTE_VALUE, TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX));
 NodePointer parentPointer = new NodePointer(testElement);
 JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(TEST_NAMESPACE_URI, TEST_NAMESPACE_PREFIX, TEST_ATTRIBUTE_NAME));

 iterator.setPosition(2);
 assertFalse(iterator.getNodePointer().getNode() instanceof Attribute);
 }
}