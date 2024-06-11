import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

  @Test
  public void testUnionTypeBasic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringAlternates = ImmutableSet.of(registry.findTypeByName("String"));
    Set<JSType> booleanAlternates = ImmutableSet.of(registry.findTypeByName("Boolean"));
    UnionType stringBooleanUnion = new UnionType(registry, stringAlternates);
    UnionType booleanStringUnion = new UnionType(registry, booleanAlternates);

    // Test equality
    assertEquals(stringBooleanUnion, booleanStringUnion);

    // Test finding properties
    JSType stringPropertyType = stringBooleanUnion.findPropertyType("length");
    assertNotNull(stringPropertyType);
    assertEquals(registry.findTypeByName("Number"), stringPropertyType);

    // Test canAssignTo
    assertTrue(stringBooleanUnion.canAssignTo(registry.findTypeByName("String")));
    assertFalse(stringBooleanUnion.canAssignTo(registry.findTypeByName("Number")));

    // Test matchesContext
    assertTrue(stringBooleanUnion.matchesStringContext());
    assertFalse(stringBooleanUnion.matchesNumberContext());

    // Test subtyping
    assertTrue(stringBooleanUnion.isSubtype(booleanStringUnion));
    assertFalse(booleanStringUnion.isSubtype(stringBooleanUnion));

    // Test meet
    UnionType stringBooleanMeet = stringBooleanUnion.meet(booleanStringUnion);
    assertNotNull(stringBooleanMeet);
    assertEquals(registry.findTypeByName("Object"), stringBooleanMeet);

    // Test getRestrictedUnion
    UnionType stringTypeRestricted = stringBooleanUnion.getRestrictedUnion(registry.findTypeByName("String"));
    assertEquals(registry.findTypeByName("Object"), stringTypeRestricted);

    UnionType booleanTypeRestricted = stringBooleanUnion.getRestrictedUnion(registry.findTypeByName("Boolean"));
    assertEquals(registry.findTypeByName("Object"), booleanTypeRestricted);

    UnionType nullTypeRestricted = stringBooleanUnion.getRestrictedUnion(registry.findTypeByName("Null"));
    assertEquals(registry.findTypeByName("Null"), nullTypeRestricted);
  }

  @Test
  public void testUnionTypeNested() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Set<JSType> stringAlternates = ImmutableSet.of(registry.findTypeByName("String"));
    Set<JSType> booleanAlternates = ImmutableSet.of(registry.findTypeByName("Boolean"));
    Set<JSType> numberAlternates = ImmutableSet.of(registry.findTypeByName("Number"));

    UnionType stringBooleanUnion = new UnionType(registry, stringAlternates);
    UnionType booleanNumberUnion = new UnionType(registry, booleanAlternates);
    UnionType numberStringUnion = new UnionType(registry, numberAlternates);
    UnionType booleanNumberStringUnion = new UnionType(registry, booleanAlternates);
    UnionType numberBooleanStringUnion = new UnionType(registry, numberAlternates);

    // Test equality
    assertNotEquals(stringBooleanUnion, booleanNumberStringUnion);

    // Test canAssignTo
    assertFalse(stringBooleanUnion.canAssignTo(numberBooleanStringUnion));

    // Test subtyping
    assertFalse(stringBooleanUnion.isSubtype(numberBooleanStringUnion));
    assertFalse(numberBooleanStringUnion.isSubtype(stringBooleanUnion));
  }

  @Test
  public void testUnionTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringAlternates = ImmutableSet.of(registry.findTypeByName("String"));
    Set<JSType> booleanAlternates = ImmutableSet.of(registry.findTypeByName("Boolean"));
    Set<JSType> numberAlternates = ImmutableSet.of(registry.findTypeByName("Number"));

    UnionType stringBooleanUnion = new UnionType(registry, stringAlternates);
    UnionType booleanStringUnion = new UnionType(registry, booleanAlternates);
    UnionType numberStringUnion = new UnionType(registry, numberAlternates);
    UnionType booleanNumberStringUnion = new UnionType(registry, booleanAlternates);
    UnionType numberBooleanStringUnion = new UnionType(registry, numberAlternates);

    // Test isSubtype
    assertTrue(stringBooleanUnion.isSubtype(booleanStringUnion));
    assertTrue(booleanStringUnion.isSubtype(stringBooleanUnion));

    // Test isSubtype with Number
    assertFalse(stringBooleanUnion.isSubtype(numberStringUnion));
    assertFalse(numberStringUnion.isSubtype(stringBooleanUnion));

    // Test isSubtype with Number and Boolean
    assertFalse(stringBooleanUnion.isSubtype(numberBooleanStringUnion));
    assertFalse(numberBooleanStringUnion.isSubtype(stringBooleanUnion));

    // Test isSubtype with Number, Boolean, and String
    assertFalse(stringBooleanUnion.isSubtype(booleanNumberStringUnion));
    assertFalse(booleanNumberStringUnion.isSubtype(stringBooleanUnion));
    assertFalse(numberBooleanStringUnion.isSubtype(stringBooleanUnion));
  }

  @Test
  public void testUnionTypeGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringAlternates = ImmutableSet.of(registry.findTypeByName("String"));
    Set<JSType> booleanAlternates = ImmutableSet.of(registry.findTypeByName("Boolean"));
    Set<JSType> numberAlternates = ImmutableSet.of(registry.findTypeByName("Number"));

    UnionType stringBooleanUnion = new UnionType(registry, stringAlternates);
    UnionType booleanStringUnion = new UnionType(registry, booleanAlternates);
    UnionType numberStringUnion = new UnionType(registry, numberAlternates);

    UnionType stringBooleanLeastSupertype = stringBooleanUnion.getLeastSupertype(booleanStringUnion);
    assertEquals(registry.findTypeByName("Object"), stringBooleanLeastSupertype);

    UnionType booleanStringLeastSupertype = booleanStringUnion.getLeastSupertype(stringBooleanUnion);
    assertEquals(registry.findTypeByName("Object"), booleanStringLeastSupertype);

    UnionType stringBooleanLeastSupertypeNumber = stringBooleanUnion.getLeastSupertype(numberStringUnion);
    assertEquals(registry.findTypeByName("Object"), stringBooleanLeastSupertypeNumber);

    UnionType numberStringLeastSupertypeString = numberStringUnion.getLeastSupertype(stringBooleanUnion);
    assertEquals(registry.findTypeByName("Object"), numberStringLeastSupertypeString);

    UnionType stringBooleanLeastSupertypeNumberBoolean = stringBooleanUnion.getLeastSupertype(booleanNumberStringUnion);
    assertEquals(registry.findTypeByName("Object"), stringBooleanLeastSupertypeNumberBoolean);

    UnionType booleanNumberStringLeastSupertypeString = booleanNumberStringUnion.getLeastSupertype(stringBooleanUnion);
    assertEquals(registry.findTypeByName("Object"), booleanNumberStringLeastSupertypeString);

    UnionType numberBooleanStringLeastSupertypeString = numberBooleanStringUnion.getLeastSupertype(stringBooleanUnion);
    assertEquals(registry.findTypeByName("Object"), numberBooleanStringLeastSupertypeString);
  }

  @Test
  public void testUnionTypeGetRestrictedTypeGivenToBooleanOutcome() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringAlternates = ImmutableSet.of(registry.findTypeByName("String"));
    Set<JSType> booleanAlternates = ImmutableSet.of(registry.findTypeByName("Boolean"));

    UnionType stringBooleanUnion = new UnionType(registry, stringAlternates);
    UnionType booleanStringUnion = new UnionType(registry, booleanAlternates);

    BooleanLiteralSet possibleToBooleanOutcomesString = stringBooleanUnion.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.BOTH, possibleToBooleanOutcomesString);

    BooleanLiteralSet possibleToBooleanOutcomesBoolean = booleanStringUnion.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.TRUE, possibleToBooleanOutcomesBoolean);

    UnionType stringBooleanRestrictedToFalse = stringBooleanUnion.getRestrictedTypeGivenToBooleanOutcome(false);
    assertEquals(registry.findTypeByName("String"), stringBooleanRestrictedToFalse);

    UnionType booleanStringRestrictedToFalse = booleanStringUnion.getRestrictedTypeGivenToBooleanOutcome(false);
    assertEquals(registry.findTypeByName("Null"), booleanStringRestrictedToFalse);

    UnionType stringBooleanRestrictedToTrue = stringBooleanUnion.getRestrictedTypeGivenToBooleanOutcome(true);
    assertEquals(registry.findTypeByName("Boolean"), stringBooleanRestrictedToTrue);

    UnionType booleanStringRestrictedToTrue = booleanStringUnion.getRestrictedTypeGivenToBooleanOutcome(true);
    assertEquals(registry.findTypeByName("Boolean"), booleanStringRestrictedToTrue);
  }

  @Test
  public void testUnionTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Set<JSType> stringAlternates = ImmutableSet.of(registry.findTypeByName("String"));
    Set<JSType> booleanAlternates = ImmutableSet.of(registry.findTypeByName("Boolean"));

    UnionType stringBooleanUnion = new UnionType(registry, stringAlternates);
    UnionType booleanStringUnion = new UnionType(registry, booleanAlternates);

    assertEquals("(String|Boolean)", stringBooleanUnion.toString());
    assertEquals("(Boolean|String)", booleanStringUnion.toString());
  }
}