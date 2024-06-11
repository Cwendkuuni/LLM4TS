import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 private static final String XML_TEST_DATA = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:test=\"http://test.com/\">\n" +
         "<element attr1=\"value1\" attr2=\"value2\" attr3=\"value3\"/>\n" +
         "</root>";

 @Test
 public void testDOMAttributeIterator() {
  Node rootNode = getDocumentFromString(XML_TEST_DATA).getDocumentElement();
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootNode), new QName("*"));
  NodePointer nodePointer = null;

  // Test initial state
  assertFalse(iterator.hasNext());

  // Test next
  nodePointer = iterator.next();
  assertTrue(nodePointer != null);
  assertEquals("attr1", nodePointer.getNodeName().getLocalPart());
  assertEquals("value1", nodePointer.getNodeValue());
  assertTrue(iterator.hasNext());

  nodePointer = iterator.next();
  assertTrue(nodePointer != null);
  assertEquals("attr2", nodePointer.getNodeName().getLocalPart());
  assertEquals("value2", nodePointer.getNodeValue());
  assertTrue(iterator.hasNext());

  nodePointer = iterator.next();
  assertTrue(nodePointer != null);
  assertEquals("attr3", nodePointer.getNodeName().getLocalPart());
  assertEquals("value3", nodePointer.getNodeValue());
  assertFalse(iterator.hasNext());
 }

 @Test
 public void testDOMAttributeIteratorFiltering() {
  Node rootNode = getDocumentFromString(XML_TEST_DATA).getDocumentElement();
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootNode), new QName("test:attr2"));
  NodePointer nodePointer = null;

  // Test initial state
  assertFalse(iterator.hasNext());

  // Test next
  nodePointer = iterator.next();
  assertNull(nodePointer);
  assertFalse(iterator.hasNext());
 }

 @Test(expected = IllegalStateException.class)
 public void testDOMAttributeIteratorInvalidParent() {
  Node rootNode = getDocumentFromString(XML_TEST_DATA).getDocumentElement();
  DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), new QName("*"));
  iterator.next();
 }

 private Document getDocumentFromString(String xmlString) {
  InputSource inputSource = new InputSource();
  inputSource.setCharacterStream(new StringReader(xmlString));
  return JaxbContext.newInstance().createDocumentBuilder().parse(inputSource);
 }
}