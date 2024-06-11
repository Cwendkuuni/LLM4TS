import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JsModuleGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.Nodeops;

/**
 * Tests for CrossModuleMethodMotion class.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.internal.parsers.*",
    "com.sun.org.apache.xerces.internal.impl.xs.trax.*",
    "javax.xml.*", "javax.xml.stream.*"})
@PrepareForTest({Compiler.class, CompilerPassManager.class})
public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private CompilerPassManager passManager;
  private ErrorReporter errorReporter;
  private IdGenerator idGenerator;
  private JsModuleGraph moduleGraph;

  @Before
  public void setUp() throws Exception {
    errorReporter = Mockito.mock(ErrorReporter.class);
    compiler = PowerMockito.spy(new Compiler(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    passManager = Mockito.spy(CompilerPassManager.getStandardPassManager(null));
    idGenerator = Mockito.mock(IdGenerator.class);
    moduleGraph = Mockito.mock(JsModuleGraph.class);
    PowerMockito.when(Compiler.create(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(compiler);
    PowerMockito.when(CompilerPassManager.getStandardPassManager(any()))
        .thenReturn(passManager);
    PowerMockito.when(moduleGraph.getModuleCount()).thenReturn(2);
    PowerMockito.when(compiler.getModuleGraph()).thenReturn(moduleGraph);
  }

  @Test
  public void testCrossModuleMethodMotion() throws Exception {
    // Initialize dependencies
    CompilerOptions options = new CompilerOptions(ParserOptions.stdJs);
    options.setAssumeFunctionCallsAreMethods(true);
    options.setKeepAllStats(true);
    options.setEmitErrorDetails(true);
    compiler.setOptions(options);
    compiler.setErrorReporter(errorReporter);
    PassRegistry passRegistry = PassRegistry.instance();
    CompilerPass crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
    passManager.addPasses(Collections.singletonList(crossModuleMethodMotion));

    // Initialize input code
    String inputCode = "module Foo {\n" +
        "  export function bar() {\n" +
        "    return 'Foo.bar';\n" +
        "  }\n" +
        "}\n" +
        "module Bar {\n" +
        "  import Foo = require(\"./Foo\");\n" +
        "  export function baz() {\n" +
        "    return Foo.bar();\n" +
        "  }\n" +
        "}";
    Node externs = Nodeops.parse(inputCode, null);
    Node root = Nodeops.parse(inputCode, externs);

    // Run tests
    passManager.runPasses(root);
    String expectedOutput = "module Foo {\n" +
        "  export function bar() {\n" +
        "    return JSCompiler_stubMethod(0);\n" +
        "  }\n" +
        "}\n" +
        "module Bar {\n" +
        "  import Foo = require(\"./Foo\");\n" +
        "  export function baz() {\n" +
        "    JSCompiler_unstubMethod(0, function() {\n" +
        "      return Foo.bar();\n" +
        "    });\n" +
        "  }\n" +
        "}" +
        "var JSCompiler_stubMap = [];\n" +
        "function JSCompiler_stubMethod(JSCompiler_stubMethod_id) {\n" +
        "  return function() {\n" +
        "    return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(\n" +
        "        this, arguments);\n" +
        "  };\n" +
        "}\n" +
        "function JSCompiler_unstubMethod(JSCompiler_unstubMethod_id, JSCompiler_unstubMethod_body) {\n" +
        "  return JSCompiler_stubMap[JSCompiler_unstubMethod_id] = JSCompiler_unstubMethod_body;\n" +
        "}";
    String outputCode = Nodeops.toString(compiler.getCompiledOutput());
    assertEquals(expectedOutput, outputCode);
  }
}