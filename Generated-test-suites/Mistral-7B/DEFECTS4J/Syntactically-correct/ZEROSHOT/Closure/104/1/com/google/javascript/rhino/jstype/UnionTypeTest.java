import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Set;

public class UnionTypeTest {

  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
  }

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> setA = ImmutableSet.of(booleanType(), stringType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);
    UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), setA);

    assertEquals(unionTypeA, unionTypeB);
    assertEquals(unionTypeA.hashCode(), unionTypeB.hashCode());

    Set<JSType> setB = ImmutableSet.of(numberType(), booleanType());
    UnionType unionTypeC = new UnionType(JSTypeRegistry.getInstance(), setB);

    assertNotEquals(unionTypeA, unionTypeC);
    assertNotEquals(unionTypeA.hashCode(), unionTypeC.hashCode());
  }

  @Test
  public void testUnionTypeMatchesContexts() {
    Set<JSType> setA = ImmutableSet.of(booleanType(), stringType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    assertTrue(unionTypeA.matchesNumberContext());
    assertTrue(unionTypeA.matchesStringContext());
    assertTrue(unionTypeA.matchesObjectContext());
  }

  @Test
  public void testUnionTypeFindPropertyType() {
    Set<JSType> setA = ImmutableSet.of(booleanType(), stringType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    assertEquals(booleanType(), unionTypeA.findPropertyType("toString"));
    assertEquals(stringType(), unionTypeA.findPropertyType("length"));
    assertNull(unionTypeA.findPropertyType("length")); // Number does not have a 'length' property
  }

  @Test
  public void testUnionTypeCanAssignTo() {
    Set<JSType> setA = ImmutableSet.of(numberType(), stringType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    assertTrue(unionTypeA.canAssignTo(numberType()));
    assertTrue(unionTypeA.canAssignTo(stringType()));
    assertFalse(unionTypeA.canAssignTo(booleanType()));
  }

  @Test
  public void testUnionTypeCanBeCalled() {
    Set<JSType> setA = ImmutableSet.of(functionType(ImmutableSet.of(numberType()), voidType()));
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    assertTrue(unionTypeA.canBeCalled());
  }

  @Test
  public void testUnionTypeRestrictByNotNullOrUndefined() {
    Set<JSType> setA = ImmutableSet.of(nullType(), booleanType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    UnionType restrictedUnionTypeA = unionTypeA.restrictByNotNullOrUndefined();
    assertNull(restrictedUnionTypeA);

    Set<JSType> setB = ImmutableSet.of(numberType(), booleanType());
    UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), setB);

    UnionType restrictedUnionTypeB = unionTypeB.restrictByNotNullOrUndefined();
    assertEquals(booleanType(), restrictedUnionTypeB);
  }

  @Test
  public void testUnionTypeTestForEquality() {
    Set<JSType> setA = ImmutableSet.of(booleanType(), numberType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    Set<JSType> setB = ImmutableSet.of(stringType(), numberType());
    UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), setB);

    TernaryValue result = unionTypeA.testForEquality(unionTypeB);
    assertEquals(UNKNOWN, result);

    result = unionTypeB.testForEquality(unionTypeA);
    assertEquals(UNKNOWN, result);

    result = unionTypeA.testForEquality(unionTypeA);
    assertEquals(TRUE, result);
  }

  @Test
  public void testUnionTypeIsNullable() {
    Set<JSType> setA = ImmutableSet.of(nullType(), booleanType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    assertTrue(unionTypeA.isNullable());

    Set<JSType> setB = ImmutableSet.of(numberType(), booleanType());
    UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), setB);

    assertFalse(unionTypeB.isNullable());
  }

  @Test
  public void testUnionTypeIsUnknownType() {
    Set<JSType> setA = ImmutableSet.of(booleanType(), nullType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    assertFalse(unionTypeA.isUnknownType());

    Set<JSType> setB = ImmutableSet.of(nullType(), booleanType());
    UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), setB);

    assertTrue(unionTypeB.isUnknownType());
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    Set<JSType> setA = ImmutableSet.of(booleanType(), numberType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    UnionType leastSupertype = unionTypeA.getLeastSupertype(booleanType());
    assertNull(leastSupertype);

    leastSupertype = unionTypeA.getLeastSupertype(numberType());
    assertEquals(numberType(), leastSupertype);

    leastSupertype = unionTypeA.getLeastSupertype(unionTypeA);
    assertEquals(unionTypeA, leastSupertype);
  }

  @Test
  public void testUnionTypeMeet() {
    Set<JSType> setA = ImmutableSet.of(booleanType(), numberType());
    UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), setA);

    Set<JSType> setB = ImmutableSet.of(stringType(), numberType());
    UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), setB);

    UnionType meet = unionTypeA.meet(unionTypeB);
    assertNull(meet);

    meet = unionTypeB.meet(unionTypeA);
    assertEquals(numberType(), meet);
  }
}