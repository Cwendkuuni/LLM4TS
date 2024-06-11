import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CheckTypes;
import com.google.javascript.jscomp.CompilerOptions.CompilerMode;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.CompilerOptions.WarningLevel;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Unit tests for {@code ProcessCommonJSModules} class.
 */
public class ProcessCommonJSModulesTest {

  private static final String TEST_INPUT_DIR = "test_inputs/";

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @org.junit.Test
  public void testConcatenateCommonJSModules() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerMode(CompilerMode.SIMPLE);
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);
    options.setSourceRoot(TEST_INPUT_DIR);
    options.setOutputFile("output.js");
    options.setGenerateDeclarationFiles(false);
    options.setCheckTypes(CheckTypes.NONE);
    options.setThrowErrorsAsExceptions(true);
    options.setWarningLevel(WarningLevel.QUIET);

    Compiler compiler = new Compiler();
    compiler.init();
    compiler.compile(new SourceFile("test.js", TEST_INPUT_DIR + "test.js"),
        options);

    Node root = compiler.getRootNode();
    assertNotNull(root);

    JSModule module = new ProcessCommonJSModules(compiler, null, false)
        .getModule();
    assertNotNull(module);

    // Check the output file.
    File output = new File("output.js");
    String outputContent = readFileToString(output);
    assertTrue(outputContent.contains("goog.provide('module$Name')"));
    assertTrue(outputContent.contains("//# sourceURL=test.js"));

    // Check the module object.
    Set<String> expected = Sets.newHashSet("module$Name", "module$Dependency");
    Set<String> actual = Sets.newHashSet();
    for (String name : module.getDependencies().keySet()) {
      actual.add(name.substring(1));
    }
    assertEquals(expected, actual);

    // Check the module exports.
    assertEquals("expectedValue", module.getModule().get("exports").get("default"));
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @org.junit.Test
  public void testHandleRelativeRequireCalls() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerMode(CompilerMode.SIMPLE);
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);
    options.setSourceRoot(TEST_INPUT_DIR);
    options.setOutputFile("output.js");
    options.setGenerateDeclarationFiles(false);
    options.setCheckTypes(CheckTypes.NONE);
    options.setThrowErrorsAsExceptions(true);
    options.setWarningLevel(WarningLevel.QUIET);

    Compiler compiler = new Compiler();
    compiler.init();
    compiler.compile(new SourceFile("test1.js", TEST_INPUT_DIR + "test1.js"),
        options);
    compiler.compile(new SourceFile("test2.js", TEST_INPUT_DIR + "test2.js"),
        options);

    Node root = compiler.getRootNode();
    assertNotNull(root);

    JSModule module1 = new ProcessCommonJSModules(compiler, null, false)
        .getModule();
    assertNotNull(module1);
    JSModule module2 = new ProcessCommonJSModules(compiler, null, false)
        .getModule();
    assertNotNull(module2);

    // Check the output file.
    File output = new File("output.js");
    String outputContent = readFileToString(output);
    assertTrue(outputContent.contains("goog.provide('module$Name1')"));
    assertTrue(outputContent.contains("goog.provide('module$Name2')"));
    assertTrue(outputContent.contains("goog.require('module$Name2')"));

    // Check the module objects.
    Set<String> expected1 = Sets.newHashSet("module$Name1", "module$Name2");
    Set<String> actual1 = Sets.newHashSet();
    for (String name : module1.getDependencies().keySet()) {
      actual1.add(name.substring(1));
    }
    assertEquals(expected1, actual1);

    Set<String> expected2 = Sets.newHashSet("module$Name1", "module$Name2");
    Set<String> actual2 = Sets.newHashSet();
    for (String name : module2.getDependencies().keySet()) {
      actual2.add(name.substring(1));
    }
    assertEquals(expected2, actual2);

    // Check the module exports.
    assertEquals("expectedValue1", module1.getModule().get("exports").get("default"));
    assertEquals("expectedValue2", module2.getModule().get("exports").get("default"));
  }

  private static String readFileToString(File file) throws IOException {
    byte[] bytes = new byte[(int) file.length()];
    FileInputStream fis = new FileInputStream(file);
    fis.read(bytes);
    fis.close();
    return new String(bytes, "UTF-8");
  }
}