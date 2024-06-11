import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class UnionTypeTest {
  @Test
  public void testUnionTypeCreation() {
    Set<JSType> alternates = Arrays.asJava(booleanType(), stringType());
    UnionType unionType = new UnionType(JSTypeRegistry.INSTANCE, alternates);
    assertEquals(alternates, unionType.getAlternates());
  }

  @Test
  public void testMatchesNumberContext() {
    UnionType unionType = testUnionTypeCreation();
    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    UnionType unionType = testUnionTypeCreation();
    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    UnionType unionType = testUnionTypeCreation();
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testFindPropertyType() {
    UnionType unionType = testUnionTypeCreation();
    JSType type = JSTypeFactory.getBuiltins().getMemberType("window", "document");
    JSType propertyType = unionType.findPropertyType("document");
    assertEquals(type, propertyType);
  }

  @Test
  public void testCanAssignTo() {
    UnionType unionType = testUnionTypeCreation();
    JSType assignableType = stringType();
    assertTrue(unionType.canAssignTo(assignableType));
  }

  @Test
  public void testCanBeCalled() {
    UnionType unionType = testUnionTypeCreation();
    assertTrue(unionType.canBeCalled());
  }

  @Test
  public void testRestrictByNotNullOrUndefined() {
    UnionType unionType = testUnionTypeCreation();
    UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
    assertFalse(unionType.isSubtype(restrictedUnionType));
    assertTrue(restrictedUnionType.isSubtype(unionType));
  }

  @Test
  public void testTestForEquality() {
    UnionType unionType1 = testUnionTypeCreation();
    UnionType unionType2 = testUnionTypeCreation();
    TernaryValue ternaryValue = unionType1.testForEquality(unionType2);
    assertEquals(UNKNOWN, ternaryValue);
  }

  @Test
  public void testIsNullable() {
    UnionType unionType = testUnionTypeCreation();
    assertFalse(unionType.isNullable());
  }

  @Test
  public void testIsUnknownType() {
    UnionType unionType = testUnionTypeCreation();
    assertFalse(unionType.isUnknownType());
  }

  @Test
  public void testGetLeastSupertype() {
    UnionType unionType1 = testUnionTypeCreation();
    UnionType unionType2 = testUnionTypeCreation();
    UnionType leastSupertype = unionType1.getLeastSupertype(unionType2);
    assertNull(leastSupertype);
  }

  @Test
  public void testMeet() {
    UnionType unionType1 = testUnionTypeCreation();
    UnionType unionType2 = testUnionTypeCreation();
    UnionType meetType = unionType1.meet(unionType2);
    assertNull(meetType);
  }

  @Test
  public void testEquals() {
    UnionType unionType1 = testUnionTypeCreation();
    UnionType unionType2 = testUnionTypeCreation();
    assertFalse(unionType1.equals(unionType2));
  }

  @Test
  public void testHashCode() {
    UnionType unionType1 = testUnionTypeCreation();
    UnionType unionType2 = testUnionTypeCreation();
    int hashCode1 = unionType1.hashCode();
    int hashCode2 = unionType2.hashCode();
    assertNotSame(hashCode1, hashCode2);
  }

  @Test
  public void testIsUnionType() {
    UnionType unionType = testUnionTypeCreation();
    assertTrue(unionType.isUnionType());
  }

  @Test
  public void testIsObject() {
    UnionType unionType = testUnionTypeCreation();
    assertTrue(unionType.isObject());
  }

  @Test
  public void testContains() {
    UnionType unionType = testUnionTypeCreation();
    JSType alternate = booleanType();
    assertTrue(unionType.contains(alternate));
  }

  @Test
  public void testGetRestrictedUnion() {
    UnionType unionType = testUnionTypeCreation();
    JSType restrictType = booleanType();
    UnionType restrictedUnionType = unionType.getRestrictedUnion(restrictType);
    assertEquals(stringType(), restrictedUnionType);
  }

  @Test
  public void testToString() {
    UnionType unionType = testUnionTypeCreation();
    assertEquals("(boolean|string)", unionType.toString());
  }

  @Test
  public void testIsSubtype() {
    UnionType unionType = testUnionTypeCreation();
    JSType subtype = booleanType();
    assertFalse(unionType.isSubtype(subtype));
  }

  @Test
  public void testGetRestrictedTypeGivenToBooleanOutcome() {
    UnionType unionType = testUnionTypeCreation();
    boolean booleanOutcome = true;
    assertEquals(booleanType(), unionType.getRestrictedTypeGivenToBooleanOutcome(booleanOutcome));
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    UnionType unionType = testUnionTypeCreation();
    BooleanLiteralSet possibleToBooleanOutcomes = unionType.getPossibleToBooleanOutcomes();
    assertTrue(possibleToBooleanOutcomes.isBoth());
  }

  @Test(expected = AssertionError.class)
  public void testGetTypesUnderEqualityNull() {
    UnionType unionType = testUnionTypeCreation();
    JSType that = null;
    unionType.getTypesUnderEquality(that);
  }

  @Test
  public void testGetTypesUnderInequality() {
    UnionType unionType1 = testUnionTypeCreation();
    UnionType unionType2 = testUnionTypeCreation();
    TypePair typePair = unionType1.getTypesUnderInequality(unionType2);
    assertNotNull(typePair.typeA);
    assertNotNull(typePair.typeB);
    assertEquals(UNKNOWN_TYPE, typePair.typeA);
    assertEquals(UNKNOWN_TYPE, typePair.typeB);
  }

  @Test
  public void testGetTypesUnderShallowInequality() {
    UnionType unionType1 = testUnionTypeCreation();
    UnionType unionType2 = testUnionTypeCreation();
    TypePair typePair = unionType1.getTypesUnderShallowInequality(unionType2);
    assertNotNull(typePair.typeA);
    assertNotNull(typePair.typeB);
    assertEquals(UNKNOWN_TYPE, typePair.typeA);
    assertEquals(UNKNOWN_TYPE, typePair.typeB);
  }

  private UnionType testUnionTypeCreation() {
    Set<JSType> alternates = Arrays.asJava(booleanType(), stringType());
    UnionType unionType = new UnionType(JSTypeRegistry.INSTANCE, alternates);
    return unionType;
  }
}