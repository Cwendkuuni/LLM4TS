import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newForInStatement(1, null, null, null, null, null, null);
    assertTrue(isForIn(forNode));

    Node notForInNode = Node.newExpressionStatement(Node.newCall(1, "alert", null, null));
    assertFalse(isForIn(notForInNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newCall(1, "alert", null, null));
    assertTrue(isStatement(statementNode));

    Node notStatementNode = Node.newIdentifier("x");
    assertFalse(isStatement(notStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newFunction(1, null, null, null, null, null, null);
    functionNode.setName("myFunction");
    assertTrue(isFunctionDeclaration(functionNode));

    Node notFunctionDeclarationNode = Node.newExpressionStatement(Node.newCall(1, "alert", null, null));
    assertFalse(isFunctionDeclaration(notFunctionDeclarationNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(1, null);
    Node functionDeclarationNode = Node.newFunction(1, null, null, null, null, null, null);
    functionDeclarationNode.setName("myFunction");
    scriptNode.addChild(functionDeclarationNode);
    Node functionExpressionNode = Node.newExpressionStatement(Node.newCall(1, "function", new int[]{1}, null, null, null, functionDeclarationNode));
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));
  }
}