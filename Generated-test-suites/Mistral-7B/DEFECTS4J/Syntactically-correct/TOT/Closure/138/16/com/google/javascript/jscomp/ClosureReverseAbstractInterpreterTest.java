import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Type;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ChainableReverseAbstractInterpreter interpreter = new ChainableReverseAbstractInterpreter(
        convention, typeRegistry);
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = Node.newCall("goog.isDef", Node.newUndefined(),
        Node.newBoolean(true));
    Type expectedType = getNativeType(OBJECT_TYPE);
    FlowScope blindScope = new EmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    Type actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(expectedType, actualType);

    // Negative test case
    condition = Node.newCall("goog.isDef", Node.newUndefined(),
        Node.newBoolean(false));
    expectedType = null;
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertNull(actualType);

    // Error test case (goog.isDef called with wrong number of arguments)
    condition = Node.newCall("goog.isDef", Node.newUndefined(), Node.newInt(1));
    Type errorType = getNativeType(NULL_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(errorType, actualType);
  }

  @Test
  public void testIsDefAndNotNull() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ChainableReverseAbstractInterpreter interpreter = new ChainableReverseAbstractInterpreter(
        convention, typeRegistry);
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = Node.newCall("goog.isDefAndNotNull", Node.newNull(),
        Node.newBoolean(true));
    Type expectedType = getNativeType(OBJECT_TYPE);
    FlowScope blindScope = new EmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    Type actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(expectedType, actualType);

    // Negative test case
    condition = Node.newCall("goog.isDefAndNotNull", Node.newNull(),
        Node.newBoolean(false));
    expectedType = null;
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertNull(actualType);

    // Error test case (goog.isDefAndNotNull called with wrong number of arguments)
    condition = Node.newCall("goog.isDefAndNotNull", Node.newNull(), Node.newInt(1));
    Type errorType = getNativeType(NULL_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(errorType, actualType);
  }

  @Test
  public void testIsString() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ChainableReverseAbstractInterpreter interpreter = new ChainableReverseAbstractInterpreter(
        convention, typeRegistry);
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = Node.newCall("goog.isString", Node.newString("hello"),
        Node.newBoolean(true));
    Type expectedType = getNativeType(STRING);
    FlowScope blindScope = new EmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    Type actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(expectedType, actualType);

    // Negative test case
    condition = Node.newCall("goog.isString", Node.newString("hello"),
        Node.newBoolean(false));
    expectedType = null;
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertNull(actualType);

    // Error test case (goog.isString called with wrong number of arguments)
    condition = Node.newCall("goog.isString", Node.newUndefined(), Node.newInt(1));
    Type errorType = getNativeType(NULL_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(errorType, actualType);
  }

  @Test
  public void testIsArray() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ChainableReverseAbstractInterpreter interpreter = new ChainableReverseAbstractInterpreter(
        convention, typeRegistry);
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case (isArray returns true)
    Node condition = Node.newCall("goog.isArray", Node.newArray(),
        Node.newBoolean(true));
    Type expectedType = getNativeType(ARRAY_TYPE);
    FlowScope blindScope = new EmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    Type actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(expectedType, actualType);

    // Positive test case (isArray returns false)
    condition = Node.newCall("goog.isArray", Node.newObject(),
        Node.newBoolean(false));
    expectedType = null;
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertNull(actualType);

    // Negative test case (goog.isArray called with wrong number of arguments)
    condition = Node.newCall("goog.isArray", Node.newString("hello"), Node.newBoolean(1));
    Type errorType = getNativeType(NULL_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(errorType, actualType);
  }

  @Test
  public void testIsObject() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ChainableReverseAbstractInterpreter interpreter = new ChainableReverseAbstractInterpreter(
        convention, typeRegistry);
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case (isObject returns true for functions)
    Node condition = Node.newCall("goog.isObject", Node.newFunction(),
        Node.newBoolean(true));
    Type expectedType = getNativeType(OBJECT_TYPE);
    FlowScope blindScope = new EmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    Type actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(expectedType, actualType);

    // Positive test case (isObject returns true for objects)
    condition = Node.newCall("goog.isObject", Node.newObject(),
        Node.newBoolean(true));
    expectedType = getNativeType(OBJECT_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(expectedType, actualType);

    // Negative test case (isObject returns false)
    condition = Node.newCall("goog.isObject", Node.newNull(),
        Node.newBoolean(false));
    expectedType = null;
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertNull(actualType);

    // Negative test case (isObject called with wrong number of arguments)
    condition = Node.newCall("goog.isObject", Node.newArray(), Node.newBoolean(1));
    Type errorType = getNativeType(NULL_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(errorType, actualType);
  }

  @Test
  public void testIsFunction() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ChainableReverseAbstractInterpreter interpreter = new ChainableReverseAbstractInterpreter(
        convention, typeRegistry);
    ClosureReverseAbstractInterpreter closureInterpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = Node.newCall("goog.isFunction", Node.newFunction(),
        Node.newBoolean(true));
    Type expectedType = getNativeType(OBJECT_TYPE);
    Type functionType = getFunctionType(ImmutableList.of(new JSType[]{}), null);
    expectedType = expectedType.setInterface(functionType);
    FlowScope blindScope = new EmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    Type actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(expectedType, actualType);

    // Negative test case
    condition = Node.newCall("goog.isFunction", Node.newUndefined(),
        Node.newBoolean(false));
    expectedType = null;
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertNull(actualType);

    // Negative test case (goog.isFunction called with wrong number of arguments)
    condition = Node.newCall("goog.isFunction", Node.newString("hello"),
        Node.newBoolean(1));
    Type errorType = getNativeType(NULL_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    actualType = informedScope.getVariableType(condition.getFirstChild());
    assertEquals(errorType, actualType);
  }

  private FunctionType getFunctionType(List<JSType> paramTypes, JSType returnType) {
    return new FunctionType(paramTypes, returnType);
  }
}