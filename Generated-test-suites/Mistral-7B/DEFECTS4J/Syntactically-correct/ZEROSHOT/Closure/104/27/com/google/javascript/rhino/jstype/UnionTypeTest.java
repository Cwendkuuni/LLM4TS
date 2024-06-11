import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));
    assertEquals(2, unionType.getAlternates().size());
    assertTrue(unionType.isUnionType());
    assertFalse(unionType.isObject());
  }

  @Test
  public void testUnionTypeMatchesContexts() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

    assertEquals(numberType, unionType.findPropertyType("length"));
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

    JSType booleanType = registry.getBooleanType();
    JSType intType = registry.getIntType();

    assertTrue(unionType.canAssignTo(booleanType));
    assertTrue(unionType.canAssignTo(intType));
    assertFalse(unionType.canAssignTo(stringType));
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType nullType = registry.getNullType();
    JSType stringType = registry.getStringType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, nullType));

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();

    assertNull(restrictedType);
  }

  @Test
  public void testUnionTypeTestForEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

    UnionType anotherUnionType = new UnionType(registry, ImmutableSet.of(booleanType, stringType));

    TernaryValue result = unionType.testForEquality(anotherUnionType);

    assertEquals(UNKNOWN, result);
  }

  @Test
  public void testUnionTypeIsNullable() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));
    JSType objectType = registry.getObjectType();

    assertEquals(objectType, unionType.getLeastSupertype(objectType));
  }

  @Test
  public void testUnionTypeMeet() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    UnionType unionTypeA = new UnionType(registry, ImmutableSet.of(stringType, numberType));
    JSType booleanType = registry.getBooleanType();
    UnionType unionTypeB = new UnionType(registry, ImmutableSet.of(booleanType, numberType));

    UnionType result = unionTypeA.meet(unionTypeB);

    assertEquals(numberType, result);
  }

  @Test
  public void testUnionTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    UnionType unionTypeA = new UnionType(registry, ImmutableSet.of(stringType, booleanType));
    UnionType unionTypeB = new UnionType(registry, ImmutableSet.of(booleanType, stringType));

    assertTrue(unionTypeA.equals(unionTypeB));
  }

  @Test
  public void testUnionTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType booleanType = registry.getBooleanType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, booleanType));

    int hashCode = unionType.hashCode();

    UnionType unionType2 = new UnionType(registry, ImmutableSet.of(booleanType, stringType));

    assertEquals(hashCode, unionType2.hashCode());
  }

  @Test
  public void testUnionTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

    assertEquals("(String|Number)", unionType.toString());
  }

  @Test
  public void testUnionTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

    assertTrue(unionType.isSubtype(stringType));
    assertTrue(unionType.isSubtype(numberType));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    UnionType unionType = new UnionType(registry, ImmutableSet.of(stringType, numberType));

    JSType intType = registry.getIntType();

    UnionType restrictedType = unionType.getRestrictedUnion(intType);

    assertNull(restrictedType);
  }
}