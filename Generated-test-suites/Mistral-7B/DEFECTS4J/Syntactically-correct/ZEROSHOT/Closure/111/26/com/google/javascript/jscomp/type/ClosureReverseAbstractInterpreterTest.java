import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.jstype.JSTypeRegistry.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static JSTypeRegistry typeRegistry;
  private static CodingConvention codingConvention;
  private static ClosureReverseAbstractInterpreter interpreter;

  @org.junit.BeforeClass
  public static void setUp() {
    typeRegistry = new JSTypeRegistry();
    codingConvention = new CodingConvention();
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @org.junit.Test
  public void testIsDef() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedType = interpreter.getRestrictedWithoutUndefined(originalType);
    assertEquals(NUMBER_STRING_BOOLEAN, restrictedType);

    JSType undefinedType = getNativeType(VOID_TYPE).getGreatestSubtype(
        getNativeType(NULL_TYPE));
    JSType isDefType = interpreter.getRestrictedWithoutUndefined(undefinedType);
    assertNull(isDefType);

    JSType nullType = getNativeType(NULL_TYPE);
    JSType isDefNullType = interpreter.getRestrictedWithoutUndefined(nullType);
    assertEquals(VOID_TYPE, isDefNullType);
  }

  @org.junit.Test
  public void testIsDefAndNotNull() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedType = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(originalType));
    assertEquals(NUMBER_STRING_BOOLEAN, restrictedType);

    JSType nullType = getNativeType(NULL_TYPE);
    JSType isDefAndNotNullType = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(nullType));
    assertEquals(VOID_TYPE, isDefAndNotNullType);

    JSType nonNullNumberType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType isDefAndNotNullNumberType = interpreter.getRestrictedWithoutUndefined(
        interpreter.getRestrictedWithoutNull(nonNullNumberType));
    assertEquals(NUMBER_STRING_BOOLEAN, isDefAndNotNullNumberType);
  }

  @org.junit.Test
  public void testIsString() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedType = interpreter.getRestrictedByTypeOfResult(
        originalType, "string", true);
    assertNull(restrictedType);

    JSType stringType = getNativeType("string");
    JSType isStringType = interpreter.getRestrictedByTypeOfResult(
        originalType, "string", false);
    assertEquals(getNativeType("string"), isStringType);
  }

  @org.junit.Test
  public void testIsBoolean() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedType = interpreter.getRestrictedByTypeOfResult(
        originalType, "boolean", true);
    assertNull(restrictedType);

    JSType booleanType = getNativeType("boolean");
    JSType isBooleanType = interpreter.getRestrictedByTypeOfResult(
        originalType, "boolean", false);
    assertEquals(getNativeType("boolean"), isBooleanType);
  }

  @org.junit.Test
  public void testIsNumber() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedType = interpreter.getRestrictedByTypeOfResult(
        originalType, "number", true);
    assertNull(restrictedType);

    JSType numberType = getNativeType("number");
    JSType isNumberType = interpreter.getRestrictedByTypeOfResult(
        originalType, "number", false);
    assertEquals(getNativeType("number"), isNumberType);
  }

  @org.junit.Test
  public void testIsFunction() {
    JSType originalType = getNativeType(FUNCTION_TYPE);
    JSType restrictedType = interpreter.getRestrictedByTypeOfResult(
        originalType, "function", true);
    assertEquals(originalType, restrictedType);

    JSType nonFunctionType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType isFunctionType = interpreter.getRestrictedByTypeOfResult(
        nonFunctionType, "function", false);
    assertNull(isFunctionType);
  }

  @org.junit.Test
  public void testIsArray() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedArrayType = interpreter.getRestrictedByTypeOfResult(
        originalType, "array", true);
    assertNull(restrictedArrayType);

    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType isArrayType = interpreter.getRestrictedByTypeOfResult(
        originalType, "array", false);
    assertEquals(arrayType, isArrayType);

    JSType nonArrayType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType isArrayNullType = interpreter.getRestrictedByTypeOfResult(
        nonArrayType, "array", true);
    assertNull(isArrayNullType);
  }

  @org.junit.Test
  public void testIsObject() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedObjectType = interpreter.getRestrictedByTypeOfResult(
        originalType, "object", true);
    assertNull(restrictedObjectType);

    JSType objectType = getNativeType(OBJECT_TYPE);
    JSType isObjectType = interpreter.getRestrictedByTypeOfResult(
        originalType, "object", false);
    assertEquals(objectType, isObjectType);

    JSType nonObjectType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType isObjectNullType = interpreter.getRestrictedByTypeOfResult(
        nonObjectType, "object", true);
    assertNull(isObjectNullType);
  }

  @org.junit.Test
  public void testIsNull() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType restrictedNullType = interpreter.getRestrictedWithoutNull(originalType);
    assertEquals(NULL_TYPE, restrictedNullType);

    JSType nullType = getNativeType(NULL_TYPE);
    JSType isNullType = interpreter.getRestrictedWithoutNull(nullType);
    assertEquals(VOID_TYPE, isNullType);

    JSType nonNullType = getNativeType(NUMBER_STRING_BOOLEAN);
    JSType isNullNonNullType = interpreter.getRestrictedWithoutNull(nonNullType);
    assertEquals(NULL_TYPE, isNullNonNullType);
  }

  @org.junit.Test
  public void testRestrictParameter() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return getNativeType(ARRAY_TYPE);
        } else {
          return null;
        }
      }
    };

    Node parameter = StringLiteral("param");
    FlowScope blindScope = NodeOperations.createScope();
    JSType paramType = getNativeType(NUMBER_STRING_BOOLEAN);

    FlowScope restrictedScope = interpreter.restrictParameter(
        parameter, paramType, blindScope, restriction, true);
    assertNotNull(restrictedScope);
    JSType restrictedType = restrictedScope.getScopeValue(parameter);
    assertEquals(getNativeType(ARRAY_TYPE), restrictedType);

    restrictedScope = interpreter.restrictParameter(
        parameter, paramType, blindScope, restriction, false);
    assertNotNull(restrictedScope);
    restrictedType = restrictedScope.getScopeValue(parameter);
    assertNull(restrictedType);
  }

  @org.junit.Test
  public void testIsDefAndNotNullAndRestrict() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return getNativeType(OBJECT_TYPE);
        } else {
          return null;
        }
      }
    };

    Node parameter = StringLiteral("param");
    FlowScope blindScope = NodeOperations.createScope();
    JSType paramType = getNativeType(NUMBER_STRING_BOOLEAN);

    FlowScope restrictedScope = interpreter.restrictParameter(
        parameter, paramType, blindScope, restriction, true);
    assertNotNull(restrictedScope);
    JSType restrictedType = restrictedScope.getScopeValue(parameter);
    assertEquals(getNativeType(OBJECT_TYPE), restrictedType);

    restrictedScope = interpreter.restrictParameter(
        parameter, paramType, blindScope, restriction, false);
    assertNotNull(restrictedScope);
    restrictedType = restrictedScope.getScopeValue(parameter);
    assertNull(restrictedType);
  }

  @org.junit.Test
  public void testIsDefAndNotNullAndRestrictWithOutcomeFalse() {
    JSType originalType = getNativeType(NUMBER_STRING_BOOLEAN);
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        if (p.outcome) {
          return getNativeType(OBJECT_TYPE);
        } else {
          return null;
        }
      }
    };

    Node parameter = StringLiteral("param");
    FlowScope blindScope = NodeOperations.createScope();
    JSType paramType = getNativeType(NUMBER_STRING_BOOLEAN);

    FlowScope restrictedScope = interpreter.restrictParameter(
        parameter, paramType, blindScope, restriction, false);
    assertNotNull(restrictedScope);
    JSType restrictedType = restrictedScope.getScopeValue(parameter);
    assertEquals(VOID_TYPE, restrictedType);

    restrictedScope = interpreter.restrictParameter(
        parameter, paramType, blindScope, restriction, true);
    assertNotNull(restrictedScope);
    restrictedType = restrictedScope.getScopeValue(parameter);
    assertEquals(getNativeType(OBJECT_TYPE), restrictedType);
  }
}