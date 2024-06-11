import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.TernaryValue.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.TestingUtil;
import com.google.javascript.rhino.jstype.UnionType;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class UnionTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @org.junit.Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING));
    JSType unionType = new UnionType(registry, alternates);
    assertEquals("(number, string)", unionType.toString());
  }

  @org.junit.Test
  public void testGetAlternates() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertEquals(ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)), unionType.getAlternates());
  }

  @org.junit.Test
  public void testForgiveUnknownNames() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    unionType.forgiveUnknownNames();
    // No action required as UnionType does not contain any unknown names
  }

  @org.junit.Test
  public void testMatchesNumberContext() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertTrue(unionType.matchesNumberContext());
  }

  @org.junit.Test
  public void testMatchesStringContext() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertTrue(unionType.matchesStringContext());
  }

  @org.junit.Test
  public void testMatchesObjectContext() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertTrue(unionType.matchesObjectContext());
  }

  @org.junit.Test
  public void testFindPropertyType() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(
        registry.getNativeType(JSTypeNative.NUMBER),
        registry.getNativeType(JSTypeNative.STRING),
        new IntType(registry)
    ));
    assertEquals(IntType.class, unionType.findPropertyType("length"));
  }

  @org.junit.Test
  public void testCanAssignTo() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    JSType that = registry.getNativeType(JSTypeNative.BOOLEAN);
    assertTrue(unionType.canAssignTo(that));
  }

  @org.junit.Test
  public void testCanBeCalled() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertTrue(unionType.canBeCalled());
  }

  @org.junit.Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NULL), registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType restricted = unionType.restrictByNotNullOrUndefined();
    assertEquals("(number, string)", restricted.toString());
  }

  @org.junit.Test
  public void testTestForEquality() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER)));
    TernaryValue result1 = unionType1.testForEquality(unionType1);
    TernaryValue result2 = unionType1.testForEquality(unionType2);
    TernaryValue result3 = unionType2.testForEquality(unionType1);
    assertTrue(result1.equals(EQ));
    assertTrue(result2.equals(UNKNOWN));
    assertTrue(result3.equals(UNKNOWN));
  }

  @org.junit.Test
  public void testIsNullable() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.NULL)));
    assertFalse(unionType.isNullable());
  }

  @org.junit.Test
  public void testIsUnknownType() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.NULL)));
    assertTrue(unionType.isUnknownType());
  }

  @org.junit.Test
  public void testGetLeastSupertype() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.BOOLEAN)));

    UnionType leastSupertype = unionType1.getLeastSupertype(unionType2);
    assertEquals(unionType2, leastSupertype);

    UnionType leastSupertype2 = unionType2.getLeastSupertype(unionType1);
    assertEquals(unionType1, leastSupertype2);
  }

  @org.junit.Test
  public void testMeet() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.BOOLEAN)));

    UnionType metUnionType = unionType1.meet(unionType2);
    assertEquals("(string, number)", metUnionType.toString());
  }

  @org.junit.Test
  public void testEquals() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER)));

    assertFalse(unionType1.equals(null));
    assertFalse(unionType1.equals(""));
    assertFalse(unionType1.equals(registry.getNativeType(JSTypeNative.NUMBER)));

    assertFalse(unionType1.equals(unionType2));

    UnionType unionType3 = new UnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertTrue(unionType1.equals(unionType3));
  }

  @org.junit.Test
  public void testHashCode() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER)));

    int hashCode1 = unionType1.hashCode();
    int hashCode2 = unionType2.hashCode();

    assertNotSame(hashCode1, hashCode2);

    UnionType unionType3 = new UnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertEquals(hashCode1, unionType3.hashCode());
  }

  @org.junit.Test
  public void testIsUnionType() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertTrue(unionType.isUnionType());
  }

  @org.junit.Test
  public void testIsObject() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertTrue(unionType.isObject());
  }

  @org.junit.Test
  public void testContains() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    JSType typeToTest = registry.getNativeType(JSTypeNative.NUMBER);

    assertTrue(unionType.contains(typeToTest));
  }

  @org.junit.Test
  public void testGetRestrictedUnion() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.BOOLEAN)));
    JSType typeToRestrict = registry.getNativeType(JSTypeNative.BOOLEAN);

    UnionType restrictedType = unionType.getRestrictedUnion(typeToRestrict);
    assertEquals("(number, string)", restrictedType.toString());
  }

  @org.junit.Test
  public void testToString() {
    UnionType unionType = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    assertEquals("(number, string)", unionType.toString());
  }

  @org.junit.Test
  public void testIsSubtype() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER)));

    assertTrue(unionType1.isSubtype(unionType2));
    assertFalse(unionType2.isSubtype(unionType1));
  }

  @org.junit.Test
  public void testGetTypesUnderEquality() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER)));

    TypePair typePair1 = unionType1.getTypesUnderEquality(unionType2);
    TypePair typePair2 = unionType2.getTypesUnderEquality(unionType1);

    assertTrue(typePair1.typeA == null && typePair1.typeB == unionType2);
    assertTrue(typePair2.typeA == unionType1 && typePair2.typeB == null);
  }

  @org.junit.Test
  public void testGetTypesUnderInequality() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER)));

    TypePair typePair1 = unionType1.getTypesUnderInequality(unionType2);
    TypePair typePair2 = unionType2.getTypesUnderInequality(unionType1);

    assertTrue(typePair1.typeA == unionType1 && typePair1.typeB == null);
    assertTrue(typePair2.typeA == null && typePair2.typeB == unionType2);
  }

  @org.junit.Test
  public void testGetTypesUnderShallowInequality() {
    UnionType unionType1 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.NUMBER), registry.getNativeType(JSTypeNative.STRING)));
    UnionType unionType2 = createUnionType(registry, ImmutableSet.of(registry.getNativeType(JSTypeNative.STRING), registry.getNativeType(JSTypeNative.NUMBER)));

    TypePair typePair1 = unionType1.getTypesUnderShallowInequality(unionType2);
    TypePair typePair2 = unionType2.getTypesUnderShallowInequality(unionType1);

    assertTrue(typePair1.typeA == null && typePair1.typeB == unionType2);
    assertTrue(typePair2.typeA == null && typePair2.typeB == unionType1);
  }

  private UnionType createUnionType(JSTypeRegistry registry, ImmutableSet<JSType> alternates) {
    return new UnionType(registry, alternates);
  }

  @org.junit.BeforeClass
  public static void setUp() {
    // Initialize the registry with the necessary types
    registry.registerType(JSTypeNative.NUMBER);
    registry.registerType(JSTypeNative.STRING);
    registry.registerType(JSTypeNative.BOOLEAN);
    registry.registerType(JSTypeNative.NULL);
    registry.registerType(JSTypeNative.NO_TYPE);
    registry.registerType(JSTypeNative.NO_OBJECT_TYPE);
    registry.registerType(IntType.class);
  }

  @org.junit.After
  public void tearDown() {
    TestingUtil.cleanupRegistry(registry);
  }
}