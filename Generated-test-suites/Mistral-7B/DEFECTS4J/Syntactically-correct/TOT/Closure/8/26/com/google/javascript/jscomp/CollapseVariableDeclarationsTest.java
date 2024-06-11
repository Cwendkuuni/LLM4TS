package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

/**
 * Test for CollapseVariableDeclarations CompilerPass
 */
public class CollapseVariableDeclarationsTest {
  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testSimpleCollapse() {
    String input = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "function foo() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test
  public void testCollapseAssignments() {
    String input = "function foo() {\n" +
        "  var a = true;\n" +
        "  var b = true;\n" +
        "  var c = true;\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "function foo() {\n" +
        "  var c = b = a = true;\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test
  public void testCollapseInsideFunction() {
    String input = "function foo() {\n" +
        "  function bar() {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "function foo() {\n" +
        "  function bar() {\n" +
        "    var a, b = 1, c = 2;\n" +
        "  }\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test
  public void testCollapseInsideIfStatement() {
    String input = "function foo() {\n" +
        "  if (true) {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "  var c = 2;\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "function foo() {\n" +
        "  if (true) {\n" +
        "    var a, b = 1;\n" +
        "  }\n" +
        "  var c = 2;\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test
  public void testCollapseInsideForStatement() {
    String input = "function foo() {\n" +
        "  for (var i = 0; i < 5; i++) {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "function foo() {\n" +
        "  for (var i = 0, a, b = 1; i < 5; i++) {\n" +
        "  }\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test
  public void testCollapseInsideIfElseStatement() {
    String input = "function foo() {\n" +
        "  if (true) {\n" +
        "    var a;\n" +
        "  } else {\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "  var c = 2;\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "function foo() {\n" +
        "  if (true) {\n" +
        "  } else {\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "  var a, c = 2;\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseInsideSwitchStatement() {
    String input = "function foo() {\n" +
        "  switch (1) {\n" +
        "    case 1:\n" +
        "      var a;\n" +
        "      break;\n" +
        "    case 2:\n" +
        "      var b = 1;\n" +
        "      break;\n" +
        "  }\n" +
        "  var c = 2;\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
  }

  @Test
  public void testCollapseInsideSwitchDefaultCase() {
    String input = "function foo() {\n" +
        "  switch (1) {\n" +
        "    case 1:\n" +
        "      var a;\n" +
        "      break;\n" +
        "    default:\n" +
        "      var b = 1;\n" +
        "  }\n" +
        "  var c = 2;\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "function foo() {\n" +
        "  switch (1) {\n" +
        "    case 1:\n" +
        "    default:\n" +
        "      var a = b = 1;\n" +
        "  }\n" +
        "  var c = 2;\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test
  public void testCollapseInsideCatchBlock() {
    String input = "try {\n" +
        "  throw new Error();\n" +
        "} catch (e) {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "try {\n" +
        "  throw new Error();\n" +
        "} catch (e) {\n" +
        "  var a, b = 1;\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test
  public void testCollapseInsideTryMultipleCatchBlocks() {
    String input = "try {\n" +
        "  throw new Error();\n" +
        "} catch (e1) {\n" +
        "  var a;\n" +
        "} catch (e2) {\n" +
        "  var b = 1;\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "try {\n" +
        "  throw new Error();\n" +
        "} catch (e1) {\n" +
        "  var a = e2 = 1;\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }

  @Test
  public void testCollapseInsideForInLoop() {
    String input = "function foo() {\n" +
        "  for (var i in ['a', 'b']) {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "}";

    Node root = compiler.parse(input, null, null);
    compiler.runPasses(root, new CollapseVariableDeclarations(compiler));
    String expected = "function foo() {\n" +
        "  for (var i in ['a', 'b']) {\n" +
        "    var a, b = 1;\n" +
        "  }\n" +
        "}";

    String output = compiler.getSource(root);
    assertEquals(expected, output);
  }
}