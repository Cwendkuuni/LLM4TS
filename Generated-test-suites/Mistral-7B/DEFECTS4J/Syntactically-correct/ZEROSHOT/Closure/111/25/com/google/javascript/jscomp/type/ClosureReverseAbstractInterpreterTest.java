package com.google.javascript.jscomp.type;

import static org.junit.Assert.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ClosureReverseAbstractInterpreter} class.
 */
public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
  private static final CodingConvention codingConvention = new CodingConvention();
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDef() {
    Node condition = createNode("goog.isDef(null)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = typeRegistry.createUndefinedType();
    assertEquals(expectedType, preciserScope.getVariable("null").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDefNotNull() {
    Node condition = createNode("goog.isDefAndNotNull(null)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = typeRegistry.createNullType();
    assertEquals(expectedType, preciserScope.getVariable("null").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDefNull() {
    Node condition = createNode("goog.isDefAndNotNull(null)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    JSType expectedType = typeRegistry.createVoidType();
    assertEquals(expectedType, preciserScope.getVariable("null").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDefBoolean() {
    Node condition = createNode("goog.isDef(someBoolean)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertTrue(preciserScope.getVariable("someBoolean").isDefined());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDefNumber() {
    Node condition = createNode("goog.isDef(someNumber)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertTrue(preciserScope.getVariable("someNumber").isDefined());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDefString() {
    Node condition = createNode("goog.isDef(someString)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertTrue(preciserScope.getVariable("someString").isDefined());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDefFunction() {
    Node condition = createNode("goog.isDef(someFunction)");
    FlowScope blindScope = new FlowScope("root");
    FunctionType functionType = new FunctionType(typeRegistry.createObjectType(), typeRegistry.createObjectType());
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertTrue(preciserScope.getVariable("someFunction").isDefined());
    assertEquals(functionType, preciserScope.getVariable("someFunction").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDefArray() {
    Node condition = createNode("goog.isDef(myArray)");
    FlowScope blindScope = new FlowScope("root");
    ArrayType arrayType = new ArrayType(typeRegistry.createNumberType());
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertTrue(preciserScope.getVariable("myArray").isDefined());
    assertEquals(arrayType, preciserScope.getVariable("myArray").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsDefObject() {
    Node condition = createNode("goog.isDef(myObject)");
    FlowScope blindScope = new FlowScope("root");
    ObjectType objectType = new ObjectType(ImmutableMap.of("a", typeRegistry.createNumberType()));
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertTrue(preciserScope.getVariable("myObject").isDefined());
    assertEquals(objectType, preciserScope.getVariable("myObject").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNotNull() {
    Node condition = createNode("goog.isDefAndNotNull(null)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(preciserScope.getVariable("null"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNotNullBoolean() {
    Node condition = createNode("goog.isDefAndNotNull(someBoolean)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(preciserScope.getVariable("someBoolean"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNotNullNumber() {
    Node condition = createNode("goog.isDefAndNotNull(someNumber)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(preciserScope.getVariable("someNumber"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNotNullString() {
    Node condition = createNode("goog.isDefAndNotNull(someString)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(preciserScope.getVariable("someString"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNotNullFunction() {
    Node condition = createNode("goog.isDefAndNotNull(someFunction)");
    FlowScope blindScope = new FlowScope("root");
    FunctionType functionType = new FunctionType(typeRegistry.createObjectType(), typeRegistry.createObjectType());
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(preciserScope.getVariable("someFunction"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNotNullArray() {
    Node condition = createNode("goog.isDefAndNotNull(myArray)");
    FlowScope blindScope = new FlowScope("root");
    ArrayType arrayType = new ArrayType(typeRegistry.createNumberType());
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(preciserScope.getVariable("myArray"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNotNullObject() {
    Node condition = createNode("goog.isDefAndNotNull(myObject)");
    FlowScope blindScope = new FlowScope("root");
    ObjectType objectType = new ObjectType(ImmutableMap.of("a", typeRegistry.createNumberType()));
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(preciserScope.getVariable("myObject"));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsString() {
    Node condition = createNode("goog.isString(someString)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = typeRegistry.createStringType();
    assertEquals(expectedType, preciserScope.getVariable("someString").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsBoolean() {
    Node condition = createNode("goog.isBoolean(someBoolean)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = typeRegistry.createBooleanType();
    assertEquals(expectedType, preciserScope.getVariable("someBoolean").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNumber() {
    Node condition = createNode("goog.isNumber(someNumber)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = typeRegistry.createNumberType();
    assertEquals(expectedType, preciserScope.getVariable("someNumber").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsFunction() {
    Node condition = createNode("goog.isFunction(someFunction)");
    FlowScope blindScope = new FlowScope("root");
    FunctionType functionType = new FunctionType(typeRegistry.createObjectType(), typeRegistry.createObjectType());
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = functionType;
    assertEquals(expectedType, preciserScope.getVariable("someFunction").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsArray() {
    Node condition = createNode("goog.isArray(myArray)");
    FlowScope blindScope = new FlowScope("root");
    ArrayType arrayType = new ArrayType(typeRegistry.createNumberType());
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = arrayType;
    assertEquals(expectedType, preciserScope.getVariable("myArray").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsObject() {
    Node condition = createNode("goog.isObject(myObject)");
    FlowScope blindScope = new FlowScope("root");
    ObjectType objectType = new ObjectType(ImmutableMap.of("a", typeRegistry.createNumberType()));
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = objectType;
    assertEquals(expectedType, preciserScope.getVariable("myObject").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsUndefined() {
    Node condition = createNode("goog.isDef(undefined)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = typeRegistry.createUndefinedType();
    assertEquals(expectedType, preciserScope.getVariable("undefined").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeIsNull() {
    Node condition = createNode("goog.isDef(null)");
    FlowScope blindScope = new FlowScope("root");
    FlowScope preciserScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = typeRegistry.createUndefinedType();
    assertEquals(expectedType, preciserScope.getVariable("null").getType());
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcomeInvalidCall() {
    Node condition = createNode("goog.isDef(1)");
    FlowScope blindScope = new FlowScope("root");
    interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
  }

  @Test(expected = AssertionError.class)
  public void testGetPreciserScopeKnowingConditionOutcomeInvalidCondition() {
    Node condition = createNode("goog.isDef(goog)");
    FlowScope blindScope = new FlowScope("root");
    interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
  }

  private Node createNode(String nodeString) {
    return new NodeBuilder(null).parse(nodeString).toNode();
  }
}

class NodeBuilder {
  // NodeBuilder implementation
}