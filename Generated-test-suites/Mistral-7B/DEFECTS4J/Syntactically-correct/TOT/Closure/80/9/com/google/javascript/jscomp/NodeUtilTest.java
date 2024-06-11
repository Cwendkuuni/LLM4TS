import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, 2, 3, Node.newIdentifier("i"), Node.newExpressionStatement(Node.newExpression(1)));
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newAssignment(Node.newVariable("x"), Node.newNumber(1)));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newExpression(1));
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, "myFunc", new Node[0], Node.newBlock(Node.newReturn(Node.newIdentifier("x"))));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newAssignment(Node.newVariable("myFunc"), Node.newFunction(1, null, new Node[0], Node.newBlock(Node.newReturn(Node.newIdentifier("x"))))));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(1, "myFunc", new Node[0], Node.newBlock(Node.newReturn(Node.newIdentifier("x"))));
    Node scriptNode = Node.newScript(new Node[0]);
    scriptNode.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(1, "myFunc", new Node[0], Node.newBlock(Node.newReturn(Node.newIdentifier("x"))));
    Node nodeFunctionStatement = Node.newExpressionStatement(Node.newAssignment(Node.newVariable("x"), Node.newNumber(1)));
    Node blockNode = Node.newBlock(nodeFunctionStatement, nodeHoistedFunctionDeclaration);
    assertTrue(isFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
  }
}