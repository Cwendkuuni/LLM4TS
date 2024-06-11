import static org.junit.Assert.assertTrue;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node nodeForIn = new NodeToken(Token.FOR, "for");
    Node nodeForInWithThreeChildren = new NodeList(nodeForIn, new NodeToken(Token.IDENT, "i"), new NodeList(new NodeToken(Token.ASSIGN, "="), new NodeToken(Token.IDENT, "A"), new NodeList(new NodeToken(Token.IDENT, "in"), new NodeToken(Token.IDENT, "B"))));

    // Test case for true condition
    assertTrue(NodeUtil.isForIn(nodeForInWithThreeChildren));

    // Test case for false condition
    Node nodeNotForIn = new NodeToken(Token.IF, "if");
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }
}