import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENTIFIER, "i"));
    forNode.setChild(1, new Node(Token.KEYWORD, "in"));
    forNode.setChild(2, new Node(Token.IDENTIFIER, "arr"));

    assertTrue(isForIn(forNode));

    Node invalidForNode = new Node(Token.FOR);
    invalidForNode.setChild(0, new Node(Token.IDENTIFIER, "i"));

    assertFalse(isForIn(invalidForNode));

    Node ifNode = new Node(Token.IF);
    assertFalse(isForIn(ifNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF);
    statementNode.setParent(new Node(Token.BLOCK));

    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.IF);
    Node parent = new Node(Token.ASSIGN);
    parent.setLeft(new Node(Token.IDENTIFIER, "x"));
    parent.setRight(expressionNode);

    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "foo"), new Node(Token.BLOCK));
    Node parent = new Node(Token.SCRIPT);
    parent.setChild(0, functionNode);

    assertTrue(isFunctionDeclaration(functionNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "foo"));
    Node context = new Node(Token.SCRIPT);
    context.setChild(0, functionExpressionNode);

    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "foo"), new Node(Token.BLOCK));
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.setChild(0, functionNode);

    Node functionDeclarationInFunctionNode = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "bar"), new Node(Token.BLOCK));
    Node functionNodeInFunction = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "func"), new Node(Token.BLOCK).addChild(functionDeclarationInFunctionNode));
    Node functionInScriptNode = new Node(Token.SCRIPT).addChild(functionNodeInFunction);

    assertTrue(isHoistedFunctionDeclaration(functionNode));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunctionNode));
  }
}