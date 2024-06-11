import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, 1, new int[] { 1, 2, 3 });
 Node nodeNotForIn = new Node(Token.IF, 1, new int[] { 1, 2 });

 assertTrue(NodeUtil.isForIn(nodeForIn));
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, 1, new int[] { 1, 2 });
 Node nodeNotStatement = new Node(Token.IDENT, "x", 1);
 Node nodeScript = new Node(Token.SCRIPT, 1);
 Node nodeBlock = new Node(Token.BLOCK, 1);
 Node nodeLabel = new Node(Token.LABEL, "label", 1);

 assertTrue(NodeUtil.isStatement(nodeScript));
 assertTrue(NodeUtil.isStatement(nodeBlock));
 assertTrue(NodeUtil.isStatement(nodeStatement));
 assertTrue(NodeUtil.isStatement(nodeLabel));

 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1, new int[] { 1 });
 Node nodeFunctionExpression = new Node(Token.FUNCTION, 1, new int[] { 1, 2 });
 Node nodeNotFunction = new Node(Token.VAR, 1, new int[] { 1, 2 });

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunction));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, 1, new int[] { 1, 1 });
 Node nodeFunctionDeclarationInBlock = new Node(Token.FUNCTION, 1, new int[] { 1, 1, 1 });
 Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, 1, new int[] { 1, 1, 1, 1 });

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 }
}