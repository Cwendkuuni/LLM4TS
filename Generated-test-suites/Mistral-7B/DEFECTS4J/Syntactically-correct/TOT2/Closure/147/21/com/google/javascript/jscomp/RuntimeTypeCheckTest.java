package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new Compiler();
 }

 @Test
 public void testRuntimeTypeCheck() {
 // Create a simple Java script with a function that returns a number
 String script = "function add(x, y) { return x + y; }";

 // Compile the script and apply the RuntimeTypeCheck pass
 Node rootNode = compiler.parseTree(script, null, null);
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
 runtimeTypeCheck.process(null, rootNode);

 // Check that the expected type assertions have been added
 Node functionNode = findFunctionNodeByName(rootNode, "add");
 List<Node> checkNodes = findCheckerNodes(functionNode);
 List<String> expectedCheckers = Arrays.asList(
 "numberChecker", "numberChecker", "numberChecker" // x, y, return value
 );
 assertTrue(checkNodes.size() == expectedCheckers.size());
 for (int i = 0; i < checkNodes.size(); i++) {
 assertTrue(checkNodes.get(i).getClass().getName().startsWith(
 "com.google.javascript.rhino.jstype.JSTypeChecker"));
 assertTrue(expectedCheckers.get(i).equals(
 getCheckerName(checkNodes.get(i))));
 }

 // Check that the function returns the expected type
 FunctionType functionType = (FunctionType) functionNode.getJSType();
 assertTrue(functionType.getReturnType().isNumberValueType());

 // Check that type assertions are respecting the specified types
 Node callNode = findCallNodeByName(functionNode, "add");
 Node arg1Node = callNode.getFirstChild();
 Node arg2Node = callNode.getLastChild();
 Node resultNode = callNode.getLastChild().getNext();

 // Check the first argument is a number
 assertTrue(checkType(arg1Node, compiler, "numberChecker"));
 // Check the second argument is a number
 assertTrue(checkType(arg2Node, compiler, "numberChecker"));
 // Check the result is a number
 assertTrue(checkType(resultNode, compiler, "numberChecker"));
 }

 @Test
 public void testRuntimeTypeCheckWithInterface() {
 // Create a simple Java script with a function that returns an object implementing an interface
 String script = "function MyClass() { this.myMethod = function() {}; } MyClass.prototype.implements__IMyInterface = true;" +
 "function IMyInterface { method() {} }" +
 "function add(x, y) { return new MyClass().myMethod(); }";

 // Compile the script and apply the RuntimeTypeCheck pass
 Node rootNode = compiler.parseTree(script, null, null);
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
 runtimeTypeCheck.process(null, rootNode);

 // Check that the expected type assertions have been added
 Node functionNode = findFunctionNodeByName(rootNode, "add");
 List<Node> checkNodes = findCheckerNodes(functionNode);
 List<String> expectedCheckers = Arrays.asList(
 "objectChecker", "objectChecker", "objectChecker", // x, y, return value
 "myInterfaceChecker" // return value implements IMyInterface
 );
 assertTrue(checkNodes.size() == expectedCheckers.size());
 for (int i = 0; i < checkNodes.size(); i++) {
 assertTrue(checkNodes.get(i).getClass().getName().startsWith(
 "com.google.javascript.rhino.jstype.JSTypeChecker"));
 assertTrue(expectedCheckers.get(i).equals(
 getCheckerName(checkNodes.get(i))));
 }

 // Check that the function returns an object implementing the interface
 FunctionType functionType = (FunctionType) functionNode.getJSType();
 ObjectType returnType = functionType.getReturnType();
 assertTrue(returnType.isInstanceType());
 assertTrue(returnType.getReferenceName().equals("MyClass"));
 assertTrue(returnType.getConstructor().getSource().getName().equals("MyClass"));
 assertTrue(returnType.getConstructor().getProperties().get("implements__IMyInterface") != null);

 // Check that type assertions are respecting the specified types
 Node callNode = findCallNodeByName(functionNode, "add");
 Node arg1Node = callNode.getFirstChild();
 Node arg2Node = callNode.getLastChild();
 Node resultNode = callNode.getLastChild().getNext();

 // Check the first argument is an object
 assertTrue(checkType(arg1Node, compiler, "objectChecker"));
 // Check the second argument is an object
 assertTrue(checkType(arg2Node, compiler, "objectChecker"));
 // Check the result is an object implementing the interface
 assertTrue(checkType(resultNode, compiler, "myInterfaceChecker"));
 }

 @Test
 public void testRuntimeTypeCheckWithError() {
 // Create a simple Java script with a function that returns an error
 String script = "function add(x, y) { throw new Error('add error'); }";

 // Compile the script and apply the RuntimeTypeCheck pass
 Node rootNode = compiler.parseTree(script, null, null);
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
 runtimeTypeCheck.process(null, rootNode);

 // Check that no type assertions have been added
 Node functionNode = findFunctionNodeByName(rootNode, "add");
 List<Node> checkNodes = findCheckerNodes(functionNode);
 assertTrue(checkNodes.isEmpty());

 // Check that an error is thrown when running the script
 try {
 Node callNode = findCallNodeByName(functionNode, "add");
 Node arg1Node = callNode.getFirstChild();
 Node arg2Node = callNode.getLastChild();
 compiler.run(rootNode, null);
 fail("Expected an error to be thrown");
 } catch (CompilationError e) {
 // Expected
 }
 }

 private Node findFunctionNodeByName(Node rootNode, String name) {
 return Iterables.find(NodeUtil.getFunctionDeclarations(rootNode), node -> node.getFirstChild()
 .getFirstChild().getNodeType() == Token.IDENT && node.getFirstChild().getFirstChild()
 .getStringValue().equals(name), null);
 }

 private Node findCallNodeByName(Node functionNode, String name) {
 return Iterables.find(NodeUtil.getCalls(functionNode), node -> node.getLastChild().getFirstChild()
 .getNodeType() == Token.IDENT && node.getLastChild().getFirstChild()
 .getStringValue().equals(name), null);
 }

 private List<Node> findCheckerNodes(Node functionNode) {
 return Iterables.filter(NodeUtil.getChildren(functionNode), node -> node.getClass().getName()
 .startsWith("com.google.javascript.rhino.jstype.JSTypeChecker"));
 }

 private String getCheckerName(Node checkerNode) {
 return ((JSTypeChecker) checkerNode).getCheckerName();
 }

 private boolean checkType(Node node, AbstractCompiler compiler, String checkerName) {
 Node checkNode = NodeUtil.newQualifiedNameNode("typecheck." + checkerName, -1, -1);
 Node callNode = new Node(Token.CALL, checkNode, node);
 try {
 compiler.run(NodeUtil.getParentNodes(node, 1)[0], callNode);
 return true;
 } catch (CompilationError e) {
 return false;
 }
 }
}