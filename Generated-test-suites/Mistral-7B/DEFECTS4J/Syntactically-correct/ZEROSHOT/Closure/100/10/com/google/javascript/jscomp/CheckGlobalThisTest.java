import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Parser parser;
  private Node rootNode;
  private Diagnostics diagnostics;

  @Before
  public void setUp() {
    compiler = new CompilerOptions().createCompiler();
    parser = new Parser(compiler);
  }

  @Test
  public void testSafeUseOfThis() {
    String code = "function Foo() { var that = this; }";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, null);
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(0));
  }

  @Test
  public void testUnsafeUseOfThis() {
    String code = "function Foo() { this.bar = 1; }";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(1));
    assertThat(diagnostics.getError(0).getCode(), is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testFunctionAnnotatedWithConstructor() {
    String code = "function Foo() { this.constructor = Foo; }";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(0));
  }

  @Test
  public void testFunctionAnnotatedWithThis() {
    String code = "/** @this {Object} */ function Foo() { }";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(0));
  }

  @Test
  public void testFunctionWithNoThisAnnotation() {
    String code = "function Foo() { return this.x; }";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(1));
    assertThat(diagnostics.getError(0).getCode(), is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testPrototypeMethod() {
    String code = "function Foo() { }; Foo.prototype.bar = function() { this.baz = 1; }";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(0));
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    String code = "function Foo() { }; Foo.prototype.bar = this;";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(1));
    assertThat(diagnostics.getError(0).getCode(), is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testAssignmentToSubProperty() {
    String code = "function Foo() { }; Foo.prototype.bar = { baz: this };";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(1));
    assertThat(diagnostics.getError(0).getCode(), is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testPropertyAccessOnPrototype() {
    String code = "function Foo() { }; Foo.prototype.bar.baz = this;";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(1));
    assertThat(diagnostics.getError(0).getCode(), is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testPropertyAccessOnSubProperty() {
    String code = "function Foo() { }; Foo.prototype.bar.quux.baz = this;";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(1));
    assertThat(diagnostics.getError(0).getCode(), is(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testImplicitConstructor() {
    String code = "function Foo() {} var x = new Foo();";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(0));
  }

  @Test
  public void testImplicitConstructorWithThisAnnotation() {
    String code = "/** @constructor */ function Foo() {} var x = new Foo();";
    rootNode = parser.parse(code, null, null);
    compiler.process(rootNode, new CheckLevel(DiagnosticType.warning(1)));
    diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.getNumErrors(), is(0));
  }
}

class TestCheckGlobalThis implements Callback {

  @Override
  public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    return true;
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    // Do nothing
  }
}