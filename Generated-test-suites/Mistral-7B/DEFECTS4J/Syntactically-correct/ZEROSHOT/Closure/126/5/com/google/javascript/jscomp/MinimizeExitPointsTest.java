package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParserTestUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;

/**
 * Tests for {@link MinimizeExitPoints}.
 */
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    String source =
        "function foo(x, y) {\n"
            + "  if (x) {\n"
            + "    return;\n"
            + "  }\n"
            + "  if (y) {\n"
            + "    return;\n"
            + "  }\n"
            + "  console.log('never reaches this');\n"
            + "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Node root = ParserTestUtil.parseOneFile(compiler, source);

    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);

    NodeUtil.assertCodeEquals(
        "function foo(x, y) {\n"
            + "  if (x) ;\n"
            + "  if (y) ;\n"
            + "  console.log('never reaches this');\n"
            + "}",
        IR.toString(root));

    // Test for named breaks
    source =
        "function foo() {\n"
            + "  for (var i = 0; i < 5; i++) {\n"
            + "    if (i == 2) {\n"
            + "      break label1;\n"
            + "    }\n"
            + "    console.log(i);\n"
            + "  }\n"
            + "  label1:\n"
            + "  console.log('breaks at 2');\n"
            + "}";

    root = ParserTestUtil.parseOneFile(compiler, source);

    minimizer.process(null, root);

    NodeUtil.assertCodeEquals(
        "function foo() {\n"
            + "  for (var i = 0; i < 5; i++) {\n"
            + "    if (i != 2) {\n"
            + "      console.log(i);\n"
            + "    }\n"
            + "  }\n"
            + "  console.log('breaks at 2');\n"
            + "}",
        IR.toString(root));

    // Test for empty blocks
    source =
        "function foo() {\n"
            + "  if (true) {\n"
            + "  }\n"
            + "  return;\n"
            + "}";

    root = ParserTestUtil.parseOneFile(compiler, source);

    minimizer.process(null, root);

    NodeUtil.assertCodeEquals(
        "function foo() {\n"
            + "  return;\n"
            + "}",
        IR.toString(root));

    // Test for exits in try/catch/finally
    source =
        "function foo() {\n"
            + "  try {\n"
            + "    throw new Error();\n"
            + "  } catch (e) {\n"
            + "    return;\n"
            + "  } finally {\n"
            + "    return;\n"
            + "  }\n"
            + "}";

    root = ParserTestUtil.parseOneFile(compiler, source);

    minimizer.process(null, root);

    NodeUtil.assertCodeEquals(
        "function foo() {\n"
            + "  try {\n"
            + "  \n"
            + "  } catch (e) {\n"
            + "  } finally {\n"
            + "  }\n"
            + "  return;\n"
            + "}",
        IR.toString(root));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMinimizeExitPointsNullCompiler() {
    new MinimizeExitPoints(null);
  }
}