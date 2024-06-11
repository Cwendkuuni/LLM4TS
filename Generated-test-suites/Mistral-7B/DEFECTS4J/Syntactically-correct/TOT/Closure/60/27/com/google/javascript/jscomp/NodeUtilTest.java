import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newForInitStatement(1, 1, null, null, Node.newIdentifier("i"), Node.newNumberLiteral(0), Node.newStatementList(), Node.newBlockStatement(Node.newStatementList()));
    assertTrue(isForIn(forNode));

    Node notForNode = Node.newExpressionStatement(Node.newIdentifier("x").setProperty("foo", Node.newNumberLiteral(42)));
    assertFalse(isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newIdentifier("x").setProperty("foo", Node.newNumberLiteral(42)));
    assertTrue(isStatement(statementNode));

    Node notStatementNode = Node.newIdentifier("x");
    assertFalse(isStatement(notStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunction(Token.VAR, "myFunction", new Node[0], Node.newBlockStatement(Node.newStatementList()));
    assertTrue(isFunctionDeclaration(functionDeclaration));

    Node functionExpression = Node.newExpressionStatement(Node.newIdentifier("myFunction").setInitialValue(Node.newFunction(Token.FUNCTION, null, new Node[0], Node.newBlockStatement(Node.newStatementList()))));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Node.newFunction(Token.VAR, "myFunction", new Node[0], Node.newBlockStatement(Node.newStatementList()));
    Node functionDeclarationInBlock = Node.newFunction(Token.VAR, "myFunction", new Node[0], Node.newBlockStatement(Node.newStatementList())).setParent(Node.newBlockStatement(Node.newStatementList()));
    Node functionDeclarationInFunction = Node.newFunction(Token.VAR, "outerFunction", new Node[0], Node.newBlockStatement(Node.newStatementList())).setChild(hoistedFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInBlock));
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }
}