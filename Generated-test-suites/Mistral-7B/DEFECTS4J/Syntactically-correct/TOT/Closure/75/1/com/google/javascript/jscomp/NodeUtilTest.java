import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node forNode = Node.newForStatement();
 Node ifNode = Node.newExpressionStatement(Node.newIfStatement());

 assertTrue(NodeUtil.isForIn(forNode));
 assertFalse(NodeUtil.isForIn(ifNode));
 }

 @Test
 public void testIsStatement() {
 Node scriptNode = Node.newScript();
 Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0]);
 Node ifStatementNode = Node.newIfStatement();
 Node expressionStatementNode = Node.newExpressionStatement(Node.newName("x"));

 assertTrue(NodeUtil.isStatement(scriptNode));
 assertTrue(NodeUtil.isStatement(functionNode));
 assertTrue(NodeUtil.isStatement(ifStatementNode));
 assertTrue(NodeUtil.isStatement(expressionStatementNode));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node simpleFunctionNode = Node.newFunction("testFunction", new Node[0], new Node[0]);
 Node functionInExpressionNode = Node.newExpressionStatement(Node.newFunction("testFunction", new Node[0], new Node[0]));

 assertTrue(NodeUtil.isFunctionDeclaration(simpleFunctionNode));
 assertFalse(NodeUtil.isFunctionDeclaration(functionInExpressionNode));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node scriptNode = Node.newScript();
 Node functionDeclarationNode = Node.newFunction("testFunction", new Node[0], new Node[0]);
 Node functionDeclarationInScriptNode = Node.newFunction("testFunction", new Node[0], new Node[0]);
 Node functionExpressionNode = Node.newExpressionStatement(Node.newFunction("testFunction", new Node[0], new Node[0]));
 Node functionExpressionInScriptNode = Node.newExpressionStatement(Node.newFunction("testFunction", new Node[0], new Node[0]));

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInScriptNode));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionInScriptNode));
 }

 @Test(expected = IllegalStateException.class)
 public void testCheckParentIsNotNull() {
 Node node = Node.newExpressionStatement(Node.newName("x"));
 NodeUtil.isStatement(node);
 }
}