import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.parser.bare.BareParser;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil() { this.useful = undefined; }";

    NodeFactory nodeFactory = new NodeFactory();
    Parser parser = new BareParser(nodeFactory);
    Node ast = parser.parse(jsCode, null, null);

    Compiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(compiler, WARNING));

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    traversal.traverse(ast);

    assertThat(compiler.getDiagnostics(), instanceOf(java.util.ArrayList.class));
    java.util.List<DiagnosticPosition> diagnostics = compiler.getDiagnostics();
    assertThat(diagnostics.size(), is(1));
    DiagnosticPosition diagnostic = diagnostics.get(0);
    assertThat(diagnostic.getPosition(), is(1));
    assertThat(diagnostic.getStartColumn(), is(8));
    assertThat(diagnostic.getEndColumn(), is(20));
    assertThat(diagnostic.getMessage(), is("JSC_USED_GLOBAL_THIS: dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThisNoDiagnostics() {
    String jsCode = "function safe() { return this; }";

    NodeFactory nodeFactory = new NodeFactory();
    Parser parser = new BareParser(nodeFactory);
    Node ast = parser.parse(jsCode, null, null);

    Compiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(compiler, WARNING));

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    traversal.traverse(ast);

    assertThat(compiler.getDiagnostics(), is(empty()));
  }

  @Test
  public void testCheckGlobalThisConstructor() {
    String jsCode = "function MyConstructor() {}";

    NodeFactory nodeFactory = new NodeFactory();
    Parser parser = new BareParser(nodeFactory);
    Node ast = parser.parse(jsCode, null, null);

    Compiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(compiler, WARNING));

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    traversal.traverse(ast);

    assertThat(compiler.getDiagnostics(), is(empty()));
  }

  @Test
  public void testCheckGlobalThisFnWithThisAnnotation() {
    String jsCode = "function MyFn() { @this }";

    NodeFactory nodeFactory = new NodeFactory();
    Parser parser = new BareParser(nodeFactory);
    Node ast = parser.parse(jsCode, null, null);

    Compiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(compiler, WARNING));

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    traversal.traverse(ast);

    assertThat(compiler.getDiagnostics(), is(empty()));
  }

  @Test
  public void testCheckGlobalThisFnWithNoThisAnnotation() {
    String jsCode = "function MyFn() { }";

    NodeFactory nodeFactory = new NodeFactory();
    Parser parser = new BareParser(nodeFactory);
    Node ast = parser.parse(jsCode, null, null);

    Compiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(compiler, WARNING));

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    traversal.traverse(ast);

    assertThat(compiler.getDiagnostics(), is(empty()));
  }

  @Test
  public void testCheckGlobalThisFunctionWithPrototypeProperty() {
    String jsCode = "function MyFn() { this.prototype.x = 1; }";

    NodeFactory nodeFactory = new NodeFactory();
    Parser parser = new BareParser(nodeFactory);
    Node ast = parser.parse(jsCode, null, null);

    Compiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(compiler, WARNING));

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    traversal.traverse(ast);

    assertThat(compiler.getDiagnostics(), is(empty()));
  }

  @Test
  public void testCheckGlobalThisFunctionWithPrototypeSubProperty() {
    String jsCode = "function MyFn() { this.prototype.x.y = 1; }";

    NodeFactory nodeFactory = new NodeFactory();
    Parser parser = new BareParser(nodeFactory);
    Node ast = parser.parse(jsCode, null, null);

    Compiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(compiler, WARNING));

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    traversal.traverse(ast);

    assertThat(compiler.getDiagnostics(), is(empty()));
  }

  @Test
  public void testCheckGlobalThisFunctionWithPrototypeGetProp() {
    String jsCode = "function MyFn() { this.prototype.getName = function() { return 'name'; } }";

    NodeFactory nodeFactory = new NodeFactory();
    Parser parser = new BareParser(nodeFactory);
    Node ast = parser.parse(jsCode, null, null);

    Compiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(compiler, WARNING));

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    traversal.traverse(ast);

    assertThat(compiler.getDiagnostics(), is(empty()));
  }
}