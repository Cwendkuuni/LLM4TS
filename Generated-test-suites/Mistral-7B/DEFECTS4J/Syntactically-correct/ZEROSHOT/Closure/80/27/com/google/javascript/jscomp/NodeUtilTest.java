import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, new int[]{1, 2, 3});
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node(Token.IF, new int[]{1});
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, new int[]{1});
 Node parentScript = new Node(Token.SCRIPT, new int[]{1});
 parentScript.addChildToEnd(nodeStatement);

 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeNotStatement = new Node(Token.IF, new int[]{1});
 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2}, new int[]{3}, null);
 nodeFunctionDeclaration.setName("foo");
 Node parentBlock = new Node(Token.BLOCK, new int[]{1});
 parentBlock.addChildToEnd(nodeFunctionDeclaration);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{1, 2}, new int[]{3}, null);
 Node parentExpression = new Node(Token.CALL, new int[]{1, 5});
 parentExpression.addChildToEnd(nodeFunctionExpression);

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2}, new int[]{3}, null);
 nodeHoistedFunctionDeclaration.setName("foo");
 Node parentScript = new Node(Token.SCRIPT, new int[]{1});
 parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

 Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{1, 2}, new int[]{3}, null);
 nodeFunctionDeclarationInFunction.setName("foo");
 Node parentFunction = new Node(Token.FUNCTION, new int[]{1, 2}, new int[]{3}, null);
 parentFunction.addChildToEnd(nodeFunctionDeclarationInFunction);

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPreconditionsCheckStateFail() {
 Node node = new Node(Token.SCRIPT, new int[]{1});
 NodeUtil.isStatement(node);
 Preconditions.checkState(false, "test fail");
 }
}