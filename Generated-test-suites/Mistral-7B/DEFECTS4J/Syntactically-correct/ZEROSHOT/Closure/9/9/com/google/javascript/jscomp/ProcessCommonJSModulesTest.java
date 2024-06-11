package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.util.List;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    CompilerOptions options = TestCompiler.getDefaultOptions();
    options.setCompilerPass(CompilerPass.class, new ProcessCommonJSModules(null, ""));

    Compiler compiler = new TestCompiler(options);
    Node root = NodeFactory.createScriptlet(null, null, null);

    String input =
        "function main() {" +
        "require('a');" +
        "require('b');" +
        "}" +
        "module.exports = main;" +
        "// a.js" +
        "var a = 1;" +
        "module.exports = a;" +
        "// b.js" +
        "var b = 2;" +
        "module.exports = b;" +
        ";";

    Node ast = compiler.compile(input, "test.js");
    assertNotNull(ast);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(NodeFactory.createNull(), ast);

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    Node main = traversal.findFunctionByName("main");
    assertNotNull(main);

    // Check that require('a') is replaced with 'a' and require('b') is replaced with 'b'
    assertEquals("a", main.getChildAtIndex(0).getChildAtIndex(1).getString());
    assertEquals("b", main.getChildAtIndex(1).getChildAtIndex(1).getString());

    // Check that 'a' and 'b' are renamed to 'a$$main' and 'b$$main'
    assertEquals("a$$main", main.getChildAtIndex(0).getChildAtIndex(0).getString());
    assertEquals("b$$main", main.getChildAtIndex(1).getChildAtIndex(0).getString());

    // Check that module.exports is renamed to 'main$$exports'
    assertEquals("main$$exports", main.getChildAtIndex(main.getChildCount() - 1).getString());

    // Check that 'a' and 'b' are assigned to 'main$$exports'
    List<Node> children = main.getChildren();
    assertEquals(3, children.size());
    Node assignments = children.get(2);
    List<Node> assignmentChildren = assignments.getChildren();
    assertEquals(2, assignmentChildren.size());
    assertEquals("a$$main", assignmentChildren.get(0).getChildren().get(0).getString());
    assertEquals("b$$main", assignmentChildren.get(1).getChildren().get(0).getString());
  }
}