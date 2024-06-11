import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testUnionType() {
 UnionType intOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType strOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.STRING, JSTypeNative.BOOLEAN));

 // Test matchesNumberContext
 assertTrue(intOrBool.matchesNumberContext());
 assertFalse(strOrBool.matchesNumberContext());

 // Test matchesStringContext
 assertTrue(strOrBool.matchesStringContext());
 assertFalse(intOrBool.matchesStringContext());

 // Test matchesObjectContext
 assertTrue(intOrBool.matchesObjectContext());
 assertTrue(strOrBool.matchesObjectContext());

 // Test findPropertyType
 UnionType intOrBoolOrString = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN, JSTypeNative.STRING));
 JSType result = intOrBoolOrString.findPropertyType("length");
 assertEquals(JSTypeNative.UNKNOWN, result);

 // Test canAssignTo
 UnionType intOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType stringType = TypeRegistry.getInstance().getStringType();
 assertTrue(intOrBool.canAssignTo(stringType));
 assertFalse(stringType.canAssignTo(intOrBool));

 // Test canBeCalled
 UnionType intOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 assertTrue(intOrBool.canBeCalled());

 // Test restrictByNotNullOrUndefined
 UnionType intOrNull = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.NULL));
 UnionType intOrNullOrUndefined = intOrNull.restrictByNotNullOrUndefined();
 assertNotNull(intOrNullOrUndefined);
 assertFalse(intOrNullOrUndefined.canAssignTo(JSTypeNative.NULL));
 assertTrue(intOrNullOrUndefined.canAssignTo(JSTypeNative.INT));

 // Test testForEquality
 UnionType intOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType intOrBool2 = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType intOrString = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.STRING));
 UnionType stringOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.STRING, JSTypeNative.BOOLEAN));

 assertEquals(intOrBool, intOrBool);
 assertEquals(intOrBool, intOrBool2);
 assertEquals(intOrBool, intOrBool.testForEquality(intOrBool2));
 assertNotEquals(intOrBool, intOrString);
 assertNotEquals(intOrBool, stringOrBool);
 assertNotEquals(intOrBool, JSTypeNative.NUMBER);
 assertNotEquals(intOrBool, JSTypeNative.OBJECT);

 // Test isNullable
 UnionType intOrNull = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.NULL));
 UnionType boolOrNull = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.BOOLEAN, JSTypeNative.NULL));
 UnionType intType = TypeRegistry.getInstance().getIntType();
 assertFalse(intType.isNullable());
 assertTrue(intOrNull.isNullable());
 assertFalse(boolOrNull.isNullable());

 // Test isUnknownType
 UnionType intOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType unknownType = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.UNKNOWN));
 assertFalse(intOrBool.isUnknownType());
 assertTrue(unknownType.isUnknownType());

 // Test getLeastSupertype
 UnionType intOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType intOrBoolOrString = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN, JSTypeNative.STRING));
 UnionType intOrBoolOrStringOrObject = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN, JSTypeNative.STRING, JSTypeNative.OBJECT));
assertEquals(intOrBoolOrString, intOrBool.getLeastSupertype(intOrBoolOrString));
 assertEquals(intOrBoolOrStringOrObject, intOrBool.getLeastSupertype(intOrBoolOrStringOrObject));
 assertEquals(intOrBool, intOrBool.getLeastSupertype(intOrBool));

 // Test meet
 UnionType intOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType boolOrInt = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.BOOLEAN, JSTypeNative.INT));
 UnionType intOrBoolOrBoolOrInt = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN, JSTypeNative.BOOLEAN, JSTypeNative.INT));
 assertEquals(intOrBoolOrBoolOrInt, intOrBool.meet(boolOrInt));

 // Test equals
 UnionType intOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType intOrBool2 = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN));
 UnionType intOrString = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.STRING));
 UnionType stringOrBool = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.STRING, JSTypeNative.BOOLEAN));
 UnionType intOrNull = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.NULL));
 UnionType boolOrNull = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.BOOLEAN, JSTypeNative.NULL));
 UnionType intOrBoolOrString = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN, JSTypeNative.STRING));
 UnionType intOrBoolOrString2 = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN, JSTypeNative.STRING));
 UnionType intOrBoolOrStringOrObject = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN, JSTypeNative.STRING, JSTypeNative.OBJECT));
 UnionType intOrBoolOrStringOrObject2 = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.BOOLEAN, JSTypeNative.STRING, JSTypeNative.OBJECT));
 UnionType intOrNullOrUndefined = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.NULL, JSTypeNative.UNDEFINED));
 UnionType intOrNullOrUndefined2 = new UnionType(TypeRegistry.getInstance(), ImmutableSet.of(JSTypeNative.INT, JSTypeNative.NULL, JSTypeNative.UNDEFINED));

 assertEquals(intOrBool, intOrBool);
 assertEquals(intOrBool, intOrBool2);
 assertEquals(intOrBool, intOrBool.testForEquality(intOrBool2));
 assertNotEquals(intOrBool, intOrString);
 assertNotEquals(intOrBool, stringOrBool);
 assertNotEquals(intOrBool, JSTypeNative.NUMBER);
 assertNotEquals(intOrBool, JSTypeNative.OBJECT);
 assertEquals(intOrBool, intOrBoolOrString);
 assertEquals(intOrBoolOrString, intOrBool);
 assertEquals(intOrBool, intOrBoolOrString2);
 assertEquals(intOrBoolOrString2, intOrBool);
 assertEquals(intOrNull, intOrNull);
 assertEquals(intOrNull, intOrNullOrUndefined);
 assertEquals(intOrNullOrUndefined, intOrNull);
 assertNotEquals(intOrNull, intOrBool);
 assertNotEquals(intOrNull, intOrString);
 assertNotEquals(intOrNull, JSTypeNative.NUMBER);
 assertNotEquals(intOrNull, JSTypeNative.OBJECT);
 assertNotEquals(intOrNullOrUndefined, intOrBool);
 assertNotEquals(intOrNullOrUndefined, intOrString);
 assertNotEquals(intOrNullOrUndefined, JSTypeNative.NUMBER);
 assertNotEquals(intOrNullOrUndefined, JSTypeNative.OBJECT);
 assertNotEquals(boolOrNull, intOrBool);
 assertNotEquals(boolOrNull, intOrString);
 assertNotEquals(boolOrNull, JSTypeNative.NUMBER);
 assertNotEquals(boolOrNull, JSTypeNative.OBJECT);
 assertNotEquals(intOrBoolOrString, boolOrNull);
 assertNotEquals(intOrBoolOrString, intOrNull);
 assertNotEquals(intOrBoolOrStringOrObject, intOrNull);
 assertNotEquals(intOrBoolOrStringOrObject, JSTypeNative.NUMBER);
 assertNotEquals(intOrBoolOrStringOrObject, JSTypeNative.OBJECT);
 assertNotEquals(intOrBoolOrStringOrObject2, intOrNull);
 assertNotEquals(intOrBoolOrStringOrObject2, JSTypeNative.NUMBER);
 assertNotEquals(intOrBoolOrStringOrObject2, JSTypeNative.OBJECT);
 assertNotEquals(intOrBoolOrString, intOrBoolOrStringOrObject);
 assertNotEquals(intOrBoolOrStringOrObject, intOrBoolOrStringOrObject2);
 assertEquals(intOrBoolOrString, intOrBoolOrStringOrObject);
 assertEquals(intOrBoolOrStringOrObject, intOrBoolOrStringOrObject2);
 }
}