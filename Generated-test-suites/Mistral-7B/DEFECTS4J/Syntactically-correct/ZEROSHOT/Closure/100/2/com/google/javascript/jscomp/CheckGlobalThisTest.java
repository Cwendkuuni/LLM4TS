import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.bubble.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private Compiler compiler;
  private Checker checker;
  private Parser parser;
  private NodeFactory factory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checker = new Checker(compiler, new AbstractCompiler() {
      @Override
      publicvoidreport(NodeTraversal t, int level, DiagnosticType type, int start, int end, String message,
          Object data) {
        System.out.println(message);
      }
    }, new CheckLevel(DiagnosticType.warning(null), CheckLevel.Warning));
    parser = new Parser(compiler);
    factory = compiler.getFactory();
  }

  @Test
  public void testCheckGlobalThis() {
    String code = "function evil() { this.useful = undefined; }";
    Node ast = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    assertThat(compiler.getDiagnostics(), instanceOf(java.util.ArrayList.class));
    java.util.ArrayList<com.google.javascript.jscomp.Diagnostic> diagnostics =
        (java.util.ArrayList<com.google.javascript.jscomp.Diagnostic>) compiler.getDiagnostics();
    assertThat(diagnostics.size(), is(1));
    Diagnostic expectedDiagnostic = new Diagnostic(GLOBAL_THIS, null, null, null, null, null, null, null);
    assertThat(diagnostics.get(0), is(expectedDiagnostic));
  }

  @Test
  public void testCheckGlobalThisConstructorFunction() {
    String code = "function Constructor() { this.name = 'John Doe'; }";
    Node ast = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    // No warning should be generated
    assertThat(compiler.getDiagnostics(), is(java.util.Collections.emptyList()));
  }

  @Test
  public void testCheckGlobalThisPrototypeFunction() {
    String code = "function Prototype() { } Prototype.prototype.sayHello = function() { this.greeting = 'Hello'; }";
    Node ast = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    // No warning should be generated
    assertThat(compiler.getDiagnostics(), is(java.util.Collections.emptyList()));
  }

  @Test
  public void testCheckGlobalThisFunctionWithThisAnnotation() {
    String code = "function @this MyFunction() {}";
    Node ast = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    // No warning should be generated
    assertThat(compiler.getDiagnostics(), is(java.util.Collections.emptyList()));
  }

  @Test
  public void testCheckGlobalThisFunctionWithNoAnnotation() {
    String code = "function MyFunction() {}";
    Node ast = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    // No warning should be generated
    assertThat(compiler.getDiagnostics(), is(java.util.Collections.emptyList()));
  }

  @Test
  public void testCheckGlobalThisAssignment() {
    String code = "var a = this;";
    Node ast = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    // No warning should be generated
    assertThat(compiler.getDiagnostics(), is(java.util.Collections.emptyList()));
  }

  @Test public void testCheckGlobalThisPropertyAccess() {
    String code = "function MyFunction() { this.property = 5; }";
    Node ast = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    // No warning should be generated
    assertThat(compiler.getDiagnostics(), is(java.util.Collections.emptyList()));
  }

  @Test public void testCheckGlobalThisPropertyAccessThroughPrototype() {
    String code = "function MyPrototype() {} MyPrototype.prototype.sayHello = function() { this.greeting = 'Hello'; }";
    Node ast = parser.parse(code, null, null);

    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();

    // No warning should be generated
    assertThat(compiler.getDiagnostics(), is(java.util.Collections.emptyList()));
  }
}