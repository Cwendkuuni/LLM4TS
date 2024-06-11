import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    // Given
    Node condition = new Node(CALL, new Node[]{
      new Node(GETPROP, new Node[]{"goog", "isDef"}),
      new Node(STRING, "someVar")
    });
    FlowScope blindScope = new FlowScope();
    JSType expectedType = getNativeType(JSTypeNative.BOOLEAN_TYPE);

    // When
    FlowScope informedScope = getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Then
    JSType actualType = informedScope.variables.get("someVar").type;
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_notIsDef() {
    // Given
    Node condition = new Node(CALL, new Node[]{
      new Node(GETPROP, new Node[]{"goog", "isDef"}),
      new Node(STRING, "someNonExistentVar")
    });
    FlowScope blindScope = new FlowScope();

    // When
    FlowScope informedScope = getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    // Then
    assertTrue(informedScope.variables.isEmpty());
  }
}