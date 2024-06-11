import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertEquals("(String, Number)", unionType.toString());
 }

 @Test
 public void testUnionTypeGetAlternates() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 Iterable<JSType> alternates = unionType.getAlternates();
 assertEquals(2, alternates.size());
 assertTrue(alternates.contains(new StringType(JSTypeRegistry.getInstance())));
 assertTrue(alternates.contains(new NumberType(JSTypeRegistry.getInstance())));
 }

 @Test
 public void testUnionTypeMatchesNumberContext() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertTrue(unionType.matchesNumberContext());
 }

 @Test
 public void testUnionTypeMatchesStringContext() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertTrue(unionType.matchesStringContext());
 }

 @Test
 public void testUnionTypeMatchesObjectContext() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertTrue(unionType.matchesObjectContext());
 }

 @Test
 public void testUnionTypeFindPropertyType() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 String propertyName = "length";
 JSType propertyType = unionType.findPropertyType(propertyName);
 assertTrue(propertyType instanceof NumberType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnionTypeCanAssignToNull() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 JSType nullType = JSTypeRegistry.getInstance().getNullType();
 assertFalse(unionType.canAssignTo(nullType));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnionTypeCanAssignToVoid() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 JSType voidType = JSTypeRegistry.getInstance().getVoidType();
 assertFalse(unionType.canAssignTo(voidType));
 }

 @Test
 public void testUnionTypeRestrictByNotNullOrUndefined() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 UnionType restricted = unionType.restrictByNotNullOrUndefined();
 assertTrue(restricted.isSubtype(JSTypeRegistry.getInstance().getNullType()));
 assertTrue(restricted.isSubtype(JSTypeRegistry.getInstance().getObjectType()));
 }

 @Test
 public void testUnionTypeTestForEquality() {
 UnionType unionType1 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 UnionType unionType2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new NumberType(JSTypeRegistry.getInstance()),
 new StringType(JSTypeRegistry.getInstance())
 ));
 TernaryValue result = unionType1.testForEquality(unionType2);
 assertEquals(TernaryValue.FALSE, result);
 }

 @Test
 public void testUnionTypeIsNullable() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertFalse(unionType.isNullable());
 }

 @Test
 public void testUnionTypeIsUnknownType() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertFalse(unionType.isUnknownType());
 }

 @Test
 public void testUnionTypeGetLeastSupertype() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 JSType supertype = JSTypeRegistry.getInstance().getObjectType();
 JSType leastSupertype = unionType.getLeastSupertype(supertype);
 assertTrue(leastSupertype instanceof UnionType);
 }

 @Test
 public void testUnionTypeMeet() {
 UnionType unionType1 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 UnionType unionType2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new NumberType(JSTypeRegistry.getInstance()),
 new StringType(JSTypeRegistry.getInstance())
 ));
 UnionType meetType = (UnionType) unionType1.meet(unionType2);
 assertEquals("(Number, String)", meetType.toString());
 }

 @Test
 public void testUnionTypeEquals() {
 UnionType unionType1 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 UnionType unionType2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertTrue(unionType1.equals(unionType2));
 }

 @Test
 public void testUnionTypeHashCode() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 int hashCode1 = unionType.hashCode();
 UnionType unionType2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new NumberType(JSTypeRegistry.getInstance()),
 new StringType(JSTypeRegistry.getInstance())
 ));
 int hashCode2 = unionType2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testUnionTypeIsUnionType() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertTrue(unionType.isUnionType());
 }

 @Test
 public void testUnionTypeIsObject() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertTrue(unionType.isObject());
 }

 @Test
 public void testUnionTypeContains() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 JSType stringType = new StringType(JSTypeRegistry.getInstance());
 JSType numberType = new NumberType(JSTypeRegistry.getInstance());
 assertTrue(unionType.contains(stringType));
 assertTrue(unionType.contains(numberType));
 }

 @Test
 public void testUnionTypeGetRestrictedUnion() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 JSType restrictedType = unionType.getRestrictedUnion(new NumberType(JSTypeRegistry.getInstance()));
 assertEquals("String", restrictedType.toString());
 }
}