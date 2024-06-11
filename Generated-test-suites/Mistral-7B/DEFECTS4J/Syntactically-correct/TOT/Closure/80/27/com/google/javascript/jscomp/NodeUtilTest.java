import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, "var", "i", null, Node.newBlock(2, Node.newEmptyStatement(3), null), Node.newEmptyStatement(4), null);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newAssign(1, Node.newIdentifier("x"), Node.newNumberLiteral(2.0)), null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newAssign(1, Node.newIdentifier("x"), Node.newNumberLiteral(2.0)), null);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        1,
        "foo",
        Node.EMPTY_NODE_ARRAY,
        Node.newBlock(2, Node.newExpressionStatement(Node.newAssign(1, Node.newIdentifier("x"), Node.newNumberLiteral(2.0)), null), null),
        Node.newBlock(3, Node.newReturnStatement(Node.newNumberLiteral(3.0), null), null));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newAssign(1, Node.newIdentifier("x"), Node.newFunction(3, null, Node.newBlock(4, Node.newReturnStatement(Node.newNumberLiteral(4.0), null), null), null)), null);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        1,
        "foo",
        Node.EMPTY_NODE_ARRAY,
        Node.newBlock(2, Node.newExpressionStatement(Node.newAssign(1, Node.newIdentifier("x"), Node.newNumberLiteral(2.0)), null), null),
        Node.newBlock(3, Node.newReturnStatement(Node.newNumberLiteral(3.0), null), null)
    );
    Node parentOfHoistedFunctionDeclaration = Node.newBlock(1, Node.newVariable("x", Node.newName("x"), Node.newUndefined(), Node.newFunction(5, Node.EMPTY_NODE_ARRAY, Node.newBlock(6, null, null), null), null), null);
    Node grandparentOfHoistedFunctionDeclaration = Node.newScript(1, Node.newBlock(4, Node.newVariable("y", Node.newName("y"), Node.newUndefined(), null, null), null), Node.newList(parentOfHoistedFunctionDeclaration, nodeHoistedFunctionDeclaration, Node.newStatementList()), null);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(
        1,
        "foo",
        Node.EMPTY_NODE_ARRAY,
        Node.newBlock(2, Node.newExpressionStatement(Node.newAssign(1, Node.newIdentifier("x"), Node.newNumberLiteral(2.0)), null), null),
        Node.newBlock(3, Node.newReturnStatement(Node.newNumberLiteral(3.0), null), null)
    );
    Node parentOfNotHoistedFunctionDeclaration = Node.newVariable("x", Node.newName("x"), Node.newUndefined(), null, null);
    Node grandparentOfNotHoistedFunctionDeclaration = Node.newScript(1, Node.newBlock(4, Node.newVariable("y", Node.newName("y"), Node.newUndefined(), null, null), null), Node.newList(parentOfNotHoistedFunctionDeclaration, nodeNotHoistedFunctionDeclaration, Node.newStatementList()), null);

    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}