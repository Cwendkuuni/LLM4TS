import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createLabel;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.rhino.NodeFactory.createToken;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Suppliers;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TopLevelEnvironment;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeUtil;

public class RenameLabelsTest {

  private Compiler compiler;
  private CompilerOptions options;
  private TopLevelEnvironment env;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.WARNINGS);
    compiler = new CheckerFactory().getChecker(options);
    env = new TopLevelEnvironment(compiler, null, null);
  }

  @Test
  public void testRenameLabels() {
    String jsCode = "function bar() {"
        + "  var a = 1;"
        + "  a: { "
        + "    b: { "
        + "      foo();"
        + "    }"
        + "  }"
        + "  a: { "
        + "    b: break a;"
        + "  }"
        + "}";

    Node root = compiler.parseTree(jsCode, "bar", env);
    NodeUtil.tryMergeBlock(root);

    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    NodeTraversal.traverse(renameLabels, traversal);

    Node expectedOutput = NodeFactory.createBlock("function bar() {", "}");
    expectedOutput.setLineInfo(0, 0, 0);
    Node actualOutput = traversal.getRootNode();
    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test(expected = AssertionError.class)
  public void testLabelClash() {
    String jsCode = "function foo() {"
        + "  var a = 1;"
        + "  a: { "
        + "    label a;"
        + "  }"
        + "}";

    Node root = compiler.parseTree(jsCode, "foo", env);
    NodeUtil.tryMergeBlock(root);

    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    NodeTraversal.traverse(renameLabels, traversal);

    fail("Expected an assertion error due to label clash.");
  }

  @Test
  public void testRemoveUnusedLabels() {
    String jsCode = "function foo() {"
        + "  a: { "
        + "    b: break a;"
        + "  }"
        + "  c: { "
        + "    d: break a;"
        + "  }"
        + "}";

    Node root = compiler.parseTree(jsCode, "foo", env);
    NodeUtil.tryMergeBlock(root);

    RenameLabels renameLabels = new RenameLabels(compiler, Suppliers.ofInstance(() -> "X"), false);
    renameLabels.process(null, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    NodeTraversal.traverse(renameLabels, traversal);

    Node expectedOutput = NodeFactory.createBlock("function foo() {", "}");
    expectedOutput.setLineInfo(0, 0, 0);
    Node actualOutput = traversal.getRootNode();
    assertEquals(expectedOutput.toString(), actualOutput.toString());

    Node aLabel = findLabelNode(traversal, "a");
    assertNull(aLabel);
  }

  private Node findLabelNode(NodeTraversal traversal, String labelName) {
    for (Node child = traversal.getRootNode(); child != null; child = child.getNext()) {
      if (child.getType() == Token.LABEL && child.getFirstChild().getString().equals(labelName)) {
        return child;
      }
    }
    return null;
  }

}