package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestingUtil.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest {

 private Compiler compiler;
 private ErrorReporter errorReporter;

 @Before
 public void setUp() {
 compiler = newCompiler();
 errorReporter = newErrorReporter();
 compiler.setErrorReporter(errorReporter);
 }

 @Test
 public void testConstantFolding() {
 Node code = toNode("function foo() { return 1 + 1; }");
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding());
 pass.process(null, code);
 assertEquals("function foo() { return 2; }", printNode(code));
 }

 @Test
 public void testEmptyFunction() {
 Node code = toNode("function foo() {}");
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new DeadCodeRemoval());
 pass.process(null, code);
 assertEquals("", printNode(code));
 }

 @Test
 public void testVariableAssignment() {
 Node code = toNode("function foo() { var a = 1; return a + 1; }");
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new DeadCodeRemoval());
 pass.process(null, code);
 assertEquals("function foo() { return 2; }", printNode(code));
 }

 @Test
 public void testVariableReassignment() {
 Node code = toNode("function foo() { var a = 1; a = 2; return a; }");
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new DeadCodeRemoval());
 pass.process(null, code);
 assertEquals("function foo() { return 2; }", printNode(code));
 }

 @Test
 public void testVariableDeclarationWithInitialization() {
 Node code = toNode("function foo() { var a = 1; return a + 1; }");
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new DeadCodeRemoval());
 pass.process(null, code);
 assertEquals("function foo() { return 2; }", printNode(code));
 }

 @Test
 public void testVariableDeclarationWithoutInitialization() {
 Node code = toNode("function foo() { var a; return a + 1; }");
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new DeadCodeRemoval());
 pass.process(null, code);
 assertEquals("function foo() { return 1; }", printNode(code));
 }

 @Test
 public void testFunctionCallWithConstantArguments() {
 Node code = toNode("function foo(a, b) { return a + b; }");
 Node callNode = toNode("foo(1, 2)"); ;
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding());
 pass.process(null, callNode);
 assertEquals("2", printNode(callNode));
 }

 @Test
 public void testFunctionCallWithVariableArguments() {
 Node code = toNode("function foo(a, b) { return a + b; }");
 Node callNode = toNode("foo(1, 2 * 3)"); ;
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding());
 pass.process(null, callNode);
 assertEquals("7", printNode(callNode));
 }

 @Test(expected = AssertionError.class)
 public void testErrorScenario() {
 Node code = toNode("function foo() { throw new Error(\"test\"); }");
 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new DeadCodeRemoval());
 pass.process(null, code);
 fail("Expected error but none was thrown.");
 }

 @After
 public void tearDown() {
 compiler = null;
 errorReporter = null;
}

}