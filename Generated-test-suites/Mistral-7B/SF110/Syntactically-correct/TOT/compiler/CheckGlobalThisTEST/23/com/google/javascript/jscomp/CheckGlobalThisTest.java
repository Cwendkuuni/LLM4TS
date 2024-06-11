import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUseOfThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "function Foo() {\n" +
        "  this.foo = 1;\n" +
        "  var bar = function() { this.bar = 2; };\n" +
        "  this.baz = {\n" +
        "    qux: function() { this.qux = 3; }\n" +
        "  };\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertThat(checker.getDiagnostics(), empty());
  }

  @Test
  public void testUnsafeUseOfThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "function Foo() {\n" +
        "  this.foo = 1;\n" +
        "  (function() { this.bar = 2; }).call(this);\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    Node diagnostic = checker.getDiagnostics()[0];
    assertThat(diagnostic.getSourceName(), equalTo("CheckGlobalThis.java"));
    assertThat(diagnostic.getStartPosition().getLineNumber(), equalTo(4));
    assertThat(diagnostic.getEndPosition().getLineNumber(), equalTo(4));
    assertThat(diagnostic.getMessage(), equalTo("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafeUseOfThisInPrototypeMethod() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "function Foo() {}\n" +
        "Foo.prototype.bar = function() {\n" +
        "  this.baz = 2;\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertThat(checker.getDiagnostics(), empty());
  }

  @Test
  public void testUnsafeUseOfThisInConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "function Foo() {\n" +
        "  this.foo = 1;\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    Node diagnostic = checker.getDiagnostics()[0];
    assertThat(diagnostic.getSourceName(), equalTo("CheckGlobalThis.java"));
    assertThat(diagnostic.getStartPosition().getLineNumber(), equalTo(2));
    assertThat(diagnostic.getEndPosition().getLineNumber(), equalTo(2));
    assertThat(diagnostic.getMessage(), equalTo("dangerous use of the global 'this' object"));

    code = "function Foo() {\n" +
        "  this.constructor = Foo;\n" +
        "  this.foo = 1;\n" +
        "}";

    ast = Parser.parse(code, null, null);
    traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertThat(checker.getDiagnostics(), empty());
  }

  @Test
  public void testUnsafeUseOfThisWithJSDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "/** @constructor */\n" +
        "function Foo() {\n" +
        "  this.foo = 1;\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertThat(checker.getDiagnostics(), empty());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithoutAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "function evil() {\n" +
        "  this.foo = 1;\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertThat(checker.getDiagnostics(), empty());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "function evil() {\n" +
        "  var a = this;\n" +
        "  a.foo = 1;\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    Node diagnostic = checker.getDiagnostics()[0];
    assertThat(diagnostic.getSourceName(), equalTo("CheckGlobalThis.java"));
    assertThat(diagnostic.getStartPosition().getLineNumber(), equalTo(2));
    assertThat(diagnostic.getEndPosition().getLineNumber(), equalTo(2));
    assertThat(diagnostic.getMessage(), equalTo("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafeUseOfThisWithPrototypeProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "function Foo() {\n" +
        "  this.foo = 1;\n" +
        "}" +
        "Foo.prototype.bar = function() {\n" +
        "  this.baz = 2;\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    Node diagnostic = checker.getDiagnostics()[0];
    assertThat(diagnostic.getSourceName(), equalTo("CheckGlobalThis.java"));
    assertThat(diagnostic.getStartPosition().getLineNumber(), equalTo(7));
    assertThat(diagnostic.getEndPosition().getLineNumber(), equalTo(7));
    assertThat(diagnostic.getMessage(), equalTo("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafeUseOfThisWithPrototypeSubproperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(true);
    Checker checker = new Checker(compiler, options);

    String code = "function Foo() {\n" +
        "  this.foo = 1;\n" +
        "}" +
        "Foo.prototype.bar = {\n" +
        "  qux: function() {\n" +
        "    this.baz = 2;\n" +
        "  }\n" +
        "}";

    Node ast = Parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    Node diagnostic = checker.getDiagnostics()[0];
    assertThat(diagnostic.getSourceName(), equalTo("CheckGlobalThis.java"));
    assertThat(diagnostic.getStartPosition().getLineNumber(), equalTo(8));
    assertThat(diagnostic.getEndPosition().getLineNumber(), equalTo(8));
    assertThat(diagnostic.getMessage(), equalTo("dangerous use of the global 'this' object"));
  }
}