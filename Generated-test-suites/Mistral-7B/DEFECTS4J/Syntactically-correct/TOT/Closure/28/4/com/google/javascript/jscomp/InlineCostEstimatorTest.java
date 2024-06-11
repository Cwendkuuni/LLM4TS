import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 0;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfSimpleDeclaration() {
    Node root = Node.newIdent("x");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = ESTIMATED_IDENTIFIER_COST;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfSimpleAssignement() {
    Node root = Node.newBinOp("=", Node.newIdent("x"), Node.newNum(5));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 2; // for "x" and "="
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    Node root = Node.newBinOp("+", Node.newBinOp("*", Node.newIdent("x"), Node.newNum(2)), Node.newBinOp("-", Node.newNum(3), Node.newNum(5)));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = ESTIMATED_IDENTIFIER_COST * 5 // for "x", "*", "-", "-", and "+"
                + 1 * 3 // for "+" and "=" in each multiplication and subtraction
                + 1; // for "=" at the end
    assertEquals(expectedCost, actualCost);
  }

  @Test(expected = AssertionError.class)
  public void testGetCostThreshholdExceeded() {
    Node root = Node.newBinOp("+", Node.newBinOp("*", Node.newIdent("x"), Node.newNum(2)), Node.newBinOp("-", Node.newNum(3), Node.newNum(5)));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(root);
  }
}