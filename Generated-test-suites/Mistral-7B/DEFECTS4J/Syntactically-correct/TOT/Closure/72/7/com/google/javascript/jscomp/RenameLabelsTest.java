import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for RenameLabels CompilerPass.
 */
public class RenameLabelsTest {

  /**
   * Test method for RenameLabels class.
   */
  @org.junit.Test
  public void testRenameLabels() {
    // Create compiler and pass
    AbstractCompiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    // Create test code
    Node root = NodeFactory.createProgram();
    Node label1 = NodeFactory.createLabel("a");
    Node label2 = NodeFactory.createLabel("b");
    Node label3 = NodeFactory.createLabel("a");
    Node block1 = NodeFactory.createBlock();
    Node breakStatement1 = NodeFactory.createBreak("a");
    Node block2 = NodeFactory.createBlock();
    Node breakStatement2 = NodeFactory.createBreak("b");
    Node label4 = NodeFactory.createLabel("a");
    Node continueStatement = NodeFactory.createContinue();

    root.addChild(label1);
    root.addChild(block1);
    block1.addChild(LabelUtil.createLabeledStatement(continueStatement));
    root.addChild(label2);
    root.addChild(block2);
    block2.addChild(LabelUtil.createLabeledStatement(breakStatement1));
    block2.addChild(label3);
    root.addChild(label4);

    // Run the pass
    renameLabels.process(null, root);

    // Check if labels were renamed correctly
    Node visitedLabel1 = findNode(root, "renamed_a_0");
    Node visitedLabel2 = findNode(root, "renamed_b_0");
    Node visitedLabel3 = findNode(root, "renamed_a_1");
    Node visitedLabel4 = findNode(root, "renamed_a_2");

    assertNotNull(visitedLabel1);
    assertNotNull(visitedLabel2);
    assertNotNull(visitedLabel3);
    assertNotNull(visitedLabel4);

    assertEquals("renamed_a_0", visitedLabel1.getString());
    assertEquals("renamed_b_0", visitedLabel2.getString());
    assertEquals("renamed_a_1", visitedLabel3.getString());
    assertEquals("renamed_a_2", visitedLabel4.getString());

    // Check if breaks and continues were renamed correctly
    Node visitedBreakStatement1 = findNode(root, "renamed_a_0");
    Node visitedBreakStatement2 = findNode(root, "renamed_b_1");

    assertNotNull(visitedBreakStatement1);
    assertNotNull(visitedBreakStatement2);

    assertEquals("renamed_a_0", visitedBreakStatement1.getString());
    assertEquals("renamed_b_1", visitedBreakStatement2.getString());

    // Check if unreferenced labels were removed
    Node removedLabel = findNode(root, "a");
    assertNull(removedLabel);

    // Check if unused break or continue were removed
    Node unusedBreakOrContinue = findNode(root, "continue");
    assertNull(unusedBreakOrContinue);
  }

  private Node findNode(Node node, String name) {
    if (node.getType() == Token.LABEL && node.getString().equals(name)) {
      return node;
    }

    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = node.getChild(i);
      Node result = findNode(child, name);
      if (result != null) {
        return result;
      }
    }

    return null;
  }
}