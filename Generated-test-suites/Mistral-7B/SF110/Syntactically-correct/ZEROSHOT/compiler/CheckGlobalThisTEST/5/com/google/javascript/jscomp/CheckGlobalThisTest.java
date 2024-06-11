import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.AbstractTraversal.TRAVERSE;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(PowerMockRunner.class)
@SuppressWarnings("unchecked")
public class CheckGlobalThisTest {

  private static final String SOURCE = "function evil() { this.useful = undefined; }";

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private Diagnostics diagnostics;
  private SourceFile sourceFile;

  @Before
  public void setUp() throws Exception {
    CheckerFactory checkerFactory = new CheckerFactory();
    AbstractCompiler.GlobalRegistrar registrar = checkerFactory.getGlobalRegistrar();
    registrar.registerChecker(CheckGlobalThis.class.getName(), new CheckGlobalThis(null));

    AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
    classLoader.definePackage("com.google.javascript.jscomp");
    classLoader.defineClass(CheckGlobalThis.class, CheckGlobalThis.class.getBytecode());
    classLoader.defineClass(AbstractCompiler.class, AbstractCompiler.class.getBytecode());
    classLoader.defineClass(NodeTraversal.Callback.class, NodeTraversal.Callback.class.getBytecode());

    compiler = (AbstractCompiler) checkerFactory.createCompiler(null, new CompilerOptions());
    options = compiler.getOptions();
    diagnostics = new Diagnostics();
    sourceFile = new SourceFile("CheckGlobalThisTest.js", SOURCE);
  }

  @Test
  public void testGlobalThis() throws Exception {
    Parser parser = compiler.getParser();
    Node ast = parser.parse(sourceFile, null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertTrue(diagnostics.getNumDiagnostics() > 0);
    Diagnostics actualDiagnostics = diagnostics;
    Diagnostics expectedDiagnostics = new Diagnostics();
    expectedDiagnostics.addDiagnostic(new Diagnostic(
        CheckGlobalThis.GLOBAL_THIS,
        new SourcePosition(0, 0),
        new SourcePosition(0, 9),
        "dangerous use of the global 'this' object"
    ));
    assertTrue(actualDiagnostics.equals(expectedDiagnostics));
  }

  @Test
  public void testSafeThis() throws Exception {
    String source = "function Foo() {}";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("Foo.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testConstructor() throws Exception {
    String source = "function Foo() { this.useful = undefined; }";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("Foo.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testPrototypeMethod() throws Exception {
    String source = "function Foo() { this.prototype.useful = undefined; }";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("Foo.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testThisAnnotation() throws Exception {
    String source = "function Foo@this() { this.useful = undefined; }";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("Foo.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testFunctionWithNoPlaceForThisAnnotation() throws Exception {
    String source = "function evil() { this.useful = undefined; }";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("CheckGlobalThisTest.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testFunctionAnnotatedWithInterface() throws Exception {
    String source = "interface Foo {} function Foo() {} Foo.prototype.useful = undefined;";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("Foo.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testNestedAssignment() throws Exception {
    String source = "(a = this).useful = undefined;";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("CheckGlobalThisTest.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertTrue(diagnostics.getNumDiagnostics() > 0);
    Diagnostics actualDiagnostics = diagnostics;
    Diagnostics expectedDiagnostics = new Diagnostics();
    expectedDiagnostics.addDiagnostic(new Diagnostic(
        CheckGlobalThis.GLOBAL_THIS,
        new SourcePosition(0, 19),
        new SourcePosition(0, 34),
        "dangerous use of the global 'this' object"
    ));
    assertTrue(actualDiagnostics.equals(expectedDiagnostics));
  }

  @Test
  public void testGetProperty() throws Exception {
    String source = "this.prototype.useful = undefined;";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("CheckGlobalThisTest.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testGetPrototypeProperty() throws Exception {
    String source = "this.prototype.prototype.useful = undefined;";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("CheckGlobalThisTest.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertTrue(diagnostics.getNumDiagnostics() > 0);
    Diagnostics actualDiagnostics = diagnostics;
    Diagnostics expectedDiagnostics = new Diagnostics();
    expectedDiagnostics.addDiagnostic(new Diagnostic(
        CheckGlobalThis.GLOBAL_THIS,
        new SourcePosition(0, 41),
        new SourcePosition(0, 56),
        "dangerous use of the global 'this' object"
    ));
    assertTrue(actualDiagnostics.equals(expectedDiagnostics));
  }

  @Test
  public void testFunctionWithMultipleDefinitions() throws Exception {
    String source = "function evil() { this.useful = undefined; } function evil() {}";
    Parser parser = compiler.getParser();
    Node ast = parser.parse(new SourceFile("CheckGlobalThisTest.js", source), null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, ast, options, diagnostics, null, TRAVERSE);
    traversal.traverse(new CheckGlobalThis(compiler));

    assertTrue(diagnostics.getNumDiagnostics() > 0);
    Diagnostics actualDiagnostics = diagnostics;
    Diagnostics expectedDiagnostics = new Diagnostics();
    expectedDiagnostics.addDiagnostic(new Diagnostic(
        CheckGlobalThis.GLOBAL_THIS,
        new SourcePosition(1, 19),
        new SourcePosition(1, 34),
        "dangerous use of the global 'this' object"
    ));
    assertTrue(actualDiagnostics.equals(expectedDiagnostics));
  }
}