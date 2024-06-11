import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.test_util.AbstractTestCompiler;
import com.google.javascript.jscomp.test_util.Tests;

public class CheckGlobalThisTest {

  public static void main(String[] args) {
    Tests.runTests(new Object() {}.getClass().getEnclosingClass());
  }

  @SuppressWarnings("unchecked")
  static class TestCompiler extends AbstractTestCompiler {
    TestCompiler() {
      super(new Checker(), new Parser(), new CompilerOptions());
    }

    @Override
    protected AbstractCompiler createCompiler() {
      return new AbstractCompiler() {
        @Override
        public CheckLevel getCheckLevel(String names) {
          return names.equals("checkGlobalThis") ? WARNING : null;
        }
      };
    }
  }

  static class TestCheckGlobalThis extends CheckGlobalThis {
    TestCheckGlobalThis(TestCompiler compiler, CheckLevel level) {
      super(compiler, level);
    }

    @Override
    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
      if (parent != null && parent.getType() == Token.ASSIGN) {
        // Simulate the behavior of the real CheckGlobalThis class
        Node lhs = parent.getFirstChild();
        if (lhs.getType() == Token.THIS) {
          // Always traverse the left side of the assignment for testing purposes
          return true;
        }
      }
      return super.shouldTraverse(t, n, parent);
    }
  }

  @org.junit.Test
  public void testSafeUseOfThis() {
    String source = "function Foo() {}";
    TestCompiler compiler = new TestCompiler();
    Node ast = compiler.parse(source);
    TestCheckGlobalThis checker = new TestCheckGlobalThis(compiler, null);
    checker.traverse(ast, null);
    assertFalse(checker.hasErrors());
  }

  @org.junit.Test
  public void testUnsafeUseOfThis() {
    String source = "function Foo() { this.x = 5; }";
    TestCompiler compiler = new TestCompiler();
    Node ast = compiler.parse(source);
    TestCheckGlobalThis checker = new TestCheckGlobalThis(compiler, null);
    checker.traverse(ast, null);
    assertTrue(checker.hasErrors());
  }

  @org.junit.Test
  public void testUnsafeUseOfThisWithPrototypeProperty() {
    String source = "function Foo() { Object.prototype.x = this; }";
    TestCompiler compiler = new TestCompiler();
    Node ast = compiler.parse(source);
    TestCheckGlobalThis checker = new TestCheckGlobalThis(compiler, null);
    checker.traverse(ast, null);
    assertTrue(checker.hasErrors());
  }

  @org.junit.Test
  public void testSafeUseOfThisInPrototypeMethod() {
    String source = "function Foo() {} Foo.prototype.bar = function() { this.x = 5; }";
    TestCompiler compiler = new TestCompiler();
    Node ast = compiler.parse(source);
    TestCheckGlobalThis checker = new TestCheckGlobalThis(compiler, null);
    checker.traverse(ast, null);
    assertFalse(checker.hasErrors());
  }

  @org.junit.Test
  public void testSafeUseOfThisInFunctionAnnotation() {
    String source = "/** @constructor */ function Foo() { this.x = 5; }";
    TestCompiler compiler = new TestCompiler();
    Node ast = compiler.parse(source);
    TestCheckGlobalThis checker = new TestCheckGlobalThis(compiler, null);
    checker.traverse(ast, null);
    assertFalse(checker.hasErrors());
  }

  @org.junit.Test
  public void testSafeUseOfThisInFunctionWithoutAnnotation() {
    String source = "function Foo() { this.x = 5; }";
    TestCompiler compiler = new TestCompiler();
    Node ast = compiler.parse(source);
    TestCheckGlobalThis checker = new TestCheckGlobalThis(compiler, null);
    checker.traverse(ast, null);
    assertFalse(checker.hasErrors());
  }
}