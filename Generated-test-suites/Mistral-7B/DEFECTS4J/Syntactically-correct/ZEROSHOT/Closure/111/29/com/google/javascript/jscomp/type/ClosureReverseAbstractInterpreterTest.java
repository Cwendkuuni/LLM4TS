import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testIsDef() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = VOID_TYPE;
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricticters.get("isDef").apply(restriction);
    assertEquals(type, result);

    restriction = new TypeRestriction( type, false);
    result = interpreter.restricticters.get("isDef").apply(restriction);
    assertNull(result);
  }

  @Test
  public void testIsDefAndNotNull() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType type = BOOL_TYPE;
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricticters.get("isDefAndNotNull").apply(restriction);
    assertEquals(type, result);

    restriction = new TypeRestriction( type, false);
    result = interpreter.restricticters.get("isDefAndNotNull").apply(restriction);
    assertNull(result);
  }

  @Test
  public void testIsFunction() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    FunctionType functionType = Mockito.mock(FunctionType.class);
    JSType type = functionType;
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricticters.get("isFunction").apply(restriction);
    assertEquals(type, result);

    restriction = new TypeRestriction( type, false);
    result = interpreter.restricticters.get("isFunction").apply(restriction);
    assertNull(result);
  }

  @Test
  public void testIsArray() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    ObjectType arrayType = Mockito.mock(ObjectType.class);
    JSType trueType = ARRAY_TYPE;
    TypeRestriction restriction = new TypeRestriction(null, true);
    JSType result = interpreter.getRestrictToArrayVisitor().visit(arrayType);
    assertEquals(trueType, result);

    restriction = new TypeRestriction(null, false);
    result = interpreter.getRestrictToNotArrayVisitor().visit(arrayType);
    assertNull(result);
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    ObjectType objectType = Mockito.mock(ObjectType.class);
    JSType trueType = OBJECT_TYPE;
    TypeRestriction restriction = new TypeRestriction(objectType, true);
    JSType result = interpreter.getRestrictToObjectVisitor().visit(objectType);
    assertEquals(trueType, result);

    restriction = new TypeRestriction(objectType, false);
    result = interpreter.getRestrictToNotObjectVisitor().visit(objectType);
    assertNull(result);
  }

  @Test
  public void testIsString() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType stringType = String.class.getTypeName();
    TypeRestriction restriction = new TypeRestriction(stringType, true);
    JSType result = interpreter.getRestrictedByTypeOfResult(stringType, "string", true);
    assertEquals(stringType, result);

    restriction = new TypeRestriction(stringType, false);
    result = interpreter.getRestrictedByTypeOfResult(stringType, "string", false);
    assertNull(result);
  }

  @Test
  public void testIsBoolean() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType booleanType = BOOL_TYPE;
    TypeRestriction restriction = new TypeRestriction(booleanType, true);
    JSType result = interpreter.getRestrictedByTypeOfResult(booleanType, "boolean", true);
    assertEquals(booleanType, result);

    restriction = new TypeRestriction(booleanType, false);
    result = interpreter.getRestrictedByTypeOfResult(booleanType, "boolean", false);
    assertNull(result);
  }

  @Test
  public void testIsNumber() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    JSType numberType = Number.class.getTypeName();
    TypeRestriction restriction = new TypeRestriction(numberType, true);
    JSType result = interpreter.getRestrictedByTypeOfResult(numberType, "number", true);
    assertEquals(numberType, result);

    restriction = new TypeRestriction(numberType, false);
    result = interpreter.getRestrictedByTypeOfResult(numberType, "number", false);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    JSTypeRegistry typeRegistry = Mockito.mock(JSTypeRegistry.class);
    CodingConvention convention = Mockito.mock(CodingConvention.class);
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    Node condition = Mockito.mock(Node.class);
    FlowScope blindScope = Mockito.mock(FlowScope.class);
    boolean outcome = true;

    Mockito.when(condition.isCall()).thenReturn(true);
    Mockito.when(condition.getChildCount()).thenReturn(2);
    Mockito.when(condition.getFirstChild().isName()).thenReturn(true);
    Mockito.when(condition.getFirstChild().getString()).thenReturn("goog");
    Mockito.when(condition.getLastChild().isQualifiedName()).thenReturn(true);
    Mockito.when(condition.getLastChild().getString()).thenReturn("isDef");

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    assertEquals(blindScope, result);

    Mockito.verify(condition).isCall();
    Mockito.verify(condition).getChildCount();
    Mockito.verify(condition).getFirstChild().isName();
    Mockito.verify(condition).getFirstChild().getString().equals("goog");
    Mockito.verify(condition).getLastChild().isQualifiedName();
    Mockito.verify(condition).getLastChild().getString().equals("isDef");
  }
}