import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {
  private AbstractCompiler compiler;
  private Node root;
  private NodeTraversal nodeTraversal;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    CompilerOptions options = new CompilerOptions();
    when(compiler.getOptions()).thenReturn(options);
    root = Node.createTree("");
    nodeTraversal = new NodeTraversal(compiler, root);
  }

  @Test
  public void testRenameLabels() {
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    NodeTraversal labelTraversal = new NodeTraversal(compiler, root);
    labelTraversal.traverse(new RenameLabels.ProcessLabels());

    Node labelNode = Node.createLabelNode("a");
    labelNode.setParent(root);
    Node labelNameNode = labelNode.getFirstChild();

    Node labelNode2 = Node.createLabelNode("a");
    labelNode2.setParent(root);
    Node labelNameNode2 = labelNode2.getFirstChild();

    Node breakNode = Node.createBreakNode();
    breakNode.setParent(root);
    Node breakNameNode = breakNode.getFirstChild();

    Node continueNode = Node.createContinueNode();
    continueNode.setParent(root);
    Node continueNameNode = continueNode.getFirstChild();

    Node labelBlock = Node.createBlock();
    labelBlock.setParent(root);

    labelNameNode.setString("renamedLabel");
    breakNameNode.setString("renamedBreak");
    continueNameNode.setString("renamedContinue");

    labelTraversal.traverse(new RenameLabels.ProcessLabels());

    // Test label rename
    String labelName = labelNode.getFirstChild().getString();
    String labelNewName = renameLabels.getNameForId(1);
    assertNull("Label name should be removed", labelName);
    assertLabelName(labelNewName, labelNode);

    // Test label rename for multiple labels with same name
    String labelName2 = labelNode2.getFirstChild().getString();
    assertLabelName(labelNewName, labelNode2);

    // Test break and continue rename
    String breakName = breakNode.getFirstChild().getString();
    String continueName = continueNode.getFirstChild().getString();
    assertLabelName(labelNewName, breakNode);
    assertLabelName(labelNewName, continueNode);

    // Test removing unreferenced labels
    Node unreferencedLabel = Node.createLabelNode("unreferencedLabel");
    unreferencedLabel.setParent(labelBlock);
    labelTraversal.traverse(new RenameLabels.ProcessLabels());

    assertNull("Unreferenced label name should be removed",
        unreferencedLabel.getFirstChild().getString());
    assertFalse("Unreferenced label should be removed",
        root.contains(unreferencedLabel));
  }

  private void assertLabelName(String expectedLabelName, Node node) {
    String labelName = node.getFirstChild().getString();
    assertEquals(expectedLabelName, labelName);
  }

  @After
  public void teardown() {
    if (root != null) {
      root.deleteTree();
      root = null;
    }
  }
}