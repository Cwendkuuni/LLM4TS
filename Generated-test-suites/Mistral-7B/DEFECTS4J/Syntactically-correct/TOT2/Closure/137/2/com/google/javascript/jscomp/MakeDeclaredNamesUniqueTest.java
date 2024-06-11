import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

 private Node rootNode;
 private Scope globalScope;
 private MakeDeclaredNamesUnique makeDeclaredNamesUnique;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 // Create a dummy Node and Scope for testing
 TokenStream ts = new TokenStream(Lists.newArrayList(
 new Token("function", 0, 0),
 new Token("{", 1, 0),
 new Token("var", 1, 3),
 new Token("a", 1, 5),
 new Token("=", 1, 8),
 new Token("1", 1, 10),
 new Token(",", 1, 11),
 new Token("var", 1, 12),
 new Token("b", 1, 15),
 new Token("=", 1, 18),
 new Token("2", 1, 20),
 new Token("}", 1, 21)
 ));
 rootNode = ts.getRootNode();
 globalScope = (Scope) rootNode.getFirstChild();
 makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 }

 @Test
 public void testSimpleRename() {
 // Test simple renaming of a single variable
 Node aNode = rootNode.getFirstChild().getLastChild().getLastChild();
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 assertEquals("a$$0", aNode.getString());
 }

 @Test
 public void testRenameVariablesWithSameName() {
 // Test renaming of variables with the same name in the same scope
 Node aNode1 = rootNode.getFirstChild().getLastChild().getLastChild(0);
 Node aNode2 = rootNode.getFirstChild().getLastChild().getLastChild(1);
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 assertEquals("a$$1", aNode1.getString());
 assertEquals("a$$2", aNode2.getString());
 }

 @Test
 public void testRenameVariablesWithSameNameInDifferentScopes() {
 // Test renaming of variables with the same name in different scopes
 Node functionNode = rootNode.getFirstChild();
 Node functionScope = functionNode.getLastChild();
 Node bNode1 = functionScope.getLastChild().getLastChild(0);
 Node bNode2 = globalScope.getLastChild().getLastChild(1);
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 assertEquals("b$$0", bNode1.getString());
 assertEquals("b$$3", bNode2.getString());
 }

 @Test
 public void testRenameFunctionName() {
 // Test renaming of a function name
 Node functionNode = rootNode.getFirstChild();
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 assertEquals("anonymousFunction$$0", functionNode.getString());
 }

 @Test(expected = AssertionError.class)
 public void testRenameFunctionNameWithSameNameInParentScope() {
 // Test renaming of a function name that collides with a variable name in the
 // parent scope
 Node functionNode = rootNode.getFirstChild();
 Node aNode = globalScope.getLastChild().getLastChild(0);
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 }

 @Test
 public void testRenameFunctionWithArguments() {
 // Test renaming of functions with arguments
 Node functionNode = rootNode.getFirstChild();
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 assertEquals("anonymousFunction$$0", functionNode.getString());
 Node argumentsNode = functionNode.getLastChild();
 assertEquals("arg$$0", argumentsNode.getFirstChild().getString());
 assertEquals("arg$$1", argumentsNode.getLastChild().getString());
 }

 @Test
 public void testRenameCatchClause() {
 // Test renaming of catch clauses
 Node functionNode = rootNode.getFirstChild();
 Node catchNode = functionNode.getLastChild().getLastChild();
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 assertEquals("catchClause$$0", catchNode.getString());
 }

 @Test
 public void testRenameLocalConstant() {
 // Test renaming of local constants
 Node constantNode = rootNode.getFirstChild().getLastChild().getLastChild(1);
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 assertTrue(constantNode.hasProp(Node.IS_CONSTANT_NAME));
 assertEquals("c$$2", constantNode.getString());
 }

 @Test
 public void testRenameGlobalConstant() {
 // Test renaming of global constants
 Node globalConstantNode = globalScope.getLastChild();
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 assertTrue(globalConstantNode.hasProp(Node.IS_CONSTANT_NAME));
 assertEquals("c$$3", globalConstantNode.getString());
 }

 @Test(expected = AssertionError.class)
 public void testRenameGlobalConstantWithSameName() {
 // Test renaming of a global constant with the same name as a local variable
 Node aNode = globalScope.getLastChild().getLastChild(0);
 Node constantNode = globalScope.getLastChild().getLastChild(1);
 makeDeclaredNamesUnique.traverse(NodeTraversal.create(
 rootNode, null, null, null, makeDeclaredNamesUnique), null, null);
 }
}