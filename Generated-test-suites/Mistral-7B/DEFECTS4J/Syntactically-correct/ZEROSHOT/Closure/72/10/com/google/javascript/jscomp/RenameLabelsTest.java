import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private Node createLabelNode(String labelName) {
    Node label = NodeFactory.createLabel(labelName);
    Node labelStatement = NodeFactory.createStatement(label);
    Node block = NodeFactory.createBlock(labelStatement);
    return block;
  }

  @SuppressWarnings("unchecked")
  private Node createBreakOrContinueNode(String labelName) {
    Node breakNode = NodeFactory.createBreak();
    Node labelNode = NodeFactory.createLabel(labelName);
    Node newLabelNode = NodeFactory.createStatement(labelNode);
    Node statement = NodeFactory.createStatement(breakNode);
    Node block = NodeFactory.createBlock(statement, newLabelNode);
    return block;
  }

  private CompilerPass createRenameLabelsPass(AbstractCompiler compiler) {
    return new RenameLabels(compiler, null, true);
  }

  @org.junit.Test
  public void testRenamingLabel() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass renameLabels = createRenameLabelsPass(compiler);
    Node root = NodeFactory.createProgram();

    Node labelNode = createLabelNode("a");
    Node labelStatement = NodeFactory.createStatement(labelNode);
    Node block = NodeFactory.createBlock(labelStatement);
    Node rootWithLabel = NodeFactory.createBlock(block, root);

    when(compiler.addToDebugLog(String.valueOf(String.format("label renamed: %s => %s", "a", "newName")))).thenReturn(null);

    renameLabels.process(NodeFactory.createNullNode(), rootWithLabel);
    Node renamedLabelNode = NodeUtil.getFirstChild(labelStatement);
    assertEquals("newName", renamedLabelNode.getString());
  }

  @org.junit.Test
  public void testRenamingLabelWithReferences() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass renameLabels = createRenameLabelsPass(compiler);
    Node root = NodeFactory.createProgram();

    Node labelNode1 = createLabelNode("a");
    Node labelStatement1 = NodeFactory.createStatement(labelNode1);
    Node block1 = NodeFactory.createBlock(labelStatement1);
    Node labelNode2 = createLabelNode("a");
    Node labelStatement2 = NodeFactory.createStatement(labelNode2);
    Node breakNode = createBreakOrContinueNode("a");
    Node block2 = NodeFactory.createBlock(breakNode, labelStatement2);
    Node rootWithLabels = NodeFactory.createBlock(block1, block2, root);

    when(compiler.addToDebugLog(String.valueOf(String.format("label renamed: %s => %s", "a", "newName1"))))
        .thenReturn(null);
    when(compiler.addToDebugLog(String.valueOf(String.format("label renamed: %s => %s", "a", "newName2"))))
        .thenReturn(null);

    renameLabels.process(NodeFactory.createNullNode(), rootWithLabels);
    Node renamedLabelNode1 = NodeUtil.getFirstChild(labelStatement1);
    Node renamedLabelNode2 = NodeUtil.getFirstChild(labelStatement2);
    assertEquals("newName1", renamedLabelNode1.getString());
    assertEquals("newName2", renamedLabelNode2.getString());
  }

  @org.junit.Test
  public void testRemovingUnreferencedLabels() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass renameLabels = createRenameLabelsPass(compiler);
    Node root = NodeFactory.createProgram();

    Node labelNode1 = createLabelNode("a");
    Node labelStatement1 = NodeFactory.createStatement(labelNode1);
    Node block1 = NodeFactory.createBlock(labelStatement1);
    Node labelNode2 = createLabelNode("a");
    Node labelStatement2 = NodeFactory.createStatement(labelNode2);
    Node block2 = NodeFactory.createBlock(labelStatement2);
    Node rootWithLabels = NodeFactory.createBlock(block1, block2, root);

    renameLabels.process(NodeFactory.createNullNode(), rootWithLabels);

    Node renamedLabelNode1 = NodeUtil.getFirstChild(labelStatement1);
    Node renamedLabelNode2 = NodeUtil.getFirstChild(labelStatement2);
    assertEquals("newName1", renamedLabelNode1.getString());
    Node removedLabelNode = NodeUtil.getFirstChild(labelStatement2);
    assertEquals(Token.END, removedLabelNode.getType());
  }

  @org.junit.Test
  public void testNestedLabels() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass renameLabels = createRenameLabelsPass(compiler);
    Node root = NodeFactory.createProgram();

    Node outerLabelNode = createLabelNode("outer");
    Node outerLabelStatement = NodeFactory.createStatement(outerLabelNode);
    Node outerBlock = NodeFactory.createBlock(outerLabelStatement);
    Node innerLabelNode = createLabelNode("inner");
    Node innerLabelStatement = NodeFactory.createStatement(innerLabelNode);
    Node innerBreakNode = createBreakOrContinueNode("inner");
    Node innerBlock = NodeFactory.createBlock(innerLabelStatement, innerBreakNode);
    Node outerLabelWithInnerLabel = NodeFactory.createBlock(outerBlock, innerBlock, root);

    when(compiler.addToDebugLog(String.valueOf(String.format("label renamed: %s => %s", "outer", "outerName"))))
        .thenReturn(null);
    when(compiler.addToDebugLog(String.valueOf(String.format("label renamed: %s => %s", "inner", "innerName"))))
        .thenReturn(null);

    renameLabels.process(NodeFactory.createNullNode(), outerLabelWithInnerLabel);
    Node outerRenamedLabelNode = NodeUtil.getFirstChild(outerLabelStatement);
    Node innerRenamedLabelNode = NodeUtil.getFirstChild(innerLabelStatement);
    assertEquals("outerName", outerRenamedLabelNode.getString());
    assertEquals("innerName", innerRenamedLabelNode.getString());
  }
}