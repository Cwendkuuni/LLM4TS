import static org.junit.Assert.assertThat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    String code =
        "function foo() {" +
            "this.foo = 'bar';" +
            "}" +
            "function Foo() {" +
            "}" +
            "Foo.prototype.foo = function() {" +
            "}" +
            "function FooBar() {}" +
            "FooBar.prototype.foo = function() {}";

    Compiler compiler = new Compiler();
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler));
    traversal.traverse();
    List<DiagnosticPosition> errors = Lists.newArrayList(compiler.getErrors());

    assertThat(errors, isEmpty());
  }

  @Test
  public void testUnsafeUsesOfThis() {
    String code = "function evil() { this.useful = undefined; }";

    Compiler compiler = new Compiler();
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler));
    traversal.traverse();
    List<DiagnosticPosition> errors = Lists.newArrayList(compiler.getErrors());

    assertThat(errors, not(isEmpty()));
    DiagnosticPosition error = errors.get(0);
    assertThat(error.getCode(), is(GLOBAL_THIS.getCode()));
    assertThat(error.getMessage(), is(GLOBAL_THIS.getMessage()));
    assertThat(error.getSourceName(), is(codeSourceName(code)));
    assertThat(error.getLineNumber(), is(1));
    assertThat(error.getColumnNumber(), is(5));
  }

  @Test
  public void testUnsafeUsesOfThisInPrototypeMethod() {
    String code = "function Foo() {}" +
        "Foo.prototype.evil = function() {" +
            "this.useful = undefined;" +
        "}";

    Compiler compiler = new Compiler();
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler));
    traversal.traverse();
    List<DiagnosticPosition> errors = Lists.newArrayList(compiler.getErrors());

    assertThat(errors, not(isEmpty()));
    DiagnosticPosition error = errors.get(0);
    assertThat(error.getCode(), is(GLOBAL_THIS.getCode()));
    assertThat(error.getMessage(), is(GLOBAL_THIS.getMessage()));
    assertThat(error.getSourceName(), is(codeSourceName(code)));
    assertThat(error.getLineNumber(), is(2));
    assertThat(error.getColumnNumber(), is(12));
  }

  @Test
  public void testUnsafeUseOfThisInFunctionAnnotatedWithConstructor() {
    String code = "function Foo() {" +
            "this.foo = function() {};" +
        "}";

    Compiler compiler = new Compiler();
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler));
    traversal.traverse();
    List<DiagnosticPosition> errors = Lists.newArrayList(compiler.getErrors());

    assertThat(errors, isEmpty());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionAnnotatedWithThis() {
    String code = "function Foo@this() {}";

    Compiler compiler = new Compiler();
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler));
    traversal.traverse();
    List<DiagnosticPosition> errors = Lists.newArrayList(compiler.getErrors());

    assertThat(errors, isEmpty());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    String code = "function evil() {" +
            "this.foo = function() {}" +
        "}";

    Compiler compiler = new Compiler();
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler));
    traversal.traverse();
    List<DiagnosticPosition> errors = Lists.newArrayList(compiler.getErrors());

    assertThat(errors, not(isEmpty()));
    DiagnosticPosition error = errors.get(0);
    assertThat(error.getCode(), is(GLOBAL_THIS.getCode()));
    assertThat(error.getMessage(), is(GLOBAL_THIS.getMessage()));
    assertThat(error.getSourceName(), is(codeSourceName(code)));
    assertThat(error.getLineNumber(), is(1));
    assertThat(error.getColumnNumber(), is(5));
  }

  private String codeSourceName(String code) {
    return newArrayList(code.split("\r\n")).get(0);
  }

}