import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringOrBooleanUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 assertEquals(ImmutableSet.of(JSType.STRING, JSType.BOOLEAN), stringOrBooleanUnion.getAlternates());
 }

 @Test
 public void testUnionTypeMatchesNumberContext() {
 UnionType intOrBoolUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN));
 assertFalse("Boolean alternate should not match number context", intOrBoolUnion.matchesNumberContext());
 assertTrue("Int alternate should match number context", intOrBoolUnion.matchesNumberContext());
 }

 @Test
 public void testUnionTypeMatchesStringContext() {
 UnionType intOrStringUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.STRING));
 assertFalse("Int alternate should not match string context", intOrStringUnion.matchesStringContext());
 assertTrue("String alternate should match string context", intOrStringUnion.matchesStringContext());
 }

 @Test
 public void testUnionTypeMatchesObjectContext() {
 UnionType nullOrStringUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.NULL, JSType.STRING));
 assertFalse("Null alternate should not match object context", nullOrStringUnion.matchesObjectContext());
 assertTrue("String alternate should match object context", nullOrStringUnion.matchesObjectContext());
 }

 @Test(expected = AssertionError.class)
 public void testUnionTypeFindPropertyTypeNullName() {
 UnionType intOrBoolUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN));
 intOrBoolUnion.findPropertyType(null);
 }

 @Test
 public void testUnionTypeFindPropertyTypeStringPropertyName() {
 UnionType stringOrBooleanUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 String propertyName = "someProperty";
 JSType stringPropertyType = JSType.STRING;
 JSType booleanPropertyType = JSType.BOOLEAN;
 JSType stringOrBooleanType = JSTypeRegistry.instance().findTypeByNameOrNull("StringOrBoolean");
 JSType expectedStringPropertyType = stringOrBooleanType.findPropertyType(propertyName);
 assertEquals("Unexpected String property type", stringPropertyType, expectedStringPropertyType);
 }

 @Test
 public void testUnionTypeCanAssignTo() {
 UnionType intOrStringUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.STRING));
 JSType intType = JSType.INT;
 JSType stringType = JSType.STRING;
 assertTrue("Int can assign to Int", intOrStringUnion.canAssignTo(intType));
 assertTrue("String can assign to Int", intOrStringUnion.canAssignTo(stringType));
 assertTrue("String can assign to String", intOrStringUnion.canAssignTo(stringType));
 }

 @Test
 public void testUnionTypeCanBeCalled() {
 UnionType nullOrFunctionUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.NULL, JSType.FUNCTION));
 assertFalse("Null alternate should not be callable", nullOrFunctionUnion.canBeCalled());
 }

 @Test
 public void testUnionTypeRestrictByNotNullOrUndefined() {
 UnionType nullOrStringUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.NULL, JSType.STRING));
 UnionType nonNullOrStringUnion = nullOrStringUnion.restrictByNotNullOrUndefined();
 assertFalse("Null alternate should not be in nonNullOrStringUnion", nonNullOrStringUnion.contains(JSType.NULL));
 assertTrue("String alternate should be in nonNullOrStringUnion", nonNullOrStringUnion.contains(JSType.STRING));
 }

 @Test
 public void testUnionTypeTestForEquality() {
 UnionType intOrBoolUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN));
 UnionType intOrBoolUnion2 = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN));
 assertEquals("Unexpected equality result", TernaryValue.TRUE, intOrBoolUnion.testForEquality(intOrBoolUnion2));
 UnionType intOrBoolUnion3 = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 assertEquals("Unexpected equality result", TernaryValue.FALSE, intOrBoolUnion.testForEquality(intOrBoolUnion3));
 }

 @Test
 public void testUnionTypeIsNullable() {
 UnionType intOrNullUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.NULL));
 assertTrue("INT alternate should not be nullable", !intOrNullUnion.isNullable());
 UnionType nullOrNullUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.NULL, JSType.NULL));
 assertTrue("NULL alternate should be nullable", intOrNullUnion.isNullable());
 }

 @Test(expected = AssertionError.class)
 public void testUnionTypeGetLeastSupertypeNullType() {
 UnionType intOrBoolUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN));
 JSType nullType = JSTypeRegistry.instance().findTypeByNameOrNull("Null");
 assertEquals(intOrBoolUnion.getLeastSupertype(nullType), intOrBoolUnion);
 }

 @Test
 public void testUnionTypeMeet() {
 UnionType intOrBoolUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.BOOLEAN));
 UnionType intOrStringUnion = new UnionType(JSTypeRegistry.instance(), ImmutableSet.of(JSType.INT, JSType.STRING));
 UnionType intOrStringOrBoolUnion = intOrBoolUnion.meet(intOrStringUnion);
 assertEquals(intOrStringOrBoolUnion.getAlternates(), ImmutableSet.of(JSType.INT, JSType.STRING, JSType.BOOLEAN));
 }
}