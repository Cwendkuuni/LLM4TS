import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

 private AbstractCompiler compiler;
 private CompilerContext context;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {};
 context = new CompilerContext(compiler, null);
 nodeFactory = context.getFactory();
 }

 @Test
 public void testPrepareAst() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createNode("{}");
 prepareAst.process(null, root);
 assertFalse(root.isObjectLit());
 assertEquals(0, root.getChildCount());
 }

 @Test
 public void testPrepareAstWithObjectLit() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createNode("{a: 1}");
 prepareAst.process(null, root);
 assertTrue(root.isObjectLit());
 assertEquals(2, root.getChildCount());

 Node key = root.getChild(0);
 Node value = root.getChild(1);
 assertTrue(key.isName());
 assertEquals("a", key.getString());
 assertTrue(value.isNumber());
 assertEquals(1, value.getNumber().intValue());
 }

 @Test
 public void testPrepareAstWithControlStructures() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createControlStructureNode("if (true) {}");
 prepareAst.process(null, root);
 assertTrue(root.isIfStatement());
 assertEquals(3, root.getChildCount());

 Node condition = root.getChild(0);
 assertTrue(condition.isLogicalOperator());
 assertTrue(condition.getFirstChild().isLiteral());
 assertTrue(condition.getFirstChild().isBoolean());
 assertEquals(Token.TRUE, condition.getFirstChild().getTokenType());

 Node thenBlock = root.getChild(1);
 assertTrue(thenBlock.isBlock());
 assertFalse(thenBlock.isEmpty());

 Node elseBlock = root.getChild(2);
 assertNull(elseBlock);
 }

 @Test
 public void testPrepareAstWithControlStructuresAndCodeBlocks() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createControlStructureNode("if (true) {return 1;}");
 prepareAst.process(null, root);
 assertTrue(root.isIfStatement());
 assertEquals(4, root.getChildCount());

 Node condition = root.getChild(0);
 assertTrue(condition.isLogicalOperator());
 assertTrue(condition.getFirstChild().isLiteral());
 assertTrue(condition.getFirstChild().isBoolean());
 assertEquals(Token.TRUE, condition.getFirstChild().getTokenType());

 Node thenBlock = root.getChild(1);
 assertTrue(thenBlock.isBlock());
 assertTrue(thenBlock.isEmpty());

 Node returnStatement = thenBlock.getFirstChild();
 assertTrue(returnStatement.isReturnStatement());
 assertTrue(returnStatement.getExpression().isNumber());
 assertEquals(1, returnStatement.getExpression().getNumber().intValue());

 Node elseBlock = root.getChild(3);
 assertNull(elseBlock);
 }

 @Test
 public void testPrepareAstWithControlStructuresAndEmptyCodeBlocks() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createControlStructureNode("if (true) {} else {}");
 prepareAst.process(null, root);
 assertTrue(root.isIfStatement());
 assertEquals(5, root.getChildCount());

 Node condition = root.getChild(0);
 assertTrue(condition.isLogicalOperator());
 assertTrue(condition.getFirstChild().isLiteral());
 assertTrue(condition.getFirstChild().isBoolean());
 assertEquals(Token.TRUE, condition.getFirstChild().getTokenType());

 Node thenBlock = root.getChild(1);
 assertTrue(thenBlock.isBlock());
 assertFalse(thenBlock.isEmpty());

 Node emptyThenBlock = thenBlock.getFirstChild();
 assertTrue(emptyThenBlock.isBlock());
 assertTrue(emptyThenBlock.isEmpty());

 Node elseBlock = root.getChild(4);
 assertTrue(elseBlock.isBlock());
 assertFalse(elseBlock.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testPrepareAstWithInvalidControlStructure() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createNode("if (true: 1) {}");
 prepareAst.process(null, root);
 fail("should throw an AssertionError");
 }

 @Test
 public void testPrepareAstWithCallNodes() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createCallNode("eval()");
 prepareAst.process(null, root);
 assertTrue(root.isCall());
 assertEquals(2, root.getChildCount());

 Node firstChild = root.getChild(0);
 assertTrue(firstChild.isName());
 assertEquals("eval", firstChild.getString());
 assertTrue(root.getFirstChild() instanceof Node.ExpressionStatement);
 }

 @Test
 public void testPrepareAstWithCallNodesAndFreeCalls() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createCallNode("f()");
 prepareAst.process(null, root);
 assertTrue(root.isCall());
 assertEquals(2, root.getChildCount());

 Node firstChild = root.getChild(0);
 assertFalse(firstChild.isName());
 assertTrue(firstChild.isGet());

 Node getCall = firstChild.getFirstChild();
 assertTrue(getCall.isName());
 assertEquals("f", getCall.getString());
 assertTrue(root.getFirstChild() instanceof Node.ExpressionStatement);
 }

 @Test
 public void testPrepareAstWithFunctionNodes() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createFunctionNode("function f() {}");
 prepareAst.process(null, root);
 assertTrue(root.isFunction());
 assertEquals(1, root.getChildCount());

 Node body = root.getChild(0);
 assertTrue(body.isBlock());
 assertFalse(body.isEmpty());
 }

 @Test
 public void testPrepareAstWithFunctionNodesAndDispatchers() {
 PrepareAst prepareAst = new PrepareAst(compiler);
 Node root = createFunctionNode("function MyClass() {\n" +
     "  this.myMethod = function() {};\n" +
     "}");
 prepareAst.process(null, root);
 assertTrue(root.isFunction());
 assertEquals(1, root.getChildCount());

 Node constructorBody = root.getChild(0);
 assertTrue(constructorBody.isBlock());
 assertFalse(constructorBody.isEmpty());

 Node myMethodNode = findNodeWithJSDoc("myMethod", constructorBody);
 assertTrue(myMethodNode != null);
 assertTrue(myMethodNode.isFunction());
 assertTrue(myMethodNode.getParent().isStatement());
 assertTrue(myMethodNode.getParent() instanceof Node.ExpressionStatement);
 assertTrue(myMethodNode.getParent().getParent() == root);

 Node methodJSDoc = myMethodNode.getJSDocInfo();
 assertNotNull(methodJSDoc);
 assertEquals("MyClass.myMethod", methodJSDoc.getSourceText());
 }

 private Node createControlStructureNode(String code) {
 return createNodeFromCode(code, nodeFactory);
 }

 private Node createCallNode(String code) {
 return createNodeFromCode(code, nodeFactory).getFirstChild();
 }

 private Node createFunctionNode(String code) {
 return createNodeFromCode(code, nodeFactory);
 }

 private Node findNodeWithJSDoc(String name, Node node) {
 for (Node child = node.getFirstChild(); child != null;
      child = child.getNext()) {
   if (child.isFunction() &&
       NodeUtil.getName(child).equals(name)) {
     return child;
   }
   Node current = findNodeWithJSDoc(name, child);
   if (current != null) {
     return current;
   }
 }
 return null;
 }

 private Node createNodeFromCode(String code, NodeFactory nodeFactory) {
 Node root = nodeFactory.parse(code);
 return root;
 }

 private void assertEquals(Node expected, Node actual) {
 assertEquals(expected, actual, "Expected node to be equal to actual node.");
 }

 private void assertNotNull(Object o) {
 assertNotNull(o, "Expected object to not be null.");
 }

 private void assertNull(Object o) {
 assertNull(o, "Expected object to be null.");
 }
}