import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler.LogAliasTransformation.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() throws Exception {
    compiler = Mockito.mock(AbstractCompiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandler = new AliasTransformationHandler() {
      @Override
      public LogAliasTransformation logAliasTransformation(
          String fileName, SourcePosition position) {
        return new LogAliasTransformation(fileName, position);
      }
    };
  }

  @Test
  public void testScopedAliases() throws Exception {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test an empty tree.
    Node root = Node.NULL;
    scopedAliases.process(Node.NULL, root);
    assertTrue(scopedAliases.hasErrors());

    // Test a tree with a call to goog.scope but no alias.
    Node externs = Node.newText("jscomp.externs = {};");
    Node rootWithGoogScope = Node.newText("goog.scope(function() { jscomp.externs = {}; });");
    scopedAliases.process(externs, rootWithGoogScope);
    assertTrue(scopedAliases.hasErrors());

    // Test a tree with a call to goog.scope that contains an alias.
    Node aliasDefinition = Node.newText("var DIV = goog.dom.TagName.DIV;");
    Node rootWithGoogScopeAndAlias = Node.newText(
        "goog.scope(function() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});");
    Node rootWithGoogScopeAndAliasProcessed = Node.newText(
        "goog.dom.createElement(goog.dom.TagName.DIV);");
    scopedAliases.process(externs, rootWithGoogScopeAndAlias);
    assertFalse(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(rootWithGoogScopeAndAliasProcessed, root);

    // Test a tree with multiple calls to goog.scope.
    Node rootWithMultipleGoogScopeCalls = Node.newText(
        "goog.scope(function() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    var DIV = goog.dom.TagName.DIV;" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});");
    Node rootWithMultipleGoogScopeCallsProcessed = Node.newText(
        "goog.dom.createElement(goog.dom.TagName.DIV);");
    scopedAliases.process(externs, rootWithMultipleGoogScopeCalls);
    assertFalse(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(2, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(rootWithMultipleGoogScopeCallsProcessed, root);

    // Test a tree with a call to goog.scope that references 'this'.
    Node rootWithGoogScopeAndThis = Node.newText(
        "goog.scope(function() {" +
        "  this.test = 'hello';" +
        "});");
    scopedAliases.process(externs, rootWithGoogScopeAndThis);
    assertTrue(scopedAliases.hasErrors());

    // Test a tree with a call to goog.scope that uses 'return'.
    Node rootWithGoogScopeAndReturn = Node.newText(
        "goog.scope(function() {" +
        "  return 1;" +
        "});");
    scopedAliases.process(externs, rootWithGoogScopeAndReturn);
    assertTrue(scopedAliases.hasErrors());

    // Test a tree with a call to goog.scope that uses 'throw'.
    Node rootWithGoogScopeAndThrow = Node.newText(
        "goog.scope(function() {" +
        "  throw new Error('error');" +
        "});");
    scopedAliases.process(externs, rootWithGoogScopeAndThrow);
    assertTrue(scopedAliases.hasErrors());

    // Test a tree with a call to goog.scope that assigns an alias multiple times.
    Node rootWithGoogScopeAndAliasRedefined = Node.newText(
        "goog.scope(function() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "});");
    scopedAliases.process(externs, rootWithGoogScopeAndAliasRedefined);
    assertTrue(scopedAliases.hasErrors());
  }
}