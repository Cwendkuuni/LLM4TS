import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.newObject(); // empty object
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    Node simpleNode = Node.newLiteral(1);
    cost = InlineCostEstimator.getCost(simpleNode);
    assertEquals(1, cost);

    Node complexNode = Node.newObject(); // complex object with multiple properties
    complexNode.putProperty("property1", Node.newLiteral(1));
    complexNode.putProperty("property2", Node.newLiteral(2));
    cost = InlineCostEstimator.getCost(complexNode);
    assertEquals(ESTIMATED_IDENTIFIER_COST * 4, cost); // assuming 2 properties per identifier

    Node largerNode = Node.newObject(); // larger object with many properties
    for (int i = 0; i < 100; i++) {
      largerNode.putProperty("property" + i, Node.newLiteral(i));
    }
    cost = InlineCostEstimator.getCost(largerNode);
    assertEquals(ESTIMATED_IDENTIFIER_COST * 102, cost); // assuming 101 properties per identifier plus the initial 'largerNode'

    Node arrayNode = Node.newArray(); // empty array
    cost = InlineCostEstimator.getCost(arrayNode);
    assertEquals(0, cost);

    Node arrayWithElementsNode = Node.newArray(); // array with 5 elements
    arrayWithElementsNode.setElement(0, Node.newLiteral(1));
    arrayWithElementsNode.setElement(1, Node.newLiteral(2));
    arrayWithElementsNode.setElement(2, Node.newLiteral(3));
    arrayWithElementsNode.setElement(3, Node.newLiteral(4));
    arrayWithElementsNode.setElement(4, Node.newLiteral(5));
    cost = InlineCostEstimator.getCost(arrayWithElementsNode);
    assertEquals(ESTIMATED_IDENTIFIER_COST * 6, cost); // assuming 1 identifier per element plus the initial 'arrayWithElementsNode'

    Node functionNode = Node.newFunction(null, null); // empty function
    cost = InlineCostEstimator.getCost(functionNode);
    assertEquals(0, cost);

    Node functionWithBodyNode = Node.newFunction(null, null); // function with a single statement return 1
    functionWithBodyNode.setBody(Node.newReturn(Node.newLiteral(1)));
    cost = InlineCostEstimator.getCost(functionWithBodyNode);
    assertEquals(ESTIMATED_IDENTIFIER_COST * 2, cost); // assuming 1 identifier for the function plus 1 for the return statement

    Node functionWithMultipleStatementsNode = Node.newFunction(null, null); // function with multiple statements
    Node body = Node.newBlock();
    body.setLocal("x", Node.newLiteral(1));
    body.setLocal("y", Node.newLiteral(2));
    body.setLocal("sum", Node.newOperator("+", Node.get("x"), Node.get("y")));
    functionWithMultipleStatementsNode.setBody(body);
    cost = InlineCostEstimator.getCost(functionWithMultipleStatementsNode);
    assertEquals(ESTIMATED_IDENTIFIER_COST * 5, cost); // assuming 1 identifier for the function plus 1 for each local variable and 1 for the sum variable
  }
}