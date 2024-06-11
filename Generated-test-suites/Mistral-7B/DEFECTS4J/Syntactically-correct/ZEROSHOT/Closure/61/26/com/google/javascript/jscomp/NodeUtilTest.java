import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.newNode(Token.FOR, new int[]{}, new Node[]{}, null);
    assertTrue(NodeUtil.isForIn(node));

    node = Node.newNode(Token.IF, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    assertTrue(NodeUtil.isStatement(node.getFirstChild()));

    node = Node.newNode(Token.IF, new int[]{}, new Node[]{}, node);
    assertTrue(NodeUtil.isStatement(node.getFirstChild()));

    node = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    assertTrue(NodeUtil.isStatement(node));

    node = Node.newNode(Token.IF, new int[]{}, new Node[]{}, node);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    node.setName(new Node("myFunction", null));
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    Node functionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    functionNode.setName(new Node("myFunction", null));
    scriptNode.addChild(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

    Node blockNode = Node.newNode(Token.BLOCK, new int[]{}, new Node[]{}, scriptNode);
    Node innerFunctionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    innerFunctionNode.setName(new Node("myFunction", null));
    blockNode.addChild(innerFunctionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(innerFunctionNode));
  }
}