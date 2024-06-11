package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.compiler.CompilerPassManager;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  AbstractCompiler compiler;
  CompilerPassManager passManager;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    passManager = new CompilerPassManager(compiler);
  }

  @Test
  public void testMinimizeExitPoints() {
    // Test case 1: No exit points
    String sourceCode = "function test() { var x = 1; }";
    Node ast = ParserTestUtil.parseAndCompile(sourceCode);
    passManager.addPass(new MinimizeExitPoints(compiler));
    NodeTraversal.traverse(passManager, ast, new NodeOperations.IdentityCallback());
    assertFalse(ast.isLabel());
    assertFalse(ast.isFunction());
    assertFalse(ast.isIf());
    assertFalse(ast.isFor());
    assertFalse(ast.isWhile());
    assertFalse(ast.isDo());
    assertFalse(ast.isBlock());
    assertFalse(ast.isReturn());
    assertFalse(ast.isBreak());
    assertFalse(ast.isContinue());

    // Test case 2: Return statement
    sourceCode = "function test() { return 1; }";
    ast = ParserTestUtil.parseAndCompile(sourceCode);
    passManager.addPass(new MinimizeExitPoints(compiler));
    NodeTraversal.traverse(passManager, ast, new NodeOperations.IdentityCallback());
    assertTrue(ast.isReturn());
    assertFalse(ast.isLabel());
    assertFalse(ast.isFunction());
    assertFalse(ast.isIf());
    assertFalse(ast.isFor());
    assertFalse(ast.isWhile());
    assertFalse(ast.isDo());
    assertFalse(ast.isBlock());
    assertFalse(ast.getLastChild().isReturn());

    // Test case 3: Break statement
    sourceCode = "function test() { break testLabel; }";
    ast = ParserTestUtil.parseAndCompile(sourceCode);
    passManager.addPass(new MinimizeExitPoints(compiler));
    NodeTraversal.traverse(passManager, ast, new NodeOperations.IdentityCallback());
    assertFalse(ast.isReturn());
    assertFalse(ast.isLabel());
    assertFalse(ast.isFunction());
    assertFalse(ast.isIf());
    assertFalse(ast.isFor());
    assertFalse(ast.isWhile());
    assertFalse(ast.isDo());
    assertFalse(ast.isBlock());
    Node breakNode = ast.getLastChild();
    assertTrue(breakNode.isBreak());
    Node labelNode = breakNode.getPrevious();
    assertTrue(labelNode.isLabel());
    assertTrue(labelNode.getString().equals("testLabel"));

    // Test case 4: Continue statement
    sourceCode = "function test() { for (var i = 0; i < 5; i++) { if (i === 3) continue; console.log(i); } }";
    ast = ParserTestUtil.parseAndCompile(sourceCode);
    passManager.addPass(new MinimizeExitPoints(compiler));
    NodeTraversal.traverse(passManager, ast, new NodeOperations.IdentityCallback());
    assertFalse(ast.isReturn());
    assertFalse(ast.isLabel());
    assertFalse(ast.isFunction());
    assertFalse(ast.isIf());
    assertFalse(ast.isFor());
    assertFalse(ast.isWhile());
    assertFalse(ast.isDo());
    assertFalse(ast.isBlock());
    Node forNode = ast.getLastChild();
    assertTrue(forNode.isFor());
    Node continuenode = forNode.getFirstChild().getNext();
    assertTrue(continuenode.isContinue());

    // Test case 5: If statements with return and break
    sourceCode = "function test() { if (true) return; if (false) break testLabel; }";
    ast = ParserTestUtil.parseAndCompile(sourceCode);
    passManager.addPass(new MinimizeExitPoints(compiler));
    NodeTraversal.traverse(passManager, ast, new NodeOperations.IdentityCallback());
    assertFalse(ast.isReturn());
    assertFalse(ast.isLabel());
    assertFalse(ast.isFunction());
    assertFalse(ast.isIf());
    assertFalse(ast.isFor());
    assertFalse(ast.isWhile());
    assertFalse(ast.isDo());
    assertFalse(ast.isBlock());
    Node ifNode1 = ast.getLastChild();
    assertTrue(ifNode1.isIf());
    Node ifNode2 = ifNode1.getPrevious();
    assertTrue(ifNode2.isIf());
    assertFalse(ifNode1.getLastChild().isReturn());
    assertFalse(ifNode1.getLastChild().isBreak());
    Node breakNode2 = ifNode2.getLastChild();
    assertFalse(breakNode2.isReturn());
    assertTrue(breakNode2.isBreak());
    Node labelNode2 = breakNode2.getPrevious();
    assertTrue(labelNode2.isLabel());
    assertTrue(labelNode2.getString().equals("testLabel"));

    // Test case 6: If statements with return and continue
    sourceCode = "function test() { if (true) continue; if (false) return; }";
    ast = ParserTestUtil.parseAndCompile(sourceCode);
    passManager.addPass(new MinimizeExitPoints(compiler));
    NodeTraversal.traverse(passManager, ast, new NodeOperations.IdentityCallback());
    assertFalse(ast.isReturn());
    assertFalse(ast.isLabel());
    assertFalse(ast.isFunction());
    assertFalse(ast.isIf());
    assertFalse(ast.isFor());
    assertFalse(ast.isWhile());
    assertFalse(ast.isDo());
    assertFalse(ast.isBlock());
    Node ifNode3 = ast.getLastChild();
    assertTrue(ifNode3.isIf());
    Node ifNode4 = ifNode3.getPrevious();
    assertTrue(ifNode4.isIf());
    assertFalse(ifNode3.getLastChild().isReturn());
    assertFalse(ifNode3.getLastChild().isBreak());
    Node continueNode3 = ifNode4.getLastChild();
    assertFalse(continueNode3.isReturn());
    assertTrue(continueNode3.isContinue());
  }
}