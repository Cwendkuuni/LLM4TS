package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

 private @Mock EvalContext parentContext;
 private AttributeContext attributeContext;
 private NodeNameTest nodeNameTest;
 private NodePointer currentNodePointer;
 private NodeIterator nodeIterator;

 @Before
 public void setup() {
 nodeNameTest = Mockito.mock(NodeNameTest.class);
 nodeIterator = Mockito.mock(NodeIterator.class);
 currentNodePointer = Mockito.mock(NodePointer.class);
 Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
 Mockito.when(nodeIterator.getNodePointer()).thenReturn(currentNodePointer);
 attributeContext = new AttributeContext(parentContext, nodeNameTest);
 }

 @Test
 public void testAttributeContextConstruction() {
 assertNotNull(attributeContext);
 assertEquals(parentContext, attributeContext.getParentContext());
 }

 @Test
 public void testGetCurrentNodePointer() {
 assertNull(attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testReset() {
 attributeContext.reset();
 assertFalse(attributeContext.isStarted());
 assertNull(attributeContext.iterator);
 }

 @Test
 public void testSetPositionWithValidPosition() {
 attributeContext.setPosition(0);
 assertTrue(attributeContext.iterator.hasNext());
 }

 @Test
 public void testSetPositionWithInvalidPosition() {
 attributeContext.setPosition(1);
 assertFalse(attributeContext.iterator.hasNext());
 }

 @Test
 public void testNextNodeWhenAttributeExists() {
 attributeContext.nextNode();
 assertTrue(attributeContext.iterator.hasNext());
 }

 @Test
 public void testNextNodeWhenAttributeDoesNotExist() {
 Mockito.when(parentContext.getCurrentNodePointer().attributeIterator(nodeNameTest)).thenReturn(nodeIterator);
 Mockito.when(nodeIterator.hasNext()).thenReturn(false);
 attributeContext.nextNode();
 assertFalse(attributeContext.iterator.hasNext());
 }

 @Test
 public void testNextNodeWhenNodeTestIsANodeTest() {
 Mockito.when(parentContext.getCurrentNodePointer().attributeIterator(nodeNameTest)).thenReturn(nodeIterator);
 Mockito.when(nodeIterator.hasNext()).thenReturn(false);
 Mockito.when(nodeIterator.getNodePointer()).thenReturn(currentNodePointer);
 attributeContext.nextNode();
 assertFalse(attributeContext.iterator.hasNext());
 }

 @Test
 public void testGetCurrentNodePointerAfterNextNode() {
 attributeContext.nextNode();
 assertNotNull(attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testGetCurrentNodePointerBeforeNextNode() {
 assertNull(attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testSetPositionBeforeFirstNode() {
 attributeContext.setPosition(-1);
 assertFalse(attributeContext.iterator.hasNext());
 }

 @Test
 public void testSetPositionAfterLastNode() {
 attributeContext.setPosition(Integer.MAX_VALUE);
 assertFalse(attributeContext.iterator.hasNext());
 }

 @Test
 public void testIteratorHasNextWhenStarted() {
 Mockito.when(parentContext.getCurrentNodePointer().attributeIterator(nodeNameTest)).thenReturn(nodeIterator);
 Mockito.when(nodeIterator.hasNext()).thenReturn(true);
 attributeContext.nextNode();
 attributeContext.setStarted();
 assertTrue(attributeContext.iterator.hasNext());
 }

 @Test
 public void testIteratorDoesNotHaveNextWhenNotStarted() {
 Mockito.when(parentContext.getCurrentNodePointer().attributeIterator(nodeNameTest)).thenReturn(nodeIterator);
 Mockito.when(nodeIterator.hasNext()).thenReturn(false);
 assertFalse(attributeContext.iterator.hasNext());
 }
}