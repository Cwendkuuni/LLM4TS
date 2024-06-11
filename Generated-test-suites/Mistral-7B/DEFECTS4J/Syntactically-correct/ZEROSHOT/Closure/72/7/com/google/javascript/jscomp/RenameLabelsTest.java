import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RenameLabelsTest {

  private static AbstractCompiler createCompiler() {
    return new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // No-op
      }
    };
  }

  @SuppressWarnings("unchecked")
  private static <T> T newSupplier(T value) {
    return (Supplier<T>) () -> value;
  }

  @Test
  public void testRenameLabels() {
    CompilerPass pass = new RenameLabels(createCompiler());
    Supplier<String> nameSupplier = newSupplier("a");

    AbstractCompiler compiler = createCompiler();
    compiler.addPasses(pass);

    Node root = Node.newBlock("root");
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, null);

    pass.process(null, root);
    NodeTraversal.traverse(compiler, root, nodeTraversal);

    String labelName = "a";
    int labelDepth = 1;

    // Test label rename.
    Node labelNode = Node.newLabel(labelName);
    Node labelReference = Node.newIdentifier(labelName);
    Node labelBlock = Node.newBlock();
    labelBlock.addChild(labelNode);
    labelBlock.addChild(Node.newBreak(Token.BREAK, 0));

    root.addChild(labelBlock);
    root.addChild(Node.newExpressionStatement(labelReference));

    // Test label reference.
    Node labelNode2 = Node.newLabel(labelName);
    root.addChild(labelNode2);
    Node labelReference2 = Node.newIdentifier(labelName);
    root.addChild(Node.newExpressionStatement(labelReference2));

    Map<String, LabelInfo> renameMap = (Map<String, LabelInfo>) nodeTraversal.getUserData();
    LabelInfo li = renameMap.get(labelName);

    assertTrue(li != null);
    assertTrue(li.referenced);

    assertEquals(labelName, li.id + "");

    String newLabelName = pass.getNameForId(labelDepth);
    assertEquals(newLabelName, "a");

    labelReference.setString(newLabelName);

    Node renameLabelNode = nodeTraversal.findFirstChildOfType(root, Node.LABEL);
    assertNotNull(renameLabelNode);
    assertEquals(newLabelName, renameLabelNode.getFirstChild().getString());

    // Test removing unreferenced labels.
    if (pass.removeUnused) {
      Node unusedLabelNode = Node.newLabel("unusedLabel");
      Node unusedLabelBlock = Node.newBlock();
      unusedLabelBlock.addChild(unusedLabelNode);
      unusedLabelBlock.addChild(Node.newBreak(Token.BREAK, 0));
      root.addChild(unusedLabelBlock);

      pass.process(null, root);
      NodeUtil.tryMergeBlock(root);

      Node removedLabelNode = nodeTraversal.findChildOfType(root, Node.LABEL);
      assertNull(removedLabelNode);
    }

    // Test new label with same name as previous one.
    labelDepth++;

    labelName = "a";
    String newLabelName2 = pass.getNameForId(labelDepth);
    assertEquals(newLabelName2, "b");
    labelNode = Node.newLabel(labelName);
    labelReference = Node.newIdentifier(labelName);
    labelBlock = Node.newBlock();
    labelBlock.addChild(labelNode);
    labelBlock.addChild(Node.newBreak(Token.BREAK, 0));

    root.addChild(labelBlock);
    root.addChild(Node.newExpressionStatement(labelReference));

    li = renameMap.get(labelName);
    assertNotNull(li);
    assertFalse(li.referenced);

    assertEquals(labelName, li.id + "");
    assertEquals("b", pass.getNameForId(labelDepth));
    assertEquals("b", labelNode.getFirstChild().getString());
  }
}