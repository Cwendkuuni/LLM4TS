import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.testutils.NodeBuilder;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MakeDeclaredNamesUniqueTest {

 private @InjectMocks MakeDeclaredNamesUnique makeDeclaredNamesUnique;
 private NodeTraversal nodeTraversal;
 private Node rootNode;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 nodeTraversal = new NodeTraversal(new TokenStream(new NodeBuilder()));
 rootNode = new NodeBuilder()
     .root(Token.FUNCTION, "testFunction")
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.VAR, "y", Token.NAME, "y")
     .addChild(Token.VAR, "z", Token.NAME, "z")
     .addChild(Token.BLOCK, null)
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .build();
 }

 @Test
 public void testUniqueNamesInFunction() {
 makeDeclaredNamesUnique.traverse(nodeTraversal, rootNode, null);

 // Check that variables have unique names within the function.
 List<String> variableNames = Lists.newArrayList();
 for (Node child = rootNode.getFirstChild(); child != null; child = child.getNext()) {
 if (NodeUtil.isVarDeclaration(child)) {
 variableNames.add(child.getFirstChild().getString());
 }
 }

 // Check that all variable names are unique.
 Set<String> uniqueNames = Sets.newHashSet();
 for (String name : variableNames) {
 // The first occurrence of a name is not considered when checking for uniqueness.
 if (!uniqueNames.contains(name)) {
 uniqueNames.add(name);
 } else {
 throw new AssertionError("Variable name '" + name + "' is not unique.");
 }
 }

 // Check that the function name is not changed since it is not in the scope to be renamed.
 assertThat(rootNode.getFirstChild().getString()).isEqualTo("testFunction");
 }

 @Test
 public void testUniqueNamesInNestedFunctions() {
 Node functionNode = new NodeBuilder()
     .root(Token.FUNCTION, "outerFunction")
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.VAR, "y", Token.NAME, "y")
     .addChild(Token.BLOCK, null)
     .addChild(Token.FUNCTION, "innerFunction")
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.VAR, "z", Token.NAME, "z")
     .addChild(Token.BLOCK, null)
     .build();

 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);

 // Check that variables have unique names within the inner function.
 List<String> innerVariableNames = Lists.newArrayList();
 for (Node child = functionNode.getLastChild().getFirstChild(); child != null; child = child.getNext()) {
 if (NodeUtil.isVarDeclaration(child)) {
 innerVariableNames.add(child.getFirstChild().getString());
 }
 }

 // Check that all variable names are unique.
 Set<String> innerUniqueNames = Sets.newHashSet();
 for (String name : innerVariableNames) {
 // The first occurrence of a name is not considered when checking for uniqueness.
 if (!innerUniqueNames.contains(name)) {
 innerUniqueNames.add(name);
 } else {
 throw new AssertionError("Variable name '" + name + "' is not unique.");
 }
 }

 // Check that the function names are not changed.
 assertThat(functionNode.getFirstChild().getString()).isEqualTo("outerFunction");
 assertThat(functionNode.getLastChild().getFirstChild().getString()).isEqualTo("innerFunction");
 }

 @Test(expected = AssertionError.class)
 public void testDuplicateNamesInFunction() {
 Node functionNode = new NodeBuilder()
     .root(Token.FUNCTION, "testFunction")
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .build();

 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);
 }

 @Test(expected = AssertionError.class)
 public void testDuplicateNamesInNestedFunctions() {
 Node functionNode = new NodeBuilder()
     .root(Token.FUNCTION, "outerFunction")
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.BLOCK, null)
     .addChild(Token.FUNCTION, "innerFunction")
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .build();

 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);
 }

 @Test
 public void testUniqueNamesInBlock() {
 Node blockNode = new NodeBuilder()
     .root(Token.BLOCK, null)
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.VAR, "y", Token.NAME, "y")
     .addChild(Token.VAR, "z", Token.NAME, "z")
     .build();

 makeDeclaredNamesUnique.traverse(nodeTraversal, blockNode, null);

 // Check that variables have unique names within the block.
 List<String> variableNames = Lists.newArrayList();
 for (Node child = blockNode.getFirstChild(); child != null; child = child.getNext()) {
 if (NodeUtil.isVarDeclaration(child)) {
 variableNames.add(child.getFirstChild().getString());
 }
 }

 // Check that all variable names are unique.
 Set<String> uniqueNames = Sets.newHashSet();
 for (String name : variableNames) {
 // The first occurrence of a name is not considered when checking for uniqueness.
 if (!uniqueNames.contains(name)) {
 uniqueNames.add(name);
 } else {
 throw new AssertionError("Variable name '" + name + "' is not unique.");
 }
 }
 }

 @Test
 public void testUniqueNamesInNestedBlock() {
 Node blockNode = new NodeBuilder()
     .root(Token.BLOCK, null)
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.BLOCK, null)
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.VAR, "z", Token.NAME, "z")
     .build();

 makeDeclaredNamesUnique.traverse(nodeTraversal, blockNode, null);

 // Check that variables have unique names within the inner block.
 List<String> innerVariableNames = Lists.newArrayList();
 for (Node child = blockNode.getLastChild().getFirstChild(); child != null; child = child.getNext()) {
 if (NodeUtil.isVarDeclaration(child)) {
 innerVariableNames.add(child.getFirstChild().getString());
 }
 }

 // Check that all variable names are unique.
 Set<String> innerUniqueNames = Sets.newHashSet();
 for (String name : innerVariableNames) {
 // The first occurrence of a name is not considered when checking for uniqueness.
 if (!innerUniqueNames.contains(name)) {
 innerUniqueNames.add(name);
 } else {
 throw new AssertionError("Variable name '" + name + "' is not unique.");
 }
 }

 // Check that the block names are not changed.
 assertThat(blockNode.getFirstChild().getString()).isEqualTo("block");
 }

 @Test(expected = AssertionError.class)
 public void testDuplicateNamesInBlock() {
 Node blockNode = new NodeBuilder()
     .root(Token.BLOCK, null)
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .build();

 makeDeclaredNamesUnique.traverse(nodeTraversal, blockNode, null);
 }

 @Test(expected = AssertionError.class)
 public void testDuplicateNamesInNestedBlock() {
 Node blockNode = new NodeBuilder()
     .root(Token.BLOCK, null)
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .addChild(Token.BLOCK, null)
     .addChild(Token.VAR, "x", Token.NAME, "x")
     .build();

 makeDeclaredNamesUnique.traverse(nodeTraversal, blockNode, null);
 }

}