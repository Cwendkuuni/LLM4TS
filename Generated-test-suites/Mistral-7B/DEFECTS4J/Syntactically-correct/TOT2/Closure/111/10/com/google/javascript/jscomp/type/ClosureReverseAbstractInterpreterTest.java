import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.FlowScope;
import com.google.javascript.jscomp.Node;

public class ClosureReverseAbstractInterpreterTest {
  @org.junit.Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), null);
    Node condition = mock(Node.class);
    FlowScope blindScope = mock(FlowScope.class);
    boolean outcome = true;

    when(condition.isCall()).thenReturn(true);
    when(condition.getChildCount()).thenReturn(2);
    when(condition.getFirstChild().isName()).thenReturn(true);
    when(condition.getFirstChild().getString()).thenReturn("goog");
    when(condition.getLastChild().isString()).thenReturn(true);
    when(condition.getLastChild().getString()).thenReturn("isDef");

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);

    assertNotNull(result);
  }
}