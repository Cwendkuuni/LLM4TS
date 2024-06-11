import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{0, 1, 2});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{0, 1, 2});
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.SCRIPT, new int[]{0});
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.ADD, new int[]{0, 1, 2});
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 1}, null, null);
    nodeFunctionDeclaration.setProperty("id", "testFunction");

    Node parentScript = new Node(Token.SCRIPT, new int[]{0});
    parentScript.addChildToEnd(nodeFunctionDeclaration);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{0, 1}, null, null);
    parentScript.addChildToEnd(nodeFunctionExpression);

    Node statement = new Node(Token.ASSIGN, new int[]{0, 1, 2});
    parentScript.addChildToEnd(statement);

    nodeFunctionExpression.setProperty("id", "testFunctionExpression");
    statement.setProperty("value", nodeFunctionExpression);

    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{0});

    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{0, 1}, null, null);
    nodeFunctionDeclaration.setProperty("id", "testFunction");
    nodeScript.addChildToEnd(nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{0, 1}, null, null);
    nodeFunctionDeclarationInFunction.setProperty("id", "testFunctionInFunction");

    Node functionNode = new Node(Token.FUNCTION, new int[]{0, 1}, null, null);
    functionNode.addChildToEnd(nodeFunctionDeclarationInFunction);
    nodeScript.addChildToEnd(functionNode);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}