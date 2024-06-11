import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompiledSizeEstimator;
import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.jscomp.CodeGenerator;
import com.google.javascript.jscomp.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost_RootIsNull_ReturnsZero() {
    Node rootNode = null;
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    int cost = InlineCostEstimator.getCost(rootNode, estimator);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCost_EmptyNode_ReturnsZero() {
    Node rootNode = Node.newNull();
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    int cost = InlineCostEstimator.getCost(rootNode, estimator);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCost_SimpleNode_EstimatesCorrectly() {
    Node rootNode = Node.newIdentifier("identifier");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    int cost = InlineCostEstimator.getCost(rootNode, estimator);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);

    rootNode = Node.newBoolean(true);
    cost = InlineCostEstimator.getCost(rootNode, estimator);
    assertEquals(0, cost);

    rootNode = Node.newNull();
    cost = InlineCostEstimator.getCost(rootNode, estimator);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCost_ComplexNode_EstimatesCorrectly() {
    Node rootNode = Node.newCall(Node.newIdentifier("functionName"), Node.newArray(Node.newNumber(1), Node.newNumber(2)));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    int cost = InlineCostEstimator.getCost(rootNode, estimator);
    assertEquals(2 * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST + Node.newNumber(1).toString().length(), cost);
  }

  @Test
  public void testGetCost_LargeNodeTree_EstimatesCorrectly() {
    Node rootNode = Node.newProperty(Node.newObjectLiteral(), "propertyName", Node.newCall(Node.newIdentifier("functionName"), Node.newArray()));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
    int cost = InlineCostEstimator.getCost(rootNode, estimator);

    int expectedCost = InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 4 + "propertyName".length() + "functionName".length() * 2;
    assertEquals(expectedCost, cost);
  }

  @Test
  public void testCreateCompiledSizeEstimator_MaxCostIsSet() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    assertEquals(10, estimator.maxCost);
  }

  @Test
  public void testCodeConsumerMethods() {
    CompiledSizeEstimator estimator = mock(CompiledSizeEstimator.class);
    CodeConsumer codeConsumer = new InlineCostEstimator.CompiledSizeEstimator();

    when(estimator.getLastChar()).thenReturn('a');

    codeConsumer.add(estimator);

    assertEquals('a', codeConsumer.getLastChar());
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, codeConsumer.cost);

    codeConsumer.addIdentifier("identifier");

    assertEquals('a', codeConsumer.getLastChar());
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST * 2, codeConsumer.cost);
  }
}