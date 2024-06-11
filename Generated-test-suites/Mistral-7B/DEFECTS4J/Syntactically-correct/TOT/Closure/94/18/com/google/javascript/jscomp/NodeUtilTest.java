import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newNode(Token.IDENT, new int[] {"expr"});
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunction(Token.FUNCTION, new int[] {0},
        Node.newNodeList(), Node.newIdentifier("funcName"), Node.newList(),
        Node.newBlock(new int[] {1, 2, 3}));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = Node.newFunction(Token.FUNCTION, new int[] {0},
        Node.newNodeList(), Node.newIdentifier("funcName"), Node.newList(),
        Node.newList(new int[] {1, 2, 3}));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newBlock(new int[] {1, 2, 3});
    Node functionDeclaration = Node.newFunction(Token.FUNCTION, new int[] {0},
        Node.newNodeList(), Node.newIdentifier("funcName"), Node.newList(),
        Node.newBlock(new int[] {1, 2, 3}));
    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0},
        Node.newNodeList(), Node.newIdentifier("funcName"), Node.newList(),
        scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node functionDeclarationInFunction = Node.newFunction(Token.FUNCTION, new int[] {0},
        Node.newNodeList(), Node.newIdentifier("funcName"), Node.newList(),
        Node.newBlock(new int[] {1, 2, 3}));
    Node hoistedFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {0},
        Node.newNodeList(), Node.newIdentifier("funcName"), Node.newList(),
        Node.newFunction(Token.FUNCTION, new int[] {0}, Node.newNodeList(),
            Node.newIdentifier("outerFuncName"), Node.newList(),
            Node.newBlock(new int[] {1, 2, 3})));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunction));
  }

  @Test
  public void testIsStatementWithNullParent() {
    Node statementNode = Node.newNode(Token.IF, new int[] {1, 2, 3});
    Node nullParentNode = null;
    Preconditions.checkArgument(NodeUtil.isStatement(statementNode),
        "Expected statement node to be a statement.");
    Preconditions.checkState(statementNode.getParent() != nullParentNode,
        "Expected statement node to have a parent.");
  }
}