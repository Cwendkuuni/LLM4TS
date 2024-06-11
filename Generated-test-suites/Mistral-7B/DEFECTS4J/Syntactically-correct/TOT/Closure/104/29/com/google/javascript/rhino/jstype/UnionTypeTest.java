import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class UnionTypeTest {
  private JSTypeRegistry registry;
  private UnionType nullType;
  private UnionType stringType;
  private UnionType numberType;
  private UnionType booleanType;
  private UnionType errorType;
  private UnionType objectType;
  private UnionType nullableNumberType;
  private UnionType nullableBooleanType;
  private UnionType nullableErrorType;
  private UnionType nullableObjectType;

  @org.junit.Before
  public void setUp() {
    registry = new JSTypeRegistry();
    nullType = (UnionType) registry.getTypeRegistry().getNativeType(JSTypeNative.NULL_TYPE);
    stringType = (UnionType) registry.getTypeRegistry().getNativeType(JSTypeNative.STRING_TYPE);
    numberType = (UnionType) registry.getTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE);
    booleanType = (UnionType) registry.getTypeRegistry().getNativeType(JSTypeNative.BOOLEAN_TYPE);
    errorType = (UnionType) registry.getTypeRegistry().getNativeType(JSTypeNative.ERROR_TYPE);
    objectType = (UnionType) registry.getTypeRegistry().getNativeType(JSTypeNative.OBJECT_TYPE);

    nullableNumberType = new UnionType(registry, Arrays.asList(numberType, nullType));
    nullableBooleanType = new UnionType(registry, Arrays.asList(booleanType, nullType));
    nullableErrorType = new UnionType(registry, Arrays.asList(errorType, nullType));
    nullableObjectType = new UnionType(registry, Arrays.asList(objectType, nullType));
  }

  @org.junit.Test
  public void testUnionType_getAlternates_returnsCorrectAlternates() {
    assertEquals(Arrays.asList(stringType, numberType), stringType.getAlternates());
    assertEquals(Arrays.asList(numberType), numberType.getAlternates());
  }

  @org.junit.Test
  public void testUnionType_canAssignTo_assignsToCorrectAlternates() {
    assertTrue(numberType.canAssignTo(numberType));
    assertTrue(stringType.canAssignTo(stringType));
    assertFalse(numberType.canAssignTo(stringType));
    assertFalse(stringType.canAssignTo(numberType));
  }

  @org.junit.Test
  public void testUnionType_getLeastSupertype_findsLeastCommonSupertype() {
    JSType leastCommonSupertype = numberType.getLeastSupertype(stringType);
    assertTrue(leastCommonSupertype instanceof ObjectType);
  }

  @org.junit.Test
  public void testUnionType_restrictByNotNullOrUndefined_removesNullType() {
    assertEquals(numberType, nullableNumberType.restrictByNotNullOrUndefined());
    assertEquals(booleanType, nullableBooleanType.restrictByNotNullOrUndefined());
    assertEquals(errorType, nullableErrorType.restrictByNotNullOrUndefined());
    assertEquals(objectType, nullableObjectType.restrictByNotNullOrUndefined());
  }

  @org.junit.Test
  public void testUnionType_testForEquality_returnsCorrectEqualityValues() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    UnionType numberString = new UnionType(registry, Arrays.asList(numberType, stringType));

    assertEquals(TernaryValue.TRUE, stringType.testForEquality(numberType));
    assertEquals(TernaryValue.TRUE, numberType.testForEquality(stringType));
    assertEquals(TernaryValue.TRUE, stringNumber.testForEquality(numberString));
    assertEquals(TernaryValue.TRUE, numberString.testForEquality(stringNumber));

    assertEquals(TernaryValue.FALSE, stringType.testForEquality(stringType));
    assertEquals(TernaryValue.FALSE, numberType.testForEquality(numberType));
  }

  @org.junit.Test
  public void testUnionType_isNullable_returnsCorrectNullableValue() {
    assertFalse(numberType.isNullable());
    assertFalse(stringType.isNullable());
    assertTrue(nullableNumberType.isNullable());
    assertTrue(nullableBooleanType.isNullable());
    assertTrue(nullableErrorType.isNullable());
    assertTrue(nullableObjectType.isNullable());
  }

  @org.junit.Test
  public void testUnionType_equals_returnsCorrectEqualsValue() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    UnionType numberString = new UnionType(registry, Arrays.asList(numberType, stringType));

    assertFalse(stringType.equals(numberType));
    assertFalse(stringNumber.equals(numberString));
    assertTrue(stringType.equals(stringType));
    assertTrue(numberType.equals(numberType));
    assertTrue(stringNumber.equals(stringNumber));
    assertTrue(numberString.equals(numberString));
  }

  @org.junit.Test
  public void testUnionType_meet_returnsCorrectMeetValue() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    UnionType numberString = new UnionType(registry, Arrays.asList(numberType, stringType));

    JSType result = stringNumber.meet(numberString);
    assertTrue(result instanceof ObjectType);
  }

  @org.junit.Test
  public void testUnionType_toString_returnsCorrectStringRepresentation() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    assertEquals("(String|Number)", stringNumber.toString());
  }

  @org.junit.Test
  public void testUnionType_isSubtype_returnsCorrectSubtypeValue() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    UnionType numberString = new UnionType(registry, Arrays.asList(numberType, stringType));

    assertTrue(numberType.isSubtype(numberType));
    assertTrue(numberType.isSubtype(numberString));
    assertTrue(numberString.isSubtype(numberType));
    assertTrue(numberString.isSubtype(stringNumber));
    assertTrue(stringType.isSubtype(stringType));
    assertTrue(stringType.isSubtype(stringNumber));
    assertTrue(stringNumber.isSubtype(stringType));
    assertTrue(stringNumber.isSubtype(numberType));

    assertFalse(numberType.isSubtype(stringType));
    assertFalse(numberType.isSubtype(booleanType));
    assertFalse(numberType.isSubtype(errorType));
    assertFalse(numberType.isSubtype(objectType));

    assertFalse(stringType.isSubtype(numberType));
    assertFalse(stringType.isSubtype(booleanType));
    assertFalse(stringType.isSubtype(errorType));
    assertFalse(stringType.isSubtype(objectType));

    assertFalse(booleanType.isSubtype(numberType));
    assertFalse(booleanType.isSubtype(stringType));
    assertFalse(booleanType.isSubtype(numberString));
    assertFalse(booleanType.isSubtype(stringNumber));

    assertFalse(errorType.isSubtype(numberType));
    assertFalse(errorType.isSubtype(stringType));
    assertFalse(errorType.isSubtype(numberString));
    assertFalse(errorType.isSubtype(stringNumber));

    assertFalse(objectType.isSubtype(numberType));
    assertFalse(objectType.isSubtype(stringType));
    assertFalse(objectType.isSubtype(numberString));
    assertFalse(objectType.isSubtype(stringNumber));
  }

  @org.junit.Test
  public void testUnionType_getRestrictedUnion_returnsCorrectRestrictedUnion() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    UnionType numberString = new UnionType(registry, Arrays.asList(numberType, stringType));
    UnionType stringNumberWithoutNumber = new UnionType(registry, Arrays.asList(stringType));

    assertEquals(stringNumberWithoutNumber, stringNumber.getRestrictedUnion(numberType));
    assertEquals(numberString, numberString.getRestrictedUnion(stringType));
  }

  @org.junit.Test
  public void testUnionType_getPossibleToBooleanOutcomes_returnsCorrectBooleanOutcomes() {
    UnionType booleanNumber = new UnionType(registry, Arrays.asList(booleanType, numberType));
    BooleanLiteralSet booleanOutcomes = booleanNumber.getPossibleToBooleanOutcomes();

    assertEquals(EnumSet.of(BooleanLiteral.TRUE, BooleanLiteral.FALSE), booleanOutcomes);
  }

  @org.junit.Test
  public void testUnionType_getTypesUnderEquality_returnsCorrectTypesUnderEquality() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    UnionType numberString = new UnionType(registry, Arrays.asList(numberType, stringType));

    TypePair stringNumberEqualsNumberString = stringNumber.getTypesUnderEquality(numberString);
    TypePair numberStringEqualsStringNumber = numberString.getTypesUnderEquality(stringNumber);

    assertNotNull(stringNumberEqualsNumberString.typeA);
    assertNotNull(stringNumberEqualsNumberString.typeB);
    assertNotNull(numberStringEqualsStringNumber.typeA);
    assertNotNull(numberStringEqualsStringNumber.typeB);

    assertEquals(numberType, stringNumberEqualsNumberString.typeA);
    assertEquals(stringType, stringNumberEqualsNumberString.typeB);
    assertEquals(stringType, numberStringEqualsStringNumber.typeA);
    assertEquals(numberType, numberStringEqualsStringNumber.typeB);
  }

  @org.junit.Test
  public void testUnionType_getTypesUnderInequality_returnsCorrectTypesUnderInequality() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    UnionType numberString = new UnionType(registry, Arrays.asList(numberType, stringType));

    TypePair stringNumberNotEqualsNumberString = stringNumber.getTypesUnderInequality(numberString);
    TypePair numberStringNotEqualsStringNumber = numberString.getTypesUnderInequality(stringNumber);

    assertNotNull(stringNumberNotEqualsNumberString.typeA);
    assertNotNull(stringNumberNotEqualsNumberString.typeB);
    assertNotNull(numberStringNotEqualsStringNumber.typeA);
    assertNotNull(numberStringNotEqualsStringNumber.typeB);

    assertEquals(nullType, stringNumberNotEqualsNumberString.typeA);
    assertEquals(nullType, stringNumberNotEqualsNumberString.typeB);
    assertEquals(nullType, numberStringNotEqualsStringNumber.typeA);
    assertEquals(nullType, numberStringNotEqualsStringNumber.typeB);
  }

  @org.junit.Test
  public void testUnionType_getTypesUnderShallowInequality_returnsCorrectTypesUnderShallowInequality() {
    UnionType stringNumber = new UnionType(registry, Arrays.asList(stringType, numberType));
    UnionType numberString = new UnionType(registry, Arrays.asList(numberType, stringType));

    TypePair stringNumberNotEqualsNumberStringShallow =
        stringNumber.getTypesUnderShallowInequality(numberString);
    TypePair numberStringNotEqualsStringNumberShallow =
        numberString.getTypesUnderShallowInequality(stringNumber);

    assertNotNull(stringNumberNotEqualsNumberStringShallow.typeA);
    assertNotNull(stringNumberNotEqualsNumberStringShallow.typeB);
    assertNotNull(numberStringNotEqualsStringNumberShallow.typeA);
    assertNotNull(numberStringNotEqualsStringNumberShallow.typeB);

    assertEquals(numberType, stringNumberNotEqualsNumberStringShallow.typeA);
    assertEquals(stringType, stringNumberNotEqualsNumberStringShallow.typeB);
    assertEquals(stringType, numberStringNotEqualsStringNumberShallow.typeA);
    assertEquals(numberType, numberStringNotEqualsStringNumberShallow.typeB);
  }
}