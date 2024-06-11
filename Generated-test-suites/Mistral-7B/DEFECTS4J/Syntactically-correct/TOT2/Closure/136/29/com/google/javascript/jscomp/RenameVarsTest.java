import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.*;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private static Node createNode(int type, String value) {
    return (Node) NodeFactory.createExpressionStatement(
        NodeFactory.createName(value));
  }

  @SuppressWarnings("unchecked")
  private static Node createVariableDeclaration(String name, int initialValue) {
    return (Node) NodeFactory.createVariableDeclarationList(
        NodeFactory.createVariableDeclaration(
            NodeFactory.createIdentifier(name),
            NodeFactory.createNumericLiteral(initialValue),
            NodeFactory.createNoInitializer())).getFirstChild();
  }

  @SuppressWarnings("unchecked")
  private static Node createFunctionDeclaration(String name) {
    return (Node) NodeFactory.createFunctionDeclaration(
        NodeFactory.createIdentifier(name),
        NodeFactory.createList(NodeFactory.SENTINEL, NodeFactory.SENTINEL),
        NodeFactory.createBlock(NodeFactory.SENTINEL, NodeFactory.SENTINEL));
  }

  private CompilerPass createRenameVars(String prefix, CompilerPass next) {
    return new RenameVars(
        new DummyCompiler(), prefix, false, false, false, null, null, null) {
          @Override
          public void process(Node externs, Node root) {
            super.process(externs, root);
            next.process(externs, root);
          }
        };
  }

  private void testRenameVars(String input, String expectedOutput) {
    CompilerInput ci = new CompilerInput("test.js", input, null, null);
    CompilerOptions options = new CompilerOptions();

    AbstractCompiler compiler = new Compiler(options);
    List<CompilerPass> passes = new ArrayList<>();
    passes.add(createRenameVars("_", null));
    passes.add(new PrintCompiledJs());

    Node externs = null;
    Node root = null;
    try {
      compiler.compile(ci, passes);
    } finally {
      externs = compiler.getExterns();
      root = compiler.getCompilationUnit();
    }

    String actualOutput = NodeUtil.getSource(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testNoRename() {
    String input = "function foo() {}";
    String expectedOutput = "function foo() {}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameGlobal() {
    String input = "var a = 1, b = 2;";
    String expectedOutput = "var a__ = 1, b___ = 2;";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameLocal() {
    String input = "function foo(a, b) {\n" +
        "  var c = a + b;\n" +
        "  var d = c * 2;\n" +
        "  return d;\n" +
        "}";
    String expectedOutput = "function foo(a, b) {\n" +
        "  var c__ = a;\n" +
        "  var d__ = b;\n" +
        "  var e = c__ + d__;\n" +
        "  var f = e * 2;\n" +
        "  return f;\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameGlobalWithPrefix() {
    String input = "var a = 1, b = 2;";
    String expectedOutput = "_a = 1, _b = 2;";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameLocalWithPrefix() {
    String input = "function foo(a, b) {\n" +
        "  var c = a + b;\n" +
        "  var d = c * 2;\n" +
        "  return d;\n" +
        "}";
    String expectedOutput = "function foo(a, b) {\n" +
        "  var c__ = a;\n" +
        "  var d__ = b;\n" +
        "  var e = c__ + d__;\n" +
        "  var f = e * 2;\n" +
        "  return f;\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameGlobalWithReservedName() {
    String input = "var function foo() {}";
    String expectedOutput = "var __function foo__ = {}";
    testRenameVars(input, expectedOutput);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRenameReservedName() {
    String input = "var var foo = 1";
    String expectedOutput = null;
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameExtern() {
    String input = "function foo() {}";
    String expectedOutput = "function foo() {}"; // No change
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameAnonymousFunction() {
    String input = "function foo() {\n" +
        "  (function() {\n" +
        "  })();\n" +
        "}";
    String expectedOutput = "function foo() {\n" +
        "  (function() {\n" +
        "    var g__ = function() {};\n" +
        "  })();\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameFunctionWithPreservedNames() {
    String input = "function foo() {\n" +
        "  function bar() {} // Preserve name\n" +
        "  function baz() {} // Preserve name\n" +
        "}";
    String expectedOutput = "function foo() {\n" +
        "  function bar() {} // Preserve name\n" +
        "  function baz() {} // Preserve name\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameFunctionWithPreservedNamesAndNewNames() {
    String input = "function foo() {\n" +
        "  function bar() {} // Preserve name\n" +
        "  function baz() {} // New name\n" +
        "}";
    String expectedOutput = "function foo() {\n" +
        "  function bar__() {} // Preserve name\n" +
        "  function baz__ = function() {} // New name\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameLocalWithPrefixAndSameName() {
    String input = "function foo(a, b) {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "  return a;\n" +
        "}";
    String expectedOutput = "function foo(a, b) {\n" +
        "  var a__ = 1;\n" +
        "  var a____ = 2;\n" +
        "  return a____;\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameVariablesWithSameNameInDifferentScopes() {
    String input = "function foo() {\n" +
        "  var a = 1;\n" +
        "  function bar() {\n" +
        "    var a = 2;\n" +
        "  }\n" +
        "  bar();\n" +
        "  return a;\n" +
        "}";
    String expectedOutput = "function foo() {\n" +
        "  var a__ = 1;\n" +
        "  function bar() {\n" +
        "    var a____ = 2;\n" +
        "  }\n" +
        "  bar();\n" +
        "  return a__;\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameVariablesWithSameNameInSameScope() {
    String input = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "  return a;\n" +
        "}";
    String expectedOutput = "function foo() {\n" +
        "  var a__ = 1;\n" +
        "  var a___ = 2;\n" +
        "  return a__;\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameVariablesWithSameNameInSameScopeMultipleTimes() {
    String input = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "  var a = 3;\n" +
        "  return a;\n" +
        "}";
    String expectedOutput = "function foo() {\n" +
        "  var a__ = 1;\n" +
        "  var a____ = 2;\n" +
        "  var a_____ = 3;\n" +
        "  return a_____;\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameVariablesWithSameNameInSameScopeMultipleTimesDifferentOrder() {
    String input = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var a = 3;\n" +
        "  var a = 2;\n" +
        "  return a;\n" +
        "}";
    String expectedOutput = "function foo() {\n" +
        "  var a__ = 1;\n" +
        "  var a____ = 3;\n" +
        "  var a_____ = 2;\n" +
        "  return a_____;\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }

  @Test
  public void testRenameVariablesWithSameNameInDifferentScopesDifferentOrder() {
    String input = "function foo() {\n" +
        "  var a = 1;\n" +
        "  function bar() {\n" +
        "    var a = 2;\n" +
        "  }\n" +
        "  bar();\n" +
        "  var a = 3;\n" +
        "  return a;\n" +
        "}";
    String expectedOutput = "function foo() {\n" +
        "  var a__ = 1;\n" +
        "  function bar() {\n" +
        "    var a____ = 2;\n" +
        "  }\n" +
        "  bar();\n" +
        "  var a_____ = 3;\n" +
        "  return a_____;\n" +
        "}";
    testRenameVars(input, expectedOutput);
  }
}