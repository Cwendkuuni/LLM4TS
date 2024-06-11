import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.CheckLevel;
import static com.google.javascript.jscomp.CompilerOptions.SourceFile;
import static com.google.javascript.jscomp.CompilerOptions.WarningLevel;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class VarCheckTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    Parser parser = new Parser();
    NodeFactory factory = new NodeFactory();
    SourceFile input = new SourceFile("Test.js", "test", parser, factory);
    compiler = new Compiler();
    options = new CompilerOptions();
  }

  @Test
  public void testVarCheck() {
    String code = "function foo() {" +
        "var x; " +
        "var y = 1; " +
        "var z = x;" +
        "}";
    options.setSourceFile(SourceFile.guessName("Test.js"));
    options.setOption(CompilerOptions.WARNING_NON_EXPORTED_IDENTIFIERS,
        WarningLevel.ERROR);
    options.setOption(CompilerOptions.ERRORS, CheckLevel.ERROR);
    Node ast = parser.parse(code, null, null);
    Diagnostics diagnostics = NodeTraversal.traverse(compiler, ast, new VarCheck(compiler));
    assertEquals(0, diagnostics.getNumErrors());
  }

  @Test
  public void testUndefinedVar() {
    String code = "function foo() {" +
        "var x; " +
        "y = 1;" +
        "}";
    options.setSourceFile(SourceFile.guessName("Test.js"));
    options.setOption(CompilerOptions.WARNING_NON_EXPORTED_IDENTIFIERS,
        WarningLevel.ERROR);
    options.setOption(CompilerOptions.ERRORS, CheckLevel.ERROR);
    Node ast = parser.parse(code, null, null);
    Diagnostics diagnostics = NodeTraversal.traverse(compiler, ast, new VarCheck(compiler));
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("variable y is undefined", diagnostics.getError(0).getMessage());
  }

  @Test
  public void testUndefinedVarInFunction() {
    String code = "function foo() {" +
        "function bar() {" +
        "var x; " +
        "y = 1;" +
        "} " +
        "bar(); " +
        "}";
    options.setSourceFile(SourceFile.guessName("Test.js"));
    options.setOption(CompilerOptions.WARNING_NON_EXPORTED_IDENTIFIERS,
        WarningLevel.ERROR);
    options.setOption(CompilerOptions.ERRORS, CheckLevel.ERROR);
    Node ast = parser.parse(code, null, null);
    Diagnostics diagnostics = NodeTraversal.traverse(compiler, ast, new VarCheck(compiler));
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("variable y is undefined", diagnostics.getError(0).getMessage());
  }

  @Test
  public void testUndefinedVarInGlobalScope() {
    String code = "var x; " +
        "y = 1;";
    options.setSourceFile(SourceFile.guessName("Test.js"));
    options.setOption(CompilerOptions.WARNING_NON_EXPORTED_IDENTIFIERS,
        WarningLevel.ERROR);
    options.setOption(CompilerOptions.ERRORS, CheckLevel.ERROR);
    Node ast = parser.parse(code, null, null);
    Diagnostics diagnostics = NodeTraversal.traverse(compiler, ast, new VarCheck(compiler));
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("variable y is undefined", diagnostics.getError(0).getMessage());
  }

  @Test
  public void testStrictModuleDependency() {
    String moduleA = "function foo() {}";
    String moduleB = "function bar() { import foo; }";
    options.setSourceFile(SourceFile.guessName("ModuleA.js"));
    options.setOption(CompilerOptions.WARNING_NON_EXPORTED_IDENTIFIERS,
        WarningLevel.ERROR);
    options.setOption(CompilerOptions.ERRORS, CheckLevel.ERROR);
    Node astA = parser.parse(moduleA, null, null);
    Diagnostics diagnosticsA = NodeTraversal.traverse(compiler, astA, new VarCheck(compiler));
    assertEquals(0, diagnosticsA.getNumErrors());

    options.setSourceFile(SourceFile.guessName("ModuleB.js"));
    Node astB = parser.parse(moduleB, null, null);
    Diagnostics diagnosticsB = NodeTraversal.traverse(compiler, astB, new VarCheck(compiler));
    assertEquals(1, diagnosticsB.getNumErrors());
    assertEquals("module ModuleA cannot reference foo, defined in " +
        "module ModuleA, since ModuleA loads after ModuleB",
        diagnosticsB.getError(0).getMessage());
  }

  @Test
  public void testMissingModuleDependency() {
    String moduleA = "function foo() {}";
    String moduleB = "function bar() { import foo; }";
    String code = "function main() {" +
        "import ModuleB; " +
        "ModuleB.bar(); " +
        "}";
    options.setSourceFile(SourceFile.guessName("Test.js"));
    options.setOption(CompilerOptions.WARNING_NON_EXPORTED_IDENTIFIERS,
        WarningLevel.ERROR);
    options.setOption(CompilerOptions.ERRORS, CheckLevel.ERROR);
    Node astA = parser.parse(moduleA, null, null);
    Node astB = parser.parse(moduleB, null, null);
    Node astMain = parser.parse(code, null, ImmutableSet.of(astA, astB));
    Diagnostics diagnostics = NodeTraversal.traverse(compiler, astMain, new VarCheck(compiler));
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("missing module dependency; module Test should depend " +
        "on ModuleA because it references foo",
        diagnostics.getError(0).getMessage());
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    String code = "function foo() {}";
    options.setSourceFile(SourceFile.guessName("Test.js"));
    options.setOption(CompilerOptions.WARNING_NON_EXPORTED_IDENTIFIERS,
        WarningLevel.ERROR);
    options.setOption(CompilerOptions.ERRORS, CheckLevel.ERROR);
    Node ast = parser.parse(code, null, null);
    Diagnostics diagnostics = NodeTraversal.traverse(compiler, ast, new VarCheck(compiler));
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("Syntax error: function declaration must have a name",
        diagnostics.getError(0).getMessage());
  }

  @Test
  public void testNameReferenceInExterns() {
    String code = "function foo() {" +
        "var x = { y: 1 };" +
        "var z = x.y;" +
        "}";
    options.setSourceFile(SourceFile.guessName("Test.js"));
    options.setOption(CompilerOptions.WARNING_NON_EXPORTED_IDENTIFIERS,
        WarningLevel.ERROR);
    options.setOption(CompilerOptions.ERRORS, CheckLevel.ERROR);
    Node ast = parser.parse(code, null, null);
    Diagnostics diagnostics = NodeTraversal.traverse(compiler, ast, new VarCheck(compiler));
    assertEquals(1, diagnostics.getNumErrors());
    assertEquals("accessing name y in externs has no effect",
        diagnostics.getError(0).getMessage());
  }
}