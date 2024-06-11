package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.test.BaseCompilerTest;
import com.google.javascript.rhino.SourceName;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;

/**
 * Tests for {@link ProcessCommonJSModules} class.
 */
public class ProcessCommonJSModulesTest extends BaseCompilerTest {

  public void testProcessCommonJSModules() throws IOException {
    String input = "// @compilationLevel ADVANCED\n" +
        "// @fileOverrides \"**/*.js\".processing = require(\"process\").mainModule\n" +
        "(function() {\n" +
        "  // commonjs module 1\n" +
        "  var m1 = require(\"m1\");\n" +
        "  var m1 exports = m1.exports;\n" +
        "  m1 exports.add = function(x, y) {\n" +
        "    return x + y;\n" +
        "  };\n" +
        "  m1 exports.subtract = function(x, y) {\n" +
        "    return x - y;\n" +
        "  };\n" +
        "  // commonjs module 2\n" +
        "  var m2 = require(\"m2\");\n" +
        "  m2.exports.multiply = function(x, y) {\n" +
        "    return x * y;\n" +
        "  };\n" +
        "})();";

    Compiler compiler = newCompiler();
    CompilerOptions options = new CompilerOptions();

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler,
        options.getOutputFileRoot(), true);
    Node root = parseTree(input);
    processor.process(null, root);

    Node expectedOutput = readInputFromClasspath("/com/google/javascript/jscomp/test/" +
        "ProcessCommonJSModulesTest_expected_testProcessCommonJSModules.js");

    assertEquals(expectedOutput.toPrintableTree(printer), root.toPrintableTree(printer));

    Node m1Module = findModuleByName(root, "m1");
    assertNotNull(m1Module);
    Node m1ModuleExports = findModuleExports(m1Module);
    assertEquals(2, m1ModuleExports.getChildCount());

    Node addFunction = m1ModuleExports.getChildAtIndex(0);
    assertEquals("add", addFunction.getString());
    assertEquals(2, addFunction.getChildCount());
    assertEquals("x", addFunction.getChildAtIndex(0).getString());
    assertEquals("y", addFunction.getChildAtIndex(1).getString());

    Node subtractFunction = m1ModuleExports.getChildAtIndex(1);
    assertEquals("subtract", subtractFunction.getString());
    assertEquals(2, subtractFunction.getChildCount());
    assertEquals("x", subtractFunction.getChildAtIndex(0).getString());
    assertEquals("y", subtractFunction.getChildAtIndex(1).getString());

    Node m2Module = findModuleByName(root, "m2");
    assertNotNull(m2Module);
    Node m2ModuleExports = findModuleExports(m2Module);
    assertEquals(1, m2ModuleExports.getChildCount());

    Node multiplyFunction = m2ModuleExports.getChildAtIndex(0);
    assertEquals("multiply", multiplyFunction.getString());
    assertEquals(2, multiplyFunction.getChildCount());
    assertEquals("x", multiplyFunction.getChildAtIndex(0).getString());
    assertEquals("y", multiplyFunction.getChildAtIndex(1).getString());

    Set<String> requiredModules = getRequiredModules(root);
    assertEquals(ImmutableSet.of("m1", "m2"), requiredModules);
  }

  private Node findModuleByName(Node root, String name) {
    NodeTraversal traversal = new NodeTraversal(root, new AbstractNodeVisitor() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 1 &&
            "goog.require".equals(n.getChildAtIndex(0).getQualifiedName())) {
          Node[] arguments = n.getChildren();
          for (Node argument : arguments) {
            if (argument.isString() && argument.getString().equals(name)) {
              return n.getParent();
            }
          }
        }
        super.visit(t, n, parent);
      }
    });
    Node moduleNode = traversal.traverseAllChildrenSync(root).get(0);
    return moduleNode;
  }

  private Node findModuleExports(Node moduleNode) {
    for (Node child : moduleNode.getChildren()) {
      if (child.isGetProp() && "module.exports".equals(child.getQualifiedName())) {
        return child.getChildAtIndex(0);
      }
    }
    throw new AssertionError("Could not find module exports in module node.");
  }

  private Set<String> getRequiredModules(Node root) {
    NodeTraversal traversal = new NodeTraversal(root, new AbstractNodeVisitor() {
      private final Set<String> requiredModules = new java.util.HashSet<>();

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 1 &&
            "goog.require".equals(n.getChildAtIndex(0).getQualifiedName())) {
          Node[] arguments = n.getChildren();
          for (Node argument : arguments) {
            requiredModules.add(argument.getString());
          }
        }
        super.visit(t, n, parent);
      }

      @Override
      public void endVisit(NodeTraversal t, Node n, Node parent) {
        super.endVisit(t, n, parent);
      }

      public Set<String> getRequiredModules() {
        return requiredModules;
      }
    });
    traversal.traverseAllChildrenSync(root);
    return traversal.getRequiredModules();
  }
}