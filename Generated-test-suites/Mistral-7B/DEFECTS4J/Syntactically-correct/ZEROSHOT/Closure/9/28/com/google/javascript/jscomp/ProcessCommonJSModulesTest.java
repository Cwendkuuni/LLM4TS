import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestingCompiler.checkRewrittenSource;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JsOutput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessingEnvironment;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.jscomp.testing.ProcessCompileResult;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String TEST_FILE = "ProcessCommonJSModulesTest.js";
  private static final String EXPECTED_OUTPUT = "ProcessCommonJSModulesTest-out.js";
  private static final String FILENAME_PREFIX = "test" + File.separator;

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    CompilerOptions options = new CompilerOptions(
        Arrays.asList("-d", TEST_FILE + "," + EXPECTED_OUTPUT),
        new CompilerInput(new File("src", TEST_FILE)),
        JsOutput.COMPILE);

    ProcessingEnvironment env = new ProcessingEnvironment(options);
    AbstractCompiler compiler = new AbstractCompiler(options, env);
    compiler.addPass(new ProcessCommonJSModules(compiler, FILENAME_PREFIX));

    TestSourceFile source = new TestSourceFile("src", TEST_FILE);
    List<ProcessCompileResult> results = compiler.compile(source);

    Node actual = results.get(0).getOutput();
    Node expected = new Node(IR.prog(new Node[0]));

    try {
      expected = TestingCompiler.loadJsSourceFile(new File(EXPECTED_OUTPUT));
    } finally {
      new File(EXPECTED_OUTPUT).deleteOnExit();
    }

    checkRewrittenSource(actual, expected);

    Set<String> expectedModules = new HashSet<>();
    Set<String> actualModules = new HashSet<>();

    visitModules(expected, expectedModules);
    visitModules(actual, actualModules);

    assertEquals(expectedModules, actualModules);
  }

  private void visitModules(Node node, Set<String> modules) {
    if (node.isScript()) {
      String moduleName = ProcessCommonJSModules.toModuleName(
          node.getSourceFileName(), "");
      modules.add(moduleName);
    }

    for (int i = 0; i < node.getChildCount(); ++i) {
      visitModules(node.getChildAtIndex(i), modules);
    }
  }
}