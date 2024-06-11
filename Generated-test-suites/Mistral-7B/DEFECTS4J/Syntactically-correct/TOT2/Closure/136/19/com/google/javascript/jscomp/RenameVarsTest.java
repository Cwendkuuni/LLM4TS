import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link RenameVars}.
 */
public class RenameVarsTest {

  /**
   * Test for basic functionality of RenameVars.
   */
  @Test
  public void testRenameVars() {
    String sourceCode = "function foo(a, b) {" +
        "  var c = a + b;" +
        "  return c;" +
        "}";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVars.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());

    Node tree = inputNode.getTree();
    NodeTraversal traversal = new NodeTraversal(tree, null);
    // Check that the variables have been renamed
    assertEquals("a", traversal.getScope().getVar("a").getName());
    assertEquals("b", traversal.getScope().getVar("b").getName());
    assertEquals("c1", traversal.getScope().getVar("c").getName());
  }

  /**
   * Test for edge case: renaming variables in a loop.
   */
  @Test
  public void testRenameVarsWithLoop() {
    String sourceCode = "function foo(a) {" +
        "  for (var i = 0; i < a.length; i++) {" +
        "    a[i] = i;" +
        "  }" +
        "}";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithLoop.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());

    Node tree = inputNode.getTree();
    NodeTraversal traversal = new NodeTraversal(tree, null);
    // Check that the variables have been renamed correctly
    assertEquals("a", traversal.getScope().getVar("a").getName());
    assertEquals("i0", traversal.getScope().getVar("i").getName());
  }

  /**
   * Test for exception handling: renaming variables that are not assignable.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRenameVarsWithNonAssignableVariable() {
    String sourceCode = "function foo() {" +
        "  var a = [1, 2, 3];" +
        "  this.b = a[0];" +
        "}" +
        "foo();";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithNonAssignableVariable.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());
  }

  /**
   * Test for exception handling: renaming globally defined variables.
   */
  @Test(expected = IllegalStateException.class)
  public void testRenameVarsWithGlobalVariables() {
    String sourceCode = "var a = 1;" +
        "function foo() {" +
        "  var b = a;" +
        "}" +
        "foo();";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithGlobalVariables.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());
  }

  /**
   * Test for exception handling: renaming functions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRenameVarsWithFunction() {
    String sourceCode = "function foo() {}" +
        "foo();";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithFunction.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());
  }

  /**
   * Test for exception handling: renaming private variables.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testRenameVarsWithPrivateVariables() {
    String sourceCode = "function foo() {" +
        "  var _a = 1;" +
        "}" +
        "foo();";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithPrivateVariables.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());
  }

  /**
   * Test for exception handling: renaming variables with reserved keywords.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRenameVarsWithReservedKeywords() {
    String sourceCode = "function foo(this, super) {" +
        "  var for = 1;" +
        "}" +
        "foo();";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithReservedKeywords.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());
  }

  /**
   * Test for exceptional handling: renaming variables with characters not allowed.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRenameVarsWithInvalidCharacters() {
    String sourceCode = "function foo() {" +
        "  var !a = 1;" +
        "}" +
        "foo();";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithInvalidCharacters.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, new char[]{'!'}, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());
  }

  /**
   * Test for exceptional handling: renaming variables with duplicate names.
   */
  @Test(expected = IllegalStateException.class)
  public void testRenameVarsWithDuplicateNames() {
    String sourceCode = "function foo() {" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}" +
        "foo();";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithDuplicateNames.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());
  }

  /**
   * Test for exceptional handling: renaming variables with long names.
   */
  @Test
  public void testRenameVarsWithLongNames() {
    String sourceCode = "function foo(veryLongVariableName1, veryLongVariableName2) {" +
        "  var veryLongVariableName3 = veryLongVariableName1 + veryLongVariableName2;" +
        "  return veryLongVariableName3;" +
        "}";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithLongNames.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());

    Node tree = inputNode.getTree();
    NodeTraversal traversal = new NodeTraversal(tree, null);
    // Check that the variables have been renamed
    assertEquals("veryLongVariableName1", traversal.getScope().getVar("veryLongVariableName1").getName());
    assertEquals("veryLongVariableName2", traversal.getScope().getVar("veryLongVariableName2").getName());
    assertEquals("veryLongVariableName3", traversal.getScope().getVar("veryLongVariableName3").getName());

    // Check that the function name has not been changed
    assertEquals("foo", traversal.getScope().getFunc("foo").getName());
  }

  /**
   * Test for exceptional handling: renaming variables with long names in a loop.
   */
  @Test
  public void testRenameVarsWithLongNamesInLoop() {
    String sourceCode = "function foo(veryLongVariableName1) {" +
        "  for (var veryLongVariableName2 = 0; veryLongVariableName2 < veryLongVariableName1.length; veryLongVariableName2++) {" +
        "    var veryLongVariableName3 = veryLongVariableName1[veryLongVariableName2];" +
        "  }" +
        "}" +
        "foo([1, 2, 3]);";

    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);

    SourceFile input = new SourceFile("TestRenameVarsWithLongNamesInLoop.js", sourceCode);
    CompilerInput inputNode = compiler.parse(input, null);

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(inputNode.getTree(), inputNode.getTree());

    Node tree = inputNode.getTree();
    NodeTraversal traversal = new NodeTraversal(tree, null);
    // Check that the variables have been renamed
    assertEquals("veryLongVariableName1", traversal.getScope().getVar("veryLongVariableName1").getName());
    assertEquals("veryLongVariableName2", traversal.getScope().getVar("veryLongVariableName2").getName());
    assertEquals("veryLongVariableName3", traversal.getScope().getVar("veryLongVariableName3").getName());

    // Check that the function name has not been changed
    assertEquals("foo", traversal.getScope().getFunc("foo").getName());
  }
}