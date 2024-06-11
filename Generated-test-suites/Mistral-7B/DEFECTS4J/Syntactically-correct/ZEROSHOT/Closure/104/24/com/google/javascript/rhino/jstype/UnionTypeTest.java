import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.BooleanType;
import static com.google.javascript.rhino.jstype.JSType.NullType;
import static com.google.javascript.rhino.jstype.JSType.NumberType;
import static com.google.javascript.rhino.jstype.JSType.StringType;
import static com.google.javascript.rhino.jstype.JSType.UnionType;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);
    JSType unionType = new UnionType(registry, alternates);
    assertEquals(StringType.getQualifiedName(), unionType.getQualifiedName());
    assertEquals(NumberType.getQualifiedName(), unionType.getQualifiedName(true));
  }

  @Test
  public void testUnionTypeAlternates() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);
    JSType unionType = new UnionType(registry, alternates);
    assertEquals(alternates, unionType.getAlternates());
  }

  @Test
  public void testUnionTypeMatchesContexts() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);
    JSType unionType = new UnionType(registry, alternates);

    // Test string context
    assertTrue(unionType.matchesStringContext());

    // Test number context
    assertTrue(unionType.matchesNumberContext());

    // Test object context
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(
        new ObjectType(registry, ImmutableSet.of("x"), ImmutableSet.of("getX")),
        new ObjectType(registry, ImmutableSet.of("y"), ImmutableSet.of("getY")));

    JSType unionType = new UnionType(registry, alternates);
    String propertyName = "x";
    JSType expectedPropertyType = new ObjectType(registry, ImmutableSet.of("x"), ImmutableSet.of("getX"));

    JSType propertyType = unionType.findPropertyType(propertyName);
    assertEquals(expectedPropertyType, propertyType);
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);
    JSType unionType = new UnionType(registry, alternates);

    JSType that = StringType;
    assertTrue(unionType.canAssignTo(that));

    JSType anotherUnionType = new UnionType(registry, ImmutableSet.of(BooleanType, NullType));
    assertFalse(unionType.canAssignTo(anotherUnionType));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);
    JSType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NullType);
    JSType unionType = new UnionType(registry, alternates);

    JSType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertNotNull(restrictedUnionType);
    assertTrue(restrictedUnionType.isNullType());
  }

  @Test
  public void testUnionTypeTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates1 = ImmutableSet.of(StringType, NumberType);
    Set<JSType> alternates2 = ImmutableSet.of(NumberType, StringType);

    JSType unionType1 = new UnionType(registry, alternates1);
    JSType unionType2 = new UnionType(registry, alternates2);

    TernaryValue result = unionType1.testForEquality(unionType2);
    assertEquals(TernaryValue.UNKNOWN, result);

    result = unionType1.testForEquality(unionType1);
    assertEquals(TernaryValue.TRUE, result);
  }

  @Test
  public void testUnionTypeIsNullable() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NullType);
    JSType unionType = new UnionType(registry, alternates);

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(UnknownType.getInstance(registry));
    JSType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates1 = ImmutableSet.of(StringType, NumberType);
    Set<JSType> alternates2 = ImmutableSet.of(NumberType, StringType);

    JSType unionType1 = new UnionType(registry, alternates1);
    JSType unionType2 = new UnionType(registry, alternates2);
    JSType expectedLeastSupertype = ObjectType.getInstance(registry, ImmutableSet.of());

    JSType leastSupertype = unionType1.getLeastSupertype(unionType2);
    assertEquals(expectedLeastSupertype, leastSupertype);
  }

  @Test
  public void testUnionTypeMeet() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates1 = ImmutableSet.of(StringType, NumberType);
    Set<JSType> alternates2 = ImmutableSet.of(NumberType, StringType);

    JSType unionType1 = new UnionType(registry, alternates1);
    JSType unionType2 = new UnionType(registry, alternates2);

    JSType meetType = unionType1.meet(unionType2);
    assertNotNull(meetType);
    assertTrue(meetType.isUnionType());

    Set<JSType> meetAlternates = ((UnionType) meetType).getAlternates();
    assertEquals(alternates1, meetAlternates);
  }

  @Test
  public void testUnionTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates1 = ImmutableSet.of(StringType, NumberType);
    Set<JSType> alternates2 = ImmutableSet.of(NumberType, StringType);

    JSType unionType1 = new UnionType(registry, alternates1);
    JSType unionType2 = new UnionType(registry, alternates2);

    assertNotEquals(unionType1, unionType2);

    Set<JSType> alternates3 = ImmutableSet.copyOf(alternates1);
    JSType unionType3 = new UnionType(registry, alternates3);

    assertEquals(unionType1, unionType3);
  }

  @Test
  public void testUnionTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);

    JSType unionType1 = new UnionType(registry, alternates);
    JSType unionType2 = new UnionType(registry, alternates);

    assertEquals(unionType1.hashCode(), unionType2.hashCode());
  }

  @Test
  public void testUnionTypeIsUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);

    JSType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);

    JSType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.isObject());
  }

  @Test
  public void testUnionTypeContains() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);

    JSType unionType = new UnionType(registry, alternates);

    JSType stringType = StringType;
    JSType numberType = NumberType;

    assertTrue(unionType.contains(stringType));
    assertTrue(unionType.contains(numberType));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType, ObjectType.getInstance(registry, ImmutableSet.of()));

    JSType unionType = new UnionType(registry, alternates);
    JSType numberType = NumberType;

    JSType restrictedUnionType = unionType.getRestrictedUnion(numberType);

    assertNotNull(restrictedUnionType);
    assertFalse(restrictedUnionType.isObject());
    assertEquals(StringType, restrictedUnionType);
  }

  @Test
  public void testUnionTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(StringType, NumberType);

    JSType unionType = new UnionType(registry, alternates);

    String expectedString = "(%s|%s)";
    String actualString = unionType.toString();

    assertEquals(expectedString, actualString);
  }

  @Test
  public void testUnionTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates1 = ImmutableSet.of(StringType, NumberType);
    Set<JSType> alternates2 = ImmutableSet.of(NumberType, StringType);

    JSType unionType1 = new UnionType(registry, alternates1);
    JSType unionType2 = new UnionType(registry, alternates2);

    assertTrue(unionType1.isSubtype(unionType2));
  }

  @Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates = ImmutableSet.of(BooleanType, NullType);

    JSType unionType = new UnionType(registry, alternates);

    BooleanLiteralSet possibleToBooleanOutcomes = unionType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.BOTH, possibleToBooleanOutcomes);
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates1 = ImmutableSet.of(StringType, NumberType);
    Set<JSType> alternates2 = ImmutableSet.of(NumberType, StringType);

    JSType unionType1 = new UnionType(registry, alternates1);
    JSType unionType2 = new UnionType(registry, alternates2);

    TypePair typesUnderEquality = unionType1.getTypesUnderEquality(unionType2);

    assertNotNull(typesUnderEquality.typeA);
    assertEquals(ObjectType.getInstance(registry, ImmutableSet.of()), typesUnderEquality.typeA);
    assertNotNull(typesUnderEquality.typeB);
    assertEquals(ObjectType.getInstance(registry, ImmutableSet.of()), typesUnderEquality.typeB);
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates1 = ImmutableSet.of(StringType, NumberType);
    Set<JSType> alternates2 = ImmutableSet.of(NumberType, StringType);

    JSType unionType1 = new UnionType(registry, alternates1);
    JSType unionType2 = new UnionType(registry, alternates2);

    TypePair typesUnderInequality = unionType1.getTypesUnderInequality(unionType2);

    assertNotNull(typesUnderInequality.typeA);
    assertNotNull(typesUnderInequality.typeB);
    assertEquals(NumberType, typesUnderInequality.typeA);
    assertEquals(StringType, typesUnderInequality.typeB);
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> alternates1 = ImmutableSet.of(StringType, NumberType);
    Set<JSType> alternates2 = ImmutableSet.of(NumberType, StringType);

    JSType unionType1 = new UnionType(registry, alternates1);
    JSType unionType2 = new UnionType(registry, alternates2);

    TypePair typesUnderShallowInequality = unionType1.getTypesUnderShallowInequality(unionType2);

    assertNotNull(typesUnderShallowInequality.typeA);
    assertNotNull(typesUnderShallowInequality.typeB);
    assertEquals(NumberType, typesUnderShallowInequality.typeA);
    assertEquals(StringType, typesUnderShallowInequality.typeB);
  }
}