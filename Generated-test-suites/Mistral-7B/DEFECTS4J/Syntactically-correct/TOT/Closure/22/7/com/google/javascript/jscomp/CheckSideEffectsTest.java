package com.google.javascript.jscomp.CheckSideEffectsTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp. compiler.Compiler;
import com.google.javascript.jscomp.parser.js.JsParser;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CompilerInput input;
 private DiagnosticType warning;

 @Before
 public void setup() {
 compiler = new Compiler();
 warning = DiagnosticType.warning("JSC_USELESS_CODE");
 input = new CompilerInput(new JsParser().parse("(function() {" +
 "  // Test case 1: empty statement at the end of a block" +
 "  if (true) {} // empty statement" +
 "  // Test case 2: empty statement in the middle of an expression" +
 "  x = (5 + (function() {}.bind(this))()) * 2; // empty statement" +
 "  // Test case 3: comment with a missing '+' sign" +
 "  var s = 'this string is ' // continued on the next line but you forgot the +';" +
 "  // Test case 4: expression with a missing '=' sign" +
 "  x == foo(); // missing '=' sign" +
 "  // Test case 5: stray semicolon" +
 "  foo();;" +
 "  // Test case 6: valid statement" +
 "  var x = 5; // valid statement" +
 "})();", null, null));
 }

 @Test
 public void testCheckSideEffects() {
 CheckLevel level = CheckLevel.WARNING;
 compiler.setOptions(new CompilerOptions().check(level, warning));
 NodeTraversal traverser = new NodeTraversal(compiler);
 Node root = input.getAstRoot(compiler);
 CheckSideEffects cb = new CheckSideEffects(compiler, level, false);
 NodeTraversal.traverse(cb, root, cb);
 List<Node> diagnostics = compiler.getDiagnostics();
 int expectedDiagnostics = 3;
 assertEquals(expectedDiagnostics, diagnostics.size());
 // Test case 1: empty statement at the end of a block
 assertEquals(DiagnosticType.WARNING, diagnostics.get(0).getType());
 assertEquals("Suspicious code. empty statement", diagnostics.get(0).getMessage());
 // Test case 2: empty statement in the middle of an expression
 assertEquals(DiagnosticType.WARNING, diagnostics.get(1).getType());
 assertEquals("Suspicious code. The result of the ')' operator is not being used.", diagnostics.get(1).getMessage());
 // Test case 3: comment with a missing '+' sign
 assertNull(diagnostics.get(2));
 // Test case 4: expression with a missing '=' sign
 assertNull(diagnostics.get(3));
 // Test case 5: stray semicolon
 assertNull(diagnostics.get(4));
 // Test case 6: valid statement
 assertNull(diagnostics.get(5));
 }

 @Test
 public void testProtectSideEffects() {
 CheckLevel level = CheckLevel.WARNING;
 boolean protectSideEffectFreeCode = true;
 compiler.setOptions(new CompilerOptions().check(level, warning).keepSideEffectFree(protectSideEffectFreeCode));
 NodeTraversal traverser = new NodeTraversal(compiler);
 Node root = input.getAstRoot(compiler);
 CheckSideEffects cb = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
 NodeTraversal.traverse(cb, root, cb);
 List<Node> diagnostics = compiler.getDiagnostics();
 int expectedDiagnostics = 0;
 assertEquals(expectedDiagnostics, diagnostics.size());
 }

 @After
 public void teardown() {
 compiler = null;
 input = null;
 }
}