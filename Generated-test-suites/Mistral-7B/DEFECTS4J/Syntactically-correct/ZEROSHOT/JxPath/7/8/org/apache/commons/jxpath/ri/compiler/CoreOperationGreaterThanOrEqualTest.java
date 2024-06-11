package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual;
 private EvalContext context;
 private InfoSet infoSet;

 @Before
 public void setUp() {
 arg1 = new LiteralExpression(new Double(5.0));
 arg2 = new LiteralExpression(new Double(4.0));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 context = new EvalContext();
 infoSet = new InfoSet();
 context.setNodeInfo(new NodeInfo("root", null));
 context.setInfoSet(infoSet);
 }

 @Test
 public void testComputeValueWhenLeftIsGreaterThanRight() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 double result = coreOperationGreaterThanOrEqual.computeValue(context);

 // Then
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueWhenLeftIsLessThanRight() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 double result = coreOperationGreaterThanOrEqual.computeValue(context);

 // Then
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueWhenLeftAndRightAreEqual() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 arg1 = new LiteralExpression(new Double(4.0));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 double result = coreOperationGreaterThanOrEqual.computeValue(context);

 // Then
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueWhenLeftAndRightAreBothGreaterThanZero() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 arg1 = new LiteralExpression(new Double(6.0));
 arg2 = new LiteralExpression(new Double(5.0));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 double result = coreOperationGreaterThanOrEqual.computeValue(context);

 // Then
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueWhenLeftAndRightAreBothNegative() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 arg1 = new LiteralExpression(new Double(-3.0));
 arg2 = new LiteralExpression(new Double(-2.0));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 double result = coreOperationGreaterThanOrEqual.computeValue(context);

 // Then
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueWhenLeftIsZeroAndRightIsNegative() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 arg1 = new LiteralExpression(new Double(0.0));
 arg2 = new LiteralExpression(new Double(-2.0));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 double result = coreOperationGreaterThanOrEqual.computeValue(context);

 // Then
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueWhenLeftIsNegativeAndRightIsZero() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 arg1 = new LiteralExpression(new Double(-2.0));
 arg2 = new LiteralExpression(new Double(0.0));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 double result = coreOperationGreaterThanOrEqual.computeValue(context);

 // Then
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueWhenLeftIsNegativeAndRightIsNegative() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 arg1 = new LiteralExpression(new Double(-3.0));
 arg2 = new LiteralExpression(new Double(-2.0));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 double result = coreOperationGreaterThanOrEqual.computeValue(context);

 // Then
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsExceptionWhenLeftTypeIsNotNumber() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 arg1 = new LiteralExpression(new String("string"));
 arg2 = new LiteralExpression(new Double(4.0));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 coreOperationGreaterThanOrEqual.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsExceptionWhenRightTypeIsNotNumber() {
 infoSet.setCurrentNode(new NodeInfo(1, "node1", null));
 infoSet.addNode("node2", null);
 // Given
 arg1 = new LiteralExpression(new Double(4.0));
 arg2 = new LiteralExpression(new String("string"));
 coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 coreOperationGreaterThanOrEqual.computeValue(context);
 }
}