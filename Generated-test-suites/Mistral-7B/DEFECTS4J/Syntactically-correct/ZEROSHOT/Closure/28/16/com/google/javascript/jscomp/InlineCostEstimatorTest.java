import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Before;
import org.junit.Test;

public class InlineCostEstimatorTest {

 private Node rootNode;
 private InlineCostEstimator estimator;
 private CompiledSizeEstimator compiledSizeEstimator;

 @Before
 public void setUp() {
 rootNode = NodeOperations.createLiteral(1);
 estimator = new InlineCostEstimator();
 compiledSizeEstimator = mock(CompiledSizeEstimator.class);
 when(compiledSizeEstimator.getCost()).thenReturn(0);
 estimator.add(compiledSizeEstimator);
 }

 @Test
 public void testGetCostOfRootNode() {
 int cost = InlineCostEstimator.getCost(rootNode);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostOfRootNodeWithThreshold() {
 int cost = InlineCostEstimator.getCost(rootNode, 10);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostOfEmptyNode() {
 Node emptyNode = NodeOperations.createEmptyStatement();
 int cost = InlineCostEstimator.getCost(emptyNode);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostOfSimpleExpression() {
 Node simpleExpressionNode = NodeOperations.createBinaryOperator("+", NodeOperations.createNumber(1), NodeOperations.createNumber(2));
 int cost = InlineCostEstimator.getCost(simpleExpressionNode);
 assertEquals(2, cost);
 }

 @Test
 public void testGetCostOfComplexExpression() {
 Node complexExpressionNode = NodeOperations.createAssignment("x", NodeOperations.createBinaryOperator("+", NodeOperations.createName("y"), NodeOperations.createNumber(3)));
 int cost = InlineCostEstimator.getCost(complexExpressionNode);
 assertEquals(6, cost);
 }

 @Test
 public void testGetCostOfStatement() {
 Node statementNode = NodeOperations.createExpressionStatement(NodeOperations.createBinaryOperator("+", NodeOperations.createName("x"), NodeOperations.createNumber(1)));
 int cost = InlineCostEstimator.getCost(statementNode);
 assertEquals(3, cost);
 }

 @Test
 public void testGetCostOfBlock() {
 Node blockNode = NodeOperations.createBlock(NodeOperations.createExpressionStatement(NodeOperations.createBinaryOperator("+", NodeOperations.createName("x"), NodeOperations.createNumber(1))), NodeOperations.createExpressionStatement(NodeOperations.createBinaryOperator("+", NodeOperations.createName("y"), NodeOperations.createNumber(2))));
 int cost = InlineCostEstimator.getCost(blockNode);
 assertEquals(9, cost);
 }

 @Test
 public void testGetCostOfComplexBlock() {
 Node complexBlockNode = NodeOperations.createBlock(NodeOperations.createExpressionStatement(NodeOperations.createAssignment("x", NodeOperations.createBinaryOperator("+", NodeOperations.createName("y"), NodeOperations.createNumber(3)))), NodeOperations.createExpressionStatement(NodeOperations.createAssignment("z", NodeOperations.createBinaryOperator("*", NodeOperations.createName("x"), NodeOperations.createNumber(2)))));
 int cost = InlineCostEstimator.getCost(complexBlockNode);
 assertEquals(15, cost);
 }
}