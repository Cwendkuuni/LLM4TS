import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(FOR, null);
 nodeForIn.setLineNumber(1);
 nodeForIn.setColumnNumber(1);
 nodeForIn.setChildCount(3);

 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node(SCRIPT, null);
 nodeNotForIn.setLineNumber(1);
 nodeNotForIn.setColumnNumber(1);
 nodeNotForIn.setChildCount(1);

 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(SCRIPT, null);
 nodeStatement.setLineNumber(1);
 nodeStatement.setColumnNumber(1);
 nodeStatement.setChildCount(1);

 Node nodeFunction = new Node(FUNCTION, "func", null);
 nodeFunction.setLineNumber(1);
 nodeFunction.setColumnNumber(1);
 nodeFunction.setChildCount(1);

 Node nodeExpression = new Node(IDENT, "identifier", null);
 nodeExpression.setLineNumber(1);
 nodeExpression.setColumnNumber(1);
 nodeExpression.setParent(new Node(ASSIGN, null, nodeFunction, nodeExpression));

 assertTrue(NodeUtil.isStatement(nodeStatement));
 assertFalse(NodeUtil.isStatement(nodeFunction));
 assertFalse(NodeUtil.isStatement(nodeExpression));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(FUNCTION, "func", null);
 nodeFunctionDeclaration.setLineNumber(1);
 nodeFunctionDeclaration.setColumnNumber(1);
 nodeFunctionDeclaration.setChildCount(2);

 Node nodeFunctionBody = new Node(SCRIPT, null);
 nodeFunctionBody.setLineNumber(1);
 nodeFunctionBody.setColumnNumber(1);
 nodeFunctionBody.setChildCount(1);

 nodeFunctionDeclaration.setChild(0, new Node(PARAMS, null, nodeFunctionBody));

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = new Node(FUNCTION, null, null, nodeFunctionBody);
 nodeFunctionExpression.setLineNumber(1);
 nodeFunctionExpression.setColumnNumber(1);
 nodeFunctionExpression.setChildCount(1);

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeScript = new Node(SCRIPT, null);
 nodeScript.setLineNumber(1);
 nodeScript.setColumnNumber(1);
 nodeScript.setChildCount(2);

 Node nodeFuncDeclaration = new Node(FUNCTION, "func", null);
 nodeFuncDeclaration.setLineNumber(1);
 nodeFuncDeclaration.setColumnNumber(1);
 nodeFuncDeclaration.setChildCount(2);

 Node nodeFuncBody = new Node(SCRIPT, null);
 nodeFuncBody.setLineNumber(1);
 nodeFuncBody.setColumnNumber(1);
 nodeFuncBody.setChildCount(1);

 nodeFuncDeclaration.setChild(0, new Node(PARAMS, null, nodeFuncBody));

 Node nodeFunction = new Node(FUNCTION, null, null, nodeFuncBody);
 nodeFunction.setLineNumber(1);
 nodeFunction.setColumnNumber(1);
 nodeFunction.setChildCount(1);

 nodeFuncBody.setChild(0, nodeFunction);

 nodeScript.setChild(0, nodeFuncDeclaration);
 nodeScript.setChild(1, new Node(ASSIGN, null, nodeFunction, new Node(IDENT, "x", null)));

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFuncDeclaration));
 }
}