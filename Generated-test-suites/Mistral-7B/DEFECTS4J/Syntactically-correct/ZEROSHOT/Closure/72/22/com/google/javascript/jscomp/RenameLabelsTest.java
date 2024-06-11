import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private CompilerPass pass;
  private Node rootNode;
  private NodeTraversal traversal;

  @ org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    pass = new RenameLabels(compiler);
    rootNode = mock(Node.class);
    traversal = mock(NodeTraversal.class);

    when(compiler.addToDebugLog(String.class)).thenReturn(null);
    when(compiler.reportCodeChange()).thenReturn(null);

    NodeUtil.initBlock(rootNode);
  }

  @ org.junit.Test
  public void testRenameLabels() {
    // Given a simple Node tree
    String labelName = "testLabel";
    String newLabelName = "newLabelName";
    Node labelNode = createLabelNode(labelName);
    Node breakNode = createBreakNode(labelName);
    Node continueNode = createContinueNode(labelName);

    Node parent = mock(Node.class);
    Node blockNode = mock(Node.class);

    NodeUtil.initBlock(blockNode);
    NodeUtil.appendChild(blockNode, labelNode);
    NodeUtil.appendChild(blockNode, breakNode);
    NodeUtil.appendChild(blockNode, continueNode);
    NodeUtil.appendChild(parent, blockNode);

    when(nodeTraversal.getCurrentNode()).thenReturn(parent);
    when(nodeTraversal.getParentNode()).thenReturn(rootNode);

    // When passing the test to the RenameLabels compiler pass
    pass.process(null, rootNode);

    // Then the label is renamed in the Node tree
    assertEquals(newLabelName, labelNode.getString());
    assertEquals(newLabelName, breakNode.getFirstChild().getString());
    assertEquals(newLabelName, continueNode.getFirstChild().getString());
  }

  @ org.junit.Test
  public void testRenameLabelsWithMultipleLabels() {
    // Given a Node tree with multiple labels
    String label1Name = "label1";
    String label2Name = "label2";
    String newLabel1Name = "newLabel1";
    String newLabel2Name = "newLabel2";

    Node label1Node = createLabelNode(label1Name);
    Node label2Node = createLabelNode(label2Name);
    Node breakNode1 = createBreakNode(label1Name);
    Node breakNode2 = createBreakNode(label2Name);
    Node continueNode1 = createContinueNode(label1Name);
    Node continueNode2 = createContinueNode(label2Name);

    Node parent = mock(Node.class);
    Node blockNode1 = mock(Node.class);
    Node blockNode2 = mock(Node.class);

    NodeUtil.initBlock(blockNode1);
    NodeUtil.appendChild(blockNode1, label1Node);
    NodeUtil.appendChild(blockNode1, breakNode1);
    NodeUtil.appendChild(blockNode1, continueNode1);
    NodeUtil.appendChild(parent, blockNode1);

    NodeUtil.initBlock(blockNode2);
    NodeUtil.appendChild(blockNode2, label2Node);
    NodeUtil.appendChild(blockNode2, breakNode2);
    NodeUtil.appendChild(blockNode2, continueNode2);
    NodeUtil.appendChild(parent, blockNode2);

    when(nodeTraversal.getCurrentNode()).thenReturn(parent);
    when(nodeTraversal.getParentNode()).thenReturn(rootNode);

    // When passing the test to the RenameLabels compiler pass
    pass.process(null, rootNode);

    // Then the labels are renamed in the Node tree
    assertEquals(newLabel1Name, label1Node.getString());
    assertEquals(newLabel1Name, breakNode1.getFirstChild().getString());
    assertEquals(newLabel1Name, continueNode1.getFirstChild().getString());

    assertEquals(newLabel2Name, label2Node.getString());
    assertEquals(newLabel2Name, breakNode2.getFirstChild().getString());
    assertEquals(newLabel2Name, continueNode2.getFirstChild().getString());
  }

  @ org.junit.Test
  public void testRenameLabelsWithUnreferencedLabel() {
    // Given a Node tree with an unreferenced label
    String labelName = "label";
    String newLabelName = "newLabel";

    Node labelNode = createLabelNode(labelName);

    Node parent = mock(Node.class);
    Node blockNode = mock(Node.class);

    NodeUtil.initBlock(blockNode);
    NodeUtil.appendChild(blockNode, labelNode);
    NodeUtil.appendChild(parent, blockNode);

    when(nodeTraversal.getCurrentNode()).thenReturn(parent);
    when(nodeTraversal.getParentNode()).thenReturn(rootNode);

    // When passing the test to the RenameLabels compiler pass
    pass.process(null, rootNode);

    // Then the label is removed from the Node tree
    assertEquals(Token.NONE, labelNode.getType());
  }

  private Node createLabelNode(String name) {
    Node labelNode = new Node(Token.LABEL);
    labelNode.setString(name);
    labelNode.setLineNumber(0);
    labelNode.setLineOffset(0);
    return labelNode;
  }

  private Node createBreakNode(String labelName) {
    Node breakNode = new Node(Token.BREAK);
    Node label = new Node(Token.IDENT);
    label.setString(labelName);
    breakNode.setFirstChild(label);
    breakNode.setLineNumber(0);
    breakNode.setLineOffset(0);
    return breakNode;
  }

  private Node createContinueNode(String labelName) {
    Node continueNode = new Node(Token.CONTINUE);
    Node label = new Node(Token.IDENT);
    label.setString(labelName);
    continueNode.setFirstChild(label);
    continueNode.setLineNumber(0);
    continueNode.setLineOffset(0);
    return continueNode;
  }
}