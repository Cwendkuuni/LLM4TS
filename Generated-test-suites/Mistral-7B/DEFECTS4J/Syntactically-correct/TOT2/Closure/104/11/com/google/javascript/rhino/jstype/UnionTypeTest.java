import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 assertEquals(ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()), stringBooleanUnion.getAlternates());
 }

 @Test
 public void testGetAlternates() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType(), JSTypeRegistry.getInstance().getBooleanType()));

 assertEquals(ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()), stringBooleanUnion.getAlternates());
 assertEquals(ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType(), JSTypeRegistry.getInstance().getBooleanType()), numberBooleanUnion.getAlternates());
 }

 @Test
 public void testMatchesNumberContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType()));

 assertTrue(stringBooleanUnion.matchesNumberContext());
 assertTrue(numberUnion.matchesNumberContext());
 }

 @Test
 public void testMatchesStringContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType()));

 assertTrue(stringBooleanUnion.matchesStringContext());
 assertTrue(stringBooleanUnion.matchesStringContext());
 }

 @Test
 public void testMatchesObjectContext() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType()));
 UnionType nullType = JSTypeRegistry.getInstance().getNullType();
 UnionType objectUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getObjectType()));

 assertTrue(stringBooleanUnion.matchesObjectContext());
 assertTrue(numberUnion.matchesObjectContext());
 assertTrue(objectUnion.matchesObjectContext());
 assertFalse(nullType.matchesObjectContext());
 }

 @Test
 public void testFindPropertyType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType()));
 StringPropertyType stringPropertyType = JSTypeRegistry.getInstance().getStringPropertyType();
 NumberPropertyType numberPropertyType = JSTypeRegistry.getInstance().getNumberPropertyType();

 assertEquals(stringPropertyType, stringBooleanUnion.findPropertyType("length"));
 assertEquals(numberPropertyType, numberUnion.findPropertyType("length"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCanAssignToNullType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 JSType nullType = JSTypeRegistry.getInstance().getNullType();
 stringBooleanUnion.canAssignTo(nullType);
 }

 @Test
 public void testCanBeCalled() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType()));
 FunctionType stringFunctionType = JSTypeRegistry.getInstance().getStringFunctionType();
 FunctionType numberFunctionType = JSTypeRegistry.getInstance().getNumberFunctionType();

 assertTrue(stringBooleanUnion.canBeCalled());
 assertTrue(numberUnion.canBeCalled());
 assertTrue(stringFunctionType.canBeCalled());
 assertTrue(numberFunctionType.canBeCalled());
 }

 @Test
 public void testRestrictByNotNullOrUndefined() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType()));

 assertEquals(stringUnion, stringBooleanUnion.restrictByNotNullOrUndefined());
 }

 @Test
 public void testTestForEquality() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType stringUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType()));
 UnionType numberUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType()));
 UnionType booleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getBooleanType()));
 String literalString = "abc";
 boolean literalBoolean = true;

 assertEquals(stringBooleanUnion, stringBooleanUnion.testForEquality(stringBooleanUnion));
 assertEquals(stringBooleanUnion, stringBooleanUnion.testForEquality(stringUnion));
 assertEquals(stringBooleanUnion, stringBooleanUnion.testForEquality(numberUnion));
 assertEquals(booleanUnion, stringBooleanUnion.testForEquality(booleanUnion));
 assertEquals(stringUnion, stringUnion.testForEquality(stringBooleanUnion));
 assertEquals(numberUnion, numberUnion.testForEquality(stringBooleanUnion));
 assertEquals(booleanUnion, numberUnion.testForEquality(booleanUnion));
 assertFalse(stringBooleanUnion.testForEquality(literalString));
 assertFalse(stringBooleanUnion.testForEquality(literalBoolean));
 assertFalse(stringBooleanUnion.testForEquality(null));
 assertFalse(numberBooleanUnion.testForEquality(literalString));
 assertFalse(numberBooleanUnion.testForEquality(literalBoolean));
 assertFalse(numberBooleanUnion.testForEquality(null));
 }

 @Test
 public void testIsNullable() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getStringType(), JSTypeRegistry.getInstance().getBooleanType()));
 UnionType numberUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSTypeRegistry.getInstance().getNumberType()));
 NullType nullType = JSTypeRegistry.getInstance().getNullType();

 assertFalse(stringBooleanUnion.isNullable());
 assertFalse(numberUnion.isNullable());
 assertTrue(nullType.isNullable());
 }
}