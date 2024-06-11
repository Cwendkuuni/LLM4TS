import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testValidScopedAliases() {
    // Given
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Node node = Node.newNode("goog.scope(function() { var x = goog.x; })");

    // When
    scopedAliases.process(null, node);

    // Then
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertNotNull(aliasUsages);
    assertFalse(aliasUsages.isEmpty());

    ArgumentCaptor<AliasTransformation> transformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);
    AliasTransformation transformation = transformationCaptor.getValue();
    assertEquals("goog.x", transformation.getAliasName());
    assertEquals("goog", transformation.getAliasValue());

    assertEquals(1, transformation.getAliases().size());
    Map<String, String> aliases = transformation.getAliases();
    assertEquals("goog.x", aliases.keySet().iterator().next());
    assertEquals("goog", aliases.values().iterator().next());
  }

  @Test
  public void testInvalidScopedAliases() {
    // Given
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Node node = Node.newNode("goog.scope(function() { return goog.x; })");

    // When
    scopedAliases.process(null, node);

    // Then
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertNotNull(aliasUsages);
    assertFalse(aliasUsages.isEmpty());

    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    when(compiler.report(ArgumentCaptor.any(NodeTraversal.class),
        ArgumentCaptor.any(Node.class), diagnosticTypeCaptor.capture(),
        ArgumentCaptor.anyVariables()))
        .thenReturn(null);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertNotNull(scopeCalls);
    assertFalse(scopeCalls.isEmpty());

    DiagnosticType diagnosticType = diagnosticTypeCaptor.getValue();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", diagnosticType.getTypeName());
  }

  @Test
  public void testInvalidScopedAliasesDuplicateAlias() {
    // Given
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    Node node = Node.newNode("goog.scope(function() { var x = goog.x; var x = goog.y; })");

    // When
    scopedAliases.process(null, node);

    // Then
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertNotNull(aliasUsages);
    assertFalse(aliasUsages.isEmpty());

    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    when(compiler.report(ArgumentCaptor.any(NodeTraversal.class),
        ArgumentCaptor.any(Node.class), diagnosticTypeCaptor.capture(),
        ArgumentCaptor.anyVariables()))
        .thenReturn(null);

    List<AliasUsage> invalidAliasUsages = Lists.newArrayList();
    for (AliasUsage aliasUsage : aliasUsages) {
      if (aliasUsage instanceof AliasedNode) {
        AliasedNode aliasedNode = (AliasedNode) aliasUsage;
        if (aliasedNode.aliasDefinition.getFirstChild().isName()) {
          invalidAliasUsages.add(aliasUsage);
        }
      }
    }

    ArgumentCaptor<DiagnosticType> diagnosticType =
        ArgumentCaptor.forClass(DiagnosticType.class);
    when(compiler.report(ArgumentCaptor.any(NodeTraversal.class),
        ArgumentCaptor.any(Node.class), diagnosticType.capture(),
        ArgumentCaptor.anyVariables()))
        .thenReturn(null);

    assertEquals(1, invalidAliasUsages.size());
    AliasedNode aliasUsage = (AliasedNode) invalidAliasUsages.get(0);
    DiagnosticType error = diagnosticType.getValue();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getTypeName());
    assertEquals("x", error.getMessageArguments()[0]);
  }
}