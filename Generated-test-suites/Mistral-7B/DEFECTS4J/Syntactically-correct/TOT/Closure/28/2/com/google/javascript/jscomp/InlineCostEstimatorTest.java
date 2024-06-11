import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyFile() {
    Node root = Node.EMPTY_NODE;
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleVariableDeclaration() {
    String nodeString = "var x = 5;";
    Node root = Parser.parse(nodeString);
    int cost = getCost(root);
    assertEquals(ESTIMATED_IDENTIFIER_COST * 2 + nodeString.length(), cost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    String nodeString = "(a + b) * (c > d ? e : f);";
    Node root = Parser.parse(nodeString);
    int cost = getCost(root);
    assertEquals(ESTIMATED_IDENTIFIER_COST * 10 + nodeString.length(), cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNullNode() {
    Node root = null;
    getCost(root);
  }

  @Test
  public void testGetCostWithLargeNode() {
    String largeNodeString = "for (var i = 0; i < 100000; i++) { var j = i * i; }";
    Node root = Parser.parse(largeNodeString);
    int cost = getCost(root, 1000);
    assertTrue(cost > 19000); // Simple identifier + length of the large node string
  }

  @Test
  public void testGetCostWithLargeNodeWithThreshold() {
    String largeNodeString = "for (var i = 0; i < 100000; i++) { var j = i * i; }";
    Node root = Parser.parse(largeNodeString);
    int cost = getCost(root, 500);
    assertEquals(500, cost);
  }

  @Test
  public void testCompiledSizeEstimatorBehavior() {
    String nodeString = "var x = 5; var y = 10; var z = x + y;";
    Node root = Parser.parse(nodeString);
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.add(root);
    assertEquals(3 * ESTIMATED_IDENTIFIER_COST + nodeString.length(), estimator.getCost());
  }
}