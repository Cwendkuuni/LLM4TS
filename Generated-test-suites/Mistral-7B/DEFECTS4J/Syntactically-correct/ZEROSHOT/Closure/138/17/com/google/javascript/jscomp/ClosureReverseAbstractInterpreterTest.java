import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  @org.junit.Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    // Setup
    CodingConvention convention = mock(CodingConvention.class);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    // Test case 1: isDef returns true
    Node condition1 = createNode("goog.isDef(someValue)", null);
    JSType expectedType1 = createJSType(JSTypeNative.getTopType());
    FlowScope blindScope1 = createFlowScope();
    FlowScope informedScope1 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition1, blindScope1, true);
    assertEquals(expectedType1, informedScope1.getVariableType("someValue"));

    // Test case 2: isDef returns false
    Node condition2 = createNode("goog.isDef(someValue)", null);
    FlowScope blindScope2 = createFlowScope();
    FlowScope informedScope2 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition2, blindScope2, false);
    assertEquals(null, informedScope2.getVariableType("someValue"));

    // Test case 3: isNull returns true
    Node condition3 = createNode("goog.isNull(someValue)", null);
    JSType expectedType3 = createJSType(JSTypeNative.getNativeType(JSTypeNative.NULL_TYPE));
    FlowScope blindScope3 = createFlowScope();
    FlowScope informedScope3 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition3, blindScope3, true);
    assertEquals(expectedType3, informedScope3.getVariableType("someValue"));

    // Test case 4: isNull returns false
    Node condition4 = createNode("goog.isNull(someValue)", null);
    FlowScope blindScope4 = createFlowScope();
    FlowScope informedScope4 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition4, blindScope4, false);
    assertEquals(null, informedScope4.getVariableType("someValue"));

    // Test case 5: isDefAndNotNull returns true
    Node condition5 = createNode("goog.isDefAndNotNull(someValue)", null);
    JSType expectedType5 = createJSType(
        JSTypeNative.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
    FlowScope blindScope5 = createFlowScope();
    FlowScope informedScope5 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition5, blindScope5, true);
    assertEquals(expectedType5, informedScope5.getVariableType("someValue"));

    // Test case 6: isDefAndNotNull returns false
    Node condition6 = createNode("goog.isDefAndNotNull(someValue)", null);
    FlowScope blindScope6 = createFlowScope();
    FlowScope informedScope6 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition6, blindScope6, false);
    assertEquals(null, informedScope6.getVariableType("someValue"));

    // Test case 7: isString returns true
    Node condition7 = createNode("goog.isString(someValue)", null);
    JSType expectedType7 = createJSType(JSTypeNative.getStringType());
    FlowScope blindScope7 = createFlowScope();
    FlowScope informedScope7 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition7, blindScope7, true);
    assertEquals(expectedType7, informedScope7.getVariableType("someValue"));

    // Test case 8: isString returns false
    Node condition8 = createNode("goog.isString(someValue)", null);
    FlowScope blindScope8 = createFlowScope();
    FlowScope informedScope8 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition8, blindScope8, false);
    assertEquals(null, informedScope8.getVariableType("someValue"));

    // Test case 9: isBoolean returns true
    Node condition9 = createNode("goog.isBoolean(someValue)", null);
    JSType expectedType9 = createJSType(JSTypeNative.getBooleanType());
    FlowScope blindScope9 = createFlowScope();
    FlowScope informedScope9 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition9, blindScope9, true);
    assertEquals(expectedType9, informedScope9.getVariableType("someValue"));

    // Test case 10: isBoolean returns false
    Node condition10 = createNode("goog.isBoolean(someValue)", null);
    FlowScope blindScope10 = createFlowScope();
    FlowScope informedScope10 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition10, blindScope10, false);
    assertEquals(null, informedScope10.getVariableType("someValue"));

    // Test case 11: isNumber returns true
    Node condition11 = createNode("goog.isNumber(someValue)", null);
    JSType expectedType11 = createJSType(JSTypeNative.getNumberType());
    FlowScope blindScope11 = createFlowScope();
    FlowScope informedScope11 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition11, blindScope11, true);
    assertEquals(expectedType11, informedScope11.getVariableType("someValue"));

    // Test case 12: isNumber returns false
    Node condition12 = createNode("goog.isNumber(someValue)", null);
    FlowScope blindScope12 = createFlowScope();
    FlowScope informedScope12 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition12, blindScope12, false);
    assertEquals(null, informedScope12.getVariableType("someValue"));

    // Test case 13: isFunction returns true
    Node condition13 = createNode("goog.isFunction(someValue)", null);
    JSType expectedType13 = createJSType(JSTypeNative.getFunctionType(null, null));
    FlowScope blindScope13 = createFlowScope();
    FlowScope informedScope13 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition13, blindScope13, true);
    assertEquals(expectedType13, informedScope13.getVariableType("someValue"));

    // Test case 14: isFunction returns false
    Node condition14 = createNode("goog.isFunction(someValue)", null);
    FlowScope blindScope14 = createFlowScope();
    FlowScope informedScope14 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition14, blindScope14, false);
    assertEquals(null, informedScope14.getVariableType("someValue"));

    // Test case 15: isArray returns true
    Node condition15 = createNode("goog.isArray(someValue)", null);
    JSType expectedType15 = createJSType(JSTypeNative.getArrayType());
    FlowScope blindScope15 = createFlowScope();
    FlowScope informedScope15 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition15, blindScope15, true);
    assertEquals(expectedType15, informedScope15.getVariableType("someValue"));

    // Test case 16: isArray returns false
    Node condition16 = createNode("goog.isArray(someValue)", null);
    FlowScope blindScope16 = createFlowScope();
    FlowScope informedScope16 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition16, blindScope16, false);
    assertEquals(null, informedScope16.getVariableType("someValue"));

    // Test case 17: isObject returns true
    Node condition17 = createNode("goog.isObject(someValue)", null);
    JSType expectedType17 = createJSType(JSTypeNative.getObjectType());
    FlowScope blindScope17 = createFlowScope();
    FlowScope informedScope17 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition17, blindScope17, true);
    assertEquals(expectedType17, informedScope17.getVariableType("someValue"));

    // Test case 18: isObject returns false
    Node condition18 = createNode("goog.isObject(someValue)", null);
    FlowScope blindScope18 = createFlowScope();
    FlowScope informedScope18 = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition18, blindScope18, false);
    assertEquals(null, informedScope18.getVariableType("someValue"));
  }

  private Node createNode(String nodeString, Node parent) {
    Node node = new Node();
    node.setType(JSTypeNative.getTopType());
    node.setToStringValue(nodeString);
    node.setParent(parent);
    return node;
  }

  private JSType createJSType(JSType type) {
    return new ObjectType(type);
  }

  private FlowScope createFlowScope() {
    return new FlowScope();
  }
}