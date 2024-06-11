import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessLabels;
import com.google.javascript.jscomp.RenameLabels;

@RunWith(MockitoJUnitRunner.class)
public class RenameLabelsTest {
  @Mock
  private Compiler compiler;

  private Node node;
  private ProcessLabels processLabels;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.node = Node.createIntLiteral(1);
    this.processLabels = new ProcessLabels();
  }

  @Test
  public void testProcessLabels_noLabels_noChange() {
    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, node, null);
    processLabels.process(nodeTraversal);

    assertEquals(node, nodeTraversal.root);
    assertNull(nodeTraversal.getRoot().getFirstChild());
  }

  @Test
  public void testProcessLabels_simpleLabel_nameChanged() {
    Node labelNode = Node.createLabel("a");
    node.addChild(labelNode);
    Node labelNameNode = labelNode.getFirstChild();

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, node, null);
    processLabels.process(nodeTraversal);

    assertEquals(node, nodeTraversal.root);
    String newLabelName = processLabels.getNameForId(1);
    assertEquals("a", labelNameNode.getString());
    assertEquals(newLabelName, labelNameNode.getString());
  }

  @Test
  public void testProcessLabels_multipleLabels_namesChanged() {
    Node labelNode1 = Node.createLabel("a");
    Node labelNameNode1 = labelNode1.getFirstChild();
    node.addChild(labelNode1);

    Node labelNode2 = Node.createLabel("a");
    Node labelNameNode2 = labelNode2.getFirstChild();
    node.addChild(labelNode2);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, node, null);
    processLabels.process(nodeTraversal);

    assertEquals(node, nodeTraversal.root);
    String newLabelName1 = processLabels.getNameForId(1);
    assertEquals("a", labelNameNode1.getString());
    assertEquals(newLabelName1, labelNameNode1.getString());

    String newLabelName2 = processLabels.getNameForId(1);
    assertEquals("b", labelNameNode2.getString());
    assertEquals(newLabelName2, labelNameNode2.getString());
    assertFalse(newLabelName1.equals(newLabelName2));
  }

  @Test
  public void testProcessLabels_labelReferenced_nameUnchanged() {
    Node labelNode = Node.createLabel("a");
    Node labelNameNode = labelNode.getFirstChild();
    node.addChild(labelNode);

    Node breakNode = Node.createBreak();
    Node breakNameNode = breakNode.getFirstChild();
    Node labelNameNodeRef = Node.createString("a");
    breakNameNode.addChild(labelNameNodeRef);
    node.addChild(breakNode);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, node, null);
    processLabels.process(nodeTraversal);

    assertEquals(node, nodeTraversal.root);
    assertSame(labelNameNode, labelNameNodeRef);
  }

  @Test
  public void testProcessLabels_unreferencedLabelRemoved() {
    Node labelNode = Node.createLabel("a");
    Node labelNameNode = labelNode.getFirstChild();
    node.addChild(labelNode);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, node, null);
    processLabels.process(nodeTraversal);

    assertEquals(node, nodeTraversal.root);
    assertNull(nodeTraversal.getRoot().getLastChild());
  }

  @Test
  public void testProcessLabels_labelsWithinBlock_namesChanged() {
    Node labelNode1 = Node.createLabel("a");
    Node labelNameNode1 = labelNode1.getFirstChild();
    Node breakNode1 = Node.createBreak();
    Node labelNameNodeRef1 = Node.createString("a");
    breakNode1.addChild(labelNameNodeRef1);
    Node blockNode1 = Node.createBlock();
    blockNode1.addChild(labelNode1);
    blockNode1.addChild(breakNode1);

    Node labelNode2 = Node.createLabel("a");
    Node labelNameNode2 = labelNode2.getFirstChild();
    Node breakNode2 = Node.createBreak();
    Node labelNameNodeRef2 = Node.createString("a");
    breakNode2.addChild(labelNameNodeRef2);
    Node blockNode2 = Node.createBlock();
    blockNode2.addChild(labelNode2);
    blockNode2.addChild(breakNode2);

    node.addChild(blockNode1);
    node.addChild(blockNode2);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, node, null);
    processLabels.process(nodeTraversal);

    assertEquals(node, nodeTraversal.root);

    Map<String, LabelInfo> renameMap = processLabels.namespaceStack.peek().renameMap;
    assertEquals(2, renameMap.size());

    LabelInfo labelInfo1 = renameMap.get("a");
    LabelInfo labelInfo2 = renameMap.get("a");
    assertFalse(labelInfo1.equals(labelInfo2));

    String newLabelName1 = processLabels.getNameForId(1);
    assertEquals("a", labelNameNode1.getString());
    assertEquals(newLabelName1, labelNameNode1.getString());

    String newLabelName2 = processLabels.getNameForId(2);
    assertEquals("b", labelNameNode2.getString());
    assertEquals(newLabelName2, labelNameNode2.getString());
    assertFalse(newLabelName1.equals(newLabelName2));
  }
}