import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.XPathNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;
 private XPathNode parentNode;

 @Before
 public void setUp() {
 parentContext = Mockito.spy(EvalContext.class);
 nodeTest = Mockito.mock(NodeNameTest.class);
 parentNode = Mockito.mock(XPathNode.class);
 Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointer(parentNode));
 }

 @Test
 public void testAttributeContextConstructor() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 assertNotNull(attributeContext.iterator);
 assertFalse(attributeContext.setStarted);
 assertNull(attributeContext.currentNodePointer);
 }

 @Test
 public void testReset() {
 attributeContext.reset();
 assertFalse(attributeContext.setStarted);
 assertNull(attributeContext.iterator);
 }

 @Test
 public void testSetPositionLessThanCurrentPosition() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.setPosition(1);
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testSetPositionGreaterThanCurrentPosition() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.nextNode();
 attributeContext.setPosition(2);
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testSetPositionEqualCurrentPosition() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 attributeContext.nextNode();
 attributeContext.setPosition(1);
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeNoAttributeFound() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeAttributeFound() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 Mockito.when(parentNode.getAttributeNames()).thenReturn(new String[]{"testAttribute"});
 Mockito.when(parentNode.getAttributeValue("testAttribute")).thenReturn("testValue");
 Mockito.when(parentNode.getAttributeIterator(ArgumentMatchers.eq(new QName("test")))).thenReturn(Mockito.mock(NodeIterator.class));
 attributeContext.nextNode();
 assertNotNull(attributeContext.currentNodePointer);
 assertEquals("testValue", attributeContext.currentNodePointer.getValue().getNodeValue());
 }

 @Test
 public void testNextNodeAttributeFoundNextAttribute() {
 attributeContext = new AttributeContext(parentContext, nodeTest);
 Mockito.when(parentNode.getAttributeNames()).thenReturn(new String[]{"testAttribute1", "testAttribute2"});
 Mockito.when(parentNode.getAttributeValue("testAttribute1")).thenReturn("testValue1");
 Mockito.when(parentNode.getAttributeValue("testAttribute2")).thenReturn("testValue2");
 Mockito.when(parentNode.getAttributeIterator(ArgumentMatchers.eq(new QName("test")))).thenReturn(Mockito.mock(NodeIterator.class));
 attributeContext.nextNode();
 assertNotNull(attributeContext.currentNodePointer);
 assertEquals("testValue1", attributeContext.currentNodePointer.getValue().getNodeValue());
 attributeContext.nextNode();
 assertNotNull(attributeContext.currentNodePointer);
 assertEquals("testValue2", attributeContext.currentNodePointer.getValue().getNodeValue());
 }
}