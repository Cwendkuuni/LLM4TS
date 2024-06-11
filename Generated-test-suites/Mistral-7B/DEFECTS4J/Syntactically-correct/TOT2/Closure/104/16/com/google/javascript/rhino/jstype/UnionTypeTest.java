import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.JSType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertEquals("(String, Boolean)", stringBooleanUnion.toString());
 assertTrue(stringBooleanUnion.isUnionType());
 }

 @Test
 public void testGetAlternates() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 Iterable<JSType> alternates = stringBooleanUnion.getAlternates();
 ImmutableSet<JSType> expectedAlternates = ImmutableSet.of(
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType());

 assertEquals(expectedAlternates, ImmutableSet.copyOf(alternates));
 }

 @Test
 public void testUnionTypeMatchesContexts() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertTrue(stringBooleanUnion.matchesNumberContext());
 assertTrue(stringBooleanUnion.matchesStringContext());
 assertTrue(stringBooleanUnion.matchesObjectContext());
 }

 @Test
 public void testFindPropertyType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 StringPropertyType stringProperty = JSTypeRegistry.getInstance().getStringPropertyType();
 BooleanPropertyType booleanProperty = JSTypeRegistry.getInstance().getBooleanPropertyType();

 JSType propertyType = stringBooleanUnion.findPropertyType("length");
 assertNull(propertyType);

 propertyType = stringBooleanUnion.findPropertyType("toString");
 assertEquals(stringProperty, propertyType);

 propertyType = stringBooleanUnion.findPropertyType("valueOf");
 assertEquals(booleanProperty, propertyType);
 }

 @Test
 public void testCanAssignTo() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 JSType stringType = JSTypeRegistry.getInstance().getStringType();
 JSType booleanType = JSTypeRegistry.getInstance().getBooleanType();

 assertTrue(stringBooleanUnion.canAssignTo(stringType));
 assertTrue(stringBooleanUnion.canAssignTo(booleanType));

 JSType numberType = JSTypeRegistry.getInstance().getNumberType();
 assertFalse(stringBooleanUnion.canAssignTo(numberType));
 }

 @Test
 public void testCanBeCalled() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertTrue(stringBooleanUnion.canBeCalled());
 }

 @Test
 public void testRestrictByNotNullOrUndefined() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 UnionType restrictedUnion = stringBooleanUnion.restrictByNotNullOrUndefined();

 assertEquals("(null, String, Boolean)", restrictedUnion.toString());
 }

 @Test
 public void testTestForEquality() {
 UnionType stringBooleanUnion1 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 JSType stringType = JSTypeRegistry.getInstance().getStringType();
 JSType booleanType = JSTypeRegistry.getInstance().getBooleanType();
 JSType nullType = JSTypeRegistry.getInstance().getNullType();
 JSType numberType = JSTypeRegistry.getInstance().getNumberType();

 assertEquals(stringType, stringBooleanUnion1.testForEquality(stringType));
 assertEquals(booleanType, stringBooleanUnion1.testForEquality(booleanType));
 assertEquals(nullType, stringBooleanUnion1.testForEquality(nullType));
 assertEquals(numberType, stringBooleanUnion1.testForEquality(numberType));
 assertEquals(stringBooleanUnion1, stringBooleanUnion1.testForEquality(stringBooleanUnion1));
 assertEquals(stringBooleanUnion2, stringBooleanUnion1.testForEquality(stringBooleanUnion2));
 assertNull(stringBooleanUnion1.testForEquality(nullType));
 assertNull(stringBooleanUnion1.testForEquality(numberType));
 }

 @Test
 public void testIsNullable() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertFalse(stringBooleanUnion.isNullable());
 }

 @Test
 public void testIsUnknownType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertFalse(stringBooleanUnion.isUnknownType());
 }

 @Test(expected = AssertionError.class)
 public void testGetLeastSupertypeWithIncompatibleTypes() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 JSType numberType = JSTypeRegistry.getInstance().getNumberType();
 JSType leastSupertype = stringBooleanUnion.getLeastSupertype(numberType);
 }

 @Test
 public void testMeet() {
 UnionType stringBooleanUnion1 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 UnionType meetResult = stringBooleanUnion1.meet(numberBooleanUnion);

 assertEquals("(Number, String, Boolean)", meetResult.toString());
 }

 @Test
 public void testEquals() {
 UnionType stringBooleanUnion1 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 UnionType stringStringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertTrue(stringBooleanUnion1.equals(stringBooleanUnion1));
 assertTrue(stringBooleanUnion1.equals(stringBooleanUnion2));
 assertFalse(stringBooleanUnion1.equals(stringStringBooleanUnion));
 }

 @Test
 public void testHashCode() {
 UnionType stringBooleanUnion1 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 UnionType stringStringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertEquals(stringBooleanUnion1.hashCode(), stringBooleanUnion1.hashCode());
 assertEquals(stringBooleanUnion1.hashCode(), stringBooleanUnion2.hashCode());
 assertNotSame(stringBooleanUnion1.hashCode(), stringStringBooleanUnion.hashCode());
 }

 @Test
 public void testIsObject() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertTrue(stringBooleanUnion.isObject());
 }

 @Test
 public void testToString() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(),
 ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(),
 JSTypeRegistry.getInstance().getBooleanType()));

 assertEquals("(String, Boolean)", stringBooleanUnion.toString());
 }
}