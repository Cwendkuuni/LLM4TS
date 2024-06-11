import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RenameVarsTest {

  public static void main(String[] args) throws Exception {
    testRenameVars();
  }

  public static void testRenameVars() throws Exception {
    // Create aCompiler instance and set options
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);
    compiler.setOptions(options);

    // Create a test source file
    TestSourceFile testSourceFile = new TestSourceFile("test.js");
    SourceFile sourceFile = NodeFactory.createSourceFile("test.js", testSourceFile.getCode().getBytes());

    // Create a RenameVars instance
    String prefix = "test_";
    boolean localRenamingOnly = true;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    @Nullable VariableMap prevUsedRenameMap = null;
    @Nullable char[] reservedCharacters = null;
    @Nullable Set<String> reservedNames = Sets.newHashSet();
    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames,
        generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

    // Perform the test
    Node root = NodeTraversal.traverse(compiler, sourceFile.getChildNodes(), NodeTraversal.EMPTY);
    renameVars.process(root, root);

    // Check the output
    String expectedOutput =
        "test_a = a;\n" +
        "test_b = b;\n" +
        "test_c = c;\n" +
        "test_d = d;\n" +
        "test_e = e;\n" +
        "test_extern = extern;\n" +
        "test_fa = fa;\n" +
        "test_ga = ga;\n";
    String actualOutput = compiler.getDebugInfo().getJsSource();
    assertEquals(expectedOutput, actualOutput);

    // Check local variable renaming
    String localTestSourceFileCode =
        "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = function() { return a; };\n" +
        "  var d = 3;\n" +
        "  var e = 4;\n" +
        "}";
    TestSourceFile localTestSourceFile = new TestSourceFile("local_test.js", localTestSourceFileCode);
    sourceFile = NodeFactory.createSourceFile("local_test.js", localTestSourceFile.getCode().getBytes());
    root = NodeTraversal.traverse(compiler, sourceFile.getChildNodes(), NodeTraversal.EMPTY);
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames,
        generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(root, root);
    expectedOutput =
        "test_L0_a = a;\n" +
        "test_L0_b = b;\n" +
        "test_L0_c = function test_L0_c() { return test_L0_a; };\n" +
        "test_L1_d = d;\n" +
        "test_L1_e = e;\n";
    actualOutput = compiler.getDebugInfo().getJsSource();
    assertEquals(expectedOutput, actualOutput);

    // Check global variable renaming
    String globalTestSourceFileCode =
        "var a = 1;\n" +
        "var b = 2;\n" +
        "var extern = 3;\n" +
        "function fa() {}\n" +
        "function ga() {}\n";
    TestSourceFile globalTestSourceFile =
        new TestSourceFile("global_test.js", globalTestSourceFileCode);    sourceFile = NodeFactory.createSourceFile("global_test.js",
        globalTestSourceFile.getCode().getBytes());
    root = NodeTraversal.traverse(compiler, sourceFile.getChildNodes(), NodeTraversal.EMPTY);
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames,
        generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(root, root);
    expectedOutput =
        "test_a = a;\n" +
        "test_b = b;\n" +
        "test_extern = extern;\n" +
        "test_fa = fa;\n" +
        "test_ga = ga;\n";
    actualOutput = compiler.getDebugInfo().getJsSource();
    assertEquals(expectedOutput, actualOutput);

    // Check variable renaming with reserved names
    String reservedTestSourceFileCode =
        "var a = 1;\n" +
        "var this = 2;\n" +
        "function foo() {}\n";
    TestSourceFile reservedTestSourceFile = new TestSourceFile("reserved_test.js",
        reservedTestSourceFileCode);
    sourceFile = NodeFactory.createSourceFile("reserved_test.js",
        reservedTestSourceFile.getCode().getBytes());
    root = NodeTraversal.traverse(compiler, sourceFile.getChildNodes(), NodeTraversal.EMPTY);
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames,
        generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(root, root);
    expectedOutput =
        "test_a = a;\n" +
        "test_this$ = this;\n" +
        "test_foo = foo;\n";
    actualOutput = compiler.getDebugInfo().getJsSource();
    assertEquals(expectedOutput, actualOutput);

    // Check variable renaming with invalid characters
    String invalidTestSourceFileCode =
        "var a# = 1;\n" +
        "var a$ = 2;\n" +
        "var a@ = 3;\n";
    TestSourceFile invalidTestSourceFile = new TestSourceFile("invalid_test.js",
        invalidTestSourceFileCode);
    sourceFile = NodeFactory.createSourceFile("invalid_test.js",
        invalidTestSourceFile.getCode().getBytes());
    root = NodeTraversal.traverse(compiler, sourceFile.getChildNodes(), NodeTraversal.EMPTY);
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames,
        generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(root, root);
    expectedOutput =
        "test_a_ = a#;\n" +
        "test_a_$ = a$\n" +
        "test_a_@ = a@;\n";
    actualOutput = compiler.getDebugInfo().getJsSource();
    assertEquals(expectedOutput, actualOutput);

    // Check variable renaming with long names
    String longTestSourceFileCode =
        "var aVeryLongVariableName = 1;\n" +
        "var bVeryLongVariableName = 2;\n";
    TestSourceFile longTestSourceFile = new TestSourceFile("long_test.js", longTestSourceFileCode);
    sourceFile = NodeFactory.createSourceFile("long_test.js", longTestSourceFile.getCode().getBytes());
    root = NodeTraversal.traverse(compiler, sourceFile.getChildNodes(), NodeTraversal.EMPTY);
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames,
        generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(root, root);
    expectedOutput =
        "test_avlvrn = aVeryLongVariableName;\n" +
        "test_bvlvrn = bVeryLongVariableName;\n";
    actualOutput = compiler.getDebugInfo().getJsSource();
    assertEquals(expectedOutput, actualOutput);

    // Check variable renaming with short names
    String shortTestSourceFileCode =
        "var a = 1;\n" +
        "var b = 2;\n" +
        "var c = 3;\n" +
        "var d = 4;\n" +
        "var e = 5;\n";
    TestSourceFile shortTestSourceFile = new TestSourceFile("short_test.js", shortTestSourceFileCode);
    sourceFile = NodeFactory.createSourceFile("short_test.js", shortTestSourceFile.getCode().getBytes());
    root = NodeTraversal.traverse(compiler, sourceFile.getChildNodes(), NodeTraversal.EMPTY);
    renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames,
        generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
    renameVars.process(root, root);
    expectedOutput =
        "test_a = a;\n" +
        "test_b = b;\n" +
        "test_c = c;\n" +
        "test_d = d;\n" +
        "test_e = e;\n";
    actualOutput = compiler.getDebugInfo().getJsSource();
    assertEquals(expectedOutput, actualOutput);

    // Clean up
    compiler.dispose();
  }
}