import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ProcessLabels;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * RenameLabelsTest is a JUnit test file that tests the RenameLabels class.
 */
public class RenameLabelsTest {

  /**
   * Test method for RenameLabels class.
   */
  @org.junit.Test
  public void testRenameLabels() {

    // Create a mock Compiler instance.
    Compiler compiler = mock(Compiler.class);

    // Create a mock NodeFactory instance.
    NodeFactory nodeFactory = mock(NodeFactory.class);

    // Create a RenameLabels instance.
    RenameLabels renameLabels = new RenameLabels(compiler);

    // Create a NodeTraversal instance.
    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory);

    // Set up the test case.
    String inputCode = "function foo() {"
        + "  a: { "
        + "    b: { "
        + "      foo(); "
        + "    } "
        + "  } "
        + "  a: { "
        + "    b: break a; "
        + "  } "
        + "}";

    Node root = NodeUtil.parseJs(inputCode);
    renameLabels.process(null, root);

    // Create a ProcessLabels instance.
    ProcessLabels processLabels = new ProcessLabels();

    // Test traversal of the Node tree.
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check if the labels were renamed correctly.
    Node expectedOutput = NodeUtil.parseJs(
        "function foo() {"
            + "  " + ProcessLabels.NAME_A + ": { "
            + "    " + ProcessLabels.NAME_B + ": { "
            + "      foo(); "
            + "    } "
            + "  } "
            + "  " + ProcessLabels.NAME_A + ": { "
            + "    " + ProcessLabels.NAME_B + ": break "
            + "  } "
            + "}");

    assertEquals(expectedOutput.toJson(), root.toJson());
  }

  /**
   * Test method for edge cases of RenameLabels class when processing a Node tree
   * with labels that have the same name but different nesting levels.
   */
  @org.junit.Test
  public void testRenameLabelsWithSameNameButDifferentNestingLevels() {

    // Create a mock Compiler instance.
    Compiler compiler = mock(Compiler.class);

    // Create a mock NodeFactory instance.
    NodeFactory nodeFactory = mock(NodeFactory.class);

    // Create a RenameLabels instance.
    RenameLabels renameLabels = new RenameLabels(compiler);

    // Create a NodeTraversal instance.
    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory);

    // Set up the test case.
    String inputCode = "function foo() {"
        + "  a: { "
        + "    b: { "
        + "      a: { "
        + "        c: { "
        + "          break a; "
        + "        } "
        + "      } "
        + "    } "
        + "  } "
        + "  a: { "
        + "    b: break a; "
        + "  } "
        + "}";

    Node root = NodeUtil.parseJs(inputCode);
    renameLabels.process(null, root);

    // Create a ProcessLabels instance.
    ProcessLabels processLabels = new ProcessLabels();

    // Test traversal of the Node tree.
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check if the labels were renamed correctly.
    Node expectedOutput = NodeUtil.parseJs(
        "function foo() {"
            + "  " + ProcessLabels.NAME_A + ": { "
            + "    " + ProcessLabels.NAME_A_1 + ": { "
            + "      " + ProcessLabels.NAME_A_2 + ": { "
            + "        " + ProcessLabels.NAME_C + ": { "
            + "          break " + ProcessLabels.NAME_A + ";"
            + "        } "
            + "      } "
            + "    } "
            + "  } "
            + "  " + ProcessLabels.NAME_A + ": { "
            + "    " + ProcessLabels.NAME_B + ": break "
            + "  } "
            + "}");

    assertEquals(expectedOutput.toJson(), root.toJson());
  }

  /**
   * Test method for error scenarios of RenameLabels class when processing a Node tree
   * with invalid labels (e.g., empty label names or labels without a corresponding
   * break statement).
   */
  @org.junit.Test
  public void testRenameLabelsWithInvalidLabels() {

    // Create a mock Compiler instance.
    Compiler compiler = mock(Compiler.class);

    // Create a mock NodeFactory instance.
    NodeFactory nodeFactory = mock(NodeFactory.class);

    // Create a RenameLabels instance.
    RenameLabels renameLabels = new RenameLabels(compiler);

    // Create a NodeTraversal instance.
    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory);

    // Set up the test case for an empty label name.
    String inputCodeEmptyLabel = "function foo() {"
        + "  a: {} "
        + "}";

    Node rootEmptyLabel = NodeUtil.parseJs(inputCodeEmptyLabel);
    renameLabels.process(null, rootEmptyLabel);

    // Create a ProcessLabels instance.
    ProcessLabels processLabelsEmptyLabel = new ProcessLabels();

    // Test traversal of the Node tree.
    NodeTraversal.traverse(compiler, rootEmptyLabel, processLabelsEmptyLabel);

    // Check if no changes were made to the input code.
    Node expectedOutputEmptyLabel = NodeUtil.parseJs(inputCodeEmptyLabel);

    assertEquals(expectedOutputEmptyLabel.toJson(), rootEmptyLabel.toJson());

    // Set up the test case for a label without a corresponding break statement.
    String inputCodeNoBreak = "function foo() {"
        + "  a: { "
        + "    b: {} "
        + "  } "
        + "}";

    Node rootNoBreak = NodeUtil.parseJs(inputCodeNoBreak);
    renameLabels.process(null, rootNoBreak);

    // Create a ProcessLabels instance.
    ProcessLabels processLabelsNoBreak = new ProcessLabels();

    // Test traversal of the Node tree.
    NodeTraversal.traverse(compiler, rootNoBreak, processLabelsNoBreak);

    // Check if no changes were made to the input code.
    Node expectedOutputNoBreak = NodeUtil.parseJs(inputCodeNoBreak);

    assertEquals(expectedOutputNoBreak.toJson(), rootNoBreak.toJson());
  }

  /**
   * Test method for RenameLabels class when processing a Node tree with unused labels.
   */
  @org.junit.Test
  public void testRenameLabelsWithUnusedLabels() {

    // Create a mock Compiler instance.
    Compiler compiler = mock(Compiler.class);

    // Create a mock NodeFactory instance.
    NodeFactory nodeFactory = mock(NodeFactory.class);

    // Create a RenameLabels instance with removeUnused set to true.
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);

    // Create a NodeTraversal instance.
    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory);

    // Set up the test case.
    String inputCode = "function foo() {"
        + "  a: {} "
        + "  b: break a; "
        + "}";

    Node root = NodeUtil.parseJs(inputCode);
    renameLabels.process(null, root);

    // Create a ProcessLabels instance.
    ProcessLabels processLabels = new ProcessLabels();

    // Test traversal of the Node tree.
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check if the unused label was removed from the output code.
    Node expectedOutput = NodeUtil.parseJs("function foo() {b: break b;}");

    assertEquals(expectedOutput.toJson(), root.toJson());
  }
}