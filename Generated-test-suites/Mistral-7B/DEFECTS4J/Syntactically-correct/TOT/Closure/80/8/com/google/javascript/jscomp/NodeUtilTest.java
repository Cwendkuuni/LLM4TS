import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.List;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(createToken(Token.FOR, 0, 0));
    forNode.setChild(0, Node.newNode(createToken(Token.KEYWORD, "for", 0, 0)));
    forNode.setChild(1, Node.newNode(createToken(Token.IDENT, "i", 0, 0)));
    forNode.setChild(2, Node.newNode(createToken(Token.ASSIGN, ":=", 0, 0)));
    forNode.setChild(3, Node.newNode(createToken(Token.IDENT, "array", 0, 0)));

    assertFalse("Is not a FOR-IN loop", NodeUtil.isForIn(forNode));

    Node forInNode = Node.newNode(createToken(Token.FOR, 0, 0));
    forInNode.setChild(0, Node.newNode(createToken(Token.KEYWORD, "for", 0, 0)));
    forInNode.setChild(1, Node.newNode(createToken(Token.KEYWORD, "in", 0, 0)));
    forInNode.setChild(2, Node.newNode(createToken(Token.IDENT, "i", 0, 0)));
    forInNode.setChild(3, Node.newNode(createToken(Token.IDENT, "array", 0, 0)));

    assertTrue("Is a FOR-IN loop", NodeUtil.isForIn(forInNode));
  }

  @Test
  public void testIsStatement() {
    Node node1 = Node.newNode(createToken(Token.NUMBER, "1", 0, 0));
    assertFalse("Is not a statement", NodeUtil.isStatement(node1));

    Node node2 = Node.newNode(createToken(Token.SCRIPT, "", 0, 0));
    Node child1 = Node.newNode(node1);
    node2.setChild(0, child1);

    assertTrue("Is a statement", NodeUtil.isStatement(node2));

    Node node3 = Node.newNode(createToken(Token.FUNCTION, "myFunction", 0, 0));
    Node body = Node.newNode(createToken(Token.KEYWORD, "return", 0, 0));
    body.setChild(0, Node.newNode(createToken(Token.NUMBER, "1", 0, 0)));
    node3.setChild(0, Node.newNode(Node.newNode(Token.BLOCK), child1, body));

    assertTrue("Is a statement", NodeUtil.isStatement(node3));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode1 = Node.newNode(createToken(Token.FUNCTION, "myFunction", 0, 0));
    Node functionNode2 = Node.newNode(createToken(Token.FUNCTION, "myFunction", 0, 0));
    Node body1 = Node.newNode(createToken(Token.KEYWORD, "return", 0, 0));
    Node body2 = Node.newNode(createToken(Token.KEYWORD, "return", 0, 0));

    functionNode1.setChild(0, Node.newNode(Node.newNode(Token.BLOCK), body1));
    functionNode2.setChild(0, Node.newNode(Node.newNode(Token.EXPR_STATEMENT), Node.newNode(createToken(Token.NUMBER, "1", 0, 0))));

    assertFalse("Is not a function declaration (no statement)", NodeUtil.isFunctionDeclaration(functionNode2));

    Node scope = Node.newNode(createToken(Token.SCRIPT, "", 0, 0));

    Node functionDeclaration = Node.newNode(Node.newNode(Token.FUNCTION, "myFunction", 0, 0));
    Node functionBody = Node.newNode(Node.newNode(Token.BLOCK));
    Node returnNode = Node.newNode(createToken(Token.KEYWORD, "return", 0, 0));
    Node expression = Node.newNode(createToken(Token.NUMBER, "1", 0, 0));
    functionBody.setChild(0, Node.newNode(Token.EXPR_STATEMENT, expression));
    returnNode.setChild(0, expression);
    functionDeclaration.setChild(0, functionBody);
    scope.setChild(0, functionDeclaration);

    assertTrue("Is a function declaration", NodeUtil.isFunctionDeclaration(functionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(createToken(Token.SCRIPT, "", 0, 0));

    Node functionDeclaration = Node.newNode(Node.newNode(Token.FUNCTION, "myFunction", 0, 0));
    Node functionBody = Node.newNode(Node.newNode(Token.BLOCK));
    Node returnNode = Node.newNode(createToken(Token.KEYWORD, "return", 0, 0));
    Node expression = Node.newNode(createToken(Token.NUMBER, "1", 0, 0));
    functionBody.setChild(0, Node.newNode(Token.EXPR_STATEMENT, expression));
    returnNode.setChild(0, expression);
    functionDeclaration.setChild(0, functionBody);
    scriptNode.setChild(0, functionDeclaration);

    assertTrue("Is a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node functionExpression = Node.newNode(Node.newNode(Token.FUNCTION, null, 0, 0));
    Node functionBodyExpression = Node.newNode(Node.newNode(Token.BLOCK));
    Node returnNodeExpression = Node.newNode(createToken(Token.KEYWORD, "return", 0, 0));
    Node expressionExpression = Node.newNode(createToken(Token.NUMBER, "1", 0, 0));
    functionBodyExpression.setChild(0, Node.newNode(Token.EXPR_STATEMENT, expressionExpression));
    returnNodeExpression.setChild(0, expressionExpression);
    functionExpression.setChild(0, functionBodyExpression);
    scriptNode.setChild(0, functionExpression);

    assertFalse("Is not a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsFunctionExpression() {
    Node functionNode1 = Node.newNode(Node.newNode(Token.FUNCTION, null, 0, 0));
    Node functionNode2 = Node.newNode(Node.newNode(Token.FUNCTION, "myFunction", 0, 0));

    assertFalse("Is not a function expression (no name)", NodeUtil.isFunctionExpression(functionNode1));

    Node scriptNode = Node.newNode(createToken(Token.SCRIPT, "", 0, 0));
    Node functionExpression = Node.newNode(Node.newNode(Token.FUNCTION, "myFunction", 0, 0));
    Node functionBody = Node.newNode(Node.newNode(Token.BLOCK));
    Node returnNode = Node.newNode(createToken(Token.KEYWORD, "return", 0, 0));
    Node expression = Node.newNode(createToken(Token.NUMBER, "1", 0, 0));
    functionBody.setChild(0, Node.newNode(Token.EXPR_STATEMENT, expression));
    returnNode.setChild(0, expression);
    functionExpression.setChild(0, functionBody);
    scriptNode.setChild(0, functionExpression);

    assertFalse("Is not a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionExpression));
    assertTrue("Is a function expression", NodeUtil.isFunctionExpression(functionExpression));
  }

  @Test
  public void testIsStatementInFunctionExpression() {
    Node functionExpression = Node.newNode(Node.newNode(Token.FUNCTION, null, 0, 0));
    Node functionBody = Node.newNode(Node.newNode(Token.BLOCK));
    Node statement = Node.newNode(createToken(Token.KEYWORD, "return", 0, 0));
    Node expression = Node.newNode(createToken(Token.NUMBER, "1", 0, 0));
    functionBody.setChild(0, Node.newNode(Token.EXPR_STATEMENT, expression));
    statement.setChild(0, expression);
    functionExpression.setChild(0, functionBody);

    assertFalse("Function expression does not have a parent statement", NodeUtil.isStatement(functionExpression));
    assertFalse("Statement is not a function expression", NodeUtil.isFunctionExpression(statement));

    Node functionDeclaration = Node.newNode(Node.newNode(Token.FUNCTION, "myFunction", 0, 0));
    Node functionBodyDeclaration = Node.newNode(Node.newNode(Token.BLOCK));
    Node statementDeclaration = Node.newNode(Node.newNode(Token.STATEMENT, null, 0, 0));
    Node expressionDeclaration = Node.newNode(Node.newNode(Token.EXPRESSION_STATEMENT, null, 0, 0));
    Node expression = Node.newNode(createToken(Token.NUMBER, "1", 0, 0));
    functionBodyDeclaration.setChild(0, Node.newNode(Token.EXPR_STATEMENT, expressionDeclaration));
    expressionDeclaration.setChild(0, Node.newNode(Token.ASSIGN, null, null, expression));
    statementDeclaration.setChild(0, Node.newNode(Token.EXPR_STATEMENT, statement));
    functionBodyDeclaration.setChild(0, statementDeclaration);
    functionDeclaration.setChild(0, functionBodyDeclaration);

    Node functionStatement = Node.newNode(Node.newNode(Token.STATEMENT, null, 0, 0));
    functionStatement.setChild(0, functionDeclaration);
    Node script = Node.newNode(createToken(Token.SCRIPT, "", 0, 0));
    script.setChild(0, functionStatement);

    assertTrue("Function declaration is a statement", NodeUtil.isStatement(functionDeclaration));
    assertTrue("Function expression is a statement", NodeUtil.isStatement(functionExpression));
    assertFalse("Function expression has no parent statement", NodeUtil.isStatement(functionExpression.getParent()));
    assertFalse("Function declaration is not a function expression", NodeUtil.isFunctionExpression(functionDeclaration));
    assertFalse("Statement is not a function declaration", NodeUtil.isFunctionDeclaration(statement));
    assertTrue("Statement is a statement", NodeUtil.isStatement(statement));
    assertTrue("Statement is a statement in the function expression", NodeUtil.isStatement(functionExpression.getChild(0)));
    assertTrue("Statement is a statement in the function declaration", NodeUtil.isStatement(functionDeclaration.getChild(0)));
    assertTrue("Statement is a statement in the script", NodeUtil.isStatement(script.getChild(0)));
  }
}