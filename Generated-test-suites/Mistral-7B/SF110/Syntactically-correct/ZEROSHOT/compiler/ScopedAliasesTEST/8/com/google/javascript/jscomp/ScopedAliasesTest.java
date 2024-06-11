import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Test case for valid usage of ScopedAliases
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Parser.parseCompilationUnit(sourceCode, null, null);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();

    assertEquals(1, scopeCalls.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(2, aliasDefinitionsInOrder.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node aliasDefinitionNode1 = aliasDefinitionsInOrder.get(0);
    Node aliasDefinitionNode2 = aliasDefinitionsInOrder.get(1);
    AliasUsage aliasUsage = aliasUsages.get(0);

    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals("goog.dom", aliasDefinitionNode1.getString());
    assertEquals("DIV", aliasDefinitionNode2.getString());
    assertEquals(aliasDefinitionNode1, aliasUsage.getAliasDefinition());
  }

  @Test
  public void testScopedAliasesBadUsage() {
    // Test case for invalid usage of ScopedAliases
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  goog.scope(function() {" +
        "    var dom2 = goog.dom;" +
        "  });" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Parser.parseCompilationUnit(sourceCode, null, null);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();

    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(1, aliasDefinitionsInOrder.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node aliasDefinitionNode = aliasDefinitionsInOrder.get(0);

    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals("dom", aliasDefinitionNode.getString());

    // Check for errors
    assertTrue(scopedAliases.hasErrors());
    DiagnosticType error = scopedAliases.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, error);
  }

  @Test
  public void testScopedAliasesAliasRedefined() {
    // Test case for alias redefined error
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var DIV = 'new_DIV';" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Parser.parseCompilationUnit(sourceCode, null, null);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();

    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(2, aliasDefinitionsInOrder.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node aliasDefinitionNode1 = aliasDefinitionsInOrder.get(0);
    Node aliasDefinitionNode2 = aliasDefinitionsInOrder.get(1);

    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals("dom", aliasDefinitionNode1.getString());
    assertEquals("DIV", aliasDefinitionNode2.getString());

    // Check for errors
    assertTrue(scopedAliases.hasErrors());
    DiagnosticType error = scopedAliases.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, error);
  }

  @Test
  public void testScopedAliasesNonAliasLocal() {
    // Test case for non-alias local variable error
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var divElement = document.createElement(DIV);" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Parser.parseCompilationUnit(sourceCode, null, null);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();

    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(2, aliasDefinitionsInOrder.size());

    Node scopeCallNode = scopeCalls.get(0);
    Node aliasDefinitionNode1 = aliasDefinitionsInOrder.get(0);
    Node aliasDefinitionNode2 = aliasDefinitionsInOrder.get(1);

    assertEquals("goog.scope", scopeCallNode.getFirstChild().getQualifiedName());
    assertEquals("dom", aliasDefinitionNode1.getString());
    assertEquals("DIV", aliasDefinitionNode2.getString());

    // Check for errors
    assertTrue(scopedAliases.hasErrors());
    DiagnosticType error = scopedAliases.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, error);
  }

  @Test
  public void testScopedAliasesUsesReturn() {
    // Test case for using return statement in a goog.scope block
    String sourceCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  return dom.TagName.DIV;" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Parser.parseCompilationUnit(sourceCode, null, null);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();

    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(0, aliasDefinitionsInOrder.size());

    Node scopeCallNode = scopeCalls.get(0);

    // Check for errors
    assertTrue(scopedAliases.hasErrors());
    DiagnosticType error = scopedAliases.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USES_RETURN, error);
  }

  @Test
  public void testScopedAliasesUsesThrow() {
    // Test case for using throw statement in a goog.scope block
    String sourceCode =
        "goog.scope(function() {" +
        "  try {" +
        "    var dom = goog.dom;" +
        "    throw new Error('Test error');" +
        "  } catch (e) {" +
        "    // Do something with the error" +
        "  }" +
        "});";

    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = Parser.parseCompilationUnit(sourceCode, null, null);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();

    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());
    assertEquals(0, aliasDefinitionsInOrder.size());

    Node scopeCallNode = scopeCalls.get(0);

    // Check for errors
    assertTrue(scopedAliases.hasErrors());
    DiagnosticType error = scopedAliases.getErrors().get(0).getType();
    assertEquals(GOOG_SCOPE_USES_THROW, error);
  }
}