import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * RenameLabelsTest is a JUnit 4 test file for the RenameLabels class.
 */
public class RenameLabelsTest {

  /**
   * Test that all labels are renamed correctly in simple cases.
   */
  @ org.junit.Test
  public void testSimpleRename() {
    String sourceCode = "function foo() {" +
                        "  a: { break a; }" +
                        "  b: { break b; }" +
                        "  c: { break c; }" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, externs, root);
    new ProcessLabels().traverse(nodeTraversal);

    String expectedOutput = "function foo() {" +
                           "  a: { break a_0; }" +
                           "  b: { break b_1; }" +
                           "  c: { break c_2; }" +
                           "}";

    assertEquals(expectedOutput, NodePrinter.printToSource(root));
  }

  /**
   * Test that all labels are renamed correctly in nested cases.
   */
  @ org.junit.Test
  public void testNestedRename() {
    String sourceCode = "function foo() {" +
                        "  a: { b: { break a; } }" +
                        "  c: { b: { break c; } }" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, externs, root);
    new ProcessLabels().traverse(nodeTraversal);

    String expectedOutput = "function foo() {" +
                           "  a: { b: { break a_0; } }" +
                           "  c: { b: { break c_1; } }" +
                           "}";

    assertEquals(expectedOutput, NodePrinter.printToSource(root));
  }

  /**
   * Test that all labels are renamed correctly in cases with multiple labels at
   * the same depth.
   */
  @ org.junit.Test
  public void testMultipleLabelsAtSameDepth() {
    String sourceCode = "function foo() {" +
                        "  a: { break a; }" +
                        "  a: { break a; }" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, externs, root);
    new ProcessLabels().traverse(nodeTraversal);

    String expectedOutput = "function foo() {" +
                           "  a_0: { break a_0; }" +
                           "  a_1: { break a_1; }" +
                           "}";

    assertEquals(expectedOutput, NodePrinter.printToSource(root));
  }

  /**
   * Test that all labels are renamed correctly when there are no labels.
   */
  @ org.junit.Test
  public void testNoLabels() {
    String sourceCode = "function foo() {}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);

    String expectedOutput = "function foo() {}";

    assertEquals(expectedOutput, NodePrinter.printToSource(root));
  }

  /**
   * Test that labels are not renamed if they are not in a label statement.
   */
  @ org.junit.Test
  public void testLabelNotRenamed() {
    String sourceCode = "function foo() {" +
                        "  var a = 5;" +
                        "  break a;" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, externs, root);
    new ProcessLabels().traverse(nodeTraversal);

    String expectedOutput = "function foo() {" +
                           "  var a = 5;" +
                           "  break a;" +
                           "}";

    assertEquals(expectedOutput, NodePrinter.printToSource(root));
  }

  /**
   * Test that labels are not renamed if they are in a comment.
   */
  @ org.junit.Test
  public void testLabelNotRenamedInComment() {
    String sourceCode = "function foo() {" +
                        "  // label a" +
                        "  break a;" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, externs, root);
    new ProcessLabels().traverse(nodeTraversal);

    String expectedOutput = "function foo() {" +
                           "  // label a" +
                           "  break a;" +
                           "}";

    assertEquals(expectedOutput, NodePrinter.printToSource(root));
  }

  /**
   * Test that labels are not renamed if they are not reachable.
   */
  @ org.junit.Test
  public void testLabelNotReachable() {
    String sourceCode = "function foo() {" +
                        "  a: { break a; }" +
                        "  return;" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);

    String expectedOutput = "function foo() {" +
                           "  a: { break a; }" +
                           "  return;" +
                           "}";

    assertEquals(expectedOutput, NodePrinter.printToSource(root));
  }

  /**
   * Test that labels with empty names are not renamed.
   */
  @ org.junit.Test
  public void testLabelWithEmptyName() {
    String sourceCode = "function foo() {" +
                        "  a: { break a; }" +
                        "  b: { break ''; }" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);

    String expectedOutput = "function foo() {" +
                           "  a: { break a; }" +
                           "  b: { break ''; }" +
                           "}";

    assertEquals(expectedOutput, NodePrinter.printToSource(root));
  }

  /**
   * Test that labels with invalid characters are not renamed.
   */
  @ org.junit.Test
  public void testLabelWithInvalidCharacter() {
    String sourceCode = "function foo() {" +
                        "  a: { break 'a\\0'; }" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    try {
      new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);
      fail("Expected compiler error");
    } catch (RuntimeException e) {
      // Expected
    }

    String expectedErrorMessage = "Invalid label name: a\\0";
    assertTrue(e.getMessage().contains(expectedErrorMessage));
  }

  /**
   * Test that labels with duplicate names are not renamed.
   */
  @ org.junit.Test
  public void testLabelWithDuplicateName() {
    String sourceCode = "function foo() {" +
                        "  a: { break a; }" +
                        "  a: { break a; }" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    try {
      new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);
      fail("Expected compiler error");
    } catch (RuntimeException e) {
      // Expected
    }

    String expectedErrorMessage = "Duplicate label name: a";
    assertTrue(e.getMessage().contains(expectedErrorMessage));
  }

  /**
   * Test that labels with keyword names are not renamed.
   */
  @ org.junit.Test
  public void testLabelWithKeywordName() {
    String sourceCode = "function foo() {" +
                        "  var break = 5;" +
                        "  break: { break break; }" +
                        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createScript(sourceCode);

    try {
      new RenameLabels(compiler, new DefaultNameSupplier(), true).process(externs, root);
      fail("Expected compiler error");
    } catch (RuntimeException e) {
      // Expected
    }

    String expectedErrorMessage = "Invalid label name: break";
    assertTrue(e.getMessage().contains(expectedErrorMessage));
  }
}