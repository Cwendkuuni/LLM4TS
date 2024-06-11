import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testSafeUsageOfThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() {\n" +
        "  this.x = 42;\n" +
        "}";

    Node root = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testUnsafeUsageOfThisAsProperty() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "function evil() {\n" +
        "  this.x = 42;\n" +
        "}";

    Node root = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testUnsafeUsageOfThisAsFunctionArgument() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "function bar(fn) {\n" +
        "  fn();\n" +
        "}";

    String unsafeCode = "function evil() {\n" +
        "  bar(this);\n" +
        "}";

    Node root = Parser.parse(code, null, null);
    root = root.getFirstChild(); // Script node
    root.setFirstChild(Parser.parse(unsafeCode, null, root));

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThisInConstructor() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() {\n" +
        "  this.x = 42;\n" +
        "}";

    String constructorCode = "function Foo() {\n" +
        "  constructor: true,\n" +
        "  this.x = 42;\n" +
        "}";

    Node root = Parser.parse(code, null, null);
    Node constructor = Parser.parse(constructorCode, null, root);
    root.setFirstChild(constructor);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisInPrototypeMethod() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() {}\n" +
        "Foo.prototype.bar = function() {\n" +
        "  this.x = 42;\n" +
        "}";

    Node root = Parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisInFunctionAnnotatedWithThis() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "function @this Foo() {}\n" +
        "Foo.prototype.bar = function() {\n" +
        "  this.x = 42;\n" +
        "}";

    Node root = Parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisInFunctionWhereNoAnnotationIsPresent() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "function Foo() {\n" +
        "  this.x = 42;\n" +
        "}";

    Node root = Parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  @org.junit.Test
  public void testCheckGlobalThisInObjectLiteral() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "var obj = {\n" +
        "  x: function() {\n" +
        "    this.y = 42;\n" +
        "  }\n" +
        "}";

    Node root = Parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisInObjectLiteralWithNestedFunction() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "var obj = {\n" +
        "  x: function() {\n" +
        "    this.y = 42;\n" +
        "    return {\n" +
        "      z: function() {\n" +
        "        this.x = 10;\n" +
        "      }\n" +
        "    };\n" +
        "  }\n" +
        "}";

    Node root = Parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @org.junit.Test
  public void testCheckGlobalThisInArrayLiteral() {
    AbstractCompiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);

    String code = "var arr = [function() {\n" +
        "  this.x = 42;\n" +
        "}]";

    Node root = Parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    DiagnosticType diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }
}