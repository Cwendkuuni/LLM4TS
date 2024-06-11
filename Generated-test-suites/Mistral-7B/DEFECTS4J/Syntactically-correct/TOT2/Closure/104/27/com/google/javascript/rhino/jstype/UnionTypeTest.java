import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeBasicFunctionality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));

    assertEquals(Int.instance(), intOrString.findPropertyType("length"));
    assertEquals(String.instance(), intOrString.findPropertyType("toString"));

    JSType intOrString2 = new UnionType(registry, ImmutableSet.of(String.instance(), Int.instance()));
    assertEquals(String.instance(), intOrString2.findPropertyType("length"));
    assertEquals(Int.instance(), intOrString2.findPropertyType("toString"));
  }

  @Test
  public void testUnionTypeMatchesContext() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));

    assertTrue(intOrString.matchesNumberContext());
    assertTrue(intOrString.matchesStringContext());
    assertTrue(intOrString.matchesObjectContext());
  }

  @Test
  public void testUnionTypeCanAssign() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));
    JSType intType = Int.instance();

    assertTrue(intOrString.canAssignTo(intType));
    assertTrue(intType.canAssignTo(intOrString));

    JSType stringType = String.instance();
    assertTrue(intOrString.canAssignTo(stringType));
    assertFalse(stringType.canAssignTo(intOrString));
  }

  @Test
  public void testUnionTypeFindPropertyTypeWithNull() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrNull = new UnionType(registry, ImmutableSet.of(Int.instance(), NullType.instance()));
    assertNull(intOrNull.findPropertyType(null));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnionTypeCannotGetSubType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));
    assertEquals(UnknownType.instance(), intOrString.getLeastSupertype(Bool.instance()));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));

    JSType intOrNull = intOrString.getRestrictedUnion(NullType.instance());
    assertEquals(Int.instance(), intOrNull.findPropertyType("length"));
    assertNull(intOrNull.findPropertyType("toString"));

    JSType stringOrNull = intOrString.getRestrictedUnion(NullType.instance());
    assertNull(stringOrNull.findPropertyType("length"));
    assertNull(stringOrNull.findPropertyType("toString"));
  }

  @Test
  public void testUnionTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));

    assertEquals("(Int, String)", intOrString.toString());
  }

  @Test
  public void testUnionTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));
    UnionType intOrString2 = new UnionType(registry, ImmutableSet.of(String.instance(), Int.instance()));

    assertTrue(intOrString.isSubtype(intOrString2));
    assertTrue(intOrString2.isSubtype(intOrString));
  }

  @Test
  public void testUnionTypeEquals() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));
    UnionType intOrString2 = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));

    assertTrue(intOrString.equals(intOrString2));
  }

  @Test
  public void testUnionTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));
    UnionType intOrString2 = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));

    assertEquals(intOrString.hashCode(), intOrString2.hashCode());
  }

  @Test
  public void testUnionTypeIsUnionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));

    assertTrue(intOrString.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrString = new UnionType(registry, ImmutableSet.of(Int.instance(), String.instance()));

    assertTrue(intOrString.isObject());
  }

  @Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrBool = new UnionType(registry, ImmutableSet.of(Int.instance(), Bool.instance()));
    JSType intType = Int.instance();

    assertEquals(Int.instance(), intOrBool.getRestrictedTypeGivenToBooleanOutcome(true).findPropertyType("length"));
    assertEquals(Bool.instance(), intOrBool.getRestrictedTypeGivenToBooleanOutcome(false).findPropertyType("toString"));
  }

  @Test
  public void testUnionTypeGetPossibleToBooleanOutcomes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrBool = new UnionType(registry, ImmutableSet.of(Int.instance(), Bool.instance()));

    assertEquals(BooleanLiteralSet.BOTH, intOrBool.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrBool = new UnionType(registry, ImmutableSet.of(Int.instance(), Bool.instance()));
    UnionType stringOrBool = new UnionType(registry, ImmutableSet.of(String.instance(), Bool.instance()));

    UnionType intOrBoolEqStringOrBool = intOrBool.getTypesUnderEquality(stringOrBool);
    UnionType stringOrBoolEqIntOrBool = stringOrBool.getTypesUnderEquality(intOrBool);

    assertEquals(intOrBoolEqStringOrBool, stringOrBoolEqIntOrBool);
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrBool = new UnionType(registry, ImmutableSet.of(Int.instance(), Bool.instance()));
    UnionType stringOrBool = new UnionType(registry, ImmutableSet.of(String.instance(), Bool.instance()));

    UnionType intOrBoolInequalStringOrBool = intOrBool.getTypesUnderInequality(stringOrBool);
    UnionType stringOrBoolInequalIntOrBool = stringOrBool.getTypesUnderInequality(intOrBool);

    assertNotSame(intOrBoolInequalStringOrBool, stringOrBoolInequalIntOrBool);
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    UnionType intOrBool = new UnionType(registry, ImmutableSet.of(Int.instance(), Bool.instance()));
    UnionType stringOrBool = new UnionType(registry, ImmutableSet.of(String.instance(), Bool.instance()));

    UnionType intOrBoolShallowInequalStringOrBool = intOrBool.getTypesUnderShallowInequality(stringOrBool);
    UnionType stringOrBoolShallowInequalIntOrBool = stringOrBool.getTypesUnderShallowInequality(intOrBool);

    assertEquals(intOrBool, intOrBoolShallowInequalStringOrBool);
    assertEquals(stringOrBool, stringOrBoolShallowInequalIntOrBool);
  }
}