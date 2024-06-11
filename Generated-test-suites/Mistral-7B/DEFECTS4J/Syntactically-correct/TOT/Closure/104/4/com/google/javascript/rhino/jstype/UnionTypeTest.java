import static com.google.javascript.rhino.testing.Assert.*;
import static com.google.javascript.rhino.testing.TypeEquals.*;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeBasic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> set1 = ImmutableSet.of(new StringType(registry), new NumberType(registry));
    UnionType unionType1 = new UnionType(registry, set1);

    JSType set2 = unionType1.getAlternates().iterator().next();

    assertEquals(set1, unionType1.getAlternates());
    assertEquals(set2, unionType1.getAlternates().iterator().next());
    assertFalse(unionType1.isNullType());
    assertFalse(unionType1.isVoidType());

    // Matches number context
    assertTrue(unionType1.matchesNumberContext());

    // Matches string context
    assertTrue(unionType1.matchesStringContext());

    // Matches object context
    assertTrue(unionType1.matchesObjectContext());

    // Find property type
    UnionType propertyTypeUnion = new UnionType(registry, ImmutableSet.of(new StringType(registry), new NumberType(registry)));
    assertEquals(propertyTypeUnion, unionType1.findPropertyType("toString"));

    // Can assign to
    JSType stringType = new StringType(registry);
    assertTrue(unionType1.canAssignTo(stringType));

    // Can be called
    assertTrue(unionType1.canBeCalled());

    // Restrict by not null
    UnionType restrictedUnionType1 = unionType1.restrictByNotNullOrUndefined();
    assertFalse(restrictedUnionType1.isNullType());
    assertFalse(restrictedUnionType1.isUnknownType());
    assertEquals(set1, restrictedUnionType1.getAlternates());

    // Test for equality
    UnionType unionType2 = new UnionType(registry, set1);
    assertEquals(unionType1, unionType2);
    assertFalse(unionType1.equals(new NumberType(registry)));

    // Is nullable
    assertFalse(unionType1.isNullable());

    // Get least supertype
    JSType leastSupertype = unionType1.getLeastSupertype(new StringType(registry));
    assertEquals(unionType1, leastSupertype);

    // Meet
    UnionType meetUnionType = new UnionType(registry, ImmutableSet.of(new StringType(registry), new NumberType(registry)));
    UnionType result = unionType1.meet(meetUnionType);
    assertNull(result);

    // Equals and hashcode
    UnionType equalUnionType = new UnionType(registry, set1);
    UnionType hashcodeUnionType = new UnionType(registry, set1);
    assertEquals(equalUnionType, hashcodeUnionType);
    assertEquals(equalUnionType.hashCode(), hashcodeUnionType.hashCode());

    // Is union type
    assertTrue(unionType1.isUnionType());

    // Is object
    assertTrue(unionType1.isObject());

    // Contains
    UnionType containsUnionType = new UnionType(registry, ImmutableSet.of(new StringType(registry), new NumberType(registry)));
    assertTrue(containsUnionType.contains(new StringType(registry)));

    // Get restricted union
    UnionType restrictedUnionType2 = unionType1.getRestrictedUnion(new StringType(registry));
    assertFalse(restrictedUnionType2.isObject());
    assertEquals(new NumberType(registry), restrictedUnionType2);

    // toString
    assertEquals("(String, Number)", unionType1.toString());

    // isSubtype
    assertTrue(new StringType(registry).isSubtype(unionType1));

    // getRestrictedTypeGivenToBooleanOutcome
    UnionType booleanRestrictedUnionType = unionType1.restrictByNotNullOrUndefined();
    assertEquals(booleanRestrictedUnionType, unionType1.getRestrictedTypeGivenToBooleanOutcome(true));
    assertEquals(booleanRestrictedUnionType, unionType1.getRestrictedTypeGivenToBooleanOutcome(false));

    // getPossibleToBooleanOutcomes
    assertEquals(BooleanLiteralSet.BOTH, unionType1.getPossibleToBooleanOutcomes());

    // getTypesUnderEquality
    UnionType equalityTypesUnion = new UnionType(registry, ImmutableSet.of(new StringType(registry), new StringType(registry)));
    TypePair typesUnderEquality = unionType1.getTypesUnderEquality(equalityTypesUnion);
    assertEquals(unionType1, typesUnderEquality.typeA);
    assertEquals(equalityTypesUnion, typesUnderEquality.typeB);

    // getTypesUnderInequality
    UnionType inequalityTypesUnion = new UnionType(registry, ImmutableSet.of(new NumberType(registry), new StringType(registry)));
    TypePair typesUnderInequality = unionType1.getTypesUnderInequality(inequalityTypesUnion);
    assertEquals(inequalityTypesUnion, typesUnderInequality.typeA);
    assertEquals(unionType1, typesUnderInequality.typeB);

    // getTypesUnderShallowInequality
    UnionType shallowInequalityTypesUnion = new UnionType(registry, ImmutableSet.of(new NumberType(registry), new NumberType(registry)));
    TypePair typesUnderShallowInequality = unionType1.getTypesUnderShallowInequality(shallowInequalityTypesUnion);
    assertEquals(shallowInequalityTypesUnion, typesUnderShallowInequality.typeA);
    assertEquals(unionType1, typesUnderShallowInequality.typeB);
  }
}