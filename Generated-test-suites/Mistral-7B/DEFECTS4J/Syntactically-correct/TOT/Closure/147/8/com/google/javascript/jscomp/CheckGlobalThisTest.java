import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.ERROR;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    String source = "class Foo {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "  @this\n" +
        "  method() {\n" +
        "  }\n" +
        "}\n" +
        "function Bar() {\n" +
        "  this.prop = 42;\n" +
        "}\n" +
        "function Baz() {\n" +
        "  this.prop = function() {}\n" +
        "}";

    Compiler compiler = new Compiler();
    compiler.setOption("warn_unused_require", ERROR);
    compiler.process(null, source, null);
    Node ast = compiler.getRootNode();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.level.isEnabled(WARNING, GLOBAL_THIS));
    assertFalse(checker.compiler.getDiagnostics().hasError());
  }

  @Test
  public void testUnsafeAssignment() {
    String source = "function evil() {\n" +
        "  this.prop = 42;\n" +
        "}";

    Compiler compiler = new Compiler();
    compiler.setOption("warn_unused_require", ERROR);
    compiler.process(null, source, null);
    Node ast = compiler.getRootNode();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertFalse(checker.level.isEnabled(WARNING, GLOBAL_THIS));
    assertFalse(checker.compiler.getDiagnostics().hasError());

    checker.level.setLevel(ERROR, GLOBAL_THIS);
    traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.compiler.getDiagnostics().hasError());
  }

  @Test
  public void testUnsafePropertyAccess() {
    String source = "function evil() {\n" +
        "  var a = this;\n" +
        "  a.prop = 42;\n" +
        "}";

    Compiler compiler = new Compiler();
    compiler.setOption("warn_unused_require", ERROR);
    compiler.process(null, source, null);
    Node ast = compiler.getRootNode();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertFalse(checker.level.isEnabled(WARNING, GLOBAL_THIS));
    assertFalse(checker.compiler.getDiagnostics().hasError());

    checker.level.setLevel(ERROR, GLOBAL_THIS);
    traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.compiler.getDiagnostics().hasError());
  }

  @Test
  public void testUnsafeFunctionWithoutThisAnnotation() {
    String source = "function evil() {\n" +
        "  function nested() {\n" +
        "    this.prop = 42;\n" +
        "  }\n" +
        "}";

    Compiler compiler = new Compiler();
    compiler.setOption("warn_unused_require", ERROR);
    compiler.process(null, source, null);
    Node ast = compiler.getRootNode();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertFalse(checker.level.isEnabled(WARNING, GLOBAL_THIS));
    assertFalse(checker.compiler.getDiagnostics().hasError());

    checker.level.setLevel(ERROR, GLOBAL_THIS);
    traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.compiler.getDiagnostics().hasError());
  }

  @Test
  public void testUnsafePrototypePropertyAccess() {
    String source = "function Foo() {\n" +
        "}\n" +
        "Foo.prototype.method = function() {\n" +
        "  this.prop = 42;\n" +
        "}";

    Compiler compiler = new Compiler();
    compiler.setOption("warn_unused_require", ERROR);
    compiler.process(null, source, null);
    Node ast = compiler.getRootNode();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertFalse(checker.level.isEnabled(WARNING, GLOBAL_THIS));
    assertFalse(checker.compiler.getDiagnostics().hasError());
  }

  @Test
  public void testUnsafePrototypePropertyAccessWithThisAnnotation() {
    String source = "function Foo() {\n" +
        "}\n" +
        "/**\n" +
        "* @constructor\n" +
        "*/\n" +
        "Foo.prototype.method = function() {\n" +
        "  @this\n" +
        "  this.prop = 42;\n" +
        "}";

    Compiler compiler = new Compiler();
    compiler.setOption("warn_unused_require", ERROR);
    compiler.process(null, source, null);
    Node ast = compiler.getRootNode();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertTrue(checker.level.isEnabled(WARNING, GLOBAL_THIS));
    assertFalse(checker.compiler.getDiagnostics().hasError());
  }
}