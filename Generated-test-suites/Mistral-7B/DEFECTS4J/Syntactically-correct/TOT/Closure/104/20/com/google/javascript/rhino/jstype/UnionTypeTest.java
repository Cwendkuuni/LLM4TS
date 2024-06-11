package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {
  private static final JSTypeRegistry registry = new JSTypeRegistry();
  private JSTypeRegistry previousRegistry;
  private ErrorReporter errorReporter;
  private JSTypeFactory factory;

  @Before
  public void setUp() {
    previousRegistry = JSTypeRegistry.getInstance();
    errorReporter = new ErrorReporter() {
      @Override
      public void error(String message) {
        throw new RuntimeException(message);
      }
    };
    factory = new JSTypeFactory(registry);
    registry.setErrorReporter(errorReporter);
  }

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> stringAndBoolean = ImmutableSet.of(
        factory.createStringType(),
        factory.createBooleanType()
    );
    JSType unionType = new UnionType(registry, stringAndBoolean);

    assertEquals("(String, Boolean)", unionType.toString());
    assertEquals(2, unionType.getAlternates().size());
    assertFalse(unionType.isNullType());
    assertFalse(unionType.isVoidType());
  }

  @Test
  public void testUnionTypeMatchesContext() {
    Set<JSType> nullAndString = ImmutableSet.of(
        factory.createNullType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, nullAndString);

    // null type can appear in a numeric context
    assertFalse(unionType.matchesNumberContext());

    // string type can appear in a numeric context, but only with type coercion
    assertTrue(unionType.matchesStringContext());

    // string type can appear in an object context
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    Set<JSType> errorAndNumber = ImmutableSet.of(
        factory.createErrorType(),
        factory.createNumberType()
    );
    JSType unionType = new UnionType(registry, errorAndNumber);

    JSType result = unionType.findPropertyType("toString");
    assertNotNull(result);
    assertTrue(result.isStringType());

    result = unionType.findPropertyType("toNumber");
    assertNull(result);
  }

  @Test
  public void testUnionTypeCanAssign() {
    Set<JSType> nullAndString = ImmutableSet.of(
        factory.createNullType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, nullAndString);

    JSType stringType = factory.createStringType();
    assertTrue(unionType.canAssignTo(stringType));

    JSType numberType = factory.createNumberType();
    assertFalse(unionType.canAssignTo(numberType));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> nullTypeAndEvalError = ImmutableSet.of(
        factory.createNullType(),
        factory.createEvalErrorType()
    );
    JSType unionType = new UnionType(registry, nullTypeAndEvalError);

    assertFalse(unionType.canBeCalled());
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    Set<JSType> nullAndString = ImmutableSet.of(
        factory.createNullType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, nullAndString);

    JSType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertEquals("String", restrictedType.toString());
  }

  @Test
  public void testUnionTypeTestForEquality() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, numberAndString);

    JSType numberType = factory.createNumberType();
    JSType stringType = factory.createStringType();

    TernaryValue result = unionType.testForEquality(numberType);
    assertEquals(TernaryValue.FALSE, result);

    result = unionType.testForEquality(stringType);
    assertEquals(TernaryValue.UNKNOWN, result);

    result = numberType.testForEquality(stringType);
    assertEquals(TernaryValue.FALSE, result);

    result = stringType.testForEquality(numberType);
    assertEquals(TernaryValue.FALSE, result);

    result = unionType.testForEquality(unionType);
    assertEquals(TernaryValue.TRUE, result);
  }

  @Test
  public void testUnionTypeIsNullable() {
    Set<JSType> numberAndNull = ImmutableSet.of(
        factory.createNumberType(),
        factory.createNullType()
    );
    JSType unionType = new UnionType(registry, numberAndNull);

    assertFalse(unionType.isNullable());

    Set<JSType> nullAndBoolean = ImmutableSet.of(
        factory.createNullType(),
        factory.createBooleanType()
    );
    JSType anotherUnionType = new UnionType(registry, nullAndBoolean);

    assertTrue(anotherUnionType.isNullable());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> numberAndObject = ImmutableSet.of(
        factory.createNumberType(),
        getNativeType(JSTypeNative.OBJECT_TYPE)
    );
    JSType unionType = new UnionType(registry, numberAndObject);

    JSType numberType = factory.createNumberType();
    JSType objectType = getNativeType(JSTypeNative.OBJECT_TYPE);

    JSType result = unionType.getLeastSupertype(numberType);
    assertEquals(numberType, result);

    result = unionType.getLeastSupertype(objectType);
    assertEquals(objectType, result);

    result = unionType.getLeastSupertype(unionType);
    assertEquals(unionType, result);
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionTypeA = new UnionType(registry, numberAndString);

    Set<JSType> stringAndBoolean = ImmutableSet.of(
        factory.createStringType(),
        factory.createBooleanType()
    );
    JSType unionTypeB = new UnionType(registry, stringAndBoolean);

    JSType result = unionTypeA.meet(unionTypeB);
    assertTrue(result.isObject());
  }

  @Test
  public void testUnionTypeIsSubtype() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, numberAndString);

    JSType numberType = factory.createNumberType();
    assertTrue(unionType.isSubtype(numberType));

    JSType stringType = factory.createStringType();
    assertTrue(stringType.isSubtype(unionType));

    JSType booleanType = factory.createBooleanType();
    assertFalse(unionType.isSubtype(booleanType));
  }

  @Test
  public void testUnionTypeRestrictedUnion() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, numberAndString);

    JSType restrictedType = unionType.getRestrictedUnion(factory.createStringType());
    assertEquals("Number", restrictedType.toString());
  }

  @Test
  public void testUnionTypeToString() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, numberAndString);

    assertEquals("(Number, String)", unionType.toString());
  }

  @Test
  public void testUnionTypeIsUnionType() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, numberAndString);

    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, numberAndString);

    assertTrue(unionType.isObject());
  }

  @Test
  public void testUnionTypeContains() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionType = new UnionType(registry, numberAndString);

    JSType numberType = factory.createNumberType();
    assertTrue(unionType.contains(numberType));

    JSType stringType = factory.createStringType();
    assertTrue(unionType.contains(stringType));
  }

  @Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    Set<JSType> numberAndBoolean = ImmutableSet.of(
        factory.createNumberType(),
        factory.createBooleanType()
    );
    JSType unionType = new UnionType(registry, numberAndBoolean);

    BooleanLiteralSet possibleOutcomes = unionType.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.BOTH, possibleOutcomes);

    JSType restrictedType = unionType.getRestrictedTypeGivenToBooleanOutcome(true);
    assertEquals("Number", restrictedType.toString());

    restrictedType = unionType.getRestrictedTypeGivenToBooleanOutcome(false);
    assertEquals("Boolean", restrictedType.toString());
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionTypeA = new UnionType(registry, numberAndString);

    Set<JSType> stringAndBoolean = ImmutableSet.of(
        factory.createStringType(),
        factory.createBooleanType()
    );
    JSType unionTypeB = new UnionType(registry, stringAndBoolean);

    TypePair result = unionTypeA.getTypesUnderEquality(unionTypeB);
    assertEquals(TypePair.NONE, result);

    result = unionTypeB.getTypesUnderEquality(unionTypeA);
    assertEquals(TypePair.NONE, result);

    result = unionTypeA.getTypesUnderEquality(unionTypeA);
    assertEquals(TypePair.IDENTITY, result);
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    Set<JSType> numberAndString = ImmutableSet.of(
        factory.createNumberType(),
        factory.createStringType()
    );
    JSType unionTypeA = new UnionType(registry, numberAndString);

    Set<JSType> stringAndBoolean = ImmutableSet.of(
        factory.createStringType(),
        factory.createBooleanType()
    );
    JSType unionTypeB = new UnionType(registry, stringAndBoolean);

    TypePair result = unionTypeA.getTypesUnderInequality(unionTypeB);
    assertEquals(TypePair.NONE, result);

    result = unionTypeB.getTypesUnderInequality(unionTypeA);
    assertEquals(TypePair.NONE, result);

    result = unionTypeA.getTypesUnderInequality(unionTypeA);
    assertEquals(TypePair.DIFFERENT, result);
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    Set<JSType> numberAndObject = ImmutableSet.of(
        factory.createNumberType(),
        getNativeType(JSTypeNative.OBJECT_TYPE)
    );
    JSType unionTypeA = new UnionType(registry, numberAndObject);

    Set<JSType> objectAndNumber = ImmutableSet.of(
        getNativeType(JSTypeNative.OBJECT_TYPE),
        factory.createNumberType()
    );
    JSType unionTypeB = new UnionType(registry, objectAndNumber);

    TypePair result = unionTypeA.getTypesUnderShallowInequality(unionTypeB);
    assertEquals(TypePair.NONE, result);

    result = unionTypeB.getTypesUnderShallowInequality(unionTypeA);
    assertEquals(TypePair.NONE, result);

    result = unionTypeA.getTypesUnderShallowInequality(unionTypeA);
    assertEquals(TypePair.DIFFERENT, result);
  }

  private JSType getNativeType(int typeCode) {
    JSTypeNative nativeType = JSTypeNative.values()[typeCode];
    return registry.getNativeType(nativeType);
  }

  @After
  public void tearDown() {
    JSTypeRegistry.setInstance(previousRegistry);
  }
}