import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    when(preprocessorSymbolTable.addReference(Mockito.any(Node.class)))
        .thenReturn(null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test the case where there is a valid goog.scope block
    Node rootNode = createNode("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});");
    scopedAliases.process(null, rootNode);
    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasDefinitionNode = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("dom", aliasDefinitionNode.getFirstChild().getString());
    assertEquals("goog.dom", aliasDefinitionNode.getLastChild().getQualifiedName());

    Node aliasUsageNode = traversal.getAliasUsages().get(0);
    assertEquals("dom", aliasUsageNode.getString());

    Node scopeCallNode = traversal.getScopeCalls().get(0);
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());

    // Test the case where the goog.scope call is not alone in a single statement
    rootNode = createNode("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "});\n" +
        "var x = 1;");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", traversal.getErrors().get(0).getMessage());

    // Test the case where the goog.scope call has bad parameters
    rootNode = createNode("goog.scope(function(a) {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", traversal.getErrors().get(0).getMessage());

    // Test the case where the body of a goog.scope function references this
    rootNode = createNode("goog.scope(function() {\n" +
        "  this.x = 1;\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", traversal.getErrors().get(0).getMessage());

    // Test the case where the body of a goog.scope function uses return
    rootNode = createNode("goog.scope(function() {\n" +
        "  return 1;\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", traversal.getErrors().get(0).getMessage());

    // Test the case where the body of a goog.scope function uses throw
    rootNode = createNode("goog.scope(function() {\n" +
        "  throw new Error('test');\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", traversal.getErrors().get(0).getMessage());

    // Test the case where an alias is assigned a value more than once
    rootNode = createNode("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  var x = 1;\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", traversal.getErrors().get(0).getMessage());

    // Test the case where a non-alias local is in a goog.scope
    rootNode = createNode("goog.scope(function() {\n" +
        "  var x = 1;\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", traversal.getErrors().get(0).getMessage());

    // Test the case where a goog.scope call is not alone in a single statement
    rootNode = createNode("goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "});\n" +
        "var x = 1;");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", traversal.getErrors().get(0).getMessage());

    // Test the case where the goog.scope call has an anonymous function with parameters
    rootNode = createNode("goog.scope(function(a, b) {\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", traversal.getErrors().get(0).getMessage());

    // Test the case where the goog.scope function has a return statement
    rootNode = createNode("goog.scope(function() {\n" +
        "  return 1;\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", traversal.getErrors().get(0).getMessage());

    // Test the case where the goog.scope function has a throw statement
    rootNode = createNode("goog.scope(function() {\n" +
        "  throw new Error('test');\n" +
        "});");
    scopedAliases.process(null, rootNode);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getErrors().size());

    assertEquals(DiagnosticType.error, traversal.getErrors().get(0).getType());
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", traversal.getErrors().get(0).getMessage());

  }

  private Node createNode(String nodeContent) {
    CompilerOptions options = new CompilerOptions();
    Node rootNode = NodeUtil.parseJs(options, nodeContent);
    NodeUtil.resolveScope(options, rootNode);
    return rootNode;
  }

  private Traversal getTraversal(ScopedAliases scopedAliases) {
    return scopedAliases.getTraversal();
  }
}