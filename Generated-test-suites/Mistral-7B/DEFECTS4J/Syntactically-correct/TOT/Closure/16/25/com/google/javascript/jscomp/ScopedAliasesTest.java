package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

/**
 * Tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private AbstractCompiler mockCompiler;
  private PreprocessorSymbolTable mockPreprocessorSymbolTable;
  private AliasTransformationHandler mockAliasTransformationHandler;

  @Before
  public void setUp() {
    mockCompiler = mock(AbstractCompiler.class);
    mockPreprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    mockAliasTransformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testSimpleAlias() {
    ScopedAliases scopedAliases = new ScopedAliases(
        mockCompiler, mockPreprocessorSymbolTable, mockAliasTransformationHandler);

    // Arrange
    Node root = createNode("goog.scope(function() { var x = goog.x; });");

    // Act
    scopedAliases.process(null, root);

    // Assert
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    assertThat(aliasDefinitionsInOrder).hasSize(1);
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertThat(aliasDefinition.getString()).isEqualTo("x");

    assertThat(aliasUsages).hasSize(1);
    AliasUsage aliasUsage = aliasUsages.get(0);
    Node aliasedNode = ((AliasedNode) aliasUsage).aliasDefinition;
    assertThat(aliasedNode.getString()).isEqualTo("goog.x");

    assertThat(scopeCalls).hasSize(1);
    Node scopeCall = scopeCalls.get(0);
    ArgumentCaptor<AliasTransformation> transformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);
    when(mockAliasTransformationHandler.logAliasTransformation(
            eq(root.getSourceFileName()), any(SourcePosition.class))).then(i -> {
      AliasTransformation transformation = i.getValue();
      assertThat(transformation.getAliasName()).isEqualTo("x");
      return transformation;
    });
    verify(mockAliasTransformationHandler).logAliasTransformation(
        eq(root.getSourceFileName()), argThat(transformationCaptor.capture()));
    Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
    assertThat(scopeClosureBlock.hasChildren()).isFalse();
    Node expectedNode = Node.newExpr(
        Node.OP_IDENT, "goog.x", null);
    Node actualNode = aliasUsage.applyAlias();
    assertThat(actualNode).isSameInstanceAs(expectedNode);
  }

  @Test
  public void testNamespaceShadowing() {
    ScopedAliases scopedAliases = new ScopedAliases(
        mockCompiler, mockPreprocessorSymbolTable, mockAliasTransformationHandler);

    // Arrange
    Node root = createNode("goog.scope(function() { var g = goog; var x = g.x; });");

    // Act
    scopedAliases.process(null, root);

    // Assert
    Traversal traversal = scopedAliases.getLastTraversal();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> scopeCalls = traversal.getScopeCalls();

    // Check that 'g' is a namespace shadow, and 'x' is an alias.
    Var gVar = traversal.getScope().getVar("g");
    Var xVar = traversal.getScope().getVar("x");
    assertThat(gVar).isInstanceOf(Scope.BleedingFunctionVar.class);
    assertThat(xVar).isNotNull();
    Node gNode = gVar.getNode();
    Node xNode = xVar.getNode();
    ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
    verify(mockAliasTransformationHandler).logAliasTransformation(
        eq(root.getSourceFileName()), argThat(transformationCaptor.capture()));
    AliasTransformation transformation = transformationCaptor.getValue();
    assertThat(transformation.getAliasName()).isEqualTo("x");
    String aliasName = transformation.getAliasName();
    assertThat(transformation.getAliasedName()).isEqualTo("goog.x");
    Node expectedNode = Node.newExpr(
        Node.OP_IDENT, aliasName, null);
    Node actualNode = aliasUsage.applyAlias();
    assertThat(actualNode).isSameInstanceAs(expectedNode);
  }

  @Test
  public void testErrorCases() {
    ScopedAliases scopedAliases = new ScopedAliases(
        mockCompiler, mockPreprocessorSymbolTable, mockAliasTransformationHandler);

    // Arrange
    Node root = createNode(
        "goog.scope(function() { " +
            "var x = goog.x;" +
            "this.x = goog.x;" +
            "return goog.x;" +
            "});");

    // Act & Assert
    try {
      scopedAliases.process(null, root);
      fail();
    } catch (RuntimeException e) {
      // Expected
      Traversal traversal = scopedAliases.getLastTraversal();
      List<DiagnosticType> diagnostics = traversal.getDiagnostics();
      assertThat(diagnostics).hasSize(4);
      assertThat(diagnostics.get(0)).isEqualTo(GOOG_SCOPE_REFERENCES_THIS);
      assertThat(diagnostics.get(1)).isEqualTo(GOOG_SCOPE_USES_RETURN);
      assertThat(diagnostics.get(2)).isEqualTo(GOOG_SCOPE_REFERENCES_THIS);
      assertThat(diagnostics.get(3)).isEqualTo(GOOG_SCOPE_USES_RETURN);
    }
  }

  private Node createNode(String code) {
    NodeFactory nodeFactory = NodeFactory.getFactory();
    return nodeFactory.parseTree(code, "test.js");
  }

  private static final DiagnosticType GOOG_SCOPE_USED_IMPROPERLY = DiagnosticType.error(
      "JSC_GOOG_SCOPE_USED_IMPROPERLY",
      "The call to goog.scope must be alone in a single statement.");

  private static final DiagnosticType GOOG_SCOPE_HAS_BAD_PARAMETERS =
      DiagnosticType.error(
          "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
          "The call to goog.scope must take only a single parameter.  It must" +
              " be an anonymous function that itself takes no parameters.");

  private static final DiagnosticType GOOG_SCOPE_REFERENCES_THIS = DiagnosticType.error(
      "JSC_GOOG_SCOPE_REFERENCES_THIS",
      "The body of a goog.scope function cannot reference 'this'.");

  private static final DiagnosticType GOOG_SCOPE_USES_RETURN = DiagnosticType.error(
      "JSC_GOOG_SCOPE_USES_RETURN",
      "The body of a goog.scope function cannot use 'return'.");

  private static final DiagnosticType GOOG_SCOPE_USES_THROW = DiagnosticType.error(
      "JSC_GOOG_SCOPE_USES_THROW",
      "The body of a goog.scope function cannot use 'throw'.");
}