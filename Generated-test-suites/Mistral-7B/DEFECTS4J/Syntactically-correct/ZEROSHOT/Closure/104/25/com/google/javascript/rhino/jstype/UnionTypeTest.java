import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeEqualityTypeMatcher.hasType;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeEqualityTypeMatcher;
import com.google.javascript.rhino.testing.ValueFactory;

import junit.framework.TestCase;

import java.util.Set;

public class UnionTypeTest extends TestCase {
  private static final ValueFactory VALUES = new ValueFactory();

  @SuppressWarnings("unchecked")
  private static <T extends JSType> T createUnionType(Set<JSType> types) {
    JSTypeRegistry registry = new JSTypeRegistry();
    return (T) new UnionType(registry, ImmutableSet.copyOf(types));
  }

  public void testUnionTypeCreation() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);
    assertNotNull(unionType);
    assertEquals("(number, boolean)", unionType.toString());
  }

  public void testGetAlternates() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);
    assertEquals(types, unionType.getAlternates());
  }

  public void testMatchesNumberContext() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);
    assertTrue(unionType.matchesNumberContext());
  }

  public void testMatchesStringContext() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);
    assertFalse(unionType.matchesStringContext());
  }

  public void testMatchesObjectContext() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);
    assertTrue(unionType.matchesObjectContext());
  }

  public void testFindPropertyType() {
    Set<JSType> types = ImmutableSet.of(
        VALUES.createObjectType("ObjectWithNumberProperty", VALUES.numberType()),
        VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    // The property is defined on the first type only
    String propertyName = "property";
    JSType expectedType = VALUES.numberType();
    JSType actualType = unionType.findPropertyType(propertyName);
    assertEquals(expectedType, actualType);
  }

  public void testCanAssignTo() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    // Assigning from a null type is always possible
    JSType nullType = VALUES.nullType();
    assertTrue(unionType.canAssignTo(nullType));

    // Assigning to a union type from any of its alternates is possible
    JSType numberType = VALUES.numberType();
    assertTrue(numberType.canAssignTo(unionType));

    // Assigning to a union type from a different type is not possible
    JSType stringType = VALUES.stringType();
    assertFalse(stringType.canAssignTo(unionType));
  }

  public void testCanBeCalled() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    // Functions of a union type can be called as long as they can be called
    // on any of the alternates
    JSType callableFunctionType = VALUES.createFunctionType(ImmutableSet.of(), VALUES.numberType());
    assertTrue(callableFunctionType.canBeCalled());
    assertTrue(unionType.canBeCalled());
  }

  public void testRestrictByNotNullOrUndefined() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertNotNull(restrictedType);
    assertEquals(types.remove(VALUES.nullType()), restrictedType.getAlternates().iterator().next());
  }

  public void testTestForEquality() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    UnionType otherUnionType = createUnionType(types);
    assertEquals(unionType, otherUnionType);

    // The second union type should not be equal to the first one, since this
    // test uses reference equality
    UnionType differentUnionType = createUnionType(ImmutableSet.of(VALUES.booleanType(), VALUES.numberType()));
    assertNotEquals(unionType, differentUnionType);

    JSType numberType = VALUES.numberType();
    UnionType numberUnionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(numberType));
    assertEquals(UNKNOWN, unionType.testForEquality(numberUnionType));
  }

  public void testIsNullable() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);
    assertFalse(unionType.isNullable());
  }

  public void testIsUnknownType() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);
    assertFalse(unionType.isUnknownType());
  }

  public void testGetLeastSupertype() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    JSType leastSupertype = unionType.getLeastSupertype(VALUES.objectType());
    assertEquals(VALUES.objectType(), leastSupertype);

    JSType leastSupertype2 = unionType.getLeastSupertype(unionType);
    assertEquals(unionType, leastSupertype2);
  }

  public void testMeet() {
    Set<JSType> leftAlternates = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType left = new UnionType(JSTypeRegistry.getInstance(), leftAlternates);

    Set<JSType> rightAlternates = ImmutableSet.of(VALUES.booleanType(), VALUES.stringType());
    UnionType right = new UnionType(JSTypeRegistry.getInstance(), rightAlternates);

    UnionType result = left.meet(right);
    assertNotNull(result);
    assertEquals(ImmutableSet.of(VALUES.booleanType()), result.getAlternates());
  }

  public void testEquals() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType1 = createUnionType(types);
    UnionType unionType2 = createUnionType(types);

    assertEquals(unionType1, unionType2);
  }

  public void testHashCode() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    int hashCode1 = unionType.hashCode();
    Set<JSType> types2 = ImmutableSet.of(VALUES.booleanType(), VALUES.numberType());
    UnionType unionType2 = createUnionType(types2);

    assertEquals("Hashcodes should be equal if and only if the objects are equal", hashCode1, unionType2.hashCode());
  }

  public void testIsUnionType() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    assertTrue(unionType.isUnionType());
  }

  public void testIsObject() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    assertTrue(unionType.isObject());
  }

  public void testContains() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    JSType numberType = VALUES.numberType();
    assertTrue(unionType.contains(numberType));
  }

  public void testGetRestrictedUnion() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    JSType numberType = VALUES.numberType();
    UnionType restrictedType = unionType.getRestrictedUnion(numberType);
    assertEquals(VALUES.stringType(), restrictedType);
  }

  public void testToString() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    assertEquals("(number, boolean)", unionType.toString());
  }

  public void testIsSubtype() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    JSType numberType = VALUES.numberType();
    assertTrue(numberType.isSubtype(unionType));
  }

  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    JSType booleanType = VALUES.booleanType();
    UnionType result = unionType.getRestrictedTypeGivenToBooleanOutcome(true);
    assertEquals(booleanType, result);
  }

  public void testGetPossibleToBooleanOutcomes() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    BooleanLiteralSet expectedResult = BooleanLiteralSet.of(
        BooleanLiteral.TRUE,
        BooleanLiteral.FALSE);
    BooleanLiteralSet result = unionType.getPossibleToBooleanOutcomes();
    assertEquals(expectedResult, result);
  }

  public void testGetTypesUnderEquality() {
    Set<JSType> types1 = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType1 = createUnionType(types1);

    Set<JSType> types2 = ImmutableSet.of(VALUES.booleanType(), VALUES.numberType());
    UnionType unionType2 = createUnionType(types2);

    TypePair expectedResult = new TypePair(unionType1, unionType2);
    TypePair result = unionType1.getTypesUnderEquality(unionType2);
    assertEquals(expectedResult, result);
  }

  public void testGetTypesUnderInequality() {
    Set<JSType> types1 = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType1 = createUnionType(types1);

    Set<JSType> types2 = ImmutableSet.of(VALUES.booleanType(), VALUES.numberType());
    UnionType unionType2 = createUnionType(types2);

    TypePair expectedResult = new TypePair(VALUES.booleanType(), VALUES.numberType());
    TypePair result = unionType1.getTypesUnderInequality(unionType2);
    assertEquals(expectedResult, result);
  }

  public void testGetTypesUnderShallowInequality() {
    Set<JSType> types1 = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType1 = createUnionType(types1);

    Set<JSType> types2 = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType2 = createUnionType(types2);

    TypePair expectedResult = new TypePair(VALUES.numberType(), VALUES.booleanType());
    TypePair result = unionType1.getTypesUnderShallowInequality(unionType2);
    assertEquals(expectedResult, result);
  }

  public void testVisit() {
    Set<JSType> types = ImmutableSet.of(VALUES.numberType(), VALUES.booleanType());
    UnionType unionType = createUnionType(types);

    // Should not throw an exception
    unionType.visit(new Visitor<Object>() {
      @Override
      public Object caseUnionType(UnionType unionType) {
        return null;
      }
    });
  }
}