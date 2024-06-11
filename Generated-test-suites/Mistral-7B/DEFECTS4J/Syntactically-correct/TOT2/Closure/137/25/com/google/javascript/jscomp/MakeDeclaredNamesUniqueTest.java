import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MakeDeclaredNamesUniqueTest {

 private NodeTraversal nodeTraversal;
 private MakeDeclaredNamesUnique makeDeclaredNamesUnique;

 @Before
 public void setup() {
 nodeTraversal = mock(NodeTraversal.class);
 makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setTraversal(nodeTraversal);
 }

 @Test
 public void testFunctionDeclarationWithUniqueNames() {
 Node functionNode = createFunctionNode("function foo() {}");
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);
 assertUniqueNames(functionNode, "foo");
 }

 @Test
 public void testFunctionDeclarationWithConflictingNames() {
 Node functionNode = createFunctionNode("function foo() { var x = x; }");
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);
 assertUniqueNames(functionNode, "x", "foo");
 }

 @Test
 public void testVariableDeclarationWithUniqueNames() {
 Node blockNode = createBlockNode("var x = 1, y = 2, z = 3;");
 makeDeclaredNamesUnique.traverse(nodeTraversal, blockNode, null);
 assertUniqueNames(blockNode, "x", "y", "z");
 }

 @Test
 public void testVariableDeclarationWithConflictingNames() {
 Node blockNode = createBlockNode("var x = x, y = 2, z = x;");
 makeDeclaredNamesUnique.traverse(nodeTraversal, blockNode, null);
 assertUniqueNames(blockNode, "y", "x_1", "z");
 }

 @Test
 public void testFunctionDeclarationWithGlobalVariable() {
 Node functionNode = createFunctionNode("function foo() { var x = x; }");
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);
 assertGlobalVariableUnchanged("x");
 }

 @Test
 public void testVariableDeclarationWithGlobalFunction() {
 Node blockNode = createBlockNode("var foo = function() {};");
 makeDeclaredNamesUnique.traverse(nodeTraversal, blockNode, null);
 assertGlobalFunctionUnchanged("foo");
 }

 @Test(expected = AssertionError.class)
 public void testVariableDeclarationWithConflictingGlobalName() {
 Node blockNode = createBlockNode("var x = x;");
 makeDeclaredNamesUnique.traverse(nodeTraversal, blockNode, null);
 }

 @Test
 public void testCatchBlockWithUniqueNames() {
 Node tryNode = createTryNode();
 Node catchNode = createCatchNode("catch (x) {}");
 makeDeclaredNamesUnique.traverse(nodeTraversal, tryNode, null);
 makeDeclaredNamesUnique.traverse(nodeTraversal, catchNode, tryNode);
 assertUniqueNames(tryNode, "x");
 }

 @Test
 public void testCatchBlockWithConflictingNames() {
 Node tryNode = createTryNode();
 Node catchNode = createCatchNode("catch (x) { var x = 1; }");
 makeDeclaredNamesUnique.traverse(nodeTraversal, tryNode, null);
 makeDeclaredNamesUnique.traverse(nodeTraversal, catchNode, tryNode);
 assertUniqueNames(tryNode, "x_1");
 }

 private Node createFunctionNode(String nodeText) {
 Node node = createNodeFromText(nodeText);
 node.setType(Token.FUNCTION);
 return node;
 }

 private Node createBlockNode(String nodeText) {
 Node node = createNodeFromText(nodeText);
 node.setType(Token.BLOCK);
 return node;
 }

 private Node createTryNode() {
 Node node = createNodeFromText("try {}");
 node.setType(Token.TRY);
 return node;
 }

 private Node createCatchNode(String nodeText) {
 Node node = createNodeFromText(nodeText);
 node.setType(Token.CATCH);
 return node;
 }

 private Node createNodeFromText(String nodeText) {
 Node node = new Node();
 node.setFirstChild(new Node(Token.TEXT, nodeText));
 return node;
 }

 private void assertUniqueNames(Node node, String... names) {
 for (String name : names) {
 assertNotNull("Expected name '" + name + "' to be set",
 node.getFirstChildByName(name));
 }
 }

 private void assertGlobalVariableUnchanged(String variableName) {
 Node node = getRootScope().getChildByName("__global__");
 assertNotNull("Expected to find global scope", node);
 Node variableNode = node.getFirstChildByName(variableName);
 assertNotNull("Expected variable '" + variableName + "' to be set", variableNode);
 }

 private void assertGlobalFunctionUnchanged(String functionName) {
 Node node = getRootScope().getChildByName("__global__");
 assertNotNull("Expected to find global scope", node);
 Node functionNode = node.getFirstChildByName(functionName);
 assertNotNull("Expected function '" + functionName + "' to be set", functionNode);
 }

 private Node getRootScope() {
 return nodeTraversal.getScopeRoot();
 }
}