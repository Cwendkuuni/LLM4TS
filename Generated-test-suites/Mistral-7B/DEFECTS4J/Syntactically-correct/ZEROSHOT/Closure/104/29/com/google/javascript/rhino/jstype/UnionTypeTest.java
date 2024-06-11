import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeBasics() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringAlternates = Collections.singleton(StringType.instance(registry));
    Set<JSType> booleanAlternates = Collections.singleton(BooleanType.instance(registry));

    UnionType unionTypeStringBoolean = new UnionType(registry, stringAlternates);
    UnionType unionTypeBooleanString = new UnionType(registry, booleanAlternates);

    assertEquals("(String, boolean)", unionTypeStringBoolean.toString());
    assertEquals("(boolean, String)", unionTypeBooleanString.toString());

    assertTrue(unionTypeStringBoolean.equals(unionTypeBooleanString));

    // test getAlternates
    assertEquals(stringAlternates, unionTypeStringBoolean.getAlternates());
    assertEquals(booleanAlternates, unionTypeBooleanString.getAlternates());

    // test matchesNumberContext
    assertFalse(unionTypeStringBoolean.matchesNumberContext());
    assertFalse(unionTypeBooleanString.matchesNumberContext());

    // test matchesStringContext
    assertTrue(unionTypeStringBoolean.matchesStringContext());
    assertTrue(unionTypeBooleanString.matchesStringContext());

    // test matchesObjectContext
    assertTrue(unionTypeStringBoolean.matchesObjectContext());
    assertTrue(unionTypeBooleanString.matchesObjectContext());

    // test findPropertyType
    JSType propertyTypeString = StringType.instance(registry);
    JSType propertyTypeBoolean = BooleanType.instance(registry);

    JSType propertyTypeStringBoolean = unionTypeStringBoolean.findPropertyType("toString");
    assertEquals(propertyTypeString, propertyTypeStringBoolean);

    JSType propertyTypeBooleanString = unionTypeBooleanString.findPropertyType("toString");
    assertEquals(propertyTypeString, propertyTypeBooleanString);

    // test canAssignTo
    JSType typeNumber = NumberType.instance(registry);
    JSType typeString = StringType.instance(registry);

    assertTrue(unionTypeStringBoolean.canAssignTo(typeString));
    assertTrue(unionTypeStringBoolean.canAssignTo(typeNumber));
    assertTrue(unionTypeBooleanString.canAssignTo(typeString));
    assertTrue(unionTypeBooleanString.canAssignTo(typeNumber));

    // test canBeCalled
    FunctionType functionTypeWithStringArg = new FunctionType(
        new ParameterType[]{StringType.instance(registry)},
        VoidType.instance(registry), registry);

    FunctionType functionTypeWithBooleanArg = new FunctionType(
        new ParameterType[]{BooleanType.instance(registry)},
        VoidType.instance(registry), registry);

    assertTrue(unionTypeStringBoolean.canBeCalled());
    assertTrue(unionTypeStringBoolean.canBeCalled(functionTypeWithStringArg));
    assertTrue(unionTypeBooleanString.canBeCalled());
    assertTrue(unionTypeBooleanString.canBeCalled(functionTypeWithBooleanArg));

    // test restrictByNotNullOrUndefined
    UnionType unionTypeStringBooleanNotNull = unionTypeStringBoolean.restrictByNotNullOrUndefined();
    UnionType unionTypeBooleanStringNotNull = unionTypeBooleanString.restrictByNotNullOrUndefined();

    assertNotNull(unionTypeStringBooleanNotNull);
    assertNotNull(unionTypeBooleanStringNotNull);

    assertTrue(unionTypeStringBooleanNotNull.isSubtype(unionTypeStringBoolean));
    assertTrue(unionTypeBooleanStringNotNull.isSubtype(unionTypeBooleanString));

    assertFalse(unionTypeStringBooleanNotNull.isSubtype(StringType.instance(registry)));
    assertFalse(unionTypeBooleanStringNotNull.isSubtype(BooleanType.instance(registry)));

    // test testForEquality
    UnionType unionTypeStringBooleanEquality = new UnionType(registry, stringAlternates);
    UnionType unionTypeBooleanStringEquality = new UnionType(registry, booleanAlternates);

    assertEquals(UNKNOWN, unionTypeStringBooleanEquality.testForEquality(unionTypeBooleanStringEquality));
    assertEquals(UNKNOWN, unionTypeBooleanStringEquality.testForEquality(unionTypeStringBooleanEquality));

    assertEquals(StringType.instance(registry), unionTypeStringBooleanEquality.testForEquality(StringType.instance(registry)));
    assertEquals(BooleanType.instance(registry), unionTypeBooleanStringEquality.testForEquality(BooleanType.instance(registry)));

    // test isNullable
    assertFalse(unionTypeStringBoolean.isNullable());
    assertFalse(unionTypeBooleanString.isNullable());

    // test isUnknownType
    assertFalse(unionTypeStringBoolean.isUnknownType());
    assertFalse(unionTypeBooleanString.isUnknownType());

    // test getLeastSupertype
    JSType leastSupertypeStringBoolean = unionTypeStringBoolean.getLeastSupertype(unionTypeBooleanString);
    JSType leastSupertypeBooleanString = unionTypeBooleanString.getLeastSupertype(unionTypeStringBoolean);

    assertEquals(unionTypeStringBoolean, leastSupertypeStringBoolean);
    assertEquals(unionTypeBooleanString, leastSupertypeBooleanString);

    // test meet
    UnionType meetUnionTypeStringBoolean = unionTypeStringBoolean.meet(unionTypeBooleanString);
    UnionType meetUnionTypeBooleanString = unionTypeBooleanString.meet(unionTypeStringBoolean);

    assertEquals(unionTypeStringBoolean, meetUnionTypeStringBoolean);
    assertEquals(unionTypeBooleanString, meetUnionTypeBooleanString);

    // test equals
    UnionType equalUnionType1 = new UnionType(registry, stringAlternates);
    UnionType equalUnionType2 = new UnionType(registry, stringAlternates);

    assertTrue(equalUnionType1.equals(equalUnionType2));

    // test hashCode
    UnionType hashCodeUnionType1 = new UnionType(registry, stringAlternates);
    UnionType hashCodeUnionType2 = new UnionType(registry, stringAlternates);

    assertEquals(hashCodeUnionType1.hashCode(), hashCodeUnionType2.hashCode());

    // test isUnionType
    assertTrue(unionTypeStringBoolean.isUnionType());
    assertTrue(unionTypeBooleanString.isUnionType());

    // test isObject
    assertTrue(unionTypeStringBoolean.isObject());
    assertTrue(unionTypeBooleanString.isObject());

    // test contains
    UnionType unionTypeStringBooleanWithString = new UnionType(registry, Arrays.asList(StringType.instance(registry), BooleanType.instance(registry)));

    assertTrue(unionTypeStringBoolean.contains(StringType.instance(registry)));
    assertTrue(unionTypeStringBoolean.contains(BooleanType.instance(registry)));
    assertFalse(unionTypeStringBoolean.contains(NumberType.instance(registry)));
    assertFalse(unionTypeStringBooleanWithString.contains(NumberType.instance(registry)));

    // test getRestrictedUnion
    JSType restrictedUnionTypeStringBooleanNumber = unionTypeStringBoolean.getRestrictedUnion(NumberType.instance(registry));
    JSType restrictedUnionTypeBooleanStringNumber = unionTypeBooleanString.getRestrictedUnion(NumberType.instance(registry));

    assertEquals(StringType.instance(registry), restrictedUnionTypeStringBooleanNumber);
    assertEquals(BooleanType.instance(registry), restrictedUnionTypeBooleanStringNumber);
  }

  @Test
  public void testUnionTypeNested() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringAlternates = Collections.singleton(StringType.instance(registry));
    Set<JSType> booleanAlternates = Collections.singleton(BooleanType.instance(registry));

    UnionType unionTypeStringBoolean = new UnionType(registry, stringAlternates);
    UnionType unionTypeBooleanString = new UnionType(registry, booleanAlternates);

    UnionType unionTypeStringBooleanWithBoolean = new UnionType(registry, Arrays.asList(unionTypeStringBoolean, BooleanType.instance(registry)));
    UnionType unionTypeBooleanStringWithString = new UnionType(registry, Arrays.asList(unionTypeBooleanString, StringType.instance(registry)));

    // test canAssignTo
    JSType typeNumber = NumberType.instance(registry);

    assertTrue(unionTypeStringBooleanWithBoolean.canAssignTo(typeNumber));
    assertFalse(unionTypeBooleanStringWithString.canAssignTo(typeNumber));

    // test canBeCalled
    FunctionType functionTypeWithStringArg = new FunctionType(
        new ParameterType[]{StringType.instance(registry)},
        VoidType.instance(registry), registry);

    FunctionType functionTypeWithBooleanArg = new FunctionType(
        new ParameterType[]{BooleanType.instance(registry)},
        VoidType.instance(registry), registry);

    assertTrue(unionTypeStringBooleanWithBoolean.canBeCalled(functionTypeWithStringArg));
    assertFalse(unionTypeBooleanStringWithString.canBeCalled(functionTypeWithStringArg));
    assertFalse(unionTypeBooleanStringWithString.canBeCalled(functionTypeWithBooleanArg));

    // test getLeastSupertype
    JSType leastSupertypeStringBooleanWithBoolean = unionTypeStringBooleanWithBoolean.getLeastSupertype(unionTypeBooleanStringWithString);

    assertEquals(unionTypeStringBooleanWithBoolean, leastSupertypeStringBooleanWithBoolean);

    // test getTypesUnderEquality
    UnionType unionTypeStringBooleanEquality = new UnionType(registry, stringAlternates);
    UnionType unionTypeBooleanStringEquality = new UnionType(registry, booleanAlternates);

    TypePair typesUnderEqualityStringBooleanString = unionTypeStringBooleanEquality.getTypesUnderEquality(unionTypeBooleanStringEquality);
    TypePair typesUnderEqualityBooleanStringString = unionTypeBooleanStringEquality.getTypesUnderEquality(unionTypeStringBooleanEquality);

    assertNull(typesUnderEqualityStringBooleanString.typeA);
    assertNull(typesUnderEqualityStringBooleanString.typeB);

    assertNull(typesUnderEqualityBooleanStringString.typeA);
    assertNull(typesUnderEqualityBooleanStringString.typeB);

    // test getTypesUnderInequality
    TypePair typesUnderInequalityStringBooleanString = unionTypeStringBooleanEquality.getTypesUnderInequality(unionTypeBooleanStringEquality);
    TypePair typesUnderInequalityBooleanStringString = unionTypeBooleanStringEquality.getTypesUnderInequality(unionTypeStringBooleanEquality);

    assertNull(typesUnderInequalityStringBooleanString.typeA);
    assertNull(typesUnderInequalityStringBooleanString.typeB);

    assertNull(typesUnderInequalityBooleanStringString.typeA);
    assertNull(typesUnderInequalityBooleanStringString.typeB);

    // test getTypesUnderShallowInequality
    TypePair typesUnderShallowInequalityStringBooleanString = unionTypeStringBooleanEquality.getTypesUnderShallowInequality(unionTypeBooleanStringEquality);
    TypePair typesUnderShallowInequalityBooleanStringString = unionTypeBooleanStringEquality.getTypesUnderShallowInequality(unionTypeStringBooleanEquality);

    assertNotNull(typesUnderShallowInequalityStringBooleanString.typeA);
    assertNull(typesUnderShallowInequalityStringBooleanString.typeB);

    assertNull(typesUnderShallowInequalityBooleanStringString.typeA);
    assertNotNull(typesUnderShallowInequalityBooleanStringString.typeB);
  }
}