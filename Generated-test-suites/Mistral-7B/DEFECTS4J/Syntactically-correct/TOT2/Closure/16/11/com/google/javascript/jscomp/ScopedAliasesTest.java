import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Traversal;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScopedAliasesTest {

 private AbstractCompiler compiler = mock(AbstractCompiler.class);
 private PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
 private AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

 @Test
 public void testScopedAliases() {
 ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
 Traversal traversal = new Traversal();
 // Basic functionality test
 Node rootNode = createTestNode("goog.scope(function() {\n" +
 "var x = goog.dom;\n" +
 "var y = goog.dom.createElement;\n" +
 "y('div');\n" +
 "})()");
 scopedAliases.process(null, rootNode);
 traversal.setRoot(rootNode);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 assertAliasDefinitionsInOrder(traversal, "x", "goog.dom");
 assertAliasDefinitionsInOrder(traversal, "y", "goog.dom.createElement");
 assertNoScopeCalls(traversal);

 // Test with bad parameters
 Node rootNodeWithBadParameters = createTestNode("goog.scope(function() {\n" +
 "goog.scope('badParameter');\n" +
 "})()");
 scopedAliases.process(null, rootNodeWithBadParameters);
 traversal.setRoot(rootNodeWithBadParameters);
 NodeTraversal.traverse(compiler, rootNodeWithBadParameters, traversal);
 assertError(traversal, "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", "The call to goog.scope must take only a single parameter. It must be an anonymous function that itself takes no parameters.");

 // Test with non-alias local variable
 Node rootNodeWithNonAliasLocal = createTestNode("goog.scope(function() {\n" +
 "var x = 5;\n" +
 "goog.scope('x');\n" +
 "})()");
 scopedAliases.process(null, rootNodeWithNonAliasLocal);
 traversal.setRoot(rootNodeWithNonAliasLocal);
 NodeTraversal.traverse(compiler, rootNodeWithNonAliasLocal, traversal);
 assertError(traversal, "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", "The local variable x is in a goog.scope and is not an alias.");

 // Test with 'this' reference
 Node rootNodeWithThisReference = createTestNode("goog.scope(function() {\n" +
 "var self = this;\n" +
 "goog.scope('this');\n" +
 "})()");
 scopedAliases.process(null, rootNodeWithThisReference);
 traversal.setRoot(rootNodeWithThisReference);
 NodeTraversal.traverse(compiler, rootNodeWithThisReference, traversal);
 assertError(traversal, "JSC_GOOG_SCOPE_REFERENCES_THIS", "The body of a goog.scope function cannot reference 'this'.");

 // Test with return statement
 Node rootNodeWithReturnStatement = createTestNode("goog.scope(function() {\n" +
 "return 'Hello World';\n" +
 "})()");
 scopedAliases.process(null, rootNodeWithReturnStatement);
 traversal.setRoot(rootNodeWithReturnStatement);
 NodeTraversal.traverse(compiler, rootNodeWithReturnStatement, traversal);
 assertError(traversal, "JSC_GOOG_SCOPE_USES_RETURN", "The body of a goog.scope function cannot use 'return'.");

 // Test with throw statement
 Node rootNodeWithThrowStatement = createTestNode("goog.scope(function() {\n" +
 "throw new Error('Error');\n" +
 "})()");
 scopedAliases.process(null, rootNodeWithThrowStatement);
 traversal.setRoot(rootNodeWithThrowStatement);
 NodeTraversal.traverse(compiler, rootNodeWithThrowStatement, traversal);
 assertError(traversal, "JSC_GOOG_SCOPE_USES_THROW", "The body of a goog.scope function cannot use 'throw'.");

 // Test with alias redefined
 Node rootNodeWithAliasRedefined = createTestNode("goog.scope(function() {\n" +
 "goog.scope('x', function() {\n" +
 "var x = 5;\n" +
 "goog.scope('x');\n" +
 "});\n" +
 "})()");
 scopedAliases.process(null, rootNodeWithAliasRedefined);
 traversal.setRoot(rootNodeWithAliasRedefined);
 NodeTraversal.traverse(compiler, rootNodeWithAliasRedefined, traversal);
 assertError(traversal, "JSC_GOOG_SCOPE_ALIAS_REDEFINED", "The alias x is assigned a value more than once.");
}

 private Node createTestNode(String nodeContent) {
 Node rootNode = new Node(Token.BLOCK, new SourcePosition(1, 1), null);
 Node scriptNode = new Node(Token.SCRIPT, new SourcePosition(1, 1), null);
 scriptNode.setString(nodeContent);
 rootNode.appendChild(scriptNode);
 return rootNode;
}

 private void assertAliasDefinitionsInOrder(Traversal traversal, String... expectedAliasNames) {
 List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
 StringBuilder aliasDefinitionNames = new StringBuilder();
 for (Node node : aliasDefinitions) {
 aliasDefinitionNames.append(node.getString()).append(" ");
 }
 assertArrayEquals(expectedAliasNames, aliasDefinitionNames.toString().split(" "));
}

 private void assertNoScopeCalls(Traversal traversal) {
 List<Node> scopeCalls = traversal.getScopeCalls();
 assertFalse(scopeCalls.size() > 0);
}

 private void assertError(Traversal traversal, DiagnosticType errorType, String... expectedErrorArguments) {
 assertTrue(traversal.hasErrors());
 DiagnosticInfo error = traversal.getErrors().get(0);
 assertEquals(errorType, error.getType());
 assertArrayEquals(expectedErrorArguments, error.getArguments());
}

}