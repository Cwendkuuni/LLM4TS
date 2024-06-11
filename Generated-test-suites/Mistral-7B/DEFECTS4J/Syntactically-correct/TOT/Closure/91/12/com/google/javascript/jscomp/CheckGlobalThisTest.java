package com.google.javascript.jscomp.CheckGlobalThisTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private Compiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testSafeUsage() {
    String code = "function Foo() { this.x = 1; }";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testUnsafeAssignment() {
    String code = "var x = this;";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().size() == 1);
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isWarning());
    assertTrue(diagnostic.getMessage().startsWith("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafeMethodCall() {
    String code = "function Foo() { this.callUnsafeMethod(); } function callUnsafeMethod() {}";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().size() == 1);
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isWarning());
    assertTrue(diagnostic.getMessage().startsWith("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafePropertyAccess() {
    String code = "function Foo() { return this.x; }";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().size() == 1);
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isWarning());
    assertTrue(diagnostic.getMessage().startsWith("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafePrototypePropertyAccess() {
    String code = "function Foo() {} Foo.prototype.x = function() { return this.y; }";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testConstructorFunction() {
    String code = "function Foo() { this.x = 1; } new Foo();";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithJSDocAnnotation() {
    String code = "/** @constructor */ function Foo() { this.x = 1; }";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String code = "function Foo() { @this this.x = 1; }";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    String code = "function Foo() { this.x = 1; }";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertFalse(compiler.getDiagnostics().isEmpty());
    assertTrue(compiler.getDiagnostics().size() == 1);
    DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnostic.isWarning());
    assertTrue(diagnostic.getMessage().startsWith("dangerous use of the global 'this' object"));
  }

  @Test
  public void testObjectLiteral() {
    String code = "var obj = { x: function() {this.y = 1;}};";
    Node node = AbstractCompilerTest.getTopLevelNode(code);
    NodeTraversal traversal = new NodeTraversal(new Node(Token.SCRIPT, null));
    traversal.traverse(node);
    checker.traverse(traversal, node, null);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}