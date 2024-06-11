import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final String GOOG_ISDEF = "goog.isDef";
  private static final String GOOG_ISNULL = "goog.isNull";
  private static final String GOOG_ISDEF_AND_NOTNULL = "goog.isDefAndNotNull";
  private static final String GOOG_ISSTRING = "goog.isString";
  private static final String GOOG_ISBOOLEAN = "goog.isBoolean";
  private static final String GOOG_ISNUMBER = "goog.isNumber";
  private static final String GOOG_ISFUNCTION = "goog.isFunction";
  private static final String GOOG_ISARRAY = "goog.isArray";
  private static final String GOOG_ISOBJECT = "goog.isObject";

  private ClosureReverseAbstractInterpreter interpreter;
  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    convention = mock(CodingConvention.class);
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    Node condition = createConditionNode("isDef", true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.BOOLEAN_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsDefFalse() {
    Node condition = createConditionNode("isDef", false);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsDefNullParameter() {
    Node condition = createConditionNode("isDef", true, null);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.BOOLEAN_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsDefNullParameterFalse() {
    Node condition = createConditionNode("isDef", false, null);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsNonNull() {
    Node condition = createConditionNode("isNull", true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.NULL_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsNonNullFalse() {
    Node condition = createConditionNode("isNull", false);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsDefAndNotNull() {
    Node condition = createConditionNode("isDefAndNotNull", true, null);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.BOOLEAN_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    Node condition = createConditionNode("isDefAndNotNull", false, null);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsString() {
    Node condition = createConditionNode("isString", true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.STRING_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsStringFalse() {
    Node condition = createConditionNode("isString", false);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsBoolean() {
    Node condition = createConditionNode("isBoolean", true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.BOOLEAN_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsBooleanFalse() {
    Node condition = createConditionNode("isBoolean", false);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsNumber() {
    Node condition = createConditionNode("isNumber", true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.NUMBER_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsNumberFalse() {
    Node condition = createConditionNode("isNumber", false);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsFunction() {
    Node condition = createConditionNode("isFunction", true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.FUNCTION_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsFunctionFalse() {
    Node condition = createConditionNode("isFunction", false);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsArray() {
    Node condition = createConditionNode("isArray", true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.ARRAY_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsArrayFalse() {
    Node condition = createConditionNode("isArray", false);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsObject() {
    Node condition = createConditionNode("isObject", true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.OBJECT_TYPE);
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true);
    assertSame(expectedType, actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  @Test
  public void testIsObjectFalse() {
    Node condition = createConditionNode("isObject", false);
    JSType expectedType = null;
    FlowScope actualScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false);
    assertNull(actualScope.getDeclaredType(condition.getFirstChild().getName()));
  }

  private Node createConditionNode(String methodName, boolean outcome, Node parameter) {
    Node callee = mock(Node.class);
    when(callee.getType()).thenReturn(new ObjectType(JSTypeRegistry.DEFAULT_LIBRARIES));
    when(callee.getFirstChild()).thenReturn(createNameNode(GOOG_ISDEF, methodName));
    when(callee.getLastChild()).thenReturn(parameter == null ? mock(Node.class) : parameter);

    Node condition = mock(Node.class);
    when(condition.getType()).thenReturn(new FunctionType(0, JSTypeRegistry.DEFAULT_LIBRARIES));
    when(condition.getFirstChild()).thenReturn(callee);

    return condition;
  }

  private Node createNameNode(String name, String methodName) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(STRING);
    when(node.getString()).thenReturn(name);

    Node object = mock(Node.class);
    when(object.getType()).thenReturn(new ObjectType(JSTypeRegistry.DEFAULT_LIBRARIES));

    Node index = mock(Node.class);
    when(index.getType()).thenReturn(STRING);
    when(index.getString()).thenReturn(methodName);

    Node qualifiedName = mock(Node.class);
    when(qualifiedName.getType()).thenReturn(new QualifiedNameType(JSTypeRegistry.DEFAULT_LIBRARIES));
    when(qualifiedName.getFirstChild()).thenReturn(object);
    when(qualifiedName.getLastChild()).thenReturn(index);

    when(node.getParent()).thenReturn(qualifiedName);

    return node;
  }
}