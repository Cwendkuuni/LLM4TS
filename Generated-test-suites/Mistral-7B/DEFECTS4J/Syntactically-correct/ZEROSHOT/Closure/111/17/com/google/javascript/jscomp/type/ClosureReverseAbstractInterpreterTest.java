import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ClosureReverseAbstractInterpreter}.
 */
public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;
  private ClosureReverseAbstractInterpreter interpreter;
  private Node conditionNode;
  private Node parameterNode;
  private Node arrayLiteralNode;
  private Node objectLiteralNode;
  private Node functionLiteralNode;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);

    conditionNode = createConditionNode("goog.isDef", "value");
    parameterNode = createParameterNode("value");
    arrayLiteralNode = createArrayLiteralNode();
    objectLiteralNode = createObjectLiteralNode();
    functionLiteralNode = createFunctionLiteralNode();
  }

  @Test
  public void testIsDefTrue() {
    when(typeRegistry.createArrayType(new int[0])).thenReturn(getArrayType());
    when(typeRegistry.createObjectType("Object")).thenReturn(getObjectType());
    when(typeRegistry.createFunctionType(new FunctionType(null, null, null))).thenReturn(getFunctionType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getArrayType(), result);
  }

  @Test
  public void testIsDefFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(VOID_TYPE), result);
  }

  @Test
  public void testIsDefNullTrue() {
    when(typeRegistry.createArrayType(new int[0])).thenReturn(getArrayType());
    when(typeRegistry.createObjectType("Object")).thenReturn(getObjectType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(NULL_TYPE), result);
  }

  @Test
  public void testIsDefNullFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(NULL_VOID), result);
  }

  @Test
  public void testIsDefAndNotNullTrue() {
    when(typeRegistry.createArrayType(new int[0])).thenReturn(getArrayType());
    when(typeRegistry.createObjectType("Object")).thenReturn(getObjectType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true)
        .getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(NO_OBJECT_TYPE), result);
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false)
        .getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(NULL_VOID), result);
  }

  @Test
  public void testIsStringTrue() {
    when(typeRegistry.createObjectType("String")).thenReturn(getStringType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getStringType(), result);
  }

  @Test
  public void testIsStringFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(VOID_TYPE), result);
  }

  @Test
  public void testIsBooleanTrue() {
    when(typeRegistry.createObjectType("Boolean")).thenReturn(getBooleanType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getBooleanType(), result);
  }

  @Test
  public void testIsBooleanFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(VOID_TYPE), result);
  }

  @Test
  public void testIsNumberTrue() {
    when(typeRegistry.createObjectType("Number")).thenReturn(getNumberType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNumberType(), result);
  }

  @Test
  public void testIsNumberFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(VOID_TYPE), result);
  }

  @Test
  public void testIsFunctionTrue() {
    when(typeRegistry.createFunctionType(new FunctionType(null, null, null))).thenReturn(getFunctionType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getFunctionType(), result);
  }

  @Test
  public void testIsFunctionFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(VOID_TYPE), result);
  }

  @Test
  public void testIsArrayTrue() {
    when(typeRegistry.createArrayType(new int[0])).thenReturn(getArrayType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getArrayType(), result);
  }

  @Test
  public void testIsArrayFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(OBJECT_TYPE), result);
  }

  @Test
  public void testIsObjectTrue() {
    when(typeRegistry.createObjectType("Object")).thenReturn(getObjectType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getObjectType(), result);
  }

  @Test
  public void testIsObjectFalse() {
    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false).getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(VOID_TYPE), result);
  }

  @Test
  public void testIsArrayAndIsFunctionTrue() {
    when(typeRegistry.createArrayType(new int[0])).thenReturn(getArrayType());
    when(typeRegistry.createFunctionType(new FunctionType(null, null, null))).thenReturn(getFunctionType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, true)
        .getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getFunctionType(), result);
  }

  @Test
  public void testIsArrayAndIsFunctionFalse() {
    when(typeRegistry.createArrayType(new int[0])).thenReturn(getArrayType());
    when(typeRegistry.createFunctionType(new FunctionType(null, null, null))).thenReturn(getFunctionType());

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, null, false)
        .getDeclaredTypeOf(parameterNode);
    assertNotNull(result);
    assertEquals(getNativeType(VOID_TYPE), result);
  }

  private Node createConditionNode(String calleeName, String propertyName) {
    Node callee = Node.newName(calleeName);
    Node property = Node.newGetProp(callee, propertyName);
    Node condition = Node.newCall(callee, property, new Node[0]);
    return condition;
  }

  private Node createParameterNode(String name) {
    return Node.newName(name);
  }

  private Node createArrayLiteralNode() {
    return Node.newArrayLiteral();
  }

  private Node createObjectLiteralNode() {
    return Node.newObjectLiteral();
  }

  private Node createFunctionLiteralNode() {
    return Node.newFunctionLiteral("func", new Node[0], Node.newBlock(new Node[0]));
  }

  private JSType getArrayType() {
    return (JSType) JSTypeNative.ARRAY_TYPE;
  }

  private JSType getBooleanType() {
    return (JSType) JSTypeNative.BOOLEAN_TYPE;
  }

  private JSType getFunctionType() {
    FunctionType functionType = new FunctionType(null, null, null);
    return typeRegistry.createFunctionType(functionType);
  }

  private JSType getNumberType() {
    return (JSType) JSTypeNative.NUMBER_TYPE;
  }

  private JSType getObjectType() {
    return (JSType) JSTypeNative.OBJECT_TYPE;
  }

  private JSType getNativeType(int typeCode) {
    return JSTypeNative.get(typeCode);
  }

  private JSType getNativeType(String typeName) {
    return typeRegistry.getType(typeName);
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String targetType, boolean outcome) {
    return interpreter.getRestrictedByTypeOfResult(type, targetType, outcome);
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    return interpreter.getRestrictedWithoutNull(type);
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    return interpreter.getRestrictedWithoutUndefined(type);
  }
}