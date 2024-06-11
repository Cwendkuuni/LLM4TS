package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.Nodepointer;
import org.apache.commons.jxpath.ri.model.Axis;
import org.apache.commons.jxpath.ri.model.Node;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AttributeContextTest {

 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;
 private Document document;
 private Element element;

 @Before
 public void setUp() {
 parentContext = mock(EvalContext.class);
 nodeTest = mock(NodeNameTest.class);
 document = mock(Document.class);
 element = mock(Element.class);
 when(parentContext.getCurrentNodePointer()).thenReturn(new Nodepointer(document, element));
 when(parentContext.getAttributeIterator(any(QName.class))).thenReturn(createMock(NodeIterator.class));
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @Test
 public void testNextNode_AttributeExists() {
 NodeIterator iterator = createMock(NodeIterator.class);
 when(iterator.getNodePointer()).thenReturn(new Nodepointer(document, element));
 when(iterator.hasNext()).thenReturn(true, true, false);
 when(iterator.next()).thenReturn(node("attribute1", "value1"));
 when(parentContext.getAttributeIterator(any(QName.class))).thenReturn(iterator);

 attributeContext.nextNode();
 attributeContext.nextNode();
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(Axis.ATTRIBUTE, currentNodePointer.getAxis());
 assertEquals("attribute1", currentNodePointer.getNodeName().getLocalPart());
 assertEquals("value1", currentNodePointer.getNodeValue());
 }

 @Test
 public void testNextNode_AttributeDoesNotExist() {
 NodeIterator iterator = createMock(NodeIterator.class);
 when(iterator.getNodePointer()).thenReturn(new Nodepointer(document, element));
 when(iterator.hasNext()).thenReturn(false);
 when(parentContext.getAttributeIterator(any(QName.class))).thenReturn(iterator);

 attributeContext.nextNode();
 assertNull(attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testSetPosition_AttributeExists() {
 NodeIterator iterator = createMock(NodeIterator.class);
 when(iterator.getNodePointer()).thenReturn(new Nodepointer(document, element));
 when(iterator.hasNext()).thenReturn(true, true, false);
 when(iterator.next()).thenReturn(node("attribute1", "value1"));
 when(parentContext.getAttributeIterator(any(QName.class))).thenReturn(iterator);

 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.setPosition(2);
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 assertNull(currentNodePointer);

 attributeContext.nextNode();
 currentNodePointer = attributeContext.getCurrentNodePointer();
 assertEquals(Axis.ATTRIBUTE, currentNodePointer.getAxis());
 assertEquals("attribute1", currentNodePointer.getNodeName().getLocalPart());
 assertEquals("value1", currentNodePointer.getNodeValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPosition_PositionOutOfBounds() {
 NodeIterator iterator = createMock(NodeIterator.class);
 when(iterator.getNodePointer()).thenReturn(new Nodepointer(document, element));
 when(iterator.hasNext()).thenReturn(true, true, false);
 when(iterator.next()).thenReturn(node("attribute1", "value1"));
 when(parentContext.getAttributeIterator(any(QName.class))).thenReturn(iterator);

 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.setPosition(3);
 }

 @Test
 public void testReset() {
 NodeIterator iterator = createMock(NodeIterator.class);
 when(iterator.getNodePointer()).thenReturn(new Nodepointer(document, element));
 when(iterator.hasNext()).thenReturn(true, true, false);
 when(iterator.next()).thenReturn(node("attribute1", "value1"));
 when(parentContext.getAttributeIterator(any(QName.class))).thenReturn(iterator);

 attributeContext.nextNode();
 attributeContext.nextNode();
 attributeContext.setPosition(2);
 attributeContext.reset();
 assertNull(attributeContext.getCurrentNodePointer());
 }
}