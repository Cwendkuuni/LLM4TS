import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;

public class ClosureReverseAbstractInterpreterTest {
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    // Test code here
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    // Test code here
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject() {
    // Test code here
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"),
        new Node(STRING, "isNull")), new Node(NAME, "someVariable"));
    FlowScope blindScope = new FlowScope();

    // Test when isNull returns true
    JSType expectedType = interpreter.getNativeType(JSType.NULL_TYPE);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNotNull(actualType);
    assertNotNull(actualType.getSubtype(expectedType));

    // Test when isNull returns false
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNull(actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefAndNotNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"),
        new Node(STRING, "isDefAndNotNull")), new Node(NAME, "someVariable"));
    FlowScope blindScope = new FlowScope();

    // Test when isDefAndNotNull returns true
    JSType expectedType = interpreter.getNativeType(JSType.BOOLEAN);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNotNull(actualType);
    assertNotNull(actualType.getSubtype(expectedType));

    // Test when isDefAndNotNull returns false
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNull(actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"),
        new Node(STRING, "isString")), new Node(NAME, "someVariable"));
    FlowScope blindScope = new FlowScope();

    // Test when isString returns true
    JSType expectedType = interpreter.getNativeType(JSType.STRING);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNotNull(actualType);
    assertNotNull(actualType.getSubtype(expectedType));

    // Test when isString returns false
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNull(actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isBoolean() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"),
        new Node(STRING, "isBoolean")), new Node(NAME, "someVariable"));
    FlowScope blindScope = new FlowScope();

    // Test when isBoolean returns true
    JSType expectedType = interpreter.getNativeType(JSType.BOOLEAN);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNotNull(actualType);
    assertNotNull(actualType.getSubtype(expectedType));

    // Test when isBoolean returns false
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNull(actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isNumber() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"),
        new Node(STRING, "isNumber")), new Node(NAME, "someVariable"));
    FlowScope blindScope = new FlowScope();

    // Test when isNumber returns true
    JSType expectedType = interpreter.getNativeType(JSType.NUMBER);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNotNull(actualType);
    assertNotNull(actualType.getSubtype(expectedType));

    // Test when isNumber returns false
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNull(actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isFunction() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    Node condition = new Node(CALL, new Node(GETPROP, new Node(NAME, "goog"),
        new Node(STRING, "isFunction")), new Node(NAME, "someVariable"));
    FlowScope blindScope = new FlowScope();

    // Test when isFunction returns true
    JSType expectedType = interpreter.getNativeType(JSType.FUNCTION_TYPE);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNotNull(actualType);
    assertNotNull(actualType.getSubtype(expectedType));

    // Test when isFunction returns false
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariable("someVariable")
        .getDeclaredType();
    assertNull(actualType);
  }
}