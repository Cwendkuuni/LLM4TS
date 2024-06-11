import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.booleanType;
import static com.google.javascript.rhino.jstype.JSType.numberType;
import static com.google.javascript.rhino.jstype.JSType.stringType;
import static com.google.javascript.rhino.jstype.JSType.unknownType;
import static com.google.javascript.rhino.jstype.JSType.voidType;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.errors.ErrorReporter;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class UnionTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(stringType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals("(String|Boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeGetAlternates() {
    Set<JSType> alternates = ImmutableSet.of(stringType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);
    assertEquals(ImmutableSet.of(stringType(), booleanType()), unionType.getAlternates());
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> alternates = ImmutableSet.of(stringType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.canAssignTo(numberType()));
    assertTrue(unionType.canAssignTo(stringType()));
    assertTrue(unionType.canAssignTo(booleanType()));
    assertTrue(unionType.canAssignTo(unionType));
    assertTrue(unionType.canAssignTo(voidType()));
    assertTrue(unionType.canAssignTo(unknownType()));

    assertFalse(unionType.canAssignTo(numberType().restrictByNotNullOrUndefined()));
    assertFalse(unionType.canAssignTo(booleanType().restrictByNotNullOrUndefined()));
    assertFalse(unionType.canAssignTo(nullType()));
  }

  @Test
  public void testUnionTypeMatchesContexts() {
    Set<JSType> alternates = ImmutableSet.of(numberType(), stringType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    Set<JSType> alternates = ImmutableSet.of(JSTypeFactory.createStringLiteralType("String"),
        JSTypeFactory.createObjectType("Object"));
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals(unknownType(), unionType.findPropertyType("length"));
    assertEquals(stringType(), unionType.findPropertyType("toString"));
    assertEquals(JSTypeNative.NO_TYPE, unionType.findPropertyType("toString").getNativeType());
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> alternatesA = ImmutableSet.of(stringType(), booleanType());
    Set<JSType> alternatesB = ImmutableSet.of(numberType(), booleanType());
    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    UnionType result = unionTypeA.meet(unionTypeB);
    assertEquals(ImmutableSet.of(numberType(), booleanType(), stringType()), result.getAlternates().asList());
  }

  @Test
  public void testUnionTypeEquals() {
    Set<JSType> alternatesA = ImmutableSet.of(numberType(), booleanType());
    Set<JSType> alternatesB = ImmutableSet.of(booleanType(), numberType());
    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    assertFalse(unionTypeA.equals(unionTypeB));
  }

  @Test
  public void testUnionTypeHashCode() {
    Set<JSType> alternates = ImmutableSet.of(stringType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);
    UnionType unionType2 = new UnionType(registry, alternates);

    assertEquals(hashCode(unionType), hashCode(unionType2));
  }

  @Test
  public void testUnionTypeIsUnionType() {
    Set<JSType> alternates = ImmutableSet.of(stringType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testUnionTypeIsObject() {
    Set<JSType> alternates = ImmutableSet.of(numberType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertFalse(unionType.isObject());
  }

  @Test
  public void testUnionTypeIsSubtype() {
    Set<JSType> alternatesA = ImmutableSet.of(numberType(), booleanType());
    Set<JSType> alternatesB = ImmutableSet.of(booleanType(), numberType());
    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    assertTrue(unionTypeA.isSubtype(unionTypeB));
  }

  @Test
  public void testUnionTypeGetRestrictedUnion() {
    Set<JSType> alternates = ImmutableSet.of(numberType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);

    UnionType restricted = unionType.getRestrictedUnion(numberType());
    assertEquals(booleanType(), restricted);
  }

  @Test
  public void testUnionTypeToString() {
    Set<JSType> alternates = ImmutableSet.of(numberType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);

    assertEquals("(Number|Boolean)", unionType.toString());
  }

  @Test
  public void testUnionTypeGetTypesUnderEquality() {
    Set<JSType> alternates = ImmutableSet.of(numberType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);

    TypePair result = unionType.getTypesUnderEquality(booleanType());
    UnionType typeA = (UnionType) result.typeA;
    UnionType typeB = (UnionType) result.typeB;

    assertEquals(ImmutableSet.of(numberType(), booleanType()), typeA.getAlternates());
    assertEquals(ImmutableSet.of(booleanType(), numberType()), typeB.getAlternates());
  }

  @Test
  public void testUnionTypeGetTypesUnderInequality() {
    Set<JSType> alternatesA = ImmutableSet.of(numberType(), booleanType());
    Set<JSType> alternatesB = ImmutableSet.of(stringType(), booleanType());
    UnionType unionTypeA = new UnionType(registry, alternatesA);
    UnionType unionTypeB = new UnionType(registry, alternatesB);

    TypePair result = unionTypeA.getTypesUnderInequality(unionTypeB);
    UnionType typeA = (UnionType) result.typeA;
    UnionType typeB = (UnionType) result.typeB;

    assertEquals(ImmutableSet.of(numberType()), typeA.getAlternates());
    assertEquals(ImmutableSet.of(stringType()), typeB.getAlternates());
  }

  @Test
  public void testUnionTypeGetTypesUnderShallowInequality() {
    Set<JSType> alternates = ImmutableSet.of(numberType(), booleanType());
    UnionType unionType = new UnionType(registry, alternates);

    TypePair result = unionType.getTypesUnderShallowInequality(unionType);
    UnionType typeA = (UnionType) result.typeA;
    UnionType typeB = (UnionType) result.typeB;

    assertEquals(ImmutableSet.of(numberType(), booleanType()), typeA.getAlternates());
    assertEquals(ImmutableSet.of(numberType(), booleanType()), typeB.getAlternates());
  }

  private int hashCode(UnionType unionType) {
    return unionType.hashCode();
  }
}