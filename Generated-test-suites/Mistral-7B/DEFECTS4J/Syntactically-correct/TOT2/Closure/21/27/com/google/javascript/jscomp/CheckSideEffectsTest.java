import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class CheckSideEffectsTest {
  @Test
  public void testCheckSideEffects() {
    // Given a CheckSideEffects object with default settings
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);

    // When checking a simple expression without side effects
    Node simpleExpression = IR.lit(1);
    Node result = checkSideEffects.process(null, simpleExpression);
    assertTrue(result.isNull());

    // Then no warning is issued
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Given a CheckSideEffects object with warning level set to ERROR
    checkSideEffects = new CheckSideEffects(null, CheckLevel.ERROR, false);

    // When checking a simple expression with a side effect
    Node sideEffectExpression = IR.assign(IR.name("x"), IR.lit(1));
    result = checkSideEffects.process(null, sideEffectExpression);

    // Then a warning is issued
    assertEquals(1, checkSideEffects.problemNodes.size());

    // Given a CheckSideEffects object with side effect free code protection enabled
    checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);

    // When checking a simple expression without side effects
    simpleExpression = IR.lit(1);
    result = checkSideEffects.process(null, simpleExpression);

    // Then no warning is issued and the expression is protected
    assertTrue(simpleExpression.isCall());
    assertTrue(simpleExpression.getLastChild().isName());
    assertEquals(PROTECTOR_FN, simpleExpression.getLastChild().getString());
    assertEquals(0, checkSideEffects.problemNodes.size());
  }
}