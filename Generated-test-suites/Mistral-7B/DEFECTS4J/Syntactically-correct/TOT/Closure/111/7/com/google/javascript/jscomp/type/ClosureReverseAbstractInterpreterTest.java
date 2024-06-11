import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
  private static final CodingConvention convention = mock(CodingConvention.class);

  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = getNativeType(NUMBER_STRING_BOOLEAN);
    when(typeRegistry.createTopType()).thenReturn(type);

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = getNativeType(NUMBER_STRING_BOOLEAN);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  @Test
  public void testIsDefFalse() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = getNativeType(NUMBER_STRING_BOOLEAN);
    when(typeRegistry.createTopType()).thenReturn(type);

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  @Test
  public void testIsDefUndefined() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = getNativeType(NUMBER_STRING_BOOLEAN);
    when(typeRegistry.createTopType()).thenReturn(type);

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();
    when(typeRegistry.createUnionType(getNativeType(NUMBER_STRING_BOOLEAN),
        getNativeType(NULL_VOID))).thenReturn(type);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = getNativeType(NULL_VOID);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  @Test
  public void testIsDefNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = getNativeType(NUMBER_STRING_BOOLEAN);
    when(typeRegistry.createTopType()).thenReturn(type);

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();
    when(typeRegistry.createUnionType(getNativeType(NUMBER_STRING_BOOLEAN),
        getNativeType(NULL_VOID))).thenReturn(type);
    when(typeRegistry.createNullType()).thenReturn(getNativeType(NULL_TYPE));

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = getNativeType(NULL_TYPE);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  @Test
  public void testIsDefFunction() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType type = getNativeType(OBJECT_TYPE);
    when(typeRegistry.createTopType()).thenReturn(type);

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();
    when(typeRegistry.createFunctionType(new ObjectType(type), VOID_TYPE)).thenReturn(type);

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = getNativeType(OBJECT_TYPE);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  @Test
  public void testIsDefArray() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType elementType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType type = typeRegistry.createArrayType(elementType, -1);

    when(typeRegistry.createTopType()).thenReturn(type);
    when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType expectedType = arrayType.getGreatestSubtype(type);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  @Test
  public void testIsDefArrayFalse() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType elementType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType type = typeRegistry.createArrayType(elementType, -1);

    when(typeRegistry.createTopType()).thenReturn(type);
    when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  @Test
  public void testIsDefArrayNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType elementType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType type = typeRegistry.createArrayType(elementType, -1);

    when(typeRegistry.createTopType()).thenReturn(type);
    when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);
    when(typeRegistry.createNullType()).thenReturn(getNativeType(NULL_TYPE));

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = getNativeType(NULL_TYPE);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  @Test
  public void testIsDefArrayFunction() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType elementType = getNativeType(OBJECT_TYPE);
    JSType functionType = typeRegistry.createFunctionType(new ObjectType(elementType),
        VOID_TYPE);
    JSType type = typeRegistry.createArrayType(functionType, -1);

    when(typeRegistry.createTopType()).thenReturn(type);
    when(typeRegistry.getNativeType(ARRAY_TYPE)).thenReturn(arrayType);

    Node condition = createCondition("goog.isDef(x)", "x");
    FlowScope blindScope = createBlindScope();

    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType expectedType = getNativeType(OBJECT_TYPE);
    assertEquals(expectedType, informedScope.getVariableType("x"));
  }

  private Node createCondition(String conditionString, String... args) {
    Node condition = new Node_Call(conditionString, new NodeVector(args));
    return condition;
  }

  private FlowScope createBlindScope() {
    return new FlowScope(new DeclaredVariables());
  }

  private static JSType getNativeType(int typeCode) {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    return typeRegistry.getNativeType(typeCode);
  }

  private static JSType getNativeType(String typeName) {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    return typeRegistry.getNativeType(typeName);
  }
}