import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    JSType arrayType = JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE);
    when(typeRegistry.getType(JSTypeNative.ARRAY_TYPE)).thenReturn(arrayType);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;

    ObjectType objectType = mock(ObjectType.class);
    when(objectType.isSubtype(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(false);
    when(objectType.isSubtype(arrayType)).thenReturn(true);

    JSType result = objectType.visit(visitor);
    assertSame(arrayType, result);

    JSType nullType = JSTypeNative.NULL_TYPE;
    JSType notArrayType = mock(JSType.class);
    when(notArrayType.isSubtype(arrayType)).thenReturn(false);

    result = nullType.visit(visitor);
    assertNull(result);

    result = notArrayType.visit(visitor);
    assertNull(result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    JSType arrayType = JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotArrayVisitor;

    ObjectType objectType = mock(ObjectType.class);
    when(objectType.isSubtype(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(false);
    when(objectType.isSubtype(arrayType)).thenReturn(true);

    JSType result = objectType.visit(visitor);
    assertNull(result);

    JSType nullType = JSTypeNative.NULL_TYPE;
    JSType notArrayType = mock(JSType.class);
    when(notArrayType.isSubtype(arrayType)).thenReturn(false);

    result = nullType.visit(visitor);
    assertNull(result);

    result = notArrayType.visit(visitor);
    assertSame(notArrayType, result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ObjectType objectType = mock(ObjectType.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;

    FunctionType functionType = mock(FunctionType.class);

    JSType result = functionType.visit(visitor);
    assertSame(functionType, result);

    JSType arrayType = JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE);
    result = arrayType.visit(visitor);
    assertNull(result);

    result = objectType.visit(visitor);
    assertSame(objectType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ObjectType objectType = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;

    JSType result = functionType.visit(visitor);
    assertNull(result);

    result = objectType.visit(visitor);
    assertNull(result);

    result = arrayType.visit(visitor);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    JSType objectType = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);
    ObjectType arrayType = JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);

    Node condition = mock(Node.class);
    Node left = mock(Node.class);
    Node right = mock(Node.class);
    when(condition.getChildCount()).thenReturn(2);
    when(condition.getFirstChild()).thenReturn(left);
    when(condition.getLastChild()).thenReturn(right);
    when(left.getType()).thenReturn(NAME);
    when(left.getString()).thenReturn("goog");
    when(right.getType()).thenReturn(STRING);

    when(typeRegistry.getType(NAME, "goog")).thenReturn(new ObjectType(
        "goog", ImmutableMap.of(), null, null));
    when(typeRegistry.getType(STRING)).thenReturn(new ObjectType(
        "String", ImmutableMap.of(), null, null));

    when(left.getFirstChild()).thenReturn(mock(Node.class));
    when(left.getLastChild()).thenReturn(right);

    when(right.getString()).thenReturn("isDef");

    FlowScope blindScope = mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);
    assertSame(blindScope, result);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);
  }
}