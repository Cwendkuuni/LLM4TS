import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.base.Preconditions.checkState;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[] {1, 2, 3}); // FOR statement with 3 children
    Node nonForNode = new Node(Token.IF, new int[] {1, 2}); // IF statement with 2 children

    assertTrue("Expected FOR node to be FOR-IN", NodeUtil.isForIn(forNode));
    assertFalse("Expected non-FOR node to not be FOR-IN", NodeUtil.isForIn(nonForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[] {1, 2}); // IF statement
    Node expressionNode = new Node(Token.FUNCTION, new int[] {1, 2, 3, 4}); // Function expression

    // Check statement node
    Node parent = new Node(Token.SCRIPT, new int[] {}); // Script node
    parent.addChild(statementNode);
    checkState(parent != null);
    assertTrue("Expected statement node to be a statement", NodeUtil.isStatement(statementNode));

    // Check expression node in script context
    parent = new Node(Token.SCRIPT, new int[] {}); // Script node
    parent.addChild(expressionNode);
    checkState(parent != null);
    assertFalse("Expected expression node not to be a statement", NodeUtil.isStatement(expressionNode));

    // Check expression node in function context
    Node functionNode = new Node(Token.FUNCTION, new int[] {1, 2, 3}); // Function declaration
    functionNode.addChild(expressionNode);
    checkState(functionNode != null);
    assertFalse("Expected expression node not to be a statement in function context", NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3, 4}); // Function declaration
    Node functionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3}); // Function expression

    assertTrue("Expected function declaration to be a function declaration", NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse("Expected function expression to not be a function declaration", NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2, 3, 4}); // Function declaration at the script level
    Node functionDeclarationInFunction = new Node(Token.FUNCTION, new int[] {1, 2, 3, 4}); // Function declaration inside a function

    assertTrue("Expected hoisted function declaration to be hoisted", NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse("Expected function declaration inside a function not to be hoisted", NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }
}