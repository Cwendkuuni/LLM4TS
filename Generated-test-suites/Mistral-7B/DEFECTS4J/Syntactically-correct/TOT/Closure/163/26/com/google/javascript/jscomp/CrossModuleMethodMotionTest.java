import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private CompilerResults results;
  private CrossModuleMethodMotion pass;
  private IdGenerator idGenerator;
  private JSModuleGraph moduleGraph;

  @Before
  public void setUp() {
    compiler = new Compiler();
    results = new CompilerResults();
    idGenerator = new CrossModuleMethodMotion.IdGenerator();
    moduleGraph = new JSModuleGraph();
    compiler.setOptions(new CompilerOptions()
        .useModuleGraph(moduleGraph)
        .allowExperimentalOptions());
    pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
  }

  @Test
  public void testMovePrototypeMethodToAnotherModule() {
    String sourceCode =
        "module A {\n" +
        "  function foo() {\n" +
        "    return 'A';\n" +
        "  }\n" +
        "}\n" +
        "module B {\n" +
        "  function A() {}\n" +
        "  A.prototype.bar = function() {\n" +
        "    return 'B';\n" +
        "  };\n" +
        "}\n";

    Node externs = compiler.parseTree(sourceCode, null, null);
    Node root = compiler.parseTree(sourceCode, null, externs);
    pass.process(externs, root);
    compiler.compile(externs, root, results);

    Node a = results.getGlobalScope().lookupVariable("A");
    assertNotNull(a);
    Node bar = a.getChildAtIndex(0);
    assertNotNull(bar);
    assertEquals("function ()\n" +
        "{\n" +
        "  JSCompiler_stubMethod(id);\n" +
        "}", bar.toSource());

    Node b = results.getModuleScope("B");
    Node unstubMethod = b.getFirstChild();
    assertEquals("function JSCompiler_unstubMethod(id, body) {\n" +
        "  JSCompiler_unstubMap[id] = body;\n" +
        "}", unstubMethod);
    Node barFunction = b.getChildAtIndex(1);
    assertNotNull(barFunction);
    assertEquals("function bar() {\n" +
        "  return 'B';\n" +
        "}", barFunction.toSource());
  }

  @Test(expected = AssertionError.class)
  public void testCannotMoveUnreferencedPrototypeProperty() {
    String sourceCode =
        "module A {\n" +
        "  var a = {};\n" +
        "}\n" +
        "module B {\n" +
        "  function A() {}\n" +
        "  A.prototype.bar = function() {\n" +
        "    return 'B';\n" +
        "  };\n" +
        "}\n";

    Node externs = compiler.parseTree(sourceCode, null, null);
    Node root = compiler.parseTree(sourceCode, null, externs);
    pass.process(externs, root);
    compiler.compile(externs, root, results);

    Node a = results.getGlobalScope().lookupVariable("A");
    assertNotNull(a);
    Node bar = a.getChildAtIndex(0);
    assertNotNull(bar);
    assertEquals("function ()\n" +
        "{\n" +
        "  return this.bar; // unreferenced property\n" +
        "}", bar.toSource());
  }

  @Test
  public void testCannotMovePrototypePropertyThatReadsClosureVariables() {
    String sourceCode =
        "function foo() {\n" +
        "  var x = 1;\n" +
        "  function bar() {\n" +
        "    return x;\n" +
        "  }\n" +
        "}\n" +
        "module A {\n" +
        "  function foo() {\n" +
        "  }\n" +
        "  A.prototype.bar = foo.bar;\n" +
        "}\n";

    Node externs = compiler.parseTree(sourceCode, null, null);
    Node root = compiler.parseTree(sourceCode, null, externs);
    pass.process(externs, root);
    compiler.compile(externs, root, results);

    Node a = results.getGlobalScope().lookupVariable("A");
    assertNotNull(a);
    Node bar = a.getChildAtIndex(0);
    assertNotNull(bar);
    assertEquals("function foo() {}\n" +
        "A.prototype.bar = foo.bar; // reads closure variable 'x'\n",
        bar.toSource());
  }

  @Test
  public void testMovePrototypeMethodToTheSameModule() {
    String sourceCode =
        "module A {\n" +
        "  function foo() {\n" +
        "    return 'A';\n" +
        "  }\n" +
        "}\n" +
        "module A {\n" +
        "  function foo() {\n" +
        "    return 'A';\n" +
        "  }\n" +
        "  function bar() {\n" +
        "    return 'A';\n" +
        "  };\n" +
        "  A.prototype.bar = function() {\n" +
        "    return 'B';\n" +
        "  };\n" +
        "}";

    Node externs = compiler.parseTree(sourceCode, null, null);
    Node root = compiler.parseTree(sourceCode, null, externs);
    pass.process(externs, root);
    compiler.compile(externs, root, results);

    Node a = results.getGlobalScope().lookupVariable("A");
    assertNotNull(a);
    Node bar = a.getChildAtIndex(0);
    assertNotNull(bar);
    assertEquals("function bar() {\n" +
        "  return 'B';\n" +
        "}", bar.toSource());
  }
}