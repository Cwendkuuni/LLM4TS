import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private CompilerOptions options;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    checker = compiler.getCheckers().get(CheckGlobalThis.class);
    options = new CompilerOptions();
  }

  @Test
  public void testFunctionWithoutThis() {
    String code = "function foo() {}";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithThisAssignment() {
    String code = "function foo() { var a = this; }";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertTrue(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithThisPropertyAccess() {
    String code = "function foo() { this.bar = 1; }";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertTrue(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotated() {
    String code = "/** @constructor */ function Foo() {}";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithThisPrototype() {
    String code = "function Foo() {} Foo.prototype.bar = function() {};";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithAssignment() {
    String code = "function foo() { a = this; }";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithPrototypeAssignment() {
    String code = "function Foo() {} Foo.prototype.bar = function(a) { this.baz = a; }";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithThisAnnotatedAndPrototype() {
    String code = "/** @constructor */ function Foo() {} Foo.prototype.bar = function() {};";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithLogicalPlaceForThis() {
    String code = "function foo() { this.bar = function() {}; }";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithThisInPrototypeChain() {
    String code = "function Foo() {} Foo.prototype.foo = function() { return this; }";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }

  @Test
  public void testFunctionWithThisInPrototypeChainAndAssignment() {
    String code = "function Foo() {} Foo.prototype.foo = function() { this.a = 1; }";
    Node node = compiler.parseTree(code, null, options);
    NodeTraversal traverser = new NodeTraversal(compiler, node);
    checker.process(traverser, null, node);
    assertFalse(traverser.hasErrors());
  }
}