import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;
  private JSType arrayType;
  private JSType nullType;
  private JSType objectType;
  private JSType functionType;
  private Function<TypeRestriction, JSType> isDefRestrictor;
  private Function<TypeRestriction, JSType> isNullRestrictor;
  private Function<TypeRestriction, JSType> isDefAndNotNullRestrictor;
  private Function<TypeRestriction, JSType> isStringRestrictor;
  private Function<TypeRestriction, JSType> isBooleanRestrictor;
  private Function<TypeRestriction, JSType> isNumberRestrictor;
  private Function<TypeRestriction, JSType> isFunctionRestrictor;
  private Function<TypeRestriction, JSType> isArrayRestrictor;
  private Function<TypeRestriction, JSType> isObjectRestrictor;

  @Before
  public void setup() {
    typeRegistry = mock(JSTypeRegistry.class);
    convention = mock(CodingConvention.class);
    arrayType = mock(JSType.class);
    nullType = mock(JSType.class);
    objectType = mock(JSType.class);
    functionType = mock(JSType.class);

    when(typeRegistry.getNativeType(Arrays.asList(Arrays.asList(ARRAY_TYPE)))).thenReturn(arrayType);
    when(typeRegistry.getNativeType(Arrays.asList(Arrays.asList(NO_OBJECT_TYPE)))).thenReturn(nullType);
    when(typeRegistry.getNativeType(Arrays.asList(Arrays.asList(OBJECT_TYPE)))).thenReturn(objectType);
    when(typeRegistry.getNativeType(Arrays.asList(Arrays.asList(FUNCTION_TYPE)))).thenReturn(functionType);

    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    isDefRestrictor = interpreter.restricters.get("isDef");
    isNullRestrictor = interpreter.restricters.get("isNull");
    isDefAndNotNullRestrictor = interpreter.restricters.get("isDefAndNotNull");
    isStringRestrictor = interpreter.restricters.get("isString");
    isBooleanRestrictor = interpreter.restricters.get("isBoolean");
    isNumberRestrictor = interpreter.restricters.get("isNumber");
    isFunctionRestrictor = interpreter.restricters.get("isFunction");
    isArrayRestrictor = interpreter.restricters.get("isArray");
    isObjectRestrictor = interpreter.restricters.get("isObject");
  }

  @Test
  public void testRestrictToArray() {
    JSType inputType = mock(JSType.class);
    when(inputType.isSubtype(arrayType)).thenReturn(true);

    JSType expectedType = arrayType;

    JSType actualType = isArrayRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotArray() {
    JSType inputType = mock(JSType.class);
    when(inputType.isSubtype(arrayType)).thenReturn(false);

    JSType expectedType = null;

    JSType actualType = isArrayRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToObject() {
    JSType inputType = mock(JSType.class);
    when(inputType.isSubtype(objectType)).thenReturn(true);

    JSType expectedType = objectType;

    JSType actualType = isObjectRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotObject() {
    JSType inputType = mock(JSType.class);
    when(inputType.isSubtype(objectType)).thenReturn(false);

    JSType expectedType = null;

    JSType actualType = isObjectRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToFunction() {
    JSType inputType = mock(JSType.class);
    when(inputType.isSubtype(functionType)).thenReturn(true);

    JSType expectedType = functionType;

    JSType actualType = isFunctionRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotFunction() {
    JSType inputType = mock(JSType.class);
    when(inputType.isSubtype(functionType)).thenReturn(false);

    JSType expectedType = null;

    JSType actualType = isFunctionRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToString() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = mock(JSType.class);
    when(expectedType.toString()).thenReturn("string");

    when(inputType.isSubtype(expectedType)).thenReturn(true);

    JSType actualType = isStringRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotString() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = null;

    JSType actualType = isStringRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToBoolean() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = mock(JSType.class);
    when(expectedType.toString()).thenReturn("boolean");

    when(inputType.isSubtype(expectedType)).thenReturn(true);

    JSType actualType = isBooleanRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotBoolean() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = null;

    JSType actualType = isBooleanRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNumber() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = mock(JSType.class);
    when(expectedType.toString()).thenReturn("number");

    when(inputType.isSubtype(expectedType)).thenReturn(true);

    JSType actualType = isNumberRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotNumber() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = null;

    JSType actualType = isNumberRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNull() {
    JSType inputType = nullType;

    JSType expectedType = nullType;

    JSType actualType = isNullRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotNull() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = null;

    JSType actualType = isNullRestrictor.apply(new TypeRestriction(inputType, false)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToDef() {
    JSType inputType = mock(JSType.class);

    when(inputType.isSubtype(objectType)).thenReturn(true);

    JSType expectedType = getRestrictedWithoutUndefined(inputType);

    JSType actualType = isDefRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotDef() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = null;

    JSType actualType = isDefRestrictor.apply(new TypeRestriction(inputType, false)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToDefAndNotNull() {
    JSType inputType = mock(JSType.class);

    when(inputType.isSubtype(objectType)).thenReturn(true);

    JSType expectedType = getRestrictedWithoutUndefined(getRestrictedWithoutNull(inputType));

    JSType actualType = isDefAndNotNullRestrictor.apply(new TypeRestriction(inputType, true)).get();

    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToNotDefAndNotNull() {
    JSType inputType = mock(JSType.class);

    JSType expectedType = null;

    JSType actualType = isDefAndNotNullRestrictor.apply(new TypeRestriction(inputType, false)).get();

    assertEquals(expectedType, actualType);
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    Visitor<JSType> visitor = new Visitor<JSType>() {
      @Override
      public JSType visitTopType(JSType type) {
        // Ideally, we would like to return any subtype that doesn't have
        // the "undefined" type in it. Since that's not possible, we
        // just return the original type.
        return type;
      }

      @Override
      public JSType visitObjectType(ObjectType objType) {
        ObjectType newObjType = new ObjectType(objType.getName(), new ImmutableMap.Builder<String, JSType>()
            .putAll(objType.getProperties())
            .build());
        newObjType.setUndefined(false);
        return newObjType;
      }

      @Override
      public JSType visitFunctionType(FunctionType funcType) {
        FunctionType newFuncType = new FunctionType(funcType.getReturnType(),
            funcType.getParameterTypes());
        newFuncType.setUndefined(false);
        return newFuncType;
      }
    };

    return type.visit(visitor);
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    Visitor<JSType> visitor = new Visitor<JSType>() {
      @Override
      public JSType visitTopType(JSType type) {
        if (type == nullType) {
          return null;
        }
        return type;
      }

      @Override
      public JSType visitObjectType(ObjectType objType) {
        ObjectType newObjType = new ObjectType(objType.getName(), new ImmutableMap.Builder<String, JSType>()
            .putAll(objType.getProperties())
            .build());
        newObjType.setNull(false);
        return newObjType;
      }

      @Override
      public JSType visitFunctionType(FunctionType funcType) {
        FunctionType newFuncType = new FunctionType(funcType.getReturnType(),
            funcType.getParameterTypes());
        newFuncType.setNull(false);
        return newFuncType;
      }
    };

    return type.visit(visitor);
  }
}