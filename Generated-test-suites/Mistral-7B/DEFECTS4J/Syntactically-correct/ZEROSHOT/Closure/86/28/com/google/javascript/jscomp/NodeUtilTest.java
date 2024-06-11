import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(FOR, null, null, null, 0, 0, 0, null);
    Node simpleNode = new Node(IDENT, "simple", null, null, 0, 0, 0, null);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(simpleNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(SCRIPT, null, null, null, 0, 0, 0, null);
    Node blockNode = new Node(BLOCK, null, null, null, 0, 0, 0, null);
    Node labelNode = new Node(LABEL, "label", null, null, 0, 0, 0, null);
    Node statementsNode = new Node(BLOCK, null, null, null, 0, 0, 0, null);
    Node functionNode = new Node(FUNCTION, null, null, null, 0, 0, 0, null);
    Node expressionNode = new Node(IDENT, "expression", null, null, 0, 0, 0, null);

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertTrue(NodeUtil.isStatement(labelNode));
    assertTrue(NodeUtil.isStatement(statementsNode));

    assertFalse(NodeUtil.isStatement(functionNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(FUNCTION, "function", null, null, 1, 0, 0, null);
    Node functionExpressionNode = new Node(FUNCTION, null, null, null, 0, 0, 0, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(FUNCTION, "hoistedFunction", null, null, 1, 0, 0, null);
    Node scriptNode = new Node(SCRIPT, null, null, null, 0, 0, 0, null);
    Node functionNode = new Node(FUNCTION, "function", null, null, 1, 0, 0, null);
    Node rootFunctionNode = new Node(FUNCTION, null, null, null, 0, 0, 0, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(rootFunctionNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckStateWithNullParent() {
    Node node = new Node(IDENT, "node", null, null, 0, 0, 0, null);
    Preconditions.checkState(node.getParent() != null);
  }

  @Test
  public void testCheckStateWithNonStatementParent() {
    Node node = new Node(IDENT, "node", null, null, 0, 0, 0, null);
    Node nonStatementParent = new Node(EXPR_STATEMENT, null, null, null, 0, 0, 0, null);

    try {
      Preconditions.checkState(NodeUtil.isStatement(node));
      fail("Expected IllegalStateException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}