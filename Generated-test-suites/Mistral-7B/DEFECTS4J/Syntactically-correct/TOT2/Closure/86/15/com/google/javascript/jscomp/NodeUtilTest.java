import static org.junit.Assert.assertEquals;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    Node loopNode = Node.newNode(Token.FOR, new Node[] { Node.newText("i"), Node.newIdentifier("arr"), Node.newIdentifier("arr[i]") });
    Node notForNode = Node.newNode(Token.IF);

    assertEquals(true, NodeUtil.isForIn(forNode));
    assertEquals(false, NodeUtil.isForIn(notForNode));
    assertEquals(false, NodeUtil.isForIn(loopNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, Node.newBoolean(true));
    Node expressionNode = Node.newNode(Token.IF, Node.newBoolean(true), new Node[] { Node.newNumber(1) });

    assertEquals(true, NodeUtil.isStatement(statementNode));
    assertEquals(false, NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, new Node[] { Node.newIdentifier("funcName") }, Node.newBlock(Node.newReturn(Node.newNumber(1))));
    Node functionExpressionNode = Node.newNode(Token.FUNCTION, new Node[] { Node.newIdentifier("funcName"), Node.newArguments() }, Node.newBlock(Node.newReturn(Node.newNumber(1))));

    assertEquals(true, NodeUtil.isFunctionDeclaration(functionNode));
    assertEquals(false, NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new Node[] { Node.newFunction("main", new Node[] {}, Node.newBlock(Node.newReturn(Node.newNumber(1)))), functionNode });
    Node functionNodeInFunction = Node.newNode(Token.FUNCTION, new Node[] { Node.newIdentifier("funcName") }, Node.newBlock(Node.newReturn(Node.newNumber(1))));

    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(functionNode));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
  }
}