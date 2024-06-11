import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TopLevelCodeTestBase;

public class NodeUtilTest extends TopLevelCodeTestBase {

  public void testIsForIn() {
    Node node = new Node(Token.FOR, new int[] {0, 1, 2});
    assertTrue(isForIn(node));

    Node invalidNode = new Node(Token.IF, new int[] {0, 1, 2});
    assertFalse(isForIn(invalidNode));
  }

  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[] {0, 1});
    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.ADD, new int[] {0, 1, 2});
    assertFalse(isStatement(expressionNode));
  }

  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[] {0, 1}, null);
    functionDeclarationNode.setProperty("name", "testFunction");
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new int[] {0, 1}, null);
    functionExpressionNode.setProperty("name", null);
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[] {0, 1}, null);
    hoistedFunctionDeclarationNode.setProperty("name", "testFunction");
    Node scriptNode = new Node(Token.SCRIPT, new int[0]);
    scriptNode.setProperty("body", hoistedFunctionDeclarationNode);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node functionNodeInFunctionNode = new Node(Token.FUNCTION, new int[] {0, 1}, null);
    functionNodeInFunctionNode.setProperty("name", "testFunction");
    Node functionNode = new Node(Token.FUNCTION, new int[] {0, 1}, null);
    functionNode.setProperty("body", functionNodeInFunctionNode);
    Node scriptNodeInFunctionNode = new Node(Token.SCRIPT, new int[0]);
    scriptNodeInFunctionNode.setProperty("body", functionNode);
    assertFalse(isHoistedFunctionDeclaration(functionNodeInFunctionNode));
  }
}