import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.booleanType;
import static com.google.javascript.rhino.jstype.JSType.numberType;
import static com.google.javascript.rhino.jstype.JSType.stringType;
import static com.google.javascript.rhino.jstype.JSType.unionType;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.JSTypeVisitor;
import com.google.javascript.rhino.StaticScope;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    typeRegistry = new JSTypeRegistry(errorReporter);
  }

  @Test
  public void testUnionTypeCreation() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertEquals("(number, string)", unionType.toString());
  }

  @Test
  public void testUnionTypeMatchesNumberContext() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testUnionTypeMatchesStringContext() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testUnionTypeMatchesObjectContext() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    JSType propertyType = unionType.findPropertyType("length");
    assertNotNull(propertyType);
    assertEquals(numberType(), propertyType);
  }

  @Test
  public void testCanAssignTo() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    JSType that = booleanType();

    assertTrue(unionType.canAssignTo(that));
  }

  @Test
  public void testCanBeCalled() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();

    assertTrue(restrictedType.isSubtype(typeA.restrictByNotNullOrUndefined()));
    assertTrue(restrictedType.isSubtype(typeB.restrictByNotNullOrUndefined()));
  }

  @Test
  public void testTestForEquality() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionTypeA = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));
    UnionType unionTypeB = new UnionType(typeRegistry, ImmutableSet.of(typeB, typeA));

    TernaryValue result = unionTypeA.testForEquality(unionTypeB);

    assertEquals(TernaryValue.TRUE, result);
  }

  @Test
  public void testIsNullable() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertFalse(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    JSType leastSupertype = unionType.getLeastSupertype(objectType());

    assertEquals(objectType(), leastSupertype);
  }

  @Test
  public void testMeet() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionTypeA = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));
    UnionType unionTypeB = new UnionType(typeRegistry, ImmutableSet.of(typeB, typeA));

    UnionType result = unionTypeA.meet(unionTypeB);

    assertEquals("(number, string)", result.toString());
  }

  @Test
  public void testEquality() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionTypeA = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));
    UnionType unionTypeB = new UnionType(typeRegistry, ImmutableSet.of(typeB, typeA));

    assertTrue(unionTypeA.equals(unionTypeB));
  }

  @Test
  public void testHashCode() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionTypeA = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));
    UnionType unionTypeB = new UnionType(typeRegistry, ImmutableSet.of(typeB, typeA));

    assertEquals(unionTypeA.hashCode(), unionTypeB.hashCode());
  }

  @Test
  public void testIsUnionType() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertTrue(unionType.isObject());
  }

  @Test
  public void testContains() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertTrue(unionType.contains(numberType()));
    assertTrue(unionType.contains(stringType()));
    assertFalse(unionType.contains(booleanType()));
  }

  @Test
  public void testGetRestrictedUnion() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    UnionType restrictedType = unionType.getRestrictedUnion(numberType());

    assertEquals("string", restrictedType.toString());
  }

  @Test
  public void testToString() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertEquals("(number, string)", unionType.toString());
  }

  @Test
  public void testIsSubtype() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertTrue(numberType().isSubtype(unionType));
    assertFalse(booleanType().isSubtype(unionType));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    assertEquals(typeA, unionType.getRestrictedTypeGivenToBooleanOutcome(true));
    assertEquals(typeB, unionType.getRestrictedTypeGivenToBooleanOutcome(false));
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    BooleanLiteralSet literals = unionType.getPossibleToBooleanOutcomes();

    assertEquals(BooleanLiteralSet.BOTH, literals);
  }

  @Test
  public void testGetTypesUnderEquality() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    JSType typeC = booleanType();

    JSType typePairA = unionType.getTypesUnderEquality(typeC);
    JSType typePairB = typeC.getTypesUnderEquality(unionType);

    assertEquals(typeC, typePairA.typeA);
    assertEquals(unionType, typePairA.typeB);

    assertEquals(unionType, typePairB.typeA);
    assertEquals(typeC, typePairB.typeB);
  }

  @Test
  public void testGetTypesUnderInequality() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    JSType typeC = booleanType();

    JSType typePairA = unionType.getTypesUnderInequality(typeC);
    JSType typePairB = typeC.getTypesUnderInequality(unionType);

    assertEquals(typeC, typePairA.typeA);
    assertEquals(typeB, typePairA.typeB);

    assertEquals(typeA, typePairB.typeA);
    assertEquals(typeB, typePairB.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    JSType typeC = booleanType();

    JSType typePairA = unionType.getTypesUnderShallowInequality(typeC);
    JSType typePairB = typeC.getTypesUnderShallowInequality(unionType);

    assertEquals(typeC, typePairA.typeA);
    assertEquals(typeC, typePairA.typeB);

    assertEquals(typeC, typePairB.typeA);
    assertEquals(typeC, typePairB.typeB);
  }

  @Test
  public void testVisit() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    JSTypeVisitor visitor = new JSTypeVisitor();

    visitor.visit(unionType);
  }

  @Test
  public void testResolveInternal() {
    JSType typeA = numberType();
    JSType typeB = stringType();

    UnionType unionType = new UnionType(typeRegistry, ImmutableSet.of(typeA, typeB));

    StaticScope scope = new StaticScope();

    JSType resolvedType = unionType.resolveInternal(errorReporter, scope);

    assertEquals(unionType, resolvedType);
  }
}