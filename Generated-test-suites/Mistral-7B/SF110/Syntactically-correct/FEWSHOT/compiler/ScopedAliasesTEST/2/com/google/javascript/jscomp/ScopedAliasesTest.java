package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.Token;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given a class with a ScopedAliases instance
    Class<HotSwapCompilerPass> hotSwapCompilerPassClass = HotSwapCompilerPass.class;
    Class<AbstractCompiler> abstractCompilerClass = AbstractCompiler.class;
    Class<ScopedAliases> scopedAliasesClass = ScopedAliases.class;
    Object[] constructorArgs = new Object[] {
        mock(AbstractCompiler.class),
        mock(PreprocessorSymbolTable.class),
        mock(AliasTransformationHandler.class)
    };
    ScopedAliases scopedAliases = newInstance(scopedAliasesClass, constructorArgs);

    // When the process method is called
    Node root = mockNodeWithName("test.js");
    Node externs = mockNodeWithName("externs.js");
    scopedAliases.process(externs, root);

    // Then no errors are reported
    verifyNoErrorsReported();

    // And the scoped aliases are applied
    List<Node> aliasDefinitions = getAliasDefinitions(scopedAliases, root);
    List<Node> aliasUsages = getAliasUsages(scopedAliases, root);

    // Verify that the alias definitions are in the correct order
    assertEquals(3, aliasDefinitions.size());
    assertEquals("goog.dom.createElement", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinitions.get(1).getString());
    assertEquals("DIV", aliasDefinitions.get(2).getString());

    // Verify that the alias usages are applied correctly
    assertEquals(3, aliasUsages.size());
    Node elemNode = aliasUsages.get(0).getNode();
    assertEquals("goog.dom.createElement", elemNode.getParent().getFirstChild().getString());
    assertEquals("goog.dom.TagName.DIV", elemNode.getString());

    Node divNode = aliasUsages.get(1).getNode();
    assertEquals("DIV", divNode.getParent().getFirstChild().getString());

    Node tagNameNode = aliasUsages.get(2).getNode();
    assertEquals("goog.dom.TagName.DIV", tagNameNode.getString());
  }

  private List<Node> getAliasDefinitions(ScopedAliases scopedAliases, Node root) {
    List<Node> aliasDefinitions = Lists.newArrayList();
    for (Node node : scopedAliases.getAliasDefinitionsInOrder()) {
      aliasDefinitions.add(node);
    }
    return aliasDefinitions;
  }

  private List<Node> getAliasUsages(ScopedAliases scopedAliases, Node root) {
    return scopedAliases.getAliasUsages();
  }

  @Test
  public void testScopedAliases_ErrorReporting() {
    // Given a class with a ScopedAliases instance
    Class<HotSwapCompilerPass> hotSwapCompilerPassClass = HotSwapCompilerPass.class;
    Class<AbstractCompiler> abstractCompilerClass = AbstractCompiler.class;
    Class<ScopedAliases> scopedAliasesClass = ScopedAliases.class;
    Object[] constructorArgs = new Object[] {
        mock(AbstractCompiler.class),
        mock(PreprocessorSymbolTable.class),
        mock(AliasTransformationHandler.class)
    };
    ScopedAliases scopedAliases = newInstance(scopedAliasesClass, constructorArgs);

    // When the process method is called with an invalid scoped call
    Node rootWithInvalidScopeCall = mockNodeWithName("test.js");
    Node scopeCallNode = createScopeCallNode("goog.scope({ foo: function() {} });");
    rootWithInvalidScopeCall.addChild(scopeCallNode);
    scopedAliases.process(null, rootWithInvalidScopeCall);

    // Then an error is reported
    verifyErrorReported(GOOG_SCOPE_USED_IMPROPERLY);

    // Given a class with a ScopedAliases instance
    // When the process method is called with a scope call that has too many parameters
    Node rootWithTooManyParameters = mockNodeWithName("test.js");
    Node scopeCallNodeWithTooManyParameters =
        createScopeCallNode("goog.scope(function(a, b) {});");
    rootWithTooManyParameters.addChild(scopeCallNodeWithTooManyParameters);
    scopedAliases.process(null, rootWithTooManyParameters);

    // Then an error is reported
    verifyErrorReported(GOOG_SCOPE_HAS_BAD_PARAMETERS);

    // Given a class with a ScopedAliases instance
    // When the process method is called with a scope call that uses 'this'
    Node rootWithThis = mockNodeWithName("test.js");
    Node scopeCallNodeWithThis = createScopeCallNode("goog.scope(function() { this.foo = 1; });");
    rootWithThis.addChild(scopeCallNodeWithThis);
    scopedAliases.process(null, rootWithThis);

    // Then an error is reported
    verifyErrorReported(GOOG_SCOPE_REFERENCES_THIS);

    // Given a class with a ScopedAliases instance
    // When the process method is called with a scope call that uses 'return'
    Node rootWithReturn = mockNodeWithName("test.js");
    Node scopeCallNodeWithReturn = createScopeCallNode("goog.scope(function() { return 1; });");
    rootWithReturn.addChild(scopeCallNodeWithReturn);
    scopedAliases.process(null, rootWithReturn);

    // Then an error is reported
    verifyErrorReported(GOOG_SCOPE_USES_RETURN);

    // Given a class with a ScopedAliases instance
    // When the process method is called with a scope call that uses 'throw'
    Node rootWithThrow = mockNodeWithName("test.js");
    Node scopeCallNodeWithThrow = createScopeCallNode("goog.scope(function() { throw new Error(); });");
    rootWithThrow.addChild(scopeCallNodeWithThrow);
    scopedAliases.process(null, rootWithThrow);

    // Then an error is reported
    verifyErrorReported(GOOG_SCOPE_USES_THROW);
  }

  @Test
  public void testScopedAliases_AliasRedefinition() {
    // Given a class with a ScopedAliases instance
    Class<HotSwapCompilerPass> hotSwapCompilerPassClass = HotSwapCompilerPass.class;
    Class<AbstractCompiler> abstractCompilerClass = AbstractCompiler.class;
    Class<ScopedAliases> scopedAliasesClass = ScopedAliases.class;
    Object[] constructorArgs = new Object[] {
        mock(AbstractCompiler.class),
        mock(PreprocessorSymbolTable.class),
        mock(AliasTransformationHandler.class)
    };
    ScopedAliases scopedAliases = newInstance(scopedAliasesClass, constructorArgs);

    // When the process method is called with a script that has an alias redefinition
    Node rootWithAliasRedefinition = mockNodeWithName("test.js");
    Node aliasDefNode1 = createNode("var x = goog.x;");
    Node aliasDefNode2 = createNode("var x = goog.foo;");
    rootWithAliasRedefinition.addChild(aliasDefNode1);
    rootWithAliasRedefinition.addChild(aliasDefNode2);
    scopedAliases.process(null, rootWithAliasRedefinition);

    // Then an error is reported
    verifyErrorReported(GOOG_SCOPE_ALIAS_REDEFINED, "x");
  }

  @Test
  public void testScopedAliases_NonAliasLocal() {
    // Given a class with a ScopedAliases instance
    Class<HotSwapCompilerPass> hotSwapCompilerPassClass = HotSwapCompilerPass.class;
    Class<AbstractCompiler> abstractCompilerClass = AbstractCompiler.class;
    Class<ScopedAliases> scopedAliasesClass = ScopedAliases.class;
    Object[] constructorArgs = new Object[] {
        mock(AbstractCompiler.class),
        mock(PreprocessorSymbolTable.class),
        mock(AliasTransformationHandler.class)
    };
    ScopedAliases scopedAliases = newInstance(scopedAliasesClass, constructorArgs);

    // When the process method is called with a script that has a non-alias local
    Node rootWithNonAliasLocal = mockNodeWithName("test.js");
    Node nonAliasLocalNode = createNode("var x = 1;");
    rootWithNonAliasLocal.addChild(nonAliasLocalNode);
    scopedAliases.process(null, rootWithNonAliasLocal);

    // Then an error is reported
    verifyErrorReported(GOOG_SCOPE_NON_ALIAS_LOCAL, "x");
  }

  private Node createScopeCallNode(String code) {
    Node root = createNodeFromString(code);
    Node scopeCallNode = root.getFirstChild();
    return scopeCallNode;
  }

  private void verifyErrorReported(DiagnosticType errorType, String... arguments) {
    verify(compiler).report(argThat(new DiagnosticTypeMatcher(errorType, arguments)));
  }

  private void verifyNoErrorsReported() {
    verifyNoMoreInteractions(compiler);
  }

  private Node createNodeFromString(String code) {
    Node root = createNodeWithName("test.js");
    Node scriptNode = createNode("(function() { " + code + " })();");
    root.addChild(scriptNode);
    return root;
  }

  private Node createNode(String code) {
    Node node = Node.newNode(code);
    return node;
  }

  private Node mockNodeWithName(String name) {
    Node node = mock(Node.class);
    when(node.getName()).thenReturn(name);
    return node;
  }

  private Node createNodeWithName(String name) {
    Node node = Node.newNode(name);
    return node;
  }

  private AbstractCompiler mockAbstractCompiler() {
    return mock(AbstractCompiler.class);
  }

  private PreprocessorSymbolTable mockPreprocessorSymbolTable() {
    return mock(PreprocessorSymbolTable.class);
  }

  private AliasTransformationHandler mockAliasTransformationHandler() {
    return mock(AliasTransformationHandler.class);
  }

  private CompilerOptions mockCompilerOptions() {
    return mock(CompilerOptions.class);
  }

  private Scope mockScope() {
    return mock(Scope.class);
  }

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler aliasTransformationHandler;
}

enum DiagnosticType {
  JSC_GOOG_SCOPE_USED_IMPROPERLY,
  JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS,
  JSC_GOOG_SCOPE_REFERENCES_THIS,
  JSC_GOOG_SCOPE_USES_RETURN,
  JSC_GOOG_SCOPE_USES_THROW,
  JSC_GOOG_SCOPE_ALIAS_REDEFINED,
  JSC_GOOG_SCOPE_NON_ALIAS_LOCAL
}

interface DiagnosticTypeMatcher {
  boolean matches(Diagnostic diagnostic);
}