import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, 1, 1);
    Node nodeNotForIn = new Node(Token.IF, null, 1, 1);

    assertTrue("Node representing FOR-IN loop should be identified as such", isForIn(nodeForIn));
    assertFalse("Node not representing FOR-IN loop should not be identified as such", isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, 1, 1);
    Node nodeBlock = new Node(Token.BLOCK, null, 1, 1);
    Node nodeFunction = new Node(Token.FUNCTION, null, 1, 1);
    Node nodeIf = new Node(Token.IF, null, 1, 1);

    Node parentScript = new Node(Token.SCRIPT, null, 1, 1);
    Node parentBlock = new Node(Token.BLOCK, null, 1, 1);
    Node parentFunction = new Node(Token.FUNCTION, null, 1, 1);
    Node parentIf = new Node(Token.IF, null, 1, 1);

    parentScript.addChild(nodeScript);
    parentBlock.addChild(nodeBlock);
    parentFunction.addChild(nodeFunction);
    parentIf.addChild(nodeIf);

    assertTrue("Node representing script should be identified as a statement", isStatement(nodeScript));
    assertTrue("Node representing block should be identified as a statement", isStatement(nodeBlock));
    assertTrue("Node representing function declaration should be identified as a statement", isStatement(nodeFunction));
    assertFalse("Node representing IF statement should not be identified as a statement", isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "funcName", 1, 1);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, 1, 1);

    assertTrue("Node representing function declaration should be identified as such", isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse("Node representing function expression should not be identified as a function declaration", isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "funcName", 1, 1);
    Node nodeFunctionDeclarationInBlock = new Node(Token.FUNCTION, "funcName", 1, 1);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, "funcName", 1, 1);

    Node parentScript = new Node(Token.SCRIPT, null, 1, 1);
    Node parentBlock = new Node(Token.BLOCK, null, 1, 1);
    Node parentFunction = new Node(Token.FUNCTION, null, 1, 1);

    parentScript.addChild(nodeFunctionDeclaration);
    parentBlock.addChild(nodeFunctionDeclarationInBlock);
    parentFunction.addChild(nodeFunctionDeclarationInFunction);

    assertTrue("Function declaration in script should be identified as hoisted", isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse("Function declaration in block should not be identified as hoisted", isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse("Function declaration in function should not be identified as hoisted", isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}