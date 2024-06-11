import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost_RootWithNoChildren_EstimatesCorrectly() {
    Node root = Node.IDENT("root");
    int cost = getCost(root);
    assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCost_RootWithSimpleChild_EstimatesCorrectly() {
    Node root = Node.IDENT("root");
    Node child = Node.BOOL(true);
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    estimator.add(child);
    estimator.addIdentifier("i");
    int cost = estimator.getCost();
    assertEquals(ESTIMATED_IDENTIFIER_COST * 3 + 1, cost); // +1 for the bool node
  }

  @Test
  public void testGetCost_RootWithComplexChild_EstimatesCorrectly() {
    Node root = Node.BLOCK(Node.IDENT("root"), createComplexExpression());
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    estimator.addIdentifier("i");
    int cost = estimator.getCost();
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 4; // +3 for the block node
    assertEquals(expectedCost, cost);
  }

  @Test
  public void testGetCost_RootWithNegativeCostThreshold_ThrowsException() {
    try {
      InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(-1);
      estimator.add(Node.IDENT("root"));
    } catch (IllegalArgumentException e) {
      assertEquals("Cost threshold cannot be negative", e.getMessage());
      return;
    }
    fail("Expected IllegalArgumentException");
  }

  @Test
  public void testGetCost_RootWithNullNode_ThrowsException() {
    try {
      InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
      estimator.add(null);
    } catch (NullPointerException e) {
      return;
    }
    fail("Expected NullPointerException");
  }

  private Node createComplexExpression() {
    Node root = Node.BLOCK(null, Node.LIST(
        Node.IDENT("a"),
        Node.IDENT("b"),
        Node.IDENT("c"),
        Node.OPERATOR("+"),
        Node.IDENT("d"),
        Node.OPERATOR("-"),
        Node.IDENT("e"),
        Node.OPERATOR("*"),
        Node.NUMBER(2),
        Node.OPERATOR("/"),
        Node.BOOL(false),
        Node.IDENT("f"),
        Node.OPERATOR("="),
        Node.IDENT("g"),
        Node.IDENT("h"),
        Node.OPERATOR("?"),
        Node.IDENT("i"),
        Node.OPERATOR(":"),
        Node.IDENT("j"),
        Node.OPERATOR("?"),
        Node.IDENT("k"),
        Node.OPERATOR(":"),
        Node.IDENT("l")
    ));
    return root;
  }

  @Test
  public void testAddIdentifier_WithMaxCostReached_DoesNotAddMoreIdentifiers() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
    estimator.add(Node.IDENT("identifier1"));
    estimator.addIdentifier("identifier2");
    estimator.addIdentifier("identifier3");
    estimator.addIdentifier("identifier4");
    estimator.addIdentifier("identifier5");
    boolean isMaxCostReached = estimator.continueProcessing();
    assertFalse(isMaxCostReached);
  }

  @Test
  public void testAddIdentifier_WithMaxCostNotReached_ContinuesProcessing() {
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
    estimator.add(Node.IDENT("identifier1"));
    estimator.addIdentifier("identifier2");
    estimator.addIdentifier("identifier3");
    estimator.addIdentifier("identifier4");
    estimator.addIdentifier("identifier5");
    boolean isMaxCostReached = estimator.continueProcessing();
    assertTrue(isMaxCostReached);
  }
}