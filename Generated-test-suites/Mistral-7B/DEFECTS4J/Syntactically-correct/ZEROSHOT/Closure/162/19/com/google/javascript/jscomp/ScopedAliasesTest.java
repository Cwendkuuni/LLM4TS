package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePositionUtil;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Test class for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    // Set up mocks
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(
        AliasTransformationHandler.class);

    CompilerOptions compilerOptions = new CompilerOptions();
    compilerOptions.setAliasTransformation(AliasTransformation.REWRITE_CHAINED_CALLS);
    compilerOptions.setAliasTransformationHandler(transformationHandler);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, preprocessorSymbolTable, transformationHandler);

    // Create test root node
    Node root = createTestNode();

    // Run scopedAliases
    scopedAliases.process(null, root);

    // Validate results
    Collection<Node> aliasDefinitionsInOrder = scopedAliases.getTraversal()
        .getAliasDefinitionsInOrder();
    List<Node> scopeCalls = scopedAliases.getTraversal().getScopeCalls();
    List<Node> aliasUsages = scopedAliases.getTraversal().getAliasUsages();

    // Validate alias definitions in order
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getQualifiedName());

    // Validate scope calls
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());
    Node anonymousFnNode = scopeCall.getLastChild();
    assertEquals(1, anonymousFnNode.getChildCount());
    assertEquals("function() {\n}", anonymousFnNode.getString());

    // Validate alias usages
    assertEquals(1, aliasUsages.size());
    Node aliasUsage = aliasUsages.get(0);
    assertEquals("goog.dom", aliasUsage.getParent().getTypeName());

    // Validate no errors
    assertNull(compiler.getLastError());

    // Validate transformation handler log
    verifyLoggedAliasTransformation(transformationHandler, aliasDefinition);
  }

  private Node createTestNode() {
    String code = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node externs = new Node(Token.SCRIPT, null, null, null, code, 0);
    Node root = new Node(Token.SCRIPT, null, null, null, null, 0);
    root.addChild(externs);

    return root;
  }

  private void verifyLoggedAliasTransformation(
      AliasTransformationHandler transformationHandler, Node aliasDefinition) {
    when(transformationHandler.logAliasTransformation(
        mock(String.class), mock(SourcePosition.class)))
        .thenAnswer(new Answer<AliasTransformation>() {
          @Override
          public AliasTransformation answer(InvocationOnMock invocation) throws Throwable {
            AliasTransformation aliasTransformation = new AliasTransformation();
            aliasTransformation.setAlias("goog.dom");
            aliasTransformation.setAliasName("dom");
            aliasTransformation.setAliasQualifiedName("goog.dom");
            aliasTransformation.setSourcePosition(SourcePositionUtil.create(
                0, 0, 0, 0));
            return aliasTransformation;
          }
        });

    verify(transformationHandler).logAliasTransformation(
        eq("goog.js"), eq(SourcePositionUtil.create(
            1, 1, 0, 0)));
  }
}