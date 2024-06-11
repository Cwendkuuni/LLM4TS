import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.FunctionType;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;
  private CodingConvention convention;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    convention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDef() {
    JSType type = getNativeType(ObjectType.class);
    Node condition = createNode("goog.isDef(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isDef(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsString() {
    JSType type = getNativeType(String.class);
    Node condition = createNode("goog.isString(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isString(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsNumber() {
    JSType type = getNativeType(Number.class);
    Node condition = createNode("goog.isNumber(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isNumber(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsBoolean() {
    JSType type = getNativeType(Boolean.class);
    Node condition = createNode("goog.isBoolean(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isBoolean(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsFunction() {
    JSType type = getNativeType(FunctionType.class);
    Node condition = createNode("goog.isFunction(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isFunction(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsObject() {
    JSType type = getNativeType(ObjectType.class);
    Node condition = createNode("goog.isObject(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isObject(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsArray() {
    JSType type = getNativeType(ObjectType.class).getSubtype(ARRAY_TYPE);
    Node condition = createNode("goog.isArray(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isArray(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSType type = getNativeType(ObjectType.class);
    Node condition = createNode("goog.isDefAndNotNull(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isDefAndNotNull(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsNull() {
    JSType type = getNativeType(NullType.class);
    Node condition = createNode("goog.isNull(x)", "x");
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(type, result);

    condition = createNode("goog.isNull(x)", "x");
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsDefAndNotNullWithNullType() {
    JSType type = getNativeType(NullType.class);
    Node condition = createNode("goog.isDefAndNotNull(x)", null);
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);

    condition = createNode("goog.isDefAndNotNull(x)", null);
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  @Test
  public void testIsDefAndNotNullWithUndefinedType() {
    JSType type = getNativeType(UndefinedType.class);
    Node condition = createNode("goog.isDefAndNotNull(x)", null);
    FlowScope blindScope = new FlowScope(null, null, null);

    JSType result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);

    condition = createNode("goog.isDefAndNotNull(x)", null);
    blindScope = new FlowScope(null, null, null);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false).getBinding(condition.getFirstChild().getString()).getType();
    assertEquals(VOID_TYPE, result);
  }

  private Node createNode(String expression, Object... arguments) {
    Node node = new Node_();
    node.setType(typeRegistry.createExpressionType(expression));
    NodeList args = new NodeList();
    for (Object arg : arguments) {
      args.appendChild(new Node_().setString(arg.toString()));
    }
    node.setFirstChild(new Node_().setType(typeRegistry.createNameType("goog")))
        .setNextSibling(new Node_().setType(typeRegistry.createNameType("isDef")))
        .setNextSibling(new Node_().setType(typeRegistry.createGetPropType("", "")))
        .setNextSibling(args);
    return node;
  }

  private static JSType getNativeType(Class<?> clazz) {
    return typeRegistry.getType(clazz);
  }

  private static JSType getNativeType(int type) {
    return typeRegistry.getNativeType(type);
  }
}