import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.compiler.Compiler;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private RenameLabels renameLabels;

  @org.junit.Before
  public void setUp() {
    compiler = mock(Compiler.class);
    nodeFactory = mock(NodeFactory.class);
    renameLabels = new RenameLabels(compiler);
  }

  @org.junit.Test
  public void testRenameLabels_SimpleLabel() {
    Node labelNode = nodeFactory.createLabelNode("a");
    Node parentNode = nodeFactory.createBlockNode();
    parentNode.addChild(labelNode);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentScope()).thenReturn(parentNode);

    renameLabels.process(null, null);
    NodeTraversal.traverse(compiler, parentNode, renameLabels.new ProcessLabels());

    assertEquals("a", labelNode.getString());
  }

  @org.junit.Test
  public void testRenameLabels_NestedLabels() {
    Node labelNode1 = nodeFactory.createLabelNode("a");
    Node labelNode2 = nodeFactory.createLabelNode("b");
    Node labelNode3 = nodeFactory.createLabelNode("b");

    Node parentNode1 = nodeFactory.createBlockNode();
    parentNode1.addChild(labelNode1);

    Node parentNode2 = nodeFactory.createBlockNode();
    parentNode2.addChild(parentNode1);
    parentNode2.addChild(labelNode2);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentScope()).thenReturn(parentNode2);

    renameLabels.process(null, null);
    NodeTraversal.traverse(compiler, parentNode2, renameLabels.new ProcessLabels());

    assertEquals("b_0", labelNode1.getString());
    assertEquals("b_1", labelNode2.getString());
  }

  @org.junit.Test
  public void testRenameLabels_UnreferencedLabel() {
    Node labelNode = nodeFactory.createLabelNode("a");
    Node parentNode = nodeFactory.createBlockNode();
    parentNode.addChild(labelNode);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentScope()).thenReturn(parentNode);

    renameLabels.process(null, null);
    NodeTraversal.traverse(compiler, parentNode, renameLabels.new ProcessLabels());

    assertNull(labelNode.getParent());
  }

  @org.junit.Test
  public void testRenameLabels_LabelNotInCurrentScope() {
    Node labelNode = nodeFactory.createLabelNode("a");
    Node parentNode = nodeFactory.createBlockNode();
    Node outerParentNode = nodeFactory.createBlockNode();
    outerParentNode.addChild(parentNode);
    parentNode.addChild(labelNode);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentScope()).thenReturn(parentNode);

    renameLabels.process(null, null);
    NodeTraversal.traverse(compiler, outerParentNode, renameLabels.new ProcessLabels());

    assertEquals("a", labelNode.getString());
  }

  @org.junit.Test
  public void testRenameLabels_LabelWithSameNameInDifferentScopes() {
    Node labelNode1 = nodeFactory.createLabelNode("a");
    Node labelNode2 = nodeFactory.createLabelNode("a");

    Node parentNode1 = nodeFactory.createBlockNode();
    parentNode1.addChild(labelNode1);

    Node parentNode2 = nodeFactory.createBlockNode();
    parentNode2.addChild(labelNode2);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getCurrentScope()).thenReturn(parentNode1);

    renameLabels.process(null, null);
    NodeTraversal.traverse(compiler, parentNode1, renameLabels.new ProcessLabels());

    assertEquals("a_0", labelNode1.getString());
    assertEquals("a_1", labelNode2.getString());
  }
}