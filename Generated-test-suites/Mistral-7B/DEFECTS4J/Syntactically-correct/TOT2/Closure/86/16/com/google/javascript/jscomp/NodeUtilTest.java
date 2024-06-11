import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @org.junit.Test
  public void testIsForIn() {
    Node node = mock(Node.class);
    Preconditions.checkState(node != null);
    node.setType(Token.FOR);
    node.setChildCount(3);
    assertTrue(NodeUtil.isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = mock(Node.class);
    Preconditions.checkState(node != null);
    Node parent = mock(Node.class);
    Preconditions.checkState(parent != null);
    node.setParent(parent);
    parent.setType(Token.SCRIPT); // or BLOCK or LABEL
    assertTrue(NodeUtil.isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = mock(Node.class);
    Preconditions.checkState(node != null);
    node.setType(Token.FUNCTION);
    Node statementParent = mock(Node.class);
    Preconditions.checkState(statementParent != null);
    node.setParent(statementParent);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = mock(Node.class);
    Preconditions.checkState(node != null);
    Node functionParent = mock(Node.class);
    Preconditions.checkState(functionParent != null);
    Node scriptParent = mock(Node.class);
    Preconditions.checkState(scriptParent != null);
    node.setType(Token.FUNCTION);
    node.setParent(statementParent);
    statementParent.setParent(scriptParent);
    statementParent.setType(Token.SCRIPT); // or FUNCTION
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}