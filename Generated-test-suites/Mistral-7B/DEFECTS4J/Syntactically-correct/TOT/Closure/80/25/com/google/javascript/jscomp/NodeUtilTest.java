import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.testing.TopLevelTestBase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest extends TopLevelTestBase {

  private Node createForInNode() {
    Node forToken = node("for", new Object[]{"for statement", null}, null);
    Node init = node("var", new Object[]{"VariableStatement", Lists.newArrayList(node("i", null, null))}, forToken);
    Node condition = node("in", new Object[]{"ExpressionStatement", node("i", null, null), node(Token.KEYWORD, "in", null)}, forToken);
    Node update = node(";", null, forToken);
    Node body = node("{", null, forToken);
    Node statement = node("print(i)", null, null);
    Node statementBlock = node("{", null, body);
    body.addChildToEnd(statementBlock);
    body.addChildToEnd(node(";", null, null));
    return forToken;
  }

  private Node createStatementNode() {
    Node printStatement = node("print", new Object[]{"ExpressionStatement", node("42", null, null)}, null);
    Node script = node("{", null, null);
    script.addChildToEnd(printStatement);
    script.addChildToEnd(node(";", null, null));
    return script;
  }

  private Node createFunctionDeclarationNode() {
    Node functionNode = node("function", new Object[]{"FunctionDeclaration", "name", node("myFunction", null, null)}, null);
    Node body = node("{", null, functionNode);
    Node statement = node("print(arguments[0]);", null, null);
    Node statementBlock = node("{", null, body);
    body.addChildToEnd(statementBlock);
    body.addChildToEnd(node("return 42;", null, null));
    return functionNode;
  }

  private Node createHoistedFunctionDeclarationNode() {
    Node script = node("function myFunction() {", null, null);
    Node body = node("{", null, script);
    Node statement = node("print(arguments[0]);", null, null);
    Node statementBlock = node("{", null, body);
    body.addChildToEnd(statementBlock);
    body.addChildToEnd(node("return 42;", null, null));
    Node functionDeclaration = node("myFunction", null, null);
    functionDeclaration.setParent(node("Script", null, null));
    functionDeclaration.setLineNumber(1);
    script.setParent(functionDeclaration);
    return functionDeclaration;
  }

  @Test
  public void testIsForIn() {
    Node node = createForInNode();
    assertTrue(isForIn(node));
  }

  @Test
  public void testIsForInFalse() {
    Node node = createStatementNode();
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createStatementNode();
    assertTrue(isStatement(node));
  }

  @Test
  public void testIsStatementFalse() {
    Node node = node("42", null, null);
    assertFalse(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createFunctionDeclarationNode();
    assertTrue(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationFalse() {
    Node node = createStatementNode();
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = createHoistedFunctionDeclarationNode();
    assertTrue(isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node node = createFunctionDeclarationNode();
    assertFalse(isHoistedFunctionDeclaration(node));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailed() {
    Node node = createStatementNode();
    Node parent = node.getParent();
    Preconditions.checkState(parent != null);
  }

  @Test
  public void testIsForInWithEmptyChildList() {
    Node node = node("for", new Object[]{"for statement", null}, null);
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsForInWithInvalidChildCount() {
    Node node = node("for", new Object[]{"for statement", null, null}, null);
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatementWithInvalidParentType() {
    Node node = createStatementNode();
    Node parent = node.getParent();
    Preconditions.checkState(parent != null);
    parent.setType(Token.IF);
    assertFalse(isStatement(node));
  }

  @Test
  public void testIsStatementWithEmptyParentList() {
    Node node = createStatementNode();
    Node parent = node.getParent();
    parent.setChildCount(0);
    assertFalse(isStatement(node));
  }

  @Test
  public void testIsStatementWithInvalidChildCount() {
    Node node = createStatementNode();
    Node parent = node.getParent();
    parent.setChildCount(2);
    assertFalse(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclarationWithInvalidChildCount() {
    Node node = createFunctionDeclarationNode();
    Node functionName = node.getChild(0);
    functionName.setChildCount(2);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithNullChild() {
    Node node = createFunctionDeclarationNode();
    Node functionName = node.getChild(0);
    functionName.setChildCount(0);
    functionName.setFirstChild(null);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithMissingStatementFlag() {
    Node node = createFunctionDeclarationNode();
    node.getChild(1).setType(Token.EXPRESSION);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithScriptAsParent() {
    Node node = createFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.SCRIPT);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithBlockAsParent() {
    Node node = createFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.BLOCK);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithLabelAsParent() {
    Node node = createFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.LABEL);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithFunctionAsParent() {
    Node node = createFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.FUNCTION);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithInvalidParentType() {
    Node node = createHoistedFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.IF);
    assertFalse(isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithScriptAsParent() {
    Node node = createHoistedFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.SCRIPT);
    assertFalse(isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithBlockAsParent() {
    Node node = createHoistedFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.BLOCK);
    assertFalse(isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithLabelAsParent() {
    Node node = createHoistedFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.LABEL);
    assertFalse(isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithFunctionAsParent() {
    Node node = createHoistedFunctionDeclarationNode();
    Node parent = node.getParent();
    parent.setType(Token.FUNCTION);
    assertTrue(isHoistedFunctionDeclaration(node));
  }
}