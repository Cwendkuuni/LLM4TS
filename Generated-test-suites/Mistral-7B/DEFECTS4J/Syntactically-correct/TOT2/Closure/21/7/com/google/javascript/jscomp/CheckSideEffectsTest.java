import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffects() {
    // given
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node rootNode = createTestNode();

    // when
    checkSideEffects.process(null, rootNode);

    // then
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsWithWarning() {
    // given
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node rootNode = createTestNodeWithUselessCode();

    // when
    checkSideEffects.process(null, rootNode);

    // then
    assertEquals(1, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsWithError() {
    // given
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.ERROR, false);
    Node rootNode = createTestNodeWithUselessCode();

    // when
    try {
      checkSideEffects.process(null, rootNode);
      fail("Expected an error");
    } catch (RuntimeException e) {
      // then
      assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", e.getMessage());
    }
  }

  @Test
  public void testCheckSideEffectsWithProtectedCode() {
    // given
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    Node rootNode = createTestNodeWithProtectedCode();

    // when
    checkSideEffects.process(null, rootNode);

    // then
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  private Node createTestNode() {
    // ... implementation
  }

  private Node createTestNodeWithUselessCode() {
    // ... implementation
  }

  private Node createTestNodeWithProtectedCode() {
    // ... implementation
  }
}