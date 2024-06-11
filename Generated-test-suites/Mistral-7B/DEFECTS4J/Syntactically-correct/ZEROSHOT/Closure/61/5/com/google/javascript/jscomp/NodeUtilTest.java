import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3}, null);
 Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2}, null);

 assertTrue(NodeUtil.isForIn(nodeForIn));
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, new int[] {1, 2}, null);
 Node nodeNotStatement = new Node(Token.IDENT, new int[] {1}, null);
 Node nodeParentScript = new Node(Token.SCRIPT, new int[] {}, null);
 Node nodeParentBlock = new Node(Token.BLOCK, new int[] {}, null);
 Node nodeParentLabel = new Node(Token.LABEL, new int[] {1}, null);

 Node childStatement = new Node(Token.ASSIGN, new int[] {1, 2}, null);
 nodeStatement.setChild(0, childStatement);

 assertTrue(NodeUtil.isStatement(nodeStatement));
 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 assertTrue(NodeUtil.isStatement(nodeParentScript));
 assertTrue(NodeUtil.isStatement(nodeParentBlock));
 assertTrue(NodeUtil.isStatement(nodeParentLabel));
 assertFalse(NodeUtil.isStatement(nodeParentScript.getChild(0)));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2}, null);
 Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2}, null);
 nodeFunctionExpression.setProperty("callee", new Node(Token.IDENT, new int[] {1}, null));

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2}, null);
 Node nodeFunctionDeclarationInParentFunction = new Node(
         Token.FUNCTION, new int[] {1, 2}, null);
 nodeFunctionDeclarationInParentFunction.setChild(0, nodeFunctionDeclaration);
 Node nodeFunctionDeclarationInParentScript = new Node(Token.SCRIPT, new int[] {}, null);
 nodeFunctionDeclarationInParentScript.setChild(0, nodeFunctionDeclarationInParentFunction);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInParentFunction));
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInParentScript));
 }
}