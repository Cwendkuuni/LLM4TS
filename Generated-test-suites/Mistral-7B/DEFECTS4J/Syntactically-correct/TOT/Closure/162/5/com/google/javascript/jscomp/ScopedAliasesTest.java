package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope.Var;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};
  }

  @Test
  public void testScopedAliases() {
    String input =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "  goog.scope(function() {" +
        "    var dom = goog.dom;" +
        "    var DIV = dom.TagName.DIV;" +
        "    assertEquals(dom.createElement(DIV), dom.createElement(DIV));" +
        "  });" +
        "});";

    List<Node> ast = CompilerOptions.standardOptions()
        .getParser().parse(input, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, ast.get(0));

    assertEquals(1, scopedAliases.getScopeCalls().size());
    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    assertEquals(2, scopeCall.getChildCount());
    Node anonymousFunction = scopeCall.getChildAtIndex(1);
    assertEquals(1, anonymousFunction.getChildCount());
    assertEquals("function() {}", anonymousFunction.toString());

    assertTrue(preprocessorSymbolTable.containsName("dom"));
    Var domVar = preprocessorSymbolTable.getVar("dom");
    assertEquals("dom", domVar.getName());
    assertEquals(null, domVar.getValue());
    assertFalse(domVar.isDefined());

    Var domVarInScope = scopedAliases.getScope().getVar("dom");
    assertNotNull(domVarInScope);
    assertEquals("dom", domVarInScope.getName());
    assertEquals(domVar, domVarInScope);
    assertEquals("com.google.javascript.jscomp.dom.DOMFactory",
        domVarInScope.getInitialValue().getQualifiedName());

    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals(4, aliasDefinition.getChildCount());
    assertEquals("DIV", aliasDefinition.getChildAtIndex(1).getString());
    assertEquals("com.google.javascript.jscomp.dom.TagName.DIV",
        aliasDefinition.getChildAtIndex(3).getQualifiedName());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesBadUsage() {
    String input =
        "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "  }" +
        "});";

    List<Node> ast = CompilerOptions.standardOptions()
        .getParser().parse(input, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, ast.get(0));

    assertThrows(AssertionError.class, () -> {
      scopedAliases.hotSwapScript(null, null);
    });
  }

  @Test
  public void testScopedAliasesImproperUsage() {
    String input =
      "goog.scope(function() {" +
      "  var a = 1;" +
      "  goog.scope(function() {" +
      "    a = 2;" +
      "  });" +
      "});";

    List<Node> ast = CompilerOptions.standardOptions()
        .getParser().parse(input, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, ast.get(0));

    assertEquals(1, scopedAliases.getScopeCalls().size());
    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    assertEquals(2, scopeCall.getChildCount());
    Node anonymousFunction = scopeCall.getChildAtIndex(1);
    assertEquals(1, anonymousFunction.getChildCount());

    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals(4, aliasDefinition.getChildCount());
    assertEquals("a", aliasDefinition.getChildAtIndex(1).getString());
    assertEquals("a", aliasDefinition.getChildAtIndex(3).getString());
    assertFalse(scopedAliases.hasErrors());

    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.getErrors().size());
    DiagnosticType error = compiler.getErrors().get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getName());
  }
}