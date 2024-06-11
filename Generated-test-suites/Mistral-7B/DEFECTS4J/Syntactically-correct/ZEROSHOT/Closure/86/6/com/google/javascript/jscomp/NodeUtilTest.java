import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

 @org.junit.Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, null);
 nodeForIn.setChild(0, new Node(Token.KEYWORD, "for"));
 nodeForIn.setChild(1, new Node(Token.LPAREN, null));
 nodeForIn.setChild(2, new Node(Token.IDENTIFIER, "i"));
 nodeForIn.setChild(3, new Node(Token.RPAREN, null));
 nodeForIn.setChild(4, new Node(Token.SEMICOLON, null));
 nodeForIn.setChild(5, new Node(Token.LCURLYBRACE, null));

 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node(Token.KEYWORD, "if");
 nodeNotForIn.setChild(0, "if");
 nodeNotForIn.setChild(1, new Node(Token.LPAREN, null));
 nodeNotForIn.setChild(2, new Node(Token.RPAREN, null));
 nodeNotForIn.setChild(3, new Node(Token.LCURLYBRACE, null));

 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @org.junit.Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, null);
 nodeStatement.setChild(0, "if");
 nodeStatement.setChild(1, new Node(Token.LPAREN, null));
 nodeStatement.setChild(2, new Node(Token.RPAREN, null));
 nodeStatement.setChild(3, new Node(Token.LCURLYBRACE, null));
 nodeStatement.setChild(4, new Node(Token.RCURLYBRACE, null));

 Node parentStatement = new Node(Token.SCRIPT, null);
 parentStatement.setChild(0, nodeStatement);

 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeNotStatement = new Node(Token.IDENTIFIER, "x");
 nodeNotStatement.setParent(parentStatement);

 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @org.junit.Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
 nodeFunctionDeclaration.setChild(0, "myFunction");
 nodeFunctionDeclaration.setChild(1, new Node(Token.LPAREN, null));
 nodeFunctionDeclaration.setChild(2, new Node(Token.RPAREN, null));
 nodeFunctionDeclaration.setChild(3, new Node(Token.LCURLYBRACE, null));
 nodeFunctionDeclaration.setChild(4, new Node(Token.RCURLYBRACE, null));
 nodeFunctionDeclaration.setParent(new Node(Token.SCRIPT, null));

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
 nodeFunctionExpression.setChild(0, "myFunction");
 nodeFunctionExpression.setChild(1, new Node(Token.LPAREN, null));
 nodeFunctionExpression.setChild(2, new Node(Token.RPAREN, null));
 nodeFunctionExpression.setChild(3, new Node(Token.LCURLYBRACE, null));
 nodeFunctionExpression.setChild(4, new Node(Token.RCURLYBRACE, null));
 nodeFunctionExpression.setParent(new Node(Token.ASSIGNMENT, null));

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @org.junit.Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
 nodeFunctionDeclaration.setChild(0, "myFunction");
 nodeFunctionDeclaration.setChild(1, new Node(Token.LPAREN, null));
 nodeFunctionDeclaration.setChild(2, new Node(Token.RPAREN, null));
 nodeFunctionDeclaration.setChild(3, new Node(Token.LCURLYBRACE, null));
 nodeFunctionDeclaration.setChild(4, new Node(Token.RCURLYBRACE, null));
 nodeFunctionDeclaration.setParent(new Node(Token.SCRIPT, null));

 Node parentScript = new Node(Token.SCRIPT, null);
 Node parentFunction = new Node(Token.FUNCTION, null);
 parentFunction.setChild(0, "myFunction");
 parentFunction.setChild(1, new Node(Token.LPAREN, null));
 parentFunction.setChild(2, new Node(Token.RPAREN, null));
 parentFunction.setChild(3, nodeFunctionDeclaration);
 parentFunction.setChild(4, new Node(Token.LCURLYBRACE, null));
 parentFunction.setChild(5, new Node(Token.RCURLYBRACE, null));
 parentScript.setChild(0, parentFunction);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionDeclaration2 = new Node(Token.FUNCTION, null);
 nodeFunctionDeclaration2.setChild(0, "myFunction2");
 nodeFunctionDeclaration2.setChild(1, new Node(Token.LPAREN, null));
 nodeFunctionDeclaration2.setChild(2, new Node(Token.RPAREN, null));
 nodeFunctionDeclaration2.setChild(3, new Node(Token.LCURLYBRACE, null));
 nodeFunctionDeclaration2.setChild(4, new Node(Token.RCURLYBRACE, null));
 nodeFunctionDeclaration2.setParent(new Node(Token.SCRIPT, null));

 Node parentScript2 = new Node(Token.SCRIPT, null);
 Node parentFunction2 = new Node(Token.FUNCTION, null);
 parentFunction2.setChild(0, "myFunction2");
 parentFunction2.setChild(1, new Node(Token.LPAREN, null));
 parentFunction2.setChild(2, new Node(Token.RPAREN, null));
 parentFunction2.setChild(3, nodeFunctionDeclaration2);
 parentFunction2.setChild(4, new Node(Token.LCURLYBRACE, null));
 parentFunction2.setChild(5, new Node(Token.RCURLYBRACE, null));
 parentFunction2.setParent(parentScript2);
 nodeFunctionDeclaration2.setParent(parentFunction2);

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration2));
 }
}