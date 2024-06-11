import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, new int[]{}, null);
    Node nodeNotForIn = new Node(IF, new int[]{}, null);

    assertTrue("isForIn should return true for FOR-IN node", NodeUtil.isForIn(nodeForIn));
    assertFalse("isForIn should return false for IF node", NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT, new int[]{}, null);
    Node nodeFunction = new Node(FUNCTION, new int[]{}, null);
    Node nodeFunctionInExpression = new Node(FUNCTION, new int[]{}, new Node[] { new Node(IDENT, new int[]{"id"}, "func") });

    Node nodeStatementInScript = new Node(IDENT, new int[]{}, "id");
    Node nodeStatementInFunction = new Node(ASSIGN, new int[]{}, new Node[] { new Node(IDENT, new int[]{"id"}, "id"), new Node(NUMBER, new int[]{}, 42) });
    Node nodeStatementInFunctionExpression = new Node(IDENT, new int[]{}, "id");

    Node parentScript = new Node(SCRIPT, new int[]{}, null);
    Node parentBlock = new Node(BLOCK, new int[]{}, null);
    Node parentLabel = new Node(LABEL, new int[]{}, "label");

    Node parentFunction = new Node(FUNCTION, new int[]{}, null);
    Node parentFunctionExpression = new Node(FUNCTION, new int[]{}, null);

    assertTrue("isStatement should return true for statements in script", NodeUtil.isStatement(nodeStatementInScript));
    assertTrue("isStatement should return true for statements in function", NodeUtil.isStatement(nodeStatementInFunction));
    assertTrue("isStatement should return true for statements in function expression", NodeUtil.isStatement(nodeStatementInFunctionExpression));
    assertTrue("isStatement should return true for statements in script node", NodeUtil.isStatement(nodeScript));
    assertTrue("isStatement should return true for statements in block node", NodeUtil.isStatement(nodeStatementInBlock));
    assertTrue("isStatement should return true for statements in label node", NodeUtil.isStatement(nodeStatementInLabel));

    assertFalse("isStatement should return false for functions", NodeUtil.isStatement(nodeFunction));
    assertFalse("isStatement should return false for function expression", NodeUtil.isStatement(nodeFunctionInExpression));
    assertFalse("isStatement should return false for node without parent", NodeUtil.isStatement(new Node(SCRIPT, new int[]{}, null)));

    // Parents of statements
    parentScript.setChild(0, nodeStatementInScript);
    parentBlock.setChild(0, nodeStatementInBlock);
    parentLabel.setChild(0, nodeStatementInLabel);

    parentFunction.setChild(0, nodeFunction);
    parentFunctionExpression.setChild(0, nodeFunctionInExpression);

    // Checking parents
    assertTrue("isStatement should return true for statements in parent scripts", NodeUtil.isStatement(nodeStatementInScript));
    assertTrue("isStatement should return true for statements in parent blocks", NodeUtil.isStatement(nodeStatementInBlock));
    assertTrue("isStatement should return true for statements in parent labels", NodeUtil.isStatement(nodeStatementInLabel));
    assertFalse("isStatement should return false for functions in parent functions", NodeUtil.isStatement(nodeFunction));
    assertFalse("isStatement should return false for function expressions in parent functions", NodeUtil.isStatement(nodeFunctionInExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, new int[]{}, new Node[] { new Node(IDENT, new int[]{"id"}, "func"), new Node(BLOCK, new int[]{}, null) });
    Node nodeFunctionExpression = new Node(FUNCTION, new int[]{}, new Node[] { new Node(IDENT, new int[]{"id"}, "func") });

    assertTrue("isFunctionDeclaration should return true for function declarations", NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse("isFunctionDeclaration should return false for function expressions", NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(FUNCTION, new int[]{}, new Node[] { new Node(IDENT, new int[]{"id"}, "func"), new Node(BLOCK, new int[]{}, null) });
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, new int[]{}, new Node[] { new Node(IDENT, new int[]{"id"}, "func"), new Node(BLOCK, new int[]{}, null), null, nodeFunctionDeclarationInScript });

    assertTrue("isHoistedFunctionDeclaration should return true for function declarations in script", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue("isHoistedFunctionDeclaration should return true for function declarations in function", NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse("isHoistedFunctionDeclaration should return false for function expressions", NodeUtil.isHoistedFunctionDeclaration(new Node(FUNCTION, new int[]{}, new Node[] { new Node(IDENT, new int[]{"id"}, "func") })));
  }
}