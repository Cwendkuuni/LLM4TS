import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    Node nodeFor = new Node(Token.FOR);
    Node nodeIf = new Node(Token.IF);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node statement = new Node(Token.SCRIPT);
    Node expression = new Node(Token.IDENTIFIER, "expression");

    assertTrue(NodeUtil.isStatement(statement));
    assertFalse(NodeUtil.isStatement(expression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "functionName"));
    Node functionExpression = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "functionName"));
    Node statement = new Node(Token.SCRIPT);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(statement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "functionName"), null);
    Node nonHoistedFunctionDeclaration = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "functionName"), null);
    Node script = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION, new Node(Token.IDENTIFIER, "outerFunctionName"), null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(script));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
  }
}