import static org.junit.Assert.assertEquals;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    Node forNodeWithThreeChildren = Node.newNode(Token.FOR, Node.newNode(Token.IDENTIFIER, "for"), Node.newNode(Token.ASSIGN, Token.VAR, "i"), Node.newNode(Token.SEMICOLON));
    Node invalidForNode = Node.newNode(Token.IF, Node.newNode(Token.IDENTIFIER, "condition"));

    assertEquals(true, NodeUtil.isForIn(forNodeWithThreeChildren));
    assertEquals(false, NodeUtil.isForIn(forNode));
    Preconditions.checkState(NodeUtil.isForIn(invalidForNode) == false);
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.ASSIGN, Token.IDENTIFIER, "x");
    Node expressionNode = Node.newNode(Token.IDENTIFIER, "x");
    Node functionNode = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result"))));
    Node invalidExpressionNode = Node.newNode(Token.IF, Node.newNode(Token.IDENTIFIER, "condition"));

    assertEquals(true, NodeUtil.isStatement(statementNode));
    assertEquals(false, NodeUtil.isStatement(expressionNode));
    assertEquals(false, NodeUtil.isStatement(functionNode));
    Preconditions.checkState(NodeUtil.isStatement(invalidExpressionNode) == false);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNodeWithName = Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result"))));
    Node functionNodeWithoutName = Node.newNode(Token.FUNCTION, Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result"))));
    Node functionExpressionNode = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "x"), Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result")))));

    assertEquals(true, NodeUtil.isFunctionDeclaration(functionNodeWithName));
    assertEquals(false, NodeUtil.isFunctionDeclaration(functionNodeWithoutName));
    Preconditions.checkState(NodeUtil.isFunctionDeclaration(functionExpressionNode) == false);
  }

  @Test
  public void testIsFunctionDeclarationInExpression() {
    Node expression = Node.newNode(Token.ASSIGN, Node.newNode(Token.IDENTIFIER, "x"), Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result")))));

    Preconditions.checkState(NodeUtil.isFunctionDeclaration(expression.getFirstChild()) == false);
  }

  @Test
  public void testIsFunctionDeclarationNotInRootScope() {
    Node scriptNode = Node.newNode(Token.SCRIPT, Node.newNode(Token.BLOCK, Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result"))))));
    Node functionNode = Node.newNode(Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result")))), Node.newNode(Token.IDENTIFIER, "myFunction"));

    Preconditions.checkState(NodeUtil.isFunctionDeclaration(scriptNode.getFirstChild()) == true);
    Preconditions.checkState(NodeUtil.isFunctionDeclaration(functionNode) == false);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, Node.newNode(Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result")))), Node.newNode(Token.IDENTIFIER, "myFunction")));
    Node hoistedFunctionDeclaration = Node.newNode(Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result")))), Node.newNode(Token.IDENTIFIER, "myFunction"));
    Node functionDeclaration = Node.newNode(Node.newNode(Token.FUNCTION, Node.newNode(Token.IDENTIFIER, "myFunction"), Node.newNode(Token.BLOCK, Node.newNode(Token.RETURN, Node.newNode(Token.IDENTIFIER, "result")))), Node.newNode(Token.IDENTIFIER, "myFunction2"));

    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    Preconditions.checkState(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration) == false);
  }

  @Test
  public void testIsHoistedFunctionDeclarationNotFunctionDeclaration() {
    Node hoistedVariableDeclaration = Node.newNode(Node.newNode(Token.VAR, Node.newNode(Token.IDENTIFIER, "myFunction")), Node.newNode(Token.IDENTIFIER, "myFunction"));

    Preconditions.checkState(NodeUtil.isHoistedFunctionDeclaration(hoistedVariableDeclaration) == false);
  }
}