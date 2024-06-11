import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.TestSourceFile;
import static org.junit.Assert.assertEquals;

public class CrossModuleMethodMotionTest {

  private CompilerPass pass;
  private AbstractCompiler compiler;
  private TestSourceFile sourceFile;
  private CompilerOptions options;
  private JSModuleGraph moduleGraph;
  private IdGenerator idGenerator;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    moduleGraph = new JSModuleGraph();
    idGenerator = new CrossModuleMethodMotion.IdGenerator();
    pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
  }

  @Test
  public void testMoveMethodToDeeperModule() {
    String sourceCode =
        "module A {" +
        "  export function foo() {" +
        "    return 'A.foo';" +
        "  }" +
        "}" +
        "module B {" +
        "  import foo from './A.js';" +
        "  function bar() {" +
        "    return foo();" +
        "  }" +
        "  export default bar;" +
        "}";

    sourceFile = new TestSourceFile("Test.java", sourceCode);
    compiler.compile(sourceFile, null, moduleGraph);

    Node root = sourceFile.getRootNode();
    pass.process(root, root);
    compiler.compile(sourceFile, null, moduleGraph);

    Node bar = moduleGraph.getModule("B").getExport();
    assertEquals("A.foo", bar.toString());
  }

  @Test
  public void testDontMoveUnreferencedMethod() {
    String sourceCode =
        "module A {" +
        "  export function foo() {" +
        "    return 'A.foo';" +
        "  }" +
        "}" +
        "module B {" +
        "  import foo from './A.js';" +
        "  function bar() {" +
        "    return foo();" +
        "  }" +
        "  // Unreferenced method" +
        "  function baz() {" +
        "    return 'B.baz';" +
        "  }" +
        "  export default bar;" +
        "}";

    sourceFile = new TestSourceFile("Test.java", sourceCode);
    compiler.compile(sourceFile, null, moduleGraph);
    pass.process(sourceFile.getRootNode(), sourceFile.getRootNode());
    compiler.compile(sourceFile, null, moduleGraph);

    Node bar = moduleGraph.getModule("B").getExport();
    assertEquals("A.foo", bar.toString());

    Node baz = moduleGraph.getModule("B").getDeclaration("baz");
    assertEquals("B.baz", baz.toString());
  }

  @Test
  public void testMoveMethodToSameModule() {
    String sourceCode =
        "module A {" +
        "  export function foo() {" +
        "    return 'A.foo';" +
        "  }" +
        "  export function bar() {" +
        "    return foo();" +
        "  }" +
        "}";

    sourceFile = new TestSourceFile("Test.java", sourceCode);
    compiler.compile(sourceFile, null, moduleGraph);

    pass.process(sourceFile.getRootNode(), sourceFile.getRootNode());
    compiler.compile(sourceFile, null, moduleGraph);

    Node bar = moduleGraph.getModule("A").getExport();
    assertEquals("A.foo", bar.toString());
  }

  @Test(expected = AssertionError.class)
  public void testMoveMethodWithNullDeepestCommonModule() {
    String sourceCode =
        "module A {" +
        "  export function foo() {" +
        "    return 'A.foo';" +
        "  }" +
        "}" +
        "module B {" +
        "  import foo from './A.js';" +
        "  function bar() {" +
        "    return foo();" +
        "  }" +
        "  export default bar;" +
        "}";

    sourceFile = new TestSourceFile("Test.java", sourceCode);
    compiler.compile(sourceFile, null, null); // Intentionally null module graph

    pass.process(sourceFile.getRootNode(), sourceFile.getRootNode());
    compiler.compile(sourceFile, null, null); // Intentionally null module graph
  }
}