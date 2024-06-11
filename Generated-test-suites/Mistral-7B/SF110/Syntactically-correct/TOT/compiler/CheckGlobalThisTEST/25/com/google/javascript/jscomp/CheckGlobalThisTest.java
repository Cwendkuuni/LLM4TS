import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckerFactory.makeChecker;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.JSDocInfo.*;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.ParserVisitor;
import com.google.javascript.rhino.JSDocInfo;

/**
 * Tests for the {@code CheckGlobalThis} class.
 */
public final class CheckGlobalThisTest {

  private static class TestCompiler extends AbstractCompiler {

    @Override
    Checker getChecker() {
      return makeChecker(CheckGlobalThis.class);
    }

    @Override
    boolean shouldSkipExternalLibraries() {
      return true;
    }

    @Override
    CompilerOptions getOptions() {
      CompilerOptions options = new CompilerOptions();
      options.warnOnly = true;
      return options;
    }

    @Override
    ParserVisitor getSourceVisitor() {
      return null;
    }

    @Override
    void processSourceUnits(Node root) {
      // No-op
    }
  }

  @Test
  public void testSafeUses() {
    String code = "function foo() { return this.x; }";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testUnsafePrototype() {
    String code = "var obj = { foo: function() {} }; obj.foo.x = this;";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    Diagnostic diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @Test
  public void testUnsafeGetter() {
    String code = "var x = {}; x.getThis = function() { return this; }";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    Diagnostic diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @Test
  public void testUnsafeFunction() {
    String code = "function evil() { this.x = 1; }";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    Diagnostic diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String code = "function Foo@this() {}";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testConstructor() {
    String code = "function Foo() { this.x = 1; }";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithPrototypeMethod() {
    String code = "function Foo() {}\nFoo.prototype.foo = function() { this.x = 1; }";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithJSDoc() {
    String code = "/** @constructor */ function Foo() { this.x = 1; }";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithNoLogicalPlaceForThisAnnotation() {
    String code = "function evil() { this.x = 1; }";
    TestCompiler compiler = new TestCompiler();
    Node root = Parser.parseCompilationUnit(code, null);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traversal.traverse();
    assertEquals(1, compiler.getDiagnostics().size());
    Diagnostic diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }
}