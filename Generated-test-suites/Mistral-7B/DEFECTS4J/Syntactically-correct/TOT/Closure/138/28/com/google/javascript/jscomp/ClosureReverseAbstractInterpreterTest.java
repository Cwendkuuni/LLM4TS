import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Type;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isDef",
        NodeOperations.createQualifiedName("myObject", NAME), null);
    Type expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true).getVariable("myObject")
        .getType();
    assertEquals(Type.OBJECT_TYPE, expectedType);

    // Negative test case
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isDef",
        NodeOperations.createNull(NULL_TYPE), null);
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("myObject"));

    // Edge case: undefined
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isDef",
        NodeOperations.createUndefined(), null);
    flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true);
    assertNull(flowScope.getVariable("myObject"));
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isDefAndNotNull",
        NodeOperations.createQualifiedName("myObject", NAME), null);
    Type expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true).getVariable("myObject")
        .getType();
    assertEquals(Type.OBJECT_TYPE, expectedType);

    // Negative test case
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isDefAndNotNull",
        NodeOperations.createNull(NULL_TYPE), null);
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("myObject"));

    // Edge case: undefined
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isDefAndNotNull",
        NodeOperations.createUndefined(), null);
    flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("myObject"));
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isString",
        NodeOperations.createString("test"), null);
    Type expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true).getVariable("test")
        .getType();
    assertEquals(Type.STRING_TYPE, expectedType);

    // Negative test case
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isString",
        NodeOperations.createNumber(1), null);
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("test"));
  }

  @Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isBoolean",
        NodeOperations.createBoolean(true), null);
    Type expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true).getVariable("test")
        .getType();
    assertEquals(Type.BOOLEAN_TYPE, expectedType);

    // Negative test case
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isBoolean",
        NodeOperations.createNumber(1), null);
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("test"));
  }

  @Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isNumber",
        NodeOperations.createString("42"), null);
    Type expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true).getVariable("test").getType();
    assertEquals(Type.NUMBER_TYPE, expectedType);

    // Negative test case
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isNumber",
        NodeOperations.createBoolean(true), null);
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("test"));
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isFunction",
        NodeOperations.createFunction(null, ImmutableList.of(), null), null);
    Type expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true).getVariable("test")
        .getType();
    assertEquals(Type.FUNCTION_TYPE, expectedType);

    // Negative test case
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isFunction",
        NodeOperations.createNumber(1), null);
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("test"));
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isArray",
        NodeOperations.createArray(new Object[] { 1, 2, 3 }), null);
    Type expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true).getVariable("test")
        .getType();
    assertEquals(Type.ARRAY_TYPE, expectedType);

    // Negative test case
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isArray",
        NodeOperations.createString("test"), null);
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("test"));
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Positive test case
    Node condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isObject",
        NodeOperations.createObject(null), null);
    Type expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), true).getVariable("test")
        .getType();
    assertEquals(Type.OBJECT_TYPE, expectedType);

    // Negative test case
    condition = NodeOperations.createCall(
        NodeOperations.createName("goog", NAME), "isObject",
        NodeOperations.createNumber(1), null);
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, new FlowScopeImpl(), false);
    assertNull(flowScope.getVariable("test"));
  }
}