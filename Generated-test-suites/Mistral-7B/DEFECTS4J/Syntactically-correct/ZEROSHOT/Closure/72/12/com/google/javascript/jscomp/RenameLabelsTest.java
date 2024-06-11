import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.Supplier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @org.junit.Test
  public void testRenameLabels() {
    // Create mock compiler.
    Compiler compiler = mock(Compiler.class);

    // Create mock NodeTraversal.
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getOptions()).thenReturn(mock(NodeOptions.class));
    when(compiler.getTraverser()).thenReturn(nodeTraversal);

    // Create mock NodeUtil.
    NodeUtil nodeUtil = mock(NodeUtil.class);
    when(nodeTraversal.getUtil()).thenReturn(nodeUtil);

    // Create mock Supplier.
    Supplier<String> nameSupplier = mock(Supplier.class);
    when(nameSupplier.get()).thenReturn("label1");

    // Create RenameLabels instance.
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.nameSupplier = nameSupplier;

    // Create test node.
    Node root = new Node(Token.PROGRAM, null);
    Node label1 = new Node(Token.LABEL, "label1");
    Node block1 = new Node(Token.BLOCK, null);
    Node label2 = new Node(Token.LABEL, "label2");
    Node block2 = new Node(Token.BLOCK, null);
    Node break1 = new Node(Token.BREAK, null);
    Node continue1 = new Node(Token.CONTINUE, null);

    // Add children to root node.
    root.addChild(label1);
    root.addChild(block1);
    block1.addChild(label2);
    block1.addChild(block2);
    block2.addChild(break1);
    block2.addChild(continue1);

    // Call process method.
    renameLabels.process(null, root);

    // Check label names.
    assertEquals("label1", label1.getString());
    assertEquals("label2", label2.getString());

    // Check break and continue names.
    assertEquals("label2", break1.getFirstChild().getString());
    assertEquals("label2", continue1.getFirstChild().getString());

    // Check that label1 is not present in the renaming map.
    assertEquals(null, namespaceStackPeek(renameLabels.namespaceStack).renameMap.get("label1"));

    // Check that label2 is present in the renaming map.
    assertEquals(new LabelInfo(1), namespaceStackPeek(renameLabels.namespaceStack).renameMap.get("label2"));

    // Check that block1 is not merged.
    assertEquals(5, NodeUtil.getChildCount(block1));

    // Check that block2 is merged.
    assertEquals(3, NodeUtil.getChildCount(block2));
  }

  private LabelNamespace namespaceStackPeek(Deque<LabelNamespace> namespaceStack) {
    return namespaceStack.peek();
  }

}