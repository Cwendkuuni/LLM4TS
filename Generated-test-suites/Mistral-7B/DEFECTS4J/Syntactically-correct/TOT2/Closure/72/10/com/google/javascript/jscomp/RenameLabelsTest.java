import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static com.google.javascript.jscomp.RenameLabels.LabelNamespace.LabelInfo.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class RenameLabelsTest {
  @Test
  public void testRenameLabels() {
    String source = "function test() {"
        + "  a: { break a; }"
        + "  b: { break a; }"
        + "  c: { continue a; }"
        + "  d: { continue b; }"
        + "  e: { continue c; }"
        + "}";

    Processor processor = new Processor(new Compiler());
    RenameLabels renameLabels = new RenameLabels(processor);

    Node root = NodeFactory.createProgram(null, source, null);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, renameLabels);
    nodeTraversal.traverse();

    Map<String, LabelInfo> labelInfoMap = nodeTraversal.getState(RenameLabels.ProcessLabels.class)
        .namespaceStack.peek().renameMap;

    LabelInfo labelA = labelInfoMap.get("a");
    LabelInfo labelB = labelInfoMap.get("b");
    LabelInfo labelC = labelInfoMap.get("c");
    LabelInfo labelD = labelInfoMap.get("d");
    LabelInfo labelE = labelInfoMap.get("e");
    assertNull(labelInfoMap.get("test")); // test is not a label

    assertEquals(ID_1, labelA.id);
    assertEquals(ID_1, labelB.id);
    assertEquals(ID_1, labelC.id);
    assertEquals(ID_2, labelD.id);
    assertEquals(ID_2, labelE.id);

    Node labelANode = root.lookupField("test", "a");
    assertEquals("a_1", labelANode.getString());

    Node labelBNode = root.lookupField("test", "b");
    assertEquals("b_1", labelBNode.getString());

    Node labelCNode = root.lookupField("test", "c");
    assertEquals("c_1", labelCNode.getString());

    Node labelDNode = root.lookupField("test", "d");
    assertEquals("d_2", labelDNode.getString());

    Node labelENode = root.lookupField("test", "e");
    assertEquals("e_2", labelENode.getString());
  }

  @Test
  public void testRemoveUnusedLabel() {
    String source = "function test() {"
        + "  a: { break a; }"
        + "  b: { break a; }"
        + "  c: { continue a; }"
        + "  d: { continue b; }"
        + "  e: { continue c; }"
        + "  f: { break a; }" // unused label
        + "}";

    Processor processor = new Processor(new Compiler());
    RenameLabels renameLabels = new RenameLabels(processor, new DefaultNameSupplier(), false);

    Node root = NodeFactory.createProgram(null, source, null);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, renameLabels);
    nodeTraversal.traverse();

    Map<String, LabelInfo> labelInfoMap = nodeTraversal.getState(RenameLabels.ProcessLabels.class)
        .namespaceStack.peek().renameMap;

    LabelInfo labelA = labelInfoMap.get("a");
    LabelInfo labelB = labelInfoMap.get("b");
    LabelInfo labelC = labelInfoMap.get("c");
    LabelInfo labelD = labelInfoMap.get("d");
    LabelInfo labelE = labelInfoMap.get("e");
    LabelInfo labelF = labelInfoMap.get("f");

    assertNull(labelF);
  }

  @Test
  public void testLabelWithKeyword() {
    String source = "function test() {"
        + "  break: { break break; }" // label with keyword
        + "  continue: { continue continue; }" // label with keyword
        + "}";

    Processor processor = new Processor(new Compiler());
    RenameLabels renameLabels = new RenameLabels(processor);

    Node root = NodeFactory.createProgram(null, source, null);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, renameLabels);
    nodeTraversal.traverse();

    Map<String, LabelInfo> labelInfoMap = nodeTraversal.getState(RenameLabels.ProcessLabels.class)
        .namespaceStack.peek().renameMap;

    LabelInfo labelBreak = labelInfoMap.get("break");
    LabelInfo labelContinue = labelInfoMap.get("continue");

    assertNull(labelBreak);
    assertNull(labelContinue);

    assertEquals(1, nodeTraversal.getState(RenameLabels.ProcessLabels.class).namespaceStack.size());
  }
}