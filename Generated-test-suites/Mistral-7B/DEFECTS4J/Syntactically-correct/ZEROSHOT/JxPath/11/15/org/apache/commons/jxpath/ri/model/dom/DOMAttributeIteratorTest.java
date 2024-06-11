import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

 private static final String XMLNS = "xmlns";
 private static final String TEST_NAMESPACE = "test";
 private static final String TEST_PREFIX = "test:";
 private static final String TEST_LOCAL_NAME = "test";
 private static final String TEST_QNAME = new QName(TEST_PREFIX, TEST_LOCAL_NAME);

 private Node createElementWithAttribute(String tagName, String attributeName, String attributeValue) {
 String xml = "<" + tagName + " xmlns:" + TEST_PREFIX + "=\"" + TEST_NAMESPACE + "\">"
 + "<" + tagName + " xmlns:" + XMLNS + "=\"" + XMLNS + "\""
 + " " + attributeName + "=\"" + attributeValue + "\"/>";
 return JaxpHelper.parseToNode(xml);
 }

 @Test
 public void testEmptyIterator() {
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithEmptyElement() {
 Element emptyElement = createElement("emptyElement");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(emptyElement), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithSingleAttribute() {
 Element element = createElementWithAttribute("rootElement", "testAttribute", "testValue");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName(TEST_QNAME));
 DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
 assertEquals(TEST_QNAME, attributePointer.getName());
 assertEquals("testValue", attributePointer.getValue());
 }

 @Test
 public void testIteratorWithSingleWildcardAttribute() {
 Element element = createElementWithAttribute("rootElement", "testAttribute", "testValue");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), null);
 DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
 assertNotNull(attributePointer);
 assertEquals("testValue", attributePointer.getValue());
 }

 @Test
 public void testIteratorWithSingleNonMatchingAttribute() {
 Element element = createElementWithAttribute("rootElement", "otherAttribute", "otherValue");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName(TEST_QNAME));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithMultipleAttributes() {
 Element element = createElementWithAttributes("rootElement", "testAttribute1", "testValue1", "testAttribute2", "testValue2");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), null);
 List<DOMAttributePointer> attributes = new ArrayList<>();
 while (iterator.hasNext()) {
 attributes.add((DOMAttributePointer) iterator.next());
 }
 assertEquals(2, attributes.size());
 DOMAttributePointer firstAttribute = attributes.get(0);
 assertEquals(TEST_QNAME, firstAttribute.getName());
 assertEquals("testValue1", firstAttribute.getValue());
 DOMAttributePointer secondAttribute = attributes.get(1);
 assertEquals(new QName("test:testAttribute2"), secondAttribute.getName());
 assertEquals("testValue2", secondAttribute.getValue());
 }

 @Test
 public void testIteratorWithNamespaceAttribute() {
 Element element = createElementWithAttribute("rootElement", XMLNS, "http://test.com");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespaceAttributeWithPrefix() {
 Element element = createElementWithAttribute("rootElement", TEST_PREFIX + ":xmlns", "http://test.com");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), TEST_QNAME);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorWithNamespaceAttributeWithLocalName() {
 Element element = createElementWithAttribute("rootElement", XMLNS, "http://test.com");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName(XMLNS, "xmlns"));
 DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
 assertEquals(new QName("http://test.com", "xmlns"), attributePointer.getName());
 }

 @Test
 public void testIteratorWithNamespaceAttributeWithPrefixAndLocalName() {
 Element element = createElementWithAttribute("rootElement", "test:" + XMLNS, "http://test.com");
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName(TEST_PREFIX, XMLNS));
 DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
 assertEquals(new QName("http://test.com", "xmlns"), attributePointer.getName());
 }
}