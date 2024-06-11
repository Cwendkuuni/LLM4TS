import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.fragment.annotation.AnnotationApi;
import org.powermock.api.mockito.annotation.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.internal.PowerMockChangeStylesMockitoStyle.style;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DOMAttributeIterator.class})
@AnnotationApi
public class DOMAttributeIteratorTest {

 private NodePointer parentNode;
 private Element element;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 parentNode = new NodePointer(null, null, null, null, null);
 element = Mockito.spy(Mockito.mock(Element.class));
 }

 @Test
 public void testConstructor_NullParentPointer() {
 iterator = new DOMAttributeIterator(null, new QName("test"));
 assertNull(iterator);
 }

 @Test
 public void testConstructor_NullQName() {
 iterator = new DOMAttributeIterator(parentNode, null);
 assertNull(iterator);
 }

 @Test
 public void testConstructor_NullElement() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode"));
 iterator = new DOMAttributeIterator(parentNode, new QName("test"));
 Mockito.verifyNoMoreInteractions(element);
 }

 @Test
 public void testConstructor_EmptyElement() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 Element emptyElement = Mockito.mock(Element.class);
 Mockito.when(emptyElement.getAttributeNodes()).thenReturn(Mockito.mock(NamedNodeMap.class));
 iterator = new DOMAttributeIterator(parentNode, new QName("test"), emptyElement);
 Mockito.verifyZeroInteractions(element);
 }

 @Test
 public void testConstructor_ElementWithAttributes() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 String testLocalName = "test";
 String testPrefix = "test";
 Attr testAttr = Mockito.mock(Attr.class);
 Mockito.when(testAttr.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr.getNodeValue()).thenReturn("testValue");
 Mockito.when(element.getAttributeNodes()).thenReturn(Mockito.mock(NamedNodeMap.class));
 Mockito.when(element.getAttributeNodes()).thenReturn(new Attr[]{testAttr});
 iterator = new DOMAttributeIterator(parentNode, new QName(testPrefix, testLocalName));
 Attr attribute = (Attr) iterator.getNodePointer().getNode();
 assertEquals(testValue, attribute.getNodeValue());
 }

 @Test
 public void testConstructor_ElementWithMultipleAttributes() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 String testLocalName = "test";
 String testPrefix = "test";
 Attr testAttr1 = Mockito.mock(Attr.class);
 Attr testAttr2 = Mockito.mock(Attr.class);
 Mockito.when(testAttr1.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr1.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr1.getNodeValue()).thenReturn("testValue1");
 Mockito.when(testAttr2.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr2.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr2.getNodeValue()).thenReturn("testValue2");
 Mockito.when(element.getAttributeNodes()).thenReturn(new Attr[]{testAttr1, testAttr2});
 iterator = new DOMAttributeIterator(parentNode, new QName(testPrefix, testLocalName));
 Attr attribute1 = (Attr) iterator.getNodePointer().getNode();
 assertEquals(testValue1, attribute1.getNodeValue());
 Attr attribute2 = (Attr) iterator.getNodePointer().getNode(1);
 assertEquals(testValue2, attribute2.getNodeValue());
 }

 @Test
 public void testConstructor_ElementWithNoMatchingAttributes() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 String testLocalName = "test";
 String testPrefix = "test";
 String nodeNS = "nodeNS";
 Attr testAttr = Mockito.mock(Attr.class);
 Mockito.when(testAttr.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr.getNodeValue()).thenReturn("testValue");
 Mockito.when(element.getAttributeNodes()).thenReturn(new Attr[]{testAttr});
 iterator = new DOMAttributeIterator(parentNode, new QName(nodeNS, testLocalName));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testConstructor_ElementWithNoAttributes() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 Element noAttrElement = Mockito.mock(Element.class);
 iterator = new DOMAttributeIterator(parentNode, new QName("test"), noAttrElement);
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testHasNext_IteratingThroughAttributes() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 String testLocalName = "test";
 String testPrefix = "test";
 Attr testAttr1 = Mockito.mock(Attr.class);
 Attr testAttr2 = Mockito.mock(Attr.class);
 Mockito.when(testAttr1.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr1.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr1.getNodeValue()).thenReturn("testValue1");
 Mockito.when(testAttr2.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr2.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr2.getNodeValue()).thenReturn("testValue2");
 Mockito.when(element.getAttributeNodes()).thenReturn(new Attr[]{testAttr1, testAttr2});
 iterator = new DOMAttributeIterator(parentNode, new QName(testPrefix, testLocalName));
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testHasNext_NoMoreAttributes() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 String testLocalName = "test";
 String testPrefix = "test";
 Attr testAttr1 = Mockito.mock(Attr.class);
 Attr testAttr2 = Mockito.mock(Attr.class);
 Mockito.when(testAttr1.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr1.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr1.getNodeValue()).thenReturn("testValue1");
 Mockito.when(testAttr2.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr2.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr2.getNodeValue()).thenReturn("testValue2");
 Mockito.when(element.getAttributeNodes()).thenReturn(new Attr[]{testAttr1, testAttr2});
 iterator = new DOMAttributeIterator(parentNode, new QName(testPrefix, testLocalName));
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 Mockito.verify(iterator, style().times(2).calledOn("next"));
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testNext_IteratingThroughAttributes() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 String testLocalName = "test";
 String testPrefix = "test";
 Attr testAttr1 = Mockito.mock(Attr.class);
 Attr testAttr2 = Mockito.mock(Attr.class);
 Mockito.when(testAttr1.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr1.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr1.getNodeValue()).thenReturn("testValue1");
 Mockito.when(testAttr2.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr2.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr2.getNodeValue()).thenReturn("testValue2");
 Mockito.when(element.getAttributeNodes()).thenReturn(new Attr[]{testAttr1, testAttr2});
 iterator = new DOMAttributeIterator(parentNode, new QName(testPrefix, testLocalName));
 NodePointer nextNode = iterator.next();
 NodePointer nextNode2 = iterator.next();
 assertEquals(testAttr1, nextNode.getNode());
 assertEquals(testAttr2, nextNode2.getNode());
 }

 @Test
 public void testNext_NoMoreAttributes() {
 DOMAttributeIterator.setAttributeGetter(mock(Element.class, "getAttributeNode", "getAttributes"));
 String testLocalName = "test";
 String testPrefix = "test";
 Attr testAttr1 = Mockito.mock(Attr.class);
 Attr testAttr2 = Mockito.mock(Attr.class);
 Mockito.when(testAttr1.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr1.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr1.getNodeValue()).thenReturn("testValue1");
 Mockito.when(testAttr2.getNodeName()).thenReturn(new QName(testPrefix, testLocalName).getName());
 Mockito.when(testAttr2.getPrefix()).thenReturn(testPrefix);
 Mockito.when(testAttr2.getNodeValue()).thenReturn("testValue2");
 Mockito.when(element.getAttributeNodes()).thenReturn(new Attr[]{testAttr1, testAttr2});
 iterator = new DOMAttributeIterator(parentNode, new QName(testPrefix, testLocalName));
 NodePointer nextNode = iterator.next();
 NodePointer nextNode2 = iterator.next();
 assertEquals(testAttr1, nextNode.getNode());
 assertEquals(testAttr2, nextNode2.getNode());
 Mockito.verify(iterator, style().times(2).calledOn("next"));
 assertNull(iterator.next());
 }
}