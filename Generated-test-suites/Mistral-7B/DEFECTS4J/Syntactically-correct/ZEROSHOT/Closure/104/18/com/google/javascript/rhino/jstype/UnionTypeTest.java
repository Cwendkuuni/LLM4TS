import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.VoidType;

public class UnionTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> type1Alternates = new HashSet<JSType>() {{
      add(registry.getTypes().getNativeType(JSTypeNative.BOOLEAN));
      add(registry.getTypes().getNativeType(JSTypeNative.STRING));
    }};

    UnionType type1 = new UnionType(registry, type1Alternates);
    UnionType type2 = new UnionType(registry, type1Alternates);

    assertEquals(type1, type2);
  }

  @Test
  public void testMatchesNumberContext() {
    UnionType numberStringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getNumberType(),
            registry.getStringType(),
            registry.getBooleanType())));

    assertTrue(numberStringBoolean.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    UnionType numberStringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getNumberType(),
            registry.getStringType(),
            registry.getBooleanType())));

    assertTrue(numberStringBoolean.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    UnionType numberStringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getNumberType(),
            registry.getStringType(),
            registry.getBooleanType())));

    assertTrue(numberStringBoolean.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    JSType propertyType = stringBoolean.findPropertyType("toString");

    assertEquals(registry.getStringType(), propertyType);
  }

  @Test
  public void testCanAssignTo() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType numberType = registry.getNumberType();

    assertTrue(stringBoolean.canAssignTo(numberType));
  }

  @Test
  public void testCanBeCalled() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    assertTrue(stringBoolean.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType restricted = stringBoolean.restrictByNotNullOrUndefined();
    assertTrue(restricted.isSubtype(registry.getNullType()));
    assertTrue(restricted.isSubtype(registry.getUndefinedType()));
  }

  @Test
  public void testTestForEquality() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType booleanString = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getBooleanType(),
            registry.getStringType())));

    TernaryValue result = stringBoolean.testForEquality(booleanString);

    assertEquals(UNKNOWN, result);
  }

  @Test
  public void testIsNullable() {
    UnionType booleanType = registry.getBooleanType();
    UnionType nullableBoolean = new UnionType(registry, Arrays.asList(booleanType, registry.getNullType()));

    assertFalse(nullableBoolean.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType booleanType = registry.getBooleanType();
    UnionType unknownBoolean = new UnionType(registry, Arrays.asList(booleanType, registry.getUnknownType()));

    assertTrue(unknownBoolean.isUnknownType());
  }

  @Test
  public void testLeastSupertype() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType numberType = registry.getNumberType();

    assertEquals(registry.getObjectType(), stringBoolean.getLeastSupertype(numberType));
  }

  @Test
  public void testMeet() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType numberString = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getNumberType(),
            registry.getStringType())));

    UnionType result = stringBoolean.meet(numberString);

    assertEquals(registry.getStringType(), result);
  }

  @Test
  public void testEquals() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType booleanString = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getBooleanType(),
            registry.getStringType())));

    assertFalse(stringBoolean.equals(booleanString));

    UnionType same = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(stringBoolean, booleanString)));

    assertTrue(same.equals(stringBoolean));
    assertTrue(same.equals(booleanString));
  }

  @Test
  public void testHashCode() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType booleanString = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getBooleanType(),
            registry.getStringType())));

    assertEquals(stringBoolean.hashCode(), booleanString.hashCode());
  }

  @Test
  public void testIsUnionType() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    assertTrue(stringBoolean.isUnionType());
  }

  @Test
  public void testIsObject() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    assertTrue(stringBoolean.isObject());
  }

  @Test
  public void testContains() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    assertTrue(stringBoolean.contains(registry.getStringType()));
    assertTrue(stringBoolean.contains(registry.getBooleanType()));
    assertFalse(stringBoolean.contains(registry.getNumberType()));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    JSType restricted = stringBoolean.getRestrictedUnion(registry.getStringType());

    assertEquals(registry.getBooleanType(), restricted);
  }

  @Test
  public void testToString() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    assertEquals("(String|Boolean)", stringBoolean.toString());
  }

  @Test
  public void testIsSubtype() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType booleanType = registry.getBooleanType();
    UnionType stringType = registry.getStringType();

    assertTrue(stringBoolean.isSubtype(booleanType));
    assertTrue(stringBoolean.isSubtype(stringType));
    assertFalse(booleanType.isSubtype(stringBoolean));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    BooleanLiteralSet literals = stringBoolean.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.BOTH, literals);

    JSType booleanType = registry.getBooleanType();

    assertEquals(booleanType, stringBoolean.getRestrictedTypeGivenToBooleanOutcome(true));
    assertEquals(registry.getStringType(), stringBoolean.getRestrictedTypeGivenToBooleanOutcome(false));
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    BooleanLiteralSet literals = stringBoolean.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.BOTH, literals);
  }

  @Test
  public void testGetTypesUnderEquality() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType booleanString = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getBooleanType(),
            registry.getStringType())));

    TypePair pair = stringBoolean.getTypesUnderEquality(booleanString);

    assertEquals(registry.getStringType(), pair.typeA);
    assertEquals(registry.getBooleanType(), pair.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType booleanString = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getBooleanType(),
            registry.getStringType())));

    TypePair pair = stringBoolean.getTypesUnderInequality(booleanString);

    assertEquals(registry.getBooleanType(), pair.typeA);
    assertEquals(registry.getStringType(), pair.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType booleanString = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getBooleanType(),
            registry.getStringType())));

    TypePair pair = stringBoolean.getTypesUnderShallowInequality(booleanString);

    assertEquals(registry.getBooleanType(), pair.typeA);
    assertEquals(registry.getStringType(), pair.typeB);
  }

  @Test
  public void testVisit() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    Visitor<VoidType> visitor = new Visitor<VoidType>() {
      @Override
      public VoidType caseUnionType(UnionType type) {
        return null;
      }
    };

    stringBoolean.visit(visitor);

    // No exception should be thrown
  }

  @Test
  public void testResolveInternal() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType numberBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getNumberType(),
            registry.getBooleanType())));

    UnionType stringNumberBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getNumberType(),
            registry.getBooleanType())));

    UnionType stringNumberBooleanRestricted = stringNumberBoolean.restrictByNotNullOrUndefined();

    assertEquals(stringNumberBooleanRestricted, stringBoolean.resolveInternal(null, null));
    assertEquals(numberBoolean, stringNumberBoolean.resolveInternal(null, null));
    assertEquals(stringNumberBooleanRestricted, stringNumberBoolean.resolveInternal(null, null));
  }

  @Test
  public void testCreateNestedUnion() {
    UnionType stringBoolean = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            registry.getBooleanType())));

    UnionType stringString = new UnionType(registry,
        new HashSet<JSType>(Arrays.asList(
            registry.getStringType(),
            stringBoolean)));

    assertFalse(stringString.isSubtype(stringBoolean));
  }
}