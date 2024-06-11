import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, new int[0]);
    Node nodeFor = new Node(FOR, new int[0]);
    Node nodeIf = new Node(IF, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT, new int[0]);
    Node nodeBlock = new Node(BLOCK, new int[0]);
    Node nodeFunction = new Node(FUNCTION, new int[0]);
    Node nodeIf = new Node(IF, new int[0]);
    Node nodeExpression = new Node(IDENT, new int[0]);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, new int[0]);
    Node nodeFunctionExpression = new Node(IDENT, new int[0]);
    Node nodeIf = new Node(IF, new int[0]);
    Node nodeFunctionDeclarationInScript = new Node(FUNCTION, new int[0]);
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, new int[0]);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIf));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(FUNCTION, new int[0]);
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, new int[0]);
    Node nodeFunctionDeclarationInFunctionWithChild = new Node(FUNCTION, new int[0]);
    Node nodeFunctionDeclarationInFunctionWithChildAndSibling = new Node(FUNCTION, new int[0]);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionWithChild));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionWithChildAndSibling));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateWithNullParent() {
    Node node = new Node(IDENT, new int[0]);
    NodeUtil.isStatement(node);
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateWithNullChild() {
    Node nodeParent = new Node(SCRIPT, new int[0]);
    Node node = new Node(NULL, new int[0]);
    NodeUtil.isStatement(nodeParent);
    NodeUtil.isStatement(node);
  }
}