import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[] {1, 2, 3});
    Node objectNode = new Node(Token.OBJECT, new int[] {1, 2, 3});
    Node assignmentNode = new Node(Token.ASSIGN, new int[] {1, 2, 3});
    forNode.addChildToEnd(objectNode);
    forNode.addChildToEnd(Token.KEYWORD_IN);
    forNode.addChildToEnd(assignmentNode);

    assertTrue(isForIn(forNode));

    Node invalidForNode = new Node(Token.FOR, new int[] {1, 2});
    assertFalse(isForIn(invalidForNode));

    Node invalidStatementNode = new Node(Token.IF, new int[] {1, 2});
    assertFalse(isForIn(invalidStatementNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new int[] {1});
    scriptNode.addChildToEnd(new Node(Token.BLOCK, new int[] {1}));

    Node statementNode = new Node(Token.IF, new int[] {1, 2});
    statementNode.setParent(scriptNode);

    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.IF, new int[] {1, 2});
    Node assignmentNode = new Node(Token.ASSIGN, new int[] {1, 2, 3});
    expressionNode.setParent(assignmentNode);

    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new int[] {1, 2});
    functionNode.addChildToEnd(new Node(Token.IDENTIFIER, new int[] {1, 2}));
    functionNode.addChildToEnd(Token.SEMICOLON);

    Node statementNode = new Node(Token.IF, new int[] {1, 2});
    statementNode.setParent(functionNode);

    assertTrue(isFunctionDeclaration(functionNode));
    assertFalse(isFunctionDeclaration(statementNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[] {1});
    Node functionNode = new Node(Token.FUNCTION, new int[] {1, 2});
    functionNode.addChildToEnd(new Node(Token.IDENTIFIER, new int[] {1, 2}));
    functionNode.addChildToEnd(Token.SEMICOLON);
    scriptNode.addChildToEnd(functionNode);

    assertTrue(isHoistedFunctionDeclaration(functionNode));

    Node functionInsideFunctionNode = new Node(Token.FUNCTION, new int[] {1, 2});
    Node functionIdentifierNode = new Node(Token.IDENTIFIER, new int[] {1, 2});
    Node functionParentFunctionNode = new Node(Token.FUNCTION, new int[] {1, 2});
    functionParentFunctionNode.addChildToEnd(functionIdentifierNode);
    functionParentFunctionNode.addChildToEnd(functionNode);
    functionNode.addChildToEnd(Token.SEMICOLON);

    assertTrue(isHoistedFunctionDeclaration(functionNode));
  }
}