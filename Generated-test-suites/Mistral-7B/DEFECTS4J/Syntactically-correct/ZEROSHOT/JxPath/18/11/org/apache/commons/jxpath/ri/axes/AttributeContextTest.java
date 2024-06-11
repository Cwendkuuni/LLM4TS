import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

 private AttributeContext attributeContext;
 @Mock private EvalContext parentContext;
 @Mock private NodeIterator attributeIterator;
 @Mock private NodePointer nodePointer;
 @Mock private NodeTest nodeTest;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 attributeContext = new AttributeContext(parentContext, nodeTest);
 }

 @Test
 public void testAttributeContextConstruction() {
 assertEquals(parentContext, attributeContext.getParentContext());
 assertNull(attributeContext.getCurrentNodePointer());
 assertFalse(attributeContext.isSetStarted());
 }

 @Test
 public void testReset() {
 attributeContext.reset();
 assertEquals(parentContext, attributeContext.getParentContext());
 assertNull(attributeContext.getCurrentNodePointer());
 assertFalse(attributeContext.isSetStarted());
 }

 @Test
 public void testSetPositionWithEmptyList() {
 assertFalse(attributeContext.setPosition(0));
 }

 @Test
 public void testSetPositionWithPositionGreaterThanCurrent() {
 // Given
 int position = 10;
 // When
 assertFalse(attributeContext.setPosition(position));
 // Then
 assertEquals(0, attributeContext.getCurrentPosition());
 }

 @Test
 public void testSetPositionWithPositionLessThanCurrent() {
 // Given
 int position = 1;
 // When
 attributeContext.setPosition(position);
 // Then
 assertEquals(position, attributeContext.getCurrentPosition());
 }

 @Test
 public void testNextNodeWithEmptyList() {
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeWithEndOfListReached() {
 // Given
 when(attributeIterator.isDone()).thenReturn(true);
 // When
 attributeContext.iterator = attributeIterator;
 // Then
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNodeWithAttributeFound() {
 // Given
 when(attributeIterator.getNodeName()).thenReturn(new QName("test"));
 when(attributeIterator.getNodePointer()).thenReturn(nodePointer);
 // When
 attributeContext.iterator = attributeIterator;
 // Then
 assertTrue(attributeContext.nextNode());
 assertEquals(nodePointer, attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testNextNodeWithNoAttributeFound() {
 // Given
 when(attributeIterator.isDone()).thenReturn(false);
 // When
 attributeContext.iterator = attributeIterator;
 // Then
 assertFalse(attributeContext.nextNode());
 }
}