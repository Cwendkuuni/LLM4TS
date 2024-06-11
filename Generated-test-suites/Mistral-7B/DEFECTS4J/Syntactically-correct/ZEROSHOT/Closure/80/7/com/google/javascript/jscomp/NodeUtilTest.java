import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, new int[]{}, new int[3]);
    Node nodeNotFor = new Node(Token.IF, new int[]{}, new int[1]);

    assertTrue(isForIn(nodeFor));
    assertFalse(isForIn(nodeNotFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    Node nodeStatement = new Node(Token.IDENT, new int[]{}, new int[1]);
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[1]);
    Node nodeExpression = new Node(Token.IF, new int[]{}, new int[1]);

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeFunction));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[2]);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[1]);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{"name"}, new int[2]);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[1]);
    Node nodeFunctionDeclarationInBlock = new Node(Token.BLOCK, new int[]{}, new int[1]);
    Node nodeFunctionDeclarationInLabel = new Node(Token.LABEL, new int[]{Token.ID, "label"}, new int[1]);

    Node nodeFunctionDeclarationInScriptParent = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    Node nodeFunctionDeclarationInFunctionParent = new Node(Token.FUNCTION, new int[]{}, new int[1]);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInLabel));

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScriptParent));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionParent));
  }

  @Test(expected = AssertionError.class)
  public void testCheckState失败() {
    Node parent = null;
    Node n = new Node(Token.FUNCTION, new int[]{}, new int[1]);
    Preconditions.checkState(parent != null);
  }
}