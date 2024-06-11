import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry TYPE_REGISTRY = new JSTypeRegistry();
  private static final CodingConvention CONVENTION = new CodingConvention();

  @org.junit.Test
  public void testIsDef() {
    JSType type = TYPE_REGISTRY.getNativeType(Object.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isDef"),
        NodeFactory.createThis(), type);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, true);

    JSType expected = interpreter.getRestrictedWithoutUndefined(type);
    assertEquals(expected, informed.getDeclaredTypes().get(0));
  }

  @org.junit.Test
  public void testIsDefFalse() {
    JSType type = TYPE_REGISTRY.getNativeType(Object.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isDef"),
        NodeFactory.createThis(), NodeFactory.createNull());

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, false);

    assertEquals(null, informed.getDeclaredTypes());
  }

  @org.junit.Test
  public void testIsDefAndNotNull() {
    JSType type = TYPE_REGISTRY.getNativeType(Object.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isDefAndNotNull"),
        NodeFactory.createThis(), type, NodeFactory.createNull());

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, false);

    assertEquals(null, informed.getDeclaredTypes());
  }

  @org.junit.Test
  public void testIsDefAndNotNullTrue() {
    JSType type = TYPE_REGISTRY.getNativeType(Object.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isDefAndNotNull"),
        NodeFactory.createThis(), NodeFactory.createNull(), NodeFactory.createUndefined());

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, true);

    JSType expected = interpreter.getRestrictedWithoutUndefined(type);
    assertEquals(expected, informed.getDeclaredTypes().get(0));
  }

  @org.junit.Test
  public void testIsString() {
    JSType type = TYPE_REGISTRY.getNativeType(String.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isString"),
        NodeFactory.createThis(), NodeFactory.createString("test"));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, true);

    JSType expected = type;
    assertEquals(expected, informed.getDeclaredTypes().get(0));
  }

  @org.junit.Test
  public void testIsStringFalse() {
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isString"),
        NodeFactory.createThis(), NodeFactory.createNumber(1));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, false);

    assertEquals(null, informed.getDeclaredTypes());
  }

  @org.junit.Test
  public void testIsArray() {
    JSType type = TYPE_REGISTRY.getNativeType(List.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isArray"),
        NodeFactory.createThis(), NodeFactory.createNull());

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, false);

    assertEquals(null, informed.getDeclaredTypes());
  }

  @org.junit.Test
  public void testIsArrayTrue() {
    JSType type = TYPE_REGISTRY.getNativeType(List.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isArray"),
        NodeFactory.createThis(), NodeFactory.createArray(new Object[]{1, 2, 3}));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, true);

    JSType expected = type;
    assertEquals(expected, informed.getDeclaredTypes().get(0));
  }

  @org.junit.Test
  public void testIsObject() {
    JSType type = TYPE_REGISTRY.getNativeType(Object.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isObject"),
        NodeFactory.createThis(), NodeFactory.createUndefined());

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, false);

    assertEquals(null, informed.getDeclaredTypes());
  }

  @org.junit.Test
  public void testIsObjectTrue() {
    JSType type = TYPE_REGISTRY.getNativeType(Object.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isObject"),
        NodeFactory.createThis(), NodeFactory.createFunction(
            ImmutableList.of(NodeFactory.createName("arg")), new Function() {
              public Object call(Object arg) {
                return null;
              }
            }));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, true);

    JSType expected = type;
    assertEquals(expected, informed.getDeclaredTypes().get(0));
  }

  @org.junit.Test
  public void testIsFunction() {
    JSType type = TYPE_REGISTRY.getNativeType(Function.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isFunction"),
        NodeFactory.createThis(), NodeFactory.createFunction(
            ImmutableList.of(NodeFactory.createName("arg")), new Function() {
              public Object call(Object arg) {
                return null;
              }
            }));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, true);

    JSType expected = type;
    assertEquals(expected, informed.getDeclaredTypes().get(0));
  }

  @org.junit.Test
  public void testIsFunctionFalse() {
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isFunction"),
        NodeFactory.createThis(), NodeFactory.createString("test"));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, false);

    assertEquals(null, informed.getDeclaredTypes());
  }

  @org.junit.Test
  public void testIsBoolean() {
    JSType type = TYPE_REGISTRY.getNativeType(Boolean.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isBoolean"),
        NodeFactory.createThis(), NodeFactory.createBoolean(true));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, true);

    JSType expected = type;
    assertEquals(expected, informed.getDeclaredTypes().get(0));
  }

  @org.junit.Test
  public void testIsBooleanFalse() {
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isBoolean"),
        NodeFactory.createThis(), NodeFactory.createNumber(1));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, false);

    assertEquals(null, informed.getDeclaredTypes());
  }

  @org.junit.Test
  public void testIsNumber() {
    JSType type = TYPE_REGISTRY.getNativeType(Number.class);
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isNumber"),
        NodeFactory.createThis(), NodeFactory.createNumber(1));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, true);

    JSType expected = type;
    assertEquals(expected, informed.getDeclaredTypes().get(0));
  }

  @org.junit.Test
  public void testIsNumberFalse() {
    Node condition = NodeFactory.createCall(
        NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isNumber"),
        NodeFactory.createThis(), NodeFactory.createString("test"));

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        CONVENTION, TYPE_REGISTRY);

    FlowScope scope = NodeFactory.createRootScope();
    FlowScope informed = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, scope, false);

    assertEquals(null, informed.getDeclaredTypes());
  }
}