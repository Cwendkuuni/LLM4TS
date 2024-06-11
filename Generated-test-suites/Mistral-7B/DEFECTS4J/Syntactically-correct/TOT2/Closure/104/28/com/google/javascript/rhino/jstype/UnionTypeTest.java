import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.booleanType;
import static com.google.javascript.rhino.jstype.JSType.numberType;
import static com.google.javascript.rhino.jstype.JSType.stringType;
import static com.google.javascript.rhino.jstype.JSType.unionType;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = ImmutableSet.of(numberType(), stringType());
    UnionType unionType = new UnionType(null, alternates);
    assertEquals("(number, string)", unionType.toString());
  }

  @Test
  public void testGetAlternates() {
    UnionType unionType = UnionType.unionType(booleanType(), stringType());
    assertEquals(ImmutableSet.of(booleanType(), stringType()), unionType.getAlternates());
  }

  @Test
  public void testMatchesNumberContext() {
    UnionType unionType = UnionType.unionType(booleanType(), numberType());
    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    UnionType unionType = UnionType.unionType(booleanType(), stringType());
    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    UnionType unionType = UnionType.unionType(booleanType(), stringType());
    assertTrue(unionType.matchesObjectContext());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateUnionTypeWithNullAlternates() {
    new UnionType(null, null);
  }

  @Test
  public void testFindPropertyType() {
    UnionType unionType = UnionType.unionType(booleanType(), stringType());
    JSType propertyType = unionType.findPropertyType("length");
    assertNull(propertyType);

    propertyType = unionType.findPropertyType("toString");
    assertEquals(stringType, propertyType);
  }

  @Test
  public void testCanAssignTo() {
    UnionType unionType = UnionType.unionType(booleanType(), stringType());
    assertTrue(unionType.canAssignTo(booleanType()));
    assertTrue(unionType.canAssignTo(stringType()));
    assertFalse(unionType.canAssignTo(numberType()));
  }

  @Test
  public void testCanBeCalled() {
    UnionType unionType = UnionType.unionType(booleanType(), String.class);
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType unionType = UnionType.unionType(nullType(), booleanType(), numberType());
    UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
    assertEquals("null", restrictedType.toString());
  }

  @Test
  public void testTestForEquality() {
    UnionType unionType1 = UnionType.unionType(booleanType(), stringType());
    UnionType unionType2 = UnionType.unionType(stringType(), booleanType());
    TernaryValue result = unionType1.testForEquality(unionType2);
    assertEquals(UNKNOWN, result);

    result = unionType1.testForEquality(unionType1);
    assertEquals(TRUE, result);
  }

  @Test
  public void testIsNullable() {
    UnionType unionType = UnionType.unionType(numberType(), nullType());
    assertTrue(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType unionType = UnionType.unionType(numberType(), nullType());
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType unionType1 = UnionType.unionType(booleanType(), stringType());
    UnionType unionType2 = UnionType.unionType(stringType(), numberType());
    UnionType leastSupertype = unionType1.getLeastSupertype(unionType2);
    assertEquals(unionType2, leastSupertype);

    leastSupertype = unionType1.getLeastSupertype(booleanType());
    assertEquals(unionType1, leastSupertype);
  }

  @Test
  public void testMeet() {
    UnionType unionType1 = UnionType.unionType(booleanType(), stringType());
    UnionType unionType2 = UnionType.unionType(stringType(), booleanType());
    UnionType meetType = unionType1.meet(unionType2);
    assertEquals(unionType1, meetType);

    meetType = unionType1.meet(numberType());
    assertNull(meetType);
  }
}