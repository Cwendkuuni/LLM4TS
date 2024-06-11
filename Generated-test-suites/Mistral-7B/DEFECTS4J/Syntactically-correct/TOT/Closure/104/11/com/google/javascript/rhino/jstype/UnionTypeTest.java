package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.StaticScope;

import java.util.Set;

public class UnionTypeTest {

  private static JSTypeRegistry registry;
  static {
    registry = new JSTypeRegistry();
  }

  @org.junit.BeforeClass
  public static void setup() {
    registry.setErrorReporter(new ErrorReporter() {});
  }

  @org.junit.Test
  public void testEmptyUnion() {
    UnionType emptyUnion = new UnionType(registry, ImmutableSet.of());
    assertEquals(ImmutableSet.of(), emptyUnion.getAlternates());
    assertTrue(emptyUnion.matchesNumberContext());
    assertTrue(emptyUnion.matchesStringContext());
    assertTrue(emptyUnion.matchesObjectContext());
  }

  @org.junit.Test
  public void testSingleAlternateUnion() {
    UnionType singleAlternateUnion = new UnionType(
        registry, ImmutableSet.of(IntType.getInstance(registry)));
    ImmutableSet<JSType> alternates = singleAlternateUnion.getAlternates();
    assertEquals(1, alternates.size());
    assertTrue(singleAlternateUnion.matchesNumberContext());
    assertTrue(singleAlternateUnion.matchesStringContext());
    assertTrue(singleAlternateUnion.matchesObjectContext());
    assertEquals(IntType.getInstance(registry), alternates.iterator().next());
  }

  @org.junit.Test
  public void testTwoAlternatesUnion() {
    UnionType twoAlternatesUnion = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    ImmutableSet<JSType> alternates = twoAlternatesUnion.getAlternates();
    assertEquals(2, alternates.size());
    assertTrue(twoAlternatesUnion.matchesNumberContext());
    assertTrue(twoAlternatesUnion.matchesStringContext());
    assertTrue(twoAlternatesUnion.matchesObjectContext());
    assertTrue(alternates.contains(IntType.getInstance(registry)));
    assertTrue(alternates.contains(StringType.getInstance(registry)));
  }

  @org.junit.Test
  public void testGetLeastSupertype() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    JSType leastSupertype = union1.getLeastSupertype(union2);
    assertTrue(leastSupertype instanceof UnionType);
    UnionType leastSupertypeUnion = (UnionType) leastSupertype;
    assertEquals(3, leastSupertypeUnion.getAlternates().size());
    assertTrue(leastSupertypeUnion.matchesNumberContext());
    assertTrue(leastSupertypeUnion.matchesStringContext());
    assertTrue(leastSupertypeUnion.matchesObjectContext());
  }

  @org.junit.Test
  public void testCanAssignTo() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(StringType.getInstance(registry)));
    assertTrue(union1.canAssignTo(union2));

    UnionType voidType = new VoidType(registry);
    assertFalse(union1.canAssignTo(voidType));

    UnionType nullType = new NullType(registry);
    assertFalse(union1.canAssignTo(nullType));
  }

  @org.junit.Test
  public void testFindPropertyType() {
    UnionType union = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            NumberType.getInstance(registry)
        ));
    assertEquals(ObjectType.getInstance(registry), union.findPropertyType("toString"));
  }

  @org.junit.Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType union = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            NullType.getInstance(registry)
        ));
    UnionType restrictedUnion = union.restrictByNotNullOrUndefined();
    assertTrue(restrictedUnion.isSubtype(NullType.getInstance(registry)));
    assertFalse(restrictedUnion.isSubtype(ObjectType.getInstance(registry)));
  }

  @org.junit.Test
  public void testTestForEquality() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    TernaryValue result = union1.testForEquality(union2);
    assertEquals(TernaryValue.MAYBE, result);

    UnionType union3 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    result = union1.testForEquality(union3);
    assertEquals(TernaryValue.MAYBE, result);

    UnionType union4 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    result = union1.testForEquality(union4);
    assertEquals(TernaryValue.NO, result);
  }

  @org.junit.Test
  public void testIsNullable() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            NullType.getInstance(registry)
        ));
    assertTrue(union1.isNullable());
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            BooleanType.getInstance(registry)
        ));
    assertFalse(union2.isNullable());
  }

  @org.junit.Test
  public void testIsUnknownType() {
    UnionType union = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    assertFalse(union.isUnknownType());

    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            new JSType() {
              @Override
              public String toString() {
                return "MysteryType";
              }

              @Override
              public String getName() {
                return "MysteryType";
              }

              @Override
              public boolean isUnknownType() {
                return true;
              }

              @Override
              public boolean equals(Object object) {
                if (object == this) {
                  return true;
                }
                return false;
              }

              @Override
              public int hashCode() {
                return super.hashCode();
              }

              @Override
              public boolean isObject() {
                return true;
              }
            }
        ));
    assertTrue(union2.isUnknownType());
  }

  @org.junit.Test
  public void testMeet() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            NumberType.getInstance(registry)
        ));
    UnionType result = union1.meet(union2);

    assertTrue(result.isSubtype(NumberType.getInstance(registry)));
    assertTrue(result.isSubtype(IntType.getInstance(registry)));
    assertTrue(result.isSubtype(StringType.getInstance(registry)));
  }

  @org.junit.Test
  public void testEquals() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    assertFalse(union1.equals(union2));
  }

  @org.junit.Test
  public void testHashCode() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    assertNotEquals(union1.hashCode(), union2.hashCode());
  }

  @org.junit.Test
  public void testIsUnionType() {
    UnionType union = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    assertTrue(union.isUnionType());
  }

  @org.junit.Test
  public void testIsObject() {
    UnionType union = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    assertTrue(union.isObject());
  }

  @org.junit.Test
  public void testToString() {
    UnionType union = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    assertEquals("(Object|Int)", union.toString());
  }

  @org.junit.Test
  public void testGetTypesUnderEquality() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    TypePair result = union1.getTypesUnderEquality(union2);
    assertTrue(result.typeA == null || result.typeB == null);

    UnionType union3 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    result = union1.getTypesUnderEquality(union3);
    assertEquals(IntType.getInstance(registry), result.typeA);
    assertEquals(IntType.getInstance(registry), result.typeB);

    UnionType union4 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    result = union1.getTypesUnderEquality(union4);
    assertEquals(null, result.typeA);
    assertEquals(null, result.typeB);
  }

  @org.junit.Test
  public void testGetTypesUnderInequality() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    TypePair result = union1.getTypesUnderInequality(union2);
    assertEquals(null, result.typeA);
    assertEquals(null, result.typeB);

    UnionType union3 = new UnionType(
        registry, ImmutableSet.of(
            IntType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    result = union1.getTypesUnderInequality(union3);
    assertEquals(IntType.getInstance(registry), result.typeA);
    assertEquals(null, result.typeB);

    UnionType union4 = new UnionType(
        registry, ImmutableSet.of(
            StringType.getInstance(registry),
            StringType.getInstance(registry)
        ));
    result = union1.getTypesUnderInequality(union4);
    assertEquals(null, result.typeA);
    assertEquals(null, result.typeB);
  }

  @org.junit.Test
  public void testGetTypesUnderShallowInequality() {
    UnionType union1 = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    UnionType union2 = new UnionType(
        registry, ImmutableSet.of(
            ObjectType.getInstance(registry),
            IntType.getInstance(registry)
        ));
    TypePair result = union1.getTypesUnderShallowInequality(union2);
    assertEquals(null, result.typeA);
    assertEquals(null, result.typeB);
  }
}