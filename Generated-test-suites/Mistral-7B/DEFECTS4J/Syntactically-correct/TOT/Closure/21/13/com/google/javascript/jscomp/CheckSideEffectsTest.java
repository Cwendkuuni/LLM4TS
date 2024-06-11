package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USELESS_CODE");
    Compiler compiler = new Compiler();
    compiler.init(options, null, null);

    String sourceCode = "function foo() { return 1; } " +
        "var x = 1; " +
        "x = foo(); " +
        "var s = 'this string is continued on the next line but you forgot the +'; " +
        "var unused = 0; " +
        "for (var i = 0; i < 10; i++) {" +
        "  var j = 1; " +
        "  j = j + 1; " +
        "}" +
        "void 0; " +
        "foo();; " +
        "for (var i = 0; i < 10; i++) {" +
        "  var j = 1; " +
        "  if (i == 0) {" +
        "    j = i; " +
        "  } " +
        "}" +
        "function bar() { return 1; } " +
        "while (true) { bar(); } ";

    Node ast = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, ast, new CheckSideEffects(compiler, CheckLevel.WARNING, true));

    List<Node> problemNodes = ((CheckSideEffects) compiler.getPassManager().getPass("checkSideEffects"))
        .getProblemNodes();

    assertEquals(7, problemNodes.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?",
        problemNodes.get(0).getError().getMessage());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?",
        problemNodes.get(1).getError().getMessage());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?",
        problemNodes.get(2).getError().getMessage());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?",
        problemNodes.get(3).getError().getMessage());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?",
        problemNodes.get(4).getError().getMessage());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?",
        problemNodes.get(5).getError().getMessage());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?",
        problemNodes.get(6).getError().getMessage());

    problemNodes.get(0).getParent().replaceChild(problemNodes.get(0),
        NodeFactory.createNameNode("JSCOMPILER_PRESERVE", Node.NO_SUCH_NODE));
    compiler.process(ast, null);
    String output = compiler.getErrors().getErrors().get(0).getMessage();
    assertEquals("Suspicious code. {0} (JSC_USELESS_CODE)", output);
  }
}