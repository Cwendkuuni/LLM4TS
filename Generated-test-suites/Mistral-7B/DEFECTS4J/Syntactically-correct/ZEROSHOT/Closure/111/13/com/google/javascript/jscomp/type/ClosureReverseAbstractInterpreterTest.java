import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    JSType inputType = getNativeType(ObjectType.VOID);
    TypeRestriction expectation = new TypeRestriction(null, true);
    when(typeRegistry.createUnionType(NUMBER_STRING_BOOLEAN, NULL_VOID))
        .thenReturn(getNativeType(UNIONType.VOID_BOOLEAN));
    when(typeRegistry.createUnionType(NUMBER_STRING_BOOLEAN, VOID_TYPE))
        .thenReturn(getNativeType(UNIONType.VOID_BOOLEAN));
    when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN))
        .thenReturn(getNativeType(ObjectType.STRING));
    when(typeRegistry.getNativeType(NULL_VOID))
        .thenReturn(getNativeType(ObjectType.NULL));

    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isDef(x)"), null, true);
    JSType actualType = actual.getType();
    JSType expectationType = interpreter.restricters.get("isDef")
        .apply(expectation);

    assertEquals(expectationType, actualType);
  }

  @Test
  public void testIsDef_False() {
    JSType inputType = getNativeType(ObjectType.NUMBER);
    TypeRestriction expectation = new TypeRestriction(inputType, false);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isDef(x)"), null, false).getType();

    assertNull(actualType);
  }

  @Test
  public void testIsDef_Null() {
    JSType inputType = null;
    TypeRestriction expectation = new TypeRestriction(null, false);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isDef(x)"), null, false).getType();

    assertNull(actualType);
  }

  @Test
  public void testIsDef_Undefined() {
    JSType inputType = getNativeType(ObjectType.VOID);
    TypeRestriction expectation = new TypeRestriction(null, false);

    when(typeRegistry.createUnionType(NUMBER_STRING_BOOLEAN, NULL_VOID))
        .thenReturn(getNativeType(UNIONType.VOID_BOOLEAN));
    when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN))
        .thenReturn(getNativeType(ObjectType.STRING));
    when(typeRegistry.getNativeType(NULL_VOID))
        .thenReturn(getNativeType(ObjectType.NULL));

    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isDef(x)"), null, false);
    JSType actualType = actual.getType();
    JSType expectationType = interpreter.restricters.get("isDef")
        .apply(expectation);

    assertEquals(expectationType, actualType);
  }

  @Test
  public void testIsDef_Undefined_True() {
    JSType inputType = getNativeType(ObjectType.VOID);
    TypeRestriction expectation = new TypeRestriction(null, true);

    when(typeRegistry.createUnionType(NUMBER_STRING_BOOLEAN, NULL_VOID))
        .thenReturn(getNativeType(UNIONType.VOID_BOOLEAN));
    when(typeRegistry.getNativeType(NUMBER_STRING_BOOLEAN))
        .thenReturn(getNativeType(ObjectType.STRING));
    when(typeRegistry.getNativeType(NULL_VOID))
        .thenReturn(getNativeType(ObjectType.NULL));

    when(typeRegistry.getNativeType(UNIONType.VOID_BOOLEAN))
        .thenReturn(getNativeType(ObjectType.BOOLEAN));

    FlowScope actual = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isDef(x)"), null, true);
    JSType actualType = actual.getType();
    JSType expectationType = interpreter.restricters.get("isDef")
        .apply(expectation);

    assertEquals(expectationType, actualType);
  }

  @Test
  public void testIsString() {
    JSType inputType = getNativeType(ObjectType.NUMBER);
    TypeRestriction expectation = new TypeRestriction(inputType, true);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isString(x)"), null, true).getType();

    assertEquals(getNativeType(ObjectType.STRING), actualType);
  }

  @Test
  public void testIsString_False() {
    JSType inputType = getNativeType(ObjectType.STRING);
    TypeRestriction expectation = new TypeRestriction(inputType, false);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isString(x)"), null, false).getType();

    assertNull(actualType);
  }

  @Test
  public void testIsObject() {
    JSType inputType = getNativeType(ObjectType.NUMBER);
    TypeRestriction expectation = new TypeRestriction(inputType, true);

    when(typeRegistry.getNativeType(ObjectType.OBJECT))
        .thenReturn(getNativeType(ObjectType.OBJECT));

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isObject(x)"), null, true).getType();

    assertEquals(getNativeType(ObjectType.OBJECT), actualType);
  }

  @Test
  public void testIsObject_False() {
    JSType inputType = getNativeType(ObjectType.OBJECT);
    TypeRestriction expectation = new TypeRestriction(inputType, false);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isObject(x)"), null, false).getType();

    assertNull(actualType);
  }

  @Test
  public void testIsFunction() {
    JSType inputType = getNativeType(ObjectType.NUMBER);
    TypeRestriction expectation = new TypeRestriction(inputType, true);

    FunctionType expectationFunctionType = getFunctionType(ObjectType.VOID,
        new JSType[] { ObjectType.NUMBER });

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isFunction(x)"), null, true).getType();

    assertEquals(expectationFunctionType, actualType);
  }

  @Test
  public void testIsFunction_False() {
    JSType inputType = getNativeType(FunctionType.VOID_NUMBER);
    TypeRestriction expectation = new TypeRestriction(inputType, false);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isFunction(x)"), null, false).getType();

    assertNull(actualType);
  }

  @Test
  public void testIsArray() {
    JSType inputType = getNativeType(ObjectType.NUMBER);
    TypeRestriction expectation = new TypeRestriction(inputType, true);

    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType arrayTypeSubtype = arrayType.getGreatestSubtype(inputType);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isArray(x)"), null, true).getType();

    assertEquals(arrayTypeSubtype, actualType);
  }

  @Test
  public void testIsArray_False() {
    JSType inputType = getNativeType(ARRAY_TYPE);
    TypeRestriction expectation = new TypeRestriction(inputType, false);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isArray(x)"), null, false).getType();

    assertNull(actualType);
  }

  @Test
  public void testIsArray_Null() {
    JSType inputType = null;
    TypeRestriction expectation = new TypeRestriction(inputType, false);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        createNode("goog.isArray(x)"), null, false).getType();

    assertNull(actualType);
  }

  private Node createNode(String expression) {
    Node node = new Node();
    node.setType(new ObjectType("AstNode"));
    node.setClassName("ExpressionCall");
    node.setFirstChild(new Node());
    node.setLastChild(new Node());
    node.setLastChild(new Node());

    Node callee = new Node();
    callee.setType(new ObjectType("AstName"));
    callee.setClassName("Identifier");
    callee.setString("goog");

    Node left = new Node();
    left.setType(new ObjectType("AstName"));
    left.setClassName("Identifier");
    left.setString("isDef");

    Node param = new Node();
    param.setType(new ObjectType("AstQualifiedName"));
    param.setClassName("Identifier");
    param.setQualifiedName(new String[] { "x" });

    node.setFirstChild(callee);
    node.setLastChild(param);

    callee.setFirstChild(left);
    callee.setLastChild(param);

    return node;
  }

  private static FunctionType getFunctionType(JSType returnType,
      JSType[] argumentTypes) {
    FunctionType functionType = new FunctionType(returnType, argumentTypes);
    return functionType;
  }

  private static JSType getNativeType(int typeId) {
    return typeRegistry.getNativeType(typeId);
  }

  private static JSType getNativeType(JSType nativeType) {
    return typeRegistry.getNativeType(nativeType);
  }

  private static JSType getNativeType(String typeName) {
    return typeRegistry.getNativeType(typeName);
  }

  private static JSType getNativeType(JSTypeType type) {
    return typeRegistry.getNativeType(type.getTypeId());
  }

  private static JSType getNativeType(UnionType unionType) {
    return typeRegistry.getNativeType(unionType.getTypeId());
  }

  private static JSType getTypeIfRefinable(Node node, FlowScope scope) {
    return interpreter.getTypeIfRefinable(node, scope);
  }
}