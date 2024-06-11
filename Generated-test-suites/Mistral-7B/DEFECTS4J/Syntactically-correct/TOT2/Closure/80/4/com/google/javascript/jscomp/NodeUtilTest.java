import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NodeUtilTest {

  @Test
  void testNullNode() {
    Node nullNode = null;
    assertDoesNotThrow(() -> NodeUtil.isForIn(nullNode));
    assertDoesNotThrow(() -> NodeUtil.isStatement(nullNode));
    assertDoesNotThrow(() -> NodeUtil.isFunctionDeclaration(nullNode));
    assertDoesNotThrow(() -> NodeUtil.isHoistedFunctionDeclaration(nullNode));
  }

  @Test
  void testEmptyNode() {
    Node emptyNode = new Node(Token.EMPTY, new int[0]);
    assertFalse(NodeUtil.isForIn(emptyNode));
    assertFalse(NodeUtil.isStatement(emptyNode));
    assertFalse(NodeUtil.isFunctionDeclaration(emptyNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(emptyNode));
  }

  @Test
  void testCase1() {
    Node nodeForIn = new Node(Token.FOR, new int[0]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  void testCase2() {
    Node statementNode = new Node(Token.IF, new int[0]);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(Token.BINARY, new int[0], new Node[0]);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  void testCase3() {
    Node functionDeclaration = new Node(Token.FUNCTION, new int[0], new Node[0]);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = new Node(Token.FUNCTION, new int[0], new Node[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  void testCase4() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[0], new Node[0]);
    Node parentOfHoistedFunctionDeclaration = new Node(Token.SCRIPT, new int[0], new Node[0]);
    Node grandparentOfHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[0], new Node[0]);
    Node functionNodeInsideFunction = new Node(Token.FUNCTION, new int[0], new Node[0]);

    Node[] childrenOfFunctionNodeInsideFunction = new Node[1];
    childrenOfFunctionNodeInsideFunction[0] = functionNodeInsideFunction;
    Node parentOfFunctionNodeInsideFunction = new Node(Token.SCRIPT, new int[0], childrenOfFunctionNodeInsideFunction);

    Node[] childrenOfHoistedFunctionDeclaration = new Node[0];
    Node[] childrenOfParent = { hoistedFunctionDeclaration };
    Node[] childrenOfGrandparent = new Node[1];
    childrenOfGrandparent[0] = parentOfHoistedFunctionDeclaration;

    Node[] childrenOfScript = { hoistedFunctionDeclaration, parentOfFunctionNodeInsideFunction };

    hoistedFunctionDeclaration.setParent(grandparentOfHoistedFunctionDeclaration);
    grandparentOfHoistedFunctionDeclaration.setChildren(childrenOfGrandparent);
    parentOfHoistedFunctionDeclaration.setParent(parentOfScript);
    parentOfScript.setChildren(childrenOfScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideFunction));
  }
}