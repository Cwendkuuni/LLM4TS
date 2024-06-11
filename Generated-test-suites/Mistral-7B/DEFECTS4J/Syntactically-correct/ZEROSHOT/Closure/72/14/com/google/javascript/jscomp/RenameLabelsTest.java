import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.NodeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  CompilerPass renameLabels;

  @org.junit.Before
  public void setUp() {
    CompilerContext context = new CompilerContext(null, null, null);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getDebugLogger()).thenReturn(mock(Node.PrintWriter.class));
    renameLabels = new RenameLabels(compiler);
  }

  @org.junit.Test
  public void testSimpleRename() {
    String sourceCode = "function foo() { a: { break a; } }";
    Node root = ParserTestUtil.parse(sourceCode);
    renameLabels.process(null, root);
    Node labelNode = getLabelNode(root, "a");
    assertEquals("a_0", labelNode.getFirstChild().getString());
  }

  @org.junit.Test
  public void testNestedRename() {
    String sourceCode = "function foo() { a: { b: { break a; } } }";
    Node root = ParserTestUtil.parse(sourceCode);
    renameLabels.process(null, root);
    Node labelNodeA = getLabelNode(root, "a");
    assertEquals("a_0", labelNodeA.getFirstChild().getString());
    Node labelNodeB = getLabelNode(labelNodeA, "b");
    assertEquals("b_0", labelNodeB.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRemoveUnusedLabel() {
    String sourceCode = "function foo() { a: {} }";
    Node root = ParserTestUtil.parse(sourceCode);
    renameLabels = new RenameLabels(mock(AbstractCompiler.class), new DefaultNameSupplier(), false);
    renameLabels.process(null, root);
    Node labelNodeA = getLabelNode(root, "a");
    assertEquals(null, labelNodeA);
  }

  @org.junit.Test
  public void testRenameLabelInsideBlock() {
    String sourceCode = "function foo() { a: { break a; } }";
    Node root = ParserTestUtil.parse(sourceCode);
    Node blockNode = getBlockNode(root, 0);
    renameLabels.process(null, root);
    Node labelNodeA = getLabelNode(blockNode, "a");
    assertEquals("a_0", labelNodeA.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameLabelInsideConditional() {
    String sourceCode = "function foo() { if (true) { a: { break a; } } }";
    Node root = ParserTestUtil.parse(sourceCode);
    renameLabels.process(null, root);
    Node labelNodeA = getLabelNode(root, "a");
    assertEquals("a_0", labelNodeA.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameLabelInsideLoop() {
    String sourceCode = "function foo() { for (var i = 0; i < 5; i++) { a: { break a; } } }";
    Node root = ParserTestUtil.parse(sourceCode);
    renameLabels.process(null, root);
    Node labelNodeA = getLabelNode(root, "a");
    assertEquals("a_0", labelNodeA.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameLabelInsideSwitch() {
    String sourceCode = "function foo() { switch (1) { case 1: a: { break a; } } }";
    Node root = ParserTestUtil.parse(sourceCode);
    renameLabels.process(null, root);
    Node labelNodeA = getLabelNode(root, "a");
    assertEquals("a_0", labelNodeA.getFirstChild().getString());
  }

  private Node getLabelNode(Node root, String labelName) {
    NodeTraversal traversal = NodeTraversal.create(root, null, null);
    NodeTraversal.traverse(renameLabels, root, new ProcessLabels());
    return traversal.findFirstDescendantOfType(labelName, Token.LABEL);
  }

  private Node getBlockNode(Node root, int index) {
    Node blockNode = NodeUtil.findBlockNodeContaining(root, index);
    return blockNode;
  }
}