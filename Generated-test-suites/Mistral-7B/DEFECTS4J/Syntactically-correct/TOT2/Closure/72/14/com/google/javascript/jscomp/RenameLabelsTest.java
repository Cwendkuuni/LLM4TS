import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link RenameLabels}.
 */
public class RenameLabelsTest {

  /**
   * Test the basic functionality of {@link RenameLabels}.
   */
  @Test
  public void testRenameLabels() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameLabels(compiler);

    String source = "function foo() {" +
        "  a: { b: 1; }" +
        "  c: { break a; }" +
        "  a: { d: 2; }" +
        "  e: { continue a; }" +
        "  a: { break a; }" +
        "}";

    Node root = parseTree(source);
    pass.process(null, root);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    String result = printTree(root);

    assertEquals(
        "function foo() {" +
            "  a: { b: 1; }" +
            "  c: { break a_1; }" +
            "  a_1: { d: 2; }" +
            "  e: { continue a_1; }" +
            "  a_1: { break a_1; }" +
            "}",
        result);
  }

  /**
   * Test the rename of labels with the same name in different scopes.
   */
  @Test
  public void testRenameLabelsSameNameDifferentScopes() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameLabels(compiler);

    String source = "function foo() {" +
        "  a: { b: 1; }" +
        "  function bar() {" +
        "    a: { b: 2; }" +
        "  }" +
        "}";

    Node root = parseTree(source);
    pass.process(null, root);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    String result = printTree(root);

    assertEquals(
        "function foo() {" +
            "  a: { b: 1; }" +
            "  function bar() {" +
            "    a_1: { b: 2; }" +
            "  }" +
            "}",
        result);
  }

  /**
   * Test the removal of unreferenced labels.
   */
  @Test
  public void testRemoveUnreferencedLabels() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameLabels(compiler, new DefaultNameSupplier(), false);

    String source = "function foo() {" +
        "  a: { b: 1; }" +
        "  c: { break a; }" +
        "  a: { }" + // Unreferenced label
        "}";

    Node root = parseTree(source);
    pass.process(null, root);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    String result = printTree(root);

    assertEquals(
        "function foo() {" +
            "  c: { break a; }" + // The unreferenced label "a" is removed
            "}",
        result);
  }

  /**
   * Test the error handling when the same label name is used within the same scope.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSameLabelNameInSameScope() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameLabels(compiler);

    String source = "function foo() {" +
        "  a: { b: 1; }" +
        "  a: { break a; }" + // The same label name "a" is used twice
        "}";

    Node root = parseTree(source);
    pass.process(null, root);
  }

  /**
   * Test the error handling when a non-label node is used for label renaming.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonLabelNodeForLabelRenaming() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenameLabels(compiler);

    String source = "function foo() {" +
        "  a: { 1; }" + // The label node is replaced by a number
        "}";

    Node root = parseTree(source);
    pass.process(null, root);
  }
}