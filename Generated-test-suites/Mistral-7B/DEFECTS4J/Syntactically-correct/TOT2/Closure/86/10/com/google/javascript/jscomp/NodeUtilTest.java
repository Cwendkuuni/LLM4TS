import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = mock(Node.class);
    assertEquals(true, NodeUtil.isForIn(node)); // true case

    Node invalidNode = mock(Node.class);
    assertEquals(false, NodeUtil.isForIn(invalidNode)); // false case
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = mock(Node.class);
    Node expressionNode = mock(Node.class);

    Node parentStatementNode = mock(Node.class);
    Node parentExpressionNode = mock(Node.class);

    Node scriptNode = mock(Node.class);
    Node blockNode = mock(Node.class);
    Node labelNode = mock(Node.class);

    when(statementNode.getParent()).thenReturn(parentStatementNode);
    when(expressionNode.getParent()).thenReturn(parentExpressionNode);
    when(parentStatementNode.getType()).thenReturn(Token.SCRIPT);
    when(parentExpressionNode.getType()).thenReturn(Token.BLOCK);
    when(parentLabelNode.getType()).thenReturn(Token.LABEL);

    assertEquals(true, NodeUtil.isStatement(statementNode)); // true case for statement
    assertEquals(false, NodeUtil.isStatement(expressionNode)); // false case for expression
    assertEquals(true, NodeUtil.isStatement(labelNode)); // true case for label

    Node functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(Token.FUNCTION);

    assertEquals(false, NodeUtil.isStatement(functionNode)); // false case for function node
  }

  // Similarly, generate test cases for isFunctionDeclaration and isHoistedFunctionDeclaration
  // methods.
}