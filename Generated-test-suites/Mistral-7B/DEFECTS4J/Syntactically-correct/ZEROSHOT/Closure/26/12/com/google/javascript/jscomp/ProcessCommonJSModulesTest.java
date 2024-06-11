import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.abstractPostOrder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.net.URI;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProcessCommonJSModules.class)
public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    processor = new ProcessCommonJSModules(compiler, ".");
  }

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String sourceCode = "var a = require('b');";
    Node root = nodeFactory.parseTree(sourceCode);

    // Act
    processor.process(nodeFactory.getConstantNode(null), root);

    // Assert
    AbstractPostOrderCallback callback = new NameCallback();
    abstractPostOrder(compiler, root, callback);
    Set<String> names = Sets.newHashSet(callback.names);
    assertTrue(names.contains("b$require$0"));
    assertTrue(names.contains("b$"));
    assertTrue(names.contains("a"));
  }

  @Test
  public void testProcessCommonJSModulesWithProvide() throws Exception {
    String sourceCode = "goog.provide('a');\n" +
        "var b = require('c');";
    Node root = nodeFactory.parseTree(sourceCode);

    // Act
    processor.process(nodeFactory.getConstantNode(null), root);

    // Assert
    AbstractPostOrderCallback callback = new NameCallback();
    abstractPostOrder(compiler, root, callback);
    Set<String> names = Sets.newHashSet(callback.names);
    assertTrue(names.contains("a"));
    assertTrue(names.contains("c$require$0"));
    assertTrue(names.contains("c$"));
    assertTrue(names.contains("b"));
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() throws Exception {
    String sourceCode = "var module = {exports: {}};\n" +
        "goog.require('a');\n" +
        "module.exports = a;";
    Node root = nodeFactory.parseTree(sourceCode);

    // Act
    processor.process(nodeFactory.getConstantNode(null), root);

    // Assert
    AbstractPostOrderCallback callback = new NameCallback();
    abstractPostOrder(compiler, root, callback);
    Set<String> names = Sets.newHashSet(callback.names);
    assertTrue(names.contains("a"));
    assertTrue(names.contains("module$exports$"));
    assertTrue(names.contains("module$"));
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() throws Exception {
    String currentFileName = "a.js";
    String sourceCode = "var b = require('./b');";
    Node root = nodeFactory.parseTree(sourceCode);

    // Act
    processor.process(nodeFactory.getConstantNode(currentFileName), root);

    // Assert
    AbstractPostOrderCallback callback = new NameCallback();
    abstractPostOrder(compiler, root, callback);
    Set<String> names = Sets.newHashSet(callback.names);
    String expectedModuleName = toModuleName("b.js", currentFileName);
    assertTrue(names.contains(expectedModuleName));
    assertTrue(names.contains("b$require$0"));
    assertTrue(names.contains("b$"));
    assertTrue(names.contains("b"));
  }

  private String toModuleName(String requiredFilename, String currentFilename) {
    return ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
  }

  private static class NameCallback extends AbstractPostOrderCallback {
    private Set<String> names = Sets.newHashSet();

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isName()) {
        names.add(n.getString());
      }
    }
  }
}