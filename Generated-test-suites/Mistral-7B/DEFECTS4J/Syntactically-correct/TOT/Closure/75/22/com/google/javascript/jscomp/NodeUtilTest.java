import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node createForInNode() {
    Node script = new Node(Token.SCRIPT);
    Node forToken = new Node(Token.FOR);
    Node init = new Node(Token.IDENT, "i");
    Node condition = new Node(Token.NUMBER, 0);
    Node increment = new Node(Token.IDENT, "i");
    Node incrementValue = new Node(Token.PLUS, increment, new Node(Token.NUMBER, 1));
    Node loopBody = new Node(Token.BLOCK);
    Node statement = new Node(Token.IDENT, "console.log", new Node(Token.STRING, "i"));
    loopBody.addChild(statement);
    forToken.addChild(init);
    forToken.addChild(condition);
    forToken.addChild(increment);
    forToken.addChild(incrementValue);
    forToken.addChild(loopBody);
    script.addChild(forToken);
    return forToken;
  }

  Node createStatementNode() {
    Node script = new Node(Token.SCRIPT);
    Node printStatement = new Node(Token.PRINT, new Node(Token.STRING, "Hello, World!"));
    script.addChild(printStatement);
    return printStatement;
  }

  Node createFunctionDeclarationNode() {
    Node script = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, "myFunction");
    functionNode.addChild(new Node(Token.BLOCK,new Node(Token.RETURN, new Node(Token.NUMBER, 42))));
    script.addChild(functionNode);
    return functionNode;
  }

  Node createHoistedFunctionDeclarationNode() {
    Node script = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, "myFunction");
    functionNode.addChild(new Node(Token.IDENT, "myFunction")); // Hoist the function declaration
    functionNode.addChild(new Node(Token.BLOCK,new Node(Token.RETURN, new Node(Token.NUMBER, 42))));
    script.addChild(functionNode);
    Node functionCallNode = new Node(Token.IDENT, "myFunction");
    script.addChild(functionCallNode);
    return functionNode;
  }

  @Test
  public void testIsForIn() {
    Node forInNode = createForInNode();
    assertTrue(isForIn(forInNode));
  }

  @Test
  public void testIsNotForIn() {
    Node statementNode = createStatementNode();
    assertFalse(isForIn(statementNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createStatementNode();
    assertTrue(isStatement(statementNode));
  }

  @Test
  public void testIsNotStatement() {
    Node expressionNode = new Node(Token.PLUS, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2));
    assertFalse(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createFunctionDeclarationNode();
    assertTrue(isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node statementNode = createStatementNode();
    assertFalse(isFunctionDeclaration(statementNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = createHoistedFunctionDeclarationNode();
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionDeclarationNode = createFunctionDeclarationNode();
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));
  }
}