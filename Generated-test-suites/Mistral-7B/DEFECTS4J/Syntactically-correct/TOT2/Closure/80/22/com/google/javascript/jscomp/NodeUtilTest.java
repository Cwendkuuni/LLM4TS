import org.junit.Test;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static org.junit.Assert.assertEquals;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = createNode(Token.FOR, null, createToken(Token.IDENTIFIER, "for"), null, createNode(Token.IDENTIFIER, "in"), null, null, null);
    assertEquals(true, NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_False() {
    Node n = createNode(Token.IF, null, null, null, null, null, null, null);
    assertEquals(false, NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = createNode(Token.IF, null, null, null, createNode(Token.IDENTIFIER, "statement"), null, null, null);
    assertEquals(true, NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_False() {
    Node n = createNode(Token.IDENTIFIER, "expression");
    assertEquals(false, NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = createNode(Token.FUNCTION, null, createNode(Token.IDENTIFIER, "func"), null, null, null, null, null);
    assertEquals(true, NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node n = createNode(Token.FUNCTION, null, null, null, null, null, null, null);
    assertEquals(false, NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = createNode(Token.SCRIPT, null, null, null);
    Node node = createNode(Token.FUNCTION, null, createNode(Token.IDENTIFIER, "func"), null, null, null, null, null);
    script.addChild(node);
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node function = createNode(Token.FUNCTION, null, null, null, null, null, null, null);
    Node block = createNode(Token.BLOCK, null, null, null);
    block.addChild(function);
    Node script = createNode(Token.SCRIPT, null, null, null);
    script.addChild(block);
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(function));
  }
}