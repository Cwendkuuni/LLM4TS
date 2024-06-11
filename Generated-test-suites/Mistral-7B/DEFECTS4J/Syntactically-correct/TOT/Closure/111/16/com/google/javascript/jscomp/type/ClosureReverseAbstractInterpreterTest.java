import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  @ org.junit.Before
  public void setUp() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention() {
      @Override
      public JSType getNativeType(String typeName) {
        return typeRegistry.getNativeType(typeName);
      }
    };
    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);
  }

  /**
   * Test for isDef method with true outcome.
   */
  @org.junit.Test
  public void testIsDefTrue() {
    Node condition = createNode("goog.isDef(someVar)");
    JSType expectedType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isDef method with false outcome.
   */
  @org.junit.Test
  public void testIsDefFalse() {
    Node condition = createNode("goog.isDef(someUndefinedVar)");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isNull method with true outcome.
   */
  @org.junit.Test
  public void testIsNullTrue() {
    Node condition = createNode("goog.isNull(nullVar)");
    JSType expectedType = getNativeType(NULL_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isNull method with false outcome.
   */
  @org.junit.Test
  public void testIsNullFalse() {
    Node condition = createNode("goog.isNull(someVar)");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isDefAndNotNull method with true outcome.
   */
  @org.junit.Test
  public void testIsDefAndNotNullTrue() {
    Node condition = createNode("goog.isDefAndNotNull(someVar)");
    JSType expectedType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isDefAndNotNull method with false outcome.
   */
  @org.junit.Test
  public void testIsDefAndNotNullFalse() {
    Node condition = createNode("goog.isDefAndNotNull(someUndefinedVar)");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isString method with true outcome.
   */
  @org.junit.Test
  public void testIsStringTrue() {
    Node condition = createNode("goog.isString('someString')");
    JSType expectedType = getNativeType(STRING_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isString method with false outcome.
   */
  @org.junit.Test
  public void testIsStringFalse() {
    Node condition = createNode("goog.isString(123)");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isBoolean method with true outcome.
   */
  @org.junit.Test
  public void testIsBooleanTrue() {
    Node condition = createNode("goog.isBoolean(true)");
    JSType expectedType = getNativeType(BOOLEAN_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isBoolean method with false outcome.
   */
  @org.junit.Test
  public void testIsBooleanFalse() {
    Node condition = createNode("goog.isBoolean(123)");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isNumber method with true outcome.
   */
  @org.junit.Test
  public void testIsNumberTrue() {
    Node condition = createNode("goog.isNumber(123)");
    JSType expectedType = getNativeType(NUMBER_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isNumber method with false outcome.
   */
  @org.junit.Test
  public void testIsNumberFalse() {
    Node condition = createNode("goog.isNumber('someString')");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isFunction method with true outcome.
   */
  @org.junit.Test
  public void testIsFunctionTrue() {
    Node condition = createNode("goog.isFunction(someFunction)");
    JSType expectedType = FunctionType.BYTE_FUNCTION_TYPE; // replace with actual function type
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isFunction method with false outcome.
   */
  @org.junit.Test
  public void testIsFunctionFalse() {
    Node condition = createNode("goog.isFunction('someString')");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isArray method with true outcome.
   */
  @org.junit.Test
  public void testIsArrayTrue() {
    Node condition = createNode("goog.isArray([1, 2, 3])");
    JSType expectedType = ARRAY_TYPE;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isArray method with false outcome.
   */
  @org.junit.Test
  public void testIsArrayFalse() {
    Node condition = createNode("goog.isArray('someString')");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isObject method with true outcome.
   */
  @org.junit.Test
  public void testIsObjectTrue() {
    Node condition = createNode("goog.isObject(someObject)");
    JSType expectedType = ObjectType.createObjectType();
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), true).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  /**
   * Test for isObject method with false outcome.
   */
  @org.junit.Test
  public void testIsObjectFalse() {
    Node condition = createNode("goog.isObject(123)");
    JSType expectedType = getNativeType(VOID_TYPE);
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new NullFlowScope(), false).getLastRegisteredType();
    assertEquals(expectedType, actualType);
  }

  private Node createNode(String nodeString) {
    Node node = new Node();
    node.setFirstChild(new Node(nodeString));
    node.setLastChild(null);
    node.setParent(null);
    return node;
  }
}