import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given a valid ScopedAliases instance
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a valid Node tree with a goog.scope block
    Node root = Node.createTree(
        "function test() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});" +
            "testCase('test', function() {" +
            "  var div = goog.dom.createElement(goog.dom.TagName.DIV);" +
            "  assertEquals('DIV', div.tagName);" +
            "});" +
            "}");
    scopedAliases.process(null, root);

    // Then the Node tree should be transformed correctly
    assertTrue(scopedAliases.hasErrors());
    Node transformedRoot = scopedAliases.getLastTransformedNode();
    Node googScopeCall = transformedRoot.getFirstChild();
    Node anonymousFunction = googScopeCall.getLastChild();
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();

    // Check that the goog.scope function call and its parameter are transformed
    assertEquals("goog.scope", googScopeCall.getFirstChild().getString());
    assertEquals("function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "}",
        anonymousFunction.toString());

    // Check that the alias definitions are transformed and in order
    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("dom", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("DIV", aliasDefinitionsInOrder.get(1).getString());

    // Check that the alias usages are transformed correctly
    assertEquals(2, aliasUsages.size());
    AliasUsage aliasUsage1 = aliasUsages.get(0);
    Node aliasReference1 = aliasUsage1.aliasReference;
    Node aliasDefinition1 = aliasUsage1.aliasDefinition;
    assertEquals("dom", aliasReference1.getString());
    assertEquals("dom", aliasDefinition1.getString());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    Node aliasReference2 = aliasUsage2.aliasReference;
    Node aliasDefinition2 = aliasUsage2.aliasDefinition;
    assertEquals("DIV", aliasReference2.getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinition2.getString());

    // Check that the testCase function call uses the transformed aliases
    Node testCaseCall = transformedRoot.getLastChild();
    assertEquals("testCase", testCaseCall.getFirstChild().getString());
    Node testCaseFunction = testCaseCall.getLastChild();
    assertEquals("test", testCaseFunction.getFirstChild().getString());
    Node testCaseBody = testCaseFunction.getLastChild();
    Node div = testCaseBody.getLastChild();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)", div.toString());

  }

  @Test
  public void testScopedAliasesImproperlyUsed() {
    // Given a ScopedAliases instance
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a Node tree with a goog.scope block used improperly
    Node root = Node.createTree(
        "function test() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var d = dom.createElement(goog.dom.TagName.DIV);" +
            "});" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var d = dom.createElement(goog.dom.TagName.DIV);" +
            "});" +
            "testCase('test', function() {" +
            "  var div = goog.dom.createElement(goog.dom.TagName.DIV);" +
            "  assertEquals('DIV', div.tagName);" +
            "});" +
            "}");

    // Then an error should be reported for the improper use of goog.scope
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));
    assertEquals(2, scopedAliases.getLastTransformedNode().getChildCount());
    assertEquals(2, scopedAliases.getScopeCalls().size());

  }

  @Test
  public void testScopedAliasesBadParameters() {
    // Given a ScopedAliases instance
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a Node tree with a goog.scope block that has bad parameters
    Node root = Node.createTree(
        "function test() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var d = dom.createElement(goog.dom.TagName.DIV);" +
            "});" +
            "goog.scope(" +
            "  function() {" +
            "    var dom = goog.dom;" +
            "    var d = dom.createElement(goog.dom.TagName.DIV);" +
            "  }" +
            ");" +
            "testCase('test', function() {" +
            "  var div = goog.dom.createElement(goog.dom.TagName.DIV);" +
            "  assertEquals('DIV', div.tagName);" +
            "});" +
            "}");

    // Then an error should be reported for the bad parameters in the goog.scope function call
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));
    assertEquals(3, scopedAliases.getLastTransformedNode().getChildCount());

  }

  @Test
  public void testScopedAliasesRedefined() {
    // Given a ScopedAliases instance
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a Node tree with a goog.scope block that redefines an alias
    Node root = Node.createTree(
        "function test() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  var DIV = 'DIV2';" +
            "});" +
            "testCase('test', function() {" +
            "  var div = goog.dom.createElement(goog.dom.TagName.DIV);" +
            "  assertEquals('DIV', div.tagName);" +
            "});" +
            "}");

    // Then an error should be reported for the redefined alias
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));
    assertEquals(3, scopedAliases.getLastTransformedNode().getChildCount());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals("DIV", scopedAliases.getAliasDefinitionsInOrder().get(0).getString());

  }

  @Test
  public void testScopedAliasesNonAliasLocal() {
    // Given a ScopedAliases instance
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a Node tree with a goog.scope block that has a non-alias local
    Node root = Node.createTree(
        "function test() {" +
            "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var div = goog.dom.createElement(goog.dom.TagName.DIV);" +
            "});" +
            "testCase('test', function() {" +
            "  var div = goog.dom.createElement(goog.dom.TagName.DIV);" +
            "  assertEquals('DIV', div.tagName);" +
            "  var d = div;" +
            "});" +
            "}");

    // Then an error should be reported for the non-alias local
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));
    assertEquals(3, scopedAliases.getLastTransformedNode().getChildCount());

  }

  @Test
  public void testScopedAliasesAliasReferenceThis() {
    // Given a ScopedAliases instance
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a Node tree with a goog.scope block that references 'this'
    Node root = Node.createTree(
        "function test() {" +
            "goog.scope(function() {" +
            "  this.dom = goog.dom;" +
            "  var DIV = goog.dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});" +
            "testCase('test', function() {" +
            "  var div = goog.dom.createElement(goog.dom.TagName.DIV);" +
            "  assertEquals('DIV', div.tagName);" +
            "});" +
            "}");

    // Then an error should be reported for the reference to 'this'
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));
    assertEquals(3, scopedAliases.getLastTransformedNode().getChildCount());

  }

  @Test
  public void testScopedAliasesAliasUseReturn() {
    // Given a ScopedAliases instance
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a Node tree with a goog.scope block that uses 'return'
    Node root = Node.createTree(
        "function test() {" +
            "goog.scope(function() {" +
            "  function foo() {" +
            "    return 42;" +
            "  }" +
            "  goog.dom.createElement(goog.dom.TagName.DIV);" +
            "});" +
            "testCase('test', function() {" +
            "  assertEquals(42, foo());" +
            "});" +
            "}");

    // Then an error should be reported for the use of 'return'
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));
    assertEquals(3, scopedAliases.getLastTransformedNode().getChildCount());

  }

  @Test
  public void testScopedAliasesAliasUseThrow() {
    // Given a ScopedAliases instance
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a Node tree with a goog.scope block that uses 'throw'
    Node root = Node.createTree(
        "function test() {" +
            "goog.scope(function() {" +
            "  function foo() {" +
            "    throw new Error('foo');" +
            "  }" +
            "  goog.dom.createElement(goog.dom.TagName.DIV);" +
            "});" +
            "testCase('test', function() {" +
            "  assertThrows(Error.class, function() {" +
            "    foo();" +
            "  });" +
            "});" +
            "}");

    // Then an error should be reported for the use of 'throw'
    assertThrows(IllegalStateException.class, () -> scopedAliases.process(null, root));
    assertEquals(3, scopedAliases.getLastTransformedNode().getChildCount());

  }

}