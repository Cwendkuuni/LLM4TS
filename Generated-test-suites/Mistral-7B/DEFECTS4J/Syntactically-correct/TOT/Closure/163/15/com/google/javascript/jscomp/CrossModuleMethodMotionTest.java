import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private AbstractCompiler abstractCompiler;
  private CrossModuleMethodMotion crossModuleMethodMotion;

  @Before
  public void setup() throws Exception {
    compiler = new Compiler();
    abstractCompiler = new AbstractCompiler(compiler);
    IdGenerator idGenerator = new IdGenerator();
    crossModuleMethodMotion = new CrossModuleMethodMotion(
        abstractCompiler, idGenerator, true);
  }

  @Test
  public void testMoveMethodToDeeperModule() throws IOException {
    String sourceCode1 =
        "module A {\n"
        + "  function foo() {\n"
        + "    print('A.foo');\n"
        + "  }\n"
        + "}\n";

    String sourceCode2 =
        "module B {\n"
        + "  use 'A';\n"
        + "  function bar() {\n"
        + "    A.foo();\n"
        + "  }\n"
        + "}\n";

    Node ast1 = compiler.parseJavaScript(sourceCode1, null);
    Node ast2 = compiler.parseJavaScript(sourceCode2, ast1);

    compiler.setOption(ALLOW_AS_DEF, true);
    compiler.setOption(ALLOW_GLOBAL_ACCESS_FROM_AS_IMPORTED_MODULES, true);

    CompilerPass previousPass = compiler.getPassManager().getNextPass();
    compiler.getPassManager().setNextPass(crossModuleMethodMotion);
    crossModuleMethodMotion.process(null, null);
    crossModuleMethodMotion.process(null, null);
    compiler.getPassManager().setNextPass(previousPass);

    Node expectedAst1 = newNode("module A {\n"
        + "  function foo() {\n"
        + "    JSCompiler_stubMethod(0);\n"
        + "  }\n"
        + "}\n");
    Node expectedAst2 = newNode("module B {\n"
        + "  use 'A';\n"
        + "  function bar() {\n"
        + "    JSCompiler_unstubMethod(0, function() {\n"
        + "      A.foo();\n"
        + "    });\n"
        + "  }\n"
        + "}\n");

    assertEquals(expectedAst1.toString(), ast1.toString());
    assertEquals(expectedAst2.toString(), ast2.toString());
  }

  @Test
  public void testMoveMethodToSameModule() throws IOException {
    String sourceCode =
        "module A {\n"
        + "  function foo() {\n"
        + "    print('A.foo');\n"
        + "  }\n"
        + "  function bar() {\n"
        + "    foo();\n"
        + "  }\n"
        + "}\n";

    Node ast = compiler.parseJavaScript(sourceCode, null);

    compiler.setOption(ALLOW_AS_DEF, true);
    compiler.setOption(ALLOW_GLOBAL_ACCESS_FROM_AS_IMPORTED_MODULES, true);

    CompilerPass previousPass = compiler.getPassManager().getNextPass();
    compiler.getPassManager().setNextPass(crossModuleMethodMotion);
    crossModuleMethodMotion.process(null, null);
    crossModuleMethodMotion.process(null, null);
    compiler.getPassManager().setNextPass(previousPass);

    Node expectedAst = newNode("module A {\n"
        + "  function foo() {\n"
        + "    JSCompiler_stubMethod(0);\n"
        + "  }\n"
        + "  function bar() {\n"
        + "    JSCompiler_unstubMethod(0, function() {\n"
        + "      foo();\n"
        + "    });\n"
        + "  }\n"
        + "}\n");

    assertEquals(expectedAst.toString(), ast.toString());
  }

  @Test
  public void testMoveMethodToDeeperModuleWithErrors() throws IOException {
    String sourceCode1 =
        "module A {\n"
        + "  function foo() {\n"
        + "    print('A.foo');\n"
        + "  }\n"
        + "}\n";

    String sourceCode2 =
        "module B {\n"
        + "  use 'A';\n"
        + "  function bar() {\n"
        + "    A.foo();\n"
        + "  }\n"
        + "}\n";

    Node ast1 = compiler.parseJavaScript(sourceCode1, null);
    Node ast2 = compiler.parseJavaScript(sourceCode2, ast1);

    compiler.setOption(ALLOW_AS_DEF, true);
    compiler.setOption(ALLOW_GLOBAL_ACCESS_FROM_AS_IMPORTED_MODULES, true);
    compiler.setOption(WARNINGS, false);

    CompilerPass previousPass = compiler.getPassManager().getNextPass();
    compiler.getPassManager().setNextPass(crossModuleMethodMotion);
    try {
      crossModuleMethodMotion.process(null, null);
      crossModuleMethodMotion.process(null, null);
    } catch (AssertionError e) {
      // Expected error
    } finally {
      compiler.getPassManager().setNextPass(previousPass);
    }

    Node expectedAst1 = newNode("module A {\n"
        + "  function foo() {\n"
        + "    JSCompiler_stubMethod(0);\n"
        + "  }\n"
        + "}\n");
    Node expectedAst2 = newNode("module B {\n"
        + "  use 'A';\n"
        + "  function bar() {\n"
        + "    JSCompiler_unstubMethod(0, function() {\n"
        + "      A.foo();\n"
        + "    });\n"
        + "  }\n"
        + "}\n");

    assertEquals(expectedAst1.toString(), ast1.toString());
    assertEquals(expectedAst2.toString(), ast2.toString());

    // Check for expected error message
    String expectedErrorMessage =
        "JSC_INTERNAL_ERROR_MODULE_DEPEND: null deepest common module";
    assertTrue( Arrays.stream(compiler.getErrors()).anyMatch(e -> e.getMessage().contains(expectedErrorMessage) ) );
  }

  private Node newNode(String code) {
    try {
      return compiler.parseJavaScript(code, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}