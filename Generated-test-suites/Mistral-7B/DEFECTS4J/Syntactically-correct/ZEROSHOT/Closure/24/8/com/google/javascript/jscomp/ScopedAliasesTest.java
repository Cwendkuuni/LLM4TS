import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test the empty case.
    Node root = Node.NULL;
    scopedAliases.process(Node.NULL, root);
    assertEquals(0, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Test a simple scoped alias.
    Node externs = Node.objectLiteral();
    Node rootWithScope = Node.script(
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});");

    scopedAliases.process(externs, rootWithScope);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom.TagName.DIV", aliasDefinition.getQualifiedName());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      Node aliasedNode = ((AliasedNode) aliasUsage).aliasDefinition;
      assertEquals("DIV", aliasedNode.getFirstChild().getString());
      assertEquals("goog.dom.TagName.DIV", aliasedNode.getQualifiedName());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      fail("Unexpected alias usage type.");
    }

    // Test a scoped alias with a type node.
    externs = Node.objectLiteral();
    rootWithScope = Node.script(
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "  @type {{ DOM: { createElement: function(tag: TagName) { ... } }}}" +
            "});");

    scopedAliases.process(externs, rootWithScope);
    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("DIV", aliasDefinition.getFirstChild().getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinition.getQualifiedName());

    aliasUsage = aliasUsages.get(0);
    aliasDefinition = ((AliasedTypeNode) aliasUsage).aliasDefinition;
    assertEquals("DIV", aliasDefinition.getFirstChild().getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinition.getQualifiedName());

    Node typeNode = aliasDefinition.getFirstChild().getNext();
    assertEquals("DOM", typeNode.getFirstChild().getString());
    assertEquals("{ createElement: function(tag: TagName) { ... } }",
        typeNode.getLastChild().getString());
  }

  @Test
  public void testInvalidScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test an empty scoped block.
    Node rootWithEmptyScope = Node.script("goog.scope()");
    scopedAliases.process(Node.NULL, rootWithEmptyScope);
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());

    // Test a scoped block with a non-anonymous function.
    Node rootWithNonAnonymousScope = Node.script("goog.scope(function foo() { });");
    scopedAliases.process(Node.NULL, rootWithNonAnonymousScope);
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getErrors().size());
    List<DiagnosticType> errors = scopedAliases.getErrors();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));

    // Test a scoped block with a function that references 'this'.
    Node rootWithThisScope = Node.script("goog.scope(function() { this.foo = 42; });");
    scopedAliases.process(Node.NULL, rootWithThisScope);
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getErrors().size());
    errors = scopedAliases.getErrors();
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0));

    // Test a scoped block with a function that uses 'return'.
    Node rootWithReturnScope = Node.script("goog.scope(function() { return 42; });");
    scopedAliases.process(Node.NULL, rootWithReturnScope);
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getErrors().size());
    errors = scopedAliases.getErrors();
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0));

    // Test a scoped block with a function that uses 'throw'.
    Node rootWithThrowScope = Node.script("goog.scope(function() { throw new Error(); });");
    scopedAliases.process(Node.NULL, rootWithThrowScope);
    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(0, scopedAliases.getScopeCalls().size());
    assertEquals(0, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getErrors().size());
    errors = scopedAliases.getErrors();
    assertEquals(GOOG_SCOPE_USES_THROW, errors.get(0));

    // Test an invalid alias definition.
    Node rootWithInvalidAliasDefinition = Node.script(
        "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV)" +
            "  var DIV2 = DIV;" +
            "});");
    scopedAliases.process(Node.NULL, rootWithInvalidAliasDefinition);
    aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("DIV", aliasDefinition.getFirstChild().getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinition.getQualifiedName());

    aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    aliasUsage = aliasUsages.get(0);
    aliasDefinition = ((AliasedNode) aliasUsage).aliasDefinition;
    assertEquals("DIV", aliasDefinition.getFirstChild().getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinition.getQualifiedName());

    aliasUsage = aliasUsages.get(1);
    aliasDefinition = ((AliasedNode) aliasUsage).aliasDefinition;
    assertEquals("DIV2", aliasDefinition.getFirstChild().getString());
    assertEquals("DIV", aliasDefinition.getQualifiedName());

    assertEquals(1, scopedAliases.getErrors().size());
    errors = scopedAliases.getErrors();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0));
  }
}