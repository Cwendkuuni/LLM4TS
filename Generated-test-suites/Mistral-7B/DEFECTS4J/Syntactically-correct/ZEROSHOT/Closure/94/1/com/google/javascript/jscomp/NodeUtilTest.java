import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = Node.newNode(FOR, new int[]{}, new Object[]{}, null);
    assertTrue(NodeUtil.isForIn(n));

    n = Node.newNode(IF, new int[]{}, new Object[]{}, null);
    assertFalse(NodeUtil.isForIn(n));

    n = Node.newNode(FUNCTION, new int[]{}, new Object[]{}, null);
    assertFalse(NodeUtil.isForIn(n));

    n = Node.newNode(VAR, new int[]{}, new Object[]{"x"}, null);
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = Node.newNode(SCRIPT, new int[]{}, new Object[]{}, null);
    assertTrue(NodeUtil.isStatement(n));

    n = Node.newNode(BLOCK, new int[]{}, new Object[]{}, null);
    assertTrue(NodeUtil.isStatement(n));

    n = Node.newNode(LABEL, new int[]{}, new Object[]{"label"}, null);
    assertTrue(NodeUtil.isStatement(n));

    n = Node.newNode(IDENT, new int[]{}, new Object[]{"x"}, null);
    assertFalse(NodeUtil.isStatement(n));

    n = Node.newNode(ASSIGN, new int[]{}, new Object[]{IDENT, NUMBER}, null);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(FUNCTION, new int[]{}, new Object[]{"x"}, null);
    Node statementFunctionNode = Node.newNode(FUNCTION, new int[]{}, new Object[]{"x"}, Node.newNode(BLOCK, new int[]{}, new Object[]{}, functionNode));
    Node expressionFunctionNode = Node.newNode(FUNCTION, new int[]{}, new Object[]{"x"}, Node.newNode(ASSIGN, new int[]{}, new Object[]{IDENT, NUMBER}, functionNode));

    assertTrue(NodeUtil.isFunctionDeclaration(statementFunctionNode));
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    assertFalse(NodeUtil.isFunctionDeclaration(expressionFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = Node.newNode(FUNCTION, new int[]{}, new Object[]{"x"}, null);
    Node scriptNode = Node.newNode(SCRIPT, new int[]{}, new Object[]{}, functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode));

    Node functionDeclarationInBlockNode = Node.newNode(FUNCTION, new int[]{}, new Object[]{"x"}, Node.newNode(BLOCK, new int[]{}, new Object[]{}, functionNode));
    Node functionDeclarationInFunctionNode = Node.newNode(FUNCTION, new int[]{}, new Object[]{"x"}, Node.newNode(FUNCTION, new int[]{}, new Object[]{}, functionDeclarationInBlockNode));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInBlockNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
  }
}