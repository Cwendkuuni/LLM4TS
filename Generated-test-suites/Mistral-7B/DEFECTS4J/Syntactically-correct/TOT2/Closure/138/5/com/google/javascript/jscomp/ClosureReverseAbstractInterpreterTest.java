import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.TypeRestriction;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.JSType;
import static com.google.javascript.rhino.types.JSTypeFactory.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSType type = booleanType();
    Node condition = makeCallNode("goog.isDef", makeStringNode("x"), type);
    TypeRestriction restriction = new TypeRestriction(type, true);
    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertEquals(interpreter.getNativeType(ObjectType.class), scope.getDeclaredType("x"));
  }

  @Test
  public void testIsDefFalse() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSType type = booleanType();
    Node condition = makeCallNode("goog.isDef", null, type);
    TypeRestriction restriction = new TypeRestriction(type, false);
    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(scope.getDeclaredType("x"));
  }

  @Test
  public void testIsDefNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSType type = booleanType();
    Node condition = makeCallNode("goog.isDef", null, type);
    TypeRestriction restriction = new TypeRestriction(type, null);
    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(scope);
  }

  // Add other test cases for other methods like isNull, isDefAndNotNull, isString, etc.

  // Test edge cases and exception handling scenarios
  // ...

  // Merge all individual test cases into a complete JUnit 4 test file
  // ...
}