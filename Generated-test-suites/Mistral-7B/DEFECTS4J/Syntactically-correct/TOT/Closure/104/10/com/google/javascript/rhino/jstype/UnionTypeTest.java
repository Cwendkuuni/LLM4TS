package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.TestTypes;
import com.google.javascript.rhino.jstype.UnionType;

public class UnionTypeTest {

  private static JSTypeRegistry registry = new JSTypeRegistry();
  static {
    registry.register(JSTypeNative.VOID_TYPE);
    registry.register(JSTypeNative.NULL_TYPE);
    registry.register(TestTypes.NumberType);
    registry.register(TestTypes.BooleanType);
    registry.register(TestTypes.StringType);
  }

  @Test
  public void testUnionType() {
    Set<JSType> alternates = ImmutableSet.of(TestTypes.NumberType, TestTypes.BooleanType);
    UnionType unionType = new UnionType(registry, alternates);

    // Test alternate types
    assertEquals(TestTypes.NumberType, unionType.getAlternates().iterator().next());
    assertEquals(TestTypes.BooleanType, unionType.getAlternates().iterator().next());
    assertFalse(unionType.getAlternates().contains(TestTypes.StringType));

    // Test for numeric context:
    assertTrue(unionType.matchesNumberContext());

    // Test for string context:
    assertTrue(unionType.matchesStringContext());

    // Test for object context:
    assertTrue(unionType.matchesObjectContext());

    // Test property type
    String propertyName = "foo";
    JSType propertyType = unionType.findPropertyType(propertyName);
    assertNotNull(propertyType);
    assertEquals(TestTypes.NumberType, propertyType);

    // Test equality
    UnionType equalUnionType = new UnionType(registry, alternates);
    assertEquals(unionType, equalUnionType);

    // Test inequality
    UnionType differentUnionType = new UnionType(registry, ImmutableSet.of(TestTypes.BooleanType, TestTypes.NumberType));
    assertFalse(unionType.equals(differentUnionType));

    // Test subclass relationship
    assertTrue(TestTypes.NumberType.isSubtype(unionType));
    assertTrue(TestTypes.BooleanType.isSubtype(unionType));
    assertFalse(unionType.isSubtype(TestTypes.StringType));

    // Test restrictByNotNullOrUndefined
    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertEquals(TestTypes.BooleanType, restrictedUnionType.getAlternates().iterator().next());
    assertEquals(TestTypes.NumberType, restrictedUnionType.getAlternates().iterator().next());
    assertFalse(TestTypes.NullType.isAssignableFrom(restrictedUnionType));

    // Test canAssignTo
    UnionType assignableUnionType = new UnionType(registry, ImmutableSet.of(TestTypes.NumberType, TestTypes.BooleanType));
    assertTrue(assignableUnionType.canAssignTo(TestTypes.NumberType.getInstance(42)));
    assertTrue(assignableUnionType.canAssignTo(TestTypes.BooleanType.getInstance(true)));

    // Test canBeCalled
    assertTrue(assignableUnionType.canBeCalled());

    // Test meets
    UnionType meetsUnionType = new UnionType(registry, ImmutableSet.of(TestTypes.BooleanType, TestTypes.NumberType));
    UnionType result = assignableUnionType.meet(meetsUnionType);
    assertNotNull(result);
    assertTrue(result.isInstanceOf(UnionType.class));
    assertEquals(ImmutableSet.of(TestTypes.BooleanType, TestTypes.NumberType), result.getAlternates());

    // Test isNullable
    assertFalse(unionType.isNullable());

    // Test isUnknownType
    assertFalse(unionType.isUnknownType());

    // Test getLeastSupertype
    UnionType leastSupertype = new UnionType(registry, ImmutableSet.of(TestTypes.NumberType, TestTypes.BooleanType));
    UnionType superType = unionType.getLeastSupertype(leastSupertype);
    assertEquals(unionType, superType);

    // Test testForEquality
    UnionType otherUnionType = new UnionType(registry, ImmutableSet.of(TestTypes.BooleanType));
    TernaryValue ternaryValue = unionType.testForEquality(otherUnionType);
    assertEquals(TernaryValue.UNKNOWN, ternaryValue);

    // Test equals
    assertTrue(unionType.equals(unionType));

    // Test hashcode
    UnionType unionType1 = new UnionType(registry, alternates);
    UnionType unionType2 = new UnionType(registry, alternates);
    assertEquals(unionType1.hashCode(), unionType2.hashCode());

    // Test isUnionType
    assertTrue(unionType.isUnionType());

    // Test isObject
    assertTrue(unionType.isObject());

    // Test contains
    assertTrue(unionType.contains(TestTypes.NumberType));
    assertFalse(unionType.contains(TestTypes.StringType));

    // Test getRestrictedUnion
    UnionType restrictedUnion = unionType.getRestrictedUnion(TestTypes.NumberType);
    assertEquals(TestTypes.BooleanType, restrictedUnion.getAlternates().iterator().next());

    // Test toString
    assertEquals("(number, boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeWithNull() {
    Set<JSType> alternates = ImmutableSet.of(TestTypes.NullType, TestTypes.NumberType);
    UnionType unionType = new UnionType(registry, alternates);

    // Test matchesObjectContext
    assertTrue(unionType.matchesObjectContext());

    // Test getRestrictedTypeGivenToBooleanOutcomes
    BooleanLiteralSet literals = unionType.getPossibleToBooleanOutcomes();
    assertTrue(literals.contains(BooleanLiteral.FALSE));
    assertTrue(literals.contains(BooleanLiteral.TRUE));
    assertEquals(BooleanLiteralSet.BOTH, literals);

    // Test getTypesUnderEquality
    UnionType otherUnionType = new UnionType(registry, ImmutableSet.of(TestTypes.NumberType, TestTypes.NullType));
    TypePair typePair = unionType.getTypesUnderEquality(otherUnionType);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);

    // Test getTypesUnderInequality
    UnionType inequalityUnionType = new UnionType(registry, ImmutableSet.of(TestTypes.NumberType, TestTypes.NullType));
    typePair = unionType.getTypesUnderInequality(inequalityUnionType);
    assertEquals(TestTypes.NumberType, typePair.typeA);
    assertNull(typePair.typeB);

    // Test getTypesUnderShallowInequality
    UnionType shallowInequalityUnionType = new UnionType(registry, ImmutableSet.of(TestTypes.NumberType, TestTypes.NullType));
    typePair = unionType.getTypesUnderShallowInequality(shallowInequalityUnionType);
    assertNull(typePair.typeA);
    assertNull(typePair.typeB);
  }

  @Test
  public void testUnionTypeWithVoid() {
    Set<JSType> alternates = ImmutableSet.of(TestTypes.VoidType, TestTypes.NumberType);
    UnionType unionType = new UnionType(registry, alternates);

    // Test canBeCalled
    assertFalse(unionType.canBeCalled());

    // Test getLeastSupertype
    UnionType leastSupertype = new UnionType(registry, ImmutableSet.of(TestTypes.NumberType));
    UnionType superType = unionType.getLeastSupertype(leastSupertype);
    assertNull(superType);
  }
}