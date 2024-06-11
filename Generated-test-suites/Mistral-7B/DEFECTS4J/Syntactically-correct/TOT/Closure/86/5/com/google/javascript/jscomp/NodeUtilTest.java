import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TopLevelTestBase;

public class NodeUtilTest extends TopLevelTestBase {

  @org.junit.Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new int[]{1, 2, 3});
    assertTrue(isForIn(node));

    Node invalidNode = new Node(Token.IF, new int[]{1, 2});
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[]{1, 2});
    statementNode.setParent(new Node(Token.SCRIPT, new int[]{0}));
    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.IF, new int[]{1, 2});
    Node functionNode = new Node(Token.FUNCTION, new int[]{1, 2});
    Node blockNode = new Node(Token.BLOCK, new int[]{1, 2});
    Node labelNode = new Node(Token.LABEL, new int[]{1, 2});
    Node scriptNode = new Node(Token.SCRIPT, new int[]{0});

    assertFalse(isStatement(expressionNode));
    assertFalse(isStatement(functionNode));
    assertFalse(isStatement(blockNode));
    assertFalse(isStatement(labelNode));

    Node invalidStatementNode = new Node(Token.WHILE, new int[]{1, 2});
    assertFalse(isStatement(invalidStatementNode));
    invalidStatementNode.setParent(scriptNode);
    assertFalse(isStatement(invalidStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{1, 2, 3});
    functionDeclarationNode.setName(new Node("myFunction", new int[]{1}));
    functionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{0}));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{1, 2, 3});
    functionExpressionNode.setParent(new Node(Token.ASSIGN, new int[]{1, 2, 3}));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{1, 2, 3});
    hoistedFunctionDeclarationNode.setName(new Node("myFunction", new int[]{1}));
    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{0}));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{1, 2, 3});
    notHoistedFunctionDeclarationNode.setName(new Node("myFunction", new int[]{1}));
    Node functionDeclarationParentNode = new Node(Token.BLOCK, new int[]{1, 2});
    notHoistedFunctionDeclarationNode.setParent(functionDeclarationParentNode);
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));

    Node hoistedFunctionDeclarationInFunctionNode = new Node(Token.FUNCTION, new int[]{1, 2, 3});
    hoistedFunctionDeclarationInFunctionNode.setName(new Node("myFunction", new int[]{1}));
    Node functionNode = new Node(Token.FUNCTION, new int[]{1, 2});
    hoistedFunctionDeclarationInFunctionNode.setParent(functionNode);
    Node functionParentNode = new Node(Token.SCRIPT, new int[]{0});
    functionNode.setParent(functionParentNode);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunctionNode));
  }
}