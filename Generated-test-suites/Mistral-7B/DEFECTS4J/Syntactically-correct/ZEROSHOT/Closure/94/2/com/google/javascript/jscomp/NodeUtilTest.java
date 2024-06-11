import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
    assertTrue("Expected FOR-IN node to be identified correctly.", isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2});
    assertFalse("Expected non-FOR-IN node to not be identified as FOR-IN.", isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[] {});
    Node nodeBlock = new Node(Token.BLOCK, new int[] {});
    Node nodeLabel = new Node(Token.LABEL, new int[] {1});
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2});
    Node nodeExpression = new Node(Token.IDENT, new int[] {1});

    assertTrue("Expected Script node to be identified as statement.", isStatement(nodeScript));
    assertTrue("Expected Block node to be identified as statement.", isStatement(nodeBlock));
    assertTrue("Expected Label node to be identified as statement.", isStatement(nodeLabel));
    assertFalse("Expected Function node to not be identified as statement when not part of an expression.", isStatement(nodeFunction));
    assertFalse("Expected Identifier node to not be identified as statement.", isStatement(nodeExpression));

    Node parentScript = new Node(Token.SCRIPT, new int[] {});
    Node parentFunction = new Node(Token.FUNCTION, new int[] {1, 2});

    assertTrue("Expected Function node to be identified as statement when part of an expression.", isStatement(nodeFunction)
        + " when its parent is a Script node.", isStatement(nodeFunction, parentScript));
    assertFalse("Expected Function node to not be identified as statement when its parent is a Function node.", isStatement(nodeFunction, parentFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2, 3});

    assertFalse("Expected Function node that is part of an expression not to be a function declaration.", isFunctionDeclaration(nodeFunctionExpression));
    assertTrue("Expected Function node that is a statement to be a function declaration.", isFunctionDeclaration(nodeFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionScript = new Node(Token.FUNCTION, new int[] {1, 2});
    Node nodeFunctionBlock = new Node(Token.BLOCK, new int[] {});
    Node functionNodeInBlock = new Node(Token.FUNCTION, new int[] {1, 2});
    Node nodeFunctionFunction = new Node(Token.FUNCTION, new int[] {1, 2});

    assertFalse("Expected Function node that is not in a function or script context not to be hoisted.", isHoistedFunctionDeclaration(functionNodeInBlock));
    assertTrue("Expected Function node that is in a function context to be hoisted.", isHoistedFunctionDeclaration(nodeFunctionFunction));
    assertTrue("Expected Function node that is in a script context to be hoisted.", isHoistedFunctionDeclaration(nodeFunctionScript));
    assertFalse("Expected Function node that is in a block context not to be hoisted.", isHoistedFunctionDeclaration(nodeFunctionBlock));
  }
}