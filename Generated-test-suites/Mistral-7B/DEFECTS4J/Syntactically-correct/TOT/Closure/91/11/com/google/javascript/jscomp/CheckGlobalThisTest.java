import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test_utils.AbstractCompilerTestCase;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest extends AbstractCompilerTestCase {

  public void testCheckGlobalThis_NoIssues() {
    String source = "function Foo() {}";
    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(0, checker.numErrors());
  }

  public void testCheckGlobalThis_Constructor() {
    String source = "function Foo() {}";
    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(0, checker.numErrors());

    source = "function Foo() { this.x = 1; }";
    compiler = new Compiler();
    options = new CompilerOptions(source);
    checker = new CheckGlobalThis(compiler, WARNING);
    ast = traverse(source, checker);
    assertEquals(1, checker.numErrors());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getError(0).getCode());
  }

  public void testCheckGlobalThis_PrototypeMethod() {
    String source = "function Foo() {}";
    source += "\nFoo.prototype.bar = function() { this.x = 1; }";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(0, checker.numErrors());
  }

  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    String source = "function Foo@this() { this.x = 1; }";
    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(0, checker.numErrors());
  }

  public void testCheckGlobalThis_ImplicitFunction() {
    String source = "var x = function() { this.x = 1; }";
    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(1, checker.numErrors());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getError(0).getCode());
  }

  public void testCheckGlobalThis_Assignment() {
    String source = "var a = function() { this.x = 1; }";
    source += "\nvar b = a;";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(1, checker.numErrors());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getError(0).getCode());
  }

  public void testCheckGlobalThis_GetProperty() {
    String source = "function Foo() {}";
    source += "\nFoo.prototype.bar = function() { this.x = 1; }";
    source += "\nvar a = new Foo();";
    source += "\nvar b = a.bar;";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(0, checker.numErrors());
  }

  public void testCheckGlobalThis_GetPropertyChain() {
    String source = "function Foo() {}";
    source += "\nFoo.prototype.bar = function() { this.x = 1; }";
    source += "\nFoo.prototype.baz = { foo: function() {}}";
    source += "\nvar a = new Foo();";
    source += "\nvar b = a.bar.foo;";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(0, checker.numErrors());
  }

  public void testCheckGlobalThis_GlobalFunction() {
    String source = "function globalFunc() { this.x = 1; }";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(source);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node ast = traverse(source, checker);
    assertEquals(1, checker.numErrors());
    assertEquals("JSC_USED_GLOBAL_THIS", checker.getError(0).getCode());
  }
}