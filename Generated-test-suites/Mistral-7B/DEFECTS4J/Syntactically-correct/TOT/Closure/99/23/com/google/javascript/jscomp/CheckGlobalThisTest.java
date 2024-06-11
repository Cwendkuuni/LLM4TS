import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Checker compiler = new Checker(new CompilerOptions());
    AbstractCompiler abstractCompiler = compiler.getOptions().getAbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.traverse(checker);

    String source = "function evil() { this.useful = undefined; }";
    TestSourceFile ts = new TestSourceFile(source, "Test.js");
    Node rootNode = compiler.process(ts, null);

    int numDiagnostics = compiler.getDiagnostics().size();
    assertEquals(1, numDiagnostics);
    assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getDiagnosticType());
    assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testSafeUsage() {
    Checker compiler = new Checker(new CompilerOptions());
    AbstractCompiler abstractCompiler = compiler.getOptions().getAbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.traverse(checker);

    String source = "function Safe() { this.useful = 42; }";
    TestSourceFile ts = new TestSourceFile(source, "Safe.js");
    Node rootNode = compiler.process(ts, null);

    int numDiagnostics = compiler.getDiagnostics().size();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testPrototypeProperty() {
    Checker compiler = new Checker(new CompilerOptions());
    AbstractCompiler abstractCompiler = compiler.getOptions().getAbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.traverse(checker);

    String source = "var eviler = { evil: function() { this.useful = 42; } };";
    TestSourceFile ts = new TestSourceFile(source, "Eviler.js");
    Node rootNode = compiler.process(ts, null);

    int numDiagnostics = compiler.getDiagnostics().size();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testFunctionAnnotatedWithConstructor() {
    Checker compiler = new Checker(new CompilerOptions());
    AbstractCompiler abstractCompiler = compiler.getOptions().getAbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.traverse(checker);

    String source = "function EvilConstructor() { this.useful = 42; }";
    TestSourceFile ts = new TestSourceFile(source, "EvilConstructor.js", null, "constructor");
    Node rootNode = compiler.process(ts, null);

    int numDiagnostics = compiler.getDiagnostics().size();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testFunctionAnnotatedWithThis() {
    Checker compiler = new Checker(new CompilerOptions());
    AbstractCompiler abstractCompiler = compiler.getOptions().getAbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.traverse(checker);

    String source = "function EvilThis() { '@this' usef
    ul = 42; }";
    TestSourceFile ts = new TestSourceFile(source, "EvilThis.js", null, "@this");
    Node rootNode = compiler.process(ts, null);

    int numDiagnostics = compiler.getDiagnostics().size();
    assertEquals(0, numDiagnostics);
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    Checker compiler = new Checker(new CompilerOptions());
    AbstractCompiler abstractCompiler = compiler.getOptions().getAbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler.getRootNode());
    traversal.traverse(checker);

    String source = "function EvilNoAnnotation() { this.useful = 42; }";
    TestSourceFile ts = new TestSourceFile(source, "EvilNoAnnotation.js");
    Node rootNode = compiler.process(ts, null);

    int numDiagnostics = compiler.getDiagnostics().size();
    assertEquals(1, numDiagnostics);
    assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getDiagnosticType());
    assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
  }
}