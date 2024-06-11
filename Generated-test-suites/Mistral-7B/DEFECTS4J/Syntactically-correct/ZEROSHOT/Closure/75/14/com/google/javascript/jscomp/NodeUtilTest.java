import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  private Node createNode(int type, int... children) {
    Node node = new Node();
    node.setType(type);
    node.setLine(1);
    node.setColumn(1);

    if (children != null) {
      for (int child : children) {
        Node childNode = new Node();
        childNode.setType(child);
        node.appendChild(childNode);
      }
    }

    return node;
  }

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, 1, 2, 3);
    Node ifNode = createNode(IF, 1, 2, 3);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(IF, 1, 2, 3);
    Node expressionNode = createNode(ADD, 1, 2, 3);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode(FUNCTION, IDENT, 1, LBRACE, 1, 2, RBRACE);
    Node functionExpression = createNode(FUNCTION, LPAR, IDENT, RPAR, 1, 2, 3);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(SCRIPT, 1, 2, 3, FUNCTION, IDENT, 1, LBRACE, 1, 2, RBRACE);
    Node functionDeclaration = createNode(FUNCTION, IDENT, 1, LBRACE, 1, 2, RBRACE);
    Node functionExpression = createNode(FUNCTION, LPAR, IDENT, RPAR, 1, 2, 3);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpression));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getChild(0)));
  }
}