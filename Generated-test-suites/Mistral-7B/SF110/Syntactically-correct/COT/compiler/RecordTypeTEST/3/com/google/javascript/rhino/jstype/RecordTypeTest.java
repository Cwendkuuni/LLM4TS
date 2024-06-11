import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class RecordTypeTest {

 private static final JSTypeRegistry registry = new JSTypeRegistry();

 @Test
 public void testRecordTypeConstruction() {
 RecordType recordType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 assertEquals(registry.getInstanceType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
 }

 @Test
 public void testDeclaredRecordTypeConstruction() {
 RecordType recordType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)), true);
 assertEquals(recordType.getClass(), RecordType.class);
 }

 @Test
 public void testIsSynthetic() {
 RecordType recordType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 assertTrue(recordType.isSynthetic());
 }

 @Test
 public void testIsFrozen() {
 RecordType recordType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 assertFalse(recordType.isFrozen());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 RecordType recordType3 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", String.TYPE)));

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTICAL));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullPropertyRecordProperty() {
 RecordType recordType = new RecordType(registry, Map.ofEntries(Map.entry("a", null)));
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 JSType type = registry.getSimpleTypes().getIntType();

 assertTrue(recordType.defineProperty("a", type, false, null));
 assertFalse(recordType.defineProperty("a", type, true, null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDefinePropertyFrozen() {
 RecordType recordType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 recordType.isFrozen = true;
 recordType.defineProperty("a", Integer.TYPE, false, null);
 }

 @Test
 public void testGetGreatestSubtypeHelperWithSameType() {
 JSType typeA = registry.getSimpleTypes().getIntType();
 JSType typeB = typeA;
 JSType greatestSubtype = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)))
 .getGreatestSubtypeHelper(typeB);
 assertEquals(typeA, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperDifferentTypes() {
 JSType typeA = registry.getSimpleTypes().getIntType();
 JSType typeB = registry.getSimpleTypes().getStringType();
 JSType greatestSubtype = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)))
 .getGreatestSubtypeHelper(typeB);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperRecordType() {
 RecordType typeA = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 RecordType typeB = new RecordType(registry, Map.of("a", new RecordProperty("a", String.TYPE)));
 JSType greatestSubtype = typeA.getGreatestSubtypeHelper(typeB);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperParentRecordType() {
 RecordType parentType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 RecordType childType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", String.TYPE)));
 JSType greatestSubtype = parentType.getGreatestSubtypeHelper(childType);
 assertEquals(parentType, greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperSamePropertiesDifferentTypes() {
 RecordType parentType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 RecordType childType = new RecordType(registry, Map.of("a", new RecordProperty("a", String.TYPE)));
 JSType greatestSubtype = parentType.getGreatestSubtypeHelper(childType);
 assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
 }

 @Test
 public void testGetGreatestSubtypeHelperDifferentProperties() {
 RecordType parentType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", String.TYPE)));
 RecordType childType = new RecordType(registry, Map.of("c", new RecordProperty("c", Integer.TYPE)));
 JSType greatestSubtype = parentType.getGreatestSubtypeHelper(childType);
 assertEquals(parentType, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", String.TYPE)));
 RecordType recordType3 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)));

 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeDifferentObjectTypes() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertFalse(recordType1.isSubtype(objectType));
 }

 @Test
 public void testIsSubtypeRecordTypeWithDeclaredProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", String.TYPE)), true);

 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeWithInvariantDeclaredProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", String.TYPE)), true);

 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeWithNonInvariantDeclaredProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", String.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);

 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeWithNonInvariantDeclaredPropertyAndInheritedProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", String.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);
 RecordType recordType3 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));

 assertFalse(recordType1.isSubtype(recordType2));
 assertTrue(recordType1.isSubtype(recordType3));
 }

 @Test
 public void testIsSubtypeRecordTypeWithInvariantDeclaredPropertyAndInheritedNonInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "c", new RecordProperty("c", String.TYPE)), true);

 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeObjectTypeRecordType() {
 RecordType recordType = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(objectType.isSubtype(recordType));
 }

 @Test
 public void testIsSubtypeObjectTypeObjectType() {
 ObjectType objectType1 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 ObjectType objectType2 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

 assertTrue(objectType1.isSubtype(objectType2));
 }

 @Test
 public void testIsSubtypeRecordTypeRecordType() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE)));
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", String.TYPE)));

 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeRecordTypeWithInheritedNonInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)));
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "c", new RecordProperty("c", String.TYPE)));

 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeRecordTypeWithInheritedInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "c", new RecordProperty("c", Integer.TYPE)), true);

 assertTrue(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeRecordTypeWithDeclaredNonInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", String.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);

 assertFalse(recordType1.isSubtype(recordType2));
 }

 @Test
 public void testIsSubtypeRecordTypeRecordTypeWithDeclaredInvariantProperty() {
 RecordType recordType1 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE)), true);
 RecordType recordType2 = new RecordType(registry, Map.of("a", new RecordProperty("a", Integer.TYPE), "b", new RecordProperty("b", Integer.TYPE), "c", new RecordProperty("c", Integer.TYPE)), true);

 assertTrue(recordType1.isSubtype(recordType2));
 }
}