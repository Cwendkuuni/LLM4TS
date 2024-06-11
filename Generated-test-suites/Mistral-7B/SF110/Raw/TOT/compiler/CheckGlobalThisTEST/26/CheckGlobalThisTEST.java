import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.Checker.$;
import static com.google.javascript.jscomp.NodeTraversal.abstractTraverse;
import static com.google.javascript.rhino.NodeType.ASSIGN;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.NAME;
import static com.google.javascript.rhino.NodeType.THIS;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    String jsCode = ""
        + "function Foo() {}"
        + "Foo.prototype.bar = function() {}"
        + "function Foo2() {}"
        + "function Foo3() {}"
        + "Foo3.prototype.bar = function() {}"
        + "var foo = function() {}";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    result.getDiagnostics().isEmpty();
  }

  @Test
  public void testUnsafeUseOfThisOnLeftSideOfAssignment() {
    String jsCode = "var a = this;";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    assertThat(result.getDiagnostics().size(), equalTo(1));
    Diagnostic diagnostic = result.getDiagnostics().get(0);
    assertThat(diagnostic.getSourceName(), equalTo("<string 'eval'>"));
    assertThat(diagnostic.getMessage(), equalTo("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafeUseOfThisOnLeftSideOfPropertyAccess() {
    String jsCode = "var a = { x: this };";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    assertThat(result.getDiagnostics().size(), equalTo(1));
    Diagnostic diagnostic = result.getDiagnostics().get(0);
    assertThat(diagnostic.getSourceName(), equalTo("<string 'eval'>"));
    assertThat(diagnostic.getMessage(), equalTo("dangerous use of the global 'this' object"));
  }

  @Test
  public void testUnsafeUseOfThisInFunctionAnnotatedWithAtConstructor() {
    String jsCode = ""
        + "function Foo() {"
        + " this.bar = function() {};"
        + "}"
        + "new Foo();";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    result.getDiagnostics().isEmpty();
  }

  @Test
  public void testUnsafeUseOfThisInFunctionAnnotatedWithAtThis() {
    String jsCode = ""
        + "function @this Foo() {"
        + " this.bar = function() {};"
        + "}"
        + "new Foo();";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    result.getDiagnostics().isEmpty();
  }

  @Test
  public void testUnsafeUseOfThisWhereThereIsNoLogicalPlaceToPutAThisAnnotation() {
    String jsCode = ""
        + "var a = function() {"
        + " this.bar = function() {};"
        + "}"
        + "a();";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    assertThat(result.getDiagnostics().size(), equalTo(1));
    Diagnostic diagnostic = result.getDiagnostics().get(0);
    assertThat(diagnostic.getSourceName(), equalTo("<string 'eval'>"));
    assertThat(diagnostic.getMessage(), equalTo("dangerous use of the global 'this' object"));
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    String jsCode = ""
        + "var obj = {}"
        + "obj.prototype.x = function() { this.y = 1; };";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    result.getDiagnostics().isEmpty();
  }

  @Test
  public void testAssignmentToPrototypeSubproperty() {
    String jsCode = ""
        + "var obj = {}"
        + "obj.prototype.x = {"
        + " y: function() { this.z = 1; }"
        + "};";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    result.getDiagnostics().isEmpty();
  }

  @Test
  public void testFunctionLentToPrototype() {
    String jsCode = ""
        + "var obj = {}"
        + "obj.prototype.foo = function() { this.bar = 1; };";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    result.getDiagnostics().isEmpty();
  }

  @Test
  public void testAssignmentToThisOnLeftSideOfNestedAssignment() {
    String jsCode = ""
        + "var a = {"
        + " b: {"
        + " c: function() { this = 1; }"
        + " }"
        + " }"
        + "a.b.c();";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    result.getDiagnostics().isEmpty();
  }

  @Test
  public void testPropertyAccessThroughPrototypeChain() {
    String jsCode = ""
        + "var obj = {"
        + " prototype: {"
        + " x: function() { this.y = 1; }"
        + " }"
        + " }"
        + "obj.prototype.x();";

    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    Checker result = $(checker);
    abstractTraverse(result, $(compiler.parse(jsCode)), null);

    result.getDiagnostics().isEmpty();
  }
}