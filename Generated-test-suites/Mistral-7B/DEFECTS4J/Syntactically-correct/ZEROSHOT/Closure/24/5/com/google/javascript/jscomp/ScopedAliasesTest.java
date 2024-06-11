package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);

    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    // Map alias name to Var in scope
    Map<String, Var> aliases = Collections.singletonMap("dom", mock(Var.class));

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = Node.newCompilationUnit();

    Node googScopeCall = Node.newCall(
        Node.newQualifiedName("goog", "scope"),
        Arrays.asList(Node.newFunction("anonymous", Collections.emptyList(), null)));

    root.setBody(googScopeCall);

    Node domVar = Node.newVar("dom", null, null, null);

    Node domAssignment = Node.newAssignment(domVar, Node.newName("DIV"));

    Node aliasedDivCall = Node.newCall(
        Node.newName("goog.dom"),
        Arrays.asList(Node.newQualifiedName("goog.dom", "createElement")));

    Node aliasDefinition = Node.newVar(
        "DIV", null, null, Node.newCall(Node.newQualifiedName("goog.dom", "createElement"), Arrays.asList(Node.newString("div"))));

    Node aliasUsage = Node.newAssignment(domVar, aliasDefinition);

    when(preprocessorSymbolTable.addReference(Mockito.any(Node.class)))
        .thenReturn(Node.newCall(Node.NUL, Collections.emptyList(), null));

    when(transformationHandler.logAliasTransformation(
        Mockito.eq("test.js"),
        Mockito.any(SourcePosition.class)))
        .thenReturn(new AliasTransformation("dom", "goog.dom.createElement"));

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);

    // Traverse and process the node tree
    scopedAliases.process(null, root);

    // Check that the alias definitions have been added in the correct order
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<Node> expectedAliasDefinitions = Arrays.asList(aliasDefinition);
    assertThat(aliasDefinitionsInOrder).isEqualTo(expectedAliasDefinitions);

    // Check that the alias usages have been applied
    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    List<Node> expectedAliasUsages = Arrays.asList(aliasUsage);
    assertThat(aliasUsages).isEqualTo(expectedAliasUsages);

    // Check that the scope calls have been removed
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<Node> expectedScopeCalls = Collections.emptyList();
    assertThat(scopeCalls).isEqualTo(expectedScopeCalls);

    // Check that no errors have been reported
    assertThat(scopedAliases.hasErrors()).isFalse();

    // Verify that the alias definitions have been added to the symbol table
    Node googDom = Node.newQualifiedName("goog.dom");
    Node googDomCreateElement = Node.newQualifiedName("goog.dom", "createElement");
    List<Node> symbols = preprocessorSymbolTable.getSymbols();
    List<Node> expectedSymbols = Arrays.asList(
        Node.newSymbolNode("dom", Var.VAR, domVar, null),
        Node.newSymbolNode("DIV", Var.VAR, domVar, null),
        Node.newSymbolNode("goog", Var.CONST, Node.newQualifiedName("goog"), null),
        Node.newSymbolNode("goog.dom", Var.CONST, googDom, null),
        Node.newSymbolNode("goog.dom.createElement", Var.FUNCTION,
            Node.newCall(googDom, Arrays.asList(new Node[] { Node.newString("createElement") })), null));
    assertThat(symbols).isEqualTo(expectedSymbols);

    // Verify that the alias transformation has been logged
    AliasTransformation[] transformations = transformationHandler.getAliasTransformations();
    AliasTransformation expectedTransformation = new AliasTransformation("dom", "goog.dom.createElement");
    assertThat(transformations).contains(expectedTransformation);
  }
}

class NodeUtil {
  // Empty class used to suppress warnings about unused methods

  static String getFunctionName(Node n) {
    return n.isFunction() ? n.getString() : null;
  }

  static Node getFunctionParameters(Node n) {
    return n.isFunction() ? n.getFirstChild().getFirstChild() : null;
  }

  static void tryMergeBlock(Node n) {
    // Empty method used to suppress warnings about unused methods
  }
}