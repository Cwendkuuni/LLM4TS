import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node1 = Node.newNode(Token.FOR, new int[]{}, new Node[3]);
    Node node2 = Node.newNode(Token.IF, new int[]{}, new Node[0]);

    assertTrue(isForIn(node1));
    assertFalse(isForIn(node2));
  }

  @Test
  public void testIsStatement() {
    Node node1 = Node.newNode(Token.BLOCK, new int[]{}, new Node[0]);
    Node node2 = Node.newNode(Token.FUNCTION, new int[]{}, new Node[0]);

    // It is not possible to determine definitely if a node is a statement or
    // not if it is not part of the AST. A FUNCTION node can be either part of
    // an expression or a statement.
    Preconditions.checkState(node1.getParent() != null);
    assertTrue(isStatement(node1));

    Preconditions.checkState(node2.getParent() != null);
    Node parentNode1 = node2.getParent();

    // Statement
    Preconditions.checkState(parentNode1.getType() == Token.SCRIPT
        || parentNode1.getType() == Token.BLOCK
        || parentNode1.getType() == Token.LABEL);
    assertTrue(isStatement(node2));

    // Expression
    Node parentNode2 = Node.newNode(Token.ASSIGN, new int[]{}, new Node[] { node2 });
    Preconditions.checkState(parentNode2.getParent() != null);
    assertFalse(isStatement(node2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node1 = Node.newNode(Token.FUNCTION, new int[]{}, new Node[0]);
    Node node2 = Node.newNode(Token.FUNCTION, new int[] { Token.NAME, 1 }, new Node[0]);

    assertTrue(isFunctionDeclaration(node1));
    assertTrue(isFunctionDeclaration(node2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node1 = Node.newNode(Token.FUNCTION, new int[]{}, new Node[0]);
    Node node2 = Node.newNode(Token.FUNCTION, new int[] { Token.NAME, 1 }, new Node[0]);

    Node functionNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[] { node1, node2 });
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[0]);

    Node functionDeclarationParentNode1 = node1.getParent();
    Node functionDeclarationParentNode2 = node2.getParent();

    // Hoisted function declaration
    assertTrue(isHoistedFunctionDeclaration(node1));
    assertTrue(isHoistedFunctionDeclaration(node2));

    // Not hoisted function declaration
    assertFalse(isHoistedFunctionDeclaration(functionNode));

    // Function declaration inside script
    Node functionDeclarationScriptParentNode = functionNode.getParent();
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationScriptParentNode));

    // Function declaration inside function
    Node functionDeclarationFunctionParentNode = functionDeclarationParentNode1;
    Node functionNodeFunctionParentNode = functionDeclarationParentNode2.getParent();

    assertTrue(isHoistedFunctionDeclaration(functionNodeFunctionParentNode));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationFunctionParentNode));

    // Function declaration inside block
    Node functionDeclarationBlockParentNode = functionDeclarationFunctionParentNode.getParent();
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationBlockParentNode));

    // Function declaration inside script (without function node)
    Node functionDeclarationScriptParentNode2 = scriptNode.getChild(0).getParent();
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationScriptParentNode2));
  }
}