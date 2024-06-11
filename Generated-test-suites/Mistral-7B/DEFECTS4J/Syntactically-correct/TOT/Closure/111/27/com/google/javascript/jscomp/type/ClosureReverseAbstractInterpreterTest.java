import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import static com.google.javascript.rhino.jstype.JSTypeRegistry.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = new JSTypeRegistry();
  private static final CodingConvention convention = new CodingConvention();
  private static final ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
      convention, typeRegistry);

  @org.junit.Test
  public void testIsDef() {
    JSType actual = interpreter.restrictParameter(createStringNode("x"), null, null, interpreter.restricters.get("isDef"), true);
    assertEquals(TYPE_BOOLEAN, actual);

    actual = interpreter.restrictParameter(createNullNode(), null, null, interpreter.restricters.get("isDef"), false);
    assertNull(actual);
  }

  @org.junit.Test
  public void testIsDef_True() {
    JSType actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isDef"), true);
    assertEquals(NUMBER_TYPE, actual);
  }

  @org.junit.Test
  public void testIsDef_False() {
    JSType actual = interpreter.restrictParameter(createUndefinedNode(), null, null, interpreter.restricters.get("isDef"), false);
    assertEquals(VOID_TYPE, actual);
  }

  @org.junit.Test
  public void testIsNull() {
    JSType actual = interpreter.restrictParameter(createNullNode(), null, null, interpreter.restricters.get("isNull"), true);
    assertEquals(NULL_TYPE, actual);

    actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isNull"), false);
    assertNull(actual);
  }

  @org.junit.Test
  public void testIsNull_True() {
    JSType actual = interpreter.restrictParameter(createStringNode(""), null, null, interpreter.restricters.get("isNull"), true);
    assertEquals(STRING_TYPE, actual);
  }

  @org.junit.Test
  public void testIsNull_False() {
    JSType actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isNull"), false);
    assertEquals(NUMBER_TYPE, actual);
  }

  @org.junit.Test
  public void testIsDefAndNotNull() {
    JSType actual = interpreter.restrictParameter(createUndefinedNode(), null, null, interpreter.restricters.get("isDefAndNotNull"), false);
    assertEquals(NULL_VOID_TYPE, actual);

    actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isDefAndNotNull"), true);
    assertEquals(NUMBER_TYPE, actual);
  }

  @org.junit.Test
  public void testIsDefAndNotNull_True() {
    JSType actual = interpreter.restrictParameter(createStringNode(""), null, null, interpreter.restricters.get("isDefAndNotNull"), true);
    assertEquals(VOID_TYPE, actual);
  }

  @org.junit.Test
  public void testIsDefAndNotNull_False() {
    JSType actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isDefAndNotNull"), false);
    assertEquals(NUMBER_TYPE, actual);
  }

  @org.junit.Test
  public void testIsString() {
    JSType actual = interpreter.restrictParameter(createStringNode(""), null, null, interpreter.restricters.get("isString"), true);
    assertEquals(STRING_TYPE, actual);

    actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isString"), false);
    assertNull(actual);
  }

  @org.junit.Test
  public void testIsBoolean() {
    JSType actual = interpreter.restrictParameter(createBooleanNode(true), null, null, interpreter.restricters.get("isBoolean"), true);
    assertEquals(BOOLEAN_TYPE, actual);

    actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isBoolean"), false);
    assertNull(actual);
  }

  @org.junit.Test
  public void testIsNumber() {
    JSType actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isNumber"), true);
    assertEquals(NUMBER_TYPE, actual);

    actual = interpreter.restrictParameter(createStringNode(""), null, null, interpreter.restricters.get("isNumber"), false);
    assertNull(actual);
  }

  @org.junit.Test
  public void testIsFunction() {
    JSType actual = interpreter.restrictParameter(createFunctionNode(1), null, null, interpreter.restricters.get("isFunction"), true);
    assertEquals(FUNCTION_TYPE, actual);

    actual = interpreter.restrictParameter(createStringNode(""), null, null, interpreter.restricters.get("isFunction"), false);
    assertNull(actual);
  }

  @org.junit.Test
  public void testIsArray() {
    JSType actual = interpreter.restrictParameter(createArrayNode(new Object(){}), null, null, interpreter.restricters.get("isArray"), true);
    assertEquals(ARRAY_TYPE, actual);

    actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isArray"), false);
    assertNull(actual);
  }

  @org.junit.Test
  public void testIsObject() {
    JSType actual = interpreter.restrictParameter(createObjectNode(new Object(){}), null, null, interpreter.restricters.get("isObject"), true);
    assertEquals(OBJECT_TYPE, actual);

    actual = interpreter.restrictParameter(createNumberNode(1), null, null, interpreter.restricters.get("isObject"), false);
    assertNull(actual);
  }

  private static Node createStringNode(String value) {
    return new Node(STRING_LITERAL, null, value);
  }

  private static Node createNumberNode(double value) {
    return new Node(NUMBER_LITERAL, null, value);
  }

  private static Node createBooleanNode(boolean value) {
    return new Node(BOOLEAN_LITERAL, null, value);
  }

  private static Node createFunctionNode(int arity) {
    Node body = new Node(BLOCK, null);
    body.appendChild(new Node(RETURN, null, new Node(NUMBER_LITERAL, null, 1)));
    return new Node(FUNCTION, new Node(IDENTIFIER, null, "func"), arity, body);
  }

  private static Node createArrayNode(Object value) {
    Node node = new Node(ARRAY_LITERAL, null);
    node.appendChild(new Node(DIMENSION, null, 1));
    node.appendChild(new Node(LIST, null, new Node[]{new Node(NUMBER_LITERAL, null, 1)}));
    return node;
  }

  private static Node createObjectNode(Object value) {
    Node node = new Node(OBJECT_LITERAL, null);
    return node;
  }

  private static Node createNullNode() {
    return new Node(NULL, null);
  }

  private static Node createUndefinedNode() {
    return new Node(UNDEFINED, null);
  }

  private static JSType getTypeIfRefinable(Node node, FlowScope blindScope) {
    return blindScope.getType(node);
  }

  private static JSType getRestrictedWithoutUndefined(JSType type) {
    return type == UNDERDEFINED_TYPE ? VOID_TYPE : type;
  }

  private static JSType getRestrictedWithoutNull(JSType type) {
    return type == NULL_TYPE ? VOID_TYPE : type;
  }

  private static JSType getRestrictedByTypeOfResult(JSType type, String expectedType, boolean outcome) {
    Node node = new Node(CALL, null, new Node[]{
      new Node(GET_PROP, null, new Node[]{new Node(IDENTIFIER, null, "goog"), new Node(STRING_LITERAL, null, "isArray")}),
      new Node(IDENTIFIER, null, "x"),
      new Node(VOID, null)
    });

    FlowScope flowScope = new FlowScope(typeRegistry, convention);
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(node, flowScope, outcome).getType(node);
    return result.equals(getTypeByTypeName(expectedType)) ? result : null;
  }

  private static JSType getTypeByTypeName(String typeName) {
    return typeRegistry.getTypeByName(typeName);
  }
}