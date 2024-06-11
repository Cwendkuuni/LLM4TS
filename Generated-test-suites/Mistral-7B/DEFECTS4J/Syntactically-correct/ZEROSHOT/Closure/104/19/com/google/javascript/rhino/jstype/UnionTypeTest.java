import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(stringType);
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);
    UnionType stringOrBoolean = new UnionType(registry, stringSet);
    UnionType booleanOrString = new UnionType(registry, booleanSet);

    assertEquals(stringOrBoolean, booleanOrString);
    assertEquals(stringOrBoolean.toString(), "(|String|Boolean)");

    JSType stringAndBoolean = new UnionType(registry, ImmutableSet.ofAll(stringSet, booleanSet));
    assertEquals(stringAndBoolean.toString(), "(|String|Boolean)");
  }

  @Test
  public void testMatchesContexts() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(stringType);
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);
    UnionType stringOrBoolean = new UnionType(registry, stringSet);

    assertTrue(stringOrBoolean.matchesNumberContext());
    assertTrue(stringOrBoolean.matchesStringContext());
    assertTrue(stringOrBoolean.matchesObjectContext());

    assertFalse(stringOrBoolean.matchesNumberContext(registry));
    assertFalse(stringOrBoolean.matchesStringContext(registry));
    assertFalse(stringOrBoolean.matchesObjectContext(registry));

    Set<JSType> nullSet = ImmutableSet.of(nullType);
    UnionType nullableStringOrBoolean = new UnionType(registry, ImmutableSet.ofAll(stringSet, booleanSet, nullSet));

    assertTrue(nullableStringOrBoolean.isNullable());
    assertFalse(nullableStringOrBoolean.matchesNumberContext());
    assertFalse(nullableStringOrBoolean.matchesStringContext());
    assertTrue(nullableStringOrBoolean.matchesObjectContext());

    Set<JSType> voidSet = ImmutableSet.of(voidType);
    UnionType nullableVoidOrStringOrBoolean = new UnionType(registry, ImmutableSet.ofAll(voidSet, stringSet, booleanSet, nullSet));

    assertFalse(nullableVoidOrStringOrBoolean.isNullable());
    assertFalse(nullableVoidOrStringOrBoolean.matchesNumberContext());
    assertFalse(nullableVoidOrStringOrBoolean.matchesStringContext());
    assertFalse(nullableVoidOrStringOrBoolean.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringSet = ImmutableSet.of(stringType);
    Set<JSType> numberSet = ImmutableSet.of(numberType);
    UnionType stringOrNumber = new UnionType(registry, ImmutableSet.ofAll(stringSet, numberSet));

    assertEquals(stringType, stringOrNumber.findPropertyType("toString"));
    assertEquals(numberType, stringOrNumber.findPropertyType("valueOf"));

    assertNull(stringOrNumber.findPropertyType("toString2"));
  }

  @Test
  public void testCanAssignTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> numberSet = ImmutableSet.of(numberType);
    Set<JSType> stringSet = ImmutableSet.of(stringType);
    UnionType numberOrString = new UnionType(registry, ImmutableSet.ofAll(numberSet, stringSet));

    UnionType booleanTypeUnion = registry.getUnionType(ImmutableSet.of(booleanType));
    assertTrue(numberOrString.canAssignTo(booleanTypeUnion));

    UnionType nullableNumberOrString = new UnionType(registry, ImmutableSet.ofAll(numberSet, nullSet, stringSet));
    assertTrue(nullableNumberOrString.canAssignTo(booleanTypeUnion));

    UnionType voidTypeUnion = registry.getVoidType();
    assertFalse(numberOrString.canAssignTo(voidTypeUnion));
    assertFalse(nullableNumberOrString.canAssignTo(voidTypeUnion));

    UnionType objectTypeUnion = registry.getObjectType();
    assertTrue(numberOrString.canAssignTo(objectTypeUnion));
    assertTrue(nullableNumberOrString.canAssignTo(objectTypeUnion));

    UnionType stringTypeUnion = registry.getStringType();
    assertTrue(stringTypeUnion.canAssignTo(numberOrString));
    assertTrue(stringTypeUnion.canAssignTo(nullableNumberOrString));
  }

  @Test
  public void testCanBeCalled() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);
    UnionType booleanOrFunction = new UnionType(registry, ImmutableSet.ofAll(booleanSet, functionType));

    assertTrue(booleanOrFunction.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> nullSet = ImmutableSet.of(nullType);
    Set<JSType> stringSet = ImmutableSet.of(stringType);
    UnionType nullableString = new UnionType(registry, ImmutableSet.ofAll(stringSet, nullSet));

    UnionType nonNullableString = nullableString.restrictByNotNullOrUndefined();
    assertEquals(stringType, nonNullableString);
  }

  @Test
  public void testTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);
    UnionType booleanOrNumber = new UnionType(registry, ImmutableSet.ofAll(booleanSet, numberType));

    UnionType booleanOrString = new UnionType(registry, ImmutableSet.ofAll(booleanSet, stringType));

    assertEquals(UNKNOWN, booleanOrNumber.testForEquality(booleanOrString));
    assertEquals(UNKNOWN, booleanOrString.testForEquality(booleanOrNumber));

    assertEquals(Boolean.TRUE, booleanOrNumber.testForEquality(booleanOrNumber));
    assertEquals(Boolean.TRUE, booleanOrString.testForEquality(booleanOrString));
  }

  @Test
  public void testIsUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);
    UnionType booleanOrString = new UnionType(registry, ImmutableSet.ofAll(booleanSet, stringType));

    assertFalse(booleanOrString.isUnknownType());

    UnionType unknownTypeUnion = registry.getUnknownType();
    assertTrue(unknownTypeUnion.isUnknownType());
  }

  @Test
  public void testLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> numberSet = ImmutableSet.of(numberType);
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);
    UnionType numberOrBoolean = new UnionType(registry, ImmutableSet.ofAll(numberSet, booleanSet));
    UnionType booleanOrNumber = new UnionType(registry, ImmutableSet.ofAll(booleanSet, numberSet));

    UnionType leastSupertype = numberOrBoolean.getLeastSupertype(booleanOrNumber);
    assertEquals(UNKNOWN, leastSupertype);

    UnionType numberTypeUnion = registry.getNumberType();
    UnionType leastSupertype2 = numberTypeUnion.getLeastSupertype(booleanOrNumber);
    assertEquals(numberTypeUnion, leastSupertype2);

    UnionType booleanTypeUnion = registry.getBooleanType();
    UnionType leastSupertype3 = numberOrBoolean.getLeastSupertype(booleanTypeUnion);
    assertEquals(booleanTypeUnion, leastSupertype3);

    UnionType objectTypeUnion = registry.getObjectType();
    UnionType leastSupertype4 = numberOrBoolean.getLeastSupertype(objectTypeUnion);
    assertEquals(objectTypeUnion, leastSupertype4);
  }

  @Test
  public void testMeet() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> numberSet = ImmutableSet.of(numberType);
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);
    UnionType numberOrBoolean = new UnionType(registry, ImmutableSet.ofAll(numberSet, booleanSet));
    UnionType booleanOrNumber = new UnionType(registry, ImmutableSet.ofAll(booleanSet, numberSet));

    UnionType meet = numberOrBoolean.meet(booleanOrNumber);
    assertEquals(UNKNOWN, meet);

    UnionType numberTypeUnion = registry.getNumberType();
    UnionType meet2 = numberTypeUnion.meet(booleanOrNumber);
    assertEquals(numberTypeUnion, meet2);

    UnionType booleanTypeUnion = registry.getBooleanType();
    UnionType meet3 = numberOrBoolean.meet(booleanTypeUnion);
    assertEquals(booleanTypeUnion, meet3);

    UnionType objectTypeUnion = registry.getObjectType();
    UnionType meet4 = numberOrBoolean.meet(objectTypeUnion);
    assertEquals(objectTypeUnion, meet4);
  }

  @Test
  public void testEqualsAndHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);

    UnionType booleanOrString = new UnionType(registry, ImmutableSet.ofAll(booleanSet, stringType));
    UnionType booleanOrString2 = new UnionType(registry, ImmutableSet.ofAll(booleanSet, stringType));

    assertTrue(booleanOrString.equals(booleanOrString2));
    assertEquals(booleanOrString.hashCode(), booleanOrString2.hashCode());

    UnionType booleanOrNumber = new UnionType(registry, ImmutableSet.ofAll(booleanSet, numberType));
    assertFalse(booleanOrString.equals(booleanOrNumber));
    assertNotEquals(booleanOrString.hashCode(), booleanOrNumber.hashCode());

    UnionType booleanOrNull = new UnionType(registry, ImmutableSet.ofAll(booleanSet, nullType));
    assertFalse(booleanOrString.equals(booleanOrNull));
    assertNotEquals(booleanOrString.hashCode(), booleanOrNull.hashCode());
  }

  @Test
  public void testIsUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);

    UnionType booleanOrString = new UnionType(registry, ImmutableSet.ofAll(booleanSet, stringType));
    assertTrue(booleanOrString.isUnionType());

    UnionType booleanTypeUnion = new UnionType(registry, ImmutableSet.of(booleanType));
    assertTrue(booleanTypeUnion.isUnionType());

    UnionType numberTypeUnion = new UnionType(registry, ImmutableSet.of(numberType));
    assertFalse(numberTypeUnion.isUnionType());

    UnionType objectTypeUnion = new UnionType(registry, ImmutableSet.of(objectType));
    assertFalse(objectTypeUnion.isUnionType());
  }

  @Test
  public void testIsObject() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);

    UnionType booleanOrString = new UnionType(registry, ImmutableSet.ofAll(booleanSet, stringType));
    assertTrue(booleanOrString.isObject());

    UnionType booleanOrNumber = new UnionType(registry, ImmutableSet.ofAll(booleanSet, numberType));
    assertTrue(booleanOrNumber.isObject());

    UnionType voidTypeUnion = new UnionType(registry, ImmutableSet.of(voidType));
    assertFalse(voidTypeUnion.isObject());
  }

  @Test
  public void testGetAlternates() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> booleanSet = ImmutableSet.of(booleanType);
    Set<JSType> numberSet = ImmutableSet.of(numberType);

    UnionType booleanOrString = new UnionType(registry, ImmutableSet.ofAll(booleanSet, stringType));
    assertEquals(ImmutableSet.of(booleanType, stringType), booleanOrString.getAlternates());

    UnionType booleanOrNumber = new UnionType(registry, ImmutableSet.ofAll(booleanSet, numberType));
    assertEquals(ImmutableSet.of(booleanType, numberType), booleanOrNumber.getAlternates());

    UnionType booleanOrNull = new UnionType(registry, ImmutableSet.ofAll(booleanSet, nullType));
    assertEquals(ImmutableSet.of(booleanType, nullType), booleanOrNull.getAlternates());
  }
}