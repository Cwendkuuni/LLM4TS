import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.UnionType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionTypeTest {

 @Test
 public void testUnionTypeCreation() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 assertEquals(ImmutableSet.of(JSType.STRING, JSType.BOOLEAN), stringBooleanUnion.getAlternates());
 }

 @Test
 public void testFindPropertyType() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 JSType stringPropertyType = JSType.STRING;
 JSType booleanPropertyType = JSType.BOOLEAN;
 JSType propertyType = stringBooleanUnion.findPropertyType("length");
 assertNull(propertyType);
 propertyType = stringBooleanUnion.findPropertyType("toString");
 assertEquals(stringPropertyType, propertyType);
 propertyType = stringBooleanUnion.findPropertyType("valueOf");
 assertEquals(booleanPropertyType, propertyType);
 }

 @Test
 public void testCanAssignTo() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 JSType stringType = JSType.STRING;
 JSType booleanType = JSType.BOOLEAN;
 JSType numberType = JSType.NUMBER;
 JSType nullType = JSType.NULL;
 JSType undefinedType = JSType.UNDEFINED;
 JSType objectType = JSType.OBJECT;
 JSType voidType = JSType.VOID;

 assertTrue(stringBooleanUnion.canAssignTo(stringType));
 assertTrue(stringBooleanUnion.canAssignTo(booleanType));
 assertFalse(stringBooleanUnion.canAssignTo(numberType));
 assertFalse(stringBooleanUnion.canAssignTo(nullType));
 assertFalse(stringBooleanUnion.canAssignTo(undefinedType));
 assertFalse(stringBooleanUnion.canAssignTo(objectType));
 assertFalse(stringBooleanUnion.canAssignTo(voidType));
 }

 @Test
 public void testRestrictedUnion() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 UnionType stringUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING));
 UnionType booleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.BOOLEAN));
 UnionType restrictedStringBoolean = stringBooleanUnion.getRestrictedUnion(stringUnion);
 UnionType restrictedBoolean = booleanUnion.getRestrictedUnion(stringBooleanUnion);
 assertEquals(ImmutableSet.of(JSType.BOOLEAN), restrictedStringBoolean.getAlternates());
 assertEquals(ImmutableSet.of(JSType.STRING), restrictedBoolean.getAlternates());
 assertNotSame(stringBooleanUnion, restrictedStringBoolean);
 assertNotSame(booleanUnion, restrictedBoolean);
 }

 @Test(expected = AssertionError.class)
 public void testEquals() {
 UnionType stringBooleanUnion1 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 UnionType stringBooleanUnion2 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 assertEquals(stringBooleanUnion1, stringBooleanUnion2);
 UnionType stringBooleanUnion3 = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.NUMBER));
 assertNotEquals(stringBooleanUnion1, stringBooleanUnion3);
 }

 @Test
 public void testToString() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 assertEquals("(String|Boolean)", stringBooleanUnion.toString());
 }

 @Test
 public void testIsSubtype() {
 UnionType stringBooleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING, JSType.BOOLEAN));
 UnionType stringUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.STRING));
 UnionType booleanUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.BOOLEAN));
 UnionType numberUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.NUMBER));
 UnionType objectUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.OBJECT));
 UnionType voidUnion = new UnionType(JSTypeRegistry.getInstance(), ImmutableSet.of(JSType.VOID));

 assertTrue(stringUnion.isSubtype(stringBooleanUnion));
 assertFalse(booleanUnion.isSubtype(stringBooleanUnion));
 assertFalse(numberUnion.isSubtype(stringBooleanUnion));
 assertFalse(objectUnion.isSubtype(stringBooleanUnion));
 assertFalse(voidUnion.isSubtype(stringBooleanUnion));
 assertTrue(stringBooleanUnion.isSubtype(stringUnion));
 assertFalse(stringBooleanUnion.isSubtype(booleanUnion));
 assertFalse(stringBooleanUnion.isSubtype(numberUnion));
 assertFalse(stringBooleanUnion.isSubtype(objectUnion));
 assertFalse(stringBooleanUnion.isSubtype(voidUnion));
 }
}