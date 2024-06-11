import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private CompilerPass createRenameVars(String prefix, boolean localRenamingOnly, boolean preserveAnonymousFunctionNames, boolean generatePseudoNames, VariableMap prevUsedRenameMap, char[] reservedCharacters, Set<String> reservedNames) {
    CompilerContext context = mock(CompilerContext.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(mockCodingConvention());
    CompilerPassManager passManager = mock(CompilerPassManager.class);

    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
    when(compiler.getInput()).thenReturn(mockInput());
    when(compiler.getPassManager()).thenReturn(passManager);

    return renameVars;
  }

  private CompilerInput mockInput() {
    return mock(CompilerInput.class);
  }

  private CodingConvention mockCodingConvention() {
    return mock(CodingConvention.class);
  }

  private CodingConvention mockCodingConventionThatReturnsFalseForExportedNames() {
    return new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return false;
      }
    };
  }

  private void testRenameVars(String sourceCode, String expectedOutput, String prefix, boolean localRenamingOnly, boolean preserveAnonymousFunctionNames, boolean generatePseudoNames, VariableMap prevUsedRenameMap, char[] reservedCharacters, Set<String> reservedNames) {
    CompilerPass renameVars = createRenameVars(prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

    CompilerPassManager passManager = mock(CompilerPassManager.class);
    when(passManager.runPasses(any(AbstractCompiler.class), anyList(), anyList())).thenReturn(null);

    SourceFile input = SourceFile.fromText("test.js", sourceCode);
    when(compiler.getInput()).thenReturn(input);

    Node root = Node.parse(input.getText());

    renameVars.process(null, root);

    String actualOutput = compiler.getDebugLog().toString();
    assertEquals(expectedOutput, actualOutput);
  }

  @org.junit.Test
  public void testRenameVarsWithDefaults() {
    String sourceCode = "function foo() { var a = 1; }";
    String expectedOutput = "function foo() {\n" +
        "  var b = a;\n" +
        "  a = 1;\n" +
        "}\n";

    testRenameVars(sourceCode, expectedOutput, null, false, false, false, null, null, null);
  }

  @org.junit.Test
  public void testRenameVarsWithPrefix() {
    String sourceCode = "function foo() { var a = 1; }";
    String expectedOutput = "function foo() {\n" +
        "  var _$1 = a;\n" +
        "  a = 1;\n" +
        "}\n";

    testRenameVars(sourceCode, expectedOutput, "_$", false, false, false, null, null, null);
  }

  @org.junit.Test
  public void testRenameVarsWithLocalRenamingOnly() {
    String sourceCode = "function foo() { var a = 1, b = 1; }";
    String expectedOutput = "function foo() {\n" +
        "  var c = a, d = b;\n" +
        "  a = 1;\n" +
        "  b = 1;\n" +
        "}\n";

    testRenameVars(sourceCode, expectedOutput, null, true, false, false, null, null, null);
  }

  @org.junit.Test
  public void testRenameVarsWithPreserveAnonymousFunctionNames() {
    String sourceCode = "function foo() { var x = function() {}; }";
    String expectedOutput = "function foo() {\n" +
        "  var x = function x() {};\n" +
        "}\n";

    testRenameVars(sourceCode, expectedOutput, null, false, true, false, null, null, null);
  }

  @org.junit.Test
  public void testRenameVarsWithGeneratePseudoNames() {
    String sourceCode = "function foo() { var a = 1; }";
    String expectedOutput = "function foo() {\n" +
        "  var $0 = a;\n" +
        "  a = 1;\n" +
        "}\n";

    testRenameVars(sourceCode, expectedOutput, null, false, false, true, null, null, null);
  }

  @org.junit.Test
  public void testRenameVarsWithPrevUsedRenameMap() {
    String sourceCode = "function foo() { var a = 1, b = 1; }";
    String expectedOutput = "function foo() {\n" +
        "  var c = a, d = b;\n" +
        "  a = 1;\n" +
        "  b = 1;\n" +
        "}\n";

    VariableMap prevUsedRenameMap = new VariableMap();
    prevUsedRenameMap.put("a", "c");
    prevUsedRenameMap.put("b", "d");

    testRenameVars(sourceCode, expectedOutput, null, false, false, false, prevUsedRenameMap, null, null);
  }

  @org.junit.Test
  public void testRenameVarsWithReservedCharacters() {
    String sourceCode = "function foo() { var a@=1, b%=1; }";
    String expectedOutput = "function foo() {\n" +
        "  var a_ = 1, b__ = 1;\n" +
        "}\n";

    char[] reservedCharacters = new char[]{'@', '%'};

    testRenameVars(sourceCode, expectedOutput, null, false, false, false, null, Arrays.asList(reservedCharacters).toArray(new char[0]), Collections.emptySet());
  }

  @org.junit.Test
  public void testRenameVarsWithReservedNames() {
    String sourceCode = "function foo() { var Math = 1; }";
    String expectedOutput = "function foo() {\n" +
        "  var Math__ = 1;\n" +
        "}\n";

    Set<String> reservedNames = Sets.newHashSet("Math");

    testRenameVars(sourceCode, expectedOutput, null, false, false, false, null, null, reservedNames);
  }

  @org.junit.Test
  public void testRenameVarsWithExterns() {
    String sourceCode = "var a = 1; function foo() { var b = 1; }";
    String expectedOutput = "var a = 1;\n" +
        "function foo() {\n" +
        "  var b__ = 1;\n" +
        "}\n";

    testRenameVars(sourceCode, expectedOutput, null, false, false, false, null, null, null);
  }
}