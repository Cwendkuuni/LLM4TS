package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp. Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node externs = Node.NULL;
    Node root = Node.makeCall(
        Node.makeName("goog.scope", null),
        Node.makeBlock(
            Node.makeVariable("dom", Node.makeUndefined(), Node.makeName("goog.dom")),
            Node.makeCall(Node.makeName("goog.dom.createElement", null),
                Node.makeQualifiedName("goog.dom.TagName", "DIV"))));

    scopedAliases.process(externs, root);
    Traversal traversal = scopedAliases.getLastTraversal();

    // No errors
    assertThat(traversal.hasErrors()).isFalse();

    // Check aliases
    ArgumentCaptor<AliasTransformation> transformationArgumentCaptor = ArgumentCaptor.forClass(AliasTransformation.class);
    when(transformationHandler.logAliasTransformation(anyString(), any(SourcePosition.class))).thenReturn(transformationArgumentCaptor.capture());
    List<AliasTransformation> transformations = transformationArgumentCaptor.getAllValues();
    assertThat(transformations).hasSize(1);
    AliasTransformation transformation = transformations.get(0);
    assertThat(transformation.getFileName()).isEqualTo("test.js");
    Map<String, Var> aliases = Maps.newHashMap();
    aliases.put("dom", new Var("dom", null, null, null, null, null));
    assertThat(transformation.getAliases()).isEqualTo(aliases);

    // Check alias definitions in order
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> expectedAliasDefinitionsInOrder = Lists.newArrayList(
        Node.makeVariable("dom", Node.makeName("goog.dom"), Node.SCOPE_VAR, null, null, null));
    assertThat(aliasDefinitionsInOrder).isEqualTo(expectedAliasDefinitionsInOrder);

    // Check scope calls
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<Node> expectedScopeCalls = Lists.newArrayList(
        Node.makeCall(Node.makeName("goog.scope", null),
            Node.makeBlock(
                Node.makeVariable("dom", Node.makeName("goog.dom"), Node.SCOPE_VAR, null, null, null))));
    assertThat(scopeCalls).isEqualTo(expectedScopeCalls);

    // Check alias usages
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<AliasUsage> expectedAliasUsages = Lists.newArrayList(
        new AliasedNode(Node.makeName("dom"), Node.makeName("goog.dom")),
        new AliasedTypeNode(
            Node.makeStringLiteral("goog.dom"),
            Node.makeQualifiedName("goog.dom", "TagName", "DIV")));
    assertThat(aliasUsages).isEqualTo(expectedAliasUsages);
  }
}

class AliasedNode implements AliasUsage {
  private final Node aliasReference;

  private final Node aliasDefinition;

  AliasedNode(Node aliasReference, Node aliasDefinition) {
    this.aliasReference = aliasReference;
    this.aliasDefinition = aliasDefinition;
  }

  @Override
  public void applyAlias() {
    aliasReference.setParent(aliasDefinition.getParent());
    aliasReference.setParentIndex(aliasDefinition.getParentIndex());
    aliasDefinition.setParent(aliasReference.getParent());
    aliasDefinition.setParentIndex(aliasReference.getParentIndex());
    aliasDefinition.getParent().replaceChild(
        aliasDefinition.getParentIndex(), aliasDefinition);
    aliasDefinition.setParentIndex(aliasReference.getParentIndex());
  }
}

class AliasedTypeNode implements AliasUsage {
  private final Node aliasReference;

  private final String correctedType;

  AliasedTypeNode(Node aliasReference, String correctedType) {
    this.aliasReference = aliasReference;
    this.correctedType = correctedType;
  }

  @Override
  public void applyAlias() {
    aliasReference.setString(correctedType);
  }
}

interface AliasUsage {
  public void applyAlias();
}