import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {
  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess() {
    // Given: An input root node
    Node root = Node.createTree("function test() { var d = goog.dom; d.createElement('DIV'); }");

    // When: The process method is called
    scopedAliases.process(null, root);

    // Then: The alias is renamed and the scope is collapsed
    Node expectedRoot = Node.createTree("goog.dom.createElement('DIV');");
    assertEquals("Unexpected result", expectedRoot.toString(), root.toString());
  }

  @Test
  public void testProcessWithErrors() {
    // Given: An input root node with errors
    Node root = Node.createTree("function test() { goog.scope(function() { goog.scope(function() {}); }); }");

    // When: The process method is called
    scopedAliases.process(null, root);

    // Then: The compilation should fail with multiple errors
    List<DiagnosticType> diagnostics = Mockito.mock(List.class);
    Mockito.when(compiler.getDiagnostics()).thenReturn(diagnostics);
    assertFalse("Compilation should have failed", scopedAliases.hasErrors());
    Mockito.verify(diagnostics).add(ArgumentMatchers.isA(DiagnosticType.class));
    Mockito.verify(diagnostics).add(ArgumentMatchers.isA(DiagnosticType.class));
  }

  @Test
  public void testHotSwapScript() {
    // Given: A root node with a goog.scope block and a call to goog.scope
    Node root = Node.createTree("function test() { goog.scope(function() { goog.scope(function() {}); }); goog.scope(function() {}); }");

    // When: The hotSwapScript method is called
    scopedAliases.hotSwapScript(null, root);

    // Then: The alias is renamed and the scope is collapsed
    Node expectedRoot = Node.createTree("goog.scope(function() {}); goog.scope(function() {});");
    assertEquals("Unexpected result", expectedRoot.toString(), root.toString());
  }

  @Test
  public void testHotSwapScriptWithErrors() {
    // Given: A root node with a goog.scope block and errors
    Node root = Node.createTree("function test() { goog.scope(function() { goog.scope(function() { goog.scope(function() {}); }); }); }); };");

    // When: The hotSwapScript method is called
    scopedAliases.hotSwapScript(null, root);

    // Then: The compilation should fail with multiple errors
    List<DiagnosticType> diagnostics = Mockito.mock(List.class);
    Mockito.when(compiler.getDiagnostics()).thenReturn(diagnostics);
    assertFalse("Compilation should have failed", scopedAliases.hasErrors());
    Mockito.verify(diagnostics).add(ArgumentMatchers.isA(DiagnosticType.class));
    Mockito.verify(diagnostics).add(ArgumentMatchers.isA(DiagnosticType.class));
    Mockito.verify(diagnostics).add(ArgumentMatchers.isA(DiagnosticType.class));
  }

  @Test
  public void testApplyAlias() {
    // Given: A Node representing an aliased name
    Node aliasReference = Node.createTree("goog.x");

    // And: A Node representing the alias definition
    Node aliasDefinition = Node.createTree("var x = 1;");

    // When: An AliasedNode instance is created and its applyAlias method is called
    AliasedNode aliasedNode = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
    aliasedNode.applyAlias();

    // Then: The aliasReference is replaced with the aliasDefinition
    assertEquals("Unexpected reference result", aliasDefinition.toString(), aliasReference.toString());
  }

  @Test
  public void testApplyAliasType() {
    // Given: A Node representing a type alias
    Node typeReference = Node.createTree("goog.x.Y");

    // And: A Node representing the alias definition
    Node aliasDefinition = Node.createTree("var x = {Y: 1};");

    // When: An AliasedTypeNode instance is created and its applyAlias method is called
    AliasedTypeNode aliasedNode = new ScopedAliases.AliasedTypeNode(typeReference, aliasDefinition, "x");
    aliasedNode.applyAlias();

    // Then: The typeReference is updated with the aliasDefinition
    assertEquals("Unexpected type reference result", "goog.x.Y".replace("x", ""), typeReference.toString());
  }

  @Test
  public void testFindAliases() {
    // Given: A Scope with variables
    Scope scope = NodeTraversal.createTestScope();
    scope.declare("x", Node.createTree("1"));
    scope.declare("goog.x", Node.createTree("1"));
    scope.declare("goog.y", Node.createTree("1"));

    // When: A Traversal instance is created and its findAliases method is called
    ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
    traversal.findAliases(null);

    // Then: The aliases map contains the correct mappings
    Map<String, Scope.Var> aliases = traversal.aliases;
    assertEquals("Unexpected number of aliases", 2, aliases.size());
    assertEquals("Unexpected alias for 'goog.x'", "goog.x", aliases.keySet().iterator().next().getName());
    assertEquals("Unexpected alias for 'goog.y'", "goog.y",
        aliases.entrySet().iterator().next().getValue().getName());
  }

  @Test
  public void testIsCallToScopeMethod() {
    // Given: A Node representing a call to goog.scope
    Node scopeCall = Node.createTree("goog.scope(function() {});");

    // When: The isCallToScopeMethod method is called
    assertTrue("Unexpected result", ScopedAliases.isCallToScopeMethod(scopeCall));
  }

  @Test
  public void testIsCallToScopeMethod_False() {
    // Given: A Node that is not a call to goog.scope
    Node notAScopeCall = Node.createTree("1 + 1");

    // When: The isCallToScopeMethod method is called
    assertFalse("Unexpected result", ScopedAliases.isCallToScopeMethod(notAScopeCall));
  }
}