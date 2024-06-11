import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDef"), Node.newLiteral(1));
    Node blindScope = Node.newRoot();

    JSType expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();
    JSType actualType = JSTypeNative.INT_TYPE;

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDef"), Node.newNull());
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDef"), Node.newLiteral(null));
    blindScope = Node.newRoot();

    expectedType = JSTypeNative.NULL_TYPE;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDef"), Node.newLiteral(new Object()));
    blindScope = Node.newRoot();

    expectedType = JSTypeNative.OBJECT_TYPE;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDef"), Node.newLiteral(new FunctionType()));
    blindScope = Node.newRoot();

    expectedType = JSTypeNative.FUNCTION_TYPE;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDefAndNotNull"), Node.newLiteral(1));
    Node blindScope = Node.newRoot();

    JSType expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();
    JSType actualType = JSTypeNative.INT_TYPE
        .and(JSTypeNative.NOT_NULL_TYPE);

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDefAndNotNull"), Node.newNull());
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDefAndNotNull"), Node.newLiteral(new Object()));
    blindScope = Node.newRoot();

    expectedType = JSTypeNative.OBJECT_TYPE
        .and(JSTypeNative.NOT_NULL_TYPE);
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isDefAndNotNull"), Node.newLiteral(new FunctionType()));
    blindScope = Node.newRoot();

    expectedType = JSTypeNative.FUNCTION_TYPE
        .and(JSTypeNative.NOT_NULL_TYPE);
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("1").getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isString"), Node.newString("abc"));
    Node blindScope = Node.newRoot();

    JSType expectedType = JSTypeNative.STRING_TYPE;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("abc").getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isString"), Node.newNull());
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("abc").getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isString"), Node.newLiteral(1));
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("abc").getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isString"), Node.newLiteral(new Object()));
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("abc").getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isBoolean"), Node.newBoolean(true));
    Node blindScope = Node.newRoot();

    JSType expectedType = JSTypeNative.BOOLEAN_TYPE;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(true).getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isBoolean"), Node.newBoolean(false));
    blindScope = Node.newRoot();

    expectedType = JSTypeNative.BOOLEAN_TYPE;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(false).getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isBoolean"), Node.newNull());
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(true).getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isBoolean"), Node.newLiteral(1));
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(true).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isNumber"), Node.newNumber(1));
    Node blindScope = Node.newRoot();

    JSType expectedType = JSTypeNative.NUMBER_TYPE;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(1).getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isNumber"), Node.newNull());
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(1).getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isNumber"), Node.newLiteral(true));
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(1).getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isFunction"), Node.newFunction(new ObjectType(), null));
    Node blindScope = Node.newRoot();

    JSType expectedType = JSTypeNative.FUNCTION_TYPE;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(function -> function.getType())
            .getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isFunction"), Node.newNull());
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(function -> function.getType())
            .getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isArray"), Node.newLiteral(new ObjectType()));
    Node blindScope = Node.newRoot();

    JSType expectedType = null;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("Object")
            .getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isArray"), Node.newArrayType(ObjectType.getInstance(), null, null));
    blindScope = Node.newRoot();

    expectedType = JSTypeNative.ARRAY_TYPE;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("Array<Object>")
            .getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isArray"), Node.newNull());
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("Array<Object>")
            .getType();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);

    ChainableReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isObject"), Node.newObjectType(new ObjectType()));
    Node blindScope = Node.newRoot();

    JSType expectedType = JSTypeNative.OBJECT_TYPE;
    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable("Object")
            .getType();

    assertEquals(expectedType, actualType);

    condition = Node.newCall(Node.newGetProp(Node.newLiteral("goog"),
        "isObject"), Node.newFunctionType(new ObjectType(), null, null));
    blindScope = Node.newRoot();

    expectedType = null;
    actualType = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true).getScopeVariable(function -> function.getType())
            .getType();

    assertEquals(expectedType, actualType);
  }
}