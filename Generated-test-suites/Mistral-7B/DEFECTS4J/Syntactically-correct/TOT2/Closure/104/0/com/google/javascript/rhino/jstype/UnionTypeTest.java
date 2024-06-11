import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testUnionTypeBasicFunctionality() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertEquals("(String, Number)", unionType.toString());
 }

 @Test
 public void testGetAlternatesFunctionality() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 Set<JSType> alternates = unionType.getAlternates();
 assertEquals(2, alternates.size());
 assertTrue(alternates.contains(new StringType(JSTypeRegistry.getInstance())));
 assertTrue(alternates.contains(new NumberType(JSTypeRegistry.getInstance())));
 }

 @Test
 public void testMatchesNumberContextEdgeCase() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new BooleanType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertFalse(unionType.matchesNumberContext());
 }

 @Test(expected = AssertionError.class)
 public void testMatchesNumberContextException() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new NullType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 unionType.matchesNumberContext();
 }

 @Test
 public void testMatchesStringContextEdgeCase() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertTrue(unionType.matchesStringContext());
 }

 @Test
 public void testMatchesObjectContextEdgeCase() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertTrue(unionType.matchesObjectContext());
 }

 @Test
 public void testFindPropertyTypeEdgeCase() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 JSType stringPropertyType = unionType.findPropertyType("length");
 assertTrue(stringPropertyType instanceof NumberType);
 }

 @Test
 public void testCanAssignToEdgeCase() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 JSType stringType = new StringType(JSTypeRegistry.getInstance());
 JSType numberType = new NumberType(JSTypeRegistry.getInstance());
 assertTrue(unionType.canAssignTo(stringType));
 assertTrue(unionType.canAssignTo(numberType));
 }

 @Test
 public void testCanBeCalledEdgeCase() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 assertFalse(unionType.canBeCalled());
 }

 @Test
 public void testRestrictByNotNullOrUndefinedEdgeCase() {
 UnionType unionType = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NullType(JSTypeRegistry.getInstance())
 ));
 UnionType restrictedUnionType = unionType.restrictByNotNullOrUndefined();
 assertNotNull(restrictedUnionType);
 assertEquals("String", restrictedUnionType.toString());
 }

 @Test
 public void testTestForEqualityEdgeCase() {
 UnionType unionTypeA = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new NumberType(JSTypeRegistry.getInstance())
 ));
 UnionType unionTypeB = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new NumberType(JSTypeRegistry.getInstance()),
 new StringType(JSTypeRegistry.getInstance())
 ));
 UnionType unionTypeC = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(
 new StringType(JSTypeRegistry.getInstance()),
 new StringType(JSTypeRegistry.getInstance())
 ));
 TernaryValue result = unionTypeA.testForEquality(unionTypeB);
 assertEquals(UNKNOWN, result);
 result = unionTypeA.testForEquality(unionTypeC);
 assertEquals(TRUE, result);
 }
}