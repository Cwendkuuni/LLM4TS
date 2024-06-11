import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Whitespace;
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
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testProcess() {
    Node externs = createNode("externs");
    Node root = createNode("root");
    scopedAliases.process(externs, root);
    // Add assertions here
  }

  @Test
  public void testHotSwapScript() {
    Node root = createNode("root");
    scopedAliases.hotSwapScript(root, null);
    // Add assertions here
  }

  @Test
  public void givenNullExternsAndRoot_whenProcessCalled_thenHasNoErrors() {
    scopedAliases.process(null, null);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenNullRoot_whenHotSwapScriptCalled_thenHasNoErrors() {
    scopedAliases.hotSwapScript(null, null);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenValidInput_whenHotSwapScriptCalled_thenHasNoErrors() {
    Node root = createNode("root");
    scopedAliases.hotSwapScript(root, null);
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void givenInvalidInput_whenHotSwapScriptCalled_thenHasErrors() {
    Node root = createNode("root");
    root.setType(Token.THROW);
    scopedAliases.hotSwapScript(root, null);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenCallToInvalidScopeMethod_whenProcessCalled_thenHasErrors() {
    Node externs = createNode("externs");
    Node root = createNode("root", "goog.invalidScopeMethod()");
    scopedAliases.process(externs, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenCallToScopeMethodWithInvalidParameter_whenProcessCalled_thenHasErrors() {
    Node externs = createNode("externs");
    Node root = createNode("root", "goog.scope(function() { this.parameter })");
    scopedAliases.process(externs, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenCallToScopeMethodWithRefToThis_whenProcessCalled_thenHasErrors() {
    Node externs = createNode("externs");
    Node root = createNode("root", "goog.scope(function() { return this })");
    scopedAliases.process(externs, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenCallToScopeMethodWithReturn_whenProcessCalled_thenHasErrors() {
    Node externs = createNode("externs");
    Node root = createNode("root", "goog.scope(function() { return 1 })");
    scopedAliases.process(externs, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenCallToScopeMethodWithThrow_whenProcessCalled_thenHasErrors() {
    Node externs = createNode("externs");
    Node root = createNode("root", "goog.scope(function() { throw new Error() })");
    scopedAliases.process(externs, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenCallToScopeMethodWithAliasRedefinition_whenProcessCalled_thenHasErrors() {
    Node externs = createNode("externs");
    Node root = createNode("root");
    Node aliasDefinition1 = createNode("aliasDefinition1");
    Node aliasReference1 = createNode("aliasReference1", "goog.scope");
    Node aliasDefinition2 = createNode("aliasDefinition2");
    Node aliasReference2 = createNode("aliasReference2", "goog.scope");

    aliasDefinition1.setFirstChild(aliasReference1);
    aliasDefinition1.setLastChild(aliasDefinition2);
    root.setFirstChild(aliasDefinition1);

    scopedAliases.process(externs, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenCallToScopeMethodWithNonAliasLocal_whenProcessCalled_thenHasErrors() {
    Node externs = createNode("externs");
    Node root = createNode("root");
    Node aliasDefinition = createNode("aliasDefinition");
    Node aliasReference = createNode("aliasReference", "goog.scope");
    Node local = createNode("local");

    aliasDefinition.setFirstChild(aliasReference);
    root.setFirstChild(local);

    scopedAliases.process(externs, root);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void givenCallToScopeMethod_whenApplyAliases_thenAliasesApplied() {
    Node externs = createNode("externs");
    Node root = createNode("root", "goog.scope(function() { goog.dom.createElement(DIV) })");
    Node aliasDefinition = createNode("aliasDefinition", "goog.dom.createElement", "createElement");
    Node aliasReference = createNode("aliasReference");
    Node scopeCall = createNode("scopeCall", "goog.scope");

    aliasDefinition.setFirstChild(aliasReference);
    scopeCall.setFirstChild(aliasDefinition);
    scopeCall.setLastChild(root);

    scopedAliases.process(externs, root);
    scopedAliases.hotSwapScript(root, null);

    ArgumentCaptor<List<AliasUsage>> aliasUsageCaptor = ArgumentCaptor.forClass(List.class);
    when(transformationHandler.getAliasDefinitionsInOrder(aliasDefinition))
        .thenReturn(aliasDefinitionsInOrder);
    when(transformationHandler.getAliasUsages(aliasReference))
        .thenReturn(new AliasedNode(aliasReference, aliasDefinition));
    when(compiler.reportCodeChange()).thenReturn(Void.TYPE);

    scopedAliases.applyAliases(aliasDefinition, root, transformationHandler);

    List<AliasUsage> aliasUsages = aliasUsageCaptor.getValue();
    assertEquals(1, aliasUsages.size());
    assertTrue(aliasUsages.get(0) instanceof AliasedNode);
  }

  @Test
  public void givenCallToScopeMethodWithShadowedNamespace_whenApplyAliases_thenNamespacesRenamed() {
    Node externs = createNode("externs");
    Node root = createNode("root", "goog.foo.bar.scope(function() { goog.foo.baz.createElement(DIV) })");
    Node aliasDefinition = createNode("aliasDefinition", "goog.foo.baz.createElement", "createElement");
    Node aliasReference = createNode("aliasReference");
    Node scopeCall = createNode("scopeCall", "goog.foo.bar.scope");

    aliasDefinition.setFirstChild(aliasReference);
    scopeCall.setFirstChild(aliasDefinition);
    scopeCall.setLastChild(root);

    scopedAliases.process(externs, root);
    scopedAliases.hotSwapScript(root, null);

    ArgumentCaptor<List<AliasUsage>> aliasUsageCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    when(transformationHandler.getAliasDefinitionsInOrder(aliasDefinition))
        .thenReturn(aliasDefinitionsInOrder);
    when(transformationHandler.getAliasUsages(aliasReference))
        .thenReturn(new AliasedNode(aliasReference, aliasDefinition));
    when(compiler.reportCodeChange()).thenReturn(Void.TYPE);
    when(preprocessorSymbolTable.getLastProcessedName())
        .thenReturn("foo.baz.createElement");

    scopedAliases.applyAliases(aliasDefinition, root, transformationHandler);

    List<AliasUsage> aliasUsages = aliasUsageCaptor.getValue();
    assertEquals(1, aliasUsages.size());
    assertTrue(aliasUsages.get(0) instanceof AliasedNode);

    when(transformationHandler.getOldNameForNewName("foo.baz.createElement"))
        .thenReturn("goog.foo.goog_createElement_");

    scopedAliases.renameNamespaceShadows(null);

    List<String> renamedNames = Lists.newArrayList();
    for (Var var : ((Scope) root.getParent()).getVarIterable()) {
      if (var.getName().startsWith("goog_")) {
        renamedNames.add(var.getName());
      }
    }

    assertEquals(1, renamedNames.size());
    assertEquals("goog_foo_baz_createElement_", renamedNames.get(0));
  }

  private Node createNode(String name) {
    return createNode(name, null);
  }

  private Node createNode(String name, String value) {
    Node node = new Node();
    node.setType(Token.NAME);
    node.setString(name);

    if (value != null) {
      node.setString(value);
    }

    return node;
  }

  private Node createNode(String name, Node... children) {
    Node node = new Node();
    node.setType(Token.CALL);
    node.setFirstChild(createNode(name));

    for (Node child : children) {
      node.addChild(child);
    }

    return node;
  }
}