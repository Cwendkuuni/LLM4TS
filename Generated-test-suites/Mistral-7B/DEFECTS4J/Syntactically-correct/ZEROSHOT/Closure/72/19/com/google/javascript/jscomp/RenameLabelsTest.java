import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ProcessNode;
import com.google.javascript.rhino.Block;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private static <T> T mockNode(int type) {
    return (T) NodeFactory.createNodeFromString("{}", type);
  }

  @SuppressWarnings("unchecked")
  private static Node mockLabelNode() {
    Node labelNode = mockNode(Token.LABEL);
    Node labelNameNode = mockNode(Token.IDENT);
    labelNameNode.setString("oldLabelName");
    labelNode.addChild(labelNameNode);
    return labelNode;
  }

  @SuppressWarnings("unchecked")
  private static Node mockBreakNode() {
    Node breakNode = mockNode(Token.BREAK);
    Node labelNameNode = mockNode(Token.IDENT);
    labelNameNode.setString("oldLabelName");
    breakNode.addChild(labelNameNode);
    return breakNode;
  }

  @SuppressWarnings("unchecked")
  private static Node mockContinueNode() {
    Node continueNode = mockNode(Token.CONTINUE);
    Node labelNameNode = mockNode(Token.IDENT);
    labelNameNode.setString("oldLabelName");
    continueNode.addChild(labelNameNode);
    return continueNode;
  }

  @SuppressWarnings("unchecked")
  private static Node mockBlockNode() {
    Node blockNode = NodeFactory.createBlock(1);
    return blockNode;
  }

  private static NodeFactory nodeFactory = NodeFactory.getInitial();
  private static CompilerOptions options = new CompilerOptions();
  private static AbstractCompiler compiler = mock(AbstractCompiler.class);

  @org.junit.Test
  public void testRenameLabels() {
    // Given
    when(compiler.addToDebugLog(anyString())).thenReturn(null);
    when(compiler.reportCodeChange()).thenReturn(null);

    String oldSourceCode =
        "function foo() {\n"
            + "  a: {\n"
            + "    b: {\n"
            + "      break a;\n"
            + "    }\n"
            + "  }\n"
            + "  a: {\n"
            + "    continue a;\n"
            + "  }\n"
            + "}";
    Node oldRootNode = nodeFactory.parseTree(oldSourceCode, options);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, oldRootNode, null);
    RenameLabels renameLabels = new RenameLabels(compiler);

    // When
    renameLabels.process(null, oldRootNode);

    // Then
    ProcessNode processNode = nodeTraversal.getProcessor();
    Node newRootNode = processNode.processNode(oldRootNode);

    // Check the labels have been renamed
    assertEquals("a", getLabelName(newRootNode, 0));
    assertEquals("b", getLabelName(newRootNode, 1));

    // Check the break node has been updated
    Node breakNode = findBreakNode(newRootNode);
    assertEquals("a", getLabelName(breakNode, 0));

    // Check the continue node has been updated
    Node continueNode = findContinueNode(newRootNode);
    assertEquals("a", getLabelName(continueNode, 0));
  }

  private static String getLabelName(Node node, int index) {
    Node labelNameNode = findLabelNameNode(node, index);
    return labelNameNode.getString();
  }

  private static Node findLabelNameNode(Node node, int index) {
    if (node.getType() == Token.LABEL && index == 0) {
      return node.getFirstChild();
    }
    for (int i = 0; i < node.getNumberOfChildren(); i++) {
      Node child = node.getChild(i);
      if (child.getType() == Token.LABEL) {
        if (index + 1 == getLabelChildCount(child)) {
          return findLabelNameNode(child, index + 1);
        }
      }
      Node result = findLabelNameNode(child, index);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private static int getLabelChildCount(Node node) {
    if (node.getType() == Token.LABEL) {
      return 1 + node.getNumberOfChildren();
    }
    return 0;
  }

  private static Node findBreakNode(Node node) {
    if (node.getType() == Token.BREAK) {
      return node;
    }
    for (int i = 0; i < node.getNumberOfChildren(); i++) {
      Node child = node.getChild(i);
      if (child.getType() == Token.BREAK) {
        return child;
      }
      Node result = findBreakNode(child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private static Node findContinueNode(Node node) {
    if (node.getType() == Token.CONTINUE) {
      return node;
    }
    for (int i = 0; i < node.getNumberOfChildren(); i++) {
      Node child = node.getChild(i);
      if (child.getType() == Token.CONTINUE) {
        return child;
      }
      Node result = findContinueNode(child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}