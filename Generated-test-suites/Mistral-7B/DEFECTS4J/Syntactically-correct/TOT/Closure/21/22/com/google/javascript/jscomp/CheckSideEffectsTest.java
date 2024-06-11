package com.google.javascript.jscomp;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffectsWithSimpleAssignment() {
    String sourceCode = "function foo() {" +
            "  var x = 1;" +
            "  return x * 2;" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = AbstractCompiler.parseCompilationUnit(sourceCode, null);
    checkSideEffects.process(null, root);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsWithUselessCode() {
    String sourceCode = "function foo() {" +
            "  var x = 1;" +
            "  x = x;" + // useless assignment
            "  return x * 2;" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = AbstractCompiler.parseCompilationUnit(sourceCode, null);
    checkSideEffects.process(null, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertFalse(problemNodes.isEmpty());
    Node problemNode = problemNodes.get(0);
    assertEquals(NodeType.ASSIGN, problemNode.getType());
    Node parent = problemNode.getParent();
    assertEquals(NodeType.BLOCK, parent.getType());
  }

  @Test
  public void testCheckSideEffectsWithSimpleOperator() {
    String sourceCode = "function foo() {" +
            "  return 1 + 2;" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = AbstractCompiler.parseCompilationUnit(sourceCode, null);
    checkSideEffects.process(null, root);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsWithCommaOperator() {
    String sourceCode = "function foo() {" +
            "  return 1," + // comma operator
            "            2;" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = AbstractCompiler.parseCompilationUnit(sourceCode, null);
    checkSideEffects.process(null, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertFalse(problemNodes.isEmpty());
    Node problemNode = problemNodes.get(0);
    assertEquals(NodeType.EXPR_RESULT, problemNode.getType());
    Node parent = problemNode.getParent();
    assertEquals(NodeType.BLOCK, parent.getType());
  }

  @Test
  public void testCheckSideEffectsWithEmptyStatement() {
    String sourceCode = "function foo() {" +
            "}" +
            "return 1;";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = AbstractCompiler.parseCompilationUnit(sourceCode, null);
    checkSideEffects.process(null, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithForLoop() {
    String sourceCode = "function foo() {" +
            "  for (var i = 0; i < 5; i++) {" +
            "  }" +
            "  return 1;";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = AbstractCompiler.parseCompilationUnit(sourceCode, null);
    checkSideEffects.process(null, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());
  }

  @Test
  public void testCheckSideEffectsWithSideEffectFreeCodeProtected() {
    String sourceCode = "function foo() {" +
            "  var result = JSCOMPILER_PRESERVE(function() {" +
            "    var x = 1;" +
            "    return x * 2;" +
            "  });" +
            "  return result();" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = AbstractCompiler.parseCompilationUnit(sourceCode, null);
    checkSideEffects.process(null, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue(problemNodes.isEmpty());
  }
}