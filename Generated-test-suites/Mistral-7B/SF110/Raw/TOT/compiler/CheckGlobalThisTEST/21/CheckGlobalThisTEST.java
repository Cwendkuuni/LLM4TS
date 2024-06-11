import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.AbstractTraversal.traverse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  public static void main(String[] args) {
    runTest(new CheckGlobalThisTest());
  }

  public void testSafeUses() {
    String source = "function Foo() {}\n"
        + "var bar = {\n"
        + "  foo: function() {}\n"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertTrue(diagnostics.getNumErrors() == 0);
  }

  public void testGlobalThis() {
    String source = "function evil() { this.useful = undefined; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertTrue(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testPrototypeProperty() {
    String source = "var obj = {\n"
        + "  foo: function() { this.prototype.bar = undefined; },\n"
        + "  bar: function() {}\n"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertTrue(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testFunctionWithJSDoc() {
    String source = "/**\n"
        + "@constructor\n"
        + "*/\n"
        + "function Foo() {}\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testFunctionWithThisType() {
    String source = "/**\n"
        + "@this {value: number}\n"
        + "*/\n"
        + "function Foo(o) { this.value += o; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testFunctionWithNoAnnotation() {
    String source = "function Foo() { this.x = 1; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testFunctionLentToPrototype() {
    String source = "function Foo() {}\n"
        + "Foo.prototype.bar = function() { this.x = 1; };";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertTrue(diagnostics.getNumErrors() == 0);
  }

  public void testAssignment() {
    String source = "var x = this;";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testAssignmentWithThisLhs() {
    String source = "this.x = this;";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertTrue(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testPropertyAccess() {
    String source = "function Foo() { this.prototype.x = 1; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertTrue(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testPropertyAccessWithNonPrototype() {
    String source = "function Foo() { this.x = 1; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testBlockScopeFunction() {
    String source = "function Foo() {\n"
        + "  this.x = 1;\n"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  public void testBlockScopeFunctionWithPrototypeProperty() {
    String source = "function Foo() {\n"
        + "  this.prototype.x = 1;\n"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    Diagnostics diagnostics = new Diagnostics();

    Node root = TestSourceFile.getAST(source, null, compiler);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new CheckGlobalThis(compiler).traverse(traversal);
    assertTrue(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }
}