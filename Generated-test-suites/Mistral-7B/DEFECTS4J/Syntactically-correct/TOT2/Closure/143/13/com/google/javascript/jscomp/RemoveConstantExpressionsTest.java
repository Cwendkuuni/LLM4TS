import static org.junit.Assert.assertNotNull;

public class RemoveConstantExpressionsTest {

  @Test
  public void testProcess() {
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(new Compiler());
    Node node = new Node(Token.PROGRAM);

    removeConstantExpressions.process(new Node(Token.EMPTY), node);
    assertNotNull(node);
  }
}