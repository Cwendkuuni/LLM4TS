import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, null, createNode(IDENT, "for"), null,
        createNode(ASSIGN, null, createNode(IDENT, "i"), null, null), null,
        createNode(BLOCK, null, null, null, null), null);
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = createNode(FUNCTION, null, createNode(IDENT, "f"),
        createNode(BLOCK, null, null, null, null), null);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(ASSIGN, null, createNode(IDENT, "x"),
        null, createNode(INT_LITERAL, 42));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node notStatementNode = createNode(IDENT, "x");
    assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(FUNCTION, null, createNode(IDENT, "f"),
        createNode(BLOCK, null, null, null, null), null);
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node notFunctionNode = createNode(IDENT, "x");
    assertFalse(NodeUtil.isFunctionDeclaration(notFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = createNode(SCRIPT, null,
        createNode(FUNCTION, null, createNode(IDENT, "g"),
            createNode(BLOCK, null, null, null, null), null), null);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode.getFirstChild()));

    Node notHoistedFunctionNode = createNode(FUNCTION, null, createNode(IDENT, "f"),
        createNode(BLOCK, null, null, null, null), null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
  }
}