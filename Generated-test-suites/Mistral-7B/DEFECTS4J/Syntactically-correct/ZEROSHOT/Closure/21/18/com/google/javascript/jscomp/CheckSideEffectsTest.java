import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SimpleCompilerPass;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = new Compiler();
    Checker checker = new Checker(compiler, new SimpleCompilerPass[0], null);
    CompilerInput input = new CompilerInput(CheckSideEffectsTest.class.getResource("test_check_side_effects.js"));
    checker.check(input, new DiagnosticLevel(WARNING), null);
    List<Node> problemNodes = Lists.newArrayList();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = input.getAstRoot(checkSideEffects.getCompiler());
    NodeTraversal.traverse(checkSideEffects, root, null);
    checkSideEffects.protectSideEffects();
    problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    checkSideEffects.problemNodes.clear();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(checkSideEffects.compiler);
    NodeTraversal.traverse(stripProtection, root, null);
    assertEquals(0, problemNodes.size());

    // Test for suspicious code with missing '+' sign
    Node testNode1 = NodeFactory.createStringLiteral("this string is ");
    Node testNode2 = NodeFactory.createStringLiteral("continued on the next line but you forgot the +");
    Node testNode3 = NodeFactory.createBinaryOperator(Token.PLUS, testNode1, testNode2);
    Node testNode4 = NodeFactory.createAssign(Token.ASSIGN, NodeFactory.createName("x"), NodeFactory.createCall(NodeFactory.createName("foo"), null));
    Node testNode5 = NodeFactory.createExpressionStatement(testNode4);
    Node testNode6 = NodeFactory.createExpressionStatement(testNode3);
    Node testNode7 = NodeFactory.createBlock(new Node[] {testNode5, testNode6});
    Node testRoot = NodeFactory.createBlock(new Node[] {testNode7});
    checker.check(new CompilerInput(testRoot), new DiagnosticLevel(WARNING), null);
    problemNodes.clear();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(checkSideEffects, testRoot, null);
    checkSideEffects.protectSideEffects();
    problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    checkSideEffects.problemNodes.clear();
    stripProtection = new CheckSideEffects.StripProtection(checkSideEffects.compiler);
    NodeTraversal.traverse(stripProtection, testRoot, null);
    assertEquals(1, problemNodes.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", problemNodes.get(0).toString());

    // Test for stray semicolon
    Node testNode8 = NodeFactory.createExpressionStatement(NodeFactory.createCall(NodeFactory.createName("foo"), null));
    Node testNode9 = NodeFactory.createSemicolon();
    Node testNode10 = NodeFactory.createBlock(new Node[] {testNode8, testNode9});
    testNode7 = NodeFactory.createBlock(new Node[] {testNode10});
    testRoot = NodeFactory.createBlock(new Node[] {testNode7});
    checker.check(new CompilerInput(testRoot), new DiagnosticLevel(WARNING), null);
    problemNodes.clear();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(checkSideEffects, testRoot, null);
    checkSideEffects.protectSideEffects();
    problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    checkSideEffects.problemNodes.clear();
    stripProtection = new CheckSideEffects.StripProtection(checkSideEffects.compiler);
    NodeTraversal.traverse(stripProtection, testRoot, null);
    assertEquals(1, problemNodes.size());
    assertEquals("Suspicious code. Probably just a stray-semicolon. Doesn't hurt to check though", problemNodes.get(0).toString());

    // Test for function call with no result
    Node testNode11 = NodeFactory.createName("foo");
    Node testNode12 = NodeFactory.createCall(testNode11, null);
    Node testNode13 = NodeFactory.createExpressionStatement(testNode12);
    testNode7 = NodeFactory.createBlock(new Node[] {testNode13});
    testRoot = NodeFactory.createBlock(new Node[] {testNode7});
    checker.check(new CompilerInput(testRoot), new DiagnosticLevel(WARNING), null);
    problemNodes.clear();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(checkSideEffects, testRoot, null);
    checkSideEffects.protectSideEffects();
    problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    checkSideEffects.problemNodes.clear();
    stripProtection = new CheckSideEffects.StripProtection(checkSideEffects.compiler);
    NodeTraversal.traverse(stripProtection, testRoot, null);
    assertEquals(1, problemNodes.size());
    assertEquals("Suspicious code. The result of the 'function_call' operator is not being used.", problemNodes.get(0).toString());
  }

  @Test
  public void testCheckSideEffectsWithProtectedCode() {
    AbstractCompiler compiler = new Compiler();
    Checker checker = new Checker(compiler, new SimpleCompilerPass[0], null);
    CompilerInput input = new CompilerInput(CheckSideEffectsTest.class.getResource("test_check_side_effects.js"));
    checker.check(input, new DiagnosticLevel(WARNING), null);
    List<Node> problemNodes = Lists.newArrayList();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    Node root = input.getAstRoot(checkSideEffects.getCompiler());
    NodeTraversal.traverse(checkSideEffects, root, null);
    checkSideEffects.protectSideEffects();
    problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    checkSideEffects.problemNodes.clear();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(checkSideEffects.compiler);
    NodeTraversal.traverse(stripProtection, root, null);
    assertEquals(0, problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsWithDeadCode() {
    AbstractCompiler compiler = new Compiler();
    Checker checker = new Checker(compiler, new SimpleCompilerPass[0], null);
    CompilerInput input = new CompilerInput(CheckSideEffectsTest.class.getResource("test_check_side_effects_dead_code.js"));
    checker.check(input, new DiagnosticLevel(WARNING), null);
    List<Node> problemNodes = Lists.newArrayList();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    Node root = input.getAstRoot(checkSideEffects.getCompiler());
    NodeTraversal.traverse(checkSideEffects, root, null);
    checkSideEffects.protectSideEffects();
    problemNodes = Lists.newArrayList(checkSideEffects.problemNodes);
    checkSideEffects.problemNodes.clear();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(checkSideEffects.compiler);
    NodeTraversal.traverse(stripProtection, root, null);
    assertEquals(1, problemNodes.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", problemNodes.get(0).toString());
  }
}