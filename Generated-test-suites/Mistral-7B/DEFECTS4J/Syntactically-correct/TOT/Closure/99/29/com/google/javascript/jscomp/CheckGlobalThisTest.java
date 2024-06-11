import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @org.junit.Before
  public void setUp() {
    options = new CompilerOptions(null);
    compiler = new AbstractCompiler(options);
  }

  @org.junit.Test
  public void testSafeUses() {
    String code = "function Foo() { } Foo.prototype.bar = function() { }";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertFalse("No warning expected for safe use of 'this'", cb.hasWarnings());
  }

  @org.junit.Test
  public void testUnsafeAssignment() {
    String code = "var a = this; a.prop = 42";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertTrue("Warning expected for unsafe use of 'this'", cb.hasWarnings());
  }

  @org.junit.Test
  public void testUnsafeFunctionCall() {
    String code = "function evil() { this.prop = 42 }; evil();";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertTrue("Warning expected for unsafe use of 'this'", cb.hasWarnings());
  }

  @org.junit.Test
  public void testPrototypeProperty() {
    String code = "function Foo() { } Foo.prototype.bar = function() {}; Foo.prototype.prototype.qux = function() { }";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertFalse("No warning expected for safe use of 'this' in prototype property", cb.hasWarnings());
  }

  @org.junit.Test
  public void testFunctionAnnotatedWithConstructor() {
    String code = "function Foo() { this.prop = 42 }";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertFalse("No warning expected for safe use of 'this' in constructor", cb.hasWarnings());
  }

  @org.junit.Test
  public void testFunctionAnnotatedWithThis() {
    String code = "@this class Foo { this.prop = 42 }";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertFalse("No warning expected for safe use of 'this' in '@this' annotated class", cb.hasWarnings());
  }

  @org.junit.Test
  public void testFunctionWithNoClearLocationForThisAnnotation() {
    String code = "function evil() { this.prop = 42 }; var x = 42; function safe() { x = 42 }";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertFalse("No warning expected for safe use of 'this' in a function with no clear location for 'this' annotation", cb.hasWarnings());
  }

  @org.junit.Test
  public void testGlobalVariableWithThis() {
    String code = "var a = this;";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertTrue("Warning expected for unsafe use of 'this' in global variable assignment", cb.hasWarnings());
  }

  @org.junit.Test
  public void testFunctionCallWithThis() {
    String code = "function evil() { this.prop = 42 }; evil();";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertTrue("Warning expected for unsafe use of 'this' in function call", cb.hasWarnings());
  }

  @org.junit.Test
  public void testNestedFunctionWithThis() {
    String code = "function outer() { function inner() { this.prop = 42 } } outer();";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertTrue("Warning expected for unsafe use of 'this' in nested function", cb.hasWarnings());
  }

  @org.junit.Test
  public void testThisInAssignemntStatement() {
    String code = "this.prop = 42";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertTrue("Warning expected for unsafe use of 'this'", cb.hasWarnings());
  }

  @org.junit.Test
  public void testThisInPropertyAccess() {
    String code = "x = this.prop";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertTrue("Warning expected for unsafe use of 'this'", cb.hasWarnings());
  }

  @org.junit.Test
  public void testThisIngetProp() {
    String code = "x = this.prototype.prop";
    Node node = NodeUtil.parse(code);
    CheckGlobalThis cb = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(node, cb);
    traversal.traverse();
    assertTrue("Warning expected for unsafe use of 'this'", cb.hasWarnings());
  }
}