import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(FOR, null);
    Node threeChildrenForNode = Node.newNode(FOR, null, Node.newNode(IDENT, "for"),
        Node.newNode(ASSIGN, null, Node.newNode(IDENT, "i"), Node.newNode(NUM, 0)),
        Node.newNode(PUNCTUATION, null, Token.SEMICOLON));

    assertTrue(NodeUtil.isForIn(forNode));
    assertTrue(NodeUtil.isForIn(threeChildrenForNode));

    Node notForNode = Node.newNode(IDENT, "notFor");
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(SCRIPT, null);
    Node functionNode = Node.newNode(FUNCTION, null, Node.newNode(IDENT, "func"),
        Node.newNode(BLOCK, null));
    Node statementNode = Node.newNode(IDENT, "statement");
    Node expressionNode = Node.newNode(IDENT, "expression");

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(FUNCTION, null, Node.newNode(IDENT, "funcDecl"),
        Node.newNode(BLOCK, null));
    Node functionExpressionNode = Node.newNode(FUNCTION, null, null,
        Node.newNode(BLOCK, null));
    Node notFunctionNode = Node.newNode(IDENT, "notFunction");

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newNode(FUNCTION, null, Node.newNode(IDENT, "hoistedFuncDecl"),
        Node.newNode(BLOCK, null), Node.newNode(IDENT, "hoistedFuncDecl"));
    Node notHoistedFunctionDeclarationNode = Node.newNode(FUNCTION, null, Node.newNode(IDENT, "notHoistedFuncDecl"),
        Node.newNode(BLOCK, null), Node.newNode(IDENT, "notHoistedFuncDecl"));
    Node hoistedFunctionDeclarationInFunctionNode = Node.newNode(FUNCTION, null, Node.newNode(IDENT, "func"),
        Node.newNode(BLOCK, null, Node.newNode(FUNCTION, null, Node.newNode(IDENT, "hoistedFuncDecl"),
            Node.newNode(BLOCK, null))));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunctionNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateFailure() {
    Node node = Node.newNode(IDENT, "checkStateFailure");
    NodeUtil parentNode = Node.newNode(IDENT, "parentNode");
    Preconditions.checkState(parentNode != null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsForInWithNullNode() {
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsStatementWithNullNode() {
    Node node = Node.newNode(IDENT, "statementWithNullNode");
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclarationWithNullNode() {
    Node node = Node.newNode(IDENT, "functionDeclarationWithNullNode");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node node = Node.newNode(IDENT, "hoistedFunctionDeclarationWithNullNode");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}