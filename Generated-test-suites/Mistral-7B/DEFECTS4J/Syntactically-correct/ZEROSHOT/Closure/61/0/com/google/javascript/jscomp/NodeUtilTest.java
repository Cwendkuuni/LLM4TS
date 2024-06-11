import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, null);
    Node forNodeWithThreeChildren = Node.newNode(Token.FOR, new Node[] {
        Node.newNode(Token.IDENTIFIER, ""),
        Node.newNode(Token.ASSIGN, null),
        Node.newNode(Token.IDENTIFIER, "")
    });

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newNode(Token.IF, null);
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null);
    Node blockNode = Node.newNode(Token.BLOCK, null);
    Node labelNode = Node.newNode(Token.LABEL, "label");
    Node statementNode = Node.newNode(Token.ASSIGN, null);
    Node expressionNode = Node.newNode(Token.IDENTIFIER, "");

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(blockNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newNode(Token.FUNCTION, new Node[] {
        Node.newNode(Token.IDENTIFIER, "functionName"),
        Node.newNode(Token.BLOCK, null)
    });
    Node functionExpressionNode = Node.newNode(Token.FUNCTION, new Node[] {
        Node.newNode(Token.IDENTIFIER, "functionName"),
        Node.newNode(Token.BLOCK, null),
        Node.newNode(Token.RETURN, null),
        Node.newNode(Token.IDENTIFIER, "someVariable")
    });

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclarationInScript = Node.newNode(Token.FUNCTION, new Node[] {
        Node.newNode(Token.IDENTIFIER, "functionName"),
        Node.newNode(Token.BLOCK, null)
    });
    Node functionDeclarationInFunction = Node.newNode(Node.newNode(Token.FUNCTION, new Node[] {
        Node.newNode(Token.IDENTIFIER, "functionNameInFunction"),
        Node.newNode(Token.BLOCK, null)
    }), NodeUtil.newList(functionDeclarationInScript));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));

    Node functionExpressionInFunction = Node.newNode(Node.newNode(Token.FUNCTION, new Node[] {
        Node.newNode(Token.IDENTIFIER, "functionNameInFunction"),
        Node.newNode(Token.BLOCK, null)
    }), NodeUtil.newList(functionExpressionNode));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionInFunction));
  }

  private static NodeUtil.NodeList newList(Node... nodes) {
    return new NodeUtil.NodeList(nodes);
  }
}