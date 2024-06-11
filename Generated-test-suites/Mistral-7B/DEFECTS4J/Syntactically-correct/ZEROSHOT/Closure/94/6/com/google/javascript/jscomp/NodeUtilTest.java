import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, null, new int[] { 1, 2, 3 }, null);
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node(Token.IF, null, new int[] { 1 }, null);
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, null, new int[] { 1 }, null);
 Node parentScript = new Node(Token.SCRIPT, null, new int[] {}, null);
 parentScript.addChildToEnd(nodeStatement);
 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeNotStatement = new Node(Token.IF, null, new int[] { 1 }, null);
 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "testFunction", new int[] { 1, 2 }, null);
 nodeFunctionDeclaration.addChildToEnd(new Node(Token.BLOCK, null, new int[] {}, null));
 Node parentScript = new Node(Token.SCRIPT, null, new int[] {}, null);
 parentScript.addChildToEnd(nodeFunctionDeclaration);
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = new Node(Token.FUNCTION, null, new int[] { 1, 2 }, null);
 Node parentExpression = new Node(Token.ADD, null, new int[] { 1, 1 }, null);
 parentExpression.addChildToEnd(nodeFunctionExpression);
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, "testFunction", new int[] { 1, 2 }, null);
 nodeHoistedFunctionDeclaration.addChildToEnd(new Node(Token.BLOCK, null, new int[] {}, null));
 Node parentScript = new Node(Token.SCRIPT, null, new int[] {}, null);
 parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

 Node nodeNotHoistedFunctionDeclaration = new Node(Token.FUNCTION, "testFunction", new int[] { 1, 2 }, null);
 nodeNotHoistedFunctionDeclaration.addChildToEnd(new Node(Token.BLOCK, null, new int[] {}, null));
 Node parentFunction = new Node(Token.FUNCTION, null, new int[] {}, null);
 parentFunction.addChildToEnd(nodeNotHoistedFunctionDeclaration);
 Node parentScript = new Node(Token.SCRIPT, null, new int[] {}, null);
 parentScript.addChildToEnd(parentFunction);
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
 }
}