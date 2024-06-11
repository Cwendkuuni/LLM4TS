package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.AliasTransformationHandler;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    when(compiler.report(NodeTraversal.makeError(Node.createTree("test"),
        DiagnosticType.error(1, 1, "error"), new String[]{"arg1"}))).thenCallRealMethod();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    Node root = Node.createTree("function test() {}");
    Node externs = Node.createTree("{}");

    scopedAliases.process(externs, root);

    // No errors reported, so test the output
    Traversal traversal = scopedAliases.getTraversal();
    assertNull(traversal.getAliasDefinitionsInOrder().get(0));
    assertNull(traversal.getScopeCalls().get(0));
    assertNull(traversal.getAliasUsages().get(0));
  }

  @Test
  public void testScopedAliasesWithAliasDefinition() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    when(compiler.report(NodeTraversal.makeError(Node.createTree("test"),
        DiagnosticType.error(1, 1, "error"), new String[]{"arg1"}))).thenCallRealMethod();

    String aliasName = "goog";
    String aliasValue = "googNameSpace";

    Node aliasDefinition = Node.createTree(
        "var " + aliasName + " = " + aliasValue + ";");
    Node aliasUsage = Node.createTree("var x = " + aliasName + ".x;");

    Node root = Node.createTree("function test() {" + aliasUsage.toString() + "}");
    Node externs = Node.createTree("{" + aliasDefinition.toString() + "}");

    when(preprocessorSymbolTable.addReference(Node.createTree("goog"))).thenReturn(null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    scopedAliases.process(externs, root);

    // No errors reported, so test the output
    Traversal traversal = scopedAliases.getTraversal();
    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(aliasUsage.getClass(), AliasedNode.class);
    AliasedNode aliasedNode = (AliasedNode) aliasUsage;
    assertSame(aliasedNode.aliasReference, aliasUsage);
    assertSame(aliasedNode.aliasDefinition, aliasDefinition);

    AliasDefinition aliasedDefinition =
        (AliasDefinition) traversal.getAliasDefinitionsInOrder().get(0);
    assertSame(aliasedDefinition.getNode(), aliasDefinition);
    assertEquals(aliasedDefinition.getAliasName(), aliasName);
    assertEquals(aliasedDefinition.getAliasValue().getString(), aliasValue);
  }

  @Test
  public void testScopedAliasesWithAliasTypeDefinition() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    when(compiler.report(NodeTraversal.makeError(Node.createTree("test"),
        DiagnosticType.error(1, 1, "error"), new String[]{"arg1"}))).thenCallRealMethod();

    String aliasName = "goog";
    String aliasValue = "googNameSpace";

    Node aliasDefinition = Node.createTree("goog.x = " + aliasValue + ";");
    Node typeNode = Node.createTree("function test() { @type {goog.x: Number} }");

    Node root = Node.createTree("function test() {}");
    Node externs = Node.createTree("{}");

    when(preprocessorSymbolTable.addReference(Node.createTree("goog"))).thenReturn(null);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);

    scopedAliases.process(externs, root);

    // No errors reported, so test the output
    Traversal traversal = scopedAliases.getTraversal();
    AliasUsage aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(aliasUsage.getClass(), AliasedTypeNode.class);
    AliasedTypeNode aliasedTypeNode = (AliasedTypeNode) aliasUsage;
    assertSame(aliasedTypeNode.getTypeReference, aliasUsage);
    assertSame(aliasedTypeNode.aliasDefinition, aliasDefinition);
    assertEquals(aliasedTypeNode.aliasName, aliasName);

    AliasDefinition aliasedDefinition =
        (AliasDefinition) traversal.getAliasDefinitionsInOrder().get(0);
    assertSame(aliasedDefinition.getNode(), aliasDefinition);
    assertEquals(aliasedDefinition.getAliasName(), aliasName);
    assertEquals(aliasedDefinition.getAliasValue().getString(), aliasValue);
  }
}

class AliasDefinition {
  private final Node node;
  private final String aliasName;
  private final Node aliasValue;

  AliasDefinition(Node node, String aliasName, Node aliasValue) {
    this.node = node;
    this.aliasName = aliasName;
    this.aliasValue = aliasValue;
  }

  Node getNode() {
    return node;
  }

  String getAliasName() {
    return aliasName;
  }

  Node getAliasValue() {
    return aliasValue;
  }

  @Override
  public String toString() {
    return "AliasDefinition{" +
        "node=" + node +
        ", aliasName='" + aliasName + '\'' +
        ", aliasValue=" + aliasValue +
        '}';
  }
}