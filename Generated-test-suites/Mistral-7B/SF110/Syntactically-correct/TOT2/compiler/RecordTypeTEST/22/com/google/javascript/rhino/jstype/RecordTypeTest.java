import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.*;

public class RecordTypeTest {

 @Test
 public void testRecordTypeCreation() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, properties, true);
 assertEquals(recordType.getClass(), RecordType.class);
 }

 @Test
 public void testRecordTypeCreationWithFalseDeclared() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put("name", new RecordProperty("name", String.class));
 properties.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType = new RecordType(registry, properties, false);
 assertEquals(recordType.getClass(), RecordType.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 RecordType registry = new JSTypeRegistry();
 Map<String, RecordProperty> properties = new java.util.HashMap<>();
 properties.put(null, new RecordProperty("name", String.class));
 try {
 RecordType recordType = new RecordType(registry, properties, true);
 } catch (IllegalStateException e) {
 assertEquals(e.getMessage(), "RecordProperty associated with a property should not be null!");
 }
 }

 @Test
 public void testIsFrozen() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), null, true);
 recordType.setFrozen(true);
 assertFalse(recordType.isFrozen());
 recordType.setFrozen(false);
 assertFalse(recordType.isFrozen());
 recordType.setFrozen(true);
 assertTrue(recordType.isFrozen());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetFrozenAfterConstruction() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), null, true);
 recordType.setFrozen(false);
 try {
 recordType.setFrozen(true);
 } catch (IllegalStateException e) {
 assertEquals(e.getMessage(), "Cannot modify the frozenness of a RecordType instance after construction.");
 }
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordTypeRegistry registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties1 = new java.util.HashMap<>();
 properties1.put("name", new RecordProperty("name", String.class));
 properties1.put("age", new RecordProperty("age", Integer.class));
 RecordType recordType1 = new RecordType(registry, properties1, true);
 RecordType recordType2 = new RecordType(registry, properties1, true);
 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.DEFENSIVE));

 Map<String, RecordProperty> properties2 = new java.util.HashMap<>();
 properties2.put("name", new RecordProperty("name", String.class));
 properties2.put("age", new RecordProperty("age", Integer.class));
 properties2.put("city", new RecordProperty("city", String.class));
 RecordType recordType3 = new RecordType(registry, properties2, true);
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.DEFENSIVE));
 }

 @Test
 public void testDefineProperty() {
 RecordType registry = new JSTypeRegistry();
 RecordType recordType = new RecordType(registry, null, true);

 assertTrue(recordType.defineProperty("name", String.class, false, null));
 assertTrue(recordType.hasProperty("name"));
 assertEquals(recordType.getPropertyType("name"), String.class);

 assertFalse(recordType.defineProperty("name", String.class, false, null));
 assertFalse(recordType.hasProperty("name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefinePropertyWhenFrozen() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), null, true);
 recordType.setFrozen(true);
 try {
 recordType.defineProperty("name", String.class, false, null);
 } catch (IllegalStateException e) {
 assertEquals(e.getMessage(), "Cannot modify the properties of a frozen RecordType instance.");
 }
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), null, true);
 ObjectType implicitPrototype = recordType.getImplicitPrototype();
 assertEquals(implicitPrototype.getClass(), ObjectType.class);
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 RecordTypeRegistry registry = new JSTypeRegistry();
 ObjectType obj = registry.getNativeObjectType(ObjectTypeNative.OBJECT_TYPE);
 RecordType recordType1 = new RecordType(registry, null, true);
 RecordType recordType2 = new RecordType(registry, null, true);
 RecordType recordType3 = new RecordType(registry, null, true);
 recordType3.defineProperty("city", String.class, false, null);

 assertEquals(recordType1.getGreatestSubtypeHelper(obj), obj);
 assertEquals(recordType1.getGreatestSubtypeHelper(recordType1), recordType1);
 assertEquals(recordType1.getGreatestSubtypeHelper(recordType2), recordType1);
 assertEquals(recordType1.getGreatestSubtypeHelper(recordType3), recordType3);
 assertEquals(recordType2.getGreatestSubtypeHelper(recordType1), recordType1);
 assertEquals(recordType2.getGreatestSubtypeHelper(recordType2), recordType2);
 assertEquals(recordType2.getGreatestSubtypeHelper(recordType3), recordType3);
 assertEquals(recordType3.getGreatestSubtypeHelper(recordType1), recordType3);
 assertEquals(recordType3.getGreatestSubtypeHelper(recordType2), recordType3);
 assertEquals(recordType3.getGreatestSubtypeHelper(recordType3), recordType3);
 }

 @Test
 public void testToMaybeRecordType() {
 RecordType recordType = new RecordType(new JSTypeRegistry(), null, true);
 assertEquals(recordType.toMaybeRecordType(), recordType);
 }

 @Test
 public void testIsSubtype() {
 RecordTypeRegistry registry = new JSTypeRegistry();
 ObjectType obj = registry.getNativeObjectType(ObjectTypeNative.OBJECT_TYPE);
 RecordType recordType1 = new RecordType(registry, null, true);
 RecordType recordType2 = new RecordType(registry, null, true);
 RecordType recordType3 = new RecordType(registry, null, true);
 recordType3.defineProperty("city", String.class, false, null);

 assertTrue(recordType1.isSubtype(obj));
 assertTrue(recordType1.isSubtype(recordType1));
 assertTrue(recordType1.isSubtype(recordType2));
 assertTrue(recordType1.isSubtype(recordType3));
 assertTrue(recordType2.isSubtype(obj));
 assertTrue(recordType2.isSubtype(recordType1));
 assertTrue(recordType2.isSubtype(recordType2));
 assertTrue(recordType2.isSubtype(recordType3));
 assertTrue(recordType3.isSubtype(obj));
 assertTrue(recordType3.isSubtype(recordType1));
 assertTrue(recordType3.isSubtype(recordType2));
 assertTrue(recordType3.isSubtype(recordType3));

 assertFalse(obj.isSubtype(recordType1));
 assertFalse(obj.isSubtype(recordType2));
 assertFalse(obj.isSubtype(recordType3));
 assertFalse(recordType1.isSubtype(obj));
 assertFalse(recordType2.isSubtype(obj));
 assertFalse(recordType3.isSubtype(obj));
 }
}