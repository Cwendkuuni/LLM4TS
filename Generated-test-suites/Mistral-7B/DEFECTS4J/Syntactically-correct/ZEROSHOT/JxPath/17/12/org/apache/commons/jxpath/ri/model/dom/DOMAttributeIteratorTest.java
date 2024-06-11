import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DOMAttributeIteratorTest {

 private DOMAttributeIterator iterator;
 @Mock
 private NodePointer parent;
 @Mock
 private QName name;
 @Mock
 private Element element;
 @Mock
 private Attr attribute;
 @Mock
 private NamedNodeMap namedNodeMap;
 @Mock
 private Node node;

 @Before
 public void setUp() {
 Mockito.when(parent.getNode()).thenReturn(node);
 Mockito.when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
 Mockito.when(node.getAttributes()).thenReturn(namedNodeMap);
 Mockito.when(namedNodeMap.getLength()).thenReturn(2);
 Mockito.when(namedNodeMap.item(0)).thenReturn(attribute);
 Mockito.when(element.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(attribute);
 Mockito.when(element.getAttributeNode(Mockito.anyString())).thenReturn(attribute);
 }

 @Test
 public void testConstructorWithEmptyAttributeList() {
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNull(iterator.nextNodePointer());
 }

 @Test
 public void testConstructorWithSingleAttribute() {
 Mockito.when(namedNodeMap.getLength()).thenReturn(1);
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNotNull(iterator.nextNodePointer());
 }

 @Test
 public void testConstructorWithMultipleAttributes() {
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 assertNotNull(iterator.nextNodePointer());
 Mockito.verify(namedNodeMap).getLength();
 }

 @Test
 public void testGetNodePointerWithFirstAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasPrevious());
 NodePointer nodePointer = iterator.nextNodePointer();
 assertNotNull(nodePointer);
 assertEquals(attribute, nodePointer.getNode());
 }

 @Test
 public void testGetNodePointerWithMiddleAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 NodePointer nodePointer = iterator.nextNodePointer();
 assertNotNull(nodePointer);
 assertEquals(attribute, nodePointer.getNode());
 }

 @Test
 public void testGetNodePointerWithLastAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 for (int i = 0; i < namedNodeMap.getLength(); i++) {
 iterator.next();
 }
 NodePointer nodePointer = iterator.nextNodePointer();
 assertNotNull(nodePointer);
 assertEquals(attribute, nodePointer.getNode());
 }

 @Test
 public void testGetNodePointerWithNextAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 NodePointer nodePointer = iterator.nextNodePointer();
 assertNotNull(nodePointer);
 assertEquals(attribute, nodePointer.getNode());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetNodePointerWithPreviousAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 iterator.previous();
 iterator.previous();
 iterator.previous();
 iterator.previous();
 iterator.previous();
 iterator.previous();
 NodePointer nodePointer = iterator.previousNodePointer();
 }

 @Test
 public void testHasNextWithFirstAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNextWithMiddleAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNextWithLastAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 for (int i = 0; i < namedNodeMap.getLength(); i++) {
 iterator.next();
 }
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testHasPreviousWithFirstAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testHasPreviousWithMiddleAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 iterator.previous();
 assertTrue(iterator.hasPrevious());
 }

 @Test
 public void testHasPreviousWithLastAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 for (int i = 0; i < namedNodeMap.getLength(); i++) {
 iterator.next();
 }
 assertFalse(iterator.hasPrevious());
 }

 @Test
 public void testTestAttrWithEqualAttributes() {
 String localName = "test";
 String prefix = "testPrefix";
 iterator = new DOMAttributeIterator(parent, new QName(prefix, localName));
 Attr attribute1 = new Attr("test", "value1");
 Attr attribute2 = new Attr("test", "value2");
 attribute1.setPrefix(prefix);
 attribute2.setPrefix(prefix);
 Mockito.when(element.getAttributeNodeNS(prefix, localName)).thenReturn(attribute1);
 Mockito.when(element.getAttributeNode(localName)).thenReturn(attribute2);
 assertTrue(iterator.testAttr(attribute1));
 assertTrue(iterator.testAttr(attribute2));
 }

 @Test
 public void testTestAttrWithDifferentNames() {
 String localName1 = "test";
 String localName2 = "test2";
 iterator = new DOMAttributeIterator(parent, new QName("", localName1));
 Attr attribute1 = new Attr("test", "value1");
 Attr attribute2 = new Attr("test2", "value2");
 Mockito.when(element.getAttributeNodeNS("", localName1)).thenReturn(attribute1);
 Mockito.when(element.getAttributeNode("test2")).thenReturn(attribute2);
 assertFalse(iterator.testAttr(attribute1));
 assertFalse(iterator.testAttr(attribute2));
 }

 @Test
 public void testTestAttrWithDifferentPrefixes() {
 String localName = "test";
 String prefix1 = "testPrefix1";
 String prefix2 = "testPrefix2";
 iterator = new DOMAttributeIterator(parent, new QName(prefix1, localName));
 Attr attribute1 = new Attr("test", "value1");
 attribute1.setPrefix(prefix2);
 Attr attribute2 = new Attr("test", "value2");
 Mockito.when(element.getAttributeNodeNS(prefix1, localName)).thenReturn(attribute1);
 Mockito.when(element.getAttributeNode("test", Mockito.eq(prefix2))).thenReturn(attribute2);
 assertFalse(iterator.testAttr(attribute1));
 assertFalse(iterator.testAttr(attribute2));
 }

 @Test
 public void testTestAttrWithEmptyPrefix() {
 String localName = "test";
 iterator = new DOMAttributeIterator(parent, new QName("", localName));
 Attr attribute1 = new Attr("test", "value1");
 Mockito.when(element.getAttributeNode(localName)).thenReturn(attribute1);
 assertTrue(iterator.testAttr(attribute1));
 }

 @Test
 public void testTestAttrWithNullPrefix() {
 String localName = "test";
 iterator = new DOMAttributeIterator(parent, new QName(null, localName));
 Attr attribute1 = new Attr("test", "value1");
 Mockito.when(element.getAttributeNode(localName)).thenReturn(attribute1);
 assertTrue(iterator.testAttr(attribute1));
 }

 @Test
 public void testTestAttrWithNullLocalName() {
 iterator = new DOMAttributeIterator(parent, null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testTestAttrWithNullPrefixAndNullLocalName() {
 iterator = new DOMAttributeIterator(parent, null);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSetPositionWithFirstAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.setPosition(1));
 }

 @Test
 public void testSetPositionWithMiddleAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 for (int i = 0; i < namedNodeMap.getLength() - 1; i++) {
 iterator.next();
 }
 assertTrue(iterator.setPosition(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithLastAttribute() {
 iterator = new DOMAttributeIterator(parent, name);
 for (int i = 0; i < namedNodeMap.getLength(); i++) {
 iterator.next();
 }
 assertFalse(iterator.setPosition(namedNodeMap.getLength() + 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithNegativePosition() {
 iterator = new DOMAttributeIterator(parent, name);
 for (int i = 0; i < namedNodeMap.getLength(); i++) {
 iterator.next();
 }
 assertFalse(iterator.setPosition(-1));
 }

 @Test
 public void testSetPositionWithValidPosition() {
 iterator = new DOMAttributeIterator(parent, name);
 for (int i = 0; i < namedNodeMap.getLength() - 1; i++) {
 iterator.next();
 }
 assertTrue(iterator.setPosition(namedNodeMap.getLength()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetPositionWithInvalidPosition() {
 iterator = new DOMAttributeIterator(parent, name);
 for (int i = 0; i < namedNodeMap.getLength(); i++) {
 iterator.next();
 }
 assertFalse(iterator.setPosition(namedNodeMap.getLength() + 1));
 }
}