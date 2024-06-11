import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.AbstractCompiler.TO_ERROR_REPORTER;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis.DiagnosticType;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private NodeTraversal nodeTraversal;
 private CheckGlobalThis checkGlobalThis;

 @Before
 public void setUp() {
 compiler = Mockito.mock(AbstractCompiler.class);
 nodeTraversal = new NodeTraversal(Compiler.factory(checker ->
 new CheckerFactory().getChecker(checker)).getChecker(), TO_ERROR_REPORTER);
 checkGlobalThis = new CheckGlobalThis(compiler);
 }

 @Test
 public void testShouldTraverseFunctions() {
 Node functionNode = Node.newFunction(null, "function() {}", null);
 Node parentNode = Node.newName("parent");
 Node functionWithParent = Node.newAssign(parentNode, functionNode);

 assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), functionNode, functionWithParent));
 }

 @Test
 public void testShouldNotTraverseNonFunctionNodes() {
 Node numberNode = Node.newNumber(42);
 Node parentNode = Node.newName("parent");
 Node assignNode = Node.newAssign(parentNode, numberNode);

 assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), numberNode, assignNode));
 }

 @Test
 public void testShouldNotTraverseFunctionsThatAreConstructorsOrAnnotated() {
 Node constructorNode = Node.newFunction(null, "function MyConstructor() {}", null);
 Node parentNode = Node.newName("parent");
 Node constructorWithAnnotation = Node.newAssign(parentNode, constructorNode);
 constructorNode.setJSDocInfo(new JSDocInfo("MyConstructor", null, null, true, null, null, null));

 assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), constructorNode, constructorWithAnnotation));
 }

 @Test
 public void testShouldNotTraverseFunctionsNotInGlobalContext() {
 Node functionNode = Node.newFunction(null, "function() {}", null);
 Node parentNode = Node.newName("parent", null, Node.NEW(Token.BLOCK), Node.NEW(Token.SCRIPT), null, null, null, null);
 Node functionWithParent = Node.newAssign(parentNode, functionNode);

 assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), functionNode, functionWithParent));
 }

 @Test
 public void testShouldNotTraverseFunctionsLentToPrototype() {
 Node objectLiteralNode = Node.newObjectLiteral();
 Node prototypeNameNode = Node.newName("prototype");
 Node propertyNameNode = Node.newString("property");
 Node propertyValueNode = Node.newName("value");
 Node assignNode = Node.newAssign(propertyNameNode, propertyValueNode);
 Node prototypeAssignmentNode = Node.newAssign(prototypeNameNode, objectLiteralNode);
 objectLiteralNode.addChildToEnd(prototypeAssignmentNode);
 Node parentNode = Node.newName("parent");
 Node functionNode = Node.newFunction(null, "function() {}", null);
 Node functionWithPrototypeAssignment = Node.newAssign(parentNode, functionNode);
 Node assignNodeToPrototype = Node.newAssign(prototypeNameNode, functionNode);

 assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), functionNode, functionWithPrototypeAssignment));
 }

 @Test
 public void testShouldReportThisOnLeftSideOfAssignment() {
 Node assignNode = Node.newAssign(Node.NEW(Node.NAME, "a"), Node.NEW(Node.THIS, ""));
 assertTrue(checkGlobalThis.shouldReportThis(assignNode));
 }

 @Test
 public void testShouldReportThisOnPropertyAccess() {
 Node getNode = Node.newGetProp(Node.NEW(Node.NAME, "a"), Node.NEW(Node.NAME, "prototype"));
 assertTrue(checkGlobalThis.shouldReportThis(getNode));
 }

 @Test(expected = AssertionError.class)
 public void testShouldNotReportThisOnFunctionCall() {
 Node functionNode = Node.newFunction(null, "function() {}", null);
 Node callNode = Node.newCall(functionNode, Node.NEW(Node.THIS, ""));
 checkGlobalThis.visit(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), callNode, null);
 }

 @Test(expected = AssertionError.class)
 public void testShouldNotReportThisOnVariableAssignment() {
 Node assignNode = Node.newAssign(Node.NEW(Node.NAME, "a"), Node.NEW(Node.THIS, ""));
 checkGlobalThis.visit(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), assignNode, null);
 }

 @Test(expected = AssertionError.class)
 public void testShouldNotReportThisOnFunctionDeclarationWithVariableDeclaration() {
 Node functionDeclarationNode = Node.newFunction(null, "function a() {}", null);
 Node variableDeclarationNode = Node.newVar("a", Node.NEW(Node.THIS, ""));
 Node functionWithVariableDeclaration = Node.newAssign(Node.NEW(Node.NAME, "a"), functionDeclarationNode);
 Node scriptNode = Node.newScript();
 scriptNode.addChildToEnd(functionWithVariableDeclaration);
 Node parentNode = Node.NEW(Node.SCRIPT);
 parentNode.addChildToEnd(scriptNode);

 checkGlobalThis.visit(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), Node.NEW(Node.NAME, "a"), parentNode);
 }

 @Test(expected = AssertionError.class)
 public void testShouldNotReportThisOnFunctionDeclarationWithFunctionExpression() {
 Node functionDeclarationNode = Node.newFunction(null, "function a() {}", null);
 Node functionExpressionNode = Node.newAssign(Node.NEW(Node.NAME, "a"), Node.NEW(Node.FUNCTION, null, Node.NEW(Node.NAME, "a"), null, null, null));
 Node scriptNode = Node.newScript();
 scriptNode.addChildToEnd(functionExpressionNode);
 Node parentNode = Node.NEW(Node.SCRIPT);
 parentNode.addChildToEnd(scriptNode);

 checkGlobalThis.visit(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), Node.NEW(Node.NAME, "a"), parentNode);
 }

 @Test(expected = AssertionError.class)
 public void testShouldNotReportThisOnObjectLiteralKey() {
 Node objectLiteralNode = Node.newObjectLiteral();
 Node prototypeNameNode = Node.newName("prototype");
 Node propertyNameNode = Node.newString("property");
 Node propertyValueNode = Node.newName("value");
 Node assignNode = Node.newAssign(propertyNameNode, propertyValueNode);
 Node assignNodeToPrototype = Node.newAssign(prototypeNameNode, objectLiteralNode);
 objectLiteralNode.addChildToEnd(assignNodeToPrototype);
 Node parentNode = Node.newName("parent");
 Node functionNode = Node.newFunction(null, "function() {}", null);
 Node functionWithPrototypeAssignment = Node.newAssign(parentNode, functionNode);
 Node getNode = Node.newGetProp(prototypeNameNode, propertyNameNode);

 checkGlobalThis.visit(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), getNode, functionWithPrototypeAssignment);
 }

 @Test(expected = AssertionError.class)
 public void testShouldNotReportThisOnObjectLiteralKeyChain() {
 Node objectLiteralNode = Node.newObjectLiteral();
 Node prototypeNameNode = Node.newName("prototype");
 Node propertyNameNode1 = Node.newString("property1");
 Node propertyNameNode2 = Node.newString("property2");
 Node propertyValueNode = Node.newName("value");
 Node assignNode1 = Node.newAssign(propertyNameNode1, propertyValueNode);
 Node assignNode2 = Node.newAssign(propertyNameNode2, Node.NEW(Node.GETPROP, prototypeNameNode, propertyNameNode1));
 Node assignNodeToPrototype = Node.newAssign(prototypeNameNode, objectLiteralNode);
 objectLiteralNode.addChildToEnd(assignNodeToPrototype);
 Node parentNode = Node.newName("parent");
 Node functionNode = Node.newFunction(null, "function() {}", null);
 Node functionWithPrototypeAssignment = Node.newAssign(parentNode, functionNode);
 Node getNode = Node.newGetProp(prototypeNameNode, propertyNameNode2);

 checkGlobalThis.visit(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), getNode, functionWithPrototypeAssignment);
 }

 @Test(expected = AssertionError.class)
 public void testShouldNotReportThisOnVariableDeclarationInFunction() {
 Node functionDeclarationNode = Node.newFunction(null, "function a() { var thisVar = this; }", null);
 Node variableDeclarationNode = Node.newVar("thisVar", Node.NEW(Node.THIS, ""));
 Node functionWithVariableDeclaration = Node.newAssign(Node.NEW(Node.NAME, "a"), functionDeclarationNode);
 Node scriptNode = Node.newScript();
 scriptNode.addChildToEnd(functionWithVariableDeclaration);
 Node parentNode = Node.NEW(Node.SCRIPT);
 parentNode.addChildToEnd(scriptNode);

 checkGlobalThis.visit(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), Node.NEW(Node.NAME, "a"), parentNode);
 }

 @Test
 public void testShouldNotReportThisOnVariableDeclarationOutsideFunction() {
 Node variableDeclarationNode = Node.newVar("thisVar", Node.NEW(Node.THIS, ""));
 Node scriptNode = Node.newScript();
 scriptNode.addChildToEnd(variableDeclarationNode);
 Node parentNode = Node.NEW(Node.SCRIPT);
 parentNode.addChildToEnd(scriptNode);

 assertFalse(checkGlobalThis.shouldReportThis(variableDeclarationNode));
 }

 @Test
 public void testShouldReportThisOnPrototypeMethod() {
 Node prototypeNameNode = Node.newName("prototype");
 Node functionNode = Node.newFunction(null, "function foo() {}", null);
 Node prototypeAssignmentNode = Node.newAssign(prototypeNameNode, functionNode);
 functionNode.setJSDocInfo(new JSDocInfo("foo", null, null, true, null, null, null, null));
 Node parentNode = Node.NEW(Node.SCRIPT);
 Node objectLiteralNode = Node.newObjectLiteral();
 objectLiteralNode.addChildToEnd(prototypeAssignmentNode);
 parentNode.addChildToEnd(objectLiteralNode);
 Node scriptNode = Node.newScript();
 scriptNode.addChildToEnd(parentNode);

 checkGlobalThis.visit(nodeTraversal, Node.NEW(CheckGlobalThis.class), Node.NEW(NodeTraversal.class), Node.NEW(AbstractCompiler.class), Node.NEW(Node.THIS, ""), scriptNode);
 }

}