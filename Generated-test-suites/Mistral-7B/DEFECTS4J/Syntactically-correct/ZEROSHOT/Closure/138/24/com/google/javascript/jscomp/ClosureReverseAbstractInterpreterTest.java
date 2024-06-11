import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeType.*;
import static com.google.javascript.jscomp.JSType.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter.FlowScope;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private NodeFactory nodeFactory;
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory(null);
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
  }

  /**
   * Test for isDef method with true outcome.
   */
  @Test
  public void testIsDefTrue() {
    Node condition = nodeFactory.createCall(null, "goog.isDef", null,
        nodeFactory.createString("myVariable"));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedResult = getNativeType(OBJECT_TYPE);
    JSType actualResult = informedScope.getVariable("myVariable").getType();
    assertEquals(expectedResult, actualResult);
  }

  /**
   * Test for isDef method with false outcome.
   */
  @Test
  public void testIsDefFalse() {
    Node condition = nodeFactory.createCall(null, "goog.isDef", null,
        nodeFactory.createString("myUndefinedVariable"));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(informedScope.getVariable("myUndefinedVariable").getType());
  }

  /**
   * Test for isDefAndNotNull method with true outcome.
   */
  @Test
  public void testIsDefAndNotNullTrue() {
    Node condition = nodeFactory.createCall(null, "goog.isDefAndNotNull", null,
        nodeFactory.createString("myNonnullVariable"));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedResult = getNativeType(OBJECT_TYPE);
    JSType actualResult = informedScope.getVariable("myNonnullVariable").getType();
    assertEquals(expectedResult, actualResult);
  }

  /**
   * Test for isDefAndNotNull method with false outcome.
   */
  @Test
  public void testIsDefAndNotNullFalse() {
    Node condition = nodeFactory.createCall(null, "goog.isDefAndNotNull", null,
        nodeFactory.createString("myNullVariable"));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(informedScope.getVariable("myNullVariable").getType());
  }

  /**
   * Test for isString method with true outcome.
   */
  @Test
  public void testIsStringTrue() {
    Node condition = nodeFactory.createCall(null, "goog.isString", null,
        nodeFactory.createString("myStringVariable"));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedResult = getNativeType(STRING);
    JSType actualResult = informedScope.getVariable("myStringVariable").getType();
    assertEquals(expectedResult, actualResult);
  }

  /**
   * Test for isString method with false outcome.
   */
  @Test
  public void testIsStringFalse() {
    Node condition = nodeFactory.createCall(null, "goog.isString", null,
        nodeFactory.createNumber(10));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(informedScope.getVariable(0).getType());
  }

  /**
   * Test for isBoolean method with true outcome.
   */
  @Test
  public void testIsBooleanTrue() {
    Node condition = nodeFactory.createCall(null, "goog.isBoolean", null,
        nodeFactory.createBoolean(true));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedResult = getNativeType(BOOLEAN);
    JSType actualResult = informedScope.getVariable(0).getType();
    assertEquals(expectedResult, actualResult);
  }

  /**
   * Test for isBoolean method with false outcome.
   */
  @Test
  public void testIsBooleanFalse() {
    Node condition = nodeFactory.createCall(null, "goog.isBoolean", null,
        nodeFactory.createNumber(10));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(informedScope.getVariable(0).getType());
  }

  /**
   * Test for isNumber method with true outcome.
   */
  @Test
  public void testIsNumberTrue() {
    Node condition = nodeFactory.createCall(null, "goog.isNumber", null,
        nodeFactory.createNumber(10));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedResult = getNativeType(NUMBER);
    JSType actualResult = informedScope.getVariable(0).getType();
    assertEquals(expectedResult, actualResult);
  }

  /**
   * Test for isNumber method with false outcome.
   */
  @Test
  public void testIsNumberFalse() {
    Node condition = nodeFactory.createCall(null, "goog.isNumber", null,
        nodeFactory.createString("myString"));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(informedScope.getVariable(0).getType());
  }

  /**
   * Test for isFunction method with true outcome.
   */
  @Test
  public void testIsFunctionTrue() {
    Node condition = nodeFactory.createCall(null, "goog.isFunction", null,
        nodeFactory.createFunction(null, null, null, null));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedResult = getNativeType(FunctionType.VOID_FUNCTION_TYPE);
    JSType actualResult = informedScope.getVariable(0).getType();
    assertEquals(expectedResult, actualResult);
  }

  /**
   * Test for isFunction method with false outcome.
   */
  @Test
  public void testIsFunctionFalse() {
    Node condition = nodeFactory.createCall(null, "goog.isFunction", null,
        nodeFactory.createNumber(10));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(informedScope.getVariable(0).getType());
  }

  /**
   * Test for isArray method with true outcome.
   */
  @Test
  public void testIsArrayTrue() {
    Node condition = nodeFactory.createCall(null, "goog.isArray", null,
        nodeFactory.createArray(new Object[] { 1, 2, 3 }));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedResult = getNativeType(ARRAY_TYPE);
    JSType actualResult = informedScope.getVariable(0).getType();
    assertEquals(expectedResult, actualResult);
  }

  /**
   * Test for isArray method with false outcome.
   */
  @Test
  public void testIsArrayFalse() {
    Node condition = nodeFactory.createCall(null, "goog.isArray", null,
        nodeFactory.createNumber(10));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(informedScope.getVariable(0).getType());
  }

  /**
   * Test for isObject method with true outcome.
   */
  @Test
  public void testIsObjectTrue() {
    Node condition = nodeFactory.createCall(null, "goog.isObject", null,
        nodeFactory.createObject(null));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    JSType expectedResult = getNativeType(OBJECT_TYPE);
    JSType actualResult = informedScope.getVariable(0).getType();
    assertEquals(expectedResult, actualResult);
  }

  /**
   * Test for isObject method with false outcome.
   */
  @Test
  public void testIsObjectFalse() {
    Node condition = nodeFactory.createCall(null, "goog.isObject", null,
        nodeFactory.createNumber(10));
    Node blindScope = createEmptyFlowScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);
    assertNull(informedScope.getVariable(0).getType());
  }

  private FlowScope createEmptyFlowScope() {
    return createEmptyFlowScope(typeRegistry);
  }

  private FlowScope createEmptyFlowScope(JSTypeRegistry registry) {
    return new FlowScope(registry, null, registry.getTopType(), null);
  }
}