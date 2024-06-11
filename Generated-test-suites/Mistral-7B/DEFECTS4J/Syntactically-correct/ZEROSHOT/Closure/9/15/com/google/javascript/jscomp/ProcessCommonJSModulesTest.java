package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.IR;
import java.io.IOException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new TestCompiler(options);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput("test.js", "test.js");
    Node rootNode = createTestModuleNode();
    input.setRootNode(rootNode);
    processor.process(input, input.getRootNode());

    Node processedRootNode = input.getRootNode();
    Node exportsNode = getModuleExportsNode(processedRootNode);
    assertThat(exportsNode).isNotNull();
    Node moduleNameNode = exportsNode.getFirstChild();
    assertThat(moduleNameNode.getString()).isEqualTo("test$module$exports");

    Node requireNode = getRequireNode(processedRootNode, "other");
    assertThat(requireNode).isNotNull();
    Node otherModuleNode = requireNode.getChildAtIndex(1);
    assertThat(otherModuleNode.getString()).isEqualTo("other$module$exports");
  }

  @org.junit.Test
  public void testVisitRequireCall() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node requireNode = createRequireNode("require", "other");
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    callback.visitRequireCall(nodeTraversal, requireNode, null);

    Node exportNode = getModuleExportsNode(requireNode.getParent());
    Node otherModuleNode = exportNode.getChildAtIndex(0);
    assertThat(otherModuleNode.getString()).isEqualTo("other$module$exports");
  }

  @org.junit.Test
  public void testVisitScript() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node scriptNode = createScriptNode("test");

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    callback.visitScript(nodeTraversal, scriptNode);

    Node moduleNameNode = scriptNode.getFirstChild();
    assertThat(moduleNameNode.getString()).isEqualTo("test$module$exports");
  }

  @org.junit.Test
  public void testVisitModuleExports() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node moduleExportsNode = createModuleExportsNode();

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    callback.visitModuleExports(moduleExportsNode);

    Node exportsNode = moduleExportsNode.getParent();
    Node moduleNameNode = exportsNode.getFirstChild();
    assertThat(moduleNameNode.getString()).isEqualTo("module$exports");
  }

  private Node createTestModuleNode() {
    Node scriptNode = createScriptNode("test");
    Node exportsNode = createModuleExportsNode();
    scriptNode.addChildToBack(exportsNode);
    return scriptNode;
  }

  private Node createScriptNode(String name) {
    Node scriptNode = new Node_Script();
    scriptNode.setSourceFileName(name + ".js");
    return scriptNode;
  }

  private Node createRequireNode(String name, String module) {
    Node requireNode = new Node_Call();
    Node requireFunctionNode = new Node_Name("require");
    requireNode.addChildToBack(requireFunctionNode);
    requireNode.addChildToBack(createStringNode(module));
    return requireNode;
  }

  private Node createModuleExportsNode() {
    Node exportsNode = new Node_ObjectLiteral();
    Node moduleExportsPropNode = new Node_Property();
    moduleExportsPropNode.setName("module$exports");
    moduleExportsPropNode.setValue(new Node_ObjectLiteral());
    exportsNode.addChildToBack(moduleExportsPropNode);
    return exportsNode;
  }

  private Node getModuleExportsNode(Node node) {
    for (Node child : node.getChildren()) {
      if (child instanceof Node_Property &&
          "module$exports".equals(child.getString())) {
        return child;
      }
    }
    return null;
  }

  private Node getRequireNode(Node node, String module) {
    for (Node child : node.getChildren()) {
      if (child instanceof Node_Call &&
          "require".equals(child.getFirstChild().getString())) {
        Node requireModuleNode = child.getChildAtIndex(1);
        if (requireModuleNode.isString() &&
            requireModuleNode.getString().equals(module)) {
          return child;
        }
      }
    }
    return null;
  }
}