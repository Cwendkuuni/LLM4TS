import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForStatement(0, null, null, null, null, null, null);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpressionStatement(Node.newAdd(Node.newNumberLiteral(1), Node.newNumberLiteral(2)));
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"),
        null,
        Node.newBlock(Node.newReturnStatement(Node.newIdentifier("x"))));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunction(Node.newIdentifier("myFunction"), null, Node.newBlock(Node.newReturnStatement(Node.newIdentifier("x")))));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(Node.newFunction(
        Node.newIdentifier("myFunction"),
        null,
        Node.newBlock(Node.newReturnStatement(Node.newIdentifier("x")))));
    Node nodeHoistedFunctionDeclaration = nodeScript.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        Node.newIdentifier("outerFunction"),
        null,
        Node.newBlock(
            Node.newVariable("x", Node.newIdentifier("x")),
            Node.newFunction(
                Node.newIdentifier("myFunction"),
                null,
                Node.newBlock(Node.newReturnStatement(Node.newIdentifier("x"))))));
    Node nodeFunctionDeclaration = nodeFunctionDeclarationInFunction.getChild(1);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeHoistedFunctionDeclarationInFunction = nodeFunctionDeclarationInFunction.getChild(0);
    assertTrue(isFunctionDeclaration(nodeHoistedFunctionDeclarationInFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclarationInFunction));
  }
}