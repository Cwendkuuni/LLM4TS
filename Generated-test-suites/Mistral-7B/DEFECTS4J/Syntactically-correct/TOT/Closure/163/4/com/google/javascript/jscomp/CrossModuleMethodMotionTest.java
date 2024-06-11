import static com.google.javascript.jscomp.CompilerPass.*;
import static com.google.javascript.jscomp.JSError.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.AnalyzerFactory;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.LogWriter;
import com.google.javascript.rhino.NodeOperations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private static final String TEST_SOURCE =
      "// Test file for CrossModuleMethodMotion\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    return 'foo';\n" +
      "  }\n" +
      "  function baz() {\n" +
      "    return 'bar';\n" +
      "  }\n" +
      "}\n" +
      "class Bar extends Foo {\n" +
      "}\n";

  private static final String MOVED_SOURCE =
      "// Test file for CrossModuleMethodMotion\n" +
      "class Foo {\n" +
      "  function bar() {\n" +
      "    return 'foo';\n" +
      "  }\n" +
      "}\n" +
      "class Bar extends Foo {\n" +
      "  function JSCompiler_stubMethod(id) {\n" +
      "    return function() {\n" +
      "      return Foo.prototype.bar.apply(this, arguments);\n" +
      "    };\n" +
      "  }\n" +
      "  function baz() {\n" +
      "    return 'bar';\n" +
      "  }\n" +
      "  function JSCompiler_unstubMethod(id, body) {\n" +
      "    Foo.prototype.bar = body;\n" +
      "  }\n" +
      "}\n";

  private Compiler compiler;
  private IdGenerator idGenerator;
  private LogWriter logWriter;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setPrintErrorMessages(true);
    logWriter = new LogWriter(System.out);
    compiler = new Compiler(options, logWriter);
    idGenerator = compiler.getIdGenerator();
  }

  @After
  public void tearDown() {
    compiler = null;
    idGenerator = null;
  }

  @Test
  public void testCrossModuleMethodMotion() {
    sourceFile = new SourceFile("Test.js", TEST_SOURCE);
    Node root = compiler.parseSourceFile(sourceFile, null, null);

    CrossModuleMethodMotion pass =
        new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, root);

    // Check that the method was moved
    Node movedMethod = NodeOperations.getFirstChild(
        NodeOperations.getFirstChild(NodeOperations.getFirstChild(
            root, IR.IDENT("Bar"))));
    assertEquals("function JSCompiler_stubMethod(id)",
        movedMethod.getClass().getName());

    // Check that the method is still callable
    Node callSite = NodeOperations.getFirstChild(
        NodeOperations.getFirstChild(root, IR.IDENT("new Bar")));
    Node call = NodeOperations.getFirstChild(callSite);
    assertEquals("bar", call.getChild(0).getClass().getName());

    // Check that the method returns the expected result
    assertEquals("foo", call.getChild(1).getChild(0).getString());

    // Check that the original method is replaced with a stub
    Node originalMethod = NodeOperations.getFirstChild(
        NodeOperations.getFirstChild(NodeOperations.getFirstChild(
            root, IR.IDENT("Foo"))));
    assertTrue(originalMethod.isFunction());
    assertFalse(originalMethod.getBooleanProp(Node.IS_DEF));

    // Check that the stub function is defined in the Bar class
    Node stubDefinition = NodeOperations.getFirstChild(
        NodeOperations.getFirstChild(NodeOperations.getFirstChild(
            NodeOperations.getFirstChild(root, IR.IDENT("Bar"))),
            IR.VARDEC));
    assertEquals("JSCompiler_stubMethod",
        stubDefinition.getChild(0).getChild(1).getString());

    // Check that the unstubbed method is defined in the Foo class
    Node unstubbedMethod = NodeOperations.getFirstChild(
        NodeOperations.getFirstChild(NodeOperations.getFirstChild(
            root, IR.IDENT("Foo"))));
    assertEquals("function bar() {...}", unstubbedMethod.getString());

    // Check that there are no errors reported
    assertTrue(compiler.getErrors().isEmpty());

    // Check that the unstubbed method is callable from the Bar class
    Node unstubbedCallSite = NodeOperations.getFirstChild(
        NodeOperations.getFirstChild(NodeOperations.getFirstChild(
            NodeOperations.getFirstChild(NodeOperations.getFirstChild(
                root, IR.IDENT("new Bar"))), IR.DOT),
            IR.IDENT("bar")));
    assertEquals("bar", unstubbedCallSite.getClass().getName());

    // Check that the unstubbed method returns the expected result
    assertEquals("foo", unstubbedCallSite.getChild(1).getChild(0).getString());
  }

  @Test
  public void testNullDeepestCommonModuleError() {
    String source = "class Foo {} class Bar extends Foo {}";
    SourceFile testSource = new SourceFile("Test.js", source);
    Node root = compiler.parseSourceFile(testSource, null, null);
    CrossModuleMethodMotion pass =
        new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, root);

    CompilerOptions options = new CompilerOptions();
    options.setPrintErrorMessages(true);
    Compiler compiler2 = new Compiler(options, logWriter);
    compiler2.parseSourceFile(testSource, null, root);

    Iterator<JSError> errors = compiler2.getErrors().iterator();
    assertTrue(errors.hasNext());
    JSError error = errors.next();
    assertEquals(JSError.Type.ERROR, error.getType());
    assertEquals("JSC_INTERNAL_ERROR_MODULE_DEPEND", error.getCode());
  }
}