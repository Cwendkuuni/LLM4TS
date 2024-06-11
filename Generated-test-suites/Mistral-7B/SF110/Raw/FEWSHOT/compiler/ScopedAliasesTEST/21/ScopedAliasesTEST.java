package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test cases for ScopedAliases class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given a valid ScopedAliases instance
    AbstractCompiler compiler = new NoOpCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new TestAliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When processing a valid program
    Node root = getTestNode("");
    scopedAliases.process(null, root);

    // Then no errors occurred
    assertTrue(scopedAliases.getTraversal().hasErrors() == false);

    // And the aliases were applied correctly
    List<Node> aliasDefinitions = scopedAliases.getTraversal().getAliasDefinitionsInOrder();
    assertEquals(5, aliasDefinitions.size());
    assertEquals("Div", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinitions.get(1).getString());
    assertEquals("DIV", aliasDefinitions.get(2).getString());
    assertEquals("dom", aliasDefinitions.get(3).getString());
    assertEquals("goog.dom", aliasDefinitions.get(4).getString());

    // And the scopes were collapsed correctly
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(1, scopeCall.getChildCount());
    Node scopeClosureBlock = scopeCall.getChildAtIndex(0);
    assertEquals(11, scopeClosureBlock.getChildCount());
    assertEquals("goog.scope(function() {", scopeClosureBlock.getString());

    // And the aliases were used correctly
    List<AliasUsage> aliasUsages = scopedAliases.getTraversal().getAliasUsages();
    assertEquals(5, aliasUsages.size());
    AliasUsage aliasUsage1 = aliasUsages.get(0);
    assertEquals("divElement", aliasUsage1.getClass().getName());
    AliasUsage aliasUsage2 = aliasUsages.get(1);
    assertEquals("Div", aliasUsage2.getClass().getName());
    AliasUsage aliasUsage3 = aliasUsages.get(2);
    assertEquals("div", aliasUsage3.getClass().getName());
    AliasUsage aliasUsage4 = aliasUsages.get(3);
    assertEquals("dom", aliasUsage4.getClass().getName());
    AliasUsage aliasUsage5 = aliasUsages.get(4);
    assertEquals("goog.dom", aliasUsage5.getClass().getName());

    // And the type nodes were fixed correctly
    JSDocInfo jsdocInfo = root.getFirstChild().getJSDocInfo();
    Node typeNode = jsdocInfo.getTypeNodes().get(0);
    assertEquals("goog.dom.TagName", typeNode.getClassName());
    Node typeNode2 = jsdocInfo.getTypeNodes().get(1);
    assertEquals("Div", typeNode2.getString());

    // When processing a program with errors
    root = getTestNode("goog.scope(function() { var x = goog.x; });");
    scopedAliases.process(null, root);

    // Then errors occurred
    assertTrue(scopedAliases.getTraversal().hasErrors() == true);

    // And the errors were reported correctly
    List<DiagnosticType> errors = scopedAliases.getTraversal().getErrors();
    assertEquals(1, errors.size());
    DiagnosticType error = errors.get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getTypeName());
    assertEquals("x", error.getMessageArguments()[0]);
  }

  private Node getTestNode(String code) {
    String testCode = "function test() {" + code + " test(); }";
    Node root = new Parser(null).parseCompilationUnit(testCode, new SourceFile("Test.java", null));
    return root;
  }

  private static class TestAliasTransformationHandler implements AliasTransformationHandler {
    private int aliasCount = 0;

    @Override
    public AliasTransformation logAliasTransformation(
        String sourceFileName, SourcePosition sourcePosition) {
      aliasCount++;
      return new AliasTransformation("TestAliasTransformation", sourceFileName, sourcePosition);
    }
  }

  private static class NoOpCompiler implements AbstractCompiler {
    @Override
    public void report(Node n, DiagnosticType error, String... arguments) {
      // Do nothing
    }

    @Override
    public void reportCodeChange() {
      // Do nothing
    }
  }
}