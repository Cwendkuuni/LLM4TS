package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.TestUtils;
import org.apache.commons.jxpath.ri.model.Node;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

 private EvalContext parentContext;
 private AttributeContext attributeContext;
 private Node testNode;

 @Before
 public void setUp() {
 testNode = TestUtils.createTestDocument();
 parentContext = new EvalContext(null, null);
 parentContext.setCurrentNodePointer(new NodePointer(testNode));
 attributeContext = new AttributeContext(parentContext, new NodeNameTest(new QName("testAttribute")));
 }

 @Test
 public void testNextNode_AttributeExists() {
 attributeContext.reset();
 assertTrue(attributeContext.nextNode());
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
 assertNotNull(currentNodePointer);
 assertEquals(testNode, currentNodePointer.getNode());
 assertEquals("testAttribute", currentNodePointer.getLocalName());
 assertEquals("testValue", currentNodePointer.getValue().getStringValue());
 }

 @Test
 public void testNextNode_AttributeDoesntExist_FirstCall() {
 attributeContext.reset();
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testNextNode_AttributeDoesntExist_SubsequentCall() {
 attributeContext.nextNode();
 assertFalse(attributeContext.nextNode());
 }

 @Test
 public void testSetPosition() {
 attributeContext.reset();
 NodePointer currentNodePointer = new NodePointer(testNode.getChildNodes().item(1));
 attributeContext.setPosition(1);
 assertTrue(attributeContext.nextNode());
 assertEquals(currentNodePointer, attributeContext.getCurrentNodePointer());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPosition_OutOfBounds() {
 attributeContext.reset();
 attributeContext.setPosition(100);
 }

 @Test
 public void testReset() {
 attributeContext.nextNode();
 attributeContext.reset();
 assertFalse(attributeContext.nextNode());
 }
}