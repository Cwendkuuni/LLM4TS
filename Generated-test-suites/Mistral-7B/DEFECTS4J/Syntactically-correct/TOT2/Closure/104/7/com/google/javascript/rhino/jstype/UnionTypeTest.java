import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.javascript.rhino.jstype.*;

public class UnionTypeTest {

 private static UnionType stringBooleanUnion() {
 return new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(StringType.instance(), BooleanType.instance()));
 }

 @Test
 public void testStringBooleanUnionBasics() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 assertTrue(stringBooleanUnion.matchesStringContext());
 assertTrue(stringBooleanUnion.matchesObjectContext());
 assertFalse(stringBooleanUnion.matchesNumberContext());

 assertEquals(stringBooleanUnion, stringBooleanUnion.meet(stringBooleanUnion));

 assertTrue(stringBooleanUnion.isSubtype(StringType.instance()));
 assertTrue(stringBooleanUnion.isSubtype(BooleanType.instance()));

 assertFalse(BooleanType.instance().isSubtype(stringBooleanUnion));
 assertFalse(StringType.instance().isSubtype(stringBooleanUnion));
 }

 @Test
 public void testStringBooleanUnionCanAssignTo() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 assertEquals(stringBooleanUnion, stringBooleanUnion.restrictByNotNullOrUndefined());

 assertTrue(stringBooleanUnion.canAssignTo(StringType.instance()));
 assertTrue(stringBooleanUnion.canAssignTo(BooleanType.instance()));

 assertFalse(BooleanType.instance().canAssignTo(stringBooleanUnion));
 assertFalse(StringType.instance().canAssignTo(stringBooleanUnion));
 }

 @Test(expected = AssertionError.class)
 public void testStringBooleanUnionCannotAssignToDifferentUnionType() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();
 UnionType stringNumberUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(StringType.instance(), NumberType.instance()));

 assertFalse(stringBooleanUnion.canAssignTo(stringNumberUnion));
 }

 @Test
 public void testStringBooleanUnionGetAlternates() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 Set<JSType> alternates = stringBooleanUnion.getAlternates();
 assertEquals(2, alternates.size());
 assertTrue(alternates.contains(StringType.instance()));
 assertTrue(alternates.contains(BooleanType.instance()));
 }

 @Test
 public void testStringBooleanUnionGetLeastSupertype() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();
 UnionType objectUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(ObjectType.instance()));

 assertEquals(objectUnion, stringBooleanUnion.getLeastSupertype(objectUnion));
 }

 @Test(expected = IllegalStateException.class)
 public void testStringBooleanUnionGetLeastSupertypesWithDifferentUnionTypes() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();
 NumberType numberType = NumberType.instance();

 assertNull(stringBooleanUnion.getLeastSupertype(numberType));
 }

 @Test
 public void testStringBooleanUnionFindPropertyType() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 assertEquals(StringType.instance(), stringBooleanUnion.findPropertyType("length"));
 assertNull(stringBooleanUnion.findPropertyType("toString"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringBooleanUnionFindPropertyTypeWithUnknownProperty() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 stringBooleanUnion.findPropertyType("foo");
 }

 @Test
 public void testStringBooleanUnionIsUnionType() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 assertTrue(stringBooleanUnion.isUnionType());
 }

 @Test
 public void testStringBooleanUnionIsObject() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 assertTrue(stringBooleanUnion.isObject());
 }

 @Test
 public void testStringBooleanUnionIsSubtype() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();
 UnionType stringType = StringType.instance();
 UnionType booleanType = BooleanType.instance();

 assertTrue(stringBooleanUnion.isSubtype(stringType));
 assertTrue(stringBooleanUnion.isSubtype(booleanType));
 }

 @Test
 public void testStringBooleanUnionToString() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 assertEquals("(String, Boolean)", stringBooleanUnion.toString());
 }

 @Test
 public void testStringBooleanUnionEquals() {
 UnionType stringBooleanUnion1 = this.stringBooleanUnion();
 UnionType stringBooleanUnion2 = this.stringBooleanUnion();

 assertEquals(stringBooleanUnion1, stringBooleanUnion2);
 }

 @Test
 public void testStringBooleanUnionHashCode() {
 UnionType stringBooleanUnion1 = this.stringBooleanUnion();
 UnionType stringBooleanUnion2 = this.stringBooleanUnion();

 assertEquals(stringBooleanUnion1.hashCode(), stringBooleanUnion2.hashCode());
 }

 @Test
 public void testStringBooleanUnionCanBeCalled() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 assertTrue(stringBooleanUnion.canBeCalled());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testStringBooleanUnionGetRestrictedTypeGivenToBooleanOutcome() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 stringBooleanUnion.getRestrictedTypeGivenToBooleanOutcome(true);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testStringBooleanUnionGetRestrictedTypeGivenToBooleanOutcomeFalse() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 stringBooleanUnion.getRestrictedTypeGivenToBooleanOutcome(false);
 }

 @Test
 public void testStringBooleanUnionGetPossibleToBooleanOutcomes() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();

 BooleanLiteralSet booleanLiterals = stringBooleanUnion.getPossibleToBooleanOutcomes();
 assertEquals(BooleanLiteralSet.BOTH, booleanLiterals);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStringBooleanUnionGetTypesUnderEqualityWithDifferentTypes() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();
 NumberType numberType = NumberType.instance();

 assertNull(stringBooleanUnion.getTypesUnderEquality(numberType));
 }

 @Test
 public void testStringBooleanUnionGetTypesUnderInequality() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();
 UnionType stringType = StringType.instance();
 UnionType booleanType = BooleanType.instance();

 TypePair result = stringBooleanUnion.getTypesUnderInequality(booleanType);
 assertNull(result.typeA);
 assertEquals(booleanType, result.typeB);
 }

 @Test
 public void testStringBooleanUnionGetTypesUnderShallowInequality() {
 UnionType stringBooleanUnion = this.stringBooleanUnion();
 UnionType stringType = StringType.instance();
 UnionType booleanType = BooleanType.instance();

 TypePair result = stringBooleanUnion.getTypesUnderShallowInequality(booleanType);
 assertNull(result.typeA);
 assertEquals(booleanType, result.typeB);
 }
}