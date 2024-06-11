import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;

public class ClosureReverseAbstractInterpreterTest {

  @org.junit.Before
  public void setup() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);
    this.interpreter = interpreter;
  }

  @org.junit.Test
  public void testIsDef() {
    Node condition = createNode("goog.isDef(x)");
    JSType originalType = JSType.OBJECT;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = interpreter.getNativeType(OBJECT_TYPE);
    JSType actualType = informedScope.getVariableType("x");
    assertEquals("Expected 'x' to be defined, but it was not.", expectedType, actualType);
  }

  @org.junit.Test
  public void testIsDefFalse() {
    Node condition = createNode("goog.isDef(x)");
    JSType originalType = JSType.NULL;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariableType("x");
    assertNull("Expected 'x' to not be defined, but it was.", actualType);
  }

  @org.junit.Test
  public void testIsDefUndefined() {
    Node condition = createNode("goog.isDef(x)");
    JSType originalType = JSType.UNDEFINED;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariableType("x");
    assertNull("Expected 'x' to be undefined, but it was not.", actualType);
  }

  @org.junit.Test
  public void testIsDefFunction() {
    Node condition = createNode("goog.isDef(x)");
    JSType originalType = JSType.FUNCTION;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariableType("x");
    assertNull("Expected 'x' to not be a variable, but it was.", actualType);
  }

  @org.junit.Test
  public void testIsArrayTrue() {
    Node condition = createNode("goog.isArray(x)");
    JSType originalType = JSType.ARRAY;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = JSType.ARRAY;
    JSType actualType = informedScope.getVariableType("x");
    assertEquals("Expected 'x' to be an array, but it was not.", expectedType, actualType);
  }

  @org.junit.Test
  public void testIsArrayFalse() {
    Node condition = createNode("goog.isArray(x)");
    JSType originalType = JSType.OBJECT;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariableType("x");
    assertNull("Expected 'x' to not be an array, but it was.", actualType);
  }

  @org.junit.Test
  public void testIsObjectTrue() {
    Node condition = createNode("goog.isObject(x)");
    JSType originalType = JSType.OBJECT;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = JSType.OBJECT;
    JSType actualType = informedScope.getVariableType("x");
    assertEquals("Expected 'x' to be an object, but it was not.", expectedType, actualType);
  }

  @org.junit.Test
  public void testIsObjectFalse() {
    Node condition = createNode("goog.isObject(x)");
    JSType originalType = JSType.ARRAY;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariableType("x");
    assertNull("Expected 'x' to not be an object, but it was.", actualType);
  }

  @org.junit.Test
  public void testIsString() {
    Node condition = createNode("goog.isString(x)");
    JSType originalType = JSType.OBJECT;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = JSType.STRING;
    JSType actualType = informedScope.getVariableType("x");
    assertEquals("Expected 'x' to be a string, but it was not.", expectedType, actualType);
  }

  @org.junit.Test
  public void testIsStringFalse() {
    Node condition = createNode("goog.isString(x)");
    JSType originalType = JSType.NUMBER;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariableType("x");
    assertNull("Expected 'x' to not be a string, but it was.", actualType);
  }

  @org.junit.Test
  public void testIsBoolean() {
    Node condition = createNode("goog.isBoolean(x)");
    JSType originalType = JSType.OBJECT;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = JSType.BOOLEAN;
    JSType actualType = informedScope.getVariableType("x");
    assertEquals("Expected 'x' to be a boolean, but it was not.", expectedType, actualType);
  }

  @org.junit.Test
  public void testIsBooleanFalse() {
    Node condition = createNode("goog.isBoolean(x)");
    JSType originalType = JSType.STRING;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariableType("x");
    assertNull("Expected 'x' to not be a boolean, but it was.", actualType);
  }

  @org.junit.Test
  public void testIsNumber() {
    Node condition = createNode("goog.isNumber(x)");
    JSType originalType = JSType.OBJECT;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = JSType.NUMBER;
    JSType actualType = informedScope.getVariableType("x");
    assertEquals("Expected 'x' to be a number, but it was not.", expectedType, actualType);
  }

  @org.junit.Test
  public void testIsNumberFalse() {
    Node condition = createNode("goog.isNumber(x)");
    JSType originalType = JSType.STRING;
    FlowScope blindScope = new FlowScope(originalType);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = null;
    JSType actualType = informedScope.getVariableType("x");
    assertNull("Expected 'x' to not be a number, but it was.", actualType);
  }

  private Node createNode(String nodeString) {
    return new Node(new org.mozilla.javascript.ast.CallNode(
        new org.mozilla.javascript.ast.IdentifierNode("goog"), new org.mozilla.javascript.ast.CallNode(
        new org.mozilla.javascript.ast.IdentifierNode(nodeString.substring(1)), new org.mozilla.javascript.ast.ArrayLiteralNode())));
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String expectedTypeString,
      boolean expectedOutcome) {
    TypeRestriction restriction = new TypeRestriction(type, expectedOutcome);
    JSType expectedType = interpreter.restricters.get("is" + expectedTypeString)
        .apply(restriction);
    return expectedType;
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    TypeRestriction restriction = new TypeRestriction(type, true);
    return interpreter.restricters.get("isNotNull")
        .apply(restriction);
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    TypeRestriction restriction = new TypeRestriction(type, true);
    return interpreter.restricters.get("isDef")
        .apply(restriction);
  }

  private JSType getRestrictedWithoutUndefinedOrNull(JSType type) {
    TypeRestriction restriction = new TypeRestriction(type, true);
    return interpreter.restricters.get("isDefAndNotNull")
        .apply(restriction);
  }

  private JSType getTypeIfRefinable(Node node, FlowScope scope) {
    JSType type = scope.getType(node);
    if (type != null) {
      return type;
    }
    return null;
  }
}