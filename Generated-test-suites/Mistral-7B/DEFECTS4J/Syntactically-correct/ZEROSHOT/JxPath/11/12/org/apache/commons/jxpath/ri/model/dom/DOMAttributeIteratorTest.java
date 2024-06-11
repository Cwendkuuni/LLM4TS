import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

 @org.junit.Before
 public void setUp() {
 // Initialize test data
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForRootElementWithSpecificAttribute_thenReturnAttribute() {
 // Given
 Node root = createTestElement("root", null);
 Attr attribute = createTestAttribute("test", "testValue", root);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null, root), new QName("test"));

 // Then
 assertEquals(attribute, iterator.getNodePointer().getNode());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForRootElementWithWildcardName_thenReturnAllAttributes() {
 // Given
 Node root = createTestElement("root", null);
 Attr attribute1 = createTestAttribute("attr1", "value1", root);
 Attr attribute2 = createTestAttribute("attr2", "value2", root);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null, root), new QName("*"));

 // Then
 assertEquals(2, iterator.getPosition());
 assertNotNull(iterator.getNodePointer());
 assertEquals(attribute1.getNodeName(), iterator.getNodePointer().getNode().getNodeName());
 assertNotNull(iterator.next());
 assertEquals(attribute2.getNodeName(), iterator.getNodePointer().getNode().getNodeName());
 assertNull(iterator.next());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForRootElementWithPrefix_thenReturnCorrespondingAttribute() {
 // Given
 Node root = createTestElement("root", null);
 Attr attribute = createTestAttribute("test:attr", "testValue", root);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null, root), new QName("test", "attr"));

 // Then
 assertEquals(attribute, iterator.getNodePointer().getNode());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForRootElementWithNamespace_thenReturnCorrespondingAttribute() {
 // Given
 Node root = createTestElement("root", "http://test.com/");
 Attr attribute = createTestAttribute("test:attr", "testValue", root);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null, root), new QName("test", "attr"));

 // Then
 assertEquals(attribute, iterator.getNodePointer().getNode());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForElementWithSpecificAttribute_thenReturnAttribute() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);
 Attr attribute = createTestAttribute("test", "testValue", child);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("test"));

 // Then
 assertEquals(attribute, iterator.getNodePointer().getNode());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForElementWithWildcardName_thenReturnAllAttributes() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);
 Attr attribute1 = createTestAttribute("attr1", "value1", child);
 Attr attribute2 = createTestAttribute("attr2", "value2", child);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("*"));

 // Then
 assertEquals(2, iterator.getPosition());
 assertNotNull(iterator.getNodePointer());
 assertEquals(attribute1.getNodeName(), iterator.getNodePointer().getNode().getNodeName());
 assertNotNull(iterator.next());
 assertEquals(attribute2.getNodeName(), iterator.getNodePointer().getNode().getNodeName());
 assertNull(iterator.next());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForElementWithPrefix_thenReturnCorrespondingAttribute() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);
 Attr attribute = createTestAttribute("test:attr", "testValue", child);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("test", "attr"));

 // Then
 assertEquals(attribute, iterator.getNodePointer().getNode());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForElementWithNamespace_thenReturnCorrespondingAttribute() {
 // Given
 Node root = createTestElement("root", "http://test.com/");
 Element child = createTestElement("child", root);
 Attr attribute = createTestAttribute("test:attr", "testValue", child);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("test", "attr"));

 // Then
 assertEquals(attribute, iterator.getNodePointer().getNode());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenIteratorIsCreatedForElementWithNoMatchingAttribute_thenReturnNull() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("test", "nonExistentAttribute"));

 // Then
 assertNull(iterator.getNodePointer());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenNextIsCalledMultipleTimes_thenReturnNextAttribute() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);
 Attr attribute1 = createTestAttribute("attr1", "value1", child);
 Attr attribute2 = createTestAttribute("attr2", "value2", child);

 // When
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("*"));

 // Then
 assertEquals(2, iterator.getPosition());
 assertNotNull(iterator.getNodePointer());
 assertEquals(attribute1.getNodeName(), iterator.getNodePointer().getNode().getNodeName());
 assertNotNull(iterator.next());
 assertEquals(attribute2.getNodeName(), iterator.getNodePointer().getNode().getNodeName());
 assertNull(iterator.next());
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenSetPositionIsCalledWithPositionGreaterThanSize_thenReturnFalse() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);
 Attr attribute1 = createTestAttribute("attr1", "value1", child);
 Attr attribute2 = createTestAttribute("attr2", "value2", child);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("*"));

 // When
 boolean result = iterator.setPosition(3);

 // Then
 assertFalse(result);
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenSetPositionIsCalledWithPositionLessThanOne_thenReturnFalse() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);
 Attr attribute1 = createTestAttribute("attr1", "value1", child);
 Attr attribute2 = createTestAttribute("attr2", "value2", child);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("*"));

 // When
 boolean result = iterator.setPosition(0);

 // Then
 assertFalse(result);
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenSetPositionIsCalledWithPositionEqualOne_thenReturnTrue() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);
 Attr attribute1 = createTestAttribute("attr1", "value1", child);
 Attr attribute2 = createTestAttribute("attr2", "value2", child);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("*"));

 // When
 iterator.next();
 boolean result = iterator.setPosition(1);

 // Then
 assertTrue(result);
 }

 @org.junit.Test
 public void testDOMAttributeIterator_whenSetPositionIsCalledWithValidPosition_thenReturnTrue() {
 // Given
 Node root = createTestElement("root", null);
 Element child = createTestElement("child", root);
 Attr attribute1 = createTestAttribute("attr1", "value1", child);
 Attr attribute2 = createTestAttribute("attr2", "value2", child);
 DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(child, child), new QName("*"));

 // When
 iterator.next();
 iterator.next();
 boolean result = iterator.setPosition(2);

 // Then
 assertTrue(result);
 }

 private Node createTestElement(String tagName, String namespaceURI) {
 Element element = document.createElement(tagName);
 if (namespaceURI != null) {
 element.setNamespaceURI(namespaceURI);
 }
 document.appendChild(element);
 return element;
 }

 private Attr createTestAttribute(String localName, String value, Node parent) {
 Attr attr = document.createAttribute(localName);
 attr.setValue(value);
 parent.setAttributeNode(attr);
 return attr;
 }

 private Document document = document.newDocument();
}