import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckSideEffectsTest {
  @Test
  public void testEmptyStatement() {
    // Given an empty statement
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = Node.newEmpty();

    // When the checkSideEffects is processed
    checkSideEffects.process(null, root);

    // Then there should be no warning
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testVoidStatement() {
    // Given a void statement
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = Node.newVoid();

    // When the checkSideEffects is processed
    checkSideEffects.process(null, root);

    // Then there should be no warning
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testStatementWithSideEffects() {
    // Given a statement with side effects
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = Node.newExpression(Node.NAME, "document.write", Node.newString("Hello World"));

    // When the checkSideEffects is processed
    checkSideEffects.process(null, root);

    // Then there should be a warning
    assertEquals(1, checkSideEffects.problemNodes.size());
  }

  @Test(expected = AssertionError.class)
  public void testProtectedStatement() {
    // Given a statement protected by the CheckSideEffects compiler pass
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    Node root = Node.newExpression(Node.NAME, "JSCOMPILER_PRESERVE", Node.newString("Hello World"));

    // When the checkSideEffects is processed
    checkSideEffects.process(null, root);

    // Then a warning should not be thrown
  }
}