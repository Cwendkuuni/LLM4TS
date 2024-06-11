package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import java.util.Map;

/**
 * Test for {@link ClosureReverseAbstractInterpreter} class.
 */
public class ClosureReverseAbstractInterpreterTest {

  @org.junit.Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myVar"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = ra.getTypeIfRefinable(param, blindScope);
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isDefAndNotNull"), true).getVariableType();

    assertEquals(expected, actual);

    expected = JSType.NULL_TYPE;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isDefAndNotNull"), false).getVariableType();

    assertNull(actual);
  }

  @org.junit.Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myVar"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.UNDEFINED;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isDef"), true).getVariableType();

    assertEquals(JSType.UNDEFINED, actual);

    expected = JSType.NULL_TYPE;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isDef"), false).getVariableType();

    assertNull(actual);
  }

  @org.junit.Test
  public void testIsNotNull() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myVar"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.UNDEFINED;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isNull"), true).getVariableType();

    assertNull(actual);

    expected = JSType.NULL_TYPE;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isNull"), false).getVariableType();

    assertEquals(JSType.NULL_TYPE, actual);
  }

  @org.junit.Test
  public void testIsDefAndNotNullWithNullType() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myVar"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.NULL_TYPE;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isDefAndNotNull"), true).getVariableType();

    assertNull(actual);

    expected = JSType.NULL_TYPE;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isDefAndNotNull"), false).getVariableType();

    assertNull(actual);
  }

  @org.junit.Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myString"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.STRING;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isString"), true).getVariableType();

    assertEquals(expected, actual);

    expected = JSType.NUMBER;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isString"), false).getVariableType();

    assertNull(actual);
  }

  @org.junit.Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myBoolean"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.BOOLEAN;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isBoolean"), true).getVariableType();

    assertEquals(expected, actual);

    expected = JSType.NUMBER;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isBoolean"), false).getVariableType();

    assertNull(actual);
  }

  @org.junit.Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myNumber"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.NUMBER;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isNumber"), true).getVariableType();

    assertEquals(expected, actual);

    expected = JSType.STRING;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isNumber"), false).getVariableType();

    assertNull(actual);
  }

  @org.junit.Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myFunction"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.FUNCTION;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isFunction"), true).getVariableType();

    assertEquals(expected, actual);

    expected = JSType.NUMBER;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isFunction"), false).getVariableType();

    assertNull(actual);
  }

  @org.junit.Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myArray"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.ARRAY_TYPE;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isArray"), true).getVariableType();

    assertEquals(expected, actual);

    expected = JSType.NUMBER;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isArray"), false).getVariableType();

    assertNull(actual);
  }

  @org.junit.Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ChainableReverseAbstractInterpreter ra = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node node = new Node(new NAME("goog"), new int[] {GETPROP});
    Node param = new Node(new NAME("myObject"), new int[0]);
    Node condition = new Node(new NAME("goog"), new int[] {GETPROP});
    condition.addChild(node);
    condition.addChild(param);

    Node trueNode = new Node(new NAME("true"), new int[0]);
    Node falseNode = new Node(new NAME("false"), new int[0]);

    FlowScope blindScope = new FlowScope();

    JSType expected = JSType.OBJECT_TYPE;
    JSType actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isObject"), true).getVariableType();

    assertEquals(expected, actual);

    expected = JSType.NUMBER;
    actual = ra.restrictParameter(param, expected, blindScope,
        ra.restricters.get("isObject"), false).getVariableType();

    assertNull(actual);
  }
}